package P8;




import java.awt.Container; 

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
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JOptionPane;

import java.util.Vector;

import javax.swing.ImageIcon;

import team.gl.nio.cln.ZhiboClient;
import team.gl.nio.svr.NettyServer;

public class StoneAge {
	
	
	public static JButton btnMergepData;
	
	public static JButton btnZhibopData;
	
	public static JButton btnpData;
	
	public static JButton btnLogin;
	
	public static JButton btnAccount;
	
	public static boolean bLogin = false;
	
	public static GrabEventsThread grabThead;
	
	public static StoneAge sa;
		
	public static JButton btnZhiboConnect;
	
	public static JButton btnMergeWnd;
	
	
	public static boolean zhiboConnected = false;
	
	static ZhiboThread zhiboThread = null;
	
	
	
	public static boolean showMergeWnd = false;
	public static boolean showP8 = false;
	public static boolean showZhibo = false;

	
	public JTextField textFieldZhiboProxyAddress;
	
	public JTextField textFieldZhiboProxyAccount;
	
	
	
	
	static AccountsDetailsWindow accountWnd = new AccountsDetailsWindow();
	
	public static AccountManager accMgr = null;
	
	static SeverThread serverThread = null;
	
	
	public PrintStream psFile = null;
	public PrintStream psConsole = null;
	
	
	
	public static void main(String[] args) throws Exception {
		
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); 
		

		System.out.println("start");
		
		
		accMgr = new AccountManager(accountWnd);
		
		accountWnd.setAccountMgr(accMgr);
		
		accMgr.init();
		
		MergeManager.init();
		
		P8Http.initShowLeagueName();
		
		ZhiboManager.initShowLeagueName();

		sa = new StoneAge();
		
		serverThread = new SeverThread();
		
		serverThread.start();
		
		sa.launchFrame();
	}
	
	
	public static void setSleepTime(int sec){
		grabThead.setSleepTime(sec);
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
        
        
        
        int Xposition = 100;
        int Yposition = 140;
        
        
		btnMergepData = new JButton("合并历史注单");
		btnMergepData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MergeManager.updatepDataDetails();
				MergeManager.showpDataWnd();
			}
		});
		
		
		btnMergepData.setSize(100, 25);
		btnMergepData.setLocation(Xposition, Yposition - 80);
		
		
		contain.add(btnMergepData);
        
		btnZhibopData = new JButton("LL历史注单");
		btnZhibopData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ZhiboManager.updatepDataDetails();
				ZhiboManager.showpDataWnd();
			}
		});
		
		
		btnZhibopData.setSize(100, 25);
		btnZhibopData.setLocation(Xposition, Yposition - 40);
		
		
		contain.add(btnZhibopData);
        
        
        
		btnpData = new JButton("历史注单");
		btnpData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				P8Http.updatepDataDetails();
				P8Http.showpDataWnd();
			}
		});
		
		
		btnpData.setSize(100, 25);
		btnpData.setLocation(Xposition, Yposition);
		
		
		contain.add(btnpData);
		
		
		btnLogin = new JButton("即时注单");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					accountWnd.dispose();
					
					btnAccount.setEnabled(false);
					
					btnLogin.setEnabled(false);
					
					grabThead = new GrabEventsThread(sa);
					
					grabThead.start();
			}
		});
		
		
		btnLogin.setSize(100, 25);
		btnLogin.setLocation(Xposition, Yposition + 40);
		
		
		contain.add(btnLogin);
		
		
		btnAccount = new JButton("账户详情");
		btnAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				accountWnd.setVisible(true);
				

			}
		});
		
		
		btnAccount.setSize(100, 25);
		btnAccount.setLocation(Xposition, Yposition + 80);

		
		contain.add(btnAccount);
		
		
		
		
		
		
		
		JLabel labelZhiboProxyAddress = new JLabel("网址:");
		labelZhiboProxyAddress.setSize(50, 25);
		labelZhiboProxyAddress.setLocation(Xposition + 200, Yposition);

		textFieldZhiboProxyAddress = new JTextField();
		textFieldZhiboProxyAddress.setSize(100, 25);
		textFieldZhiboProxyAddress.setLocation(Xposition + 50 + 200, Yposition);
		textFieldZhiboProxyAddress.setText("103.26.127.50");

		JLabel labelZhiboProxyAccount = new JLabel("端口:");
		labelZhiboProxyAccount.setSize(50, 25);
		labelZhiboProxyAccount.setLocation(Xposition + 200, Yposition + 30);
        
        
        
		textFieldZhiboProxyAccount = new JTextField();
		textFieldZhiboProxyAccount.setSize(100, 25);
		textFieldZhiboProxyAccount.setLocation(Xposition + 50 + 200, Yposition + 30);
		textFieldZhiboProxyAccount.setText("25836");
        
        
		btnZhiboConnect = new JButton("显示智博");
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
					
					System.out.println("连接成功");
					
					showZhibo = true;

					
				}catch(Exception ex){
					ex.printStackTrace();
				}
				


			}
		});

		btnZhiboConnect.setSize(90, 25);
		btnZhiboConnect.setLocation(Xposition + 200, Yposition + 60);
		
		
		
		
		btnMergeWnd = new JButton("显示合并");
		
		btnMergeWnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
					
					if(bLogin == false){
						JOptionPane.showMessageDialog(null,"请先连接平博");
						return;
					}
					
					
					if(zhiboConnected == false){
						JOptionPane.showMessageDialog(null,"请先连接智博");
						return;
					}
					

					showMergeWnd = true;
					
			        MergeManager.clearMergeData();
			        MergeManager.constructMergeRes();
			        MergeManager.updateEventsDetails();
			        MergeManager.showMergeDetailsWnd(StoneAge.showMergeWnd);

					
				}catch(Exception ex){
					ex.printStackTrace();
				}
				


			}
		});
		
		btnMergeWnd.setSize(90, 25);
		btnMergeWnd.setLocation(Xposition + 200, Yposition + 120);
		
		
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
