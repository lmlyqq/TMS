package com.rd.client.prop;

import com.google.gwt.i18n.client.Messages;

public interface I18N_TIL extends Messages{
	String USER(); 
	String PWD();
	String FUZZYQRY();
	
	String SEARCHINFO();//search info---?
	String LISTINFO();  //list info--- 
	String MAININFO();  //main info 
	String BASEINFO();  
	String OTHERINFO(); //other info--
	String LOADINFO();
	String UNDISPATCHORDER();
	String UNLOADINFO();
	String BY_ODER();
	String BY_VEHICLE();
	
	String PRIV_MANAGER();  //权限管理
	String INTER_MANAGER();//接口管理
	String SYS_MANAGER();//系统管理
	String BAS_MANAGER();//基础资料
	String ORDER_MANAGER();//订单管理
	String TRANS_MANAGER();//运输管理
	String WAREHOUSE_MANAGER();//仓库管理
	String CUST_SERVICE();//客户服务
	String FINAN_MANAGER();//财务管理
	String REPORT_MANAGER();//报表
	String BUSSINESS_MANAGER();//业务规则
//	
	String USER_ID();
	String USER_NAME();
	String USER_PWD();
	String USER_TEL();
	String USER_GROUP();
	String USER_EMAIL();
	String USER_DESCR();
	String USER_UDF1();// user_udf---
	String USER_UDF2();// user_udf---
	String USER_ORG_ID();//user_org_id---
	String ACTIVE_FLAG();//active_flag--
	String MODIFY_FLAG();//modify_flag--
	String SHOW_SEQ();  //show_seq--
	String CUR_STATUS();//cur_status--
	String IP_ADDR();
	String CHANGEORG();
	String CHANGEWMS();
	String LOGIN_TIME();
	
	String ROLE_ID();
	String ROLE_NAME();
	String ADDTIME();
	String ADDWHO();
	String EDITTIME();
	String EDITWHO();
	
	String SUBSYSTEM_TYPE();
	String CLIENT_ID();//client_id
	
	String CFG_PARAM();//cfg_param
	String CFG_INT();
	String CFG_STRING();
	String CFG_DESCR();
	String CFG_TYPE();
	String CFG_MODEL();
	String CFG_UDF1();
	String CFG_UDF2();
	
	//数据字典(BAS_CODEPROP)
	/**
	 * @param
	 */
	String PROP_CODE();//
	String PROP_NAMEC();
	String PROP_NAMEE();
	String BIZ_TYPE();
	String PARENT_PROP_ID();
	String PROP_UDF1();
	String PROP_UDF2();
	String ENABLE_FLAG();
	
	//数据字典(BAS_CODES)
	String CODE();
	String CODE_NAMEC();
	String CODE_NAMEE();
	String CODE_UDF1();
	String CODE_UDF2();
	
	//列表配置(SYS_LIST_CONFIG,SYS_USER_LIST)
	String LIST_VIEW_NAME();
	String LIST_COLUMN_FIELD();
	String LIST_COLUMN_CNAME();
	String LIST_COLUMN_I18NAME();
	String LIST_COLUMN_WIDTH();
	String LIST_SHOW_FLAG();
	String LIST_FUNC_MODEL();
	String LIST_UGRP_ID();
	String LIST_SHOW_SEQ();
	
	//短信配置
	String SMS_MODEL();
	String SMS_CONTENT();
	
	//行政区划(BAS_AREA)
	String AREA_CODE();
	String AREA_CNAME();
	String AREA_ENAME();
	String PARENT_AREA_ID();
	String AREA_TYPE();
	String AREA_LEVEL();
	String NOTES();
	
	
	//组织机构(BAS_ORG)
	String ORG_CNAME();
	String ORG_ENAME();
	String SHORT_NAME();
	String PARENT_ORG_NAME();
	String ORG_TYPE();
	String ORG_CARRIER();
	String AREA_ID();
	String ORG_COMPANY();
	String ORG_LEVEL();
	String SEPACCOUNT_FLAG();
	String CORPORG_FLAG();
	String TRANSORG_FLAG();
	String WAREHOUSE_FLAG();
	String ORG_ADDRESS();
	String ORG_ZIP();
	String ORG_LEADER();
	String ORG_CONTACTER();
	String ORG_TEL();
	String ORG_FAX();
	String ORG_EMAIL();
	String ORG_WEBSITE();
	String ORG_NOTES();
	String HINT_CODE();
	String DIRECT_FLAG();
	String ORG_REPORT_TIL();
	String ORG_TAX_NO();
	
