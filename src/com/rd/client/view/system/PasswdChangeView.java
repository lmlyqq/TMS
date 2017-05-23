package com.rd.client.view.system;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGPanel;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;


public class PasswdChangeView extends Window {
	private int width=340;
	private int height=250;
	public ButtonItem confirmItem;
	public Window window = null;
	public DynamicForm form;
	public ListGrid table;
	public DynamicForm mainItem;
	public SectionStackSection section;
	private String title;
	public DynamicForm mainForm;
	public TextItem userId;
	public PasswordItem oldPWD;
	public PasswordItem newPWD;
	public PasswordItem secPWD;
	public ToolStrip toolStrip;
	protected String fid;
	
	public PasswdChangeView() {
		
	}

	public PasswdChangeView(int width, int height, String top, String left,
			String title, Window window, DataSource ds) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.window = window;
	}


//	public void createForm(DynamicForm searchForm) {
	public void createBtnWidget() {
		
		IButton saveBtn = new IButton(Util.BI18N.SAVE());
		saveBtn.setIcon("rd/save.png");
		saveBtn.setWidth(60);
//		saveBtn.setEndRow(false);
		saveBtn.setAlign(Alignment.RIGHT);
		
		saveBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					if(!newPWD.getValue().equals(secPWD.getValue())){
						SC.warn("两次输入的新密码不同，重新输入。");
					}else{
						/*if(!isValidPwd(newPWD.getValue().toString())) {
							SC.warn("密码必须由6-16位的数字和字母组成!");
							return;
						}*/
						Util.async.updatePWD(userId.getValue().toString(), oldPWD.getValue().toString(), 
								newPWD.getValue().toString(),new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onSuccess(String result) {
								if("00".equals(result)){
									SC.say("密码修改成功!");
								}else if("01".equals(result)){
									SC.warn("密码修改失败!");
								}else{
									SC.warn(result);
								}
							}
						});
					}
				}
			});
		
		IButton clearBtn = new IButton(Util.BI18N.CANCEL());
		clearBtn.setIcon("rd/cancel.png");
		clearBtn.setWidth(60);
//		clearBtn.setStartRow(false);
		clearBtn.setAlign(Alignment.RIGHT);
		
		clearBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				
				form.clearValues();
			}
		});
			
		
		toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(8);
        toolStrip.setMembers(saveBtn,clearBtn);
  
	}
	
	
	public Window getViewPanel() {
		createBtnWidget();
		
		userId = new TextItem("USER_ID","用户名");
		userId.setValue(LoginCache.getLoginUser().getUSER_ID());
		userId.setDisabled(true);
		userId.setTitle(ColorUtil.getBlackTitle("用户名"));
		
		
		oldPWD = new PasswordItem("OLD_PWD","原密码");
		newPWD = new PasswordItem("NEW_PWD","新密码");
		secPWD = new PasswordItem("secPWD","重复密码");
		/**
		secPWD.addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				
			}
		});**/
		
		
		form = new SGPanel();
		form.setItems(userId,oldPWD,newPWD,secPWD);
		form.setHeight(height / 2);
		form.setWidth(width - 30);
		
		form.setNumCols(2);
		form.setCellPadding(1);
		
		form.setPadding(10);
		form.setTitleWidth(75);
		form.setAlign(Alignment.LEFT);
	//	form.setHeight("35%");
		form.setHeight(150);
//		createForm(form);
		
		TabSet tabSet=new TabSet();
		tabSet.setWidth("100%");
		tabSet.setHeight("100%");
		
		Tab tab=new Tab("密码修改");
		tab.setPane(form);
		tabSet.addTab(tab);
	
		VLayout lay1 = new VLayout();
		lay1.setWidth100();
		lay1.setHeight100();
		lay1.addMember(tabSet);

		window = new Window();
		window.setTitle(title);  
		window.setLeft("30%");
		window.setTop("35%");
		window.setWidth(width);
		window.setHeight(height);
		window.setCanDragReposition(true);
		window.setCanDragResize(true);
		
		window.addItem(lay1);
//		window.addItem(mainItem);
		window.addItem(toolStrip);

		window.setShowCloseButton(true);
		window.show();
		window.addMinimizeClickHandler(new MinimizeClickHandler() {

			@Override
			public void onMinimizeClick(MinimizeClickEvent event) {
				window.setMinimized(false);
				window.hide();
				event.cancel();
			}

		});

		return window;
	}
	
	/*private boolean isValidPwd(String password) {
		if(password.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$")){
			return true;
		}
		return false;
	}*/

}