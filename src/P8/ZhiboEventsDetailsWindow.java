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
import java.awt.event.WindowFocusListener;
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

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Stack;















import P8.EventsDetailsWindow.MyTableModel;




enum ZHIBOTABLEHEADINDEX{
	INDEX,
	LEAGUE,
	TIME,
	EVENTNAME,
	RQCHUPAN,
	RQZHONGPAN,
	RQPANAS,
	P0HOME,
	DXQCHUPAN,
	DXQZHONGPAN,
	DXQPANAS,
	P0OVER,
	SCORE
	
}






public class ZhiboEventsDetailsWindow  extends JFrame{
	  
	  
	   
		private static final long serialVersionUID = 508685038515369544L;
		
		private  Vector<String[]> originalDetailsData = new Vector<String[]>();
		
		private  Vector<String[]> detailsData = null;
		
		
		private Vector<String[]> showItemVec = new Vector<String[]>();
		
		private Vector<String[]> scoreDetails = new Vector<String[]>();
		
		private Vector<Integer> hightlightRows = new Vector<Integer>();
		
		
	    private JLabel labelHighlightNum = new JLabel("让球高亮金额:");
	    private JTextField textFieldHighlightNum = new JTextField(15);  
	    
	    private JLabel labelp0oHighlightNum = new JLabel("大小球高亮金额:");
	    private JTextField textFieldp0oHighlightNum = new JTextField(15);  
	    
	    private JLabel labelInterval = new JLabel("刷新时间:");
	    
	    //String str1[] = {"30", "60","90","120","180"};
	    String str1[] = {"20"};
	    
	   
	    
	    private JComboBox jcb = new JComboBox(str1); 
	    
	    
	    private JLabel labelHideNum = new JLabel("让球隐藏金额:");
	    private JTextField textFieldHideNum = new JTextField(15); 
	    
	    private JLabel labelp0oHideNum = new JLabel("大小球隐藏金额:");
	    private JTextField textFieldp0oHideNum = new JTextField(15); 
	    
	    private JCheckBox onlyShow5Big = new JCheckBox("只看五大,世界杯");
	    private JCheckBox onlyShowInplay = new JCheckBox("只看滚动盘");
	    private JCheckBox onlyShowNotInplay = new JCheckBox("只看单式盘");
	    
	    private boolean bonlyShow5Big = false;
	    private boolean bonlyShowInplay = false;
	    private boolean bonlyShowNotInplay = false;
	    
	    private JLabel labelGrabStat= new JLabel("状态:");
	    
	    private JLabel labelGrabStatkong1= new JLabel("");
	    private JLabel labelGrabStatkong2= new JLabel("");
	    
	    private JTextField textFieldGrabStat = new JTextField(15);  
	    
	    
	    private JPopupMenu m_popupMenu;
	    
	    private JMenuItem chooseMenItem; 
	    
	    private JMenuItem mergeMenItem; 
	    
	    private int focusedRowIndex = -1;
	    
	    private int selectedOrMerge = 0; //0选择, 1合并
	    
	    Double p0hhiglightBigNum = 1000000.0;
	    
	    Double p0hhideNum = 0.0;
	    
	    Double p0ohiglightBigNum = 1000000.0;
	    
	    Double p0ohideNum = 0.0;
	    
	    

	    
	    
	/*    private JLabel labeltime = new JLabel("距封盘:");
	    private JTextField textFieldtime = new JTextField(15);  
	    
	    private AtomicLong remainTime = new AtomicLong(0);*/
	    
	    
	    MyTableModel tableMode = new MyTableModel();
	    
	    
	    JTable table = null;

	    
	    
	    
		

