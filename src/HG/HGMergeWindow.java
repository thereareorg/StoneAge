package HG;

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

import P8.SCOREINDEX;
import P8.TYPEINDEX;

public class HGMergeWindow extends JFrame{
	private static final long serialVersionUID = 123485938511232389L;
	

	
    private JLabel labelP8 = new JLabel("P8:");    
    private JLabel labelHG = new JLabel("皇冠:");
    
    private JTextField textFieldP8league = new JTextField(15);
    private JTextField textFieldHGleague = new JTextField(15);
    
    private JTextField textFieldP8Time = new JTextField(15);
    private JTextField textFieldHGTime = new JTextField(15);
    
    private JTextField textFieldP8Event = new JTextField(15);
    private JTextField textFieldHGEvent = new JTextField(15);
    
    private Button mergeButton = new Button("合并");
    private Button cancleButton = new Button("取消");
    
 
    
    String oldAdd = "";
    String oldAcc = "";
    String oldPwd = "";
    String oldScode = "";
    
    
    
	public HGMergeWindow()  
    {  
		setTitle("皇冠合并");  
		
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
	
	public void setHGTxt(String[] event){
		try{
			
			if(event == null)
				return;
			textFieldHGleague.setText(event[SCOREINDEX.LEAGUENAME.ordinal()]);
			textFieldHGTime.setText(event[SCOREINDEX.TIME.ordinal()]);
			textFieldHGEvent.setText(event[SCOREINDEX.EVENTNAMNE.ordinal()]);
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
                	
                	String hgEventName = textFieldHGEvent.getText();
                	String[] HGName = hgEventName.split("-vs-");
                	
                	boolean homeTeamRes = HGMergeManager.saveTofile(P8Name[0], HGName[0]);
                	boolean awayTeamRes = HGMergeManager.saveTofile(P8Name[1], HGName[1]);

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
        panelNorth.add(labelHG);

        
        panelNorth.add(textFieldP8league);
        panelNorth.add(textFieldHGleague);

        panelNorth.add(textFieldP8Time);
        panelNorth.add(textFieldHGTime);
        
        panelNorth.add(textFieldP8Event);
        panelNorth.add(textFieldHGEvent);
        
        panelNorth.add(mergeButton);
        
        panelNorth.add(cancleButton);

        setVisible(false);  
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  

        setBounds(100, 100, 630, 400); 

    }  
    
    
    
    
    
    
}