	//度量衡(BAS_MSRMNT,BAS_MSRMNT_UNIT)
	String MSRMNT();
	String MSRMNT_NAME();
	String UNIT();
	String UNIT_NAME();
	String CONVERT_SCALE();
	String MSRMNT_UDF1();
	String MSRMNT_UDF2();
	String BASUNIT_FLAG();
	String TRANS_UOM_FLAG();
	
	//产品类别(BAS_SKU_CLS)
	String SKU_CLS();
	String CUSTOMER_ID();
	String DESCRC();
	String DESCRE();
	String FACTOR();
	String ISMIX();
	String SKU_CLS_UDF1();
	String SKU_CLS_UDF2();
	String SKU_CLS_UDF3();
	String SKU_CLS_UDF4();
	String SKU_CLS_UDF5();
	String RATION();//系数
	
	//包装(BAS_PACKAGE)
	String EXTRAINFO();
	String PACK();
	String PACK_DESCR();
	String PACK_QTY();
	String PACK_MATERIAL();
	String CARTONIZE();
	String IN_LABEL();
	String OUT_LABEL();
	String PACK_EACH();
	String PACK_IP();
	String PACK_CS();
	String PACK_PL();
	String PACK_OTHER();
	String PACK_LEN();
	String PACK_WIDTH();
	String PACK_HEIGHT();
	String PACK_VOLUME();
	String PACK_WEIGHT();
	String PACK_TIHI();
	
	//服务范围(BAS_RANGE)
	String RANGE_NAME();
	String FROM_PROVINCE_NAME();
	String FROM_AREA_NAME();
	String REQ_HOURS();
	
	//货品(BAS_SKU)
	String SKU();
	String SKU_CNAME();
//	String SKU_ENAME();
	String SKU_SPEC();
	String TEMPERATURE();
	String PACK_ID();
	String TRANS_UOM();
	String LOTID();
	String VOLUME_UNIT();
	String WEIGHT_UNIT();
	String LENGHT_UNIT();
	String LENGHT();
	String WIDTH();
	String HEIGHT();
	String VOLUME();
	String GROSSWEIGHT();
	String NETWEIGHT();
	String TARE();
	String PRICE();
	String DENSITY();
	String SKU_ATTR();
	String TRANS_COND();
	String MIX_FLAG();
	String TEMPERATURE1();
	String TEMPERATURE2();
	String TRANS_INFO();
	String WMS_INFO();
	String IMG_INFO();
	String COMMON_FLAG();
	String WORTH();
	
	//地址点(BAS_ADDRESS)
	String ADDR_CODE();
	String ADDR_NAME();
	String ADDR_AREA_ID();
	String ADDRESS();
	String CONT_NAME();
	String CONT_TEL();
	String LOAD_FLAG();
	String RECV_FLAG();
	String TRANSFER_FLAG();
	String CUSTOMER_NAME();
	String ADDR_TYP();
	String EXEC_ORG_ID();
	String GRADE();
	String REGION_ID();
	String WHSE_ID();
	String CREATE_ORG_ID();
	String CONT_FAX();
	String CONT_DEP();
	String CONT_EMAIL();
	String ZIP();
	String URL();
	String LONGITUDE();
	String LATITUDE();
	String TM_ZN_OFST();
	String ADDR_GRP1();
	String CONTROL_INFO();
	String SFADDR_INFO();
	String START_OPER_TIME();
	String END_OPER_TIME();
	String LOAD_PRIOR();
	String UNLOAD_PRIOR();
	String TRANSFER_ORG_ID();
	String MAX_EQMT_HGHT();
	String MAX_EQMT_LEN();
	String MAX_EQMT_WDTH();
	String SELFHAND_FLAG();
	String COD_FLAG();
	String ADDR_MIX_FLAG();
	String RQAPPT_FLAG();
	String SELF_PKUP_FLAG();
	String SELF_DLVR_FLAG();
	String PROVINCE();
	String CITY();
	String AREA();
	String COUNTY();
	String BIZ_REGION();
	String LOAD_REGION();
	String UNLOAD_REGION();
	String LEVEL1_CODE();
	String LEVEL2_CODE();
	String LEVEL3_CODE();
	
