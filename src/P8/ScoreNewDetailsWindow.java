package P8;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import MergeNew.NEWMERGEINDEX;



enum SCORETABLEHEADINDEX{
	INDEX,
	LEAGUENAME,
	TIME,
	STATUS,
	EVENTNAMNE,
	RQCHUPAN,
	RQZHONGPAN,
	RQPANAS,
	DXQCHUPAN,
	DXQZHONGPAN,
	DXQPANAS,
	SCORE
	
}




public class ScoreNewDetailsWindow extends JFrame{
	  
	  
	   
		private static final long serialVersionUID = 666685038515369544L;
		
		private  Vector<String[]> originalDetailsData = new Vector<String[]>();
		
		private  Vector<String[]> detailsData = new Vector<String[]>();
		
		private Vector<String> showLeagueName = new Vector<String>();
	    
	    
	    private JPopupMenu m_popupMenu;
	    
	    private JMenuItem chooseMenItem; 
	    
	    private JMenuItem mergeMenItem; 
	    
	    private int focusedRowIndex = -1;
	    
	    private int selectedOrMerge = 0; //0选择, 1合并
	    
	    Double p0hhiglightBigNum = 1000000.0;
	    
	    Double p0hhideNum = 0.0;
	    
	    Double p0ohiglightBigNum = 1000000.0;
	    
	    Double p0ohideNum = 0.0;
	    
	    
	    private JCheckBox simpleDatabox = new JCheckBox("精简");
	    
	    private JCheckBox onlybig5 = new JCheckBox("只看五大联赛，欧冠");
	    boolean bonlybig5 = true;
	    
	    private JLabel labelGrabStat= new JLabel("状态:");
	    private JTextField textFieldGrabStat = new JTextField(40);

	    
	    private boolean bsimpleData = true;
	    
	    
	    private JCheckBox overgamebox = new JCheckBox("完场");
	    private boolean bovergame = true;
	    private JCheckBox notbeginbox = new JCheckBox("未开赛");
	    private boolean bnotbegin = true;
	    private JCheckBox inplaybox = new JCheckBox("走地");
	    private boolean binplay = true;
	    
	    
	    
	    MyTableModel tableMode = new MyTableModel();
	    
	    
	    JTable table = null;



		public ScoreNewDetailsWindow()  
	    {  
			setTitle("比分网");  
			
			showLeagueName.add("英超");
			showLeagueName.add("意甲");
			showLeagueName.add("法甲");
			showLeagueName.add("西甲");
			showLeagueName.add("德甲");
			showLeagueName.add("欧冠杯");
			showLeagueName.add("欧罗巴杯");
			
	        intiComponent();  
	        
	        

	    }  
		
		
		
		public void setStateText(String txt){
			textFieldGrabStat.setText(txt);
		}
		
