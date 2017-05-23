package com.rd.client.common.util;

import com.smartgwt.client.types.DateDisplayFormat;

/**
 * 常量的静态类
 * @author yuanlei
 *
 */
public class StaticRef {
	
	//分页
	public static final int FIRST_PAGE = 0;
	public static final int PRE_PAGE = 1;
	public static final int NEXT_PAGE = 2;
	public static final int LAST_PAGE = 3;
	public static final int GO_PAGE = 4;
	public static final int INIT_PAGE = 5;
	
	public static final String MAIN_PAGE = "rd/main_page.png";
	public static final String MAIN_INFO = "rd/company.png";
	
	public static final String TO_MAX = "最大化";
	public static final String TO_NORMAL = "还原";
	
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	
	public static final String SYS_DATE = "sysdate";
	
	//系统小数位显示
	public static final String QNTY_FLOAT = "#,##0.00"; //数量
	public static final String VOL_FLOAT = "0.0000"; //体积、长、宽、高
	public static final String GWT_FLOAT = "#,##0.000"; //重量
	public static final String PRICE_FLOAT = "#,##0.000"; //金额
	
	public static final String MASK_FLOAT = "^#";
	
	//按钮图片路径
	public static final String ICON_SEARCH = "rd/find.png";
	public static final String ICON_CLEAR = "rd/find.png";
	public static final String ICON_NEW = "rd/new.png";
	public static final String ICON_COPY = "rd/new.png";
	public static final String ICON_SAVE = "rd/save.png";
	public static final String ICON_DEL = "rd/del.png";
	public static final String ICON_CANCEL = "rd/cancel.png";
	public static final String ICON_IMPORT = "rd/export.png";
	public static final String ICON_EXPORT = "rd/export.png";
	public static final String ICON_PRINT = "rd/printer.png";
	public static final String ICON_PVIEW = "rd/printerview.png";
	public static final String ICON_FIRST = "rd/first.png";
	public static final String ICON_PRE = "rd/front.png";
	public static final String ICON_NEXT = "rd/next.png";
	public static final String ICON_LAST = "rd/last.png";
	public static final String ICON_NODE = "rd/leaf.png";
	public static final String ICON_LEAF = "rd/node.png";
	public static final String ICON_UP = "rd/arrow_up.png";
	public static final String ICON_DOWN = "rd/arrow_down.png";
	public static final String ICON_LEFT = "rd/arrow_left.png";
	public static final String ICON_RIGHT = "rd/arrow_right.png";
	public static final String ICON_WIN = "rd/window.png";
	public static final String ICON_START = "rd/start.png";
	public static final String ICON_TO = "rd/to.png";
	public static final String ICON_END = "rd/end.png";
	public static final String ICON_TOUP = "rd/toup.png";
	public static final String ICON_TODOWN = "rd/todown.png";
	public static final String ICON_NORMAL = "rd/normal.png";
	public static final String ICON_TORIGHT = "rd/right_expand.png";
	public static final String ICON_TOLEFT = "rd/left_contract.png";
	public static final String ICON_UPLOAD = "rd/upload.png";
	public static final String ICON_CONFIRM = "rd/confirm.png";
	public static final String ICON_IMGLINK = "rd/images_link.png";
	public static final String ICON_REFRESH = "rd/refresh.png";
	
	public static final String ICON_GOLEFT= "rd/go_left.png";
	public static final String ICON_GORIGHT = "rd/go_right.png";
	
	public static final String SUCCESS_CODE = "00";
	public static final String FAILURE_CODE = "01";
	public static final String WARNING_CODE = "02";
	
	public static final String AND = "and";
	public static final String OR = "or";
	public static final String NOT = "not";
	
	public static final String T_SYSROLE = "SYS_ROLE"; 
	
	//日志文件类型
	public static final String SQL_LOG = "SQL_LOG";
	public static final String USER_LOG = "USER_LOG";
	public static final String ERROR_LOG = "ERROR_LOG";
	public static final String SERVER_LOG = "SERVER_LOG";
	
	//用户日志信息
	public static final String ACT_LOGIN = "用户登录系统";
	public static final String ACT_LOGOUT = "用户退出系统";
	public static final String ACT_OPEN = "进入";
	public static final String ACT_FETCH = "执行查询";
	public static final String ACT_INSERT = "添加";
	public static final String ACT_DELETE = "删除";
	public static final String ACT_SUCCESS = "成功";
	public static final String ACT_FAILURE = "失败";
	public static final String ACT_FROM = "由";
	public static final String ACT_TO = "修改为";
	
