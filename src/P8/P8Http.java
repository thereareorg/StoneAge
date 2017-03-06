package P8;




import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import org.json.*;

import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;





public class P8Http {
	
	
	
     CloseableHttpClient httpclient = null;
     RequestConfig requestConfig = null;
     HttpClientContext clientContext = null;
    
    
     static Vector<String> showLeagueName = new Vector<String>();
    
    public  int pngnumber = 101;
    
    boolean bLogin = false;
    
    
    static PreviousDataWindow pDataWindow = new PreviousDataWindow();
    
    static PreviousDataManager pDataManager = new PreviousDataManager(pDataWindow);
    
    
    
     static EventsDetailsWindow eventsDetailsDataWindow = new EventsDetailsWindow();
    

    public static   Vector<String[]> eventDetailsVec = new Vector<String[]>();
    
    
    private static ReadWriteLock lockeFinalEventsDetails = new ReentrantReadWriteLock();
    
    private static ReadWriteLock lockeSuccessTime = new ReentrantReadWriteLock();
    
    public static Vector<String[]> finalEventDetailsVec = new Vector<String[]>();
    
    public static String successTime = "";
    
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
	
	
	
     public static Vector<String> failedCatchAccount = new Vector<String>();
    

	 String strCookies = "";
	
     Vector<Long> lastTenRequestTime = new Vector<Long>();
     long avgRequestTime = 0;    
     boolean bcalcRequestTime = true;
     boolean bneedChangeLine = false;
    
    
     long lastChangeLineTime = 0;
    
    
    
     int requestFailTimes = 0;
     long lastFailtime = 0;
    
     boolean isNeedRelogin = false;
    
     public boolean isInRelogin = false;
    
	
	
	 String ADDRESS = "";
	 String ACCOUNT = "";
	 String PWD = "";
	 String SECURITYCODE = "";
	
	 int defaultTimeout = 20*1000;
	
	 String line = "";

	
	public  String lineuri = "http://www.p88agent.com/partner/zh-cn/login";
	
	public  String testpy(){

		//System.out.println("用户的当前工作目录:/n"+System.getProperty("user.dir"));
		
		String res = "";  
		
        try{  
            System.out.println("start");  
            Process pr = Runtime.getRuntime().exec("python test.py");  
              
            BufferedReader in = new BufferedReader(new  
                    InputStreamReader(pr.getInputStream()));  
            String line;
            while ((line = in.readLine()) != null) {  
            	res = line;
                System.out.println(res);  
            }  
            in.close();  
            pr.waitFor();              
            System.out.println("end");  
            return res;
        } catch (Exception e){  
                e.printStackTrace();  
            }  
        
        return res;
    }  
	
	public boolean islogin(){
		return bLogin;
	}
	
	public void setIslogin(boolean b){
		bLogin = b;
	}

	
	public static boolean isfailedAccountEmpty(){
		return failedCatchAccount.size() == 0;
	}
	
	
    public  void setIscalcRequestTime(boolean flag){
    	bcalcRequestTime = flag;
    }
    
    public  void setisNeedChangeLine(boolean flag){
    	bneedChangeLine = flag;
    }
    
    public  boolean getIsisNeedChangeLine(){
    	return bneedChangeLine;
    }
    
    public  void clearAvgRequest(){
    	
    	if(lastTenRequestTime.size() >0){
    		lastTenRequestTime.clear();
    	}
    	avgRequestTime = 0;

    }
	
	public static void initShowLeagueName(){
		showLeagueName.add("德国 - 德甲");
		showLeagueName.add("欧足联 - 冠军联赛");
		showLeagueName.add("西班牙 - 西甲");
		showLeagueName.add("意大利 - 甲级联赛");
		showLeagueName.add("法国 - 甲级联赛");
		showLeagueName.add("英格兰 - 超级联赛");
		showLeagueName.add("欧足联 - 欧罗巴联赛");
		
		pDataManager.init();
	}
	
	
    public static void showEventsDeatilsTable(){
    	eventsDetailsDataWindow.setVisible(true);
    }
    
    
    public static void updateEventsDetailsData(){
    	eventsDetailsDataWindow.updateEventsDetails(eventDetailsVec);
    }
    
