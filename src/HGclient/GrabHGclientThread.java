package HGclient;

import java.util.Vector;



public class GrabHGclientThread extends Thread{

	@Override
    public void run() {
		
		String address = "http://66.133.87.54/";
		
		
		
		try{

				HGclienthttp hg = new HGclienthttp();
				hg.setLoginParams(address, "gcwfool1", "aaa222");

		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
}
