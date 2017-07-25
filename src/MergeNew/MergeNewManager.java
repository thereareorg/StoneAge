package MergeNew;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import team.gl.nio.cln.ZhiboClientHandler;
import HG.HGINDEX;
import HG.HGMergeManager;
import HG.HGhttp;
import P8.GrabEventsThread;
import P8.MergeManager;
import P8.P8Http;
import P8.SCOREINDEX;
import P8.ScoreMergeManager;
import P8.StoneAge;
import P8.TYPEINDEX;
import P8.ZHIBOINDEX;
import P8.ZhiboManager;

public class MergeNewManager {

    static MergeNewPreviousDataWindow pDataWindow = new MergeNewPreviousDataWindow();
    
    static MergeNewPreviousDataManager pDataManager = new MergeNewPreviousDataManager(pDataWindow);
    
    public static Vector<String[]> mergeEventDetailsVec = new Vector<String[]>();
    
    public static MergeNewDetailsWindow mergeDetailsWnd = new MergeNewDetailsWindow();
    
    
	private static ReadWriteLock lockeFinalMergeEventsDetails = new ReentrantReadWriteLock();
	
	
	public static Vector<String[]> finalMergeEventDetailsVec = new Vector<String[]>();

	
	public static void showMergeDetailsWnd(boolean flag){
		mergeDetailsWnd.setVisible(flag);
	}
	
	
	public static void clearMergeData(){
		if(mergeEventDetailsVec.size() != 0){
			mergeEventDetailsVec.clear();
		}
	}
	
	
	public static void showpDataWnd(){
		pDataWindow.setVisible(true);
	}
    
	
	public static void updatepDataDetails(){
		pDataManager.updatepEventsDetails();
	}
	
