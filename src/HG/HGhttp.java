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


public class HGhttp {
	
	
	CloseableHttpClient httpclient = null;
	RequestConfig requestConfig = null;
	HttpClientContext clientContext = null;
	
	String blackbox = "3034303045367442697642757434626a4b39474665634f51692f74773253797857553350444a4d75354f634c477251577a4e4c73364b32536d58326540435453794776434f3152314c4e6359786e4d4d326b51714d5879486940624861383343445a42365942756c54695563533341765a2f787064513739767a504d53654e4740364e406378506235396630494b68774152547048657831476a626276594957653147787078323835714b5133616f7051732f45477a6d5a4c3277747469302f6f53676f576f6768434e425872494e48706c76386e6b3873354774474d75535a78324b544033445a4c4c465a5463384d6b79376b357773617442624d30757a6f725a4b5a665a37344a4e4c49613849375648557331786a4763777a6152436f786649654c774b72304d66406f675761726d4e306738626952396838466b634133554a6971614665536e2f536865556e617040344c77546459546733754b4045744e70304578496f51524833656f5a6a3476483662626b40466675657935357244392f42324c667777446461485661676d42564b54536c7770524d494d6d784c6e534140577167784277636444476f4b6a414d646476466963647657556a397274723456506b72626f4756303361394b5a34376e594a4330674d784c58727736683633374b6a4e5873674d456768473244486d33674a62426437493847547142465254326d6d45634f534d2f3955524b43754a6f7967444b6c433749407653337a4f3155576a6d553031654b52517752317341716b50794c3552525a6b467161555a656762404b61655041646a30644331444f6b5a3979625278487866595633576541305559734d5a6d56593566534f776a7a3463526149587031695633346f716e3973667536383238523947516d366b484c646e7946495177656456767534654264415a71676a49547251566379716d6a496b713571667453354852732f314a7867426c665957702f307a733469384d676f673157744373306b6e744d774441524875567a6466784c796c5942696273455642574f38585149717061394e7650397a36767436542f4134726d4c704f4a776853527645725437546e744563566d57316b4d696f774556777569466e6e424e755371575946486b39566f3179686343374336364e4455576e4f5a594e376345346e5a336d65566e6e6236647844696f6b73656a677448487171453238687635743261736264384e515a40693861466a70326275754b36394f793845526c3261636e4f36454346657438576e54524e6f366d40714e7941424857776673515838436a4845534b486962623239353970323250694751336757474f6b6e30345063444a4e39536d3839494b6d565638543967374135406f47324b4136735171725249436e5842664d406e635a7a684971584c576e454d437930733368524c6e3038546839466c4e484450624b525865793036635934613765754671694d55677a716277706d57613849775059544b76476368796540463940564e486c637a3253316a706361626447464c6b664234725a5577674a54455664474a6c784277436338616362634b714175576637676f757a42504a61454d437930733368524c6c583375486e542f6d4d717a456e44566a363574344a43684230784b3172797430774433314645664e4d464b446c3146735a59386a774b485a56725a4d30366b75304b4c3558483638326c6751792f6d3279765765444c65374473794a384262544569684245666436686d506938667074755434564035374c6e6d7350333848614a404b6d414d5a323159557a5a65792f725a4e456363444438373471352f4c644c72545356716c6b47384265394f5a6633664e562f78386455516375397a6f37777449624650456c61615a334f386b31466b66624a794a6b6d444b3049307230506e77654863677a2f626831454178787a7959724a563751493253593048784c5677366c40744f4e47727650664270386a395a6763584b504466396630304935746e38707062794e59524f614650536d7133656f7a77565935626a5a5579644b4d75504171767a642f7641354037536b5a43554b6c404e58554e56303254395436416c474a5059355a6f4557797a5563517554655858467565724c54433433466b514c49665843375a304a5563444d474d6873634861504c6c4c57435a30696c663975504f4c55794e426d7930734f4e41524e6c7352563540533544796662447330594d6539546b646842537432574d596540526770614b32366b3766474058315352476c6946796b61344131394c415a4d735a4c54456b594a454d55324b5a32706f4f514849793263404b776f66366f497759767639654f76737976544255735a6e317a4077656b506a6a306e456f335a6a6f50465134773262645874415037704c59427231694738382f5658743051304178633431467361704c40786567786d6b37476854746544506443364c4a414a56507a525748677348504c7135655262504675462f6c386635313038743765683836444863683965464040456c7570397548476c483370646b4a5446665133786730764f307775657a767831366571404e4e566250647046724046714357316a6761516a5834693266753268704763665157484c396a627745576f5a56596a653863534261704f315a7934657132704032506150714d71794956724c554a483061694f4677404b78656b77516f712f476d596976365a33754e74324f416b39333936544d30585259484033717956415540616472323743614e38456e6144776f416950546233444d706b5161375a6351364d3077595937783961637531353771524b78744f32406d32353035774d547679506b726e787350776c3856582f7569774936527249556b674350394d41476241516a4e6b55756e307451306472396565455461444c74454b5479435574544d403237364778526648774073764079415448754d79523846564b514339366b704e4b7973544e364432687a655a67453779376a67515162735863442f41657732484c736f4e62337137767755626c6f4b73576c686652354d36413078666763545039336c524a40494c764150385476647652585a4e52454a5544627652383d3b30343030324038576676726a2f44626a4b39474665634f5169326e7538366c54486450327266367778652f7742334d3767554a6b3246514d364436456e6633557445497477306579632f4c4c4f5346614a4c4c316954516c3370527646434d53626c357869406a2f4573423637416f71617550336b43414d4f47495041726a447265544354343352356a406f387734715845636977444d77477531317270494d46464f4f39516d49766b7761713936585a4150635a4941426d6d4a30696870707a32336562383144545a402f4f376e72786f76774b377747664d2f486a41635552534437585a4a532f314b4c6a31737a446e497063356461595774474d75535a78324b546165377a71564d64302f61742f724446372f4148637a7542516d545956417a6f506f53642f6453305169334452374a7a3873733549566f6b7376574a4e435865774b72304d66406f675761726d4e306738626952396838466b634133554a6971614665536e2f536865556b67724248357876796b6d55504b7463537a544938574963414164344b3265416738582f70645958715956325a7856694a46544e474077357a614b2f6272744978436d734f39716a49486c4650635752402f686e4255667055676b37626e47396273363947664a43553330596d636545715833406477697a69494a33626c4f475868706b6a6346773043365032506d3278643650335557406d477463626d6140534f6a386a3477374b7256417372386a7a633279764b5478776c43767a4b3141737a5367506d7662504f6d444d6b664256536b417665324049584a6b6d616739706d652f684a7a4877754340767231704870677366466769464b4f73586353494c6a46696a494e5339404d5743744a706b6d784b4a63367a416d38704047325052674f3936324762534c46566f452f697365424b577473304a7674366f512f5a4c4f4b644a707572734a49383468774e37654b346f6a704b4c36704b5033307071572f406535644264416136334e45334a49652f3365596552534c4741394571377877554a7664656b72517465625263473240545a4662617a5238376665586a37447a697241556177664e65434953617a7339504438332f73654b634f6c7658724778684b6175777145574c78554e307359463872303866387543634330784f4463624043714d4b453676525a4450503559483069547750494f357635326257424d7950706a7a5975755640566854324a5178765464667159416e646c4d653762645a6f5578796b4f3438716b316935544048345a7a53647533445a3244306b464c4d7250724d56764a6c764c45387841792f74686e326247307370665a424064784b7a593063316e4d4168447440625636777049506d3078666b615567327374415232375a646940766a70406f5271746b574d49716b74593658476d3352685268354e777531783650494c4540545a53536f4f39316752674e405a384841466847467a642f6d36736e56716932584a7777514576556a504a56365443456a6e403040396439415344346f744657774d31544f637a376769564641377a7049505134684c7373666e3357534474443359555971353637494b6838636a316f65597056384c73666b34484d386d76524952467235505274466f484d76706e67696f70646a756c6b5562364857464c42364d35666c32456f5a697579375865747a684856686768504c4e48432f773964656b357238574839724863764537724265334f376d7a66592f4141617a454f43626e527a705839537754626b716c6d425235505761402f497076506771755451486a7937483558684c484348414759334a306a704c71567253567a7349314c42364d35666c32456f4d45416f45697445747446556f3144526b557450474244744062563677704950506b65573934796134346c3456536862594045564244644d4c365a6856357945666f62694f68634f4e4a504e6e426738396b3536724675684f5540306f3230375872406a7146563642354432476275703038766f7239427978386f506a702f7842444c4062624b395a344d7437734f7a496e7746744d534b4545523933714759404c78406d3235506858376e73756561772f6677646f6e34715941786e625668544e6c374c40746b305278774d507a7669726e38743075744e4a577157516277463730356c2f643831582f48783152427937334f6a76433068735538535670704d6b4b5957554f55494f4f7a6942426b56625a714e4362366330672f693653356f4e6431556277686c446249587736405852504b654630754c634e6c774173516735714c5630396d336c66435968474b5a75474377586851366742546e766c5933616c41594f717045447253516a4c493551785a3263736a7345494d39714d42636e78406e7a506f3377564e44514634774f2f35706e677967516c374a3635306a49564b6c714f38312f424c4256667a474350394c6b38755264714539416f696273584f7553693251305436597854454676724a69337535586d787a5754623856715774687a65797148516a5954536a794465364377474351614a6b58553747356f7277684050754f3138616568452f4032413072747678484e5a334e6b7268743236683441344d565a6166342f5163334465513749416f767946474836785a33487a59543061454f343636576454475878403757637556514840724f6e326d2f6f65624f446c41487767786e435864496c4742387452754d7437776c33695634416e4f556c7335616432522f7a7979475638764f52554b774443304d35734258463930455655506c785058516832497065777269334e2f6f5852386468557a6f2f775834514c78687a56715a6437694d30785836792f737666466164717542394e4f3065514f7564464d6b354d5665635761326d773847757852325453425744404b7637656f706a4b386f30362f7a424874394a4b4f764944794979456f31714b6a434d4f6773365a304a493373706c5a4b6c514133496863397249574a6471366a69595833776b624771494732306b63526b6a524b68427073757442374d7268633471303747784259595461654d386c714d4d6e4f7333376e52656d754633636f41726364386440375745616d644741766f6f7a4f48374c48595971387840637352336a7a5a375747464d51714a35797076466f50594866457673324343486b6a57756b5840596c3658353162702f445335724b40644732316d64357545524a6e6a72704358787740472f65722f4354655141586a754a44366c72454f4b6c72647a654e57563267513d";
	 {
	    requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
	    requestConfig = RequestConfig.copy(requestConfig).setRedirectsEnabled(false).build();//禁止重定向 ， 以便获取cookieb18
	    //requestConfig = RequestConfig.copy(requestConfig).setConnectTimeout(autoBet.timeOut).setConnectionRequestTimeout(autoBet.timeOut).setSocketTimeout(autoBet.timeOut).build();//设置超时
	        httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
	   }
	
	 
	 
	 
    static HGPreviousDataWindow pDataWindow = new HGPreviousDataWindow();
    
