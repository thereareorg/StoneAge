package HGclient;


import java.awt.Color;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Date;
import java.util.Stack;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.json.*;

import Mail.MailManager;
import P8.EventNameCompare;
import P8.EventsDetailsWindow;
import P8.MyCompare;
import P8.PreviousDataManager;
import P8.PreviousDataWindow;
import P8.StoneAge;

import java.util.Vector;
import java.util.Comparator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
  



























import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NodeList; 
import org.w3c.dom.Node;


public class HGclienthttp {
	
	
	CloseableHttpClient httpclient = null;
	RequestConfig requestConfig = null;
	HttpClientContext clientContext = null;
	
	
	 {
	    requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
	    requestConfig = RequestConfig.copy(requestConfig).setRedirectsEnabled(false).build();//禁止重定向 ， 以便获取cookieb18
	    //requestConfig = RequestConfig.copy(requestConfig).setConnectTimeout(autoBet.timeOut).setConnectionRequestTimeout(autoBet.timeOut).setSocketTimeout(autoBet.timeOut).build();//设置超时
	        httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
	 }
	
	 
	 

	 
	 
	
	static int defaultTimeout = 15000;
	
	public static   Vector<String[]> eventDetailsVec = new Vector<String[]>();
	

	public static Vector<GameDetails> gameDetailsVec = new Vector<GameDetails>();
	
	
	public static Vector<GameDetails> daysGameDetailsVec = new Vector<GameDetails>();
	
	
	public static  GameDetailsWindow gamedetailswnd = new GameDetailsWindow();
	
	public static GamePreviousDetailsWindow gamepdwnd = new GamePreviousDetailsWindow();
	
	public static DXQPreviousDetailsWindow dxqpdwnd = new DXQPreviousDetailsWindow();
	
	public static DXQAnalysisWindow dxqAnswnd = new DXQAnalysisWindow();
	
	public static RatioAnalysisWindow ratioAnswnd = new RatioAnalysisWindow();
	
	public static DXQdetailsWindow dxqdetailswnd = new DXQdetailsWindow();
	
	
	
    Vector<Long> lastTenRequestTime = new Vector<Long>();
    long avgRequestTime = 0;    
    boolean bcalcRequestTime = true;
    boolean bneedChangeLine = false;
   
   
    long lastChangeLineTime = 0;
   
    String strCookies = "";
   
    int requestFailTimes = 0;
    long lastFailtime = 0;
   
    boolean isNeedRelogin = false;
   
    public boolean isInRelogin = false;
    
	 String ADDRESS = "";
	 String ACCOUNT = "";
	 String PWD = "";
	 String SECURITYCODE = "";
	 
	 String user_id = "";
	 String mtype = "3";
	 String cid = "";
	 int time_zone = 0;
	 
	 boolean bLogin = false;
	 public static Vector<String> failedCatchAccount = new Vector<String>();
	 
	 static Vector<String> showLeagueName = new Vector<String>();
	 
    private static ReadWriteLock lockeFinalEventsDetails = new ReentrantReadWriteLock();
    
    private static ReadWriteLock lockeSuccessTime = new ReentrantReadWriteLock();
    
    public static Vector<String[]> finalEventDetailsVec = new Vector<String[]>();

    public static String successTime = "";
	 
	public static void initShowLeagueName(){
		showLeagueName.add("德国 - 德甲");
		showLeagueName.add("欧足联 - 冠军联赛");
		showLeagueName.add("西班牙 - 西甲");
		showLeagueName.add("意大利 - 甲级联赛");
		showLeagueName.add("法国 - 甲级联赛");
		showLeagueName.add("英格兰 - 超级联赛");
		showLeagueName.add("欧足联 - 欧罗巴联赛");
	}
	
	
	public static void showdxqanswnd(boolean b){
		dxqAnswnd.setVisible(b);
	}
	
	
	public static void showratioanswnd(boolean b){
		ratioAnswnd.setVisible(b);
	}
	
	
	public static void showdetailswnd(boolean b){
		gamedetailswnd.setVisible(b);
	}
	
	public  void updateGamedetailsvec(){
		gamedetailswnd.updateGameDetailsVec(gameDetailsVec);
	}
	
	
	public static void showdxqwnd(boolean b){
		dxqdetailswnd.setVisible(b);
	}
	
	
	public void updateDXQdetailsvec(){
		dxqdetailswnd.updateGameDetailsVec(gameDetailsVec);
	}
	
	
	
	
	
	public static void showpdwnd(boolean b){
		gamepdwnd.setVisible(b);
	}
	
	
	public static void showdxqpdwnd(boolean b){
		dxqpdwnd.setVisible(b);
	}
	
