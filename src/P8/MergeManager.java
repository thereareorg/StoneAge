package P8;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import Mail.MailManager;

public class MergeManager {
	
	static BufferedWriter fw = null;  //写	
	static BufferedReader reader = null; //读
	
	public static Map<String,String> checkList = new HashMap<String,String>();
	
	private static ReadWriteLock lockeFinalMergeEventsDetails = new ReentrantReadWriteLock();
	
	
	public static Vector<String[]> finalMergeEventDetailsVec = new Vector<String[]>();

	public static Vector<String[]> mergeEventDetailsVec = new Vector<String[]>();
	
	public static MergeDetailsWindow mergeDetailsWnd = new MergeDetailsWindow();
	
	
	
	public static int mergeHideNumber = 5000;
	public static int mergeSendNumber = 10000;
	
	static Map<String, Vector<Integer>> mailRecords = new HashMap<String, Vector<Integer>>();  
	
	public static String[] zhiboSelectedRow = null;
	public static String[] p8SelectedRow = null;
	
	public static MergeWindow mergeWnd = new MergeWindow();
	
	public static Vector<String> notAddtomerge = new Vector<String>(); 
	
	
	
	
    static MergePreviousDataWindow pDataWindow = new MergePreviousDataWindow();
    
    static MergePreviousDataManager pDataManager = new MergePreviousDataManager(pDataWindow);
	
    
    
	public static void showpDataWnd(){
		pDataWindow.setVisible(true);
	}
    
	
	public static void updatepDataDetails(){
		pDataManager.updatepEventsDetails();
	}
    


    
    
	
	
	public static void showMergeWnd(boolean flag){
		
		setP8Txt();
		setZhiboTxt();
		
		mergeWnd.setVisible(flag);
	}
	
	public static void showMergeDetailsWnd(boolean flag){
		mergeDetailsWnd.setVisible(flag);
	}
	
	
	
