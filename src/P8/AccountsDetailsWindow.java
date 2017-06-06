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

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Stack;
import java.awt.Button;


import javax.swing.JTabbedPane;  




public class AccountsDetailsWindow extends JFrame  
{  
  
   
	private static final long serialVersionUID = 508685938515369546L;
	
	private  Vector<String[]> detailsData = null;
	
	private  Vector<String[]> hgdetailsData = null;
	
	AccountMgrWindow accountMgrWnd = new AccountMgrWindow();
	
	AccounthgMgrWindow accounthgMgrWnd = new AccounthgMgrWindow();
	
	
	
	
    private Button addAccountBtn = new Button("增加账户");
    
    private Button teamMatchBtn = new Button("队名匹配管理");
    
    private Button addhgAccountBtn = new Button("增加账户");
   
    private JTabbedPane jTabbedpane = new JTabbedPane();// 存放选项卡的组件  
    

    
    JPanel accountDetailsPan = new JPanel(null);
    
    JPanel accountDetailsHGPan = new JPanel(null);
    
    MyTableModel tableMode = new MyTableModel();
    
    MyTableModel1 tableMode1 = new MyTableModel1();
    
    
    EditAccountWindow editAccWnd = new EditAccountWindow();

    EdithgAccountWindow edithgAccWnd = new EdithgAccountWindow();

    

	public AccountsDetailsWindow()  
    {  
        intiComponent();  
        setTitle("设置");
    }
	
	
	public void setAccountMgr(AccountManager acc){
		accountMgrWnd.setAccountMgr(acc);
		editAccWnd.setAccountMgr(acc);
	}
	
	public void sethgAccountMgr(AccounthgManager acc){
		accounthgMgrWnd.setAccountMgr(acc);
		edithgAccWnd.sethgAccountMgr(acc);
	}

	
	
	
	public void setAccountsDetails(Vector<String[]> accountsDetailsVec){
		
		try{
			detailsData = accountsDetailsVec;
			
			tableMode.updateTable();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	
	public void sethgAccountsDetails(Vector<String[]> accountsDetailsVec){
		
		try{
			hgdetailsData = accountsDetailsVec;
			
			tableMode1.updateTable();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	

	
  
    /** 
     * 初始化窗体组件 
     */  
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		//JPanel accountDetailsPan = new JPanel(new GridLayout(10, 10));
		//accountDetailsPan = new JPanel(null);
		
		
		jTabbedpane.addTab("PP账户", null, accountDetailsPan, "accountSet");// 加入第一个页面  
		jTabbedpane.addTab("HG账户", null, accountDetailsHGPan, "hgaccount");// 加入第一个页面  
		
		
		accountDetailsPan.setLocation(0, 0);
		accountDetailsPan.setSize(1220, 630);

        container.add(jTabbedpane);  
        
       // addAccountBtn = new Button("连接");
        addAccountBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accountMgrWnd.setVisible(true);
			}
		});
        
        addAccountBtn.setLocation(50, 50);
        addAccountBtn.setSize(90, 25);
        
        
        accountDetailsPan.add(addAccountBtn);
        
       
        teamMatchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MergeManager.showTeamMatchWnd();
			}
		});
        
        teamMatchBtn.setLocation(150, 50);
        teamMatchBtn.setSize(90, 25);
        
        accountDetailsPan.add(teamMatchBtn);
        
        
/*        accountDetailsPan.add(labeltime);
        accountDetailsPan.add(textFieldtime);
        textFieldjinrichazhi.setEditable(false);*/


	    
		
	    final JTable table = new JTable(tableMode);
	    
	    table.getColumnModel().getColumn(0).setPreferredWidth(120);
	    
	    table.setRowHeight(30);
	    
	    table.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));
	    
	    //table.setColumnModel(columnModel);
	    
	    //tableMode.

	    
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
        tcr.setHorizontalAlignment(JLabel.CENTER);
       // tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
        table.setDefaultRenderer(Object.class, tcr);
	    

