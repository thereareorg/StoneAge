package team.gl.nio.cmn;
import java.io.Serializable; 

import java.util.Vector;

public class Bag implements Serializable{

	String req;
    private Vector<String[]> datas;
    private String successTime = "";


    public Bag(String req){
        this.req = req;
    }

    public Vector<String[]> getDatas() {
        return datas;
    }
    
    public String getSuccessTime(){
    	return successTime;
    }
    
    public void setSuccessTime(String time){
    	successTime = time;
    }

    public void setDatas(Vector<String[]> datas) {
        this.datas = datas;
    }
    
    public String getReq() {
    	return req;
    }

    @Override
    public String toString() {
    	String strData = "";
    	for(int i = 0; i < datas.size(); i++) {
    		String []strs = datas.elementAt(i);
    		for(int j = 0; j < 8; j++) {
    			strData += strs[j] + ",";
    		}
    		strData += "\n";
    	}
    	
        return strData + successTime;
    }
}
