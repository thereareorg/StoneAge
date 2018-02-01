package P8;

import java.awt.Color;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
    

    public static Vector<String> alreadSendmailEvents = new Vector<String>();
    
    public static Vector<String> sendpankoualready = new Vector<String>();
	
	static Vector<String> showLeagueName = new Vector<String>();
	
	public static int Zhibop0hSendNumber = 400000;
	public static int Zhibop0oSendNumber = 400000;
	
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
	
	
	public static void setStateColor(Color color){
		eventsDetailsDataWindow.setStateColor(color);
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
	
    public static Vector<String[]> getpSubevents(){
    	return pDataManager.getpSubevents();
    }
	
    
    public static Vector<String[]> getpreviouseventsdata(){
    	return pDataManager.getpreviousdata();
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
    			
    			if((event[ZHIBOINDEX.TIME.ordinal()].contains("(") || event[ZHIBOINDEX.TIME.ordinal()].contains(",")) &&
    					!event[ZHIBOINDEX.TIME.ordinal()].equals("(0 - 0)")){
    				
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
    				
    				if(timeStr.contains("(")){
    					event[ZHIBOINDEX.TIME.ordinal()] = timeStr;
    				}else{
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
    				}
    				

    				
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
    

    
    public static void sendpankouMails(){
    	
    	try{
    		
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    		
    		for(int i = 0; i< eventDetailsVec.size(); i++){
    			
    			String timeStr = eventDetailsVec.elementAt(i)[ZHIBOINDEX.TIME.ordinal()];
    			
    			if(timeStr.contains("(")){
    				continue;
    			}
    			
    			SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
    			long currentTimeL = System.currentTimeMillis();
    			
    			String todayStr = dfDay.format(currentTimeL);
    			
    			if(!timeStr.contains("-")){
					timeStr = todayStr + " " + timeStr;
				}
    			
    			
    			
    			if(df.parse(timeStr).getTime() - System.currentTimeMillis() > 120*1000){
    				continue;
    			}
    			

    			Vector<String[]> scoreDetails = StoneAge.score.getFinalScoresDetails();
    			
    			if(scoreDetails== null ||scoreDetails.size() == 0){
    				return;
    			}
    			
    			
    			
    			String[] olditem = eventDetailsVec.elementAt(i).clone();
				
				String zhibohometeam = olditem[TYPEINDEX.EVENTNAMNE.ordinal()].split(" vs ")[0];
				String zhiboawayteam = olditem[TYPEINDEX.EVENTNAMNE.ordinal()].split(" vs ")[1];
				
				String[] item = {Integer.toString(i+1), olditem[TYPEINDEX.LEAGUENAME.ordinal()], olditem[TYPEINDEX.TIME.ordinal()], olditem[TYPEINDEX.EVENTNAMNE.ordinal()], "", "", "",
						olditem[TYPEINDEX.PERIOD0HOME.ordinal()], "", "", "", olditem[TYPEINDEX.PERIOD0OVER.ordinal()], ""};
				
				String scorehometeam = MergeManager.findScoreTeambyzhiboteam(zhibohometeam);
				if(scorehometeam != null){
					
					String scoreawayteam = MergeManager.findScoreTeambyzhiboteam(zhiboawayteam);

					if(scoreawayteam != null){
						
						int indexinscoredetails = -1;
						for(int j = 0; j < scoreDetails.size(); j++){
							if(scoreDetails.elementAt(j)[SCORENEWINDEX.EVENTNAMNE.ordinal()].equals(scorehometeam + " vs " + scoreawayteam) ){
								indexinscoredetails = j;
								break;
							}
						}
						
						if(indexinscoredetails != -1){
							
							item[ZHIBOTABLEHEADINDEX.RQCHUPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQCHUPAN.ordinal()];
							item[ZHIBOTABLEHEADINDEX.RQZHONGPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQZHONGPAN.ordinal()];
							item[ZHIBOTABLEHEADINDEX.RQPANAS.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQPANAS.ordinal()];
							item[ZHIBOTABLEHEADINDEX.DXQCHUPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQCHUPAN.ordinal()];
							item[ZHIBOTABLEHEADINDEX.DXQZHONGPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQZHONGPAN.ordinal()];
							item[ZHIBOTABLEHEADINDEX.DXQPANAS.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQPANAS.ordinal()];
							item[ZHIBOTABLEHEADINDEX.SCORE.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.SCORE.ordinal()];
							
							
							//大小球盘结果分析
							if(!item[ZHIBOTABLEHEADINDEX.DXQZHONGPAN.ordinal()].equals("") && !item[ZHIBOTABLEHEADINDEX.DXQZHONGPAN.ordinal()].equals("-")
									&& !item[ZHIBOTABLEHEADINDEX.DXQPANAS.ordinal()].equals("")){
								
								
								
								
								int p0over = 0;
								if(item[ZHIBOTABLEHEADINDEX.P0OVER.ordinal()].contains("=")){
									p0over = Integer.parseInt(item[ZHIBOTABLEHEADINDEX.P0OVER.ordinal()].split("=")[1]);
								}else{
									p0over = Integer.parseInt(item[ZHIBOTABLEHEADINDEX.P0OVER.ordinal()]);
								}
								
								if(Math.abs(p0over) < 400000){
									continue;
								}
								

								String bet = "";
								
								//只统计赌降的一边
								if(item[ZHIBOTABLEHEADINDEX.DXQPANAS.ordinal()].contains("降") && 
										p0over  < 0){
									bet = "大";
								}else if(item[ZHIBOTABLEHEADINDEX.DXQPANAS.ordinal()].contains("升") && 
										p0over  > 0){
									bet = "小";
								}

								String sendmark = item[ZHIBOTABLEHEADINDEX.EVENTNAME.ordinal()] + "大";
								
								
								if(bet.equals("大") && !sendpankoualready.contains(sendmark)){
								//if(!sendpankoualready.contains(sendmark)){
									
									String sendTitle = "LL " + "【" + "大" + "】";
					    			String sendContent = "联赛:" + item[ZHIBOTABLEHEADINDEX.LEAGUE.ordinal()] + "<br>" +
					    								 "球队:" + item[ZHIBOTABLEHEADINDEX.EVENTNAME.ordinal()] + "<br>" +
					    								 "时间:" + item[ZHIBOTABLEHEADINDEX.TIME.ordinal()] + "<br>" +
					    								 "初盘:" + item[ZHIBOTABLEHEADINDEX.DXQCHUPAN.ordinal()] + "<br>" +
					    								 "终盘:" + item[ZHIBOTABLEHEADINDEX.DXQZHONGPAN.ordinal()] + "<br>" +
					    								 "分析:" + item[ZHIBOTABLEHEADINDEX.DXQPANAS.ordinal()] + "<br>" +
					    								 "金额:" + item[ZHIBOTABLEHEADINDEX.P0OVER.ordinal()];
									
									
									Vector<String> mails = StoneAge.getPankouMailList();
									
									for(int k = 0, b = 0; k < mails.size()&& b < 50; b++){
										String mail = mails.elementAt(k);
									//	if(true == MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", mail, sendTitle, sendContent)){
										if(true == MailManager.sendMail("240749322@qq.com", "240749322", "beaqekgmzscocbab", mail, sendTitle, sendContent)){
											k++;
										}else{
											Thread.currentThread().sleep(2000);
										}
										
									}
									
									sendpankoualready.add(sendmark);
								}
								
							}
							
							
							
						}
							
							
							
							
							//给出赌哪边的提示
    			
    			
					}
    			
				}
    			
    		}
    		

    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	

    }
    
    
    
    
    public static  void sortEventDetails(){
    	
    	try{
    		
/*    		System.out.println("before sort");
    		
    		for(int k = 0; k<eventDetailsVec.size(); k++ ){
    			
    			String[] outRow = eventDetailsVec.elementAt(k);
    			
    			System.out.println(outRow[0] + "," +outRow[1] + "," + outRow[2] + "," +outRow[3] + "," +outRow[4] + "," +outRow[5] + "," +
    					outRow[6] + "," + outRow[7]);
    		}*/
    		
    		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); 

    		
    		
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
    		
    		String currentTime = df.format(System.currentTimeMillis());
    		String todayStr = currentTime.split(" ")[0];
    		
    		for(int i = 0; i < eventDetailsVec.size(); i++){
    			String timeStr = eventDetailsVec.elementAt(i)[ZHIBOINDEX.TIME.ordinal()];
    			if(!timeStr.contains("-")){
    				timeStr = todayStr + " " + timeStr;
    				timeStr = Long.toString(df.parse(timeStr).getTime());
    				eventDetailsVec.elementAt(i)[ZHIBOINDEX.TIME.ordinal()] = timeStr;
    			}else if(timeStr.contains("(")){
    				eventDetailsVec.elementAt(i)[ZHIBOINDEX.TIME.ordinal()] = timeStr;
    			}else{
    				timeStr = Long.toString(df.parse(timeStr).getTime());
    				eventDetailsVec.elementAt(i)[ZHIBOINDEX.TIME.ordinal()] = timeStr;
    			}
    		}
    		
        	if(eventDetailsVec.size() != 0){
        		
        		Vector<String[]> highShowVec = new Vector<String[]>();
        		
        		for(int i = 0; i < eventDetailsVec.size(); i++){
        			String leagueName = eventDetailsVec.elementAt(i)[TYPEINDEX.LEAGUENAME.ordinal()];
        			
        			if(isInShowLeagueName(leagueName)){        				
        				highShowVec.add(eventDetailsVec.elementAt(i));        				
        				
        			}
        				
        			
        			
        		}        
        		
        		for(int i = 0; i < eventDetailsVec.size(); ){
        			String leagueName = eventDetailsVec.elementAt(i)[TYPEINDEX.LEAGUENAME.ordinal()];
        			
        			if(isInShowLeagueName(leagueName)){        				
        				
        				eventDetailsVec.remove(i);        			
        			}
        			else{
        				i++;
        			}
        				
        			
        			
        		}   
        		
        		
/*        		System.out.println("after remove event details:");
        		
        		for(int k = 0; k<eventDetailsVec.size(); k++ ){
        			
        			String[] outRow = eventDetailsVec.elementAt(k);
        			
        			System.out.println(outRow[0] + "," +outRow[1] + "," + outRow[2] + "," +outRow[3] + "," +outRow[4] + "," +outRow[5] + "," +
        					outRow[6] + "," + outRow[7]);
        		}
        		
        		
        		System.out.println("after remove highshow details:");
        		
        		for(int k = 0; k<highShowVec.size(); k++ ){
        			
        			String[] outRow = highShowVec.elementAt(k);
        			
        			System.out.println(outRow[0] + "," +outRow[1] + "," + outRow[2] + "," +outRow[3] + "," +outRow[4] + "," +outRow[5] + "," +
        					outRow[6] + "," + outRow[7]);
        		}
        		
        		System.out.println("------------------------");*/
        		
        		
            	Comparator ct = new MyCompare();  
            	
            	Comparator cn = new EventNameCompare(); 
            	

            	
            	
            	if(eventDetailsVec.size() != 0){
            		
            		Collections.sort(eventDetailsVec, cn);
            		
            		Collections.sort(eventDetailsVec, ct);
            	}
            	
        		
            	if(highShowVec.size() != 0){
            		
            		Collections.sort(highShowVec, cn);
            		
            		Collections.sort(highShowVec, ct);
            		
/*            		System.out.println("after sort event details:");
            		
            		for(int k = 0; k<eventDetailsVec.size(); k++ ){
            			
            			String[] outRow = eventDetailsVec.elementAt(k);
            			
            			System.out.println(outRow[0] + "," +outRow[1] + "," + outRow[2] + "," +outRow[3] + "," +outRow[4] + "," +outRow[5] + "," +
            					outRow[6] + "," + outRow[7]);
            		}
            		
            		
            		System.out.println("after sort highshow details:");
            		
            		for(int k = 0; k<highShowVec.size(); k++ ){
            			
            			String[] outRow = highShowVec.elementAt(k);
            			
            			System.out.println(outRow[0] + "," +outRow[1] + "," + outRow[2] + "," +outRow[3] + "," +outRow[4] + "," +outRow[5] + "," +
            					outRow[6] + "," + outRow[7]);
            		}*/
            		
            		for(int k = 0; k < highShowVec.size(); k++){
            			//eventDetailsVec.add( highShowVec.elementAt(k));
            			eventDetailsVec.insertElementAt(highShowVec.elementAt(k), k);
            			//highShowVec.remove(k);
            		}
            	}
            	

            	
            	
            	for(int i = 0; i < eventDetailsVec.size(); i++){
            		
            		if(eventDetailsVec.elementAt(i)[TYPEINDEX.TIME.ordinal()].contains("(")){
            			
            		}else{
            			String currentTimeArray[] = currentTime.split(" ");
            			
            			long time = Long.parseLong(eventDetailsVec.elementAt(i)[TYPEINDEX.TIME.ordinal()]);
            			
            			String eventTimeArray[] = df.format(time).split(" ");
            			
            			String timeStr = "";
            			
            			if(currentTimeArray[0].contains(eventTimeArray[0])){
            				timeStr = eventTimeArray[1];
            			}else{
            				timeStr = df.format(time);
            			}
            			
            			
            			eventDetailsVec.elementAt(i)[TYPEINDEX.TIME.ordinal()] = timeStr;
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
				
				if(timeStr.contains("(")){
					
					String[] item = eventDetailsVec.elementAt(i).clone();
					item[ZHIBOINDEX.TIME.ordinal()] = timeStr;
					//boolean res = pDataManager.saveTofile(item);	
					
					
					
					//System.out.println("(0 - 0)zhibo save success:" + Arrays.toString(item));
					
					boolean sendMail = false;
					
					String eventName = item[ZHIBOINDEX.EVENTNAMNE.ordinal()];
					String leagueName = item[ZHIBOINDEX.LEAGUENAME.ordinal()];
	    			double p0h = Double.parseDouble(item[ZHIBOINDEX.PERIOD0HOME.ordinal()]);
	    			double p0o = Double.parseDouble(item[ZHIBOINDEX.PERIOD0OVER.ordinal()]);
	    			
	    			String sendTitle = "LL " + "【" + leagueName + "】" +  eventName + " " + timeStr;
	    			String sendContent = "";
					
/*						if(Math.abs(p0h) >= Zhibop0hSendNumber){
						sendMail = true;
						sendContent = "全场让球: " + String.format("%.0f\n", p0h);
					}*/
					
					if(Math.abs(p0o) >= Zhibop0oSendNumber){
						sendMail = true;
						sendContent += "全场大小: " + String.format("%.0f\n", p0o);
					}
					
					boolean sendalready = false;
					for(int j = 0; j < alreadSendmailEvents.size(); j++){
						if(alreadSendmailEvents.elementAt(j).equals(item[ZHIBOINDEX.EVENTNAMNE.ordinal()])){
							sendalready = true;
						}
					}
					
					
					if(sendMail == true && false == sendalready){
						
						Vector<String> mails = StoneAge.getMailList();
						
						for(int k = 0; k < mails.size(); k++){
							String mail = mails.elementAt(k);
							//MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", mail, sendTitle, sendContent);
							
							MailManager.sendMail("240749322@qq.com", "240749322", "beaqekgmzscocbab", mail, sendTitle, sendContent);
							Thread.currentThread().sleep(1000);
						}
					
						alreadSendmailEvents.add(item[ZHIBOINDEX.EVENTNAMNE.ordinal()]);
						
						
					}
					
					eventDetailsVec.elementAt(i)[ZHIBOINDEX.SAVED.ordinal()] = "1";	
					
					continue;
				}
				
				if(!timeStr.contains("-")){
					timeStr = todayStr + " " + timeStr;
				}
				
				long currentTime = System.currentTimeMillis();
				
				long passMinutes = 120*60*1000;
				
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
						
						boolean sendMail = false;
						
						String eventName = item[ZHIBOINDEX.EVENTNAMNE.ordinal()];
						String leagueName = item[ZHIBOINDEX.LEAGUENAME.ordinal()];
		    			double p0h = Double.parseDouble(item[ZHIBOINDEX.PERIOD0HOME.ordinal()]);
		    			double p0o = Double.parseDouble(item[ZHIBOINDEX.PERIOD0OVER.ordinal()]);
		    			
		    			String sendTitle = "LL " + "【" + leagueName + "】" +   eventName + " " + timeStr;
		    			String sendContent = "";
						
/*						if(Math.abs(p0h) >= Zhibop0hSendNumber){
							sendMail = true;
							sendContent = "全场让球: " + String.format("%.0f\n", p0h);
						}*/
						
						if(Math.abs(p0o) >= Zhibop0oSendNumber){
							sendMail = true;
							sendContent += "全场大小: " + String.format("%.0f\n", p0o);
						}
						
						if(sendMail == true){
							
							Vector<String> mails = StoneAge.getMailList();
							
							for(int k = 0; k < mails.size(); k++){
								String mail = mails.elementAt(k);
//								MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", mail, sendTitle, sendContent);
								MailManager.sendMail("240749322@qq.com", "240749322", "beaqekgmzscocbab", mail, sendTitle, sendContent);
								Thread.currentThread().sleep(1000);
							}
						
							
						}
						
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