	public  static void updateGamepdetailsvec(){
		
		
		
		try{
			
			
			String date = gamepdwnd.getmpdate();
			
			
			
			
			
			SimpleDateFormat dfmin = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			Calendar currentTime = Calendar.getInstance();
			
			String folder = date;
			
			Vector<GameDetails> gamePreviousDetailsVec = new Vector<GameDetails>();
			
			
			//读取改目录下所有文件:
			File filefolder = new File("hgdata/" + folder + "/");
			
			if(!filefolder.exists()){
				gamepdwnd.updateGameDetailsVec(gamePreviousDetailsVec);
				return;
			}
			
			File flist[] = filefolder.listFiles();
			
			int linenum = 0;
			
			for(File f : flist){
				if(f.isDirectory()){
					System.out.println("fuck directory here");
				}else{
					//System.out.println(f.getAbsolutePath());
					//
					GameDetails gamedetails = new GameDetails();
					
					BufferedReader freader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
					
					String str = "";
					
					linenum = 0;
					
					while ((str = freader.readLine()) != null) {
						
						String[] contents = str.split(",");
						
						if(linenum == 0){
							gamedetails.eventid = contents[0];
							gamedetails.datetime = contents[1];
							gamedetails.league = contents[2];
							gamedetails.teamh = contents[3];
							gamedetails.teamc = contents[4];
							gamedetails.filename = f.getName();
							if(contents.length > 5){
								gamedetails.gameresult = contents[5];
							}
							//System.out.println(f.getName());
						}else{
							gamedetails.addodds(contents);
						}
						
						linenum = linenum + 1;
						
							
						}
					
					
					gamePreviousDetailsVec.add(gamedetails);
					
				}
			}
			
			gamepdwnd.updateGameDetailsVec(gamePreviousDetailsVec);
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
		
		
		
		
		

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public  static void updateDXQpdetailsvec(){
		
		
		
		try{
			
			
			String date = dxqpdwnd.getmpdate();
			
			
			
			
			
			SimpleDateFormat dfmin = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			Calendar currentTime = Calendar.getInstance();
			
			String folder = date;
			
			Vector<GameDetails> gamePreviousDetailsVec = new Vector<GameDetails>();
			
			
			//读取改目录下所有文件:
			File filefolder = new File("hgdata/" + folder + "/");
			
			if(!filefolder.exists()){
				dxqpdwnd.updateGameDetailsVec(gamePreviousDetailsVec);
				return;
			}
			
			File flist[] = filefolder.listFiles();
			
			int linenum = 0;
			
			for(File f : flist){
				if(f.isDirectory()){
					System.out.println("fuck directory here");
				}else{
					//System.out.println(f.getAbsolutePath());
					//
					GameDetails gamedetails = new GameDetails();
					
					BufferedReader freader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
					
					String str = "";
					
					linenum = 0;
					
					while ((str = freader.readLine()) != null) {
						
						String[] contents = str.split(",");
						
						if(linenum == 0){
							gamedetails.eventid = contents[0];
							gamedetails.datetime = contents[1];
							gamedetails.league = contents[2];
							gamedetails.teamh = contents[3];
							gamedetails.teamc = contents[4];
							gamedetails.filename = f.getName();
							
							if(contents.length > 5){
								gamedetails.gameresult = contents[5];
							}
							
							//System.out.println(f.getName());
						}else{
							gamedetails.addodds(contents);
						}
						
						linenum = linenum + 1;
						
							
						}
					
					
					gamePreviousDetailsVec.add(gamedetails);
					
				}
			}
			
			dxqpdwnd.updateGameDetailsVec(gamePreviousDetailsVec);
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
		
		
		
		
		

	}
	
	
	

	
	
	public static boolean recoverGamedetailsVecfromefile(){
		
		try{
			
			SimpleDateFormat dfmin = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			Calendar currentTime = Calendar.getInstance();
			
			String folder = "";
			
			if(currentTime.get(Calendar.HOUR_OF_DAY) > 12){
				folder = dfmin.format(currentTime.getTimeInMillis());
			}else{
				currentTime.add(Calendar.DAY_OF_MONTH, -1);
				folder = dfmin.format(currentTime.getTimeInMillis());
			}
			
			
			//读取改目录下所有文件:
			File filefolder = new File("hgdata/" + folder + "/");
			
			if(!filefolder.exists()){
				return true;
			}
			
			File flist[] = filefolder.listFiles();
			
			int linenum = 0;
			
			for(File f : flist){
				if(f.isDirectory()){
					//System.out.println("fuck directory here");
				}else{
					//System.out.println(f.getAbsolutePath());
					//
					GameDetails gamedetails = new GameDetails();
					
					BufferedReader freader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
					
					String str = "";
					
					linenum = 0;
					
					while ((str = freader.readLine()) != null) {
						
						String[] contents = str.split(",");
						
						if(linenum == 0){
							gamedetails.eventid = contents[0];
							gamedetails.datetime = contents[1];
							gamedetails.league = contents[2];
							gamedetails.teamh = contents[3];
							gamedetails.teamc = contents[4];
							
							if(contents.length > 5){
								gamedetails.gameresult = contents[5];
							}
							
							gamedetails.filename = f.getName();
							//System.out.println(f.getName());
						}else{
							gamedetails.addodds(contents);
						}
						
						linenum = linenum + 1;
						
							
						}
					
					
					gameDetailsVec.add(gamedetails);
					
					if(null != freader){
						freader.close();
					}
					
					
				}
				
				
				
				
			}
			
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		
	}
	

	
	
	
	public static void constructDaysGaemDetails(){
		
		String startdate = dxqAnswnd.getmpstartdate();
		String enddate = dxqAnswnd.getmpenddate();
		
		SimpleDateFormat dfday = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		
		
		
		try{
			
			Calendar currentTime = Calendar.getInstance();
			
			java.util.Date currentdate = dfday.parse(enddate);
			
			java.util.Date sdate = dfday.parse(startdate);
			
			if(sdate.getTime() > currentdate.getTime()){
				return;
			}
			
			
			
			
			currentTime.setTime(currentdate);
			currentTime.add(Calendar.DAY_OF_YEAR, 1);
			
			enddate = dfday.format(currentTime.getTimeInMillis());
			
			
			
			
			
			
			
			String date = startdate;
			
			
			Vector<GameDetails> gamePreviousDetailsVec = new Vector<GameDetails>();
			
			
			
			while(!enddate.equals(date)){
				
				

				
				String folder = date;
				
				
				
				
				//读取改目录下所有文件:
				File filefolder = new File("hgdata/" + folder + "/");
				
				if(!filefolder.exists()){
					//dxqAnswnd.updateGameDetailsVec(gamePreviousDetailsVec);
					
					currentdate = dfday.parse(date);
					currentTime.setTime(currentdate);
					currentTime.add(Calendar.DAY_OF_YEAR, 1);
					
					date = dfday.format(currentTime.getTimeInMillis());
					
					continue;
				}
				
				File flist[] = filefolder.listFiles();
				
				int linenum = 0;
				
				for(File f : flist){
					if(f.isDirectory()){
						//System.out.println("fuck directory here");
					}else{
						
						//
						GameDetails gamedetails = new GameDetails();
						
						BufferedReader freader = null;
						int last1danshiindex = -1;
						int firstinplayindex = -1;
						try{
							freader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
							
							String str = "";
							
							linenum = 0;

							
							while ((str = freader.readLine()) != null) {
								
								String[] contents = str.split(",");
								
								if(linenum == 0){
									
									gamedetails.eventid = contents[0];
									gamedetails.datetime = contents[1];
									gamedetails.league = contents[2];
									gamedetails.teamh = contents[3];
									gamedetails.teamc = contents[4];
									gamedetails.filename = f.getName();
									
									if(contents.length > 5){
										gamedetails.gameresult = contents[5];
									}

								}else{
									gamedetails.addodds(contents);
									
									if(contents[HGODDSINDEX.PRIOTITY.ordinal()].equals("1") && contents[HGODDSINDEX.TYPE.ordinal()].equals("danshi")){
										last1danshiindex = linenum-1;
									}
									
									if(contents[HGODDSINDEX.PRIOTITY.ordinal()].equals("1") && contents[HGODDSINDEX.TYPE.ordinal()].equals("inplay") && firstinplayindex == -1){
										firstinplayindex = linenum -1;
									}
									
								}
								
								linenum = linenum + 1;

								}
						}catch(Exception e){
							e.printStackTrace();
						}finally{
							if(null != freader){
								freader.close();
							}
						}
						

						
						if(gamedetails.getodds().elementAt(gamedetails.getodds().size()-1)[HGODDSINDEX.TYPE.ordinal()].equals("inplay")){
							
							GameDetails gamedetails1 = new GameDetails();
							gamedetails1.eventid = gamedetails.eventid;
							gamedetails1.datetime = gamedetails.datetime;
							gamedetails1.league = gamedetails.league;
							gamedetails1.teamh = gamedetails.teamh;
							gamedetails1.teamc = gamedetails.teamc;
							gamedetails1.filename = gamedetails.filename;
							gamedetails1.gameresult = gamedetails.gameresult;
							
							gamedetails1.addodds(gamedetails.getodds().elementAt(last1danshiindex));
							gamedetails1.addodds(gamedetails.getodds().elementAt(firstinplayindex));
							
							//System.out.println(f.getAbsolutePath());
							gamePreviousDetailsVec.add(gamedetails1);
						}
						
						
					}
				}
				
				
				currentdate = dfday.parse(date);
				currentTime.setTime(currentdate);
				currentTime.add(Calendar.DAY_OF_YEAR, 1);
				
				date = dfday.format(currentTime.getTimeInMillis());

			}
			
			
			dxqAnswnd.updateGameDetailsVec(gamePreviousDetailsVec);
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	
	
	
	public static void constructDaysGaemDetailsForRatioAns(){
		
		String startdate = ratioAnswnd.getmpstartdate();
		String enddate = ratioAnswnd.getmpenddate();
		
		int comparemins = ratioAnswnd.getComparemins();
		
		SimpleDateFormat dfday = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		
		
		
		try{
			
			Calendar currentTime = Calendar.getInstance();
			
			java.util.Date currentdate = dfday.parse(enddate);
			
			java.util.Date sdate = dfday.parse(startdate);
			
			if(sdate.getTime() > currentdate.getTime()){
				return;
			}
			
			
			
			
			currentTime.setTime(currentdate);
			currentTime.add(Calendar.DAY_OF_YEAR, 1);
			
			enddate = dfday.format(currentTime.getTimeInMillis());
			
			
			
			
			
			
			
			String date = startdate;
			
			
			Vector<GameDetails> gamePreviousDetailsVec = new Vector<GameDetails>();
			
			
			
			long currentTimetmp = System.currentTimeMillis();
			
			Calendar eventtime = Calendar.getInstance();
			
			SimpleDateFormat dfmin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			SimpleDateFormat dfsec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			
			GameDetails gamedetails = null;
			
			
			
			java.util.Date startTimeDate = null;
			
			String folder = "";
			
			
			while(!enddate.equals(date)){
				
				

				
				folder = date;
				
				
				
				
				//读取改目录下所有文件:
				File filefolder = new File("hgdata/" + folder + "/");
				
				if(!filefolder.exists()){
					//dxqAnswnd.updateGameDetailsVec(gamePreviousDetailsVec);
					
					currentdate = dfday.parse(date);
					currentTime.setTime(currentdate);
					currentTime.add(Calendar.DAY_OF_YEAR, 1);
					
					date = dfday.format(currentTime.getTimeInMillis());
					
					continue;
				}
				
				File flist[] = filefolder.listFiles();
				
				int linenum = 0;
				
				for(File f : flist){
					if(f.isDirectory()){
						//System.out.println("fuck directory here");
					}else{
						
						//
						gamedetails = new GameDetails();
						
						
						BufferedReader freader = null;
						
						try{
							
							freader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
							
							String str = "";
							
							linenum = 0;

							
							while ((str = freader.readLine()) != null) {
								
								String[] contents = str.split(",");
								
								if(linenum == 0){
									
									gamedetails.eventid = contents[0];
									gamedetails.datetime = contents[1];
									gamedetails.league = contents[2];
									gamedetails.teamh = contents[3];
									gamedetails.teamc = contents[4];
									gamedetails.filename = f.getName();
									
									if(contents.length > 5){
										gamedetails.gameresult = contents[5];
									}

								}else{
									gamedetails.addodds(contents);
									

								}
								
								linenum = linenum + 1;

								}
							
						}catch(Exception e){
							e.printStackTrace();
						}finally{
							if(null != freader){
								freader.close();
							}
						}
						
						
						
						//计算水位差值
						
						
						currentTimetmp = System.currentTimeMillis();
						
						eventtime = Calendar.getInstance();
						
						
						
						
						
						
						
						
						startTimeDate = dfmin.parse(gamedetails.datetime);
						
						eventtime.setTime(startTimeDate);
						

						
						
						
						int latestOdd = -1;
						
						if(currentTimetmp > eventtime.getTimeInMillis()){
							
							String[] latestodds = null;
							
							Long searchTime = eventtime.getTimeInMillis() - comparemins * 60 * 1000;
							
							
							
							
							//盘口
							for(int j = gamedetails.getodds().size() - 1; j >=0 ; j--){
								
								String[] odds = gamedetails.getodds().elementAt(j);
								
								
								
								if(odds[HGODDSINDEX.PRIOTITY.ordinal()].equals("1") && odds[HGODDSINDEX.TYPE.ordinal()].equals("danshi") &&latestOdd == -1){
									latestOdd = j;
									
									latestodds = gamedetails.getodds().elementAt(latestOdd);
									
									gamedetails.currentpankou = latestodds[HGODDSINDEX.PANKOU.ordinal()];
									
									gamedetails.currentscore = latestodds[HGODDSINDEX.O.ordinal()];
									
									
									
								}
								
								Calendar oddtime = Calendar.getInstance();
								java.util.Date oddtimedate = dfmin.parse(odds[HGODDSINDEX.TIME.ordinal()]);
								oddtime.setTime(oddtimedate);
								
								if(searchTime > oddtime.getTimeInMillis() &&  odds[HGODDSINDEX.TYPE.ordinal()].equals("danshi")){
									
									if(latestOdd == -1){
										//System.out.println(gameDetailsVec.elementAt(i).teamh + "vs" + gameDetailsVec.elementAt(i).teamc);
										break;
									}
									
									if(!odds[HGODDSINDEX.PANKOU.ordinal()].equals(gamedetails.getodds().elementAt(latestOdd)[HGODDSINDEX.PANKOU.ordinal()])){
										continue;
									}
									
									
									if(gamedetails.getodds().elementAt(j).length < 9){
										
										
										continue;
									}
									
									double odd1;
									double odd2;
									
									int res;
									
									
									if(!gamedetails.getodds().elementAt(latestOdd)[HGODDSINDEX.HODD.ordinal()].equals("none") && !gamedetails.getodds().elementAt(j)[HGODDSINDEX.HODD.ordinal()].equals("none")){
										odd1 = Double.parseDouble(gamedetails.getodds().elementAt(latestOdd)[HGODDSINDEX.HODD.ordinal()]);
										odd2 = Double.parseDouble(gamedetails.getodds().elementAt(j)[HGODDSINDEX.HODD.ordinal()]);
										
										res = (int)(odd1*100) - (int)(odd2*100);
										
										gamedetails.pankouh = res;
									}


									break;
									
								}
								
								
							}
							//盘口结束
							
							
							
							//大小球
							
								
							for(int j = gamedetails.getodds().size() - 1; j >=0 ; j--){
								
								String[] odds = gamedetails.getodds().elementAt(j);
								
								if(latestOdd == -1){
									//System.out.println(gameDetailsVec.elementAt(i).teamh + "vs" + gameDetailsVec.elementAt(i).teamc);
									break;
								}
								
								
								
								Calendar oddtime = Calendar.getInstance();
								java.util.Date oddtimedate = dfmin.parse(odds[HGODDSINDEX.TIME.ordinal()]);
								oddtime.setTime(oddtimedate);
								
								if(searchTime > oddtime.getTimeInMillis() &&  odds[HGODDSINDEX.TYPE.ordinal()].equals("danshi")){
									
									
									
									if(!odds[HGODDSINDEX.O.ordinal()].equals(gamedetails.getodds().elementAt(latestOdd)[HGODDSINDEX.O.ordinal()])){
										continue;
									}
									
									
									if(gamedetails.getodds().elementAt(j).length < 9){
										/*System.out.println(Arrays.toString(gamedetails.getodds().elementAt(j)));
										System.out.println("fuck fuck");*/
										
										continue;
									}
									
									double odd1;
									double odd2;
									
									int res;
									
									

									
									if(!gamedetails.getodds().elementAt(latestOdd)[HGODDSINDEX.OODD.ordinal()].equals("none") && !gamedetails.getodds().elementAt(j)[HGODDSINDEX.OODD.ordinal()].equals("none")){
										odd1 = Double.parseDouble(gamedetails.getodds().elementAt(latestOdd)[HGODDSINDEX.OODD.ordinal()]);
										odd2 = Double.parseDouble(gamedetails.getodds().elementAt(j)[HGODDSINDEX.OODD.ordinal()]);

										res = (int)(odd1*100) - (int)(odd2*100);
										
										gamedetails.ouh = res;
									}
									

									
									break;
									
								}
								
								
							}
							//大小球结束
							
							
							
							

							
						}
						
						
						
						//计算水位差值结束
						
						
						
						
						
						
						
						
						if(gamedetails.pankouh != -1000 || gamedetails.ouh != -1000){
							
							GameDetails gamedetails1 = new GameDetails();
							gamedetails1.eventid = gamedetails.eventid;
							gamedetails1.datetime = gamedetails.datetime;
							gamedetails1.league = gamedetails.league;
							gamedetails1.teamh = gamedetails.teamh;
							gamedetails1.teamc = gamedetails.teamc;
							gamedetails1.filename = gamedetails.filename;
							gamedetails1.gameresult = gamedetails.gameresult;
							gamedetails1.currentpankou = gamedetails.currentpankou;//rangqiupan pankou
							gamedetails1.currentscore = gamedetails.currentscore;//dxq pankou
							gamedetails1.pankouh = gamedetails.pankouh;
							gamedetails1.ouh = gamedetails.ouh;
							

							gamePreviousDetailsVec.add(gamedetails1);
						}
						

						
					}
				}
				
				
				currentdate = dfday.parse(date);
				currentTime.setTime(currentdate);
				currentTime.add(Calendar.DAY_OF_YEAR, 1);
				
				date = dfday.format(currentTime.getTimeInMillis());

			}
			
			
			ratioAnswnd.updateGameDetailsVec(gamePreviousDetailsVec);
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
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
    
    
	public static void setGrabStext(String str){

		SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm:ss");// 设置日期格式
		
		String timeStr = df.format(System.currentTimeMillis());

		gamedetailswnd.setStateText(timeStr + ":" + str);

	}
	
	public static void setGrabColor(Color cr){
		
		gamedetailswnd.setStateColor(cr);
	
	}
    
    
    public static void copyTofinalEventsDetails(){
    	
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
		
		
		long currentTimeL = System.currentTimeMillis();
    	
		String currentTime = df.format(currentTimeL);
    	
    	lockeFinalEventsDetails.writeLock().lock();
    	
    	if(finalEventDetailsVec.size() != 0){
    		finalEventDetailsVec.clear();
    	}
    	
    	for(int i = 0; i < eventDetailsVec.size(); i++ ){
    		finalEventDetailsVec.add(eventDetailsVec.elementAt(i).clone());
    	}

    	
    	lockeFinalEventsDetails.writeLock().unlock();
    	
    	lockeSuccessTime.writeLock().lock();
    	successTime = currentTime + " 成功";
    	lockeSuccessTime.writeLock().unlock();
    }
    
    
    public static Vector<String[]> getFinalEventsDetails(){
    	Vector<String[]> vec = new Vector<String[]>();
    	lockeFinalEventsDetails.readLock().lock();
    	
    	for(int i = 0; i < finalEventDetailsVec.size(); i++){
    		vec.add(finalEventDetailsVec.elementAt(i).clone());
    	}
    	
    	//vec = (Vector<String[]>)finalEventDetailsVec.clone();
    	lockeFinalEventsDetails.readLock().unlock();
    	return vec;
    }
    
    
    public static String getSuccessTime(){
    	String successTimetmp = "";
    	lockeSuccessTime.readLock().lock();
    	successTimetmp = successTime;
    	lockeSuccessTime.readLock().unlock();
    	return successTimetmp;
    }

	 
	 
	public static  void clearEventsDetails(){
		if(eventDetailsVec.size() != 0){
			eventDetailsVec.clear();
		}
	}
	 
	
	
	public  static void clearfailedCatchAccount(){
		if(failedCatchAccount.size() != 0){
			failedCatchAccount.clear();
		}
	}
	
	public static void addFailedCatchAccount(String acc){
		
		for(int i = 0; i <failedCatchAccount.size(); i++ ){
			if(failedCatchAccount.elementAt(i).contains(acc)){
				return;
			}
		}
		failedCatchAccount.add(acc);
	}
	
	public static void removeFailedAccount(String acc){
		for(int i = 0; i <failedCatchAccount.size(); i++ ){
			if(failedCatchAccount.elementAt(i).contains(acc)){
				failedCatchAccount.remove(i);
				return;
			}
		}
	}
	
	public static boolean isfailedAccountEmpty(){
		return failedCatchAccount.size() == 0;
	}
	
	
	
	
	
	
	
	
	
	public String getAccount(){
		return ACCOUNT;
	}
    
    
	public boolean islogin(){
		return bLogin;
	}	 
	
	public void setIslogin(boolean b){
		bLogin = b;
	}
	
	public  void setLoginParams(String address, String account, String pwd){
		ADDRESS = address;
		ACCOUNT = account;
		PWD = pwd;
	}
	
	
	public void removelastdayevents(){
		
		try{
			Calendar lastdaydeadline = Calendar.getInstance();
			
			lastdaydeadline.set(Calendar.HOUR_OF_DAY, 12);
			
			lastdaydeadline.set(Calendar.MINUTE, 0);
			
			Long currenttime = System.currentTimeMillis();
			
			SimpleDateFormat dfmin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			//System.out.println(dfmin.format(lastdaydeadline.getTimeInMillis()));
			
			if(currenttime > lastdaydeadline.getTimeInMillis()){
				
			//	for(GameDetails game : gameDetailsVec){
				
				for(Iterator<GameDetails> iterator = gameDetailsVec.iterator(); iterator.hasNext();){
					GameDetails tmpgame = iterator.next();
					java.util.Date startTimeDate = dfmin.parse(tmpgame.datetime);
					
					if(startTimeDate.getTime() < lastdaydeadline.getTimeInMillis() && !tmpgame.gameresult.equals("")){
						
						System.out.println(dfmin.format(System.currentTimeMillis()) + "删除比赛:" + tmpgame.datetime + tmpgame.teamh + "vs" + tmpgame.teamc);
						iterator.remove();
						//gameDetailsVec.remove(game);
					}
					
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		

		
		
		
	}
	 
    
    public int login(){// 1成功	2维护   -1失败
    	
    	try{
    		
    		
    		
        	String loginLine = ADDRESS;
        	
        	String res = doGet(loginLine, "", "");
        	
        	
        	System.out.println("address" + ADDRESS + "        account:" + ACCOUNT + "    pwd:" + PWD);
        	
        	
        	if(res != null && res.contains("Welcome")){
        		res = doGet(loginLine + "app/member/", "", "");
        		
        		if(res.contains("連對方的資料")){
        			
        			res = doGet(ADDRESS + "iovation/vindex.php", "", "");
        			
    		        List<NameValuePair> params = new ArrayList<NameValuePair>();		  
    		        
    		        params.add(new BasicNameValuePair("username", ACCOUNT));
    		        params.add(new BasicNameValuePair("passwd", PWD));
    		        params.add(new BasicNameValuePair("auto", "CDCAAH"));
    		        params.add(new BasicNameValuePair("langx", "zh-cn"));
    		        params.add(new BasicNameValuePair("blackbox", "0400YF6d92uvKm0HcCiyOFFxUNbEtcBcrNWm7tjCQkotdjQw5xWQZwLvBMyYsEeVxkp8AsEb5KdHtSVIoHD9oBcKvSjz#xHPtjuExyrUMidKQPdDdd59r5gHsk3Jqo5kIgFTWgT#Kx4Epa2zQm#3qhD9kgWQTC0m/HvQziHA3t4riiOkovqko/fSmkNYIad05MNb8cFCb3XpK0LXm0XBtvk2RW2s0fO33l4#I5kdy9nogBJzJxGFLwTACkxFXRiZcQcAnPGnG3CqgLln#4KLswTyWhDAstLN4US5V97h50/5jKuksZ#gFcc8aSfcCd7fI2wGBY4wR2MiMzmg5dRbGWPI8M6GAfCDvUlifMI371dvXAlCeVnfbH9JZylCz8QbOZkvGRHvjriYIbIg9HUSBfXWuKXJErkyaWn5BDFLHM#5G3JxvQfkiMeQJwpKzgpVgoHiiKr8#cIUNmwDMCagJZpV4kcOjS9hgMLfbLOSjLND7KBtrNHzt95ePjtsUScOr3#mWrjg3IZNlqx9izplUCi1HTOpvCmZZrwjRMXJ9IEnfRv4X35U0eVzPZLWOlxpt0YU9cmKrLQxcxA5OE2KkOZe/0jXzk77ILZ/eUsQ7RNrLro1kTKIs1496YkpIh3A707lm2e25SQbo1NCGnRFJ9gtT0iED#cGuWwPkTvlqBdC12PmY1XpDGBtyWF8hRwW7Jsss29L0sFkQPnkjHloApNDPJIEFaG7TsuZF23lTgKl7AgMpOFU/WEDIN342j#XX27r3xadNE2jqb6o3IAEdbB#xALkiiRCA6evF2DQIx40pMrR#V2uydxMn8KIELRmyFBRJU1LrmbzelD6oxNEfoZFV#DscBxLl#dLtt4maFOL6yMyCcKD6b0XNCXEa99vL6##cavgcLy#nCx3lS47UriKIBZvEwomnPIiZRIJh8#MYnBjjZSkBTBViCtv5Q#LwBkPsYCocoX3ay#ZYWs5ngHUuqh1fJQrFSBW1n74tpcOq4iPaqS7dmS/OQAz11sFE541B9tAPWn4oRxXqLz#1vlqUAr9dE2jcfl0TIBnmyVMxonU1BmpxahzafJVg5zBIetJQ3Xefa#YB7ITNEE/Z3YwxutPPQ2GfVUsST63vMEjSJvXcAKvCGTjEWW8FcEnsI4IYmfElm5kQLAjYyWW#JjxmQNQW0lAb85RpiIn/4g7jAkiUi5StOu7r2IPArjDreTC6uFgohzG6tj56xHvIydRqRTKZa4iOl37jofZbR2X0CLn3eIcxKkwcJxyTYzWRQCI05#m/s9uEinmaXtSl2UmmhmloMaC#EMyflyElqcirhzppla8ZDW2bRXD8/H#Z6OjIIpfSMVFz9D2oYC/XhwVjHdl07AJ8M8661zNfpWIs8ccjFNznha7un7RKil3e3ILbfnJeW/Yd7I6npjlUJWbuQ/t47DkHIydbRuRfHjyH8k=;0400R9HVeoYv1gsNf94lis1ztpUkBF3gMqJVUvQPAuo2LgKAC3SfR7deSLSUZpybFcXA9SHJSmUeiL2574HgzbCSorEfVk9EjAzqJqKR0TY3#IFLuAeH2h6vGuixLXGBICA18sQyiQagYnGC7XLTgcuc7Li8jBePvEwYYDvethm0ixXKgDzSlcJpdqYR9FtpPsLf0LVQmADUFBlDgm50c6V/UsE25KpZgUeTfmJPV146JuMtAB9loDuNKLAcesNbcdJjGc#x7ZkkKyrh7d/W78RPOnlWmCRBdNDNnZsKRSTH#IO/n2TRrnUfz408VtFnRD0j9gpHloQhD09UgBbu4NCNc/UJiL5MGqveuaS#rdCzyPnMiYmUc71BnBd2rg#lFhraKNsnunrHByLq6nUttt2l9ujJRwgyXS2Q9KG9HNi#2VI85h7zmVcUJBNBM7WaTBsiYt9JBUxxiSlCSOP8F/eyAGlZmakw#7cndFvPrWIExT58wjfvV29cCUJ5Wd9sf0lnKULPxBs5mS8ZEe#OuJghsiD0dRIF9da4X770SAb3TRP#mvvWgPnrWmK0aG2E3OZcNlPGOByHX4huF83Aa9GmgYQonmwROuiCBJVw9IRDLb0JdqJazYFDT6nqahhMcUIPjTxW0WdEPSP0r/qhtNUJaUF/V33vgKI2yAfUMZ5LKFpDFK356A5knwl2Y1Y/iacvjAucj41K34TIB9QxnksoWmbZIYRwZnYim9ceHDKRCiK4xI1RTIYC8ouD71qCKcmZqa#c5UMfdLNXqLz#1vlqUAr9dE2jcfl0wgroQBfpyuIW#rd/mIenAaO5heWtZpAZVTw2C4oQ2p6vWc20/w4QKST/riUqiozfAOitx40UDzaLaxNWMM2S8UTjbKzZpUNBxKb7FG#fia#fFCEvMT9cc6XakoCa7XCW5#Cltm6/m0VPMQF00uJew0LT2BH9Dx8Z6yFodg/w6rrQcsfKD46f8YCZ40cfr7DqC5#fMqH79MWbCOyMpXT69QD9gfNsKE0OTpKVKH42hNGROirpBMeFwJbCNXUqqTCq8v2oTkvCePSyGF4U0UGkX4j/DuWtwWHvxu8HkHDHdhgOuh4IvPUNS9mgigMcqzmtWrjg3IZNlqx9izplUCi1HWHk3C7XHo8go3BLDGnaz5XZuCYrzj4mEQTXvE##wwwV4vY8lHQcj#Q4fFf2NX4F#7ro#M4ipsfWJ3GXWNSW6tchW6oU287S#V22PjcuhFhUlslmfUf9dDYrUFN9IOYdo3Gr4HC8vpwsd5UuO1K4iiAWbxMKJpzyIianSZZ3QS5Wiuv6Xcf4KsK5LEMIr6#sTO9JtnL1pW8sgn5/pctOOs8Gf8KtIH3o8RLGPxs#gOfLtiKNVrMXJ6Cbt1mVTbhWyJHvK6mfVAgSx/UA41KIRpRwPpPV0ZSMvwjoZA0RWfglGlMNiDk8cpbQEMxtWqZWeSBtbA/EssweGUfIPaozkqWdjIx23VgkipYstdZ3m/rUpwLBtZfh#7IyA21v8MPMxPMyventUynb8yHl2y5x3t9/3n2CdEwdsx88Bwt#VCNf"));
    		        
    		        res = doPost(ADDRESS + "app/member/new_login.php", params, "");
    		        
    		        if(null != res && res.contains("200|100")){
    		        	
    		        	int ps = res.indexOf("||") + 2;
    		        	int pe = res.indexOf("|",ps);
    		        	
    		        	user_id = res.substring(ps, pe);
    		        	
    		        	
    		        	return 1;

    		        }
    		        
    		        
        		}
        		
        		
        	}
        	
        	System.out.println("登录失败:");
        	
        	System.out.println(res);
        	
        	if(res.contains("System Maintenance")){
        		return 2;
        	}else if(res.contains("today_gmt")){
        		return 1;
        	}
        	
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return -1;
    	}
    	

    	
    	return -1;
    }
    
    

    
    
    public boolean getTotalBet(){
    	
    	try{
        	String res = "";
        	
            List<NameValuePair> params = new ArrayList<NameValuePair>();		  
            params.add(new BasicNameValuePair("uid", user_id));
            params.add(new BasicNameValuePair("langx", "zh-cn"));
            params.add(new BasicNameValuePair("mtype", "personal"));
            params.add(new BasicNameValuePair("code", "count"));
            params.add(new BasicNameValuePair("selDate", "All"));
            
            res = doGet(ADDRESS + "app/member/FT_browse/body_var.php?" + "uid=" + user_id + "&rtype=r" + "&langx=zh-cn" + "&mtype=" + mtype + "&page_no=0" + "&league_id=&hot_game="   , "", "");
            
            
            if(res == null){
                res = doGet(ADDRESS + "app/member/FT_browse/body_var.php?" + "uid=" + user_id + "&rtype=r" + "&langx=zh-cn" + "&mtype=" + mtype + "&page_no=0" + "&league_id=&hot_game="   , "", "");            	
            }
            
            
          //  System.out.println(res);
            
            
            if(res.contains("logout_warn")){
            	
            	System.out.println("contains logout_warn");
            	
            	return false;
            	
            	/*boolean bl = login();
            	
            	int logintimes = 0;
            	
            	
            	while(bl == false && logintimes < 10){
            		bl = login();
            		logintimes++;
            		Thread.currentThread().sleep(5*1000);
            	}
            	
            	
                res = doGet(ADDRESS + "app/member/FT_browse/body_var.php?" + "uid=" + user_id + "&rtype=r" + "&langx=zh-cn" + "&mtype=" + mtype + "&page_no=0" + "&league_id=&hot_game="   , "", "");
                
                
                if(res == null){
                    res = doGet(ADDRESS + "app/member/FT_browse/body_var.php?" + "uid=" + user_id + "&rtype=r" + "&langx=zh-cn" + "&mtype=" + mtype + "&page_no=0" + "&league_id=&hot_game="   , "", "");            	
                }*/
            	
            }
            
            
            
            
            
            String previousgame = "";
            
            String currentgame = "";
            
            int priority = -1;

            
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
    		
    		
    		long currentTimeL = System.currentTimeMillis();
        	
    		String currentTime = df.format(currentTimeL);
            
            
            
            if(null != res && res.contains("g([")){

            	//解析工作  第一页
            	//res = bala...
            	
            	int ps = res.indexOf("today_gmt");
            	ps = res.indexOf("'", ps) + 1;
            	int pe = res.indexOf("'", ps);
            	
            	String year = res.substring(ps, pe).split("-")[0];
            	
            	
            	ps = res.indexOf("g([") + 3;
            	pe = res.indexOf(";", ps);
            	
            	
            	
            	//System.out.println(res);
            	
            	
            	SimpleDateFormat dfmin = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            	
            	while(ps != -1){
            		String[] details = res.substring(ps, pe).split(",");
            		
            		String eventid = details[39];
            		eventid = eventid.replace("'", "");
            		
            		
            		currentgame = eventid;
            		
            		if(!currentgame.equals(previousgame)){
            			priority = 1;
            		}else{
            			priority = priority + 1;
            		}
            		
            		String gid = details[0];	//标识一个盘口的
            		gid = gid.replace("'", "");
            		
            		String datetime = details[1];
            		
            		
            		
            		if(!datetime.contains("Running Ball")){
                		ps = res.indexOf("g([", pe);
                		previousgame = currentgame;
                		if(ps == -1){
                			
                		//	System.out.println("不考特别投注");
                			break;
                		}
                		
                		ps = ps +3;
                		
                    	pe = res.indexOf(";", ps);
                    	
                    	
                    	
                    	continue;
            		}
            		
            		datetime = datetime.replace("'", "");
            		datetime = datetime.replace("<br>", " ");
            		
            		int pst = datetime.indexOf("<") - 1;
            		//int pet = datetime.indexOf(">", pst);
            		
            		if(pst > 0){
            			datetime = datetime.substring(0, pst);
            		}
            		
            		if(datetime.contains("a")){
            			datetime = datetime.replace("a", "");
            			datetime = year + "-" + datetime;
            			
            			java.util.Date startTimeDate = dfmin.parse(datetime);
            			
            			Calendar startTime = Calendar.getInstance();  
            			startTime.setTime(startTimeDate);
            			startTime.add(Calendar.HOUR_OF_DAY, 12);
            			
            			datetime = dfmin.format(startTime.getTimeInMillis());
            		}
            		
            		if(datetime.contains("p")){
            			datetime = datetime.replace("p", "");
            			datetime = year + "-" + datetime;

            			java.util.Date startTimeDate = dfmin.parse(datetime);
            			
            			Calendar startTime = Calendar.getInstance();  
            			startTime.setTime(startTimeDate);
            			//startTime.add(Calendar.DAY_OF_MONTH, 1);
            			
            			if(startTime.getTime().getHours()==12){
            				startTime.add(Calendar.HOUR_OF_DAY, 12);
            			}else{
            				startTime.add(Calendar.DAY_OF_MONTH, 1);
            			}
            			
            			
            			datetime = dfmin.format(startTime.getTimeInMillis());
            			
            		}
            		
            		
            		
            		
            		String league = details[2];
            		league = league.replace("'", "");
            		
            		if(league.contains("特别投注")){
                		ps = res.indexOf("g([", pe);
                		previousgame = currentgame;
                		if(ps == -1){
                			
                		//	System.out.println("不考特别投注");
                			break;
                		}
                		
                		ps = ps +3;
                		
                    	pe = res.indexOf(";", ps);
                    	
                    	
                    	
                    	continue;
            		}
            		
            		String teamh = details[5];
            		teamh = teamh.replace("'", "");
            		
            		if(teamh.contains("角球数")||teamh.contains("罚牌数")){
                		ps = res.indexOf("g([", pe);
                		previousgame = currentgame;
                		if(ps == -1){
                			
                			//System.out.println("不考虑角球数,罚牌数");
                			break;
                		}
                		
                		ps = ps +3;
                		
                    	pe = res.indexOf(";", ps);
                    	
                    	
                    	
                    	continue;
            		}
            		
            		
            		String teamc = details[6];
            		teamc = teamc.replace("'", "");
            		
            		String strong = details[7];
            		strong = strong.replace("'", "");
            		
            		if(strong.equals("")){
            			strong = "none";
            		}
            		
            		
            		String pankou = details[8];
            		pankou = pankou.replace("'", "");
            		if(pankou.equals("")){
            			pankou = "none";
            		}
            		
            		String rh = details[9];
            		rh = rh.replace("'", "");
            		if(rh.equals("")){
            			rh = "none";
            		}
            		
            		String rc = details[10];
            		rc = rc.replace("'", "");
            		if(rc.equals("")){
            			rc = "none";
            		}
            		
            		String o = details[11];
            		o = o.replace("'", "");
            		if(o.equals("")){
            			o = "none";
            		}
            		
            		String u = details[12];
            		u = u.replace("'", "");
            		if(u.equals("")){
            			u = "none";
            		}
            		
            		
            		String oc = details[13];
            		oc = oc.replace("'", "");
            		if(oc.equals("")){
            			oc = "none";
            		}
            		
            		String oh = details[14];
            		oh = oh.replace("'", "");
            		if(oh.equals("")){
            			oh = "none";
            		}
            		
            		int findindex= -1;
            		
            		for(int i = 0; i < gameDetailsVec.size(); i++){
            			if(gameDetailsVec.elementAt(i).eventid.equals(eventid)){
            				
            				
            				findindex = i;
            				
            				Vector<String[]> odds = gameDetailsVec.elementAt(i).getodds();
            				
            				int latestsamegid = -1;
            				
            				
            				for(int j = 0; j < odds.size(); j++){

            					if(odds.elementAt(j)[HGODDSINDEX.GID.ordinal()].equals(gid)){
            						latestsamegid = j;
            					}

            				}
            				
            				if(latestsamegid != -1){
            					String[] previousItem = odds.elementAt(latestsamegid);
            					
        						if(!previousItem[HGODDSINDEX.HODD.ordinal()].equals(rh)
            							||!previousItem[HGODDSINDEX.CODD.ordinal()].equals(rc)
            							||!previousItem[HGODDSINDEX.OODD.ordinal()].equals(oh)
            							||!previousItem[HGODDSINDEX.UODD.ordinal()].equals(oc)){
        							
        							
        							
        							
        							
            							
            							String[] newodditem = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
            	            					o, u, oh, oc, "danshi"};
            							
            							if(rh.equals("none") && rc.equals("none") && oh.equals("none") && oc.equals("none")){
            								
            								//System.out.println(res);
            								
            								System.out.println("wrong data in:");
            								
            								System.out.println(gameDetailsVec.elementAt(i).datetime + gameDetailsVec.elementAt(i).teamh);
            								
            								System.out.println(Arrays.toString(newodditem));
            								
            								break;
            							}else{
                							gameDetailsVec.elementAt(i).addodds(newodditem);
                							
                							gameDetailsVec.elementAt(i).savetofile();
            							}
            							

            							
            						}
            				}else{
    							String[] newodditem = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
    	            					o, u, oh, oc, "danshi"};
    							
    							
    							if(rh.equals("none") && rc.equals("none") && oh.equals("none") && oc.equals("none")){
    								
    								
    								
    								System.out.println("wrong data in:");
    								
    								System.out.println(gameDetailsVec.elementAt(i).datetime + gameDetailsVec.elementAt(i).teamh);
    								
    								System.out.println(Arrays.toString(newodditem));
    								
    								break;
    							}else{
        							gameDetailsVec.elementAt(i).addodds(newodditem);
        							
        							gameDetailsVec.elementAt(i).savetofile();
    							}
    							
    							

            				}
            				

            				
            				
            				
            			}
            		}
            		
            		if(findindex == -1){
            			GameDetails gamedetails = new GameDetails();
            			//gamedetails.gid = gid;
            			gamedetails.datetime = datetime;
            			gamedetails.league = league;
            			gamedetails.teamc = teamc;
            			gamedetails.teamh = teamh;
            			gamedetails.eventid = eventid;
            			
            			
            			
            			String[] odds = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
            					o, u, oh, oc, "danshi"};
            			
            			if(rh.equals("none") && rc.equals("none") && oh.equals("none") && oc.equals("none")){
							
							
							
							System.out.println("wrong data in:");
							
							System.out.println(datetime + teamh);
							
							System.out.println(Arrays.toString(odds));
							
							
						}else{
                			gamedetails.addodds(odds);
                			
                			gamedetails.savetofile();
                			
                			gameDetailsVec.add(gamedetails);
            			}
            			

            		}
            		
            		previousgame = currentgame;
            		
            		
            		ps = res.indexOf("g([", pe);
            		
            		if(ps == -1){
            			
            			//System.out.println("fuck fuck");
            			break;
            		}
            		
            		ps = ps +3;
            		
                	pe = res.indexOf(";", ps);
            		
            		
            		
            		
            	}
            	
            	
            	
            	
            	
            	ps = res.indexOf("t_page");
            	ps = res.indexOf("=", ps) + 1;
            	pe = res.indexOf(";", ps);
            	
            	int totalpage = Integer.parseInt(res.substring(ps, pe));
            	
            	for(int page = 1; page < totalpage; page++){
            		
            		res = doGet(ADDRESS + "app/member/FT_browse/body_var.php?" + "uid=" + user_id + "&rtype=r" + "&langx=zh-cn" + "&mtype=" + mtype + "&page_no=" + Integer.toString(page) + "&league_id=&hot_game="   , "", "");

                	//解析工作  第i页
                	//res = bala...
            		
            		if(res == null || !res.contains("g([")){
            			res = doGet(ADDRESS + "app/member/FT_browse/body_var.php?" + "uid=" + user_id + "&rtype=r" + "&langx=zh-cn" + "&mtype=" + mtype + "&page_no=" + Integer.toString(page) + "&league_id=&hot_game="   , "", "");
            		}
            		
            		if(null != res && res.contains("g([")){
            			
            			
            			//System.out.println(res);
            			
            			
                    	ps = res.indexOf("g([") + 3;
                    	pe = res.indexOf(";", ps);
                    	
                    	
                    	
                    	
                    	while(ps != -1){
                    		String[] details = res.substring(ps, pe).split(",");
                    		
                    		String eventid = details[39];
                    		eventid = eventid.replace("'", "");
                    		
                    		
                    		currentgame = eventid;
                    		
                    		if(!currentgame.equals(previousgame)){
                    			priority = 1;
                    		}else{
                    			priority = priority + 1;
                    		}
                    		
                    		String gid = details[0];	//标识一个盘口的
                    		gid = gid.replace("'", "");
                    		
                    		String datetime = details[1];
                    		
                    		if(!datetime.contains("Running Ball")){
                        		ps = res.indexOf("g([", pe);
                        		previousgame = currentgame;
                        		if(ps == -1){
                        			
                        		//	System.out.println("不考特别投注");
                        			break;
                        		}
                        		
                        		ps = ps +3;
                        		
                            	pe = res.indexOf(";", ps);
                            	
                            	
                            	
                            	continue;
                    		}
                    		
                    		datetime = datetime.replace("'", "");
                    		datetime = datetime.replace("<br>", " ");
                    		
                    		int pst = datetime.indexOf("<") - 1;
                    		//int pet = datetime.indexOf(">", pst);
                    		
                    		if(pst > 0){
                    			datetime = datetime.substring(0, pst);
                    		}
                    		
                    		if(datetime.contains("a")){
                    			datetime = datetime.replace("a", "");
                    			datetime = year + "-" + datetime;
                    			
                    			java.util.Date startTimeDate = dfmin.parse(datetime);
                    			
                    			Calendar startTime = Calendar.getInstance();  
                    			startTime.setTime(startTimeDate);
                    			startTime.add(Calendar.HOUR_OF_DAY, 12);
                    			
                    			datetime = dfmin.format(startTime.getTimeInMillis());
                    		}
                    		
                    		if(datetime.contains("p")){
                    			datetime = datetime.replace("p", "");
                    			datetime = year + "-" + datetime;

                    			java.util.Date startTimeDate = dfmin.parse(datetime);
                    			
                    			Calendar startTime = Calendar.getInstance();  
                    			startTime.setTime(startTimeDate);
                    			//startTime.add(Calendar.DAY_OF_MONTH, 1);
                    			
                    			if(startTime.getTime().getHours()==12){
                    				startTime.add(Calendar.HOUR_OF_DAY, 12);
                    			}else{
                    				startTime.add(Calendar.DAY_OF_MONTH, 1);
                    			}
                    			
                    			
                    			datetime = dfmin.format(startTime.getTimeInMillis());
                    			
                    		}
                    		
                    		
                    		
                    		
                    		String league = details[2];
                    		league = league.replace("'", "");
                    		
                    		if(league.contains("特别投注")){
                        		ps = res.indexOf("g([", pe);
                        		
                        		previousgame = currentgame;
                        		
                        		if(ps == -1){
                        			
                        			//System.out.println("不考虑角球数");
                        			break;
                        		}
                        		
                        		ps = ps +3;
                        		
                            	pe = res.indexOf(";", ps);
                            	
                            	
                            	
                            	continue;
                    		}
                    		
                    		String teamh = details[5];
                    		teamh = teamh.replace("'", "");
                    		
                    		if(teamh.contains("角球数")||teamh.contains("罚牌数")){
                        		ps = res.indexOf("g([", pe);
                        		
                        		previousgame = currentgame;
                        		
                        		if(ps == -1){
                        			
                        			//System.out.println("不考虑角球数");
                        			break;
                        		}
                        		
                        		ps = ps +3;
                        		
                            	pe = res.indexOf(";", ps);
                            	
                            	
                            	
                            	continue;
                    		}
                    		
                    		
                    		String teamc = details[6];
                    		teamc = teamc.replace("'", "");
                    		
                    		String strong = details[7];
                    		strong = strong.replace("'", "");
                    		
                    		if(strong.equals("")){
                    			strong = "none";
                    		}
                    		
                    		
                    		String pankou = details[8];
                    		pankou = pankou.replace("'", "");
                    		if(pankou.equals("")){
                    			pankou = "none";
                    		}
                    		
                    		String rh = details[9];
                    		rh = rh.replace("'", "");
                    		if(rh.equals("")){
                    			rh = "none";
                    		}
                    		
                    		String rc = details[10];
                    		rc = rc.replace("'", "");
                    		if(rc.equals("")){
                    			rc = "none";
                    		}
                    		
                    		String o = details[11];
                    		o = o.replace("'", "");
                    		if(o.equals("")){
                    			o = "none";
                    		}
                    		
                    		String u = details[12];
                    		u = u.replace("'", "");
                    		if(u.equals("")){
                    			u = "none";
                    		}
                    		
                    		
                    		String oc = details[13];
                    		oc = oc.replace("'", "");
                    		if(oc.equals("")){
                    			oc = "none";
                    		}
                    		
                    		String oh = details[14];
                    		oh = oh.replace("'", "");
                    		if(oh.equals("")){
                    			oh = "none";
                    		}
                    		

                    		

                    		
                    		int findindex= -1;
                    		
                    		for(int i = 0; i < gameDetailsVec.size(); i++){
                    			if(gameDetailsVec.elementAt(i).eventid.equals(eventid)){
                    				
                    				
                    				findindex = i;
                    				
                    				Vector<String[]> odds = gameDetailsVec.elementAt(i).getodds();
                    				
                    				int latestsamegid = -1;
                    				
                    				
                    				for(int j = 0; j < odds.size(); j++){

                    					if(odds.elementAt(j)[HGODDSINDEX.GID.ordinal()].equals(gid)){
                    						latestsamegid = j;
                    					}

                    				}
                    				
                    				if(latestsamegid != -1){
                    					String[] previousItem = odds.elementAt(latestsamegid);
                    					
                						if(!previousItem[HGODDSINDEX.HODD.ordinal()].equals(rh)
                    							||!previousItem[HGODDSINDEX.CODD.ordinal()].equals(rc)
                    							||!previousItem[HGODDSINDEX.OODD.ordinal()].equals(oh)
                    							||!previousItem[HGODDSINDEX.UODD.ordinal()].equals(oc)){
                							
                							
                							
                							
                							
                    							
                    							String[] newodditem = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
                    	            					o, u, oh, oc, "danshi"};
                    							
                    							if(rh.equals("none") && rc.equals("none") && oh.equals("none") && oc.equals("none")){
                    								
                    								
                    								
                    								System.out.println("wrong data in:");
                    								
                    								System.out.println(gameDetailsVec.elementAt(i).datetime + gameDetailsVec.elementAt(i).teamh);
                    								
                    								System.out.println(Arrays.toString(newodditem));
                    								
                    								break;
                    							}else{
                        							gameDetailsVec.elementAt(i).addodds(newodditem);
                        							
                        							gameDetailsVec.elementAt(i).savetofile();
                    							}
                    							

                    							
                    						}
                    				}else{
            							String[] newodditem = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
            	            					o, u, oh, oc, "danshi"};
            							
            							
            							if(rh.equals("none") && rc.equals("none") && oh.equals("none") && oc.equals("none")){
            								
            								
            								
            								System.out.println("wrong data in:");
            								
            								System.out.println(gameDetailsVec.elementAt(i).datetime + gameDetailsVec.elementAt(i).teamh);
            								
            								System.out.println(Arrays.toString(newodditem));
            								
            								break;
            							}else{
                							gameDetailsVec.elementAt(i).addodds(newodditem);
                							
                							gameDetailsVec.elementAt(i).savetofile();
            							}
            							
            							

                    				}
                    				

                    				
                    				
                    				
                    			}
                    		}
                    		
                    		if(findindex == -1){
                    			GameDetails gamedetails = new GameDetails();
                    			//gamedetails.gid = gid;
                    			gamedetails.datetime = datetime;
                    			gamedetails.league = league;
                    			gamedetails.teamc = teamc;
                    			gamedetails.teamh = teamh;
                    			gamedetails.eventid = eventid;
                    			
                    			
                    			String[] odds = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
                    					o, u, oh, oc, "danshi"};
                    			
                    			if(rh.equals("none") && rc.equals("none") && oh.equals("none") && oc.equals("none")){
    								
    								
    								
    								System.out.println("wrong data in:");
    								
    								System.out.println(datetime + teamh);
    								
    								System.out.println(Arrays.toString(odds));
    								
    								
    							}else{
                        			gamedetails.addodds(odds);
                        			
                        			gamedetails.savetofile();
                        			
                        			gameDetailsVec.add(gamedetails);
                    			}
                    		}
                    		
                    		
                    		previousgame = currentgame;
                    		
                    		
                    		ps = res.indexOf("g([", pe);
                    		if(ps == -1){
                    			break;
                    		}
                    		ps = ps +3;
                        	pe = res.indexOf(";", ps);
                    		
                        	
                    		
                    		
                    	}
            			
            		}
            		

            		
            		
            		
            		
            		
            		
            		
            		
            	}
            	
            	
            }else{
            	
            	if(null == res){
            		return false;
            	}else if(res.contains("gid")){
            		return true;
            	}
            	
            	System.out.println(res);
            	
            	return false;
            }
            
            return true;
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	


    	
    	
    	
    	
    }
    
    
    
    
    
    
    
    public boolean getInplaybet(){

    	try{
        	String res = "";
        	
        	//http://66.133.87.54/app/member/FT_browse/body_var.php?uid=n6s6t6y6zm17379296l2984894&rtype=re&langx=zh-cn&mtype=4&page_no=0&league_id=&hot_game=
        	
        	
            res = doGet(ADDRESS + "app/member/FT_browse/body_var.php?" + "uid=" + user_id + "&rtype=re" + "&langx=zh-cn" + "&mtype=" + mtype + "&page_no=0" + "&league_id=&hot_game="   , "", "");
            
            
            if(res == null){
                res = doGet(ADDRESS + "app/member/FT_browse/body_var.php?" + "uid=" + user_id + "&rtype=re" + "&langx=zh-cn" + "&mtype=" + mtype + "&page_no=0" + "&league_id=&hot_game="   , "", "");            	
            }
            
            
          //  System.out.println(res);
            
            
            if(res.contains("logout_warn")){
            	
            	return false;
            	
            }
            
            
            
            
            
            String previousgame = "";
            
            String currentgame = "";
            
            int priority = -1;

            
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
    		
    		
    		long currentTimeL = System.currentTimeMillis();
        	
    		String currentTime = df.format(currentTimeL);
            
            
            
            if(null != res && res.contains("g([")){

            	//解析工作  第一页
            	//res = bala...
            	
            	int ps = res.indexOf("today_gmt");
            	ps = res.indexOf("'", ps) + 1;
            	int pe = res.indexOf("'", ps);
            	
            	String year = res.substring(ps, pe).split("-")[0];
            	
            	
            	ps = res.indexOf("g([") + 3;
            	pe = res.indexOf(";", ps);
            	
            	
            	
            	//System.out.println(res);
            	
            	
            	SimpleDateFormat dfmin = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            	
            	while(ps != -1){
            		String[] details = res.substring(ps, pe).split(",");
            		
            		if(details.length < 57){
            			System.out.println("funck length small than 57");
            		}
            		
            		String eventid = details[50];
            		eventid = eventid.replace("'", "");
            		
            		
            		currentgame = eventid;
            		
            		if(!currentgame.equals(previousgame)){
            			priority = 1;
            		}else{
            			priority = priority + 1;
            		}
            		

            		
             		String scoreh = details[18];
            		scoreh = scoreh.replace("'", "");
            		
            		String scorec = details[19];
            		scorec = scorec.replace("'", "");
            		
            		int scores = Integer.parseInt(scoreh) + Integer.parseInt(scorec);
            		

            		
            		
            		String timer = details[48];
            		timer = timer.replace("'", "");

            		
            		
            		
            		
            		
            		
            		
            		
            		
            		
            		
            		
            		
            		
            		
            		String gid = details[0];	//标识一个盘口的
            		gid = gid.replace("'", "");
            		
            		String datetime = details[1];
            		datetime = datetime.replace("'", "");
            		datetime = datetime.replace("<br>", " ");
            		
            		if(datetime.contains("半场")){
            			datetime = "半场";
            		}
            		
            		
            		
            		
            		String league = details[2];
            		league = league.replace("'", "");
            		
            		if(league.contains("特别投注")){
            			
            			previousgame = currentgame;
            			
                		ps = res.indexOf("g([", pe);
                		
                		if(ps == -1){
                			
                			//System.out.println("不考虑特别投注");
                			break;
                		}
                		
                		ps = ps +3;
                		
                    	pe = res.indexOf(";", ps);
                    	
                    	
                    	
                    	continue;
            		}
            		
            		String teamh = details[5];
            		teamh = teamh.replace("'", "");
            		
            		if(teamh.contains("角球数")||teamh.contains("罚牌数")){
            			
            			previousgame = currentgame;
            			
                		ps = res.indexOf("g([", pe);
                		
                		if(ps == -1){
                			
                			//System.out.println("不考虑角球数");
                			break;
                		}
                		
                		ps = ps +3;
                		
                    	pe = res.indexOf(";", ps);
                    	
                    	
                    	
                    	continue;
            		}
            		
            		
            		String teamc = details[6];
            		teamc = teamc.replace("'", "");
            		
            		String strong = details[7];
            		strong = strong.replace("'", "");
            		
            		if(strong.equals("")){
            			strong = "none";
            		}
            		
            		
            		String pankou = details[8];
            		pankou = pankou.replace("'", "");
            		if(pankou.equals("")){
            			pankou = "none";
            		}
            		
            		String rh = details[9];
            		rh = rh.replace("'", "");
            		if(rh.equals("")){
            			rh = "none";
            		}
            		
            		String rc = details[10];
            		rc = rc.replace("'", "");
            		if(rc.equals("")){
            			rc = "none";
            		}
            		
            		String o = details[11];
            		o = o.replace("'", "");
            		o = o.replace("O", "");
            		if(o.equals("")){
            			o = "none";
            			
            			previousgame = currentgame;
            			
                		ps = res.indexOf("g([", pe);
                		
                		if(ps == -1){
                			
                			//System.out.println("大小球为空，不如何条件");
                			break;
                		}
                		
                		ps = ps +3;
                		
                    	pe = res.indexOf(";", ps);
                    	
                    	
                    	
                    	continue;
            			
            		}
            		
            		String u = details[12];
            		u = u.replace("'", "");
            		if(u.equals("")){
            			u = "none";
            		}
            		
            		String oc = details[13];
            		oc = oc.replace("'", "");
            		if(oc.equals("")){
            			oc = "none";
            		}
            		
            		String oh = details[14];
            		oh = oh.replace("'", "");
            		if(oh.equals("")){
            			oh = "none";
            		}
            		
            		

            		

            		
            		int findindex= -1;
            		
            		if(priority == 1){
            			
                		for(int i = 0; i < gameDetailsVec.size(); i++){
                			if(gameDetailsVec.elementAt(i).eventid.equals(eventid)){
                				
                				

                				
                				findindex = i;
                				
                				Vector<String[]> odds = gameDetailsVec.elementAt(i).getodds();
                				
                				gameDetailsVec.elementAt(i).currentscore = scoreh + "-" + scorec;
                				
                				gameDetailsVec.elementAt(i).currentpankou = o;
                				
                				gameDetailsVec.elementAt(i).currentratio = oh;
                				
                				int last1danshiodd = -1;
                				
                				boolean hasinplayodd = false;
                				
                				if(odds.elementAt(odds.size() - 1)[HGODDSINDEX.TYPE.ordinal()].equals("inplay")){
                					hasinplayodd = true;
            						//break;
            					}
                				
                				
                				for(int j = odds.size() - 1; j >= 0; j--){

                					if(odds.elementAt(j)[HGODDSINDEX.TYPE.ordinal()].equals("danshi") && odds.elementAt(j)[HGODDSINDEX.PRIOTITY.ordinal()].equals("1")){
                						last1danshiodd = j;
                						break;
                					}

                				}
                				
                				if(last1danshiodd != -1){
                					

                					
                					if(hasinplayodd == true){//已经存在滚球盘的
                						
                    					String[] previousItem = odds.elementAt(odds.size() - 1);
                    					
                    					String preo = previousItem[HGODDSINDEX.O.ordinal()];
                    					
                    					String preodd = previousItem[HGODDSINDEX.OODD.ordinal()];
                    					
                        				double preonum = 0.0;
                        				
                    					if(preo.contains("/")){
                    						preonum = (Double.parseDouble(preo.split("/")[0].replace(" ", "")) + Double.parseDouble(preo.split("/")[1].replace(" ", "")))/2;
                    					}else{
                    						preonum = Double.parseDouble(preo.replace(" ", ""));
                    					}
                    					
                    					
                        				double onum = 0.0;
                        				
                    					if(o.contains("/")){
                    						onum = (Double.parseDouble(o.split("/")[0].replace(" ", "")) + Double.parseDouble(o.split("/")[1].replace(" ", "")))/2;
                    					}else{
                    						onum = Double.parseDouble(o.replace(" ", ""));
                    					}

                    					if(onum < preonum && scores < 2){	//&&到时候需要增加水位判断
                    						String[] newodditem = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
                	            					o, u, oh, oc, "inplay", scoreh, scorec, timer};
                    						
                    						System.out.println("second inplay add:" + teamh + "vs" + teamc);
                    						
                							gameDetailsVec.elementAt(i).addodds(newodditem);
                							
                							gameDetailsVec.elementAt(i).savetofile();
                							
                    					}else if(scores > Integer.parseInt(previousItem[HGODDSINDEX.SCOREH.ordinal()]) + Integer.parseInt(previousItem[HGODDSINDEX.SCOREC.ordinal()])){
                    						String[] newodditem = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
                	            					o, u, oh, oc, "inplay", scoreh, scorec, timer};
                    						
                    						System.out.println("second inplay add:" + teamh + "vs" + teamc);
                    						
                							gameDetailsVec.elementAt(i).addodds(newodditem);
                							
                							gameDetailsVec.elementAt(i).savetofile();
                    					}else if(onum == preonum && !preodd.equals(oh)){//更新水位
                    						System.out.println("update inplay add:"+ teamh + "vs" + teamc);
                    						
                    						int oddsize = gameDetailsVec.elementAt(i).getodds().size();
                    						
                    						gameDetailsVec.elementAt(i).getodds().elementAt(oddsize-1)[HGODDSINDEX.OODD.ordinal()] = oh;
                    						
                    					}
                    					

                						
                					}else{
                						
                                		if(timer.contains("2H")){
                                			timer = timer.replace("^", "");
                                			timer = timer.replace("2H", "");
                                			int min = Integer.parseInt(timer.split(":")[0]);

                                			if(min <= 2){//这里判断
                                				double onum = 0.0;
                                				
                            					if(o.contains("/")){
                            						onum = (Double.parseDouble(o.split("/")[0].replace(" ", "")) + Double.parseDouble(o.split("/")[1].replace(" ", "")))/2;
                            					}else{
                            						onum = Double.parseDouble(o.replace(" ", ""));
                            					}
                                				
                                				if(scores == 0){
                                					if(onum >= 1.5){
                                						String[] newodditem = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
                            	            					o, oh, oh, oc, "inplay", scoreh, scorec, timer};
                                						
                                						gameDetailsVec.elementAt(i).addodds(newodditem);
                                						
                                						gameDetailsVec.elementAt(i).savetofile();
                                						
                                						System.out.println("inplay add:" + gameDetailsVec.elementAt(i).teamh + "vs" + gameDetailsVec.elementAt(i).teamc);
                                						
                                					}
                                				}else if(scores == 1){
                                					if(onum >= 2.5){
                                						String[] newodditem = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
                            	            					o, oh, oh, oc, "inplay", scoreh, scorec, timer};
                                						
                                						gameDetailsVec.elementAt(i).addodds(newodditem);
                                						gameDetailsVec.elementAt(i).savetofile();
                                						
                                						System.out.println("inplay add:" + gameDetailsVec.elementAt(i).teamh + "vs" + gameDetailsVec.elementAt(i).teamc);
                                					}
                                				}
                                				
                                			}
                                			
                                		}//2H end
                					}
                					

                				}//has danshi odds

            			}
            		}
            	}
            		

            		
            		
            		ps = res.indexOf("g([", pe);
            		
            		if(ps == -1){
            			
            			
            			break;
            		}
            		
            		ps = ps +3;
            		
                	pe = res.indexOf(";", ps);
            		
            		previousgame = currentgame;
            		
            		
            	}
            	
            	
            	
            	
            	
            	ps = res.indexOf("t_page");
            	ps = res.indexOf("=", ps) + 1;
            	pe = res.indexOf(";", ps);
            	
            	int totalpage = Integer.parseInt(res.substring(ps, pe));
            	
            	for(int page = 1; page < totalpage; page++){
            		
            		res = doGet(ADDRESS + "app/member/FT_browse/body_var.php?" + "uid=" + user_id + "&rtype=re" + "&langx=zh-cn" + "&mtype=" + mtype + "&page_no=" + Integer.toString(page) + "&league_id=&hot_game="   , "", "");

                	//解析工作  第i页
                	//res = bala...
            		
            		if(res == null || !res.contains("g([")){
            			res = doGet(ADDRESS + "app/member/FT_browse/body_var.php?" + "uid=" + user_id + "&rtype=re" + "&langx=zh-cn" + "&mtype=" + mtype + "&page_no=" + Integer.toString(page) + "&league_id=&hot_game="   , "", "");
            		}
            		
            		if(null != res && res.contains("g([")){
            			
            			
            			//System.out.println(res);
            			
            			
                    	ps = res.indexOf("g([") + 3;
                    	pe = res.indexOf(";", ps);
                    	
                    	
                    	
                    	
                    	while(ps != -1){
                    		String[] details = res.substring(ps, pe).split(",");
                    		
                    		if(details.length < 57){
                    			System.out.println("funck you");
                    		}
                    		
                    		String eventid = details[50];
                    		eventid = eventid.replace("'", "");
                    		
                    		
                    		currentgame = eventid;
                    		
                    		if(!currentgame.equals(previousgame)){
                    			priority = 1;
                    		}else{
                    			priority = priority + 1;

                    		}
                    		

                    		
                    		String scoreh = details[18];
                    		scoreh = scoreh.replace("'", "");
                    		
                    		String scorec = details[19];
                    		scorec = scorec.replace("'", "");
                    		
                    		int scores = Integer.parseInt(scoreh) + Integer.parseInt(scorec);
                    		

                    		
                    		
                    		String timer = details[48];
                    		timer = timer.replace("'", "");

                    		
                    		
                    		
                    		
                    		
                    		
                    		
                    		
                    		
                    		
                    		
                    		
                    		
                    		
                    		
                    		String gid = details[0];	//标识一个盘口的
                    		gid = gid.replace("'", "");
                    		
                    		String datetime = details[1];
                    		datetime = datetime.replace("'", "");
                    		datetime = datetime.replace("<br>", " ");
                    		
                    		if(datetime.contains("半场")){
                    			datetime = "半场";
                    		}
                    		
                    		
                    		
                    		
                    		String league = details[2];
                    		league = league.replace("'", "");
                    		
                    		if(league.contains("特别投注")){
                    			
                    			previousgame = currentgame;
                    			
                        		ps = res.indexOf("g([", pe);
                        		
                        		if(ps == -1){
                        			
                        			//System.out.println("不考虑特别投注");
                        			break;
                        		}
                        		
                        		ps = ps +3;
                        		
                            	pe = res.indexOf(";", ps);
                            	
                            	
                            	
                            	continue;
                    		}
                    		
                    		String teamh = details[5];
                    		teamh = teamh.replace("'", "");
                    		
                    		if(teamh.contains("角球数")||teamh.contains("罚牌数")){
                    			
                    			previousgame = currentgame;
                    			
                        		ps = res.indexOf("g([", pe);
                        		
                        		if(ps == -1){                        			
                        			//System.out.println("不考虑角球数");
                        			break;
                        		}
                        		
                        		ps = ps +3;
                        		
                            	pe = res.indexOf(";", ps);
                            	
                            	
                            	
                            	continue;
                    		}
                    		
                    		
                    		String teamc = details[6];
                    		teamc = teamc.replace("'", "");
                    		
                    		String strong = details[7];
                    		strong = strong.replace("'", "");
                    		
                    		if(strong.equals("")){
                    			strong = "none";
                    		}
                    		
                    		
                    		String pankou = details[8];
                    		pankou = pankou.replace("'", "");
                    		if(pankou.equals("")){
                    			pankou = "none";
                    		}
                    		
                    		String rh = details[9];
                    		rh = rh.replace("'", "");
                    		if(rh.equals("")){
                    			rh = "none";
                    		}
                    		
                    		String rc = details[10];
                    		rc = rc.replace("'", "");
                    		if(rc.equals("")){
                    			rc = "none";
                    		}
                    		
                    		String o = details[11];
                    		o = o.replace("'", "");
                    		o = o.replace("O", "");
                    		if(o.equals("")){
                    			o = "none";
                    			
                    			previousgame = currentgame;
                    			
                        		ps = res.indexOf("g([", pe);
                        		
                        		if(ps == -1){
                        			
                        			//System.out.println("大小球为空，不如何条件");
                        			break;
                        		}
                        		
                        		ps = ps +3;
                        		
                            	pe = res.indexOf(";", ps);
                            	
                            	
                            	
                            	continue;
                    			
                    		}
                    		
                    		String u = details[12];
                    		u = u.replace("'", "");
                    		if(u.equals("")){
                    			u = "none";
                    		}
                    		
                    		String oc = details[13];
                    		oc = oc.replace("'", "");
                    		if(oc.equals("")){
                    			oc = "none";
                    		}
                    		
                    		String oh = details[14];
                    		oh = oh.replace("'", "");
                    		if(oh.equals("")){
                    			oh = "none";
                    		}
                    		
                    		

                    		

                    		
                    		int findindex= -1;
                    		
                    		if(priority == 1){
                    			
                        		for(int i = 0; i < gameDetailsVec.size(); i++){
                        			if(gameDetailsVec.elementAt(i).eventid.equals(eventid)){
                        				
                        				
                        				findindex = i;
                        				
                        				Vector<String[]> odds = gameDetailsVec.elementAt(i).getodds();
                        				
                        				gameDetailsVec.elementAt(i).currentscore = scoreh + "-" + scorec;
                        				
                        				gameDetailsVec.elementAt(i).currentpankou = o;
                        				
                        				gameDetailsVec.elementAt(i).currentratio = oh;
                        				
                        				int last1danshiodd = -1;
                        				
                        				boolean hasinplayodd = false;
                        				
                        				if(odds.elementAt(odds.size() - 1)[HGODDSINDEX.TYPE.ordinal()].equals("inplay")){
                        					hasinplayodd = true;
                    						//break;
                    					}
                        				
                        				
                        				for(int j = odds.size() - 1; j >= 0; j--){

                        					if(odds.elementAt(j)[HGODDSINDEX.TYPE.ordinal()].equals("danshi") && odds.elementAt(j)[HGODDSINDEX.PRIOTITY.ordinal()].equals("1")){
                        						last1danshiodd = j;
                        						break;
                        					}

                        				}
                        				
                        				if(last1danshiodd != -1){
                        					

                        					
                        					if(hasinplayodd == true){//已经存在滚球盘的
                        						
                            					String[] previousItem = odds.elementAt(odds.size() - 1);
                            					
                            					String preo = previousItem[HGODDSINDEX.O.ordinal()];
                            					
                            					String preodd = previousItem[HGODDSINDEX.OODD.ordinal()];
                            					
                                				double preonum = 0.0;
                                				
                            					if(preo.contains("/")){
                            						preonum = (Double.parseDouble(preo.split("/")[0].replace(" ", "")) + Double.parseDouble(preo.split("/")[1].replace(" ", "")))/2;
                            					}else{
                            						preonum = Double.parseDouble(preo.replace(" ", ""));
                            					}
                            					
                            					
                                				double onum = 0.0;
                                				
                            					if(o.contains("/")){
                            						onum = (Double.parseDouble(o.split("/")[0].replace(" ", "")) + Double.parseDouble(o.split("/")[1].replace(" ", "")))/2;
                            					}else{
                            						onum = Double.parseDouble(o.replace(" ", ""));
                            					}

                            					if(onum < preonum && scores < 2){	//&&到时候需要增加水位判断
                            						String[] newodditem = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
                        	            					o, u, oh, oc, "inplay", scoreh, scorec, timer};
                            						
                            						System.out.println("second inplay add:" + teamh + "vs" + teamc);
                            						
                        							gameDetailsVec.elementAt(i).addodds(newodditem);
                        							
                        							gameDetailsVec.elementAt(i).savetofile();
                            					}else if(scores > Integer.parseInt(previousItem[HGODDSINDEX.SCOREH.ordinal()]) + Integer.parseInt(previousItem[HGODDSINDEX.SCOREC.ordinal()])){
                            						String[] newodditem = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
                        	            					o, u, oh, oc, "inplay", scoreh, scorec, timer};
                            						
                            						System.out.println("second inplay add:" + teamh + "vs" + teamc);
                            						
                        							gameDetailsVec.elementAt(i).addodds(newodditem);
                        							
                        							gameDetailsVec.elementAt(i).savetofile();
                            					}else if(onum == preonum && !preodd.equals(oh)){//更新水位
                            						System.out.println("update inplay add:"+ teamh + "vs" + teamc);
                            						
                            						int oddsize = gameDetailsVec.elementAt(i).getodds().size();
                            						
                            						gameDetailsVec.elementAt(i).getodds().elementAt(oddsize-1)[HGODDSINDEX.OODD.ordinal()] = oh;
                            						
                            					}
                            					

                        						
                        					}else{
                        						
                                        		if(timer.contains("2H")){
                                        			timer = timer.replace("^", "");
                                        			timer = timer.replace("2H", "");
                                        			
                                        			int min = Integer.parseInt(timer.split(":")[0]);

                                        			if(min <= 2){//这里判断
                                        				double onum = 0.0;
                                        				
                                    					if(o.contains("/")){
                                    						onum = (Double.parseDouble(o.split("/")[0].replace(" ", "")) + Double.parseDouble(o.split("/")[1].replace(" ", "")))/2;
                                    					}else{
                                    						onum = Double.parseDouble(o.replace(" ", ""));
                                    					}
                                        				
                                        				if(scores == 0){
                                        					if(onum >= 1.5){
                                        						String[] newodditem = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
                                    	            					o, oh, oh, oc, "inplay", scoreh, scorec, timer};
                                        						
                                        						gameDetailsVec.elementAt(i).addodds(newodditem);
                                        						
                                        						gameDetailsVec.elementAt(i).savetofile();
                                        						
                                        						System.out.println("inplay add:" + gameDetailsVec.elementAt(i).teamh + "vs" + gameDetailsVec.elementAt(i).teamc);
                                        					}
                                        				}else if(scores == 1){
                                        					if(onum >= 2.5){
                                        						String[] newodditem = {gid, Integer.toString(priority), currentTime, strong + pankou, rh, rc, 
                                    	            					o, oh, oh, oc, "inplay", scoreh, scorec, timer};
                                        						
                                        						gameDetailsVec.elementAt(i).addodds(newodditem);
                                        						
                                        						gameDetailsVec.elementAt(i).savetofile();
                                        						
                                        						System.out.println("inplay add:" + gameDetailsVec.elementAt(i).teamh + "vs" + gameDetailsVec.elementAt(i).teamc);
                                        					}
                                        				}
                                        				
                                        			}
                                        			
                                        		}//2H end
                        					}
                        					

                        				}//has danshi odds
                    				
                    				

                    				
                    				

                    				
                    				
                    				
                    			}
                    		}
                    	}
                    		

                    		
                    		
                    		ps = res.indexOf("g([", pe);
                    		
                    		if(ps == -1){
                    			
                    			
                    			break;
                    		}
                    		
                    		ps = ps +3;
                    		
                        	pe = res.indexOf(";", ps);
                    		
                    		previousgame = currentgame;
                    		
                    		
                    	}
            			
            		}
            		

            		
            		
            		
            		
            		
            		
            		
            		
            	}
            	
            	
            }
            
            return true;
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	


    	
    	
    	
    	
    

}
  


