package P8;

import java.util.Vector;


public class GrabEventsThread  extends Thread{
	
	
	private StoneAge sa;
	
	public long sleepTime = 2*60*1000;
	
	GrabEventsThread(StoneAge sat){
		sa = sat;
	}

	public void setSleepTime(int min){
		sleepTime = min*60*1000;
	}
	
	
	@Override
    public void run() {

		while(true){
			
			try{
				P8Http.clearEventsDetails();
				
				P8Http.clearfailedCatchAccount();
				
				Vector<String[]> accountDetails = sa.accMgr.getAccountDetails();
				
				for(int i = 0; i < accountDetails.size(); i++){
					String[] account = accountDetails.elementAt(i);
					P8Http.setLoginParams(account[0], account[1], account[2], account[3]);
					int loginRes = 0;
					loginRes = P8Http.login();
					
					for(int j = 0; j < 2 && loginRes == 0; j++){
						
						try{
							Thread.currentThread().sleep(5*1000);
							
						}catch(Exception exception){
							
							
							
						}
						
						
						loginRes = P8Http.login();			
					}
					
					if(loginRes == 1){
						
						
						
						
						
						if(account[0].contains("p88agent")){
							boolean getRes = P8Http.getTotalP8Bet();

							if(getRes == false){
								getRes = P8Http.getTotalP8Bet();
							}
							
							if(getRes = true){
								System.out.println("会员  " + account[1] + " 抓取成功");
							}else{
								System.out.println("会员  " + account[1] + " 抓取失败");
								i--;
							}
							
							sa.setConsoleout();
							
							if(getRes = true){
								System.out.println("会员  " + account[1] + " 抓取成功");
							}else{
								System.out.println("会员  " + account[1] + " 抓取失败");
								i--;
							}
							
							sa.setFileout();
							
						}else{								
							
							boolean getRes = P8Http.getTotalPS38Bet();
							
							if(getRes == false){
								getRes = P8Http.getTotalPS38Bet();
							}
							
							if(getRes = true){
								System.out.println("会员  " + account[1] + " 抓取成功");
							}else{
								System.out.println("会员  " + account[1] + " 抓取失败");
								i--;
							}
							
							sa.setConsoleout();
							
							if(getRes = true){
								System.out.println("会员  " + account[1] + " 抓取成功");
							}else{
								System.out.println("会员  " + account[1] + " 抓取失败");
								i--;
							}
							
							sa.setFileout();
						}
						
						
					}
					else{
						
						System.out.println("会员  " + account[1] + " 抓取失败");

						
						sa.setConsoleout();
						System.out.println("会员  " + account[1] + " 抓取失败");
						sa.setFileout();
						
						P8Http.addFailedCatchAccount(account[1]);
					}
					
				}
			
			
			P8Http.sortEventDetails();

			
			P8Http.updateEventsDetailsData();
			
			P8Http.showEventsDeatilsTable();
			
			
			
			P8Http.setGrabStext();
			
			if(P8Http.isfailedAccountEmpty()){
				Thread.currentThread().sleep(sleepTime);
			}else{
				Thread.currentThread().sleep(10*1000);
			}
			
			
				
			}catch(Exception e){
				e.printStackTrace();
				
			}
			

		}

		
    }
	
}
