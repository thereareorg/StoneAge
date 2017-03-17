package P8;

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
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MergeManager {
	
	static BufferedWriter fw = null;  //写	
	static BufferedReader reader = null; //读
	
	public static Map<String,String> checkList = new HashMap<String,String>();
	
	private static ReadWriteLock lockeFinalMergeEventsDetails = new ReentrantReadWriteLock();
	
	
	public static Vector<String[]> finalMergeEventDetailsVec = new Vector<String[]>();

	public static Vector<String[]> mergeEventDetailsVec = new Vector<String[]>();
	
	public static MergeDetailsWindow mergeDetailsWnd = new MergeDetailsWindow();
	
	
	
	public static String[] zhiboSelectedRow = null;
	public static String[] p8SelectedRow = null;
	
	public static MergeWindow mergeWnd = new MergeWindow();
	
	
	
	
    static MergePreviousDataWindow pDataWindow = new MergePreviousDataWindow();
    
    static MergePreviousDataManager pDataManager = new MergePreviousDataManager(pDataWindow);
	
    
    
	public static void showpDataWnd(){
		pDataWindow.setVisible(true);
	}
    
	
	public static void updatepDataDetails(){
		pDataManager.updatepEventsDetails();
	}
    


    
    
	
	
	public static void showMergeWnd(boolean flag){
		
		setP8Txt();
		setZhiboTxt();
		
		mergeWnd.setVisible(flag);
	}
	
	public static void showMergeDetailsWnd(boolean flag){
		mergeDetailsWnd.setVisible(flag);
	}
	
	
	
	public static void updateEventsDetails(){
		mergeDetailsWnd.updateEventsDetails(mergeEventDetailsVec);
	}
	
	

	
	
	
	public static void init(){
		
		pDataManager.init();
		
		try{
			
			File dir = new File("data");
			if (dir.exists()) {
			} else {
				dir.mkdirs();
			}
			
			File file = new File("data/" + "checkList"
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
	
	
	
	
	
    public static void saveEvents(){
		try{
			
			
			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
			
			SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			
			long currentTimeL = System.currentTimeMillis();
			
			String todayStr = dfDay.format(currentTimeL);
			
			//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			for(int i = 0; i < mergeEventDetailsVec.size(); i++){
				//long eventTime = Long.parseLong(eventDetailsVec.elementAt(i)[ZHIBOINDEX.TIME.ordinal()]);
				
				String timeStr = mergeEventDetailsVec.elementAt(i)[ZHIBOINDEX.TIME.ordinal()];
				
				if(!timeStr.contains("-")){
					timeStr = todayStr + " " + timeStr;
				}
				
				long currentTime = System.currentTimeMillis();
				
				long passMinutes = 108*60*1000;
				
				long twoMinutes = 2*60*1000;
				
				
				java.util.Date time = dfMin.parse(timeStr);
				
				
				Calendar eventTime = Calendar.getInstance();  
				eventTime.setTime(time);

				long pass = currentTime - eventTime.getTimeInMillis();
				
				if(pass > passMinutes){
					
					System.out.println("merge events remove:" + Arrays.toString(mergeEventDetailsVec.elementAt(i)));
					
					mergeEventDetailsVec.remove(i);
					i--;
					continue;
				}
				
				if(pass > twoMinutes){
					//mergeEventDetailsVec.elementAt(i)[ZHIBOINDEX.SAVED.ordinal()] = "1";					
					String[] item = mergeEventDetailsVec.elementAt(i).clone();
					item[ZHIBOINDEX.TIME.ordinal()] = timeStr;
					boolean saveRes = pDataManager.saveTofile(item);	
					if(saveRes == true){
						System.out.println("merge save success:" + Arrays.toString(item));
					}
				}

				
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	
	
	

	public static boolean saveTofile(String p8Name, String zhiboName){
		
		try{
			

				
				String find = checkList.get(p8Name);
				
				if(null != find){
					if(!find.contains(zhiboName)){
						return false;
					}else{
						return true;
					}
						
				}

			
			
			File file = new File("data/" + "checkList"
					+ ".data");
			
			BufferedWriter fwlocal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			
			
			fwlocal.append(p8Name + "," + zhiboName);
			fwlocal.newLine();
			fwlocal.flush();
			
			fwlocal.close();
			
			checkList.put(p8Name, zhiboName);
			
			//updatepEventsDetails();
			
			return true;
			
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
	
	
	public static void constructMergeRes(){
		
		try{
			
			Vector<String[]> p8events = (Vector<String[]>)P8Http.getFinalEventsDetails().clone();
			Vector<String[]> zhiboevents = ZhiboManager.getEventdetails();
			
			String[] item = null;
			String p8eventsname = "";
			String[] p8teams = null;
			String[] zhiboitem = null;
			
			
			for(int i = 0; i < p8events.size(); i++){
				item = p8events.elementAt(i).clone();
				
				p8eventsname = item[TYPEINDEX.EVENTNAMNE.ordinal()];
				
				p8teams = p8eventsname.split("-vs-");
				
				String zhiboHome = findZhiboTeam(p8teams[0]);
				
				if(zhiboHome != null){
					String zhiboAway = findZhiboTeam(p8teams[1]);
					
					if(zhiboAway != null){
						String zhiboeventname = zhiboHome + " vs " + zhiboAway;
						
						for(int j = 0; j< zhiboevents.size(); j++){
							zhiboitem = zhiboevents.elementAt(j);
							
							if(zhiboitem[ZHIBOINDEX.EVENTNAMNE.ordinal()].contains(zhiboeventname)
									&&!zhiboitem[ZHIBOINDEX.TIME.ordinal()].contains("(")){
								
								boolean addTomerge = false;
								
								Double p80home = Double.parseDouble(item[TYPEINDEX.PERIOD0HOME.ordinal()]);
								Double p80over = Double.parseDouble(item[TYPEINDEX.PERIOD0OVER.ordinal()]);
								Double p81home = Double.parseDouble(item[TYPEINDEX.PERIOD1HOME.ordinal()]);
								Double p81over = Double.parseDouble(item[TYPEINDEX.PERIOD1OVER.ordinal()]);
								
								Double zhibo0home = Double.parseDouble(zhiboitem[ZHIBOINDEX.PERIOD0HOME.ordinal()]);
								Double zhibo0over = Double.parseDouble(zhiboitem[ZHIBOINDEX.PERIOD0OVER.ordinal()]);
								Double zhibo1home = Double.parseDouble(zhiboitem[ZHIBOINDEX.PERIOD1HOME.ordinal()]);
								Double zhibo1over = Double.parseDouble(zhiboitem[ZHIBOINDEX.PERIOD1OVER.ordinal()]);
								
								
								if((p80home >0.0 && zhibo0home >0.0) || (p80home <0.0 && zhibo0home <0.0)){
									//item[TYPEINDEX.PERIOD0HOME.ordinal()] = String.format("%.0f", p80home + zhibo0home);
									
									item[TYPEINDEX.PERIOD0HOME.ordinal()] = String.format("(%.0f)", p80home) + "+" +
											String.format("(%.0f)", zhibo0home) + "=" + String.format("%.0f", p80home + zhibo0home);
									
									addTomerge = true;
								}else{
									item[TYPEINDEX.PERIOD0HOME.ordinal()] = "0";
								}
								
								if((p80over >0.0 && zhibo0over >0.0) || (p80over <0.0 && zhibo0over <0.0)){
									//item[TYPEINDEX.PERIOD0OVER.ordinal()] = String.format("%.0f", p80over + zhibo0over);
									
									item[TYPEINDEX.PERIOD0OVER.ordinal()] = String.format("(%.0f)", p80over) + "+" +
											String.format("(%.0f)", zhibo0over) + "=" + String.format("%.0f", p80over + zhibo0over);
									
									addTomerge = true;
								}
								else{
									item[TYPEINDEX.PERIOD0OVER.ordinal()] = "0";
								}
								
								if((p81over >0.0 && zhibo1over >0.0) || (p81over <0.0 && zhibo1over <0.0)){
									//item[TYPEINDEX.PERIOD1OVER.ordinal()] = String.format("%.0f", p81over + zhibo1over);
									
									item[TYPEINDEX.PERIOD1OVER.ordinal()] = String.format("(%.0f)", p81over) + "+" +
											String.format("(%.0f)", zhibo1over) + "=" + String.format("%.0f", p81over + zhibo1over);
									
									addTomerge = true;
								}
								else{
									item[TYPEINDEX.PERIOD1OVER.ordinal()] = "0";
								}
								
								if((p81home >0.0 && zhibo1home >0.0) || (p81home <0.0 && zhibo1home <0.0)){
									//item[TYPEINDEX.PERIOD1HOME.ordinal()] = String.format("%.0f", p81home + zhibo1home);
									item[TYPEINDEX.PERIOD1HOME.ordinal()] = String.format("(%.0f)", p81home) + "+" +
											String.format("(%.0f)", zhibo1home) + "=" + String.format("%.0f", p81home + zhibo1home);
									addTomerge = true;
								}else{
									item[TYPEINDEX.PERIOD1HOME.ordinal()] = "0";
								}
								
								if(addTomerge == true){
									mergeEventDetailsVec.add(item);
								}
								
								break;
								
							}
							
						}
						
					}
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	
	
	
	private static String findZhiboTeam(String p8team){
		
		String zhiboteam = checkList.get(p8team);
		
		return zhiboteam;
		
	}
	
	
	public static void setP8Txt(){
		mergeWnd.setP8Txt(p8SelectedRow);
	}
	
	public static void setZhiboTxt(){
		mergeWnd.setZhiboTxt(zhiboSelectedRow);
	}
	
}
