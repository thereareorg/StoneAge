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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.table.JTableHeader;

import org.python.icu.util.Calendar;

import HGclient.DXQdetailsWindow.MyTableModel;
import P8.Common;
import P8.DateChooser;



class ratioClassifyItem{
	public String classify = "";
	public int pankoutotalgames = 0;
	public int pankouwingames = 0;
	public int pankoulosegames = 0;
	public int dxqtotalgames = 0;
	public int dxqwingames = 0;
	public int dxqlosegames = 0;
}


class ratioGamesnumCompare implements Comparator //ʵ��Comparator�������Լ��ıȽϷ���
{

	public int compare(Object o1, Object o2) {
		
		try{
			
			String[] g1 = (String[])(o1);
			String[] g2 = (String[])(o2);

			
			
			String gn1 = (String)g1[RatioAnalysisWindow.clickcolumnIndex];
			String gn2 = (String)g2[RatioAnalysisWindow.clickcolumnIndex];

			int n1 = Integer.parseInt(gn1);
			int n2 = Integer.parseInt(gn2);
			
			
			if(n1 < n2)//����Ƚ��ǽ���,����-1�ĳ�1��������.
			{
			   return 1;
			}
			else if(n1 == n2)
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


/*class gamesnumFenziCompare implements Comparator //ʵ��Comparator�������Լ��ıȽϷ���
{

	public int compare(Object o1, Object o2) {
		
		try{
			
			String[] g1 = (String[])(o1);
			String[] g2 = (String[])(o2);

			
			
			String gn1 = (String)g1[2];
			String gn2 = (String)g2[2];

			int n1 = Integer.parseInt(gn1);
			int n2 = Integer.parseInt(gn2);
			
			int fenzi1 = Integer.parseInt(g1[RatioAnalysisWindow.clickcolumnIndex]);
			int fenzi2 = Integer.parseInt(g2[RatioAnalysisWindow.clickcolumnIndex]);
			
			double r1 = fenzi1/n1;
			double r2 = fenzi2/n2;
			
			if(r1 < r2)//����Ƚ��ǽ���,����-1�ĳ�1��������.
			{
			   return 1;
			}
			else if(r1 == r2)
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
}*/






public class RatioAnalysisWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1000L;
	
	
	public Vector<GameDetails> gameDetailsVec = new Vector<GameDetails>(); 
	
	public Vector<String[]> showitemVec = new Vector<String[]>();

	public Vector<ratioClassifyItem> ratioClassifyItemVec = new Vector<ratioClassifyItem>();
	
    private JLabel datelb1 = new JLabel("起始日期:");
    DateChooser mpstart = new DateChooser("ratioAnsdates");
	

    private JLabel datelb2 = new JLabel("结束日期:");
    DateChooser mpend = new DateChooser("ratioAnsdatee");
    
    private JLabel comparetimelb = new JLabel("与开赛前比较时间:");
    private JTextField comparetimetxt = new JTextField(15); 
    
    private int comparemins = 59;
    
    private JLabel ratioChangelb = new JLabel("水位变动最小值:");
    private JTextField ratioChangetxt = new JTextField(15);
    
    private int ratioChange = 10;
    
    
    public static int clickcolumnIndex = 2;
    

	
	RatioAnalysisWindow(){
		
		
		setTitle("水位分析");
		
		
		intiComponent();
	}
	
	

	
    MyTableModel tableMode = new MyTableModel();
    
    
    JTable table = null;
    
    
    private JLabel classirylb = new JLabel("分类选择:");
    private JCheckBox allcb = new JCheckBox("汇总");
    boolean bclassifyByall = true;
    
    private JCheckBox leaguecb = new JCheckBox("联赛名");
    boolean bclassifyByleague = false;
    
    
    
 
    
    
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
			
			//System.out.println("符合条件的球赛");
			
			for(int i = 0; i< gamesvec.size(); i++){
				GameDetails gameitem = new GameDetails();
				gameitem.eventid = gamesvec.elementAt(i).eventid;
				gameitem.datetime = gamesvec.elementAt(i).datetime;
				gameitem.league = gamesvec.elementAt(i).league;
				gameitem.teamh = gamesvec.elementAt(i).teamh;
				gameitem.teamc = gamesvec.elementAt(i).teamc;
				gameitem.gameresult = gamesvec.elementAt(i).gameresult;
				
				gameitem.currentpankou = gamesvec.elementAt(i).currentpankou;
				gameitem.currentscore = gamesvec.elementAt(i).currentscore;
				gameitem.pankouh = gamesvec.elementAt(i).pankouh;
				gameitem.pankouc = gamesvec.elementAt(i).pankouc;
				gameitem.ouh = gamesvec.elementAt(i).ouh;
				gameitem.ouc = gamesvec.elementAt(i).ouc;
				

				
				gameDetailsVec.add(gameitem);
				
				//System.out.println(gameitem.teamh + "vs" + gameitem.teamc);


			}

			
			//System.out.println("-------------");
			
			updateShowItem();
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	
	public int getComparemins(){
		return comparemins;
	}
	
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
								//System.out.println(gameDetailsVec.elementAt(i).teamh + "vs" + gameDetailsVec.elementAt(i).teamc);
								break;
							}
							
							if(!odds[HGODDSINDEX.PANKOU.ordinal()].equals(tmp.getodds().elementAt(latestOdd)[HGODDSINDEX.PANKOU.ordinal()])){
								continue;
							}
							
							
							if(tmp.getodds().elementAt(j).length < 9){
								/*System.out.println(Arrays.toString(tmp.getodds().elementAt(j)));
								System.out.println("fuck fuck");*/
								
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
							//System.out.println(gameDetailsVec.elementAt(i).teamh + "vs" + gameDetailsVec.elementAt(i).teamc);
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
								/*System.out.println(Arrays.toString(tmp.getodds().elementAt(j)));
								System.out.println("fuck fuck");*/
								
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
	
	
	
	
	
	
	public void showbyall(){
		try{
			int pankoutotalgames = 0;
			int pankouwingames = 0;
			int pankoulosegames =0;
			int dxqtotalgames = 0;
			int dxqwingames = 0;
			int dxqlosegames = 0;
			
			if(showitemVec.size() != 0){
				showitemVec.clear();
			}
			
			

			
			for(int i = 0; i < gameDetailsVec.size(); i++){
				GameDetails onegame = gameDetailsVec.elementAt(i);
				
				
				if(onegame.gameresult.equals("")){
					continue;
				}
				
        		
				
				
				
				//让球盘开始
    			String hdvaluestr = "";
    			String pankoures = "";
    			
    			
    			
    			if(onegame.pankouh != -1000 && Math.abs(onegame.pankouh) >= ratioChange){
    				hdvaluestr = Integer.toString(onegame.pankouh);
    				
    				
    				if(onegame.gameresult.contains("-")){
    					
    					int scoreh = Integer.parseInt(onegame.gameresult.split("-")[0]);
    					int scorec = Integer.parseInt(onegame.gameresult.split("-")[1]);
    					int scoredvalue = scoreh - scorec;
    					
    					String tmppankoustr = onegame.currentpankou;
    					
    					pankoutotalgames++;	//让球盘总数
    					
    					//System.out.println(onegame.teamh + "vs" + onegame.teamc);
    					
    					
    					if(tmppankoustr.contains("C")){	//客队让球
    						tmppankoustr = tmppankoustr.replace("C", "");
    						tmppankoustr = tmppankoustr.replace(" ", "");
    						
    						scoredvalue = scorec -  scoreh;
    						
    						
    						if(tmppankoustr.contains("/")){
    							double tmp1 = Double.parseDouble(tmppankoustr.split("/")[0]);
    							double tmp2 = Double.parseDouble(tmppankoustr.split("/")[1]);
    							
    							if(scoredvalue <= tmp1){	//买主队的人输
    								if(onegame.pankouh < 0){
    									pankoures = "输";
    									pankoulosegames++;
    								}else{
    									pankoures = "赢";
    									pankouwingames++;
    								}

    								
    							}else if(scoredvalue >= tmp2){
    								if(onegame.pankouh < 0){
    									pankoures = "赢";
    									pankouwingames++;
    								}else{
    									pankoures = "输";
    									pankoulosegames++;
    								}
    							}
    							
    						}else{
    							
    							double tmp = Double.parseDouble(tmppankoustr);
    							
    							if(scoredvalue < tmp){	//买主队的人输
    								if(onegame.pankouh < 0){
    									pankoures = "输";
    									pankoulosegames++;
    								}else{
    									pankoures = "赢";
    									pankouwingames++;
    								}

    								
    							}else if(scoredvalue > tmp){
    								if(onegame.pankouh < 0){
    									pankoures = "赢";
    									pankouwingames++;
    								}else{
    									pankoures = "输";
    									pankoulosegames++;
    								}
    							}else if(scoredvalue == tmp){
    								pankoures = "走水";
    							}
    							
    							
    						}
    						
    						
    						
    					}else{	//主队让球
    						
    						scoredvalue = scoreh - scorec;
    						
    						tmppankoustr = tmppankoustr.replace("H", "");
    						tmppankoustr = tmppankoustr.replace(" ", "");
    						
    						
    						
    						if(tmppankoustr.contains("/")){
    							double tmp1 = Double.parseDouble(tmppankoustr.split("/")[0]);
    							double tmp2 = Double.parseDouble(tmppankoustr.split("/")[1]);
    							
    							if(scoredvalue <= tmp1){	//买主队的人输
    								if(onegame.pankouh < 0){
    									pankoures = "赢";
    									pankouwingames++;
    								}else{
    									pankoures = "输";
    									pankoulosegames++;
    								}

    								
    							}else if(scoredvalue >= tmp2){
    								if(onegame.pankouh < 0){
    									pankoures = "输";
    									pankoulosegames++;
    								}else{
    									pankoures = "赢";
    									pankouwingames++;
    								}
    							}
    							
    						}else{
    							
    							double tmp = Double.parseDouble(tmppankoustr);
    							
    							if(scoredvalue < tmp){	//买主队的人输
    								if(onegame.pankouh < 0){
    									pankoures = "赢";
    									pankouwingames++;
    								}else{
    									pankoures = "输";
    									pankoulosegames++;
    								}

    								
    							}else if(scoredvalue > tmp){
    								if(onegame.pankouh < 0){
    									pankoures = "输";
    									pankoulosegames++;
    								}else{
    									pankoures = "赢";
    									pankouwingames++;
    								}
    							}else if(scoredvalue == tmp){
    								pankoures = "走水";
    							}
    							
    							
    						}
    					}
    					
    				}
    				

    			}//让球盘结束
				
    			
    			
    			
    			//大小球盘开始
    			String odvaluestr = "";
    			
    			String dxqres = "";
    			
    			if(onegame.ouh != -1000 && Math.abs(onegame.ouh) >= ratioChange){
    				odvaluestr = Integer.toString(onegame.ouh);
    				
    				if(onegame.gameresult.contains("-")){
    					
    					dxqtotalgames++;	//大小球总数
    					
    					int scoreh = Integer.parseInt(onegame.gameresult.split("-")[0]);
    					int scorec = Integer.parseInt(onegame.gameresult.split("-")[1]);
    					int scoretvalue = scoreh + scorec;
    					
    					String tmpdxqstr = onegame.currentscore;
    					tmpdxqstr = tmpdxqstr.replace("O", "");
					
						
    					tmpdxqstr = tmpdxqstr.replace(" ", "");
						if(tmpdxqstr.contains("/")){
							double tmp1 = Double.parseDouble(tmpdxqstr.split("/")[0]);
							double tmp2 = Double.parseDouble(tmpdxqstr.split("/")[1]);
							
							if(scoretvalue <= tmp1){	
								if(onegame.ouh < 0){
									dxqres = "赢";
									dxqwingames++;
								}else{
									dxqres = "输";
									dxqlosegames++;
								}

								
							}else if(scoretvalue >= tmp2){
								if(onegame.ouh < 0){
									dxqres = "输";
									dxqlosegames++;
								}else{
									dxqres = "赢";
									dxqwingames++;
								}
							}
							
						}else{
							
							double tmp = Double.parseDouble(tmpdxqstr);
							
							if(scoretvalue < tmp){	//买主队的人输
								if(onegame.ouh < 0){
									dxqres = "赢";
									dxqwingames++;
								}else{
									dxqres = "输";
									dxqlosegames++;
								}

								
							}else if(scoretvalue > tmp){
								if(onegame.ouh < 0){
									dxqres = "输";
									dxqlosegames++;
								}else{
									dxqres = "赢";
									dxqwingames++;
								}
							}else if(scoretvalue == tmp){
								dxqres = "走水";
							}
							
							
						}
    					
    					
    				}
    				
    				
    			}
    			//大小球盘结束
    			
    			


			}
	
			String[] item = {"1", "总", Integer.toString(pankoutotalgames), Integer.toString(pankouwingames),Integer.toString(pankoulosegames),
					Integer.toString(dxqtotalgames),Integer.toString(dxqwingames),Integer.toString(dxqlosegames)};
			
			
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
			
			
			if(ratioClassifyItemVec.size() != 0){
				ratioClassifyItemVec.clear();
			}
			

			
			for(int i = 0; i < gameDetailsVec.size(); i++){
				GameDetails onegame = gameDetailsVec.elementAt(i);
				
				if(onegame.gameresult.equals("")){
					continue;
				}
				
        		
				
				
				
				//让球盘开始
    			String hdvaluestr = "";
    			String pankoures = "";
    			
    			
    			
    			if(onegame.pankouh != -1000 && Math.abs(onegame.pankouh) >= ratioChange){
    				hdvaluestr = Integer.toString(onegame.pankouh);
    				
    				
    				if(onegame.gameresult.contains("-")){
    					
    					int scoreh = Integer.parseInt(onegame.gameresult.split("-")[0]);
    					int scorec = Integer.parseInt(onegame.gameresult.split("-")[1]);
    					int scoredvalue = scoreh - scorec;
    					
    					String tmppankoustr = onegame.currentpankou;
    					
    					
    					
    					int findIndex = -1;
						for(int k = 0; k < ratioClassifyItemVec.size(); k++){
							if(ratioClassifyItemVec.elementAt(k).classify.equals(onegame.league)){
								
								findIndex = k;
								break;
							}
						}
						
						if(findIndex == -1){
							ratioClassifyItem tmpitem = new ratioClassifyItem();
							tmpitem.classify = onegame.league;
							ratioClassifyItemVec.add(tmpitem);
							findIndex = ratioClassifyItemVec.size() -1;
						}
    					
    					
						ratioClassifyItemVec.elementAt(findIndex).pankoutotalgames++;
    					
    					
    					//System.out.println(onegame.teamh + "vs" + onegame.teamc);
    					
    					
    					if(tmppankoustr.contains("C")){	//客队让球
    						tmppankoustr = tmppankoustr.replace("C", "");
    						tmppankoustr = tmppankoustr.replace(" ", "");
    						
    						scoredvalue = scorec -  scoreh;
    						
    						
    						if(tmppankoustr.contains("/")){
    							double tmp1 = Double.parseDouble(tmppankoustr.split("/")[0]);
    							double tmp2 = Double.parseDouble(tmppankoustr.split("/")[1]);
    							
    							if(scoredvalue <= tmp1){	//买主队的人输
    								if(onegame.pankouh < 0){
    									pankoures = "输";
    									
    									ratioClassifyItemVec.elementAt(findIndex).pankoulosegames++;
    								}else{
    									pankoures = "赢";
    									ratioClassifyItemVec.elementAt(findIndex).pankouwingames++;
    								}

    								
    							}else if(scoredvalue >= tmp2){
    								if(onegame.pankouh < 0){
    									pankoures = "赢";
    									ratioClassifyItemVec.elementAt(findIndex).pankouwingames++;
    								}else{
    									pankoures = "输";
    									ratioClassifyItemVec.elementAt(findIndex).pankoulosegames++;
    								}
    							}
    							
    						}else{
    							
    							double tmp = Double.parseDouble(tmppankoustr);
    							
    							if(scoredvalue < tmp){	//买主队的人输
    								if(onegame.pankouh < 0){
    									pankoures = "输";
    									ratioClassifyItemVec.elementAt(findIndex).pankoulosegames++;
    								}else{
    									pankoures = "赢";
    									ratioClassifyItemVec.elementAt(findIndex).pankouwingames++;
    								}

    								
    							}else if(scoredvalue > tmp){
    								if(onegame.pankouh < 0){
    									pankoures = "赢";
    									ratioClassifyItemVec.elementAt(findIndex).pankouwingames++;
    								}else{
    									pankoures = "输";
    									ratioClassifyItemVec.elementAt(findIndex).pankoulosegames++;
    								}
    							}else if(scoredvalue == tmp){
    								pankoures = "走水";
    							}
    							
    							
    						}
    						
    						
    						
    					}else{	//主队让球
    						
    						scoredvalue = scoreh - scorec;
    						
    						tmppankoustr = tmppankoustr.replace("H", "");
    						tmppankoustr = tmppankoustr.replace(" ", "");
    						
    						
    						
    						if(tmppankoustr.contains("/")){
    							double tmp1 = Double.parseDouble(tmppankoustr.split("/")[0]);
    							double tmp2 = Double.parseDouble(tmppankoustr.split("/")[1]);
    							
    							if(scoredvalue <= tmp1){	//买主队的人输
    								if(onegame.pankouh < 0){
    									pankoures = "赢";
    									ratioClassifyItemVec.elementAt(findIndex).pankouwingames++;
    								}else{
    									pankoures = "输";
    									ratioClassifyItemVec.elementAt(findIndex).pankoulosegames++;
    								}

    								
    							}else if(scoredvalue >= tmp2){
    								if(onegame.pankouh < 0){
    									pankoures = "输";
    									ratioClassifyItemVec.elementAt(findIndex).pankoulosegames++;
    								}else{
    									pankoures = "赢";
    									ratioClassifyItemVec.elementAt(findIndex).pankouwingames++;
    								}
    							}
    							
    						}else{
    							
    							double tmp = Double.parseDouble(tmppankoustr);
    							
    							if(scoredvalue < tmp){	//买主队的人输
    								if(onegame.pankouh < 0){
    									pankoures = "赢";
    									ratioClassifyItemVec.elementAt(findIndex).pankouwingames++;
    								}else{
    									pankoures = "输";
    									ratioClassifyItemVec.elementAt(findIndex).pankoulosegames++;
    								}

    								
    							}else if(scoredvalue > tmp){
    								if(onegame.pankouh < 0){
    									pankoures = "输";
    									ratioClassifyItemVec.elementAt(findIndex).pankoulosegames++;
    								}else{
    									pankoures = "赢";
    									ratioClassifyItemVec.elementAt(findIndex).pankouwingames++;
    								}
    							}else if(scoredvalue == tmp){
    								pankoures = "走水";
    							}
    							
    							
    						}
    					}
    					
    				}
    				

    			}//让球盘结束
    			
    			
    			
    			
    			
    			
    			//大小球盘开始
    			String odvaluestr = "";
    			
    			String dxqres = "";
    			
    			if(onegame.ouh != -1000 && Math.abs(onegame.ouh) >= ratioChange){
    				odvaluestr = Integer.toString(onegame.ouh);
    				
    				if(onegame.gameresult.contains("-")){
    					
    					
    					
    					int scoreh = Integer.parseInt(onegame.gameresult.split("-")[0]);
    					int scorec = Integer.parseInt(onegame.gameresult.split("-")[1]);
    					int scoretvalue = scoreh + scorec;
    					
    					String tmpdxqstr = onegame.currentscore;
    					tmpdxqstr = tmpdxqstr.replace("O", "");
    					
    					
    					int findIndex = -1;
						for(int k = 0; k < ratioClassifyItemVec.size(); k++){
							if(ratioClassifyItemVec.elementAt(k).classify.equals(onegame.league)){
								
								findIndex = k;
								break;
							}
						}
						
						if(findIndex == -1){
							ratioClassifyItem tmpitem = new ratioClassifyItem();
							tmpitem.classify = onegame.league;
							ratioClassifyItemVec.add(tmpitem);
							findIndex = ratioClassifyItemVec.size() -1;
						}
    					
    					
						ratioClassifyItemVec.elementAt(findIndex).dxqtotalgames++;
    					
    					
    					
					
						
    					tmpdxqstr = tmpdxqstr.replace(" ", "");
						if(tmpdxqstr.contains("/")){
							double tmp1 = Double.parseDouble(tmpdxqstr.split("/")[0]);
							double tmp2 = Double.parseDouble(tmpdxqstr.split("/")[1]);
							
							if(scoretvalue <= tmp1){	
								if(onegame.ouh < 0){
									dxqres = "赢";
									ratioClassifyItemVec.elementAt(findIndex).dxqwingames++;
								}else{
									dxqres = "输";
									ratioClassifyItemVec.elementAt(findIndex).dxqlosegames++;
								}

								
							}else if(scoretvalue >= tmp2){
								if(onegame.ouh < 0){
									dxqres = "输";
									ratioClassifyItemVec.elementAt(findIndex).dxqlosegames++;
								}else{
									dxqres = "赢";
									ratioClassifyItemVec.elementAt(findIndex).dxqwingames++;
								}
							}
							
						}else{
							
							double tmp = Double.parseDouble(tmpdxqstr);
							
							if(scoretvalue < tmp){	//买主队的人输
								if(onegame.ouh < 0){
									dxqres = "赢";
									ratioClassifyItemVec.elementAt(findIndex).dxqwingames++;
								}else{
									dxqres = "输";
									ratioClassifyItemVec.elementAt(findIndex).dxqlosegames++;
								}

								
							}else if(scoretvalue > tmp){
								if(onegame.ouh < 0){
									dxqres = "输";
									ratioClassifyItemVec.elementAt(findIndex).dxqlosegames++;
								}else{
									dxqres = "赢";
									ratioClassifyItemVec.elementAt(findIndex).dxqwingames++;
								}
							}else if(scoretvalue == tmp){
								dxqres = "走水";
							}
							
							
						}
    					
    					
    				}
    				
    				
    			}
    			//大小球盘结束
    			
    			
    			
				
			}


			
			
			for(int i = 0; i < ratioClassifyItemVec.size(); i++){
				ratioClassifyItem ci = ratioClassifyItemVec.elementAt(i);
				String[] item = {Integer.toString(i+1), ci.classify, Integer.toString(ci.pankoutotalgames), Integer.toString(ci.pankouwingames), 
						Integer.toString(ci.pankoulosegames), Integer.toString(ci.dxqtotalgames), Integer.toString(ci.dxqwingames), Integer.toString(ci.dxqlosegames)};
				
				showitemVec.add(item);
			}
			
			
			if(clickcolumnIndex == 2 || clickcolumnIndex == 5){
				Comparator cn = new ratioGamesnumCompare(); 

	        	if(showitemVec.size() != 0){
	        		
	        		Collections.sort(showitemVec, cn);

	        	}
			}

        	
        	for(int i = 0; i < showitemVec.size(); i++){
        		showitemVec.elementAt(i)[0] = Integer.toString(i+1);
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
		
		JPanel panelNorth = new JPanel(new GridLayout(1, 14));

        container.add(panelNorth, BorderLayout.NORTH);  
        

        panelNorth.add(datelb1);
        panelNorth.add(mpstart);

        panelNorth.add(datelb2);
        panelNorth.add(mpend);
        

        panelNorth.add(comparetimelb);
        panelNorth.add(comparetimetxt);
        
        panelNorth.add(ratioChangelb);
        panelNorth.add(ratioChangetxt);

        panelNorth.add(classirylb);
        panelNorth.add(allcb);

        panelNorth.add(leaguecb);

        
        

        

        

        
        comparetimetxt.setText(Integer.toString(comparemins));

        comparetimetxt.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = comparetimetxt.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	comparemins = Integer.parseInt(value);
                    	
                    	//sortgamedetails();
                    	HGclienthttp.constructDaysGaemDetailsForRatioAns();
                    	
                    	
                    	updateShowItem();
                    }
                    
                }  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });
        
        
        
        
        ratioChangetxt.setText(Integer.toString(ratioChange));

        ratioChangetxt.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = ratioChangetxt.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	ratioChange = Integer.parseInt(value);
                    	
                    	//sortgamedetails();
                    	
                    	updateShowItem();
                    }
                    
                }  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });
        
        
        
        
        
        
        
        
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
        
        
        
        final JTableHeader header = table.getTableHeader();
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	int tmpclickcolumnIndex = header.columnAtPoint(e.getPoint());
                
                if(tmpclickcolumnIndex ==2 ||tmpclickcolumnIndex == 5){
                	clickcolumnIndex = tmpclickcolumnIndex;
                	updateShowItem();
                }
                
            }
        });
        
        
        
        
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
        	 { "序号", "分类", "让球总场*","让球赢场", "让球输场", "大小球总场*", "大小球赢场" , "大小球输场"};
        

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
