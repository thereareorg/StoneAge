package P8;




import java.awt.Container; 

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel; 

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



public class StoneAge {
	
	public static JButton btnLogin;
	
	public static JButton btnAccount;
	
	public static boolean bLogin = false;
	
	public static GrabEventsThread grabThead;
	
	public static StoneAge sa;
	
	
	static AccountsDetailsWindow accountWnd = new AccountsDetailsWindow();
	
	public static AccountManager accMgr = null;
	
	
	
	
	public PrintStream psFile = null;
	public PrintStream psConsole = null;
	
	
	
	public static void main(String[] args) throws Exception {
		
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); 
		

		
		
		accMgr = new AccountManager(accountWnd);
		
		accountWnd.setAccountMgr(accMgr);
		
		accMgr.init();
		
		P8Http.initShowLeagueName();

		sa = new StoneAge();
		
		sa.launchFrame();
	}
	
	
	public static void setSleepTime(int min){
		grabThead.setSleepTime(min);
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
		
		
		btnLogin = new JButton("显示注单");
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
		btnLogin.setLocation(300, 200);
		
		
		contain.add(btnLogin);
		
		
		btnAccount = new JButton("账户详情");
		btnAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				accountWnd.setVisible(true);
				

			}
		});
		
		
		btnAccount.setSize(100, 25);
		btnAccount.setLocation(300, 240);
		
		
		contain.add(btnAccount);
		

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
