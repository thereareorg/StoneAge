package team.gl.nio.cln;

import java.text.SimpleDateFormat;

import P8.MergeManager;
import P8.StoneAge;
import P8.ZhiboManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit; 

public class ZhiboClientHandler extends ChannelInboundHandlerAdapter {  
	

	public static String lastRes = "";
	
	public static String res = "";
	
	public static boolean firstTime = true;
	
	public static long time = 0L; 
	
	public static long time1 = System.currentTimeMillis();
	
	public static long printTime = 0L;
	
	public static ChannelHandlerContext ctx_s = null;
	

    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg)  
            throws Exception {
       
       res = (String)msg;
       
       
       time1 = System.currentTimeMillis();
           
       
       //ZhiboManager.setStateText("		连接成功");
       
       
       if(firstTime == true){
    	   if(!res.contains("[[") || !res.contains("]]")){
        	   return;
           }
           
           if(!lastRes.contains(res)){
               lastRes = res;
               
               time = System.currentTimeMillis();
               
               ZhiboManager.clearEventsVec();
               
               ZhiboManager.constructEventsVec((String)msg);
               
               ZhiboManager.sendMails();
               
               ZhiboManager.updateEventsDetailsData();
               
               if(StoneAge.showZhibo == true){
               	ZhiboManager.showEventsDeatilsTable();
               }
               
               MergeManager.constructMergeRes();
               MergeManager.updateEventsDetails();
               MergeManager.copyTofinalEventsDetails();
               
               ZhiboManager.saveEvents();
               
               MergeManager.saveEvents();
               
               ZhiboManager.setStateText("		连接成功");
           }
       }
       
       if(!lastRes.contains(res)){
    	   
    	   if(System.currentTimeMillis() - printTime > 300*1000){
    		   System.out.println(res);
    		   printTime =  System.currentTimeMillis();
    	   }
    	   
    	   time = System.currentTimeMillis();
    	   lastRes = res;
       }
       
       firstTime = false;

    }  
    
    
    
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {		
        System.out.println("--- Server is active ---");
        ctx_s = ctx;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("--- Server is inactive ---"); 
        // 10s 之后尝试重新连接服务器
        ZhiboManager.setStateText("		失去连接,重新连接中...");
        System.out.println("尝试重新连接服务器...");
        
        
        
        ZhiboClient.group.schedule(new Runnable() {  
            
            @Override  
          
            public void run() {  
                while(ZhiboClient.connect() == false) {
                	
                	try{  					
    					Thread.currentThread().sleep(5000);
    					
    				}catch(Exception e){
    					e.printStackTrace();
    				} 
                }
            }  
          
          }, 1L, TimeUnit.SECONDS);  
          
        super.channelInactive(ctx);
             
    }
    
    
    
    public static void updateZhiboandMergeData(){
    	
    	try{
    		
            if(System.currentTimeMillis() - time1 > 180 * 1000) {
            	if(ctx_s != null) {
            		ctx_s.close();
            	}
         	    time1 = System.currentTimeMillis();
         	    
         	    return;
            }
    		
    		
    		
            if(!res.contains("[[") || !res.contains("]]")){
          	   return;
             }
            

             
             
             ZhiboManager.clearEventsVec();
             
             ZhiboManager.constructEventsVec(res);
             
             ZhiboManager.sendMails();
             
             ZhiboManager.updateEventsDetailsData();
             
        
             SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
     		

         	
     		String currentTime = df.format(time);
     		
     		ZhiboManager.setStateText("数据更新于:" + currentTime);
             
     		ZhiboManager.saveEvents();
     		
             
             MergeManager.clearMergeData();
             MergeManager.constructMergeRes();
             MergeManager.copyTofinalEventsDetails();
             MergeManager.updateEventsDetails();
           
             
             MergeManager.saveEvents();
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	

        
    }
    
    

}  