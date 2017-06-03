package P8;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;  
 
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;  
  
import java.awt.Color;






















import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JMenuItem;
import javax.swing.JPanel;  
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;  
import javax.swing.JTable;  
import javax.swing.JTextField;  
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel; 
import javax.swing.table.AbstractTableModel; 


import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.util.Date;      

import javax.swing.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Stack;





class ColorTableCellRenderer extends JLabel implements TableCellRenderer

{

private static final long serialVersionUID = 1L;

//定义构造器

public ColorTableCellRenderer ()

{

//设置标签为不透明状态

this.setOpaque(true);

//设置标签的文本对齐方式为居中

this.setHorizontalAlignment(JLabel.CENTER);

}

//实现获取呈现控件的getTableCellRendererComponent方法

public Component getTableCellRendererComponent(JTable table,Object value,

           boolean isSelected,boolean hasFocus,int row,int column)

{           

//获取要呈现的颜色

Color c=(Color)value;

//根据参数value设置背景色

this.setBackground(c);



return this;

}

 }   



public class EventsDetailsWindow extends JFrame  
{  
  
   
	private static final long serialVersionUID = 508685938515369544L;
	
	private  Vector<String[]> originalDetailsData = new Vector<String[]>();
	
	private  Vector<String[]> detailsData = null;
	
	private Vector<Integer> hightlightRows = new Vector<Integer>();
	
	
    private JLabel labelHighlightNum = new JLabel("让球高亮金额:");
    private JTextField textFieldHighlightNum = new JTextField(15);  
    
    private JLabel labelp0oHighlightNum = new JLabel("大小球高亮金额:");
    private JTextField textFieldp0oHighlightNum = new JTextField(15);  
    
    private JLabel labelInterval = new JLabel("刷新时间:");
    
    String str1[] = {"30", "60","90","120","180"};
    
    private JComboBox jcb = new JComboBox(str1); 
    
    
    private JLabel labelHideNum = new JLabel("让球隐藏金额:");
    private JTextField textFieldHideNum = new JTextField(15); 
    
    private JLabel labelp0oHideNum = new JLabel("大小球隐藏金额:");
    private JTextField textFieldp0oHideNum = new JTextField(15); 
    
    private JLabel labelInplayHideNum = new JLabel("走地让球隐藏金额:");
    private JTextField textFieldInplayHideNum = new JTextField(15); 
    
    private JLabel labelp0oInplayHideNum = new JLabel("走地大小球隐藏金额:");
    private JTextField textFieldp0oInplayHideNum = new JTextField(15); 
    
    private JLabel labelInplayDirNum = new JLabel("走地让球方向金额:");
    private JTextField textFieldInplayDirNum = new JTextField(15); 
    
    private JLabel labelp0oInplayDirNum = new JLabel("走地大小球方向金额:");
    private JTextField textFieldp0oInplayDirNum = new JTextField(15); 
    
    private JCheckBox onlyShow5Big = new JCheckBox("只看五大联赛,欧冠");
    private JCheckBox onlyShowInplay = new JCheckBox("只看滚动盘");
    private JCheckBox onlyShowNotInplay = new JCheckBox("只看单式盘");
    
    private boolean bonlyShow5Big = false;
    private boolean bonlyShowInplay = false;
    private boolean bonlyShowNotInplay = false;
    
    private JLabel labelGrabStat= new JLabel("状态:");
    private JTextField textFieldGrabStat = new JTextField(15);  
    
    
    private JPopupMenu m_popupMenu;
    
    private JMenuItem chooseMenItem; 
    
    private JMenuItem mergeMenItem; 
    
    private int focusedRowIndex = -1;
    
    private int selectedOrMerge = 0;
	
    Double p0hhiglightBigNum = 1000000.0;
    
    Double p0hhideNum = 0.0;
    
    Double p0ohiglightBigNum = 1000000.0;
    
    Double p0ohideNum = 0.0;

    Double p0hInplayhideNum = 1.0;
    
    Double p0oInplayhideNum = 1.0;
    
    Double p0hInplayDirNum = 300000.0;
    
    Double p0oInplayDirNum = 300000.0;
   
    
/*    private JLabel labeltime = new JLabel("距封盘:");
    private JTextField textFieldtime = new JTextField(15);  
    
    private AtomicLong remainTime = new AtomicLong(0);*/
    
    
    MyTableModel tableMode = new MyTableModel();
    
    
    JTable table = null;

    
    
    
	

