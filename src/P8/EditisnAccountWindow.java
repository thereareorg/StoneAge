package P8;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.table.TableCellRenderer;
import javax.swing.*;



public class EditisnAccountWindow extends JFrame{
	private static final long serialVersionUID = 508685935515369549L;
	
/*    private JLabel labelAddress = new JLabel("网址:");
    //private JTextField textFieldAddress = new JTextField(15);  
    
    String str1[] = {"http://www.p88agent.com", "http://www.ps38ag.com"};
    
    private JComboBox jcb = new JComboBox(str1); */
	
    private JLabel labelAccount = new JLabel("账号:");
    private JTextField textFieldAccount = new JTextField(15);  
    
    private JLabel labelPassword = new JLabel("密码:");
    private JTextField textFieldPassword = new JTextField(15);  
    

    
    private JLabel labelSecurityCode = new JLabel("安全码:");
    private JTextField textFieldSecurityCode = new JTextField(15);  
    


	
	
    private Button deleteAccountBtn = new Button("删除账户");
    private Button cancleBtn = new Button("取消");
    
    private AccountisnManager accisnMgr = null;
    
    String oldAdd = "";
    String oldAcc = "";
    String oldPwd = "";
    String oldScode = "";
    
    
    
	public EditisnAccountWindow()  
    {  
		setTitle("删除账户");  
		
		//accMgr = acc;
		
        intiComponent();  
        
    }  
	
	public void setisnAccountMgr(AccountisnManager acc){
		accisnMgr = acc;
	}
	
/*    public void setAddressText(String address){
    	
    	String aa = jcb.getItemAt(0).toString();
    	
    	oldAdd = address;
    	
    	if(address.contains(aa)){
    		jcb.setSelectedIndex(0);
    	}else{
    		jcb.setSelectedIndex(1);
    	}
    	
    	
    }*/
	

    public void setAccountText(String account){
    	oldAcc = account;
    	textFieldAccount.setText(account);
    }
    
    public void setpwdText(String pwd){
    	oldPwd = pwd;
    	textFieldPassword.setText(pwd);
    }
	
    
    public void setsCodeText(String scode){
    	oldScode = scode;
    	textFieldSecurityCode.setText(scode);
    }
	
    
    /** 
     * 初始化窗体组件 
     */  
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel(new GridLayout(5, 4));

        container.add(panelNorth, BorderLayout.NORTH);  
        
        deleteAccountBtn.addActionListener(new ActionListener() {  
      	  
            @Override  
            public void actionPerformed(ActionEvent e) {  
            	String del = oldAcc;
                
            	accisnMgr.deleteAccount(del);
            	
            	JOptionPane.showMessageDialog(null,"删除账号成功");
            	
                dispose();  
                
            }  
        });  
        
        
        cancleBtn.addActionListener(new ActionListener() {  
        	  
            @Override  
            public void actionPerformed(ActionEvent e) {  
            	dispose();  

            }  
        });  
        
        

        
        

        panelNorth.add(labelAccount);
        panelNorth.add(textFieldAccount);

        panelNorth.add(labelPassword);
        panelNorth.add(textFieldPassword);
        
        panelNorth.add(labelSecurityCode);
        panelNorth.add(textFieldSecurityCode);
        
        panelNorth.add(deleteAccountBtn);
        
        panelNorth.add(cancleBtn);

        setVisible(false);  
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  

        setBounds(100, 100, 630, 400); 

    }  
    
    
    
    
    
    
}
