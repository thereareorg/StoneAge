package team.gl.nio.cln;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import team.gl.nio.cmn.Bag;

public class ClientHandler extends ChannelInboundHandlerAdapter {  

    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg)  
            throws Exception {     
    	Bag us = (Bag)msg;
       System.out.println(us);         
    }  

}  
