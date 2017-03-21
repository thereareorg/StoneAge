package P8;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ZhiboManager {
	
	static ZhiboEventsDetailsWindow eventsDetailsDataWindow = new ZhiboEventsDetailsWindow();
	
	
	//private static ReadWriteLock lockeEventsDetails = new ReentrantReadWriteLock();
	
	public static   Vector<String[]> eventDetailsVec = new Vector<String[]>();
	
    static ZhiboPreviousDataWindow pDataWindow = new ZhiboPreviousDataWindow();
    
    static ZhiboPreviousDataManager pDataManager = new ZhiboPreviousDataManager(pDataWindow);
	
    public static   Vector<String[]> inPlayeventDetailsVec = new Vector<String[]>();
    

	
	static Vector<String> showLeagueName = new Vector<String>();
	
    public static void showEventsDeatilsTable(){
    	eventsDetailsDataWindow.setVisible(true);
    }
	
    public static void updateEventsDetailsData(){
    	//lockeEventsDetails.readLock().lock();
    	eventsDetailsDataWindow.updateEventsDetails(eventDetailsVec);
    	//lockeEventsDetails.readLock().unlock();
    }
    
/*    public static void clearEventsVec(){
    	lockeEventsDetails.writeLock().lock();
    	if(eventDetailsVec.size() != 0){
    		eventDetailsVec.clear();
    	}
    	lockeEventsDetails.writeLock().unlock();
    }*/
    

    
    
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
    			
    			//oneItem = oneItem.replace("\"", "");
    			
    			
    			String[] event = {"", "",  "", "", "", "", "", ""};
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
    						
    						if(item[ZHIBOINDEX.SAVED.ordinal()].contains("1")){
    							break;
    						}else{
    							eventDetailsVec.elementAt(i)[ZHIBOINDEX.PERIOD0HOME.ordinal()] = event[ZHIBOINDEX.PERIOD0HOME.ordinal()];
    							eventDetailsVec.elementAt(i)[ZHIBOINDEX.PERIOD0OVER.ordinal()] = event[ZHIBOINDEX.PERIOD0OVER.ordinal()];
    							eventDetailsVec.elementAt(i)[ZHIBOINDEX.PERIOD1HOME.ordinal()] = event[ZHIBOINDEX.PERIOD1HOME.ordinal()];
    							eventDetailsVec.elementAt(i)[ZHIBOINDEX.PERIOD1OVER.ordinal()] = event[ZHIBOINDEX.PERIOD1OVER.ordinal()];
    							break;
    							
    						}
    							
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
    
}
