package P8;



import java.awt.Button;
import java.awt.Container; 
import java.awt.Frame;
import java.awt.Panel;

import javax.print.DocFlavor.URL;
import javax.sound.sampled.AudioInputStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel; 
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JOptionPane;

import java.util.Vector;

import javax.swing.ImageIcon;

import HG.GrabHGEventsThread;
import HG.HGMergeManager;
import HG.HGhttp;
import HGclient.GrabHGclientThread;
import HGclient.HGclienthttp;
import HttpServer.HttpServer;
import HttpServer.HttpServerThread;
import MergeNew.MergeNewManager;
import P8client.P8clienthttp;
import team.gl.nio.cln.ZhiboClient;




@SuppressWarnings("unused")
public class StoneAge {
	
	
	public static JButton btnHGpData;
	
	public static JButton btnMergepData;
	
	public static JButton btnNewMergepData;
	
	public static JButton btnZhibopData;
	
	public static JButton btnpData;
	
	public static JButton btnLogin;
	
	public static JButton btnhgLogin;
	
	public static JButton btnAccount;
	
	public static JButton btnodds;
	
	public static JButton btndxq;
	
	public static JButton btndxqans;
	
	public static JButton btnpdxq;
	
	public static JButton btnpodds;
	
	public static boolean bLogin = false;
	
	public static boolean bHGLogin = false;
	
	public static boolean bScoreLogin = false;
	
	public static GrabEventsThread grabThead;
	
	public static GrabHGEventsThread grabhgThead;
	
	public static StoneAge sa;
		
	public static JButton btnZhiboConnect;
	
	public static JButton btnMergeWnd;
	
	public static JButton btnNewMergeWnd;
	
	public static Vector<String> mailList = new Vector<String>();
	
	
	public static boolean zhiboConnected = false;
	
	static ZhiboThread zhiboThread = null;
	
	private JButton scoreBtn = new JButton("比分网");
	
	
	public static Score score = new Score();
	
	public static ScoreThread scorethread = null;
	
	
	
	
	//public static boolean showMergeWnd = false;
	public static boolean showP8 = false;
	public static boolean showHG = false;
	public static boolean showZhibo = false;
	public static boolean showScore = false;

	
	public JTextField textFieldZhiboProxyAddress;
	
	public JTextField textFieldZhiboProxyAccount;
	
	
	
	
	static AccountsDetailsWindow accountWnd = new AccountsDetailsWindow();
	
	public static AccountManager accMgr = null;
	
	public static AccounthgManager acchgMgr = null;
	
	static SeverThread serverThread = null;
	
	static HttpServerThread httpServerThread = null;
	
	
	public PrintStream psFile = null;
	public PrintStream psConsole = null;
	
	
	public static void initMailList(){
		
		mailList.add("490207143@qq.com");
		
		mailList.add("1131894627@qq.com");
		
		mailList.add("tongjigujinlong@126.com");
		
		mailList.add("2503706418@qq.com");
		
		mailList.add("2195876152@qq.com");
		
		
		
/*		mailList.add("43069453@qq.com");
		mailList.add("490207143@qq.com");
		mailList.add("2503706418@qq.com");
		mailList.add("281426295@qq.com");
		mailList.add("84131403@qq.com");
		mailList.add("1131894627@qq.com");
		mailList.add("228394940@qq.com");
		mailList.add("2195876152@qq.com");
		mailList.add("3215676858@qq.com");
		mailList.add("573306035@qq.com");
		mailList.add("45517203@qq.com");*/

	}
	
