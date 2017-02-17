package P8;

import java.util.Comparator;

class MyCompare implements Comparator //ʵ��Comparator�������Լ��ıȽϷ���
{
public int compare(Object o1, Object o2) {
	
	System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); 
	
Object[] e1=(Object[])o1;
Object[] e2=(Object[])o2;

if(Long.parseLong((String)e1[2]) > Long.parseLong((String)e2[2]))//����Ƚ��ǽ���,����-1�ĳ�1��������.
{
   return 1;
}
else
{
   return -1;
}
}
}