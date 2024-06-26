package team.gl.nio.cln;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import team.gl.nio.cmn.P8Bag;

public class ClientHandler extends ChannelInboundHandlerAdapter {  

    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg)  
            throws Exception {     
    	P8Bag us = (P8Bag)msg;
       System.out.println(us);         
    }  

}  