    static HGPreviousDataManager pDataManager = new HGPreviousDataManager(pDataWindow);
	 
	 
	
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
//		showLeagueName.add("德国 - 德甲");
//		showLeagueName.add("欧足联 - 冠军联赛");
//		showLeagueName.add("西班牙 - 西甲");
//		showLeagueName.add("意大利 - 甲级联赛");
//		showLeagueName.add("法国 - 甲级联赛");
//		showLeagueName.add("英格兰 - 超级联赛");
//		showLeagueName.add("欧足联 - 欧罗巴联赛");
		
		showLeagueName.add("欧洲国家联赛");
		showLeagueName.add("西班牙甲组联赛");
		showLeagueName.add("法国甲组联赛");
		showLeagueName.add("英格兰超级联赛");
		showLeagueName.add("意大利甲组联赛");
		showLeagueName.add("德国甲组联赛");
		showLeagueName.add("欧洲冠军联赛");
		showLeagueName.add("欧洲联赛");
		//showLeagueName.add("中北美洲及加勒比海冠军杯");
		
		
		pDataManager.init();
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
        	
        	//System.out.println(res);
        	
        	if(res.contains("oldSite")){
        		//res = doGet(loginLine + "new_index.php?type_chk=&langx=", "", "");
        		
        		if(res.contains("Login ID")){
        			
//        			p: login_chk
//        			ver: version-03-06
//        			login_layer: co
//        			username: kk6320
//        			pwd: QQww889900
//        			pwd_safe: QQww2233
//        			uid: 
//        			langx: zh-cn
//        			auto: FCGCCD
    		        List<NameValuePair> params = new ArrayList<NameValuePair>();		  
    		        params.add(new BasicNameValuePair("p", "login_chk"));
    		        params.add(new BasicNameValuePair("username", ACCOUNT));
    		        params.add(new BasicNameValuePair("ver", "version-03-06"));
    		        params.add(new BasicNameValuePair("pwd", PWD));
    		        params.add(new BasicNameValuePair("pwd_safe", SECURITYCODE));
    		        params.add(new BasicNameValuePair("login_layer", "co"));
    		        params.add(new BasicNameValuePair("uid", ""));
    		        params.add(new BasicNameValuePair("langx", "zh-cn"));
    		        params.add(new BasicNameValuePair("auto", "FCGCCD"));
    		        params.add(new BasicNameValuePair("blackbox", ""));
    		        
    		        System.out.println("has login id");
    		        
    		        res = doPost(ADDRESS + "transform.php?ver=version-03-06", params, "", ADDRESS);
    		        
    		        System.out.println(res);
    		        
    		        if(null != res && res.contains("success")){
    		        	
    		        	JSONObject jb = new JSONObject(res);
    		        	

    		        		

    		        		
		        		user_id = jb.getString("uid");
		        		

		        		
		        		bLogin = true;
		        		
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
    
    
    public static void geteventsfromesavedata(){
    	try{
    		
    		Vector<String[]> tmp = pDataManager.getpLatestevents();
    		
    		for(int i = 0; i < tmp.size(); i++){
    			eventDetailsVec.add(tmp.elementAt(i).clone());
    		}
    		
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
    public boolean getTotalBet(){
    	
    	try{
        	String res = "";
        	
            List<NameValuePair> params = new ArrayList<NameValuePair>();		  
            params.add(new BasicNameValuePair("login_layer", "co"));
            params.add(new BasicNameValuePair("uid", user_id));
            params.add(new BasicNameValuePair("langx", "zh-cn"));
            params.add(new BasicNameValuePair("ver", "version-03-06"));
            params.add(new BasicNameValuePair("p", "get_league_wager"));
            params.add(new BasicNameValuePair("session", "FT"));
            
            params.add(new BasicNameValuePair("gtype", "FT"));
            params.add(new BasicNameValuePair("date", ""));
            params.add(new BasicNameValuePair("market", "FT"));
            params.add(new BasicNameValuePair("gold", "FT"));
            params.add(new BasicNameValuePair("percentage", "full"));
            params.add(new BasicNameValuePair("down_id", "all"));
            params.add(new BasicNameValuePair("league_id", "all"));
            params.add(new BasicNameValuePair("filter", "Y"));
            params.add(new BasicNameValuePair("symbol", "more"));
            
            res = doPost(ADDRESS + "transform.php?ver=version-03-06", params, "",ADDRESS);
            
            System.out.println(res);
            
            if(null != res && res.contains("gtype")){
            	return parseBet(res);
            }
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}

    	return false;
    }
    
    
    
    
    public boolean getTotalInplayBet(){
    	
    	try{
        	String res = "";
        	
            List<NameValuePair> params = new ArrayList<NameValuePair>();		  
            params.add(new BasicNameValuePair("login_layer", "co"));
            params.add(new BasicNameValuePair("uid", user_id));
            params.add(new BasicNameValuePair("langx", "zh-cn"));
            params.add(new BasicNameValuePair("ver", "version-03-06"));
            params.add(new BasicNameValuePair("p", "get_league_wager"));
            params.add(new BasicNameValuePair("session", "RB"));
            
            params.add(new BasicNameValuePair("gtype", "FT"));
            params.add(new BasicNameValuePair("date", ""));
            params.add(new BasicNameValuePair("market", "FT"));
            params.add(new BasicNameValuePair("gold", "FT"));
            params.add(new BasicNameValuePair("percentage", "full"));
            params.add(new BasicNameValuePair("down_id", "all"));
            params.add(new BasicNameValuePair("league_id", "all"));
            params.add(new BasicNameValuePair("filter", "Y"));
            params.add(new BasicNameValuePair("symbol", "more"));
            
            res = doPost(ADDRESS + "transform.php?ver=version-03-06", params, "",ADDRESS);
            
            System.out.println(res);
            
            if(null != res && res.contains("gtype")){
            	return parseInplayBet(res);
            }
            
            if(res.contains("no_data")) {
            	return true;
            }
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}

    	return false;
    }
    
    
    
    
    
    
    
	public static void saveEvents(){
		
		try{
			
/*			String[] test = {"993839509", "西班牙 - 西甲", "1491413400000", "【滚动盘】巴萨-vs-西维尔", "100000", "200000", "-100000", "-100000"};
			eventDetailsVec.add(test);*/
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			

			
			
			for(int i = 0; i < eventDetailsVec.size(); i++){
				
				//String[] tmp =  eventDetailsVec.elementAt(i).clone();
				
				long eventTime = Long.parseLong(eventDetailsVec.elementAt(i)[HGINDEX.TIME.ordinal()]);
				
				String eventName = eventDetailsVec.elementAt(i)[HGINDEX.EVENTNAMNE.ordinal()];
				

				
				long currentTime = System.currentTimeMillis();
				
				long passMinutes = 108*60*1000;
				
				
				if(eventTime - currentTime < 6*60*1000){
					
					String eventTimestr = df.format(eventTime);
					
					String[] saveItem = (String[])eventDetailsVec.elementAt(i).clone();
					
					saveItem[HGINDEX.TIME.ordinal()] = eventTimestr;
					
					boolean saveRes = pDataManager.saveTofile(saveItem);
					
					if(saveRes == true){
						System.out.println("hg save success:" + Arrays.toString(saveItem));

					}
				}
						
				

				
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}
    
  
    public boolean parseBet(String res){
    	
    	try{
    		JSONObject betData = new JSONObject(res);
    		JSONArray league = betData.getJSONArray("league");
    		
    		
        	
        	for(int j = 0;j <league.length();j++ ){
            	JSONObject oneLeague = league.getJSONObject(j);
            	JSONArray game = oneLeague.getJSONArray("game");
            	String leagueName = oneLeague.getString("league_name");
            	if(isInShowLeagueName(leagueName)==false) {
            		continue;
            	}
            	for(int k = 0; k< game.length();k++) {
            		JSONObject oneGame = game.getJSONObject(k);
            		String gidm = oneGame.getString("gidm");
            		
                    List<NameValuePair> params1 = new ArrayList<NameValuePair>();		  
                    
                    params1.add(new BasicNameValuePair("login_layer", "co"));
                    params1.add(new BasicNameValuePair("uid", user_id));
                    params1.add(new BasicNameValuePair("langx", "zh-cn"));
                    params1.add(new BasicNameValuePair("ver", "version-03-06"));
                    params1.add(new BasicNameValuePair("p", "get_allbet_wager"));
                    params1.add(new BasicNameValuePair("session", "FT"));
                    params1.add(new BasicNameValuePair("gtype", "FT"));                    
                    params1.add(new BasicNameValuePair("date", ""));
                    params1.add(new BasicNameValuePair("market", ""));
                    params1.add(new BasicNameValuePair("symbol", "more"));                    
                    params1.add(new BasicNameValuePair("gold", "0"));
                    params1.add(new BasicNameValuePair("percentage", "full"));
                    params1.add(new BasicNameValuePair("down_id", "all"));
                    params1.add(new BasicNameValuePair("league_id", "all"));
                    params1.add(new BasicNameValuePair("gidm", gidm));
                    
                    String oneEventres = doPost(ADDRESS + "transform.php?ver=version-03-06", params1, "",ADDRESS);

                    Thread.sleep(1000);
                    
//                    if(oneEventres == null || !oneEventres.contains("league_name")){
//                    	oneEventres = doPost(ADDRESS + "app/ft/get_today_ft_allbets_wager.php", params1, "","");
//                    	Thread.currentThread().sleep(1000);
//                    }
                    
                    if(null != oneEventres && oneEventres.contains("league_name")){
                    	
                    	//解析一场比赛

            	        JSONObject oneGameDetail = new JSONObject(oneEventres);
            	        
            	        
                        String league_name = oneGameDetail.getString("league_name");

                        String team_h = oneGameDetail.getString("team_h");

                        String team_c = oneGameDetail.getString("team_c");

                        String live = oneGameDetail.getString("live");

                        
                        JSONObject session = oneGameDetail.getJSONObject("session");
                        
                        String date = session.getString("date");

                        
                        String time = session.getString("time");

                        
                        
                        JSONArray wtypeJA = oneGameDetail.getJSONArray("wtype"); 

                    	
                        String eventIdStr = team_h + team_c + date + time;
                        
                        String eventId = Integer.toString(eventIdStr.hashCode());
                        
                        System.out.println(eventIdStr + ":" + eventId);
                        
                    	
            			int index = 0;
            			
            			boolean eventIDexist = false;
            			
            			for(index = 0; index < eventDetailsVec.size(); index++){
            				if(eventDetailsVec.elementAt(index)[HGINDEX.EVENTID.ordinal()].equals(eventId)){	//todo gidm是否唯一
            					eventIDexist = true;
            					break;
            				}
            			}
                    	
            			
            			if(eventIDexist == false){
            				
                			String[] row = new String[HGINDEX.SIZE.ordinal()];

                			
                			row[HGINDEX.EVENTID.ordinal()] = eventId;
                			row[HGINDEX.LEAGUENAME.ordinal()] = league_name;
                			
                			//change to local time
                			
                			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
                			
                			String timeStr = date + " " + time;
                			
                			java.util.Date Mintime = dfMin.parse(timeStr);
                			
                			Calendar calTime = Calendar.getInstance();
                			calTime.setTime(Mintime);
                			calTime.add(Calendar.HOUR_OF_DAY, 12);
                			
                			//timeStr = dfMin.format(calTime.getTimeInMillis());

                			row[HGINDEX.TIME.ordinal()] = Long.toString(calTime.getTimeInMillis());
                			
                			//
                			
                			row[HGINDEX.EVENTNAMNE.ordinal()] = team_h + "-vs-" + team_c;
                			
                			if(team_h.contains("角球数")) {
                				continue;
                			}
                			
                			row[HGINDEX.PERIOD0HOME.ordinal()] = "0";
                			row[HGINDEX.PERIOD0OVER.ordinal()] = "0";
                			row[HGINDEX.PERIOD1HOME.ordinal()] = "0";
                			row[HGINDEX.PERIOD1OVER.ordinal()] = "0";
            				eventDetailsVec.add(row);
            			}
            			
            			
                        for(int i = 0; i < wtypeJA.length(); i++){
                        	JSONObject oneType = wtypeJA.getJSONObject(i);
                        	String wtypeId = oneType.getString("wtype");                        	                        	                       
                        	JSONArray gidJA = oneType.getJSONArray("gid");
                        	if(wtypeId.toUpperCase().equals("OU")){
                        		for(int kk= 0;kk<gidJA.length();kk++) {
                        			JSONObject oneGid = gidJA.getJSONObject(kk);
                        			Object value = oneGid.get("rtype");
                        			if(value instanceof JSONObject) {
                        				JSONObject rtype = oneGid.getJSONObject("rtype");                        				
                        				Iterator<String> keys = rtype.keys();                        				
                        				while (keys.hasNext()) {
                        					String key = keys.next();
                        					JSONObject oneRtype = rtype.getJSONObject(key);                        					
                                			String rtypeId = oneRtype.getString("rtype");                     			
                                    		if(rtypeId.toUpperCase().equals("OUH")){       //客场                                  			
                                    			String goldStr = oneRtype.getString("gold");                        			
                        						String countStr = oneRtype.getString("count");
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()];
                        						
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
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = oldhgoldStr + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + Integer.toString(dvalue);
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = "(0|0)" + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + "-" + goldStr;
                        						}
                                    		}else if(rtypeId.toUpperCase().equals("OUC")){//OUC是主队
                                    			String goldStr = oneRtype.getString("gold");                       			
                        						String countStr = oneRtype.getString("count");            						
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()];
                        						
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
                        							
                        							
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = "(" + goldStr + "|" + countStr + ")" + "-" + oldcgoldStr + "=" + Integer.toString(dvalue);
                        							
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = "(" + goldStr + "|" + countStr + ")"  + "-" + "(0|0)" + "=" + goldStr;
                        						}
                                    			
                                    		}
                        				}
                        				

                        				
                        			}else if(value instanceof JSONArray) {
                                		JSONArray rtypeJA = oneGid.getJSONArray("rtype");
                                		
                                		for(int jj = 0; jj < rtypeJA.length(); jj++){
                                			JSONObject oneRtype = rtypeJA.getJSONObject(jj);                            			
                                			String rtypeId = oneRtype.getString("rtype");                            			
                                    		if(rtypeId.toUpperCase().equals("OUH")){       //客场                                  			
                                    			String goldStr = oneRtype.getString("gold");                        			
                        						String countStr = oneRtype.getString("count");
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()];
                        						
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
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = oldhgoldStr + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + Integer.toString(dvalue);
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = "(0|0)" + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + "-" + goldStr;
                        						}
                                    		}else if(rtypeId.toUpperCase().equals("OUC")){//OUC是主队
                                    			String goldStr = oneRtype.getString("gold");                       			
                        						String countStr = oneRtype.getString("count");            						
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()];
                        						
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
                        							
                        							
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = "(" + goldStr + "|" + countStr + ")" + "-" + oldcgoldStr + "=" + Integer.toString(dvalue);
                        							
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = "(" + goldStr + "|" + countStr + ")"  + "-" + "(0|0)" + "=" + goldStr;
                        						}
                                    			
                                    		}
                                			
                                		}
                        			}


                            		

                            		
                        			
                        			
                        		}
                        		

                        		
                        	}else if(wtypeId.toUpperCase().equals("R")){
                        		
                        		for(int kk= 0;kk<gidJA.length();kk++) {
                        			JSONObject oneGid = gidJA.getJSONObject(kk);
                        			
                        			Object value = oneGid.get("rtype");
                        			if(value instanceof JSONObject) {
                        				
                        				JSONObject rtype = oneGid.getJSONObject("rtype");                        				
                        				Iterator<String> keys = rtype.keys();                        				
                        				while (keys.hasNext()) {
                        					String key = keys.next();
                        					JSONObject oneRtype = rtype.getJSONObject(key);  
                                			String rtypeId = oneRtype.getString("rtype");
                                    		if(rtypeId.toUpperCase().equals("RH")){
                                    			String goldStr = oneRtype.getString("gold");                        			
                        						String countStr = oneRtype.getString("count");
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()];                    						
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
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = "(" + goldStr + "|" + countStr + ")" + "-" + oldcgoldStr + "=" + Integer.toString(dvalue);
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = "(" + goldStr + "|" + countStr + ")"  + "-" + "(0|0)" + "=" + goldStr;
                        						}                                		                                			
                                    		}else if(rtypeId.toUpperCase().equals("RC")){
                                    			String goldStr = oneRtype.getString("gold");                        			
                        						String countStr = oneRtype.getString("count");            						
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()];
                        						
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
                        							
                        							
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = oldhgoldStr + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + Integer.toString(dvalue);
                        							
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = "(0|0)" + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + "-" + goldStr;
                        						}
                                    		}
                        				}
                        				
                        			}else if(value instanceof JSONArray) {
                                		JSONArray rtypeJA = oneGid.getJSONArray("rtype");
                                		for(int jj = 0; jj < rtypeJA.length(); jj++){
                                			JSONObject oneRtype = rtypeJA.getJSONObject(jj);                            			
                                			String rtypeId = oneRtype.getString("rtype");
                                    		if(rtypeId.toUpperCase().equals("RH")){
                                    			String goldStr = oneRtype.getString("gold");                        			
                        						String countStr = oneRtype.getString("count");
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()];                    						
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
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = "(" + goldStr + "|" + countStr + ")" + "-" + oldcgoldStr + "=" + Integer.toString(dvalue);
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = "(" + goldStr + "|" + countStr + ")"  + "-" + "(0|0)" + "=" + goldStr;
                        						}                                		                                			
                                    		}else if(rtypeId.toUpperCase().equals("RC")){
                                    			String goldStr = oneRtype.getString("gold");                        			
                        						String countStr = oneRtype.getString("count");            						
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()];
                        						
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
                        							
                        							
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = oldhgoldStr + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + Integer.toString(dvalue);
                        							
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = "(0|0)" + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + "-" + goldStr;
                        						}
                                    		}
                                		}
                        			}
                        			

                        		}
                        }                        
                        }

                    }else{
                    	return false;
                    }
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
    }
    

    public boolean parseInplayBet(String res){
    	
    	try{
    		JSONObject betData = new JSONObject(res);
    		JSONArray league = betData.getJSONArray("league");
    		
    		
        	
        	for(int j = 0;j <league.length();j++ ){
            	JSONObject oneLeague = league.getJSONObject(j);
            	JSONArray game = oneLeague.getJSONArray("game");
            	String leagueName = oneLeague.getString("league_name");
            	if(isInShowLeagueName(leagueName)==false) {
            		continue;
            	}
            	for(int k = 0; k< game.length();k++) {
            		JSONObject oneGame = game.getJSONObject(k);
            		String gidm = oneGame.getString("gidm");
            		
                    List<NameValuePair> params1 = new ArrayList<NameValuePair>();		  
                    
                    params1.add(new BasicNameValuePair("login_layer", "co"));
                    params1.add(new BasicNameValuePair("uid", user_id));
                    params1.add(new BasicNameValuePair("langx", "zh-cn"));
                    params1.add(new BasicNameValuePair("ver", "version-03-06"));
                    params1.add(new BasicNameValuePair("p", "get_allbet_wager"));
                    params1.add(new BasicNameValuePair("session", "RB"));
                    params1.add(new BasicNameValuePair("gtype", "FT"));                    
                    params1.add(new BasicNameValuePair("date", ""));
                    params1.add(new BasicNameValuePair("market", ""));
                    params1.add(new BasicNameValuePair("symbol", "more"));                    
                    params1.add(new BasicNameValuePair("gold", "0"));
                    params1.add(new BasicNameValuePair("percentage", "full"));
                    params1.add(new BasicNameValuePair("down_id", "all"));
                    params1.add(new BasicNameValuePair("league_id", "all"));
                    params1.add(new BasicNameValuePair("gidm", gidm));
                    
                    String oneEventres = doPost(ADDRESS + "transform.php?ver=version-03-06", params1, "",ADDRESS);

                    Thread.sleep(1000);
                    
//                    if(oneEventres == null || !oneEventres.contains("league_name")){
//                    	oneEventres = doPost(ADDRESS + "app/ft/get_today_ft_allbets_wager.php", params1, "","");
//                    	Thread.currentThread().sleep(1000);
//                    }
                    
                    if(null != oneEventres && oneEventres.contains("league_name")){
                    	
                    	//解析一场比赛

            	        JSONObject oneGameDetail = new JSONObject(oneEventres);
            	        
            	        
                        String league_name = oneGameDetail.getString("league_name");

                        String team_h = oneGameDetail.getString("team_h");

                        String team_c = oneGameDetail.getString("team_c");

                        String live = oneGameDetail.getString("live");

                        
                        JSONObject session = oneGameDetail.getJSONObject("session");
                        
                        String m_gid = oneGameDetail.getString("m_gid");

                        
                        String time = session.getString("reminute");

                        
                        
                        JSONArray wtypeJA = oneGameDetail.getJSONArray("wtype"); 

                    	
                        String eventIdStr = team_h + team_c + m_gid;
                        
                        String eventId = Integer.toString(eventIdStr.hashCode());
                        
                        System.out.println(eventIdStr + ":" + eventId);
                        
                    	
            			int index = 0;
            			
            			boolean eventIDexist = false;
            			
            			for(index = 0; index < eventDetailsVec.size(); index++){
            				if(eventDetailsVec.elementAt(index)[HGINDEX.EVENTID.ordinal()].equals(eventId)){	//todo gidm是否唯一
            					eventIDexist = true;
            					break;
            				}
            			}
                    	
            			
            			if(eventIDexist == false){
            				
                			String[] row = new String[HGINDEX.SIZE.ordinal()];

                			
                			row[HGINDEX.EVENTID.ordinal()] = eventId;
                			row[HGINDEX.LEAGUENAME.ordinal()] = league_name;
                			
                			//change to local time
                			
                			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
                			
                			String timeStr =  time;
                			
//                			java.util.Date Mintime = dfMin.parse(timeStr);
//                			
//                			Calendar calTime = Calendar.getInstance();
//                			calTime.setTime(Mintime);
//                			calTime.add(Calendar.HOUR_OF_DAY, 12);
                			
                			//timeStr = dfMin.format(calTime.getTimeInMillis());

                			row[HGINDEX.TIME.ordinal()] = "1000000";

                			row[HGINDEX.EVENTNAMNE.ordinal()] = "【滚动盘】" + team_h + "-vs-" + team_c;
                			
                			if(team_h.contains("角球数")) {
                				continue;
                			}
                			
                			row[HGINDEX.PERIOD0HOME.ordinal()] = "0";
                			row[HGINDEX.PERIOD0OVER.ordinal()] = "0";
                			row[HGINDEX.PERIOD1HOME.ordinal()] = "0";
                			row[HGINDEX.PERIOD1OVER.ordinal()] = "0";
            				eventDetailsVec.add(row);
            			}
            			
            			
                        for(int i = 0; i < wtypeJA.length(); i++){
                        	JSONObject oneType = wtypeJA.getJSONObject(i);
                        	String wtypeId = oneType.getString("wtype");                        	                        	                       
                        	JSONArray gidJA = oneType.getJSONArray("gid");
                        	if(wtypeId.toUpperCase().equals("ROU")){
                        		for(int kk= 0;kk<gidJA.length();kk++) {
                        			JSONObject oneGid = gidJA.getJSONObject(kk);
                        			Object value = oneGid.get("rtype");
                        			if(value instanceof JSONObject) {
                        				JSONObject rtype = oneGid.getJSONObject("rtype");                        				
                        				Iterator<String> keys = rtype.keys();                        				
                        				while (keys.hasNext()) {
                        					String key = keys.next();
                        					JSONObject oneRtype = rtype.getJSONObject(key);                        					
                                			String rtypeId = oneRtype.getString("rtype");                     			
                                    		if(rtypeId.toUpperCase().equals("ROUH")){       //客场                                  			
                                    			String goldStr = oneRtype.getString("gold");                        			
                        						String countStr = oneRtype.getString("count");
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()];
                        						
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
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = oldhgoldStr + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + Integer.toString(dvalue);
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = "(0|0)" + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + "-" + goldStr;
                        						}
                                    		}else if(rtypeId.toUpperCase().equals("ROUC")){//OUC是主队
                                    			String goldStr = oneRtype.getString("gold");                       			
                        						String countStr = oneRtype.getString("count");            						
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()];
                        						
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
                        							
                        							
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = "(" + goldStr + "|" + countStr + ")" + "-" + oldcgoldStr + "=" + Integer.toString(dvalue);
                        							
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = "(" + goldStr + "|" + countStr + ")"  + "-" + "(0|0)" + "=" + goldStr;
                        						}
                                    			
                                    		}
                        				}
                        				

                        				
                        			}else if(value instanceof JSONArray) {
                                		JSONArray rtypeJA = oneGid.getJSONArray("rtype");
                                		
                                		for(int jj = 0; jj < rtypeJA.length(); jj++){
                                			JSONObject oneRtype = rtypeJA.getJSONObject(jj);                            			
                                			String rtypeId = oneRtype.getString("rtype");                            			
                                    		if(rtypeId.toUpperCase().equals("ROUH")){       //客场                                  			
                                    			String goldStr = oneRtype.getString("gold");                        			
                        						String countStr = oneRtype.getString("count");
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()];
                        						
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
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = oldhgoldStr + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + Integer.toString(dvalue);
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = "(0|0)" + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + "-" + goldStr;
                        						}
                                    		}else if(rtypeId.toUpperCase().equals("ROUC")){//OUC是主队
                                    			String goldStr = oneRtype.getString("gold");                       			
                        						String countStr = oneRtype.getString("count");            						
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()];
                        						
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
                        							
                        							
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = "(" + goldStr + "|" + countStr + ")" + "-" + oldcgoldStr + "=" + Integer.toString(dvalue);
                        							
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0OVER.ordinal()] = "(" + goldStr + "|" + countStr + ")"  + "-" + "(0|0)" + "=" + goldStr;
                        						}
                                    			
                                    		}
                                			
                                		}
                        			}


                            		

                            		
                        			
                        			
                        		}
                        		

                        		
                        	}else if(wtypeId.toUpperCase().equals("RE")){
                        		
                        		for(int kk= 0;kk<gidJA.length();kk++) {
                        			JSONObject oneGid = gidJA.getJSONObject(kk);
                        			
                        			Object value = oneGid.get("rtype");
                        			if(value instanceof JSONObject) {
                        				
                        				JSONObject rtype = oneGid.getJSONObject("rtype");                        				
                        				Iterator<String> keys = rtype.keys();                        				
                        				while (keys.hasNext()) {
                        					String key = keys.next();
                        					JSONObject oneRtype = rtype.getJSONObject(key);  
                                			String rtypeId = oneRtype.getString("rtype");
                                    		if(rtypeId.toUpperCase().equals("REH")){
                                    			String goldStr = oneRtype.getString("gold");                        			
                        						String countStr = oneRtype.getString("count");
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()];                    						
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
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = "(" + goldStr + "|" + countStr + ")" + "-" + oldcgoldStr + "=" + Integer.toString(dvalue);
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = "(" + goldStr + "|" + countStr + ")"  + "-" + "(0|0)" + "=" + goldStr;
                        						}                                		                                			
                                    		}else if(rtypeId.toUpperCase().equals("REC")){
                                    			String goldStr = oneRtype.getString("gold");                        			
                        						String countStr = oneRtype.getString("count");            						
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()];
                        						
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
                        							
                        							
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = oldhgoldStr + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + Integer.toString(dvalue);
                        							
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = "(0|0)" + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + "-" + goldStr;
                        						}
                                    		}
                        				}
                        				
                        			}else if(value instanceof JSONArray) {
                                		JSONArray rtypeJA = oneGid.getJSONArray("rtype");
                                		for(int jj = 0; jj < rtypeJA.length(); jj++){
                                			JSONObject oneRtype = rtypeJA.getJSONObject(jj);                            			
                                			String rtypeId = oneRtype.getString("rtype");
                                    		if(rtypeId.toUpperCase().equals("REH")){
                                    			String goldStr = oneRtype.getString("gold");                        			
                        						String countStr = oneRtype.getString("count");
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()];                    						
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
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = "(" + goldStr + "|" + countStr + ")" + "-" + oldcgoldStr + "=" + Integer.toString(dvalue);
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = "(" + goldStr + "|" + countStr + ")"  + "-" + "(0|0)" + "=" + goldStr;
                        						}                                		                                			
                                    		}else if(rtypeId.toUpperCase().equals("REC")){
                                    			String goldStr = oneRtype.getString("gold");                        			
                        						String countStr = oneRtype.getString("count");            						
                        						String allp0hStr = eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()];
                        						
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
                        							
                        							
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = oldhgoldStr + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + Integer.toString(dvalue);
                        							
                        						}else{
                        							eventDetailsVec.elementAt(index)[HGINDEX.PERIOD0HOME.ordinal()] = "(0|0)" + "-" + "(" + goldStr + "|" + countStr + ")" + "=" + "-" + goldStr;
                        						}
                                    		}
                                		}
                        			}
                        			

                        		}
                        }                        
                        }

                    }else{
                    	return false;
                    }
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
        			String leagueName = eventDetailsVec.elementAt(i)[HGINDEX.LEAGUENAME.ordinal()];
        			
        			if(isInShowLeagueName(leagueName)){        				
        				highShowVec.add(eventDetailsVec.elementAt(i));        				
        				
        			}
        				
        			
        			
        		}        
        		
        		for(int i = 0; i < eventDetailsVec.size(); ){
        			String leagueName = eventDetailsVec.elementAt(i)[HGINDEX.LEAGUENAME.ordinal()];
        			
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
        			
        			
        			
        			long time = Long.parseLong(eventDetailsVec.elementAt(i)[HGINDEX.TIME.ordinal()]);
        			
        			String eventTimeArray[] = df.format(time).split(" ");
        			
        			String timeStr = "";
        			
        			if(currentTimeArray[0].contains(eventTimeArray[0])){
        				timeStr = eventTimeArray[1];
        			}else{
        				timeStr = df.format(time);
        			}
        			
        			
        			eventDetailsVec.elementAt(i)[HGINDEX.TIME.ordinal()] = timeStr;
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
    public String doPost(String url,List<NameValuePair> formparams, String cookies, String refer) {
        return doPost(url, formparams,"UTF-8", cookies, refer);
    }

    public String doPost(String url,List<NameValuePair> formparams,String charset, String cookies, String refer) {


     // 创建httppost   
    	
    	try {
    	
        HttpPost httppost = new HttpPost(url); 
        
/*        if(url.contains("get_today_ft_league_wager.php")){
        	httppost.addHeader("Cookie", strCookies);
        }*/
        
        //httppost.addHeader("Cookie", cookies);
        //httppost.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
        //httppost.addHeader("x-requested-with","XMLHttpRequest");
        httppost.addHeader("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        httppost.addHeader("Accept","*/*");
        httppost.addHeader("Accept-Encoding","gzip, deflate");
        httppost.addHeader("Connection","keep-alive");
        
        
        //httppost.addHeader("Origin","https://ag.hga030.com");
        if(refer.length()>0) {
        	httppost.addHeader("Referer",refer);
        }
        
        //httppost.addHeader("Host","ag.hga030.com");
        
        
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
    
    
    
	public static void showpDataWnd(){
		pDataWindow.setVisible(true);
	}
    
	
	public static void updatepDataDetails(){
		pDataManager.updatepEventsDetails();
	}
	
	
    public static Vector<String[]> getpSubevents(){
    	return pDataManager.getpSubevents();
    }
    
    
}
