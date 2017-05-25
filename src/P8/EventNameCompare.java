package P8;

import java.util.Comparator;

public class EventNameCompare implements Comparator //ʵ��Comparator�������Լ��ıȽϷ���
{
	public int compare(Object o1, Object o2) {
		
		
		
		try{
			Object[] e1=(Object[])o1;
			Object[] e2=(Object[])o2;
			
			String name1 = (String)e1[3];
			String name2 = (String)e2[3];
			
			boolean name1Isinplay = false;
			boolean name2Isinplay = false;
			
			if(name1.contains("【滚动盘】")){
				name1 = name1.replace("【滚动盘】", "");
				name1Isinplay = true;
			}
			
			if(name2.contains("【滚动盘】")){
				name2 = name2.replace("【滚动盘】", "");
				name2Isinplay = true;
			}
			
			int hash1 = name1.hashCode();
			int hash2 = name2.hashCode();
			
			
			if(hash1 > hash2)//����Ƚ��ǽ���,����-1�ĳ�1��������.
			{
			   return 1;
			}
			else if(hash1 == hash2)
			{
				if(name1Isinplay){
					return 1;
				}else if(name2Isinplay){
					return -1;
				}else{
					return 0;
				}

			}else{
				return -1;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
		

	}
}
