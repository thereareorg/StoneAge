package HG;

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

import P8.Common;
import P8.StoneAge;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Stack;





class ColorTableCellRenderer extends JLabel implements TableCellRenderer

{

private static final long serialVersionUID = 1L;

//定义构造器

public ColorTableCellRenderer ()

{

//设置标签为不透明状态

this.setOpaque(true);

//设置标签的文本对齐方式为居中

this.setHorizontalAlignment(JLabel.CENTER);

}

//实现获取呈现控件的getTableCellRendererComponent方法

public Component getTableCellRendererComponent(JTable table,Object value,

           boolean isSelected,boolean hasFocus,int row,int column)

{           

//获取要呈现的颜色

Color c=(Color)value;

//根据参数value设置背景色

this.setBackground(c);



return this;

}

 }   



public class HGEventsDetailsWindow extends JFrame  
{  
  
   
	private static final long serialVersionUID = 508685938515369544L;
	
	private  Vector<String[]> originalDetailsData = new Vector<String[]>();
	
	private  Vector<String[]> detailsData = null;
	
	private Vector<Integer> hightlightRows = new Vector<Integer>();
	
	
    private JLabel labelHighlightNum = new JLabel("让球高亮金额:");
    private JTextField textFieldHighlightNum = new JTextField(15);  
    
    private JLabel labelp0oHighlightNum = new JLabel("大小球高亮金额:");
    private JTextField textFieldp0oHighlightNum = new JTextField(15);  
    
    private JLabel labelInterval = new JLabel("刷新时间:");
    
    String str1[] = {"30", "60","90","120","180"};
    
    private JComboBox jcb = new JComboBox(str1); 
    
    
    private JLabel labelHideNum = new JLabel("让球隐藏金额:");
    private JTextField textFieldHideNum = new JTextField(15); 
    
    private JLabel labelp0oHideNum = new JLabel("大小球隐藏金额:");
    private JTextField textFieldp0oHideNum = new JTextField(15); 
    
/*    private JLabel labelInplayHideNum = new JLabel("走地让球隐藏金额:");
    private JTextField textFieldInplayHideNum = new JTextField(15); 
    
    private JLabel labelp0oInplayHideNum = new JLabel("走地大小球隐藏金额:");
    private JTextField textFieldp0oInplayHideNum = new JTextField(15); */
    
    private JCheckBox onlyShow5Big = new JCheckBox("只看五大联赛,欧冠");
    private JCheckBox onlyShowInplay = new JCheckBox("只看滚动盘");
    private JCheckBox onlyShowNotInplay = new JCheckBox("只看单式盘");
    
    private boolean bonlyShow5Big = false;
    private boolean bonlyShowInplay = false;
    private boolean bonlyShowNotInplay = false;
    
    private JLabel labelGrabStat= new JLabel("状态:");
    private JTextField textFieldGrabStat = new JTextField(15);  
    
    
    private JPopupMenu m_popupMenu;
    
    private JMenuItem chooseMenItem; 
    
    private JMenuItem mergeMenItem; 
    
    private int focusedRowIndex = -1;
    
    private int selectedOrMerge = 0;
	
    Double p0hhiglightBigNum = 1000000.0;
    
    Double p0hhideNum = 0.0;
    
    Double p0ohiglightBigNum = 1000000.0;
    
    Double p0ohideNum = 0.0;

    Double p0hInplayhideNum = 1.0;
    
    Double p0oInplayhideNum = 1.0;
   
    
/*    private JLabel labeltime = new JLabel("距封盘:");
    private JTextField textFieldtime = new JTextField(15);  
    
    private AtomicLong remainTime = new AtomicLong(0);*/
    
    
    MyTableModel tableMode = new MyTableModel();
    
    
    JTable table = null;

    
    
    
	

	public HGEventsDetailsWindow()  
    {  
		setTitle("HG注单");  
		
        intiComponent();  
        

        
    }  
	
