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

import HGclient.GameDetailsWindow.MyTableModel;
import P8.Common;
import P8.DateChooser;









public class DXQPreviousDetailsWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	public DXQPreviousDetailsWindow()  
    {  
		
		dxqs.add("2");
		dxqs.add("2/2.5");
		dxqs.add("2.5");
		dxqs.add("2.5/3");
		dxqs.add("3");
		
		
		setTitle("大小球历史");  
		
        intiComponent();  
    } 
	
	public Vector<GameDetails> gameDetailsVec = new Vector<GameDetails>(); 
	
	public Vector<String[]> showitemVec = new Vector<String[]>();
	
	
    MyTableModel tableMode = new MyTableModel();
    
    
    JTable table = null;
    
    
    private JLabel datelb = new JLabel("日期:");
    
    
    DateChooser mp = new DateChooser("DXQPreviousDetailsWindow");
    
    
    private JLabel ratiolb = new JLabel("水位置设置:");
    private JTextField ratiotxt = new JTextField(15); 
    
    private double ratio = 0.5;
    
    
    
    private JLabel pklb = new JLabel("盘口选择:");
    private JCheckBox alldxq = new JCheckBox("所有盘口");
    private JCheckBox dxq1 = new JCheckBox("2");
    private JCheckBox dxq2 = new JCheckBox("2/2.5");
    private JCheckBox dxq3 = new JCheckBox("2.5");
    private JCheckBox dxq4 = new JCheckBox("2.5/3");
    private JCheckBox dxq5 = new JCheckBox("3");
    
    boolean balldxq = false;
    
    public Vector<String> dxqs = new Vector<String>();


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
				
				
				if(gameitem.getodds().size() != 0 && gameitem.getodds().elementAt(gameitem.getodds().size() - 1)[HGODDSINDEX.TYPE.ordinal()].equals("inplay")){
					gameDetailsVec.add(gameitem);
				}
				


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

			Comparator ct = new dxqTimeCompare(); 
        	

        	
        	
        	if(gameDetailsVec.size() != 0){
        		
        		Collections.sort(gameDetailsVec, ct);
        		
        		
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
			
			int tableindex = 0;
			
			for(int i = 0; i < gameDetailsVec.size(); i++){
				//
				
				GameDetails onegame = gameDetailsVec.elementAt(i);
				
				//System.out.println("dxq show:" + onegame.teamh + "vs" + onegame.teamc);
				
				int latestdanshiindex = -1;
				for(int j = onegame.getodds().size() - 1; j >= 0 ; j--){
					if(onegame.getodds().elementAt(j)[HGODDSINDEX.TYPE.ordinal()].equals("danshi") && onegame.getodds().elementAt(j)[HGODDSINDEX.PRIOTITY.ordinal()].equals("1")){
						latestdanshiindex = j;
						break;
					}
				}
				
				
				int firstinplayindex = -1;
				for(int j= latestdanshiindex; j < onegame.getodds().size(); j++){
					if(onegame.getodds().elementAt(j)[HGODDSINDEX.TYPE.ordinal()].equals("inplay") && onegame.getodds().elementAt(j)[HGODDSINDEX.PRIOTITY.ordinal()].equals("1")){
						firstinplayindex = j;
						break;
					}
				}
				
				
				
				//
				if(latestdanshiindex != -1 && firstinplayindex != -1){
					
					
					//盘口过滤
					if(balldxq == false && !dxqs.contains(onegame.getodds().elementAt(latestdanshiindex)[HGODDSINDEX.O.ordinal()].replace("O", "").replace(" ", ""))){
						continue;
					}
					//end
					
					
					
					
					tableindex++;
					
					int oddsize = onegame.getodds().size();

					String[] item = {Integer.toString(tableindex), onegame.league, onegame.datetime, onegame.teamh + "-vs-" + onegame.teamc, "单式", 
							onegame.getodds().elementAt(latestdanshiindex)[HGODDSINDEX.O.ordinal()].replace("O", ""), onegame.getodds().elementAt(latestdanshiindex)[HGODDSINDEX.OODD.ordinal()], "" ,
							""};				
					showitemVec.add(item);
					
					String half = "";
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					long gametime = df.parse(onegame.datetime).getTime();
					
					for(int j = firstinplayindex; j < onegame.getodds().size(); j++){
						
						String[] odd = onegame.getodds().elementAt(j);
						
						String[] nextodd = null;
						
						int nextoddIndex = j +1;
						if(nextoddIndex < onegame.getodds().size()){
							nextodd = onegame.getodds().elementAt(nextoddIndex);
						}
						
						String scoreratiostr = "";
						
						String ratiostr = "";
						
						String scorestr = "";
						
						if(nextodd != null){
							int nextscore = Integer.parseInt(nextodd[HGODDSINDEX.SCOREC.ordinal()]) + Integer.parseInt(nextodd[HGODDSINDEX.SCOREH.ordinal()]);
							int currentscore = Integer.parseInt(odd[HGODDSINDEX.SCOREC.ordinal()]) + Integer.parseInt(odd[HGODDSINDEX.SCOREH.ordinal()]);
							
							if(nextscore > currentscore){
								scoreratiostr = odd[HGODDSINDEX.OODD.ordinal()];
							}
							
						}else if(System.currentTimeMillis() - gametime <= 120*60*1000){
							ratiostr = odd[HGODDSINDEX.OODD.ordinal()];
						}
						
						if(j == firstinplayindex){
							half = "下半场";
							
							scorestr = odd[HGODDSINDEX.SCOREH.ordinal()] + "-" + odd[HGODDSINDEX.SCOREC.ordinal()];
							
						}else{
							half = "";
							if(Double.parseDouble(odd[HGODDSINDEX.OODD.ordinal()]) < ratio){
								continue;
							}
						}
						
						
						String halftime = odd[HGODDSINDEX.GAMETIME.ordinal()];
						halftime = halftime.replace("2H^", "");
						
						
						
						String[] itemtmp = {"", "", half + halftime, "", odd[HGODDSINDEX.SCOREH.ordinal()] + "-" + odd[HGODDSINDEX.SCOREC.ordinal()], odd[HGODDSINDEX.O.ordinal()], ratiostr, scoreratiostr, scorestr};
						showitemVec.add(itemtmp);
					}
					
					

					
					
					if(!onegame.gameresult.equals("")){
						String[] itemtmp = {"", "", "全场比分", "", "", "", "", "", onegame.gameresult};
						showitemVec.add(itemtmp);
					}else if(onegame.gameresult.equals("") && System.currentTimeMillis() - gametime > 120*60*1000){
						
						String[] itemtmp = {"", "", "全场比分", "", "", "", "", "", onegame.getodds().elementAt(oddsize -1)[HGODDSINDEX.SCOREH.ordinal()] + "-" + onegame.getodds().elementAt(oddsize -1)[HGODDSINDEX.SCOREC.ordinal()]};
						showitemVec.add(itemtmp);
					}
					
					
					
					
					
					
					
				}


			}
			
			
			tableMode.updateTable();
			
			setOneRowBackgroundColor();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		

		

	}
	
	
	
	
	
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel(new GridLayout(1, 7));

        container.add(panelNorth, BorderLayout.NORTH);  
        

        panelNorth.add(datelb);
        
        panelNorth.add(mp);
        
        panelNorth.add(ratiolb);
        panelNorth.add(ratiotxt);
        
        panelNorth.add(pklb);
        panelNorth.add(alldxq);
        panelNorth.add(dxq1);
        panelNorth.add(dxq2);
        panelNorth.add(dxq3);
        panelNorth.add(dxq4);
        panelNorth.add(dxq5);
        


        

        
        alldxq.setSelected(false);
        
        alldxq.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					
					balldxq = false;
					
					
				}else{
					
					balldxq = true;
					
				}
				
				updateShowItem();

			}
        });

        
 
        
        dxq1.setSelected(true);
        
        dxq1.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					dxqs.remove(dxq1.getText());
					
				}else{
					if(!dxqs.contains(dxq1.getText())){
						dxqs.add(dxq1.getText());
					}

				}
				
				updateShowItem();	
			}
        });
        

        
        
        dxq2.setSelected(true);
        
        dxq2.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					dxqs.remove(dxq2.getText());
					
				}else{
					if(!dxqs.contains(dxq2.getText())){
						dxqs.add(dxq2.getText());
					}

				}
				
				updateShowItem();	
			}
        });

        
 
        dxq3.setSelected(true);
        
        dxq3.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					dxqs.remove(dxq3.getText());
					
				}else{
					if(!dxqs.contains(dxq3.getText())){
						dxqs.add(dxq3.getText());
					}

				}
				
				updateShowItem();	
			}
        });
        
        
        dxq4.setSelected(true);
        
        dxq4.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					dxqs.remove(dxq4.getText());
					
				}else{
					if(!dxqs.contains(dxq4.getText())){
						dxqs.add(dxq4.getText());
					}

				}
				
				updateShowItem();	
			}
        });
        
        
        
        dxq5.setSelected(true);
        
        dxq5.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					dxqs.remove(dxq5.getText());
					
				}else{
					if(!dxqs.contains(dxq5.getText())){
						dxqs.add(dxq5.getText());
					}

				}
				
				updateShowItem();	
			}
        });
        
        
        
        ratiotxt.setText(Double.toString(ratio));

        ratiotxt.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = ratiotxt.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	ratio = Double.parseDouble(value);
                    	
                    	
                    	
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
	    //table.getColumnModel().getColumn(7).setPreferredWidth(50);//大小球
	    
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
        
        

        
        setBounds(100, 100, 1600, 800); 

    }
 
	
    
    public void setOneRowBackgroundColor() {
    	

    	
        try {  
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
            	
            	
  
                public Component getTableCellRendererComponent(JTable table,  
                        Object value, boolean isSelected, boolean hasFocus,  
                        int row, int column) {  
                	
                	
                	
                    if (showitemVec.elementAt(row)[0].equals("")) {  
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
        { "序号", "联赛", "时间", "球队", "即时比分", "盘口", "水位", "进球水位", "比分"};
        

        /** 
         * 得到数据所对应对象 
         */  
        @Override  
        public Object getValueAt(int rowIndex, int columnIndex)  
        { 
        	
        	try{
        		
/*        		if(columnIndex == 0){
        			return Integer.toString(rowIndex+1);
        		}else{*/
        			return showitemVec.elementAt(rowIndex)[columnIndex];
        	//	}
        		
        		
        		

        		
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