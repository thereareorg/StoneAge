package HG;

import java.awt.Color;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
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

import P8.EventNameCompare;
import P8.EventsDetailsWindow;
import P8.MyCompare;
import P8.TYPEINDEX;

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


public class HGhttp {
	
	
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
	
	static HGEventsDetailsWindow eventsDetailsDataWindow = new HGEventsDetailsWindow();
	
	
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
		
		//pDataManager.init();
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
	
	
	
    public static void showEventsDeatilsTable(){
    	eventsDetailsDataWindow.setVisible(true);
    }
    
    
    public static void updateEventsDetailsData(){
    	eventsDetailsDataWindow.updateEventsDetails(eventDetailsVec);
    }
    
    
	public static void setGrabStext(){

		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
		
		String timeStr = df.format(System.currentTimeMillis());
		
		String res = "";
		
		if(failedCatchAccount.size() != 0){
			
			res = "失败:";
			
			for(int i = 0; i < failedCatchAccount.size(); i++){
				res = res + "  " + failedCatchAccount.elementAt(i);
			}
		}else{
			res = "成功";
		}
		

		
		eventsDetailsDataWindow.setStateText(timeStr + "   " + res);
	}
	
	public static void setGrabColor(Color cr){
		eventsDetailsDataWindow.setStateColor(cr);
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
    	
    	//finalEventDetailsVec = (Vector<String[]>)eventDetailsVec.clone();
    	
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
	
	public  void setLoginParams(String address, String account, String pwd, String secerityCode){
		ADDRESS = address;
		ACCOUNT = account;
		PWD = pwd;
		SECURITYCODE = secerityCode;

	}
	 
    
    public boolean login(){
    	
    	try{
    		
        	String loginLine = ADDRESS;
        	
        	String res = doGet(loginLine, "", "");
        	
        	if(res.contains("管理端")){
        		res = doGet(loginLine + "new_index.php?type_chk=&langx=", "", "");
        		
        		if(res.contains("Login ID")){
    		        List<NameValuePair> params = new ArrayList<NameValuePair>();		  
    		        params.add(new BasicNameValuePair("langx", "zh-cn"));
    		        params.add(new BasicNameValuePair("username", ACCOUNT));
    		        params.add(new BasicNameValuePair("passwd", PWD));
    		        params.add(new BasicNameValuePair("passwd_safe", SECURITYCODE));
    		        params.add(new BasicNameValuePair("login_layer", "corp"));
    		        
    		        res = doPost(ADDRESS + "app/login_chk.php", params, "");
    		        
    		        if(null != res && res.contains("SUCCESS")){
    		        	
    		        	res = doPost(ADDRESS + "app/login.php", params, "");
    		        	
    		        	if(res.contains("top.user_id")){
    		        		
    		        		int posStart = res.indexOf("top.user_id");
    		        		posStart = res.indexOf("\'", posStart);
    		        		int posEnd = res.indexOf("\'" , posStart+1);
    		        		
    		        		user_id = res.substring(posStart+1, posEnd);
    		        		
    		        		posStart = res.indexOf("top.cid", posEnd);
    		        		posStart = res.indexOf("\'", posStart);
    		        		
    		        		posEnd = res.indexOf("\'", posStart + 1);
    		        		
    		        		cid = res.substring(posStart+1, posEnd);
    		        		
    		        		posStart = res.indexOf("top.WEB_TIME_ZONE", posEnd);
    		        		posStart = res.indexOf("=", posStart);
    		        		
    		        		posEnd = res.indexOf(";", posStart);
    		        		
    		        		String web_time_zone = res.substring(posStart+1, posEnd);
    		        		web_time_zone = web_time_zone.trim();
    		        		
    		        		time_zone = Integer.parseInt(web_time_zone);
    		        		
    			        	//System.out.println(res);
    		        		
    		        		bLogin = true;
    		        		
    			        	return true;
    		        	}

    		        }
    		        
    		        
        		}
        		
        		
        	}
        	
        	//System.out.println(res);
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	

    	
    	return false;
    }
    
    public boolean getTotalBet(){
    	
    	try{
        	String res = "";
        	
            List<NameValuePair> params = new ArrayList<NameValuePair>();		  
            params.add(new BasicNameValuePair("login_layer", "corp"));
            params.add(new BasicNameValuePair("uid", user_id));
            params.add(new BasicNameValuePair("langx", "zh-cn"));
            params.add(new BasicNameValuePair("showtype", "personal"));
            params.add(new BasicNameValuePair("code", "count"));
            params.add(new BasicNameValuePair("selDate", "All"));
            
            res = doPost(ADDRESS + "app/scoll.php", params, "");
            
            if(null != res && res.contains("systime")){
            	int posStart = res.indexOf("systime");
            	posStart = res.indexOf(">", posStart);
            	int posEnd = res.indexOf("<", posStart);
            	
            	String systime = res.substring(posStart + 1, posEnd);

            	
            	
            	SimpleDateFormat dfSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	java.util.Date time = dfSec.parse(systime);
            	
				Calendar americaTime = Calendar.getInstance();  
				americaTime.setTime(time);
				
				americaTime.add(Calendar.HOUR_OF_DAY, 12);
				
				int hour = americaTime.get(Calendar.HOUR_OF_DAY);
				
				int verSub = hour - 8;
				String verSubStr = "";
				
				
				String timeStr = dfSec.format(americaTime.getTimeInMillis());
				String[] tmp = timeStr.split(" ");
				
				timeStr = tmp[0].replace("-", "");
				
				if(verSub >= 10){
					verSubStr = Integer.toString(verSub);
				}else{
					verSubStr = "0" + Integer.toString(verSub);
				}
				
				String ver = timeStr + verSubStr;
				
/*				res = doGet(ADDRESS + "tpl/zh-cn/overview.html?ver=" + ver, "", "");
				
				System.out.println("overView");
				
				
				res = doGet(ADDRESS + "tpl/zh-cn/overview.html?ver=" + ver, "", "");
				
				
				System.out.println(res);
				
				res = doGet(ADDRESS + "tpl/zh-cn/today_ft_league.html?ver=" + ver, "", "");
				
				System.out.println(res);*/

            	
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();		  
                params1.add(new BasicNameValuePair("uid", user_id));
                params1.add(new BasicNameValuePair("login_layer", "corp"));
                params1.add(new BasicNameValuePair("session", "ft"));
                params1.add(new BasicNameValuePair("gtype", "ft"));
                params1.add(new BasicNameValuePair("filter", "Y"));
                params1.add(new BasicNameValuePair("symbol", "more"));
                params1.add(new BasicNameValuePair("gold", "0"));
                params1.add(new BasicNameValuePair("percentage", "full"));
                params1.add(new BasicNameValuePair("down_id", "all"));
                params1.add(new BasicNameValuePair("league_id", "all"));
                
                res = doPost(ADDRESS + "app/ft/get_today_ft_league_wager.php", params1, "");
            	
            	//System.out.println(res);
            	
            	
            	
            	if(null != res && res.contains("no data")){
            		return true;
            	}
            	
            	if(null != res && res.contains("<gidm>")){
            		return parseBet(res);
            	}
            	
            	
            }
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	


    	
    	
    	
    	return false;
    }
    
    
    public static void  testXML(){
    	
    	try{
/*        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        	
    		DocumentBuilder db = dbf.newDocumentBuilder();*/
    		
    		String text = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><serverresponse id='s36'><game id='FT'><league_name>欧洲冠军杯</league_name><league_count>39</league_count><league_gold>44817</league_gold><team_h>马德里体育会</team_h><team_c>皇家马德里</team_c><concede_w3></concede_w3><midfield>N</midfield><live>Y</live><session><date>2017-05-10</date><time>14:45:00</time></session><wtype id='M'><wtype_count>1</wtype_count><wtype_gold>116</wtype_gold><gid id='2713950'><ptype></ptype><rtype id='MN'><count>1</count><gold>116</gold></rtype></gid></wtype><wtype id='OU'><wtype_count>10</wtype_count><wtype_gold>18046</wtype_gold><gid id='2713950'><ptype></ptype><rtype id='OUH'><count>9</count><gold>16046</gold></rtype></gid><gid id='2713954'><ptype></ptype><rtype id='OUH'><count>1</count><gold>2000</gold></rtype></gid></wtype><wtype id='PD'><wtype_count>8</wtype_count><wtype_gold>1120</wtype_gold><gid id='2713950'><ptype></ptype><rtype id='H1C0'><count>1</count><gold>100</gold></rtype><rtype id='H1C1'><count>3</count><gold>420</gold></rtype><rtype id='H1C2'><count>2</count><gold>300</gold></rtype><rtype id='H2C1'><count>1</count><gold>100</gold></rtype><rtype id='H2C2'><count>1</count><gold>200</gold></rtype></gid></wtype><wtype id='R'><wtype_count>20</wtype_count><wtype_gold>25535</wtype_gold><gid id='2713950'><ptype></ptype><rtype id='RC'><count>11</count><gold>16935</gold></rtype><rtype id='RH'><count>8</count><gold>6800</gold></rtype></gid><gid id='2713952'><ptype></ptype><rtype id='RH'><count>1</count><gold>1800</gold></rtype></gid></wtype></game></serverresponse>";
    		
    		String text1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><serverresponse id='s50'><game id='FT'><league_name>法国乙组联赛</league_name><league_count>2</league_count><league_gold>400</league_gold><team_h>爱米恩斯</team_h><team_c>拉华尔</team_c><concede_w3></concede_w3><midfield>N</midfield><live>Y</live><session><date>2017-05-12</date><time>14:30:00</time></session><wtype id='R'><wtype_count>2</wtype_count><wtype_gold>400</wtype_gold><gid id='2738106'><ptype></ptype><rtype id='RH'><count>2</count><gold>400</gold></rtype></gid></wtype></game></serverresponse>";
    		//String txt = new String(text, "UTF-8");
    		
    		String[] txtArry = {text, text1};
    		
    		for(int p = 0; p < txtArry.length; p++){
        		InputStream is = new ByteArrayInputStream(txtArry[p].getBytes("UTF-8"));
        		
            	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            	
    			DocumentBuilder db = dbf.newDocumentBuilder();
    	        Document document = db.parse(is);  
    	        
    	        
    	        
    	        
                NodeList list = document.getElementsByTagName("serverresponse");        
    	        Element element = (Element)list.item(0);  
    	        
    	      
    	        
                String league_name = element.getElementsByTagName("league_name").item(0).getFirstChild().getNodeValue();

                String team_h = element.getElementsByTagName("team_h").item(0).getFirstChild().getNodeValue();

                String team_c = element.getElementsByTagName("team_c").item(0).getFirstChild().getNodeValue();

                String live = element.getElementsByTagName("live").item(0).getFirstChild().getNodeValue();

                
                list = element.getElementsByTagName("session");
                
                Element sessionelement = (Element)list.item(0);  
                
                String date = sessionelement.getElementsByTagName("date").item(0).getFirstChild().getNodeValue();

                
                String time = sessionelement.getElementsByTagName("time").item(0).getFirstChild().getNodeValue();

                
                
                list = element.getElementsByTagName("wtype");    

            	
                String eventIdStr = team_h + team_c + date + time;
                
                String eventId = Integer.toString(eventIdStr.hashCode());
                
            	
    			int index = 0;
    			
    			boolean eventIDexist = false;
    			
    			for(index = 0; index < eventDetailsVec.size(); index++){
    				if(eventDetailsVec.elementAt(index)[TYPEINDEX.EVENTID.ordinal()].equals(eventId)){	//todo gidm是否唯一
    					eventIDexist = true;
    					break;
    				}
    			}
            	
    			
    			if(eventIDexist == false){
    				
        			String[] row = new String[8];

        			
        			row[TYPEINDEX.EVENTID.ordinal()] = eventId;
        			row[TYPEINDEX.LEAGUENAME.ordinal()] = league_name;

        			row[TYPEINDEX.TIME.ordinal()] = date + " " + time;            			            			            			
        			row[TYPEINDEX.EVENTNAMNE.ordinal()] = team_h + "-vs-" + team_c;
        			
        			row[TYPEINDEX.PERIOD0HOME.ordinal()] = "0";
        			row[TYPEINDEX.PERIOD0OVER.ordinal()] = "0";
        			row[TYPEINDEX.PERIOD1HOME.ordinal()] = "0";
        			row[TYPEINDEX.PERIOD1OVER.ordinal()] = "0";
    				eventDetailsVec.add(row);
    			}
    			
    			
                for(int i = 0; i < list.getLength(); i++){
                	Element wtypeelement = (Element)list.item(i);
                	String wtypeId = wtypeelement.getAttributes().getNamedItem("id").getNodeValue();
                	if(wtypeId.toUpperCase().equals("OU")){
                		
                		NodeList rtypeList = wtypeelement.getElementsByTagName("rtype");
                		
                		for(int j = 0; j < rtypeList.getLength(); j++){
                			Element rtypeElement = (Element)rtypeList.item(j);
                			
                			String rtypeId = rtypeElement.getAttributes().getNamedItem("id").getNodeValue();
                			
                			System.out.println(rtypeId);
                			
                			
                    		if(rtypeId.toUpperCase().equals("OUH")){       //客场  
                    			
                    			String goldStr = rtypeElement.getElementsByTagName("gold").item(0).getFirstChild().getNodeValue();                        			
        						String countStr = rtypeElement.getElementsByTagName("count").item(0).getFirstChild().getNodeValue();            						
        						String allp0hStr = eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()];
        						
        						if(allp0hStr.contains("=")){
        							String[] tmp = allp0hStr.split("=");
        							String[] tmp1 = tmp[0].split("-");
        							String hgoldStr = tmp1[0];
        							String cgoldStr = tmp1[1];
        							String oldhgoldStr = tmp1[0];
        							hgoldStr = hgoldStr.replace("(", "");
        							hgoldStr = hgoldStr.replace(")", "");
        							
        							cgoldStr = cgoldStr.replace("(", "");
        							cgoldStr = cgoldStr.replace(")", "");
        							
        							int hgold = 0;
        							int cgold = 0;
        							int hcount = 0;
        							int ccount = 0;
        							
        							if(hgoldStr.contains("|")){
        								tmp = hgoldStr.split("\\|");
        								hgold = Integer.parseInt(tmp[0]);
        								hcount = Integer.parseInt(tmp[1]);
        							}
        							
        							if(cgoldStr.contains("|")){
        								tmp = cgoldStr.split("\\|");
        								cgold = Integer.parseInt(tmp[0]);
        								ccount = Integer.parseInt(tmp[1]);
        							}
        							
        							goldStr = Integer.toString(Integer.parseInt(goldStr) + cgold);
        							countStr = Integer.toString(Integer.parseInt(countStr) + ccount);
        							
        							int dvalue = hgold - Integer.parseInt(goldStr);
        							
        							
        							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()] = oldhgoldStr + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + Integer.toString(dvalue);
        							
        						}else{
        							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()] = "(0|0)" + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + "-" + goldStr;
        						}
        						

                    		}else if(rtypeId.toUpperCase().equals("OUC")){//OUC是主队
        						
                    			String goldStr = rtypeElement.getElementsByTagName("gold").item(0).getFirstChild().getNodeValue();                        			
        						String countStr = rtypeElement.getElementsByTagName("count").item(0).getFirstChild().getNodeValue();            						
        						String allp0hStr = eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()];
        						
        						if(allp0hStr.contains("=")){
        							String[] tmp = allp0hStr.split("=");
        							String[] tmp1 = tmp[0].split("-");
        							String hgoldStr = tmp1[0];
        							String cgoldStr = tmp1[1];
        							String oldcgoldStr = tmp1[1];
        							hgoldStr = hgoldStr.replace("(", "");
        							hgoldStr = hgoldStr.replace(")", "");
        							
        							cgoldStr = cgoldStr.replace("(", "");
        							cgoldStr = cgoldStr.replace(")", "");
        							
        							int hgold = 0;
        							int cgold = 0;
        							int hcount = 0;
        							int ccount = 0;
        							
        							if(hgoldStr.contains("|")){
        								tmp = hgoldStr.split("\\|");
        								hgold = Integer.parseInt(tmp[0]);
        								hcount = Integer.parseInt(tmp[1]);
        							}
        							
        							if(cgoldStr.contains("|")){
        								tmp = cgoldStr.split("\\|");
        								cgold = Integer.parseInt(tmp[0]);
        							}
        							
        							goldStr = Integer.toString(Integer.parseInt(goldStr) + hgold);
        							countStr = Integer.toString(Integer.parseInt(countStr) + hcount);
        							
        							int dvalue = Integer.parseInt(goldStr) - cgold;
        							
        							
        							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()] = "(" + goldStr + "|" + countStr + ")" + "-" + oldcgoldStr + "=" + Integer.toString(dvalue);
        							
        						}else{
        							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()] = "(" + goldStr + "|" + countStr + ")"  + "-" + "(0|0)" + "=" + goldStr;
        						}
                    			
                    		}
                			
                		}

                		

                		
                		
                	}else if(wtypeId.toUpperCase().equals("R")){
                		NodeList rtypeList = wtypeelement.getElementsByTagName("rtype");
                		
                		for(int j = 0; j < rtypeList.getLength(); j++){
                			Element rtypeElement = (Element)rtypeList.item(j);
                			
                			String rtypeId = rtypeElement.getAttributes().getNamedItem("id").getNodeValue();
                			
                			System.out.println(rtypeId);
                			
                			
                    		if(rtypeId.toUpperCase().equals("RH")){
                    			String goldStr = rtypeElement.getElementsByTagName("gold").item(0).getFirstChild().getNodeValue();                        			
        						String countStr = rtypeElement.getElementsByTagName("count").item(0).getFirstChild().getNodeValue();            						
        						String allp0hStr = eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()];
        						
        						if(allp0hStr.contains("=")){
        							String[] tmp = allp0hStr.split("=");
        							String[] tmp1 = tmp[0].split("-");
        							String hgoldStr = tmp1[0];
        							String cgoldStr = tmp1[1];
        							String oldcgoldStr = tmp1[1];
        							hgoldStr = hgoldStr.replace("(", "");
        							hgoldStr = hgoldStr.replace(")", "");
        							
        							cgoldStr = cgoldStr.replace("(", "");
        							cgoldStr = cgoldStr.replace(")", "");
        							
        							int hgold = 0;
        							int cgold = 0;
        							int hcount = 0;
        							int ccount = 0;
        							
        							if(hgoldStr.contains("|")){
        								tmp = hgoldStr.split("\\|");
        								hgold = Integer.parseInt(tmp[0]);
        								hcount = Integer.parseInt(tmp[1]);
        							}
        							
        							if(cgoldStr.contains("|")){
        								tmp = cgoldStr.split("\\|");
        								cgold = Integer.parseInt(tmp[0]);
        							}
        							
        							goldStr = Integer.toString(Integer.parseInt(goldStr) + hgold);
        							countStr = Integer.toString(Integer.parseInt(countStr) + hcount);
        							
        							int dvalue = Integer.parseInt(goldStr) - cgold;
        							
        							
        							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()] = "(" + goldStr + "|" + countStr + ")" + "-" + oldcgoldStr + "=" + Integer.toString(dvalue);
        							
        						}else{
        							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()] = "(" + goldStr + "|" + countStr + ")"  + "-" + "(0|0)" + "=" + goldStr;
        						}
                    		
                    			 
                    		}else if(rtypeId.toUpperCase().equals("RC")){
                    			String goldStr = rtypeElement.getElementsByTagName("gold").item(0).getFirstChild().getNodeValue();                        			
        						String countStr = rtypeElement.getElementsByTagName("count").item(0).getFirstChild().getNodeValue();            						
        						String allp0hStr = eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()];
        						
