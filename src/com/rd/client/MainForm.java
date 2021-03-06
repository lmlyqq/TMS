package com.rd.client;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.Util;
import com.rd.client.view.system.ChangeOrganizationWin;
import com.rd.client.view.system.PasswdChangeView;
import com.rd.client.view.system.SQLExecuteView;
import com.rd.client.win.SystemWin;
import com.rd.client.win.WeatherWin;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.KeyPressEvent;
import com.smartgwt.client.widgets.events.KeyPressHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;
/**
 * 主界面布局
 * 
 * @author yuanlei
 * 
 */
public class MainForm implements EntryPoint {
    
	private TabSet mainTabSet;
	private Label userLab;
	public String ip;
	public Label orglab;
	public Label wareLab2;
	private Label userLab2;
	
    public static Menu contextMenu;
    private SideNavTree sideNav;
    private Img img;
	@Override
	public void onModuleLoad() {
		
		FormUtil.longWidth = new Double(Page.getWidth()*0.20).intValue();
		FormUtil.Width = new Double(Page.getWidth()*0.10).intValue();
		FormUtil.shortWidth = new Double(Page.getWidth()*0.05).intValue();
		
		/*DateUtil.setNormalDateDisplayFormatter(new DateDisplayFormatter() {
		     public String format(java.util.Date date) {
		         if(date == null) return null;
		         System.out.println("sddss");
		         final DateTimeFormat dateFormatter = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm");
		         String format = dateFormatter.format(date);
		         return format;
		     }
		 });
		DateUtil.setDateInputFormatter(new DateInputFormatter() {

			@Override
			public Date parse(String dateString) {
				final DateTimeFormat dateFormatter = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm");
			    java.util.Date date = dateFormatter.parse(dateString);
			    return date;
			}
		 });*/

		RPCManager.setShowPrompt(false);
		
		final String initToken = History.getToken();
		VLayout main = new VLayout() {
            @Override
            protected void onInit() {
                super.onInit();
                if (initToken.length() != 0) {
                    onHistoryChanged(initToken);
                }
            }
        };
		//main.setStylePrimaryName("mainform");
		//main.setWidth(Page.getWidth());
		//main.setAlign(Alignment.CENTER);

		//final VLayout vRight = new VLayout();

		// 创建BANNER布局
		ToolStrip topBar = new ToolStrip();
        topBar.setHeight(30);
        topBar.setWidth100();
		//topBar.setBackgroundImage("rd/banner.png");

        topBar.addSpacer(6);
        ImgButton sgwtHomeButton = new ImgButton();
        sgwtHomeButton.setSrc("rd/platform.png");
        sgwtHomeButton.setWidth(24);
        sgwtHomeButton.setHeight(24);
        sgwtHomeButton.setPrompt("普菲斯物流信息化管理平台");
        sgwtHomeButton.setHoverStyle("interactImageHover");
        sgwtHomeButton.setShowRollOver(false);
        sgwtHomeButton.setShowDownIcon(false);
        sgwtHomeButton.setShowDown(false);
        
        topBar.addMember(sgwtHomeButton);
        topBar.addSpacer(6);

        Label title = new Label("普菲斯物流信息化管理平台");
		title.setStyleName("sgwtTitle");
		title.setWidth(230);
		topBar.addMember(title);

		topBar.addFill();


        
		userLab2 = new Label("欢迎您 <span style='font-size:12px;color:black;font-weight:bold;'>" + LoginCache.getLoginUser().getUSER_NAME().replaceAll("\"", "") + "</span>&nbsp&nbsp");
		userLab2.setWidth(180);
		userLab2.setStyleName("sgwtTitle2");
		topBar.addMember(userLab2);
		
	    orglab = new Label("<font style=\"font-size:12px;font-family:微软雅黑;\">当前机构： <span style='font-size:12px;color:black;font-weight:bold;'>" + LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME().replaceAll("\"", "") + "</span></font>");
		orglab.setWidth(180);
		orglab.setStyleName("sgwtTitle2");

        topBar.addMember(orglab);
        //topBar.addSpacer(40);
        userLab = new Label("当前位置：<span style='font-size:12px;color:black;font-weight:bold;'>系统首页</span>&nbsp&nbsp");
        userLab.setWidth(350);
        userLab.setStyleName("sgwtTitle2");
        topBar.addMember(userLab);
		topBar.addSeparator();
	
		ToolStripButton weaButton = new ToolStripButton();
		weaButton.setTitle("天气预报");
		weaButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
		
			@Override
			public void onClick(ClickEvent event) {
				WeatherWin weatherWin=new WeatherWin();
				weatherWin.getViewPanel();
			}
		});		
	    topBar.addButton(weaButton);
		topBar.addSeparator();
		
		ToolStripButton passButton = new ToolStripButton();
		passButton.setTitle("修改密码");
		passButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
		
			@Override
			public void onClick(ClickEvent event) {
				PasswdChangeView PasswdChangeView=new PasswdChangeView();
				PasswdChangeView.getViewPanel();
			}
		});		
	    topBar.addButton(passButton);
		topBar.addSeparator();
        
        ToolStripButton orgButton = new ToolStripButton();
        orgButton.setTitle("切换组织");
        orgButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ChangeOrganizationWin win = new ChangeOrganizationWin(getThis());
				win.getViewPanel();
				
			}
		});		
        topBar.addButton(orgButton);
		topBar.addSeparator();
		
        ToolStripButton weatherButton = new ToolStripButton();
        weatherButton.setTitle("切换系统");
        //orgButton.setIcon("rd/sitemap_color.png");
        weatherButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				new SystemWin(mainTabSet,sideNav,img,"25%","40%").getViewPanel();		
