package P8;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

import org.json.JSONObject;



public class ScoreMergeManager {
	
	static BufferedWriter fw = null;  //写	
	static BufferedReader reader = null; //读
	
	public static Map<String,String> checkList = new LinkedHashMap<String,String>();
	
	private static ReadWriteLock lockeFinalMergeEventsDetails = new ReentrantReadWriteLock();
	
	
	public static Vector<String[]> finalMergeEventDetailsVec = new Vector<String[]>();

	public static Vector<String[]> mergeEventDetailsVec = new Vector<String[]>();
	
	public static MergeDetailsWindow mergeDetailsWnd = new MergeDetailsWindow();
	
	
	
	public static int mergeHideNumber = 5000;
	
	public static TeamMatchWindow teamMatchWnd = new TeamMatchWindow("score");
	
	
	
	public static String[] scoreSelectedRow = null;
	public static String[] p8SelectedRow = null;
	
	
	
	
	
	
	public static int mergep0hSendNumber = 1800000;
	public static int mergep0oSendNumber = 700000;
	public static int mergep0oHideSendNumber = 100000;
	
	static Map<String, Vector<Integer>> mailRecords = new HashMap<String, Vector<Integer>>();  
	

	
	public static ScoreMergeWindow mergeWnd = new ScoreMergeWindow();
	
	public static Vector<String> notAddtomerge = new Vector<String>(); 
	
	
	
	
/*    static MergePreviousDataWindow pDataWindow = new MergePreviousDataWindow();
    
    static MergePreviousDataManager pDataManager = new MergePreviousDataManager(pDataWindow);
	
    
    
	public static void showpDataWnd(){
		pDataWindow.setVisible(true);
	}
    
	
	public static void updatepDataDetails(){
		pDataManager.updatepEventsDetails();
	}
    


    public static Vector<String[]> getpSubMergeevents(){
    	return pDataManager.getpSubevents();
    }*/
    
    public static void showTeamMatchWnd(){
    	teamMatchWnd.setVisible(true);
    }
    
	
	
	public static void showMergeWnd(boolean flag){
		
		setP8Txt();
		setScoreTxt();
		
		mergeWnd.setVisible(flag);
	}
	
	public static void showMergeDetailsWnd(boolean flag){
		mergeDetailsWnd.setVisible(flag);
	}
	
	
	

	
	

	
	
	
	public static void init() throws IOException{
		
		teamMatchWnd.setTitle("比分网匹配管理");
		
		//pDataManager.init();
		
		try{
			
			File dir = new File("data");
			if (dir.exists()) {
			} else {
				dir.mkdirs();
			}
			
			File file = new File("data/" + "scorecheckList"
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
	
	
	
    public static void copyTofinalEventsDetails(){
    	

    	
		lockeFinalMergeEventsDetails.writeLock().lock();
		finalMergeEventDetailsVec = (Vector<String[]>)mergeEventDetailsVec.clone();
    	
    	lockeFinalMergeEventsDetails.writeLock().unlock();
    	

    }
    
    public static Vector<String[]> getFinalEventsDetails(){
    	Vector<String[]> vec = null;
    	lockeFinalMergeEventsDetails.readLock().lock();
    	vec = (Vector<String[]>)finalMergeEventDetailsVec.clone();
    	lockeFinalMergeEventsDetails.readLock().unlock();
    	return vec;
    }
	
	
	
	public static String getchecklist(){
		try{
			
			
			
			JSONObject gameObj = new JSONObject(checkList);
			
			String res = gameObj.toString();

			
			
			return res;
			
		}catch(Exception e){
			e.printStackTrace();
			return "[]";
		}
	}

	

	public static boolean saveTofile(String p8Name, String scoreName){
		
		try{
			

				
				String find = checkList.get(p8Name);
				
				if(null != find){
					if(!find.contains(scoreName)){
						return false;
					}else{
						return true;
					}
						
				}

			
			
			File file = new File("data/" + "scorecheckList"
					+ ".data");
			
			BufferedWriter fwlocal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			
			
			fwlocal.append(p8Name + "," + scoreName);
			fwlocal.newLine();
			fwlocal.flush();
			
			fwlocal.close();
			
			checkList.put(p8Name, scoreName);
			
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
					
					File file = new File("data/" + "scorecheckList"
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
					
					//checkList.put(p8Name, scoreName);
					
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
	
	
	
	
	public static void clearMergeData(){
		if(mergeEventDetailsVec.size() != 0){
			mergeEventDetailsVec.clear();
		}
	}
	
	

	
	
	
	
	
	
	public static String findScoreTeam(String p8team){
		
		String scoreteam = checkList.get(p8team);
		
		return scoreteam;
		
	}
	
	
	public static void setP8Txt(){
		mergeWnd.setP8Txt(p8SelectedRow);
	}
	
	public static void setScoreTxt(){
		mergeWnd.setScoreTxt(scoreSelectedRow);
	}
	
}
