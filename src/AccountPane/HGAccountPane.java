package AccountPane;

import java.awt.Button;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import P8.AccounthgManager;
import P8.AccounthgMgrWindow;
import P8.EdithgAccountWindow;


public class HGAccountPane {
	
	
    EdithgAccountWindow edithgAccWnd = new EdithgAccountWindow();

	
	private  Vector<String[]> hgdetailsData = null;
	
	
	
	AccounthgMgrWindow accounthgMgrWnd = new AccounthgMgrWindow();
	
    JPanel accountDetailsHGPan = new JPanel(null);
    
    
    
    MyTableModel1 tableMode1 = new MyTableModel1();
	
	
	public HGAccountPane(){
		initaccountDetailsHGPan();
	}
	
	
	public JPanel getaccountDetailsHGPan(){
		return accountDetailsHGPan;
	}
	
	
	public void sethgAccountMgr(AccounthgManager acc){
		accounthgMgrWnd.setAccountMgr(acc);
		edithgAccWnd.sethgAccountMgr(acc);
	}
	
	public void sethgAccountsDetails(Vector<String[]> accountsDetailsVec){
		
		try{
			hgdetailsData = accountsDetailsVec;
			
			tableMode1.updateTable();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	
    private void initaccountDetailsHGPan(){
    	
    	Button addhgAccountBtn = new Button("增加账户");
    	
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
        
		
	    final JTable table1 = new JTable(tableMode1);
	    
	    table1.getColumnModel().getColumn(0).setPreferredWidth(120);
	    
	    table1.setRowHeight(30);
	    
	    table1.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));
	    

        
	    

/*        JScrollPane scroll = new JScrollPane(table);  
        
        TableCellRenderer tcr = new ColorTableCellRenderer(); 
        
        
        
        
        table.setDefaultRenderer(Color.class,tcr);*/
        
        JScrollPane scroll1 = new JScrollPane(table1);  
        
        scroll1.setLocation(0, 100);
        scroll1.setSize(1220, 530);
        
        accountDetailsHGPan.add(scroll1);
        
        
        //container.add(scroll, BorderLayout.CENTER);  

        
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