	public EventsDetailsWindow()  
    {  
		setTitle("PP注单");  
		
        intiComponent();  
        
        addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				StoneAge.showP8 = false;
				//setVisible(false);
			}
		});
        
    }  
	
	
	
	private void createPopupMenu() {  
        m_popupMenu = new JPopupMenu();  
          
        chooseMenItem = new JMenuItem();  
        chooseMenItem.setText("选择");  
        
        mergeMenItem = new JMenuItem();
        mergeMenItem.setText("合并");
        
        
        mergeMenItem.addActionListener(new java.awt.event.ActionListener() {  
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
                //该操作需要做的事  
            	
            	try{
            		
	            	if(focusedRowIndex != -1 && focusedRowIndex < detailsData.size()){
	            		

	            			
	            			MergeManager.p8SelectedRow = detailsData.elementAt(focusedRowIndex);
	            			System.out.println(Arrays.toString(MergeManager.p8SelectedRow));

	            			MergeManager.showMergeWnd(true);
	            			
	            			MergeManager.zhiboSelectedRow = null;
	            			MergeManager.p8SelectedRow = null;

	            	}

	    	        
            	}catch(Exception e){
            		
            	}
            	

            	

            	
            }  
        });  
        
        
        chooseMenItem.addActionListener(new java.awt.event.ActionListener() {  
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
                //该操作需要做的事  
            	
            	try{
            		
	            	if(focusedRowIndex != -1 && focusedRowIndex < detailsData.size()){

	            			MergeManager.p8SelectedRow = detailsData.elementAt(focusedRowIndex);
	            			System.out.println(Arrays.toString(MergeManager.p8SelectedRow));
	            		
	            	}

	    	        
            	}catch(Exception e){
            		
            	}

            }  
        });  
        m_popupMenu.add(chooseMenItem);  
        m_popupMenu.add(mergeMenItem);  
    }  
	
	
	
	//鼠标右键点击事件  
	   private void mouseRightButtonClick(java.awt.event.MouseEvent evt) {  
	       //判断是否为鼠标的BUTTON3按钮，BUTTON3为鼠标右键  
	       if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {  
	           //通过点击位置找到点击为表格中的行  
	           focusedRowIndex = table.rowAtPoint(evt.getPoint());  
	           if (focusedRowIndex == -1) {  
	               return;  
	           }  
	           //将表格所选项设为当前右键点击的行  
	           table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);  
	           
	           System.out.println(focusedRowIndex);
	           //弹出菜单  
	           
	           if(MergeManager.zhiboSelectedRow == null){
	        	   mergeMenItem.setEnabled(false);
	       
	           }else{
	        	   mergeMenItem.setEnabled(true);
	        	   
	           }
	           
	           
	           m_popupMenu.show(table, evt.getX(), evt.getY());  
	       }  
	  
	   }  
	
	
	
	
	private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {  
		  
	       mouseRightButtonClick(evt);  
	}  
	
	
	
	
	public void setStateText(String txt){
		textFieldGrabStat.setText(txt);
	}
	
	public void setStateColor(Color cr){
		textFieldGrabStat.setBackground(cr);
	}
	
