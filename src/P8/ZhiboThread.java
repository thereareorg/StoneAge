package P8;

import team.gl.nio.cln.NettyClient;
import team.gl.nio.cln.ZhiboClientHandler;
import team.gl.nio.cmn.Bag;

public class ZhiboThread extends Thread{
	
	public long sleepTime = 60*1000;

	
	public void setSleepTime(int sec){
		sleepTime = sec * 1000;
	}
	
	@Override
    public void run() {
		try{
			while(true){
				
				try{
					ZhiboClientHandler.updateZhiboandMergeData();
					
					Thread.currentThread().sleep(sleepTime);
					
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
