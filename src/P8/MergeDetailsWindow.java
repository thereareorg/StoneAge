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
import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
import javax.swing.table.TableColumnModel;

import MergeNew.NEWMERGEINDEX;
import P8.EventsDetailsWindow.MyTableModel;


enum MERGETABLEHEADINDEX{
	INDEX,
	LEAGUE,
	TIME,
	EVENTNAME,
	RQCHUPAN,
	RQZHONGPAN,
	RQPANAS,
	P8HOMERES,
	P8HOMEINPLAYRES,
	ZHIBOHOMERES,
	P0HOME,
	DXQCHUPAN,
	DXQZHONGPAN,
	DXQPANAS,
	P8OVERRES,
	P8OVERINPLAYRES,
	ZHIBOOVERRES,
	P0OVER
}

public class MergeDetailsWindow extends JFrame{
	  
	  
	   
		private static final long serialVersionUID = 508685938515369544L;
		
		private  Vector<String[]> originalDetailsData = new Vector<String[]>();
		
		private  Vector<String[]> detailsData = null;
		
		
		//private  ReadWriteLock lockshowItem = new ReentrantReadWriteLock();
		
		
		
		
		private Vector<String[]> showItemVec = new Vector<String[]>();
		
		private Vector<String[]> scoreDetails = new Vector<String[]>();

		
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
	    
	    
	    private JLabel labeloneSide = new JLabel("占边设置:");
	    private JCheckBox allgames = new JCheckBox("所有数据");
	    private JCheckBox p8oneside = new JCheckBox("pp");
	    private JCheckBox p8inplayoneside = new JCheckBox("PP走地");
	    private JCheckBox zhibooneside = new JCheckBox("LL");
	    private JCheckBox hgoneside = new JCheckBox("HG");
	    
	    
	    private boolean ballgames = true;
	    private boolean bp8side = false;
	    private boolean bzhiboside = false;
	    private boolean bp8inplayside = false;
	    private boolean bhgside = false;
	    
	    
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
	    
	    
	    JTable table = null;
	    
	    CMap m = new CMap1();

	    
	    
	    

	    
	    
	    

		public MergeDetailsWindow()  
	    {  
			setTitle("合并注单");  
			
	        intiComponent();  
	        
	        //hideAbandonedcols();
	        
	        
	        
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
				
				scoreDetails = StoneAge.score.getFinalScoresDetails();
				
				
				updateShowItem();
				
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			

			
		}
		
		
		
		public void updateShowItem(){
			
		try{
			
			
			
			Vector<String[]> DetailsDatatmp = new Vector<String[]>();
			
			//只显示走地盘
			if(bonlyShowInplay == true){
				for(int i = 0; i < originalDetailsData.size(); i++){
					if(originalDetailsData.elementAt(i)[MERGEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
						DetailsDatatmp.add(originalDetailsData.elementAt(i).clone());
					}
				}
			}
			
			//只显示单式盘
			if(bonlyShowNotInplay == true){
				for(int i = 0; i < originalDetailsData.size(); i++){
					if(!originalDetailsData.elementAt(i)[MERGEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
						DetailsDatatmp.add(originalDetailsData.elementAt(i).clone());
					}
				}
			}
			
			Vector<String[]> DetailsDatatmp1 = new Vector<String[]>();
			
			
			if(DetailsDatatmp.size() == 0){

				for(int i = 0; i < originalDetailsData.size();i++){
					DetailsDatatmp.add(originalDetailsData.elementAt(i).clone());
				}
				
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
					double betp8inplay1 = 0.0;
					double betzhibo1 = 0.0;
					double bethg1 = 0.0;
					
					double betAmt2 = 0.0;
					double betp82 = 0.0;					
					double betp8inplay2 = 0.0;
					double betzhibo2 = 0.0;
					double bethg2 = 0.0;
					


					
					String str1 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.PERIOD0HOME.ordinal()];
					
					
					
					if(str1.contains("=")){
						String[] tmp = str1.split("=");
						betAmt1 = Double.parseDouble(tmp[1]);

					}else{
						betAmt1 = Double.parseDouble(str1);
					}
					
					
					//全场让球
					str1 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.P8HRES.ordinal()];
					
					if(str1.contains("=")){
						String[] tmp = str1.split("=");
						betp81 = Double.parseDouble(tmp[1]);

					}else{
						betp81 = Double.parseDouble(str1);
					}
					
					str1 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.ZHIBOHRES.ordinal()];
					
					if(str1.contains("=")){
						String[] tmp = str1.split("=");
						betzhibo1 = Double.parseDouble(tmp[1]);

					}else{
						betzhibo1 = Double.parseDouble(str1);
					}
					
					str1 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.INP8HRES.ordinal()];
					
					if(str1.contains("=")){
						String[] tmp = str1.split("=");
						betp8inplay1 = Double.parseDouble(tmp[1]);

					}else{
						betp8inplay1 = Double.parseDouble(str1);
					}
					
					str1 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.HGHRES.ordinal()];
					
					
					if(str1.contains("=")){
						String[] tmp = str1.split("=");
						bethg1 = Double.parseDouble(tmp[1]);

					}else{
						bethg1 = Double.parseDouble(str1);
					}
					
					
					