/*	public void hightlightBigNumrows(){
		
		if(hightlightRows.size() != 0){
			hightlightRows.clear();
		}
		
		for(int i = 0; i< detailsData.size(); i++){
			String leagueName = detailsData.elementAt(i)[TYPEINDEX.LEAGUENAME.ordinal()];
			
			if(P8Http.isInShowLeagueName(leagueName) || true){
				double betAmt1 = Double.parseDouble(detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()]);
				double betAmt2 = Double.parseDouble(detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()]);
				double betAmt3 = Double.parseDouble(detailsData.elementAt(i)[TYPEINDEX.PERIOD1HOME.ordinal()]);
				double betAmt4 = Double.parseDouble(detailsData.elementAt(i)[TYPEINDEX.PERIOD1OVER.ordinal()]);
				
				if(Math.abs(betAmt1) > higlightBigNum || Math.abs(betAmt2) > higlightBigNum|| 
						Math.abs(betAmt3) > higlightBigNum || Math.abs(betAmt4) > higlightBigNum){
					//
					
					hightlightRows.add(i);
					
				}
				
				
			}
			
			setOneRowBackgroundColor(table, 0, new Color(255, 100, 100));
		}
	}*/

	
	
	
	public void updateEventsDetails(Vector<String[]> eventDetailsVec){
		
		try{
			
			
			if(originalDetailsData.size() != 0){
				originalDetailsData.clear();
			}

			
			for(int i = 0; i< eventDetailsVec.size(); i++){
				originalDetailsData.add(eventDetailsVec.elementAt(i).clone());
			}
			
			for(int i = 0; i < originalDetailsData.size(); i++){
				String[] tmp = originalDetailsData.elementAt(i);
				if(tmp[TYPEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
					originalDetailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()] = "g" + originalDetailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()];
					originalDetailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()] = "g" + originalDetailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()];
				}
			}
			
			updateShowItem();
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		
	}
	
	public  void addData(Object[] a){
		
/*		try{
			detailsData.push(a);
			
	    	Comparator ct = new CompareStr();
	    	
	    	Collections.sort(detailsData, ct);
			
			tableMode.updateTable();
		}catch(Exception e){
			e.printStackTrace();
		}*/
		

	}
	
	
	public void updateShowItem(){
		
		Vector<String[]> DetailsDatatmp = new Vector<String[]>();
		
		//只显示走地盘
		if(bonlyShowInplay == true){
			for(int i = 0; i < originalDetailsData.size(); i++){
				if(originalDetailsData.elementAt(i)[TYPEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
					DetailsDatatmp.add(originalDetailsData.elementAt(i));
				}
			}
		}
		
		//只显示单式盘
		if(bonlyShowNotInplay == true){
			for(int i = 0; i < originalDetailsData.size(); i++){
				if(!originalDetailsData.elementAt(i)[TYPEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
					DetailsDatatmp.add(originalDetailsData.elementAt(i));
				}
			}
		}
		
		Vector<String[]> DetailsDatatmp1 = new Vector<String[]>();
		
		
		if(DetailsDatatmp.size() == 0){
			DetailsDatatmp = (Vector<String[]>)originalDetailsData.clone();
		}
		
		
		//只看五大联赛
		if(bonlyShow5Big == true){
			for(int i = 0; i < DetailsDatatmp.size(); i++){
				if(P8Http.isInShowLeagueName(DetailsDatatmp.elementAt(i)[TYPEINDEX.LEAGUENAME.ordinal()])){
					DetailsDatatmp1.add(DetailsDatatmp.elementAt(i));
				}
			}
		}
		
		Vector<String[]> DetailsDatatmp2 = new Vector<String[]>();
		
		if(DetailsDatatmp1.size() == 0){

			DetailsDatatmp1 = (Vector<String[]>)DetailsDatatmp.clone();
			
		}
		
		//隐藏数额
		for(int i = 0; i< DetailsDatatmp1.size(); i++){
			String eventName = DetailsDatatmp1.elementAt(i)[TYPEINDEX.EVENTNAMNE.ordinal()];

			
			
			double betAmt1 = 0.0;
			double betAmt2 = 0.0;

			
			String bet1Str = DetailsDatatmp1.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()];
			String bet2Str = DetailsDatatmp1.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()];

			if(bet1Str.contains("=")){
				String[] tmp = bet1Str.split("=");
				betAmt1 = Double.parseDouble(tmp[1]);
			}else{
				if(bet1Str.contains("g")){
					bet1Str = bet1Str.replace("g", "");
				}
				betAmt1 = Double.parseDouble(bet1Str);
			}
			
			if(bet2Str.contains("=")){
				String[] tmp = bet2Str.split("=");
				betAmt2 = Double.parseDouble(tmp[1]);
			}else{
				if(bet2Str.contains("g")){
					bet2Str = bet2Str.replace("g", "");
				}
				betAmt2 = Double.parseDouble(bet2Str);
			}
			
			if(eventName.contains("滚动盘")){
				if(Math.abs(betAmt1) > p0hInplayhideNum || Math.abs(betAmt2) > p0oInplayhideNum){
					DetailsDatatmp2.add(DetailsDatatmp1.elementAt(i).clone());
				}
			}else{
				if(Math.abs(betAmt1) > p0hhideNum || Math.abs(betAmt2) > p0ohideNum){
					DetailsDatatmp2.add(DetailsDatatmp1.elementAt(i).clone());
				}
			}

			
			
		}
		
		detailsData = (Vector<String[]>)DetailsDatatmp2.clone();

		for(int i = 0; i < detailsData.size(); i++){
			String[] tmp = detailsData.elementAt(i).clone();
			String name = tmp[TYPEINDEX.EVENTNAMNE.ordinal()];
			
			if(name.contains("滚动盘")){
				
				//System.out.println();
				
				for(int j = 0; j < detailsData.size(); j++){
					if(j == i){
						continue;
					}
					
					String[] tmp1 = detailsData.elementAt(j).clone();
					String name1 = tmp1[TYPEINDEX.EVENTNAMNE.ordinal()];
					if(name.contains(name1)){
						double betp0h = 0.0;
						double betp0o = 0.0;
						
						String betp0hStr = tmp[TYPEINDEX.PERIOD0HOME.ordinal()];
						String betp0oStr = tmp[TYPEINDEX.PERIOD0OVER.ordinal()];
						
						if(betp0hStr.contains("=")){
							String[] tmpArray = betp0hStr.split("=");
							betp0h = Double.parseDouble(tmpArray[1]);
						}
						
						if(betp0oStr.contains("=")){
							String[] tmpArray = betp0oStr.split("=");
							betp0o = Double.parseDouble(tmpArray[1]);
						}
						
						
						double betp0h1 = 0.0;
						double betp0o1 = 0.0;
						
						String betp0hStr1 = tmp1[TYPEINDEX.PERIOD0HOME.ordinal()];
						String betp0oStr1 = tmp1[TYPEINDEX.PERIOD0OVER.ordinal()];
						
						if(betp0hStr1.contains("=")){
							String[] tmpArray = betp0hStr1.split("=");
							betp0h1 = Double.parseDouble(tmpArray[1]);
						}
						
						if(betp0oStr1.contains("=")){
							String[] tmpArray = betp0oStr1.split("=");
							betp0o1 = Double.parseDouble(tmpArray[1]);
						}
						
						
						
						if((betp0h > p0hInplayhideNum && betp0h1 >p0hhideNum) || (betp0h < 0.0 -  p0hInplayhideNum && betp0h1 < 0.0 - p0hhideNum)){
							detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()] = "b" + detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()];
							detailsData.elementAt(j)[TYPEINDEX.PERIOD0HOME.ordinal()] = "b" + detailsData.elementAt(j)[TYPEINDEX.PERIOD0HOME.ordinal()];
						}
						
						if((betp0o > p0oInplayhideNum && betp0o1 >p0ohideNum) || (betp0o < 0.0 - p0oInplayhideNum && betp0o1 < 0.0 - p0ohideNum)){
							detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()] = "b" + detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()];
							detailsData.elementAt(j)[TYPEINDEX.PERIOD0OVER.ordinal()] = "b" + detailsData.elementAt(j)[TYPEINDEX.PERIOD0OVER.ordinal()];
						}
						
						break;
						
					}
					
				}
				
				
				//deal direction
				String p0hFlagStr = tmp[TYPEINDEX.PERIOD1HOME.ordinal()];
				if(!p0hFlagStr.equals("0")){
					String[] p0hFlags = p0hFlagStr.split("\\|");
					int flag = (int) (p0hInplayDirNum/P8Http.flagNumber);
					
					if(flag < 100){
						for(int k = 0; k < p0hFlags.length; k++){
							if(flag <= Math.abs(Integer.parseInt(p0hFlags[k]))){
								if(Integer.parseInt(p0hFlags[k]) < 0){
									detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()] = "↓" + detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()];
								}else{
									detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()] = "↑" + detailsData.elementAt(i)[TYPEINDEX.PERIOD0HOME.ordinal()];
								}
								
								break;
							}
						}
					}
					

				}
				
				
				String p0oFlagStr = tmp[TYPEINDEX.PERIOD1OVER.ordinal()];
				if(!p0oFlagStr.equals("0")){
					String[] p0oFlags = p0oFlagStr.split("\\|");
					int flag = (int) (p0oInplayDirNum/P8Http.flagNumber);
					
					if(flag < 100){
						for(int k = 0; k < p0oFlags.length; k++){
							if(flag <= Math.abs(Integer.parseInt(p0oFlags[k]))){
								if(Integer.parseInt(p0oFlags[k]) < 0){
									detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()] = "↓" + detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()];
								}else{
									detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()] = "↑" + detailsData.elementAt(i)[TYPEINDEX.PERIOD0OVER.ordinal()];
								}
								break;
							}
						}
					}
					

				}
				
				
				
				
			}
		}
		
		tableMode.updateTable();
		
	}

	
	

	
	
	
  
    /** 
     * 初始化窗体组件 
     */  
    private void intiComponent()  
    {  

		final Container container = getContentPane();
		
		container.setLayout(new BorderLayout());
		
		JPanel panelNorth = new JPanel(new GridLayout(6, 4));

        container.add(panelNorth, BorderLayout.NORTH);  
        
        jcb.setSelectedIndex(1);
        
        jcb.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
                String content = jcb.getSelectedItem().toString();
                
                StoneAge.setSleepTime(Integer.parseInt(content));
			}
        });
        
        
        textFieldHighlightNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldHighlightNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0hhiglightBigNum = Double.parseDouble(value);
                    	updateShowItem();
                    }
                    
                }  
                // System.out.println("Text " + value);  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });
        
        
        textFieldHideNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldHideNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0hhideNum = Double.parseDouble(value);
                    	updateShowItem();
                    	
                    	//tableMode.updateTable();
                    }
                    
                }  
                // System.out.println("Text " + value);  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });
        
        textFieldp0oHighlightNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldp0oHighlightNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0ohiglightBigNum = Double.parseDouble(value);
                    	updateShowItem();
                    }
                    
                }  
                // System.out.println("Text " + value);  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });
        
        
        textFieldp0oHideNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldp0oHideNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0ohideNum = Double.parseDouble(value);
                    	updateShowItem();
                    	
                    	//tableMode.updateTable();
                    }
                    
                }  
                // System.out.println("Text " + value);  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });
        
        
        textFieldInplayHideNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldInplayHideNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0hInplayhideNum = Double.parseDouble(value);
                    	updateShowItem();
                    	
                    	//tableMode.updateTable();
                    }
                    
                }  
                // System.out.println("Text " + value);  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });
        
        
        textFieldp0oInplayHideNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldp0oInplayHideNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0oInplayhideNum = Double.parseDouble(value);
                    	updateShowItem();
                    	
                    	//tableMode.updateTable();
                    }
                    
                }  
                // System.out.println("Text " + value);  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });    
        
        
        textFieldInplayDirNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldInplayDirNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0hInplayDirNum = Double.parseDouble(value);
                    	updateShowItem();
                    	
                    	//tableMode.updateTable();
                    }
                    
                }  
                // System.out.println("Text " + value);  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });
        
        textFieldInplayDirNum.setText(String.format("%.0f", p0hInplayDirNum));
        
        
        textFieldp0oInplayDirNum.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {  
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                    String value = textFieldp0oInplayDirNum.getText();  
                    
                    if(!Common.isNum(value)){
                    	return;
                    }else{
                    	p0oInplayDirNum = Double.parseDouble(value);
                    	updateShowItem();
                    	
                    	//tableMode.updateTable();
                    }
                    
                }  
                // System.out.println("Text " + value);  
            }  
            public void keyReleased(KeyEvent e) {  
            }  
            public void keyTyped(KeyEvent e) {  
            }  

        });        
        
        textFieldp0oInplayDirNum.setText(String.format("%.0f", p0oInplayDirNum));
        
        
        textFieldGrabStat.setEditable(false);
        
        onlyShow5Big.setSelected(false);
        
        onlyShow5Big.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					bonlyShow5Big = false;
				}else{
					bonlyShow5Big = true;
				}
				
				updateShowItem();
			}
        });
        
        onlyShowInplay.setSelected(false);
        
        onlyShowInplay.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					bonlyShowInplay = false;

				}else{
					bonlyShowInplay = true;
					bonlyShowNotInplay = false;
					onlyShowNotInplay.setSelected(false);
				}
				
				updateShowItem();
			}
        });
        
        onlyShowNotInplay.setSelected(false);
        
        onlyShowNotInplay.addItemListener(new ItemListener() {


			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
               // int index = jcb.getSelectedIndex();
				if(e.getStateChange() == ItemEvent.DESELECTED){
					bonlyShowNotInplay = false;
				}else{
					bonlyShowNotInplay = true;
					bonlyShowInplay = false;
					onlyShowInplay.setSelected(false);
				}
				
				updateShowItem();
			}
        });
        
        createPopupMenu();
        
        panelNorth.add(labelInterval);
        panelNorth.add(jcb);

        panelNorth.add(labelp0oHighlightNum);
        panelNorth.add(textFieldp0oHighlightNum);
        
        panelNorth.add(labelHighlightNum);
        panelNorth.add(textFieldHighlightNum);
        
        panelNorth.add(labelp0oHideNum);
        panelNorth.add(textFieldp0oHideNum);

        
        panelNorth.add(labelHideNum);
        panelNorth.add(textFieldHideNum);
        
        
        panelNorth.add(labelp0oInplayHideNum);
        panelNorth.add(textFieldp0oInplayHideNum);
        
        panelNorth.add(labelInplayHideNum);
        panelNorth.add(textFieldInplayHideNum);
        
        panelNorth.add(labelp0oInplayDirNum);
        panelNorth.add(textFieldp0oInplayDirNum);
        
        panelNorth.add(labelInplayDirNum);
        panelNorth.add(textFieldInplayDirNum);

        

        panelNorth.add(onlyShow5Big);
        panelNorth.add(onlyShowInplay);
        panelNorth.add(onlyShowNotInplay);

        
        
        panelNorth.add(labelGrabStat);
        panelNorth.add(textFieldGrabStat);
        
        
        
        
