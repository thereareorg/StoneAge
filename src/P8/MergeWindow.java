package P8;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MergeWindow extends JFrame{
	private static final long serialVersionUID = 508685938515369550L;
	

	
    private JLabel labelP8 = new JLabel("P8:");    
    private JLabel labelZhibo = new JLabel("智博:");
    
    private JTextField textFieldP8league = new JTextField(15);
    private JTextField textFieldZhiboleague = new JTextField(15);
    
    private JTextField textFieldP8Time = new JTextField(15);
    private JTextField textFieldZhiboTime = new JTextField(15);
    
    private JTextField textFieldP8Event = new JTextField(15);
    private JTextField textFieldZhiboEvent = new JTextField(15);
    
    private Button mergeButton = new Button("合并");
    private Button cancleButton = new Button("取消");
    

    
    String oldAdd = "";
    String oldAcc = "";
    String oldPwd = "";
    String oldScode = "";
    
    
    
	public MergeWindow()  
    {  
		setTitle("合并球赛");  
		
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
	
	public void setZhiboTxt(String[] event){
		try{
			
			if(event == null)
				return;
			textFieldZhiboleague.setText(event[ZHIBOINDEX.LEAGUENAME.ordinal()]);
			textFieldZhiboTime.setText(event[ZHIBOINDEX.TIME.ordinal()]);
			textFieldZhiboEvent.setText(event[ZHIBOINDEX.EVENTNAMNE.ordinal()]);
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
                	
                	String zhiboEventName = textFieldZhiboEvent.getText();
                	String[] ZhiboName = zhiboEventName.split(" vs ");
                	
                	boolean homeTeamRes = MergeManager.saveTofile(P8Name[0], ZhiboName[0]);
                	boolean awayTeamRes = MergeManager.saveTofile(P8Name[1], ZhiboName[1]);

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
        panelNorth.add(labelZhibo);

        
        panelNorth.add(textFieldP8league);
        panelNorth.add(textFieldZhiboleague);

        panelNorth.add(textFieldP8Time);
        panelNorth.add(textFieldZhiboTime);
        
        panelNorth.add(textFieldP8Event);
        panelNorth.add(textFieldZhiboEvent);
        
        panelNorth.add(mergeButton);
        
        panelNorth.add(cancleButton);

        setVisible(false);  
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  

        setBounds(100, 100, 630, 400); 

    }  
    
    
    
    
    
    
}

