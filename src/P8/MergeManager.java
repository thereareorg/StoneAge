package P8;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.json.JSONObject;

import team.gl.nio.cln.ZhiboClientHandler;
import HG.HGINDEX;
import HG.HGMergeManager;
import HG.HGhttp;
import Mail.MailManager;

public class MergeManager {
	
	static BufferedWriter fw = null;  //写	
	static BufferedReader reader = null; //读
	
	public static Map<String,String> checkList = new LinkedHashMap<String,String>();
	
	private static ReadWriteLock lockeFinalMergeEventsDetails = new ReentrantReadWriteLock();
	
	
	public static Vector<String[]> finalMergeEventDetailsVec = new Vector<String[]>();

	public static Vector<String[]> mergeEventDetailsVec = new Vector<String[]>();
	
	public static MergeDetailsWindow mergeDetailsWnd = new MergeDetailsWindow();
	
	public static Vector<String> sendpankoualready = new Vector<String>();
	
	
	
	public static Vector<String[]> mergebeforegoaldetails = new Vector<String[]>();
	
	
	public static int sendtotal = 500000;
	public static int sendside = 100000;
	
	
/*	public static int sendtotal = 500;
	public static int sendside = 100;*/
	
	
	
	public static int mergeHideNumber = 5000;
	
	public static TeamMatchWindow teamMatchWnd = new TeamMatchWindow("zhibo");
	
	
	
	public static int mergep0hSendNumber = 1800000;
	public static int mergep0oSendNumber = 700000;
	public static int mergep0oHideSendNumber = 100000;
	
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
    


    public static Vector<String[]> getpSubMergeevents(){
    	return pDataManager.getpSubevents();
    }
    
    public static void showTeamMatchWnd(){
    	teamMatchWnd.setVisible(true);
    }
    
	
	
	public static void showMergeWnd(boolean flag){
		
		setP8Txt();
		setZhiboTxt();
		
		mergeWnd.setVisible(flag);
	}
	
	public static void showMergeDetailsWnd(boolean flag){
		mergeDetailsWnd.setVisible(flag);
	}
	
    public static Vector<String[]> getpreviouseventsdata(){
    	return pDataManager.getpreviousdata();
    }
	
	
	