	private void createPopupMenu() {  
        m_popupMenu = new JPopupMenu();  
          
        chooseMenItem = new JMenuItem();  
        chooseMenItem.setText("选择");  
        
        
        mergeMenItem = new JMenuItem();
        mergeMenItem.setText("合并");
        
        
        mergeMenItem.addActionListener(new java.awt.event.ActionListener() {  
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
                //该操作需要做的事  
            	
            	try{
            		
	            	if(focusedRowIndex != -1 && focusedRowIndex < detailsData.size()){
	            		

	            			
	            			HGMergeManager.hgSelectedRow = detailsData.elementAt(focusedRowIndex);
	            			System.out.println(Arrays.toString(HGMergeManager.hgSelectedRow));

	            			HGMergeManager.showMergeWnd(true);
	            			
	            			HGMergeManager.hgSelectedRow = null;
	            			HGMergeManager.p8SelectedRow = null;

	            	}

	    	        
            	}catch(Exception e){
            		e.printStackTrace();
            	}
            	

            	

            	
            }  
        });  
        
        chooseMenItem.addActionListener(new java.awt.event.ActionListener() {  
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
                //该操作需要做的事  
            	
            	try{
            		
	            	if(focusedRowIndex != -1 && focusedRowIndex < detailsData.size()){

	            			HGMergeManager.hgSelectedRow = detailsData.elementAt(focusedRowIndex);
	            			System.out.println(Arrays.toString(HGMergeManager.hgSelectedRow));

	            	}

	    	        
            	}catch(Exception e){
            		e.printStackTrace();
            	}
            	

            	

            	
            }  
        });  
        m_popupMenu.add(chooseMenItem);  
        m_popupMenu.add(mergeMenItem);  
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
	           
	           if(HGMergeManager.p8SelectedRow == null){
	        	   //chooseMenItem.setText("选择");
	        	   
	        	   mergeMenItem.setEnabled(false);
	        	   
	        	   //selectedOrMerge = 0;
	        	   
	           }else{
	        	   mergeMenItem.setEnabled(true);
	           }
	           
	           
	           m_popupMenu.show(table, evt.getX(), evt.getY());  
	       }  
	  
	   }  
	
	
	
	
	private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {  
		  
	       mouseRightButtonClick(evt);  
	}  
	
	
	
	
	public void setStateText(String txt){
		textFieldGrabStat.setText(txt);
	}
	
	public void setStateColor(Color cr){
		textFieldGrabStat.setBackground(cr);
	}


	
	
	
	public void updateEventsDetails(Vector<String[]> eventDetailsVec){
		
		try{
			
			
			if(originalDetailsData.size() != 0){
				originalDetailsData.clear();
			}

			
			for(int i = 0; i< eventDetailsVec.size(); i++){
				originalDetailsData.add(eventDetailsVec.elementAt(i).clone());
			}
			

			
			updateShowItem();
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}

	
	
	public void updateShowItem(){
		
		Vector<String[]> DetailsDatatmp = new Vector<String[]>();
		
		//只显示走地盘
		if(bonlyShowInplay == true){
			for(int i = 0; i < originalDetailsData.size(); i++){
				if(originalDetailsData.elementAt(i)[HGINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
					DetailsDatatmp.add(originalDetailsData.elementAt(i));
				}
			}
		}
		
		//只显示单式盘
		if(bonlyShowNotInplay == true){
			for(int i = 0; i < originalDetailsData.size(); i++){
				if(!originalDetailsData.elementAt(i)[HGINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
					DetailsDatatmp.add(originalDetailsData.elementAt(i));
				}
			}
		}
		
		Vector<String[]> DetailsDatatmp1 = new Vector<String[]>();
		
		
		if(DetailsDatatmp.size() == 0){
			DetailsDatatmp = (Vector<String[]>)originalDetailsData.clone();
		}
		
		
		//只看五大联赛
		if(bonlyShow5Big == true){
			for(int i = 0; i < DetailsDatatmp.size(); i++){
				if(HGhttp.isInShowLeagueName(DetailsDatatmp.elementAt(i)[HGINDEX.LEAGUENAME.ordinal()])){
					DetailsDatatmp1.add(DetailsDatatmp.elementAt(i));
				}
			}
		}
		
		Vector<String[]> DetailsDatatmp2 = new Vector<String[]>();
		
		if(DetailsDatatmp1.size() == 0){

			DetailsDatatmp1 = (Vector<String[]>)DetailsDatatmp.clone();
			
		}
		
		//隐藏数额
		for(int i = 0; i< DetailsDatatmp1.size(); i++){
			String eventName = DetailsDatatmp1.elementAt(i)[HGINDEX.EVENTNAMNE.ordinal()];

			
			
			double betAmt1 = 0.0;
			double betAmt2 = 0.0;

			
			String bet1Str = DetailsDatatmp1.elementAt(i)[HGINDEX.PERIOD0HOME.ordinal()];
			String bet2Str = DetailsDatatmp1.elementAt(i)[HGINDEX.PERIOD0OVER.ordinal()];

			if(bet1Str.contains("=")){
				String[] tmp = bet1Str.split("=");
				betAmt1 = Double.parseDouble(tmp[1]);
			}else{
				if(bet1Str.contains("g")){
					bet1Str = bet1Str.replace("g", "");
				}
				betAmt1 = Double.parseDouble(bet1Str);
			}
			
			if(bet2Str.contains("=")){
				String[] tmp = bet2Str.split("=");
				betAmt2 = Double.parseDouble(tmp[1]);
			}else{
				if(bet2Str.contains("g")){
					bet2Str = bet2Str.replace("g", "");
				}
				betAmt2 = Double.parseDouble(bet2Str);
			}
			
			
			if(Math.abs(betAmt1) > p0hhideNum || Math.abs(betAmt2) > p0ohideNum){
				DetailsDatatmp2.add(DetailsDatatmp1.elementAt(i).clone());
			}
			

			
			
		}
		
		detailsData = (Vector<String[]>)DetailsDatatmp2.clone();


		
		tableMode.updateTable();
		
	}

	
	

	
	
	
  
    /** 
     * 初始化窗体组件 
     */  
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel(new GridLayout(4, 4));

        container.add(panelNorth, BorderLayout.NORTH);  
        
        jcb.setSelectedIndex(1);
        
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
                    	p0hhiglightBigNum = Double.parseDouble(value);
                    	updateShowItem();
                    }
                    
                }  
                // System.out.println("Text " + value);  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });
        
        
        textFieldHideNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldHideNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0hhideNum = Double.parseDouble(value);
                    	updateShowItem();
                    	
                    	//tableMode.updateTable();
                    }
                    
                }  
                // System.out.println("Text " + value);  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });
        
        textFieldp0oHighlightNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldp0oHighlightNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0ohiglightBigNum = Double.parseDouble(value);
                    	updateShowItem();
                    }
                    
                }  
                // System.out.println("Text " + value);  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });
        
        
        textFieldp0oHideNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldp0oHideNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0ohideNum = Double.parseDouble(value);
                    	updateShowItem();
                    	
                    	//tableMode.updateTable();
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
        
        onlyShow5Big.setSelected(false);
        
        onlyShow5Big.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					bonlyShow5Big = false;
				}else{
					bonlyShow5Big = true;
				}
				
				updateShowItem();
			}
        });
        
        onlyShowInplay.setSelected(false);
        
        onlyShowInplay.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					bonlyShowInplay = false;

				}else{
					bonlyShowInplay = true;
					bonlyShowNotInplay = false;
					onlyShowNotInplay.setSelected(false);
				}
				
				updateShowItem();
			}
        });
        
        onlyShowNotInplay.setSelected(false);
        
        onlyShowNotInplay.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					bonlyShowNotInplay = false;
				}else{
					bonlyShowNotInplay = true;
					bonlyShowInplay = false;
					onlyShowInplay.setSelected(false);
				}
				
				updateShowItem();
			}
        });
        
        createPopupMenu();
        
        
        
        panelNorth.add(labelInterval);
        panelNorth.add(jcb);

        panelNorth.add(labelp0oHighlightNum);
        panelNorth.add(textFieldp0oHighlightNum);
        
        panelNorth.add(labelHighlightNum);
        panelNorth.add(textFieldHighlightNum);
        
        panelNorth.add(labelp0oHideNum);
        panelNorth.add(textFieldp0oHideNum);

        
        panelNorth.add(labelHideNum);
        panelNorth.add(textFieldHideNum);
        
        

        

        panelNorth.add(onlyShow5Big);
        panelNorth.add(onlyShowInplay);
        panelNorth.add(onlyShowNotInplay);

        
        
        panelNorth.add(labelGrabStat);
        panelNorth.add(textFieldGrabStat);
        
        
        
        
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
        

        
        
	    table.getColumnModel().getColumn(2).setPreferredWidth(240);
	    
	    table.setRowHeight(30);
	    
	    table.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));
	    
	    
	    
	    //table.setColumnModel(columnModel);
	    
	    //tableMode.
	    
	    
	    
	    //设置列单元格渲染模式  开始
        TableColumn p0hColumn = table.getColumn("全场让球");   
        TableColumn p0oColumn = table.getColumn("全场大小");   


        //绘制月薪列的字体颜色   

        DefaultTableCellRenderer p0hRender = new DefaultTableCellRenderer() {   

            public void setValue(Object value) { //重写setValue方法，从而可以动态设置列单元字体颜色   

               
            	String str = value.toString();
            	
				Double betAmt = 0.0;
				
				String showStr = str.replace("g", "");
				showStr = showStr.replace("b", "");
				
				if(str.contains("=")){
					String[] tmp = str.split("=");
					betAmt = Double.parseDouble(tmp[1]);
				}else{
	
					betAmt = Double.parseDouble(showStr);
				}
				
				
				if(Math.abs(betAmt) > p0hhiglightBigNum){
					setForeground(Color.red);
					
				}else{
					setForeground(Color.black);
					
				}
				

				
				setText((value == null) ? "" : str);

				Double hideNum = 0.0;
				
				
				hideNum = p0hhideNum;
				
				
				if(Math.abs(betAmt) < hideNum){
					setForeground(Color.black);
					setText("0");
				}
				
				
					


            }   

        };   
        
        DefaultTableCellRenderer p0oRender = new DefaultTableCellRenderer() {   

            public void setValue(Object value) { //重写setValue方法，从而可以动态设置列单元字体颜色   
            	String str = value.toString();
            	
				Double betAmt = 0.0;
				
				if(str.contains("=")){
					String[] tmp = str.split("=");
					betAmt = Double.parseDouble(tmp[1]);
				}else{
					betAmt = Double.parseDouble(str.replace("g", ""));
				}
				
				
				if(Math.abs(betAmt) > p0ohiglightBigNum){
					setForeground(Color.red);
					
				}else{
					setForeground(Color.black);
					
				}
				
				setText((value == null) ? "" : str);
				
				Double hideNum = 0.0;
				
				
				hideNum = p0ohideNum;
				

				if(Math.abs(betAmt) < hideNum){
					setForeground(Color.black);
					setText("0");
				}

				

            }   

        };   

        p0hColumn.setCellRenderer(p0hRender);   
        p0oColumn.setCellRenderer(p0oRender);   

      //设置列单元格渲染模式  结束

	    
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
    
    

    

    
    
    
  
    public class MyTableModel extends AbstractTableModel  
    {  
        /* 
         * 这里和刚才一样，定义列名和每个数据的值 
         */  
        String[] columnNames =  
        { "联赛", "时间", "球队", "全场让球", "全场大小"};  
        

        
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