	public static Vector<String> getMailList(){
		return mailList;
	}
	
	
	public static void main(String[] args) throws Exception {

/*		
		GrabHGclientThread hgclientthread = new GrabHGclientThread();
		hgclientthread.start();*/
		
		

		
		
		//平博会员
/*		String address = "https://www.pinbet88.com/";
		P8clienthttp p8 = new P8clienthttp();
		p8.setLoginParams(address, "12q0301001", "aabb1122");*/
		//if(p8.login()==1){
/*		P8clienthttp p8 = new P8clienthttp();
		if(true){
			p8.getTotalbet();
		}*/

/*		scorethread = new ScoreThread();
		scorethread.start();*/
		
		
		initMailList();
		
		accMgr = new AccountManager(accountWnd);
		
		acchgMgr = new AccounthgManager(accountWnd);
		
		accountWnd.setAccountMgr(accMgr);
		
		accountWnd.sethgAccountMgr(acchgMgr);
		
		accMgr.init();
		
		acchgMgr.init();
		
		MergeManager.init();
		
		MergeNewManager.init();
		
		ScoreMergeManager.init();
		
		HGMergeManager.init();
		
		P8Http.initShowLeagueName();
		
		HGhttp.initShowLeagueName();
		
		ZhiboManager.initShowLeagueName();

		sa = new StoneAge();
		
		serverThread = new SeverThread();
		
		serverThread.start();
		
		httpServerThread = new HttpServerThread();
		
		httpServerThread.start();
		
		sa.launchFrame();
	}
	
	
	public static void setSleepTime(int sec){
		grabThead.setSleepTime(sec);
	}
	
	public static void sethgSleepTime(int sec){
		grabhgThead.setSleepTime(sec);
	}
	
	public static void setZhiboSleepTime(int sec){
		zhiboThread.setSleepTime(sec);
	}
	