	//客户(BAS_CUSTOMER)
	String CONTRACT_INFO();
	String CUSTOMER_CODE();
	String CUSTOMER_CNAME();
	String CUSTOMER_ENAME();
	String CUSTOMER_FLAG();
	String PAYER_FLAG();
	String TRANSPORT_FLAG();
	String C_WAREHOUSE_FLAG();
	String BILL_TO();
	String INDUSTRY();
	String PROPERTY();
	String C_GRADE();
	String C_ADDRESS();
	String AREA_ID_NAME();
	String MAINT_ORG_ID();
	String PARENT_CUSTOMER_ID();
	String CONTACTER_FLAG();
	String INVOICE_FLAG();
	String BANK();
	String TAXNO();
	String INVOICE_TITLE();
	String SETT_TYP();
//	String PAY_TYP();
//	String SETT_RUL();
	String SETT_CYC();
//	String CREDIT_LIMIT();
	String CURRENCY();
//	String AR_DEADLINE();
	String FOLLOWUP();
	String DFT_SUPLR_ID();
	String LOAD_ID();
	String UNLOAD_ID();
	String C_PACK_ID();
	String MATCHROUTE_FLAG();
	String ADDR_EDIT_FLAG();
	String UNIQ_CONO_FLAG();
	String UNIQ_ADDR_FLAG();
	String DFT_SKU_FLAG();
	String SKU_EDIT_FLAG();
	String SLF_DELIVER_FLAG();
	String SLF_PICKUP_FLAG();
	String POD_FLAG();
	String REP_TIL();
	String GEN_METHOD();
	String CUST_UDF1();
	String CUST_UDF2();
	String CUST_UDF3();
	String CUST_UDF4();
	String ORG_ID();
	String ORG_ID_NAME();
	String DEFAULT_FLAG();
	String SERVICE_ID();
	String SERVICE_NAME();
	String ORD_TYP();
	String ORD_NAME();
	String C_ORG_ID();
	String C_ORG_FLAG();
	String C_RDC_FLAG();
	String CUSTOM_ATTR();
	String TAX_RATE();
	String SELL_BELONG_TO();
	String SELL_AREA_CODE();
	
	//供应商管理
	String SUP_SUPLR_CODE();//供应商代码
	String SUP_SUPLR_CNAME();//供应商名称
	String SUP_SUPLR_ENAME();//英文名称
	String SUP_SUPLR_TYP();//供应商类别
	String SUP_PROPERTY();//供应商性质
	String SUP_GRADE();//供应商等级
	String SUP_WHSE_ID();//隶属仓库
	String BAS_INFO();//基础信息
	String SUP_BILL_TO();//结算方
	String SUP_TRANSPORT_FLAG();//运输业务
	String SUP_WAREHOUSE_FLAG();//仓库业务员
	String CONTACT_INFO();//联系信息
	String ACCOUNT_INFO();//结算信息
	String SUP_AP_DEADLINE();//应付帐期
	String CONTRO_INFO();//控制信息
	String ACTUATOR_ASSIGN();//执行机构分配
	String TRANSPORT_SERVICE();//运输服务
	String EQMT_NUM();//可用运力数量
	String VEHICLE_FOR_FLAG();//需要车辆预报
	String INS_FLAG();//保险
	String INS_AMT();//保险金额
	String INS_EFCT_DT();//保险生效日期
	String INS_EXP_DT();//保险到期日期
	String SRVC_ID();//运输服务代码
    String INTL_FLAG();//内部供应商
    String ORG_NAME();//组织机构
    String SELECT_VEHICLE();//固定车辆运输

	//线路管理[BAS_ROUTE_HEADER/BAS_ROUTE_DETAIL]
	String ROUTE_CODE();
	String ROUTE_NAME();
	String START_ARAE();
	String END_AREA();
	String START_DATE();
	String END_DATE();
	String TOTL_DISTANCE();
	String OTD_TIME();
	String TIME_UNIT();	
	String ROUTE_AREA();
	String ROUTE_ADDR();
	String ROUTE_DISTENCE();
	String RUNTIME();
	String MAINWAY();
	String TOTAL_AMOUNT();
	String POINTS_FLAG();
	String POINTS_NUM();
	String BILL_LINE_FLAG();
	
