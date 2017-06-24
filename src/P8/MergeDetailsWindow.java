package P8;

import javax.swing.table.JTableHeader;

import CTable.*;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import P8.EventsDetailsWindow.MyTableModel;

public class MergeDetailsWindow extends JFrame{
	  
	  
	   
		private static final long serialVersionUID = 508685938515369544L;
		
		private  Vector<String[]> originalDetailsData = new Vector<String[]>();
		
		private  Vector<String[]> detailsData = null;
		
		private Vector<Integer> hightlightRows = new Vector<Integer>();
		
		
	    private JLabel labelHighlightNum = new JLabel("让球总金额:");
	    private JTextField textFieldHighlightNum = new JTextField(15);  
	    
	    private JLabel labelp0oHighlightNum = new JLabel("大小球总金额:");
	    private JTextField textFieldp0oHighlightNum = new JTextField(15);  
	    
	    private JLabel labelInterval = new JLabel("刷新时间:");
	    
	    String str1[] = {"20"};
	    
	    private JComboBox jcb = new JComboBox(str1); 
	    
	    
	    String str2[] = {"0.2", "0.3","0.4"};
	    
	    private JLabel labelPercent = new JLabel("占成:");
	    private JComboBox jcb2 = new JComboBox(str2); 
	    
	    private Double percent = 0.4;
	    
	    
	    private JLabel labelHideNum = new JLabel("让球单边金额:");
	    private JTextField textFieldHideNum = new JTextField(15); 
	    
	    private JLabel labelp0oHideNum = new JLabel("大小球单边金额:");
	    private JTextField textFieldp0oHideNum = new JTextField(15); 
	    
	    private JCheckBox onlyShow5Big = new JCheckBox("只看五大联赛,欧冠");
	    private JCheckBox onlyShowInplay = new JCheckBox("只看滚动盘");
	    private JCheckBox onlyShowNotInplay = new JCheckBox("只看单式盘");
	    
	    private boolean bonlyShow5Big = false;
	    private boolean bonlyShowInplay = false;
	    private boolean bonlyShowNotInplay = false;
	    
	    private JLabel labelGrabStat= new JLabel("状态");
	    
	    private JLabel labelGrabStatkong1= new JLabel("");
	    private JLabel labelGrabStatkong2= new JLabel("");

	    
	    private JTextField textFieldGrabStat = new JTextField(15);  
	    
	    
	    private JPopupMenu m_popupMenu;
	    
	    private JMenuItem delMenItem; 
	    
	    private int focusedRowIndex = -1;
	    
	    private int selectedOrMerge = 0;
		
	    Double p0hhiglightBigNum = 500000.0;
	    
	    Double p0hhideNum = 100000.0;
	    
	    Double p0ohiglightBigNum = 500000.0;
	    
	    Double p0ohideNum = 100000.0;

	    
	    
	/*    private JLabel labeltime = new JLabel("距封盘:");
	    private JTextField textFieldtime = new JTextField(15);  
	    
	    private AtomicLong remainTime = new AtomicLong(0);*/
	    
	    
	    MyTableModel tableMode = new MyTableModel();
	    
	    
	    CTable table = null;
	    
	    CMap m = new CMap1();

	    
	    
	    
		

		public MergeDetailsWindow()  
	    {  
			setTitle("合并注单");  
			
	        intiComponent();  
	        
	        
	        
	        
	        
	    }  
		
		
		