		public ZhiboEventsDetailsWindow()  
	    {  
			setTitle("智博注单");  
			
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
		            		

		            			
		            			MergeManager.zhiboSelectedRow = detailsData.elementAt(focusedRowIndex);
		            			System.out.println(Arrays.toString(MergeManager.zhiboSelectedRow));

		            			MergeManager.showMergeWnd(true);
		            			
		            			MergeManager.zhiboSelectedRow = null;
		            			MergeManager.p8SelectedRow = null;

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

		            			MergeManager.zhiboSelectedRow = detailsData.elementAt(focusedRowIndex);
		            			System.out.println(Arrays.toString(MergeManager.zhiboSelectedRow));

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
		           
		           if(MergeManager.p8SelectedRow == null){
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
				
				
				scoreDetails = StoneAge.score.getFinalScoresDetails();

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
					if(originalDetailsData.elementAt(i)[ZHIBOINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
						DetailsDatatmp.add(originalDetailsData.elementAt(i));
					}
				}
			}
			
			//只显示单式盘
			if(bonlyShowNotInplay == true){
				for(int i = 0; i < originalDetailsData.size(); i++){
					if(!originalDetailsData.elementAt(i)[ZHIBOINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
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
					if(ZhiboManager.isInShowLeagueName(DetailsDatatmp.elementAt(i)[ZHIBOINDEX.LEAGUENAME.ordinal()])){
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
				String leagueName = DetailsDatatmp1.elementAt(i)[ZHIBOINDEX.LEAGUENAME.ordinal()];
				
				if(P8Http.isInShowLeagueName(leagueName) || true){
					double betAmt1 = Double.parseDouble(DetailsDatatmp1.elementAt(i)[ZHIBOINDEX.PERIOD0HOME.ordinal()]);
					double betAmt2 = Double.parseDouble(DetailsDatatmp1.elementAt(i)[ZHIBOINDEX.PERIOD0OVER.ordinal()]);
					double betAmt3 = Double.parseDouble(DetailsDatatmp1.elementAt(i)[ZHIBOINDEX.PERIOD1HOME.ordinal()]);
					double betAmt4 = Double.parseDouble(DetailsDatatmp1.elementAt(i)[ZHIBOINDEX.PERIOD1HOME.ordinal()]);
					
					if(Math.abs(betAmt1) > p0hhideNum || Math.abs(betAmt2) > p0ohideNum){
						//
						
						DetailsDatatmp2.add(DetailsDatatmp1.elementAt(i));
						//DetailsDatatmp2.add(DetailsDatatmp1.elementAt(1000));
					}
					
					
				}
				
				
			}
			
			detailsData = (Vector<String[]>)DetailsDatatmp2.clone();
			
			
			
			
			if(showItemVec.size()!= 0){
				showItemVec.clear();
			}
			
			
			//合并score
			for(int i = 0; i < detailsData.size(); i++){
				
				String[] olditem = detailsData.elementAt(i).clone();
				
				String zhibohometeam = olditem[TYPEINDEX.EVENTNAMNE.ordinal()].split(" vs ")[0];
				String zhiboawayteam = olditem[TYPEINDEX.EVENTNAMNE.ordinal()].split(" vs ")[1];
				
				String[] item = {Integer.toString(i+1), olditem[TYPEINDEX.LEAGUENAME.ordinal()], olditem[TYPEINDEX.TIME.ordinal()], olditem[TYPEINDEX.EVENTNAMNE.ordinal()], "", "", "",
						olditem[TYPEINDEX.PERIOD0HOME.ordinal()], "", "", "", olditem[TYPEINDEX.PERIOD0OVER.ordinal()], ""};
				
				String scorehometeam = MergeManager.findScoreTeambyzhiboteam(zhibohometeam);
				if(scorehometeam != null){
					
					String scoreawayteam = MergeManager.findScoreTeambyzhiboteam(zhiboawayteam);

					if(scoreawayteam != null){
						
						int indexinscoredetails = -1;
						for(int j = 0; j < scoreDetails.size(); j++){
							if(scoreDetails.elementAt(j)[SCORENEWINDEX.EVENTNAMNE.ordinal()].equals(scorehometeam + " vs " + scoreawayteam) ){
								indexinscoredetails = j;
								break;
							}
						}
						
						if(indexinscoredetails != -1){
							
							item[ZHIBOTABLEHEADINDEX.RQCHUPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQCHUPAN.ordinal()];
							item[ZHIBOTABLEHEADINDEX.RQZHONGPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQZHONGPAN.ordinal()];
							item[ZHIBOTABLEHEADINDEX.RQPANAS.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQPANAS.ordinal()];
							item[ZHIBOTABLEHEADINDEX.DXQCHUPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQCHUPAN.ordinal()];
							item[ZHIBOTABLEHEADINDEX.DXQZHONGPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQZHONGPAN.ordinal()];
							item[ZHIBOTABLEHEADINDEX.DXQPANAS.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQPANAS.ordinal()];
							item[ZHIBOTABLEHEADINDEX.SCORE.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.SCORE.ordinal()];
							
							
							
							
							
							//给出赌哪边的提示
							
							//让球盘结果分析
							if(!item[ZHIBOTABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("") && !item[ZHIBOTABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("-")
									&& !item[ZHIBOTABLEHEADINDEX.RQPANAS.ordinal()].equals("")){
								
								System.out.println("p8 events:" + Arrays.toString(item));
								
								
								

								
								String betside = "";
								
								
								
								int p0home = 0;
								if(item[ZHIBOTABLEHEADINDEX.P0HOME.ordinal()].contains("=")){
									p0home = Integer.parseInt(item[ZHIBOTABLEHEADINDEX.P0HOME.ordinal()].split("=")[1]);
								}else{
									p0home = Integer.parseInt(item[ZHIBOTABLEHEADINDEX.P0HOME.ordinal()]);
								}
								
								
								
								
								//只统计赌降的那一边
								if(item[ZHIBOTABLEHEADINDEX.RQPANAS.ordinal()].contains("降") && 
										item[ZHIBOTABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
										p0home  > 0){
									betside = "right";
								}else if(item[ZHIBOTABLEHEADINDEX.RQPANAS.ordinal()].contains("降") && 
										!item[ZHIBOTABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
										p0home  < 0){
									betside = "left";
								}else if(item[ZHIBOTABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
										item[ZHIBOTABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
										p0home  < 0){
									betside = "left";
								}else if(item[ZHIBOTABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
										!item[ZHIBOTABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
										p0home  > 0){
									betside = "right";
								}
								
								String[] oldteams = item[ZHIBOTABLEHEADINDEX.EVENTNAME.ordinal()].split(" vs ");
								
								if(betside.equals("right")){
									
									item[ZHIBOTABLEHEADINDEX.EVENTNAME.ordinal()] = "<html>" + oldteams[0] + " vs " +  "<font color='red'>" + oldteams[1] + "</font>";
								}else if(betside.equals("left")){
									item[ZHIBOTABLEHEADINDEX.EVENTNAME.ordinal()] = "<html>" +"<font color='red'>" + oldteams[0] + "</font>" + " vs " + oldteams[1];
								}

								
							}
							
							
							
							//大小球盘结果分析
							if(!item[ZHIBOTABLEHEADINDEX.DXQZHONGPAN.ordinal()].equals("") && !item[ZHIBOTABLEHEADINDEX.DXQZHONGPAN.ordinal()].equals("-")
									&& !item[ZHIBOTABLEHEADINDEX.DXQPANAS.ordinal()].equals("")){
								
								
								
								
								int p0over = 0;
								if(item[ZHIBOTABLEHEADINDEX.P0OVER.ordinal()].contains("=")){
									p0over = Integer.parseInt(item[ZHIBOTABLEHEADINDEX.P0OVER.ordinal()].split("=")[1]);
								}else{
									p0over = Integer.parseInt(item[ZHIBOTABLEHEADINDEX.P0OVER.ordinal()]);
								}
								
								
								

								String bet = "";
								
								//只统计赌降的一边
								if(item[ZHIBOTABLEHEADINDEX.DXQPANAS.ordinal()].contains("降") && 
										p0over  < 0){
									bet = "大";
								}else if(item[ZHIBOTABLEHEADINDEX.DXQPANAS.ordinal()].contains("升") && 
										p0over  > 0){
									bet = "小";
								}

								
								
								
								if(!bet.equals("")){
									
									item[ZHIBOTABLEHEADINDEX.DXQPANAS.ordinal()] = "<html>" + "<font color='red'>" + item[ZHIBOTABLEHEADINDEX.DXQPANAS.ordinal()] + "</font>";
								}
								
							}
							
							
							
							
							
							
							
							
							
							
						}
						
						
					}
					
				}
				
				showItemVec.add(item);
				
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
			
			JPanel panelNorth = new JPanel(new GridLayout(4, 4));

	        container.add(panelNorth, BorderLayout.NORTH);  
	        
	        jcb.setSelectedIndex(0);
	        
	        jcb.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
	                String content = jcb.getSelectedItem().toString();
	                
	                StoneAge.setZhiboSleepTime(Integer.parseInt(content));
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
/*	        panelNorth.add(onlyShowInplay);
	        panelNorth.add(onlyShowNotInplay);*/

	        //调整框位置
	        panelNorth.add(labelGrabStatkong1);
	        panelNorth.add(labelGrabStatkong2);
	        
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
	        
	        
	        
	        
		  
		    
		    table.setRowHeight(30);
		    
		    table.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));
		    
		    
	        table.getColumnModel().getColumn(ZHIBOTABLEHEADINDEX.INDEX.ordinal()).setPreferredWidth(40);//序号
	        table.getColumnModel().getColumn(ZHIBOTABLEHEADINDEX.LEAGUE.ordinal()).setPreferredWidth(180);;//联赛
	        table.getColumnModel().getColumn(ZHIBOTABLEHEADINDEX.TIME.ordinal()).setPreferredWidth(140);//时间
	        table.getColumnModel().getColumn(ZHIBOTABLEHEADINDEX.EVENTNAME.ordinal()).setPreferredWidth(270);//球队
	        table.getColumnModel().getColumn(ZHIBOTABLEHEADINDEX.P0HOME.ordinal()).setPreferredWidth(300);
	        table.getColumnModel().getColumn(ZHIBOTABLEHEADINDEX.P0OVER.ordinal()).setPreferredWidth(300);
	        
	        table.getColumnModel().getColumn(ZHIBOTABLEHEADINDEX.RQCHUPAN.ordinal()).setPreferredWidth(110);
	        table.getColumnModel().getColumn(ZHIBOTABLEHEADINDEX.RQZHONGPAN.ordinal()).setPreferredWidth(110);

		    
	        //hide column
		    Vector<Integer> hideColumn = new  Vector<Integer>();
		    hideColumn.add(4);
		    hideColumn.add(5);
		    hideColumn.add(6);
		    hideColumn.add(8);
		    hideColumn.add(9);
		    hideColumn.add(10);
		    hideColumn.add(12);
		    for(int i = 0; i < hideColumn.size(); i++) {
		        table.getTableHeader().getColumnModel().getColumn(hideColumn.elementAt(i)).setMaxWidth(0);
		        table.getTableHeader().getColumnModel().getColumn(hideColumn.elementAt(i)).setMinWidth(0);
		        table.getTableHeader().getColumnModel().getColumn(hideColumn.elementAt(i)).setPreferredWidth(0);
		        table.getTableHeader().getColumnModel().getColumn(hideColumn.elementAt(i)).setResizable(false);
		    }
		    //hide column end
		    
		    //table.setColumnModel(columnModel);
		    
		    //tableMode.
		    
		    
		    //设置列单元格渲染模式  开始
	        TableColumn p0hColumn = table.getColumn("全场让球");   
	        TableColumn p0oColumn = table.getColumn("全场大小");   


	        //绘制月薪列的字体颜色   

	        DefaultTableCellRenderer p0hRender = new DefaultTableCellRenderer() {   

	            public void setValue(Object value) { //重写setValue方法，从而可以动态设置列单元字体颜色   

	               
	            	String str = value.toString();
					Double betAmt = Double.parseDouble(str);
					
					
					if(Math.abs(betAmt) > p0hhiglightBigNum){
						setForeground(Color.red);
						
					}else{
						setForeground(Color.black);
						
					}
					
					setText((value == null) ? "" : value.toString());

					if(Math.abs(betAmt) < p0hhideNum){
						setForeground(Color.black);
						setText("0");
					}


	            }   

	        };   
	        
	        DefaultTableCellRenderer p0oRender = new DefaultTableCellRenderer() {   

	            public void setValue(Object value) { //重写setValue方法，从而可以动态设置列单元字体颜色   
	            	String str = value.toString();
					Double betAmt = Double.parseDouble(str);
					
					
					if(Math.abs(betAmt) > p0ohiglightBigNum){
						setForeground(Color.red);
						
					}else{
						setForeground(Color.black);
						
					}
					
					setText((value == null) ? "" : value.toString());

					if(Math.abs(betAmt) < p0ohideNum){
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
	    
	    

	    

	    
	    
	    
	  
	    private class MyTableModel extends AbstractTableModel  
	    {  
	        /* 
	         * 这里和刚才一样，定义列名和每个数据的值 
	         */  
	    	
	    	// { "联赛", "时间", "球队", "全场让球", "全场大小"};  
	        String[] columnNames =  
	       
	        { "序号", "联赛", "时间", "球队", "让球初盘", "终盘", "盘口分析", "全场让球", "大小球初盘", "终盘", "盘口分析","全场大小", "比分"};  
	        

	        
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
	        	if(showItemVec == null){
	        		return 0;
	        	}
	            return showItemVec.size();  
	        }  
	  
	        /** 
	         * 得到数据所对应对象 
	         */  
	        @Override  
	        public Object getValueAt(int rowIndex, int columnIndex)  
	        {  
	            //return data[rowIndex][columnIndex];
	        	return showItemVec.elementAt(rowIndex)[columnIndex];
	        }  
	  
	        /** 
	         * 得到指定列的数据类型 
	         */  
	        @Override  
	        public Class<?> getColumnClass(int columnIndex)  
	        {  
	            return showItemVec.elementAt(0)[columnIndex].getClass();
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
