package P8;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import Mail.MailManager;

public class ZhiboManager {
	
	static ZhiboEventsDetailsWindow eventsDetailsDataWindow = new ZhiboEventsDetailsWindow();
	
	
	//private static ReadWriteLock lockeEventsDetails = new ReentrantReadWriteLock();
	
	public static   Vector<String[]> eventDetailsVec = new Vector<String[]>();
	
    static ZhiboPreviousDataWindow pDataWindow = new ZhiboPreviousDataWindow();
    
    static ZhiboPreviousDataManager pDataManager = new ZhiboPreviousDataManager(pDataWindow);
	
    public static   Vector<String[]> inPlayeventDetailsVec = new Vector<String[]>();
    

	
	static Vector<String> showLeagueName = new Vector<String>();
	
	public static int ZhiboSendNumber = 700000;
	
	static Map<String, Vector<Integer>> mailRecords = new HashMap<String, Vector<Integer>>(); 
	
	
    public static void showEventsDeatilsTable(){
    	eventsDetailsDataWindow.setVisible(true);
    }
	
    public static void updateEventsDetailsData(){
    	//lockeEventsDetails.readLock().lock();
    	eventsDetailsDataWindow.updateEventsDetails(eventDetailsVec);
    	//lockeEventsDetails.readLock().unlock();
    }
    
    public static void clearEventsVec(){
    	//lockeEventsDetails.writeLock().lock();
    	if(eventDetailsVec.size() != 0){
    		eventDetailsVec.clear();
    	}
    	//lockeEventsDetails.writeLock().unlock();
    }
    

    
    
	public static void initShowLeagueName(){
		showLeagueName.add("英格兰超级联赛");
		showLeagueName.add("意大利甲级联赛");
		showLeagueName.add("德国甲级联赛");
		showLeagueName.add("德国甲级联赛 ");
		showLeagueName.add("西班牙甲级联赛");
		showLeagueName.add("法国甲级联赛");
		showLeagueName.add("欧洲冠军联赛");
		showLeagueName.add("欧足联欧洲联赛");
		//showLeagueName.add("英格兰 - 超级联赛");
		//showLeagueName.add("欧足联 - 欧罗巴联赛");
		
		pDataManager.init();
		
		
	}
	
	
	public static void setStateText(String txt){
		eventsDetailsDataWindow.setStateText(txt);
	}
	
	public  static boolean isInShowLeagueName(String str){
		boolean in = false;
		
		for(int i = 0; i < showLeagueName.size(); i++){
			if(showLeagueName.elementAt(i).contains(str)){
				in = true;
				break;
			}
		}
		
		return in;
	}
	
	public static Vector<String[]> getEventdetails(){
		Vector<String[]> tmp = null;
				
		//lockeEventsDetails.readLock().lock();
		tmp = (Vector<String[]>)eventDetailsVec.clone();
		//lockeEventsDetails.readLock().unlock();
		
		return tmp;
	}
	
	
	
	public static Vector<String[]> getInplayEventsDetails(){
		Vector<String[]> tmp = null;
		
		//lockeEventsDetails.readLock().lock();
		tmp = (Vector<String[]>)inPlayeventDetailsVec.clone();
		//lockeEventsDetails.readLock().unlock();
		
		return tmp;
	}
	
	
    