	public static void init(){
		try{
			pDataManager.init();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
    public static Vector<String[]> getpSubMergeevents(){
    	return pDataManager.getpSubevents();
    }
    
    
    
    
    
    public static void copyTofinalEventsDetails(){
    	

    	
		lockeFinalMergeEventsDetails.writeLock().lock();
		finalMergeEventDetailsVec = (Vector<String[]>)mergeEventDetailsVec.clone();
    	
    	lockeFinalMergeEventsDetails.writeLock().unlock();
    	

    }
    
    public static Vector<String[]> getFinalEventsDetails(){
    	Vector<String[]> vec = null;
    	lockeFinalMergeEventsDetails.readLock().lock();
    	vec = (Vector<String[]>)finalMergeEventDetailsVec.clone();
    	lockeFinalMergeEventsDetails.readLock().unlock();
    	return vec;
    }

    
    
    
    
	
	
    public static void saveEvents(){
		try{
			
			
			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
			
			SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			
			long currentTimeL = System.currentTimeMillis();
			
			String todayStr = dfDay.format(currentTimeL);
			
			//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			for(int i = 0; i < mergeEventDetailsVec.size(); i++){
				//long eventTime = Long.parseLong(eventDetailsVec.elementAt(i)[ZHIBOINDEX.TIME.ordinal()]);
				
				String timeStr = mergeEventDetailsVec.elementAt(i)[ZHIBOINDEX.TIME.ordinal()];
				
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

				String inplaytimestr = mergeEventDetailsVec.elementAt(i)[NEWMERGEINDEX.INPLAYTIME.ordinal()];
				
				if(null != inplaytimestr && (inplaytimestr.contains("'") || inplaytimestr.contains("中"))){
					continue;
				}
			
				if(pass > passMinutes){					
					String[] item = mergeEventDetailsVec.elementAt(i).clone();
					item[ZHIBOINDEX.TIME.ordinal()] = timeStr;
					boolean saveRes = pDataManager.saveTofile(item);	
					
					
					
					if(saveRes == true){
						System.out.println("game over merge save success:" + Arrays.toString(item));
						
						
						
					}
					
					
					
				}
				
				
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	
	public static void updateEventsDetails(){
		
		try{
				for(int i = 0; i < mergeEventDetailsVec.size(); i++){
					String inplaytime = mergeEventDetailsVec.elementAt(i)[NEWMERGEINDEX.INPLAYTIME.ordinal()];
					if(inplaytime != null&& inplaytime.contains("'")){
						inplaytime = inplaytime.replace("'", "");
						inplaytime = inplaytime.replace("+", "");
						int tmp = Integer.parseInt(inplaytime);
						if(tmp < 89){
							mergeEventDetailsVec.elementAt(i)[NEWMERGEINDEX.TIME.ordinal()] = mergeEventDetailsVec.elementAt(i)[NEWMERGEINDEX.INPLAYTIME.ordinal()];
						}else{
							
							SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
							
							
							long currentTimeL = System.currentTimeMillis();
							
							String todayStr = dfDay.format(currentTimeL);
							
							
							String[] item = mergeEventDetailsVec.elementAt(i);
							
							String timeStr = item[NEWMERGEINDEX.TIME.ordinal()];
							
							if(!timeStr.contains("-")){
								timeStr = todayStr + " " + timeStr;
							}
							
							
							item[NEWMERGEINDEX.TIME.ordinal()] = timeStr;
							
							boolean saveRes = pDataManager.saveTofile(item);	
							if(saveRes == true){
								System.out.println("score merge already: game over merge save success:" + Arrays.toString(item));
								
								
								
							}
							
							mergeEventDetailsVec.remove(i);
							i--;
						}
					}else if(inplaytime != null && inplaytime.contains("中")){
						mergeEventDetailsVec.elementAt(i)[NEWMERGEINDEX.TIME.ordinal()] = mergeEventDetailsVec.elementAt(i)[NEWMERGEINDEX.INPLAYTIME.ordinal()];
					}
				}
				
				
				
				
				
				mergeDetailsWnd.updateEventsDetails(mergeEventDetailsVec);
				
				SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				mergeDetailsWnd.setStateText("数据更新于: " + dfMin.format(System.currentTimeMillis()));
			//}
			
			if(GrabEventsThread.grabStat && ZhiboClientHandler.grabStat){
				mergeDetailsWnd.setStateColor(Color.GREEN);
			}else{
				mergeDetailsWnd.setStateColor(Color.RED);

			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
    
    
	public static void constructMergeRes(){
		
		try{
			
			Vector<String[]> p8events = (Vector<String[]>)P8Http.getFinalEventsDetails().clone();
			Vector<String[]> zhiboevents = ZhiboManager.getEventdetails();
			Vector<String[]> inPlayzhiboevents = ZhiboManager.getInplayEventsDetails();
			Vector<String[]> scoresDetails = StoneAge.score.getFinalScoresDetails();
			Vector<String[]> hgDetails = HGhttp.getFinalEventsDetails();
			
			String[] item = null;
			String p8eventsname = "";
			String[] p8teams = null;
			String[] zhiboitem = null;
			
			//先处理单式
			for(int i  = 0; i < p8events.size(); i++){
				
				item = p8events.elementAt(i).clone();
				
				p8eventsname = item[TYPEINDEX.EVENTNAMNE.ordinal()];
				
				if(!p8eventsname.contains("滚动盘")){
					p8teams = p8eventsname.split("-vs-");
					
					//寻找比分网配对
					
					String[] scoreItem = null;
					
					String scorehome = findScoreTeam(p8teams[0]);
					if(scorehome != null){
						String scoreaway = findScoreTeam(p8teams[1]);
						if(scoreaway != null){
							String scoreeventname = scorehome + " vs " + scoreaway;
							for(int si = 0; si < scoresDetails.size(); si++){
								if(scoresDetails.elementAt(si)[SCOREINDEX.EVENTNAMNE.ordinal()].equals(scoreeventname)){
									scoreItem = scoresDetails.elementAt(si).clone();
								}
							}
						}
					}
					//寻找比分网配对 结束
					
					boolean isZhibohasGoals = false;
					
					
					if(null != scoreItem && !scoreItem[SCOREINDEX.SCORE.ordinal()].equals("0:0")){
						isZhibohasGoals = true;
					}
					
					
					String zhiboHome = MergeManager.findZhiboTeam(p8teams[0]);
					
					if(zhiboHome != null){
						String zhiboAway = MergeManager.findZhiboTeam(p8teams[1]);
						
						if(zhiboAway != null){
							String zhiboeventname = zhiboHome + " vs " + zhiboAway;
							
							item[ZHIBOINDEX.EVENTNAMNE.ordinal()] = zhiboeventname;
							
							
							
							String[] saveItem = pDataManager.findLatestEvents(zhiboeventname);
							
							
							
							
							if(saveItem != null){
								
								SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
								String dayStr = dfDay.format(System.currentTimeMillis());
								
								if(saveItem[NEWMERGEINDEX.TIME.ordinal()].contains(dayStr)){
									saveItem[NEWMERGEINDEX.TIME.ordinal()] = saveItem[NEWMERGEINDEX.TIME.ordinal()].replace(dayStr + " ", "");
								}
								
								for(int j = 0; j < mergeEventDetailsVec.size(); j++){
									if(mergeEventDetailsVec.elementAt(j)[NEWMERGEINDEX.EVENTNAMNE.ordinal()].equals(saveItem[NEWMERGEINDEX.EVENTNAMNE.ordinal()])){
										mergeEventDetailsVec.remove(j);
										break;
									}
								}
								
								
/*								if(scoreItem != null){
									saveItem[NEWMERGEINDEX.LEAGUENAME.ordinal()] = scoreItem[SCOREINDEX.LEAGUENAME.ordinal()];
									saveItem[NEWMERGEINDEX.SCORE.ordinal()] = scoreItem[SCOREINDEX.SCORE.ordinal()];
									saveItem[NEWMERGEINDEX.RQPK.ordinal()] = scoreItem[SCOREINDEX.RQPANKOU.ordinal()];
									saveItem[NEWMERGEINDEX.DXQPK.ordinal()] = scoreItem[SCOREINDEX.DXQPANKOU.ordinal()];
									
								}*/
								
								
								
								//处理比赛结束时显示错误的比赛时间 开始
								String inplaytimestr = saveItem[NEWMERGEINDEX.INPLAYTIME.ordinal()];
								
								if(inplaytimestr != null && (inplaytimestr.contains("'") || inplaytimestr.contains("中"))){
									if(scoreItem == null){
										continue;
									}
								}
								//处理比赛结束时显示错误的比赛时间 结束
								
								
								
								mergeEventDetailsVec.add(saveItem);
								
								continue;
							}
							
							
							
							
							
							for(int j = 0; j< zhiboevents.size(); j++){
								zhiboitem = zhiboevents.elementAt(j);
								
								if(zhiboitem[ZHIBOINDEX.EVENTNAMNE.ordinal()].contains(zhiboeventname)
										&&!zhiboitem[ZHIBOINDEX.TIME.ordinal()].contains("(")){
									
									boolean addTomerge = false;
									
									Double p80home = 0.0;
									Double p80over = 0.0;
									
									
									String p80homeStr = item[TYPEINDEX.PERIOD0HOME.ordinal()];
									String p80overStr = item[TYPEINDEX.PERIOD0OVER.ordinal()];
									
									if(p80homeStr.contains("=")){
										String[] tmp = p80homeStr.split("=");
										p80home = Double.parseDouble(tmp[1]);
									}
									
									if(p80overStr.contains("=")){
										String[] tmp = p80overStr.split("=");
										p80over = Double.parseDouble(tmp[1]);
									}

									
									Double zhibo0home = Double.parseDouble(zhiboitem[ZHIBOINDEX.PERIOD0HOME.ordinal()]);
									Double zhibo0over = Double.parseDouble(zhiboitem[ZHIBOINDEX.PERIOD0OVER.ordinal()]);

									

										
										
										if(isZhibohasGoals == true){
											item[ZHIBOINDEX.SAVED.ordinal()] = "1";
											System.out.println("merge has goals" + Arrays.toString(item));
										}
										
										//merge格式有变化，需要重新组织
										String[] mergeItem = new String[NEWMERGEINDEX.SIZE.ordinal()];
										
										for(int kk = 0; kk < mergeItem.length; kk++){
											mergeItem[kk] = "";
										}
										
										mergeItem[NEWMERGEINDEX.EVENTID.ordinal()] = 		item[TYPEINDEX.EVENTID.ordinal()];
										mergeItem[NEWMERGEINDEX.LEAGUENAME.ordinal()] = 	item[TYPEINDEX.LEAGUENAME.ordinal()];
										mergeItem[NEWMERGEINDEX.TIME.ordinal()] = 			item[TYPEINDEX.TIME.ordinal()];
										mergeItem[NEWMERGEINDEX.EVENTNAMNE.ordinal()] = 	item[TYPEINDEX.EVENTNAMNE.ordinal()];
										mergeItem[NEWMERGEINDEX.SCORE.ordinal()] = 		"-";
										
										
										
										mergeItem[NEWMERGEINDEX.P8HRES.ordinal()] = 		p80homeStr;
										mergeItem[NEWMERGEINDEX.INP8HRES.ordinal()] = 		"0";
										mergeItem[NEWMERGEINDEX.ZHIBOHRES.ordinal()] = 	zhiboitem[ZHIBOINDEX.PERIOD0HOME.ordinal()];
										
										
										

										
										mergeItem[NEWMERGEINDEX.P8ORES.ordinal()] = 		p80overStr;
										mergeItem[NEWMERGEINDEX.INP8ORES.ordinal()] = 		"0";
										mergeItem[NEWMERGEINDEX.ZHIBOORES.ordinal()] = 		zhiboitem[ZHIBOINDEX.PERIOD0OVER.ordinal()];
										
										
										
										
										if(scoreItem != null){
											mergeItem[NEWMERGEINDEX.LEAGUENAME.ordinal()] = scoreItem[SCOREINDEX.LEAGUENAME.ordinal()];
											
											mergeItem[NEWMERGEINDEX.RQPK.ordinal()] = scoreItem[SCOREINDEX.RQPANKOU.ordinal()];
											mergeItem[NEWMERGEINDEX.RQSW.ordinal()] = scoreItem[SCOREINDEX.RQHOMESHUI.ordinal()] + "|" + scoreItem[SCOREINDEX.RQAWAYSHUI.ordinal()];
											mergeItem[NEWMERGEINDEX.DXQPK.ordinal()] = scoreItem[SCOREINDEX.DXQPANKOU.ordinal()];
											mergeItem[NEWMERGEINDEX.DXQSW.ordinal()] = scoreItem[SCOREINDEX.DXQHOMESHUI.ordinal()] + "|" + scoreItem[SCOREINDEX.DXQAWAYSHUI.ordinal()];
											
											mergeItem[NEWMERGEINDEX.INPLAYTIME.ordinal()] = scoreItem[SCOREINDEX.TIME.ordinal()];
											
											mergeItem[NEWMERGEINDEX.GQRQPK.ordinal()] = scoreItem[SCOREINDEX.GQRQPANKOU.ordinal()];
											mergeItem[NEWMERGEINDEX.GQRQSW.ordinal()] = scoreItem[SCOREINDEX.GQRQHOMESHUI.ordinal()] + "|" + scoreItem[SCOREINDEX.GQRQAWAYSHUI.ordinal()];
											mergeItem[NEWMERGEINDEX.GQDXQPK.ordinal()] = scoreItem[SCOREINDEX.GQDXQPANKOU.ordinal()];
											mergeItem[NEWMERGEINDEX.GQDXQSW.ordinal()] = scoreItem[SCOREINDEX.GQDXQHOMESHUI.ordinal()] + "|" + scoreItem[SCOREINDEX.GQDXQAWAYSHUI.ordinal()];

											
											mergeItem[NEWMERGEINDEX.SCORE.ordinal()] = scoreItem[SCOREINDEX.SCORE.ordinal()];
										}
										

										mergeEventDetailsVec.add(mergeItem);
									break;
									
								}
								
							}
							
						}
					}
				}
				
				

			}
			
			
			
			
			
			
			for(int i = 0; i < p8events.size(); i++){
				item = p8events.elementAt(i).clone();
				
				p8eventsname = item[TYPEINDEX.EVENTNAMNE.ordinal()];
				
				//处理滚动盘	开始
				
				if(p8eventsname.contains("滚动盘")){
					
				
					p8eventsname = p8eventsname.replace("【滚动盘】", "");
					
					boolean isZhibohasGoals = false;
					
					p8teams = p8eventsname.split("-vs-");
					
					
					//寻找比分网配对
					
					String[] scoreItem = null;
					
					String scorehome = findScoreTeam(p8teams[0]);
					if(scorehome != null){
						String scoreaway = findScoreTeam(p8teams[1]);
						if(scoreaway != null){
							String scoreeventname = scorehome + " vs " + scoreaway;
							for(int si = 0; si < scoresDetails.size(); si++){
								if(scoresDetails.elementAt(si)[SCOREINDEX.EVENTNAMNE.ordinal()].equals(scoreeventname)){
									scoreItem = scoresDetails.elementAt(si).clone();
								}
							}
						}
					}
					//寻找比分网配对 结束
					
					if(null != scoreItem && !scoreItem[SCOREINDEX.SCORE.ordinal()].equals("0:0")){
						isZhibohasGoals = true;
					}
					
					
					String zhiboHome = MergeManager.findZhiboTeam(p8teams[0]);
					
					if(zhiboHome != null){
						String zhiboAway = MergeManager.findZhiboTeam(p8teams[1]);
						
						if(zhiboAway != null){
							String zhiboeventname = zhiboHome + " vs " + zhiboAway;
							
							
							//zhiboeventname = "【滚动盘】"+ zhiboeventname;
							
							//找到单式盘
							int danshiIndex = -1;
							for(int j = 0; j < mergeEventDetailsVec.size(); j++){
								if(mergeEventDetailsVec.elementAt(j)[NEWMERGEINDEX.EVENTNAMNE.ordinal()].equals(zhiboeventname)){
									danshiIndex = j;
								}
							}
							
							item[ZHIBOINDEX.EVENTNAMNE.ordinal()] = zhiboeventname;
							
							
							String[] saveItem = pDataManager.findLatestEvents(zhiboeventname);
							
							
							
							
							if(saveItem != null){
								
								
								
								continue;
							}

							
							
							
							
							for(int j = 0; j< inPlayzhiboevents.size(); j++){
								String[] inplayItem = inPlayzhiboevents.elementAt(j).clone();
								if(inplayItem[ZHIBOINDEX.EVENTNAMNE.ordinal()].contains(zhiboeventname)){
									isZhibohasGoals = true;
									break;
								}
							}
							
							
							//String[] zhiboSaveItem = ZhiboManager.getZhiboSaveItem(zhiboeventname);
							
							String[] p8DanshiItem = null;
							
							for(int j = 0; j < p8events.size(); j++){
								String[] p8tmpItem = p8events.elementAt(j).clone();
								if(p8eventsname.contains(p8tmpItem[TYPEINDEX.EVENTNAMNE.ordinal()])){
									p8DanshiItem = p8tmpItem;
									break;
								}
							}
							
							if(p8DanshiItem == null){
								p8DanshiItem = P8Http.getP8SaveItem(p8eventsname);
							}
							
							
							
							//如果智博已经有进球
							//if(isZhibohasGoals == true){
							if(p8DanshiItem != null){
								for(int j = 0; j < zhiboevents.size(); j++){
									String[] zhiboItemNow = zhiboevents.elementAt(j).clone();
									
									if(zhiboItemNow[ZHIBOINDEX.EVENTNAMNE.ordinal()].contains(zhiboeventname)){

										
										Double zhibo0homenow = Double.parseDouble(zhiboItemNow[ZHIBOINDEX.PERIOD0HOME.ordinal()]);
										Double zhibo0overnow = Double.parseDouble(zhiboItemNow[ZHIBOINDEX.PERIOD0OVER.ordinal()]);

										
										
										Double p8danshibet1 = 0.0;
										Double p8danshibet2 = 0.0;
										
										
										String p8danshibet1Str = p8DanshiItem[ZHIBOINDEX.PERIOD0HOME.ordinal()];
										String p8danshibet2Str = p8DanshiItem[ZHIBOINDEX.PERIOD0OVER.ordinal()];
										
										if(p8danshibet1Str.contains("=")){
											String[] tmp = p8danshibet1Str.split("=");
											p8danshibet1 = Double.parseDouble(tmp[1]);
										}
										
										if(p8danshibet2Str.contains("=")){
											String[] tmp = p8danshibet2Str.split("=");
											p8danshibet2 = Double.parseDouble(tmp[1]);
										}

										
										Double p80homeInplayVal = 0.0;
										Double p80overInplayVal = 0.0;
										
										
										String p80homeInplayValStr = item[TYPEINDEX.PERIOD0HOME.ordinal()];
										String p80overInplayValStr = item[TYPEINDEX.PERIOD0OVER.ordinal()];
										
										if(p80homeInplayValStr.contains("=")){
											String[] tmp = p80homeInplayValStr.split("=");
											p80homeInplayVal = Double.parseDouble(tmp[1]);
										}
										
										if(p80overInplayValStr.contains("=")){
											String[] tmp = p80overInplayValStr.split("=");
											p80overInplayVal = Double.parseDouble(tmp[1]);
										}


											
											
											System.out.println("merge inplay add" + Arrays.toString(item));
											
											
											
											//merge格式有变化，需要重新组织
											String[] mergeItem = new String[NEWMERGEINDEX.SIZE.ordinal()];
											
											for(int kk = 0; kk < mergeItem.length; kk++){
												mergeItem[kk] = "";
											}
											
											mergeItem[NEWMERGEINDEX.EVENTID.ordinal()] = 		item[ZHIBOINDEX.SAVED.ordinal()];
											mergeItem[NEWMERGEINDEX.LEAGUENAME.ordinal()] = 	item[TYPEINDEX.LEAGUENAME.ordinal()];
											mergeItem[NEWMERGEINDEX.TIME.ordinal()] = 			item[TYPEINDEX.TIME.ordinal()];
											mergeItem[NEWMERGEINDEX.EVENTNAMNE.ordinal()] = 	item[TYPEINDEX.EVENTNAMNE.ordinal()];
											mergeItem[NEWMERGEINDEX.SCORE.ordinal()] = 		"-";
											

											
											mergeItem[NEWMERGEINDEX.P8HRES.ordinal()] = 		p8danshibet1Str;
											mergeItem[NEWMERGEINDEX.INP8HRES.ordinal()] = 		p80homeInplayValStr;
											mergeItem[NEWMERGEINDEX.ZHIBOHRES.ordinal()] = 	String.format("%.0f", zhibo0homenow);
											
										
										


											mergeItem[NEWMERGEINDEX.P8ORES.ordinal()] = 		p8danshibet2Str;
											mergeItem[NEWMERGEINDEX.INP8ORES.ordinal()] = 		p80overInplayValStr;
											mergeItem[NEWMERGEINDEX.ZHIBOORES.ordinal()] = 		String.format("%.0f", zhibo0overnow);
											
											
											
											
											if(scoreItem != null){
												mergeItem[NEWMERGEINDEX.LEAGUENAME.ordinal()] = scoreItem[SCOREINDEX.LEAGUENAME.ordinal()];
												mergeItem[NEWMERGEINDEX.RQPK.ordinal()] = scoreItem[SCOREINDEX.RQPANKOU.ordinal()];
												mergeItem[NEWMERGEINDEX.RQSW.ordinal()] = scoreItem[SCOREINDEX.RQHOMESHUI.ordinal()] + "|" + scoreItem[SCOREINDEX.RQAWAYSHUI.ordinal()];
												mergeItem[NEWMERGEINDEX.DXQPK.ordinal()] = scoreItem[SCOREINDEX.DXQPANKOU.ordinal()];
												mergeItem[NEWMERGEINDEX.DXQSW.ordinal()] = scoreItem[SCOREINDEX.DXQHOMESHUI.ordinal()] + "|" + scoreItem[SCOREINDEX.DXQAWAYSHUI.ordinal()];
												
												mergeItem[NEWMERGEINDEX.INPLAYTIME.ordinal()] = scoreItem[SCOREINDEX.TIME.ordinal()];
												
												mergeItem[NEWMERGEINDEX.GQRQPK.ordinal()] = scoreItem[SCOREINDEX.GQRQPANKOU.ordinal()];
												mergeItem[NEWMERGEINDEX.GQRQSW.ordinal()] = scoreItem[SCOREINDEX.GQRQHOMESHUI.ordinal()] + "|" + scoreItem[SCOREINDEX.GQRQAWAYSHUI.ordinal()];
												mergeItem[NEWMERGEINDEX.GQDXQPK.ordinal()] = scoreItem[SCOREINDEX.GQDXQPANKOU.ordinal()];
												mergeItem[NEWMERGEINDEX.GQDXQSW.ordinal()] = scoreItem[SCOREINDEX.GQDXQHOMESHUI.ordinal()] + "|" + scoreItem[SCOREINDEX.GQDXQAWAYSHUI.ordinal()];

												
												mergeItem[NEWMERGEINDEX.SCORE.ordinal()] = scoreItem[SCOREINDEX.SCORE.ordinal()];
											}
											
											
											
											
											if(danshiIndex == -1){
												mergeEventDetailsVec.add(mergeItem);
											}else{

												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.EVENTID.ordinal()] = mergeItem[NEWMERGEINDEX.EVENTID.ordinal()];
												
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.P8HRES.ordinal()] = mergeItem[NEWMERGEINDEX.P8HRES.ordinal()];
												
												
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.INP8HRES.ordinal()] = mergeItem[NEWMERGEINDEX.INP8HRES.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.ZHIBOHRES.ordinal()] = mergeItem[NEWMERGEINDEX.ZHIBOHRES.ordinal()];

												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.P8ORES.ordinal()] = mergeItem[NEWMERGEINDEX.P8ORES.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.INP8ORES.ordinal()] = mergeItem[NEWMERGEINDEX.INP8ORES.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.ZHIBOORES.ordinal()] = mergeItem[NEWMERGEINDEX.ZHIBOORES.ordinal()];
												
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.LEAGUENAME.ordinal()] = mergeItem[NEWMERGEINDEX.LEAGUENAME.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.RQPK.ordinal()] = mergeItem[NEWMERGEINDEX.RQPK.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.RQSW.ordinal()] = mergeItem[NEWMERGEINDEX.RQSW.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.DXQPK.ordinal()] = mergeItem[NEWMERGEINDEX.DXQPK.ordinal()];
 												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.DXQSW.ordinal()] = mergeItem[NEWMERGEINDEX.DXQSW.ordinal()];
												
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.INPLAYTIME.ordinal()] = mergeItem[NEWMERGEINDEX.INPLAYTIME.ordinal()];
												
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.GQRQPK.ordinal()] = mergeItem[NEWMERGEINDEX.GQRQPK.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.GQRQSW.ordinal()] = mergeItem[NEWMERGEINDEX.GQRQSW.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.GQDXQPK.ordinal()] = mergeItem[NEWMERGEINDEX.GQDXQPK.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.GQDXQSW.ordinal()] = mergeItem[NEWMERGEINDEX.GQDXQSW.ordinal()];

												
												mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.SCORE.ordinal()] = mergeItem[NEWMERGEINDEX.SCORE.ordinal()];
												
												
												
												
												
												
											}


											
											
										

										
										break;
										
									}
									
								}
							}
							
						
						}
					
					
						
					}
					
					continue;
				}
				
				//处理滚动盘	结束
				


				
			}
			
			
			
			
			
			
			
