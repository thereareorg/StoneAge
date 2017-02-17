package P8;

import java.math.BigDecimal;
import java.util.Comparator;
public class Common {
	
	public static boolean isNum(String str){
        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
	}

}