		private void createPopupMenu() {  
	        m_popupMenu = new JPopupMenu();  
	          
	        delMenItem = new JMenuItem();  
	        delMenItem.setText("选择");  
	        delMenItem.addActionListener(new java.awt.event.ActionListener() {  
	            public void actionPerformed(java.awt.event.ActionEvent evt) {  
	                //该操作需要做的事  
	            	
	            	try{
	            		
		            	if(focusedRowIndex != -1 && focusedRowIndex < detailsData.size()){
		            		if(selectedOrMerge == 0){
		            			MergeManager.p8SelectedRow = detailsData.elementAt(focusedRowIndex);
		            			System.out.println(Arrays.toString(MergeManager.p8SelectedRow));
		            		}else{

		            			
		            			MergeManager.p8SelectedRow = detailsData.elementAt(focusedRowIndex);
		            			System.out.println(Arrays.toString(MergeManager.p8SelectedRow));
		            			
		            			//Todo
		            			//处理合并

		            			
		            			MergeManager.showMergeWnd(true);
		            			
		            			MergeManager.zhiboSelectedRow = null;
		            			MergeManager.p8SelectedRow = null;
		            		}
		            	}

		    	        
	            	}catch(Exception e){
	            		
	            	}

	            }  
	        });  
	        m_popupMenu.add(delMenItem);  
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
		           
		           if(MergeManager.zhiboSelectedRow == null){
		        	   delMenItem.setText("选择");
		        	   selectedOrMerge = 0;
		           }else{
		        	   delMenItem.setText("合并");
		        	   selectedOrMerge = 1;
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
			
			Vector<String[]> DetailsDatatmp = new Vector<String[]>();
			
			//只显示走地盘
			if(bonlyShowInplay == true){
				for(int i = 0; i < originalDetailsData.size(); i++){
					if(originalDetailsData.elementAt(i)[MERGEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
						DetailsDatatmp.add(originalDetailsData.elementAt(i));
					}
				}
			}
			
			//只显示单式盘
			if(bonlyShowNotInplay == true){
				for(int i = 0; i < originalDetailsData.size(); i++){
					if(!originalDetailsData.elementAt(i)[MERGEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
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
					if(P8Http.isInShowLeagueName(DetailsDatatmp.elementAt(i)[MERGEINDEX.LEAGUENAME.ordinal()])){
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
				String leagueName = DetailsDatatmp1.elementAt(i)[MERGEINDEX.LEAGUENAME.ordinal()];
				
				
				String eventName = DetailsDatatmp1.elementAt(i)[MERGEINDEX.EVENTNAMNE.ordinal()];
				

				
				if(P8Http.isInShowLeagueName(leagueName) || true){
					double betAmt1 =0.0;
					double betp81 = 0.0;
					double betzhibo1 = 0.0;
					double betp8inplay1 = 0.0;
					
					double betAmt2 = 0.0;
					double betp82 = 0.0;
					double betzhibo2 = 0.0;
					double betp8inplay2 = 0.0;
					
					double betAmt3 =0.0;
					double betp83 = 0.0;
					double betzhibo3 = 0.0;
					double betp8inplay3 = 0.0;
					
					double betAmt4 = 0.0;
					double betp84 = 0.0;
					double betzhibo4 = 0.0;
					double betp8inplay4 = 0.0;
					
					String str1 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.PERIOD0HOME.ordinal()];
					
					if(str1.contains("=")){
						String[] tmp = str1.split("=");
						betAmt1 = Double.parseDouble(tmp[1]);
						
						String[] tmp1 = tmp[0].split("\\+");
						tmp1[0] = tmp1[0].replace("(", "");
						tmp1[0] = tmp1[0].replace(")", "");
						
					
						tmp1[1] = tmp1[1].replace("(", "");
						tmp1[1] = tmp1[1].replace(")", "");
						
						if(tmp1.length > 2){
							tmp1[2] = tmp1[2].replace("(", "");
							tmp1[2] = tmp1[2].replace(")", "");
							betp8inplay1 = Double.parseDouble(tmp1[2]);
						}
						
						betp81 = Double.parseDouble(tmp1[0]);
						betzhibo1 = Double.parseDouble(tmp1[1]);
						
						
					}else{
						betAmt1 = Double.parseDouble(str1);
					}
					
					
					String str2 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.PERIOD0OVER.ordinal()];
					
					if(str2.contains("=")){
						String[] tmp = str2.split("=");
						betAmt2 = Double.parseDouble(tmp[1]);
						
						String[] tmp1 = tmp[0].split("\\+");
						tmp1[0] = tmp1[0].replace("(", "");
						tmp1[0] = tmp1[0].replace(")", "");
						
					
						tmp1[1] = tmp1[1].replace("(", "");
						tmp1[1] = tmp1[1].replace(")", "");
						
						if(tmp1.length > 2){
							tmp1[2] = tmp1[2].replace("(", "");
							tmp1[2] = tmp1[2].replace(")", "");
							betp8inplay2 = Double.parseDouble(tmp1[2]);
						}
						
						betp82 = Double.parseDouble(tmp1[0]);
						betzhibo2 = Double.parseDouble(tmp1[1]);
						
					}else{
						betAmt2 = Double.parseDouble(str2);
					}
					
					
/*					String str3 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.PERIOD1HOME.ordinal()];
					
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
						
						if(tmp1.length > 3){
							tmp1[2] = tmp1[2].replace("(", "");
							tmp1[2] = tmp1[2].replace(")", "");
							betp8inplay3 = Double.parseDouble(tmp1[2]);
						}
						
					}else{
						betAmt3 = Double.parseDouble(str3);
					}
					
					String str4 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.PERIOD1OVER.ordinal()];
					
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
						
						if(tmp.length > 2){
							tmp1[2] = tmp1[2].replace("(", "");
							tmp1[2] = tmp1[2].replace(")", "");
							betp8inplay4 = Double.parseDouble(tmp1[2]);
						}
						
					}else{
						betAmt4 = Double.parseDouble(str4);
					}*/
					
					if(Math.abs(betAmt1) < p0hhiglightBigNum && Math.abs(betAmt2) < p0ohiglightBigNum){
						continue;
					}
					

					
					if(eventName.contains("滚动盘")){
						if( (Math.abs(betp81) > p0hhideNum && Math.abs(betzhibo1) > p0hhideNum   && Math.abs(betp8inplay1) > p0hhideNum)|| 
								(Math.abs(betp82) > p0ohideNum && Math.abs(betzhibo2) > p0ohideNum && Math.abs(betp8inplay2) > p0ohideNum)){
							//
							
							DetailsDatatmp2.add(DetailsDatatmp1.elementAt(i));
							
						}
					}else{
						if( (Math.abs(betp81) > p0hhideNum && Math.abs(betzhibo1) > p0hhideNum) || 
								(Math.abs(betp82) > p0ohideNum && Math.abs(betzhibo2) > p0ohideNum)){
							//
							
							DetailsDatatmp2.add(DetailsDatatmp1.elementAt(i));
							
						}
					}
					


					
					
				}
				
				
			}
			
			detailsData = (Vector<String[]>)DetailsDatatmp2.clone();
			

			
			
			tableMode.updateTable();
			
			//setOneRowBackgroundColor();
			
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
	        
	        jcb.setSelectedIndex(0);
	        
	        jcb.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
	                String content = jcb.getSelectedItem().toString();
	                
	              //  StoneAge.setSleepTime(Integer.parseInt(content));
				}
	        });
	        
	        
	        jcb2.setSelectedIndex(2);
	        
	        jcb2.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
	                percent = Double.parseDouble(jcb2.getSelectedItem().toString());
	                
	                tableMode.updateTable();
	                //StoneAge.setSleepTime(Integer.parseInt(content));
				}
	        });
	        
	        textFieldHighlightNum.setText("500000");
	        
	        
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
	        
	        textFieldHideNum.setText("100000");
	        
	        
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
	        
	        textFieldp0oHighlightNum.setText("500000");
	        
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
	        
	        textFieldp0oHideNum.setText("100000");
	        
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
		    
	        panelNorth.add(labelPercent);
	        panelNorth.add(jcb2);

	        
	        panelNorth.add(labelHighlightNum);
	        panelNorth.add(textFieldHighlightNum);
	        
	        panelNorth.add(labelp0oHighlightNum);
	        panelNorth.add(textFieldp0oHighlightNum);
	        
	        panelNorth.add(labelHideNum);
	        panelNorth.add(textFieldHideNum);
	        
	        panelNorth.add(labelp0oHideNum);
	        panelNorth.add(textFieldp0oHideNum);
	        panelNorth.add(onlyShow5Big);
	        panelNorth.add(onlyShowInplay);
	        panelNorth.add(onlyShowNotInplay);

	        //调整框位置
	        panelNorth.add(labelGrabStatkong1);
	        panelNorth.add(labelGrabStatkong2);
	        
	        panelNorth.add(labelGrabStat);

	        panelNorth.add(textFieldGrabStat);
	        
	        
	        
	        
	/*        panelNorth.add(labeltime);
	        panelNorth.add(textFieldtime);
	        textFieldjinrichazhi.setEditable(false);*/


		    
			
	        table = new CTable(m,tableMode);

	        JScrollPane scroll = new JScrollPane(table); 
	        
	        
	        //设置选中行
	        table.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                int sr;
	                if ((sr = table.getSelectedRow()) == -1) {
	                    return;
	                }else{
	                	int block = ((int)(sr/2))%2;
	                	int nRowblock = ((int)((sr+1)/2))%2;
	                	if(nRowblock== block){
	                		table.setRowSelectionInterval(sr, sr + 1);
	                	}else{
	                		table.setRowSelectionInterval(sr-1, sr);
	                	}
	                	
	                }
	                
	            }
	        });

	        
	        table.getColumnModel().getColumn(0).setPreferredWidth(20);//序号
	        table.getColumnModel().getColumn(1).setPreferredWidth(120);//联赛
	        table.getColumnModel().getColumn(2).setPreferredWidth(120);//时间	        
	        table.getColumnModel().getColumn(3).setPreferredWidth(150);	//队名        
		    table.getColumnModel().getColumn(8).setPreferredWidth(280);//合并让球
		    table.getColumnModel().getColumn(12).setPreferredWidth(280);//合并大小
		    
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
	            	Double betp8 = 0.0;
	            	Double betzhibo = 0.0;
	            	Double betp8inplay = 0.0;
	            	
	            	String[] tmp1 = null;
	            	
					if(str.contains("=")){
						String[] tmp = str.split("=");
						betAmt = Double.parseDouble(tmp[1]);
						
						tmp1 = tmp[0].split("\\+");
						tmp1[0] = tmp1[0].replace("(", "");
						tmp1[0] = tmp1[0].replace(")", "");
						
					
						tmp1[1] = tmp1[1].replace("(", "");
						tmp1[1] = tmp1[1].replace(")", "");
						
						if(tmp1.length > 2){
							tmp1[2] = tmp1[2].replace("(", "");
							tmp1[2] = tmp1[2].replace(")", "");
							
							betp8inplay = Double.parseDouble(tmp1[2]);
						}
						
						betp8 = Double.parseDouble(tmp1[0]);
						betzhibo = Double.parseDouble(tmp1[1]);
						
						
					}else{
						betAmt = Double.parseDouble(str);
					}
					
					if(Math.abs(betAmt) >= p0hhiglightBigNum){
						//setForeground(Color.red);
						
						setForeground(Color.black);
						
						if(null != tmp1 && tmp1.length <= 2){
							if(Math.abs(betp8) >= Math.abs(betAmt)*percent && Math.abs(betzhibo) >= Math.abs(betAmt)*percent){
								setForeground(new Color(0, 0, 255));
							}
						}
						
						setText((value == null) ? "" : value.toString());
						
					}else{
						setForeground(Color.black);
						//setText((value == null) ? "" : value.toString());
						setText("0");
					}
					
					
					if(null != tmp1){
						
						if(tmp1.length <= 2){
							if(Math.abs(betp8)< p0hhideNum || Math.abs(betzhibo) < p0hhideNum){
								setForeground(Color.black);
								setText("0");
							}
						}
						
						if(tmp1.length > 2){
							if(Math.abs(betp8) < p0hhideNum || Math.abs(betzhibo) < p0hhideNum || Math.abs(betp8inplay) < p0hhideNum){
								setForeground(Color.black);
								setText("0");
							}
						}
						
					}
					


	            }   

	        };   
	        
	        
	        DefaultTableCellRenderer p0oRender = new DefaultTableCellRenderer() {   

	            public void setValue(Object value) { //重写setValue方法，从而可以动态设置列单元字体颜色   

	               
	            	String str = value.toString();
	            	Double betAmt = 0.0;
	            	Double betp8 = 0.0;
	            	Double betzhibo = 0.0;
	            	Double betp8inplay = 0.0;
	            	
	            	String[] tmp1 = null;
	            	
					if(str.contains("=")){
						String[] tmp = str.split("=");
						betAmt = Double.parseDouble(tmp[1]);
						
						tmp1 = tmp[0].split("\\+");
						tmp1[0] = tmp1[0].replace("(", "");
						tmp1[0] = tmp1[0].replace(")", "");
						
					
						tmp1[1] = tmp1[1].replace("(", "");
						tmp1[1] = tmp1[1].replace(")", "");
						
						if(tmp1.length > 2){
							tmp1[2] = tmp1[2].replace("(", "");
							tmp1[2] = tmp1[2].replace(")", "");
							
							betp8inplay = Double.parseDouble(tmp1[2]);
						}
						
						betp8 = Double.parseDouble(tmp1[0]);
						betzhibo = Double.parseDouble(tmp1[1]);
						
						
					}else{
						betAmt = Double.parseDouble(str);
					}
					
					if(Math.abs(betAmt) >= p0ohiglightBigNum){
						//setForeground(Color.red);
						setForeground(Color.black);
						
						if(null != tmp1 && tmp1.length <= 2){
							if(Math.abs(betp8) >= Math.abs(betAmt)*percent && Math.abs(betzhibo) >= Math.abs(betAmt)*percent){
								setForeground(new Color(0, 0, 255));
							}
						}
						
						setText((value == null) ? "" : value.toString());
						
					}else{
						setForeground(Color.black);
						//setText((value == null) ? "" : value.toString());
						setText("0");
					}
					
					
					if(null != tmp1){
						
						if(tmp1.length <= 2){
							if(Math.abs(betp8)< p0ohideNum || Math.abs(betzhibo) < p0ohideNum){
								setForeground(Color.black);
								setText("0");
							}
						}
						
						if(tmp1.length > 2){
							if(Math.abs(betp8) < p0ohideNum || Math.abs(betzhibo) < p0ohideNum || Math.abs(betp8inplay) < p0ohideNum){
								setForeground(Color.black);
								setText("0");
							}
						}
						
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
	    
	    
	    
	    public void setOneRowBackgroundColor() {
	    	

	    	
	        try {  
	            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {  
	  
	                public Component getTableCellRendererComponent(JTable table,  
	                        Object value, boolean isSelected, boolean hasFocus,  
	                        int row, int column) {  
	                    if (((int)(row/2))%2 == 0) {  
	                    	setBackground(new Color(246,246,246));  
	                        //setForeground(Color.BLACK);  
	                    }else{  
	                        setBackground(new Color(222,222,243));  
	                      //  setForeground(Color.BLACK);  
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
	        	{ "序号", "联赛", "时间", "球队", "比分", "平博" , "平博滚动盘", "智博", "全场让球", "平博 ", "平博滚动盘 ", "智博 ", "全场大小"};  
	        

	        
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
	        	return detailsData.size()*2;  
	        }  
	  
	        /** 
	         * 得到数据所对应对象 
	         */  
	        @Override  
	        public Object getValueAt(int rowIndex, int columnIndex)  
	        {  
	            //return data[rowIndex][columnIndex];
	        	//return detailsData.elementAt(rowIndex)[columnIndex+1];
	        	if(columnIndex == 0){
	        		return Integer.toString(rowIndex/2 + 1);
	        	}
	        	
	        	if(columnIndex == 3){
	        		int newRow = 0;
	        		newRow = (int)(rowIndex/2);
	        		String res = detailsData.elementAt(newRow)[columnIndex];
	        		String[] resA = res.split(" vs ");
	        		return resA[rowIndex%2];
	        	}else if(columnIndex == 4){
	        		int newRow = 0;
	        		newRow = (int)(rowIndex/2);
	        		String res = detailsData.elementAt(newRow)[columnIndex];
	        		String[] resA = res.split(":");
	        		return resA[rowIndex%2];
	        		
	        	}else if(columnIndex == 5){
	        		int newRow = 0;
	        		newRow = (int)(rowIndex/2);
	        		String res = detailsData.elementAt(newRow)[columnIndex];
	        		if(res.contains("=")){
	            		String[] resA = res.split("=");
	            		res = resA[0];
	            		resA = res.split("-");
	            		res = resA[rowIndex%2];
	            		res = res.replace("(", "");
	            		res = res.replace(")", "");
	            		if(rowIndex%2 == 1)
	            			res = "-" + res;
	            		return res;
	        		}else{
	        			return res;
	        		}

	        		
	        	}else if(columnIndex == 6){
	        		int newRow = 0;
	        		newRow = (int)(rowIndex/2);
	        		String res = detailsData.elementAt(newRow)[columnIndex];
	        		if(res.contains("=")){
	            		String[] resA = res.split("=");
	            		res = resA[0];
	            		resA = res.split("-");
	            		res = resA[rowIndex%2];
	            		res = res.replace("(", "");
	            		res = res.replace(")", "");
	            		if(rowIndex%2 == 1)
	            			res = "-" + res;
	            		return res;
	        		}else{
	        			return res;
	        		}

	        		
	        	}else if(columnIndex == 9){
	        		int newRow = 0;
	        		newRow = (int)(rowIndex/2);
	        		String res = detailsData.elementAt(newRow)[columnIndex];
	        		if(res.contains("=")){
	            		String[] resA = res.split("=");
	            		res = resA[0];
	            		resA = res.split("-");
	            		res = resA[rowIndex%2];
	            		res = res.replace("(", "");
	            		res = res.replace(")", "");
	            		if(rowIndex%2 == 1)
	            			res = "-" + res;
	            		return res;
	        		}else{
	        			return res;
	        		}

	        		
	        	}else if(columnIndex == 10){
	        		int newRow = 0;
	        		newRow = (int)(rowIndex/2);
	        		String res = detailsData.elementAt(newRow)[columnIndex];
	        		if(res.contains("=")){
	            		String[] resA = res.split("=");
	            		res = resA[0];
	            		resA = res.split("-");
	            		res = resA[rowIndex%2];
	            		res = res.replace("(", "");
	            		res = res.replace(")", "");
	            		if(rowIndex%2 == 1)
	            			res = "-" + res;
	            		return res;
	        		}else{
	        			return res;
	        		}

	        		
	        	}else{
	        		return detailsData.elementAt(rowIndex/2)[columnIndex];
	        	}
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