    public static void constructEventsVec(String eventsStr){
    	
    	//lockeEventsDetails.writeLock().lock();
    	
    	try{
    		
    		int posStart = -1;
    		int posEnd = -1;
    		
    		posStart = eventsStr.indexOf("[\"");
    		
    		
    		while(posStart != -1){
    			
    			posStart = posStart + 1;
    			
    			posEnd = eventsStr.indexOf("]", posStart);
    			
    			String oneItem = eventsStr.substring(posStart, posEnd);
    			
    			
    			
    			String[] event = {"","","","","","","",""};
    			event[0] = "0";
    			
    			
    			//gaidong
    			int posStartTmp = oneItem.indexOf("\"");
    			int posEndTmp = oneItem.indexOf("\"", posStartTmp + 1);
    			
    			event[1] = oneItem.substring(posStartTmp + 1, posEndTmp);
    			
    			posStartTmp = oneItem.indexOf("\"", posEndTmp + 1);
    			posEndTmp = oneItem.indexOf("\"", posStartTmp + 1);
    			event[2] = oneItem.substring(posStartTmp + 1, posEndTmp);
    			
    			posStartTmp = oneItem.indexOf("\"", posEndTmp + 1);
    			posEndTmp = oneItem.indexOf("\"", posStartTmp + 1);
    			event[3] = oneItem.substring(posStartTmp + 1, posEndTmp);
    			
    			
    			
    			posStartTmp = oneItem.indexOf(",", posEndTmp + 1);
    			
    			String strNumber = oneItem.substring(posStartTmp + 1);
    			
    			String[] eventTmp = strNumber.split(",");
    			
    			event[4] = eventTmp[0];
    			event[5] = eventTmp[1];
    			event[6] = eventTmp[2];
    			event[7] = eventTmp[3];
    			
/*    			String[] event = {"0", eventTmp[ZHIBOINDEX.LEAGUENAME.ordinal()-1], eventTmp[ZHIBOINDEX.TIME.ordinal()-1], eventTmp[ZHIBOINDEX.EVENTNAMNE.ordinal()-1]
    					,eventTmp[ZHIBOINDEX.PERIOD0HOME.ordinal()-1], eventTmp[ZHIBOINDEX.PERIOD0OVER.ordinal()-1], eventTmp[ZHIBOINDEX.PERIOD1HOME.ordinal()-1]
    							,eventTmp[ZHIBOINDEX.PERIOD1OVER.ordinal()-1]};*/
    			
    			if(event[ZHIBOINDEX.TIME.ordinal()].contains("(") || event[ZHIBOINDEX.TIME.ordinal()].contains(",")){
    				
    				boolean alreadyIn = false;
    				for(int i = 0; i < inPlayeventDetailsVec.size(); i++){
    					String[] item = inPlayeventDetailsVec.elementAt(i);
    					if(item[ZHIBOINDEX.EVENTNAMNE.ordinal()].equals(event[ZHIBOINDEX.EVENTNAMNE.ordinal()])){
    						alreadyIn = true;
    						break;
    					}
    				}
    				
    				if(alreadyIn == false){
    					
    					System.out.println("zhibo add to inpaly:" + Arrays.toString(event));
    					
    					inPlayeventDetailsVec.add(event);
    				}
    				
    				
    			}else{
    				
    				String timeStr = event[ZHIBOINDEX.TIME.ordinal()];
    				
    				
    				SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
    				
    				SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
    				
    				
    				long currentTimeL = System.currentTimeMillis();
    				
    				String LocaltodayStr = dfDay.format(currentTimeL);
    				
    				
    				String MinStr = dfMin.format(currentTimeL);
    				
    				java.util.Date Mintime = dfMin.parse(MinStr);
    				
    				
    				Calendar americaTime = Calendar.getInstance();  
    				americaTime.setTime(Mintime);
    				
    				americaTime.add(Calendar.HOUR_OF_DAY, -12);
    				
    				
    				String todayStr = dfDay.format(americaTime.getTimeInMillis());
    				
    				//eventTime.add(Calendar.HOUR_OF_DAY, 12);
    				
    				
    				//String[] timeA = dfMin.format(currentTimeL).split(" ");
    				
    				if(!timeStr.contains("-")){
    					
    					timeStr = todayStr + " " + timeStr;
    				}
    				
    				java.util.Date time = dfMin.parse(timeStr);
    				
    				
    				Calendar eventTime = Calendar.getInstance();  
    				eventTime.setTime(time);
    				
    				
    				eventTime.add(Calendar.HOUR_OF_DAY, 12);
    				
    				timeStr = dfMin.format(eventTime.getTimeInMillis());
    				
    				if(timeStr.contains(LocaltodayStr)){
    					timeStr = timeStr.replace(LocaltodayStr + " ", "");
    				}

    				event[ZHIBOINDEX.TIME.ordinal()] = timeStr;
    				
    				boolean alreadyIn = false;
    				
    				//看是否加入
    				for(int i = 0; i < eventDetailsVec.size(); i++){
    					String[] item = eventDetailsVec.elementAt(i);
    					
    					if(item[ZHIBOINDEX.EVENTNAMNE.ordinal()].equals(event[ZHIBOINDEX.EVENTNAMNE.ordinal()])
    							&& item[ZHIBOINDEX.TIME.ordinal()].equals(event[ZHIBOINDEX.TIME.ordinal()])){
    						
    						alreadyIn = true;
    						

							eventDetailsVec.elementAt(i)[ZHIBOINDEX.PERIOD0HOME.ordinal()] = event[ZHIBOINDEX.PERIOD0HOME.ordinal()];
							eventDetailsVec.elementAt(i)[ZHIBOINDEX.PERIOD0OVER.ordinal()] = event[ZHIBOINDEX.PERIOD0OVER.ordinal()];
							eventDetailsVec.elementAt(i)[ZHIBOINDEX.PERIOD1HOME.ordinal()] = event[ZHIBOINDEX.PERIOD1HOME.ordinal()];
							eventDetailsVec.elementAt(i)[ZHIBOINDEX.PERIOD1OVER.ordinal()] = event[ZHIBOINDEX.PERIOD1OVER.ordinal()];
							System.out.println("存在两场相同球赛");
							//eventDetailsVec.elementAt(i)[ZHIBOINDEX.PERIOD1OVER.ordinal()] = event[12];
							
							break;

    							
    					}
    					
    				}
    				
    				if(alreadyIn == false){
    					eventDetailsVec.add(event);
    				}

    				
    			}
    			
    			
    			posStart = eventsStr.indexOf("[\"", posEnd);
    			
    		}
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		//lockeEventsDetails.writeLock().unlock();
    	}
    	
    	//lockeEventsDetails.writeLock().unlock();
    	
    }
    

    
    public static void sendMails(){
    	
    	try{
    		
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    		
        	for(int i = 0; i < eventDetailsVec.size(); i++){
        		String[] item = eventDetailsVec.elementAt(i).clone();
        		
				SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				
				//SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				
				
				long currentTimeL = System.currentTimeMillis();
				
				String todayStr = dfDay.format(currentTimeL);
				

					
				String timeStr = item[TYPEINDEX.TIME.ordinal()];
				
				if(!timeStr.contains("-")){
					timeStr = todayStr + " " + timeStr;
				}
        		
        		
        		//long time = Long.parseLong(item[ZHIBOINDEX.TIME.ordinal()]);
        		
        		String key = item[ZHIBOINDEX.EVENTNAMNE.ordinal()] + " " + timeStr;
        		
/*        		String saved = item[ZHIBOINDEX.SAVED.ordinal()];
        		
        		if(saved.contains("1"))
        			continue;
        		
        		//过滤滚动盘
        		if(key.contains("滚动盘")){
        			continue;
        		}*/
        		
        		if(true != mailRecords.containsKey(key)){
        			Vector<Integer> records = new Vector<Integer>();
        			mailRecords.put(key, records);
        		}
        		
        		//开始解析数据
    			double p0h = Double.parseDouble(item[ZHIBOINDEX.PERIOD0HOME.ordinal()]);
    			double p0o = Double.parseDouble(item[ZHIBOINDEX.PERIOD0OVER.ordinal()]);
    			double p1h = Double.parseDouble(item[ZHIBOINDEX.PERIOD1HOME.ordinal()]);
    			double p1o = Double.parseDouble(item[ZHIBOINDEX.PERIOD1OVER.ordinal()]);
    			
    			int p0hsend = (int) (p0h/ZhiboSendNumber);    			
    			Vector<Integer> records = mailRecords.get(key);    			
    			if(true != records.contains(p0hsend) && p0hsend != 0){
    				records.add(p0hsend);
    				System.out.println("LL send 全场让球 " + df.format(System.currentTimeMillis()));
    				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "43069453@qq.com", "LL " + key, "全场让球:" + Integer.toString(p0hsend));
    				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "490207143@qq.com", "LL " + key, "全场让球:" + Integer.toString(p0hsend));
    				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "240749322@qq.com", "LL " + key, "全场让球:" + Integer.toString(p0hsend));

    			}
    			