	//车型管理【BAS_VEHICLE_TYPE】
	String ID();
	String VEHICLE_TYPE();//车辆类型
	String LENGTH_UNIT();//长度单位
	String MAX_WEIGHT();//额定载重
	String MAX_VOLUME();//额定体积
	String RATIO();//超额百分比
	String ENGINE_POWER();//发动机功率
	String FUEL();//发动机燃料
	String LENGTH();//长度
	String LENGTH1();//长（内径）
	String LENGTH2();//长（外径）
	String WIDTH2();//宽外径
	String HEIGHT2();//高外径
		//运力管理【BAS_VEHICLE】
	String PLATE_NO();//车牌号
	String VEHICLE_NO();//车辆编号
	String VECHILE_TYP_ID();//车辆类型
	String VEHICLE_STAT();//车辆状态
	String VEHICLE_ATTR();//车辆属性
	String SUPLR_ID();//隶属承运商
	String DRIVER1();//司机
	String MOBILE();//手机号
	String LOCATION();//当前位置
	String TRAILER_NO();//挂车号
    String SPEED();//当前时速
    String VEH_LOCK_REASON();//
    String REASON();//原因描述
    String TRAILER_FLAG();//挂车标识
    String BRAND();//品牌型号
    String ENGINE_NO();//发动机型号
    String BED_NO();//底盘号
    String FRAME_NO();//车架号
    String PRODUCT_DATE();//出厂日期
    String PCHD_DATE();//购买日期
    String PLATE_DATE();//上牌日期
    String CREATE_DATE();//建档日期
    String PCHD_PRICE();//购入价格
    String MONTH_DEPRECIATION();//月折旧金额
    String EOC();
    String HOC();
    String LICENSE_NO();
    String REG_NO();
    String TMP_TYP();
    String TMP_ATTR();//车辆温层
    String AVAIL_ATTR();
    String RECORD();
    String CLOSE_OPER_TIME();
    String BLACKLIST_FLAG();//黑名单
    String AVAIL_FLAG();//冻结
    String COLD_NO();//冷机型号
    String TRAIL_PLATE_NO();//挂车牌号
    String TRAIL_FRAME_NO();//挂车车架号
    String GRADE_EXPIRYDATE();//等级评定有效期
    
	//仓库管理[BAS_WAREHOUSE]
	String WHSE_CODE();
	String WHSE_NAME();
	String WHSE_ENAME();
	String WHSE_ATTR();
	String WHSE_TYP();
	String WHSE_CLS();
	String WHSE_TMP();
	String START_TIME();
	String END_TIME();
	String WHSE_BOSS();
	String WHSE_MANAGER();
	String WHSE_ADDRESS();
	
	//结算区域
	String CHARGE_REGION_NAME();//结算区域名称
	String CHARGE_REGION_ENAME();//英文名称
	String AREA_NAME();//行政区域名称
	String AREA_CUSTOMER_ID();//客户名称
	String UDF1();//预留字段1
	String UDF2();//预留字段2
	
	//BAS_STAFF
	String STAFF_CODE();//人员编号
	String STAFF_NAME();//姓名
	String STAFF_ENAME();//英文名称
	String SEX();//性别
	String BIRTHDAY();//出生日期
	String STAFF_TYP();//人员类别
	String DEP_ID();//所属部门
	String POSITION();//职位
	String EMPLOY_TIME();//入职时间
	String WORK_LIFE();//工作年限
	String EMAIL();//点子邮箱
	String PHOTO_DIR();//照片路径
	String ID_NO();//身份证号
	String DRVR_LIC_NUM();//驾驶证号
	String LIC_CLS();//驾照级别
	String LIC_EFCT_DT();//驾照生效期
    String LIC_EXPD_DT();//驾照失效期
    String OPER_LICENSE();//操作证号
    String BAS_ORG_ID();
    
    
   
	//运输服务
	String SRVC_NAME();//运输服务
	String SRVC_ENAME();//英文名称
	String FOR_CUSTOMER_FLAG();//客户服务
	String UDF3();//预留字段3
	String UDF4();//预留字段4
	String TRANS_TYPE();
	String STOP_CONTROL();
	
	
	//角色管理
	String ROLE_USER();
	String ROLE_USER_GROUP();
	String ROLE_USER_NAME();
	String ROLE_USER_GROUP_NAME();
	
	//用户组管理
	String UGROUP_CODE();
	String UGROUP_NAME_C();
	String UGROUP_NAME_E();
	String UGROUP_PROP_CODE();
	String USERGROP_ROLE();
	