	//操作标记
	public static final String MOD_FLAG = "M";//修改
	public static final String INS_FLAG = "A";//增加
	public static final String DEL_FLAG = "D";//删除
	
	//校验类型
	public static final String CHK_NOTNULL = "01";
	public static final String CHK_UNIQUE = "02";
	public static final String CHK_MAXLEN = "03";
	public static final String CHK_DATE   = "04";
	
	//按钮类型
	public static final String FETCH_BTN = "FETCH";//fetch
	public static final String CLEAR_BTN = "CLEAR";//clear
	public static final String CREATE_BTN = "CREATE";//create
	public static final String SAVE_BTN = "SAVE";//
	public static final String DELETE_BTN = "REMOVE";
	public static final String CANCEL_BTN = "CANCEL";
	public static final String FREEZE_BTN = "FREEZE";
	public static final String FREE_BTN = "FREE";
	public static final String PRINT_BTN = "PRINT";//print
	public static final String PRVIEW_BTN = "PRINTVIEW";//printview
	public static final String IMPORT_BTN = "IMPORT";//import
	public static final String EXPORT_BTN = "EXPORT";//export
	public static final String EXPORT_DETAIL_BTN = "EXPORT_DETAIL";
	public static final String SUPER_ROLE = "SUPER_MAN";
	public static final String CONFIRM_BTN = "CONFIRM";
	public static final String PAYOUT_BTN="PAYOUT";
	public static final String CLOSE_BTN="CLOSE";
	public static final String CONFIRM_ORDER_BTN="CONFIRMORDER";
	public static final String MANY_ORDER_BTN="MANYORDER";
	public static final String CANCEL_ORDER_BTN="CANCELORDER";
	public static final String PUT_IMAGE_BTN="PUTIMAGE";
	public static final String CREATE_CONDITION_BTN="CREATECONDITION";
	public static final String SAVE_CONDITION_BTN="SAVECONDITION";
	public static final String DELETE_CONDITION_BTN="DELETECONDITION";
	public static final String CANCEL_CONDITION_BTN="CANCELCONDITION";
	public static final String NUM_BTN="NUMBTN";
	public static final String STALOAD_BTN="STALOAD";
	public static final String FINLOAD_BTN="FINLOAD";
	public static final String ARRIREG_BTN="ARRIREG";
	public static final String VEHLCHECK_BTN="VEHLCHECK";
	public static final String LEVAREG_BTN="LEVAREG";
	public static final String REFRESH_BTN="REFRESH";
	public static final String PUTIN_BTN="PUTIN";
	public static final String CANCEL_LOAD_BTN = "CANCEL_LOAD_BTN";
	public static final String COPY_BTN = "COPY";
	
	//时间格式的类型
	public static final DateDisplayFormat FORMAT_DATE = DateDisplayFormat.TOJAPANSHORTDATE;
	public static final DateDisplayFormat FORMAT_DATETIME = DateDisplayFormat.TOJAPANSHORTDATETIME;
	public static final String DB_DATE_FUNC = "to_date";
	
	//订单优先级
	public static final String UGRT_GRD="D982747B389A4B22BF1B87FBCF9A37D2"; //普通
	
	//数据字典中定值
	public static final String ON_LINE = "750E29B2F3A54466A2F610482EECB9AC";    //在线
	public static final String ON_LINE_NAME = "在线";    //在线
	public static final String OFF_LINE = "1872EC8A11CE4ED79534D1729CE54D7D";    //离线
	public static final String OFF_LINE_NAME = "离线";    //离线
	public static final String ORD_STATUS_CREATE = "10" ; //订单状态已创建
	public static final String ASSIGN_STATUS_NO = "10";//派发状态未派发
	public static final String PLAN_STATUS_NO = "10" ; //调度状态-未调度
	public static final String LOAD_STATUS_NO = "10";//发货状态-未发货
	public static final String UNLOAD_STATUS_NO = "10" ; //收货状态未收货
	public static final String SHPMENT_STATUS_CONFIRM = "10";//作业单状况已确认
	
	//车辆状态
	public static final String VEH_UNUSE = "CB3026880F624816B1DB258674FE7502";
	
