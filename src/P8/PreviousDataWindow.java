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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
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





import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.util.Date;      

import javax.swing.Timer;

import org.json.JSONArray;
import org.json.JSONObject;














import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Stack;



enum P8PRETABLEHEADINDEX{
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




public class PreviousDataWindow extends JFrame  
{  
	
	Map<String, Double> rqpmap = new HashMap<String, Double>();
  
   
	private static final long serialVersionUID = 508685938515369544L;
	
	private  Vector<String[]> detailsData = null;
	
	private Vector<String[]> originalDetailsData = new Vector<String[]>();
	
	private Vector<Integer> hightlightRows = new Vector<Integer>();
	
	
    private JLabel labelHighlightNum = new JLabel("让球高亮金额:");
    private JTextField textFieldHighlightNum = new JTextField(15);  
    
    private JLabel labelp0oHighlightNum = new JLabel("大小球高亮金额:");
    private JTextField textFieldp0oHighlightNum = new JTextField(15);  

    private Vector<String[]> showItemVec = new Vector<String[]>();
    
    private Vector<String[]> scoreDetails = new Vector<String[]>();
    
    
    private JLabel labelInterval = new JLabel("日期选择:");
    
    String str1[] = {"1", "2","3","4","5"};
    
    private JComboBox jcb = new JComboBox(str1); 
    
    DateChooser mp = new DateChooser("yyyy-MM-dd", this);
    
    
    private JLabel labelHideNum = new JLabel("让球隐藏金额:");
    private JTextField textFieldHideNum = new JTextField(15); 
    
    private JLabel labelp0oHideNum = new JLabel("大小球隐藏金额:");
    private JTextField textFieldp0oHideNum = new JTextField(15); 
    
    private JLabel labelInplayHideNum = new JLabel("走地让球隐藏金额:");
    private JTextField textFieldInplayHideNum = new JTextField(15); 
    
    private JLabel labelp0oInplayHideNum = new JLabel("走地大小球隐藏金额:");
    private JTextField textFieldp0oInplayHideNum = new JTextField(15); 
    
    
    private JLabel labelInplayDirNum = new JLabel("走地让球方向金额:");
    private JTextField textFieldInplayDirNum = new JTextField(15); 
    
    private JLabel labelp0oInplayDirNum = new JLabel("走地大小球方向金额:");
    private JTextField textFieldp0oInplayDirNum = new JTextField(15); 
    
    
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

    Double p0hInplayhideNum = 1.0;
    
    Double p0oInplayhideNum = 1.0;
    
    Double p0hInplayDirNum = 300000.0;
    
    Double p0oInplayDirNum = 300000.0;
    
    
    
/*    private JLabel labeltime = new JLabel("距封盘:");
    private JTextField textFieldtime = new JTextField(15);  
    
    private AtomicLong remainTime = new AtomicLong(0);*/
    
    
    MyTableModel tableMode = new MyTableModel();
    
    
    JTable table = null;


    
    public void getscoresdetails(){
    	scoreDetails = StoneAge.score.getpreviousdetailsbyday(mp.getChooseDate());
    }
    
    
	

