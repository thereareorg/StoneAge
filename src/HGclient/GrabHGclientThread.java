package HGclient;

import java.text.SimpleDateFormat;
import java.util.Vector;



public class GrabHGclientThread extends Thread{

	@Override
    public void run() {
		
		String address = "http://66.133.87.54/";
		
		//String address = "http://hga020.com/";
		
		
		try{

				HGclienthttp hg = new HGclienthttp();
				
				hg.recoverGamedetailsVecfromefile();
				
				hg.setLoginParams(address, "gcwfool1", "aaa222");
				
				boolean b = hg.login();
				if(b){
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
						
						System.out.println("get all success:" + df.format(System.currentTimeMillis()));
					}else{
						failtimes++;
						
						if(failtimes > 10){
							System.out.println("failed:" + df.format(System.currentTimeMillis()));
							System.out.println("relogin:");
							
							b = hg.login();
							
							
							failtimes = 0;
							
							
						}
					}
					
					
					
					Thread.currentThread().sleep(30*1000);
					
				}
				

		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
}