/*        panelNorth.add(labeltime);
        panelNorth.add(textFieldtime);
        textFieldjinrichazhi.setEditable(false);*/


	    
		
	    table = new JTable(tableMode);

        JScrollPane scroll = new JScrollPane(table); 
        
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {  
            public void mouseClicked(java.awt.event.MouseEvent evt) {  
                jTable1MouseClicked(evt);  
            }  
        });  
        
        
	    table.getColumnModel().getColumn(2).setPreferredWidth(240);
	    
	    table.setRowHeight(30);
	    
	    table.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));
	    
	    
	    
	    //table.setColumnModel(columnModel);
	    
	    //tableMode.
	    
	    
	    
	    //设置列单元格渲染模式  开始
        TableColumn p0hColumn = table.getColumn("全场让球");   
        TableColumn p0oColumn = table.getColumn("全场大小");   


        //绘制月薪列的字体颜色   

        DefaultTableCellRenderer p0hRender = new DefaultTableCellRenderer() {   

            public void setValue(Object value) { //重写setValue方法，从而可以动态设置列单元字体颜色   

               
            	String str = value.toString();
            	
				Double betAmt = 0.0;
				
				String showStr = str.replace("g", "");
				showStr = showStr.replace("b", "");
				
				if(str.contains("=")){
					String[] tmp = str.split("=");
					betAmt = Double.parseDouble(tmp[1]);
				}else{
	
					betAmt = Double.parseDouble(showStr);
				}
				
				
				if(Math.abs(betAmt) > p0hhiglightBigNum){
					setForeground(Color.red);
					
				}else{
					setForeground(Color.black);
					
				}
				

				
				setText((value == null) ? "" : showStr);

				Double hideNum = 0.0;
				
				if(str.contains("g")){
					hideNum = p0hInplayhideNum;
				}else{
					hideNum = p0hhideNum;
				}
				
				if(Math.abs(betAmt) < hideNum){
					setForeground(Color.black);
					setText("0");
				}
				
				if(str.contains("b")){
					setBackground(new Color(179,232,255));
				}else{
					setBackground(Color.white);
				}
					


            }   

        };   
        
        DefaultTableCellRenderer p0oRender = new DefaultTableCellRenderer() {   

            public void setValue(Object value) { //重写setValue方法，从而可以动态设置列单元字体颜色   

               
            	String str = value.toString();
            	
				Double betAmt = 0.0;
				
				String showStr = str.replace("g", "");
				showStr = showStr.replace("b", "");
				
				if(str.contains("=")){
					String[] tmp = str.split("=");
					betAmt = Double.parseDouble(tmp[1]);
				}else{
					betAmt = Double.parseDouble(str.replace("g", ""));
				}
				
				
				if(Math.abs(betAmt) > p0ohiglightBigNum){
					setForeground(Color.red);
					
				}else{
					setForeground(Color.black);
					
				}
				
				setText((value == null) ? "" : showStr);
				
				Double hideNum = 0.0;
				
				if(str.contains("g")){
					hideNum = p0oInplayhideNum;
				}else{
					hideNum = p0ohideNum;
				}

				if(Math.abs(betAmt) < hideNum){
					setForeground(Color.black);
					setText("0");
				}

				if(str.contains("b")){
					setBackground(new Color(179,232,255));
				}else{
					setBackground(Color.white);
				}

            }   

        };   

        p0hColumn.setCellRenderer(p0hRender);   
        p0oColumn.setCellRenderer(p0oRender);   

      //设置列单元格渲染模式  结束

	    
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
        tcr.setHorizontalAlignment(JLabel.CENTER);
       // tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
        table.setDefaultRenderer(Object.class, tcr);
        
        
        
        container.add(scroll, BorderLayout.CENTER);  

        setVisible(false);  
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  
        
        

        
        setBounds(100, 100, 1600, 800); 

    }
    
    public boolean isInhighlightrows(int row){
    	
    	for(int i = 0; i < hightlightRows.size(); i ++){
    		if(hightlightRows.elementAt(i) == row){
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    
    
    public void setOneRowBackgroundColor(JTable table, int rowIndex1,  
            Color color1) {
    	
    	final int rowIndex = rowIndex1;
    	
    	final Color color = color1;
    	
        try {  
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {  
  
                public Component getTableCellRendererComponent(JTable table,  
                        Object value, boolean isSelected, boolean hasFocus,  
                        int row, int column) {  
                    if (isInhighlightrows(row)) {  
                        setBackground(color);  
                        setForeground(Color.BLACK);  
                    }else{  
                        setBackground(Color.WHITE);  
                        setForeground(Color.BLACK);  
                    }  
  
                    return super.getTableCellRendererComponent(table, value,  
                            isSelected, hasFocus, row, column);  
                }  
            };  
            int columnCount = table.getColumnCount();  
            for (int i = 0; i < columnCount; i++) {  
                table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);  
            }  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
    }  
    
    

    

    
    
    
  
    public class MyTableModel extends AbstractTableModel  
    {  
        /* 
         * 这里和刚才一样，定义列名和每个数据的值 
         */  
        String[] columnNames =  
        { "联赛", "时间", "球队", "全场让球", "全场大小"};  
        

        
        //Object[][] data = new Object[2][5];  
        
        

        
  
        /** 
         * 构造方法，初始化二维数组data对应的数据 
         */  
        public MyTableModel()  
        {  

        }  
  
        // 以下为继承自AbstractTableModle的方法，可以自定义  
        /** 
         * 得到列名 
         */  
        @Override  
        public String getColumnName(int column)  
        {  
            return columnNames[column];  
        }  
          
        /** 
         * 重写方法，得到表格列数 
         */  
        @Override  
        public int getColumnCount()  
        {  
            return columnNames.length;  
        }  
  
        /** 
         * 得到表格行数 
         */  
        @Override  
        public int getRowCount()  
        {         
	        if(null == detailsData){
	    		return 0;
	    	}
            return detailsData.size();  
        }  
  
        /** 
         * 得到数据所对应对象 
         */  
        @Override  
        public Object getValueAt(int rowIndex, int columnIndex)  
        {  
            //return data[rowIndex][columnIndex];
        	return detailsData.elementAt(rowIndex)[columnIndex+1];
        }  
  
        /** 
         * 得到指定列的数据类型 
         */  
        @Override  
        public Class<?> getColumnClass(int columnIndex)  
        {  
            return detailsData.elementAt(0)[columnIndex].getClass();
        }  
  
        /** 
         * 指定设置数据单元是否可编辑.这里设置"姓名","学号"不可编辑 
         */  
        @Override  
        public boolean isCellEditable(int rowIndex, int columnIndex)  
        {  
            return false;
        }  
          
        /** 
         * 如果数据单元为可编辑，则将编辑后的值替换原来的值 
         */  
        @Override  
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)  
        {  
            detailsData.elementAt(rowIndex)[columnIndex] = (String)aValue;  
            /*通知监听器数据单元数据已经改变*/  
            fireTableCellUpdated(rowIndex, columnIndex);  
        }  
        
        public void updateTable(){
        	fireTableDataChanged();
        }
        
  
    }  
    
    
}