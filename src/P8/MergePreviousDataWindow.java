package P8;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;



public class MergePreviousDataWindow extends PreviousDataWindow  
{  
  
   
	private static final long serialVersionUID = 538685938515369544L;
	
	private  Vector<String[]> detailsData = null;
	
	private Vector<String[]> originalDetailsData = null;
	
	private Vector<Integer> hightlightRows = new Vector<Integer>();
	
	
    private JLabel labelHighlightNum = new JLabel("金额:");
    private JTextField textFieldHighlightNum = new JTextField(15);  
    
    private JLabel labelInterval = new JLabel("日期选择:");
    
    String str1[] = {"1", "2","3","4","5"};
    
    private JComboBox jcb = new JComboBox(str1); 
    
    DateChooser mp = new DateChooser("yyyy-MM-dd", this);
    
    
    private JLabel labelHideNum = new JLabel("隐藏金额:");
    private JTextField textFieldHideNum = new JTextField(15); 
    
    private JCheckBox onlyShow5Big = new JCheckBox("只看五大联赛,欧冠");
    private JCheckBox onlyShowInplay = new JCheckBox("只看滚动盘");
    private JCheckBox onlyShowNotInplay = new JCheckBox("只看单式盘");
    
    private boolean bonlyShow5Big = false;
    private boolean bonlyShowInplay = false;
    private boolean bonlyShowNotInplay = false;
    
    private JLabel labelGrabStat= new JLabel("状态:");
    private JTextField textFieldGrabStat = new JTextField(15);  
    
	
    Double higlightBigNum = 1000000.0;
    
    Double hideNum = 0.0;

    
    
/*    private JLabel labeltime = new JLabel("距封盘:");
    private JTextField textFieldtime = new JTextField(15);  
    
    private AtomicLong remainTime = new AtomicLong(0);*/
    
    
    MyTableModel tableMode = new MyTableModel();
    
    
    JTable table = null;


    
    
	

	public MergePreviousDataWindow()  
    {  
		setTitle("合并历史注单");  
		
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
				double betAmt1 =0.0;
				double betAmt2 = 0.0;
				double betAmt3 =0.0;
				double betAmt4 = 0.0;
				
				String str1 = detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()];
				