	public static void updateEventsDetails(){
		mergeDetailsWnd.updateEventsDetails(mergeEventDetailsVec);
	}
	
	

	
	
	
	public static void init(){
		
		pDataManager.init();
		
		try{
			
			File dir = new File("data");
			if (dir.exists()) {
			} else {
				dir.mkdirs();
			}
			
			File file = new File("data/" + "checkList"
					+ ".data");
			
			
			if(!file.exists()){
				fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
				fw.close();
			}else{
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); // 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码,
				
				String str = null;
				
				String lastLine = null;
				
				while ((str = reader.readLine()) != null) {
					//System.out.println(str);
					String[] item = str.split(",");
					
					checkList.put(item[0], item[1]);
					
						
					}
				//fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			
			}
			
			
			//pdataWnd.updateEventsDetails(pEventsDetails);
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
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
				
				long passMinutes = 105*60*1000;
				
				long twoMinutes = 2*60*1000;
				
				
				java.util.Date time = dfMin.parse(timeStr);
				
				
				Calendar eventTime = Calendar.getInstance();  
				eventTime.setTime(time);

				long pass = currentTime - eventTime.getTimeInMillis();
				
				if(pass > passMinutes){
					
					System.out.println("merge events remove:" + Arrays.toString(mergeEventDetailsVec.elementAt(i)));
					
					mergeEventDetailsVec.remove(i);
					i--;
					continue;
				}
				
				//滚动盘，只有有进球，存
				if(mergeEventDetailsVec.elementAt(i)[ZHIBOINDEX.SAVED.ordinal()].equals("1")){
					//mergeEventDetailsVec.elementAt(i)[ZHIBOINDEX.SAVED.ordinal()] = "1";					
					String[] item = mergeEventDetailsVec.elementAt(i).clone();
					item[ZHIBOINDEX.TIME.ordinal()] = timeStr;
					boolean saveRes = pDataManager.saveTofile(item);	
					if(saveRes == true){
						System.out.println("merge inplay save success:" + Arrays.toString(item));
					}//滚动盘，无进球，到105分钟，存
				}else if(mergeEventDetailsVec.elementAt(i)[ZHIBOINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")
						&& pass > passMinutes){
					
					String[] item = mergeEventDetailsVec.elementAt(i).clone();
					item[ZHIBOINDEX.TIME.ordinal()] = timeStr;
					boolean saveRes = pDataManager.saveTofile(item);	
					if(saveRes == true){
						System.out.println("game over no goal merge inplay save success:" + Arrays.toString(item));
					}
					
				}	//单式盘，开赛两分钟,存			
				else if(pass > twoMinutes){
					//mergeEventDetailsVec.elementAt(i)[ZHIBOINDEX.SAVED.ordinal()] = "1";					
					String[] item = mergeEventDetailsVec.elementAt(i).clone();
					item[ZHIBOINDEX.TIME.ordinal()] = timeStr;
					boolean saveRes = pDataManager.saveTofile(item);	
					if(saveRes == true){
						System.out.println("merge save success:" + Arrays.toString(item));
					}
				}

				
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	
	
	

	public static boolean saveTofile(String p8Name, String zhiboName){
		
		try{
			

				
				String find = checkList.get(p8Name);
				
				if(null != find){
					if(!find.contains(zhiboName)){
						return false;
					}else{
						return true;
					}
						
				}

			
			
			File file = new File("data/" + "checkList"
					+ ".data");
			
			BufferedWriter fwlocal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			
			
			fwlocal.append(p8Name + "," + zhiboName);
			fwlocal.newLine();
			fwlocal.flush();
			
			fwlocal.close();
			
			checkList.put(p8Name, zhiboName);
			
			//updatepEventsDetails();
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public static void clearMergeData(){
		if(mergeEventDetailsVec.size() != 0){
			mergeEventDetailsVec.clear();
		}
	}
	
	
	public static void constructMergeRes(){
		
		try{
			
			Vector<String[]> p8events = (Vector<String[]>)P8Http.getFinalEventsDetails().clone();
			Vector<String[]> zhiboevents = ZhiboManager.getEventdetails();
			Vector<String[]> inPlayzhiboevents = ZhiboManager.getInplayEventsDetails();
			
			String[] item = null;
			String p8eventsname = "";
			String[] p8teams = null;
			String[] zhiboitem = null;
			
			
			for(int i = 0; i < p8events.size(); i++){
				item = p8events.elementAt(i).clone();
				
				p8eventsname = item[TYPEINDEX.EVENTNAMNE.ordinal()];
				
				//处理滚动盘	开始
				
				if(p8eventsname.contains("滚动盘")){
					
/*					String[] saveItem = pDataManager.findLatestEvents(p8eventsname);
					
					if(notAddtomerge.contains(p8eventsname)){
						continue;
					}
					
					if(saveItem != null){
						
						SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
						String dayStr = dfDay.format(System.currentTimeMillis());
						
						if(saveItem[TYPEINDEX.TIME.ordinal()].contains(dayStr)){
							saveItem[TYPEINDEX.TIME.ordinal()] = saveItem[TYPEINDEX.TIME.ordinal()].replace(dayStr + " ", "");
						}
						
						mergeEventDetailsVec.add(saveItem);
						
						continue;
					}
*/					
					p8eventsname = p8eventsname.replace("【滚动盘】", "");
					
					boolean isZhibohasGoals = false;
					
					p8teams = p8eventsname.split("-vs-");
					
					String zhiboHome = findZhiboTeam(p8teams[0]);
					
					if(zhiboHome != null){
						String zhiboAway = findZhiboTeam(p8teams[1]);
						
						if(zhiboAway != null){
							String zhiboeventname = zhiboHome + " vs " + zhiboAway;
							
							
							zhiboeventname = "【滚动盘】"+ zhiboeventname;
							
							item[ZHIBOINDEX.EVENTNAMNE.ordinal()] = zhiboeventname;
							
							
							String[] saveItem = pDataManager.findLatestEvents(zhiboeventname);
							
							if(notAddtomerge.contains(zhiboeventname)){
								continue;
							}
							
							if(saveItem != null){
								
								SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
								String dayStr = dfDay.format(System.currentTimeMillis());
								
								if(saveItem[TYPEINDEX.TIME.ordinal()].contains(dayStr)){
									saveItem[TYPEINDEX.TIME.ordinal()] = saveItem[TYPEINDEX.TIME.ordinal()].replace(dayStr + " ", "");
								}
								
								mergeEventDetailsVec.add(saveItem);
								
								continue;
							}

							
							
							String findZhiboEventname = zhiboeventname.replace("【滚动盘】", "");
							
							for(int j = 0; j< inPlayzhiboevents.size(); j++){
								String[] inplayItem = inPlayzhiboevents.elementAt(j).clone();
								if(inplayItem[ZHIBOINDEX.EVENTNAMNE.ordinal()].contains(findZhiboEventname)){
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
									
									if(zhiboItemNow[ZHIBOINDEX.EVENTNAMNE.ordinal()].contains(findZhiboEventname)){

										
										Double zhibo0homenow = Double.parseDouble(zhiboItemNow[ZHIBOINDEX.PERIOD0HOME.ordinal()]);
										Double zhibo0overnow = Double.parseDouble(zhiboItemNow[ZHIBOINDEX.PERIOD0OVER.ordinal()]);
										Double zhibo1homenow = Double.parseDouble(zhiboItemNow[ZHIBOINDEX.PERIOD1HOME.ordinal()]);
										Double zhibo1overnow = Double.parseDouble(zhiboItemNow[ZHIBOINDEX.PERIOD1OVER.ordinal()]);
										
										Double p8nogoalsbet1 = 0.0;
										Double p8nogoalsbet2 = 0.0;
										Double p8nogoalsbet3 = 0.0;										
										Double p8nogoalsbet4 = 0.0;
										
										//String[] zhiboSaveItem = ZhiboManager.getZhiboSaveItem(zhiboeventname);
										
										//if(null != zhiboSaveItem){
										Double p8danshibet1 = Double.parseDouble(p8DanshiItem[ZHIBOINDEX.PERIOD0HOME.ordinal()]);
										Double p8danshibet2 = Double.parseDouble(p8DanshiItem[ZHIBOINDEX.PERIOD0OVER.ordinal()]);
										Double p8danshibet3 = Double.parseDouble(p8DanshiItem[ZHIBOINDEX.PERIOD1HOME.ordinal()]);
										Double p8danshibet4 = Double.parseDouble(p8DanshiItem[ZHIBOINDEX.PERIOD1OVER.ordinal()]);

										
										Double p80homeInplayVal = Double.parseDouble(item[TYPEINDEX.PERIOD0HOME.ordinal()]);
										Double p80overInplayVal = Double.parseDouble(item[TYPEINDEX.PERIOD0OVER.ordinal()]);
										Double p81homeInplayVal = Double.parseDouble(item[TYPEINDEX.PERIOD1HOME.ordinal()]);
										Double p81overInplayVal = Double.parseDouble(item[TYPEINDEX.PERIOD1OVER.ordinal()]);
										
										//P8没进球之前的和值
										p8nogoalsbet1 = p8danshibet1 + p80homeInplayVal;
										p8nogoalsbet2 = p8danshibet2 + p80overInplayVal;
										p8nogoalsbet3 = p8danshibet3 + p81homeInplayVal;
										p8nogoalsbet4 = p8danshibet4 + p81overInplayVal;
										
										
										boolean addTomerge = false;
										
										
										if((p8nogoalsbet1 >0.0 && zhibo0homenow >0.0) || (p8nogoalsbet1 <0.0 && zhibo0homenow <0.0)){
											
											
											item[TYPEINDEX.PERIOD0HOME.ordinal()] = String.format("(%.0f)", p8nogoalsbet1) + "+" +
													String.format("(%.0f)", zhibo0homenow) + "=" + String.format("%.0f", p8nogoalsbet1 + zhibo0homenow);
											
											addTomerge = true;
										}else{
											item[TYPEINDEX.PERIOD0HOME.ordinal()] = "0";
										}
										
										if((p8nogoalsbet2 >0.0 && zhibo0overnow >0.0) || (p8nogoalsbet2 <0.0 && zhibo0overnow <0.0)){
											//item[TYPEINDEX.PERIOD0OVER.ordinal()] = String.format("%.0f", p80over + zhibo0over);
											
											item[TYPEINDEX.PERIOD0OVER.ordinal()] = String.format("(%.0f)", p8nogoalsbet2) + "+" +
													String.format("(%.0f)", zhibo0overnow) + "=" + String.format("%.0f", p8nogoalsbet2 + zhibo0overnow);
											
											addTomerge = true;
										}
										else{
											item[TYPEINDEX.PERIOD0OVER.ordinal()] = "0";
										}
										

										
										if((p8nogoalsbet3 >0.0 && zhibo1homenow >0.0) || (p8nogoalsbet3 <0.0 && zhibo1homenow <0.0)){
											//item[TYPEINDEX.PERIOD1HOME.ordinal()] = String.format("%.0f", p81home + zhibo1home);
											item[TYPEINDEX.PERIOD1HOME.ordinal()] = String.format("(%.0f)", p8nogoalsbet3) + "+" +
													String.format("(%.0f)", zhibo1homenow) + "=" + String.format("%.0f", p8nogoalsbet3 + zhibo1homenow);
											addTomerge = true;
										}else{
											item[TYPEINDEX.PERIOD1HOME.ordinal()] = "0";
										}
										
										
										if((p8nogoalsbet4 >0.0 && zhibo1overnow >0.0) || (p8nogoalsbet4 <0.0 && zhibo1overnow <0.0)){
											//item[TYPEINDEX.PERIOD1OVER.ordinal()] = String.format("%.0f", p81over + zhibo1over);
											
											item[TYPEINDEX.PERIOD1OVER.ordinal()] = String.format("(%.0f)", p8nogoalsbet4) + "+" +
													String.format("(%.0f)", zhibo1overnow) + "=" + String.format("%.0f", p8nogoalsbet4 + zhibo1overnow);
											
											addTomerge = true;
										}
										else{
											item[TYPEINDEX.PERIOD1OVER.ordinal()] = "0";
										}
										
										
										if(addTomerge == true){
											
											
											System.out.println("merge inplay add" + Arrays.toString(item));
											
											if(isZhibohasGoals == true){
												item[ZHIBOINDEX.SAVED.ordinal()] = "1";
											}
											
											mergeEventDetailsVec.add(item);
										}else{
											if(isZhibohasGoals == true){
												
												System.out.println("not add to merge" + Arrays.toString(item));
												
												notAddtomerge.add(item[TYPEINDEX.EVENTNAMNE.ordinal()]);
											}
										}
											
											
											
										//}
										
										
										
										break;
										
									}
									
								}
							}
							
						
						}
					
					
						
					}
					
					continue;
				}
				
				//处理滚动盘	结束
				
				
				
				p8teams = p8eventsname.split("-vs-");
				
				String zhiboHome = findZhiboTeam(p8teams[0]);
				
				if(zhiboHome != null){
					String zhiboAway = findZhiboTeam(p8teams[1]);
					
					if(zhiboAway != null){
						String zhiboeventname = zhiboHome + " vs " + zhiboAway;
						
						item[ZHIBOINDEX.EVENTNAMNE.ordinal()] = zhiboeventname;
						
						for(int j = 0; j< zhiboevents.size(); j++){
							zhiboitem = zhiboevents.elementAt(j);
							
							if(zhiboitem[ZHIBOINDEX.EVENTNAMNE.ordinal()].contains(zhiboeventname)
									&&!zhiboitem[ZHIBOINDEX.TIME.ordinal()].contains("(")){
								
								boolean addTomerge = false;
								
								Double p80home = Double.parseDouble(item[TYPEINDEX.PERIOD0HOME.ordinal()]);
								Double p80over = Double.parseDouble(item[TYPEINDEX.PERIOD0OVER.ordinal()]);
								Double p81home = Double.parseDouble(item[TYPEINDEX.PERIOD1HOME.ordinal()]);
								Double p81over = Double.parseDouble(item[TYPEINDEX.PERIOD1OVER.ordinal()]);
								
								Double zhibo0home = Double.parseDouble(zhiboitem[ZHIBOINDEX.PERIOD0HOME.ordinal()]);
								Double zhibo0over = Double.parseDouble(zhiboitem[ZHIBOINDEX.PERIOD0OVER.ordinal()]);
								Double zhibo1home = Double.parseDouble(zhiboitem[ZHIBOINDEX.PERIOD1HOME.ordinal()]);
								Double zhibo1over = Double.parseDouble(zhiboitem[ZHIBOINDEX.PERIOD1OVER.ordinal()]);
								
								
								if((p80home >0.0 && zhibo0home >0.0) || (p80home <0.0 && zhibo0home <0.0)){
									//item[TYPEINDEX.PERIOD0HOME.ordinal()] = String.format("%.0f", p80home + zhibo0home);
									
									item[TYPEINDEX.PERIOD0HOME.ordinal()] = String.format("(%.0f)", p80home) + "+" +
											String.format("(%.0f)", zhibo0home) + "=" + String.format("%.0f", p80home + zhibo0home);
									
									addTomerge = true;
								}else{
									item[TYPEINDEX.PERIOD0HOME.ordinal()] = "0";
								}
								
								if((p80over >0.0 && zhibo0over >0.0) || (p80over <0.0 && zhibo0over <0.0)){
									//item[TYPEINDEX.PERIOD0OVER.ordinal()] = String.format("%.0f", p80over + zhibo0over);
									
									item[TYPEINDEX.PERIOD0OVER.ordinal()] = String.format("(%.0f)", p80over) + "+" +
											String.format("(%.0f)", zhibo0over) + "=" + String.format("%.0f", p80over + zhibo0over);
									
									addTomerge = true;
								}
								else{
									item[TYPEINDEX.PERIOD0OVER.ordinal()] = "0";
								}
								
								if((p81over >0.0 && zhibo1over >0.0) || (p81over <0.0 && zhibo1over <0.0)){
									//item[TYPEINDEX.PERIOD1OVER.ordinal()] = String.format("%.0f", p81over + zhibo1over);
									
									item[TYPEINDEX.PERIOD1OVER.ordinal()] = String.format("(%.0f)", p81over) + "+" +
											String.format("(%.0f)", zhibo1over) + "=" + String.format("%.0f", p81over + zhibo1over);
									
									addTomerge = true;
								}
								else{
									item[TYPEINDEX.PERIOD1OVER.ordinal()] = "0";
								}
								
								if((p81home >0.0 && zhibo1home >0.0) || (p81home <0.0 && zhibo1home <0.0)){
									//item[TYPEINDEX.PERIOD1HOME.ordinal()] = String.format("%.0f", p81home + zhibo1home);
									item[TYPEINDEX.PERIOD1HOME.ordinal()] = String.format("(%.0f)", p81home) + "+" +
											String.format("(%.0f)", zhibo1home) + "=" + String.format("%.0f", p81home + zhibo1home);
									addTomerge = true;
								}else{
									item[TYPEINDEX.PERIOD1HOME.ordinal()] = "0";
								}
								
								if(addTomerge == true){
									mergeEventDetailsVec.add(item);
								}
								
								break;
								
							}
							
						}
						
					}
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	
	public static void sendMails(){
		try{
			
			for(int i = 0; i < mergeEventDetailsVec.size(); i++){
				
				String[] item = mergeEventDetailsVec.elementAt(i).clone();
				
				if(item[TYPEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
					continue;
				}
				
				double betAmt1 =0.0;
				double betp81 = 0.0;
				double betzhibo1 = 0.0;
				
				double betAmt2 = 0.0;
				double betp82 = 0.0;
				double betzhibo2 = 0.0;
				
				double betAmt3 =0.0;
				double betp83 = 0.0;
				double betzhibo3 = 0.0;
				
				double betAmt4 = 0.0;
				double betp84 = 0.0;
				double betzhibo4 = 0.0;
				
				String str1 = item[TYPEINDEX.PERIOD0HOME.ordinal()];
				
				if(str1.contains("=")){
					String[] tmp = str1.split("=");
					betAmt1 = Double.parseDouble(tmp[1]);
					
					String[] tmp1 = tmp[0].split("\\+");
					tmp1[0] = tmp1[0].replace("(", "");
					tmp1[0] = tmp1[0].replace(")", "");
					
				
					tmp1[1] = tmp1[1].replace("(", "");
					tmp1[1] = tmp1[1].replace(")", "");
					
					betp81 = Double.parseDouble(tmp1[0]);
					betzhibo1 = Double.parseDouble(tmp1[1]);
					
					
				}else{
					betAmt1 = Double.parseDouble(str1);
				}
				
				
				String str2 = item[TYPEINDEX.PERIOD0OVER.ordinal()];
				
				if(str2.contains("=")){
					String[] tmp = str2.split("=");
					betAmt2 = Double.parseDouble(tmp[1]);
					
					String[] tmp1 = tmp[0].split("\\+");
					tmp1[0] = tmp1[0].replace("(", "");
					tmp1[0] = tmp1[0].replace(")", "");
					
				
					tmp1[1] = tmp1[1].replace("(", "");
					tmp1[1] = tmp1[1].replace(")", "");
					
					betp82 = Double.parseDouble(tmp1[0]);
					betzhibo2 = Double.parseDouble(tmp1[1]);
					
				}else{
					betAmt2 = Double.parseDouble(str2);
				}
				
				
				String str3 = item[TYPEINDEX.PERIOD1HOME.ordinal()];
				
				if(str3.contains("=")){
					String[] tmp = str3.split("=");
					betAmt3 = Double.parseDouble(tmp[1]);
					
					String[] tmp1 = tmp[0].split("\\+");
					tmp1[0] = tmp1[0].replace("(", "");
					tmp1[0] = tmp1[0].replace(")", "");
					
				
					tmp1[1] = tmp1[1].replace("(", "");
					tmp1[1] = tmp1[1].replace(")", "");
					
					betp83 = Double.parseDouble(tmp1[0]);
					betzhibo3 = Double.parseDouble(tmp1[1]);
					
				}else{
					betAmt3 = Double.parseDouble(str3);
				}
				
				String str4 = item[TYPEINDEX.PERIOD1OVER.ordinal()];
				
				if(str4.contains("=")){
					String[] tmp = str4.split("=");
					betAmt4 = Double.parseDouble(tmp[1]);
					
					String[] tmp1 = tmp[0].split("\\+");
					tmp1[0] = tmp1[0].replace("(", "");
					tmp1[0] = tmp1[0].replace(")", "");
					
				
					tmp1[1] = tmp1[1].replace("(", "");
					tmp1[1] = tmp1[1].replace(")", "");
					
					betp84 = Double.parseDouble(tmp1[0]);
					betzhibo4 = Double.parseDouble(tmp1[1]);
					
				}else{
					betAmt4 = Double.parseDouble(str4);
				}
				
				
				//SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
				
				SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				
				
				long currentTimeL = System.currentTimeMillis();
				
				String todayStr = dfDay.format(currentTimeL);
				

					
				String timeStr = item[TYPEINDEX.TIME.ordinal()];
				
				if(!timeStr.contains("-")){
					timeStr = todayStr + " " + timeStr;
				}
				
				String key = item[TYPEINDEX.EVENTNAMNE.ordinal()] + " " + timeStr;
				
        		if(true != mailRecords.containsKey(key)){
        			Vector<Integer> records = new Vector<Integer>();
        			mailRecords.put(key, records);
        		}
				
				
        		
        		Vector<Integer> records = mailRecords.get(key); 
        		
				if(Math.abs(betp81) > mergeHideNumber && Math.abs(betzhibo1) > mergeHideNumber){
					
					
					
	    			int p0hsend = (int) (betAmt1/mergeSendNumber);    			
	    			
	    			if(true != records.contains(p0hsend) && p0hsend != 0){
	    				records.add(p0hsend);
	    				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "ks68889@163.com", "【合并】 " + key, "全场让球:" + Integer.toString(p0hsend));
	    			}
				}
				
				if(Math.abs(betp82) > mergeHideNumber && Math.abs(betzhibo2) > mergeHideNumber){
					
	    			int p0osend = (int) (betAmt2/mergeSendNumber);
	    			int p0osendsaved = 0;
	    			if(p0osend != 0){
	    				if(p0osend < 0){
	    					p0osendsaved = p0osend - 10;
	    				}else{
	    					p0osendsaved = p0osend + 10;
	    				}
	    				
	        			if(true != records.contains(p0osendsaved)){
	        				records.add(p0osendsaved);
	        				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "ks68889@163.com", "【合并】 " + key, "全场大小:" + Integer.toString(p0osend));
	        			}
	    				
	    			}
				}
				
				
				if(Math.abs(betp83) > mergeHideNumber && Math.abs(betzhibo3) > mergeHideNumber){
					
	    			int p1hsend = (int) (betAmt3/mergeSendNumber);
	    			int p1hsendsaved = 0;
	    			if(p1hsend != 0){
	    				if(p1hsend < 0){
	    					p1hsendsaved = p1hsend - 20;
	    				}else{
	    					p1hsendsaved = p1hsend + 20;
	    				}
	    				
	        			if(true != records.contains(p1hsendsaved)){
	        				records.add(p1hsendsaved);
	        				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "ks68889@163.com", "【合并】 " + key, "全场大小:" + Integer.toString(p1hsend));
	        			}
	    				
	    			}
				}
				
				
				if(Math.abs(betp84) > mergeHideNumber && Math.abs(betzhibo4) > mergeHideNumber){
					
	    			int p1osend = (int) (betAmt3/mergeSendNumber);
	    			int p1osendsaved = 0;
	    			if(p1osend != 0){
	    				if(p1osend < 0){
	    					p1osendsaved = p1osend - 30;
	    				}else{
	    					p1osendsaved = p1osend + 30;
	    				}
	    				
	        			if(true != records.contains(p1osendsaved)){
	        				records.add(p1osendsaved);
	        				MailManager.sendMail("tongjigujinlong@126.com", "tongjigujinlong", "gcw701!", "ks68889@163.com", "【合并】 " + key, "全场大小:" + Integer.toString(p1osend));
	        			}
	    				
	    			}
				}
				
				
				
				
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	
	
	
	
	
	
	private static String findZhiboTeam(String p8team){
		
		String zhiboteam = checkList.get(p8team);
		
		return zhiboteam;
		
	}
	
	
	public static void setP8Txt(){
		mergeWnd.setP8Txt(p8SelectedRow);
	}
	
	public static void setZhiboTxt(){
		mergeWnd.setZhiboTxt(zhiboSelectedRow);
	}
	
}
