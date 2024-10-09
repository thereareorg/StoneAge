package P8;

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


public class ISNhttp {
	
	
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
	
	public static JSONArray JSONISNEvents = null;

	String Bearer = "";//todo 退出时变成""
	
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
        	
        	if(res!=null){

				JSONObject jb = new JSONObject();
				jb.put("loginSource", "ManagerSite");
	        	jb.put("username", ACCOUNT);
	        	jb.put("password", PWD);
        		
        		res = doPost(ADDRESS + "agency-server/api/auth/authenticate",  jb.toString(), "");
        		
        		if(res.contains("成功")) {
        			System.out.println("success");
    				JSONObject jb1 = new JSONObject();
    				jb1.put("secureCode", SECURITYCODE);
    				
    				JSONObject jbres = new JSONObject(res);
    				Bearer = jbres.getString("token");
    		        
        			res = doPost(ADDRESS + "agency-server/api/auth/validateSecureCode",  jb1.toString(), "");
        			if(res.contains("成功")) {
        				System.out.println("success 1");
        				jbres = new JSONObject(res);
        				Bearer = jbres.getString("token");   
        				setIslogin(true);
        				return true;
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
        	
        	JSONObject jb = new JSONObject();
        	//{"queryVO":{"fullStake":0,"sportId":-1},"_search":false,"nd":1668580061218,"rows":10000,"page":1,"sidx":"sportName asc, categoryName asc, eventName asc, ","sord":""}
        	
        	JSONObject queryVO = new JSONObject();
        	queryVO.put("fullStake", 1);
        	queryVO.put("sportId", -1);
        	
        	jb.put("queryVO", queryVO);
        	jb.put("_search", false);
        	jb.put("nd", System.currentTimeMillis());
        	jb.put("rows", 10000);
        	jb.put("sidx", "sportName asc, categoryName asc, eventName asc, ");
            jb.put("sord", "");
        	
            res = doPost(ADDRESS + "agency-server/api/totalbet/hdpOu", jb.toString(), "");
            
            if(res!=null&&res.contains("成功")) {
            	JSONObject jbRes = new JSONObject(res);
            	JSONArray jaRows = jbRes.getJSONArray("rows");
            	
            	//process eventTime variables
    			SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");
    			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    			
    			SimpleDateFormat dfMinISN = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            	
            	for(int i = 0; i <jaRows.length();i++) {// parse all match
            		JSONObject oneRow = jaRows.getJSONObject(i);
            		if(oneRow.getString("sportName").contains("足球")) {  //parse one match
            			String eventName = oneRow.getString("eventName");
            			String eventTime = oneRow.getString("eventTime");
            			

            			//process event time
            			Calendar calCurrent = Calendar.getInstance();
            			calCurrent.add(Calendar.HOUR_OF_DAY, -12);

            			if(eventTime.contains("/")) {
            				Long time = dfMinISN.parse(eventTime).getTime();
            				time = time + 12*60*60*1000;
            				eventTime = dfMin.format(time);
            			}else {
            				String tmp1 = dfDay.format(calCurrent.getTime());
            				eventTime = tmp1 + " " + eventTime;
            				long time = dfMin.parse(eventTime).getTime();
            				time = time + 12*60*60*1000;
            				eventTime = dfMin.format(time);
            			}
            			
            			
            			String leagueName = oneRow.getString("categoryName");
            			JSONArray BetDataList = oneRow.getJSONArray("list");
            			JSONObject danshiData = null;
            			JSONObject rollData = null;
            			
            			for(int j = 0; j < BetDataList.length(); j++) {
            				
            				boolean isLiveData = BetDataList.getJSONObject(j).getBoolean("live");
            				if(isLiveData==false) {// find danshi data... break loop
            					danshiData = BetDataList.getJSONObject(j);            					
            				}else {            					            				
            					rollData = BetDataList.getJSONObject(j);
            					boolean rollMatchExisted = false;
            					
                				for(int k = 0; k < JSONISNEvents.length(); k++) { //check if the roll match is exist
                            		JSONObject event = JSONISNEvents.getJSONObject(k);
                            		String rollName = "【滚动盘】" + eventName;
                            		if(event.getString("eventName").equals(rollName)) {  // the roll match existed. add current data to 
                            			rollMatchExisted = true;
                            			int home = rollData.optInt("fthdp",0);
                    					int over = rollData.optInt("ftou",0);
                            			
                    					JSONObject newScoreData = new JSONObject();
                    					newScoreData.put("score", rollData.get("score"));
                    					newScoreData.put("fthdp", home);
                    					newScoreData.put("ftou", over);
                    					JSONArray scoreDataArry = JSONISNEvents.getJSONObject(k).getJSONArray("scoreData");
                    					
                    					boolean scoreExist = false;
                    					for (int ii = 0; ii < scoreDataArry.length(); ii++) {
                    					    JSONObject scoreData = scoreDataArry.getJSONObject(ii);

                    					    // 如果score匹配 "(0 - 0)"
                    					    if (scoreData.getString("score").equals(newScoreData.getString("score"))) {
                    					        // 原来的ftou和fthdp
                    					        int oldFtou = scoreData.optInt("ftou", 0);
                    					        int oldFthdp = scoreData.optInt("fthdp", 0);

                    					        // 新的ftou和fthdp
                    					        int newFtou = newScoreData.optInt("ftou", 0);
                    					        int newFthdp = newScoreData.optInt("fthdp", 0);

                    					        // 更新ftou和fthdp
                    					        scoreData.put("ftou", oldFtou + newFtou);
                    					        scoreData.put("fthdp", oldFthdp + newFthdp);

                    					        // 已经更新过，跳出循环
                    					        scoreExist = true;
                    					        break;
                    					    }
                    					}

                    					if(scoreExist==false) {
                    						scoreDataArry.put(newScoreData);
                    					}
                    					
                    					JSONISNEvents.getJSONObject(k).put("scoreData", scoreDataArry);
                    					JSONISNEvents.getJSONObject(k).put("home", event.optInt("home",0) +home);
                    					JSONISNEvents.getJSONObject(k).put("over", event.optInt("over",0) +over);
                    					
                            			break;
                            		}
                            	}
                				
                				if(rollMatchExisted==false) {
                					
                					JSONObject currentMatch = new JSONObject();
                					JSONArray scoreDataArry = new JSONArray();
                					JSONObject scoreData = new JSONObject();
                					scoreData.put("score", rollData.get("score"));
                					currentMatch.put("eventName", "【滚动盘】" + eventName);
                					currentMatch.put("eventTime", eventTime);
                					currentMatch.put("leagueName", leagueName);
                					int home = rollData.optInt("fthdp", 0);
                					int over = rollData.optInt("ftou", 0);
                					currentMatch.put("home", home);
                					currentMatch.put("over", over);
                					scoreData.put("fthdp", home);
                					scoreData.put("ftou", over);
                					scoreDataArry.put(scoreData);
                					currentMatch.put("scoreData", scoreDataArry);
                					JSONISNEvents.put(currentMatch);
                					
                				}
            					
            				}
            			}
            			
            			boolean matchExisted = false;            			
            			
            			if(danshiData!=null) { // danshi data is not null. add match data to JSONArray
            				for(int j = 0; j < JSONISNEvents.length(); j++) { //check if the match is exist
                        		JSONObject event = JSONISNEvents.getJSONObject(j);
                        		if(event.getString("eventName").equals(eventName)) {  // the match existed. add current data to 
                        			matchExisted = true;
                        			
                					int home = danshiData.optInt("fthdp",0);
                					int over = danshiData.optInt("ftou",0);
                        			
                					JSONISNEvents.getJSONObject(j).put("home", event.optInt("home",0) +home);
                					JSONISNEvents.getJSONObject(j).put("over", event.optInt("over",0) +over);
                					
                        			break;
                        		}
                        	}
            				
            				if(matchExisted== false) { //new match
            					JSONObject currentMatch = new JSONObject();
            					currentMatch.put("eventName", eventName);
            					currentMatch.put("eventTime", eventTime);
            					currentMatch.put("leagueName", leagueName);
            					int home = danshiData.optInt("fthdp", 0);
            					int over = danshiData.optInt("ftou", 0);
            					currentMatch.put("home", home);
            					currentMatch.put("over", over);
            					JSONISNEvents.put(currentMatch);
            				}
            			}
            		}
            	}
            	return true;
            }
            
            
            
    	}catch(Exception e){
    		e.printStackTrace();
    		
    	}
    	
    	return false;
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
    public String doPost(String url,String formparams, String cookies) {
        return doPost(url, formparams,"UTF-8", cookies);
    }

    public String doPost(String url,String formparams,String charset, String cookies) {


     // 创建httppost   
    	
    	try {
    	
        HttpPost httppost = new HttpPost(url); 
        
/*        if(url.contains("get_today_ft_league_wager.php")){
        	httppost.addHeader("Cookie", strCookies);
        }*/
        
        //httppost.addHeader("Cookie", cookies);
        //httppost.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
        //httppost.addHeader("x-requested-with","XMLHttpRequest");
        httppost.addHeader("Accept-Language","zh-CN,zh;q=0.9");
        httppost.addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        httppost.addHeader("Content-type", "application/json");
        httppost.addHeader("Accept-Encoding","gzip, deflate");
        httppost.addHeader("Connection","keep-alive");
        httppost.addHeader("Cache-Control","max-age=0");
        httppost.addHeader("locale","zh_CN");
        httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");    

        if(Bearer.length()>1) {
        	httppost.addHeader("authorization","Bearer " + Bearer);
        }

        StringEntity requestEntity = new StringEntity(formparams,"utf-8");

           requestEntity.setContentEncoding("UTF-8");
           httppost.setEntity(requestEntity);


            
            
            
//            HttpHost proxy = new HttpHost("127.0.0.1", 1080, "http");
//            
//
//            
//            
//            
//            RequestConfig requestConfigtmp = RequestConfig.custom().setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();
//            requestConfigtmp= RequestConfig.copy(requestConfigtmp).setProxy(proxy).build();
//            httppost.setConfig(requestConfigtmp);
            
            CloseableHttpResponse response = execute(httppost);
            try {
                // 打印响应状态    
            	setCookie(response);
            	//Common.logOut("设置cookie:" + strCookies);
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
            		String location = response.getFirstHeader("Location").getValue();
            		//Common.logOut(response.getStatusLine());
            		
            		
            		
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
                	if(res!=null && (res.contains("æ"))) {
                		res = new  String(res.getBytes("ISO-8859-1"), "utf-8");
                	}
                }
                
                
                if(res != null && res.length() > 0 ){   

//                	if(res.contains("logout_warn")) {
//                		updateStatusCode(-1);
//                	}
                	
                	//Common.logOut(res);
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

