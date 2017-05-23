package com.rd.client.win;



import com.rd.client.common.ds.TreeListDS;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.widgets.TreeTable;
import com.rd.client.ds.base.OrgDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.tree.TreeGridField;

/**
 * 组织机构公用弹出窗口
 * @author Administrator
 * 
 */
public class OrgWin extends Window {
	
	private int width = 240;
	private int height = 300;
	private String top = "25%";
	private String left = "40%";
	private String title = "组织机构";
	public Window window;
	private DataSource orgDS;
	private TextItem orgNameItem = null;
	private TextItem orgIdItem = null;
	private TextItem fatherOrgId = null;
	private ListGridField orgFiled=null;
	private boolean isAll = true;
	private ListGrid table;
	private int col;
	
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */
	public OrgWin(TextItem orgNameItem,boolean isAll){
		this.orgNameItem = orgNameItem;
		this.isAll = isAll;
	}
	
	public OrgWin(TextItem orgNameItem,boolean isAll,String top,String left){
		this.orgNameItem = orgNameItem;
		this.isAll = isAll;
		this.top = top;
		this.left = left;
	}
	
	public OrgWin(TextItem orgNameItem,TextItem orgIdItem,boolean isAll,String top,String left){
		this.orgNameItem = orgNameItem;
		this.orgIdItem = orgIdItem;
		this.isAll = isAll;
		this.top = top;
		this.left = left;
	}
	
	public OrgWin(TextItem orgNameItem,TextItem orgIdItem,boolean isAll,String top,String left,TextItem fatherOrgId){
		this.orgNameItem = orgNameItem;
		this.orgIdItem = orgIdItem;
		this.isAll = isAll;
		this.top = top;
		this.left = left;
		this.fatherOrgId = fatherOrgId;
	}
	
	public OrgWin(ListGrid table,ListGridField orgFiled,int col,boolean isAll,String top,String left){
		this.table = table;
		this.orgFiled = orgFiled;
		this.isAll = isAll;
		this.top = top;
		this.col =col;
		this.left = left;
	}
	
	public Window getViewPanel() {
	
		if(isAll){//全部显示
			orgDS = OrgDS.getInstance("BAS_ORG");
			
		}else{
			orgDS = TreeListDS.getInstance("V_USER_ORG");
		}
		final TreeTable tree = new TreeTable(orgDS, "100%", "100%"); 
		
		tree.addDrawHandler(new DrawHandler() {  
        	public void onDraw(DrawEvent event) { 
        		Criteria criteria = new Criteria();
        		criteria.addCriteria("OP_FLAG","M");
        		if(!isAll){
        			criteria.addCriteria("USER_ID",LoginCache.getLoginUser().getUSER_ID());
        		}
        		tree.fetchData(criteria);  
        	}  
        }); 
		tree.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record selectedRecord  = tree.getSelectedRecord();
				if(orgNameItem != null){
					orgNameItem.setValue(selectedRecord.getAttributeAsString("SHORT_NAME"));
				}
				if(orgIdItem != null){
					orgIdItem.setValue(selectedRecord.getAttributeAsString("ID"));
					orgNameItem.focusInItem();
				}
				
				if(orgFiled != null){
					table.setEditValue(col,orgFiled.getName(),selectedRecord.getAttribute("SHORT_NAME"));
					orgFiled.setAttribute("ORG_ID", selectedRecord.getAttribute("ID"));
				}
				if(fatherOrgId != null) {
					fatherOrgId.setValue(selectedRecord.getAttributeAsString("PARENT_ORG_ID"));
				}
				
            	window.hide();
			}
		});
		
        TreeGridField orgField = new TreeGridField("ORG_CNAME");
        tree.setFields(orgField);

		
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(tree);
		window.setShowCloseButton(true);
		window.show();
		
		return window;
	}
	
}