	//运输订单
	String ORD_NO();
	String CUSTOM_ODR_NO();
	String TRANS_TYP();
	String TRANS_SRVC_ID();
	String ODR_TYP();
	String ODR_TIME();
	String LOAD_NAME();
	String LOAD_NAME_ID();
	String LOAD_AREA_ID();
	String LOAD_AREA_NAME();
	String LOAD_ADDRESS();
	String LOAD_CONTACT();
	String LOAD_TEL();
	String FROM_LOAD_TIME();
	String TO_LOAD_TIME();
	String UNLOAD_NAME();
	String UNLOAD_NAME_ID();
	String UNLOAD_AREA_ID();
	String UNLOAD_AREA_NAME();
	String UNLOAD_ADDRESS();
	String UNLOAD_CONTACT();
	String UNLOAD_TEL();
	String FROM_UNLOAD_TIME();
	String TO_UNLOAD_TIME();
	String POD_TIME();
	String TRANS_ODR_STAT();
	String PRE_ORDER_NO();
	String REFENENCE1();
	String REFENENCE2();
	String REFENENCE3();
	String UGRT_GRD();
	String SALES_MAN();
	String SETTLE_TYP();
	String BTCH_NUM();
	String ALLOW_SPLIT_FLAG();
	String LOAD_APT_FLAG();
	String UNLOAD_APT_FLAG();
	String GEN_ORDER_TYP();
	String TOT_QNTY();
	String TOT_GROSS_W();
	String TOT_VOL();
	String TOT_NET_W();
	String TOT_WORTH();
	String TOT_QNTY_EACH();
	String SKU_NAME();
	String DEFAULT_SKU_NAME();
	String ROUTE_ID();
	String ALERT_FLAG();
	String PRINT_FLAG();
	String VEHICLE_TYP();
	String ABNOMAL_STAT();
    String MODEL();
    String CODE_PACKAGE();
    String ORD_ID();
    String ORD_ROW();
    String START_END_MES();
    String OTHERS_MES();
    String ORDER_HISTORY();
    String BUS_DIARY();
    String EXP_DETAIL();
    String CAP_DEMAND();
    String PRO_DETAIL();
    String COMPLAINT();
    String COST_CATEGORY();
    String COST_ITEM();
    String CHARGE_ITEM();
    String REFERENCE_VALUE();
    String PLAN_PRICE();
    String ORDER_STATE();
    String OCCUR_TIME();
  	String OPERATE_PERSON();
  	String OPERATE_RECODE();
  	String OPERATE_TIME();
  	String ORDER_CODE();
  	String ORDER_TIME();
  	String COUNT_PRI_VALUE();
  	String RECEIVE_PRICE();
  	String ACTUAL_PRICE();
  	String COLLECTE_STATE();
  	String OREDER_GRO_ID();
  	String ORD_ADDTIME_FROM();
  	String ORDER_PER();
  	String ORDER_ORG();
	String BASE_MESSAGE();
	String FROM_PLAN_LOAD_TIME(); //
	String FROM_PLAN_UNLOAD_TIME();//
	String FROM_POD_TIME();
	String REQ_DELIVERY_TIME();
    String CUSTOMER();
    String ORD_PRO_LEVER();
    String ORD_POD_FLAG();//
    String ASSIGN_STAT();
    String PLAN_STAT();
    String LOAD_STAT();
    String UNLOAD_STAT();
    String ODR_TIME_FROM();
    String ICLUDE_CLOSE();
    String ORDER_STATE2();
    String ORD_ADDTIME();
    String ORD_PERSON();
    String ORD_TOT_QNTY();
    String ORD_PACK_ID();
    String ORD_PLAN_TIME();//
    String OP_LOAD_TIME();
    String SUPLR_NAME();
    String UOM();
    String LD_QNTY();
    String LD_VOL();
    String LD_GWGT();
    String UNLD_QNTY();
    String QNTY_EACH();
    String BIZ_TYP();
    String BIZ_CODE();
    String SHPM_UDF5();
    String SHPM_UDF6();
    String SHPM_UDF7();
    String SHPM_UDF8();
    String DISCOUNT();
    String BILL_FLAG();
    String BILL_PRICE();
    String GOODS_WORTH();
    String INDUSTRY_ATTR();
   
    //调度配载
    String SKU_ID();
    String LOAD_NO();
    String PRE_DEPART_TM();
    String SHPM_NO();
    String STATUS();
    String DEPART_TM_FROM();
    String UNLOAD_TIME_FROM();
    String PRE_UNLOAD_TIME_FROM();
    String SKU_UOM();
    String QNTY();
    String VOL();
    String G_WGT();
    String N_WGT();
    String UNLOAD_SEQ();
    String LOAD_TIME();
    String DISPATCH_STAT_NAME();
    String ASSIGN_TIME();
    String ASSIGN_TIME_FROM();//派发时间 从
    
    //费用种类
    String FEE_CODE();
    String FEE_ENAME();
    String TRANS_FEE_TYP();
    String FEE_ATTR();
    String DFT_AMT();
    String FEE_CODE_NAME();
  