    			int p0osend = (int) (p0o/ZhiboSendNumber);
    			int p0osendsaved = 0;
    			if(p0osend != 0){
    				if(p0osend < 0){
    					p0osendsaved = p0osend - 10;
    				}else{
    					p0osendsaved = p0osend + 10;
    				}
    				
        			if(true != records.contains(p0osendsaved)){
        				records.add(p0osendsaved);
        				System.out.println("LL send 全场大小 " +  df.format(System.currentTimeMillis()));
        				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "43069453@qq.com", "LL " + key, "全场大小:" + Integer.toString(p0osend));
        				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "490207143@qq.com", "LL " + key, "全场大小:" + Integer.toString(p0osend));
        				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "240749322@qq.com", "LL " + key, "全场大小:" + Integer.toString(p0osend));

        			}
    				
    			}
    			
    			
    			int p1hsend = (int) (p1h/ZhiboSendNumber);
    			int p1hsendsaved = 0;
    			if(p1hsend != 0){
    				if(p1hsend < 0){
    					p1hsendsaved = p1hsend - 20;
    				}else{
    					p1hsendsaved = p1hsend + 20;
    				}
    				
        			if(true != records.contains(p1hsendsaved)){
        				records.add(p1hsendsaved);
        				System.out.println("LL send 半场让球 " + df.format(System.currentTimeMillis()));
        				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "43069453@qq.com", "LL " + key, "半场让球:" + Integer.toString(p1hsend));
        				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "490207143@qq.com", "LL " + key, "半场让球:" + Integer.toString(p1hsend));
        				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "240749322@qq.com", "LL " + key, "半场让球:" + Integer.toString(p1hsend));
        				
        			}
    				
    			}
    			
    			
    			int p1osend = (int) (p1o/ZhiboSendNumber);
    			int p1osendsaved = 0;
    			if(p1osend != 0){
    				if(p1osend < 0){
    					p1osendsaved = p1osend - 30;
    				}else{
    					p1osendsaved = p1osend + 30;
    				}
    				
        			if(true != records.contains(p1osendsaved)){
        				records.add(p1osendsaved);
        				System.out.println("LL send 半场大小 "+ df.format(System.currentTimeMillis()));
        				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "43069453@qq.com", "LL " + key, "半场大小:" + Integer.toString(p1osend));
        				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "490207143@qq.com", "LL " + key, "半场大小:" + Integer.toString(p1osend));
        				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "240749322@qq.com", "LL " + key, "半场大小:" + Integer.toString(p1osend));

        			}
    				
    			}
    			

        	}
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	

    }
    
    
    
    public static void saveEvents(){
    	
    	//lockeEventsDetails.writeLock().lock();
		try{
			
			
			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
			
			SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			
			long currentTimeL = System.currentTimeMillis();
			
			String todayStr = dfDay.format(currentTimeL);
			
			//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			for(int i = 0; i < eventDetailsVec.size(); i++){
				//long eventTime = Long.parseLong(eventDetailsVec.elementAt(i)[ZHIBOINDEX.TIME.ordinal()]);
				
				String timeStr = eventDetailsVec.elementAt(i)[ZHIBOINDEX.TIME.ordinal()];
				
				if(!timeStr.contains("-")){
					timeStr = todayStr + " " + timeStr;
				}
				
				long currentTime = System.currentTimeMillis();
				
				long passMinutes = 108*60*1000;
				
				long twoMinutes = 2*60*1000;
				
				
				java.util.Date time = dfMin.parse(timeStr);
				
				
				Calendar eventTime = Calendar.getInstance();  
				eventTime.setTime(time);

				long pass = currentTime - eventTime.getTimeInMillis();
				
				if(pass > passMinutes){
					
					System.out.println("zhibo events remove:" + Arrays.toString(eventDetailsVec.elementAt(i)));
					
					eventDetailsVec.remove(i);
					i--;
					continue;
				}
				
				if(pass > twoMinutes && eventDetailsVec.elementAt(i)[ZHIBOINDEX.SAVED.ordinal()].contains("0")){
					
					String[] item = eventDetailsVec.elementAt(i).clone();
					item[ZHIBOINDEX.TIME.ordinal()] = timeStr;
					boolean res = pDataManager.saveTofile(item);	
					
					if(res == true){
						
						System.out.println("zhibo save success:" + Arrays.toString(item));
						eventDetailsVec.elementAt(i)[ZHIBOINDEX.SAVED.ordinal()] = "1";		
					}
				}

				
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			//lockeEventsDetails.writeLock().unlock();
		}
		
		
		//lockeEventsDetails.writeLock().unlock();
		
    }
    
    
    
	public static void showpDataWnd(){
		pDataWindow.setVisible(true);
	}
    
	
	public static void updatepDataDetails(){
		pDataManager.updatepEventsDetails();
	}
	
	
	public static String[] getZhiboSaveItem(String eventName){
		return pDataManager.findLatestEvents(eventName);
	}
    
}
