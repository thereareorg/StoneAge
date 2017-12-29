package team.gl.nio.cln;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class ZhiboClient {
    public static String HOST = "103.26.127.50";
    public static int PORT = 25836;  

    public static Bootstrap bootstrap = null; 
    public static EventLoopGroup group = null;
    //public static Channel channel = null;


    /** 
     * 初始化Bootstrap 
     * @return 
     */  
    public static final Bootstrap getBootstrap(){
    	if(group == null) {
    		group = new NioEventLoopGroup();
    	}
        Bootstrap b = new Bootstrap();  
        b.group(group).channel(NioSocketChannel.class);  
        b.handler(new ChannelInitializer<Channel>() {  
            @Override  
            protected void initChannel(Channel ch) throws Exception {  
                ChannelPipeline pipeline = ch.pipeline();  
                //pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));  
                //pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));  
                pipeline.addLast("decoder", new StringDecoder());  
                pipeline.addLast("decoder1", new FootDecoder());
                //pipeline.addLast("encoder", new BagEncoder());  
                pipeline.addLast("handler", new ZhiboClientHandler());  
            }  
        });  
        b.option(ChannelOption.SO_KEEPALIVE, true);  
        return b;  
    }  

    
    public static boolean connect(){
    	bootstrap = getBootstrap();
    	try {  
            bootstrap.connect(HOST, PORT).sync();
            System.out.println("connected");
        } catch (Exception e) {  
        	System.out.println("连接失败！！！！！！" );
        	e.printStackTrace();
            return false; 
        }  
   
    	return true;
    }
    
	 public static void reConnect(){
		
	  }
    
    public static final Channel getChannel(String host,int port){  
        Channel channel = null;  
        try {  
            channel = bootstrap.connect(host, port).sync().channel();
            System.out.println("connected!!!");
        } catch (Exception e) {   
            return null;  
        }  
        return channel;  
    }  
}