					//全场大小
					String str2 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.PERIOD0OVER.ordinal()];
					
					if(str2.contains("=")){
						String[] tmp = str2.split("=");
						betAmt2 = Double.parseDouble(tmp[1]);
						

						
					}else{
						betAmt2 = Double.parseDouble(str2);
					}
					
					
					str2 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.P8ORES.ordinal()];
					
					if(str2.contains("=")){
						String[] tmp = str2.split("=");
						betp82 = Double.parseDouble(tmp[1]);
						

						
					}else{
						betp82 = Double.parseDouble(str2);
					}
					
					str2 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.ZHIBOORES.ordinal()];
					
					if(str2.contains("=")){
						String[] tmp = str2.split("=");
						betzhibo2 = Double.parseDouble(tmp[1]);
						

						
					}else{
						betzhibo2 = Double.parseDouble(str2);
					}
					
					str2 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.INP8ORES.ordinal()];
					
					if(str2.contains("=")){
						String[] tmp = str2.split("=");
						betp8inplay2 = Double.parseDouble(tmp[1]);
						

						
					}else{
						betp8inplay2 = Double.parseDouble(str2);
					}
					
					str2 = DetailsDatatmp1.elementAt(i)[MERGEINDEX.HGORES.ordinal()];
					
					if(str2.contains("=")){
						String[] tmp = str2.split("=");
						bethg2 = Double.parseDouble(tmp[1]);
						

						
					}else{
						bethg2 = Double.parseDouble(str2);
					}
					
					
					
					
					double[] rangqiuArry = {betp81, betp8inplay1, betzhibo1, bethg1};
					double[] daxiaoqiuArray = {betp82, betp8inplay2, betzhibo2, bethg2};
					
					Vector<Integer> sideIndex = new Vector<Integer>();

					if(bp8side&&bzhiboside&&bp8inplayside&&bhgside){
						sideIndex.add(0);
						sideIndex.add(1);
						sideIndex.add(2);
						sideIndex.add(3);
						
					}else if(bp8side&&bzhiboside&&bp8inplayside){
						sideIndex.add(0);
						sideIndex.add(1);
						sideIndex.add(2);
					}else if(bp8side&&bzhiboside&&bhgside){
						sideIndex.add(0);
						sideIndex.add(2);
						sideIndex.add(3);
					}else if(bp8side&&bp8inplayside&&bhgside){
						sideIndex.add(0);
						sideIndex.add(1);
						sideIndex.add(3);
					}else if(bzhiboside&&bp8inplayside&&bhgside){
						sideIndex.add(2);
						sideIndex.add(1);
						sideIndex.add(3);
					}else if(bp8side&&bzhiboside){
						sideIndex.add(0);
						sideIndex.add(2);
					}else if(bp8side&&bp8inplayside){
						sideIndex.add(0);
						sideIndex.add(1);
					}else if(bp8side&&bhgside){
						sideIndex.add(0);
						sideIndex.add(3);
					}else if(bzhiboside&&bp8inplayside){
						sideIndex.add(1);
						sideIndex.add(2);
					}else if(bp8inplayside&&bhgside){
						sideIndex.add(1);
						sideIndex.add(3);
					}else if(bzhiboside&&bhgside){
						sideIndex.add(2);
						sideIndex.add(3);
					}else if(bp8side){
						sideIndex.add(0);

					}else if(bp8inplayside){
						sideIndex.add(1);

					}else if(bzhiboside){
						sideIndex.add(2);

					}else if(bhgside){
						sideIndex.add(3);

					}
					
					

					if(sideIndex.size() != 0){
						//让球处理
						double res = 1.0;
						double p0htmp1 = 0;
						double p0htmp2 = 0;
						for(int j = 0; j < sideIndex.size(); j++){
							
							if(Math.abs(rangqiuArry[sideIndex.elementAt(j)]) < p0hhideNum){
								res = 0.0;
								break;
							}
							
							
							
							res = res * rangqiuArry[sideIndex.elementAt(j)];
							p0htmp1 += rangqiuArry[sideIndex.elementAt(j)];
							p0htmp2 += Math.abs(rangqiuArry[sideIndex.elementAt(j)]);
						}
						//p0htmp1 = Math.abs(p0htmp1);
						boolean add = false;
						String p0hStr = "0";
						if(res != 0.0 && (Math.abs(Math.abs(p0htmp1) - p0htmp2) < 1.0)){
							add = true;
							for(int j = 0; j < sideIndex.size(); j++){
								if(j == 0){
									p0hStr = String.format("(%.0f)", rangqiuArry[sideIndex.elementAt(j)]);
								}
								else{
									p0hStr = p0hStr + "+" + String.format("(%.0f)", rangqiuArry[sideIndex.elementAt(j)]);
								}
							}
							
							if(p0hStr.contains("+")){
								p0hStr = p0hStr + "=" + String.format("%.0f", p0htmp1);
							}else{
								p0hStr = String.format("%.0f", p0htmp1);
							}
							
							
							
							
						}else{
							p0htmp1 = 0.0;
						}
						
						DetailsDatatmp1.elementAt(i)[MERGEINDEX.PERIOD0HOME.ordinal()] = p0hStr;
						
						
						
						
						res = 1.0;
						double p0otmp1 = 0;
						double p0otmp2 = 0;
						for(int j = 0; j < sideIndex.size(); j++){
							
							if(Math.abs(daxiaoqiuArray[sideIndex.elementAt(j)]) < p0hhideNum){
								res = 0.0;
								break;
							}
							
							res = res * daxiaoqiuArray[sideIndex.elementAt(j)];
							p0otmp1 += daxiaoqiuArray[sideIndex.elementAt(j)];
							p0otmp2 += Math.abs(daxiaoqiuArray[sideIndex.elementAt(j)]);
						}
						//p0htmp1 = Math.abs(p0htmp1);
						
						String p0oStr = "0";
						if(res != 0.0 && (Math.abs(Math.abs(p0otmp1) - p0otmp2) < 1.0)){
							add = true;
							for(int j = 0; j < sideIndex.size(); j++){
								if(j == 0){
									p0oStr = String.format("(%.0f)", daxiaoqiuArray[sideIndex.elementAt(j)]);
								}
								else{
									p0oStr = p0oStr + "+" + String.format("(%.0f)", daxiaoqiuArray[sideIndex.elementAt(j)]);
								}
							}
							
							
							if(p0oStr.contains("+")){
								p0oStr = p0oStr + "=" + String.format("%.0f", p0otmp1);
							}else{
								p0oStr = String.format("%.0f", p0otmp1);
							}
							
							
						}else{
							p0otmp1 = 0.0;
						}
						
						DetailsDatatmp1.elementAt(i)[MERGEINDEX.PERIOD0OVER.ordinal()] = p0oStr;
						
						if(add == true &&(Math.abs(p0htmp1) > p0hhiglightBigNum || Math.abs(p0otmp1) > p0ohiglightBigNum)){
							DetailsDatatmp2.add(DetailsDatatmp1.elementAt(i));
						}
						
						
						
					}else if(ballgames == true){
						DetailsDatatmp2.add(DetailsDatatmp1.elementAt(i));
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
				
				String zhibohometeam = olditem[MERGEINDEX.EVENTNAMNE.ordinal()].split(" vs ")[0];
				String zhiboawayteam = olditem[MERGEINDEX.EVENTNAMNE.ordinal()].split(" vs ")[1];
				
				//{ "序号", "联赛", "时间", "球队", "让球初盘", "终盘", "盘口分析", "平博", "平博滚动盘", "智博",  "全场让球", "大小初盘", "终盘","盘口分析","平博 ", "平博滚动盘 ", "智博 ", "全场大小"};  
				
				String[] item = {Integer.toString(i+1), olditem[MERGEINDEX.LEAGUENAME.ordinal()], olditem[MERGEINDEX.TIME.ordinal()], olditem[MERGEINDEX.EVENTNAMNE.ordinal()], "", "", "",
						olditem[MERGEINDEX.P8HRES.ordinal()], olditem[MERGEINDEX.INP8HRES.ordinal()], olditem[MERGEINDEX.ZHIBOHRES.ordinal()], olditem[MERGEINDEX.PERIOD0HOME.ordinal()],
						"", "","", olditem[MERGEINDEX.P8ORES.ordinal()],olditem[MERGEINDEX.INP8ORES.ordinal()],olditem[MERGEINDEX.ZHIBOORES.ordinal()],olditem[MERGEINDEX.PERIOD0OVER.ordinal()]};
				
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
							item[MERGETABLEHEADINDEX.RQCHUPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQCHUPAN.ordinal()];
							item[MERGETABLEHEADINDEX.RQZHONGPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQZHONGPAN.ordinal()];
							item[MERGETABLEHEADINDEX.RQPANAS.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.RQPANAS.ordinal()];
							item[MERGETABLEHEADINDEX.DXQCHUPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQCHUPAN.ordinal()];
							item[MERGETABLEHEADINDEX.DXQZHONGPAN.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQZHONGPAN.ordinal()];
							item[MERGETABLEHEADINDEX.DXQPANAS.ordinal()] = scoreDetails.elementAt(indexinscoredetails)[SCORENEWINDEX.DXQPANAS.ordinal()];
							
							
							
							
							
							//给出赌哪边的提示
							
							//让球盘结果分析
							if(!item[MERGETABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("") && !item[MERGETABLEHEADINDEX.RQZHONGPAN.ordinal()].equals("-")
									&& !item[MERGETABLEHEADINDEX.RQPANAS.ordinal()].equals("")){
								
								System.out.println("p8 events:" + Arrays.toString(item));
								
								
								

								
								String betside = "";
								
								
								
								int p0home = 0;
								if(item[MERGETABLEHEADINDEX.P0HOME.ordinal()].contains("=")){
									p0home = Integer.parseInt(item[MERGETABLEHEADINDEX.P0HOME.ordinal()].split("=")[1]);
								}
								
								
								
								
								//只统计赌降的那一边
								if(item[MERGETABLEHEADINDEX.RQPANAS.ordinal()].contains("降") && 
										item[MERGETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
										p0home  > 0){
									betside = "right";
								}else if(item[MERGETABLEHEADINDEX.RQPANAS.ordinal()].contains("降") && 
										!item[MERGETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
										p0home  < 0){
									betside = "left";
								}else if(item[MERGETABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
										item[MERGETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
										p0home  < 0){
									betside = "left";
								}else if(item[MERGETABLEHEADINDEX.RQPANAS.ordinal()].contains("升") && 
										!item[MERGETABLEHEADINDEX.RQCHUPAN.ordinal()].contains("受让")&&
										p0home  > 0){
									betside = "right";
								}
								
								String[] oldteams = item[MERGETABLEHEADINDEX.EVENTNAME.ordinal()].split(" vs ");
								
								if(betside.equals("right")){
									
									item[MERGETABLEHEADINDEX.EVENTNAME.ordinal()] = "<html>" + oldteams[0] + " vs " +  "<font color='red'>" + oldteams[1] + "</font>";
								}else if(betside.equals("left")){
									item[MERGETABLEHEADINDEX.EVENTNAME.ordinal()] = "<html>" +"<font color='red'>" + oldteams[0] + "</font>" + " vs " + oldteams[1];
								}

								
							}
							
							
							
							//大小球盘结果分析
							if(!item[MERGETABLEHEADINDEX.DXQZHONGPAN.ordinal()].equals("") && !item[MERGETABLEHEADINDEX.DXQZHONGPAN.ordinal()].equals("-")
									&& !item[MERGETABLEHEADINDEX.DXQPANAS.ordinal()].equals("")){
								
								
								
								
								int p0over = 0;
								if(item[MERGETABLEHEADINDEX.P0OVER.ordinal()].contains("=")){
									p0over = Integer.parseInt(item[MERGETABLEHEADINDEX.P0OVER.ordinal()].split("=")[1]);
								}
								
								
								

								String bet = "";
								
								//只统计赌降的一边
								if(item[MERGETABLEHEADINDEX.DXQPANAS.ordinal()].contains("降") && 
										p0over  < 0){
									bet = "大";
								}else if(item[MERGETABLEHEADINDEX.DXQPANAS.ordinal()].contains("升") && 
										p0over  > 0){
									bet = "小";
								}

								
								
								
								if(!bet.equals("")){
									
									item[MERGETABLEHEADINDEX.DXQPANAS.ordinal()] = "<html>" + "<font color='red'>" + item[MERGETABLEHEADINDEX.DXQPANAS.ordinal()] + "</font>";
								}
								
							}
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
						}
						
						
					}
					
				}
				
				showItemVec.add(item);
				
			}
			
			
			tableMode.updateTable();
			
			hideDatacols();
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
			

			
		}

		public void refreshData(){
			updateShowItem();
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
			
			JPanel panelNorth = new JPanel(new GridLayout(5, 4));

	        container.add(panelNorth, BorderLayout.NORTH);  
	        
	        
	        
	        JPanel panelCheckbox = new JPanel(new GridLayout(1, 5));
	        

	        panelCheckbox.add(labeloneSide);
	        panelCheckbox.add(allgames);
	        panelCheckbox.add(p8oneside);
	        panelCheckbox.add(p8inplayoneside);
	        panelCheckbox.add(zhibooneside);
	        //panelCheckbox.add(hgoneside);
	        
	        
	        
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
	        
	        allgames.setSelected(true);
	        
	        allgames.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
					if(e.getStateChange() == ItemEvent.DESELECTED){
						ballgames = false;

					}else{
						
						refreshData();
						
						ballgames = true;
						
						bp8side = false;
						p8oneside.setSelected(false);
						
						bzhiboside = false;
						zhibooneside.setSelected(false);
						
						bp8inplayside = false;
						p8inplayoneside.setSelected(false);
					}
					
					updateShowItem();
				}
	        });
	        
	        
	        
	        
	        
	        
	        
	        p8oneside.setSelected(false);
	        
	        p8oneside.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
					if(e.getStateChange() == ItemEvent.DESELECTED){
						bp8side = false;

					}else{
						bp8side = true;
						ballgames = false;
						allgames.setSelected(false);
					}
					
					updateShowItem();
				}
	        });
	        
	        
	        p8inplayoneside.setSelected(false);
	        
	        p8inplayoneside.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
					if(e.getStateChange() == ItemEvent.DESELECTED){
						bp8inplayside = false;

					}else{
						bp8inplayside = true;
						ballgames = false;
						allgames.setSelected(false);
					}
					
					updateShowItem();
				}
	        });
	        
	        
	        
	        zhibooneside.setSelected(false);
	        
	        zhibooneside.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
					if(e.getStateChange() == ItemEvent.DESELECTED){
						bzhiboside = false;

					}else{
						bzhiboside = true;
						ballgames = false;
						allgames.setSelected(false);
					}
					
					updateShowItem();
				}
	        });
	        
	        
	        hgoneside.setSelected(false);
	        
	        hgoneside.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
					if(e.getStateChange() == ItemEvent.DESELECTED){
						bhgside = false;

					}else{
						bhgside = true;
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
	        panelNorth.add(panelCheckbox);

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
	        
	        
	        //设置选中行
/*	        table.addMouseListener(new MouseAdapter() {
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
	        });*/

	        
	        table.getColumnModel().getColumn(0).setPreferredWidth(20);//序号
	        table.getColumnModel().getColumn(MERGEINDEX.LEAGUENAME.ordinal()).setPreferredWidth(180);//联赛
	        table.getColumnModel().getColumn(MERGEINDEX.TIME.ordinal()).setPreferredWidth(120);//时间	        
	        table.getColumnModel().getColumn(MERGEINDEX.EVENTNAMNE.ordinal()).setPreferredWidth(150);	//队名        
	        table.getColumnModel().getColumn(MERGEINDEX.RQPK.ordinal()).setPreferredWidth(40);//合并让球
	        table.getColumnModel().getColumn(MERGEINDEX.DXQPK.ordinal()).setPreferredWidth(40);//合并让球
	        table.getColumnModel().getColumn(MERGEINDEX.P8HRES.ordinal()).setPreferredWidth(50);//合并让球
	        table.getColumnModel().getColumn(MERGEINDEX.P8ORES.ordinal()).setPreferredWidth(50);//合并让球
	        table.getColumnModel().getColumn(MERGEINDEX.INP8HRES.ordinal()).setPreferredWidth(50);//合并让球
	        table.getColumnModel().getColumn(MERGEINDEX.INP8ORES.ordinal()).setPreferredWidth(50);//合并让球
	        table.getColumnModel().getColumn(MERGEINDEX.ZHIBOHRES.ordinal()).setPreferredWidth(50);//合并让球
	        table.getColumnModel().getColumn(MERGEINDEX.ZHIBOORES.ordinal()).setPreferredWidth(50);//合并让球
	        table.getColumnModel().getColumn(MERGEINDEX.HGHRES.ordinal()).setPreferredWidth(50);//合并让球
	        table.getColumnModel().getColumn(MERGEINDEX.HGORES.ordinal()).setPreferredWidth(50);//合并让球
	        
		    table.getColumnModel().getColumn(MERGEINDEX.PERIOD0HOME.ordinal()).setPreferredWidth(280);//合并让球
		    table.getColumnModel().getColumn(MERGEINDEX.PERIOD0OVER.ordinal()).setPreferredWidth(280);//合并大小
		    
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
	            	setHorizontalAlignment(JLabel.CENTER);
	               
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

	            	setHorizontalAlignment(JLabel.CENTER);
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
	    
	    
	    
		public void hideDatacols(){
			
			try{
				TableColumnModel columnModel=table.getColumnModel();   
				
				Vector<Integer> hidecol = new Vector<Integer>();
				
				if(ballgames == false){
					if(bp8side == false){
						hidecol.add(MERGETABLEHEADINDEX.P8HOMERES.ordinal());
						hidecol.add(MERGETABLEHEADINDEX.P8OVERRES.ordinal());
					}
					
					if(bzhiboside == false){
						hidecol.add(MERGETABLEHEADINDEX.ZHIBOHOMERES.ordinal());
						hidecol.add(MERGETABLEHEADINDEX.ZHIBOOVERRES.ordinal());
					}
					
					if(bp8inplayside == false){
						hidecol.add(MERGETABLEHEADINDEX.P8HOMEINPLAYRES.ordinal());
						hidecol.add(MERGETABLEHEADINDEX.P8OVERINPLAYRES.ordinal());
					}
				}
				
				
				

				
				
			
			
				Vector<Integer> keepsizecols = new Vector<Integer>();
			keepsizecols.add(MERGETABLEHEADINDEX.P8HOMEINPLAYRES.ordinal());
			keepsizecols.add(MERGETABLEHEADINDEX.P8HOMERES.ordinal());
			keepsizecols.add(MERGETABLEHEADINDEX.P8OVERINPLAYRES.ordinal());
			keepsizecols.add(MERGETABLEHEADINDEX.P8OVERRES.ordinal());
			keepsizecols.add(MERGETABLEHEADINDEX.ZHIBOHOMERES.ordinal());
			keepsizecols.add(MERGETABLEHEADINDEX.ZHIBOOVERRES.ordinal());
			
			
			
			for(int i = 0;i < keepsizecols.size(); i++ ){
			    
			    TableColumn   column=columnModel.getColumn(keepsizecols.elementAt(i));   
			    column.setMinWidth(0);   
			    column.setMaxWidth(500);
			    column.setWidth(80);
			    column.setPreferredWidth(80);
	
			    
			}
				
				
				for(int i = 0;i < hidecol.size(); i++ ){
				    
				    TableColumn   column=columnModel.getColumn(hidecol.elementAt(i));   
				    column.setMinWidth(0);   
				    column.setMaxWidth(0);
				    column.setWidth(0);
				    column.setPreferredWidth(0);
				}
				
				
				
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			

			
		}
	    

	    
		public void hideAbandonedcols(){
			try{
				TableColumnModel   columnModel=table.getColumnModel();   
				TableColumnModel column_id_header = table.getTableHeader().getColumnModel(); 
				
				Vector<Integer> hidecol = new Vector<Integer>();
				
					hidecol.add(MERGEINDEX.RQPK.ordinal());
					
				
				
				
					hidecol.add(MERGEINDEX.DXQPK.ordinal());
					
				
				
				

				
					hidecol.add(MERGEINDEX.HGHRES.ordinal());
					hidecol.add(MERGEINDEX.HGORES.ordinal());
				
				
				

				
				
				for(int i = 0;i < hidecol.size(); i++ ){
				    
				    TableColumn   column=columnModel.getColumn(hidecol.elementAt(i));   
				    column.setMinWidth(0);   
				    column.setMaxWidth(0);
				    column.setWidth(0);
				    column.setPreferredWidth(0);
				}

				
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	    
	    
	    
	  
	    public class MyTableModel extends AbstractTableModel  
	    {  
	        /* 
	         * 这里和刚才一样，定义列名和每个数据的值 
	         */  
	        String[] columnNames =  
	        	//{ "序号", "联赛", "时间", "球队", "让球盘", "平博" , "平博滚动盘", "智博","皇冠",  "全场让球", "大小盘", "平博 ", "平博滚动盘 ", "智博 ","皇冠 ", "全场大小"};  
	        	{ "序号", "联赛", "时间", "球队", "让球初盘", "终盘", "盘口分析", "平博", "平博滚动盘", "智博",  "全场让球", "大小初盘", "终盘","盘口分析","平博 ", "平博滚动盘 ", "智博 ", "全场大小"};  

	        

	  
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
	           
	        	
	        	
	        	
	        	
	        	
	        	if(columnIndex == MERGETABLEHEADINDEX.P8HOMERES.ordinal()){
	        		
	        		String res = showItemVec.elementAt(rowIndex)[columnIndex];
	        		if(res.contains("=")){
	            		String[] resA = res.split("=");
	            		res = resA[1];
	            		
	            		return res;
	        		}else{
	        			return res;
	        		}

	        		
	        	}else if(columnIndex == MERGETABLEHEADINDEX.P8HOMEINPLAYRES.ordinal()){
	        		
	        		String res = showItemVec.elementAt(rowIndex)[columnIndex];
	        		if(res.contains("=")){
	            		String[] resA = res.split("=");
	            		res = resA[1];
	            		
	            		return res;
	        		}else{
	        			return res;
	        		}

	        		
	        	}else if(columnIndex == MERGETABLEHEADINDEX.P8OVERRES.ordinal()){
	        		
	        		String res = showItemVec.elementAt(rowIndex)[columnIndex];
	        		if(res.contains("=")){
	            		String[] resA = res.split("=");
	            		res = resA[1];
	            		
	            		return res;
	        		}else{
	        			return res;
	        		}

	        		
	        	}else if(columnIndex == MERGETABLEHEADINDEX.P8OVERINPLAYRES.ordinal()){
	        		
	        		String res = showItemVec.elementAt(rowIndex)[columnIndex];
	        		if(res.contains("=")){
	            		String[] resA = res.split("=");
	            		res = resA[1];
	            		
	            		return res;
	        		}else{
	        			return res;
	        		}

	        		
	        	}else{
	        		return showItemVec.elementAt(rowIndex)[columnIndex];
	        	}
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
