package P8;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ScoreMergeWindow extends JFrame{
	private static final long serialVersionUID = 508685938511234550L;
	

	
    private JLabel labelP8 = new JLabel("P8:");    
    private JLabel labelScore = new JLabel("比分网:");
    
    private JTextField textFieldP8league = new JTextField(15);
    private JTextField textFieldScoreleague = new JTextField(15);
    
    private JTextField textFieldP8Time = new JTextField(15);
    private JTextField textFieldScoreTime = new JTextField(15);
    
    private JTextField textFieldP8Event = new JTextField(15);
    private JTextField textFieldScoreEvent = new JTextField(15);
    
    private Button mergeButton = new Button("合并");
    private Button cancleButton = new Button("取消");
    

    
    String oldAdd = "";
    String oldAcc = "";
    String oldPwd = "";
    String oldScode = "";
    
    
    
	public ScoreMergeWindow()  
    {  
		setTitle("比分网合并");  
		
		//accMgr = acc;
		
        intiComponent();  
        
    }  
	

	

	public void setP8Txt(String[] event){
		
		try{
			
			if(event == null)
				return;
			
			textFieldP8league.setText(event[TYPEINDEX.LEAGUENAME.ordinal()]);
			textFieldP8Time.setText(event[TYPEINDEX.TIME.ordinal()]);
			textFieldP8Event.setText(event[TYPEINDEX.EVENTNAMNE.ordinal()]);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public void setScoreTxt(String[] event){
		try{
			
			if(event == null)
				return;
			textFieldScoreleague.setText(event[SCORENEWINDEX.LEAGUENAME.ordinal()]);
			textFieldScoreTime.setText(event[SCORENEWINDEX.TIME.ordinal()]);
			textFieldScoreEvent.setText(event[SCORENEWINDEX.EVENTNAMNE.ordinal()]);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
    
    /** 
     * 初始化窗体组件 
     */  
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel(new GridLayout(5, 2));

        container.add(panelNorth, BorderLayout.NORTH);  
        
        mergeButton.addActionListener(new ActionListener() {  
      	  
            @Override  
            public void actionPerformed(ActionEvent e) {  
            	
            	try{
            		
                	String p8EventName = textFieldP8Event.getText();            	
                	String[] P8Name = p8EventName.split("-vs-");
                	
                	String zhiboEventName = textFieldScoreEvent.getText();
                	String[] ScoreName = zhiboEventName.split(" vs ");
                	
                	boolean homeTeamRes = ScoreMergeManager.saveTofile(P8Name[0], ScoreName[0]);
                	boolean awayTeamRes = ScoreMergeManager.saveTofile(P8Name[1], ScoreName[1]);

                	if(homeTeamRes == true && awayTeamRes ==  true){
                		JOptionPane.showMessageDialog(null,"合并成功");
                	}else{
                		JOptionPane.showMessageDialog(null,"合并失败，可能已合并或合并出错，请检查");
                	}
                	
                	
                    dispose();  
            		
            	}catch(Exception ept){
            		ept.printStackTrace();
            		dispose();  
            	}
            	

                
            }  
        });  
        
        
        cancleButton.addActionListener(new ActionListener() {  
        	  
            @Override  
            public void actionPerformed(ActionEvent e) {  
            	dispose();  

            }  
        });  
        
        

        
        
        
        panelNorth.add(labelP8);
        panelNorth.add(labelScore);

        
        panelNorth.add(textFieldP8league);
        panelNorth.add(textFieldScoreleague);

        panelNorth.add(textFieldP8Time);
        panelNorth.add(textFieldScoreTime);
        
        panelNorth.add(textFieldP8Event);
        panelNorth.add(textFieldScoreEvent);
        
        panelNorth.add(mergeButton);
        
        panelNorth.add(cancleButton);

        setVisible(false);  
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  

        setBounds(100, 100, 630, 400); 

    }  
    
    
    
    
    
    
}