    public boolean getgameresult(){
    	try{
    		
    		//http://66.133.87.54/app/member/result/result.php?game_type=FT&uid=42t7kzd5m17409677l4300439&langx=zh-cn
    		
    		String res = doGet(ADDRESS + "app/member/result/result.php?game_type=FT&" + "uid=" + user_id + "&langx=zh-cn", "", "");

        	//解析工作  第i页
        	//res = bala...
    		
    		if(res == null || !res.contains("足球 : 赛果")){
    			doGet(ADDRESS + "app/member/result/result.php?game_type=FT&" + "uid=" + user_id + "&langx=zh-cn", "", "");
    		}
    		
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			long currenttime = System.currentTimeMillis();
			
			//long gametime = df.parse(onegame.datetime).getTime();
			
			if(res != null && res.contains("足球 : 赛果")){
				
				
				
	    		for(int i = 0; i < gameDetailsVec.size(); i++){
	    			
	    			GameDetails onegame = gameDetailsVec.elementAt(i);
	    			
	    			if(onegame.gameresult.contains("-")){
	    				continue;
	    			}
	    			
	    			int latestdanshiOdd = -1;
	    			
	    			for(int j = onegame.getodds().size() - 1; j >= 0 ; j--){
	    				
	    				String[] odds = onegame.getodds().elementAt(j);
	    				
	    				if(odds[HGODDSINDEX.PRIOTITY.ordinal()].equals("1") && odds[HGODDSINDEX.TYPE.ordinal()].equals("danshi")){
	    					latestdanshiOdd = j;
							
							break;
							
						}
	    			}
	    			
	    			if(currenttime - df.parse(onegame.datetime).getTime() > 110*60*1000 && latestdanshiOdd != -1){
	    				String gid = onegame.getodds().elementAt(latestdanshiOdd)[HGODDSINDEX.GID.ordinal()];
	    				
	    				int ps = res.indexOf("足球 : 赛果");
	    				
	    				ps = res.indexOf("'"+gid+"'", ps);
	    				if(ps != -1){
	    					ps = res.indexOf("全场", ps);
	    					if(ps != -1){
	    						ps = res.indexOf("style=", ps);
	    						ps = res.indexOf(">", ps)+1;
	    						int pe = res.indexOf("<", ps);
	    						String scoreh = res.substring(ps, pe);
	    						
	    						
	    						
	    						ps = res.indexOf("style=", ps);
	    						ps = res.indexOf(">", ps)+1;
	    						pe = res.indexOf("<", ps);
	    						
	    						String scorec = res.substring(ps, pe);
	    						
	    						
	    						
	    						if(scoreh.contains("赛事延赛")){
	    							continue;
	    						}else{
	    							onegame.gameresult = scoreh + "-" + scorec;
	    							onegame.savetofile();
	    						}
	    						
	    					}
	    				}
	    				
	    			}
	    			
	    		}
			}
			
			
			SimpleDateFormat dfday = new SimpleDateFormat("yyyy-MM-dd");
			
			
			
			
			Calendar caltimenow = Calendar.getInstance();
			


			if(caltimenow.get(Calendar.HOUR_OF_DAY) > 12 && caltimenow.get(Calendar.HOUR_OF_DAY) < 15){
				
				caltimenow.add(Calendar.DAY_OF_YEAR, -1);
				
				String catchdate = dfday.format(caltimenow.getTimeInMillis());
				
				
					
					
				res = doGet(ADDRESS + "app/member/result/result.php?game_type=FT&list_date=" + catchdate + "&uid=" + user_id + "&langx=zh-cn", "", "");

	        	//解析工作  第i页
	        	//res = bala...
	    		
	    		if(res == null || !res.contains("足球 : 赛果")){
	    			doGet(ADDRESS + "app/member/result/result.php?game_type=FT&list_date=" + catchdate + "&uid=" + user_id + "&langx=zh-cn", "", "");
	    		}
	    		
				
				
				currenttime = System.currentTimeMillis();
				
				//long gametime = df.parse(onegame.datetime).getTime();
				
				if(res != null && res.contains("足球 : 赛果")){
					
					
					
		    		for(int i = 0; i < gameDetailsVec.size(); i++){
		    			
		    			GameDetails onegame = gameDetailsVec.elementAt(i);
		    			
		    			if(onegame.gameresult.contains("-")){
		    				continue;
		    			}
		    			
		    			int latestdanshiOdd = -1;
		    			
		    			for(int j = onegame.getodds().size() - 1; j >= 0 ; j--){
		    				
		    				String[] odds = onegame.getodds().elementAt(j);
		    				
		    				if(odds[HGODDSINDEX.PRIOTITY.ordinal()].equals("1") && odds[HGODDSINDEX.TYPE.ordinal()].equals("danshi")){
		    					latestdanshiOdd = j;
								
								break;
								
							}
		    			}
		    			
		    			if(currenttime - df.parse(onegame.datetime).getTime() > 110*60*1000 && latestdanshiOdd != -1){
		    				String gid = onegame.getodds().elementAt(latestdanshiOdd)[HGODDSINDEX.GID.ordinal()];
		    				
		    				int ps = res.indexOf("足球 : 赛果");
		    				
		    				ps = res.indexOf("'"+gid+"'", ps);
		    				if(ps != -1){
		    					ps = res.indexOf("全场", ps);
		    					if(ps != -1){
		    						ps = res.indexOf("style=", ps);
		    						ps = res.indexOf(">", ps)+1;
		    						int pe = res.indexOf("<", ps);
		    						String scoreh = res.substring(ps, pe);
		    						
		    						
		    						
		    						ps = res.indexOf("style=", ps);
		    						ps = res.indexOf(">", ps)+1;
		    						pe = res.indexOf("<", ps);
		    						
		    						String scorec = res.substring(ps, pe);
		    						
		    						
		    						
		    						if(scoreh.contains("赛事延赛")){
		    							continue;
		    						}else{
		    							onegame.gameresult = scoreh + "-" + scorec;
		    							onegame.savetofile();
		    						}
		    						
		    					}
		    				}
		    				
		    			}
		    			
		    		}
				}
					
			}
    		
    		

    		
    		
    		
    		
    		return true;
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    }
    
    
    
    
    
	
    public  CloseableHttpResponse  execute(HttpUriRequest request) throws IOException, ClientProtocolException{
    	
    	long time1 = System.currentTimeMillis();
    	long time2 = System.currentTimeMillis();
    	
    	CloseableHttpResponse response;
    	
    	try{
    		response = httpclient.execute(request);    		
    		time2 = System.currentTimeMillis();    		
    		calcRequestAveTime(time2 - time1);
    		
    	}catch(Exception e){
    		
    		time2 = System.currentTimeMillis();
    		calcRequestAveTime(time2 - time1);
    		
    		throw e;
    	}
    	

    	
    	return response;
    	
    }
    
    
    public String doGet(String url, String cookies, String referUrl) {
    	
        try {  
            // 创建httpget.    
            HttpGet httpget = new HttpGet(url);
            
            if(cookies != "") {
            	httpget.addHeader("Cookie",cookies);
            }
            httpget.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
            httpget.addHeader("Accept-Language","Accept-Language: zh-CN,zh;q=0.8");
            httpget.addHeader("Connection","keep-alive");
            httpget.addHeader("Upgrade-Insecure-Requests","1");
            httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            //
            
            if(referUrl != "")
            {
            	httpget.addHeader("Referer",referUrl);
            	
            }
            
            httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
           // System.out.println("executing request " + httpget.getURI()); 
           
            //设置超时
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();
            httpget.setConfig(requestConfig);
            
            // 执行get请求.    
            CloseableHttpResponse response = execute(httpget); 
            
            String statusLine = response.getStatusLine().toString();   
            if(statusLine.indexOf("200 OK") == -1) {
         	  // System.out.println(statusLine); 
            }
            
            try{
            	setCookie(response);  	
            	//System.out.println("设置cookie:" + strCookies);
            	
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
             	   return response.getFirstHeader("Location").getValue();
                }
                HttpEntity entity = response.getEntity(); 
                
                String res = EntityUtils.toString(entity);
                
                if(res != null && res.length() > 0 ){     
                	//System.out.println(res);
                    return res;
                }
            }finally{
                httpget.releaseConnection();
                response.close();
            }
            

            

        } catch (ClientProtocolException e) {  
            e.printStackTrace(); 
        } catch (ParseException e) {  
            e.printStackTrace(); 
        } catch (IOException e) {  
            e.printStackTrace(); 
        } catch (Exception e){
        	e.printStackTrace();
        }
        
        return null;
    }
    
    
    