	public void setFileout(){
		
		try{
			System.setOut(psFile);
			System.setErr(psFile);
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void setConsoleout(){
		try{
			System.setOut(psConsole);
			System.setErr(psConsole);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void launchFrame() {
		
		
		try {
			// 生成路径
			File dir = new File("log");
			if (dir.exists()) {
			} else {
				dir.mkdirs();
			}

			// 把输出重定向到文件
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// 设置日期格式
			
			psFile = new PrintStream("log/" + df.format(new Date())
						+ ".txt");
			
			
			psConsole = System.out;

			System.setOut(psFile);
			System.setErr(psFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		JFrame mainFrame = new JFrame("实况足球");

		mainFrame.setLayout(null);
		
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		//mainFrame.add(panel);
		
		
		
        ImageIcon img = new ImageIcon("images/bg.jpg");  
        //要设置的背景图片  
        JLabel imgLabel = new JLabel(img);  
        //将背景图放在标签里。  
        mainFrame.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));  
        //将背景标签添加到jfram的LayeredPane面板里。  
        imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());  
        

        
        Container contain = mainFrame.getContentPane();  
        ((JPanel) contain).setOpaque(false);   
        
        
        
        int Xposition = 0;
        int Yposition = 140;
        
        
		btnLogin = new JButton("P8即时注单");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
					if(bLogin == true){
						P8Http.showEventsDeatilsTable();
						return;
					}
				
					accountWnd.dispose();

					btnLogin.setEnabled(false);
					
					grabThead = new GrabEventsThread(sa);
					
					grabThead.start();
					
					showP8 = true;
			}
		});
		
		
		btnLogin.setSize(120, 25);
		btnLogin.setLocation(Xposition, Yposition - 120);
		
		
		contain.add(btnLogin);
		
		
		btnpData = new JButton("P8历史注单");
		btnpData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				P8Http.updatepDataDetails();
				P8Http.showpDataWnd();
			}
		});
		
		
		btnpData.setSize(120, 25);
		btnpData.setLocation(Xposition + 150, Yposition - 120);
		
		
		contain.add(btnpData);
		
        
		
		btnZhiboConnect = new JButton("LL即时注单");
		btnZhiboConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
					
					if(zhiboConnected){
						showZhibo = true;
						ZhiboManager.showEventsDeatilsTable();
						return;
					}
					String address = textFieldZhiboProxyAddress.getText();
					String port = textFieldZhiboProxyAccount.getText();
					
					ZhiboClient.HOST = address;
					ZhiboClient.PORT = Integer.parseInt(port);
					
					if(ZhiboClient.connect() == false){
						JOptionPane.showMessageDialog(null,"连接智博失败！");
						return;
					}
					
					zhiboConnected = true;
					

					
					zhiboThread = new ZhiboThread();
					zhiboThread.start();
					
					ZhiboManager.showEventsDeatilsTable();
					
					System.out.println("连接成功");
					
					showZhibo = true;

					
				}catch(Exception ex){
					ex.printStackTrace();
				}
				


			}
		});

		btnZhiboConnect.setSize(120, 25);
		btnZhiboConnect.setLocation(Xposition, Yposition - 80);
		
        
		
		btnZhibopData = new JButton("LL历史注单");
		btnZhibopData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ZhiboManager.updatepDataDetails();
				ZhiboManager.showpDataWnd();
			}
		});
		
		
		btnZhibopData.setSize(120, 25);
		btnZhibopData.setLocation(Xposition + 150, Yposition - 80);
		
		
		contain.add(btnZhibopData);
		
        
		
		
		
		btnhgLogin = new JButton("HG即时注单");
		btnhgLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				if(bHGLogin == true){
					HGhttp.showEventsDeatilsTable();
					return;
				}
				
					
					
				grabhgThead = new GrabHGEventsThread(sa);
				
				grabhgThead.start();
				
				showHG = true;
					
					
			}
		});
		
		
		btnhgLogin.setSize(120, 25);
		btnhgLogin.setLocation(Xposition, Yposition - 40);
		
		
		//contain.add(btnhgLogin);
		
		
		
        
		btnHGpData = new JButton("HG历史注单");
		btnHGpData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HGhttp.updatepDataDetails();
				HGhttp.showpDataWnd();
			}
		});
		
		
		btnHGpData.setSize(120, 25);
		btnHGpData.setLocation(Xposition+ 150, Yposition - 40);
		
		//contain.add(btnHGpData);
        
        
		
		
		btnMergeWnd = new JButton("合并即时注单");
		
		btnMergeWnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
					
					if(bLogin == true && zhiboConnected == true){
				        MergeManager.clearMergeData();
				        MergeManager.constructMergeRes();
				        MergeManager.updateEventsDetails();
				        MergeManager.showMergeDetailsWnd(true);
					}
					
					if(bLogin == false){
						JOptionPane.showMessageDialog(null,"请先连接平博");
						return;
					}
					
					
					if(zhiboConnected == false){
						JOptionPane.showMessageDialog(null,"请先连接智博");
						return;
					}
					

					//showMergeWnd = true;
					
			        MergeManager.clearMergeData();
			        MergeManager.constructMergeRes();
			        MergeManager.updateEventsDetails();
			        MergeManager.showMergeDetailsWnd(true);

					
				}catch(Exception ex){
					ex.printStackTrace();
				}
				


			}
		});
		
		btnMergeWnd.setSize(120, 25);
		btnMergeWnd.setLocation(Xposition, Yposition);
		
		
		
		
		
		
		
		
		
		
        
		btnMergepData = new JButton("合并历史注单");
		btnMergepData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MergeManager.updatepDataDetails();
				MergeManager.showpDataWnd();
			}
		});
		
		
		btnMergepData.setSize(120, 25);
		btnMergepData.setLocation(Xposition + 150, Yposition);
		
		
		contain.add(btnMergepData);
		
		
		
		btnNewMergeWnd = new JButton("新合并即时注单");
		
		btnNewMergeWnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
					
					if(bLogin == true && zhiboConnected == true){
				        MergeNewManager.clearMergeData();
				        MergeNewManager.constructMergeRes();
				        MergeNewManager.updateEventsDetails();
				        MergeNewManager.showMergeDetailsWnd(true);
					}
					
					if(bLogin == false){
						JOptionPane.showMessageDialog(null,"请先连接平博");
						return;
					}
					
					
					if(zhiboConnected == false){
						JOptionPane.showMessageDialog(null,"请先连接智博");
						return;
					}
					

					//showMergeWnd = true;
					
			        MergeNewManager.clearMergeData();
			        MergeNewManager.constructMergeRes();
			        MergeNewManager.updateEventsDetails();
			        MergeNewManager.showMergeDetailsWnd(true);

					
				}catch(Exception ex){
					ex.printStackTrace();
				}
				


			}
		});
		
		btnNewMergeWnd.setSize(120, 25);
		btnNewMergeWnd.setLocation(Xposition, Yposition + 40);
		
		//contain.add(btnNewMergeWnd);
		
		
		
		
		btnNewMergepData = new JButton("新合并历史注单");
		btnNewMergepData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MergeNewManager.updatepDataDetails();
				MergeNewManager.showpDataWnd();
			}
		});
		
		
		btnNewMergepData.setSize(120, 25);
		btnNewMergepData.setLocation(Xposition + 150, Yposition + 40);
		
		
		//contain.add(btnNewMergepData);
		
		

		
        scoreBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(StoneAge.bScoreLogin == true){
					StoneAge.score.showscoredetailswnd();
					return;
				}

				StoneAge.showScore = true;
				
				
				
			}
		});
        
        scoreBtn.setLocation(Xposition, Yposition + 80);
        scoreBtn.setSize(90, 25);
        
        //contain.add(scoreBtn);

        
		btndxq = new JButton("大小球");
		btndxq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				HGclienthttp.showdxqwnd(true);
			}
		});
		
		
		btndxq.setSize(120, 25);
		btndxq.setLocation(Xposition, Yposition + 40);

		
		contain.add(btndxq);
        

		btnpdxq = new JButton("大小球历史");
		btnpdxq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				HGclienthttp.showdxqpdwnd(true);
			}
		});
		
		
		btnpdxq.setSize(120, 25);
		btnpdxq.setLocation(Xposition + 150, Yposition + 40);

		
		contain.add(btnpdxq);

		
		
		
        
		
		
		btnodds = new JButton("水位");
		btnodds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				HGclienthttp.showdetailswnd(true);
			}
		});
		
		
		btnodds.setSize(120, 25);
		btnodds.setLocation(Xposition, Yposition + 80);

		
		contain.add(btnodds);
        
        
		
		btnpodds = new JButton("历史水位");
		btnpodds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				HGclienthttp.showpdwnd(true);
			}
		});
		
		
		btnpodds.setSize(120, 25);
		btnpodds.setLocation(Xposition + 150, Yposition + 80);

		
		contain.add(btnpodds);
		
		
		
		
		
		
		
		btnAccount = new JButton("设置");
		btnAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				accountWnd.setVisible(true);
				
				//HGclienthttp.showdetailswnd(true);
			}
		});
		
		
		btnAccount.setSize(120, 25);
		btnAccount.setLocation(Xposition, Yposition + 120);

		
		contain.add(btnAccount);
		
		
		btndxqans = new JButton("大小球分析");
		btndxqans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				HGclienthttp.showdxqanswnd(true);
			}
		});
		
		
		btndxqans.setSize(120, 25);
		btndxqans.setLocation(Xposition + 150, Yposition + 120);

		
		contain.add(btndxqans);
		
		
		
		
		JLabel labelZhiboProxyAddress = new JLabel("网址:");
		labelZhiboProxyAddress.setSize(50, 25);
		labelZhiboProxyAddress.setLocation(Xposition + 300, Yposition);

		textFieldZhiboProxyAddress = new JTextField();
		textFieldZhiboProxyAddress.setSize(120, 25);
		textFieldZhiboProxyAddress.setLocation(Xposition + 50 + 300, Yposition);
		textFieldZhiboProxyAddress.setText("110.165.46.121");

		JLabel labelZhiboProxyAccount = new JLabel("端口:");
		labelZhiboProxyAccount.setSize(50, 25);
		labelZhiboProxyAccount.setLocation(Xposition + 300, Yposition + 30);
        
        
        
		textFieldZhiboProxyAccount = new JTextField();
		textFieldZhiboProxyAccount.setSize(120, 25);
		textFieldZhiboProxyAccount.setLocation(Xposition + 50 + 300, Yposition + 30);
		textFieldZhiboProxyAccount.setText("25836");

		
		contain.add(labelZhiboProxyAddress);
		contain.add(textFieldZhiboProxyAddress);
		contain.add(labelZhiboProxyAccount);
		contain.add(textFieldZhiboProxyAccount);
		contain.add(btnZhiboConnect);
		
		contain.add(btnMergeWnd);
		
		
		

		mainFrame.setSize(img.getIconWidth(), img.getIconHeight());

		mainFrame.setVisible(true);
		
		mainFrame.setResizable(false);
		
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "确认退出吗?", "退出程序",
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {					
					System.exit(0);
				}
			}
		});
		
		
	}
	
	
	
	
	
	
	

}
