package P8;

import team.gl.nio.svr.NettyServer;

public class SeverThread extends Thread{
	
	
	SeverThread(){
		
	}
	
	@Override
    public void run() {
		try{
			new NettyServer().run();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
