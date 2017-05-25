package P8;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;

public class TimeCompare implements Comparator //ʵ��Comparator�������Լ��ıȽϷ���
{

	public int compare(Object o1, Object o2) {
		
		try{
			
			Object[] e1=(Object[])o1;
			Object[] e2=(Object[])o2;
			
			
			String time1 = (String)e1[2];
			String time2 = (String)e2[2];
			
			SimpleDateFormat dfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
			
			SimpleDateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			
			
			long currentTimeL = System.currentTimeMillis();
			
			String LocaltodayStr = dfDay.format(currentTimeL);
			

			
			if(!time1.contains("-")){
				
				time1 = LocaltodayStr + " " + time1;
			}
			
			if(!time2.contains("-")){
				
				time2 = LocaltodayStr + " " + time2;
			}
			
			java.util.Date Mintime1 = dfMin.parse(time1);
			java.util.Date Mintime2 = dfMin.parse(time2);
			
			Calendar calTime1 = Calendar.getInstance();  
			calTime1.setTime(Mintime1);
			
			Calendar calTime2 = Calendar.getInstance();  
			calTime2.setTime(Mintime2);
			
			
			if(calTime1.getTimeInMillis() > calTime2.getTimeInMillis())//����Ƚ��ǽ���,����-1�ĳ�1��������.
			{
			   return 11;
			}
			else if(calTime1.getTimeInMillis() == calTime2.getTimeInMillis())
			{
			   return 0;
			}else{
				return -1;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
		

	}
}