    //运输跟踪
    String TRANS_FOLLOW();
    String TRANS_LOAD_LIST();
    String UNLOAD_RECIVE();
    String UNLOAD_TIME();
    String LOSDAM_FLAG();
    String ABNOMAL_NOTE();
    String TRACE_TIME();
    String CURRENT_LOC();
    String PRE_UNLOAD_TIME();
    String END_LOAD_TIME();
    String LOAD_UDF21();
    String LOAD_UDF22();
    String LOAD_UDF23();
    String LOAD_UDF24();
    String TRANS_AMOUNT();
    String LOSS_DAMAGE_TYP();
    String TRANS_QNTY();
    String TRANS_UOM_W();
    String ODR_QNTY();
    String TRANS_ODR_QNTY();
    String TRACER();
    String SOLUTION();
    String SOLVE_TIME();
    String UNLOAD_ORG_ID();//卸货机构
    String SHPM_QNTY();
    String PRE_SOLVE_TIME();//
    String FOLLOW_LD_QNTY();//发货数量
    String SHPM_ROW();
    String STATUS_FOLLOW();//调度单状态从
    String STATUS_TO();//调度单状态到
    String START_AREA_ID();//起点区域
    String DRIVER_SERVICE();
    String CUSTOMER_SERVICE();
    String UNLOAD_DELAY_DAYS();
    String POD_DELAY_DAYS();
    String INFORMATION();
    String LOAD_UDF1();
    String LOAD_UDF2();
    
    //回单管理
    String FOLLOW_POD_TIME();//实际回单时间
    String POD_DELAY_REASON();//回单延迟原因
    String UNLOAD_DELAY_REASON();//收获延迟原因
    String FOLLOW_UNLOAD_TIME();//实际收货时间
    String ORD_LIST();//同一车托运单
    String BACK_OR();//应回未回
    String PRE_LOAD_TIME();//计划发运时间
    String MANAGE_END_LOAD_TIME();//实际发运时间
    String TRANS_ORDER_LIST();//托运单列表
    String ORDER_OPERATION();//回单操作
    String OP_POD_TIME();//回单登记时间
    String CLOSE_REASON();//
    String LOSDAM_QNTY();
    
    //订单动态监控
    String TRACK_UNLOAD_FLAG();//未完成订单
    String TRACK_LOAD_FLAG();//销售未提
    String TRACK_LOADED_FLAG();//发货未到
    String TRACK_NOPLAN_FLAG();//确认未配车
    String TRACK_PLANED_FLAG();//配车未发货
    String TRANSACT_NOTES();//节点信息
    String TRANSACT_LOG();//跟踪历史信息
    String TRANS_TRACK_TRACE();//在途信息
    String TRANS_SHMP_LIST();//作业单列表
    
    //货损货差
    String CUSTOMER_ID_NAME();//客户
    String LOSS_DAMAGE_TYP_NAME();//残损类型
    String AMOUNT();//残损金额
    String DUTYER();//责任人
    String LOTATT01();//批号
    String LOTATT02();//专供标识
    
    //作业单回单
    String POD_DELAY_REASON_NAME();//回单延迟原因
    String PRE_POD_TIME();//计划回单时间
    String ORD_IMA();//回单影像
    String ODR_NO();//托运单号 
    String UNLD_NWGT();//收获体积
    String UNLD_WORTH();//收货货值
    String UNLD_VOL();
    String UNLD_GWGT();
    String SHPM_STSTUS();
    String LOT_ID();
    
    //列表配置
    String CONFIG_LIST_NAME();
    String CONFIG_MODEL_NAME();
    
    //时间管理
    String IN_PRIOR();//优先级
    String FORMULA();//表达式
    String DOC_TYPE();//单据类型
    String TIME_TYPE();//时间类别
    String C_FIELD();//字段
    String O_OPERATOR();//运算符
    String C_VALUE();//条件值
    String OP_METHOD();//操作方式
    String TIME_MAN_INFO();//时间管理主信息
    String SELECT_BY();//过滤条件
    String FROM_DATE();//生效期
    String TO_DATE();//失效期
    String UNIT_FIELD();//级差单位
    String UNIT_FROM();//从
    String UNIT_TO();//到
    
