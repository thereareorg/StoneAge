package HGclient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import P8.TYPEINDEX;

public class GameDetails {
	public String datetime;
	//public String gid;
	public String league;
	public String teamh;
	public String teamc;
	public String eventid;
	
	public String currentpankou = "";
	public String currentratio = "";
	
	public String gameresult = "";
	
	public String filename = "";
	
	
	public String currentscore = "";	//不存进文件，仅用来判断是否符合上半场球赛条件
	
	
	public int pankouh = -1000;
	public int pankouc = -1000;
	public int ouh = -1000;
	public int ouc = -1000;
	
	
	
	public Vector<String[]> odds = new Vector<String[]>();
	
	
	public GameDetails(){
		
		
		
	}
	
	
	public Vector<String[]> getodds(){
		return odds;
	}
	
	
	public boolean readfromefile(){
		return true;
	}
	
	public void addodds(String[] oddsItem){
		odds.add(oddsItem);
	}
	
	
	public boolean savetofile(){
		
		try{
			
			SimpleDateFormat dfmin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			java.util.Date startTimeDate = dfmin.parse(datetime);
			
			Calendar startTime = Calendar.getInstance();  
			startTime.setTime(startTimeDate);
			
			String foldername = "";
			
			if(startTimeDate.getHours() > 12){
				foldername = datetime.split(" ")[0];
				//foldername = foldername + teamh;
			}else{
				startTime.add(Calendar.DAY_OF_MONTH, -1);
				foldername = dfmin.format(startTime.getTimeInMillis()).split(" ")[0];
				//foldername = foldername + teamh;
			}
			
			
			
			
			
			File filefolder = new File("hgdata/" + foldername + "/");
			
			if(!filefolder.exists()){
				filefolder.mkdir();
			}
			
			String filenametmp = eventid + teamh.replace(" ", "") + "vs" + teamc.replace(" ", "")
					+ ".data";
			
			if(filenametmp.contains("/")){
				filenametmp = filenametmp.replace("/", "");
			}
			
			if(filename.equals("")){
				filename = filenametmp;
			}
			
			File file = new File("hgdata/" + foldername + "/" +  filename);
			
			if(!file.exists()){
				System.out.println("hgdata/" + foldername + "/" +  filename);
				file.createNewFile();
			}
			
			BufferedWriter fwlocal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
			
			
			fwlocal.append(eventid + "," + datetime + ","  + league + "," + teamh + "," + teamc + "," + gameresult);
			fwlocal.newLine();
			fwlocal.flush();
			
			for(int i = 0; i < odds.size(); i++){
				
				String[] item = odds.elementAt(i);
				
				String str = "";
				for(int j = 0; j < item.length; j++){
					if(j == 0){
						str = item[j];
					}else{
						str = str + "," + item[j];
					}
				}
				
				fwlocal.append(str);
				fwlocal.newLine();
				fwlocal.flush();

			}
			
			fwlocal.close();
			
			
			
			return true;
			
			
		}catch(Exception e){
			e.printStackTrace();
			
			
			
			
			return false;
		}
		
		

	}
	
}