    /**以utf-8形式读取*/
    public String doPost(String url,List<NameValuePair> formparams, String cookies) {
        return doPost(url, formparams,"UTF-8", cookies);
    }

    public String doPost(String url,List<NameValuePair> formparams,String charset, String cookies) {


     // 创建httppost   
    	
    	try {
    	
        HttpPost httppost = new HttpPost(url); 
        
/*        if(url.contains("get_today_ft_league_wager.php")){
        	httppost.addHeader("Cookie", strCookies);
        }*/
        
        //httppost.addHeader("Cookie", cookies);
        //httppost.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
        //httppost.addHeader("x-requested-with","XMLHttpRequest");
        httppost.addHeader("Accept-Language","Accept-Language: zh-CN");
        httppost.addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        httppost.addHeader("Accept-Encoding","gzip, deflate");
        httppost.addHeader("Connection","keep-alive");
        httppost.addHeader("Cache-Control","max-age=0");
        httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");    


        UrlEncodedFormEntity uefEntity;
        
            uefEntity = new UrlEncodedFormEntity(formparams, charset);
            httppost.setEntity(uefEntity);
            
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();
            httppost.setConfig(requestConfig);
            
            CloseableHttpResponse response = execute(httppost);
            try {
                // 打印响应状态    
            	setCookie(response);
            	//System.out.println("设置cookie:" + strCookies);
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
            		String location = response.getFirstHeader("Location").getValue();
            		//System.out.println(response.getStatusLine());
            		
            		
            		
            		if(location != null) {
            			return location;
            		}
            	}
            	
            	
            	
            	//Header headers[] = response.getHeaders("Content-Type");
            	
            	
                HttpEntity entity = response.getEntity(); 
                
                String res = "";
                
                if(url.contains("get_today_ft_league_wager.php") || url.contains("app/ft/get_today_ft_allbets_wager.php")){            		
                	res = new  String(EntityUtils.toString(entity).getBytes("ISO-8859-1"), "UTF-8");
                }else{
                	res = EntityUtils.toString(entity);
                }
                
                
                if(res != null && res.length() > 0 ){     
                	//System.out.println(res);
                    return res;
                }
            	
            	

            } finally {  
            	httppost.releaseConnection();
                response.close(); 
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace(); 
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace(); 
        } catch (IOException e) {  
            e.printStackTrace(); 
     
        } catch(Exception e){
     	   e.printStackTrace();
        } 
        return null;
    }
    
    
    