    //提货装车
    String VEHL_TYP();//车型
    String UNLOAD_AREA_NAME_LOAD();//发货去向
    String CONSIGNER();//发货员
    String START_LOAD_TIME();//开始装货时间
    String END_LOAD_TIME_LOAD();//预计完成时间
    String END_LOAD_TIME_FINISH();//完成装货时间
    String QUEUE();//排队号
    String LOAD_WHSE();//提货仓库
    String ARRIVE_WHSE_TIME();//到库时间
    String QNTY_NUM();//数量[箱]
    String WAIT_VECH();//等待车辆
    String LOAD_GOOD();//正在装货
    String START_LOAD();//开始装货
    String FINISH_LOAD();//完成装货
    String DOCK_TYP();//类型
    
    //车辆登记
    String LEAVE_WHSE_TIME();//离库时间
    String STEVEDORE();//装卸工
    String STEVE_COUNT();//人数
    String SCANNER();//扫描员
    String VAN_CIRCS();//篷布状况
    String VEHL_CIRCS();//车况
    String TYRE_CIRCS();//轮胎状况
    String SANIT_CIRCS();//车厢清洁卫生
    String DIRVER_CIRCS();//司机精神状态
    String END_LOAD_TIME_JOB();//完成发货时间
    String VECH_CHECK();//车辆检查
    String ARRIVE_REG();//到库登记
    String LEAVE_REG();//离库时间
    String LOAD_VECH_LIST();//提货车辆列表
    
    //预警信息
    String EXEC_ORG_ID_NAME();//运作机构
    String START_ID_NAME();//发货单位
    String END_ID_NAME();//收货单位
    String END_ADDRESS();//收货地址
    String ODR_TIME_WARN();//接单时间
    String PRE_LOAD_TIME_WARN();//要求发运时间
    String PRE_POD_TIME_WARN();//要求回单时间
    String PRE_UNLOAD_TIME_WARN();//要求到货时间
    String DELAY_DAYS();//到货逾期天数
    String DELAY_DAYS_POD();//回单逾期天数
    String START_AREA_ID_NAME();//发货区域
    String END_AREA_ID_NAME();//收货区域
    String LOAD_OR();//应发未发
    String ARRIVE_OR();//应到未到
    
    //报表   营运日/周/月汇总报表
    String EST_FEE();//预估运费
    
    //报表  区域业务汇总表
    String R_AREA_NAME();
    String R_PROVENCE();
    String R_LD_QNTY();
    String R_UNLOAD_VOL();
    String R_NWGT();
    String R_COST();
    String R_PRCENT();
    String R_SALES();
    String R_ODR_TYP();
    String R_SKU_TYP();
    String R_ON_TIME();
    String R_1DAY();
    String R_2DAY();
    String R_3DAY();
    String R_4DAY();
    String R_5DAY();
    String R_6DAY();
    
    String LOSDAM_NO();
    String DAM1_QNTY();
    String DAM2_QNTY();
    String DAM3_QNTY();
    String LOS_QNTY();  
    String DAM_RATE();
    String ODR_NUM();
    String R_TOT_QNTY();
    String R_TOT_QNTY_EACH();
    String R_UNLOAD_RATE();
    String R_UNLOAD_ACCOUNT();
    String R_POD_RATE();
    String R_POD_ACCOUNT();
    String R_LOASS_RATE();
    String R_LOASS_QNTY();
    
    String POD_TIME_FROM();//回单时间 从
    String NEED_FEE();//应付运费
    String R_EA();//EA数
    
    //区域/客户运费分析表
    String QNTY_EACH_NUM();//数量[瓶]
    String QNTY_SUM();//瓶数
    String DUE_FEE();//总运费
    String EA_FEE();//每箱运费
    String CS_FEE();//每瓶运费
    String ORDER_NUM();//订单次数
    String C_SALE_FLAG();//含促销品
    String ORD_SUM();//订货频次
    String LOAD_NUM();//车次
    String DUE_FEE_SUM();//应付金额
    String ACCOUNT_FEE();//实付金额
    String UNACC_FEE();//未付金额
    
    //打印授权
    String GRANT_REASON();//授权原因
    String LOAD_PRINT_COUNT();//打印次数
    String PRINT_TIME();//打印时间
    String PRINTER();//打印人
    
    String ROUTE_MILE();
    String MILE();
    
    //手机定位
    String OPERATOR_TYPE();//操作类型
    String SUCCESS_FLAG();//定位是否成功
    String POSITION_TIME();//定位时间
    String POSITION_PERSON();//定位人
    String FEE();//费用
    String TOT_FEE();
    
