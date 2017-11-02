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

import HGclient.DXQdetailsWindow.MyTableModel;
import P8.Common;
import P8.DateChooser;



class classifyItem{
	public String classify = "";
	public int totalgames = 0;
	public int nogoalgames = 0;
	public int onegoalgames = 0;
	public int twogoalsgames = 0;
	public int noresgames = 0;
}



public class DXQAnalysisWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	
	
	public Vector<GameDetails> gameDetailsVec = new Vector<GameDetails>(); 
	
	public Vector<String[]> showitemVec = new Vector<String[]>();

	public Vector<classifyItem> classifyItemVec = new Vector<classifyItem>();
	
    private JLabel datelb1 = new JLabel("起始日期:");
    DateChooser mpstart = new DateChooser("dxqAnsdates");
	

    private JLabel datelb2 = new JLabel("结束日期:");
    DateChooser mpend = new DateChooser("dxqAnsdatee");
    
    

	
	DXQAnalysisWindow(){
		
		dxqs.add("2");
		dxqs.add("2/2.5");
		dxqs.add("2.5");
		dxqs.add("2.5/3");
		dxqs.add("3");
		
		setTitle("大小球分析");
		
		
		intiComponent();
	}
	
	

	
    MyTableModel tableMode = new MyTableModel();
    
    
    JTable table = null;
    
    
    private JLabel classirylb = new JLabel("分类选择:");
    private JCheckBox allcb = new JCheckBox("汇总");
    boolean bclassifyByall = true;
    
    private JCheckBox leaguecb = new JCheckBox("联赛名");
    boolean bclassifyByleague = false;
    
    
    
    private JLabel pklb = new JLabel("盘口选择:");
    private JCheckBox alldxq = new JCheckBox("所有盘口");
    private JCheckBox dxq1 = new JCheckBox("2");
    private JCheckBox dxq2 = new JCheckBox("2/2.5");
    private JCheckBox dxq3 = new JCheckBox("2.5");
    private JCheckBox dxq4 = new JCheckBox("2.5/3");
    private JCheckBox dxq5 = new JCheckBox("3");
    
    boolean balldxq = false;
    
    public Vector<String> dxqs = new Vector<String>();
    
    
	public String getmpstartdate(){
		return mpstart.getChooseDate();
	}
    
    
	public String getmpenddate(){
		return mpend.getChooseDate();
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
				
				
				//if(gameitem.getodds().size() != 0 && gameitem.getodds().elementAt(gameitem.getodds().size() - 1)[HGODDSINDEX.TYPE.ordinal()].equals("inplay")){
					gameDetailsVec.add(gameitem);
				//}
				


			}
			
			//sortgamedetails();
			
			updateShowItem();
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	
	public void showbyall(){
		try{
			int totalgames = 0;
			int noresgames = 0;
			int nogoalgames =0;
			int onegoalgames = 0;
			int twogoalgames = 0;
			
			if(showitemVec.size() != 0){
				showitemVec.clear();
			}
			
			

			
			for(int i = 0; i < gameDetailsVec.size(); i++){
				GameDetails onegame = gameDetailsVec.elementAt(i);
				
				
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				long gametime = df.parse(onegame.datetime).getTime();
				

				
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
					
					
					totalgames++;
					
					int halfscores = Integer.parseInt(onegame.getodds().elementAt(firstinplayindex)[HGODDSINDEX.SCOREH.ordinal()]) + Integer.parseInt(onegame.getodds().elementAt(firstinplayindex)[HGODDSINDEX.SCOREC.ordinal()]);
					if(onegame.gameresult.equals("")){
						System.out.println("无结果："+ onegame.teamh + "vs" + onegame.teamc);
						noresgames++;
						continue;
					}else{
						int totalscores = Integer.parseInt(onegame.gameresult.split("-")[0]) + Integer.parseInt(onegame.gameresult.split("-")[1]);
						totalscores = totalscores - halfscores;
						if(totalscores == 0){
							nogoalgames++;
						}else if(totalscores == 1){
							onegoalgames++;
						}else if(totalscores >= 2){
							twogoalgames++;
						}else if(totalscores < 0){
							System.out.println("结果出错："+ onegame.teamh + "vs" + onegame.teamc);
							noresgames++;
						}
					}
					
				}
			}
			
			String[] item = {"1","总", Integer.toString(totalgames), Integer.toString(nogoalgames), Integer.toString(onegoalgames),Integer.toString(twogoalgames),Integer.toString(noresgames)};
			
			showitemVec.add(item);
			
			

			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void showbyleague(){
		try{
			
			
			if(showitemVec.size() != 0){
				showitemVec.clear();
			}
			
			
			if(classifyItemVec.size() != 0){
				classifyItemVec.clear();
			}
			

			
			for(int i = 0; i < gameDetailsVec.size(); i++){
				GameDetails onegame = gameDetailsVec.elementAt(i);
				
				
				

				
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
					
					
					
					
					int halfscores = Integer.parseInt(onegame.getodds().elementAt(firstinplayindex)[HGODDSINDEX.SCOREH.ordinal()]) + Integer.parseInt(onegame.getodds().elementAt(firstinplayindex)[HGODDSINDEX.SCOREC.ordinal()]);
					if(onegame.gameresult.equals("")){
						System.out.println("无结果："+ onegame.teamh + "vs" + onegame.teamc);
						boolean find = false;
						for(int k = 0; k < classifyItemVec.size(); k++){
							if(classifyItemVec.elementAt(k).classify.equals(onegame.league)){
								classifyItemVec.elementAt(k).noresgames++;
								classifyItemVec.elementAt(k).totalgames++;
								find = true;
								break;
							}
						}
						
						if(find == false){
							classifyItem tmpitem = new classifyItem();
							tmpitem.classify = onegame.league;
							tmpitem.noresgames = 1;
							tmpitem.totalgames = 1;
							classifyItemVec.add(tmpitem);
						}
						
						
						
						
						continue;
					}else{
						int totalscores = Integer.parseInt(onegame.gameresult.split("-")[0]) + Integer.parseInt(onegame.gameresult.split("-")[1]);
						totalscores = totalscores - halfscores;
						if(totalscores == 0){
							
							boolean find = false;
							for(int k = 0; k < classifyItemVec.size(); k++){
								if(classifyItemVec.elementAt(k).classify.equals(onegame.league)){
									classifyItemVec.elementAt(k).nogoalgames++;
									classifyItemVec.elementAt(k).totalgames++;
									find = true;
									break;
								}
							}
							
							if(find == false){
								classifyItem tmpitem = new classifyItem();
								tmpitem.classify = onegame.league;
								tmpitem.nogoalgames = 1;
								tmpitem.totalgames = 1;
								classifyItemVec.add(tmpitem);
							}

							
						}else if(totalscores == 1){
							boolean find = false;
							for(int k = 0; k < classifyItemVec.size(); k++){
								if(classifyItemVec.elementAt(k).classify.equals(onegame.league)){
									classifyItemVec.elementAt(k).onegoalgames++;
									classifyItemVec.elementAt(k).totalgames++;
									find = true;
									break;
								}
							}
							
							if(find == false){
								classifyItem tmpitem = new classifyItem();
								tmpitem.classify = onegame.league;
								tmpitem.onegoalgames = 1;
								tmpitem.totalgames = 1;
								classifyItemVec.add(tmpitem);
							}
						}else if(totalscores >= 2){
							boolean find = false;
							for(int k = 0; k < classifyItemVec.size(); k++){
								if(classifyItemVec.elementAt(k).classify.equals(onegame.league)){
									classifyItemVec.elementAt(k).twogoalsgames++;
									classifyItemVec.elementAt(k).totalgames++;
									find = true;
									break;
								}
							}
							
							if(find == false){
								classifyItem tmpitem = new classifyItem();
								tmpitem.classify = onegame.league;
								tmpitem.twogoalsgames = 1;
								tmpitem.totalgames = 1;
								classifyItemVec.add(tmpitem);
							}
						}else if(totalscores < 0){
							System.out.println("结果出错："+ onegame.teamh + "vs" + onegame.teamc);
							boolean find = false;
							for(int k = 0; k < classifyItemVec.size(); k++){
								if(classifyItemVec.elementAt(k).classify.equals(onegame.league)){
									classifyItemVec.elementAt(k).noresgames++;
									classifyItemVec.elementAt(k).totalgames++;
									find = true;
									break;
								}
							}
							
							if(find == false){
								classifyItem tmpitem = new classifyItem();
								tmpitem.classify = onegame.league;
								tmpitem.noresgames = 1;
								tmpitem.totalgames = 1;
								classifyItemVec.add(tmpitem);
							}
						}
					}
					
				}
			}
			
			
			for(int i = 0; i < classifyItemVec.size(); i++){
				classifyItem ci = classifyItemVec.elementAt(i);
				String[] item = {Integer.toString(i+1), ci.classify, Integer.toString(ci.totalgames), Integer.toString(ci.nogoalgames), 
						Integer.toString(ci.onegoalgames), Integer.toString(ci.twogoalsgames), Integer.toString(ci.noresgames)};
				
				showitemVec.add(item);
			}
			
			

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	
	
	
	public void updateShowItem(){
		

		if(bclassifyByall == true){
			showbyall();
		}else if(bclassifyByleague == true){
			showbyleague();
		}
		
		
		tableMode.updateTable();
		
		setOneRowBackgroundColor();
		

	}
	
	
	
	
	
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel(new GridLayout(1, 10));

        container.add(panelNorth, BorderLayout.NORTH);  
        

        panelNorth.add(datelb1);
        panelNorth.add(mpstart);

        panelNorth.add(datelb2);
        panelNorth.add(mpend);
        
        
        
        
        
        

        panelNorth.add(classirylb);
        panelNorth.add(allcb);

        panelNorth.add(leaguecb);

        
        

        
        panelNorth.add(pklb);
        panelNorth.add(alldxq);
        panelNorth.add(dxq1);
        panelNorth.add(dxq2);
        panelNorth.add(dxq3);
        panelNorth.add(dxq4);
        panelNorth.add(dxq5);
        
        
        allcb.setSelected(true);
        
        allcb.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					
					bclassifyByall = false;
					
					
				}else{
					
					bclassifyByall = true;
					leaguecb.setSelected(false);
					
				}
				
				updateShowItem();

			}
        });
        
        
        leaguecb.setSelected(false);
        
        leaguecb.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					
					bclassifyByleague = false;
					
					
				}else{
					
					bclassifyByleague = true;
					allcb.setSelected(false);
					
				}
				
				updateShowItem();

			}
        });
        
        
        
        


        
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

                	
                    if (row%2==1) {  
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
        	 { "序号", "分类", "场次","无进球场次", "进1球场次", "进2球场次",  "无结果场次"};
        

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
        		
        		//System.out.println(showitemVec.elementAt(rowIndex)[columnIndex]);
        		
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