//				//ChangeOrganizationWin win = new ChangeOrganizationWin(getThis());
//				//win.getViewPanel();
//				System.out.println(LoginCache.getLoginUser().getTOKEN());
//				System.out.println(LoginCache.getLoginUser().getUSER_ID());
//				System.out.println(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
//				doForword("http://127.0.0.1:8086/pfs/mainform.html?service=bms&token="+LoginCache.getLoginUser().getTOKEN());
			}
		});		
        topBar.addButton(weatherButton);
		topBar.addSeparator();

        ToolStripButton exitButton = new ToolStripButton();
        exitButton.setTitle("退出系统");
        //exitButton.setIcon("rd/exit.png");
        exitButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				SC.confirm("您是否确认退出系统？", new BooleanCallback() {
					public void execute(Boolean value) {
	                    if (value != null && value) {
	                    	doForword("/pfs/initDataServlet?ds_id=relogo");
	                    }
	                }
	            });
			}
		});
		topBar.addButton(exitButton);
		topBar.addSpacer(6);

		main.addMember(topBar);
		main.setWidth100();
        main.setHeight100();
        //main.setStyleName("tabSetContainer");

		/*HLayout menu_stack = new HLayout();
		createMenu(menu_stack);
		main.addMember(menu_stack);
		*/

		HLayout formLayout = new HLayout();
		formLayout.setWidth100();

		//vRight.setWidth100(); 
		//vRight.setBackgroundColor("#8FA2B3");
		//vRight.setHeight(Page.getHeight() - 52);
		
		VLayout sideNavLayout = new VLayout();
        sideNavLayout.setHeight100();
        sideNavLayout.setWidth(185);
        sideNavLayout.setShowResizeBar(true);

        sideNav = new SideNavTree();
        sideNav.setShowHeader(false);
        sideNav.addLeafClickHandler(new LeafClickHandler() {
            public void onLeafClick(LeafClickEvent event) {
                TreeNode node = event.getLeaf();
                showSample(node);
                userLab.setContents("当前位置:<span style='font-size:12px;color:black;font-weight:bold;'>" + node.getName() + "</span>&nbsp&nbsp");
            }
        });

        sideNavLayout.addMember(sideNav);
		
		addKeyPress(main);

		mainTabSet = new TabSet();

		Layout paneContainerProperties = new Layout();
		paneContainerProperties.setLayoutMargin(0);
		paneContainerProperties.setLayoutTopMargin(1);
		mainTabSet.setPaneContainerProperties(paneContainerProperties);
		
		/*LayoutSpacer layoutSpacer = new LayoutSpacer();
        layoutSpacer.setWidth(5);
        
		DynamicForm form = new DynamicForm();
        form.setPadding(0);
        form.setMargin(0);
        form.setCellPadding(1);
        form.setNumCols(1);
        
		mainTabSet.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER, layoutSpacer, form);*/
		contextMenu = createContextMenu();

		mainTabSet.setWidth100();
		mainTabSet.setHeight100();
		mainTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			public void onTabSelected(TabSelectedEvent event) {
				Tab selectedTab = event.getTab();
				String historyToken = selectedTab.getAttribute("historyToken");
				if (historyToken != null) {
					History.newItem(historyToken, false);
				}
			}
		});

		mainTabSet.setTabBarControls(TabBarControls.TAB_SCROLLER,
				TabBarControls.TAB_PICKER);

		VLayout lay = new VLayout();
		final Canvas bgCanvas = new Canvas();
		String sys = Cookies.getCookie("LOGIN_SYSTEM");
		if("B".equals(sys)) {
			img = new Img("rd/bms.png");
		}
		else {
			img = new Img("rd/tms.png");
		}
		img.setWidth100();
		img.setHeight(Page.getHeight() - 80);
		//img.setLeft(40);
		//img.setRight(784);
		img.setPageTop(0);
		bgCanvas.addChild(img);
		bgCanvas.setWidth100();
		bgCanvas.setHeight(Page.getHeight() - 110);

		lay.setMembers(bgCanvas);

		Tab tab = new Tab();
		tab.setTitle("首页&nbsp;&nbsp;");
		tab.setIcon("rd/models.png");
		tab.setWidth(80);
		tab.setPane(lay);
		mainTabSet.addTab(tab);
		//vRight.addMember(mainTabSet);
		
		Canvas canvas = new Canvas();
        canvas.setBackgroundImage("[SKIN]/shared/background.gif");
        canvas.setWidth100();
        canvas.setHeight100();
        canvas.addChild(mainTabSet);

		formLayout.addMember(sideNavLayout);
		formLayout.addMember(canvas);
		main.addMember(formLayout);

		main.draw();
		
		LoginCache.getBizCodes();   //获取数据字典信息
		//LoginCache.getStatCodes(); //获取状态信息
		LoginCache.getSysParam();//获取系统参数信息
		//LoginCache.getUserPrivilege();  //获取用户权限菜单
		LoginCache.getListConfig();
		//LoginCache.getUserCustomer(); //获取用户客户权限
		//LoginCache.getClientProp(); //获取配置文件信息
		Util.initMsgWin();
		/*Util.async.getLoginUserInfo("", "", new AsyncCallback<SYS_USER>() {
			
			@Override
			public void onSuccess(SYS_USER result) {
				//LoginCache.setLoginUser(result);			
				userLab2.setContents("欢迎您 <span style='font-size:12px;color:red;'>" + result.getUSER_NAME() + "</span>&nbsp&nbsp");
				orglab.setContents("当前机构：<span style='font-size:12px;color:red;'>" + result.getDEFAULT_ORG_ID_NAME() + "</span>&nbsp&nbsp");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				SC.warn(caught.getMessage());
			}
		});*/
		getPrivilCache();
	}
	
	/**
     * 获取登录用户的权限,保存到缓存   XWB 2016-04-06
     */
    private void getPrivilCache(){
    	Util.async.getUserPrivilege(new AsyncCallback<LinkedHashMap<String,String>>() {
			
			@Override
			public void onSuccess(LinkedHashMap<String, String> result) {
				if(result == null) 
					result = new LinkedHashMap<String, String>(); 	//做这个判断,是为了保证LoginCache.privilegeMap一定不是NULL,减少isPrivil()方法的判断次数
				LoginCache.privilegeMap = result;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("获取用户权限失败...失败原因:"+caught.getMessage());
			}
		});
    }

	static DataSource getDataSource() {

		DataSource dataSource = new DataSource();
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL(GWT.getHostPageBaseURL()
				+ "initDataServlet?ds_id=FUNCTION");
		dataSource.setClientOnly(true);

		// id
		DataSourceTextField idField = new DataSourceTextField("FUNCTION_ID");
		idField.setPrimaryKey(true);
		idField.setRequired(true);
		idField.setHidden(true);

		// parentId
		DataSourceTextField parentIdField = new DataSourceTextField(
				"PARENT_FUNCTION_ID");
		parentIdField.setForeignKey("FUNCTION.FUNCTION_ID");
		parentIdField.setRequired(true);
		parentIdField.setHidden(true);
		parentIdField.setRootValue(1);

		// name
		DataSourceTextField nameField = new DataSourceTextField("FUNCTION_NAME");
		nameField.setRequired(true);

		dataSource.setFields(idField, nameField, parentIdField);

		return dataSource;
	}

	/*private Canvas createView(String view_id, String func_id) {
		Canvas canvas = null;
		if (view_id.compareTo("S_USER") == 0) {  //用户管理
			canvas = new UserView(func_id);
		} 
		else if (view_id.compareTo("S_ROLE") == 0) {  //角色管理
			canvas = new RoleView(func_id);
		} 
		else if (view_id.compareTo("S_PARAM") == 0) {  //系统参数
			canvas = new ConfigView(func_id);
		} 
		else if (view_id.compareTo("S_PRIVILEGE") == 0) {  //权限管理
			canvas = new PrivilegeView(func_id);
		} 
		else if (view_id.compareTo("S_LIST_CONFIG") == 0) {  //列表配置
			canvas = new ListConfigView(func_id);
		} 
		else if (view_id.compareTo("S_MANUALINTER") == 0) {  //手动接口
			canvas = new ManualInterfaceView(func_id);
		} 
		else if (view_id.compareTo("B_CODES") == 0) {  //数据字典
			canvas = new DataDictionaryView(func_id);
//		} else if (view_id.compareTo("S_MODIFY_PWD") == 0) {
//			canvas = new PasswdChangeView(func_id);
		} 
		else if (view_id.compareTo("TB_AREA") == 0) {  //行政区域
			canvas = new BasAreaView(func_id);
		} 
		else if (view_id.compareTo("TB_ORG") == 0) {   //组织机构
			canvas = new BasOrgView(func_id);
		} 
		else if (view_id.compareTo("TB_MSRMNT") == 0) {  //度量衡
			canvas = new BasMeasureView(func_id);
		} 
		else if (view_id.compareTo("TB_SKU_CLS") == 0) {  //货品类别
			canvas = new BasSkuClassView(func_id);
			//canvas = new RestfulDataSourceSample();
		} 
		else if (view_id.compareTo("TB_PACKAGE") == 0) {  //包装
			canvas = new BasPackageView(func_id);
		} 
		else if (view_id.compareTo("TB_SKU") == 0) {  //货品管理
			canvas = new BasSkuView(func_id);
		} 
		else if (view_id.compareTo("TB_ADDR") == 0) {  //地址点管理
			canvas = new BasAddressView(func_id);
		} 
		else if(view_id.compareTo("TB_SFADDR") == 0){  //顺丰网点管理
    		canvas = new SFAddressView(func_id);
    	}
		else if(view_id.compareTo("SFODR_STATUS") == 0){  //顺丰网点管理
    		canvas = new OdrStatusView(func_id);
    	}
		else if (view_id.compareTo("TB_CUSTOMER") == 0) { //客户管理
			canvas = new BasCustomerView(func_id);
		} 
		else if (view_id.compareTo("TB_SUPPLIER") == 0) {  //供应商管理
			canvas = new BasSupplierView(func_id);
		} 
		else if (view_id.compareTo("TB_ROUTE") == 0) {   //线路管理
			canvas = new BasRouteView(func_id);
		} 
		else if (view_id.compareTo("TB_CARMODE") == 0) {  //车型管理
			canvas = new BasVehTypeView(func_id);
		} 
		else if (view_id.compareTo("HC_MANAGER") == 0) {  //运力管理
			canvas = new BasVehCapacityView(func_id);
		} 
		else if (view_id.compareTo("TB_WAREHOUSE") == 0) {  //仓库管理
			canvas = new BasWareHouseView(func_id);
		} 
		else if (view_id.compareTo("TB_ACCOUNT") == 0) {    //结算区域
			canvas = new BasChargeRegionview(func_id);
		} 
		else if(view_id.compareTo("TB_STAFF") == 0){  //人员管理
    		canvas = new BasPersonView(func_id);
    	} 
		else if(view_id.compareTo("TB_RANGE") == 0){  //服务范围
    		canvas = new BasRangeView(func_id);
    	} 
		else if(view_id.compareTo("TB_TRANSPORT")==0){  //运输服务
    		canvas =new BasTransServiceView(func_id);
    	} 
    	else if(view_id.compareTo("S_GROUP")==0){   //用户组
    		canvas=new UserGroupView(func_id);
    	} 
    	else if(view_id.compareTo("ORD_TRANSPORT")==0){  //托运单管理
    		canvas=new OrderView(func_id);
    	} 
    	else if(view_id.compareTo("ORDER_GROUP")==0){  //订单组管理
    		canvas=new OrderGroupView(func_id);
    	} 
    	else if(view_id.compareTo("SF_ORDER")==0){  //原始订单 2014-08-13 yuanlei
    		canvas=new SFOrderView(func_id);
    	} 
    	else if(view_id.compareTo("TMS_LOAD")==0){  //调度管理
     		canvas=new DispatchView(func_id);
    	} 
    	else if (view_id.compareTo("TMS_CHARGE_TYPE")==0){  //费用种类
    		canvas=new TmsChargeTypeView(func_id);
    	} 
    	else if (view_id.compareTo("TMS_TRAS_FOLLOW")==0){   //运输跟踪
    		canvas=new TmsTrackView(func_id);
    	}
    	else if (view_id.compareTo("TMS_TRAS_SHIPMENT")==0){  //作业单管理
    		canvas = new TmsShipmentView(func_id);
    	} 
    	else if (view_id.compareTo("TMS_ORD_MANAGE")==0){    //回单管理
     		canvas=new TmsOdrReceiptView(func_id);
    	} 
    	else if (view_id.compareTo("WMS_STATUS")==0){   //订单动态监视控
    		canvas=new TmsWatchView(func_id);
    	} 
    	else if(view_id.compareTo("LOSS_DAMAGE") == 0){  //货损货差
    		canvas = new DamageView(func_id);
    	} 
    	else if(view_id.compareTo("TMS_SHPM_MANAGE") == 0){  //作业单回单
    		canvas = new TmsShpmReceiptView(func_id);
    	} 
    	else if(view_id.compareTo("TB_TIME") == 0){  //时间管理
    		canvas = new BasTimeMangeView(func_id);
    	} 
    	else if(view_id.compareTo("INTERFACE_LOG") == 0){  //U8接口日志
    		canvas = new U8InterLogView(func_id);
    	} 
    	else if(view_id.compareTo("TMS_LOAD_JOB") == 0){   //提货装车
    		canvas = new PickLoadView(func_id);
    	} 
    	else if(view_id.compareTo("TMS_LJOB_SHIP") == 0){  //车辆登记
    		canvas = new VechRegistView(func_id);
    	} 
    	else if(view_id.compareTo("S_TIMER") == 0){  //定时器管理
    		canvas = new SysTimerView(func_id);
    	} 
    	else if(view_id.compareTo("T_PRE_ALTER") == 0){  //KPI预警
    		canvas = new TimeWarnManagerView(func_id);
    	} 
    	else if(view_id.compareTo("T_INSUR_ALTER") == 0){  //yuanlei 2014-06-10 保险到期提醒
    		canvas = new InsurWarnManagerView(func_id);
    	} 
    	else if(view_id.compareTo("T_VEH_ALTER") == 0){  //yuanlei 2014-06-10 车辆保养体i系那个
    		canvas = new VehRepairWarnManagerView(func_id);
    	} 
    	else if(view_id.compareTo("AREA_JOB_HEADER")==0){     //区域业务汇总表
    		canvas = new Area_Job_Header(func_id);
    	}
    	else if(view_id.compareTo("SUPPLR_JOB_HEADER")==0){   //供应商业务汇总表
    		canvas = new Supplr_Job_Header(func_id);
    	}
    	else if(view_id.compareTo("ORG_JOB_HEADER")==0){   //组织机构业务汇总表
    		canvas = new Org_Job_Header(func_id);
    	}
    	else if(view_id.compareTo("SKU_JOB_HEADER")==0){    //货品分类业务汇总表
    		canvas = new Sku_Job_Header(func_id);
    	}
    	else if(view_id.compareTo("ODR_JOB_HEADER")==0){    //订单类型业务汇总表
    		canvas = new Odr_Job_Header(func_id);
    	}
    	else if(view_id.compareTo("R_KPI_LOAD_RATE")==0){  //准时发运率
    		canvas = new R_KPI_LOAD_RATE(func_id);
    	}
    	else if(view_id.compareTo("R_KPI_DMG_RATE")==0){  //运输残损率
    		canvas = new R_KPI_DMG_RATE_VIEW(func_id);
    	}
    	else if(view_id.compareTo("R_KEY_COLL")==0){  //综合KPI
    		canvas = new R_KPI_COLL_VIEW(func_id);
    	}
    	else if(view_id.compareTo("R_JOB_OPER_HEADER") == 0){  //营运日/周/月汇总报表
    		canvas = new R_JOB_OPER_HEADER(func_id);
    	}
    	else if(view_id.compareTo("R_VEH_LOAD_VIEW") == 0){  //干线车装载数据统计
    		canvas = new R_VehicleLoadView(func_id);
    	}
    	else if(view_id.compareTo("R_TRUNK_MOVEMENT_PREVIEW") == 0){  //干线流向货量预览表
    		canvas = new R_TrunkMovementPreview(func_id);
    	}
    	else if(view_id.compareTo("R_KPI_UNLOAD_RATE") == 0){  //准时到货率
    		canvas = new R_KPI_UNLOAD_RATE(func_id);
    	}
    	else if(view_id.compareTo("R_KPI_POD_RATE") == 0){  //准时回单率
    		canvas = new R_KPI_POD_RATE(func_id);
    	}
    	else if(view_id.compareTo("KPI_LOADWH_RATE") == 0){
    		canvas =new R_KPI_LOADWH_RATE(func_id);
    	}
    	else if(view_id.compareTo("R_JOB_OPER_ITEM") == 0){
    		canvas = new R_JOB_OPER_ITEM(func_id);
    	}
    	else if(view_id.compareTo("R_JOB_TRANS_TRACK") == 0){
    		canvas = new R_JOB_TRANS_TRACK(func_id);
    	}
    	else if(view_id.compareTo("F_SUPLR_FEE_SETT") == 0){
    		canvas = new SuplrFeeSettView(func_id);
    	}
    	else if(view_id.compareTo("F_BUSS_FEE_SETT") == 0){
    		canvas = new CustomFeeSettView(func_id);
    	}
    	else if(view_id.compareTo("R_SKU_TRANS_ANAY") == 0){
    		canvas = new R_SKU_TRANS_ANAY(func_id);
    	}
    	else if(view_id.compareTo("R_TRANS_PAY_SUM") == 0){
    		canvas = new R_TRANS_PAY_SUM(func_id);
    	}
    	else if(view_id.compareTo("R_ARE_CUS_ANAY") == 0){
    		canvas = new R_ARE_CUS_ANAY(func_id);
    	}
    	else if(view_id.compareTo("T_TRANS_AB") == 0){
    		canvas = new R_TRANS_AB(func_id);
    	}
    	else if(view_id.compareTo("PRINT_CONTROL") == 0){
    		canvas = new PrintGrantView(func_id);
    	}
    	else if(view_id.compareTo("T_TRANS_POSITION") == 0){
    		canvas = new PositionLogView(func_id);
    	}
    	else if(view_id.compareTo("T_GPS") == 0){
    		canvas = new GPSManagerView(func_id);
    	}
    	else if(view_id.compareTo("R_SORTING_WH") == 0){
    		canvas = new R_SORTING_WH(func_id);
    	}
    	else if(view_id.compareTo("SETT_RATE") == 0){  //供应商计费协议
    		canvas = new SuplrRateManagerView(func_id);
    	}
    	else if(view_id.compareTo("CUSTOM_SETT_RATE") == 0){  //客户计费协议
    		canvas = new RateManagerView(func_id);
    	}
    	else if(view_id.compareTo("SETT_RATE_REC") == 0){
    		canvas = new RateManagerRecView(func_id);
    	}
    	else if(view_id.compareTo("SETT_POLE") == 0){
    		canvas = new PoleManagerView(func_id);
    	}
    	else if(view_id.compareTo("R_CUSTOMER_ORD") == 0){  //客户订单报表
    		canvas =new R_CustomerOrderView(func_id);
    	}
		else if(view_id.compareTo("R_CUSTOM_REC") == 0) {  //客户结算报表
			canvas = new R_CustomRecView(func_id);
		}
		else if(view_id.compareTo("R_CUSTOM_PAY") == 0) {  //成本结算报表
			canvas = new R_CustomPayView(func_id);
		}
		else if(view_id.compareTo("R_CUSTOM_GROUP") == 0) {  //订单组结算报表
			canvas = new R_CustomGroupView(func_id);
		}
    	else if(view_id.compareTo("R_UNLOADRATE") == 0){
    		canvas =new R_UnloadRateView(func_id);
    	}
    	else if(view_id.compareTo("TMP_CUSTOMER_ACC") == 0){
    		canvas =new TMP_CUSTOMER_ACC(func_id);
    	}
    	else if(view_id.compareTo("R_REC_VIEW")==0){
    		canvas=new R_RecView(func_id);
    	}
    	else if(view_id.compareTo("R_SUP_VIEW")==0){
    		canvas=new R_SupplierView(func_id);
    	}
    	else if(view_id.compareTo("F_CUSTOM_FEE_SETT") == 0){
    		canvas = new CustomFeeSettView(func_id);
    	}
    	else if(view_id.compareTo("SMS_MODEL") == 0){
    		canvas = new SmsModelView(func_id);
    	}
    	else if(view_id.compareTo("DISPATCH_RULE") == 0) {  //yuanlei 2012-06-11 配载规则
    		canvas = new DispatchRuleView(func_id);
    	}
    	else if(view_id.compareTo("T_VEH_REPT") == 0) {    //yuanlei 2012-06-11 车辆车型上报
    		canvas = new VehicleReportView(func_id);
    	}
    	else if(view_id.compareTo("T_DISPATCH_LOG") == 0) {    //yuanlei 2012-06-11 配载结果查询
    		canvas = new DispatchLogView(func_id);
    	}
    	else if(view_id.compareTo("F_SETTLE") == 0){   //fanglm 2012-06-13 结算单管理
    		canvas = new SettlementView(func_id);
    	}
    	else if(view_id.compareTo("F_SETTLE_REC") == 0){   //fanglm 2012-06-13 结算单管理
    		canvas = new SettlementRecView(func_id);
    	}
    	else if(view_id.compareTo("F_INVOICE") == 0){   //fanglm 2012-06-13 结算单管理
    		canvas = new InvoiceView(func_id);
    	}
    	else if(view_id.compareTo("NODE_RULE") == 0){   //yuanlei 2012-06-26 节点规则
    		canvas = new NodeRuleView(func_id);
    	}
    	else if(view_id.compareTo("T_FEE_WARNING") == 0){
    		canvas = new FeeWarnManagerView(func_id);
    	}
    	else if(view_id.compareTo("T_WARN_MANAGER") == 0){
    		canvas = new WarnManagerView(func_id);
    	}
    	else if(view_id.compareTo("GRAPH_DISPATCH") == 0){   //yuanlei 2012-07-11 图形化调度
    		canvas = new GraphDispatchView(func_id);
    	}
    	else if (view_id.compareTo("T_BARCODE") == 0){ //fanglm 2012-07-24 条码管理
    		canvas = new TmsBarcodeView(func_id);
    	}
    	else if(view_id.compareTo("R_CUSTOM_REP") == 0){
    		canvas = new Custom_Report(func_id);
    	}
    	else if (view_id.compareTo("BAS_TARIFF") == 0){
    		canvas = new BasTariffView(func_id);
    	}
    	else if (view_id.compareTo("TRANSACTION_LOG") == 0){ //业务日志查询
    		canvas = new TransactionLogView(func_id);
    	}
    	else if (view_id.compareTo("DIS_VIEW")==0){//配载视图
    		canvas=new DispatchWinView(func_id);
    	}
    	else if(view_id.compareTo("SQL_EXECUTE") == 0){
    		canvas = new SQLExecuteView(func_id);
    	}
    	else if(view_id.compareTo("LSCM") == 0){
    		canvas = new LSCMLogView(func_id);
    	}
    	else if(view_id.compareTo("SF_BIZLOG") == 0){
    		canvas = new SFBizLogView(func_id);
    	}
    	else if(view_id.compareTo("ORDER_CLOSE") == 0){
    		canvas = new OrderCloseView(func_id);
    	}else if(view_id.compareTo("CUSTOM_MONTHLY") == 0) {  //客户月结
			canvas = new CustomMonthlyView(func_id);
		}
    	else if(view_id.compareTo("SLF_CONTROL")==0){
			canvas=new TmsSelfControl(func_id);
		}else if(view_id.compareTo("F_WARN") == 0){ //费用警告信息
			canvas=new FeeWarnView(func_id);
		}
		else if(view_id.compareTo("SUPREMO_VIEW") == 0){ //总载视图
			canvas=new TmsSupremoView(func_id);
		}
		else if(view_id.compareTo("TB_ADDR_DIST") == 0){ //地址点间距离
			canvas=new BasAddressDistView(func_id);
		}
		else if(view_id.compareTo("F_SHOP_ROUTE_MANIFEST") == 0){ //店配线路结算清单
			canvas=new ShopRouteManifestView(func_id);
		}else if(view_id.compareTo("CHANGE_RDC")==0){  //转仓
			canvas = new ChangeRDCView(func_id);
		}
		else if (view_id.compareTo("R_ODR_KPI") == 0) {
			canvas = new R_OdrKPIView(func_id);
		}
		else if (view_id.compareTo("R_PICKUP") == 0) {
			canvas = new R_PickupShpm(func_id);
		}
		else if (view_id.compareTo("R_DELIVER") == 0) {
			canvas = new R_DeliverShpm(func_id);
		}else if (view_id.compareTo("TB_TEMPEQ") == 0) {//温控设备
			canvas = new BasTempEqView(func_id);
		}
		else if(view_id.compareTo("TB_GPSEQ") == 0){ //GPS设备管理
			canvas = new BasGpsEqView(func_id);
		}
		else if(view_id.compareTo("TB_INS_PURCHASE") == 0){ //保险购买记录
			canvas = new InsPurchaseRecordView(func_id);
		}
		else if(view_id.compareTo("TB_OIL_FUEL") == 0){ //加油记录
			canvas = new OilFuelView(func_id);
		}
		else if(view_id.compareTo("TB_RECE_ACCOUNT") == 0){ //加油记录
			canvas = new BasReceAccountView(func_id);
		}
		else if(view_id.compareTo("ACCIDENT_MANAGE") == 0){ //事故管理
			canvas = new AccidentManagerView(func_id);
		}
		else if(view_id.compareTo("TMS_VEH_LOAD") == 0){ //
			canvas = new VehicleDispatchView(func_id);
		}
		else if(view_id.compareTo("TMS_GPS_RECLAIM") == 0){ //
			canvas = new GpsReclaimView(func_id);
		}
		else if(view_id.compareTo("TMS_APPROVE_SET") == 0){ //审批设置
			canvas = new ApproveSetView(func_id);
		}
		else if(view_id.compareTo("TMS_CHANGE_RECORD") == 0){ //换车记录
			canvas = new ChangeRecordView(func_id);
		}
		else if(view_id.compareTo("TMS_CLAIM_APPROVE") == 0){ //
			canvas = new ClaimApproveView(func_id);
		}
		else if(view_id.compareTo("TMS_COMPLAINT") == 0){ //投诉管理
			canvas = new ComplaintView(func_id);
		}
		else if(view_id.compareTo("TMS_VEH_CHECK") == 0){ //车辆检查
			canvas = new VehicleCheckView(func_id);
		}
		else if(view_id.compareTo("VEH_VERIFY") == 0){ //年审管理
			canvas = new VehVerifyView(func_id);
		}
		else if(view_id.compareTo("TMS_APPOINT") == 0){ //到货预约
			canvas = new AppointmentView(func_id);
		}
		else if(view_id.compareTo("VEH_REPAIR") == 0){ //维修保养管理
			canvas = new VehRepairView(func_id);
		}
		else if(view_id.compareTo("TMS_PAYMENT") == 0){ //请款单管理
			canvas = new BillPaymentView(func_id);
		}
		else if(view_id.compareTo("TMS_ADJUST") == 0){ //维修保养管理
			canvas = new BillAdjustView(func_id);
		}
		else if(view_id.compareTo("TMS_RECADJUST") == 0){ //应收调整单
			canvas = new BillRecjustView(func_id);
		}
		else if(view_id.compareTo("BILL_PAY_DEDUCT") == 0){ //应付扣款单
			canvas = new PayDeductView(func_id);
		}
		else if(view_id.compareTo("BILL_PAY_ALLOWANCE") == 0){ //应付补贴单
			canvas = new PayAllowanceView(func_id);
		}
		else if(view_id.compareTo("BILL_REC_ALLOWANCE") == 0){ //应收补贴单
			canvas = new RecAllowanceView(func_id);
		}
		else if(view_id.compareTo("BILL_REC_DEDUCT") == 0){ //应付扣款单
			canvas = new RecDeductView(func_id);
		}
		else if(view_id.compareTo("BILL_PAY_INIT") == 0){ //应付期初账单
			canvas = new PayInitBillView(func_id);
		}
		else if(view_id.compareTo("BILL_REC_INIT") == 0){ //费用管理---结算管理-应时期初账单
			canvas = new RecInitBillView(func_id);
		}
		else if(view_id.compareTo("BILL_PAY_ADJUST") == 0){ //应付调整账单
			canvas = new PayAdjBillView(func_id);
		}
		else if(view_id.compareTo("BILL_REC_ADJUST") == 0){ //费用管理---结算管理-应收调整账单
			canvas = new RecAdjBillView(func_id);
		}
		else if(view_id.compareTo("BILL_PAY_ADJAUDIT") == 0){ //费用管理-结算管理-待审应付调整单
			canvas = new PayAdjAuditView(func_id);
		}
		else if(view_id.compareTo("BILL_REC_ADJAUDIT") == 0){ //费用管理-结算管理-待审应收调整单
			canvas = new RecAdjBillCheckView(func_id);
		}
		else if(view_id.compareTo("BILL_PAY_REQUEST") == 0){ //费用管理-结算管理-应付请款单
			canvas = new PayReqBillView(func_id);
		}
		else if(view_id.compareTo("BILL_PAY_REQAUDIT") == 0){ //费用管理-结算管理-待审应付请款单
			canvas = new PayReqAuditView(func_id);
		}
		else if(view_id.compareTo("BILL_REC_INVOICEAUDIT") == 0){ //费用管理-结算管理-待审批开票申请
			canvas = new RecAuditInvoiceView(func_id);
		}
		return canvas;
	}*/

	/*private void createMenu(final HLayout menu_stack) {

		Util.async.getFuncList(new AsyncCallback<List<FUNCTION>>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError("获取菜单信息失败：" + caught.getMessage());
			}

			@Override
			public void onSuccess(List<FUNCTION> result) {
				if (result != null) {
					Menu menu = null;
					Menu sub_menu = null,sec_menu = null;  //第一级和第二级子菜单
					String parent_func_id = "";
					//获取菜单列表，目前只支持3级子菜单（运输管理->A->B->C）
					int k = 0;
					for (int i = 0; i < result.size(); i++) {
						FUNCTION func = (FUNCTION) result.get(i);
						if(func.getPARENT_FUNCTION_ID().compareTo("P") == 0 || ObjUtil.ifNull(func.getFUNCTION_FORMNAME(),"").length() == 0) {   //菜单							
							if(func.getPARENT_FUNCTION_ID().compareTo("0") != 0) {
								if(func.getPARENT_FUNCTION_ID().compareTo("P") == 0) {
									menu = new Menu();
									menu.setWidth(111);
									menu.setShowShadow(false);
									menu.setShadowDepth(1);
									
									IMenuButton btn = new IMenuButton(func.getFUNCTION_NAME(), menu);
									btn.setWidth(111);
									menu_stack.addMember(btn);
									sub_menu = null;
									sec_menu = null;
									parent_func_id = "";
									k++;
								}
								else {
									if(sub_menu != null && func.getPARENT_FUNCTION_ID().equals(parent_func_id)) {
										sec_menu = new Menu();
										MenuItem newItem = new MenuItem(func.getFUNCTION_NAME());
										newItem.setSubmenu(sec_menu);
										sub_menu.addItem(newItem);
									}
									else {
										sub_menu = new Menu();
										sub_menu.setWidth(111);
										MenuItem newItem = new MenuItem(func.getFUNCTION_NAME());
										newItem.setSubmenu(sub_menu);
										menu.addItem(newItem);
										parent_func_id = func.getFUNCTION_ID();
									}
								}
							}
						} 
						else {  //功能
							final String panelID = func.getFUNCTION_FORMNAME();
							final String function = func.getFUNCTION_NAME();
							final String funcID = func.getFUNCTION_ID();
							MenuItem newItem = new MenuItem(function);
							newItem.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(MenuItemClickEvent event) {
									Tab tab = null;
									if(panelID == null)
										return;
									String tabID = panelID;
									tab = mainTabSet.getTab(tabID);
									if(panelID.compareTo("TB_CHANGORG")==0){
										new ChangeOrganizationWin(getThis()).getViewPanel();
										return ;
									}
									if(panelID.compareTo("TB_ONLINERUSER")==0){
										new OnlineUserView().getViewPanel();
										return;
							    	}
									if(panelID.compareTo("S_MODIFY_PWD")==0){
										new PasswdChangeView().getViewPanel();
										return;//2010-2-16 wangjun
							    	}
									if(panelID.compareTo("WMS_CHANGE") == 0){
										new SwitchOrgView(getThis()).getViewPanel();
										return;
									}
									//fanglm 2011-2-25 手动EDI
									if(panelID.compareTo("INTER_USER_ALERT") == 0){
										new InterUserCtrolView().getViewPanel();
										return;
									}
									if(panelID.compareTo("RELOGO") == 0) {
										doForword("/tms/initDataServlet?ds_id=relogo");
										return;
									}
									if(panelID.compareTo("LOGOUT") == 0) {
										doForword("/tms/initDataServlet?ds_id=logout");
										return;
									}
									if(panelID.compareTo("TMS_LOAD_JOB_SCREE")==0){
										new ScreenWin().getViewPanel1();
										return;
									}
									if (tab == null) {
										Canvas panel = createView(panelID,funcID);
//										Util.insertLog(StaticRef.ACT_OPEN + "【" + function + "】", StaticRef.ACT_SUCCESS, "");
										if (panel == null) {
											panel = new Canvas();
										}
										tab = new Tab();
										tab.setID(panelID);
										tab.setAttribute("historyToken", panelID);

										//String icon = "rd/form.png";
										//String imgHTML = Canvas.imgHTML(icon,
										//		16, 16);
										tab.setTitle("<span>" + function
												+ "</span>");
										tab.setPane(panel);
										tab.setCanClose(true);
										mainTabSet.addTab(tab);
										mainTabSet.selectTab(tab);
									} else {
										mainTabSet.selectTab(tab);
									}
									userLab.setContents("当前位置:<span style='font-size:12px;color:red;'>" + tab.getTitle() + "</span>&nbsp&nbsp");
									History.newItem(panelID, false);
									mainTabSet.addTabSelectedHandler(new TabSelectedHandler() {
										
										@Override
										public void onTabSelected(TabSelectedEvent event) {
											userLab.setContents("当前位置:<span style='font-size:12px;color:red;'>" + event.getTab().getTitle() + "</span>&nbsp&nbsp");
										}
									});
								}
							});
							if(sub_menu != null) {
								if(!ObjUtil.isNotNull(parent_func_id)) {
									parent_func_id = func.getPARENT_FUNCTION_ID();
								}
								if(parent_func_id.equals(func.getPARENT_FUNCTION_ID())) {
									sub_menu.addItem(newItem);
								}
								else {
									if(sec_menu != null) {
										sec_menu.addItem(newItem);
									}
									else {
										menu.addItem(newItem);
										sec_menu = null;
										sub_menu = null;
										parent_func_id = "";
									}
								}
							}
							else {
								menu.addItem(newItem);
								sec_menu = null;
								sub_menu = null;
							}
						}
					}
				}
			}
		});
	}*/
	
	private void addKeyPress(VLayout main){
		main.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.isCtrlKeyDown() && 
						event.isAltKeyDown() &&
						"S".equalsIgnoreCase(event.getKeyName()) &&
						event.getX() < 10 && event.getY() < 10){
					String tabID = "SQL_EXECUTE";
					Canvas panel = new SQLExecuteView();
					Tab tab = mainTabSet.getTab(tabID);
					if(tab == null){
						tab = new Tab();
					}else{
						return;
					}
					tab.setID(tabID);
					tab.setAttribute("historyToken", tabID);

					//String icon = "rd/form.png";
					//String imgHTML = Canvas.imgHTML(icon,
					//		16, 16);
					tab.setTitle("<span>SQL EXECUTE</span>");
					tab.setPane(panel);
					tab.setCanClose(true);
					mainTabSet.addTab(tab);
					mainTabSet.selectTab(tab);
				}
			}
		});
	}
	
	public MainForm getThis(){
		return this;
	}
	
	/*public TabSet getTabSet() {
		return mainTabSet;
	}*/
	public native static void doForword(String str)/*-{
		$wnd.location.href=str;
	}-*/;
	
    protected void showSample(TreeNode node) {
        boolean isExplorerTreeNode = node instanceof ExplorerTreeNode;
        if (isExplorerTreeNode) {
            ExplorerTreeNode explorerTreeNode = (ExplorerTreeNode) node;
            PanelFactory factory = explorerTreeNode.getFactory();
            if (factory != null) {
                String panelID = factory.getCanvasID();
                Tab tab = null;
                if (panelID != null) {
                    String tabID = panelID + "_tab";
                    tab = mainTabSet.getTab(tabID);
                }
                if (tab == null) {
                    Canvas panel = factory.createCanvas(explorerTreeNode.getNodeID(),mainTabSet);
                    tab = new Tab();
                    tab.setID(factory.getCanvasID() + "_tab");
                    //store history token on tab so that when an already open is selected, one can retrieve the
                    //history token and update the URL
                    tab.setAttribute("historyToken", explorerTreeNode.getNodeID());
                    tab.setContextMenu(contextMenu);

                    String sampleName = explorerTreeNode.getName();

                    String icon = explorerTreeNode.getIcon();
                    if (icon == null) {
                        icon = "silk/plugin.png";
                    }
                    String imgHTML = Canvas.imgHTML(icon, 16, 16);
                    tab.setTitle("<span>" + imgHTML + "&nbsp;" + sampleName + "</span>");
                    tab.setPane(panel);
                    tab.setCanClose(true);
                    mainTabSet.addTab(tab);
                    mainTabSet.selectTab(tab);
                } else {
                    mainTabSet.selectTab(tab);
                }
            }
        }
    }
    
    private Menu createContextMenu() {
        Menu menu = new Menu();
        menu.setWidth(140);

        MenuItemIfFunction enableCondition = new MenuItemIfFunction() {
            public boolean execute(Canvas target, Menu menu, MenuItem item) {
                int selectedTab = mainTabSet.getSelectedTabNumber();
                return selectedTab != 0;
            }
        };

        MenuItem closeItem = new MenuItem("<u>C</u>lose");
        closeItem.setEnableIfCondition(enableCondition);
        closeItem.setKeyTitle("Alt+C");
        KeyIdentifier closeKey = new KeyIdentifier();
        closeKey.setAltKey(true);
        closeKey.setKeyName("C");
        closeItem.setKeys(closeKey);
        closeItem.addClickHandler(new ClickHandler() {
            public void onClick(MenuItemClickEvent event) {
                int selectedTab = mainTabSet.getSelectedTabNumber();
                mainTabSet.removeTab(selectedTab);
                mainTabSet.selectTab(selectedTab - 1);
            }
        });

        MenuItem closeAllButCurrent = new MenuItem("Close All But Current");
        closeAllButCurrent.setEnableIfCondition(enableCondition);
        closeAllButCurrent.addClickHandler(new ClickHandler() {
            public void onClick(MenuItemClickEvent event) {
                int selected = mainTabSet.getSelectedTabNumber();
                Tab[] tabs = mainTabSet.getTabs();
                int[] tabsToRemove = new int[tabs.length - 2];
                int cnt = 0;
                for (int i = 1; i < tabs.length; i++) {
                    if (i != selected) {
                        tabsToRemove[cnt] = i;
                        cnt++;
                    }
                }
                mainTabSet.removeTabs(tabsToRemove);
            }
        });

        MenuItem closeAll = new MenuItem("Close All");
        closeAll.setEnableIfCondition(enableCondition);
        closeAll.addClickHandler(new ClickHandler() {
            public void onClick(MenuItemClickEvent event) {
                Tab[] tabs = mainTabSet.getTabs();
                int[] tabsToRemove = new int[tabs.length - 1];

                for (int i = 1; i < tabs.length; i++) {
                    tabsToRemove[i - 1] = i;
                }
                mainTabSet.removeTabs(tabsToRemove);
                mainTabSet.selectTab(0);
            }
        });

        menu.setItems(closeItem, closeAllButCurrent, closeAll);
        return menu;
    }
    
    public void onHistoryChanged(String historyToken) {
        if (historyToken == null || historyToken.equals("")) {
            mainTabSet.selectTab(0);
        } else {
            ExplorerTreeNode[] showcaseData = sideNav.getShowcaseData();
            if(showcaseData != null){
	            for (ExplorerTreeNode explorerTreeNode : showcaseData) {
	                if (explorerTreeNode.getNodeID().equals(historyToken)) {
	                    showSample(explorerTreeNode);
	                    sideNav.selectRecord(explorerTreeNode);
	                    Tree tree = sideNav.getData();
	                    TreeNode categoryNode = tree.getParent(explorerTreeNode);
	                    while (categoryNode != null && !"/".equals(tree.getName(categoryNode))) {
	                        tree.openFolder(categoryNode);
	                        categoryNode = tree.getParent(categoryNode);
	                    }
	                }
	            }
            }
        }
    }
}
