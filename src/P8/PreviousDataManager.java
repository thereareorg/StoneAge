package P8;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class PreviousDataManager {
	
	BufferedWriter fw = null;  //写	
	BufferedReader reader = null; //读
	
	Vector<String[]> accountDetails = new Vector<String[]>();
	
	PreviousDataWindow pdataWnd = null; 
	
	PreviousDataManager(PreviousDataWindow wnd) {
		pdataWnd = wnd;
	}
	
	public void init(){
		
		try{
			
			File dir = new File("account");
			if (dir.exists()) {
			} else {
				dir.mkdirs();
			}
			
			File file = new File("account/" + "account"
					+ ".txt");
			
			
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
					
					accountDetails.add(account);
					
						
					}
				//fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			
			}
			
			
			//accWnd.setAccountsDetails(accountDetails);
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
}