	public PreviousDataWindow()  
    {  
		setTitle("PP历史");  
		
        intiComponent(); 
        
        
		rqpmap.put("平手", 0.0);
		rqpmap.put("平手/半球", 0.25);
		rqpmap.put("半球", 0.5);
		rqpmap.put("半球/一球", 0.75);
		rqpmap.put("一球", 1.0);
		rqpmap.put("一球/球半", 1.25);
		rqpmap.put("球半", 1.5);
		rqpmap.put("球半/两球", 1.75);
		rqpmap.put("两球", 2.0);
		rqpmap.put("两球/两球半", 2.25);
		rqpmap.put("两球半", 2.5);
		rqpmap.put("两球半/三球", 2.75);
		rqpmap.put("三球", 3.0);
		rqpmap.put("三球/三球半", 3.25);
		rqpmap.put("三球半", 3.5);
		rqpmap.put("三球半/四球", 3.75);
		rqpmap.put("四球", 4.0);
		rqpmap.put("四球/四球半", 4.25);
		rqpmap.put("四球半", 4.5);
		rqpmap.put("四球半/五球", 4.75);
		rqpmap.put("五球", 5.0);
		rqpmap.put("五球/五球半", 5.25);
		rqpmap.put("五球半", 5.5);
		rqpmap.put("五球半/六球", 5.75);
		rqpmap.put("六球", 6.0);
		rqpmap.put("六球/六球半", 6.25);
		rqpmap.put("六球半", 6.5);
		rqpmap.put("六球半/七球", 6.75);
		rqpmap.put("七球", 7.0);
		rqpmap.put("七球/七球半", 7.25);
		rqpmap.put("七球半", 7.5);
		rqpmap.put("七球半/八球", 7.75);
		rqpmap.put("八球", 8.0);
		rqpmap.put("八球/八球半", 8.25);
		rqpmap.put("八球半", 8.5);
		rqpmap.put("八球半/九球", 8.75);
		rqpmap.put("九球", 9.0);
		rqpmap.put("九球/九球半", 9.25);
		rqpmap.put("九球半", 9.5);
		rqpmap.put("九球半/十球", 9.75);
		rqpmap.put("十球", 10.0);
        
    }  
	
	
	public void setStateText(String txt){
		textFieldGrabStat.setText(txt);
	}
	
/*	public void hightlightBigNumrows(){
		
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
				
				if(Math.abs(betAmt1) > higlightBigNum || Math.abs(betAmt2) > higlightBigNum){
					//
					
					hightlightRows.add(i);
					
				}
				
				
			}
			
			setOneRowBackgroundColor(table, 0, new Color(255, 100, 100));
		}
	}*/

	
	
	
	public void updateEventsDetails(Vector<String[]> eventDetailsVec){
		
		try{
			
			
			

			if(originalDetailsData.size() != 0){
				originalDetailsData.clear();
			}
			
			for(int i = 0; i< eventDetailsVec.size(); i++){
				originalDetailsData.add(eventDetailsVec.elementAt(i).clone());
			}
			
			

			for(int i = 0; i < originalDetailsData.size(); i++){
				String[] tmp = originalDetailsData.elementAt(i);
				if(tmp[TYPEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
					originalDetailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()] = "g" + originalDetailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()];
					originalDetailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()] = "g" + originalDetailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()];
				}
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
				String eventName = DetailsDatatmp1.elementAt(i)[TYPEINDEX.EVENTNAMNE.ordinal()];

				
				
