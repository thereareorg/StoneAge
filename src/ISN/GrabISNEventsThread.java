package ISN;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.json.JSONArray;

import MergeNew.MergeNewManager;
import P8.ISNhttp;
import P8.MergeManager;
import P8.StoneAge;
import P8.ZhiboManager;

public class GrabISNEventsThread  extends Thread{
	
	
	private StoneAge sa;
	
	public long sleepTime = 1*60*1000;
	
	public long time = System.currentTimeMillis();
	
	public static boolean grabStat = true;
	
	public static String successTime = "";
	
	public static String eventsStr = "";
	
	private static ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public static String getSuccessTime() {
		lock.readLock().lock();
		String res = successTime;
		lock.readLock().unlock();
		return res;
	}
	
	
	public static String getEventsStr() {
		lock.readLock().lock();
		String res = eventsStr;
		lock.readLock().unlock();
		return res;
	}
	
	public GrabISNEventsThread(StoneAge sat){
		sa = sat;
	}

	public void setSleepTime(int sec){
		sleepTime = sec*1000;
	}
	
	
	@Override
    public void run() {
		
		Vector<ISNhttp> accounts = new Vector<ISNhttp>();
		
		try{
			Vector<String[]> accountDetails = sa.accisnMgr.getAccountDetails();
			
			for(int i = 0; i < accountDetails.size(); i++){
				String[] account = accountDetails.elementAt(i);
				ISNhttp isn = new ISNhttp();
				isn.setLoginParams("https://www.isn999.com/",account[0], account[1], account[2]);
				System.out.println("login " + account[0]);
				sa.setConsoleout();
				System.out.println("login " + account[0]);
				sa.setFileout();
				isn.login();
				accounts.add(isn);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
		while(true){
			try{

        		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
        		
        		
        		long currentTimeL = System.currentTimeMillis();
            	
        		String currentTime = df.format(currentTimeL);
        		
        		System.out.println("开始抓取时间:" + currentTime + "  ----------------------------------------------------------------");
				
        		ISNhttp.JSONISNEvents = new JSONArray("[]"); // clear events
        		
				
				ISNhttp.clearEventsDetails();				
				ISNhttp.clearfailedCatchAccount();
				
				int tryTimes = accounts.size() * 4;
				
				for(int i = 0, j= 0; i < accounts.size() && j < tryTimes; i++, j++){
					ISNhttp isn = accounts.elementAt(i);
					
					if(isn.islogin()){
						boolean getRes = isn.getTotalBet();

							
							if(getRes == true){
								System.out.println("account  " + isn.getAccount() + " grab success");
								ISNhttp.removeFailedAccount(isn.getAccount());
							}else{
								//System.out.println("会员  " + p8.getAccount() + " 抓取失败");
								ISNhttp.addFailedCatchAccount( isn.getAccount());
								isn.setIslogin(false);
								i--;
							}
							
							sa.setConsoleout();
							
							if(getRes == true){
								System.out.println("account  " + isn.getAccount() + " grab success");
							}else{
								//System.out.println("会员  " + p8.getAccount() + " 抓取失败");
							}
							
							sa.setFileout();
							
						
					}else{
						
						sa.setConsoleout();
						System.out.println("重新登录 " + isn.getAccount());
						sa.setFileout();
						
						ISNhttp.addFailedCatchAccount( isn.getAccount());
						
						for(int k = 0; k < 2 && isn.islogin() == false; k++){
							isn.login();
							Thread.currentThread().sleep(1000);
						}
						i--;
					}
					
				}
				
				
				
        		currentTimeL = System.currentTimeMillis();
            	
        		currentTime = df.format(currentTimeL);
        		
        		System.out.println("抓取结束时间:" + currentTime + "  ----------------------------------------------------------------");
				
				
				
//				if(ISNhttp.isfailedAccountEmpty()){
//					//ZhiboManager.parseDirection();
//					ZhiboManager.saveEvents();
//
//				}
//				
//				
//				ZhiboManager.sortEventDetails();
//
//				if(ISNhttp.isfailedAccountEmpty()){
//					ISNhttp.copyTofinalEventsDetails();
//					ZhiboManager.updateEventsDetailsData();
//				}
				
				
				//ISNhttp.showEventsDeatilsTable();
				
				
				
				
				
				if(ISNhttp.isfailedAccountEmpty()){
					
					
					StoneAge.zhiboConnected = true;
					
		               ZhiboManager.clearEventsVec();  
		               
		               System.out.println(ISNhttp.JSONISNEvents.toString());
		               ZhiboManager.constructEventsVec(ISNhttp.JSONISNEvents.toString());
		               
		               
		               ZhiboManager.sortEventDetails();
		               
		               ZhiboManager.updateEventsDetailsData();
		               
		               
		               ZhiboManager.saveEvents();

					if(StoneAge.showZhibo == true){
						ZhiboManager.showEventsDeatilsTable();
						StoneAge.showZhibo = false;
						StoneAge.btnZhiboConnect.setEnabled(true);
					}
					
					grabStat = true;
					
					ZhiboManager.setStateColor(Color.GREEN);
					
					ZhiboManager.setStateText("数据更新于:" + currentTime);
		               
		               
		               
		               MergeManager.clearMergeData();
		               
		               MergeManager.constructMergeRes();
		               MergeManager.saveEvents();
		               
		               //可以做一些改变显示的事情
		               MergeManager.updateEventsDetails();
		               
		               
		               MergeManager.copyTofinalEventsDetails();
		               
		               
//		               MergeNewManager.constructMergeRes();
//		               MergeNewManager.saveEvents();
//		             //可以做一些改变显示的事情
//		               MergeNewManager.updateEventsDetails();
//		               
//		               MergeNewManager.copyTofinalEventsDetails();
		               
		               lock.writeLock().lock();
		               successTime = df.format(System.currentTimeMillis());
		               eventsStr = ISNhttp.JSONISNEvents.toString();
		               lock.writeLock().unlock();
					
		               Thread.currentThread().sleep(sleepTime);
					
					
				}else{
					sa.setConsoleout();
					for(int j = 0;  j < ISNhttp.failedCatchAccount.size(); j++){
						System.out.println("zhibo account  " + ISNhttp.failedCatchAccount.elementAt(j) + " grab failed");
					}
					sa.setFileout();
					
					for(int j = 0;  j < ISNhttp.failedCatchAccount.size(); j++){
						System.out.println("zhibo account  " + ISNhttp.failedCatchAccount.elementAt(j) + " grab failed");
					}
					
					grabStat = false;
					
					ZhiboManager.setStateColor(Color.RED);
					
					ZhiboManager.setStateText("数据更新失败");
					
					Thread.currentThread().sleep(10*1000);
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		



		
    }
	
}
