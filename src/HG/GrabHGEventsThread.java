package HG;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Vector;

import HG.HGhttp;
import P8.StoneAge;




public class GrabHGEventsThread  extends Thread{
	
	
	private StoneAge sa;
	
	public long sleepTime = 1*90*1000;
	
	public long time = System.currentTimeMillis();
 
	public static boolean grabStat = true;
	
	public GrabHGEventsThread(StoneAge sat){
		sa = sat;
	}

	public void setSleepTime(int sec){
		sleepTime = sec*1000;
	}
 
	
	@Override
    public void run() {
		
		String address = "https://ag.hga050.com/";
		
		Vector<HGhttp> accounts = new Vector<HGhttp>();
		
		try{
			Vector<String[]> accountDetails = sa.acchgMgr.getAccountDetails();
			
			for(int i = 0; i < accountDetails.size(); i++){
				String[] account = accountDetails.elementAt(i);
				HGhttp hg = new HGhttp();
				hg.setLoginParams(address, account[0], account[1], account[2]);
				System.out.println("login HG " + account[0]);
				sa.setConsoleout();
				System.out.println("login HG" + account[0]);
				//sa.setFileout();
				hg.login();
				accounts.add(hg);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
		while(true){
			try{
				
				
				
        		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
        		
        		
        		long currentTimeL = System.currentTimeMillis();
            	
        		String currentTime = df.format(currentTimeL);
        		
        		System.out.println("HG start grab time:" + currentTime + "  ----------------------------------------------------------------");
				
				
				
				
				HGhttp.clearEventsDetails();				
				HGhttp.clearfailedCatchAccount();
				
				
				HGhttp.geteventsfromesavedata();
				
				
				int tryTimes = accounts.size() * 4;
				
				for(int i = 0, j= 0; i < accounts.size() && j < tryTimes; i++, j++){
					HGhttp hg = accounts.elementAt(i);
					
					if(hg.islogin()){
						boolean getRes = false;
						boolean getInplayRes = false;
						
						getRes = hg.getTotalBet();
						
						getInplayRes = hg.getTotalInplayBet();
							
							if(getRes&&getInplayRes == true){
								System.out.println("hg account  " + hg.getAccount() + " grab success");
								HGhttp.removeFailedAccount(hg.getAccount());
							}else{
								//System.out.println("会员  " + p8.getAccount() + " 抓取失败");
								HGhttp.addFailedCatchAccount( hg.getAccount());
								hg.setIslogin(false);
								i--;
							}
							
							sa.setConsoleout();
							
							if(getRes&&getInplayRes == true){
								System.out.println("hg account  " + hg.getAccount() + " grab success");
							}else{
								System.out.println("hg account  " + hg.getAccount() + " grab failed");
							}
							
							sa.setFileout();
							
						
					}else{
						
						sa.setConsoleout();
						System.out.println("hg relogin " + hg.getAccount());
						sa.setFileout();
						
						HGhttp.addFailedCatchAccount( hg.getAccount());
						
						for(int k = 0; k < 2 && hg.islogin() == false; k++){
							hg.login();
							Thread.sleep(1000);
						}
						i--;
					}
					
				}
				
			 
				
        		currentTimeL = System.currentTimeMillis();
            	
        		currentTime = df.format(currentTimeL);
        		
        		System.out.println("hg grab end time:" + currentTime + "  ----------------------------------------------------------------");
				
				
				
				if(HGhttp.isfailedAccountEmpty()){
					HGhttp.saveEvents();
					
/*					sa.setConsoleout();
					HGhttp.printEvents();
					sa.setFileout();*/

				}
				
				
				

				
				
				HGhttp.sortEventDetails();

				if(HGhttp.isfailedAccountEmpty()){
					HGhttp.copyTofinalEventsDetails();
					HGhttp.updateEventsDetailsData();
				}
				
				
				//HGhttp.showEventsDeatilsTable();
				
				
				
				HGhttp.setGrabStext();
				
				if(HGhttp.isfailedAccountEmpty()){
					
					StoneAge.bHGLogin = true;
					
					if(StoneAge.showHG == true){
						HGhttp.showEventsDeatilsTable();
						StoneAge.showHG = false;
						StoneAge.btnhgLogin.setEnabled(true);
					}
					
					grabStat = true;
					
					HGhttp.setGrabColor(Color.GREEN);
					
					
					Thread.currentThread().sleep(sleepTime);
				}else{
					sa.setConsoleout();
					for(int j = 0;  j < HGhttp.failedCatchAccount.size(); j++){
						System.out.println("hg account  " + HGhttp.failedCatchAccount.elementAt(j) + " grab failed");
					}
					sa.setFileout();
					
					for(int j = 0;  j < HGhttp.failedCatchAccount.size(); j++){
						System.out.println("hg account  " + HGhttp.failedCatchAccount.elementAt(j) + " grab failed");
					}
					
					grabStat = false;
					
					HGhttp.setGrabColor(Color.RED);
					
					Thread.currentThread().sleep(10*1000);
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		



	 
    }
	
}