		public void setStateColor(Color cr){
			textFieldGrabStat.setBackground(cr);
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
		            		

		            			
		            			ScoreMergeManager.scoreSelectedRow = detailsData.elementAt(focusedRowIndex);
		            			System.out.println(Arrays.toString(ScoreMergeManager.scoreSelectedRow));

		            			ScoreMergeManager.showMergeWnd(true);
		            			
		            			ScoreMergeManager.scoreSelectedRow = null;
		            			ScoreMergeManager.p8SelectedRow = null;

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

		            			ScoreMergeManager.scoreSelectedRow = detailsData.elementAt(focusedRowIndex);
		            			System.out.println(Arrays.toString(ScoreMergeManager.scoreSelectedRow));

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
		           
		           if(ScoreMergeManager.p8SelectedRow == null){
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
			
			if(detailsData.size() != 0){
				detailsData.clear();
			}
			
			//只显示走地盘
			if(bsimpleData == true){
				for(int i = 0; i < originalDetailsData.size(); i++){
					if(originalDetailsData.elementAt(i)[SCORENEWINDEX.IMPORTANTCUP.ordinal()].equals("1")){
						
						
						boolean add = false;
						
						if(bovergame == true && originalDetailsData.elementAt(i)[SCORENEWINDEX.STATUS.ordinal()].equals("完")){
							add = true;
						}
						
						if(binplay == true && !originalDetailsData.elementAt(i)[SCORENEWINDEX.STATUS.ordinal()].equals("完") && 
								!originalDetailsData.elementAt(i)[SCORENEWINDEX.STATUS.ordinal()].equals("")){
							add = true;
						}
						
						if(bnotbegin == true && originalDetailsData.elementAt(i)[SCORENEWINDEX.STATUS.ordinal()].equals("")){
							add = true;
						}
						
						if(bonlybig5 && !showLeagueName.contains(originalDetailsData.elementAt(i)[SCORENEWINDEX.LEAGUENAME.ordinal()])){
							add  = false;
						}
						
						if(add == true){
							detailsData.add(originalDetailsData.elementAt(i));
						}
					}
				}
				
			}else{
				for(int i = 0; i < originalDetailsData.size(); i++){
					
					boolean add = false;
					
					if(bovergame == true && originalDetailsData.elementAt(i)[SCORENEWINDEX.STATUS.ordinal()].equals("完")){
						add  = true;
					}
					
					if(binplay == true && !originalDetailsData.elementAt(i)[SCORENEWINDEX.STATUS.ordinal()].equals("完") && 
							!originalDetailsData.elementAt(i)[SCORENEWINDEX.STATUS.ordinal()].equals("")){
						add  = true;
					}
					
					if(bnotbegin == true && originalDetailsData.elementAt(i)[SCORENEWINDEX.STATUS.ordinal()].equals("")){
						add  = true;
					}
					
					if(bonlybig5 && !showLeagueName.contains(originalDetailsData.elementAt(i)[SCORENEWINDEX.LEAGUENAME.ordinal()])){
						add  = false;
					}
					
					if(add == true){
						detailsData.add(originalDetailsData.elementAt(i));
					}
				}
			}
			

			
			tableMode.updateTable();
		}

		
		

		
		
		
	  
	    /** 
	     * 初始化窗体组件 
	     */  
	    private void intiComponent()  
	    {  

			final Container container = getContentPane();
			
			container.setLayout(new BorderLayout());
			
			JPanel panelNorth = new JPanel(new GridLayout(1, 4));

			container.add(panelNorth, BorderLayout.NORTH);  
			
			simpleDatabox.setSelected(true);
			
			simpleDatabox.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
					if(e.getStateChange() == ItemEvent.DESELECTED){
						
						bsimpleData = false;
					}else{
						
						bsimpleData = true;
					}
					
					updateShowItem();
				}
	        });
			
			
			onlybig5.setSelected(true);
			
			onlybig5.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
					if(e.getStateChange() == ItemEvent.DESELECTED){
						
						bonlybig5 = false;
					}else{
						
						bonlybig5 = true;
					}
					
					updateShowItem();
				}
	        });
			
			
			notbeginbox.setSelected(true);
			
			notbeginbox.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
					if(e.getStateChange() == ItemEvent.DESELECTED){
						
						bnotbegin = false;
					}else{
						
						bnotbegin = true;
					}
					
					updateShowItem();
				}
	        });
			

			
			inplaybox.setSelected(true);
			
			inplaybox.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
					if(e.getStateChange() == ItemEvent.DESELECTED){
						
						binplay = false;
					}else{
						
						binplay = true;
					}
					
					updateShowItem();
				}
	        });
			
			
			
			overgamebox.setSelected(true);
			
			overgamebox.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
					if(e.getStateChange() == ItemEvent.DESELECTED){
						
						bovergame = false;
					}else{
						
						bovergame = true;
					}
					
					updateShowItem();
				}
	        });
			
			
			panelNorth.add(simpleDatabox);
			panelNorth.add(onlybig5);
			panelNorth.add(notbeginbox);
			panelNorth.add(inplaybox);
			panelNorth.add(overgamebox);
			
			
			
			panelNorth.add(labelGrabStat);
			panelNorth.add(textFieldGrabStat);
			textFieldGrabStat.setEditable(false);
			
			
			

		    table = new JTable(tableMode);

	        JScrollPane scroll = new JScrollPane(table);  
	        
	        createPopupMenu();
	        
	        
	        table.addMouseListener(new java.awt.event.MouseAdapter() {  
	            public void mouseClicked(java.awt.event.MouseEvent evt) {  
	                jTable1MouseClicked(evt);  
	            }  
	        });  
	        
	        
	        
	        
		   // table.getColumnModel().getColumn(2).setPreferredWidth(240);
		    
		    table.setRowHeight(30);
		    
		    table.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));
		    
		    
		    table.getColumnModel().getColumn(SCORETABLEHEADINDEX.EVENTNAMNE.ordinal()).setPreferredWidth(300);//球队
		    
		    table.getColumnModel().getColumn(SCORETABLEHEADINDEX.TIME.ordinal()).setPreferredWidth(140);//球队
		    
		    


		    
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
	    

	    
	    
	    

	    

	    

	    
	    
	    
	  
	    private class MyTableModel extends AbstractTableModel  
	    {  
	        /* 
	         * 这里和刚才一样，定义列名和每个数据的值 
	         */  
	        String[] columnNames =  
	        { "序号", "联赛","时间", "状态","球队", "让球初盘", "终盘", "盘口分析","大小初盘", "终盘", "盘口分析", "比分"};  
	        

	        
	  
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
	        	if(detailsData == null){
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
	        	
	        	if(columnIndex == 0){
	        		return Integer.toString(rowIndex + 1);
	        	}
	        	
	        	return detailsData.elementAt(rowIndex)[columnIndex-1];
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
