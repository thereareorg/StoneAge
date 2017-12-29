package P8;

import java.awt.Color;
import java.text.SimpleDateFormat;

public class ScoreThread extends Thread{
	
	
	ScoreThread(){
		
	}
	
	@Override
    public void run() {
		try{
			
			SimpleDateFormat dfSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			while(true){
				try{
					
					if(StoneAge.score.getScorePankou() == true){
						
						StoneAge.score.setnewwndstatetxt("更新于:"+ dfSec.format(System.currentTimeMillis()));
						StoneAge.score.setnewwndstatecor(Color.green);
						
						StoneAge.bScoreLogin = true;
						
						StoneAge.score.updateEventsDetailsData();
						
						StoneAge.score.copyTofinalScoresDetails();
						
						

						Thread.sleep(2*1000);
					}else{
						
						StoneAge.score.setnewwndstatecor(Color.red);
						
						Thread.sleep(2*1000);
					}
					
					
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