/*        JScrollPane scroll = new JScrollPane(table);  
        
        TableCellRenderer tcr = new ColorTableCellRenderer(); 
        
        
        
        
        table.setDefaultRenderer(Color.class,tcr);*/
        
        JScrollPane scroll = new JScrollPane(table);  
        
        scroll.setLocation(0, 100);
        scroll.setSize(1220, 530);
        
       accountDetailsPan.add(scroll);
        
        
        //container.add(scroll, BorderLayout.CENTER);  

        setVisible(false);  
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
        table.addMouseListener(new MouseAdapter(){

              public void mouseClicked(MouseEvent e) {
            	  if(e.getClickCount() == 2){
                	  int row =((JTable)e.getSource()).rowAtPoint(e.getPoint()); //获得行位置 
                	  
                	  //System.out.println("第几行："+row);
                	  
                	  
                	  editAccWnd.setAddressText(detailsData.elementAt(row)[0]);
                	  
                	  editAccWnd.setAccountText(detailsData.elementAt(row)[1]);
                	  
                	  editAccWnd.setpwdText(detailsData.elementAt(row)[2]);
                	  
                	  editAccWnd.setsCodeText(detailsData.elementAt(row)[3]);
                	  
                	  editAccWnd.setVisible(true);

                     // int  col=((JTable)e.getSource()).columnAtPoint(e.getPoint()); //获得列位置 
                      
                      
                      
                      //String cellVal=(String)(tableMode.getValueAt(row,col)); //获得点击单元格数据 
                      //txtboxRow.setText((row+1)+""); txtboxCol.setText((col+1)+""); 

                     // txtboxContent.setText(cellVal); 
            	  }else{
            		  return;
            	  }

              }
        });
        
        
        
        
        
        accountDetailsHGPan.setLocation(0, 0);
        accountDetailsHGPan.setSize(1220, 630);

        
        
       // addAccountBtn = new Button("连接");
        addhgAccountBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accounthgMgrWnd.setVisible(true);
			}
		});
        
        addhgAccountBtn.setLocation(50, 50);
        addhgAccountBtn.setSize(90, 25);
        
        
        accountDetailsHGPan.add(addhgAccountBtn);
        
       
        
/*        accountDetailsPan.add(labeltime);
        accountDetailsPan.add(textFieldtime);
        textFieldjinrichazhi.setEditable(false);*/


	    
		
	    final JTable table1 = new JTable(tableMode1);
	    
	    table1.getColumnModel().getColumn(0).setPreferredWidth(120);
	    
	    table1.setRowHeight(30);
	    
	    table1.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));
	    

        table.setDefaultRenderer(Object.class, tcr);
	    

