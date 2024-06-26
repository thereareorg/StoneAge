package team.gl.nio.cmn;
import java.io.Serializable; 

import java.util.Vector;

public class P8Bag implements Serializable{

	String req;
    private Vector<String[]> datas;
    private String isnStr;
    private Vector<String[]> mergeDatas;
    private Vector<String[]> mergepSubDatas;
    private Vector<String[]> newMergeDatas;
    private Vector<String[]> newMergepSubDatas;
    private Vector<String[]> p8pSubDatas;
    private Vector<String[]> zhibopSubDatas;
    private Vector<String[]> matchTeams;
    private String successTime = "";
    private String isnSuccessTime = "";
    
    private Vector<String[]> hgdatas;
    private Vector<String[]> hgpSubDatas;
    private String hgsuccessTime = "";
    
    private boolean P8GrabStat = false;
    private boolean ISNGrabStat = false;
    private boolean MergeGrabStat = false;
    
    private boolean hgGrabStat = false;


    public P8Bag(String req){
        this.req = req;
    }

    public Vector<String[]> getDatas() {
        return datas;
    }
    
    public String getISNDatas() {
    	return isnStr;
    }
    
    public void setISNDatas(String str) {
    	isnStr = str;
    }
    
    public Vector<String[]> getMergeDatas() {
        return mergeDatas;
    }
    
    public Vector<String[]> getNewMergeDatas() {
        return newMergeDatas;
    }
    
    public Vector<String[]> getMergepSubDatas() {
        return mergepSubDatas;
    }

    
    public Vector<String[]> getNewMergepSubDatas() {
        return newMergepSubDatas;
    }
    
    public Vector<String[]> getP8pSubDatas() {
        return p8pSubDatas;
    }
    
    public Vector<String[]> gethgpSubDatas() {
        return hgpSubDatas;
    }
    
    public Vector<String[]> getZhibopSubDatas() {
        return zhibopSubDatas;
    }
    
    public String getSuccessTime(){
    	return successTime;
    }
    
    public String getISNSuccessTime(){
    	return isnSuccessTime;
    }
    
    public boolean getP8GrabStat(){
    	return P8GrabStat;
    }
    
    public boolean getISNGrabStat(){
    	return ISNGrabStat;
    }
    
    public boolean getMergeGrabStat(){
    	return MergeGrabStat;
    }
    
    
    public Vector<String[]> gethgDatas() {
        return hgdatas;
    } 
    
    public String gethgSuccessTime(){
    	return hgsuccessTime;
    } 
    
    
    public boolean gethgGrabStat(){
    	return hgGrabStat;
    }
    
    
    public void sethgDatas(Vector<String[]> datas) {
        this.hgdatas = datas;
    }
    
    public void sethgSuccessTime(String time){
    	hgsuccessTime = time;
    }
    
    
    public void setSuccessTime(String time){
    	successTime = time;
    }
    
    public void setISNSuccessTime(String time){
    	isnSuccessTime = time;
    }

    public void setDatas(Vector<String[]> datas) {
        this.datas = datas;
    }
    
    public void setMergeDatas(Vector<String[]> datas) {
        this.mergeDatas = datas;
    }
    
    public void setNewMergeDatas(Vector<String[]> datas) {
        this.newMergeDatas = datas;
    }
    
    public void setMergepSubDatas(Vector<String[]> datas) {
        this.mergepSubDatas = datas;
    }
    
    public void setNewMergepSubDatas(Vector<String[]> datas) {
        this.newMergepSubDatas = datas;
    }
    
    public void setP8pSubDatas(Vector<String[]> datas) {
        this.p8pSubDatas = datas;
    }
    
    public void sethgpSubDatas(Vector<String[]> datas) {
        this.hgpSubDatas = datas;
    }
    
    public void setZhibopSubDatas(Vector<String[]> datas) {
        this.zhibopSubDatas = datas;
    }
    
    public void setP8GrabStat(boolean val){
    	P8GrabStat = val;
    }
    
    public void setISNGrabStat(boolean val){
    	ISNGrabStat = val;
    }
    
    public void sethgGrabStat(boolean val){
    	hgGrabStat = val;
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

	public Vector<String[]> getMatchTeams() {
		return matchTeams;
	}

	public void setMatchTeams(Vector<String[]> matchTeams) {
		this.matchTeams = matchTeams;
	}
}
