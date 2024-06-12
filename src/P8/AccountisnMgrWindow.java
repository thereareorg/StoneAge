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





public class AccountisnMgrWindow extends JFrame{
	private static final long serialVersionUID = 556685938515369549L;
	
    //private JLabel labelAddress = new JLabel("网址:");
    //private JTextField textFieldAddress = new JTextField(15);  
    
/*    String str1[] = {"http://www.p88agent.com", "http://www.ps38ag.com"};
    
    private JComboBox jcb = new JComboBox(str1); */
	
    private JLabel labelAccount = new JLabel("账号:");
    private JTextField textFieldAccount = new JTextField(15);  
    
    private JLabel labelPassword = new JLabel("密码:");
    private JTextField textFieldPassword = new JTextField(15);  
    

    
    private JLabel labelSecurityCode = new JLabel("安全码:");
    private JTextField textFieldSecurityCode = new JTextField(15);  
    


	
	
    private Button addAccountBtn = new Button("确定");
    private Button cancleBtn = new Button("取消");
    
    private AccountisnManager accMgr = null;
    
    
    
	public AccountisnMgrWindow()  
    {  
		setTitle("增加账户");  
		
		//accMgr = acc;
		
        intiComponent();  
        
    }  
	
	public void setAccountMgr(AccountisnManager acc){
		accMgr = acc;
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
        
        addAccountBtn.addActionListener(new ActionListener() {  
      	  
            @Override  
            public void actionPerformed(ActionEvent e) {  
                //String address = jcb.getSelectedItem().toString();
                String account = textFieldAccount.getText();
                String password = textFieldPassword.getText();
                String secrityCode = textFieldSecurityCode.getText();
                
                
                Vector<String[]> details = accMgr.getAccountDetails();
                
                for(int i = 0; i < details.size(); i ++){
                	if(account.contains(details.elementAt(i)[1])){
                		JOptionPane.showMessageDialog(null,"账号已存在");
                		return;
                	}                		
                }
                
                ISNhttp isntmp = new ISNhttp();
                
                
                isntmp.setLoginParams("https://www.isn999.com/", account, password, secrityCode);
                
                //isntmp.setLoginParams("https://www.isn999.com/", "XXHKTL67SUBKK", "qqww2233", "112233");
                
				boolean loginRes = false;
				loginRes = isntmp.login();
				
				
				
				for(int j = 0; j < 2 && loginRes == false; j++){
					
					try{
						Thread.currentThread().sleep(5 * 1000);
						
					}catch(Exception exception){

					}
					
					
					loginRes = isntmp.login();			
				}      
				
				if(loginRes == true){
					
					String[] accountdetails = {account, password, secrityCode};					
					System.out.println("save to file");
            		
            		
					if(accMgr.saveTofile(accountdetails)){
						JOptionPane.showMessageDialog(null,"账号添加成功");
						dispose();  
					}
				}else{
					JOptionPane.showMessageDialog(null,"账号无法添加");
				}
                
            }  
        });  
        
        
        cancleBtn.addActionListener(new ActionListener() {  
        	  
            @Override  
            public void actionPerformed(ActionEvent e) {  
                // TODO Auto-generated method stub  
                // System.exit(0);  
                dispose();  
            }  
        });  
        
        

        
        
        


        
        panelNorth.add(labelAccount);
        panelNorth.add(textFieldAccount);

        panelNorth.add(labelPassword);
        panelNorth.add(textFieldPassword);
        
        panelNorth.add(labelSecurityCode);
        panelNorth.add(textFieldSecurityCode);
        
        panelNorth.add(addAccountBtn);
        
        panelNorth.add(cancleBtn);

        setVisible(false);  
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  

        setBounds(100, 100, 630, 400); 

    }  
    
    
    
    
    
    
}
