package HGclient;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Vector;



public class GrabHGclientThread extends Thread{

	@Override
    public void run() {
		
		String address = "http://66.133.87.54/";
		
		//String address = "http://hga020.com/";
		
		
		try{

				HGclienthttp hg = new HGclienthttp();
				
				HGclienthttp.recoverGamedetailsVecfromefile();
				
				hg.setLoginParams(address, "gcwfool4", "aaa222");
				
				int b = hg.login();
				if(b == 1){
					//hg.getTotalBet();
				}else{
					hg.login();
				}
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				
				int failtimes = 0;
				
				while(true){
					
					
					
					boolean bgettotal = hg.getTotalBet();
					
					
					
					boolean bgetinplay = hg.getInplaybet();
					boolean bgetgameres = hg.getgameresult();
					
					if(bgettotal && bgetinplay && bgetgameres){
						hg.removelastdayevents();
						
						hg.updateGamedetailsvec();
						
						hg.updateDXQdetailsvec();
						
						//System.out.println("get all success:" + df.format(System.currentTimeMillis()));
						
						HGclienthttp.setGrabStext("更新成功");
						
						HGclienthttp.setGrabColor(Color.GREEN);
						
						
						Thread.currentThread();
						Thread.sleep(1*1000);
						
						
					}else{
						
						
						HGclienthttp.setGrabColor(Color.RED);
						
						
						System.out.println("failed:" + df.format(System.currentTimeMillis()));
						
						//hg.setLoginParams(address, "gcwfool5", "aaa222");
						
						
						int loginres = hg.login();
						
						while(loginres == 2){
							
							HGclienthttp.setGrabStext("维护中...");
							
							Thread.currentThread();
							Thread.sleep(120*1000);
							loginres = hg.login();
							
							
							
						}
						
						
						if(loginres== 1){
							System.out.println("relogin success");
						}else{
							Thread.currentThread();
							Thread.sleep(5*1000);
						}
						
						
							
							
						
					}
					
					
					

					
				}
				

		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
}
