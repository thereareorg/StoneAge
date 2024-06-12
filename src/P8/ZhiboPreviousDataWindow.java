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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;  
  
import java.awt.Color;













import javax.swing.JCheckBox;
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
import javax.swing.table.TableColumn;

import java.util.Date;      

import javax.swing.Timer;

import org.json.JSONArray;
import org.json.JSONObject;














import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Stack;


enum ZHIBOPRETABLEHEADINDEX{
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
	SCORE,
	RQPRES,
	DXQRES
}


public class ZhiboPreviousDataWindow extends PreviousDataWindow{

		private static final long serialVersionUID = 508685938515366544L;
		
		private  Vector<String[]> detailsData = null;
		
		private Vector<String[]> originalDetailsData = new Vector<String[]>();
		
		
	    private Vector<String[]> showItemVec = new Vector<String[]>();
	    
	    private Vector<String[]> scoreDetails = new Vector<String[]>();
		
		
		private Vector<Integer> hightlightRows = new Vector<Integer>();
		
	    private JLabel labelHighlightNum = new JLabel("让球高亮金额:");
	    private JTextField textFieldHighlightNum = new JTextField(15);  
	    
	    private JLabel labelp0oHighlightNum = new JLabel("大小球高亮金额:");
	    private JTextField textFieldp0oHighlightNum = new JTextField(15);  
	    
	    private JLabel labelInterval = new JLabel("日期选择:");
	    
	    String str1[] = {"1", "2","3","4","5"};
	    
	    private JComboBox jcb = new JComboBox(str1); 
	    
	    DateChooser mp = new DateChooser("yyyy-MM-dd", this);
	    
	    
	    private JLabel labelHideNum = new JLabel("让球隐藏金额:");
	    private JTextField textFieldHideNum = new JTextField(15); 
	    
	    private JLabel labelp0oHideNum = new JLabel("大小球隐藏金额:");
	    private JTextField textFieldp0oHideNum = new JTextField(15); 
	    
	    private JCheckBox onlyShow5Big = new JCheckBox("只看五大联赛,欧冠");
	    private JCheckBox onlyShowInplay = new JCheckBox("只看滚动盘");
	    private JCheckBox onlyShowNotInplay = new JCheckBox("只看单式盘");
	    
	    private boolean bonlyShow5Big = false;
	    private boolean bonlyShowInplay = false;
	    private boolean bonlyShowNotInplay = false;
	    
	    private JCheckBox chupanrescb = new JCheckBox("初盘输赢");
	    private boolean bchupanres = false;
	    private JCheckBox zhongpanrescb = new JCheckBox("终盘输赢");
	    private boolean bzhongpanres = true;
	    
	    private JLabel labelGrabStat= new JLabel("状态:");
	    private JTextField textFieldGrabStat = new JTextField(15);  
	    
		
	    Double p0hhiglightBigNum = 1000000.0;
	    
	    Double p0hhideNum = 0.0;
	    
	    Double p0ohiglightBigNum = 1000000.0;
	    
	    Double p0ohideNum = 0.0;

	    
	    
	/*    private JLabel labeltime = new JLabel("距封盘:");
	    private JTextField textFieldtime = new JTextField(15);  
	    
	    private AtomicLong remainTime = new AtomicLong(0);*/
	    
	    
	    MyTableModelZhibo tableMode = new MyTableModelZhibo();
	    
	    
	    JTable table = null;

	    
	    
		

		public ZhiboPreviousDataWindow()  
	    {  
			setTitle("LL历史注单");  
			
	        intiComponent();  
	        
	    }  
		
		
		public void setStateText(String txt){
			textFieldGrabStat.setText(txt);
		}
		

	    public void getscoresdetails(){
	    	scoreDetails = StoneAge.score.getpreviousdetailsbyday(mp.getChooseDate());
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
					String timeStr = originalDetailsData.elementAt(i)[ZHIBOINDEX.TIME.ordinal()];
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
						if(ZhiboManager.isInShowLeagueName(DetailsDatatmp.elementAt(i)[TYPEINDEX.LEAGUENAME.ordinal()])){
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
						double betAmt1 = Double.parseDouble(DetailsDatatmp1.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()]);
						double betAmt2 = Double.parseDouble(DetailsDatatmp1.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()]);
						double betAmt3 = Double.parseDouble(DetailsDatatmp1.elementAt(i)[TYPEINDEX.PERIOD1HOME.ordinal()]);
						double betAmt4 = Double.parseDouble(DetailsDatatmp1.elementAt(i)[TYPEINDEX.PERIOD1HOME.ordinal()]);
						