	public static void updateEventsDetails(){
		
		try{
			//if(mergeEventDetailsVec.size() != 0){
				
				//Vector<Integer> removeIndex = new Vector<Integer>();
				
				for(int i = 0; i < mergeEventDetailsVec.size(); i++){
					String inplaytime = mergeEventDetailsVec.elementAt(i)[MERGEINDEX.PERIOD1HOME.ordinal()];
					if(inplaytime != null&& inplaytime.contains("'")){
						inplaytime = inplaytime.replace("'", "");
						inplaytime = inplaytime.replace("+", "");
						int tmp = Integer.parseInt(inplaytime);
						if(tmp < 89){
							mergeEventDetailsVec.elementAt(i)[MERGEINDEX.TIME.ordinal()] = mergeEventDetailsVec.elementAt(i)[MERGEINDEX.PERIOD1HOME.ordinal()];
						}else{
							
							SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
							
							
							long currentTimeL = System.currentTimeMillis();
							
							String todayStr = dfDay.format(currentTimeL);
							
							
							String[] item = mergeEventDetailsVec.elementAt(i);
							
							String timeStr = item[MERGEINDEX.TIME.ordinal()];
							
							if(!timeStr.contains("-")){
								timeStr = todayStr + " " + timeStr;
							}
							
							
							item[MERGEINDEX.TIME.ordinal()] = timeStr;
							
							boolean saveRes = pDataManager.saveTofile(item);	
							if(saveRes == true){
								System.out.println("score merge already: game over merge save success:" + Arrays.toString(item));
								System.out.println(tmp);
								
								for(int j = 0; j< mergebeforegoaldetails.size(); j++){
									if(item[MERGEINDEX.EVENTNAMNE.ordinal()].equals(mergebeforegoaldetails.elementAt(j)[MERGEINDEX.EVENTNAMNE.ordinal()])){
										
										
										mergebeforegoaldetails.remove(j);
										
										break;
									}
								}
								
							}
							
							mergeEventDetailsVec.remove(i);
							i--;
						}
					}else if(inplaytime != null && inplaytime.contains("中")){
						mergeEventDetailsVec.elementAt(i)[MERGEINDEX.TIME.ordinal()] = mergeEventDetailsVec.elementAt(i)[MERGEINDEX.PERIOD1HOME.ordinal()];
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
	
	

	
	
	
	public static void init() throws IOException{
		
		teamMatchWnd.setTitle("智博队名匹配");
		
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
			
			
			teamMatchWnd.updateEventsDetails(checkList);
			
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
	
    
    
    public static void sendpankoumails(){
		try{
			
			
			boolean bchupanres = false;

			
			Vector<String[]> Vectmp = new Vector<String[]>();
			

			
			for(int i = 0; i < mergeEventDetailsVec.size(); i++){

				String timeStr = mergeEventDetailsVec.elementAt(i)[MERGEINDEX.TIME.ordinal()];
				
				if(mergeEventDetailsVec.elementAt(i)[MERGEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
					continue;
				}

				Vectmp.add(mergeEventDetailsVec.elementAt(i));
		
			}
			
			

			
			
			
			Vector<String[]> scoreDetails = StoneAge.score.getFinalScoresDetails();

			if(scoreDetails == null || scoreDetails.size() == 0 || Vectmp.size() == 0){

				return;

				
			}
			
			
			
			
			for(int k = 0; k < scoreDetails.size(); k++){
				System.out.println(Arrays.toString(scoreDetails.elementAt(k)));
			}
			
			
			System.out.println("Merge队:");
			
			for(int k = 0; k < Vectmp.size(); k++){
				System.out.println(Arrays.toString(Vectmp.elementAt(k)));
			}
			
			
			
			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			long currentTimeL = System.currentTimeMillis();
			
			String todayStr = dfDay.format(currentTimeL);
			
			
			
			//合并score
			for(int i = 0; i < Vectmp.size(); i++){
				
				String[] olditem = Vectmp.elementAt(i).clone();
				
				String mergetime = olditem[MERGEINDEX.TIME.ordinal()];
				
				if(!mergetime.contains("-")){
					mergetime = todayStr + " " + mergetime;
				}
				
				if(dfMin.parse(mergetime).getTime() - System.currentTimeMillis() > 120 * 1000 || dfMin.parse(mergetime).getTime() - System.currentTimeMillis() < -60*1000){
					continue;
				}
				
				
				String zhibohometeam = olditem[MERGEINDEX.EVENTNAMNE.ordinal()].split(" vs ")[0];
				String zhiboawayteam = olditem[MERGEINDEX.EVENTNAMNE.ordinal()].split(" vs ")[1];
				
				String[] item = {Integer.toString(i+1), olditem[MERGEINDEX.LEAGUENAME.ordinal()], olditem[MERGEINDEX.TIME.ordinal()], olditem[MERGEINDEX.EVENTNAMNE.ordinal()], "", "", "",
						olditem[MERGEINDEX.P8HRES.ordinal()], olditem[MERGEINDEX.INP8HRES.ordinal()], olditem[MERGEINDEX.ZHIBOHRES.ordinal()], olditem[MERGEINDEX.PERIOD0HOME.ordinal()],
						"", "","", olditem[MERGEINDEX.P8ORES.ordinal()],olditem[MERGEINDEX.INP8ORES.ordinal()],olditem[MERGEINDEX.ZHIBOORES.ordinal()], olditem[MERGEINDEX.PERIOD0OVER.ordinal()],"", "", ""};
				
				String scorehometeam = MergeManager.findScoreTeambyzhiboteam(zhibohometeam);
				if(scorehometeam != null){
					String scoreawayteam = MergeManager.findScoreTeambyzhiboteam(zhiboawayteam);
					
					
					
					if(scoreawayteam != null){
						
						int indexinscoredetails = -1;
						for(int j = 0; j < scoreDetails.size(); j++){
							
							String scoreTime = scoreDetails.elementAt(j)[SCORENEWINDEX.TIME.ordinal()];
							
							if(scoreDetails.elementAt(j)[SCORENEWINDEX.EVENTNAMNE.ordinal()].equals(scorehometeam + " vs " + scoreawayteam)){
								indexinscoredetails = j;
								break;
							}
						}
						
						if(indexinscoredetails != -1){
							item[MERGEPRETABLEHEADINDEX.RQCHUPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQCHUPAN.ordinal()];
							item[MERGEPRETABLEHEADINDEX.RQZHONGPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQZHONGPAN.ordinal()];
							item[MERGEPRETABLEHEADINDEX.RQPANAS.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQPANAS.ordinal()];
							item[MERGEPRETABLEHEADINDEX.DXQCHUPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQCHUPAN.ordinal()];
							item[MERGEPRETABLEHEADINDEX.DXQZHONGPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQZHONGPAN.ordinal()];
							item[MERGEPRETABLEHEADINDEX.DXQPANAS.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQPANAS.ordinal()];
							item[MERGEPRETABLEHEADINDEX.SCORE.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.SCORE.ordinal()];

							
							

								//发送大小球盘
								if(!item[MERGEPRETABLEHEADINDEX.DXQPANAS.ordinal()].equals("")){
									
								
									
									
									
									//大小球盘结果分析
									if(!item[MERGEPRETABLEHEADINDEX.DXQZHONGPAN.ordinal()].equals("") && !item[MERGEPRETABLEHEADINDEX.DXQZHONGPAN.ordinal()].equals("-")){
									
										
										
										int p0over = 0;
										
										int pp = 0;
										if(item[MERGEPRETABLEHEADINDEX.P8OVERRES.ordinal()].contains("=")){
											pp = Integer.parseInt(item[MERGEPRETABLEHEADINDEX.P8OVERRES.ordinal()].split("=")[1]);
										}
										
										int ll = 0;
										ll = Integer.parseInt(item[MERGEPRETABLEHEADINDEX.ZHIBOOVERRES.ordinal()]);
										
										boolean addtosend = true;
										
										if((Math.abs(pp) + Math.abs(ll)) != Math.abs(pp + ll)){
											addtosend = false;
										}
										
										if(Math.abs(pp) < sendside || Math.abs(ll) < sendside){
											addtosend = false;
										}
										
										p0over = pp + ll;
										
										String sendgoldstr  = "(" + Integer.toString(pp) + ")+" + "(" + Integer.toString(ll) +")=" + Integer.toString(p0over);
										
										
										if(Math.abs(p0over) < sendtotal){
											addtosend = false;
										}
										

										String bet = "";
										
										//只统计赌降的一边
										if(item[MERGEPRETABLEHEADINDEX.DXQPANAS.ordinal()].contains("降") && 
												p0over  < 0){
											bet = "大";
										}else if(item[MERGEPRETABLEHEADINDEX.DXQPANAS.ordinal()].contains("升") && 
												p0over  > 0){
											bet = "小";
										}

										
										String sendmark = item[MERGEPRETABLEHEADINDEX.EVENTNAME.ordinal()] + "大小盘";
										
										
										if(!bet.equals("") && addtosend == true && !sendpankoualready.contains(sendmark)){
											
											
											
											String sendTitle = "合并盘口分析 " + "【" + "大小盘" + "】";
							    			String sendContent = "联赛:" + item[MERGEPRETABLEHEADINDEX.LEAGUE.ordinal()] + "<br>" +
							    								 "球队:" + item[MERGEPRETABLEHEADINDEX.EVENTNAME.ordinal()] + "<br>" +
							    								 "时间:" + item[MERGEPRETABLEHEADINDEX.TIME.ordinal()] + "<br>" +
							    								 "初盘:" + item[MERGEPRETABLEHEADINDEX.DXQCHUPAN.ordinal()] + "<br>" +
							    								 "终盘:" + item[MERGEPRETABLEHEADINDEX.DXQZHONGPAN.ordinal()] + "<br>" +
							    								 "分析:" + item[MERGEPRETABLEHEADINDEX.DXQPANAS.ordinal()] + "<br>" +
							    								 "金额:" + sendgoldstr;
											
											
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

								
								//发送让球盘
								if(!item[MERGEPRETABLEHEADINDEX.RQPANAS.ordinal()].equals("")){
									
									
									//让球盘结果分析
									if(!item[MERGEPRETABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("") && !item[MERGEPRETABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("-")){
										
										System.out.println(Arrays.toString(item));
										
										
										
										
										


										
										
										int p0home = 0;
										
										int pp = 0;
										if(item[MERGEPRETABLEHEADINDEX.P8HOMERES.ordinal()].contains("=")){
											pp = Integer.parseInt(item[MERGEPRETABLEHEADINDEX.P8HOMERES.ordinal()].split("=")[1]);
										}
										
										int ll = 0;
										ll = Integer.parseInt(item[MERGEPRETABLEHEADINDEX.ZHIBOHOMERES.ordinal()]);
										
										if((Math.abs(pp) + Math.abs(ll)) != Math.abs(pp + ll)){
											continue;
										}
										
										boolean addtosend = true;
										
										if(Math.abs(pp) < sendside || Math.abs(ll) < sendside){
											addtosend = false;
										}
										
										p0home = pp + ll;
										
										String sendgoldstr  = "(" + Integer.toString(pp) + ")+" + "(" + Integer.toString(ll) +")=" + Integer.toString(p0home);
										
										if(Math.abs(p0home) < 1000000){
											addtosend = false;
										}
										
										

										String betside = "";
										
										//只统计赌降的那一边
										if(item[MERGEPRETABLEHEADINDEX.RQPANAS.ordinal()].contains("降") && 
												item[MERGEPRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												p0home  > 0){
											betside = "right";
										}else if(item[MERGEPRETABLEHEADINDEX.RQPANAS.ordinal()].contains("降") && 
												!item[MERGEPRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												p0home  < 0){
											betside = "left";
										}else if(item[MERGEPRETABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
												item[MERGEPRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												p0home  < 0){
											betside = "left";
										}else if(item[MERGEPRETABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
												!item[MERGEPRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												p0home  > 0){
											betside = "right";
										}
										
										
										String sendmark = item[MERGEPRETABLEHEADINDEX.EVENTNAME.ordinal()] + "让球盘";
										
										String[] oldteams = item[MERGEPRETABLEHEADINDEX.EVENTNAME.ordinal()].split(" vs ");
										
										if(betside.equals("right")){
											
											item[MERGEPRETABLEHEADINDEX.EVENTNAME.ordinal()] = "<html>" + oldteams[0] + " vs " +  "<font color='red'>" + oldteams[1] + "</font>";
											
											

											
										}else if(betside.equals("left")){
											item[MERGEPRETABLEHEADINDEX.EVENTNAME.ordinal()] = "<html>" +"<font color='red'>" + oldteams[0] + "</font>" + " vs " + oldteams[1];
											
											//todo send
										}else{
											addtosend = false;
										}
										

							
										if(addtosend == true && !sendpankoualready.contains(sendmark)){
											String sendTitle = "合并盘口分析 " + "【" + "让球盘" + "】";
							    			String sendContent = "联赛:" + item[MERGEPRETABLEHEADINDEX.LEAGUE.ordinal()] + "<br>" +
							    								 "球队:" + item[MERGEPRETABLEHEADINDEX.EVENTNAME.ordinal()] + "<br>" +
							    								 "时间:" + item[MERGEPRETABLEHEADINDEX.TIME.ordinal()] + "<br>" +
							    								 "初盘:" + item[MERGEPRETABLEHEADINDEX.RQCHUPAN.ordinal()] + "<br>" +
							    								 "终盘:" + item[MERGEPRETABLEHEADINDEX.RQZHONGPAN.ordinal()] + "<br>" +
							    								 "分析:" + item[MERGEPRETABLEHEADINDEX.RQPANAS.ordinal()] + "<br>" +
							    								 "金额:" + sendgoldstr;
											
											
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


							
							
							


							
							
							
							
							
							
							
							
							

							
						}
						
						
					}
					
				}
				
				
				
			}
			
			
			
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
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
				
				if(timeStr.contains("'")|| timeStr.contains("中")){
					continue;
				}
				
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

				
				//old save method
				//if(mergeEventDetailsVec.elementAt(i)[ZHIBOINDEX.SAVED.ordinal()].equals("1")){//有进球了存储
				if(pass > 0){//开赛即存储
					//mergeEventDetailsVec.elementAt(i)[ZHIBOINDEX.SAVED.ordinal()] = "1";				
					
					
					

					
					
					String[] item = mergeEventDetailsVec.elementAt(i).clone();

					boolean find = false;
					for(int j = 0; j< mergebeforegoaldetails.size(); j++){
						if(item[MERGEINDEX.EVENTNAMNE.ordinal()].equals(mergebeforegoaldetails.elementAt(j)[MERGEINDEX.EVENTNAMNE.ordinal()])){
							find = true;
							break;
						}
					}
					if(find == false){
						mergebeforegoaldetails.add(item);
					}
					
					
					item[ZHIBOINDEX.TIME.ordinal()] = timeStr;
					boolean saveRes = pDataManager.saveTofile(item);	
					
					
					if(saveRes == true){
						//System.out.println("already have goal, save success:" + Arrays.toString(item));
						
						System.out.println(dfMin.format(System.currentTimeMillis()) + " merge save success:" + Arrays.toString(item));

					}
					
					
				}/*else if(pass > passMinutes){					
					String[] item = mergeEventDetailsVec.elementAt(i).clone();
					item[ZHIBOINDEX.TIME.ordinal()] = timeStr;
					boolean saveRes = pDataManager.saveTofile(item);	
					if(saveRes == true){
						System.out.println("game over no goal merge inplay save success:" + Arrays.toString(item));
					}
					
				}*/
				//old save method

				
/*				String inplaytimestr = mergeEventDetailsVec.elementAt(i)[MERGEINDEX.PERIOD1HOME.ordinal()];
				
				if(null != inplaytimestr && (inplaytimestr.contains("'") || inplaytimestr.contains("中"))){
					continue;
				}
			
				if(pass > passMinutes){					
					String[] item = mergeEventDetailsVec.elementAt(i).clone();
					item[ZHIBOINDEX.TIME.ordinal()] = timeStr;
					boolean saveRes = pDataManager.saveTofile(item);	
					
					
					
					if(saveRes == true){
						System.out.println("game over merge save success:" + Arrays.toString(item));
						
						for(int j = 0; j< mergebeforegoaldetails.size(); j++){
							if(item[MERGEINDEX.EVENTNAMNE.ordinal()].equals(mergebeforegoaldetails.elementAt(j)[MERGEINDEX.EVENTNAMNE.ordinal()])){
								
								
								mergebeforegoaldetails.remove(j);
								
								break;
							}
						}
						
					}
					
					
					
				}*/
				
				
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
			
			teamMatchWnd.updateEventsDetails(checkList);
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	public static boolean deleteateammatch(String p8Name){
		
		try{
			

				
				String find = checkList.get(p8Name);
				
				if(null != find){
					
					checkList.remove(p8Name);
					
					File file = new File("data/" + "checkList"
							+ ".data");
					
					BufferedWriter fwlocal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
					
					
					for (String key : checkList.keySet()) {  
						  
					    String value = (String)checkList.get(key);
					    
					    String[] tmp = {key, value};
					    
						fwlocal.append(key + "," + value);
						fwlocal.newLine();
						fwlocal.flush();
					  
					}  

					
					fwlocal.close();
					
					//checkList.put(p8Name, zhiboName);
					
					teamMatchWnd.updateEventsDetails(checkList);

					
					return true;
						
				}else{
					return false;
				}

			
			

			
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
					
/*					String[] scoreItem = null;
					
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
					}*/
					
					
					
					boolean isZhibohasGoals = false;

					
					String zhiboHome = findZhiboTeam(p8teams[0]);
					
					if(zhiboHome != null){
						String zhiboAway = findZhiboTeam(p8teams[1]);
						
						if(zhiboAway != null){
							String zhiboeventname = zhiboHome + " vs " + zhiboAway;
							
							for(int j = 0; j< inPlayzhiboevents.size(); j++){
								String[] inplayItem = inPlayzhiboevents.elementAt(j).clone();
								if(inplayItem[ZHIBOINDEX.EVENTNAMNE.ordinal()].contains(zhiboeventname)){
									isZhibohasGoals = true;
									break;
								}
							}
							
							
							
							
							
							item[ZHIBOINDEX.EVENTNAMNE.ordinal()] = zhiboeventname;
							
							
							
							String[] saveItem = pDataManager.findLatestEvents(zhiboeventname);
							
							
							
							
							if(saveItem != null){
								
								SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
								String dayStr = dfDay.format(System.currentTimeMillis());
								
								if(saveItem[MERGEINDEX.TIME.ordinal()].contains(dayStr)){
									saveItem[MERGEINDEX.TIME.ordinal()] = saveItem[MERGEINDEX.TIME.ordinal()].replace(dayStr + " ", "");
								}
								
								for(int j = 0; j < mergeEventDetailsVec.size(); j++){
									if(mergeEventDetailsVec.elementAt(j)[MERGEINDEX.EVENTNAMNE.ordinal()].equals(saveItem[MERGEINDEX.EVENTNAMNE.ordinal()])){
										mergeEventDetailsVec.remove(j);
										break;
									}
								}
								
								
/*								if(scoreItem != null){
									saveItem[MERGEINDEX.LEAGUENAME.ordinal()] = scoreItem[SCOREINDEX.LEAGUENAME.ordinal()];
									saveItem[MERGEINDEX.SCORE.ordinal()] = scoreItem[SCOREINDEX.SCORE.ordinal()];
									saveItem[MERGEINDEX.RQPK.ordinal()] = scoreItem[SCOREINDEX.RQPANKOU.ordinal()];
									saveItem[MERGEINDEX.DXQPK.ordinal()] = scoreItem[SCOREINDEX.DXQPANKOU.ordinal()];
									saveItem[MERGEINDEX.PERIOD1HOME.ordinal()] = scoreItem[SCOREINDEX.TIME.ordinal()];
								}*/
								
								
								
								//处理比赛结束时显示错误的比赛时间 开始
								String inplaytimestr = saveItem[MERGEINDEX.PERIOD1HOME.ordinal()];
								
/*								if(inplaytimestr != null && (inplaytimestr.contains("'") || inplaytimestr.contains("中"))){
									if(scoreItem == null){
										continue;
									}
								}*/
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
									
									
	/*								Double p80home = Double.parseDouble(item[TYPEINDEX.PERIOD0HOME.ordinal()]);
									Double p80over = Double.parseDouble(item[TYPEINDEX.PERIOD0OVER.ordinal()]);
									Double p81home = Double.parseDouble(item[TYPEINDEX.PERIOD1HOME.ordinal()]);
									Double p81over = Double.parseDouble(item[TYPEINDEX.PERIOD1OVER.ordinal()]);*/
									
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
										//item[TYPEINDEX.PERIOD0HOME.ordinal()] = "0";
										
										item[TYPEINDEX.PERIOD0HOME.ordinal()] = String.format("(%.0f)", p80home) + "+" +
												String.format("(%.0f)", zhibo0home) + "=" + String.format("%.0f", p80home + zhibo0home);
										
										addTomerge = true;
									}
									
									if((p80over >0.0 && zhibo0over >0.0) || (p80over <0.0 && zhibo0over <0.0)){
										//item[TYPEINDEX.PERIOD0OVER.ordinal()] = String.format("%.0f", p80over + zhibo0over);
										
										item[TYPEINDEX.PERIOD0OVER.ordinal()] = String.format("(%.0f)", p80over) + "+" +
												String.format("(%.0f)", zhibo0over) + "=" + String.format("%.0f", p80over + zhibo0over);
										
										addTomerge = true;
									}
									else{
										//item[TYPEINDEX.PERIOD0OVER.ordinal()] = "0";
										item[TYPEINDEX.PERIOD0OVER.ordinal()] = String.format("(%.0f)", p80over) + "+" +
												String.format("(%.0f)", zhibo0over) + "=" + String.format("%.0f", p80over + zhibo0over);
										
										addTomerge = true;
									}
									
	/*								if((p81over >0.0 && zhibo1over >0.0) || (p81over <0.0 && zhibo1over <0.0)){
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
									}*/
									
									//if(addTomerge == true){
									if(true){
										
										
										if(isZhibohasGoals == true){
											item[ZHIBOINDEX.SAVED.ordinal()] = "1";
											System.out.println("merge has goals" + Arrays.toString(item));
										}
										
										//merge格式有变化，需要重新组织
										String[] mergeItem = new String[MERGEINDEX.SIZE.ordinal()];
										mergeItem[MERGEINDEX.EVENTID.ordinal()] = 		item[TYPEINDEX.EVENTID.ordinal()];
										mergeItem[MERGEINDEX.LEAGUENAME.ordinal()] = 	item[TYPEINDEX.LEAGUENAME.ordinal()];
										mergeItem[MERGEINDEX.TIME.ordinal()] = 			item[TYPEINDEX.TIME.ordinal()];
										mergeItem[MERGEINDEX.EVENTNAMNE.ordinal()] = 	item[TYPEINDEX.EVENTNAMNE.ordinal()];
										mergeItem[MERGEINDEX.SCORE.ordinal()] = 		"-";
										
										
										
										mergeItem[MERGEINDEX.P8HRES.ordinal()] = 		p80homeStr;
										mergeItem[MERGEINDEX.INP8HRES.ordinal()] = 		"0";
										mergeItem[MERGEINDEX.ZHIBOHRES.ordinal()] = 	String.format("%.0f", zhibo0home);
										mergeItem[MERGEINDEX.PERIOD0HOME.ordinal()] = 		item[TYPEINDEX.PERIOD0HOME.ordinal()];
										
										

										
										mergeItem[MERGEINDEX.P8ORES.ordinal()] = 		p80overStr;
										mergeItem[MERGEINDEX.INP8ORES.ordinal()] = 		"0";
										mergeItem[MERGEINDEX.ZHIBOORES.ordinal()] = 		String.format("%.0f", zhibo0over);
										mergeItem[MERGEINDEX.PERIOD0OVER.ordinal()] = 		item[TYPEINDEX.PERIOD0OVER.ordinal()];
										
										
										String[] scoreItem = null;
										if(scoreItem != null){
											mergeItem[MERGEINDEX.LEAGUENAME.ordinal()] = scoreItem[SCOREINDEX.LEAGUENAME.ordinal()];
											mergeItem[MERGEINDEX.SCORE.ordinal()] = scoreItem[SCOREINDEX.SCORE.ordinal()];
											mergeItem[MERGEINDEX.RQPK.ordinal()] = scoreItem[SCOREINDEX.RQPANKOU.ordinal()];
											mergeItem[MERGEINDEX.DXQPK.ordinal()] = scoreItem[SCOREINDEX.DXQPANKOU.ordinal()];
											mergeItem[MERGEINDEX.PERIOD1HOME.ordinal()] = scoreItem[SCOREINDEX.TIME.ordinal()];
										}else{
											mergeItem[MERGEINDEX.SCORE.ordinal()] = "-";
											mergeItem[MERGEINDEX.RQPK.ordinal()] = "";
											mergeItem[MERGEINDEX.DXQPK.ordinal()] = "";
										}
										
										
										
										for(int k = 0; k < mergebeforegoaldetails.size(); k++){
											if(mergeItem[MERGEINDEX.EVENTNAMNE.ordinal()].equals(mergebeforegoaldetails.elementAt(k)[MERGEINDEX.EVENTNAMNE.ordinal()])){
												
												
												mergeItem[MERGEINDEX.ZHIBOHRES.ordinal()] = mergebeforegoaldetails.elementAt(k)[MERGEINDEX.ZHIBOHRES.ordinal()];
												mergeItem[MERGEINDEX.ZHIBOORES.ordinal()] = mergebeforegoaldetails.elementAt(k)[MERGEINDEX.ZHIBOORES.ordinal()];
												break;
												
												
											}
										}
										
										
										
										
										
										
										
										


										//merge格式处理结束
										
										//mergeEventDetailsVec.add(item);
										mergeEventDetailsVec.add(mergeItem);
										
										//mergeEventDetailsVec.add(item);
									}
									
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
					
/*					String[] scoreItem = null;
					
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
					}*/
					
					
					String zhiboHome = findZhiboTeam(p8teams[0]);
					
					if(zhiboHome != null){
						String zhiboAway = findZhiboTeam(p8teams[1]);
						
						if(zhiboAway != null){
							String zhiboeventname = zhiboHome + " vs " + zhiboAway;
							
							
							//zhiboeventname = "【滚动盘】"+ zhiboeventname;
							
							//找到单式盘
							int danshiIndex = -1;
							for(int j = 0; j < mergeEventDetailsVec.size(); j++){
								if(mergeEventDetailsVec.elementAt(j)[MERGEINDEX.EVENTNAMNE.ordinal()].equals(zhiboeventname)){
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
										Double zhibo1homenow = Double.parseDouble(zhiboItemNow[ZHIBOINDEX.PERIOD1HOME.ordinal()]);
										Double zhibo1overnow = Double.parseDouble(zhiboItemNow[ZHIBOINDEX.PERIOD1OVER.ordinal()]);
										
										Double p8nogoalsbet1 = 0.0;
										Double p8nogoalsbet2 = 0.0;
										Double p8nogoalsbet3 = 0.0;										
										Double p8nogoalsbet4 = 0.0;
										
										//String[] zhiboSaveItem = ZhiboManager.getZhiboSaveItem(zhiboeventname);
										
										//if(null != zhiboSaveItem){
										
										
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
										

										Double p8danshibet3 = Double.parseDouble(p8DanshiItem[ZHIBOINDEX.PERIOD1HOME.ordinal()]);
										Double p8danshibet4 = Double.parseDouble(p8DanshiItem[ZHIBOINDEX.PERIOD1OVER.ordinal()]);

										
										
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
										

/*										Double p81homeInplayVal = Double.parseDouble(item[TYPEINDEX.PERIOD1HOME.ordinal()]);
										Double p81overInplayVal = Double.parseDouble(item[TYPEINDEX.PERIOD1OVER.ordinal()]);
										
										//P8没进球之前的和值
										p8nogoalsbet1 = p8danshibet1 + p80homeInplayVal;
										p8nogoalsbet2 = p8danshibet2 + p80overInplayVal;
										p8nogoalsbet3 = p8danshibet3 + p81homeInplayVal;
										p8nogoalsbet4 = p8danshibet4 + p81overInplayVal;*/
										
										
										boolean addTomerge = false;
										
										
										if((p8danshibet1 >0.0 && zhibo0homenow > 0.0&& p80homeInplayVal >0.0) || 
												(p8danshibet1 <0.0 && zhibo0homenow <0.0 && p80homeInplayVal < 0.0)){
											
											
											item[TYPEINDEX.PERIOD0HOME.ordinal()] = String.format("(%.0f)", p8danshibet1) + "+" + String.format("(%.0f)", p80homeInplayVal)
													+ "+" + String.format("(%.0f)", zhibo0homenow) + "=" 
													+ String.format("%.0f", p8danshibet1 + zhibo0homenow + p80homeInplayVal);
											
											addTomerge = true;
										}else{
											//item[TYPEINDEX.PERIOD0HOME.ordinal()] = "0";
											item[TYPEINDEX.PERIOD0HOME.ordinal()] = String.format("(%.0f)", p8danshibet1) + "+" + String.format("(%.0f)", p80homeInplayVal)
													+ "+" + String.format("(%.0f)", zhibo0homenow) + "=" 
													+ String.format("%.0f", p8danshibet1 + zhibo0homenow + p80homeInplayVal);
											
											addTomerge = true;
										}
										
										
										if((p8danshibet2 >0.0 && zhibo0overnow > 0.0&& p80overInplayVal >0.0) || 
												(p8danshibet2 <0.0 && zhibo0overnow <0.0 && p80overInplayVal < 0.0)){
											
											
											item[TYPEINDEX.PERIOD0OVER.ordinal()] = String.format("(%.0f)", p8danshibet2) + "+" + String.format("(%.0f)", p80overInplayVal) + "+" + 
													String.format("(%.0f)", zhibo0overnow) + "=" 
													+ String.format("%.0f", p8danshibet2 + zhibo0overnow + p80overInplayVal);
											
											addTomerge = true;
										}else{
											//item[TYPEINDEX.PERIOD0OVER.ordinal()] = "0";
											item[TYPEINDEX.PERIOD0OVER.ordinal()] = String.format("(%.0f)", p8danshibet2) + "+" + String.format("(%.0f)", p80overInplayVal) + "+" + 
													String.format("(%.0f)", zhibo0overnow) + "=" 
													+ String.format("%.0f", p8danshibet2 + zhibo0overnow + p80overInplayVal);
											
											addTomerge = true;
										}
										
										

										if(true){
											
											
											System.out.println("merge inplay add" + Arrays.toString(item));
											
											if(isZhibohasGoals == true){
												item[ZHIBOINDEX.SAVED.ordinal()] = "1";
												System.out.println("merge has goals" + Arrays.toString(item));
											}
											
											//merge格式有变化，需要重新组织
											String[] mergeItem = new String[MERGEINDEX.SIZE.ordinal()];
											mergeItem[MERGEINDEX.EVENTID.ordinal()] = 		item[ZHIBOINDEX.SAVED.ordinal()];
											mergeItem[MERGEINDEX.LEAGUENAME.ordinal()] = 	item[TYPEINDEX.LEAGUENAME.ordinal()];
											mergeItem[MERGEINDEX.TIME.ordinal()] = 			item[TYPEINDEX.TIME.ordinal()];
											mergeItem[MERGEINDEX.EVENTNAMNE.ordinal()] = 	item[TYPEINDEX.EVENTNAMNE.ordinal()];
											mergeItem[MERGEINDEX.SCORE.ordinal()] = 		"-";
											

											
											mergeItem[MERGEINDEX.P8HRES.ordinal()] = 		p8danshibet1Str;
											mergeItem[MERGEINDEX.INP8HRES.ordinal()] = 		p80homeInplayValStr;
											mergeItem[MERGEINDEX.ZHIBOHRES.ordinal()] = 	String.format("%.0f", zhibo0homenow);
											mergeItem[MERGEINDEX.PERIOD0HOME.ordinal()] = 		item[TYPEINDEX.PERIOD0HOME.ordinal()];
										
										


											mergeItem[MERGEINDEX.P8ORES.ordinal()] = 		p8danshibet2Str;
											mergeItem[MERGEINDEX.INP8ORES.ordinal()] = 		p80overInplayValStr;
											mergeItem[MERGEINDEX.ZHIBOORES.ordinal()] = 		String.format("%.0f", zhibo0overnow);
											mergeItem[MERGEINDEX.PERIOD0OVER.ordinal()] = 		item[TYPEINDEX.PERIOD0OVER.ordinal()];
											
											
											String[] scoreItem = null;
											if(scoreItem != null){
												mergeItem[MERGEINDEX.LEAGUENAME.ordinal()] = scoreItem[SCOREINDEX.LEAGUENAME.ordinal()];
												mergeItem[MERGEINDEX.SCORE.ordinal()] = scoreItem[SCOREINDEX.SCORE.ordinal()];
												mergeItem[MERGEINDEX.RQPK.ordinal()] = scoreItem[SCOREINDEX.RQPANKOU.ordinal()];
												mergeItem[MERGEINDEX.DXQPK.ordinal()] = scoreItem[SCOREINDEX.DXQPANKOU.ordinal()];
												mergeItem[MERGEINDEX.PERIOD1HOME.ordinal()] = scoreItem[SCOREINDEX.TIME.ordinal()];
											}else{
												mergeItem[MERGEINDEX.SCORE.ordinal()] = "-";
												mergeItem[MERGEINDEX.RQPK.ordinal()] = "";
												mergeItem[MERGEINDEX.DXQPK.ordinal()] = "";
											}
											
											
											
											
											if(danshiIndex == -1){
												mergeEventDetailsVec.add(mergeItem);
											}else{
												
												
												
												
												
												
												mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.EVENTID.ordinal()] = mergeItem[MERGEINDEX.EVENTID.ordinal()];
												
												mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.P8HRES.ordinal()] = mergeItem[MERGEINDEX.P8HRES.ordinal()];
												
												
												mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.INP8HRES.ordinal()] = mergeItem[MERGEINDEX.INP8HRES.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.ZHIBOHRES.ordinal()] = mergeItem[MERGEINDEX.ZHIBOHRES.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.PERIOD0HOME.ordinal()] = mergeItem[MERGEINDEX.PERIOD0HOME.ordinal()];
											
											


												mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.P8ORES.ordinal()] = mergeItem[MERGEINDEX.P8ORES.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.INP8ORES.ordinal()] = mergeItem[MERGEINDEX.INP8ORES.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.ZHIBOORES.ordinal()] = mergeItem[MERGEINDEX.ZHIBOORES.ordinal()];
												mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.PERIOD0OVER.ordinal()] = mergeItem[MERGEINDEX.PERIOD0OVER.ordinal()];
												
												
												
												for(int k = 0; k < mergebeforegoaldetails.size(); k++){
													if(mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.EVENTNAMNE.ordinal()].equals(mergebeforegoaldetails.elementAt(k)[MERGEINDEX.EVENTNAMNE.ordinal()])){
														
														
														mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.ZHIBOHRES.ordinal()] = mergebeforegoaldetails.elementAt(k)[MERGEINDEX.ZHIBOHRES.ordinal()];
														mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.ZHIBOORES.ordinal()] = mergebeforegoaldetails.elementAt(k)[MERGEINDEX.ZHIBOORES.ordinal()];
														
														mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.INP8HRES.ordinal()] = mergebeforegoaldetails.elementAt(k)[MERGEINDEX.INP8HRES.ordinal()];
														mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.INP8ORES.ordinal()] = mergebeforegoaldetails.elementAt(k)[MERGEINDEX.INP8ORES.ordinal()];
														
														break;
													}
												}
												
												
												
											}


											
											
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
				mergeEventDetailsVec.elementAt(i)[MERGEINDEX.HGHRES.ordinal()] = "0";
				mergeEventDetailsVec.elementAt(i)[MERGEINDEX.HGORES.ordinal()] = "0";
			}
			
			
			
/*		
			for(int i = 0; i < p8events.size(); i++){
				item = p8events.elementAt(i).clone();
				
				p8eventsname = item[TYPEINDEX.EVENTNAMNE.ordinal()];
				
				
				
				if(!p8eventsname.contains("滚动盘")){
					
				
					
					
					
					
					p8teams = p8eventsname.split("-vs-");

					String zhiboHome = findZhiboTeam(p8teams[0]);
					
					if(zhiboHome != null){
						String zhiboAway = findZhiboTeam(p8teams[1]);
						
						if(zhiboAway != null){
							String zhiboeventname = zhiboHome + " vs " + zhiboAway;
							
							
							
							
							//找到合并
							int danshiIndex = -1;
							for(int j = 0; j < mergeEventDetailsVec.size(); j++){
								if(mergeEventDetailsVec.elementAt(j)[MERGEINDEX.EVENTNAMNE.ordinal()].equals(zhiboeventname)){
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
										mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.HGHRES.ordinal()] = "0";
										mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.HGORES.ordinal()] = "0";
									}else{
										
										String p0hstr = hgDetails.elementAt(findhg)[HGINDEX.PERIOD0HOME.ordinal()];
										
										mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.HGHRES.ordinal()] = p0hstr;
										
										String p0ostr = hgDetails.elementAt(findhg)[HGINDEX.PERIOD0OVER.ordinal()];
										
										mergeEventDetailsVec.elementAt(danshiIndex)[MERGEINDEX.HGORES.ordinal()] = p0ostr;
									}
									
									
								}
								
							}
							
							
							

					
					
						
					}
					
					continue;
				}
				
			
				}


				
			}*/
			//处理皇冠   结束
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	

	
	

	
	
	
	
	
    public static  void sortEventDetails(){
    	
    	try{
    		
/*    		System.out.println("before sort");
    		
    		for(int k = 0; k<mergeEventDetailsVec.size(); k++ ){
    			
    			String[] outRow = mergeEventDetailsVec.elementAt(k);
    			
    			System.out.println(outRow[0] + "," +outRow[1] + "," + outRow[2] + "," +outRow[3] + "," +outRow[4] + "," +outRow[5] + "," +
    					outRow[6] + "," + outRow[7]);
    		}*/
    		
    		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); 

    		
    		
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
    		
    		String currentTime = df.format(System.currentTimeMillis());
    		
        	if(mergeEventDetailsVec.size() != 0){
        		
        		Vector<String[]> highShowVec = new Vector<String[]>();
        		

        		
        		
/*        		System.out.println("after remove event details:");
        		
        		for(int k = 0; k<mergeEventDetailsVec.size(); k++ ){
        			
        			String[] outRow = mergeEventDetailsVec.elementAt(k);
        			
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
            	
            	
            	

            	
            	
            	if(mergeEventDetailsVec.size() != 0){

            		Collections.sort(mergeEventDetailsVec, ct);
            	}
            	
        		
            	
            	

            	
            	
            	for(int i = 0; i < mergeEventDetailsVec.size(); i++){
        			String currentTimeArray[] = currentTime.split(" ");
        			
        			long time = Long.parseLong(mergeEventDetailsVec.elementAt(i)[TYPEINDEX.TIME.ordinal()]);
        			
        			String eventTimeArray[] = df.format(time).split(" ");
        			
        			String timeStr = "";
        			
        			if(currentTimeArray[0].contains(eventTimeArray[0])){
        				timeStr = eventTimeArray[1];
        			}else{
        				timeStr = df.format(time);
        			}
        			
        			
        			mergeEventDetailsVec.elementAt(i)[TYPEINDEX.TIME.ordinal()] = timeStr;
            	}
            	

        	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	

    	
    }
	
    
	public static String getchecklist(){
		try{
			
			
			
			JSONObject gameObj = new JSONObject(checkList);
			
			String res = gameObj.toString();
			
			

			
			return res;
			
		}catch(Exception e){
			e.printStackTrace();
			return "[]";
		}
	}
	
	
	
	public static String findZhiboTeam(String p8team){
		
		String zhiboteam = checkList.get(p8team);
		
		return zhiboteam;
		
	}
	
	
	public static String findScoreTeambyzhiboteam(String zhiboteam){
		
		try{
			Set<String>kset=checkList.keySet();
			for(String ks:kset){
			    if(zhiboteam.equals(checkList.get(ks))){
			    	String scoreteam =  findScoreTeam(ks);
			    	if(null != scoreteam){
			    		return scoreteam;
			    	}
			    }
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	
	private static String findScoreTeam(String p8team){
		
		String scoreteam = ScoreMergeManager.findScoreTeam(p8team);
		
		return scoreteam;
		
	}
	
	private static String findHGTeam(String p8team){
		
		String hgteam = HGMergeManager.findHGTeam(p8team);
		
		return hgteam;
		
	}
	
	public static void setP8Txt(){
		mergeWnd.setP8Txt(p8SelectedRow);
	}
	
	public static void setZhiboTxt(){
		mergeWnd.setZhiboTxt(zhiboSelectedRow);
	}
	
}