    public static void copyTofinalEventsDetails(){
    	
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
		
		
		long currentTimeL = System.currentTimeMillis();
    	
		String currentTime = df.format(currentTimeL);
    	
    	lockeFinalEventsDetails.writeLock().lock();
    	finalEventDetailsVec = (Vector<String[]>)eventDetailsVec.clone();
    	
    	lockeFinalEventsDetails.writeLock().unlock();
    	
    	lockeSuccessTime.writeLock().lock();
    	successTime = currentTime + " 成功";
    	lockeSuccessTime.writeLock().unlock();
    }
    
    public static Vector<String[]> getFinalEventsDetails(){
    	Vector<String[]> vec = null;
    	lockeFinalEventsDetails.readLock().lock();
    	vec = (Vector<String[]>)finalEventDetailsVec.clone();
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
	
	public  void setLoginParams(String address, String account, String pwd, String secerityCode){
		ADDRESS = address;
		ACCOUNT = account;
		PWD = pwd;
		SECURITYCODE = secerityCode;

	}
	
	public String getAddress(){
		return ADDRESS;
	}
	
	public String getAccount(){
		return ACCOUNT;
	}
	
	
	public  boolean reLogin(){
		
		setIscalcRequestTime(false);
		
		boolean res =  loginToP8();
		
		setIscalcRequestTime(true);
		
		return res;
	}
	
	
	public  boolean connFailLogin(){
		
		setIscalcRequestTime(false);
		
		while(login() !=  1){
			
			
			
			try{
				Thread.currentThread().sleep(20*1000);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		
		setIscalcRequestTime(true);
		
		return true;
	}

	//0代表网址连接不上，1代表成功，2代表Invalid的用户
	public  int login(){
		
		int res = 0;
		
		
		
		setIscalcRequestTime(false);
		
		strCookies = "";
		
		String linePageUri = ADDRESS + "/partner/zh-cn/login";
		
		int oldTimeout = defaultTimeout;
		
		try{
			

			
			String linePage = doGet(linePageUri, "", "");
	    	
	    	if(linePage == null || linePage.contains(linePageUri)){
	    		linePage = doGet(linePageUri, "", "");
	    	}
	    	
	    	if(linePage == null || linePage.contains(linePageUri)){
	    		linePage = doGet(linePageUri, "", "");
	    	}
	    	
	    	int posStart = -1;
	    	int posEnd = -1;
	    	
	    	//System.out.println(linePage);
	    	
	    	if(linePage != null && linePage.contains("page.main.js?")){
	    		//System.out.println(linePage);

	    		
	    		boolean sucess = loginToP8();
	    		
	    		if(sucess == true){
	    			bLogin = true;
	    			res = 1;
	    		}
	    		else{
	    			res = 2;
	    		}
	    	}
			
		}catch(Exception e){
			defaultTimeout = oldTimeout;
			setIscalcRequestTime(true);
			e.printStackTrace();
		}

		setIscalcRequestTime(true);
		
		defaultTimeout = oldTimeout;
		
		return res;
	}
	
	
    public  boolean loginToP8(){
    	
    	try{
    		
    		String res = "INVALID_CAPTCHA";
    		
    		
    		String keyUri = ADDRESS + "/partner/captcha/key";
    		
    		while(res != null && res.contains("INVALID_CAPTCHA")){
        		String key = getPicNum(keyUri);
        		
        		//System.out.println(key);
        		
        		if(!isNum(key)){
        			key = getPicNum(keyUri);
        		}


            	
                JSONObject params = new JSONObject(true);
                params.put("username", ACCOUNT);
                params.put("password", PWD);
                params.put("captcha", key);
                
                
                
                String loginUri = ADDRESS + "/partner/login/authentication";
                
                //System.out.println(params.toString());
                
                //JSONObject.toJSONString(json, SerializerFeature.SortField);
                
                
                
               // cookiesStr = "language=zh_CN";
                
                res = doPost(loginUri, params.toString(), "", "http://www.p88agent.com/partner/zh-cn/login");
                
                if(res == null){
                	res = doPost(loginUri, params.toString(), "", "http://www.p88agent.com/partner/zh-cn/login");
                }
                	
                
                Thread.currentThread().sleep(1000);
                
                
                //System.out.println(res);
    		}
    		

            
            if(res.contains("INVALID"))
            	return false;
            
            
            String codeGetUri = ADDRESS + "/partner/page/navigation?t=" + Long.toString(System.currentTimeMillis());
            
            
            res = doGet(codeGetUri, "", "");
            
            if(res == null){
            	res = doGet(codeGetUri, "", "");
            }
            
           // System.out.println("codeGetUri res:" + res);
            
            if(res.contains("code")){
            	String codePostUri = ADDRESS + "/partner/code/validate";
            	
                JSONObject codeParams = new JSONObject(true);
                
                int t = (int)(120+Math.random()*(500-1+1));
                
                codeParams.put("securityCode", SECURITYCODE);
                codeParams.put("t", t);
            	
            	res = doPost(codePostUri, codeParams.toString(), "", "");
            	
            	if(res == null){
            		res = doPost(codePostUri, codeParams.toString(), "", "");
            	}
            	
            	//System.out.println("codePostUri res:" + res);
            	
            	if(res.contains("VALID")){
            		return true;
            	}
            }
            
            
            
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}

	        

	        	
        
        

    	return false;
    }
    
    
    
    public  boolean getTotalP8Bet(){
    	
    	try{
        	

        	
        	String betUri = ADDRESS + "/partner-webservice/total-bet?betType=HDP&sportId=29&leagueId=0&eventId=0&groupName=";

//        	String betUri = ADDRESS + "/partner-webservice/total-bet?betType=HDP&sportId=0&leagueId=0&eventId=0&groupName=";

        	
        	String res = doGet(betUri, "", "http://www.p88agent.com/partner-ui/page/zh-cn/account-balance");
        	
        	if(res == null){
        		res = doGet(betUri, "", "http://www.p88agent.com/partner-ui/page/zh-cn/account-balance");
        		
        	}
        	
        	
        	
        	if(res != null){
        		
        		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
        		
        		
        		long currentTimeL = System.currentTimeMillis();
            	
        		String currentTime = df.format(currentTimeL);
        		
        		System.out.println(currentTime);
        		
        		System.out.println("total bet:" + res);
        		
        		
        		
        		
        		if(res.contains("eventsDetails")){


        			
        			return parseBets(res);
        		}else{
        			System.out.println(res);
        		}
        			
        		
        	}
    		
    	}catch(Exception e){
    		
    	}
    	
    	return false;

    }
    
    
    public  boolean getTotalPS38Bet(){
    	
    	try{
        	

        	
        	String betUri = ADDRESS + "/partner-webservice/total-bet?betType=HDP&sportId=29&leagueId=0&eventId=0&groupName=";
        	
        	//String betUri = ADDRESS + "/partner-webservice/total-bet?betType=HDP&sportId=0&leagueId=0&eventId=0&groupName=";
        	
        	String res = doGet(betUri, "", "http://www.ps38ag.com/partner-ui/page/zh-cn/account-balance");
        	
        	if(res == null){
        		res = doGet(betUri, "", "http://www.ps38ag.com/partner-ui/page/zh-cn/account-balance");
        		
        	}
        	
        	
        	
        	if(res != null){
        		
        		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
        		
        		
        		long currentTimeL = System.currentTimeMillis();
            	
        		String currentTime = df.format(currentTimeL);
        		
        		System.out.println(currentTime);
        		
        		System.out.println("total bet:" + res);
        		
        		
        		
        		
        		if(res.contains("eventsDetails")){
        			
        			return parseBets(res);
        		}else{
        			System.out.println(res);
        		}
        			
        		
        	}
    		
    	}catch(Exception e){
    		
    	}
    	
    	return false;

    }
    
    
    
    
    
    
    public  boolean parseBets(String jsonRes){
    	
    	boolean res = false;
    	
    	boolean find = false;
    	
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
		
		
		long currentTimeL = System.currentTimeMillis();
    	
		String currentTime = df.format(currentTimeL);
		
		
		Calendar calCurrent = Calendar.getInstance();  
		
		Calendar calDeadline = Calendar.getInstance();  
		
		calDeadline.set(Calendar.HOUR_OF_DAY, 13);
		
		int hour = calCurrent.get(Calendar.HOUR_OF_DAY);  
		
		if(hour >= 13){
			calDeadline.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		long deadlineTime = calDeadline.getTimeInMillis();

		
		
		
		
    	List<String> parsedEvent = new ArrayList<String>();
    	
    	try{
    		JSONObject sports = new JSONObject(jsonRes);
    		
    		JSONArray eventsDetailsJarry = sports.getJSONArray("eventsDetails");
    		
    		JSONObject event = null;
    		

    		
    		Double playerStake = 0.0;
    		
    		for(int i = 0; i < eventsDetailsJarry.length(); i++){  		

    			
    			event = eventsDetailsJarry.getJSONObject(i);    			
    			String leagueName = event.getString("leagueName");
    			
/*    			if(!isInShowLeagueName(leagueName))
    				continue;*/
    			
    			long time = event.getLong("eventDate");
    			//over events
    			long twoHours = 2*60*60*1000 + 2*60*1000;
    			
    			long twelveHours = 24*60*60*1000;
    			
    			if(currentTimeL - time > twoHours){
    				continue;
    			}
    			
    			if(time >= deadlineTime){
    				continue;
    			}
    			
    			
    			
    			String eventName = event.getString("eventName");    			
    			String period = event.getString("period");    			
    			String description = event.getString("description");
    			long eventId = event.getLong("eventId");
    			
    			boolean inPlay = event.getBoolean("inPlay");
    			
    			playerStake = Math.abs(event.getDouble("playerStake"));
    			
    		
    			
    			String homeover = "";
    			
    			
    			if(description.contains("HOME") || description.contains("AWAY")){
    				homeover = "HOMEAWAY";
    			}else{
    				homeover = "OVERUNDER";
    			}
    			
    			String parsedeventstr = Long.toString(eventId) + period + homeover;
    			
    			if(parsedEvent.contains(parsedeventstr))
    				continue;
    			
    			parsedEvent.add(parsedeventstr);
    			
    			int index = 0;
    			
    			boolean eventIDexist = false;
    			
    			for(index = 0; index < eventDetailsVec.size(); index++){
    				if(eventDetailsVec.elementAt(index)[TYPEINDEX.EVENTID.ordinal()].equals(Long.toString(eventId))){
    					eventIDexist = true;
    					break;
    				}
    			}
    			

    			
    			
    			if(eventIDexist == false){
    				
        			String[] row = new String[8];

        			
        			row[TYPEINDEX.EVENTID.ordinal()] = Long.toString(eventId);

        			
        			row[TYPEINDEX.LEAGUENAME.ordinal()] = leagueName;
        			
        			String currentTimeArray[] = currentTime.split(" ");
        			String eventTimeArray[] = df.format(time).split(" ");
        			
/*        			String timeStr = "";
        			
        			if(currentTimeArray[0].contains(eventTimeArray[0])){
        				timeStr = eventTimeArray[1];
        			}else{
        				timeStr = df.format(time);
        			}*/
        			
        			
        			row[TYPEINDEX.TIME.ordinal()] = Long.toString(time);
        			
        			
        			if(inPlay== true){
        				row[TYPEINDEX.EVENTNAMNE.ordinal()] = "【滚动盘】" + eventName;
        			}
        			else{
        				row[TYPEINDEX.EVENTNAMNE.ordinal()] = eventName;
        			}
        			
        			
        			
        			row[TYPEINDEX.PERIOD0HOME.ordinal()] = "0";
        			row[TYPEINDEX.PERIOD0OVER.ordinal()] = "0";
        			row[TYPEINDEX.PERIOD1HOME.ordinal()] = "0";
        			row[TYPEINDEX.PERIOD1OVER.ordinal()] = "0";
    				eventDetailsVec.add(row);
    			}



    			
    			
    			

    			
    			if(parsedeventstr.contains("HOMEAWAY") ){

    				for(int j = i + 1; j < eventsDetailsJarry.length(); j++){
    					
    					JSONObject event1 = eventsDetailsJarry.getJSONObject(j);    
    					
    					String leagueName1 = event1.getString("leagueName");
    					
    	    			if(!isInShowLeagueName(leagueName1))
    	    				continue;
    					
    	    			String period1 = event1.getString("period");    			
    	    			String description1 = event1.getString("description");
    	    			long eventId1 = event1.getLong("eventId");
    	    			Double playerStake1 = Math.abs(event1.getDouble("playerStake"));
    	    			

    	    			
    	    			if((eventId1 == eventId) && (period1.equals(period)) && homeover.contains(description1)){
    	    				if(description1.contains("HOME")){
    	    					playerStake = playerStake1 - playerStake;
    	    				}else{
    	    					playerStake = playerStake - playerStake1;
    	    				}
    	    				find = true;
    	    				break;
    	    			}
    				}
    				
    				
    				
    				if(find == false){
	    				if(description.contains("HOME")){
	    					playerStake = playerStake;
	    				}else{
	    					playerStake = 0 - playerStake;
	    				}
    				}
    			
    				
    				if(period.contains("0")){
    					Double d1= Double.parseDouble(eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()]) + playerStake;    					
    					eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0HOME.ordinal()] = String.format("%.0f", d1);    					
    				}
    				else{
    					Double d1= Double.parseDouble(eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD1HOME.ordinal()]) + playerStake;    					
    					eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD1HOME.ordinal()] = String.format("%.0f", d1);
    				}
    				
    				find = false;

    				
    			}else{
    				
    				for(int j = i + 1; j < eventsDetailsJarry.length(); j++){
    					
    					JSONObject event1 = eventsDetailsJarry.getJSONObject(j);    
    					
    					String leagueName1 = event1.getString("leagueName");
    					
    	    			if(!isInShowLeagueName(leagueName1))
    	    				continue;
    	    			
    	    			String period1 = event1.getString("period");    			
    	    			String description1 = event1.getString("description");
    	    			Long eventId1 = event1.getLong("eventId"); 
    	    			Double playerStake1 = Math.abs(event1.getDouble("playerStake"));
    	    			
    	    			
    	    			
    	    			if(eventId1 == eventId && period1.equals(period) && homeover.contains(description1)){
    	    				if(description1.contains("OVER")){
    	    					playerStake = playerStake1 - playerStake;
    	    				}else{
    	    					playerStake = playerStake - playerStake1;
    	    				}
    	    				find = true;
    	    				break;
    	    			}
    				}
    				
    				if(find == false){
	    				if(description.contains("OVER")){
	    					playerStake = playerStake;
	    				}else{
	    					playerStake = 0 - playerStake;
	    				}
    				}
    				
    				if(period.contains("0")){
    					Double d1= Double.parseDouble(eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()]) + playerStake;
    					
    					eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD0OVER.ordinal()] = String.format("%.0f", d1);
    				}
    				else{
    					Double d1= Double.parseDouble(eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD1OVER.ordinal()]) + playerStake;
    					
    					eventDetailsVec.elementAt(index)[TYPEINDEX.PERIOD1OVER.ordinal()] = String.format("%.0f", d1);
    				}
    				
    				find = false;
    				

    				
    			}
    			
    			
    		}
    		
    		for(int k = 0; k<eventDetailsVec.size(); k++ ){
    			
    			String[] outRow = eventDetailsVec.elementAt(k);
    			
/*    			System.out.println(outRow[0] + "," +outRow[1] + outRow[2] + "," +outRow[3] + "," +outRow[4] + "," +outRow[5] + "," +
    					outRow[6] + "," + outRow[7]);*/
    		}
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	

    	
    	return true;
    	
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
    
    
    
    
    public  String doGet(String url, String cookies, String referUrl) {
    	
        try {  
            // ����httpget.    
            HttpGet httpget = new HttpGet(url);
            
            if(strCookies != "") {
            	httpget.addHeader("Cookie",strCookies);
            	//System.out.println("set cookies");
            }
            httpget.addHeader("Accept-Encoding","gzip, deflate, sdch");
            httpget.addHeader("Accept-Language","zh-CN,zh;q=0.8");
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
            
            setCookie(response);
            
            String statusLine = response.getStatusLine().toString();   
            if(statusLine.indexOf("200 OK") == -1) {
         	   //System.out.println(statusLine); 
            }
            
            try{
            	
            	//System.out.println("����cookie:" + strCookies);
            	
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
             	   return response.getFirstHeader("Location").getValue();
                }
                HttpEntity entity = response.getEntity(); 
                
                String res = EntityUtils.toString(entity, "UTF-8");
                
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
    		calcRequestAveTime(time2 - time1);
    		
    	}catch(Exception e){
    		
    		time2 = System.currentTimeMillis();
    		calcRequestAveTime(time2 - time1);
    		
    		throw e;
    	}
    	

    	
    	return response;
    	
    }
    
    

    
    
    public  String doPost(String url,String json, String cookies, String refer) {
        return doPost(url, json,"UTF-8", cookies, refer);
    }
    
    
    public  String doPost(String url,String json,String charset, String cookies, String refer) {


        // ����httppost   
       	
       	try {
       	
           HttpPost httppost = new HttpPost(url); 
           //httppost.addHeader("Cookie", cookies);
           //httppost.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
           //httppost.addHeader("x-requested-with","XMLHttpRequest");
           

           
           httppost.addHeader("Accept-Language","zh-CN,zh;q=0.8");
           httppost.addHeader("Accept","application/json, text/javascript, */*; q=0.01");
           httppost.addHeader("Accept-Encoding","gzip, deflate");
           httppost.addHeader("Connection","keep-alive");
           httppost.addHeader("Cache-Control","max-age=0");
           httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");    
           
           if(refer != "")
           {
           	httppost.addHeader("Referer",refer);
           	
           }
           
           if(strCookies != "") {
             	httppost.addHeader("Cookie",strCookies);
             }
           
	           StringEntity s = new StringEntity(json);
	           //s.setContentEncoding("UTF-8");
	           s.setContentType("application/json");//发送json数据需要设置contentType
	           httppost.setEntity(s);
              
               
               
               
               //System.out.println("executing request " + httppost.getURI()); 
               
               RequestConfig localRequestConfig = RequestConfig.copy(requestConfig).setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();

               httppost.setConfig(localRequestConfig);
               
               CloseableHttpResponse response = execute(httppost);
               try {
                   // ��ӡ��Ӧ״̬    
               	
               	
               	//to do remove hard code
               //	if(url.contains("_index/c.php"))
            	
               if(url.contains("code/validate"))
               {
            	   //System.out.println("kkkkk");
               }
            	   
            	   
               setCookie(response);

               	
               	//System.out.println("����cookie:" + strCookies);
               	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
               		String location = response.getFirstHeader("Location").getValue();
               		//System.out.println(response.getStatusLine());
               		
               		
               		
               		if(location != null) {
               			return location;
               		}
               	}
               	
               	
                   HttpEntity entity = response.getEntity(); 
                   
                   String res = EntityUtils.toString(entity);
                   
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
    
    
    
    
    
    
	public  String setCookie(CloseableHttpResponse httpResponse)
	{
		
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
			if((strCookies.contains("JSESSIONID") && c.contains("JSESSIONID")) || c.indexOf("Path=") != -1 ||c.indexOf("path=") != -1 || c.indexOf("expires=") != -1||c.indexOf("Expires=") != -1 || c.indexOf("domain=") != -1 ||c.indexOf("Domain=") != -1 || c.indexOf("HttpOnly") != -1)
				continue;
			strCookies += c;
			strCookies += ";";
		}
		//System.out.println("----setCookieStore success");
		
		
		
		if(!strCookies.contains("language=zh_CN")){
			strCookies += "language=zh_CN;";
		}

		return strCookies;
	}
    
	//todo
	public  boolean isInFreetime(){
		return false;
	}
	
	

    
    

    
    
    public synchronized  void addFailsTimes(){
    	long currentTime = System.currentTimeMillis();
    	
    	if(((currentTime - lastFailtime) < 40*1000) || (lastFailtime == 0)){
    		requestFailTimes++;
    		
    		lastFailtime = currentTime;
    		
    		if(requestFailTimes >= 4){
    			setIsNeedRelogin(true);
    			requestFailTimes = 0;
    		}
    		
    	}
    	else{
    		requestFailTimes = 1;
    		lastFailtime = currentTime;
    	}
    }
    
    
    public synchronized  void setIsNeedRelogin(boolean flag){
    	isNeedRelogin = flag;
    }
    
    public synchronized  boolean getIsNeedRelogin(){
    	return isNeedRelogin;
    }
    
    
    
    public synchronized  void calcRequestAveTime(long requestTime){
        
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
            		setisNeedChangeLine(true);
            		lastChangeLineTime = currentTime;
            	}

        	}

    	}

    		
    	
    }
    
    
    
    
    public  String getPicNum(String picUri) {
    	try {
	   	    HttpGet httpget = new HttpGet(picUri);
	   	    
	   	    if(strCookies != ""){
            	httpget.addHeader("Cookie",strCookies);
            	//System.out.println("set cookies");
	   	    }
	   	    
	        httpget.addHeader("Connection","keep-alive");
	
	        httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 "
	        					+ "(KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
	       // System.out.println("executing request " + httpget.getURI()); 
       
	        // 执行get请求.    
	        
	        RequestConfig localRequestConfig = RequestConfig.copy(requestConfig).setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();
            httpget.setConfig(localRequestConfig);
	        
        
	        CloseableHttpResponse response = execute(httpget); 
       	 try {
       		    setCookie(response);
                // 打印响应状态    
                //System.out.println(response.getStatusLine()); 
                //System.out.println("------------------------------------");
                File storeFile = new File("hyyzm.png");   //图片保存到当前位置
                pngnumber= pngnumber + 1;
                FileOutputStream output = new FileOutputStream(storeFile);  
                //得到网络资源的字节数组,并写入文件  
                byte [] a = EntityUtils.toByteArray(response.getEntity());
                output.write(a);  
                output.close();  
                
/*                InputStream ins = null;
        		 String[] cmd = new String[]{"C:\\Program Files (x86)\\Tesseract-OCR" + "\\tesseract", "hyyzm.png", "result", "-l", "eng"};

        		 Process process = Runtime.getRuntime().exec(cmd);
        		 // cmd 的信息
        		 ins = process.getInputStream();
        		 BufferedReader reader = new BufferedReader(new InputStreamReader(ins));

        		 String line = null;
        	  	 while ((line = reader.readLine()) != null) {
        	  		 System.out.println(line);
        		 }
        			
        		 int exitValue = process.waitFor();
        		 System.out.println("返回值：" + exitValue);
        		 process.getOutputStream().close();
        		 File file = new File("result.txt");
        		 reader.close();
                reader = new BufferedReader(new FileReader(file));
                 // 一次读入一行，直到读入null为文件结束
                String rmNum;
                rmNum = reader.readLine();
                reader.close();*/
                
                //自动输入验证码
                
                Thread.currentThread().sleep(1000);
                
                //System.out.println("请输入验证码:");
                
                File file = new File("result.txt");
                
                BufferedReader reader = new BufferedReader(new FileReader(file));
                // 一次读入一行，直到读入null为文件结束
               String rmNum;
               rmNum = reader.readLine();
               reader.close();
            // 自动输入验证码   
               
               
               
               //手动输入验证码
               
                
/*                System.out.println("请输入验证码:");
                
        		Scanner sc = new Scanner(System.in);
        		
        		String rmNum = sc.next();*/
        		
        		//System.out.println(rmNum);
                
                
                return rmNum;
       	 }
       	 finally{
       		httpget.releaseConnection();
       		 response.close(); 
       	 }
        } catch (ClientProtocolException e) {  
            e.printStackTrace(); 
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace(); 
        } catch (IOException e) {  
            e.printStackTrace(); 
        } catch (Exception e) {
				e.printStackTrace();
		 }
        
   	return null;
   }
    
    
	public  boolean isNum(String str){
        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
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
	
	
	public static void saveEvents(){
		
		try{
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			for(int i = 0; i < eventDetailsVec.size(); i++){
				long eventTime = Long.parseLong(eventDetailsVec.elementAt(i)[TYPEINDEX.TIME.ordinal()]);
				
				String eventName = eventDetailsVec.elementAt(i)[TYPEINDEX.EVENTNAMNE.ordinal()];
				

				
				long currentTime = System.currentTimeMillis();
				
				long passMinutes = 108*60*1000;
				
				if(eventName.contains("滚动盘")){
					if(currentTime - eventTime > passMinutes){
						
						String eventTimestr = df.format(eventTime);
						
						String[] saveItem = (String[])eventDetailsVec.elementAt(i).clone();
						
						saveItem[TYPEINDEX.TIME.ordinal()] = eventTimestr;
						
						pDataManager.saveTofile(saveItem);
					}
				}else if(eventName.contains("角球")){
					return;
				}else{
					if(currentTime - eventTime > 0){
						
						String eventTimestr = df.format(eventTime);
						
						String[] saveItem = (String[])eventDetailsVec.elementAt(i).clone();
						
						saveItem[TYPEINDEX.TIME.ordinal()] = eventTimestr;
						
						pDataManager.saveTofile(saveItem);
					}
				}			
				

				
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}
	
	public static void showpDataWnd(){
		pDataWindow.setVisible(true);
	}
    
	
	public static void updatepDataDetails(){
		pDataManager.updatepEventsDetails();
	}
    
}
