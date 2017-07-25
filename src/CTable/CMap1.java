package CTable;
import javax.swing.*;
import javax.swing.table.*;

import P8.MERGEINDEX;
public class CMap1 implements CMap {
 public int span(int row, int column) {
	 if(column == MERGEINDEX.EVENTID.ordinal() || column == MERGEINDEX.LEAGUENAME.ordinal() || 
			 column == MERGEINDEX.TIME.ordinal()  || column == MERGEINDEX.RQPK.ordinal()  || 
			 column == MERGEINDEX.ZHIBOHRES.ordinal() || column == MERGEINDEX.PERIOD0HOME.ordinal() ||
			 column == MERGEINDEX.DXQPK.ordinal() || column == MERGEINDEX.ZHIBOORES.ordinal() ||
			 column == MERGEINDEX.PERIOD0OVER.ordinal()){
		 return 2;
	 }
   
  return 1;
 }
 public int visibleCell(int row, int column) {
	 
	 if(column == MERGEINDEX.EVENTID.ordinal() || column == MERGEINDEX.LEAGUENAME.ordinal() || 
			 column == MERGEINDEX.TIME.ordinal()  || column == MERGEINDEX.RQPK.ordinal()  || 
			 column == MERGEINDEX.ZHIBOHRES.ordinal() || column == MERGEINDEX.PERIOD0HOME.ordinal() ||
			 column == MERGEINDEX.DXQPK.ordinal() || column == MERGEINDEX.ZHIBOORES.ordinal() ||
			 column == MERGEINDEX.PERIOD0OVER.ordinal()){		
		 int res = 0;
		 res = (int)(row/2);
		 res = res*2;
		 return res;
	 }
	 
	 return row;
	 
/*  if( ( ( row >= 0 ) && ( row < 2  ) ) && ( column == 0 || column == 1 || column == 2 ) )
   return 0;
  if( ( ( row >= 2 ) && ( row < 4  ) ) && ( column == 0 || column == 1 || column == 2) )
   return 2;
  if( ( ( row >= 4 ) && ( row < 6  ) ) && ( column == 0 || column == 1 || column == 2) )
   return 4;
  if( ( ( row >= 6 ) && ( row < 8  ) ) && ( column == 0 || column == 1 || column == 2) )
   return 6;
  if( ( ( row >= 8 ) && ( row < 10  ) ) && ( column == 0 || column == 1 || column == 2) )
   return 8;
  if( ( ( row >= 10 ) && ( row < 12  ) ) && ( column == 0 || column == 1 || column == 2) )
   return 10;
  if( ( ( row >= 12 ) && ( row < 14  ) ) && ( column == 0 || column == 1 || column == 2) )
   return 12;
  if( ( ( row >= 14 ) && ( row < 16  ) ) && ( column == 0 || column == 1 || column == 2) )
   return 14;
 
  return row;*/
 }
}