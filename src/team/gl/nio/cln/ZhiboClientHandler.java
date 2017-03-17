package team.gl.nio.cln;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import team.gl.nio.cmn.Bag;
import P8.MergeManager;
import P8.P8Http;
import P8.StoneAge;
import P8.ZhiboManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ZhiboClientHandler extends ChannelInboundHandlerAdapter {  
	

	public static String lastRes = "";
	
	public static String res = "";
	
	public static boolean firstTime = true;
	
	public static long time = 0L;
	

    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg)  
            throws Exception {
       
       res = (String)msg;
       

           
       
       //ZhiboManager.setStateText("		连接成功");
       
       
       if(firstTime == true){
    	   if(!res.contains("[[") || !res.contains("]]")){
        	   return;
           }
           
           if(!lastRes.contains(res)){
               lastRes = res;
               
               time = System.currentTimeMillis();
               
               //ZhiboManager.clearEventsVec();
               
               ZhiboManager.constructEventsVec((String)msg);
               
               ZhiboManager.updateEventsDetailsData();
               
               if(StoneAge.showZhibo == true){
               	ZhiboManager.showEventsDeatilsTable();
               }
               
               MergeManager.constructMergeRes();
               MergeManager.updateEventsDetails();
               MergeManager.copyTofinalEventsDetails();
               MergeManager.showMergeDetailsWnd(StoneAge.showMergeWnd);
               
               ZhiboManager.saveEvents();
               
               MergeManager.saveEvents();
               
               ZhiboManager.setStateText("		连接成功");
           }
       }
       
       if(!lastRes.contains(res)){
    	   time = System.currentTimeMillis();
    	   lastRes = res;
       }
       
       
       firstTime = false;

    }  
    
    
    
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("--- Server is active ---");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("--- Server is inactive ---");

        // 10s 之后尝试重新连接服务器
        System.out.println("5s 之后尝试重新连接服务器...");
        Thread.sleep(5 * 1000);
        
        ZhiboManager.setStateText("		失去连接,重新连接中...");
        
        while(ZhiboClient.connect() == false){
        	try{
        		Thread.currentThread().sleep(5 * 1000);
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	
        }
        
        ZhiboManager.setStateText("		连接成功");
        
       
    }
    
    
    
    public static void updateZhiboandMergeData(){
    	
    	try{
    		
            if(!res.contains("[[") || !res.contains("]]")){
          	   return;
             }
             
             
             //ZhiboManager.clearEventsVec();
             
             ZhiboManager.constructEventsVec(res);
             
             ZhiboManager.updateEventsDetailsData();
             
             if(StoneAge.showZhibo == true){
             	ZhiboManager.showEventsDeatilsTable();
             }
             
             
             SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
     		

         	
     		String currentTime = df.format(time);
     		
     		ZhiboManager.setStateText("数据更新于:" + currentTime);
             
     		ZhiboManager.saveEvents();
     		
             
             MergeManager.clearMergeData();
             MergeManager.constructMergeRes();
             MergeManager.copyTofinalEventsDetails();
             MergeManager.updateEventsDetails();
             MergeManager.showMergeDetailsWnd(StoneAge.showMergeWnd);
             
             
             
             MergeManager.saveEvents();
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	

        
    }
    
    

}  