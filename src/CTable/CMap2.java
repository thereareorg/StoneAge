package CTable;
import javax.swing.*;
import javax.swing.table.*;

import MergeNew.NEWMERGEINDEX;
public class CMap2 implements CMap {
 public int span(int row, int column) {
	 if(column == NEWMERGEINDEX.EVENTID.ordinal() || column == NEWMERGEINDEX.LEAGUENAME.ordinal() || 
			 column == NEWMERGEINDEX.TIME.ordinal()  || column == NEWMERGEINDEX.RQPK.ordinal()  || 
			 column == NEWMERGEINDEX.ZHIBOHRES.ordinal() ||column == NEWMERGEINDEX.P8HRES.ordinal()||
			column == NEWMERGEINDEX.P8ORES.ordinal() ||column == NEWMERGEINDEX.INP8HRES.ordinal()||
			column == NEWMERGEINDEX.INP8ORES.ordinal() ||column == NEWMERGEINDEX.HGHRES.ordinal()||
			column == NEWMERGEINDEX.HGORES.ordinal() ||column == NEWMERGEINDEX.GQRQPK.ordinal()||
			column == NEWMERGEINDEX.GQDXQPK.ordinal() ||
			 column == NEWMERGEINDEX.DXQPK.ordinal() || column == NEWMERGEINDEX.ZHIBOORES.ordinal()){
		 return 2;
	 }
   
  return 1;
 }
 public int visibleCell(int row, int column) {
	 
	 if(column == NEWMERGEINDEX.EVENTID.ordinal() || column == NEWMERGEINDEX.LEAGUENAME.ordinal() || 
			 column == NEWMERGEINDEX.TIME.ordinal()  || column == NEWMERGEINDEX.RQPK.ordinal()  || 
			 column == NEWMERGEINDEX.ZHIBOHRES.ordinal() ||column == NEWMERGEINDEX.P8HRES.ordinal()||
			column == NEWMERGEINDEX.P8ORES.ordinal() ||column == NEWMERGEINDEX.INP8HRES.ordinal()||
			column == NEWMERGEINDEX.INP8ORES.ordinal() ||column == NEWMERGEINDEX.HGHRES.ordinal()||
			column == NEWMERGEINDEX.HGORES.ordinal() ||column == NEWMERGEINDEX.GQRQPK.ordinal()||
			column == NEWMERGEINDEX.GQDXQPK.ordinal() ||
			 column == NEWMERGEINDEX.DXQPK.ordinal() || column == NEWMERGEINDEX.ZHIBOORES.ordinal()){		
		 int res = 0;
		 res = (int)(row/2);
		 res = res*2;
		 return res;
	 }
	 
	 return row;
	 

 }
}