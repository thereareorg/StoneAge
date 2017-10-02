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
		

		
		
	}
	
	public static void setGrabColor(Color cr){
		
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
	
	public  void setLoginParams(String address, String account, String pwd){
		ADDRESS = address;
		ACCOUNT = account;
		PWD = pwd;
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
//            		return parseBet(res);
            	}
            	
            	
            }
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
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
