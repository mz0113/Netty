package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 好像突然明白了，NIO的真谛。
 * 以前的socket编程，一个线程对应一个客户端，客户端每隔3秒发一次消息给服务端。那么这个线程会一直保持长连接，并且每隔三秒阻塞在那等着客户端发消息过来。线程无法执行结束，线程池容量也有上限。
 * 现在的NIO，不是把IO操作异步化了。而是说，一个客户端chanel，假如注册了读事件，那么当这个chanel，客户端给这个chanel发消息了。那么selector会触发读事件，然后会分配线程处理这个事件
 *     那么显然易见，这个分配的线程在本次读操作结束后，立马就结束了。而不会死命的等待下一次的消息发过来，也不会占用线程池资源。
 *     但是socket的一个线程进行readLine操作，会死命等待数据过来。但是NIO不会，只有发生读事件，才会分配线程处理，没用读事件，压根没业务线程在运行。处理完，立刻结束。
 */

public class ServerBootStrap {
    private static int port = 9876;
    private Selector selector = null;
    public static void main(String[] args) throws IOException {
        ServerBootStrap serverBootStrap = new ServerBootStrap();
        serverBootStrap.initServer();//配置注册
        serverBootStrap.accept();//开启监听
    }

    private void initServer() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);//不阻塞
        this.selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//给serverSocketChannel注册一个Accept事件监听
    }

    private void accept() throws IOException {
        while (true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                handle(iterator.next());
                iterator.remove();
            }
        }
    }

    private void handle(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            handleAccept(key);//处理连接事件
        }else if (key.isReadable()) {
            handleProcess(key);//处理业务读事件
        }else if(key.isWritable()){
            handleWrite(key);
        }
    }

    private void handleWrite(SelectionKey key) {
        System.out.println("服务器正在Write");
    }

    private void handleProcess(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int i = channel.read(buffer);
        if (i<0){
            return;
        }
        byte[] bytes = buffer.array();
        String msg = new String(bytes);
        System.out.println("服务器收到:"+msg);
        channel.write(ByteBuffer.wrap("123".getBytes()));
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel)key.channel();//这里的channel其实都是单例的，就是initServer时候配置进去的哪一个实例
        SocketChannel socketChannel = channel.accept();//一个ServerSocketChannel可以accept多个长连接对象
        //得到的这个chanel是客户端的chanel,服务器和客户端可以用这个chanel来进行交互，也可以把这个chanel绑定到selector上面接受selector的管理。
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_WRITE);//write好像是说，是否可以写到缓冲区中。可以一直产生可写的事件，导致select无限循环
        socketChannel.register(selector,SelectionKey.OP_READ);//每个长连接都注册read监听到selector
    }
}