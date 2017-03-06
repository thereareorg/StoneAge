package team.gl.nio.svr;
import team.gl.nio.cmn.BagDecoder;
import team.gl.nio.cmn.BagEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;


public class NettyServer {
    /**
     * ���ڷ��䴦��ҵ���̵߳��߳������ 
     */
    protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors()*2; //Ĭ��  

    /**
     *ҵ������̴߳�С
     */  
    protected static final int BIZTHREADSIZE = 4;

    /** 
     * NioEventLoopGroupʵ���Ͼ��Ǹ��̳߳�, 
     * NioEventLoopGroup�ں�̨������n��NioEventLoop������Channel�¼�, 
     * ÿһ��NioEventLoop������m��Channel, 
     * NioEventLoopGroup��NioEventLoop�����ﰤ��ȡ��NioEventLoop������Channel 
     */  
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);  
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE); 

    public void run() throws Exception { 

        ServerBootstrap b = new ServerBootstrap();  
        b.group(bossGroup, workerGroup);  
        b.channel(NioServerSocketChannel.class);  
        b.childHandler(new ChannelInitializer<SocketChannel>() {  
            @Override  
            public void initChannel(SocketChannel ch) throws Exception {  
                ChannelPipeline pipeline = ch.pipeline();  
                System.out.println("connect" + ch);
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));  
                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));  
                pipeline.addLast("decoder", new BagDecoder());  
                pipeline.addLast("encoder", new BagEncoder());  
                pipeline.addLast(new NettyServerHandler());  
            }  
        });  

        ChannelFuture f = b.bind("0.0.0.0",9873).sync();  


        System.out.println("netty server start success...");

        /**
         * wait until the socket close
         */
        f.channel().closeFuture().sync();

        shutdown();

    }

    protected static void shutdown() {  
        workerGroup.shutdownGracefully();  
        bossGroup.shutdownGracefully();
    }

/*    public static void main(String[] args) throws Exception {
           new NettyServer().run();
    }   */ 
}
