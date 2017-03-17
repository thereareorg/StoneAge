package team.gl.nio.cmn;
import java.io.Serializable; 

import java.util.Vector;

public class Bag implements Serializable{

	String req;
    private Vector<String[]> datas;
    private Vector<String[]> mergeDatas;
    private String successTime = "";


    public Bag(String req){
        this.req = req;
    }

    public Vector<String[]> getDatas() {
        return datas;
    }
    
    public Vector<String[]> getMergeDatas() {
        return mergeDatas;
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
    
    public void setMergeDatas(Vector<String[]> datas) {
        this.mergeDatas = datas;
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
    	
    	String mergeStrData = "";
    	for(int i = 0; i < mergeDatas.size(); i++) {
    		String []strs = mergeDatas.elementAt(i);
    		for(int j = 0; j < 8; j++) {
    			mergeStrData += strs[j] + ",";
    		}
    		mergeStrData += "\n";
    	}
    	
    	
        return strData + mergeStrData + successTime;
    }
}