        						if(allp0hStr.contains("=")){
        							String[] tmp = allp0hStr.split("=");
        							String[] tmp1 = tmp[0].split("-");
        							String hgoldStr = tmp1[0];
        							String cgoldStr = tmp1[1];
        							String oldhgoldStr = tmp1[0];
        							hgoldStr = hgoldStr.replace("(", "");
        							hgoldStr = hgoldStr.replace(")", "");
        							
        							cgoldStr = cgoldStr.replace("(", "");
        							cgoldStr = cgoldStr.replace(")", "");
        							
        							int hgold = 0;
        							int cgold = 0;
        							int hcount = 0;
        							int ccount = 0;
        							
        							if(hgoldStr.contains("|")){
        								tmp = hgoldStr.split("\\|");
        								hgold = Integer.parseInt(tmp[0]);
        								hcount = Integer.parseInt(tmp[1]);
        							}
        							
        							if(cgoldStr.contains("|")){
        								tmp = cgoldStr.split("\\|");
        								cgold = Integer.parseInt(tmp[0]);
        								ccount = Integer.parseInt(tmp[1]);
        							}
        							
        							goldStr = Integer.toString(Integer.parseInt(goldStr) + cgold);
        							countStr = Integer.toString(Integer.parseInt(countStr) + ccount);
        							
        							int dvalue = hgold - Integer.parseInt(goldStr);
        							
        							
        							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()] = oldhgoldStr + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + Integer.toString(dvalue);
        							
        						}else{
        							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()] = "(0|0)" + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + "-" + goldStr;
        						}
                    		}
                			
                		}
                }
                
                }
    		}
    		

            
            

        	for(int i  = 0; i < eventDetailsVec.size(); i++){
        		System.out.println(Arrays.toString(eventDetailsVec.elementAt(i)));

			}
            
            
            
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	

    }
    
    
    
    public boolean parseBet(String res){
    	
    	try{
    		
        	int posStart = res.indexOf("<gidm>");
        	
        	while(posStart != -1){
            	
            	int posEnd = res.indexOf("</gidm>", posStart);
            	
            	String gidm = res.substring(posStart+6, posEnd);
            	
            	
            	
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();		  
                params1.add(new BasicNameValuePair("uid", user_id));
                params1.add(new BasicNameValuePair("login_layer", "corp"));
                params1.add(new BasicNameValuePair("session", "ft"));
                params1.add(new BasicNameValuePair("gtype", "ft"));
                params1.add(new BasicNameValuePair("gidm", gidm));
                params1.add(new BasicNameValuePair("symbol", "more"));
                params1.add(new BasicNameValuePair("gold", "0"));
                params1.add(new BasicNameValuePair("percentage", "full"));
                params1.add(new BasicNameValuePair("down_id", "all"));
                params1.add(new BasicNameValuePair("league_id", "all"));
                
                String oneEventres = doPost(ADDRESS + "app/ft/get_today_ft_allbets_wager.php", params1, "");
                
                Thread.currentThread().sleep(1000);
                
                if(oneEventres == null || !oneEventres.contains("league_name")){
                	oneEventres = doPost(ADDRESS + "app/ft/get_today_ft_allbets_wager.php", params1, "");
                	Thread.currentThread().sleep(1000);
                }
                
                if(null != oneEventres && oneEventres.contains("league_name")){
                	
                	//解析一场比赛
                	
                	InputStream is =  new ByteArrayInputStream(oneEventres.getBytes("UTF-8"));
            		
                	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                	
        			DocumentBuilder db = dbf.newDocumentBuilder();
        	        Document document = db.parse(is);  
        	        
        	        
        	        
        	        
                    NodeList list = document.getElementsByTagName("serverresponse");        
        	        Element element = (Element)list.item(0);  
        	        
        	      
        	        
                    String league_name = element.getElementsByTagName("league_name").item(0).getFirstChild().getNodeValue();

                    String team_h = element.getElementsByTagName("team_h").item(0).getFirstChild().getNodeValue();

                    String team_c = element.getElementsByTagName("team_c").item(0).getFirstChild().getNodeValue();

                    String live = element.getElementsByTagName("live").item(0).getFirstChild().getNodeValue();

                    
                    list = element.getElementsByTagName("session");
                    
                    Element sessionelement = (Element)list.item(0);  
                    
                    String date = sessionelement.getElementsByTagName("date").item(0).getFirstChild().getNodeValue();

                    
                    String time = sessionelement.getElementsByTagName("time").item(0).getFirstChild().getNodeValue();

                    
                    
                    list = element.getElementsByTagName("wtype");    

                	
                    String eventIdStr = team_h + team_c + date + time;
                    
                    String eventId = Integer.toString(eventIdStr.hashCode());
                    
                    System.out.println(eventIdStr + ":" + eventId);
                    
                	
        			int index = 0;
        			
        			boolean eventIDexist = false;
        			
        			for(index = 0; index < eventDetailsVec.size(); index++){
        				if(eventDetailsVec.elementAt(index)[TYPEINDEX.EVENTID.ordinal()].equals(eventId)){	//todo gidm是否唯一
        					eventIDexist = true;
        					break;
        				}
        			}
                	
        			
        			if(eventIDexist == false){
        				
            			String[] row = new String[8];

            			
            			row[TYPEINDEX.EVENTID.ordinal()] = eventId;
            			row[TYPEINDEX.LEAGUENAME.ordinal()] = league_name;
            			
            			//change to local time
            			
            			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
            			
            			String timeStr = date + " " + time;
            			
            			java.util.Date Mintime = dfMin.parse(timeStr);
            			
            			Calendar calTime = Calendar.getInstance();
            			calTime.setTime(Mintime);
            			calTime.add(Calendar.HOUR_OF_DAY, 12);
            			
            			//timeStr = dfMin.format(calTime.getTimeInMillis());

            			row[TYPEINDEX.TIME.ordinal()] = Long.toString(calTime.getTimeInMillis());
            			
            			//
            			
            			row[TYPEINDEX.EVENTNAMNE.ordinal()] = team_h + "-vs-" + team_c;
            			
            			row[TYPEINDEX.PERIOD0HOME.ordinal()] = "0";
            			row[TYPEINDEX.PERIOD0OVER.ordinal()] = "0";
            			row[TYPEINDEX.PERIOD1HOME.ordinal()] = "0";
            			row[TYPEINDEX.PERIOD1OVER.ordinal()] = "0";
        				eventDetailsVec.add(row);
        			}
        			
        			
                    for(int i = 0; i < list.getLength(); i++){
                    	Element wtypeelement = (Element)list.item(i);
                    	String wtypeId = wtypeelement.getAttributes().getNamedItem("id").getNodeValue();
                    	if(wtypeId.toUpperCase().equals("OU")){
                    		
                    		NodeList rtypeList = wtypeelement.getElementsByTagName("rtype");
                    		
                    		for(int j = 0; j < rtypeList.getLength(); j++){
                    			Element rtypeElement = (Element)rtypeList.item(j);
                    			
                    			String rtypeId = rtypeElement.getAttributes().getNamedItem("id").getNodeValue();
                    			
                    			
                    			
                    			
                        		if(rtypeId.toUpperCase().equals("OUH")){       //客场  
                        			
                        			String goldStr = rtypeElement.getElementsByTagName("gold").item(0).getFirstChild().getNodeValue();                        			
            						String countStr = rtypeElement.getElementsByTagName("count").item(0).getFirstChild().getNodeValue();            						
            						String allp0hStr = eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()];
            						
            						if(allp0hStr.contains("=")){
            							String[] tmp = allp0hStr.split("=");
            							String[] tmp1 = tmp[0].split("-");
            							String hgoldStr = tmp1[0];
            							String cgoldStr = tmp1[1];
            							String oldhgoldStr = tmp1[0];
            							hgoldStr = hgoldStr.replace("(", "");
            							hgoldStr = hgoldStr.replace(")", "");
            							
            							cgoldStr = cgoldStr.replace("(", "");
            							cgoldStr = cgoldStr.replace(")", "");
            							
            							int hgold = 0;
            							int cgold = 0;
            							int hcount = 0;
            							int ccount = 0;
            							
            							if(hgoldStr.contains("|")){
            								tmp = hgoldStr.split("\\|");
            								hgold = Integer.parseInt(tmp[0]);
            								hcount = Integer.parseInt(tmp[1]);
            							}
            							
            							if(cgoldStr.contains("|")){
            								tmp = cgoldStr.split("\\|");
            								cgold = Integer.parseInt(tmp[0]);
            								ccount = Integer.parseInt(tmp[1]);
            							}
            							
            							goldStr = Integer.toString(Integer.parseInt(goldStr) + cgold);
            							countStr = Integer.toString(Integer.parseInt(countStr) + ccount);
            							
            							int dvalue = hgold - Integer.parseInt(goldStr);
            							
            							
            							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()] = oldhgoldStr + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + Integer.toString(dvalue);
            							
            						}else{
            							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()] = "(0|0)" + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + "-" + goldStr;
            						}
            						

                        		}else if(rtypeId.toUpperCase().equals("OUC")){//OUC是主队
            						
                        			String goldStr = rtypeElement.getElementsByTagName("gold").item(0).getFirstChild().getNodeValue();                        			
            						String countStr = rtypeElement.getElementsByTagName("count").item(0).getFirstChild().getNodeValue();            						
            						String allp0hStr = eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()];
            						
            						if(allp0hStr.contains("=")){
            							String[] tmp = allp0hStr.split("=");
            							String[] tmp1 = tmp[0].split("-");
            							String hgoldStr = tmp1[0];
            							String cgoldStr = tmp1[1];
            							String oldcgoldStr = tmp1[1];
            							hgoldStr = hgoldStr.replace("(", "");
            							hgoldStr = hgoldStr.replace(")", "");
            							
            							cgoldStr = cgoldStr.replace("(", "");
            							cgoldStr = cgoldStr.replace(")", "");
            							
            							int hgold = 0;
            							int cgold = 0;
            							int hcount = 0;
            							int ccount = 0;
            							
            							if(hgoldStr.contains("|")){
            								tmp = hgoldStr.split("\\|");
            								hgold = Integer.parseInt(tmp[0]);
            								hcount = Integer.parseInt(tmp[1]);
            							}
            							
            							if(cgoldStr.contains("|")){
            								tmp = cgoldStr.split("\\|");
            								cgold = Integer.parseInt(tmp[0]);
            							}
            							
            							goldStr = Integer.toString(Integer.parseInt(goldStr) + hgold);
            							countStr = Integer.toString(Integer.parseInt(countStr) + hcount);
            							
            							int dvalue = Integer.parseInt(goldStr) - cgold;
            							
            							
            							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()] = "(" + goldStr + "|" + countStr + ")" + "-" + oldcgoldStr + "=" + Integer.toString(dvalue);
            							
            						}else{
            							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()] = "(" + goldStr + "|" + countStr + ")"  + "-" + "(0|0)" + "=" + goldStr;
            						}
                        			
                        		}
                    			
                    		}

                    		

                    		
                    		
                    	}else if(wtypeId.toUpperCase().equals("R")){
                    		NodeList rtypeList = wtypeelement.getElementsByTagName("rtype");
                    		
                    		for(int j = 0; j < rtypeList.getLength(); j++){
                    			Element rtypeElement = (Element)rtypeList.item(j);
                    			
                    			String rtypeId = rtypeElement.getAttributes().getNamedItem("id").getNodeValue();
                    			
                    			
                    			
                    			
                        		if(rtypeId.toUpperCase().equals("RH")){
                        			String goldStr = rtypeElement.getElementsByTagName("gold").item(0).getFirstChild().getNodeValue();                        			
            						String countStr = rtypeElement.getElementsByTagName("count").item(0).getFirstChild().getNodeValue();            						
            						String allp0hStr = eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()];
            						
            						if(allp0hStr.contains("=")){
            							String[] tmp = allp0hStr.split("=");
            							String[] tmp1 = tmp[0].split("-");
            							String hgoldStr = tmp1[0];
            							String cgoldStr = tmp1[1];
            							String oldcgoldStr = tmp1[1];
            							hgoldStr = hgoldStr.replace("(", "");
            							hgoldStr = hgoldStr.replace(")", "");
            							
            							cgoldStr = cgoldStr.replace("(", "");
            							cgoldStr = cgoldStr.replace(")", "");
            							
            							int hgold = 0;
            							int cgold = 0;
            							int hcount = 0;
            							int ccount = 0;
            							
            							if(hgoldStr.contains("|")){
            								tmp = hgoldStr.split("\\|");
            								hgold = Integer.parseInt(tmp[0]);
            								hcount = Integer.parseInt(tmp[1]);
            							}
            							
            							if(cgoldStr.contains("|")){
            								tmp = cgoldStr.split("\\|");
            								cgold = Integer.parseInt(tmp[0]);
            							}
            							
            							goldStr = Integer.toString(Integer.parseInt(goldStr) + hgold);
            							countStr = Integer.toString(Integer.parseInt(countStr) + hcount);
            							
            							int dvalue = Integer.parseInt(goldStr) - cgold;
            							
            							
            							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()] = "(" + goldStr + "|" + countStr + ")" + "-" + oldcgoldStr + "=" + Integer.toString(dvalue);
            							
            						}else{
            							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()] = "(" + goldStr + "|" + countStr + ")"  + "-" + "(0|0)" + "=" + goldStr;
            						}
                        		
                        			 
                        		}else if(rtypeId.toUpperCase().equals("RC")){
                        			String goldStr = rtypeElement.getElementsByTagName("gold").item(0).getFirstChild().getNodeValue();                        			
            						String countStr = rtypeElement.getElementsByTagName("count").item(0).getFirstChild().getNodeValue();            						
            						String allp0hStr = eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()];
            						
            						if(allp0hStr.contains("=")){
            							String[] tmp = allp0hStr.split("=");
            							String[] tmp1 = tmp[0].split("-");
            							String hgoldStr = tmp1[0];
            							String cgoldStr = tmp1[1];
            							String oldhgoldStr = tmp1[0];
            							hgoldStr = hgoldStr.replace("(", "");
            							hgoldStr = hgoldStr.replace(")", "");
            							
            							cgoldStr = cgoldStr.replace("(", "");
            							cgoldStr = cgoldStr.replace(")", "");
            							
            							int hgold = 0;
            							int cgold = 0;
            							int hcount = 0;
            							int ccount = 0;
            							
            							if(hgoldStr.contains("|")){
            								tmp = hgoldStr.split("\\|");
            								hgold = Integer.parseInt(tmp[0]);
            								hcount = Integer.parseInt(tmp[1]);
            							}
            							
            							if(cgoldStr.contains("|")){
            								tmp = cgoldStr.split("\\|");
            								cgold = Integer.parseInt(tmp[0]);
            								ccount = Integer.parseInt(tmp[1]);
            							}
            							
            							goldStr = Integer.toString(Integer.parseInt(goldStr) + cgold);
            							countStr = Integer.toString(Integer.parseInt(countStr) + ccount);
            							
            							int dvalue = hgold - Integer.parseInt(goldStr);
            							
            							
            							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()] = oldhgoldStr + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + Integer.toString(dvalue);
            							
            						}else{
            							eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()] = "(0|0)" + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + "-" + goldStr;
            						}
                        		}
                    			
                    		}
                    }
                    
                    }
        			
                	
                	
                	
        			//解析一场比赛
                	
                	posStart = res.indexOf("<gidm>", posEnd);
                }else{
                	return false;
                }
                
        	}

        	for(int i  = 0; i < eventDetailsVec.size(); i++){
        		System.out.println(Arrays.toString(eventDetailsVec.elementAt(i)));

			}

        			
        	
        	return true;
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	
    	//return false;

    }
    
    
    public static  void sortEventDetails(){
    	
    	try{
    		
/*    		System.out.println("before sort");
    		
    		for(int k = 0; k<eventDetailsVec.size(); k++ ){
    			
    			String[] outRow = eventDetailsVec.elementAt(k);
    			
    			System.out.println(outRow[0] + "," +outRow[1] + "," + outRow[2] + "," +outRow[3] + "," +outRow[4] + "," +outRow[5] + "," +
    					outRow[6] + "," + outRow[7]);
    		}*/
    		
    		//System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); 

    		
    		
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
    		
    		String currentTime = df.format(System.currentTimeMillis());
    		
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
            	
            	//Comparator cn = new EventNameCompare(); 
            	

            	
            	
            	if(eventDetailsVec.size() != 0){

            		
            		Collections.sort(eventDetailsVec, ct);
            	}
            	
        		
            	if(highShowVec.size() != 0){

            		
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
    	}catch(Exception e){
    		e.printStackTrace();
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
            System.out.println("executing request " + httpget.getURI()); 
           
            //设置超时
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();
            httpget.setConfig(requestConfig);
            
            // 执行get请求.    
            CloseableHttpResponse response = execute(httpget); 
            
            String statusLine = response.getStatusLine().toString();   
            if(statusLine.indexOf("200 OK") == -1) {
         	   System.out.println(statusLine); 
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
            		System.out.println(response.getStatusLine());
            		
            		
            		
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
