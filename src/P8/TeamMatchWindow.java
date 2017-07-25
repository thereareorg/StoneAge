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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;  
  
import java.awt.Color;

























import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JMenuItem;
import javax.swing.JPanel;  
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;  
import javax.swing.JTable;  
import javax.swing.JTextField;  
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel; 
import javax.swing.table.AbstractTableModel; 


import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.util.Date;      

import javax.swing.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

import HG.HGMergeManager;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Stack;






public class TeamMatchWindow extends JFrame  
{  
  
   
	private static final long serialVersionUID = 666685938515369544L;
	
	private  Vector<String[]> originalDetailsData = new Vector<String[]>();
	
	//private Map<String,String> originalDetailsData = new HashMap<String,String>();
	
	private  Vector<String[]> detailsData = null;
	
	//private Map<String,String> detailsData = null;
    
    
    private JPopupMenu m_popupMenu;
    
    private JMenuItem chooseMenItem; 
    
    private JMenuItem mergeMenItem; 
    
    int focusedRowIndex = -1;
    
    private String name = "";
   
    
/*    private JLabel labeltime = new JLabel("距封盘:");
    private JTextField textFieldtime = new JTextField(15);  
    
    private AtomicLong remainTime = new AtomicLong(0);*/
    
    
    MyTableModel tableMode = new MyTableModel();
    
    
    JTable table = null;

    
    
    
	

	public TeamMatchWindow(String n)  
    {  
		//setTitle("智博队名匹配");  
		
        intiComponent();  
        
        name = n;
        
    }  
	
	
	
	private void createPopupMenu() {  
        m_popupMenu = new JPopupMenu();  
          
        chooseMenItem = new JMenuItem();  
        chooseMenItem.setText("删除匹配");  
        

        
        

        
        
        chooseMenItem.addActionListener(new java.awt.event.ActionListener() {  
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
                //该操作需要做的事  
            	
            	try{
            		
	            	if(detailsData != null){
	            		if(name.equals("zhibo")){
		            		String p8name = detailsData.elementAt(focusedRowIndex)[0];
		            		MergeManager.deleteateammatch(p8name);
		            	}else if(name.equals("hg")){
		            		String p8name = detailsData.elementAt(focusedRowIndex)[0];
		            		HGMergeManager.deleteateammatch(p8name);
		            	}else if(name.equals("score")){
		            		String p8name = detailsData.elementAt(focusedRowIndex)[0];
		            		ScoreMergeManager.deleteateammatch(p8name);
		            	}
	            	}
	            	
	            	
	            	

	    	        
            	}catch(Exception e){
            		e.printStackTrace();
            	}

            }  
        });  
        m_popupMenu.add(chooseMenItem);  

    }  
	
	
	
	//鼠标右键点击事件  
	   private void mouseRightButtonClick(java.awt.event.MouseEvent evt) {  
	       //判断是否为鼠标的BUTTON3按钮，BUTTON3为鼠标右键  
	       if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {  
	           //通过点击位置找到点击为表格中的行  
	           focusedRowIndex = table.rowAtPoint(evt.getPoint());  
	           if (focusedRowIndex == -1) {  
	               return;  
	           }  
	           //将表格所选项设为当前右键点击的行  
	           table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);  
	           
	           System.out.println(focusedRowIndex);
	           //弹出菜单  
	           
	           
	           
	           
	           m_popupMenu.show(table, evt.getX(), evt.getY());  
	       }  
	  
	   }  
	
	
	
	
	private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {  
		  
	       mouseRightButtonClick(evt);  
	}  
	
	
	
	



	
	
	
	public void updateEventsDetails(Map<String,String> teamNames){
		
		try{
			
			
			if(originalDetailsData.size() != 0){
				originalDetailsData.clear();
			}

			
			
			for (String key : teamNames.keySet()) {  
				  
			    String value = (String)teamNames.get(key);
			    
			    String[] tmp = {key, value};
			    
			    originalDetailsData.add(tmp);
			  
			}  
			

			
			
			
			updateShowItem();
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	

	
	
	public void updateShowItem(){
		
		detailsData = originalDetailsData;
		
		tableMode.updateTable();
		
	}

	
	

	
	
	
  
    /** 
     * 初始化窗体组件 
     */  
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel(new GridLayout(6, 4));

        container.add(panelNorth, BorderLayout.NORTH);  
        

        
        createPopupMenu();
        

        
        
        
        
/*        panelNorth.add(labeltime);
        panelNorth.add(textFieldtime);
        textFieldjinrichazhi.setEditable(false);*/


	    
		
	    table = new JTable(tableMode);

        JScrollPane scroll = new JScrollPane(table); 
        
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {  
            public void mouseClicked(java.awt.event.MouseEvent evt) {  
                jTable1MouseClicked(evt);  
            }  
        });  
        
        
	    //table.getColumnModel().getColumn(2).setPreferredWidth(240);
	    
	    table.setRowHeight(30);
	    
	    table.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));
	    
	    
	    


	    
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
        tcr.setHorizontalAlignment(JLabel.CENTER);
       // tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
        table.setDefaultRenderer(Object.class, tcr);
        
        
        
        container.add(scroll, BorderLayout.CENTER);  

        setVisible(false);  
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  
        
        
        setResizable(false);
        
        setBounds(100, 100, 400, 800); 

    }
    

    
    
    

    
    

    

    
    
    
  
    public class MyTableModel extends AbstractTableModel  
    {  
        /* 
         * 这里和刚才一样，定义列名和每个数据的值 
         */  
        String[] columnNames =  
        { "P8", name};  
        

        
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
	        if(null == detailsData){
	    		return 0;
	    	}
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
    
    
}