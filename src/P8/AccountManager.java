package P8;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.io.PrintStream;
import java.util.Date;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class AccountManager {
	
	BufferedWriter fw = null;  //写	
	BufferedReader reader = null; //读
	
	Vector<String[]> accountDetails = new Vector<String[]>();
	
	AccountsDetailsWindow accWnd = null; 
	
	AccountManager(AccountsDetailsWindow wnd) {
		accWnd = wnd;
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
			
			
			accWnd.setAccountsDetails(accountDetails);
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	public  Vector<String[]> getAccountDetails(){
		return accountDetails;
	}
	
	
	public boolean saveTofile(String[] accountDeatils){
		
		try{
			
			File file = new File("account/" + "account"
					+ ".txt");
			
			BufferedWriter fwlocal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			
			
			fwlocal.append(accountDeatils[0] + "," + accountDeatils[1] +  "," + accountDeatils[2] + "," + accountDeatils[3] + "," + accountDeatils[4]);
			fwlocal.newLine();
			fwlocal.flush();
			
			fwlocal.close();
			
			updateAccountDetails();
			
			return true;
			
		}catch(Exception e){			
			e.printStackTrace();
		}
		
		return false;

	}
	
	
	public boolean deleteAccount(String[] accDeatils){
		
		try{
			
			for(int i = 0; i < accountDetails.size(); i++){
				if(accDeatils[0].contains(accountDetails.elementAt(i)[0]) && accDeatils[1].contains(accountDetails.elementAt(i)[1]))
					accountDetails.remove(i);
			}
			
			File file = new File("account/" + "account"
					+ ".txt");
			
			BufferedWriter fwlocal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
			
/*			fwlocal.write("");
			
			fwlocal.flush();*/
			
			for(int i = 0; i < accountDetails.size(); i++){
				fwlocal.write(accountDetails.elementAt(i)[0] + "," + accountDetails.elementAt(i)[1] + "," + accountDetails.elementAt(i)[2]
						+ "," + accountDetails.elementAt(i)[3] + "," + accountDetails.elementAt(i)[4]);
				
				fwlocal.newLine();
				fwlocal.flush();
				
			}
			
			fwlocal.close();

			
			//System.out.println("delete");
			
			updateAccountDetails();
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;

	}
	
	
	public void updateAccountDetails(){
		
		try{
			
			File file = new File("account/" + "account"
					+ ".txt");
			
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); // 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码,
			
			String str = null;
			
			String lastLine = null;
			
			accountDetails.clear();
			
			while ((str = reader1.readLine()) != null) {
				//System.out.println(str);
				String[] account = str.split(",");
				
				accountDetails.add(account);
				
					
				}
			
			accWnd.setAccountsDetails(accountDetails);
			
			reader1.close();
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
		
		

	}
	
	
	
}