    //费用协议
    String TFF_NAME();
    String CONTACT_NO();
    String FEE_START_DATE();
    String FEE_END_DATE();
    String SIGN_DATE();
    String SIGN_ORG_ID();
    String TFF_TYP();
    String OBJECT_ID();
    String OBJECT_NAME();
    String DOC_TYP();
    String SORTORDER();
    String COUNT_TYP();
    
    String LEFT_BRKT();
    String OPER_OBJ();
    String OPER_ATTR();
    String OPERATOR();
    String ATTR_VAL();
    String RIGHT_BRKT();
    String LINK();
    
    String TFF_ID();
    String FEE_ID();
    String FEE_NAME();
    String FEE_BASE();
    String FEE_BASE_NAME();
    String CARRY_TYP();
    String BASE_RATE();
    String EXPRESS_FLAG();
    String FEE_TYP();
    String FEE_TYP_NAME();
    String GRADE_BASE();
    String LOWER_LMT_OPRT();
    String LOWER_LMT();
    String UPPER_LMT_OPRT();
    String UPPER_LMT();
    String BASE_AMT();
    String MIN_AMT();
    String MAX_AMT();
    String MIN_UNIT();
    String PRIORTY();
    String UOM_UNIT();
    String FEE_REFENENCE1();
    
    String BAS_VALUE();
    String PRE_FEE();
    String PAY_FEE();
    
    //业务规则
    String RUL_CODE();
    String RUL_DESCR();
    String LOAD_RATE();
    String WARP_RATE();
    String DISPATCH_RULE();
    String RUL_MODE();
    String SPLIT_MODE();
    String UNION_FLAG();
    String UDF_LIMIT1();
    String UDF_LIMIT2();
    String UDF_LIMIT3();
    String UDF_LIMIT4();
    
    String CONDITION();
    String SORT_BY();
    String GROUP_CONDITION();
    
    //可用车辆上报
    String REPORT_STATUS();
    String REPORTER();
    String REPORT_TIME();
    String VEH_NUM();
    String AUDIT_STATUS();
    
    //费用结算
    String DOC_NO();
    String SETT_NO();
    String SETT_NAME();
    String SETT_TYPE();
    String SETT_CASH();
    String LOW_CASH();
    String BILL_STAT();
    String AUDIT_STAT();
    String ACCOUNT_STAT();
    String AUDIT_TIME();
    String ACCOUNT_TIME();
    String AUDITER();
    String ACCOUNTER();
    String SETT_VERIFI_STAT();//核销状态
    String SETT_VERIFI_CASH();//核销金额
    String SETT_CONT_CASH();//标杆金额
    String SETT_CONT_PRICE();//标杆单价
    String SETT_VERIFI_PRE_TIME();//预计核销时间
    String SETT_VERIFI_TIME();//实际核销时间
    String SETT_VERIFICATER();//核销人
    
    //自动配载日志
    String EXEC_TIME();
    String EXEC_SEQ();
    
    //节点规则
    String SMS_FLAG();
    String REC_FEE_FLAG();
    String PAY_FEE_FLAG();
    String TIME_FLAG();
    String BIZ_LOG_FLAG();
    String OPT_LOG_FLAG();
    
    //原始订单
    String ARRIVABLE_FLAG();
    String EXEC_STAT();
    String ABNORMAL_CODE();
    
    //业务日志配置
    String STAT_CODE_NAME();
    String NOTES_CODE_NAME();
    
    //结算单
    String MON_DEL_ACCOUNT();
    String LOAD_AREA_CODE2();
    String UNLOAD_AREA_CODE2();
    String TAX_VAL();
    String VAL_AFT_TAX();
    
    String RECE_STAT();
    String R_LOAD_QNTY();
    
    String DEF_RDC();
    
    String RDC_NO();
    String ORI_RDC();
    String CURR_RDC();
    String RDC_STAT();
    
    String PRE_AUDIT_TIME();
    //  温控/gps设备
    String EQUIP_NO();
    String PURCHASE_DATE();
    String TRS_ID();
    
    //保险购买记录
    String INS_NO();
    String INS_TYPE();
    String INS_COMPANY();
    String INS_DOCNO();
    String INS_DATE();
    String INS_FROM();
    String INS_TO();
    String INS_CLS();
    String INS_FEE();
    String INS_AMOUNT();
    
    //加油记录
    String PLATENO();
    String OIL_TIME();
    String OIL_NAME();
    String OIL_VOLUMEN();
    String OIL_PRICE();
    String OIL_AMOUNT();
    String OIL_ADDRESS();
    
//    String REMAIN_DAYS();
    
}
