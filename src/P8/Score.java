package P8;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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
	
    private ReadWriteLock lockeFinalScoreDetails = new ReentrantReadWriteLock();
    
 
    
    public Vector<String[]> finalScoreDetailsVec = new Vector<String[]>();
	
	
	
	public String rqpankoustr  = "平手,平/半,半球,半/一,一球,一/球半,球半,球半/两,两球,两/两球半,两球半,两球半/三,三球,三/三球半,三球半,三球半/四球,四球,四/四球半,四球半,四球半/五,五球,五/五球半,五球半,五球半/六,六球,六/六球半,六球半,六球半/七,七球,七/七球半,七球半,七球半/八,八球,八/八球半,八球半,八球半/九,九球,九/九球半,九球半,九球半/十,十球";
	public String[] dxqpankou = {"0", "0/0.5", "0.5", "0.5/1", "1", "1/1.5", "1.5", "1.5/2", "2", "2/2.5", "2.5", "2.5/3", "3", "3/3.5", "3.5", "3.5/4", "4", "4/4.5", "4.5", "4.5/5", "5", "5/5.5", "5.5", "5.5/6", "6", "6/6.5", "6.5", "6.5/7", "7", "7/7.5", "7.5", "7.5/8", "8", "8/8.5", "8.5", "8.5/9", "9", "9/9.5", "9.5", "9.5/10", "10", "10/10.5", "10.5", "10.5/11", "11", "11/11.5", "11.5", "11.5/12", "12", "12/12.5", "12.5", "12.5/13", "13", "13/13.5", "13.5", "13.5/14", "14" };
	public String[] rqpankou = rqpankoustr.split(",");
	
	public ScoreDetailsWindow scoreDetailsWnd = new ScoreDetailsWindow();
	
	
	
	
	
	
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
		scoreDetailsWnd.updateEventsDetails(scoresDetailsVec);
	}
	
	
	public void showscoredetailswnd(){
		scoreDetailsWnd.setVisible(true);
	}
	
	
	
	public void clearScoredetails(){
		if(scoresDetailsVec.size() != 0){
			scoresDetailsVec.clear();
		}
	}
	
	
	
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
            
           // setCookie(response);
            
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
                
                String res = "";//
                
                if(url.contains("alias2")){
                	res = EntityUtils.toString(entity, "UTF-8");
                }else{
                	res = new  String(EntityUtils.toString(entity).getBytes("ISO-8859-1"), "gbk");
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
