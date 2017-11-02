package HGclient;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.python.icu.util.Calendar;

import HGclient.GamePreviousDetailsWindow.MyTableModel;
import P8.Common;
import P8.DateChooser;





class gamedetailsTimeCompare implements Comparator //ʵ��Comparator�������Լ��ıȽϷ���
{

	public int compare(Object o1, Object o2) {
		
		try{
			
			String[] g1 = (String[])o1;
			String[] g2 = (String[])o2;

			
			
			String time1 = (String)g1[2];
			String time2 = (String)g2[2];
			
			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
			
			SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			
			long currentTimeL = System.currentTimeMillis();
			
		//	String LocaltodayStr = dfDay.format(currentTimeL);
			

			
			
			java.util.Date Mintime1 = dfMin.parse(time1);
			java.util.Date Mintime2 = dfMin.parse(time2);
			
			Calendar calTime1 = Calendar.getInstance();  
			calTime1.setTime(Mintime1);
			
			Calendar calTime2 = Calendar.getInstance();  
			calTime2.setTime(Mintime2);
			
			
			if(calTime1.getTimeInMillis() > calTime2.getTimeInMillis())//����Ƚ��ǽ���,����-1�ĳ�1��������.
			{
			   return 1;
			}
			else if(calTime1.getTimeInMillis() == calTime2.getTimeInMillis())
			{
			   return 0;
			}else{
				return -1;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
		

	}
}







class preoddcompare implements Comparator //ʵ��Comparator�������Լ��ıȽϷ���
{

	public int compare(Object o1, Object o2) {
		
		try{

			
			String[] g1 = (String[])o1;
			String[] g2 = (String[])o2;

			
			int odd1 = 0;
			int odd2 = 0;
			
			if(GamePreviousDetailsWindow.compareOdd == 0){
				
				if(g1[6].equals("")){
					odd1 = -1;
				}else{
					odd1 = Math.abs(Integer.parseInt(g1[6]));
				}
				
				if(g2[6].equals("")){
					odd2 = -1;
				}else{
					odd2 = Math.abs(Integer.parseInt(g2[6]));
				}
				

			}
			
			
			
			if(GamePreviousDetailsWindow.compareOdd == 1){

				
				if(g1[9].equals("")){
					odd1 = -1;
				}else{
					odd1 = Math.abs(Integer.parseInt(g1[9]));
				}
				
				if(g2[9].equals("")){
					odd2 = -1;
				}else{
					odd2 = Math.abs(Integer.parseInt(g2[9]));
				}
				
			}
			
			
			
			
			
			if(odd1 < odd2)//����Ƚ��ǽ���,����-1�ĳ�1��������.
			{
			   return 1;
			}
			else if(odd1 == odd2)
			{
			   return 0;
			}else{
				return -1;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
		

	}
};






public class GamePreviousDetailsWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5555555555555555556L;


	public Vector<GameDetails> gameDetailsVec = new Vector<GameDetails>(); 
	public Vector<String[]> showitemVec = new Vector<String[]>();
	
    MyTableModel tableMode = new MyTableModel();
    
    
    JTable table = null;
    
    private JLabel comparetimelb = new JLabel("与开赛前比较时间:");
    private JTextField comparetimetxt = new JTextField(15); 
    
    private int comparemins = 60;
    
    
    private JLabel labelInterval = new JLabel("日期选择:");
    
    DateChooser mp = new DateChooser("GamePreviousDetailsWindow");
    
    
    private JLabel sortlb = new JLabel("排序:");
    private JCheckBox pankouhcb = new JCheckBox("盘口主队赔率");
    //private JCheckBox pankouccb = new JCheckBox("盘口客队赔率");
    private JCheckBox ouhcb = new JCheckBox("大小球主队赔率");
    //private JCheckBox ouccb = new JCheckBox("大小球客队赔率");
    
    private JCheckBox timecb = new JCheckBox("时间");
    
    
    
    
    public static int compareOdd = 0;
    
    
    
	public GamePreviousDetailsWindow()  
    {  
		setTitle("水位变动历史");  
		
        intiComponent();  
        
        
        
    } 
	
	public String getmpdate(){
		return mp.getChooseDate();
	}
	
	
	public void updateGameDetailsVec(Vector<GameDetails> gamesvec){
		
		try{
			
			if(gameDetailsVec.size() != 0){
				gameDetailsVec.clear();
			}
			
			for(int i = 0; i< gamesvec.size(); i++){
				GameDetails gameitem = new GameDetails();
				gameitem.eventid = gamesvec.elementAt(i).eventid;
				gameitem.datetime = gamesvec.elementAt(i).datetime;
				gameitem.league = gamesvec.elementAt(i).league;
				gameitem.teamh = gamesvec.elementAt(i).teamh;
				gameitem.teamc = gamesvec.elementAt(i).teamc;
				gameitem.gameresult = gamesvec.elementAt(i).gameresult;
				
				
				
				
				for(int j = 0; j < gamesvec.elementAt(i).getodds().size(); j++){
					gameitem.addodds(gamesvec.elementAt(i).odds.elementAt(j).clone());
				}
				

				gameDetailsVec.add(gameitem);

			}
			
			sortgamedetails();
			
			updateShowItem();
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	
	@SuppressWarnings("unchecked")
	public void sortgamedetails(){
		
		try{
			Long currentTime = System.currentTimeMillis();
			
			Calendar eventtime = Calendar.getInstance();
			
			SimpleDateFormat dfmin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			SimpleDateFormat dfsec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for(int i = 0; i < gameDetailsVec.size(); i++){
				GameDetails tmp = gameDetailsVec.elementAt(i);
				
				java.util.Date startTimeDate = dfmin.parse(tmp.datetime);
				
				eventtime.setTime(startTimeDate);
				

				
				
				
				int latestOdd = -1;
				
				if(currentTime > eventtime.getTimeInMillis()){
					
					String[] latestodds = null;
					
					Long searchTime = eventtime.getTimeInMillis() - comparemins * 60 * 1000;
					
					
					
					
					//盘口
					for(int j = tmp.getodds().size() - 1; j >=0 ; j--){
						
						String[] odds = tmp.getodds().elementAt(j);
						
						
						
						if(odds[HGODDSINDEX.PRIOTITY.ordinal()].equals("1") && odds[HGODDSINDEX.TYPE.ordinal()].equals("danshi") &&latestOdd == -1){
							latestOdd = j;
							
							latestodds = tmp.getodds().elementAt(latestOdd);
							
						}
						
						Calendar oddtime = Calendar.getInstance();
						java.util.Date oddtimedate = dfmin.parse(odds[HGODDSINDEX.TIME.ordinal()]);
						oddtime.setTime(oddtimedate);
						
						if(searchTime > oddtime.getTimeInMillis() &&  odds[HGODDSINDEX.TYPE.ordinal()].equals("danshi")){
							
							if(latestOdd == -1){
								System.out.println(gameDetailsVec.elementAt(i).teamh + "vs" + gameDetailsVec.elementAt(i).teamc);
								break;
							}
							
							if(!odds[HGODDSINDEX.PANKOU.ordinal()].equals(tmp.getodds().elementAt(latestOdd)[HGODDSINDEX.PANKOU.ordinal()])){
								continue;
							}
							
							
							if(tmp.getodds().elementAt(j).length < 9){
								System.out.println(Arrays.toString(tmp.getodds().elementAt(j)));
								System.out.println("fuck fuck");
								
								continue;
							}
							
							double odd1;
							double odd2;
							
							int res;
							
							
							if(!tmp.getodds().elementAt(latestOdd)[HGODDSINDEX.HODD.ordinal()].equals("none") && !tmp.getodds().elementAt(j)[HGODDSINDEX.HODD.ordinal()].equals("none")){
								odd1 = Double.parseDouble(tmp.getodds().elementAt(latestOdd)[HGODDSINDEX.HODD.ordinal()]);
								odd2 = Double.parseDouble(tmp.getodds().elementAt(j)[HGODDSINDEX.HODD.ordinal()]);
								
								res = (int)(odd1*100) - (int)(odd2*100);
								
								gameDetailsVec.elementAt(i).pankouh = res;
							}


							break;
							
						}
						
						
					}
					//盘口结束
					
					
					
					//大小球
					
						
					for(int j = tmp.getodds().size() - 1; j >=0 ; j--){
						
						String[] odds = tmp.getodds().elementAt(j);
						
						if(latestOdd == -1){
							System.out.println(gameDetailsVec.elementAt(i).teamh + "vs" + gameDetailsVec.elementAt(i).teamc);
							break;
						}
						
						
						
						Calendar oddtime = Calendar.getInstance();
						java.util.Date oddtimedate = dfmin.parse(odds[HGODDSINDEX.TIME.ordinal()]);
						oddtime.setTime(oddtimedate);
						
						if(searchTime > oddtime.getTimeInMillis() &&  odds[HGODDSINDEX.TYPE.ordinal()].equals("danshi")){
							
							
							
							if(!odds[HGODDSINDEX.O.ordinal()].equals(tmp.getodds().elementAt(latestOdd)[HGODDSINDEX.O.ordinal()])){
								continue;
							}
							
							
							if(tmp.getodds().elementAt(j).length < 9){
								System.out.println(Arrays.toString(tmp.getodds().elementAt(j)));
								System.out.println("fuck fuck");
								
								continue;
							}
							
							double odd1;
							double odd2;
							
							int res;
							
							

							
							if(!tmp.getodds().elementAt(latestOdd)[HGODDSINDEX.OODD.ordinal()].equals("none") && !tmp.getodds().elementAt(j)[HGODDSINDEX.OODD.ordinal()].equals("none")){
								odd1 = Double.parseDouble(tmp.getodds().elementAt(latestOdd)[HGODDSINDEX.OODD.ordinal()]);
								odd2 = Double.parseDouble(tmp.getodds().elementAt(j)[HGODDSINDEX.OODD.ordinal()]);

								res = (int)(odd1*100) - (int)(odd2*100);
								
								gameDetailsVec.elementAt(i).ouh = res;
							}
							

							
							break;
							
						}
						
						
					}
					//大小球结束
					
					
					
					

					
				}
				
			}
			
			

			
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	
	
	public void updateShowItem(){
		
		
		try{
			
			if(showitemVec.size() != 0){
				showitemVec.clear();
			}
			
			
			
			for(int i = 0; i < gameDetailsVec.size(); i++){
				GameDetails onegame = gameDetailsVec.elementAt(i);
				
        		int latestIndex = -1;
        		
        		for(int j =onegame.odds.size()-1; j >=0 ; j--){
        			if(onegame.odds.elementAt(j)[HGODDSINDEX.PRIOTITY.ordinal()].equals("1") && onegame.odds.elementAt(j)[HGODDSINDEX.TYPE.ordinal()].equals("danshi")){
        				latestIndex = j;
        				break;
        			}
        		}
        		
        		if(latestIndex == -1){
        			continue;
        		}

        			
    			String pankoustr = onegame.odds.elementAt(latestIndex)[HGODDSINDEX.PANKOU.ordinal()];
    			
    			if(pankoustr.contains("C")){
    				pankoustr = pankoustr.replace("C", "受让 ");
    			}else{
    				pankoustr = pankoustr.replace("H", "");
    			}

        			
    			String hoddstr = onegame.odds.elementAt(latestIndex)[HGODDSINDEX.HODD.ordinal()];
    			
    			String hdvaluestr = "";
    			if(onegame.pankouh != -1000){
    				hdvaluestr = Integer.toString(onegame.pankouh);
    			}
        			

        		
    			String dxqstr = onegame.odds.elementAt(latestIndex)[HGODDSINDEX.O.ordinal()];
    			dxqstr = dxqstr.replace("O", "");
        		
        		
        		
        		
        			
    			String ooddstr = onegame.odds.elementAt(latestIndex)[HGODDSINDEX.OODD.ordinal()];
    			
    			String odvaluestr = "";
    			if(onegame.ouh != -1000){
    				odvaluestr = Integer.toString(onegame.ouh);
    			}

        		
        		
				
				
				
				String[] item = {Integer.toString(i), onegame.league, onegame.datetime, onegame.teamh + "-" + onegame.teamc, 
									pankoustr, hoddstr, hdvaluestr, dxqstr, ooddstr, odvaluestr, onegame.gameresult};
				
				showitemVec.add(item);
			}
			
			
			if(showitemVec.size() != 0 && compareOdd != 2){
				@SuppressWarnings({ "unchecked", "rawtypes" })
				Comparator co = new preoddcompare(); 
	        	

	        		
	        	Collections.sort(showitemVec, co);
	        		
	        		
	        	
			}else if(showitemVec.size() != 0 && compareOdd == 2){
				Comparator ct = new gamedetailsTimeCompare();
				Collections.sort(showitemVec, ct);
			}
			
			for(int i = 0; i < showitemVec.size(); i++){
				showitemVec.elementAt(i)[0] = Integer.toString(i+1);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
		tableMode.updateTable();
		
		setOneRowBackgroundColor();
	}
	
	
	
	
	
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel(new GridLayout(1, 9));

        container.add(panelNorth, BorderLayout.NORTH);  
        

        panelNorth.add(labelInterval);
        panelNorth.add(mp);
        
        
        panelNorth.add(comparetimelb);
        panelNorth.add(comparetimetxt);
        

        

        
        
        panelNorth.add(sortlb);
        panelNorth.add(pankouhcb);
        
        
        
        panelNorth.add(ouhcb);
        
        panelNorth.add(timecb);


        

        
        pankouhcb.setSelected(true);
        
        pankouhcb.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					//pankouhcb.setSelected(true);
					/*compareOdd = 0;
					
					pankouccb.setSelected(false);
					ouhcb.setSelected(false);
					ouccb.setSelected(false);*/
					
				}else{
					
					//pankouhcb.setSelected(true);
					compareOdd = 0;
					
					
					ouhcb.setSelected(false);
					timecb.setSelected(false);
					
					sortgamedetails();
					
					updateShowItem();
					
				}
				

			}
        });

        
 
        

        

        
        
        ouhcb.setSelected(false);
        
        ouhcb.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					//ouhcb.setSelected(true);
					/*compareOdd = 2;
					
					pankouhcb.setSelected(false);
					pankouccb.setSelected(false);
					ouccb.setSelected(false);*/
					
				}else{
					//ouhcb.setSelected(true);
					compareOdd = 1;
					
					pankouhcb.setSelected(false);
					
					timecb.setSelected(false);
					
					sortgamedetails();
					
					updateShowItem();
				}
				

			}
        });
        
        
        timecb.setSelected(false);
        
        timecb.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					//ouhcb.setSelected(true);
					/*compareOdd = 2;
					
					pankouhcb.setSelected(false);
					pankouccb.setSelected(false);
					ouccb.setSelected(false);*/
					
				}else{
					//ouhcb.setSelected(true);
					compareOdd = 2;
					
					pankouhcb.setSelected(false);
					ouhcb.setSelected(false);
					
					sortgamedetails();
					
					updateShowItem();
				}
				

			}
        });

        
 

        
        
        comparetimetxt.setText(Integer.toString(comparemins));

        comparetimetxt.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = comparetimetxt.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	comparemins = Integer.parseInt(value);
                    	
                    	sortgamedetails();
                    	
                    	updateShowItem();
                    }
                    
                }  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });

	    
		
	    table = new JTable(tableMode);

        JScrollPane scroll = new JScrollPane(table); 
        
        

        
        
        table.getColumnModel().getColumn(0).setPreferredWidth(20);//序号
        table.getColumnModel().getColumn(1).setPreferredWidth(140);//联赛
	    table.getColumnModel().getColumn(2).setPreferredWidth(100);//时间
	    table.getColumnModel().getColumn(3).setPreferredWidth(160);//球队
	    table.getColumnModel().getColumn(4).setPreferredWidth(50);//盘口
	    table.getColumnModel().getColumn(7).setPreferredWidth(50);//大小球
	    
	    table.setRowHeight(30);
	    
	    table.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));
	    
	    
	    

	    
	    
	    


	    