				if(str1.contains("=")){
					String[] tmp = str1.split("=");
					betAmt1 = Double.parseDouble(tmp[1]);
				}else{
					betAmt1 = Double.parseDouble(str1);
				}
				
				
				String str2 = detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()];
				
				if(str2.contains("=")){
					String[] tmp = str2.split("=");
					betAmt2 = Double.parseDouble(tmp[1]);
				}else{
					betAmt2 = Double.parseDouble(str2);
				}
				
				
				String str3 = detailsData.elementAt(i)[TYPEINDEX.PERIOD1HOME.ordinal()];
				
				if(str3.contains("=")){
					String[] tmp = str3.split("=");
					betAmt3 = Double.parseDouble(tmp[1]);
				}else{
					betAmt3 = Double.parseDouble(str3);
				}
				
				String str4 = detailsData.elementAt(i)[TYPEINDEX.PERIOD1OVER.ordinal()];
				
				if(str4.contains("=")){
					String[] tmp = str4.split("=");
					betAmt4 = Double.parseDouble(tmp[1]);
				}else{
					betAmt4 = Double.parseDouble(str4);
				}
				
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
			
			
			

			
			originalDetailsData = (Vector<String[]>)eventDetailsVec.clone();
			
			
			
			updateShowItem();
			
			
			
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
	
	
	public void updateShowItem(){
	
	
		try{
			
			String date = mp.getChooseDate();
			
			Vector<String[]> Vectmp = new Vector<String[]>();
			
			
			String startTimeStr = date + " " + "13:00";
			
			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
			
			SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			
			java.util.Date startTimeDate = dfMin.parse(startTimeStr);
			
			Calendar startTime = Calendar.getInstance();  
			startTime.setTime(startTimeDate);
			
			
			long currentTimeL = System.currentTimeMillis();
			
			String LocaltodayStr = dfDay.format(currentTimeL);
			
			
			String MinStr = dfMin.format(currentTimeL);
			
			java.util.Date Mintime = dfMin.parse(MinStr);
			
			for(int i = 0; i < originalDetailsData.size(); i++){
				String timeStr = originalDetailsData.elementAt(i)[TYPEINDEX.TIME.ordinal()];
				java.util.Date timeDate = dfMin.parse(timeStr);
				
				Calendar time = Calendar.getInstance();  
				time.setTime(timeDate);
				
				
				if(time.getTimeInMillis() >= startTime.getTimeInMillis() && time.getTimeInMillis() < startTime.getTimeInMillis() + 24*60*60*1000){
					Vectmp.add(originalDetailsData.elementAt(i));
				}
							
			}
			
			//
			
			if(Vectmp.size() == 0){
				detailsData = (Vector<String[]>)Vectmp.clone();
				hightlightBigNumrows();
				
				tableMode.updateTable();
				return;
			}
				
			
			Vector<String[]> DetailsDatatmp = new Vector<String[]>();
			
			//只显示走地盘
			if(bonlyShowInplay == true){
				for(int i = 0; i < Vectmp.size(); i++){
					if(Vectmp.elementAt(i)[TYPEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
						DetailsDatatmp.add(Vectmp.elementAt(i));
					}
				}
			}
			
			//只显示单式盘
			if(bonlyShowNotInplay == true){
				for(int i = 0; i < Vectmp.size(); i++){
					if(!Vectmp.elementAt(i)[TYPEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
						DetailsDatatmp.add(Vectmp.elementAt(i));
					}
				}
			}
			
			Vector<String[]> DetailsDatatmp1 = new Vector<String[]>();
			
			
			if(DetailsDatatmp.size() == 0){
				DetailsDatatmp = (Vector<String[]>)Vectmp.clone();
			}
			
			
			//只看五大联赛
			if(bonlyShow5Big == true){
				for(int i = 0; i < DetailsDatatmp.size(); i++){
					if(P8Http.isInShowLeagueName(DetailsDatatmp.elementAt(i)[TYPEINDEX.LEAGUENAME.ordinal()])){
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
				String leagueName = DetailsDatatmp1.elementAt(i)[TYPEINDEX.LEAGUENAME.ordinal()];
				
				if(P8Http.isInShowLeagueName(leagueName) || true){
					double betAmt1 =0.0;
					double betp81 = 0.0;
					double betzhibo1 = 0.0;
					
					double betAmt2 = 0.0;
					double betp82 = 0.0;
					double betzhibo2 = 0.0;
					
					double betAmt3 =0.0;
					double betp83 = 0.0;
					double betzhibo3 = 0.0;
					
					double betAmt4 = 0.0;
					double betp84 = 0.0;
					double betzhibo4 = 0.0;
					
					String str1 = DetailsDatatmp1.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()];
					
					if(str1.contains("=")){
						String[] tmp = str1.split("=");
						betAmt1 = Double.parseDouble(tmp[1]);
						
						String[] tmp1 = tmp[0].split("\\+");
						tmp1[0] = tmp1[0].replace("(", "");
						tmp1[0] = tmp1[0].replace(")", "");
						
					
						tmp1[1] = tmp1[1].replace("(", "");
						tmp1[1] = tmp1[1].replace(")", "");
						
						betp81 = Double.parseDouble(tmp1[0]);
						betzhibo1 = Double.parseDouble(tmp1[1]);
						
						
					}else{
						betAmt1 = Double.parseDouble(str1);
					}
					
					
					String str2 = DetailsDatatmp1.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()];
					
					if(str2.contains("=")){
						String[] tmp = str2.split("=");
						betAmt2 = Double.parseDouble(tmp[1]);
						
						String[] tmp1 = tmp[0].split("\\+");
						tmp1[0] = tmp1[0].replace("(", "");
						tmp1[0] = tmp1[0].replace(")", "");
						
					
						tmp1[1] = tmp1[1].replace("(", "");
						tmp1[1] = tmp1[1].replace(")", "");
						
						betp82 = Double.parseDouble(tmp1[0]);
						betzhibo2 = Double.parseDouble(tmp1[1]);
						
					}else{
						betAmt2 = Double.parseDouble(str2);
					}
					
					
					String str3 = DetailsDatatmp1.elementAt(i)[TYPEINDEX.PERIOD1HOME.ordinal()];
					
					if(str3.contains("=")){
						String[] tmp = str3.split("=");
						betAmt3 = Double.parseDouble(tmp[1]);
						
						String[] tmp1 = tmp[0].split("\\+");
						tmp1[0] = tmp1[0].replace("(", "");
						tmp1[0] = tmp1[0].replace(")", "");
						
					
						tmp1[1] = tmp1[1].replace("(", "");
						tmp1[1] = tmp1[1].replace(")", "");
						
						betp83 = Double.parseDouble(tmp1[0]);
						betzhibo3 = Double.parseDouble(tmp1[1]);
						
					}else{
						betAmt3 = Double.parseDouble(str3);
					}
					
					String str4 = DetailsDatatmp1.elementAt(i)[TYPEINDEX.PERIOD1OVER.ordinal()];
					
					if(str4.contains("=")){
						String[] tmp = str4.split("=");
						betAmt4 = Double.parseDouble(tmp[1]);
						
						String[] tmp1 = tmp[0].split("\\+");
						tmp1[0] = tmp1[0].replace("(", "");
						tmp1[0] = tmp1[0].replace(")", "");
						
					
						tmp1[1] = tmp1[1].replace("(", "");
						tmp1[1] = tmp1[1].replace(")", "");
						
						betp84 = Double.parseDouble(tmp1[0]);
						betzhibo4 = Double.parseDouble(tmp1[1]);
						
					}else{
						betAmt4 = Double.parseDouble(str4);
					}
					
					
					
	/*					if(Math.abs(betAmt1) > hideNum || Math.abs(betAmt2) > hideNum|| 
							Math.abs(betAmt3) > hideNum || Math.abs(betAmt4) > hideNum){
						//
						
						DetailsDatatmp2.add(DetailsDatatmp1.elementAt(i));
						
					}*/
					
					if( (Math.abs(betp81) > hideNum && Math.abs(betzhibo1) > hideNum) || 
							(Math.abs(betp82) > hideNum && Math.abs(betzhibo2) > hideNum)|| 
							(Math.abs(betp83) > hideNum && Math.abs(betzhibo3) > hideNum) || 
							(Math.abs(betp84) > hideNum && Math.abs(betzhibo4) > hideNum)){
						//
						
						DetailsDatatmp2.add(DetailsDatatmp1.elementAt(i));
						
					}

					
					
				}
				
				
			}
			
			detailsData = (Vector<String[]>)DetailsDatatmp2.clone();
			
			
			hightlightBigNumrows();
			
			tableMode.updateTable();
			
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
		
		JPanel panelNorth = new JPanel(new GridLayout(3, 4));

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
                    	higlightBigNum = Double.parseDouble(value);
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
                    	hideNum = Double.parseDouble(value);
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
        
        
        panelNorth.add(labelInterval);
        panelNorth.add(mp);

        
        panelNorth.add(labelHighlightNum);
        panelNorth.add(textFieldHighlightNum);
        
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
    
    

    

    
    
    
  
    public class MyTableModel extends AbstractTableModel  
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
