package P8;

import java.util.Vector;


public class GrabEventsThread  extends Thread{
	
	
	private StoneAge sa;
	
	public long sleepTime = 1*60*1000;
	
	GrabEventsThread(StoneAge sat){
		sa = sat;
	}

	public void setSleepTime(int sec){
		sleepTime = sec*1000;
	}
	
	
	@Override
    public void run() {
		
		Vector<P8Http> accounts = new Vector<P8Http>();
		
		try{
			Vector<String[]> accountDetails = sa.accMgr.getAccountDetails();
			
			for(int i = 0; i < accountDetails.size(); i++){
				String[] account = accountDetails.elementAt(i);
				P8Http p8 = new P8Http();
				p8.setLoginParams(account[0], account[1], account[2], account[3]);
				System.out.println("登录 " + account[1]);
				sa.setConsoleout();
				System.out.println("登录 " + account[1]);
				sa.setFileout();
				p8.login();
				accounts.add(p8);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
		while(true){
			try{
				P8Http.clearEventsDetails();				
				P8Http.clearfailedCatchAccount();
				
				int tryTimes = accounts.size() * 4;
				
				for(int i = 0, j= 0; i < accounts.size() && j < tryTimes; i++, j++){
					P8Http p8 = accounts.elementAt(i);
					
					if(p8.islogin()){
						boolean getRes = false;
						if(p8.getAddress().contains("p88agent")){
							
							getRes = p8.getTotalP8Bet();

							if(getRes == false){
								getRes = p8.getTotalP8Bet();
							}
						}else{
							
							getRes = p8.getTotalPS38Bet();

							if(getRes == false){
								getRes = p8.getTotalPS38Bet();
							}
						}
							
							if(getRes == true){
								System.out.println("会员  " + p8.getAccount() + " 抓取成功");
								P8Http.removeFailedAccount(p8.getAccount());
							}else{
								//System.out.println("会员  " + p8.getAccount() + " 抓取失败");
								P8Http.addFailedCatchAccount( p8.getAccount());
								p8.setIslogin(false);
								i--;
							}
							
							sa.setConsoleout();
							
							if(getRes == true){
								System.out.println("会员  " + p8.getAccount() + " 抓取成功");
							}else{
								//System.out.println("会员  " + p8.getAccount() + " 抓取失败");
							}
							
							sa.setFileout();
							
						
					}else{
						
						sa.setConsoleout();
						System.out.println("重新登录 " + p8.getAccount());
						sa.setFileout();
						
						for(int k = 0; k < 3 && p8.islogin() == false; k++){
							p8.login();
							Thread.currentThread().sleep(1000);
						}
						i--;
					}
					
				}
				
				if(P8Http.isfailedAccountEmpty()){
					P8Http.saveEvents();
				}
				
				
				P8Http.sortEventDetails();

				if(P8Http.isfailedAccountEmpty()){
					P8Http.updateEventsDetailsData();
				}
				
				
				P8Http.showEventsDeatilsTable();
				
				
				
				P8Http.setGrabStext();
				
				if(P8Http.isfailedAccountEmpty()){
					Thread.currentThread().sleep(sleepTime);
				}else{
					sa.setConsoleout();
					for(int j = 0;  j < P8Http.failedCatchAccount.size(); j++){
						System.out.println("会员  " + P8Http.failedCatchAccount.elementAt(j) + " 抓取失败");
					}
					sa.setFileout();
					Thread.currentThread().sleep(10*1000);
				}
				
			}catch(Exception e){
				
			}
		}
		



		
    }
	
}