/*        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
        tcr.setHorizontalAlignment(JLabel.CENTER);
       // tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
        table.setDefaultRenderer(Object.class, tcr);*/
        
        
        
        container.add(scroll, BorderLayout.CENTER);  

        setVisible(false);  
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  
        
        

        
        setBounds(100, 100, 1600, 800); 

    }
 
	
    
    
    public void setOneRowBackgroundColor() {
    	

    	
        try {  
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
            	
            	
  
                public Component getTableCellRendererComponent(JTable table,  
                        Object value, boolean isSelected, boolean hasFocus,  
                        int row, int column) {  
                    if (row%2 == 0) {  
                    	setBackground(new Color(246,246,246));  
                        
                    }else{  
                        setBackground(new Color(222,222,243));  
                      
                    }  
                    
                    setHorizontalAlignment(JLabel.CENTER);
  
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
        { "序号", "联赛", "时间", "球队", "盘口", "主队赔率", "变动", "大小球", "大小球主队赔率","变动 ", "完场比分"};
        

        /** 
         * 得到数据所对应对象 
         */  
        @Override  
        public Object getValueAt(int rowIndex, int columnIndex)  
        { 
        	
        	try{
        		
            	if(showitemVec == null || showitemVec.size() == 0){
            		return null;
            	}
            	
            	return showitemVec.elementAt(rowIndex)[columnIndex];
        		
        	}catch(Exception e){
        		e.printStackTrace();
        		return null;
        	}
        	

        	
        	
        }  
        

        
  
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
	        if(null == showitemVec){
	    		return 0;
	    	}
	        
	        //System.out.println(gameDetailsVec.size());
	        
            return showitemVec.size();  
        }  
  

  
        /** 
         * 得到指定列的数据类型 
         */  
        @Override  
        public Class<?> getColumnClass(int columnIndex)  
        {  
            return String.class;
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
            /*detailsData.elementAt(rowIndex)[columnIndex] = (String)aValue;  
            通知监听器数据单元数据已经改变  
            fireTableCellUpdated(rowIndex, columnIndex);  */
        }  
        
        public void updateTable(){
        	fireTableDataChanged();
        }
        
  
    }  
	
	
	
	
}

