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
import java.util.Scanner;

import javax.swing.JOptionPane;

import java.util.Vector;
import javax.swing.ImageIcon;


public class StoneAge {
	
	public static JButton btnLogin;
	
	public static JButton btnAccount;
	
	public static boolean bLogin = false;
	
	
	static AccountsDetailsWindow accountWnd = new AccountsDetailsWindow();
	
	public static AccountManager accMgr = null;
	
	
	public static void main(String[] args) throws Exception {
		
		//P8Http.setLoginParams("http://www.ps38ag.com", "MM260SUB02", "qqqq1111");

		
		accMgr = new AccountManager(accountWnd);
		
		accountWnd.setAccountMgr(accMgr);
		
		accMgr.init();
		
		P8Http.initShowLeagueName();
		
		//P8Http.testpy();

		new StoneAge().launchFrame();

	}
	
	
	public void launchFrame() {

		JFrame mainFrame = new JFrame("实况足球");

/*		JPanel panel = new JPanel();

		panel.setSize(500, 500);

		panel.setLocation(0, 0);
		panel.setLayout(null);*/
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
				
				if(false == false){
					accountWnd.dispose();
					
					btnAccount.setEnabled(false);
					
					
					
					P8Http.clearEventsDetails();
					
					Vector<String[]> accountDetails = accMgr.getAccountDetails();
					
					for(int i = 0; i < accountDetails.size(); i++){
						String[] account = accountDetails.elementAt(i);
						P8Http.setLoginParams(account[0], account[1], account[2], account[3]);
						int loginRes = 0;
						loginRes = P8Http.login();
						
						for(int j = 0; j < 2 && loginRes == 0; j++){
							
							try{
								Thread.currentThread().sleep(5*1000);
								
							}catch(Exception exception){
								
								
								
							}
							
							
							loginRes = P8Http.login();			
						}
						
						if(loginRes == 1){
							
							bLogin = true;
							
							System.out.println("会员  " + account[1] + " 抓取成功");
							
							if(account[0].contains("p88agent")){
								P8Http.getTotalP8Bet();
							}else{
								P8Http.getTotalPS38Bet();
							}
							
							
						}
						else{
							System.out.println("会员  " + account[1] + " 抓取失败");
						}
						
					}
				}else{//test
					P8Http.clearEventsDetails();
					
					System.out.println(P8Http.strCookies);
					
					Vector<String[]> accountDetails = accMgr.getAccountDetails();
					
					for(int i = 0; i < accountDetails.size(); i++){
						String[] account = accountDetails.elementAt(i);
						if(account[0].contains("p88agent")){
							if(P8Http.getTotalP8Bet()){
								System.out.println("会员  " + account[1] + " 抓取成功");
							}else{
								System.out.println("会员  " + account[1] + " 抓取失败");
							}
						}else{
							if(P8Http.getTotalPS38Bet()){
								System.out.println("会员  " + account[1] + " 抓取成功");
							}else{
								System.out.println("会员  " + account[1] + " 抓取失败");
							}
							
						}
						}
				}
				
				P8Http.sortEventDetails();

				
				P8Http.updateEventsDetailsData();
				
				P8Http.showEventsDeatilsTable();
				
				


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