						if(Math.abs(betAmt1) > p0hhideNum || Math.abs(betAmt2) > p0ohideNum){
							//
							
							DetailsDatatmp2.add(DetailsDatatmp1.elementAt(i));
							
						}
						
						
					}
					
					
				}
				
				detailsData = (Vector<String[]>)DetailsDatatmp2.clone();
				
				
				
				
				
				
				getscoresdetails();
				
				if(showItemVec.size()!= 0){
					showItemVec.clear();
				}
				
				//合并score
				for(int i = 0; i < detailsData.size(); i++){
					
					String[] olditem = detailsData.elementAt(i).clone();
					
					String zhibohometeam = olditem[TYPEINDEX.EVENTNAMNE.ordinal()].split(" vs ")[0];
					String zhiboawayteam = olditem[TYPEINDEX.EVENTNAMNE.ordinal()].split(" vs ")[1];
					
					String[] item = {Integer.toString(i+1), olditem[TYPEINDEX.LEAGUENAME.ordinal()], olditem[TYPEINDEX.TIME.ordinal()], olditem[TYPEINDEX.EVENTNAMNE.ordinal()], "", "", "",
							olditem[TYPEINDEX.PERIOD0HOME.ordinal()], "", "", "", olditem[TYPEINDEX.PERIOD0OVER.ordinal()], "", "", ""};
					
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
								item[ZHIBOPRETABLEHEADINDEX.RQCHUPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQCHUPAN.ordinal()];
								item[ZHIBOPRETABLEHEADINDEX.RQZHONGPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQZHONGPAN.ordinal()];
								item[ZHIBOPRETABLEHEADINDEX.RQPANAS.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQPANAS.ordinal()];
								item[ZHIBOPRETABLEHEADINDEX.DXQCHUPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQCHUPAN.ordinal()];
								item[ZHIBOPRETABLEHEADINDEX.DXQZHONGPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQZHONGPAN.ordinal()];
								item[ZHIBOPRETABLEHEADINDEX.DXQPANAS.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQPANAS.ordinal()];
								
								
								
								
								if(scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.STATUS.ordinal()].contains("完")){
									
									item[ZHIBOPRETABLEHEADINDEX.SCORE.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.SCORE.ordinal()];
									
									//让球盘结果分析
									if(!item[ZHIBOPRETABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("") && !item[ZHIBOPRETABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("-")){
										
										System.out.println(Arrays.toString(item));
										
										
										String calRespan = "";
										if(bchupanres == true){
											calRespan = item[ZHIBOPRETABLEHEADINDEX.RQCHUPAN.ordinal()];
										}else{
											calRespan = item[ZHIBOPRETABLEHEADINDEX.RQZHONGPAN.ordinal()];
										}
										
										
										double calPan = rqpmap.get(calRespan.replace("受让", ""));
										double scoreh = Double.parseDouble(item[ZHIBOPRETABLEHEADINDEX.SCORE.ordinal()].split(":")[0]);
										double scorec = Double.parseDouble(item[ZHIBOPRETABLEHEADINDEX.SCORE.ordinal()].split(":")[1]);
										
										String rqres = "";
										if(calRespan.contains("受让")){
											calPan = 0.0 - calPan;
										}
										
										
										int p0home = 0;
										
										p0home = Integer.parseInt(item[ZHIBOPRETABLEHEADINDEX.P0HOME.ordinal()]);

										if((scoreh - scorec-calPan) >=0.5){
											if(p0home > 0){
												rqres = "全输";
											}else if(p0home == 0){
												rqres = "-";
											}else{
												rqres = "全赢";
											}
											
										}else if((scoreh - scorec-calPan) == 0.25){
											
											if(p0home > 0){
												rqres = "输半";
											}else if(p0home == 0){
												rqres = "-";
											}else{
												rqres = "赢半";
											}
											
										}else if((scoreh - scorec-calPan) == -0.25){
											
											if(p0home > 0){
												rqres = "赢半";
											}else if(p0home == 0){
												rqres = "-";
											}else{
												rqres = "输半";
											}

										}else if((scoreh - scorec-calPan) <= -0.5){
											
											if(p0home > 0){
												rqres = "全赢";
											}else if(p0home == 0){
												rqres = "-";
											}else{
												rqres = "全输";
											}
											
											
										}else if((scoreh - scorec-calPan) == 0.0){
											rqres = "走水";
										}
										
										
										
										//只统计赌降的那一边
										if(item[ZHIBOPRETABLEHEADINDEX.RQPANAS.ordinal()].contains("降") && 
												item[ZHIBOPRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												Integer.parseInt(item[ZHIBOPRETABLEHEADINDEX.P0HOME.ordinal()])  < 0){
											rqres = "";
										}else if(item[ZHIBOPRETABLEHEADINDEX.RQPANAS.ordinal()].contains("降") && 
												!item[ZHIBOPRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												Integer.parseInt(item[ZHIBOPRETABLEHEADINDEX.P0HOME.ordinal()])  > 0){
											rqres = "";
										}else if(item[ZHIBOPRETABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
												item[ZHIBOPRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												Integer.parseInt(item[ZHIBOPRETABLEHEADINDEX.P0HOME.ordinal()])  > 0){
											rqres = "";
										}else if(item[ZHIBOPRETABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
												!item[ZHIBOPRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												Integer.parseInt(item[ZHIBOPRETABLEHEADINDEX.P0HOME.ordinal()])  < 0){
											rqres = "";
										}else if(item[ZHIBOPRETABLEHEADINDEX.RQPANAS.ordinal()].equals("")){
											rqres = "";
										}
										
										
										item[ZHIBOPRETABLEHEADINDEX.RQPRES.ordinal()] = rqres;
										
									}
									
									//大小球盘结果分析
									if(!item[ZHIBOPRETABLEHEADINDEX.DXQZHONGPAN.ordinal()].equals("") && !item[ZHIBOPRETABLEHEADINDEX.DXQZHONGPAN.ordinal()].equals("-")){
										double calPan = 0.0;
										
										
										
										double scoreh = Double.parseDouble(item[ZHIBOPRETABLEHEADINDEX.SCORE.ordinal()].split(":")[0]);
										double scorec = Double.parseDouble(item[ZHIBOPRETABLEHEADINDEX.SCORE.ordinal()].split(":")[1]);
										
										String dxqres = "";
										
										String calRespan ="";
										
										if(bchupanres == true){
											calRespan = item[ZHIBOPRETABLEHEADINDEX.DXQCHUPAN.ordinal()];
										}else{
											calRespan = item[ZHIBOPRETABLEHEADINDEX.DXQZHONGPAN.ordinal()];
										}
										
										
										
										if(calRespan.contains("/")){
											calPan = (Double.parseDouble(calRespan.split("/")[0]) + 
													Double.parseDouble(calRespan.split("/")[1]))/2;
										}else{
											calPan = Double.parseDouble(calRespan);
										}
										
										
										int p0over = 0;
										p0over = Integer.parseInt(item[ZHIBOPRETABLEHEADINDEX.P0OVER.ordinal()]);
										
										
										if((scoreh + scorec-calPan) >=0.5){
											if(p0over > 0){
												dxqres = "全输";
											}else if(p0over == 0){
												dxqres = "-";
											}else{
												dxqres = "全赢";
											}

											
										}else if((scoreh + scorec-calPan) == 0.25){
											if(p0over > 0){
												dxqres = "输半";
											}else if(p0over == 0){
												dxqres = "-";
											}else{
												dxqres = "赢半";
											}
										}else if((scoreh + scorec-calPan) == -0.25){
											
											if(p0over > 0){
												dxqres = "赢半";
											}else if(p0over == 0){
												dxqres = "-";
											}else{
												dxqres = "输半";
											}
											

										}else if((scoreh + scorec-calPan) <= -0.5){
											
											if(p0over > 0){
												dxqres = "全赢";
											}else if(p0over == 0){
												dxqres = "-";
											}else{
												dxqres = "全输";
											}

										}else if((scoreh + scorec-calPan) == 0.0){
											dxqres = "走水";
										}
										
										
										//只统计赌降的一边
										if(item[ZHIBOPRETABLEHEADINDEX.DXQPANAS.ordinal()].contains("降") && 
												Integer.parseInt(item[ZHIBOPRETABLEHEADINDEX.P0OVER.ordinal()])  > 0){
											dxqres = "";
										}else if(item[ZHIBOPRETABLEHEADINDEX.DXQPANAS.ordinal()].contains("升") && 
												Integer.parseInt(item[ZHIBOPRETABLEHEADINDEX.P0OVER.ordinal()])  < 0){
											dxqres = "";
										}else if(item[ZHIBOPRETABLEHEADINDEX.DXQPANAS.ordinal()].equals("")){
											dxqres = "";
										}
										
										
										
										item[ZHIBOPRETABLEHEADINDEX.DXQRES.ordinal()] = dxqres;
										
									}
								}
								
								
								

								
								

								
							}
							
							
						}
						
					}
					
					showItemVec.add(item);
					
				}
				
				
				
				
				
				
				
				
				
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
	        
	        
	        
	        chupanrescb.setSelected(false);
	        
	        chupanrescb.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
					if(e.getStateChange() == ItemEvent.DESELECTED){
						bchupanres = false;
					}else{
						bchupanres = true;
						bzhongpanres = false;
						zhongpanrescb.setSelected(false);
					}
					
					updateShowItem();
				}
	        });
	        
	        zhongpanrescb.setSelected(true);
	        
	        zhongpanrescb.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
					if(e.getStateChange() == ItemEvent.DESELECTED){
						bzhongpanres = false;
					}else{
						bzhongpanres = true;
						bchupanres = false;
						chupanrescb.setSelected(false);
					}
					
					updateShowItem();
				}
	        });
	        
	        
	        
	        panelNorth.add(labelInterval);
	        panelNorth.add(mp);

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

	        
	        
	        panelNorth.add(chupanrescb);
	        panelNorth.add(zhongpanrescb);

	        
	        
	        
	        
	/*        panelNorth.add(labeltime);
	        panelNorth.add(textFieldtime);
	        textFieldjinrichazhi.setEditable(false);*/


		    
			
		    table = new JTable(tableMode);

	        JScrollPane scroll = new JScrollPane(table);  
	        
	        
		    
		    
		    table.setRowHeight(30);
		    
		    table.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));
		    
		    
	        table.getColumnModel().getColumn(ZHIBOPRETABLEHEADINDEX.INDEX.ordinal()).setPreferredWidth(40);//序号
	        table.getColumnModel().getColumn(ZHIBOPRETABLEHEADINDEX.LEAGUE.ordinal()).setPreferredWidth(180);;//联赛
	        table.getColumnModel().getColumn(ZHIBOPRETABLEHEADINDEX.TIME.ordinal()).setPreferredWidth(140);//时间
	        table.getColumnModel().getColumn(ZHIBOPRETABLEHEADINDEX.EVENTNAME.ordinal()).setPreferredWidth(270);//球队
	        table.getColumnModel().getColumn(ZHIBOPRETABLEHEADINDEX.P0HOME.ordinal()).setPreferredWidth(300);
	        table.getColumnModel().getColumn(ZHIBOPRETABLEHEADINDEX.P0OVER.ordinal()).setPreferredWidth(300);
		    
		    
	        table.getColumnModel().getColumn(ZHIBOPRETABLEHEADINDEX.RQCHUPAN.ordinal()).setPreferredWidth(110);
	        table.getColumnModel().getColumn(ZHIBOPRETABLEHEADINDEX.RQZHONGPAN.ordinal()).setPreferredWidth(110);
		    
		    
	        
	        //hide column
		    Vector<Integer> hideColumn = new  Vector<Integer>();
		    hideColumn.add(4);
		    hideColumn.add(5);
		    hideColumn.add(6);
		    hideColumn.add(8);
		    hideColumn.add(9);
		    hideColumn.add(10);
		    hideColumn.add(12);
		    hideColumn.add(13);
		    hideColumn.add(14);
		    
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
	    
	    

	    

	    
	    
	    
	  
	    private class MyTableModelZhibo extends AbstractTableModel  
	    {  
	        /* 
	         * 这里和刚才一样，定义列名和每个数据的值 
	         */  
	        String[] columnNames =  
	            {"序号", "联赛", "时间", "球队","让球初盘","终盘","盘口分析", "全场让球", "大小初盘","终盘","盘口分析","全场大小","完场比分","盘口结果","大小结果",};  
	        

	        
	        //Object[][] data = new Object[2][5];  
	        
	        

	        
	  
	        /** 
	         * 构造方法，初始化二维数组data对应的数据 
	         */  
	        public MyTableModelZhibo()  
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
	        	if(null == showItemVec){
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
	        	showItemVec.elementAt(rowIndex)[columnIndex] = (String)aValue;  
	            /*通知监听器数据单元数据已经改变*/  
	            fireTableCellUpdated(rowIndex, columnIndex);  
	        }  
	        
	        public void updateTable(){
	        	fireTableDataChanged();
	        }
	        
	  
	    }  
}
