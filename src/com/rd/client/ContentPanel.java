package com.rd.client;

import java.util.HashMap;

import com.rd.client.common.obj.LoginCache;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public abstract class ContentPanel extends VLayout{
	
	//一组或者一对相对的按钮,add组和save组的disabled属性相反(比如审核和取消审核、结算和取消结算).	使用List是为了减少在每个界面里创建Map对象
	protected HashMap<Integer,HashMap<String,IButton>> add_list = new HashMap<Integer,HashMap<String,IButton>>();	//一组按钮
	protected HashMap<Integer,HashMap<String,IButton>> save_list = new HashMap<Integer,HashMap<String,IButton>>();	//一组按钮
	protected HashMap<String,IButton> save_map = new HashMap<String, IButton>();	//保存、取消按钮
	protected HashMap<String,IButton> add_map = new HashMap<String, IButton>();		//新增、删除按钮
	public HashMap<Integer,Boolean> isAdd = new HashMap<Integer,Boolean>();		//判断是否新增,控制多个页签
	
	public ContentPanel() {
		setWidth100();
		setHeight100();
	}
	public abstract Canvas getViewPanel();

    /**
     * 创建按钮
     * @author yuanlei
     * @param btn_name
     * @param icon_dir
     * @param privl
     * @return
     */
    public  IButton createBtn(String btn_name, String icon_dir, String privl) {
		IButton button = new IButton(btn_name);
		//button.setAccessKey(access_key);
		//button.setIcon(icon_dir);
		button.setWidth(60);
		//button.setAutoFit(true);
		button.setShowDisabledIcon(false);
		return button;
    }
    
    /**
     * 根据权限创建按钮,如果没有权限,则不创建    XWB 2016-04-06
     * @param btn_name
     * @param icon_dir
     * @param privl
     * @param t
     * @param ch
     * @return
     */
    public IButton createBtn(String btn_name, String icon_dir, String privl,ToolStrip t,ClickHandler ch) {
    	if(!isPrivil(privl)) {
    		ch = null;//释放变量
    		return null;
    	}
		IButton button = new IButton(btn_name);
		//button.setIcon(icon_dir);
		//button.setAutoFit(true);
		button.setWidth(60);
		button.setShowDisabledIcon(false);
		t.addMember(button);
		button.addClickHandler(ch);
		return button;
    }
    
    /**
     * 查看是否拥有权限   XWB 2016-04-06
     * @param privil
     * @return
     */
    public boolean isPrivil(String privil){
    	//System.out.println(LoginCache.getLoginUser().getROLE_ID());
    	if("SUPER_MAN".equals(LoginCache.getLoginUser().getROLE_ID())) {
    		return true;
    	}
    	return LoginCache.privilegeMap.containsKey(privil);
    }

    /**
     * 初始化新增按钮,使ADD组可用,SAVE组不可用
     * @param index   按钮所在的页签  ,按照add_list或save_list添加的顺序而定
     * 
     * 同时会改变新增状态    XWB 2016-04-12
     */
    public void initAddBtn(){
    	if(!add_list.isEmpty() && !save_list.isEmpty()){
    		initAddBtn(0);
    	}else{
    		enableOrDisableBtn(add_map, false);
    		enableOrDisableBtn(save_map, true);
    		isAdd.put(0, false);
    	}
    }
    public void initAddBtn(int index){
    	enableOrDisableBtn(add_list.get(index), false);
    	enableOrDisableBtn(save_list.get(index), true);
    	isAdd.put(index, false);
    }
    
    /**
     * 初始化保存按钮,使ADD组不可用,SAVE组可用
     * @param index   按钮所在的页签  ,按照add_list或save_list添加的顺序而定
     * 
     * 同时会改变新增状态   XWB 2016-04-12
     */
    public void initSaveBtn(){
    	if(!add_list.isEmpty() && !save_list.isEmpty()){
    		initSaveBtn(0);
    	}else{
    		enableOrDisableBtn(add_map, true);
    		enableOrDisableBtn(save_map, false);
    		isAdd.put(0, true);
    	}
    }
    public void initSaveBtn(int index){
    	enableOrDisableBtn(add_list.get(index), true);
    	enableOrDisableBtn(save_list.get(index), false);
    	isAdd.put(index, true);
    }
    
    /**
     * 设置单个按钮的权限
     * @param privil
     * @param btn
     * @param disabled
     */
    public void enableOrDisableBtn(String privil,IButton btn,boolean disabled){
    	if(isPrivil(privil)) btn.setDisabled(disabled);
    }
    
    /**
     * 设置一组按钮的权限
     * @param _map
     * @param disabled
     */
    public void enableOrDisableBtn(HashMap<String,IButton> _map,boolean disabled){
    	if(_map != null && !_map.isEmpty()){
    		for(String key : _map.keySet())
    			if(isPrivil(key)) _map.get(key).setDisabled(disabled);
    	}
    }
    
    /**
     * 设置新增状态
     * @param isAdd
     */
    public void setIsAdd(boolean isAdd){setIsAdd(0, isAdd);}
    
    public void setIsAdd(int index,boolean isAdd){
    	this.isAdd.put(index, isAdd);
    }
    
    /**
     * 设置form和新增、保存、删除、取消按钮的联动
     * 将itemChanged事件替换成valuesChanged事件,以监听form中的setValues方法		--XWB 2016-06-01
     * 还原itemChanged事件,去除ValuesChanged事件(因为在查询数据的时候会触发)		--XWB 2016-06-02
     * 新增DynamicForm的DoubleClick事件,暂时替换ValuesChanged事件					--XWB 2016-06-02
     * @param form
     */
    protected void initFormSaveBtn(DynamicForm form){
    	form.addDoubleClickHandler(new DoubleClickHandler() {
			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(isAdd.get(0)) return;
				initSaveBtn();
			}
		});
    	form.addItemChangedHandler(new ItemChangedHandler() {
			
			@Override
			public void onItemChanged(ItemChangedEvent event) {
				if(isAdd.get(0)) return;
				initSaveBtn();
			}
		});
    }
    /**
     * 设置form和新增、保存、删除、取消按钮的联动
     * 将itemChanged事件替换成valuesChanged事件,以监听form中的setValues方法		--XWB 2016-06-01
     * 还原itemChanged事件,去除ValuesChanged事件(因为在查询数据的时候会触发)		--XWB 2016-06-02
     * 新增DynamicForm的DoubleClick事件,暂时替换ValuesChanged事件					--XWB 2016-06-02
     * @param form
     * @param index	
     */
    protected void initFormSaveBtn(DynamicForm form,final int index) {
    	form.addDoubleClickHandler(new DoubleClickHandler() {
			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(isAdd.get(index)) return;
				initSaveBtn(index);
			}
		});
    	form.addItemChangedHandler(new ItemChangedHandler() {
			
			@Override
			public void onItemChanged(ItemChangedEvent event) {
				if(isAdd.get(index)) return;
				initSaveBtn(index);
			}
		});
	}
    
    /**
     * 根据权限判断tab，如果没有权限，则删除该tab			--XWB 2016-06-16
     * @param privil	权限字符串
     * @param tabs		TabSet对象
     * @param tab		要删除的tab对象
     * @return			true有权限，false没有权限，提供返回值是方便扩展
     */
    protected boolean chkTabPriv(String privil,TabSet tabSet,Tab tab){
    	if(isPrivil(privil)) return true;
    	removeTabPostCreate(tabSet, tab);
    	return false;
    }
    protected boolean chkTabPriv(String privil,TabSet tabSet,String id){
    	return chkTabPriv(privil, tabSet, tabSet.getTab(id));
    }
    
    /**
	 * 权限控制下删除移除tab
	 * 
	 * @param tabs	TabSet对象
	 * @param tab	需要移除的tab对象
	 */
	private native void removeTabPostCreate(TabSet tabs,Tab tab) /*-{
	var timeout = window.setTimeout(function(){
		var self = tabs.@com.smartgwt.client.widgets.BaseWidget::getOrCreateJsObj()();
	    var tabJS = tab.@com.smartgwt.client.widgets.tab.Tab::getJsObj()();
	    self.removeTab(tabJS);
	    window.clearTimeout(timeout);
	},1,tabs,tab);
}-*/;
    
    /**
     * 根据权限判断layout，如果没有权限，则删除该layout		--XWB 2016-06-16
     * @param privil	权限字符串
     * @param layout	要删除的layout对象
     * @return			true有权限，false没有权限，提供返回值是方便扩展
     */
    protected boolean chkLayoutPriv(String privil,Layout layout){
    	if(isPrivil(privil)) return true;
    	layout.destroy();
    	return false;
    }
    
    /**
     * 根据权限判断DynamicForm，如果没有权限，则删除该dynamicForm		--XWB 2016-06-16
     * @param privil	权限字符串
     * @param form		需要删除的DynamicForm对象
     * @return			true有权限，false没有权限，提供返回值是方便扩展
     */
    protected boolean chkFormPriv(String privil,DynamicForm form){
    	if(isPrivil(privil)) return true;
    	form.destroy();
    	return false;
    }
}
