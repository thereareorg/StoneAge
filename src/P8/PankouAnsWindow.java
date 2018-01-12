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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import org.python.icu.util.Calendar;

import HGclient.DXQdetailsWindow.MyTableModel;
import P8.Common;
import P8.DateChooser;



class pankouClassifyItem{
	public String classify = "";
	public int totalgames = 0;
	public int wingames = 0;
	public int winhalfgames = 0;
	public int losegames = 0;
	public int losehalfgames = 0;
	public int zoushuigames = 0;
	public int noresgames = 0;
}






public class PankouAnsWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	
	public Vector<pankouClassifyItem> classifyItemVec = new Vector<pankouClassifyItem>();
	
	PankouAnsWindow(){
		
		//llcb.setEnabled(false);
		mergecb.setEnabled(false);
		
		
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

		
		setTitle("盘口分析");
		
		
		intiComponent();
	}
	
	public Vector<String[]> showItemVec = new Vector<String[]>();
	
	Map<String, Double> rqpmap = new HashMap<String, Double>();

	
	
    private JLabel datelb1 = new JLabel("起始日期:");
    DateChooser mpstart = new DateChooser("pankouansdates");
	

    private JLabel datelb2 = new JLabel("结束日期:");
    DateChooser mpend = new DateChooser("pankouansdatee");
    
    private JCheckBox chupanrescb = new JCheckBox("初盘分析");
    private boolean bchupanres = false;
    private JCheckBox zhongpanrescb = new JCheckBox("终盘分析");
    private boolean bzhongpanres = true;
    
    
    private JCheckBox onlybig5cb = new JCheckBox("只看五大联赛欧冠");
    private boolean bonlybig5 = true;

    
    private JCheckBox dxqpancb = new JCheckBox("大小球盘");
    private boolean bdxqpan = false;

    
    private JCheckBox rqpancb = new JCheckBox("让球盘");
    private boolean brqpan = true;

    
    private JCheckBox ppcb = new JCheckBox("PP");
    private boolean bpp = true;

    
    private JCheckBox llcb = new JCheckBox("LL");
    
    
    private boolean bll = false;

    private JCheckBox mergecb = new JCheckBox("合并");
    private boolean bmerge = false;
    
    private JLabel mingoldlb = new JLabel("最低金额");
    private JTextField mingoldtxt = new JTextField();
    int mingold = 10000;
    
    private JLabel maxgoldlb = new JLabel("最高金额");
    private JTextField maxgoldtxt = new JTextField();
    int maxgold = 8000000;
    
    

    

	
    MyTableModel tableMode = new MyTableModel();
    
    
    JTable table = null;
    
    

    
    
	public String getmpstartdate(){
		return mpstart.getChooseDate();
	}
    
    
	public String getmpenddate(){
		return mpend.getChooseDate();
	}

	
	public void updateEventsDetails(){

		try{

			
			
			updateShowItem();
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	
	public void showbypp(){
		try{
			

			String startdate = mpstart.getChooseDate();
			String enddate = mpend.getChooseDate();
			Vector<String[]> eventsdetails = P8Http.getpreviouseventsdata();
			
			
			
			Vector<String[]> Vectmp = new Vector<String[]>();
			
			String startTimeStr = startdate + " " + "13:00";
			String endTimeStr = enddate + " " + "13:00";
			
			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
			
			SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			
			java.util.Date startTimeDate = dfMin.parse(startTimeStr);
			
			Calendar startTime = Calendar.getInstance();  
			startTime.setTime(startTimeDate);

			java.util.Date endTimeDate = dfMin.parse(endTimeStr);
			Calendar endTime = Calendar.getInstance();
			endTime.setTime(endTimeDate);
			
			if(startTime.getTimeInMillis() > endTime.getTimeInMillis()){
				return;
			}
			
			endTime.add(Calendar.DAY_OF_YEAR, 1);

			

			
			for(int i = 0; i < eventsdetails.size(); i++){

				String timeStr = eventsdetails.elementAt(i)[TYPEINDEX.TIME.ordinal()];
				
				if(eventsdetails.elementAt(i)[TYPEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
					continue;
				}
				
				java.util.Date timeDate = dfMin.parse(timeStr);
				
				Calendar time = Calendar.getInstance();  
				time.setTime(timeDate);
				
				
				if(time.getTimeInMillis() >= startTime.getTimeInMillis() && time.getTimeInMillis() < endTime.getTimeInMillis()){
					Vectmp.add(eventsdetails.elementAt(i));
				}
							
			}
			
			Vector<String[]> DetailsDatatmp = new Vector<String[]>();
			
			if(bonlybig5 == true){
				for(int i = 0; i < Vectmp.size(); i++){
					if(P8Http.isInShowLeagueName(Vectmp.elementAt(i)[TYPEINDEX.LEAGUENAME.ordinal()])){
						DetailsDatatmp.add(Vectmp.elementAt(i));
					}
				}
			}else{
				for(int i = 0; i < Vectmp.size(); i++){
					
						DetailsDatatmp.add(Vectmp.elementAt(i));
					
				}
			}
			
			
			
			
			
			
			if(DetailsDatatmp.size() == 0){
				
				if(classifyItemVec.size() != 0){
					classifyItemVec.clear();
				}
				
				
				if(showItemVec.size()!= 0){
					showItemVec.clear();
				}
				
				
				return;
			}
			
			
			Vector<String[]> scoreDetails = StoneAge.score.getpreviousdetailsbyperiod(startdate, enddate);

			if(scoreDetails == null){

				if(classifyItemVec.size() != 0){
					classifyItemVec.clear();
				}
				
				
				if(showItemVec.size()!= 0){
					showItemVec.clear();
				}
				
				
				return;

				
			}
			
			
			
			
			for(int k = 0; k < scoreDetails.size(); k++){
				System.out.println(Arrays.toString(scoreDetails.elementAt(k)));
			}
			
			
			System.out.println("PP队:");
			
			for(int k = 0; k < DetailsDatatmp.size(); k++){
				System.out.println(Arrays.toString(DetailsDatatmp.elementAt(k)));
			}
			
			
			if(classifyItemVec.size() != 0){
				classifyItemVec.clear();
			}
			
			
			if(showItemVec.size()!= 0){
				showItemVec.clear();
			}
			
			//合并score
			for(int i = 0; i < DetailsDatatmp.size(); i++){
				
				String[] olditem = DetailsDatatmp.elementAt(i).clone();
				
				String p8DayTime = olditem[TYPEINDEX.TIME.ordinal()].split(" ")[0];
				
				String p8hometaem = olditem[TYPEINDEX.EVENTNAMNE.ordinal()].split("-vs-")[0];
				String p8awaytaem = olditem[TYPEINDEX.EVENTNAMNE.ordinal()].split("-vs-")[1];
				
				if(p8hometaem.contains("史云斯")){
					System.out.println("11111");
				}
				
				String[] item = {Integer.toString(i+1), olditem[TYPEINDEX.LEAGUENAME.ordinal()], olditem[TYPEINDEX.TIME.ordinal()], olditem[TYPEINDEX.EVENTNAMNE.ordinal()], "", "", "",
						olditem[TYPEINDEX.PERIOD0HOME.ordinal()], "", "", "", olditem[TYPEINDEX.PERIOD0OVER.ordinal()], "", "", ""};
				
				String scorehometeam = ScoreMergeManager.findScoreTeam(p8hometaem);
				if(scorehometeam != null){
					String scoreawayteam = ScoreMergeManager.findScoreTeam(p8awaytaem);

					if(scoreawayteam != null){
						
						int indexinscoredetails = -1;
						for(int j = 0; j < scoreDetails.size(); j++){
							
							String scoreDayTime = scoreDetails.elementAt(j)[SCORENEWINDEX.TIME.ordinal()].split(" ")[0];
							
							if(scoreDetails.elementAt(j)[SCORENEWINDEX.EVENTNAMNE.ordinal()].equals(scorehometeam + " vs " + scoreawayteam) 
									&& p8DayTime.equals(scoreDayTime)){
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
								
								
								if(bdxqpan == true){
									
									
									if(item[P8PRETABLEHEADINDEX.DXQPANAS.ordinal()].equals("")){
										continue;
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
										
										
										if(Math.abs(p0over) > maxgold || Math.abs(p0over) < mingold){
											continue;
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
											dxqres = "不计";
										}else if(item[P8PRETABLEHEADINDEX.DXQPANAS.ordinal()].contains("升") && 
												p0over  < 0){
											dxqres = "不计";
										}else if(item[P8PRETABLEHEADINDEX.DXQPANAS.ordinal()].equals("")){
											dxqres = "不计";
										}

										if(dxqres.equals("不计")){
											continue;
										}
										
										
										item[P8PRETABLEHEADINDEX.DXQRES.ordinal()] = dxqres;
										
									}
									
									
									
									
								}else{
									
									
									//让球盘结果分析
									if(!item[P8PRETABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("") && !item[P8PRETABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("-")){
										
										if(item[P8PRETABLEHEADINDEX.RQPANAS.ordinal()].equals("")){
											continue;
										}
										
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
										
										if(Math.abs(p0home) > maxgold || Math.abs(p0home) < mingold){
											continue;
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
											rqres = "不计";
										}else if(item[P8PRETABLEHEADINDEX.RQPANAS.ordinal()].contains("降") && 
												!item[P8PRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												p0home  > 0){
											rqres = "不计";
										}else if(item[P8PRETABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
												item[P8PRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												p0home  > 0){
											rqres = "不计";
										}else if(item[P8PRETABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
												!item[P8PRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												p0home  < 0){
											rqres = "不计";
										}else if(item[P8PRETABLEHEADINDEX.RQPANAS.ordinal()].equals("")){
											rqres = "不计";
										}
										
										
										if(rqres.equals("不计"))
											continue;
										
										
										item[P8PRETABLEHEADINDEX.RQPRES.ordinal()] = rqres;
										
									}
								}
								

								

							}
							
							
							

							
							if(bdxqpan == true){
								
								boolean find = false;
								
								for(int j = 0; j < classifyItemVec.size(); j++){
									if(item[P8PRETABLEHEADINDEX.DXQPANAS.ordinal()].equals(classifyItemVec.elementAt(j).classify)){
										classifyItemVec.elementAt(j).totalgames++;
										if(item[P8PRETABLEHEADINDEX.SCORE.ordinal()].equals("")){
											classifyItemVec.elementAt(j).noresgames++;
										}else if(item[P8PRETABLEHEADINDEX.DXQRES.ordinal()].equals("全赢")){
											classifyItemVec.elementAt(j).wingames++;
										}else if(item[P8PRETABLEHEADINDEX.DXQRES.ordinal()].equals("全输")){
											classifyItemVec.elementAt(j).losegames++;
										}else if(item[P8PRETABLEHEADINDEX.DXQRES.ordinal()].equals("赢半")){
											classifyItemVec.elementAt(j).winhalfgames++;
										}else if(item[P8PRETABLEHEADINDEX.DXQRES.ordinal()].equals("输半")){
											classifyItemVec.elementAt(j).losehalfgames++;
										}else if(item[P8PRETABLEHEADINDEX.DXQRES.ordinal()].equals("走水")){
											classifyItemVec.elementAt(j).zoushuigames++;
										}
										
										find = true;
										break;
									}
								}
								
								if(find == false){
									pankouClassifyItem newitem = new pankouClassifyItem();
									newitem.classify = item[P8PRETABLEHEADINDEX.DXQPANAS.ordinal()];
									newitem.totalgames = 1;
									
									if(item[P8PRETABLEHEADINDEX.SCORE.ordinal()].equals("")){
										newitem.noresgames++;
									}else if(item[P8PRETABLEHEADINDEX.DXQRES.ordinal()].equals("全赢")){
										newitem.wingames++;
									}else if(item[P8PRETABLEHEADINDEX.DXQRES.ordinal()].equals("全输")){
										newitem.losegames++;
									}else if(item[P8PRETABLEHEADINDEX.DXQRES.ordinal()].equals("赢半")){
										newitem.winhalfgames++;
									}else if(item[P8PRETABLEHEADINDEX.DXQRES.ordinal()].equals("输半")){
										newitem.losehalfgames++;
									}else if(item[P8PRETABLEHEADINDEX.DXQRES.ordinal()].equals("走水")){
										newitem.zoushuigames++;
									}
									
									classifyItemVec.add(newitem);
								}
								
							}else{
							
								boolean find = false;
								
								for(int j = 0; j < classifyItemVec.size(); j++){
									if(item[P8PRETABLEHEADINDEX.RQPANAS.ordinal()].equals(classifyItemVec.elementAt(j).classify)){
										classifyItemVec.elementAt(j).totalgames++;
										if(item[P8PRETABLEHEADINDEX.SCORE.ordinal()].equals("")){
											classifyItemVec.elementAt(j).noresgames++;
										}else if(item[P8PRETABLEHEADINDEX.RQPRES.ordinal()].equals("全赢")){
											classifyItemVec.elementAt(j).wingames++;
										}else if(item[P8PRETABLEHEADINDEX.RQPRES.ordinal()].equals("全输")){
											classifyItemVec.elementAt(j).losegames++;
										}else if(item[P8PRETABLEHEADINDEX.RQPRES.ordinal()].equals("赢半")){
											classifyItemVec.elementAt(j).winhalfgames++;
										}else if(item[P8PRETABLEHEADINDEX.RQPRES.ordinal()].equals("输半")){
											classifyItemVec.elementAt(j).losehalfgames++;
										}else if(item[P8PRETABLEHEADINDEX.RQPRES.ordinal()].equals("走水")){
											classifyItemVec.elementAt(j).zoushuigames++;
										}
										
										find = true;
										break;
									}
									
									
								}
								
								if(find == false){
									pankouClassifyItem newitem = new pankouClassifyItem();
									newitem.classify = item[P8PRETABLEHEADINDEX.RQPANAS.ordinal()];
									newitem.totalgames = 1;
									
									if(item[P8PRETABLEHEADINDEX.SCORE.ordinal()].equals("")){
										newitem.noresgames++;
									}else if(item[P8PRETABLEHEADINDEX.RQPRES.ordinal()].equals("全赢")){
										newitem.wingames++;
									}else if(item[P8PRETABLEHEADINDEX.RQPRES.ordinal()].equals("全输")){
										newitem.losegames++;
									}else if(item[P8PRETABLEHEADINDEX.RQPRES.ordinal()].equals("赢半")){
										newitem.winhalfgames++;
									}else if(item[P8PRETABLEHEADINDEX.RQPRES.ordinal()].equals("输半")){
										newitem.losehalfgames++;
									}else if(item[P8PRETABLEHEADINDEX.RQPRES.ordinal()].equals("走水")){
										newitem.zoushuigames++;
									}
									
									classifyItemVec.add(newitem);
								}
							}
							
							
							
							
							

							
						}//hjkjklj
						
						
						
						
						
						
						
						

						
						
						
						
						
						
						
					}
					
				}
				

				
				
				
				
			}
			
			
			for(int i = 0; i < classifyItemVec.size(); i++){
				String[] item = {Integer.toString(i+1), classifyItemVec.elementAt(i).classify, Integer.toString(classifyItemVec.elementAt(i).totalgames), Integer.toString(classifyItemVec.elementAt(i).wingames)
						, Integer.toString(classifyItemVec.elementAt(i).winhalfgames), Integer.toString(classifyItemVec.elementAt(i).losegames), Integer.toString(classifyItemVec.elementAt(i).losehalfgames)
						, Integer.toString(classifyItemVec.elementAt(i).zoushuigames), Integer.toString(classifyItemVec.elementAt(i).noresgames)};
				
				showItemVec.add(item);
			}
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	public void showbyll(){
		
		
		try{
			

			String startdate = mpstart.getChooseDate();
			String enddate = mpend.getChooseDate();
			Vector<String[]> eventsdetails = ZhiboManager.getpreviouseventsdata();
			
			
			
			Vector<String[]> Vectmp = new Vector<String[]>();
			
			String startTimeStr = startdate + " " + "13:00";
			String endTimeStr = enddate + " " + "13:00";
			
			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
			
			SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			
			java.util.Date startTimeDate = dfMin.parse(startTimeStr);
			
			Calendar startTime = Calendar.getInstance();  
			startTime.setTime(startTimeDate);

			java.util.Date endTimeDate = dfMin.parse(endTimeStr);
			Calendar endTime = Calendar.getInstance();
			endTime.setTime(endTimeDate);
			
			if(startTime.getTimeInMillis() > endTime.getTimeInMillis()){
				return;
			}
			
			endTime.add(Calendar.DAY_OF_YEAR, 1);

			

			
			for(int i = 0; i < eventsdetails.size(); i++){

				String timeStr = eventsdetails.elementAt(i)[ZHIBOINDEX.TIME.ordinal()];
				
				if(eventsdetails.elementAt(i)[ZHIBOINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
					continue;
				}
				
				java.util.Date timeDate = dfMin.parse(timeStr);
				
				Calendar time = Calendar.getInstance();  
				time.setTime(timeDate);
				
				
				if(time.getTimeInMillis() >= startTime.getTimeInMillis() && time.getTimeInMillis() < endTime.getTimeInMillis()){
					Vectmp.add(eventsdetails.elementAt(i));
				}
							
			}
			
			Vector<String[]> DetailsDatatmp = new Vector<String[]>();
			
			if(bonlybig5 == true){
				for(int i = 0; i < Vectmp.size(); i++){
					if(ZhiboManager.isInShowLeagueName(Vectmp.elementAt(i)[ZHIBOINDEX.LEAGUENAME.ordinal()])){
						DetailsDatatmp.add(Vectmp.elementAt(i));
					}
				}
			}else{
				for(int i = 0; i < Vectmp.size(); i++){
					
						DetailsDatatmp.add(Vectmp.elementAt(i));
					
				}
			}
			
			
			
			
			
			
			if(DetailsDatatmp.size() == 0){
				
				if(classifyItemVec.size() != 0){
					classifyItemVec.clear();
				}
				
				
				if(showItemVec.size()!= 0){
					showItemVec.clear();
				}
				
				
				return;
			}
			
			
			Vector<String[]> scoreDetails = StoneAge.score.getpreviousdetailsbyperiod(startdate, enddate);

			if(scoreDetails == null){

				if(classifyItemVec.size() != 0){
					classifyItemVec.clear();
				}
				
				
				if(showItemVec.size()!= 0){
					showItemVec.clear();
				}
				
				
				return;

				
			}
			
			
			
			
			for(int k = 0; k < scoreDetails.size(); k++){
				System.out.println(Arrays.toString(scoreDetails.elementAt(k)));
			}
			
			
			System.out.println("LL队:");
			
			for(int k = 0; k < DetailsDatatmp.size(); k++){
				System.out.println(Arrays.toString(DetailsDatatmp.elementAt(k)));
			}
			
			
			if(classifyItemVec.size() != 0){
				classifyItemVec.clear();
			}
			
			
			if(showItemVec.size()!= 0){
				showItemVec.clear();
			}
			
			
			//合并score
			for(int i = 0; i < DetailsDatatmp.size(); i++){
				
				String[] olditem = DetailsDatatmp.elementAt(i).clone();
				
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
								
								
								if(bdxqpan == true){
									
									if(item[ZHIBOPRETABLEHEADINDEX.DXQPANAS.ordinal()].equals("")){
										continue;
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
										
										
										if(Math.abs(p0over) > maxgold || Math.abs(p0over) < mingold){
											continue;
										}
										
										
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
											dxqres = "不计";
										}else if(item[ZHIBOPRETABLEHEADINDEX.DXQPANAS.ordinal()].contains("升") && 
												Integer.parseInt(item[ZHIBOPRETABLEHEADINDEX.P0OVER.ordinal()])  < 0){
											dxqres = "不计";
										}else if(item[ZHIBOPRETABLEHEADINDEX.DXQPANAS.ordinal()].equals("")){
											dxqres = "不计";
										}
										
										if(dxqres.contains("不计")){
											continue;
										}
										
										item[ZHIBOPRETABLEHEADINDEX.DXQRES.ordinal()] = dxqres;
										
									}
									
									
								}else{
									
									//让球盘结果分析
									if(!item[ZHIBOPRETABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("") && !item[ZHIBOPRETABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("-")){
										
										System.out.println(Arrays.toString(item));
										
										
										if(item[ZHIBOPRETABLEHEADINDEX.RQPANAS.ordinal()].equals("")){
											continue;
										}
										
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
										
										if(Math.abs(p0home) > maxgold || Math.abs(p0home) < mingold){
											continue;
										}

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
											rqres = "不计";
										}else if(item[ZHIBOPRETABLEHEADINDEX.RQPANAS.ordinal()].contains("降") && 
												!item[ZHIBOPRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												Integer.parseInt(item[ZHIBOPRETABLEHEADINDEX.P0HOME.ordinal()])  > 0){
											rqres = "不计";
										}else if(item[ZHIBOPRETABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
												item[ZHIBOPRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												Integer.parseInt(item[ZHIBOPRETABLEHEADINDEX.P0HOME.ordinal()])  > 0){
											rqres = "不计";
										}else if(item[ZHIBOPRETABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
												!item[ZHIBOPRETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
												Integer.parseInt(item[ZHIBOPRETABLEHEADINDEX.P0HOME.ordinal()])  < 0){
											rqres = "不计";
										}else if(item[ZHIBOPRETABLEHEADINDEX.RQPANAS.ordinal()].equals("")){
											rqres = "不计";
										}
										
										if(rqres.equals("不计"))
											continue;
										
										
										item[ZHIBOPRETABLEHEADINDEX.RQPRES.ordinal()] = rqres;
										
									}
									
								}
								
								
								
								

								

								
							}
							
							
							

							
							
							
							
							
							if(bdxqpan == true){
								
								boolean find = false;
								
								for(int j = 0; j < classifyItemVec.size(); j++){
									if(item[ZHIBOPRETABLEHEADINDEX.DXQPANAS.ordinal()].equals(classifyItemVec.elementAt(j).classify)){
										classifyItemVec.elementAt(j).totalgames++;
										if(item[ZHIBOPRETABLEHEADINDEX.SCORE.ordinal()].equals("")){
											classifyItemVec.elementAt(j).noresgames++;
										}else if(item[ZHIBOPRETABLEHEADINDEX.DXQRES.ordinal()].equals("全赢")){
											classifyItemVec.elementAt(j).wingames++;
										}else if(item[ZHIBOPRETABLEHEADINDEX.DXQRES.ordinal()].equals("全输")){
											classifyItemVec.elementAt(j).losegames++;
										}else if(item[ZHIBOPRETABLEHEADINDEX.DXQRES.ordinal()].equals("赢半")){
											classifyItemVec.elementAt(j).winhalfgames++;
										}else if(item[ZHIBOPRETABLEHEADINDEX.DXQRES.ordinal()].equals("输半")){
											classifyItemVec.elementAt(j).losehalfgames++;
										}else if(item[ZHIBOPRETABLEHEADINDEX.DXQRES.ordinal()].equals("走水")){
											classifyItemVec.elementAt(j).zoushuigames++;
										}
										
										find = true;
										break;
									}
								}
								
								if(find == false){
									pankouClassifyItem newitem = new pankouClassifyItem();
									newitem.classify = item[ZHIBOPRETABLEHEADINDEX.DXQPANAS.ordinal()];
									newitem.totalgames = 1;
									
									if(item[ZHIBOPRETABLEHEADINDEX.SCORE.ordinal()].equals("")){
										newitem.noresgames++;
									}else if(item[ZHIBOPRETABLEHEADINDEX.DXQRES.ordinal()].equals("全赢")){
										newitem.wingames++;
									}else if(item[ZHIBOPRETABLEHEADINDEX.DXQRES.ordinal()].equals("全输")){
										newitem.losegames++;
									}else if(item[ZHIBOPRETABLEHEADINDEX.DXQRES.ordinal()].equals("赢半")){
										newitem.winhalfgames++;
									}else if(item[ZHIBOPRETABLEHEADINDEX.DXQRES.ordinal()].equals("输半")){
										newitem.losehalfgames++;
									}else if(item[ZHIBOPRETABLEHEADINDEX.DXQRES.ordinal()].equals("走水")){
										newitem.zoushuigames++;
									}
									
									classifyItemVec.add(newitem);
								}
								
							}else{
							
								boolean find = false;
								
								for(int j = 0; j < classifyItemVec.size(); j++){
									if(item[ZHIBOPRETABLEHEADINDEX.RQPANAS.ordinal()].equals(classifyItemVec.elementAt(j).classify)){
										classifyItemVec.elementAt(j).totalgames++;
										if(item[ZHIBOPRETABLEHEADINDEX.SCORE.ordinal()].equals("")){
											classifyItemVec.elementAt(j).noresgames++;
										}else if(item[ZHIBOPRETABLEHEADINDEX.RQPRES.ordinal()].equals("全赢")){
											classifyItemVec.elementAt(j).wingames++;
										}else if(item[ZHIBOPRETABLEHEADINDEX.RQPRES.ordinal()].equals("全输")){
											classifyItemVec.elementAt(j).losegames++;
										}else if(item[ZHIBOPRETABLEHEADINDEX.RQPRES.ordinal()].equals("赢半")){
											classifyItemVec.elementAt(j).winhalfgames++;
										}else if(item[ZHIBOPRETABLEHEADINDEX.RQPRES.ordinal()].equals("输半")){
											classifyItemVec.elementAt(j).losehalfgames++;
										}else if(item[ZHIBOPRETABLEHEADINDEX.RQPRES.ordinal()].equals("走水")){
											classifyItemVec.elementAt(j).zoushuigames++;
										}
										
										find = true;
										break;
									}
									
									
								}
								
								if(find == false){
									pankouClassifyItem newitem = new pankouClassifyItem();
									newitem.classify = item[ZHIBOPRETABLEHEADINDEX.RQPANAS.ordinal()];
									newitem.totalgames = 1;
									
									if(item[ZHIBOPRETABLEHEADINDEX.SCORE.ordinal()].equals("")){
										newitem.noresgames++;
									}else if(item[ZHIBOPRETABLEHEADINDEX.RQPRES.ordinal()].equals("全赢")){
										newitem.wingames++;
									}else if(item[ZHIBOPRETABLEHEADINDEX.RQPRES.ordinal()].equals("全输")){
										newitem.losegames++;
									}else if(item[ZHIBOPRETABLEHEADINDEX.RQPRES.ordinal()].equals("赢半")){
										newitem.winhalfgames++;
									}else if(item[ZHIBOPRETABLEHEADINDEX.RQPRES.ordinal()].equals("输半")){
										newitem.losehalfgames++;
									}else if(item[ZHIBOPRETABLEHEADINDEX.RQPRES.ordinal()].equals("走水")){
										newitem.zoushuigames++;
									}
									
									classifyItemVec.add(newitem);
								}
							}
							
							
							
							
							
							
							

							
						}
						
						
					}
					
				}
				
				
				
			}
			
			
			for(int i = 0; i < classifyItemVec.size(); i++){
				String[] item = {Integer.toString(i+1), classifyItemVec.elementAt(i).classify, Integer.toString(classifyItemVec.elementAt(i).totalgames), Integer.toString(classifyItemVec.elementAt(i).wingames)
						, Integer.toString(classifyItemVec.elementAt(i).winhalfgames), Integer.toString(classifyItemVec.elementAt(i).losegames), Integer.toString(classifyItemVec.elementAt(i).losehalfgames)
						, Integer.toString(classifyItemVec.elementAt(i).zoushuigames), Integer.toString(classifyItemVec.elementAt(i).noresgames)};
				
				showItemVec.add(item);
			}
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	

	
	
	
	public void updateShowItem(){
		

		if(bpp == true){
			showbypp();
		}else if(bll == true){
			showbyll();
		}
		
		
		tableMode.updateTable();
		
		setOneRowBackgroundColor();
		

	}
	
	
	
	
	
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel(new GridLayout(1, 14));

        container.add(panelNorth, BorderLayout.NORTH);  
        

        panelNorth.add(datelb1);
        panelNorth.add(mpstart);

        panelNorth.add(datelb2);
        panelNorth.add(mpend);
        
        panelNorth.add(chupanrescb);
        panelNorth.add(zhongpanrescb);
        
        panelNorth.add(onlybig5cb);
        panelNorth.add(dxqpancb);
        panelNorth.add(rqpancb);
        panelNorth.add(ppcb);
        
        
        
        
        
        

        panelNorth.add(llcb);
        panelNorth.add(mergecb);

        panelNorth.add(mingoldlb);

        
        

        
        panelNorth.add(mingoldtxt);
        panelNorth.add(maxgoldlb);
        panelNorth.add(maxgoldtxt);
        

        
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
        
        
        mingoldtxt.setText(Integer.toString(mingold));
        
        mingoldtxt.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = mingoldtxt.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	
                    	JOptionPane.showMessageDialog(null,"最小金额:" + value);
                    	
                    	mingold = Integer.parseInt(value);
                    	
                    	
                    	
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
        
        

        
        maxgoldtxt.setText(Integer.toString(maxgold));
        
        maxgoldtxt.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = maxgoldtxt.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	
                    	JOptionPane.showMessageDialog(null,"最大金额:" + value);
                    	
                    	maxgold = Integer.parseInt(value);
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
        
        
        
        
        
        onlybig5cb.setSelected(true);
        
        onlybig5cb.addItemListener(new ItemListener() {


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
        
        
        dxqpancb.setSelected(false);
        
        dxqpancb.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					
					bdxqpan = false;
					
					
				}else{
					
					bdxqpan = true;
					rqpancb.setSelected(false);
					brqpan = false;
				}
				
				updateShowItem();

			}
        });
        
        
        
        


        
        rqpancb.setSelected(true);
        
        rqpancb.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					
					brqpan = false;
					
					
				}else{
					
					brqpan = true;
					dxqpancb.setSelected(false);
					bdxqpan = false;
					
				}
				
				updateShowItem();

			}
        });

        
 
        
        ppcb.setSelected(true);
        
        ppcb.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					bpp = false;
					
				}else{

					bpp = true;
					bll = false;
					llcb.setSelected(false);
					bmerge = false;
					mergecb.setSelected(false);
				}
				
				updateShowItem();	
			}
        });
        

        
        
        llcb.setSelected(false);
        
        llcb.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					bll = false;
					
				}else{
					bll = true;
					bpp = false;
					ppcb.setSelected(false);
					bmerge = false;
					mergecb.setSelected(false);

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
        	 { "序号", "分类", "总场次","全赢", "赢半", "全输", "输半" , "走水", "无结果"};
        

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
        		
        			return showItemVec.elementAt(rowIndex)[columnIndex];
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
	        if(null == showItemVec){
	    		return 0;
	    	}
	        
	        //System.out.println(gameDetailsVec.size());
	        
            return showItemVec.size();  
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
