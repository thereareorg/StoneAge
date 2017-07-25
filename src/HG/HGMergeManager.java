package HG;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import P8.MergeDetailsWindow;
import P8.MergePreviousDataManager;
import P8.MergePreviousDataWindow;
import P8.TeamMatchWindow;
import P8.ZHIBOINDEX;

public class HGMergeManager {
	
	static BufferedWriter fw = null;  //写	
	static BufferedReader reader = null; //读
	
	public static Map<String,String> checkList = new LinkedHashMap<String,String>();
	
	private static ReadWriteLock lockeFinalMergeEventsDetails = new ReentrantReadWriteLock();
	
	
	public static Vector<String[]> finalMergeEventDetailsVec = new Vector<String[]>();

	public static Vector<String[]> mergeEventDetailsVec = new Vector<String[]>();
	
	public static MergeDetailsWindow mergeDetailsWnd = new MergeDetailsWindow();
	
	
	
	public static int mergeHideNumber = 5000;
	
	public static TeamMatchWindow teamMatchWnd = new TeamMatchWindow("hg");
	
	
	
	public static String[] hgSelectedRow = null;
	public static String[] p8SelectedRow = null;
	
	
	
	
	
	
	public static int mergep0hSendNumber = 1800000;
	public static int mergep0oSendNumber = 700000;
	public static int mergep0oHideSendNumber = 100000;
	
	static Map<String, Vector<Integer>> mailRecords = new HashMap<String, Vector<Integer>>();  
	

	
	public static HGMergeWindow mergeWnd = new HGMergeWindow();
	
	public static Vector<String> notAddtomerge = new Vector<String>(); 
	
	
	
	

	
    
    

    



    
    public static void showTeamMatchWnd(){
    	teamMatchWnd.setVisible(true);
    }
    
	
	
	public static void showMergeWnd(boolean flag){
		
		setP8Txt();
		setHGTxt();
		
		mergeWnd.setVisible(flag);
	}
	
	public static void showMergeDetailsWnd(boolean flag){
		mergeDetailsWnd.setVisible(flag);
	}
	
	
	

	
	

	
	
	
	public static void init(){
		
		teamMatchWnd.setTitle("皇冠队名匹配管理");
		
		try{
			
			File dir = new File("data");
			if (dir.exists()) {
			} else {
				dir.mkdirs();
			}
			
			File file = new File("data/" + "hgcheckList"
					+ ".data");
			
			
			if(!file.exists()){
				fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
				fw.close();
			}else{
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); // 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码,
				
				String str = null;
				
				String lastLine = null;
				
				while ((str = reader.readLine()) != null) {
					//System.out.println(str);
					String[] item = str.split(",");
					
					checkList.put(item[0], item[1]);
					
						
					}
				//fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			
			}
			
			
			teamMatchWnd.updateEventsDetails(checkList);
			
			//pdataWnd.updateEventsDetails(pEventsDetails);
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	
	

	
	
	
	
	

	
	
	

	public static boolean saveTofile(String p8Name, String hgName){
		
		try{
			

				
				String find = checkList.get(p8Name);
				
				if(null != find){
					if(!find.contains(hgName)){
						return false;
					}else{
						return true;
					}
						
				}

			
			
			File file = new File("data/" + "hgcheckList"
					+ ".data");
			
			BufferedWriter fwlocal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			
			
			fwlocal.append(p8Name + "," + hgName);
			fwlocal.newLine();
			fwlocal.flush();
			
			fwlocal.close();
			
			checkList.put(p8Name, hgName);
			
			//updatepEventsDetails();
			
			teamMatchWnd.updateEventsDetails(checkList);
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	public static boolean deleteateammatch(String p8Name){
		
		try{
			

				
				String find = checkList.get(p8Name);
				
				if(null != find){
					
					checkList.remove(p8Name);
					
					File file = new File("data/" + "hgcheckList"
							+ ".data");
					
					BufferedWriter fwlocal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
					
					
					for (String key : checkList.keySet()) {  
						  
					    String value = (String)checkList.get(key);
					    
					    String[] tmp = {key, value};
					    
						fwlocal.append(key + "," + value);
						fwlocal.newLine();
						fwlocal.flush();
					  
					}  

					
					fwlocal.close();
					
					//checkList.put(p8Name, hgName);
					
					teamMatchWnd.updateEventsDetails(checkList);

					
					return true;
						
				}else{
					return false;
				}

			
			

			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	

	
	

	
	
	
	
	
	
	public static String findHGTeam(String p8team){
		
		String hgteam = checkList.get(p8team);
		
		return hgteam;
		
	}
	
	
	public static void setP8Txt(){
		mergeWnd.setP8Txt(p8SelectedRow);
	}
	
	public static void setHGTxt(){
		mergeWnd.setHGTxt(hgSelectedRow);
	}
	
}