	//状态类型
	public static final String ODRNO_STAT = "TRANS_ODR_STAT";  //托运单
	public static final String SHPMNO_STAT = "SHPM_STAT";      //作业单
	public static final String LOADNO_STAT = "TRANS_LOAD_STAT"; //调度单
	public static final String ASSIGN_STAT = "ASSIGN_STAT";   //派发状态
	public static final String PLAN_STAT = "PLAN_STAT";       //调度状态
	public static final String LOAD_STAT = "LOAD_STAT";       //发货状态
	public static final String UNLOAD_STAT = "UNLOAD_STAT";   //收货状态
	public static final String TRANS_LOAD_JOB = "PICK_STAT";//提货装车状态
	public static final String SFODR_STAT= "SFODR_STAT";
	
	//托运单状态
	public static final String SO_CREATE = "10";
	public static final String SO_CREATE_NAME = "已创建";
	public static final String SO_CONFIRM = "20";
	public static final String SO_CONFIRM_NAME = "已确认";
	public static final String SO_RECEIPT = "60";
	public static final String SO_RECEIPT_NAME = "已回单";
	public static final String SO_FROZEN = "90";
	public static final String SO_FROZEN_NAME = "已冻结";
	public static final String SO_CLOSED = "99";
	public static final String SO_CLOSED_NAME = "已关闭";
	
	//派发状态
	public static final String NO_ASSIGN = "10";
	public static final String REJECT_ASSIGN = "15";  //拒绝
	public static final String ASSIGNED = "20";
	//调度状态
	public static final String NO_PLAN = "10";
	public static final String PART_PLAN = "15";
	public static final String PLANED = "20";
	//发货状态
	public static final String NO_LOAD = "10";
	public static final String NO_LOAD_NAME = "未装车";
	public static final String PART_LOAD = "15";
	public static final String LOADED = "20";
	public static final String LOADED_NAME = "已装车";
	//收货状态
	public static final String NO_UNLOAD = "10";
	public static final String PART_UNLOAD = "15";
	public static final String UNLOADED = "20";
	
	//作业单状态
	public static final String SHPM_CONFIRM = "20";
	public static final String SHPM_CONFIRM_NAME = "已确认";
	public static final String SHPM_DIPATCH = "30";
	public static final String SHPM_DIPATCH_NAME = "已调度";
	public static final String SHPM_PART_LOAD = "35";
	public static final String SHPM_PART_LOAD_NAME = "部分发运";
	public static final String SHPM_LOAD = "40";
	public static final String SHPM_LOAD_NAME = "已发运";
	public static final String SHPM_PART_UNLOAD = "45";
	public static final String SHPM_PART_UNLOAD_NAME = "部分到货";
	public static final String SHPM_UNLOAD = "50";
	public static final String SHPM_UNLOAD_NAME = "已到货";
	public static final String SHPM_RECEIPT = "60";
	public static final String SHPM_RECEIPT_NAME = "已回单";
	public static final String SHPM_FROZEN = "90";
	public static final String SHPM_FROZEN_NAME = "已冻结";
	public static final String SHPM_SPLITED = "92";
	public static final String SHPM_SPLITED_NAME = "已拆分";
	public static final String SHPM_REJECT = "85";
	public static final String SHPM_REJECT_NAME = "已拒绝";
	public static final String SHPM_CLOSED ="99";
	public static final String SHPM_CLOSED_NAME="已关闭";

	//调度单状态
	public static final String TRANS_CREATE = "10";
	public static final String TRANS_CREATE_NAME = "已创建";
	public static final String TRANS_PART_DEPART = "35";
	public static final String TRANS_PART_DEPART_NAME = "部分发运";
	public static final String TRANS_DEPART = "40";
	public static final String TRANS_DEPART_NAME = "完全发运";
	public static final String TRANS_PART_UNLOAD = "45";
	public static final String TRANS_PART_UNLOAD_NAME = "部分到货";
	public static final String TRANS_UNLOAD = "50";
	public static final String TRANS_UNLOAD_NAME = "完全到货";
	
	//提货装车状态
	public static final String TRANS_EXPECT = "10";
	public static final String TRANS_EXPECT_NAME = "等候装车";
	public static final String TRANS_LOADING = "20";
	public static final String TRANS_LOADING_NAME = "正在装车";
	public static final String TRANS_FINISH = "30";
	public static final String TRANS_FINISH_NAME = "装货完成";
	public static final String TRANS_LEAVE = "40";
	public static final String TRANS_LEAVE_NAME = "已离库";
	public static final String TRANS_DISQUALIFICATE = "50";
	public static final String TRANS_DISQUALIFICATE_NAME = "不合格退回";
	
	//配车状态
	public static final String NO_DISPATCH_NAME = "未配车";
	public static final String DISPATCHED_NAME = "已配车";
	public static final String AUDITED_NAME = "已审核";
	public static final String GET_BACK = "已打回";
	
