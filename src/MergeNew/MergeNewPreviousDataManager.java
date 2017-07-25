package MergeNew;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class MergeNewPreviousDataManager {
	BufferedWriter fw = null;  //写	
	BufferedReader reader = null; //读
	
	Vector<String[]> pEventsDetails = new Vector<String[]>();
	
	
	private ReadWriteLock lockepSubeventsDetails = new ReentrantReadWriteLock();
	
	Vector<String[]> pSubEventsDetails = new Vector<String[]>();
	
	
	
	MergeNewPreviousDataWindow pdataWnd = null; 
	
	MergeNewPreviousDataManager(MergeNewPreviousDataWindow wnd) {
		pdataWnd = wnd;
	}
	
	public void init() throws IOException{
		
		try{
			
			File dir = new File("data");
			if (dir.exists()) {
			} else {
				dir.mkdirs();
			}
			
			File file = new File("data/" + "newmergeevents"
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
					String[] account = str.split(",");
					
					pEventsDetails.add(account);
					
						
					}
				//fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			
			}
			
			
			pdataWnd.updateEventsDetails(pEventsDetails);
			
			constructPmergeData();
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			reader.close();
		}
		

		
	}
	
	
	public boolean saveTofile(String[] item){
		
		try{
			
			for(int i = 0; i < pEventsDetails.size(); i++){
				if(pEventsDetails.elementAt(i)[NEWMERGEINDEX.EVENTNAMNE.ordinal()].contains(item[NEWMERGEINDEX.EVENTNAMNE.ordinal()]) && 
						pEventsDetails.elementAt(i)[NEWMERGEINDEX.TIME.ordinal()].contains(item[NEWMERGEINDEX.TIME.ordinal()])){
					return false;
				}
			}
			
			
			File file = new File("data/" + "newmergeevents"
					+ ".data");
			
			BufferedWriter fwlocal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			
			
			String str = "";
			for(int i = 0; i < item.length; i++){
				if(i == 0){
					str = item[i];
				}else{
					str = str + "," + item[i];
				}
			}
			
			
			fwlocal.append(str);
			fwlocal.newLine();
			fwlocal.flush();
			
			fwlocal.close();
			
			pEventsDetails.add(item);
			
			updatepEventsDetails();
			
			constructPmergeData();
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void updatepEventsDetails(){
		
		try{
			


			
			pdataWnd.updateEventsDetails(pEventsDetails);
			
			//reader1.close();
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
		
		

	}
	
	
	public void constructPmergeData(){
		
		
		
		try{
			
			lockepSubeventsDetails.writeLock().lock();
			
			if(pSubEventsDetails.size() != 0){
				pSubEventsDetails.clear();
			}
			
			
			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
			
			SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式


			long currentTimeL = System.currentTimeMillis();
			
			String LocaltodayStr = dfDay.format(currentTimeL);
			
			
			
			
			String startTimeStr = LocaltodayStr + " " + "13:00";
			

			
			
			java.util.Date startTimeDate = dfMin.parse(startTimeStr);
			
			Calendar startTime = Calendar.getInstance();  
			startTime.setTime(startTimeDate);
			
			startTime.add(Calendar.DAY_OF_MONTH, -10);
			
			
			

			
			for(int i = 0; i < pEventsDetails.size(); i++){
				String timeStr = pEventsDetails.elementAt(i)[NEWMERGEINDEX.TIME.ordinal()];
				java.util.Date timeDate = dfMin.parse(timeStr);
				
				Calendar time = Calendar.getInstance();  
				time.setTime(timeDate);
				
				
				if(time.getTimeInMillis() >= startTime.getTimeInMillis() && time.getTimeInMillis() < startTime.getTimeInMillis() + 280*60*60*1000){
					pSubEventsDetails.add(pEventsDetails.elementAt(i));
				}
							
			}
			
			lockepSubeventsDetails.writeLock().unlock();
			
		}catch(Exception e){
			lockepSubeventsDetails.writeLock().unlock();
			e.printStackTrace();
		}
		
		
		
	}
	
	
	public Vector<String[]> getpSubevents(){
		try{
			
			lockepSubeventsDetails.readLock().lock();
			
			Vector<String[]> tmp = (Vector<String[]>) pSubEventsDetails.clone();
			
			
			
			lockepSubeventsDetails.readLock().unlock();
			return tmp;
			
			
		}catch(Exception e){
			lockepSubeventsDetails.readLock().unlock();
			
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	public String[] findLatestEvents(String eventName){
		
		
		try{
			String[] item = null;
			boolean find = false;
			
			
			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式

			
			long currentTimeL = System.currentTimeMillis();
			
			
			String MinStr = dfMin.format(currentTimeL);
			

			
			
			for(int i = 0; i < pEventsDetails.size(); i++){
				item = pEventsDetails.elementAt(i).clone();
				if(item[NEWMERGEINDEX.EVENTNAMNE.ordinal()].contains(eventName)){
					java.util.Date Mintime = dfMin.parse(item[NEWMERGEINDEX.TIME.ordinal()]);
					
					Calendar eventTime = Calendar.getInstance();  
					eventTime.setTime(Mintime);
					
					if(currentTimeL - eventTime.getTimeInMillis() < 3*60*60*1000){
						find = true;
						break;
					}
					
					
				}
			}
			
			if(true == find){
				return item;
			}
			
			return null;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;

	}
	
	
	
}
