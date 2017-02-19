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
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;  
  
import java.awt.Color;



















import javax.swing.JComboBox;
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








public class PreviousDataWindow extends JFrame  
{  
  
   
	private static final long serialVersionUID = 508685938515369544L;
	
	private  Vector<String[]> detailsData = null;
	
	private Vector<Integer> hightlightRows = new Vector<Integer>();
	
	
    private JLabel labelHighlightNum = new JLabel("金额:");
    private JTextField textFieldHighlightNum = new JTextField(15);  
    
    private JLabel labelInterval = new JLabel("间隔时间:");
    
    String str1[] = {"1", "2","3","4","5"};
    
    private JComboBox jcb = new JComboBox(str1); 
    
    
    private JLabel labelGrabStat= new JLabel("状态:");
    private JTextField textFieldGrabStat = new JTextField(15);  
    
	
    Double higlightBigNum = 1000000.0;

    
    
/*    private JLabel labeltime = new JLabel("距封盘:");
    private JTextField textFieldtime = new JTextField(15);  
    
    private AtomicLong remainTime = new AtomicLong(0);*/
    
    
    MyTableModel tableMode = new MyTableModel();
    
    
    JTable table = null;

    
    
    
	

	public PreviousDataWindow()  
    {  
		//setTitle("投注北京赛车详情");  
		
        intiComponent();  
        
    }  
	
	
	public void setStateText(String txt){
		textFieldGrabStat.setText(txt);
	}
	
	public void hightlightBigNumrows(){
		
		if(hightlightRows.size() != 0){
			hightlightRows.clear();
		}
		
		for(int i = 0; i< detailsData.size(); i++){
			String leagueName = detailsData.elementAt(i)[TYPEINDEX.LEAGUENAME.ordinal()];
			
			if(P8Http.isInShowLeagueName(leagueName) || true){
				double betAmt1 = Double.parseDouble(detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()]);
				double betAmt2 = Double.parseDouble(detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()]);
				double betAmt3 = Double.parseDouble(detailsData.elementAt(i)[TYPEINDEX.PERIOD1HOME.ordinal()]);
				double betAmt4 = Double.parseDouble(detailsData.elementAt(i)[TYPEINDEX.PERIOD1HOME.ordinal()]);
				
				if(Math.abs(betAmt1) > higlightBigNum || Math.abs(betAmt2) > higlightBigNum|| 
						Math.abs(betAmt3) > higlightBigNum || Math.abs(betAmt4) > higlightBigNum){
					//
					
					hightlightRows.add(i);
					
				}
				
				
			}
			
			setOneRowBackgroundColor(table, 0, new Color(255, 100, 100));
		}
	}

	
	
	
	public void updateEventsDetails(Vector<String[]> eventDetailsVec){
		
		try{
			
			
			

			
			detailsData = (Vector<String[]>)eventDetailsVec.clone();
			
			
			
			hightlightBigNumrows();
			
			tableMode.updateTable();
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	public  void addData(Object[] a){
		
/*		try{
			detailsData.push(a);
			
	    	Comparator ct = new CompareStr();
	    	
	    	Collections.sort(detailsData, ct);
			
			tableMode.updateTable();
		}catch(Exception e){
			e.printStackTrace();
		}*/
		

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
        
        jcb.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
                String content = jcb.getSelectedItem().toString();
                
                StoneAge.setSleepTime(Integer.parseInt(content));
			}
        });
        
        
        textFieldHighlightNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldHighlightNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	higlightBigNum = Double.parseDouble(value);
                    	hightlightBigNumrows();
                    	
                    	tableMode.updateTable();
                    }
                    
                }  
                // System.out.println("Text " + value);  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });
        
        textFieldGrabStat.setEditable(false);
        
        
        panelNorth.add(labelInterval);
        panelNorth.add(jcb);

        
        panelNorth.add(labelHighlightNum);
        panelNorth.add(textFieldHighlightNum);
        
        panelNorth.add(labelGrabStat);
        panelNorth.add(textFieldGrabStat);
        
        
/*        panelNorth.add(labeltime);
        panelNorth.add(textFieldtime);
        textFieldjinrichazhi.setEditable(false);*/


	    
		
	    table = new JTable(tableMode);

        JScrollPane scroll = new JScrollPane(table);  
        
        
	    table.getColumnModel().getColumn(2).setPreferredWidth(240);
	    
	    table.setRowHeight(30);
	    
	    table.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));
	    
	    
	    
	    //table.setColumnModel(columnModel);
	    
	    //tableMode.

	    
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
        tcr.setHorizontalAlignment(JLabel.CENTER);
       // tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
        table.setDefaultRenderer(Object.class, tcr);
        
        
        
        container.add(scroll, BorderLayout.CENTER);  

        setVisible(false);  
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  
        
        

        
        setBounds(100, 100, 1600, 800); 

    }
    
    public boolean isInhighlightrows(int row){
    	
    	for(int i = 0; i < hightlightRows.size(); i ++){
    		if(hightlightRows.elementAt(i) == row){
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    
    
    public void setOneRowBackgroundColor(JTable table, int rowIndex1,  
            Color color1) {
    	
    	final int rowIndex = rowIndex1;
    	
    	final Color color = color1;
    	
        try {  
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {  
  
                public Component getTableCellRendererComponent(JTable table,  
                        Object value, boolean isSelected, boolean hasFocus,  
                        int row, int column) {  
                    if (isInhighlightrows(row)) {  
                        setBackground(color);  
                        setForeground(Color.BLACK);  
                    }else{  
                        setBackground(Color.WHITE);  
                        setForeground(Color.BLACK);  
                    }  
  
                    return super.getTableCellRendererComponent(table, value,  
                            isSelected, hasFocus, row, column);  
                }  
            };  
            int columnCount = table.getColumnCount();  
            for (int i = 0; i < columnCount; i++) {  
                table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);  
            }  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
    }  
    
    

    

    
    
    
  
    private class MyTableModel extends AbstractTableModel  
    {  
        /* 
         * 这里和刚才一样，定义列名和每个数据的值 
         */  
        String[] columnNames =  
        { "联赛", "时间", "球队", "全场让球", "全场大小", "上半让球", "上半大小"};  
        

        
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
        	return detailsData.elementAt(rowIndex)[columnIndex+1];
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
