package team.gl.nio.cln;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import team.gl.nio.cmn.P8Bag;
import team.gl.nio.cmn.P8BagDecoder;
import team.gl.nio.cmn.P8BagEncoder;

public class NettyClient {    
    public static String HOST = "127.0.0.1";
    public static int PORT = 9873;  

    public static Bootstrap bootstrap = getBootstrap();  
    public static Channel channel = getChannel(HOST,PORT);  


    /** 
     * ��ʼ��Bootstrap 
     * @return 
     */  
    public static final Bootstrap getBootstrap(){ 
        EventLoopGroup group = new NioEventLoopGroup();  
        Bootstrap b = new Bootstrap();  
        b.group(group).channel(NioSocketChannel.class);  
        b.handler(new ChannelInitializer<Channel>() {  
            @Override  
            protected void initChannel(Channel ch) throws Exception {  
                ChannelPipeline pipeline = ch.pipeline();  
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));  
                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));  
                pipeline.addLast("decoder", new P8BagDecoder());  
                pipeline.addLast("encoder", new P8BagEncoder());  
                pipeline.addLast("handler", new ClientHandler());  
            }  
        });  
        b.option(ChannelOption.SO_KEEPALIVE, true);  
        return b;  
    }  

    public static final Channel getChannel(String host,int port){  
        Channel channel = null;  
        try {  
            channel = bootstrap.connect(host, port).sync().channel();  
            System.out.println("connected");
        } catch (Exception e) {   
            return null;  
        }  
        return channel;  
    }  

    public static void sendRequest(P8Bag req) throws Exception {  
        System.out.println(channel);
        if(channel!=null){  
            channel.writeAndFlush(req).sync();
        }else{  
            System.out.println("��Ϣ����ʧ��,������δ����!");  
        }  
    }  

/*    public static void main(String[] args) throws Exception {  
        try {      	
            NettyClient.sendRequest(new Bag("request")); 
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  */
} 
