package team.gl.nio.svr;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.util.Vector;

import HG.GrabHGEventsThread;
import HG.HGhttp;
import ISN.GrabISNEventsThread;
import MergeNew.MergeNewManager;
import P8.GrabEventsThread;
import P8.MergeManager;
import P8.P8Http;
import P8.ZhiboManager;
import team.gl.nio.cln.ZhiboClientHandler;
import team.gl.nio.cmn.Bag;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //System.out.println("server channelRead..");
        Bag us = (Bag) msg;
        if(us.getReq().equals("request")) {
        	//isn begin
        	String isnStr = GrabISNEventsThread.getEventsStr();
        	String isnSuccessTime = GrabISNEventsThread.getSuccessTime();
        	
        	
	        Vector<String[]> datas = P8Http.getFinalEventsDetails();
	        Vector<String[]> mergeDatas = MergeManager.getFinalEventsDetails();
	        Vector<String[]> newMergeDatas = MergeNewManager.getFinalEventsDetails();
	        
	        //去掉历史数据
	        Vector<String[]> mergepSubDatas = MergeManager.getpSubMergeevents();
//	        Vector<String[]> newMergepSubDatas = MergeNewManager.getpSubMergeevents();
	        Vector<String[]> P8pSubDatas = P8Http.getpSubevents();
	        Vector<String[]> ZhibopSubDatas = ZhiboManager.getpSubevents();
//	        
//	        Vector<String[]> hgpSubDatas = HGhttp.getpSubevents();
	        
	        String successTime = P8Http.getSuccessTime();
	        
	        
	        Vector<String[]> hgdatas = HGhttp.getFinalEventsDetails();
	        String hgsuccessTime = HGhttp.getSuccessTime();
	        
/*	        String [] strs1 = {"你好", "111","111","111","111","111","111", "111"};
	    	String [] strs2 = {"222", "222","222","222","222","222","222", "222"};
	    	
	    	datas.add(strs2);
	    	datas.add(strs1);*/
	    	Bag bag = new Bag("response");
	    	bag.setDatas(datas);	    	
	    	bag.setMergeDatas(mergeDatas);
	    	
	    	
	    	bag.setMergepSubDatas(mergepSubDatas);
	    	bag.setP8pSubDatas(P8pSubDatas);
	    	bag.setZhibopSubDatas(ZhibopSubDatas);
//	    	bag.sethgpSubDatas(hgpSubDatas);
//	    	bag.setNewMergepSubDatas(newMergepSubDatas);
	    	
	    	
	    	bag.setSuccessTime(successTime);
	    	bag.setP8GrabStat(GrabEventsThread.grabStat);
	    	bag.setMergeGrabStat(GrabEventsThread.grabStat&&GrabISNEventsThread.grabStat);

	    	bag.sethgDatas(hgdatas);
	    	bag.sethgSuccessTime(hgsuccessTime);
	    	bag.sethgGrabStat(GrabHGEventsThread.grabStat);
	    	
	    	bag.setISNDatas(isnStr);
	    	bag.setISNSuccessTime(isnSuccessTime);
	    	bag.setISNGrabStat(GrabISNEventsThread.grabStat);
	    	
	    	bag.setNewMergeDatas(newMergeDatas);
	    	
	    	
	    	ctx.channel().writeAndFlush(bag);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("server channelReadComplete..");
        ctx.flush();//ˢ�º�Ž���ݷ�����SocketChannel
    }
    

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        //System.out.println("server exceptionCaught.." + cause.getMessage());
        ctx.close();
    }
    
    
    
    
    // 心跳丢失计数器
    private int counter;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("--- Client is active ---");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
       // System.out.println("--- Client is inactive ---");
    }



    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 空闲6s之后触发 (心跳包丢失)
            if (counter >= 3) {
                // 连续丢失3个心跳包 (断开连接)
                ctx.channel().close().sync();
                System.out.println("已与Client断开连接");
            } else {
                counter++;
                System.out.println("丢失了第 " + counter + " 个心跳包");
            }
        }
    }





}
