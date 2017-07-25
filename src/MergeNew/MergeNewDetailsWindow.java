package MergeNew;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

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
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import CTable.CMap;
import CTable.CMap2;
import CTable.CTable;
import P8.Common;
import P8.MergeManager;
import P8.P8Http;
import P8.MergeDetailsWindow.MyTableModel;

public class MergeNewDetailsWindow extends JFrame{
	  
	  
	   
		private static final long serialVersionUID = 5086859385153695441L;
		
		private  Vector<String[]> originalDetailsData = new Vector<String[]>();
		
		private  Vector<String[]> detailsData = null;
		
		private Vector<Integer> hightlightRows = new Vector<Integer>();
		

	    
	    private JLabel labelInterval = new JLabel("刷新时间:");
	    
	    String str1[] = {"20"};
	    
	    private JComboBox jcb = new JComboBox(str1); 
	    
	    

	    
	    private JCheckBox onlyShow5Big = new JCheckBox("只看五大联赛,欧冠");

	    
	    

	    private JCheckBox hideDetailsData = new JCheckBox("隐藏数据");
	    private JCheckBox ShowInplay = new JCheckBox("滚动盘");
	    private JCheckBox ShowNotInplay = new JCheckBox("单式盘");
	    

	    
	    
	    private boolean bhideData = false;

	    
	    
	    private boolean bonlyShow5Big = false;
	    private boolean bShowInplay = false;
	    private boolean bonlyShowNotInplay = false;
	    
	    private JLabel labelGrabStat= new JLabel("状态");
	    


	    
	    private JTextField textFieldGrabStat = new JTextField(15);  
	    
	    

	    
	    
	    MyTableModel tableMode = new MyTableModel();
	    
	    
	    CTable table = null;
	    
	    CMap m = new CMap2();

	    
	    
	    
		

		public MergeNewDetailsWindow()  
	    {  
			setTitle("新合并注单");  
			
	        intiComponent();  
	        
	        
	        
	        
	        
	    }  
		
		
		

		
		
		

		
		
		
		
		public void setStateText(String txt){
			textFieldGrabStat.setText(txt);
		}
		
