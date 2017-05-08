package team.gl.nio.cmn;
import java.io.Serializable; 

import java.util.Vector;

public class Bag implements Serializable{

	String req;
    private Vector<String[]> datas;
    private Vector<String[]> mergeDatas;
    private Vector<String[]> mergepSubDatas;
    private Vector<String[]> p8pSubDatas;
    private Vector<String[]> zhibopSubDatas;
    private String successTime = "";
    
    private boolean P8GrabStat = false;
    private boolean MergeGrabStat = false;


    public Bag(String req){
        this.req = req;
    }

    public Vector<String[]> getDatas() {
        return datas;
    }
    
    public Vector<String[]> getMergeDatas() {
        return mergeDatas;
    }
    
    public Vector<String[]> getMergepSubDatas() {
        return mergepSubDatas;
    }

    
    public Vector<String[]> getP8pSubDatas() {
        return p8pSubDatas;
    }
    
    public Vector<String[]> getZhibopSubDatas() {
        return zhibopSubDatas;
    }
    
    public String getSuccessTime(){
    	return successTime;
    }
    
    public boolean getP8GrabStat(){
    	return P8GrabStat;
    }
    
    public boolean getMergeGrabStat(){
    	return MergeGrabStat;
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
    
    public void setMergepSubDatas(Vector<String[]> datas) {
        this.mergepSubDatas = datas;
    }
    
    public void setP8pSubDatas(Vector<String[]> datas) {
        this.p8pSubDatas = datas;
    }
    
    public void setZhibopSubDatas(Vector<String[]> datas) {
        this.zhibopSubDatas = datas;
    }
    
    public void setP8GrabStat(boolean val){
    	P8GrabStat = val;
    }
    
    public void setMergeGrabStat(boolean val){
    	MergeGrabStat = val;
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
    	
    	String mergepSubStrData = "";
    	for(int i = 0; i < mergepSubDatas.size(); i++) {
    		String []strs = mergepSubDatas.elementAt(i);
    		for(int j = 0; j < 8; j++) {
    			mergeStrData += strs[j] + ",";
    		}
    		mergeStrData += "\n";
    	}
    	
    	
        return strData + mergeStrData + mergepSubStrData + successTime;
    }
}
