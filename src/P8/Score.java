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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Score {
	
	
    CloseableHttpClient httpclient = null;
    RequestConfig requestConfig = null;
    HttpClientContext clientContext = null;
	
	String strCookies = "";
	
	public    Vector<String[]> scoresDetailsVec = new Vector<String[]>();
	
	public Vector<String[]> scoresPreviousDetailsVec = new Vector<String[]>();
	
    private ReadWriteLock lockeFinalScoreDetails = new ReentrantReadWriteLock();
    
    String lastgetpredatatime = "";
    
    public Vector<String[]> finalScoreDetailsVec = new Vector<String[]>();
    
    NumberFormat nf = NumberFormat.getInstance();
	
	
	
	public String rqpankoustr  = "平手,平/半,半球,半/一,一球,一/球半,球半,球半/两,两球,两/两球半,两球半,两球半/三,三球,三/三球半,三球半,三球半/四球,四球,四/四球半,四球半,四球半/五,五球,五/五球半,五球半,五球半/六,六球,六/六球半,六球半,六球半/七,七球,七/七球半,七球半,七球半/八,八球,八/八球半,八球半,八球半/九,九球,九/九球半,九球半,九球半/十,十球";
	public String[] dxqpankou = {"0", "0/0.5", "0.5", "0.5/1", "1", "1/1.5", "1.5", "1.5/2", "2", "2/2.5", "2.5", "2.5/3", "3", "3/3.5", "3.5", "3.5/4", "4", "4/4.5", "4.5", "4.5/5", "5", "5/5.5", "5.5", "5.5/6", "6", "6/6.5", "6.5", "6.5/7", "7", "7/7.5", "7.5", "7.5/8", "8", "8/8.5", "8.5", "8.5/9", "9", "9/9.5", "9.5", "9.5/10", "10", "10/10.5", "10.5", "10.5/11", "11", "11/11.5", "11.5", "11.5/12", "12", "12/12.5", "12.5", "12.5/13", "13", "13/13.5", "13.5", "13.5/14", "14" };
	public String[] rqpankou = rqpankoustr.split(",");
	
	public ScoreDetailsWindow scoreDetailsWnd = new ScoreDetailsWindow();
	
	public ScoreNewDetailsWindow scoreNewDetailsWnd = new ScoreNewDetailsWindow();
	
	Map<String, Double> rqpmap = new HashMap<String, Double>();
	
	double interval = 0.25;
	
	int avoidfrequencysleep = 5;
	
	public Score(){
		initializeRqpmap();
		recoverscoresDetailsVecfromefile();
	}
	
	
	public void  recoverscoresDetailsVecfromefile(){
		
		try{
			
			SimpleDateFormat dfmin = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			Calendar currentTime = Calendar.getInstance();
			
			String folder = "";
			
			if(currentTime.get(Calendar.HOUR_OF_DAY) >= 13){
				folder = dfmin.format(currentTime.getTimeInMillis());
			}else{
				currentTime.add(Calendar.DAY_OF_MONTH, -1);
				folder = dfmin.format(currentTime.getTimeInMillis());
			}
			
			
			//读取改目录下所有文件:
			File filefolder = new File("data/" + "scoredata/" + folder + "/");
			
			if(!filefolder.exists()){
				return;
			}
			
			File flist[] = filefolder.listFiles();
			
			int linenum = 0;
			
			for(File f : flist){
				if(f.isDirectory()){
					//System.out.println("fuck directory here");
				}else{
					//System.out.println(f.getAbsolutePath());
					//
					
					
					BufferedReader freader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
					
					String str = "";
					
					linenum = 0;
					
					while ((str = freader.readLine()) != null) {
						
						String[] contents = str.split(",");
						
						
						scoresDetailsVec.add(contents);
						
							
						}
					
					
					
					
					if(null != freader){
						freader.close();
					}
					
					
				}
				
				
				
				
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
		
		
	}
	
	
	public  boolean savescoresDetailsVectofile(){
		
		try{
			
			SimpleDateFormat dfmin = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			SimpleDateFormat dfrealmin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			Calendar currentTime = Calendar.getInstance();
			
			String folder = "";
			
			if(currentTime.get(Calendar.HOUR_OF_DAY) >= 13){
				folder = dfmin.format(currentTime.getTimeInMillis());
			}else{
				currentTime.add(Calendar.DAY_OF_MONTH, -1);
				folder = dfmin.format(currentTime.getTimeInMillis());
			}
			
			File filefolder = new File("data/" + "scoredata/" + folder + "/");
			
			if(!filefolder.exists()){
				filefolder.mkdir();
			}
			
			String filenametmp = "score"
					+ ".data";
			
			
			
			
			
			File file = new File("data/" + "scoredata/" + folder + "/" + filenametmp);
			
			if(!file.exists()){
				System.out.println("create scoredata/" + filenametmp);

				file.createNewFile();
			}
			
			BufferedWriter fwlocal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
			
			
			
			
			for(int i = 0; i < scoresDetailsVec.size(); i++){
				
				String[] item = scoresDetailsVec.elementAt(i);
				
				//int hour = dfrealmin.parse(item[SCORENEWINDEX.TIME.ordinal()]).getHours();
				
				if(dfrealmin.parse(item[SCORENEWINDEX.TIME.ordinal()]).getHours()<13 && currentTime.get(Calendar.HOUR_OF_DAY) >= 13){
					continue;
				}
				
				String str = "";
				for(int j = 0; j < item.length; j++){
					if(j == 0){
						str = item[j];
					}else{
						str = str + "," + item[j];
					}
				}
				
				fwlocal.append(str);
				fwlocal.newLine();
				fwlocal.flush();

			}
			
			fwlocal.close();
			
			
			
			return true;
			
			
		}catch(Exception e){
			e.printStackTrace();
			
			
			
			
			return false;
		}
	}

	
	
	
	
	public boolean updatelastdaydetails(String[] itemin){
		try{
			
			
			Vector<String[]> tmpscoresDetailsVec = new Vector<String[]>();
			
			SimpleDateFormat dfmin = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			Calendar currentTime = Calendar.getInstance();
			
			String folder = "";
			
			
			currentTime.add(Calendar.DAY_OF_MONTH, -1);
			folder = dfmin.format(currentTime.getTimeInMillis());
			
			
			
			//读取改目录下所有文件:
			File filefolder = new File("data/" + "scoredata/" + folder + "/");
			
			if(!filefolder.exists()){
				return false;
			}
			
			File flist[] = filefolder.listFiles();
			
			int linenum = 0;
			
			for(File f : flist){
				if(f.isDirectory()){
					//System.out.println("fuck directory here");
				}else{
					//System.out.println(f.getAbsolutePath());
					//
					
					
					BufferedReader freader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
					
					String str = "";
					
					linenum = 0;
					
					while ((str = freader.readLine()) != null) {
						
						String[] contents = str.split(",");
						
						
						tmpscoresDetailsVec.add(contents);
						
							
						}
					
					
					
					
					if(null != freader){
						freader.close();
					}
					
					
				}
				
				
				
				
			}
			
			
			
			
			//开始更改
			
			
			for(int i =0; i < tmpscoresDetailsVec.size(); i++){
				if(itemin[SCORENEWINDEX.TIME.ordinal()].equals(tmpscoresDetailsVec.elementAt(i)[SCORENEWINDEX.TIME.ordinal()]) && 
						itemin[SCORENEWINDEX.ID.ordinal()].equals(tmpscoresDetailsVec.elementAt(i)[SCORENEWINDEX.ID.ordinal()])){
					
					
					tmpscoresDetailsVec.elementAt(i)[SCORENEWINDEX.SCORE.ordinal()] = itemin[SCORENEWINDEX.SCORE.ordinal()];
					tmpscoresDetailsVec.elementAt(i)[SCORENEWINDEX.STATUS.ordinal()] = itemin[SCORENEWINDEX.STATUS.ordinal()];
					System.out.println("更新前一天比赛比分成功:"+ Arrays.toString(tmpscoresDetailsVec.elementAt(i)));
					
					
					File filewritefolder = new File("data/" + "scoredata/" + folder + "/");
					
					if(!filewritefolder.exists()){
						
						
						
						filewritefolder.mkdir();
					}
					
					String filenametmp = "score"
							+ ".data";
					
					
					
					
					
					File file = new File("data/" + "scoredata/" + folder + "/" + filenametmp);
					
					if(!file.exists()){
						System.out.println("create scoredata/" + filenametmp);
						System.out.println("错误，文件应该存在！！！！！！！:"+ Arrays.toString(tmpscoresDetailsVec.elementAt(i)));
						file.createNewFile();
					}
					
					BufferedWriter fwlocal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
					
					
					
					
					for(int ii = 0; ii < tmpscoresDetailsVec.size(); ii++){
						
						String[] item = tmpscoresDetailsVec.elementAt(ii);
						
						
						
						String str = "";
						for(int j = 0; j < item.length; j++){
							if(j == 0){
								str = item[j];
							}else{
								str = str + "," + item[j];
							}
						}
						
						fwlocal.append(str);
						fwlocal.newLine();
						fwlocal.flush();

					}
					
					fwlocal.close();
					
					
					
					
					
					System.out.println("回写成功");
					
					
					
					
					
					
					
					
					break;
				}
			}
			
			
			
			
			
			
			
			
			
			

			
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	


	
	
	
	
	
	public void initializeRqpmap(){
		rqpmap.put("平手", 0.0);
		rqpmap.put("平手/半球", 0.25);
		rqpmap.put("半球", 0.5);
		rqpmap.put("半球/一球", 0.75);
		rqpmap.put("一球", 1.0);
		rqpmap.put("一球/球半", 1.25);
		rqpmap.put("球半", 1.5);
		rqpmap.put("球半/两球", 1.75);
		rqpmap.put("两球", 2.0);
		rqpmap.put("两球/两球半", 2.25);
		rqpmap.put("两球半", 2.5);
		rqpmap.put("两球半/三球", 2.75);
		rqpmap.put("三球", 3.0);
		rqpmap.put("三球/三球半", 3.25);
		rqpmap.put("三球半", 3.5);
		rqpmap.put("三球半/四球", 3.75);
		rqpmap.put("四球", 4.0);
		rqpmap.put("四球/四球半", 4.25);
		rqpmap.put("四球半", 4.5);
		rqpmap.put("四球半/五球", 4.75);
		rqpmap.put("五球", 5.0);
		rqpmap.put("五球/五球半", 5.25);
		rqpmap.put("五球半", 5.5);
		rqpmap.put("五球半/六球", 5.75);
		rqpmap.put("六球", 6.0);
		rqpmap.put("六球/六球半", 6.25);
		rqpmap.put("六球半", 6.5);
		rqpmap.put("六球半/七球", 6.75);
		rqpmap.put("七球", 7.0);
		rqpmap.put("七球/七球半", 7.25);
		rqpmap.put("七球半", 7.5);
		rqpmap.put("七球半/八球", 7.75);
		rqpmap.put("八球", 8.0);
		rqpmap.put("八球/八球半", 8.25);
		rqpmap.put("八球半", 8.5);
		rqpmap.put("八球半/九球", 8.75);
		rqpmap.put("九球", 9.0);
		rqpmap.put("九球/九球半", 9.25);
		rqpmap.put("九球半", 9.5);
		rqpmap.put("九球半/十球", 9.75);
		rqpmap.put("十球", 10.0);

	}
	
	
	public Vector<String[]> getpreviousdetailsbyday(String date){
		try{
			
			if(lastgetpredatatime.equals(date)){
				return scoresPreviousDetailsVec;
			}
			
			
			//读取改目录下所有文件:
			File filefolder = new File("data/" + "scoredata/" + date + "/");
			
			if(!filefolder.exists()){
				return null;
			}
			
			File flist[] = filefolder.listFiles();
			
			int linenum = 0;
			
			for(File f : flist){
				if(f.isDirectory()){
					//System.out.println("fuck directory here");
				}else{
					
					if(scoresPreviousDetailsVec.size() != 0){
						scoresPreviousDetailsVec.clear();
					}
					
					
					BufferedReader freader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
					
					String str = "";
					
					linenum = 0;
					
					while ((str = freader.readLine()) != null) {
						String[] contents = str.split(",");
						scoresPreviousDetailsVec.add(contents);													
					}
					
					
					
					
					if(null != freader){
						freader.close();
					}
					
					
				}
				
				
				
				
			}
			
			lastgetpredatatime = date;
			
			return scoresPreviousDetailsVec;
			
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
	
	public void setnewwndstatetxt(String txt){
		scoreNewDetailsWnd.setStateText(txt);
	}
	
	public void setnewwndstatecor(Color c){
		scoreNewDetailsWnd.setStateColor(c);
	}
	
	
    public void copyTofinalScoresDetails(){
    	
		
    	
    	lockeFinalScoreDetails.writeLock().lock();
    	
    	if(finalScoreDetailsVec.size() != 0){
    		finalScoreDetailsVec.clear();
    	}
    	
    	for(int i = 0; i < scoresDetailsVec.size(); i++ ){
    		finalScoreDetailsVec.add(scoresDetailsVec.elementAt(i).clone());
    	}
    	
    	//finalEventDetailsVec = (Vector<String[]>)eventDetailsVec.clone();
    	
    	lockeFinalScoreDetails.writeLock().unlock();

    }
    
    public Vector<String[]> getFinalScoresDetails(){
    	Vector<String[]> vec = new Vector<String[]>();
    	lockeFinalScoreDetails.readLock().lock();
    	
    	for(int i = 0; i < finalScoreDetailsVec.size(); i++){
    		vec.add(finalScoreDetailsVec.elementAt(i).clone());
    	}
    	
    	//vec = (Vector<String[]>)finalEventDetailsVec.clone();
    	lockeFinalScoreDetails.readLock().unlock();
    	return vec;
    }
	
	
	
	
	public void updateEventsDetailsData(){
		scoreNewDetailsWnd.updateEventsDetails(scoresDetailsVec);
	}
	
	
	public void showscoredetailswnd(){
		scoreNewDetailsWnd.setVisible(true);
	}
	
	
	
/*	public void clearScoredetails(){
		if(scoresDetailsVec.size() != 0){
			scoresDetailsVec.clear();
		}
	}*/
	
	
	
	public static String escapeExprSpecialWord(String keyword) {  
	    if (null != keyword) {  
	        String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };  
	        for (String key : fbsArr) {  
	            if (keyword.contains(key)) {  
	                keyword = keyword.replace(key, "\\" + key);  
	            }  
	        }  
	    }  
	    return keyword;  
	}  
	
	
	
	public boolean getScorePankou(){
		try{
			String res = doGet("http://live.titan007.com", "", "");
			
			if(res == null){
				res = doGet("http://live.titan007.com", "", "");
			}
			
			if(null != res){
				
				Calendar calCurrent = Calendar.getInstance();
				calCurrent.setTimeInMillis(System.currentTimeMillis());
				
				//int month = ;
				
				String monStr = String.format("%03d", calCurrent.get(Calendar.MONTH) + 1);
				monStr = "007";
				
				
				
				String dataUri = "http://live.titan007.com/vbsxml/bfdata.js?r=" + monStr + Long.toString(System.currentTimeMillis());
				
				res = doGet(dataUri, "", "http://live.titan007.com/");
				

				
				
				
				//删除比赛
				Calendar lastdaydeadline = Calendar.getInstance();
				
				lastdaydeadline.set(Calendar.HOUR_OF_DAY, 13);
				
				lastdaydeadline.set(Calendar.MINUTE, 0);

				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				Long currenttime2 = System.currentTimeMillis();
				
				
				if(currenttime2 > lastdaydeadline.getTimeInMillis() && currenttime2 - lastdaydeadline.getTimeInMillis() < 20*60*1000){
					for(Iterator<String[]> iterator = scoresDetailsVec.iterator(); iterator.hasNext();){
						String[] itemtmp = iterator.next();
						java.util.Date startTimeDate = df.parse(itemtmp[SCORENEWINDEX.TIME.ordinal()]);
						

						if(startTimeDate.getTime() < lastdaydeadline.getTimeInMillis() && !itemtmp[SCORENEWINDEX.RQZHONGPAN.ordinal()].equals("")
								&& itemtmp[SCORENEWINDEX.STATUS.ordinal()].contains("完")){							
							System.out.println(df.format(System.currentTimeMillis()) + "删除比赛:" + itemtmp[SCORENEWINDEX.TIME.ordinal()] + itemtmp[SCORENEWINDEX.EVENTNAMNE.ordinal()]);
							iterator.remove();
							continue;
						}
						
						if(startTimeDate.getTime() < lastdaydeadline.getTimeInMillis() && currenttime2 - startTimeDate.getTime() > 130*60*1000){
							System.out.println(df.format(System.currentTimeMillis()) + "超时过多，删除比赛:" + itemtmp[SCORENEWINDEX.TIME.ordinal()] + itemtmp[SCORENEWINDEX.EVENTNAMNE.ordinal()]);
							iterator.remove();
						}
						
					}
				}
				
				
				boolean getalias2 = false;
				
				
				String alias2uri = "http://bf.win007.com/vbsxml/alias2.js";
				String alias2data = doGet(alias2uri, "", "");
				
				if(alias2data == null){
					alias2data = doGet(alias2uri, "", "");
				}
				
				
				
				if(alias2data == null){
					System.out.println("alias2data 拿不到，皇冠队名可能错误");
				}else{
					getalias2 = true;
				}
				
				if(res == null){
					return false;
				}
				
				
				if(res.contains("A[")){
					int ps = -1;
					int pe = -1;

					ps = res.indexOf("A[");
					

				int tmpcontrol = 1;	
					
					//while(ps!= -1 && tmpcontrol<8){
					while(ps!= -1 ){
						
						
						
						ps =  res.indexOf("\"", ps) + 1;
						
						if(-1 == ps){
							break;
						}
						
						pe = res.indexOf("\".", ps);
						
						if(-1 == pe){
							break;
						}
						
						String[] tmp = res.substring(ps, pe).split("\\^");
						
						
						
						String timemin = tmp[11];
						String timedate = tmp[12];
						
						
						
						String id = tmp[0];
						
						
						String hometeamkey = "nonenone";
						if(!tmp[37].equals("")){
							hometeamkey = "\"" + tmp[37] + "_" + "3"+ "\"";
						}
						String awayteamkey = "nonenone";
						if(!tmp[38].equals("")){
							awayteamkey = "\"" + tmp[38] + "_" + "3"+ "\"";
						}
						
						
						String leaguename = tmp[2];
						
						if(leaguename.contains("<")){
							int tmpps = leaguename.indexOf("<");
							int tmppe = leaguename.indexOf(">",tmpps);
							
							String str = leaguename.substring(tmpps, leaguename.length());
							
							leaguename = leaguename.replace(str, "");
						}
						
						
						
						
						String hometeam = tmp[5];

						if(null != alias2data && alias2data.contains(hometeamkey)){
							int ps1 = alias2data.indexOf(hometeamkey);
							ps1 = alias2data.indexOf("[", ps1) + 2;
							int pe1 = alias2data.indexOf("]",ps1) - 1;
							hometeam = alias2data.substring(ps1, pe1);
						}
						
						
						
						
						if(hometeam.contains("<")){
							int tmpps = hometeam.indexOf("<");
							int tmppe = hometeam.indexOf(">",tmpps);
							
							String str = hometeam.substring(tmpps, hometeam.length());
							
							hometeam = hometeam.replace(str, "");
						}
	
						String awayteam = tmp[8];
						
						if(null != alias2data && alias2data.contains(awayteamkey)){
							int ps1 = alias2data.indexOf(awayteamkey);
							ps1 = alias2data.indexOf("[", ps1) + 2;
							int pe1 = alias2data.indexOf("]",ps1) - 1;
							awayteam = alias2data.substring(ps1, pe1);
						}

						
						if(awayteam.contains("<")){
							int tmpps = awayteam.indexOf("<");
							int tmppe = awayteam.indexOf(">",tmpps);
							
							String str = awayteam.substring(tmpps, awayteam.length());
							
							awayteam = awayteam.replace(str, "");
						}
						
						String score = tmp[14] + ":" + tmp[15];
						
						
						
						String[] item = new String[SCORENEWINDEX.SIZE.ordinal()];
						
						for(int ii = 0; ii < item.length; ii++){
							item[ii] = "";
						}
						

						item[SCORENEWINDEX.LEAGUENAME.ordinal()] = leaguename;
						item[SCORENEWINDEX.EVENTNAMNE.ordinal()] = hometeam + " vs " + awayteam;
						item[SCORENEWINDEX.ID.ordinal()] = id;
						
						
						int findscoreindex = -1;
						for(int i = 0; i <scoresDetailsVec.size(); i++ ){
							if(scoresDetailsVec.elementAt(i)[SCORENEWINDEX.EVENTNAMNE.ordinal()].equals(item[SCORENEWINDEX.EVENTNAMNE.ordinal()])
									|| scoresDetailsVec.elementAt(i)[SCORENEWINDEX.ID.ordinal()].equals(item[SCORENEWINDEX.ID.ordinal()])){
								findscoreindex = i;
								break;
							}
						}
						
						
						
						if((findscoreindex != -1) && ((System.currentTimeMillis() - df.parse(scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.TIME.ordinal()]).getTime()) > 180*60*1000) && 
								(scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.RQCHUPAN.ordinal()].equals("") || scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.RQCHUPAN.ordinal()].equals("-"))){
							scoresDetailsVec.remove(findscoreindex);
							ps = res.indexOf("A[", pe);
							continue;
						}						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						

						

						
						String[] filedArry = timedate.split(",");
						
						String timeStr = filedArry[0] + "-" + String.format("%02d", Integer.parseInt(filedArry[1]) + 1) + "-" + String.format("%02d", Integer.parseInt(filedArry[2])) + " " + timemin;

						
						String correcttime = timeStr;
						
						
						if(findscoreindex != -1){
							timeStr = scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.TIME.ordinal()];
						}
						
						
						long eventtime = df.parse(timeStr).getTime();
						
						
						if(currenttime2 < lastdaydeadline.getTimeInMillis() && eventtime >= lastdaydeadline.getTimeInMillis()){

							ps = res.indexOf("A[", pe);
							continue;
						}
						
						
						/*if(currenttime2 > lastdaydeadline.getTimeInMillis() && currenttime2 - lastdaydeadline.getTimeInMillis() < 180*60*1000){
							if(eventtime < lastdaydeadline.getTimeInMillis()){
								ps = res.indexOf("A[", pe);
								continue;
							}
						}*/

						
						
						
						

						
						

						
						
						

						 if(tmp[13].equals("1")){
							 Calendar beginTime = Calendar.getInstance();
							 beginTime.set(Integer.parseInt(filedArry[0]), Integer.parseInt(filedArry[1]), Integer.parseInt(filedArry[2]),
									 Integer.parseInt(filedArry[3]), Integer.parseInt(filedArry[4]));
							 
							 int time = (int)((System.currentTimeMillis() - beginTime.getTimeInMillis())/60000);
							 
							 if(time < 1){
								 time = 1;
							 }
							 
							 
							 if(time > 45){
								 timeStr = "45'+";
							 }else{
								 timeStr = Integer.toString(time) + "'";
							 }
							 
						 }else if(tmp[13].equals("3")){
							 Calendar beginTime = Calendar.getInstance();
							 beginTime.set(Integer.parseInt(filedArry[0]), Integer.parseInt(filedArry[1]), Integer.parseInt(filedArry[2]),
									 Integer.parseInt(filedArry[3]), Integer.parseInt(filedArry[4]));
							 
							 int time = (int)((System.currentTimeMillis() - beginTime.getTimeInMillis())/60000);
							 time = time + 46;
							 
							 
							 
							 if(time > 90){
								 timeStr = "90'+";
							 }else{
								 timeStr = Integer.toString(time) + "'";
							 }
						 }else if(tmp[13].equals("2")){
							 timeStr = "中";
						 }else if(tmp[13].equals("-1") || tmp[13].equals("4")){
							 
							timeStr = "完";
							 
							if(findscoreindex != -1){
								scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.STATUS.ordinal()] = timeStr;
								scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.SCORE.ordinal()] = score;
								
								if(getalias2 == true){
									scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.EVENTNAMNE.ordinal()] = item[SCORENEWINDEX.EVENTNAMNE.ordinal()];
								}
								
							}

							 
							 ps = res.indexOf("A[", pe);
							 continue;
						 }else if(tmp[13].equals("0")){
							 timeStr = "";
							 
						 }else{
							 ps = res.indexOf("A[", pe);
							 continue;
						 }
						 
						 
						item[SCORENEWINDEX.TIME.ordinal()] = correcttime;
						item[SCORENEWINDEX.STATUS.ordinal()] = timeStr;
						item[SCORENEWINDEX.SCORE.ordinal()] = score;
						 

						
						//精简赛事
						int leagps = res.indexOf("B[");
						leagps = res.indexOf(leaguename, leagps);
						//leagps = res.indexOf("\"", leagps);
						int leagpe = res.indexOf("\".", leagps);
						
						String Barrystr = res.substring(leagps, leagpe);
						
						String[] Barry = Barrystr.split("\\^");
						
						if(Barry[5].equals("1")){
							item[SCORENEWINDEX.IMPORTANTCUP.ordinal()] = "1";
						}else{
							item[SCORENEWINDEX.IMPORTANTCUP.ordinal()] = "0";

						}
						//精简赛事

						
						//初盘终盘

						
						if(findscoreindex != -1 && !scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.RQZHONGPAN.ordinal()].equals("") 
								&& !scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.DXQZHONGPAN.ordinal()].equals("")){
							//scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.TIME.ordinal()] = item[SCORENEWINDEX.TIME.ordinal()];
							scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.STATUS.ordinal()] = item[SCORENEWINDEX.STATUS.ordinal()];
							scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.SCORE.ordinal()] = item[SCORENEWINDEX.SCORE.ordinal()];
							ps = res.indexOf("A[", pe);
							continue;
						}
						
						
						//没有盘口的比赛
						if(findscoreindex != -1 && !scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.STATUS.ordinal()].equals("")
								&& scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.RQCHUPAN.ordinal()].equals("-")){
							ps = res.indexOf("A[", pe);
							continue;
						}
						
						
						//未开赛
						if(tmp[13].equals("0")){
							if(findscoreindex == -1){
								scoresDetailsVec.add(item);
							}else if(getalias2 == true){
								scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.EVENTNAMNE.ordinal()] = item[SCORENEWINDEX.EVENTNAMNE.ordinal()];
							}

							ps = res.indexOf("A[", pe);
							continue;
						}
						
						
						Thread.sleep(avoidfrequencysleep*1000);
						
						
						String asianoddsuri = "http://vip.win007.com/AsianOdds_n.aspx?id=" + id;
						String onegameres = doGet(asianoddsuri, "", "");
						
						if(onegameres == null){
							onegameres = doGet(asianoddsuri, "", "");
						}
						
						while(null != onegameres && onegameres.contains("操作太频繁了")){
							avoidfrequencysleep++;
							Thread.sleep(avoidfrequencysleep*1000);
							onegameres = doGet(asianoddsuri, "", "");
						}
						

						if(onegameres != null && onegameres.contains("即时") && onegameres.contains("全场对比")){
							
							avoidfrequencysleep = 5;
							
							System.out.println("parse RQP:" + item[SCORENEWINDEX.EVENTNAMNE.ordinal()]);
							
							
							
							int ps1 = onegameres.indexOf(">SB ");
							if(ps1 != -1){
								

								
								ps1= onegameres.indexOf("<td", ps1) + 1;
								ps1 = onegameres.indexOf("<td", ps1) + 1;
								ps1 = onegameres.indexOf("<td",ps1)+ 1;
								ps1 = onegameres.indexOf(">",ps1) + 1;
								
								int pe1 = onegameres.indexOf("<", ps1);
								item[SCORENEWINDEX.RQCHUPAN.ordinal()] = onegameres.substring(ps1, pe1);			
								
								
								String findKey = "";
								if(onegameres.contains("终盘")){
									findKey = "wholeOdds";
								}else{
									findKey = "wholeLastOdds";
								}
								
								
								
								
								ps1= onegameres.indexOf(findKey, ps1) + 1;
								ps1= onegameres.indexOf(findKey, ps1) + 1;
								ps1= onegameres.indexOf(">", ps1) + 1;
								pe1 = onegameres.indexOf("<", ps1);
								item[SCORENEWINDEX.RQZHONGPAN.ordinal()] = onegameres.substring(ps1, pe1);
								
								double zhongpan = 0.0;
								double chupan = 0.0;
								
								if(!item[SCORENEWINDEX.RQZHONGPAN.ordinal()].equals("") && !item[SCORENEWINDEX.RQZHONGPAN.ordinal()].equals("-")){
									System.out.println(item[SCORENEWINDEX.RQZHONGPAN.ordinal()]);
									zhongpan = rqpmap.get(item[SCORENEWINDEX.RQZHONGPAN.ordinal()].replace("受让", ""));
								}else{
									item[SCORENEWINDEX.RQZHONGPAN.ordinal()] = "-";
								}
								
								if(!item[SCORENEWINDEX.RQCHUPAN.ordinal()].equals("") && !item[SCORENEWINDEX.RQCHUPAN.ordinal()].equals("-")){
									System.out.println(item[SCORENEWINDEX.RQCHUPAN.ordinal()]);
									chupan = rqpmap.get(item[SCORENEWINDEX.RQCHUPAN.ordinal()].replace("受让", ""));
								}else{
									item[SCORENEWINDEX.RQCHUPAN.ordinal()] = "-";
								}
								 
								
								
								if(item[SCORENEWINDEX.RQZHONGPAN.ordinal()].contains("受让")){
									zhongpan = 0-zhongpan;
								}
								
								if(item[SCORENEWINDEX.RQCHUPAN.ordinal()].contains("受让")){
									chupan = 0-chupan;
								}
								
								double rqpanas = 0.0;
								
								if(chupan <0.0){
									rqpanas= ((chupan - zhongpan)/interval);
								}else{
									rqpanas= ((zhongpan - chupan)/interval);
								}
								
								
								
								if(rqpanas > 0.0){
									item[SCORENEWINDEX.RQPANAS.ordinal()] = "升" + nf.format(Math.abs(rqpanas));
								}else if(rqpanas <0.0){
									item[SCORENEWINDEX.RQPANAS.ordinal()] = "降" + nf.format(Math.abs(rqpanas));
								}
									
									
								
								
							}
							
							
							
						}
						
						
						Thread.sleep(avoidfrequencysleep*1000);
						
						String overdownurl = "http://vip.win007.com/OverDown_n.aspx?id=" + id;
						onegameres = doGet(overdownurl, "", "");
						
						if(onegameres == null){
							onegameres = doGet(overdownurl, "", "");
						}
						
						
						while(onegameres != null && onegameres.contains("操作太频繁了")){
							avoidfrequencysleep++;
							Thread.sleep(avoidfrequencysleep*1000);
							onegameres = doGet(overdownurl, "", "");
						}
						
						
						
						

						if(onegameres != null && onegameres.contains("即时") && onegameres.contains("全场对比")){
							
							avoidfrequencysleep = 5;
							
							System.out.println("parse DXQ:" + item[SCORENEWINDEX.EVENTNAMNE.ordinal()]);
							
							
							int ps1 = onegameres.indexOf(">SB ");
							if(ps1 != -1){
								ps1= onegameres.indexOf("<td", ps1) + 1;
								ps1 = onegameres.indexOf("<td", ps1) + 1;
								ps1 = onegameres.indexOf("<td",ps1)+ 1;
								ps1 = onegameres.indexOf(">",ps1) + 1;
								
								int pe1 = onegameres.indexOf("<", ps1);
								item[SCORENEWINDEX.DXQCHUPAN.ordinal()] = onegameres.substring(ps1, pe1);			
								
								
								String findKey = "";
								if(onegameres.contains("终盘")){
									findKey = "wholeOdds";
								}else{
									findKey = "wholeLastOdds";
								}
								
								
								ps1= onegameres.indexOf(findKey, ps1) + 1;
								ps1= onegameres.indexOf(findKey, ps1) + 1;
								ps1= onegameres.indexOf(">", ps1) + 1;
								pe1 = onegameres.indexOf("<", ps1);
								item[SCORENEWINDEX.DXQZHONGPAN.ordinal()] = onegameres.substring(ps1, pe1);
								
								double zhongpan = 0.0;
								double chupan = 0.0;
								
								if(item[SCORENEWINDEX.DXQZHONGPAN.ordinal()].contains("/")){
									double pan1 = Double.parseDouble(item[SCORENEWINDEX.DXQZHONGPAN.ordinal()].split("/")[0]);
									double pan2 = Double.parseDouble(item[SCORENEWINDEX.DXQZHONGPAN.ordinal()].split("/")[1]);
									zhongpan = (pan1 + pan2)/2;
								}else if(!item[SCORENEWINDEX.DXQZHONGPAN.ordinal()].equals("") && !item[SCORENEWINDEX.DXQZHONGPAN.ordinal()].equals("-")){
									zhongpan = Double.parseDouble(item[SCORENEWINDEX.DXQZHONGPAN.ordinal()]);
								}else{
									item[SCORENEWINDEX.DXQZHONGPAN.ordinal()] = "-";
								}
								
								if(item[SCORENEWINDEX.DXQCHUPAN.ordinal()].contains("/")){
									double pan1 = Double.parseDouble(item[SCORENEWINDEX.DXQCHUPAN.ordinal()].split("/")[0]);
									double pan2 = Double.parseDouble(item[SCORENEWINDEX.DXQCHUPAN.ordinal()].split("/")[1]);
									chupan = (pan1 + pan2)/2;
								}else if(!item[SCORENEWINDEX.DXQCHUPAN.ordinal()].equals("") && !item[SCORENEWINDEX.DXQCHUPAN.ordinal()].equals("-")){
									chupan = Double.parseDouble(item[SCORENEWINDEX.DXQCHUPAN.ordinal()]);
								}else{
									item[SCORENEWINDEX.DXQCHUPAN.ordinal()] = "-";
								}
								
								
								
								
								double dxqpananas= ((zhongpan - chupan)/interval);
								
								if(dxqpananas > 0.0){
									item[SCORENEWINDEX.DXQPANAS.ordinal()] = "升" + nf.format(dxqpananas);
								}else if(dxqpananas <0.0){
									item[SCORENEWINDEX.DXQPANAS.ordinal()] = "降" + nf.format(Math.abs(dxqpananas));
								}
									
									
								
								
							}
							
							
							
						}
						
						
						
						
						
						if(findscoreindex == -1){
							scoresDetailsVec.add(item);
						}else{
							scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.RQCHUPAN.ordinal()] = item[SCORENEWINDEX.RQCHUPAN.ordinal()];
							scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.RQZHONGPAN.ordinal()] = item[SCORENEWINDEX.RQZHONGPAN.ordinal()];
							scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.RQPANAS.ordinal()] = item[SCORENEWINDEX.RQPANAS.ordinal()];
							scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.DXQCHUPAN.ordinal()] = item[SCORENEWINDEX.DXQCHUPAN.ordinal()];
							scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.DXQZHONGPAN.ordinal()] = item[SCORENEWINDEX.DXQZHONGPAN.ordinal()];
							scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.DXQPANAS.ordinal()] = item[SCORENEWINDEX.DXQPANAS.ordinal()];
							scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.SCORE.ordinal()] = item[SCORENEWINDEX.SCORE.ordinal()];
							scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.STATUS.ordinal()] = item[SCORENEWINDEX.STATUS.ordinal()];
							if(getalias2 == true){
								scoresDetailsVec.elementAt(findscoreindex)[SCORENEWINDEX.EVENTNAMNE.ordinal()] = item[SCORENEWINDEX.EVENTNAMNE.ordinal()];
							}
						}
						

						tmpcontrol++;
						
						
						
						System.out.println(Arrays.toString(item));
						
						
						ps = res.indexOf("A[", pe);
						
						
						
						
						
						
						

					}
					
					
					
					
					
					
					
					
					
					
					
					for(Iterator<String[]> iterator = scoresDetailsVec.iterator(); iterator.hasNext();){
						String[] itemtmp = iterator.next();
						java.util.Date startTimeDate = df.parse(itemtmp[SCORENEWINDEX.TIME.ordinal()]);
						

						if(itemtmp[SCORENEWINDEX.STATUS.ordinal()].equals("完") &&
								(    itemtmp[SCORENEWINDEX.RQZHONGPAN.ordinal()].equals("")  ||  itemtmp[SCORENEWINDEX.RQZHONGPAN.ordinal()].equals("-")    )
								){							
							System.out.println(df.format(System.currentTimeMillis()) + "删除没有盘口的比赛:" + itemtmp[SCORENEWINDEX.TIME.ordinal()] + itemtmp[SCORENEWINDEX.EVENTNAMNE.ordinal()]);
							iterator.remove();
							continue;
						}

					}
					
					
					
					
					
					Calendar currentTime = Calendar.getInstance();
					
					if((currentTime.get(Calendar.HOUR_OF_DAY) >= 13 && currentTime.get(Calendar.MINUTE) >=20 && currentTime.get(Calendar.HOUR_OF_DAY) < 16)  ){
						
						for(Iterator<String[]> iterator = scoresDetailsVec.iterator(); iterator.hasNext();){
							String[] itemtmp = iterator.next();
							java.util.Date startTimeDate = df.parse(itemtmp[SCORENEWINDEX.TIME.ordinal()]);
							

							if((currentTime.get(Calendar.HOUR_OF_DAY) >= 13 && startTimeDate.getHours() < 113)  && (itemtmp[SCORENEWINDEX.STATUS.ordinal()].contains("完"))){
								
								
								System.out.println("准备更新前一天比赛比分:" + Arrays.toString(itemtmp));
								updatelastdaydetails(itemtmp);
								
								
								System.out.println("删除该比赛:" + Arrays.toString(itemtmp));
								iterator.remove();
								continue;
							}

						}
						
					}
					
					

					
					
					
					
					
					savescoresDetailsVectofile();
					
					for(int i = 0; i < scoresDetailsVec.size(); i++){
						
						//System.out.println(Arrays.toString(scoresDetailsVec.elementAt(i)));
						
						
					}

					
				}
				
			}
			
			
			
			
			
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
		return false;
	}
	

	
	
	
	
	public  boolean getScores(){
		
		try{
			String res = doGet("http://live.titan007.com", "", "");
			
			if(res == null){
				res = doGet("http://live.titan007.com", "", "");
			}
			
			if(null != res){
				
				Calendar calCurrent = Calendar.getInstance();
				calCurrent.setTimeInMillis(System.currentTimeMillis());
				
				//int month = ;
				
				String monStr = String.format("%03d", calCurrent.get(Calendar.MONTH) + 1);
				
				monStr = "007";
				
				String yearStr = Integer.toString(calCurrent.get(Calendar.YEAR));
				
				String dataUri = "http://live.titan007.com/vbsxml/bfdata.js?r=" + monStr + Long.toString(System.currentTimeMillis());
				
				res = doGet(dataUri, "", "http://live.titan007.com/");
				
				//System.out.println(res);
				
				Calendar calDeadline = Calendar.getInstance();  
				
				calDeadline.set(Calendar.HOUR_OF_DAY, 13);
				
				int hour = calCurrent.get(Calendar.HOUR_OF_DAY);  
				
				if(hour >= 13){
					calDeadline.add(Calendar.DAY_OF_MONTH, 1);
				}
				
				long deadlineTime = calDeadline.getTimeInMillis();
				
				
				if(res.contains("A[")){
					int ps = -1;
					int pe = -1;

					ps = res.indexOf("A[");
					
					String goaluri = "http://live.titan007.com/vbsxml/goalBf3.xml?r=" + monStr + Long.toString(System.currentTimeMillis());
					String goalres = doGet(goaluri, "", "http://live.titan007.com/");
					
					if(goalres == null){
						goalres = doGet(goaluri, "", "http://live.titan007.com/");
					}
					
					if(goalres == null){
						return false;
					}
					
					String sDatauri = "http://live.titan007.com/vbsxml/sbOddsData.js?r=" + monStr + Long.toString(System.currentTimeMillis());
					String sData = doGet(sDatauri, "", "http://live.titan007.com/");
					
					
					
					String alias2uri = "http://bf.win007.com/vbsxml/alias2.js";
					String alias2data = doGet(alias2uri, "", "");
					
					if(alias2data == null){
						alias2data = doGet(alias2uri, "", "");
					}
					
					if(sData == null){
						sData = doGet(sDatauri, "", "http://live.titan007.com/");
					}
					
					if(sData == null){
						return false;
					}
					
					
					while(ps!= -1){
						
						ps =  res.indexOf("\"", ps) + 1;
						
						if(-1 == ps){
							break;
						}
						
						pe = res.indexOf("\".", ps);
						
						if(-1 == pe){
							break;
						}
						
						String[] tmp = res.substring(ps, pe).split("\\^");
						
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						
						String timemin = tmp[11];
						String timedate = tmp[12];
						
						
						
						String id = tmp[0];
						
						
						String hometeamkey = "nonenone";
						if(!tmp[37].equals("")){
							hometeamkey = "\"" + tmp[37] + "_" + "3"+ "\"";
						}
						String awayteamkey = "nonenone";
						if(!tmp[38].equals("")){
							awayteamkey = "\"" + tmp[38] + "_" + "3"+ "\"";
						}

						
						String[] goalpk = null;
						
						if(goalres.contains(id)){
							
							int ps1 = goalres.indexOf(id);
							int pe1 = goalres.indexOf("<", ps1);
							
							goalpk = goalres.substring(ps1, pe1).split(",");
							
						}
						
						
						String[] item = new String[SCOREINDEX.SIZE.ordinal()];
						
						for(int ii = 0; ii < item.length; ii++){
							item[ii] = "";
						}
						
						if(sData.contains(id)){
							int ps1 = sData.indexOf(id);
							ps1 = sData.indexOf("=", ps1) + 2;
							int pe1 = sData.indexOf(";", ps1);
							
							String dataStr = sData.substring(ps1, pe1);
				
							
			                /*String input = "\\[.*\\]";

			                
			                Pattern p = Pattern.compile(input);
			                Matcher m = p.matcher(dataStr);
			                
			                int j = 0;*/
			                
			                ps1 = dataStr.indexOf("[") + 1;
			                pe1 = dataStr.indexOf("]");
			                    
			                    
		                    String data = dataStr.substring(ps1, pe1);
		                    
		                    //让球盘
	                    	data = data.replace("[", "");
	                    	data = data.replace("]", "");

	                    	String[] dataArry = data.split(",");
	                    	

	                    	
	                    	//即时指数
							int index = (int)(Double.parseDouble(dataArry[4])*4);
							if(index < 0){
								item[SCOREINDEX.RQPANKOU.ordinal()] = "受" + rqpankou[Math.abs(index)];
							}else{
								item[SCOREINDEX.RQPANKOU.ordinal()] = rqpankou[index];
							}

	                    	item[SCOREINDEX.RQHOMESHUI.ordinal()] = dataArry[3];
	                    	item[SCOREINDEX.RQAWAYSHUI.ordinal()] = dataArry[5];
	                    	
	                    	
	                    	if(dataArry.length < 8){
	                    		//System.out.println(dataStr);
	                    	}else{
		                    	//滚球指数
		                    	index = (int)(Double.parseDouble(dataArry[7])*4);
								if(index < 0){
									item[SCOREINDEX.GQRQPANKOU.ordinal()] = "受" + rqpankou[Math.abs(index)];
								}else{
									item[SCOREINDEX.GQRQPANKOU.ordinal()] = rqpankou[index];
								}

		                    	item[SCOREINDEX.GQRQHOMESHUI.ordinal()] = dataArry[6];
		                    	item[SCOREINDEX.GQRQAWAYSHUI.ordinal()] = dataArry[8];
	                    	}
	                    	

		                    	
		                    	
		                    	
		                    
		                    
		                    //大小盘
	                    	
			                ps1 = dataStr.indexOf("[", pe1)+1;
			                ps1 = dataStr.indexOf("[", ps1) + 1;
			                pe1 = dataStr.indexOf("]", ps1);
			                
			                data = dataStr.substring(ps1, pe1);
	                    	
		                    	data = data.replace("[", "");
		                    	data = data.replace("]", "");

		                    	dataArry = data.split(",");
		                    	

		                    	
		                    	//即时指数
								item[SCOREINDEX.DXQPANKOU.ordinal()] = dxqpankou[(int)(Double.parseDouble(dataArry[4])*4)];

		                    	item[SCOREINDEX.DXQHOMESHUI.ordinal()] = dataArry[3];
		                    	item[SCOREINDEX.DXQAWAYSHUI.ordinal()] = dataArry[5];
		                    	
		                    	
		                    	if(dataArry.length < 8){
		                    		//System.out.println(dataStr);
		                    	}else{
			                    	//滚球指数
									item[SCOREINDEX.GQDXQPANKOU.ordinal()] = dxqpankou[(int)(Double.parseDouble(dataArry[7])*4)];

			                    	item[SCOREINDEX.GQDXQHOMESHUI.ordinal()] = dataArry[6];
			                    	item[SCOREINDEX.GQDXQAWAYSHUI.ordinal()] = dataArry[8];
		                    	}
		                    	

		                    	
		                    	
		                    }
			                    
			                    
							
						
						
						
						

						
						String[] filedArry = timedate.split(",");
						
						String timeStr = filedArry[0] + "-" + String.format("%02d", Integer.parseInt(filedArry[1]) + 1) + "-" + String.format("%02d", Integer.parseInt(filedArry[2])) + " " + timemin;
						
						
						
						
						
						
						long eventtime = df.parse(timeStr).getTime();
						
/*						if((System.currentTimeMillis() - eventtime > 180 *60*1000) || eventtime >= deadlineTime){
							ps = res.indexOf("A[", pe);
							continue;
						}*/
						
						
						
						 if(tmp[13].equals("1")){
							 Calendar beginTime = Calendar.getInstance();
							 beginTime.set(Integer.parseInt(filedArry[0]), Integer.parseInt(filedArry[1]), Integer.parseInt(filedArry[2]),
									 Integer.parseInt(filedArry[3]), Integer.parseInt(filedArry[4]));
							 
							 int time = (int)((System.currentTimeMillis() - beginTime.getTimeInMillis())/60000);
							 
							 if(time < 1){
								 time = 1;
							 }
							 
							 
							 if(time > 45){
								 timeStr = "45'+";
							 }else{
								 timeStr = Integer.toString(time) + "'";
							 }
							 
						 }else if(tmp[13].equals("3")){
							 Calendar beginTime = Calendar.getInstance();
							 beginTime.set(Integer.parseInt(filedArry[0]), Integer.parseInt(filedArry[1]), Integer.parseInt(filedArry[2]),
									 Integer.parseInt(filedArry[3]), Integer.parseInt(filedArry[4]));
							 
							 int time = (int)((System.currentTimeMillis() - beginTime.getTimeInMillis())/60000);
							 time = time + 46;
							 
							 
							 
							 if(time > 90){
								 timeStr = "90'+";
							 }else{
								 timeStr = Integer.toString(time) + "'";
							 }
						 }else if(tmp[13].equals("2")){
							 timeStr = "中";
						 }else if(tmp[13].equals("-1")){
							 ps = res.indexOf("A[", pe);
							 continue;
						 }
					      
						
						
						
						
						String leaguename = tmp[2];
						
						if(leaguename.contains("<")){
							int tmpps = leaguename.indexOf("<");
							int tmppe = leaguename.indexOf(">",tmpps);
							
							String str = leaguename.substring(tmpps, leaguename.length());
							
							leaguename = leaguename.replace(str, "");
						}
						
						//精简赛事
						int leagps = res.indexOf("B[");
						leagps = res.indexOf(leaguename, leagps);
						//leagps = res.indexOf("\"", leagps);
						int leagpe = res.indexOf("\".", leagps);
						
						String Barrystr = res.substring(leagps, leagpe);
						
						String[] Barry = Barrystr.split("\\^");
						
						if(Barry[5].equals("1")){
							item[SCOREINDEX.IMPORTANTCUP.ordinal()] = "1";
						}else{
							item[SCOREINDEX.IMPORTANTCUP.ordinal()] = "0";
						}
						//精简赛事
						
						String hometeam = tmp[5];
						
						if(null != alias2data && alias2data.contains(hometeamkey)){
							int ps1 = alias2data.indexOf(hometeamkey);
							ps1 = alias2data.indexOf("[", ps1) + 2;
							int pe1 = alias2data.indexOf("]",ps1) - 1;
							hometeam = alias2data.substring(ps1, pe1);
						}
						
						
						
						
						if(hometeam.contains("<")){
							int tmpps = hometeam.indexOf("<");
							int tmppe = hometeam.indexOf(">",tmpps);
							
							String str = hometeam.substring(tmpps, hometeam.length());
							
							hometeam = hometeam.replace(str, "");
						}
						
						String awayteam = tmp[8];
						if(null != alias2data && alias2data.contains(awayteamkey)){
							int ps1 = alias2data.indexOf(awayteamkey);
							ps1 = alias2data.indexOf("[", ps1) + 2;
							int pe1 = alias2data.indexOf("]",ps1) - 1;
							awayteam = alias2data.substring(ps1, pe1);
						}
						
						if(awayteam.contains("<")){
							int tmpps = awayteam.indexOf("<");
							int tmppe = awayteam.indexOf(">",tmpps);
							
							String str = awayteam.substring(tmpps, awayteam.length());
							
							awayteam = awayteam.replace(str, "");
						}
						
						String score = tmp[14] + ":" + tmp[15];
						
						
						
						item[SCOREINDEX.PLACEHODE.ordinal()] = "";
						item[SCOREINDEX.LEAGUENAME.ordinal()] = leaguename;
						item[SCOREINDEX.EVENTNAMNE.ordinal()] = hometeam + " vs " + awayteam;
						
						SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");
						String todayStr = dfDay.format(System.currentTimeMillis());
						
						if(timeStr.contains(todayStr)){
							timeStr = timeStr.replace(todayStr + " ", "");
						}
						
						item[SCOREINDEX.TIME.ordinal()] = timeStr;
						item[SCOREINDEX.SCORE.ordinal()] = score;

						
						scoresDetailsVec.add(item);
						
						ps = res.indexOf("A[", pe);
						

					}
					
					
					
					for(int i = 0; i < scoresDetailsVec.size(); i++){
						
						//System.out.println(Arrays.toString(scoresDetailsVec.elementAt(i)));
						
						
					}

					
				}
				
			}
			
			
			
			
			
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
		return false;
	}
	
	
	
	
	int defaultTimeout = 20*1000;
	
    {
        // requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
         requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

     	requestConfig = RequestConfig.copy(requestConfig).setRedirectsEnabled(false).build();//��ֹ�ض��� �� �Ա��ȡcookieb18
         //requestConfig = RequestConfig.copy(requestConfig).setConnectTimeout(autoBet.timeOut).setConnectionRequestTimeout(autoBet.timeOut).setSocketTimeout(autoBet.timeOut).build();//���ó�ʱ
         
 /*    	httpclient = HttpClients.custom()
     	        .setDefaultRequestConfig(RequestConfig.custom()
     	            .setCookieSpec(CookieSpecs.DEFAULT).build())
     	        .build();*/
     	
     	httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
     	
    }
	
	
    public   String doGet(String url, String cookies, String referUrl) {
    	
        try {  
            // ����httpget.    
            HttpGet httpget = new HttpGet(url);
            
            if(strCookies != "") {
            	httpget.addHeader("Cookie",strCookies);
            	//System.out.println("set cookies");
            }
            httpget.addHeader("Accept-Encoding","gzip, deflate, sdch");
            httpget.addHeader("Accept-Language","zh-CN,zh;q=0.9");
            httpget.addHeader("Connection","keep-alive");
            httpget.addHeader("Upgrade-Insecure-Requests","1");
            if(url.endsWith("groupName=")){
            	
            	httpget.addHeader("X-Requested-With","XMLHttpRequest");
            	httpget.addHeader("Accept","application/json, text/plain, */*");
            	
            }else{
            	httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            }
            
            
            //
            
            if(referUrl != "")
            {
            	httpget.addHeader("Referer",referUrl);
            	
            }
            
            httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
            //System.out.println("executing request " + httpget.getURI()); 
           
            //���ó�ʱ
            RequestConfig localRequestConfig = RequestConfig.copy(requestConfig).setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();
            
            //localRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
            // requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.).build();

         	//requestConfig = RequestConfig.copy(requestConfig).setRedirectsEnabled(true).build();//��ֹ�ض��� �� �Ա��ȡcookieb18
             //requestConfig = RequestConfig.copy(requestConfig).setConnectTimeout(autoBet.timeOut).setConnectionRequestTimeout(autoBet.timeOut).setSocketTimeout(autoBet.timeOut).build();//���ó�ʱ

            
            httpget.setConfig(localRequestConfig);
            
            // ִ��get����.    
            CloseableHttpResponse response = execute(httpget); 
            
           // setCookie(response);
            
            String statusLine = response.getStatusLine().toString();   
            if(statusLine.indexOf("200 OK") == -1) {
         	   //System.out.println(statusLine); 
            }
            
            try{
            	
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
             	   return response.getFirstHeader("Location").getValue();
                }
                HttpEntity entity = response.getEntity(); 
                
                String res = "";
                
                if(url.contains("alias2")){
                	
               
                	
                	//res = EntityUtils.toString(entity, "UTF-8");
                	
                	String contentType = 	response.getFirstHeader("Content-Type").getValue();
                	
                	String charset = "utf-8";
                    if(contentType.contains("charset")){
                    	int ps = contentType.indexOf("charset");
                    	charset = contentType.substring(ps, contentType.length());
                    	charset = charset.split("=")[1];
                    }
                    
                    if(charset.toLowerCase().contains("utf-8")){
                    	res = EntityUtils.toString(entity, charset);
                    }else{
                    	res = new  String(EntityUtils.toString(entity).getBytes(charset), "gbk");
                    	
                    	System.out.println("alias2 不是ut8-8："+ res);
                    	
                    }
                	
                	
                	
                	
                }else if(url.contains("AsianOdds_n") || url.contains("OverDown_n")){
                	
                	String contentType = 	response.getFirstHeader("Content-Type").getValue();
                	
                	String charset = "gb2312";
                    if(contentType.contains("charset")){
                    	int ps = contentType.indexOf("charset");
                    	charset = contentType.substring(ps, contentType.length());
                    	charset = charset.split("=")[1];
                    }
                	
                	res = new  String(EntityUtils.toString(entity).getBytes(charset), "gbk");
                }else{
                	
                String contentType = 	response.getFirstHeader("Content-Type").getValue();
                
                String charset = "ISO-8859-1";
                if(contentType.contains("charset")){
                	int ps = contentType.indexOf("charset");
                	charset = contentType.substring(ps, contentType.length());
                	charset = charset.split("=")[1];
                }
                	
                	res = new  String(EntityUtils.toString(entity).getBytes(charset), "gbk");
                }
                
                
                
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
    
    public  CloseableHttpResponse  execute(HttpUriRequest request) throws IOException, ClientProtocolException{
    	
    	long time1 = System.currentTimeMillis();
    	long time2 = System.currentTimeMillis();
    	
    	CloseableHttpResponse response;
    	
    	try{
    		response = httpclient.execute(request);    		
    		time2 = System.currentTimeMillis();    		
    		
    		
    	}catch(Exception e){
    		
    		time2 = System.currentTimeMillis();
    		
    		
    		throw e;
    	}
    	

    	
    	return response;
    	
    }
}