/*        JScrollPane scroll = new JScrollPane(table);  
        
        TableCellRenderer tcr = new ColorTableCellRenderer(); 
        
        
        
        
        table.setDefaultRenderer(Color.class,tcr);*/
        
        JScrollPane scroll1 = new JScrollPane(table1);  
        
        scroll1.setLocation(0, 100);
        scroll1.setSize(1220, 530);
        
        accountDetailsHGPan.add(scroll1);
        
        
        //container.add(scroll, BorderLayout.CENTER);  

        setVisible(false);  
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
        table1.addMouseListener(new MouseAdapter(){

              public void mouseClicked(MouseEvent e) {
            	  if(e.getClickCount() == 2){
                	  int row =((JTable)e.getSource()).rowAtPoint(e.getPoint()); //获得行位置 
                	  
                	  //System.out.println("第几行："+row);
                	  
                	  
                	  //edithgAccWnd.setAddressText(detailsData.elementAt(row)[0]);
                	  
                	  edithgAccWnd.setAccountText(hgdetailsData.elementAt(row)[0]);
                	  
                	  edithgAccWnd.setpwdText(hgdetailsData.elementAt(row)[1]);
                	  
                	  edithgAccWnd.setsCodeText(hgdetailsData.elementAt(row)[2]);
                	  
                	  edithgAccWnd.setVisible(true);

                     // int  col=((JTable)e.getSource()).columnAtPoint(e.getPoint()); //获得列位置 
                      
                      
                      
                      //String cellVal=(String)(tableMode.getValueAt(row,col)); //获得点击单元格数据 
                      //txtboxRow.setText((row+1)+""); txtboxCol.setText((col+1)+""); 

                     // txtboxContent.setText(cellVal); 
            	  }else{
            		  return;
            	  }

              }
        });
        
        
        
        
        

        
        
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  
        
        

        
        setBounds(100, 100, 1220, 630); 

    }  
    
    

    

    
    
    
  
    private class MyTableModel extends AbstractTableModel  
    {  
        /* 
         * 这里和刚才一样，定义列名和每个数据的值 
         */  
        String[] columnNames =  
        { "网址", "账号", "密码", "安全码", "状态"};  
        

        
        //Object[][] data = new Object[2][5];  
        
        

        
  
        /** 
         * 构造方法，初始化二维数组data对应的数据 
         */  
        public MyTableModel()  
        {  

        }  
  
        // 以下为继承自AbstractTableModle的方法，可以自定义  
        /** 
         * 得到列名 
         */  
        @Override  
        public String getColumnName(int column)  
        {  
            return columnNames[column];  
        }  
          
        /** 
         * 重写方法，得到表格列数 
         */  
        @Override  
        public int getColumnCount()  
        {  
            return columnNames.length;  
        }  
  
        /** 
         * 得到表格行数 
         */  
        @Override  
        public int getRowCount()  
        {  
            return detailsData.size();  
        }  
  
        /** 
         * 得到数据所对应对象 
         */  
        @Override  
        public Object getValueAt(int rowIndex, int columnIndex)  
        {  
            //return data[rowIndex][columnIndex];
        	return detailsData.elementAt(rowIndex)[columnIndex];
        }  
  
        /** 
         * 得到指定列的数据类型 
         */  
        @Override  
        public Class<?> getColumnClass(int columnIndex)  
        {  
            return detailsData.elementAt(0)[columnIndex].getClass();
        }  
  
        /** 
         * 指定设置数据单元是否可编辑.这里设置"姓名","学号"不可编辑 
         */  
        @Override  
        public boolean isCellEditable(int rowIndex, int columnIndex)  
        {  
            return false;
        }  
          
        /** 
         * 如果数据单元为可编辑，则将编辑后的值替换原来的值 
         */  
        @Override  
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)  
        {  
            detailsData.elementAt(rowIndex)[columnIndex] = (String)aValue;  
            /*通知监听器数据单元数据已经改变*/  
            fireTableCellUpdated(rowIndex, columnIndex);  
        }  
        
        public void updateTable(){
        	fireTableDataChanged();
        }
        
  
    }  
    
    
    
    
    
    
    
    private class MyTableModel1 extends AbstractTableModel  
    {  
        /* 
         * 这里和刚才一样，定义列名和每个数据的值 
         */  
        String[] columnNames =  
        {"账号", "密码", "安全码"};  
        

        /** 
         * 构造方法，初始化二维数组data对应的数据 
         */  
        public MyTableModel1()  
        {  

        }  
  
        // 以下为继承自AbstractTableModle的方法，可以自定义  
        /** 
         * 得到列名 
         */  
        @Override  
        public String getColumnName(int column)  
        {  
            return columnNames[column];  
        }  
          
        /** 
         * 重写方法，得到表格列数 
         */  
        @Override  
        public int getColumnCount()  
        {  
            return columnNames.length;  
        }  
  
        /** 
         * 得到表格行数 
         */  
        @Override  
        public int getRowCount()  
        {  
            return hgdetailsData.size();  
        }  
  
        /** 
         * 得到数据所对应对象 
         */  
        @Override  
        public Object getValueAt(int rowIndex, int columnIndex)  
        {  
            //return data[rowIndex][columnIndex];
        	return hgdetailsData.elementAt(rowIndex)[columnIndex];
        }  
  
        /** 
         * 得到指定列的数据类型 
         */  
        @Override  
        public Class<?> getColumnClass(int columnIndex)  
        {  
            return hgdetailsData.elementAt(0)[columnIndex].getClass();
        }  
  
        /** 
         * 指定设置数据单元是否可编辑.这里设置"姓名","学号"不可编辑 
         */  
        @Override  
        public boolean isCellEditable(int rowIndex, int columnIndex)  
        {  
            return false;
        }  
          
        /** 
         * 如果数据单元为可编辑，则将编辑后的值替换原来的值 
         */  
        @Override  
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)  
        {  
            hgdetailsData.elementAt(rowIndex)[columnIndex] = (String)aValue;  
            /*通知监听器数据单元数据已经改变*/  
            fireTableCellUpdated(rowIndex, columnIndex);  
        }  
        
        public void updateTable(){
        	fireTableDataChanged();
        }
        
  
    }  
    
    
    
    
    
    
    
}