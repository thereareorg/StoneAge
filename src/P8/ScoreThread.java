package P8;



public class ScoreThread extends Thread{
	
	
	ScoreThread(){
		
	}
	
	@Override
    public void run() {
		try{
			while(true){
				try{
					
					StoneAge.score.clearScoredetails();
					
					
					if(StoneAge.score.getScores() == true){
						
						StoneAge.bScoreLogin = true;
						
						StoneAge.score.updateEventsDetailsData();
						
						StoneAge.score.copyTofinalScoresDetails();
						
						if(StoneAge.showScore == true){
							StoneAge.score.showscoredetailswnd();
							StoneAge.showScore = false;
						}
						
						
						
						Thread.currentThread().sleep(10*1000);
					}else{
						Thread.currentThread().sleep(2*1000);
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