	public String setCookie(CloseableHttpResponse httpResponse)
	{
		//strCookies = "";
		//System.out.println("----setCookieStore");
		Header headers[] = httpResponse.getHeaders("Set-Cookie");
		if (headers == null || headers.length==0)
		{
			//System.out.println("----there are no cookies");
			return null;
		}

		String cookie = "";
		for (int i = 0; i < headers.length; i++) {
			cookie += headers[i].getValue();
			if(i != headers.length-1)
			{
				cookie += ";";
			}
		}
		String cookies[] = cookie.split(";");
		
		for (String c : cookies)
		{
			if(c.indexOf("path=") != -1 || c.indexOf("expires=") != -1 || c.indexOf("domain=") != -1 || c.indexOf("HttpOnly") != -1)
				continue;
			strCookies += c;
			strCookies += ";";
		}
		//System.out.println("----setCookieStore success");

		return strCookies;
	}
    
    
    public synchronized void calcRequestAveTime(long requestTime){
        
    	if(bcalcRequestTime == true){
    		
        	//requestCount++;
        	
    		long totalReqeustTime = 0;
    		
        	lastTenRequestTime.add(requestTime);
        	
        	while(lastTenRequestTime.size() >10){
        		lastTenRequestTime.remove(0);
        	}
        	
        	
        	if(lastTenRequestTime.size() == 10){
            	for(int i = 0; i < lastTenRequestTime.size(); i++){
            		totalReqeustTime += lastTenRequestTime.elementAt(i);
            	}
            	avgRequestTime = totalReqeustTime/lastTenRequestTime.size();
            	
            	
            	//System.out.printf("[迪斯尼会员]平均请求时间:%d\n", avgRequestTime);
            	
            	
            	long currentTime = System.currentTimeMillis();
            	
            	long passTime = currentTime - lastChangeLineTime;
            	
            	if(avgRequestTime >= 500 && passTime >= 90*1000){
            		//setisNeedChangeLine(true);
            		lastChangeLineTime = currentTime;
            	}

        	}

    	}

    		
    	
    }
    
    
    public static void printEvents(){
    	for(int i  = 0; i < eventDetailsVec.size(); i++){
    		System.out.println(Arrays.toString(eventDetailsVec.elementAt(i)));

		}
    }
    
    
    

    
    
}
