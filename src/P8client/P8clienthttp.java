package P8client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import P8.EventsDetailsWindow;
import P8.PreviousDataManager;
import P8.PreviousDataWindow;

public class P8clienthttp {
	
	//debug
	public boolean printEvents = false;
	
	
	
     CloseableHttpClient httpclient = null;
     RequestConfig requestConfig = null;
     HttpClientContext clientContext = null;
    
    
     static Vector<String> showLeagueName = new Vector<String>();
    
    public  int pngnumber = 101;
    
    boolean bLogin = false;
    
    public static double flagNumber = 100000;
    
    public static int P8p0hSendNumber = 1000000;
    public static int P8p0oSendNumber = 1000000;
    
    public static int p8p0oInplaySendNumber = 600000;
    
    public static Vector<String> inplaySendAlraedy = new Vector<String>();
    
    
    static PreviousDataWindow pDataWindow = new PreviousDataWindow();
    
    
    
    
    
     static EventsDetailsWindow eventsDetailsDataWindow = new EventsDetailsWindow();
    

    public static   Vector<String[]> eventDetailsVec = new Vector<String[]>();
    
    
    static Map<String, Vector<Integer>> mailRecords = new HashMap<String, Vector<Integer>>();  
    
    
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
	
	
	 
	 
	 
		//0代表网址连接不上，1代表成功，2代表Invalid的用户
		public  int login(){
			
			int res = 0;
			
			
			
			
			strCookies = "";
			
			
			
			
			
			String linePageUri = ADDRESS + "zh-cn";
			
			int oldTimeout = defaultTimeout;
			
			try{
				

				String linePage = doGet(linePageUri, "", "");
		    	
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();		  
	            //params.add(new BasicNameValuePair("login_layer", "corp"));
	            params.add(new BasicNameValuePair("loginId", ACCOUNT));
	            params.add(new BasicNameValuePair("password", PWD));
	            
	            System.out.println(params.toString());

	            linePage = doPost("https://ropinwev.pinbet88.com/member-service/v1/login?locale=zh_CN", params, "");
		    	
	            
	            if(linePage.equals("1")){
	            	return 1;
	            }
		    	

				
			}catch(Exception e){
				defaultTimeout = oldTimeout;
				
				e.printStackTrace();
			}

			
			defaultTimeout = oldTimeout;
			
			return res;
		}
	 
	 

		public void getTotalbet(){
			
			String res = null;
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			SimpleDateFormat dfmin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			
			
			String day = df.format(System.currentTimeMillis());
			
			
			System.out.println(dfmin.format(1507692017451L));
			
			System.out.println(dfmin.format(1507779346323L));
			
			
			String testuri = "https://www.pinbet88.com/zh-cn/";
			
			String testres = doGet(testuri, "", "");
			
			testuri = "https://www.pinbet88.com/zh-cn/sports/soccer";
			
			testres = doGet(testuri, "", "https://www.pinbet88.com/zh-cn/");
			
			testuri = "https://www.pinbet88.com/sports-service/sv/cmk=2&sp=29&ot=1&btg=1&o=1&lg=&ev=&d=2017-10-12&l=3&v=0&more=false&c=CN&tm=0&g=QQ%3D%3D&pa=0&_=1507776324529&locale=zh_CN";
			
			//testuri = "https://www.pinbet88.com/sports-service/sv/odds/events?mk=2&sp=29&ot=1&btg=1&o=1&lg=&ev=&d=2017-10-12&l=3&v=0&more=false&c=CN&tm=0&g=QQ%3D%3D&pa=0&_=" + Long.toString(System.currentTimeMillis()- 1000000) + "&locale=zh_CN";
			
			System.out.println(testuri);
			
			testres = doGet(testuri, "", "https://www.pinbet88.com/zh-cn/sports/soccer");
			
			
			
			
			
			String uri = "https://ropinwev.pinbet88.com/sports-service/sv/odds/events?mk=0&sp=29&ot=1&btg=1&o=1&lg=&ev=&d=2017-10-12&l=3&v=0&more=false&c=CN&tm=0&g=QQ%3D%3D&pa=0&_=1507772583798&locale=zh_CN";
			
			res = doGet(uri, "", "https://ropinwev.pinbet88.com/zh-cn/sports");
			
			
			uri = "https://ropinwev.pinbet88.com/sports-service/sv/odds/events?mk=0&sp=29&ot=4&btg=1&o=1&lg=&ev=&d=2017-10-11&l=3&v=0&more=false&c=CN&tm=0&g=QQ%3D%3D&pa=0&_=1507691338294&locale=zh_CN";
			res = doGet(uri, "", "");
			
			System.out.println(res);
			
			return;
		}
		
		
	
		public  void setLoginParams(String address, String account, String pwd){
			ADDRESS = address;
			ACCOUNT = account;
			PWD = pwd;

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
            if(url.endsWith("odds/events?")){
            	
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
            		//setisNeedChangeLine(true);
            		lastChangeLineTime = currentTime;
            	}

        	}

    	}

    		
    	
    }
}