	//上传图片路径
	public static final String ORDER_RECLIM_URL = "images\\test\\ORDMANAGER\\";
	public static final String SHPM_RECLIM_URL = "images\\test\\SHPMMANAGER\\";
	public static final String BAS_SKU_URL = "images\\test\\SKU\\";
	public static final String BAS_COMPLAINT_URL = "images\\test\\COMPLAINT\\";
	public static final String BAS_VEHCAPACITY_URL = "images\\test\\BAS_VEHCLE\\";
	public static final String BAS_STAFF_URL = "images\\test\\BAS_STAFF\\";
	public static final String SETTLE_INVOICE_URL = "images\\test\\INVOICE\\";
	//预览图片路径
	public static final String ORDER_RECLIM_PREVIEW_URL = "images/test/ORDMANAGER/";
	public static final String SHPM_RECLIM_PREVIEW_URL = "images/test/SHPMMANAGER/";
	public static final String BAS_SKU_PREVIEW_URL = "images/test/SKU/";
	public static final String BAS_COMPLAINT_PREVIEW_URL = "images/test/COMPLAINT/";
	public static final String BAS_VEHCAPACITY_PREVIEW_URL = "images/test/BAS_VEHCLE/";
	public static final String BAS_STAFF_PREVIEW_URL = "images/test/BAS_STAFF/";
	public static final String SETTLE_INVOICE_PREVIEW_URL = "images/test/INVOICE/";
	
	//获取时间格式
	public static final String TIME_FORMAT_SHORT = "yyyy/MM/dd ";
	public static final String TIME_FORMAT_LONG = "yyyy/MM/dd HH:mm";
	//业务日志单据类型
	public static final String ODR_NO = "ODR_NO";
	public static final String SHPM_NO = "SHPM_NO";
	public static final String LOAD_NO = "LOAD_NO";
	public static final String BILL_NO = "BILL_NO";

	public static final String LOG_CODE = "TRACK_POINT";
	//业务日志操作类型
	public static final String ODR_ITEM_ADD = "ODR_ITEM_ADD";
	public static final String ODR_ITEM_UPDATE = "ODR_ITEM_UPDATE";
	public static final String ODR_ITEM_DEL = "ODR_ITEM_DEL";
	
	//任大内部测试密钥
//	public static final String API_KEY = "ABQIAAAANOj46xDVuosovgafvvyuwRS10lyUThgul6438lXLwrzpexcWABRm0q3S4Z-i6EsY31Q39GAKbUW9bw";
    //洋河密钥:ABQIAAAANOj46xDVuosovgafvvyuwRSiHT5xb5nEBqSWXIzOLoV05FV8LhT-uE6IK_qifGhDdPLjuJl66XWbZg
	public static final String API_KEY_OUT = "ABQIAAAANOj46xDVuosovgafvvyuwRSiHT5xb5nEBqSWXIzOLoV05FV8LhT-uE6IK_qifGhDdPLjuJl66XWbZg"; //洋河外网
	public static final String API_KEY_IN = "ABQIAAAANOj46xDVuosovgafvvyuwRR8ffdB5VtZXtxyXP_cYd_p2diDiRQ5BTA916eesW_MqZDBDGmLVCVKOg";  //洋河内网
//	public static final String API_KEY_OUT = "ABQIAAAANOj46xDVuosovgafvvyuwRRaJPtwPBaH82piwhjUo7HnmheV6xRg2CqsxjWb51dP57rBO0JmDqndQA";  //洋河内测试
	public static final String OUT_IP = "199.10.";
	
	public static final String ODR_TYP_DB = "05D7C5799835404FAE4080C7D1195D7F";
	
	public static final String SY_GPS="http://202.102.112.25/login.aspx?company=sqyhwl&user=sqyhwl&psw=sqyhwl";
	
	public static final String TRANS_TFF_TYP = "42666CA2DE904F6687FC172138CF3E51";
	
	public static final String UNREPORT = "UNREPORT";
	public static final String UNREPORT_NAME = "未上报";
	
	public static final String REPORTED = "REPORTED";
	public static final String REPORTED_NAME = "已上报";
	
	public static final String UNAUDIT = "10";
	public static final String UNAUDIT_NAME = "未审核";
	
	public static final String AUDITED = "20";
	