			//处理皇冠
			
			for(int i = 0; i < mergeEventDetailsVec.size(); i++){
				mergeEventDetailsVec.elementAt(i)[NEWMERGEINDEX.HGHRES.ordinal()] = "0";
				mergeEventDetailsVec.elementAt(i)[NEWMERGEINDEX.HGORES.ordinal()] = "0";
			}
			
			
			
			
			for(int i = 0; i < p8events.size(); i++){
				item = p8events.elementAt(i).clone();
				
				p8eventsname = item[TYPEINDEX.EVENTNAMNE.ordinal()];
				
				
				
				if(!p8eventsname.contains("滚动盘")){
					
				
					
					
					
					
					p8teams = p8eventsname.split("-vs-");

					String zhiboHome = MergeManager.findZhiboTeam(p8teams[0]);
					
					if(zhiboHome != null){
						String zhiboAway = MergeManager.findZhiboTeam(p8teams[1]);
						
						if(zhiboAway != null){
							String zhiboeventname = zhiboHome + " vs " + zhiboAway;
							
							
							
							
							//找到合并
							int danshiIndex = -1;
							for(int j = 0; j < mergeEventDetailsVec.size(); j++){
								if(mergeEventDetailsVec.elementAt(j)[NEWMERGEINDEX.EVENTNAMNE.ordinal()].equals(zhiboeventname)){
									danshiIndex = j;
								}
							}
							
							if(danshiIndex == -1){
								continue;
							}
							
							
							String[] saveItem = pDataManager.findLatestEvents(zhiboeventname);
							
							

							
							if(saveItem != null){
								

								
								continue;
							}

							
							
							
							String hghome = findHGTeam(p8teams[0]);
							
							if(hghome != null){
								String hgaway = findHGTeam(p8teams[1]);
								
								if(hgaway != null){
									
									String hgeventname = hghome + "-vs-" + hgaway;
									
									
									int findhg = -1;
									for(int j = 0; j < hgDetails.size(); j++){
										if(hgDetails.elementAt(j)[HGINDEX.EVENTNAMNE.ordinal()].equals(hgeventname)){
											findhg = j;
											break;
										}
									}
									
									
									
									if(findhg == -1){
										mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.HGHRES.ordinal()] = "0";
										mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.HGORES.ordinal()] = "0";
									}else{
										
										String p0hstr = hgDetails.elementAt(findhg)[HGINDEX.PERIOD0HOME.ordinal()];
										
										mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.HGHRES.ordinal()] = p0hstr;
										
										String p0ostr = hgDetails.elementAt(findhg)[HGINDEX.PERIOD0OVER.ordinal()];
										
										mergeEventDetailsVec.elementAt(danshiIndex)[NEWMERGEINDEX.HGORES.ordinal()] = p0ostr;
									}
									
									
								}
								
							}
							
							
							

					
					
						
					}
					
					continue;
				}
				
			
				}


				
			}
			//处理皇冠   结束
			
			
			
			
			System.out.println("新合并注单:");
			for(int i = 0; i < mergeEventDetailsVec.size(); i++){
				System.out.println(Arrays.toString(mergeEventDetailsVec.elementAt(i)));
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	
	
	
	
	private static String findScoreTeam(String p8team){
		
		String scoreteam = ScoreMergeManager.findScoreTeam(p8team);
		
		return scoreteam;
		
	}
	
	private static String findHGTeam(String p8team){
		
		String hgteam = HGMergeManager.findHGTeam(p8team);
		
		return hgteam;
		
	}

	
}
