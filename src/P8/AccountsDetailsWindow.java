package P8;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;  
 
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;  
  
import java.awt.Color;




















import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JPanel;  
import javax.swing.JScrollPane;  
import javax.swing.JTable;  
import javax.swing.JTextField;  
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel; 
import javax.swing.table.AbstractTableModel; 





import javax.swing.table.TableCellRenderer;

import java.util.Date;      

import javax.swing.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

import AccountPane.HGAccountPane;
import AccountPane.ISNAccountPane;
import AccountPane.PPAccountPane;
import HG.HGMergeManager;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Stack;
import java.awt.Button;







import javax.swing.JTabbedPane;  




public class AccountsDetailsWindow extends JFrame  
{  
  
   
	private static final long serialVersionUID = 508685938515369546L;

	private JTabbedPane jTabbedpane = new JTabbedPane();// 存放选项卡的组件  
	PPAccountPane ppAccountPane = new PPAccountPane();	
	HGAccountPane hgAccountPane = new HGAccountPane();
	ISNAccountPane  isnAccountPane = new ISNAccountPane();

	public AccountsDetailsWindow()  
    {  
        intiComponent();  
        setTitle("设置");
    }
	
	
	public void setAccountMgr(AccountManager acc){
		ppAccountPane.setAccountMgr(acc);
	}
	
	public void setAccountsDetails(Vector<String[]> accountsDetailsVec){

		ppAccountPane.setAccountsDetails(accountsDetailsVec);

	}
	
	
	
	
	public void sethgAccountMgr(AccounthgManager acc){
		hgAccountPane.sethgAccountMgr(acc);
	}


	public void sethgAccountsDetails(Vector<String[]> accountsDetailsVec){
		hgAccountPane.sethgAccountsDetails(accountsDetailsVec);
		
	}
	
	
	
	public void setisnAccountMgr(AccountisnManager acc){
		isnAccountPane.setisnAccountMgr(acc);
	}


	public void setisnAccountsDetails(Vector<String[]> accountsDetailsVec){
		isnAccountPane.setisnAccountsDetails(accountsDetailsVec);
		
	}
	

  
    /** 
     * 初始化窗体组件 
     */  
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		

		jTabbedpane.add(ppAccountPane.getaccountDetailsPan(), "PP账户");// 加入第一个页面  
		jTabbedpane.add(hgAccountPane.getaccountDetailsHGPan(), "HG账户");// 加入第一个页面 
		
		jTabbedpane.add(isnAccountPane.getaccountDetailsHGPan(), "ISN账户");// 加入第一个页面 

		container.add(jTabbedpane);  

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  

        setBounds(100, 100, 1220, 630); 

    }  
    

    
}