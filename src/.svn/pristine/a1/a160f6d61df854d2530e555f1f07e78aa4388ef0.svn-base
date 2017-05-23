package com.rd.client.win;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.ExplorerTreeNode;
import com.rd.client.SideNavTree;
import com.rd.client.common.obj.FUNCTION;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.system.SystemListDS;
import com.rd.client.obj.system.SYS_USER;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tree.Tree;

public class SystemWin extends Window{

	private int width = 300;
	private int height = 340;
	private String top = "25%";
	private String left = "40%";
	private String title = "切换系统";
	public Window window;
	private DataSource ds;
	private TabSet mainTabSet;
    private SideNavTree sideNav;
    private Img img;
	
	public SystemWin(TabSet tabSet, SideNavTree sideNav, Img img, String top,String left){
		this.mainTabSet = tabSet;
		this.sideNav = sideNav;
		this.top = top;
		this.left = left;
		this.img = img;
	}
	
	public Window getViewPanel() {
		VLayout lay = new VLayout();
		
		ds = SystemListDS.getInstance("SYS_USER_SYSTEM1", "SYS_USER_SYSTEM");
		
		final SGTable addrList = new SGTable(ds, "100%", "90%"); 
		addrList.setCanEdit(false);
		addrList.setShowFilterEditor(false);
		addrList.setShowRowNumbers(true);
		addrList.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		addrList.setSelectionType(SelectionStyle.SINGLE);
		addrList.setEditEvent(ListGridEditEvent.DOUBLECLICK);	
		
		ListGridField ID = new ListGridField("CODE","CODE",120);
		ID.setHidden(true);
		ListGridField PRI_SYSTEM = new ListGridField("PRI_SYSTEM","系统名称",120);
		//Util.initCodesComboValue(PRI_SYSTEM, "SYS_TYP");
		addrList.setFields(ID,PRI_SYSTEM);
		
		SGPanel form=new SGPanel();
		
		SGButtonItem determine = new SGButtonItem(StaticRef.CONFIRM_BTN);
		determine.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				SYS_USER user = LoginCache.getLoginUser();
				Record selectRecord = addrList.getSelectedRecord();
				
				user.setLOGIN_SYSTEM(selectRecord.getAttribute("CODE"));
				Tab[] tabs = mainTabSet.getTabs();
				if(tabs != null && tabs.length > 0) {
					for(int i = 1; i < tabs.length; i++) {
						mainTabSet.removeTab(tabs[i]);
					}
				}
				Util.async.getFuncList(user.getROLE_ID(),user.getLOGIN_SYSTEM(),new AsyncCallback<List<FUNCTION>>() {
					
					@Override
					public void onSuccess(List<FUNCTION> result) {
						int i = 0;
						ExplorerTreeNode[] treeData = new ExplorerTreeNode[result.size()];
						for(FUNCTION power : result){
							treeData[i] = new ExplorerTreeNode(power.getFUNCTION_NAME(), power.getFUNCTION_ID(), power.getPARENT_FUNCTION_ID(),StaticRef.ICON_NODE, power.getUserPanel(), true, "");
							i++;
						}
						Tree tree = new Tree();
				        tree.setModelType(TreeModelType.PARENT);
				        tree.setNameProperty("nodeTitle");
				        tree.setOpenProperty("isOpen");
				        tree.setIdField("nodeID");
				        tree.setParentIdField("parentNodeID");
				        tree.setRootValue("root");
				        
						tree.setData(treeData);
						sideNav.setShowcaseData(treeData);
						sideNav.setData(tree);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						SC.say("获取用户功能列表失败！！！");
					}
				});
				System.out.println(user.getLOGIN_SYSTEM());
				if(user.getLOGIN_SYSTEM().equals("B")) {
					img.setSrc("rd/bms.png");
				}
				else {
					img.setSrc("rd/tms.png");
				}
				window.hide();
			}
		});
		SGButtonItem cancel = new SGButtonItem(StaticRef.CANCEL_BTN);
		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				window.destroy();
			}
			
		});
		form.setItems(determine,cancel);
		
		lay.addMember(addrList);
		lay.addMember(form);
		
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(lay);
		window.setShowCloseButton(true);
		window.show();
		
		Criteria criteria = new Criteria();
		if(criteria == null) {
			criteria = new Criteria();
		}
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("USER_ID",LoginCache.getLoginUser().getUSER_ID());
		criteria.addCriteria("ROLE_ID",LoginCache.getLoginUser().getROLE_ID());
		addrList.invalidateCache();
		addrList.fetchData(criteria);  
		
		return window;
	}

	
}