		public void setStateColor(Color cr){
			textFieldGrabStat.setBackground(cr);
		}

		
		
		
		public void updateEventsDetails(Vector<String[]> eventDetailsVec){
			
			try{
				
				
				if(originalDetailsData.size() != 0){
					originalDetailsData.clear();
				}

				
				for(int i = 0; i< eventDetailsVec.size(); i++){
					originalDetailsData.add(eventDetailsVec.elementAt(i).clone());
				}
				
				
				
				updateShowItem();
				
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			

			
		}
		

		
		
		public void updateShowItem(){
			
			Vector<String[]> DetailsDatatmp = new Vector<String[]>();
			
			//只显示走地盘
			if(bShowInplay == true){
				for(int i = 0; i < originalDetailsData.size(); i++){
					if(originalDetailsData.elementAt(i)[NEWMERGEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
						DetailsDatatmp.add(originalDetailsData.elementAt(i));
					}
				}
			}
			
			//只显示单式盘
			if(bonlyShowNotInplay == true){
				for(int i = 0; i < originalDetailsData.size(); i++){
					if(!originalDetailsData.elementAt(i)[NEWMERGEINDEX.EVENTNAMNE.ordinal()].contains("滚动盘")){
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
					if(P8Http.isInShowLeagueName(DetailsDatatmp.elementAt(i)[NEWMERGEINDEX.LEAGUENAME.ordinal()])){
						DetailsDatatmp1.add(DetailsDatatmp.elementAt(i));
					}
				}
			}
			
			
			if(DetailsDatatmp1.size() == 0){
				DetailsDatatmp1 = (Vector<String[]>)DetailsDatatmp.clone();
			}

			
			detailsData = (Vector<String[]>)DetailsDatatmp1.clone();
			
			
			if(true == bhideData){
				hideDatacols();
			}else{
				showDatacols();
			}

			fittable();
			
			tableMode.updateTable();

			
		}

		
		

		
	    public void fittable(){
		    JTableHeader header = table.getTableHeader();
		     int rowCount = table.getRowCount();
		     Enumeration columns = table.getColumnModel().getColumns();
		     while(columns.hasMoreElements()){
		         TableColumn column = (TableColumn)columns.nextElement();
		         int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
		         int width = (int)table.getTableHeader().getDefaultRenderer()
		                 .getTableCellRendererComponent(table, column.getIdentifier()
		                         , false, false, -1, col).getPreferredSize().getWidth();
		         for(int row = 0; row<rowCount; row++){
		             int preferedWidth = (int)table.getCellRenderer(row, col).getTableCellRendererComponent(table,
		            		 table.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
		             width = Math.max(width, preferedWidth);
		         }
		         header.setResizingColumn(column); // 此行很重要
		         column.setWidth(width+table.getIntercellSpacing().width);
		    
		     }
	    }
		
	  
	    /** 
	     * 初始化窗体组件 
	     */  
	    private void intiComponent()  
	    {  

			final Container container = getContentPane();
			
			container.setLayout(new BorderLayout());
			
			JPanel panelNorth = new JPanel(new GridLayout(1, 4));

	        container.add(panelNorth, BorderLayout.NORTH);  
	        
	        
	        
	        JPanel panelCheckbox = new JPanel(new GridLayout(1, 4));
	        

	        
	        panelCheckbox.add(hideDetailsData);
	        panelCheckbox.add(ShowInplay);
	        panelCheckbox.add(ShowNotInplay);

	        
	        jcb.setSelectedIndex(0);
	        
	        jcb.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
	                String content = jcb.getSelectedItem().toString();
	                
	              //  StoneAge.setSleepTime(Integer.parseInt(content));
				}
	        });
	        
	        textFieldGrabStat.setEditable(false);
	        
	        onlyShow5Big.setSelected(false);
	        
	        onlyShow5Big.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.DESELECTED){
						bonlyShow5Big = false;
					}else{
						bonlyShow5Big = true;
					}
					
					updateShowItem();
				}
	        });
	        
	        hideDetailsData.setSelected(false);
	        
	        hideDetailsData.addItemListener(new ItemListener() {


				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
	               // int index = jcb.getSelectedIndex();
					if(e.getStateChange() == ItemEvent.DESELECTED){
						bhideData = false;

					}else{
						bhideData = true;
					}
					
					updateShowItem();
				}
	        });
	        

	        
	        
	       
	        
	        panelNorth.add(labelInterval);
	        panelNorth.add(jcb);
		    


	        panelNorth.add(onlyShow5Big);
	        panelNorth.add(panelCheckbox);

	        
	        panelNorth.add(labelGrabStat);

	        panelNorth.add(textFieldGrabStat);
	        
	        



		    
			
	        table = new CTable(m,tableMode);

	        JScrollPane scroll = new JScrollPane(table); 
	        
	        
	        //设置选中行
	        table.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                int sr;
	                if ((sr = table.getSelectedRow()) == -1) {
	                    return;
	                }else{
	                	int block = ((int)(sr/2))%2;
	                	int nRowblock = ((int)((sr+1)/2))%2;
	                	if(nRowblock== block){
	                		table.setRowSelectionInterval(sr, sr + 1);
	                	}else{
	                		table.setRowSelectionInterval(sr-1, sr);
	                	}
	                	
	                }
	                
	            }
	        });

	        

	        

		    
		    table.setRowHeight(30);
		    
		    table.setFont(new java.awt.Font("黑体", Font.PLAIN, 15));

		    
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
	    
	    
	    
	    public void setOneRowBackgroundColor() {
	    	

	    	
	        try {  
	            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {  
	  
	                public Component getTableCellRendererComponent(JTable table,  
	                        Object value, boolean isSelected, boolean hasFocus,  
	                        int row, int column) {  
	                    if (((int)(row/2))%2 == 0) {  
	                    	setBackground(new Color(246,246,246));  
	                        //setForeground(Color.BLACK);  
	                    }else{  
	                        setBackground(new Color(222,222,243));  
	                      //  setForeground(Color.BLACK);  
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
	    
	    

		public void hideDatacols(){
			try{
				TableColumnModel   columnModel=table.getColumnModel();   
				TableColumnModel column_id_header = table.getTableHeader().getColumnModel(); 
				for(int i = NEWMERGEINDEX.P8HRES.ordinal();i <= NEWMERGEINDEX.HGORES.ordinal(); i++ ){
				    
				    TableColumn   column=columnModel.getColumn(i);   
				    column.setMinWidth(0);   
				    column.setMaxWidth(0);
				    column.setWidth(0);
				    column.setPreferredWidth(0);
				}

				
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public void showDatacols(){
			try{
				TableColumnModel   columnModel=table.getColumnModel();   
				TableColumnModel column_id_header = table.getTableHeader().getColumnModel(); 
				for(int i = NEWMERGEINDEX.P8HRES.ordinal();i <= NEWMERGEINDEX.HGORES.ordinal(); i++ ){
				    
				    TableColumn   column=columnModel.getColumn(i);   
				    column.setMinWidth(0);   
				    column.setMaxWidth(500);
				    column.setWidth(300);
				    column.setPreferredWidth(300);
				    
				    
				    
		/*			    TableColumn columnhead = column_id_header.getColumn(i);
				    columnhead.setMinWidth(0);   
				    columnhead.setMaxWidth(0);
				    columnhead.setWidth(0);
				    columnhead.setPreferredWidth(0);*/
				    
				}

			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		}

	    
	    
	    
	  
	    public class MyTableModel extends AbstractTableModel  
	    {  
	        /* 
	         * 这里和刚才一样，定义列名和每个数据的值 
	         */  
	        String[] columnNames =  
	        	{ "序号", "联赛", "时间", "球队", "全场让球盘", "让球水位", "全场大小盘", "大小球水", "平博让球" , "平博滚动盘", "智博","皇冠",  
	        		"平博大小球", "平博滚动盘 ", "智博 ","皇冠 ", "滚动让球盘","滚动让球水","滚动大小球盘","滚动大小球水"};  
	        

	  
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
	        	return detailsData.size()*2;  
	        }  
	  
	        /** 
	         * 得到数据所对应对象 
	         */  
	        @Override  
	        public Object getValueAt(int rowIndex, int columnIndex)  
	        {  

	        	if(columnIndex == NEWMERGEINDEX.EVENTID.ordinal()){
	        		return Integer.toString(rowIndex/2 + 1);
	        	}
	        	
	        	
	        	if(columnIndex == NEWMERGEINDEX.LEAGUENAME.ordinal()){
	        		return detailsData.elementAt(rowIndex/2)[NEWMERGEINDEX.LEAGUENAME.ordinal()] + "\r\n" + detailsData.elementAt(rowIndex/2)[NEWMERGEINDEX.SCORE.ordinal()];
	        	}
	        	
	        	
	        	if(columnIndex == NEWMERGEINDEX.EVENTNAMNE.ordinal()){
	        		int newRow = 0;
	        		newRow = (int)(rowIndex/2);
	        		String res = detailsData.elementAt(newRow)[columnIndex];
	        		String[] resA = res.split(" vs ");
	        		return resA[rowIndex%2];
	        	}else if(columnIndex == NEWMERGEINDEX.RQSW.ordinal()){
	        		int newRow = 0;
	        		newRow = (int)(rowIndex/2);
	        		String res = detailsData.elementAt(newRow)[columnIndex];
	        		
	        		if(res.equals("|")){
	        			return "";
	        		}
	        		
	        		if(res.contains("|")){
	            		String[] resA = res.split("=");
	            		res = resA[0];
	            		resA = res.split("\\|");
	            		res = resA[rowIndex%2];
	            		return res;
	        		}else{
	        			return res;
	        		}

	        		
	        	}else if(columnIndex == NEWMERGEINDEX.DXQSW.ordinal()){
	        		int newRow = 0;
	        		newRow = (int)(rowIndex/2);
	        		
	        		String res = detailsData.elementAt(newRow)[columnIndex];
	        		
	        		if(res.equals("|")){
	        			return "";
	        		}
	        		
	        		if(res.contains("|")){
	            		String[] resA = res.split("=");
	            		res = resA[0];
	            		resA = res.split("\\|");
	            		res = resA[rowIndex%2];
	            		return res;
	        		}else{
	        			return res;
	        		}

	        		
	        	}else if(columnIndex == NEWMERGEINDEX.GQRQSW.ordinal()){
	        		int newRow = 0;
	        		newRow = (int)(rowIndex/2);
	        		String res = detailsData.elementAt(newRow)[columnIndex];
	        		
	        		if(res.equals("|")){
	        			return "";
	        		}
	        		
	        		if(res.contains("|")){
	            		String[] resA = res.split("=");
	            		res = resA[0];
	            		resA = res.split("\\|");
	            		res = resA[rowIndex%2];
	            		return res;
	        		}else{
	        			return res;
	        		}

	        		
	        	}else if(columnIndex == NEWMERGEINDEX.GQDXQSW.ordinal()){
	        		int newRow = 0;
	        		newRow = (int)(rowIndex/2);
	        		String res = detailsData.elementAt(newRow)[columnIndex];
	        		
	        		if(res.equals("|")){
	        			return "";
	        		}
	        		if(res.contains("|")){
	            		String[] resA = res.split("=");
	            		res = resA[0];
	            		resA = res.split("\\|");
	            		res = resA[rowIndex%2];
	            		return res;
	        		}else{
	        			return res;
	        		}

	        		
	        	}else{
	        		return detailsData.elementAt(rowIndex/2)[columnIndex];
	        	}
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