				double betAmt1 = 0.0;
				double betAmt2 = 0.0;

				
				String bet1Str = DetailsDatatmp1.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()];
				String bet2Str = DetailsDatatmp1.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()];

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
				
				if(eventName.contains("滚动盘")){
					if(Math.abs(betAmt1) > p0hInplayhideNum || Math.abs(betAmt2) > p0oInplayhideNum){

						DetailsDatatmp2.add(DetailsDatatmp1.elementAt(i).clone());
					}
				}else{
					if(Math.abs(betAmt1) > p0hhideNum || Math.abs(betAmt2) > p0ohideNum){
						DetailsDatatmp2.add(DetailsDatatmp1.elementAt(i).clone());
					}
				}

				
			}
			

			
			
			
			detailsData = (Vector<String[]>)DetailsDatatmp2.clone();
			


			
			
        	
        	if(detailsData.size() != 0){
        		
        		Comparator cn = new EventNameCompare();  
        		Collections.sort(detailsData, cn);
        		
        		Comparator ct = new TimeCompare(); 
        		
        		Collections.sort(detailsData, ct);
        		
        	}
        	
        	
			for(int i = 0; i < detailsData.size(); i++){
				String[] tmp = detailsData.elementAt(i).clone();
				String name = tmp[TYPEINDEX.EVENTNAMNE.ordinal()];
				
				if(name.contains("滚动盘")){
					
					//System.out.println();
					
					for(int j = 0; j < detailsData.size(); j++){
						if(j == i){
							continue;
						}
						
						String[] tmp1 = detailsData.elementAt(j).clone();
						String name1 = tmp1[TYPEINDEX.EVENTNAMNE.ordinal()];
						if(name.contains(name1)){
							double betp0h = 0.0;
							double betp0o = 0.0;
							
							String betp0hStr = tmp[TYPEINDEX.PERIOD0HOME.ordinal()];
							String betp0oStr = tmp[TYPEINDEX.PERIOD0OVER.ordinal()];
							
							if(betp0hStr.contains("=")){
								String[] tmpArray = betp0hStr.split("=");
								betp0h = Double.parseDouble(tmpArray[1]);
							}
							
							if(betp0oStr.contains("=")){
								String[] tmpArray = betp0oStr.split("=");
								betp0o = Double.parseDouble(tmpArray[1]);
							}
							
							
							double betp0h1 = 0.0;
							double betp0o1 = 0.0;
							
							String betp0hStr1 = tmp1[TYPEINDEX.PERIOD0HOME.ordinal()];
							String betp0oStr1 = tmp1[TYPEINDEX.PERIOD0OVER.ordinal()];
							
							if(betp0hStr1.contains("=")){
								String[] tmpArray = betp0hStr1.split("=");
								betp0h1 = Double.parseDouble(tmpArray[1]);
							}
							
							if(betp0oStr1.contains("=")){
								String[] tmpArray = betp0oStr1.split("=");
								betp0o1 = Double.parseDouble(tmpArray[1]);
							}
							
							
							
							if((betp0h > p0hInplayhideNum && betp0h1 >p0hhideNum) || (betp0h < 0.0 -  p0hInplayhideNum && betp0h1 < 0.0 - p0hhideNum)){
								detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()] = "b" + detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()];
								detailsData.elementAt(j)[TYPEINDEX.PERIOD0HOME.ordinal()] = "b" + detailsData.elementAt(j)[TYPEINDEX.PERIOD0HOME.ordinal()];
							}
							
							if((betp0o > p0oInplayhideNum && betp0o1 >p0ohideNum) || (betp0o < 0.0 - p0oInplayhideNum && betp0o1 < 0.0 - p0ohideNum)){
								detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()] = "b" + detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()];
								detailsData.elementAt(j)[TYPEINDEX.PERIOD0OVER.ordinal()] = "b" + detailsData.elementAt(j)[TYPEINDEX.PERIOD0OVER.ordinal()];
							}
							
							break;
							
						}
						
					}
					
					
					//deal direction
					String p0hFlagStr = tmp[TYPEINDEX.PERIOD1HOME.ordinal()];
					if(!p0hFlagStr.equals("0")){
						String[] p0hFlags = p0hFlagStr.split("\\|");
						int flag = (int) (p0hInplayDirNum/P8Http.flagNumber);
						
						if(flag < 100){
							for(int k = 0; k < p0hFlags.length; k++){
								if(flag <= Math.abs(Integer.parseInt(p0hFlags[k]))){
									if(Integer.parseInt(p0hFlags[k]) < 0){
										detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()] = "↓" + detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()];
									}else{
										detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()] = "↑" + detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()];
									}
									
									break;
								}
							}
						}
						

					}
					
					
					String p0oFlagStr = tmp[TYPEINDEX.PERIOD1OVER.ordinal()];
					if(!p0oFlagStr.equals("0")){
						String[] p0oFlags = p0oFlagStr.split("\\|");
						int flag = (int) (p0oInplayDirNum/P8Http.flagNumber);
						
						if(flag < 100){
							for(int k = 0; k < p0oFlags.length; k++){
								if(flag <= Math.abs(Integer.parseInt(p0oFlags[k]))){
									if(Integer.parseInt(p0oFlags[k]) < 0){
										detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()] = "↓" + detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()];
									}else{
										detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()] = "↑" + detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()];
									}
									break;
								}
							}
						}
						

					}
					
					
					
				}
			}
			
			
			
			
			getscoresdetails();
			
			if(showItemVec.size()!= 0){
				showItemVec.clear();
			}
			
			//合并score
			for(int i = 0; i < detailsData.size(); i++){
				
				String[] olditem = detailsData.elementAt(i).clone();
				
				String p8hometaem = olditem[TYPEINDEX.EVENTNAMNE.ordinal()].split("-vs-")[0];
				String p8awaytaem = olditem[TYPEINDEX.EVENTNAMNE.ordinal()].split("-vs-")[1];
				
				String[] item = {Integer.toString(i+1), olditem[TYPEINDEX.LEAGUENAME.ordinal()], olditem[TYPEINDEX.TIME.ordinal()], olditem[TYPEINDEX.EVENTNAMNE.ordinal()], "", "", "",
						olditem[TYPEINDEX.PERIOD0HOME.ordinal()], "", "", "", olditem[TYPEINDEX.PERIOD0OVER.ordinal()], "", "", ""};
				
				String scorehometeam = ScoreMergeManager.findScoreTeam(p8hometaem);
				if(scorehometeam != null){
					String scoreawayteam = ScoreMergeManager.findScoreTeam(p8awaytaem);
					
					
					
					if(scoreawayteam != null){
						
						int indexinscoredetails = -1;
						for(int j = 0; j < scoreDetails.size(); j++){
							if(scoreDetails.elementAt(j)[SCORENEWINDEX.EVENTNAMNE.ordinal()].equals(scorehometeam + " vs " + scoreawayteam) ){
								indexinscoredetails = j;
								break;
							}
						}
						
						if(indexinscoredetails != -1){
							item[P8PRETABLEHEADINDEX.RQCHUPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQCHUPAN.ordinal()];
							item[P8PRETABLEHEADINDEX.RQZHONGPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQZHONGPAN.ordinal()];
							item[P8PRETABLEHEADINDEX.RQPANAS.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQPANAS.ordinal()];
							item[P8PRETABLEHEADINDEX.DXQCHUPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQCHUPAN.ordinal()];
							item[P8PRETABLEHEADINDEX.DXQZHONGPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQZHONGPAN.ordinal()];
							item[P8PRETABLEHEADINDEX.DXQPANAS.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQPANAS.ordinal()];
							
							
							
							
							if(scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.STATUS.ordinal()].contains("完")){
								
								item[P8PRETABLEHEADINDEX.SCORE.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.SCORE.ordinal()];
								
								//让球盘结果分析
								if(!item[P8PRETABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("") && !item[P8PRETABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("-")){
									
									System.out.println(Arrays.toString(item));
									
									
									
									String calRespan = "";
									if(bchupanres == true){
										calRespan = item[P8PRETABLEHEADINDEX.RQCHUPAN.ordinal()];
									}else{
										calRespan = item[P8PRETABLEHEADINDEX.RQZHONGPAN.ordinal()];
									}
									
									double calcpan = rqpmap.get(calRespan.replace("受让", ""));
									double scoreh = Double.parseDouble(item[P8PRETABLEHEADINDEX.SCORE.ordinal()].split(":")[0]);
									double scorec = Double.parseDouble(item[P8PRETABLEHEADINDEX.SCORE.ordinal()].split(":")[1]);
									
									String rqres = "";
									if(calRespan.contains("受让")){
										calcpan = 0.0 - calcpan;
									}
									
									
									int p0home = 0;
									if(item[P8PRETABLEHEADINDEX.P0HOME.ordinal()].contains("=")){
										p0home = Integer.parseInt(item[P8PRETABLEHEADINDEX.P0HOME.ordinal()].split("=")[1]);
									}
									
									
									if((scoreh - scorec-calcpan) >=0.5){
										if(p0home > 0){
											rqres = "全输";
										}else if(p0home == 0){
											rqres = "-";
										}else{
											rqres = "全赢";
										}
										
									}else if((scoreh - scorec-calcpan) == 0.25){
										
										if(p0home > 0){
											rqres = "输半";
										}else if(p0home == 0){
											rqres = "-";
										}else{
											rqres = "赢半";
										}
										
									}else if((scoreh - scorec-calcpan) == -0.25){
										
										if(p0home > 0){
											rqres = "赢半";
										}else if(p0home == 0){
											rqres = "-";
										}else{
											rqres = "输半";
										}

									}else if((scoreh - scorec-calcpan) <= -0.5){
										
										if(p0home > 0){
											rqres = "全赢";
										}else if(p0home == 0){
											rqres = "-";
										}else{
											rqres = "全输";
										}
										
										
									}else if((scoreh - scorec-calcpan) == 0.0){
										rqres = "走水";
									}
									
									//只统计赌降的那一边
									if(item[P8PRETABLEHEADINDEX.RQPANAS.ordinal()].contains("降") && 
											item[P8PRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
											p0home  < 0){
										rqres = "";
									}else if(item[P8PRETABLEHEADINDEX.RQPANAS.ordinal()].contains("降") && 
											!item[P8PRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
											p0home  > 0){
										rqres = "";
									}else if(item[P8PRETABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
											item[P8PRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
											p0home  > 0){
										rqres = "";
									}else if(item[P8PRETABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
											!item[P8PRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
											p0home  < 0){
										rqres = "";
									}else if(item[P8PRETABLEHEADINDEX.RQPANAS.ordinal()].equals("")){
										rqres = "";
									}
									
									
									
									item[P8PRETABLEHEADINDEX.RQPRES.ordinal()] = rqres;
									
								}
								
								//大小球盘结果分析
								if(!item[P8PRETABLEHEADINDEX.DXQZHONGPAN.ordinal()].equals("") && !item[P8PRETABLEHEADINDEX.DXQZHONGPAN.ordinal()].equals("-")){
									
									double calpan = 0.0;
									
									String calRespan = "";
									
									if(bchupanres == true){
										calRespan = item[P8PRETABLEHEADINDEX.DXQCHUPAN.ordinal()];
									}else{
										calRespan = item[P8PRETABLEHEADINDEX.DXQZHONGPAN.ordinal()];
									}
									
									
									double scoreh = Double.parseDouble(item[P8PRETABLEHEADINDEX.SCORE.ordinal()].split(":")[0]);
									double scorec = Double.parseDouble(item[P8PRETABLEHEADINDEX.SCORE.ordinal()].split(":")[1]);
									
									String dxqres = "";
									if(calRespan.contains("/")){
										calpan = (Double.parseDouble(calRespan.split("/")[0]) + 
												Double.parseDouble(calRespan.split("/")[1]))/2;
									}else{
										calpan = Double.parseDouble(calRespan);
									}
									
									
									int p0over = 0;
									if(item[P8PRETABLEHEADINDEX.P0OVER.ordinal()].contains("=")){
										p0over = Integer.parseInt(item[P8PRETABLEHEADINDEX.P0OVER.ordinal()].split("=")[1]);
									}
									
									
									
									if((scoreh + scorec-calpan) >=0.5){
										if(p0over > 0){
											dxqres = "全输";
										}else if(p0over == 0){
											dxqres = "-";
										}else{
											dxqres = "全赢";
										}

										
									}else if((scoreh + scorec-calpan) == 0.25){
										if(p0over > 0){
											dxqres = "输半";
										}else if(p0over == 0){
											dxqres = "-";
										}else{
											dxqres = "赢半";
										}
									}else if((scoreh + scorec-calpan) == -0.25){
										
										if(p0over > 0){
											dxqres = "赢半";
										}else if(p0over == 0){
											dxqres = "-";
										}else{
											dxqres = "输半";
										}
										

									}else if((scoreh + scorec-calpan) <= -0.5){
										
										if(p0over > 0){
											dxqres = "全赢";
										}else if(p0over == 0){
											dxqres = "-";
										}else{
											dxqres = "全输";
										}

									}else if((scoreh + scorec-calpan) == 0.0){
										dxqres = "走水";
									}
									
									//只统计赌降的一边
									if(item[P8PRETABLEHEADINDEX.DXQPANAS.ordinal()].contains("降") && 
											p0over  > 0){
										dxqres = "";
									}else if(item[P8PRETABLEHEADINDEX.DXQPANAS.ordinal()].contains("升") && 
											p0over  < 0){
										dxqres = "";
									}else if(item[P8PRETABLEHEADINDEX.DXQPANAS.ordinal()].equals("")){
										dxqres = "";
									}

									
									
									
									item[P8PRETABLEHEADINDEX.DXQRES.ordinal()] = dxqres;
									
								}
							}
							
							
							

							
							

							
						}
						
						
					}
					
				}
				
				showItemVec.add(item);
				
			}
        	

			//fittable();
			
			
			tableMode.updateTable();
		}catch(Exception e){
			e.printStackTrace();
		}
	
	
	

		
	}

	
	
	
    public void fittable(){
	    JTableHeader header = table.getTableHeader();
	     int rowCount = table.getRowCount();
	     Enumeration columns = table.getColumnModel().getColumns();
	     while(columns.hasMoreElements()){
	         TableColumn column = (TableColumn)columns.nextElement();
	         int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
	         int width = (int)table.getTableHeader().getDefaultRenderer()
	                 .getTableCellRendererComponent(table, column.getIdentifier()
	                         , false, false, -1, col).getPreferredSize().getWidth();
	         for(int row = 0; row<rowCount; row++){
	             int preferedWidth = (int)table.getCellRenderer(row, col).getTableCellRendererComponent(table,
	            		 table.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
	             width = Math.max(width, preferedWidth);
	         }
	         header.setResizingColumn(column); // 此行很重要
	         column.setWidth(width+table.getIntercellSpacing().width);
	    
	     }
    }

	
	
	
  
    /** 
     * 初始化窗体组件 
     */  
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel(new GridLayout(6, 4));

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
        
        
        textFieldInplayHideNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldInplayHideNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0hInplayhideNum = Double.parseDouble(value);
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
        
        
        textFieldp0oInplayHideNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldp0oInplayHideNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0oInplayhideNum = Double.parseDouble(value);
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
        
        
        
        
        textFieldInplayDirNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldInplayDirNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0hInplayDirNum = Double.parseDouble(value);
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
        
        textFieldInplayDirNum.setText(String.format("%.0f", p0hInplayDirNum));
        
        
        textFieldp0oInplayDirNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldp0oInplayDirNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0oInplayDirNum = Double.parseDouble(value);
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
        
        textFieldp0oInplayDirNum.setText(String.format("%.0f", p0oInplayDirNum));
        
        
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

        panelNorth.add(labelp0oInplayHideNum);
        panelNorth.add(textFieldp0oInplayHideNum);
        
        panelNorth.add(labelInplayHideNum);
        panelNorth.add(textFieldInplayHideNum);
        
        panelNorth.add(labelp0oInplayDirNum);
        panelNorth.add(textFieldp0oInplayDirNum);
        
        panelNorth.add(labelInplayDirNum);
        panelNorth.add(textFieldInplayDirNum);
        
        
        panelNorth.add(onlyShow5Big);
        panelNorth.add(onlyShowInplay);
        panelNorth.add(onlyShowNotInplay);

        panelNorth.add(chupanrescb);
        panelNorth.add(zhongpanrescb);
        
/*        panelNorth.add(labelGrabStat);
        panelNorth.add(textFieldGrabStat);*/
        
        
        
        
/*        panelNorth.add(labeltime);
        panelNorth.add(textFieldtime);
        textFieldjinrichazhi.setEditable(false);*/


	    
		
	    table = new JTable(tableMode);

        JScrollPane scroll = new JScrollPane(table);  
        
        
	   
	    
	    table.setRowHeight(30);
	    
	    table.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));
	    
	    
	    
	    
        table.getColumnModel().getColumn(P8PRETABLEHEADINDEX.INDEX.ordinal()).setPreferredWidth(40);//序号
        table.getColumnModel().getColumn(P8PRETABLEHEADINDEX.LEAGUE.ordinal()).setPreferredWidth(180);;//联赛
        table.getColumnModel().getColumn(P8PRETABLEHEADINDEX.TIME.ordinal()).setPreferredWidth(140);//时间
        table.getColumnModel().getColumn(P8PRETABLEHEADINDEX.EVENTNAME.ordinal()).setPreferredWidth(270);//球队
        table.getColumnModel().getColumn(P8PRETABLEHEADINDEX.P0HOME.ordinal()).setPreferredWidth(300);
        table.getColumnModel().getColumn(P8PRETABLEHEADINDEX.P0OVER.ordinal()).setPreferredWidth(300);
	    
	    
        table.getColumnModel().getColumn(P8PRETABLEHEADINDEX.RQCHUPAN.ordinal()).setPreferredWidth(110);
        table.getColumnModel().getColumn(P8PRETABLEHEADINDEX.RQZHONGPAN.ordinal()).setPreferredWidth(110);
	    
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
				

				
				setText((value == null) ? "" : showStr);

				Double hideNum = 0.0;
				
				if(str.contains("g")){
					hideNum = p0hInplayhideNum;
				}else{
					hideNum = p0hhideNum;
				}
				
				if(Math.abs(betAmt) < hideNum){
					setForeground(Color.black);
					setText("0");
				}
				
				if(str.contains("b")){
					setBackground(new Color(179,232,255));
				}else{
					setBackground(Color.white);
				}
					


            }   

        };   
        
        DefaultTableCellRenderer p0oRender = new DefaultTableCellRenderer() {   

            public void setValue(Object value) { //重写setValue方法，从而可以动态设置列单元字体颜色   

               
            	String str = value.toString();
            	
				Double betAmt = 0.0;
				
				String showStr = str.replace("g", "");
				showStr = showStr.replace("b", "");
				
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
				
				setText((value == null) ? "" : showStr);
				
				Double hideNum = 0.0;
				
				if(str.contains("g")){
					hideNum = p0oInplayhideNum;
				}else{
					hideNum = p0ohideNum;
				}

				if(Math.abs(betAmt) < hideNum){
					setForeground(Color.black);
					setText("0");
				}

				if(str.contains("b")){
					setBackground(new Color(179,232,255));
				}else{
					setBackground(Color.white);
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
/*        String[] columnNames =  
        { "联赛", "时间", "球队", "全场让球", "全场大小", "上半让球", "上半大小"};  */
        
        String[] columnNames =  
        {"序号", "联赛", "时间", "球队","让球初盘","终盘","盘口分析", "全场让球", "大小初盘","终盘","盘口分析","全场大小","完场比分","盘口结果","大小结果",};  
        
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
