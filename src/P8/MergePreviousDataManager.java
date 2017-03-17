package P8;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class MergePreviousDataManager {
	BufferedWriter fw = null;  //写	
	BufferedReader reader = null; //读
	
	Vector<String[]> pEventsDetails = new Vector<String[]>();
	
	MergePreviousDataWindow pdataWnd = null; 
	
	MergePreviousDataManager(MergePreviousDataWindow wnd) {
		pdataWnd = wnd;
	}
	
	public void init(){
		
		try{
			
			File dir = new File("data");
			if (dir.exists()) {
			} else {
				dir.mkdirs();
			}
			
			File file = new File("data/" + "mergeevents"
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
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	
	public boolean saveTofile(String[] item){
		
		try{
			
			for(int i = 0; i < pEventsDetails.size(); i++){
				if(pEventsDetails.elementAt(i)[ZHIBOINDEX.EVENTNAMNE.ordinal()].contains(item[ZHIBOINDEX.EVENTNAMNE.ordinal()]) && 
						pEventsDetails.elementAt(i)[ZHIBOINDEX.TIME.ordinal()].contains(item[ZHIBOINDEX.TIME.ordinal()])){
					return false;
				}
			}
			
			
			File file = new File("data/" + "mergeevents"
					+ ".data");
			
			BufferedWriter fwlocal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			
			
			fwlocal.append(item[0] + "," + item[1] +  "," + item[2] + "," + 
					item[3] + "," + item[4] + "," + item[5] + "," + item[6] + "," + 
					item[7]);
			fwlocal.newLine();
			fwlocal.flush();
			
			fwlocal.close();
			
			pEventsDetails.add(item);
			
			//updatepEventsDetails();
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void updatepEventsDetails(){
		
		try{
			
			File file = new File("data/" + "mergeevents"
					+ ".data");
			
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); // 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码,
			
			String str = null;
			
			String lastLine = null;
			
			pEventsDetails.clear();
			
			while ((str = reader1.readLine()) != null) {
				//System.out.println(str);
				String[] event = str.split(",");
				
				pEventsDetails.add(event);
				
				//System.out.println(account);
				
				}
			
/*			for(int i = 0; i < pEventsDetails.size(); i++){
				System.out.println(pEventsDetails.elementAt(i));
			}*/
			
			pdataWnd.updateEventsDetails(pEventsDetails);
			
			reader1.close();
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
		
		

	}
}