	//托运单执行（正常、异常）状态
	public static final String NORMAL = "B41F05C6B85A4221813B833B103D2452";   //正常
	public static final String ABNORMAL = "40272A93DC6D4D52AE20514C74A2A0A7"; //异常
	public static final String NORMAL_NAME = "正常";   //正常
	public static final String ABNORMAL_NAME = "异常";   //正常
	
	//原始托运单状态
	public static final String SFODR_STATUS_CREATE = "10";
	public static final String SFODR_STATUS_CREATE_NAME = "已创建";
	public static final String SFODR_STATUS_CONFIRM = "20";
	public static final String SFODR_STATUS_CONFIRM_NAME = "已提交";
	public static final String SFODR_STATUS_AUDIT = "30";
	public static final String SFODR_STATUS_AUDIT_NAME = "已审核";
	public static final String SFODR_STATUS_OUTBOUND = "40";
	public static final String SFODR_STATUS_OUTBOUND_NAME = "已出库";
	
	//业务类型
	public static final String B2B = "574037A56F7041428364041D1D4B2BA8";
	public static final String B2C = "ADAEE2F25B39487FA778AC78065CE373";
	public static final String LD = "A801EE8F48DE4260A74C1C09FF9034D4";
	public static final String SHOP = "47FC91E8DD1541DABC9C800D9F97E922";
	
	//运输服务
	public static final String TRANS_GX = "2";
	public static final String TRANS_PS = "3";
	public static final String TRANS_LD = "1";
	
	//温层属性
	public static final String TMP_SINGLE = "1000000000";
	public static final String TMP_DOUBLE = "2000000000";
	public static final String TMP_MULTL = "3000000000";
	
	//车辆可用性
	public static final String UNAVAIL_FLAG = "85B874F607B843D78430982DC3D77960";
	public static final String AVAIL_FLAG = "BECB9F7D7577427BB885FB32B5534177";
	
	//客户费用结算收款状态
	public static final String RECE_STAT = "已收款";
	public static final String NO_RECE = "未收款";
	
	public static final String PAY_DEPART="1950F59A0D094F75A34B837086CA176C";
	
	public static final String RDC="D8D9F41D57BC4320BAE2E1979F14BF5E";
	public static final String PART = "383297659DAA4C3CB084C9AB93A11F73"; //分部
	public static final String POINT = "9E0057747CC24A4BB998749752D2C42E";  //点部
	
	public static final String V_SHIPMENT_HEADER_LOAD = "V_SHIPMENT_HEADERDISPATCH_VIEW_2";
	public static final String V_SHIPMENT_HEADER_UNLOAD = "V_SHIPMENT_HEADERDISPATCH_VIEW";
	public static final String V_SHIPMENT_HEADER_SHPM ="V_SHIPMENT_HEADERSHPM_VIEW";
	public static final String V_SHIPMENT_HEADER_PICKLOAD="V_SHIPMENT_HEADERLOADJOB_VIEW";
	public static final String V_SHIPMENT_HEADER_TRACK ="V_SHIPMENT_HEADERTRACK_VIEW";
	public static final String V_SHIPMENT_HEADER_RECLAIM ="V_SHIPMENT_HEADERSHPM_RECLAIM";
	public static final String V_ORDER_HEADER_ODR = "V_ORDER_HEADERODR_VIEW";
	public static final String V_ORDER_HEADER_FEE = "V_ORDER_HEADERCUSTOMFEE_VIEW";
	public static final String V_ORDER_HEADER_GRP = "V_ORDER_HEADERODRGRP_VIEW";
	public static final String SF_ORDER_HEADER_ODR = "SF_ORDER_HEADERSF_ODR_VIEW";
	
	public static final String FLOAT="0.00%";
	
	public static final String RDC_CREATE="10";
	public static final String RDC_CREATE_NAME = "已创建";
	public static final String RDC_CONFIRM = "20";
	public static final String RDC_CONFIRM_NAME = "已确认";
	public static final String RDC_CHANGED ="30";
	public static final String RDC_CHANGED_NAME = "已转仓";
	
	
	//计费标识
	public static final String BILL_FLAG_Y = "BILLY";
	public static final String BILL_FLAG_N = "BILLN";
	
	//行业属性
	public static final String INDUSTRY_ATTR_FOOD = "SHIPIN";
	public static final String INDUSTRY_ATTR_MEDICINE = "YIYAO";
	
	//对账状态
	public static final String NO_ACCOUNT = "10";
	public static final String ACCOUNTED = "20";
	
	
	//gps用户密码
	public static final String GpsAccount = "YumPFS";
	public static final String GpsPassWord= "123456";
}