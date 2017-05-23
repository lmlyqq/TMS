package com.rd.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import com.google.gson.Gson;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.obj.system.SYS_USER;
import com.rd.server.util.HSQL;
import com.rd.server.util.LoginContent;
import com.rd.server.util.SQLUtil;
import com.rd.server.util.SUtil;

/**
 * 运输管理下所有模块对应的servlet
 * r
 * @author fanglm
 * 
 */
public class TmsQueryServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -962279661006063176L;

//	private Query query;
//	private final int page_record = LoginContent.getInstance().pageSize;

	private boolean isCustSize  = false;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
	    HttpSession session = null;
		session = req.getSession();
		session.setMaxInactiveInterval(24*60*60);
		SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
		String dsid = req.getParameter("ds_id"); // 数据源ID
		String flag = SUtil.iif(req.getParameter("OP_FLAG"), ""); // 操作标记
		String is_curr_page = SUtil.iif(req.getParameter("is_curr_page"),""); //分页查询
		SQLUtil sqlUtil = new SQLUtil(true);
		String key = "";
		String value = "";
		String alias = "";
		isCustSize  = false;
		if (dsid != null && flag.equals(StaticRef.MOD_FLAG)) {
			response.setCharacterEncoding("utf-8");
			PrintWriter p = response.getWriter();
			if(user == null) {
				p.print("404");
				return;
			}
			
			//Session curSession = LoginContent.getInstance().getSession();
			String CUR_PAGE = (String) req.getParameter("CUR_PAGE"); // 当前页码
			int start_row = 0; // 开始行，从0开始
			StringBuffer sf = null;
			int page_record = LoginContent.getInstance().pageSize;
			Query query = null;
			if (dsid.equals("V_LOAD_HEADER")||dsid.equals("V_LOAD_HEADER2")) {   
				key = "TRANS_SRVC_ID_NAME,VEHICLE_TYP_ID_NAME,TEMP_NO1_NAME,TEMP_NO2_NAME,LOAD_STAT_NAME,GPS_NO1_NAME";
				value = "t1.SRVC_NAME,t2.VEHICLE_TYPE,t3.EQUIP_NO,t4.EQUIP_NO,t5.NAME_C,t6.EQUIP_NO";
				alias = "t";
				//调度管理->调度单
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("TRANS_LOAD_HEADER",sqlUtil.getTableAlias());
				sf = new StringBuffer();
				sf.append("select distinct ");
				sf.append(keys);
				sf.append(",t1.SRVC_NAME AS TRANS_SRVC_ID_NAME,t2.VEHICLE_TYPE AS VEHICLE_TYP_ID_NAME");
				sf.append(",t3.EQUIP_NO AS TEMP_NO1_NAME,t4.EQUIP_NO AS TEMP_NO2_NAME,t5.NAME_C AS LOAD_STAT_NAME,t6.EQUIP_NO as GPS_NO1_NAME");
				sf.append(",t7.NAME_C as AUDIT_STAT_NAME,t8.NAME_C as ACCOUNT_STAT_NAME ");
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from TRANS_LOAD_HEADER_H t ");
				}
				else {
					sf.append(" from TRANS_LOAD_HEADER t ");
				}
				sf.append(",BAS_TRANS_SERVICE t1");
				sf.append(",BAS_VEHICLE_TYPE t2");
				sf.append(",BAS_TEMPEQ t3");
				sf.append(",BAS_TEMPEQ t4");
				sf.append(",BAS_CODES t5");
				sf.append(",BAS_GPSEQ t6");
				sf.append(",BAS_CODES t7");
				sf.append(",BAS_CODES t8");
				sf.append(" where t.TRANS_SRVC_ID = t1.id");
				sf.append(" and t.VEHICLE_TYP_ID = t2.id(+)");
				sf.append(" and t.TEMP_NO1 = t3.id(+)");
				sf.append(" and t.TEMP_NO2 = t4.id(+)");
				sf.append(" and t.LOAD_STAT = t5.CODE(+) and t5.PROP_CODE = 'PICKLOAD_STAT'");
				sf.append(" and t.GPS_NO1 = t6.id(+)");
				sf.append(" and AUDIT_STAT = t7.CODE(+) and t7.PROP_CODE='APPROVE_STS' ");
				sf.append(" and t.ACCOUNT_STAT = t8.CODE(+) and t8.PROP_CODE='ACCOUNT_STAT' ");
				//sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));        //供应商
				//sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));            //状态 从 
				//sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),req.getParameter("C_RDC_FLAG")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						if(ObjUtil.isNotNull(req.getParameter("EXEC_FLAG")) && req.getParameter("EXEC_FLAG").equals("true")) {
			            	sf.append(" and exists ");
			            	sf.append("    (SELECT 'x' ");
			            	sf.append("     From bas_org ");
			            	sf.append("     ,trans_shipment_header t1");
							sf.append("     Where t1.load_no = t.load_no and t1.SIGN_ORG_ID = bas_org.id AND (bas_org.ORG_INDEX Like '%,"); //
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("%'");
							sf.append("))");
						}
						else {
							sf.append(" and exists ");
			            	sf.append("    (SELECT 'x' ");
			            	sf.append("     From bas_org ");
							sf.append("     Where t.EXEC_ORG_ID = bas_org.id AND (bas_org.ORG_INDEX Like '%,"); //
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("%'");
							sf.append("))");
						}
					}
					else {
						if(ObjUtil.isNotNull(req.getParameter("EXEC_FLAG")) && req.getParameter("EXEC_FLAG").equals("true")) {
							sf.append(" and (SIGN_ORG_ID = '");
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("'");
							sf.append(")");
						}
						else {
							sf.append(" and (EXEC_ORG_ID = '");
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("'");
							sf.append(")");
						}
					}
				}
				//fanglm 2011-10-09 用户的客户权限
				/*if(ObjUtil.isNotNull(user.getUSER_CUSTOMER())){
					sf.append(" and t.load_no in ");
					sf.append("(select load_no from ");
					if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
						sf.append(" trans_shipment_header_h");
					}
					else {
						sf.append(" trans_shipment_header");
					}
					sf.append(" where customer_id ");
					sf.append(" in (");
					sf.append(user.getUSER_CUSTOMER());
					sf.append("))");
				}*/
				//yuanlei
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
//				sf.append(sqlUtil.addALikeSQL("START_AREA_NAME", req.getParameter("START_AREA_NAME")));
				sf.append(sqlUtil.addEqualSQL("START_AREA_ID", req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addEqualSQL("ADDWHO", req.getParameter("ADDWHO")));  //客户
//				sf.append(sqlUtil.addALikeSQL("END_AREA_NAME", req.getParameter("END_AREA_NAME"))); 
				sf.append(sqlUtil.addEqualSQL("END_AREA_ID", req.getParameter("END_AREA_ID")));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME"), ">="));//wangjun 2011-4-21
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("END_LOAD_TIME_FROM"), ">="));//发运时间从
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("END_LOAD_TIME_TO"),"<="));//发运时间到
				sf.append(sqlUtil.addTimeSQL("ACCOUNT_TIME", req.getParameter("ACCOUNT_TIME_FROM"), ">="));//发运时间从
				sf.append(sqlUtil.addTimeSQL("ACCOUNT_TIME", req.getParameter("ACCOUNT_TIME_TO"),"<="));//发运时间到
				sf.append(sqlUtil.addTimeSQL("FEEAUDIT_TIME", req.getParameter("AUDIT_TIME_FROM"), ">="));//发运时间从
				sf.append(sqlUtil.addTimeSQL("FEEAUDIT_TIME", req.getParameter("AUDIT_TIME_TO"),"<="));//发运时间到
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID",req.getParameter("SUPLR_ID")));//供应商
				sf.append(sqlUtil.addEqualSQL("ADDWHO",req.getParameter("ADDWHO")));
				sf.append(sqlUtil.addALikeSQL("SUPLR_NAME",req.getParameter("SUPLR_NAME")));//供应商
				sf.append(sqlUtil.addALikeSQL("PLATE_NO",req.getParameter("PLATE_NO")));
				//sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
				//sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addEqualSQL("DISPATCH_STAT_NAME",req.getParameter("DISPATCH_STAT_NAME")));
				sf.append(sqlUtil.addEqualSQL("DISPATCH_STAT",req.getParameter("DISPATCH_STAT")));//--wangjun 2010-2-27
				sf.append(sqlUtil.addEqualSQL("ACCOUNT_STAT",req.getParameter("ACCOUNT_STAT")));//--wangjun 2010-2-27
				sf.append(sqlUtil.addEqualSQL("FEEAUDIT_STAT",req.getParameter("FEEAUDIT_STAT")));//--wangjun 2010-2-27
				sf.append(sqlUtil.addTimeSQL("PRE_DEPART_TIME", req.getParameter("PRE_DEPART_TIME"), ">="));
				sf.append(sqlUtil.addTimeSQL("t.PRE_UNLOAD_TIME", req.getParameter("PRE_UNLOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("t.PRE_UNLOAD_TIME", req.getParameter("PRE_UNLOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("PRE_DEPART_TIME", req.getParameter("PRE_DEPART_TIME_TO"), "<="));
				
				if(ObjUtil.isNotNull(req.getParameter("ODR_NO"))){
					sf.append(" and t.LOAD_NO");
	            	sf.append(" IN ");
	            	sf.append("    (select distinct ODR_NO ");
	            	if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
	            		sf.append(" From TRANS_LAOD_HEADER ");
	            	}
	            	else {
	            		sf.append(" From TRANS_SHIPMENT_HEADER ");
	            	}
	            	sf.append(" where ODR_NO = '");
					sf.append(sqlUtil.decode(req.getParameter("ODR_NO")));
					sf.append("')) AS ODR_NO ");
					
				}



				sf.append(sqlUtil.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));  //发货方代码
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				if(ObjUtil.isNotNull(req.getParameter("LOAD_STAT"))){
					sf.append(sqlUtil.addEqualSQL("LOAD_STAT", req.getParameter("LOAD_STAT")));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FROM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FROM")));
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FROM"),">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))){
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
				if(ObjUtil.isNotNull(req.getParameter("AUDIT_STAT"))){
					sf.append(sqlUtil.addEqualSQL("AUDIT_STAT", req.getParameter("AUDIT_STAT")));
				}
//				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));           //调度单号
				if(ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))){
					sf.append(" and t.load_no");
	            	sf.append(" IN ");
	            	sf.append("    (select distinct load_no ");
	            	if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
	            		sf.append("     From TRANS_SHIPMENT_HEADER_H ");
	            	}
	            	else {
	            		sf.append("     From TRANS_SHIPMENT_HEADER ");
	            	}
					sf.append("     Where reverse(custom_odr_no) like ");
					sf.append(" reverse('%"); 
					sf.append(sqlUtil.decode(req.getParameter("CUSTOM_ODR_NO")));
					sf.append("')) ");
					
				}
				if(ObjUtil.isNotNull(req.getParameter("ODR_NO"))){
					sf.append(" and t.load_no");
	            	sf.append(" IN ");
	            	sf.append("    (select distinct load_no ");
	            	if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
	            		sf.append("     From TRANS_SHIPMENT_HEADER_H ");
	            	}
	            	else {
	            		sf.append("     From TRANS_SHIPMENT_HEADER ");
	            	}
					sf.append("     Where reverse(ODR_NO) like ");
					sf.append(" reverse('%"); 
					sf.append(sqlUtil.decode(req.getParameter("ODR_NO")));
					sf.append("')) ");
					
				}
				//sf.append(sqlUtil.addLikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));//作业单编号  --yuanlei 2011-2-18
				//yuanlei
				if(ObjUtil.isNotNull(req.getParameter("SHPM_NO"))){
					sf.append(" and t.load_no");
	            	sf.append(" IN ");
	            	sf.append("    (select distinct load_no ");
	            	sf.append("     From TRANS_SHIPMENT_HEADER ");
					sf.append("     Where reverse(shpm_no) like ");
					sf.append(" reverse('%"); 
					sf.append(sqlUtil.decode(req.getParameter("SHPM_NO")));
					sf.append("%' )) ");
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NAME"))){
					sf.append(" and t.LOAD_NO");
	            	sf.append(" IN ");
	            	sf.append("    (select distinct LOAD_NO ");
	            	sf.append("     From TRANS_SHIPMENT_HEADER ");
					sf.append("     Where LOAD_NAME like ");
					sf.append(" '%"); 
					sf.append(sqlUtil.decode(req.getParameter("LOAD_NAME")));
					sf.append("%' ) ");
				}
				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_NAME"))){
					sf.append(" and t.LOAD_NO");
	            	sf.append(" IN ");
	            	sf.append("    (select distinct LOAD_NO ");
	            	sf.append("     From TRANS_SHIPMENT_HEADER ");
					sf.append("     Where UNLOAD_NAME like ");
					sf.append(" '%"); 
					sf.append(sqlUtil.decode(req.getParameter("UNLOAD_NAME")));
					sf.append("%' ) ");
				}
				if(ObjUtil.isNotNull(req.getParameter("REFENENCE1"))){
					sf.append(" and t.LOAD_NO");
					sf.append(" in");
					sf.append(" (select distinct load_no from TRANS_SHIPMENT_HEADER where reverse(refenence1) like reverse('%");
					sf.append(sqlUtil.decode(req.getParameter("REFENENCE1")));
					sf.append("%'))");
				}
				if(ObjUtil.isNotNull(req.getParameter("BIZ_TYP"))){
					sf.append(" and t.load_no in");
					sf.append(" (select distinct load_no from TRANS_SHIPMENT_HEADER where BIZ_TYP='");
					sf.append(req.getParameter("BIZ_TYP"));
					sf.append("')");
				}
				if(!ObjUtil.isNotNull(req.getParameter("SHPM_NO")) && !ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))
						&& !ObjUtil.isNotNull(req.getParameter("LOAD_NO"))){
					sf.append(" order by t.ADDTIME desc");      //wangjun 2011-4-21
				}

				String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",TRANS_SRVC_ID_NAME,VEHICLE_TYP_ID_NAME,LOAD_STAT_NAME,TEMP_NO1_NAME,TEMP_NO2_NAME,GPS_NO1_NAME,AUDIT_STAT_NAME,ACCOUNT_STAT_NAME";
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("CHECK_FLAG", Hibernate.YES_NO);
				query  = sqlUtil.getQuery(sf.toString(), field, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//isCustSize = true;			
				//LoginContent.getInstance().setPageInfo(sqlUtil.getRecordByQuery(sf.toString()));
			}
			if (dsid.equals("V_LOAD_HEADER1")) {   
				//车辆检查->调度单
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("TRANS_LOAD_HEADER",sqlUtil.getTableAlias());
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(",t1.SRVC_NAME AS TRANS_SRVC_ID_NAME,t2.VEHICLE_TYPE AS VEHICLE_TYP_ID_NAME");
				sf.append(",case CHECK_FLAG when 'Y' then '已车检' else '未车检' end as CHECK_FLAG_NAME");
				sf.append(",case ACCOUNT_STAT WHEN '20' then '已对账' else '未对账' end as ACCOUNT_STAT_NAME");
				sf.append(",case FEEAUDIT_STAT WHEN '20' then '已审核' else '未审核' end as FEEAUDIT_STAT_NAME");
		        sf.append(",t3.NAME_C as AUDIT_STAT_NAME");
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from TRANS_LOAD_HEADER_H t ");
				}
				else {
					sf.append(" from TRANS_LOAD_HEADER t ");
				}
				sf.append(",BAS_TRANS_SERVICE t1");
				sf.append(",BAS_VEHICLE_TYPE t2");
				sf.append(",BAS_CODES t3");
				sf.append(" where t.TRANS_SRVC_ID = t1.id");
				sf.append(" and t.VEHICLE_TYP_ID = t2.id(+)");
				sf.append(" and t.AUDIT_STAT = t3.code(+) and t3.prop_code(+) = 'APPROVE_STS'");
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));        //供应商
				//sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));            //状态 从 
				//sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),req.getParameter("C_RDC_FLAG")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						if(ObjUtil.isNotNull(req.getParameter("EXEC_FLAG")) && req.getParameter("EXEC_FLAG").equals("true")) {
			            	sf.append(" and exists ");
			            	sf.append("    (SELECT 'x' ");
			            	sf.append("     From bas_org ");
			            	sf.append("     ,trans_shipment_header t1");
							sf.append("     Where t1.load_no = t.load_no and t1.SIGN_ORG_ID = bas_org.id AND (bas_org.ORG_INDEX Like '%,"); //
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("%'");
							sf.append("))");
						}
						else {
							sf.append(" and exists ");
			            	sf.append("    (SELECT 'x' ");
			            	sf.append("     From bas_org ");
							sf.append("     Where t.EXEC_ORG_ID = bas_org.id AND (bas_org.ORG_INDEX Like '%,"); //
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("%'");
							sf.append("))");
						}
					}
					else {
						if(ObjUtil.isNotNull(req.getParameter("EXEC_FLAG")) && req.getParameter("EXEC_FLAG").equals("true")) {
							sf.append(" and (SIGN_ORG_ID = '");
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("'");
							sf.append(")");
						}
						else {
							sf.append(" and (EXEC_ORG_ID = '");
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("'");
							sf.append(")");
						}
					}
				}
				//fanglm 2011-10-09 用户的客户权限
				/*if(ObjUtil.isNotNull(user.getUSER_CUSTOMER())){
					sf.append(" and t.load_no in ");
					sf.append("(select load_no from ");
					if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
						sf.append(" trans_shipment_header_h");
					}
					else {
						sf.append(" trans_shipment_header");
					}
					sf.append(" where customer_id ");
					sf.append(" in (");
					sf.append(user.getUSER_CUSTOMER());
					sf.append("))");
				}*/
				//yuanlei
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
//				sf.append(sqlUtil.addALikeSQL("START_AREA_NAME", req.getParameter("START_AREA_NAME")));
				sf.append(sqlUtil.addEqualSQL("START_AREA_ID", req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addEqualSQL("ADDWHO", req.getParameter("ADDWHO")));  //客户
//				sf.append(sqlUtil.addALikeSQL("END_AREA_NAME", req.getParameter("END_AREA_NAME"))); 
				sf.append(sqlUtil.addEqualSQL("END_AREA_ID", req.getParameter("END_AREA_ID")));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME"), ">="));//wangjun 2011-4-21
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("END_LOAD_TIME_FROM"), ">="));//发运时间从
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("END_LOAD_TIME_TO"),"<="));//发运时间到
				sf.append(sqlUtil.addTimeSQL("ACCOUNT_TIME", req.getParameter("ACCOUNT_TIME_FROM"), ">="));//发运时间从
				sf.append(sqlUtil.addTimeSQL("ACCOUNT_TIME", req.getParameter("ACCOUNT_TIME_TO"),"<="));//发运时间到
				sf.append(sqlUtil.addTimeSQL("FEEAUDIT_TIME", req.getParameter("AUDIT_TIME_FROM"), ">="));//发运时间从
				sf.append(sqlUtil.addTimeSQL("FEEAUDIT_TIME", req.getParameter("AUDIT_TIME_TO"),"<="));//发运时间到
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID",req.getParameter("SUPLR_ID")));//供应商
				sf.append(sqlUtil.addEqualSQL("ADDWHO",req.getParameter("ADDWHO")));
				sf.append(sqlUtil.addALikeSQL("SUPLR_NAME",req.getParameter("SUPLR_NAME")));//供应商
				sf.append(sqlUtil.addALikeSQL("PLATE_NO",req.getParameter("PLATE_NO")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addEqualSQL("DISPATCH_STAT_NAME",req.getParameter("DISPATCH_STAT_NAME")));
				sf.append(sqlUtil.addEqualSQL("DISPATCH_STAT",req.getParameter("DISPATCH_STAT")));//--wangjun 2010-2-27
				sf.append(sqlUtil.addEqualSQL("ACCOUNT_STAT",req.getParameter("ACCOUNT_STAT")));//--wangjun 2010-2-27
				sf.append(sqlUtil.addEqualSQL("FEEAUDIT_STAT",req.getParameter("FEEAUDIT_STAT")));//--wangjun 2010-2-27
				sf.append(sqlUtil.addTimeSQL("PRE_DEPART_TIME", req.getParameter("PRE_DEPART_TIME"), ">="));
				sf.append(sqlUtil.addTimeSQL("PRE_DEPART_TIME", req.getParameter("PRE_DEPART_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("PRE_PICKUP_TIME", req.getParameter("PRE_PICKUP_TIME"), ">="));
				sf.append(sqlUtil.addTimeSQL("PRE_PICKUP_TIME", req.getParameter("PRE_PICKUP_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));  //发货方代码
				sf.append(sqlUtil.addEqualSQL("QUALIFIED_FLAG", req.getParameter("QUALIFIED_FLAG")));  
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				if(ObjUtil.isNotNull(req.getParameter("CHECK_FLAG"))){
					if(req.getParameter("CHECK_FLAG").equals("Y")){
						sf.append(sqlUtil.addEqualSQL("CHECK_FLAG", req.getParameter("CHECK_FLAG")));
					}else{
						sf.append(" and ( t.CHECK_FLAG = 'N' or t.CHECK_FLAG is null )");
					}
					
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FROM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FROM")));
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FROM"),">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))){
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addLeftLikeSQL("t.LOAD_NO", req.getParameter("LOAD_NO")));           //调度单号
				if(ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))){
					sf.append(" and t.load_no");
	            	sf.append(" IN ");
	            	sf.append("    (select distinct load_no ");
	            	if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
	            		sf.append("     From TRANS_SHIPMENT_HEADER_H ");
	            	}
	            	else {
	            		sf.append("     From TRANS_SHIPMENT_HEADER ");
	            	}
					sf.append("     Where reverse(custom_odr_no) like ");
					sf.append(" reverse('%"); 
					sf.append(sqlUtil.decode(req.getParameter("CUSTOM_ODR_NO")));
					sf.append("')) ");
					
				}
				//sf.append(sqlUtil.addLikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));//作业单编号  --yuanlei 2011-2-18
				//yuanlei
				if(ObjUtil.isNotNull(req.getParameter("SHPM_NO"))){
					sf.append(" and t.load_no");
	            	sf.append(" IN ");
	            	sf.append("    (select distinct load_no ");
	            	sf.append("     From TRANS_SHIPMENT_HEADER ");
					sf.append("     Where reverse(shpm_no) like ");
					sf.append(" reverse('%"); 
					sf.append(sqlUtil.decode(req.getParameter("SHPM_NO")));
					sf.append("%' )) ");
				}
				
				if(ObjUtil.isNotNull(req.getParameter("REFENENCE1"))){
					sf.append(" and t.LOAD_NO");
					sf.append(" in");
					sf.append(" (select distinct load_no from TRANS_SHIPMENT_HEADER where reverse(refenence1) like reverse('%");
					sf.append(sqlUtil.decode(req.getParameter("REFENENCE1")));
					sf.append("%'))");
				}
				if(ObjUtil.isNotNull(req.getParameter("BIZ_TYP"))){
					sf.append(" and t.load_no in");
					sf.append(" (select distinct load_no from TRANS_SHIPMENT_HEADER where BIZ_TYP='");
					sf.append(req.getParameter("BIZ_TYP"));
					sf.append("')");
				}
				if(!ObjUtil.isNotNull(req.getParameter("SHPM_NO")) && !ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))
						&& !ObjUtil.isNotNull(req.getParameter("LOAD_NO"))){
					sf.append(" order by t.ADDTIME desc");      //wangjun 2011-4-21
				}
				String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",TRANS_SRVC_ID_NAME,VEHICLE_TYP_ID_NAME,CHECK_FLAG_NAME,ACCOUNT_STAT_NAME,FEEAUDIT_STAT_NAME,AUDIT_STAT_NAME";
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("CHECK_FLAG", Hibernate.YES_NO);
				map.put("QUALIFIED_FLAG", Hibernate.YES_NO);
				query  = sqlUtil.getQuery(sf.toString(), field, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//isCustSize = true;			
				//LoginContent.getInstance().setPageInfo(sqlUtil.getRecordByQuery(sf.toString()));
			}
			if (dsid.equals("BMS_LOAD_HEADER")) {   
				//车辆检查->调度单
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("BMS_LOAD_HEADER",sqlUtil.getTableAlias());
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(",t1.SRVC_NAME AS TRANS_SRVC_ID_NAME,t2.VEHICLE_TYPE AS VEHICLE_TYP_ID_NAME");
				sf.append(",case CHECK_FLAG when 'Y' then '已车检' else '未车检' end as CHECK_FLAG_NAME");
				sf.append(",case ACCOUNT_STAT WHEN '20' then '已对账' else '未对账' end as ACCOUNT_STAT_NAME");
				sf.append(",case FEEAUDIT_STAT WHEN '20' then '已审核' else '未审核' end as FEEAUDIT_STAT_NAME");
		        sf.append(",t3.NAME_C as AUDIT_STAT_NAME");
		        sf.append(" from BMS_LOAD_HEADER t ");
				sf.append(",BAS_TRANS_SERVICE t1");
				sf.append(",BAS_VEHICLE_TYPE t2");
				sf.append(",BAS_CODES t3");
				sf.append(" where t.TRANS_SRVC_ID = t1.id");
				sf.append(" and t.VEHICLE_TYP_ID = t2.id(+)");
				sf.append(" and t.AUDIT_STAT = t3.code(+) and t3.prop_code(+) = 'APPROVE_STS'");
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));        //供应商
				//sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));            //状态 从 
				//sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),req.getParameter("C_RDC_FLAG")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						if(ObjUtil.isNotNull(req.getParameter("EXEC_FLAG")) && req.getParameter("EXEC_FLAG").equals("true")) {
			            	sf.append(" and exists ");
			            	sf.append("    (SELECT 'x' ");
			            	sf.append("     From bas_org ");
			            	sf.append("     ,trans_shipment_header t1");
							sf.append("     Where t1.load_no = t.load_no and t1.SIGN_ORG_ID = bas_org.id AND (bas_org.ORG_INDEX Like '%,"); //
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("%'");
							sf.append("))");
						}
						else {
							sf.append(" and exists ");
			            	sf.append("    (SELECT 'x' ");
			            	sf.append("     From bas_org ");
							sf.append("     Where t.EXEC_ORG_ID = bas_org.id AND (bas_org.ORG_INDEX Like '%,"); //
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("%'");
							sf.append("))");
						}
					}
					else {
						if(ObjUtil.isNotNull(req.getParameter("EXEC_FLAG")) && req.getParameter("EXEC_FLAG").equals("true")) {
							sf.append(" and (SIGN_ORG_ID = '");
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("'");
							sf.append(")");
						}
						else {
							sf.append(" and (EXEC_ORG_ID = '");
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("'");
							sf.append(")");
						}
					}
				}
				//fanglm 2011-10-09 用户的客户权限
				/*if(ObjUtil.isNotNull(user.getUSER_CUSTOMER())){
					sf.append(" and t.load_no in ");
					sf.append("(select load_no from ");
					if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
						sf.append(" trans_shipment_header_h");
					}
					else {
						sf.append(" trans_shipment_header");
					}
					sf.append(" where customer_id ");
					sf.append(" in (");
					sf.append(user.getUSER_CUSTOMER());
					sf.append("))");
				}*/
				//yuanlei
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
//				sf.append(sqlUtil.addALikeSQL("START_AREA_NAME", req.getParameter("START_AREA_NAME")));
				sf.append(sqlUtil.addEqualSQL("START_AREA_ID", req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addEqualSQL("ADDWHO", req.getParameter("ADDWHO")));  //客户
//				sf.append(sqlUtil.addALikeSQL("END_AREA_NAME", req.getParameter("END_AREA_NAME"))); 
				sf.append(sqlUtil.addEqualSQL("END_AREA_ID", req.getParameter("END_AREA_ID")));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME"), ">="));//wangjun 2011-4-21
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("ACCOUNT_TIME", req.getParameter("ACCOUNT_TIME_FROM"), ">="));//发运时间从
				sf.append(sqlUtil.addTimeSQL("ACCOUNT_TIME", req.getParameter("ACCOUNT_TIME_TO"),"<="));//发运时间到
				sf.append(sqlUtil.addTimeSQL("FEEAUDIT_TIME", req.getParameter("AUDIT_TIME_FROM"), ">="));//发运时间从
				sf.append(sqlUtil.addTimeSQL("FEEAUDIT_TIME", req.getParameter("AUDIT_TIME_TO"),"<="));//发运时间到
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID",req.getParameter("SUPLR_ID")));//供应商
				sf.append(sqlUtil.addEqualSQL("ADDWHO",req.getParameter("ADDWHO")));
				sf.append(sqlUtil.addALikeSQL("SUPLR_NAME",req.getParameter("SUPLR_NAME")));//供应商
				sf.append(sqlUtil.addALikeSQL("PLATE_NO",req.getParameter("PLATE_NO")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addEqualSQL("DISPATCH_STAT_NAME",req.getParameter("DISPATCH_STAT_NAME")));
				sf.append(sqlUtil.addEqualSQL("DISPATCH_STAT",req.getParameter("DISPATCH_STAT")));//--wangjun 2010-2-27
				sf.append(sqlUtil.addEqualSQL("ACCOUNT_STAT",req.getParameter("ACCOUNT_STAT")));//--wangjun 2010-2-27
				sf.append(sqlUtil.addEqualSQL("FEEAUDIT_STAT",req.getParameter("FEEAUDIT_STAT")));//--wangjun 2010-2-27
				sf.append(sqlUtil.addTimeSQL("PRE_DEPART_TIME", req.getParameter("PRE_DEPART_TIME"), ">="));
				sf.append(sqlUtil.addTimeSQL("PRE_DEPART_TIME", req.getParameter("PRE_DEPART_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("PRE_PICKUP_TIME", req.getParameter("PRE_PICKUP_TIME"), ">="));
				sf.append(sqlUtil.addTimeSQL("PRE_PICKUP_TIME", req.getParameter("PRE_PICKUP_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));  //发货方代码
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				if(ObjUtil.isNotNull(req.getParameter("CHECK_FLAG"))){
					if(req.getParameter("CHECK_FLAG").equals("Y")){
						sf.append(sqlUtil.addEqualSQL("CHECK_FLAG", req.getParameter("CHECK_FLAG")));
					}else{
						sf.append(" and ( t.CHECK_FLAG = 'N' or t.CHECK_FLAG is null )");
					}
					
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FROM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FROM")));
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FROM"),">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))){
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addLeftLikeSQL("t.LOAD_NO", req.getParameter("LOAD_NO")));           //调度单号
				if(ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))){
					sf.append(" and t.load_no");
	            	sf.append(" IN ");
	            	sf.append("    (select distinct load_no ");
	            	if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
	            		sf.append("     From TRANS_SHIPMENT_HEADER_H ");
	            	}
	            	else {
	            		sf.append("     From TRANS_SHIPMENT_HEADER ");
	            	}
					sf.append("     Where reverse(custom_odr_no) like ");
					sf.append(" reverse('%"); 
					sf.append(sqlUtil.decode(req.getParameter("CUSTOM_ODR_NO")));
					sf.append("')) ");
					
				}
				//sf.append(sqlUtil.addLikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));//作业单编号  --yuanlei 2011-2-18
				//yuanlei
				if(ObjUtil.isNotNull(req.getParameter("SHPM_NO"))){
					sf.append(" and t.load_no");
	            	sf.append(" IN ");
	            	sf.append("    (select distinct load_no ");
	            	sf.append("     From TRANS_SHIPMENT_HEADER ");
					sf.append("     Where reverse(shpm_no) like ");
					sf.append(" reverse('%"); 
					sf.append(sqlUtil.decode(req.getParameter("SHPM_NO")));
					sf.append("%' )) ");
				}
				
				if(ObjUtil.isNotNull(req.getParameter("REFENENCE1"))){
					sf.append(" and t.LOAD_NO");
					sf.append(" in");
					sf.append(" (select distinct load_no from TRANS_SHIPMENT_HEADER where reverse(refenence1) like reverse('%");
					sf.append(sqlUtil.decode(req.getParameter("REFENENCE1")));
					sf.append("%'))");
				}
				if(ObjUtil.isNotNull(req.getParameter("BIZ_TYP"))){
					sf.append(" and t.load_no in");
					sf.append(" (select distinct load_no from TRANS_SHIPMENT_HEADER where BIZ_TYP='");
					sf.append(req.getParameter("BIZ_TYP"));
					sf.append("')");
				}
				if(!ObjUtil.isNotNull(req.getParameter("SHPM_NO")) && !ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))
						&& !ObjUtil.isNotNull(req.getParameter("LOAD_NO"))){
					sf.append(" order by t.ADDTIME desc");      //wangjun 2011-4-21
				}
				String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",TRANS_SRVC_ID_NAME,VEHICLE_TYP_ID_NAME,CHECK_FLAG_NAME,ACCOUNT_STAT_NAME,FEEAUDIT_STAT_NAME,AUDIT_STAT_NAME";
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("CHECK_FLAG", Hibernate.YES_NO);
				map.put("BILL_FLAG", Hibernate.YES_NO);
				map.put("INIT_FLAG", Hibernate.YES_NO);
				query  = sqlUtil.getQuery(sf.toString(), field, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//isCustSize = true;			
				//LoginContent.getInstance().setPageInfo(sqlUtil.getRecordByQuery(sf.toString()));
			}
			else if(dsid.equals("V_SHIPMENT_HEADER_2")) {
				key = "BIZ_TYP_NAME,ODR_TYP_NAME,REFENENCE4_NAME,VEHICLE_TYP_ID_NAME";
				value = "t1.NAME_C,t2.NAME_C,t5.NAME_C,t3.VEHICLE_TYPE";
				alias = "t";
				//调度配载->待调作业单
				sqlUtil.setTableAlias("t");
				//String keys = sqlUtil.getColName("TRANS_SHIPMENT_HEADER",sqlUtil.getTableAlias());
				
				String fields = LoginContent.getInstance().getListCfg().get(StaticRef.V_SHIPMENT_HEADER_UNLOAD).get("FIELD");
				String keys = HSQL.getInstance().setSQL(fields).addSQL("BIZ_TYP_NAME", "t1.NAME_C").addSQL("ODR_TYP_NAME", "t2.NAME_C")
				         .addSQL("REFENENCE4_NAME", "t5.NAME_C").addSQL("VEHICLE_TYP_ID_NAME", "t3.VEHICLE_TYPE").getSQL();
				
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys.replace(",NOTES", ",t.NOTES"));
				//sf.append(keys);
				//sf.append(",t1.NAME_C AS BIZ_TYP_NAME,t2.NAME_C AS ODR_TYP_NAME,t5.NAME_C as REFENENCE4_NAME");
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from TRANS_SHIPMENT_HEADER_H t");
				}
				else {
					sf.append(" from TRANS_SHIPMENT_HEADER t");
				}
				sf.append(",BAS_CODES t1");
				sf.append(",BAS_CODES t2");
				sf.append(",BAS_CODES t5");
				sf.append(",BAS_VEHICLE_TYPE t3");
//				if(ObjUtil.isNotNull(req.getParameter("LOAD_CODE"))) {
//					sf.append(",BAS_ADDRESS t3");
//				}
//				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_CODE"))) {
//					sf.append(",BAS_ADDRESS t4");
//				}
				sf.append(" where t.BIZ_TYP = t1.id and t1.prop_code = 'BIZ_TYP'");
				sf.append(" and t.ODR_TYP = t2.id and t2.prop_code = 'TRS_TYP'");
				sf.append(" and t.REFENENCE4 = t5.id(+)");
				sf.append(" and t.VEHICLE_TYP_ID = t3.id(+)");
//				if(ObjUtil.isNotNull(req.getParameter("LOAD_CODE"))) {
//					sf.append(" and t.LOAD_ID = t3.ID");
//				}
//				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_CODE"))) {
//					sf.append(" and t.UNLOAD_ID = t4.ID");
//				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));     //调度单号条件：要么有值，要么值为NULL
				}
				if(ObjUtil.isNotNull(req.getParameter("TRANS_TYPE"))) {
					sf.append(sqlUtil.addALikeSQL("TRANS_TYPE", req.getParameter("TRANS_TYPE")));     //调度单号条件：要么有值，要么值为NULL
				}
				
				if(ObjUtil.isNotNull(req.getParameter("VEHICLE_TYP_ID"))) {
					sf.append(sqlUtil.addALikeSQL("VEHICLE_TYP_ID", req.getParameter("VEHICLE_TYP_ID")));     //调度单号条件：要么有值，要么值为NULL
				}
//				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
//					sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));     //调度单号条件：要么有值，要么值为NULL
//				}
//				
				
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_NAME", req.getParameter("CUSTOMER_NAME")));//客户名称
//				sf.append(sqlUtil.addFlagSQL("UDF6", req.getParameter("UDF6"),false));
				//sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
//				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_NAME2", req.getParameter("LOAD_AREA_NAME")));//发货区域
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID")));//发货区域
				
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID2")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID3", req.getParameter("LOAD_AREA_ID3")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));//收获区域
			    sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID3", req.getParameter("UNLOAD_AREA_ID3")));
//				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_NAME2", req.getParameter("UNLOAD_AREA_NAME")));//收获区域
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID")));//收获区域
				sf.append(sqlUtil.addEqualSQL("ASSIGN_STAT", req.getParameter("ASSIGN_STAT_NAME")));//派发状态
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID",req.getParameter("TRANS_SRVC_ID")));//运输服务
				sf.append(sqlUtil.addEqualSQL("SUPLR_NAME", req.getParameter("SUPLR_NAME")));//供应商
//				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
//				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  //客户单号
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addALikeSQL("LOAD_ID", req.getParameter("LOAD_ID")));  //发货方代码
				sf.append(sqlUtil.addEqualSQL("LOAD_REGION", req.getParameter("LOAD_REGION")));//提货业务区域
				sf.append(sqlUtil.addEqualSQL("UNLOAD_REGION", req.getParameter("UNLOAD_REGION")));//配送业务区域
//				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_CODE"))) {
//					sf.append(" and t.UNLOAD_ID = t4.ID");
//				}
				//sf.append(sqlUtil.addForeignLikeSQL("ADDR_CODE", req.getParameter("LOAD_CODE"),"t3"));  //发货方代码
				sf.append(sqlUtil.addALikeSQL("UNLOAD_ID", req.getParameter("UNLOAD_ID"))); 
				sf.append(sqlUtil.addALikeSQL("LOAD_CODE", req.getParameter("LOAD_CODE")));  //发货方代码
				sf.append(sqlUtil.addALikeSQL("UNLOAD_CODE", req.getParameter("UNLOAD_CODE")));  //收货方代码
				//sf.append(sqlUtil.addForeignLikeSQL("ADDR_CODE", req.getParameter("UNLOAD_CODE"),"t4"));  //收货方代码
				sf.append(sqlUtil.addALikeSQL("END_UNLOAD_NAME", req.getParameter("END_UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME"), ">="));  //订单时间从
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));  //订单时间从
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<=")); //订单时间到
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));  //发货方代码
				sf.append(sqlUtil.addTimeSQL("PRE_LOAD_TIME", req.getParameter("PRE_LOAD_TIME_FROM"), ">="));  //要求发货时间从
				sf.append(sqlUtil.addTimeSQL("PRE_LOAD_TIME", req.getParameter("PRE_LOAD_TIME_TO"), "<=")); //要求发货时间到
//				sf.append(sqlUtil.addEqualSQL("ADDTIME", req.getParameter("ADDTIME")));
				if(req.getParameter("ADDTIME")!= null){
					sf.append(" AND ADDTIME LIKE TO_DATE('"+req.getParameter("ADDTIME")+"','YYYY-MM-DD')");
				}
//				sf.append(sqlUtil.addTimeSQL("PRE_UNLOAD_TIME",req.getParameter("PRE_UNLOAD_TIME_FROM"), ">="));
//				sf.append(sqlUtil.addTimeSQL("PRE_UNLOAD_TIME",req.getParameter("PRE_UNLOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("ABNOMAL_STAT", req.getParameter("ABNOMAL_STAT")));
				sf.append(sqlUtil.addEqualSQL("REFENENCE4", req.getParameter("REFENENCE4")));				
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID",req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID",req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID",req.getParameter("AREA_ID")));
				sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", req.getParameter("TO_UNLOAD_TIME"), ">="));  //预计到货时间从
				sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", req.getParameter("TO_UNLOAD_TIME_TO"), "<=")); //预计到货时间到
				sf.append(sqlUtil.addALikeSQL("ADDWHO", req.getParameter("ADDWHO")));      //接单员
				sf.append(sqlUtil.addALikeSQL("SKU_NAME", req.getParameter("SKU_NAME"))); //货品
				sf.append(sqlUtil.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));//wangjun 2010-04-25
				sf.append(sqlUtil.addEqualSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME")));//计划回单时间
				sf.append(sqlUtil.addEqualSQL("POD_DELAY_REASON_NAME",req.getParameter("POD_DELAY_REASON_NAME")));//回单延迟原因
				sf.append(sqlUtil.addEqualSQL("DUTYER",req.getParameter("DUTYER") ));
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("SHPM_STATUS")));
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_FROM"), ">=")); //wangjun 2011-4-2
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_TO"), "<=")); 
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_FROM"), ">=")); //wangjun 2011-6-21
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_TO"), "<=")); 
				sf.append(sqlUtil.addEqualSQL("nvl(RECE_FLAG,'N')",req.getParameter("RECE_FLAG")));//运输服务
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));//业务类型
				
				String PRE_UNLOAD_TIME_FROM = req.getParameter("PRE_UNLOAD_TIME_FROM");
				String PRE_UNLOAD_TIME_TO = req.getParameter("PRE_UNLOAD_TIME_TO");
				if(ObjUtil.isNotNull(PRE_UNLOAD_TIME_FROM) && 
						ObjUtil.isNotNull(PRE_UNLOAD_TIME_TO)){
					sf.append(" and ((PRE_UNLOAD_TIME >= to_date('");
					sf.append(PRE_UNLOAD_TIME_FROM);
					sf.append("','yyyy/mm/dd hh24:mi') and PRE_UNLOAD_TIME <= to_date('");
					sf.append(PRE_UNLOAD_TIME_TO);
					sf.append("','yyyy/mm/dd hh24:mi')) or (FROM_UNLOAD_TIME >= to_date('");
					sf.append(PRE_UNLOAD_TIME_FROM);
					sf.append("','yyyy/mm/dd hh24:mi') and FROM_UNLOAD_TIME <= to_date('");
					sf.append(PRE_UNLOAD_TIME_TO);
					sf.append("','yyyy/mm/dd hh24:mi')))");
				}else if(ObjUtil.isNotNull(PRE_UNLOAD_TIME_FROM)){
					sf.append(" and (PRE_UNLOAD_TIME >= to_date('");
					sf.append(PRE_UNLOAD_TIME_FROM);
					sf.append("','yyyy/mm/dd hh24:mi') or FROM_UNLOAD_TIME >= to_date('");
					sf.append(PRE_UNLOAD_TIME_FROM);
					sf.append("','yyyy/mm/dd hh24:mi'))");
				}else if(ObjUtil.isNotNull(PRE_UNLOAD_TIME_TO)){
					sf.append(" and (PRE_UNLOAD_TIME <= to_date('");
					sf.append(PRE_UNLOAD_TIME_TO);
					sf.append("','yyyy/mm/dd hh24:mi') or FROM_UNLOAD_TIME <= to_date('");
					sf.append(PRE_UNLOAD_TIME_TO);
					sf.append("','yyyy/mm/dd hh24:mi'))");
				}
				
				if(ObjUtil.isNotNull(req.getParameter("PRINT_STATUS"))){
					if("48B6B1E0C06647D2AE3C180C1F486BC8".equals(req.getParameter("PRINT_STATUS"))){//wangjun 2011-5-15
						sf.append(" AND LOAD_PRINT_COUNT <> 0  ");
					}else if("B2DD62397ADA41DAB545A9BC648D8A00".equals(req.getParameter("PRINT_STATUS"))){//可打印
						sf.append(" AND PRINT_FLAG='Y' ");
					}else {
						sf.append(" AND LOAD_PRINT_COUNT = 0");
					}
				}
				sf.append(sqlUtil.addAddrRole(user.getROLE_ID_NAME(),user.getUSER_ID()));
				//yuanlei 2012-3-16 提高查询效率
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FROM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FROM")));  //状态
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FROM"), ">=")); //wangjun 2011-4-6
					if(ObjUtil.isNotNull(req.getParameter("STATUS_TO"))) {
						sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"), "<=")); 
					}
					else {
						sf.append(" and status < 92");
					}
				}
				//运输跟踪发运时间
				if(ObjUtil.isNotNull(req.getParameter("PLAN_STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("PLAN_STAT_FROM")) 
						&& req.getParameter("PLAN_STATUS_TO").equals(req.getParameter("PLAN_STAT_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("PLAN_STAT_FROM")));  //状态
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("PLAN_STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("PLAN_STAT_FROM"),">="));
				}
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME",req.getParameter("END_LOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME",req.getParameter("END_LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("PRE_LOAD_TIME",req.getParameter("PRE_LOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("PRE_LOAD_TIME",req.getParameter("PRE_LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addLeftLikeSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));//运单号
				sf.append(sqlUtil.addALikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));        //作业单号
				sf.append(sqlUtil.addLeftLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  //客户单号
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));  //状态
				//yuanlei 2012-09-13 自定义查询增加‘司机’和‘手机号'
				sf.append(sqlUtil.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));     
				//yuanlei
				if(!ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO")) && !ObjUtil.isNotNull(req.getParameter("SHPM_NO"))
						&& !ObjUtil.isNotNull(req.getParameter("ODR_NO"))) {
					if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
						sf.append(" order by t.UNLOAD_SEQ asc");
					} 
					else {
						sf.append(" order by t.addtime desc");
					}
				}
				//String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",BIZ_TYP_NAME,ODR_TYP_NAME,REFENENCE4_NAME";
				query  = sqlUtil.getQuery(sf.toString(), fields, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(ObjUtil.ifNull(req.getParameter("ALL_FLAG"),"false").equals("true")) {
					isCustSize = true;
					page_record = LoginContent.getInstance().getDefPageSize();
				}
			}
			
			
			else if(dsid.equals("V_SHIPMENT_HEADER_1")) {
				key = "BIZ_TYP_NAME,ODR_TYP_NAME,REFENENCE4_NAME,VEHICLE_TYP_ID_NAME";
				value = "t1.NAME_C,t2.NAME_C,t5.NAME_C,t3.VEHICLE_TYPE";
				alias = "t";
				//调度配载->待调作业单
				sqlUtil.setTableAlias("t");
				//String keys = sqlUtil.getColName("TRANS_SHIPMENT_HEADER",sqlUtil.getTableAlias());
				
				String fields = LoginContent.getInstance().getListCfg().get(StaticRef.V_SHIPMENT_HEADER_UNLOAD).get("FIELD");
				String keys = HSQL.getInstance().setSQL(fields).addSQL("BIZ_TYP_NAME", "t1.NAME_C").addSQL("ODR_TYP_NAME", "t2.NAME_C")
				         .addSQL("REFENENCE4_NAME", "t5.NAME_C").addSQL("VEHICLE_TYP_ID_NAME", "t3.VEHICLE_TYPE").getSQL();
				
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys.replace(",NOTES", ",t.NOTES"));
				//sf.append(",t1.NAME_C AS BIZ_TYP_NAME,t2.NAME_C AS ODR_TYP_NAME,t5.NAME_C as REFENENCE4_NAME");
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from TRANS_SHIPMENT_HEADER_H t");
				}
				else {
					sf.append(" from TRANS_SHIPMENT_HEADER t");
				}
				sf.append(",BAS_CODES t1");
				sf.append(",BAS_CODES t2");
				sf.append(",BAS_CODES t5");
				sf.append(",BAS_VEHICLE_TYPE t3");
//				if(ObjUtil.isNotNull(req.getParameter("LOAD_CODE"))) {
//					sf.append(",BAS_ADDRESS t3");
//				}
//				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_CODE"))) {
//					sf.append(",BAS_ADDRESS t4");
//				}
				sf.append(" where t.BIZ_TYP = t1.id and t1.prop_code = 'BIZ_TYP'");
				sf.append(" and t.ODR_TYP = t2.id and t2.prop_code = 'TRS_TYP'");
				sf.append(" and t.REFENENCE4 = t5.id(+)");
				sf.append(" and t.VEHICLE_TYP_ID = t3.id(+)");
//				if(ObjUtil.isNotNull(req.getParameter("LOAD_CODE"))) {
//					sf.append(" and t.LOAD_ID = t3.ID");
//				}
//				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_CODE"))) {
//					sf.append(" and t.UNLOAD_ID = t4.ID");
//				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));     //调度单号条件：要么有值，要么值为NULL
				}
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_NAME", req.getParameter("CUSTOMER_NAME")));//客户名称
//				sf.append(sqlUtil.addFlagSQL("UDF6", req.getParameter("UDF6"),false));
				//sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
//				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_NAME2", req.getParameter("LOAD_AREA_NAME")));//发货区域
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID")));//发货区域
				
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID2")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID3", req.getParameter("LOAD_AREA_ID3")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));//收获区域
			    sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID3", req.getParameter("UNLOAD_AREA_ID3")));
//				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_NAME2", req.getParameter("UNLOAD_AREA_NAME")));//收获区域
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID")));//收获区域
				sf.append(sqlUtil.addEqualSQL("ASSIGN_STAT", req.getParameter("ASSIGN_STAT_NAME")));//派发状态
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID",req.getParameter("TRANS_SRVC_ID")));//运输服务
				sf.append(sqlUtil.addEqualSQL("SUPLR_NAME", req.getParameter("SUPLR_NAME")));//供应商
//				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
//				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  //客户单号
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addALikeSQL("LOAD_ID", req.getParameter("LOAD_ID")));  //发货方代码
				sf.append(sqlUtil.addEqualSQL("LOAD_REGION", req.getParameter("LOAD_REGION")));//提货业务区域
				sf.append(sqlUtil.addEqualSQL("UNLOAD_REGION", req.getParameter("UNLOAD_REGION")));//配送业务区域
//				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_CODE"))) {
//					sf.append(" and t.UNLOAD_ID = t4.ID");
//				}
				//sf.append(sqlUtil.addForeignLikeSQL("ADDR_CODE", req.getParameter("LOAD_CODE"),"t3"));  //发货方代码
				sf.append(sqlUtil.addALikeSQL("LOAD_CODE", req.getParameter("LOAD_CODE")));  //收货方代码
				sf.append(sqlUtil.addALikeSQL("UNLOAD_ID", req.getParameter("UNLOAD_ID")));  
				sf.append(sqlUtil.addALikeSQL("UNLOAD_CODE", req.getParameter("UNLOAD_CODE")));  //收货方代码
				sf.append(sqlUtil.addALikeSQL("END_UNLOAD_NAME", req.getParameter("END_UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME"), ">="));  //订单时间从
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));  //订单时间从
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<=")); //订单时间到
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));  //发货方代码
//				sf.append(sqlUtil.addEqualSQL("ADDTIME", req.getParameter("ADDTIME")));
				if(req.getParameter("ADDTIME")!= null){
					sf.append(" AND ADDTIME LIKE TO_DATE('"+req.getParameter("ADDTIME")+"','YYYY-MM-DD')");
				}
//				sf.append(sqlUtil.addTimeSQL("PRE_UNLOAD_TIME",req.getParameter("PRE_UNLOAD_TIME_FROM"), ">="));
//				sf.append(sqlUtil.addTimeSQL("PRE_UNLOAD_TIME",req.getParameter("PRE_UNLOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("ABNOMAL_STAT", req.getParameter("ABNOMAL_STAT")));
				sf.append(sqlUtil.addEqualSQL("REFENENCE4", req.getParameter("REFENENCE4")));				
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID",req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID",req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID",req.getParameter("AREA_ID")));
				sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", req.getParameter("TO_UNLOAD_TIME"), ">="));  //预计到货时间从
				sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", req.getParameter("TO_UNLOAD_TIME_TO"), "<=")); //预计到货时间到
				sf.append(sqlUtil.addALikeSQL("ADDWHO", req.getParameter("ADDWHO")));      //接单员
				sf.append(sqlUtil.addALikeSQL("SKU_NAME", req.getParameter("SKU_NAME"))); //货品
				sf.append(sqlUtil.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));//wangjun 2010-04-25
				sf.append(sqlUtil.addEqualSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME")));//计划回单时间
				sf.append(sqlUtil.addEqualSQL("POD_DELAY_REASON_NAME",req.getParameter("POD_DELAY_REASON_NAME")));//回单延迟原因
				sf.append(sqlUtil.addEqualSQL("DUTYER",req.getParameter("DUTYER") ));
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("SHPM_STATUS")));
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_FROM"), ">=")); //wangjun 2011-4-2
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_TO"), "<=")); 
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_FROM"), ">=")); //wangjun 2011-6-21
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_TO"), "<=")); 
				sf.append(sqlUtil.addEqualSQL("nvl(RECE_FLAG,'N')",req.getParameter("RECE_FLAG")));//运输服务
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));//业务类型
				
				String PRE_UNLOAD_TIME_FROM = req.getParameter("PRE_UNLOAD_TIME_FROM");
				String PRE_UNLOAD_TIME_TO = req.getParameter("PRE_UNLOAD_TIME_TO");
				if(ObjUtil.isNotNull(PRE_UNLOAD_TIME_FROM) && 
						ObjUtil.isNotNull(PRE_UNLOAD_TIME_TO)){
					sf.append(" and ((PRE_UNLOAD_TIME >= to_date('");
					sf.append(PRE_UNLOAD_TIME_FROM);
					sf.append("','yyyy/mm/dd hh24:mi') and PRE_UNLOAD_TIME <= to_date('");
					sf.append(PRE_UNLOAD_TIME_TO);
					sf.append("','yyyy/mm/dd hh24:mi')) or (FROM_UNLOAD_TIME >= to_date('");
					sf.append(PRE_UNLOAD_TIME_FROM);
					sf.append("','yyyy/mm/dd hh24:mi') and FROM_UNLOAD_TIME <= to_date('");
					sf.append(PRE_UNLOAD_TIME_TO);
					sf.append("','yyyy/mm/dd hh24:mi')))");
				}else if(ObjUtil.isNotNull(PRE_UNLOAD_TIME_FROM)){
					sf.append(" and (PRE_UNLOAD_TIME >= to_date('");
					sf.append(PRE_UNLOAD_TIME_FROM);
					sf.append("','yyyy/mm/dd hh24:mi') or FROM_UNLOAD_TIME >= to_date('");
					sf.append(PRE_UNLOAD_TIME_FROM);
					sf.append("','yyyy/mm/dd hh24:mi'))");
				}else if(ObjUtil.isNotNull(PRE_UNLOAD_TIME_TO)){
					sf.append(" and (PRE_UNLOAD_TIME <= to_date('");
					sf.append(PRE_UNLOAD_TIME_TO);
					sf.append("','yyyy/mm/dd hh24:mi') or FROM_UNLOAD_TIME <= to_date('");
					sf.append(PRE_UNLOAD_TIME_TO);
					sf.append("','yyyy/mm/dd hh24:mi'))");
				}
				
				if(ObjUtil.isNotNull(req.getParameter("PRINT_STATUS"))){
					if("48B6B1E0C06647D2AE3C180C1F486BC8".equals(req.getParameter("PRINT_STATUS"))){//wangjun 2011-5-15
						sf.append(" AND LOAD_PRINT_COUNT <> 0  ");
					}else if("B2DD62397ADA41DAB545A9BC648D8A00".equals(req.getParameter("PRINT_STATUS"))){//可打印
						sf.append(" AND PRINT_FLAG='Y' ");
					}else {
						sf.append(" AND LOAD_PRINT_COUNT = 0");
					}
				}
				sf.append(sqlUtil.addAddrRole(user.getROLE_ID_NAME(),user.getUSER_ID()));
				//yuanlei 2012-3-16 提高查询效率
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FROM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FROM")));  //状态
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FROM"), ">=")); //wangjun 2011-4-6
					if(ObjUtil.isNotNull(req.getParameter("STATUS_TO"))) {
						sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"), "<=")); 
					}
					else {
						sf.append(" and status < 92");
					}
				}
				//运输跟踪发运时间
				if(ObjUtil.isNotNull(req.getParameter("PLAN_STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("PLAN_STAT_FROM")) 
						&& req.getParameter("PLAN_STATUS_TO").equals(req.getParameter("PLAN_STAT_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("PLAN_STAT_FROM")));  //状态
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("PLAN_STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("PLAN_STAT_FROM"),">="));
				}
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME",req.getParameter("END_LOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME",req.getParameter("END_LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("PRE_LOAD_TIME",req.getParameter("PRE_LOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("PRE_LOAD_TIME",req.getParameter("PRE_LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addLeftLikeSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));//运单号
				sf.append(sqlUtil.addALikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));        //作业单号
				sf.append(sqlUtil.addLeftLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  //客户单号
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));  //状态
				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ODR_TYP")));  
				//yuanlei 2012-09-13 自定义查询增加‘司机’和‘手机号'
				sf.append(sqlUtil.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));     
				//yuanlei
				if(!ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO")) && !ObjUtil.isNotNull(req.getParameter("SHPM_NO"))
						&& !ObjUtil.isNotNull(req.getParameter("ODR_NO"))) {
					if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
						sf.append(" order by t.UNLOAD_SEQ asc");
					} 
					else {
						sf.append(" order by t.btch_num,t.customer_id,t.pre_load_time asc");
					}
				}
				//String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",BIZ_TYP_NAME,ODR_TYP_NAME,REFENENCE4_NAME";
				query  = sqlUtil.getQuery(sf.toString(), fields, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(ObjUtil.ifNull(req.getParameter("ALL_FLAG"),"false").equals("true")) {
					isCustSize = true;
					page_record = LoginContent.getInstance().getDefPageSize();
				}
			}
			else if(dsid.equals("V_SHIPMENT_HEADER_LOAD")) {
				//调度配载->作业单头
				sqlUtil.setTableAlias("t");
				//String keys = sqlUtil.getColName("TRANS_SHIPMENT_HEADER",sqlUtil.getTableAlias());
				String fields = LoginContent.getInstance().getListCfg().get(StaticRef.V_SHIPMENT_HEADER_LOAD).get("FIELD");
				String keys = HSQL.getInstance().setSQL(fields).addSQL("BIZ_TYP_NAME", "t1.NAME_C").addSQL("ODR_TYP_NAME", "t2.NAME_C")
				         .addSQL("REFENENCE4_NAME", "t5.NAME_C").addSQL("ROUTE_ID", "t6.ROUTE_NAME").getSQL();
				
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				//sf.append(",t1.NAME_C AS BIZ_TYP_NAME,t2.NAME_C AS ODR_TYP_NAME,t5.NAME_C as REFENENCE4_NAME");
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from TRANS_SHIPMENT_HEADER_H t");
				}
				else {
					sf.append(" from TRANS_SHIPMENT_HEADER t");
				}
				sf.append(",BAS_CODES t1");
				sf.append(",BAS_CODES t2");
				sf.append(",BAS_CODES t5");
				if(ObjUtil.isNotNull(req.getParameter("LOAD_CODE"))) {
					sf.append(",BAS_ADDRESS t3");
				}
				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_CODE"))) {
					sf.append(",BAS_ADDRESS t4");
				}
				sf.append(",bas_route_head t6");
				sf.append(" where t.BIZ_TYP = t1.id and t1.prop_code = 'BIZ_TYP'");
				sf.append(" and t.ODR_TYP = t2.id and t2.prop_code = 'TRS_TYP'");
				sf.append(" and t.REFENENCE4 = t5.id(+)");
				if(ObjUtil.isNotNull(req.getParameter("LOAD_CODE"))) {
					sf.append(" and t.LOAD_ID = t3.ID");
				}
				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_CODE"))) {
					sf.append(" and t.UNLOAD_ID = t4.ID");
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));     //调度单号条件：要么有值，要么值为NULL
				}
				sf.append(" and t.route_id = t6.id(+)");
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_NAME", req.getParameter("CUSTOMER_NAME")));//客户名称
//				sf.append(sqlUtil.addFlagSQL("UDF6", req.getParameter("UDF6"),false));
				//sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID")));//发货区域
				
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID2")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID3", req.getParameter("LOAD_AREA_ID3")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));//收获区域
			    sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID3", req.getParameter("UNLOAD_AREA_ID3")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID")));//收获区域
				sf.append(sqlUtil.addEqualSQL("ASSIGN_STAT", req.getParameter("ASSIGN_STAT_NAME")));//派发状态
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID",req.getParameter("TRANS_SRVC_ID")));//运输服务
				sf.append(sqlUtil.addEqualSQL("SUPLR_NAME", req.getParameter("SUPLR_NAME")));//供应商
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  //客户单号
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addALikeSQL("LOAD_ID", req.getParameter("LOAD_ID")));  //发货方代码
				sf.append(sqlUtil.addEqualSQL("LOAD_REGION", req.getParameter("LOAD_REGION")));//提货业务区域
				sf.append(sqlUtil.addEqualSQL("UNLOAD_REGION", req.getParameter("UNLOAD_REGION")));//配送业务区域
				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_CODE"))) {
					sf.append(" and t.UNLOAD_ID = t4.ID");
				}
				sf.append(sqlUtil.addForeignLikeSQL("ADDR_CODE", req.getParameter("LOAD_CODE"),"t3"));  //发货方代码
				sf.append(sqlUtil.addALikeSQL("UNLOAD_ID", req.getParameter("UNLOAD_ID")));  //收货方代码
				sf.append(sqlUtil.addForeignLikeSQL("ADDR_CODE", req.getParameter("UNLOAD_CODE"),"t4"));  //收货方代码
				sf.append(sqlUtil.addALikeSQL("END_UNLOAD_NAME", req.getParameter("END_UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME"), ">="));  //订单时间从
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));  //订单时间从
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<=")); //订单时间到
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));  //发货方代码
				if(req.getParameter("ADDTIME")!= null){
					sf.append(" AND ADDTIME LIKE TO_DATE('"+req.getParameter("ADDTIME")+"','YYYY-MM-DD')");
				}
				sf.append(sqlUtil.addEqualSQL("ABNOMAL_STAT", req.getParameter("ABNOMAL_STAT")));
				sf.append(sqlUtil.addEqualSQL("REFENENCE4", req.getParameter("REFENENCE4")));				
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID",req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID",req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID",req.getParameter("AREA_ID")));
				sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", req.getParameter("TO_UNLOAD_TIME"), ">="));  //预计到货时间从
				sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", req.getParameter("TO_UNLOAD_TIME_TO"), "<=")); //预计到货时间到
				sf.append(sqlUtil.addALikeSQL("ADDWHO", req.getParameter("ADDWHO")));      //接单员
				sf.append(sqlUtil.addALikeSQL("SKU_NAME", req.getParameter("SKU_NAME"))); //货品
				sf.append(sqlUtil.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));//wangjun 2010-04-25
				sf.append(sqlUtil.addEqualSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME")));//计划回单时间
				sf.append(sqlUtil.addEqualSQL("POD_DELAY_REASON_NAME",req.getParameter("POD_DELAY_REASON_NAME")));//回单延迟原因
				sf.append(sqlUtil.addEqualSQL("DUTYER",req.getParameter("DUTYER") ));
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("SHPM_STATUS")));
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_FROM"), ">=")); //wangjun 2011-4-2
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_TO"), "<=")); 
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_FROM"), ">=")); //wangjun 2011-6-21
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_TO"), "<=")); 
				sf.append(sqlUtil.addEqualSQL("nvl(RECE_FLAG,'N')",req.getParameter("RECE_FLAG")));//运输服务
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));//业务类型
				
				String PRE_UNLOAD_TIME_FROM = req.getParameter("PRE_UNLOAD_TIME_FROM");
				String PRE_UNLOAD_TIME_TO = req.getParameter("PRE_UNLOAD_TIME_TO");
				if(ObjUtil.isNotNull(PRE_UNLOAD_TIME_FROM) && 
						ObjUtil.isNotNull(PRE_UNLOAD_TIME_TO)){
					sf.append(" and ((PRE_UNLOAD_TIME >= to_date('");
					sf.append(PRE_UNLOAD_TIME_FROM);
					sf.append("','yyyy/mm/dd hh24:mi') and PRE_UNLOAD_TIME <= to_date('");
					sf.append(PRE_UNLOAD_TIME_TO);
					sf.append("','yyyy/mm/dd hh24:mi')) or (FROM_UNLOAD_TIME >= to_date('");
					sf.append(PRE_UNLOAD_TIME_FROM);
					sf.append("','yyyy/mm/dd hh24:mi') and FROM_UNLOAD_TIME <= to_date('");
					sf.append(PRE_UNLOAD_TIME_TO);
					sf.append("','yyyy/mm/dd hh24:mi')))");
				}else if(ObjUtil.isNotNull(PRE_UNLOAD_TIME_FROM)){
					sf.append(" and (PRE_UNLOAD_TIME >= to_date('");
					sf.append(PRE_UNLOAD_TIME_FROM);
					sf.append("','yyyy/mm/dd hh24:mi') or FROM_UNLOAD_TIME >= to_date('");
					sf.append(PRE_UNLOAD_TIME_FROM);
					sf.append("','yyyy/mm/dd hh24:mi'))");
				}else if(ObjUtil.isNotNull(PRE_UNLOAD_TIME_TO)){
					sf.append(" and (PRE_UNLOAD_TIME <= to_date('");
					sf.append(PRE_UNLOAD_TIME_TO);
					sf.append("','yyyy/mm/dd hh24:mi') or FROM_UNLOAD_TIME <= to_date('");
					sf.append(PRE_UNLOAD_TIME_TO);
					sf.append("','yyyy/mm/dd hh24:mi'))");
				}
				sf.append(sqlUtil.addAddrRole(user.getROLE_ID_NAME(),user.getUSER_ID()));
				//yuanlei 2012-3-16 提高查询效率
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FROM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FROM")));  //状态
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FROM"), ">=")); //wangjun 2011-4-6
					if(ObjUtil.isNotNull(req.getParameter("STATUS_TO"))) {
						sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"), "<=")); 
					}
					else {
						sf.append(" and status < 92");
					}
				}
				//运输跟踪发运时间
				if(ObjUtil.isNotNull(req.getParameter("PLAN_STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("PLAN_STAT_FROM")) 
						&& req.getParameter("PLAN_STATUS_TO").equals(req.getParameter("PLAN_STAT_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("PLAN_STAT_FROM")));  //状态
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("PLAN_STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("PLAN_STAT_FROM"),">="));
				}
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME",req.getParameter("END_LOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME",req.getParameter("END_LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addLeftLikeSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));//运单号
				sf.append(sqlUtil.addLeftLikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));        //作业单号
				sf.append(sqlUtil.addLeftLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  //客户单号
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));  //状态   
				//yuanlei
				if(!ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO")) && !ObjUtil.isNotNull(req.getParameter("SHPM_NO"))
						&& !ObjUtil.isNotNull(req.getParameter("ODR_NO"))) {
					if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
						sf.append(" order by UNLOAD_SEQ asc");
					} 
					else {
						sf.append(" order by addtime desc");
					}
				}
				//String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",BIZ_TYP_NAME,ODR_TYP_NAME,REFENENCE4_NAME";
				query  = sqlUtil.getQuery(sf.toString(), fields, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(ObjUtil.ifNull(req.getParameter("ALL_FLAG"),"false").equals("true")) {
					isCustSize = true;
					page_record = LoginContent.getInstance().getDefPageSize();
				}
			}	
			else if(dsid.equals("V_SHIPMENT_HEADER")) {
				key ="BIZ_TYP_NAME,ODR_TYP_NAME,REFENENCE4_NAME,ASSIGN_STAT_NAME,VEHICLE_TYP_ID_NAME,ABNOMAL_STAT_NAME,POD_DELAY_REASON_NAME,UNLOAD_DELAY_REASON_NAME";
				value="t1.NAME_C,t2.NAME_C,t5.NAME_C,t6.name_c,t3.VEHICLE_TYPE,t.ABNOMAL_STAT,t.POD_DELAY_REASON,t.UNLOAD_DELAY_REASON";
				alias = "t";
				//调度配载->在途跟踪
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("TRANS_SHIPMENT_HEADER",sqlUtil.getTableAlias());
				//String fields = LoginContent.getInstance().getListCfg().get(StaticRef.V_SHIPMENT_HEADER_SHPM).get("FIELD");
				//String keys = HSQL.getInstance().setSQL(fields).addSQL("BIZ_TYP_NAME", "t1.NAME_C").addSQL("ODR_TYP_NAME", "t2.NAME_C")
				//        .addSQL("REFENENCE4_NAME", "t5.NAME_C").getSQL();
				
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(",t1.NAME_C AS BIZ_TYP_NAME,t2.NAME_C AS ODR_TYP_NAME,t5.NAME_C as REFENENCE4_NAME, t6.name_c as ASSIGN_STAT_NAME,t3.VEHICLE_TYPE as VEHICLE_TYP_ID_NAME");
				sf.append(",t.ABNOMAL_STAT as ABNOMAL_STAT_NAME,t.POD_DELAY_REASON as POD_DELAY_REASON_NAME,t.UNLOAD_DELAY_REASON as UNLOAD_DELAY_REASON_NAME");
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from TRANS_SHIPMENT_HEADER_H t");
				}
				else {
					sf.append(" from TRANS_SHIPMENT_HEADER t");
				}
				sf.append(",BAS_CODES t1");
				sf.append(",BAS_CODES t2");
				sf.append(",BAS_CODES t5");
				sf.append(",BAS_CODES t6");
				sf.append(",BAS_VEHICLE_TYPE t3");
				//sf.append(",bas_address ba");
				//sf.append(",bas_address ba2");
				sf.append(" where t.BIZ_TYP = t1.id and t1.prop_code = 'BIZ_TYP'");
				sf.append(" and t.ODR_TYP = t2.id and t2.prop_code = 'TRS_TYP'");
				sf.append(" and t.REFENENCE4 = t5.id(+)");
				sf.append(" and t.ASSIGN_STAT = t6.code(+) and t6.prop_code(+) = 'ASSIGN_STAT'");
				sf.append(" and vehicle_typ_id = t3.id(+)");
				//sf.append(" and t.LOAD_ID=ba.id and t.UNLOAD_ID=ba2.id");
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));     //调度单号条件：要么有值，要么值为NULL
				}
				if (ObjUtil.isNotNull(req.getParameter("RDC_NO"))) {
					sf.append(sqlUtil.addALikeSQL("RDC_NO", req.getParameter("RDC_NO")));
				}
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("SIGN_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG"), req.getParameter("EXEC_FLAG")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_NAME", req.getParameter("CUSTOMER_NAME")));//客户名称
				//sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
//				sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID_NAME", req.getParameter("EXEC_ORG_ID")));//执行机构
//				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_NAME2", req.getParameter("LOAD_AREA_NAME")));//发货区域
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID")));//发货区域
//				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_NAME2", req.getParameter("UNLOAD_AREA_NAME")));//收获区域
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID")));//收获区域
				sf.append(sqlUtil.addEqualSQL("ASSIGN_STAT", req.getParameter("ASSIGN_STAT_NAME")));//派发状态
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID",req.getParameter("TRANS_SRVC_ID")));//运输服务
				sf.append(sqlUtil.addEqualSQL("SUPLR_NAME", req.getParameter("SUPLR_NAME")));//供应商
//				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
//				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  //客户单号
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addALikeSQL("LOAD_ID", req.getParameter("LOAD_ID")));//发货方代码
				sf.append(sqlUtil.addALikeSQL("UNLOAD_ID", req.getParameter("UNLOAD_ID")));//收货方代码
				sf.append(sqlUtil.addALikeSQL("END_UNLOAD_NAME", req.getParameter("END_UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addEqualSQL("LOAD_REGION_NAME", req.getParameter("LOAD_REGION_NAME")));  //发货方代码
				sf.append(sqlUtil.addEqualSQL("UNLOAD_REGION_NAME", req.getParameter("UNLOAD_REGION_NAME")));  //发货方代码
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME"), ">="));  //订单时间从
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));  //订单时间从
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<=")); //订单时间到
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_TO"),"<="));
//				sf.append(sqlUtil.addFlagSQL("UDF6", req.getParameter("UDF6"),false)); //订单时间到
				sf.append(sqlUtil.addFlagSQL("POD_FLAG", req.getParameter("POD_FLAG"),true));
				sf.append(sqlUtil.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));  //发货方代码
				sf.append(sqlUtil.addEqualSQL("REFENENCE4", req.getParameter("REFENENCE4")));
//				sf.append(sqlUtil.addFlagSQL("SLF_DELIVER_FLAG", req.getParameter("SLF_DELIVER_FLAG")));
//				sf.append(sqlUtil.addFlagSQL("SLF_PICKUP_FLAG", req.getParameter("SLF_PICKUP_FLAG")));
//				sf.append(sqlUtil.addEqualSQL("ADDTIME", req.getParameter("ADDTIME")));
				//if(req.getParameter("ADDTIME")!= null){
				//	sf.append(" AND ADDTIME LIKE TO_DATE('"+req.getParameter("ADDTIME")+"','YYYY-MM-DD')");
				//}
				if(!ObjUtil.isNotNull(req.getParameter("shoud_com_flag"))||"false".equals(req.getParameter("should_com_flag"))){
					//预达时间（从-->到）
					sf.append(sqlUtil.addTimeSQL("PRE_UNLOAD_TIME",req.getParameter("PRE_UNLOAD_TIME_FROM"), ">="));
					sf.append(sqlUtil.addTimeSQL("PRE_UNLOAD_TIME",req.getParameter("PRE_UNLOAD_TIME_TO"),"<="));
				}else{
					sf.append(" and sysdate > to_date(pre_unload_time,'yyyy-mm-dd hh24:mi') and status >=40 and status <50");
				}
				sf.append(sqlUtil.addEqualSQL("ABNOMAL_STAT", req.getParameter("ABNOMAL_STAT")));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID",req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID",req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID",req.getParameter("AREA_ID")));
				sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", req.getParameter("TO_UNLOAD_TIME"), ">="));  //预计到货时间从
				sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", req.getParameter("TO_UNLOAD_TIME_TO"), "<=")); //预计到货时间到
				sf.append(sqlUtil.addALikeSQL("ADDWHO", req.getParameter("ADDWHO")));      //接单员
				sf.append(sqlUtil.addALikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addALikeSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addALikeSQL("SKU_NAME", req.getParameter("SKU_NAME"))); //货品
				sf.append(sqlUtil.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));//wangjun 2010-04-25
				sf.append(sqlUtil.addEqualSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME")));//计划回单时间
				sf.append(sqlUtil.addEqualSQL("POD_DELAY_REASON_NAME",req.getParameter("POD_DELAY_REASON_NAME")));//回单延迟原因
				sf.append(sqlUtil.addEqualSQL("DUTYER",req.getParameter("DUTYER") ));
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("SHPM_STATUS")));
//				if(req.getParameter("LOAD_STAT")!=null){
//					if(req.getParameter("LOAD_STAT").equals("10")){
//						sf.append(sqlUtil.addEqualSQL("LOAD_STAT", "未装车"));
//					}else if(req.getParameter("LOAD_STAT").equals("20")){
//						sf.append(sqlUtil.addEqualSQL("LOAD_STAT", "已装车"));
//					}else{
//						sf.append(sqlUtil.addEqualSQL("LOAD_STAT", req.getParameter("LOAD_STAT")));
//					}
//				}
				String load_stat = req.getParameter("LOAD_STAT");
				if ("未装车".equals(sqlUtil.decode(load_stat))) {
					sf.append(" and LOAD_STAT <>'已装车'");
				}else if ("已装车".equals(sqlUtil.decode(load_stat))) {
					sf.append(" and LOAD_STAT ='已装车'");
				}
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_FROM"), ">=")); //wangjun 2011-4-2
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_TO"), "<=")); 
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_FROM"), ">=")); //wangjun 2011-6-21
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_TO"), "<=")); 
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));//业务类型
				/*if(ObjUtil.isNotNull(req.getParameter("LOAD_ID"))){
					sf.append(" AND ba.ADDR_CODE LIKE '%"+req.getParameter("LOAD_ID")+"%'");
				}
				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_ID"))){
					sf.append(" AND ba2.ADDR_CODE LIKE '%"+req.getParameter("UNLOAD_ID")+"%'");
				}*/
				
				if(ObjUtil.isNotNull(req.getParameter("PRINT_STATUS"))){
					if("48B6B1E0C06647D2AE3C180C1F486BC8".equals(req.getParameter("PRINT_STATUS"))){//wangjun 2011-5-15
						sf.append(" AND LOAD_PRINT_COUNT <> 0  ");
					}else if("B2DD62397ADA41DAB545A9BC648D8A00".equals(req.getParameter("PRINT_STATUS"))){//可打印
						sf.append(" AND PRINT_FLAG='Y' ");
					}else {
						sf.append(" AND LOAD_PRINT_COUNT = 0");
					}
				}
				sf.append(sqlUtil.addAddrRole(user.getROLE_ID_NAME(),user.getUSER_ID()));
				//yuanlei 2012-3-16 提高查询效率
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FROM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FROM")));  //状态
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FROM"), ">=")); //wangjun 2011-4-6
					if(ObjUtil.isNotNull(req.getParameter("STATUS_TO"))) {
						sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"), "<=")); 
					}
					else {
						sf.append(" and status < 92");
					}
				}
				//运输跟踪发运时间
				if(ObjUtil.isNotNull(req.getParameter("PLAN_STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("PLAN_STAT_FROM")) 
						&& req.getParameter("PLAN_STATUS_TO").equals(req.getParameter("PLAN_STAT_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("PLAN_STAT_FROM")));  //状态
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("PLAN_STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("PLAN_STAT_FROM"),">="));
				}
				sf.append(sqlUtil.addTimeSQL("PRE_LOAD_TIME",req.getParameter("PRE_LOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("PRE_LOAD_TIME",req.getParameter("PRE_LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME",req.getParameter("END_LOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME",req.getParameter("END_LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addLeftLikeSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));//运单号
				sf.append(sqlUtil.addALikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));        //作业单号
				sf.append(sqlUtil.addLeftLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  //客户单号
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));  //状态
				sf.append(sqlUtil.addEqualSQL("nvl(RECE_FLAG,'N')",req.getParameter("RECE_FLAG")));//运输服务
				sf.append(sqlUtil.addEqualSQL("UPLOAD_FLAG", req.getParameter("UPLOAD_FLAG")));
				if(ObjUtil.isNotNull(req.getParameter("LOSDAM_FLAG"))&&(req.getParameter("LOSDAM_FLAG").equals("true"))) {
					sf.append(sqlUtil.addEqualSQL("LOSDAM_FLAG", "Y"));
				}
				//yuanlei 2012-09-13 自定义查询增加‘司机’和‘手机号'
				sf.append(sqlUtil.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));     
				//yuanlei
				if(!ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO")) && !ObjUtil.isNotNull(req.getParameter("SHPM_NO"))
						&& !ObjUtil.isNotNull(req.getParameter("ODR_NO"))) {
					sf.append(" order by t.addtime desc");
				}
			
				String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",BIZ_TYP_NAME" +
						",ODR_TYP_NAME,REFENENCE4_NAME,VEHICLE_TYP_ID_NAME,ASSIGN_STAT_NAME,ABNOMAL_STAT_NAME," +
						"POD_DELAY_REASON_NAME,UNLOAD_DELAY_REASON_NAME";

				HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
	            map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
	            map.put("UDF5", Hibernate.YES_NO);
	            map.put("UDF6", Hibernate.YES_NO);
	            map.put("UPLOAD_FLAG", Hibernate.YES_NO);
	            map.put("LOSDAM_FLAG", Hibernate.YES_NO);
	            map.put("UNLOAD_FLAG", Hibernate.YES_NO);
	            map.put("BUK_FLAG", Hibernate.YES_NO);
	            
				query  = sqlUtil.getQuery(sf.toString(), field, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(ObjUtil.ifNull(req.getParameter("ALL_FLAG"),"false").equals("true")) {
					isCustSize = true;
					page_record = LoginContent.getInstance().getDefPageSize();
				}
				
				//isCustSize = true;			
				
				//LoginContent.getInstance().setPageInfo(sqlUtil.getRecordByQuery(sf.toString()));
			}else if(dsid.equals("V_SHIPMENT_HEADER2")){
//				String keys="UNLOAD_ID,UNLOAD_NAME,LOAD_STAT,CREATE_ORG_ID,CREATE_ORG_ID_NAME";
				String keys="UNLOAD_ID,UNLOAD_NAME,STATUS,STATUS_NAME";
				sf=new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(",COUNT(1) as QNTY");
				sf.append(" from TRANS_SHIPMENT_HEADER ");
				sf.append("where 1=1 ");
				sf.append(" and STATUS BETWEEN '40' AND '50'");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
				sf.append(" group by UNLOAD_ID,UNLOAD_NAME,STATUS,STATUS_NAME");
				sf.append(" order by UNLOAD_ID");
				
				String field=keys+",QNTY";
				query=sqlUtil.getQuery(sf.toString(), field, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_SHPMCOMP_HEADER")) {
				//作业单补码
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("TRANS_SHIPMENT_HEADER",sqlUtil.getTableAlias());
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(",t1.NAME_C AS BIZ_TYP_NAME,t2.NAME_C AS ODR_TYP_NAME,t5.NAME_C as REFENENCE4_NAME");
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from TRANS_SHIPMENT_HEADER_H t");
				}
				else {
					sf.append(" from TRANS_SHIPMENT_HEADER t");
				}
				sf.append(",BAS_CODES t1");
				sf.append(",BAS_CODES t2");
				sf.append(",BAS_CODES t5");
				sf.append(",BAS_ADDRESS t6");
				//sf.append(",bas_address ba");
				//sf.append(",bas_address ba2");
				sf.append(" where t.BIZ_TYP = t1.id and t1.prop_code = 'BIZ_TYP'");
				sf.append(" and t.ODR_TYP = t2.id and t2.prop_code = 'TRS_TYP'");
				sf.append(" and t.REFENENCE4 = t5.id(+)");
				sf.append(" and t.UNLOAD_ID = t6.id(+)");
				//sf.append(" and t.LOAD_ID=ba.id and t.UNLOAD_ID=ba2.id");
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));     //调度单号条件：要么有值，要么值为NULL
				}
				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_ID"))){
					sf.append(" and t6.ADDR_CODE = '"+req.getParameter("UNLOAD_ID")+"'");	//分点部代码
				}
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("SIGN_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG"), req.getParameter("EXEC_FLAG")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_NAME", req.getParameter("CUSTOMER_NAME")));//客户名称
				//sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
//				sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID_NAME", req.getParameter("EXEC_ORG_ID")));//执行机构
//				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_NAME2", req.getParameter("LOAD_AREA_NAME")));//发货区域
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID")));//发货区域
//				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_NAME2", req.getParameter("UNLOAD_AREA_NAME")));//收获区域
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID")));//收获区域
				sf.append(sqlUtil.addEqualSQL("ASSIGN_STAT", req.getParameter("ASSIGN_STAT_NAME")));//派发状态
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID",req.getParameter("TRANS_SRVC_ID")));//运输服务
				sf.append(sqlUtil.addEqualSQL("SUPLR_NAME", req.getParameter("SUPLR_NAME")));//供应商
//				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
//				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  //客户单号
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addALikeSQL("END_UNLOAD_NAME", req.getParameter("END_UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addEqualSQL("LOAD_REGION_NAME", req.getParameter("LOAD_REGION_NAME")));  //发货方代码
				sf.append(sqlUtil.addEqualSQL("UNLOAD_REGION_NAME", req.getParameter("UNLOAD_REGION_NAME")));  //发货方代码
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME"), ">="));  //订单时间从
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));  //订单时间从
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<=")); //订单时间到
				sf.append(sqlUtil.addTimeSQL(sqlUtil.getTableAlias()+".ADDTIME", req.getParameter("ADDTIME_FROM"), ">="));//创建时间从
				sf.append(sqlUtil.addTimeSQL(sqlUtil.getTableAlias()+".ADDTIME", req.getParameter("ADDTIME_TO"), "<="));//创建时间到
				sf.append(sqlUtil.addEqualSQL("REFENENCE4", req.getParameter("REFENENCE4")));
				if("Y|S".equals(req.getParameter("UDF6"))){
					sf.append(" and (UDF6 = 'Y' or UDF6 = 'S') ");
				}else{
					sf.append(sqlUtil.addEqualSQL("UDF6", req.getParameter("UDF6"))); //补码状态
				}
				sf.append(sqlUtil.addEqualSQL("BUK_FLAG", req.getParameter("BUK_FLAG"))); //SSS处理标识
				sf.append(sqlUtil.addFlagSQL("POD_FLAG", req.getParameter("POD_FLAG"),true));
				
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));//运单号
				sf.append(sqlUtil.addEqualSQL("ABNOMAL_STAT", req.getParameter("ABNOMAL_STAT")));//运输异常
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));//业务类型
//				sf.append(sqlUtil.addEqualSQL("ADDTIME", req.getParameter("ADDTIME")));
				if(req.getParameter("ADDTIME")!= null){
					sf.append(" AND ADDTIME LIKE TO_DATE('"+req.getParameter("ADDTIME")+"','YYYY-MM-DD')");
				}
				if(!ObjUtil.isNotNull(req.getParameter("shoud_com_flag"))||"false".equals(req.getParameter("should_com_flag"))){
					//预达时间（从-->到）
					sf.append(sqlUtil.addTimeSQL("PRE_UNLOAD_TIME",req.getParameter("PRE_UNLOAD_TIME_FROM"), ">="));
					sf.append(sqlUtil.addTimeSQL("PRE_UNLOAD_TIME",req.getParameter("PRE_UNLOAD_TIME_TO"),"<="));
				}else{
					sf.append(" and sysdate > to_date(pre_unload_time,'yyyy-mm-dd hh24:mi') and status >=40 and status <50");
				}
				sf.append(sqlUtil.addEqualSQL("ABNOMAL_STAT", req.getParameter("ABNOMAL_STAT")));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID",req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID",req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID",req.getParameter("AREA_ID")));
				sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", req.getParameter("TO_UNLOAD_TIME"), ">="));  //预计到货时间从
				sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", req.getParameter("TO_UNLOAD_TIME_TO"), "<=")); //预计到货时间到
				sf.append(sqlUtil.addALikeSQL("ADDWHO", req.getParameter("ADDWHO")));      //接单员
				sf.append(sqlUtil.addALikeSQL("SKU_NAME", req.getParameter("SKU_NAME"))); //货品
				sf.append(sqlUtil.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));//wangjun 2010-04-25
				sf.append(sqlUtil.addEqualSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME")));//计划回单时间
				sf.append(sqlUtil.addEqualSQL("POD_DELAY_REASON_NAME",req.getParameter("POD_DELAY_REASON_NAME")));//回单延迟原因
				sf.append(sqlUtil.addEqualSQL("DUTYER",req.getParameter("DUTYER") ));
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("SHPM_STATUS")));
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_FROM"), ">=")); //wangjun 2011-4-2
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_TO"), "<=")); 
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_FROM"), ">=")); //wangjun 2011-6-21
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_TO"), "<=")); 
				/*if(ObjUtil.isNotNull(req.getParameter("LOAD_ID"))){
					sf.append(" AND ba.ADDR_CODE LIKE '%"+req.getParameter("LOAD_ID")+"%'");
				}
				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_ID"))){
					sf.append(" AND ba2.ADDR_CODE LIKE '%"+req.getParameter("UNLOAD_ID")+"%'");
				}*/
				
				if(ObjUtil.isNotNull(req.getParameter("PRINT_STATUS"))){
					if("48B6B1E0C06647D2AE3C180C1F486BC8".equals(req.getParameter("PRINT_STATUS"))){//wangjun 2011-5-15
						sf.append(" AND LOAD_PRINT_COUNT <> 0  ");
					}else if("B2DD62397ADA41DAB545A9BC648D8A00".equals(req.getParameter("PRINT_STATUS"))){//可打印
						sf.append(" AND PRINT_FLAG='Y' ");
					}else {
						sf.append(" AND LOAD_PRINT_COUNT = 0");
					}
				}
				sf.append(sqlUtil.addAddrRole(user.getROLE_ID_NAME(),user.getUSER_ID()));
				//yuanlei 2012-3-16 提高查询效率
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FROM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FROM")));  //状态
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FROM"), ">=")); //wangjun 2011-4-6
					if(ObjUtil.isNotNull(req.getParameter("STATUS_TO"))) {
						sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"), "<=")); 
					}
					else {
						sf.append(" and status < 92");
					}
				}
				//运输跟踪发运时间
				if(ObjUtil.isNotNull(req.getParameter("PLAN_STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("PLAN_STAT_FROM")) 
						&& req.getParameter("PLAN_STATUS_TO").equals(req.getParameter("PLAN_STAT_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("PLAN_STAT_FROM")));  //状态
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("PLAN_STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("PLAN_STAT_FROM"),">="));
				}
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME",req.getParameter("END_LOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME",req.getParameter("END_LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addLeftLikeSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addALikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));        //作业单号
				sf.append(sqlUtil.addLeftLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  //客户单号
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));  //状态
				sf.append(sqlUtil.addEqualSQL("nvl(RECE_FLAG,'N')",req.getParameter("RECE_FLAG")));//运输服务
				if(ObjUtil.isNotNull(req.getParameter("NULLROUTE_FLAG")) && 
						"true".equals(req.getParameter("NULLROUTE_FLAG"))){
					sf.append(" and ");
					sf.append(sqlUtil.getTableAlias()+".route_id is null ");
				}
				sf.append(sqlUtil.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));  //发货方代码
				//yuanlei 2012-09-13 自定义查询增加‘司机’和‘手机号'
				sf.append(sqlUtil.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));     
				//yuanlei
				if(!ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO")) && !ObjUtil.isNotNull(req.getParameter("SHPM_NO"))
						&& !ObjUtil.isNotNull(req.getParameter("ODR_NO"))) {
					sf.append(" order by addtime desc");
				}
				String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",BIZ_TYP_NAME,ODR_TYP_NAME";
				HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("UDF5", Hibernate.YES_NO);
//	            map.put("UDF6", Hibernate.YES_NO);
				query  = sqlUtil.getQuery(sf.toString(), field, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//isCustSize = true;			
				
				//LoginContent.getInstance().setPageInfo(sqlUtil.getRecordByQuery(sf.toString()));
			}	
			else if(dsid.equals("V_SHIPMENT_ITEM")) {
				//调度配载->作业单明细
				String keys = sqlUtil.getColName("V_SHIPMENT_ITEM");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from V_SHIPMENT_ITEM_H t");
				}
				else {
					sf.append(" from V_SHIPMENT_ITEM t");
				}
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("SHPM_NO", req.getParameter("SHPM_NO")));    //作业单号
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addEqualSQL("SHPM_ROW", req.getParameter("SHPM_ROW")));  //行号
				sf.append(sqlUtil.addEqualSQL("SKU_ID", req.getParameter("SKU_ID")));
				sf.append(sqlUtil.addEqualSQL("SKU_NAME", req.getParameter("SKU_NAME"))); 
				sf.append(sqlUtil.addEqualSQL("SKU_SPEC", req.getParameter("SKU_SPEC")));
				sf.append(sqlUtil.addEqualSQL("UOM", req.getParameter("UOM")));
				sf.append(sqlUtil.addEqualSQL("ODR_QNTY", req.getParameter("ODR_QNTY")));
				sf.append(sqlUtil.addEqualSQL("LD_QNTY", req.getParameter("LD_QNTY")));
				sf.append(sqlUtil.addEqualSQL("UNLD_QNTY", req.getParameter("UNLD_QNTY")));
				sf.append(sqlUtil.addEqualSQL("G_WGT", req.getParameter("G_WGT")));
				sf.append(sqlUtil.addEqualSQL("VOL", req.getParameter("VOL")));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
			}
			else if(dsid.equals("V_SHIPMENT_ITEM_SF")||dsid.equals("V_SHIPMENT_ITEM_SF1")) {
				//作业单管理->作业单明细
				String keys = sqlUtil.getColName("V_SHIPMENT_ITEM_SF");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from V_SHIPMENT_ITEM_H t");
				}
				else {
					sf.append(" from V_SHIPMENT_ITEM_SF t");
				}
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("SHPM_NO", req.getParameter("SHPM_NO")));    //作业单号
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));//运单号
				sf.append(sqlUtil.addEqualSQL("SHPM_ROW", req.getParameter("SHPM_ROW")));  //行号
				sf.append(sqlUtil.addEqualSQL("SKU_ID", req.getParameter("SKU_ID")));
				sf.append(sqlUtil.addEqualSQL("SKU_NAME", req.getParameter("SKU_NAME"))); 
				sf.append(sqlUtil.addEqualSQL("SKU_SPEC", req.getParameter("SKU_SPEC")));
				sf.append(sqlUtil.addEqualSQL("UOM", req.getParameter("UOM")));
				sf.append(sqlUtil.addEqualSQL("ODR_QNTY", req.getParameter("ODR_QNTY")));
				sf.append(sqlUtil.addEqualSQL("LD_QNTY", req.getParameter("LD_QNTY")));
				sf.append(sqlUtil.addEqualSQL("UNLD_QNTY", req.getParameter("UNLD_QNTY")));
				sf.append(sqlUtil.addEqualSQL("G_WGT", req.getParameter("G_WGT")));
				sf.append(sqlUtil.addEqualSQL("VOL", req.getParameter("VOL")));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
			}
			else if (dsid.equals("V_ORDER_HEADER")) {  //托运单
				key = "BIZ_TYP_NAME,ODR_TYP_NAME,PLAN_STAT_NAME";
				value = "t1.NAME_C,t2.NAME_C,t3.NAME_C";
				alias = "t";
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("TRANS_ORDER_HEADER",sqlUtil.getTableAlias());
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(",t1.NAME_C AS BIZ_TYP_NAME,t2.NAME_C AS ODR_TYP_NAME,t3.NAME_C AS PLAN_STAT_NAME");
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from TRANS_ORDER_HEADER_H t");
				}
				else {
					sf.append(" from TRANS_ORDER_HEADER t");
				}
				sf.append(",BAS_CODES t1");
				sf.append(",BAS_CODES t2");
				sf.append(",BAS_CODES t3");
				sf.append(" where t.BIZ_TYP = t1.id and t1.prop_code = 'BIZ_TYP'");
				sf.append(" and t.ODR_TYP = t2.id and t2.prop_code = 'TRS_TYP'");
				sf.append(" and t.PLAN_STAT = t3.CODE(+) and t3.prop_code = 'PLAN_STAT'");
				sf.append(sqlUtil.addEqualSQL("ADDWHO",req.getParameter("ADDWHO")));
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
//				sf.append(sqlUtil.addALikeSQL("EXEC_ORG_ID_NAME", req.getParameter("EXEC_ORG_ID_NAME")));
				sf.append(sqlUtil.addALikeSQL("LOAD_AREA_NAME", req.getParameter("LOAD_AREA_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
				sf.append(sqlUtil.addEqualSQL("UGRT_GRD", req.getParameter("UGRT_GRD")));
				sf.append(sqlUtil.addEqualSQL("EXEC_STAT", req.getParameter("EXEC_STAT")));
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
				sf.append(sqlUtil.addEqualSQL("ADDWHO",req.getParameter("ADDWHO")));
				//sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_CONTACT", req.getParameter("UNLOAD_CONTACT")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_TEL", req.getParameter("UNLOAD_TEL")));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("PRE_LOAD_TIME", req.getParameter("PRE_LOAD_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("PRE_LOAD_TIME", req.getParameter("PRE_LOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("TRS_TYPE", req.getParameter("TRS_TYPE")));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_TO"),"<="));
				
				sf.append(sqlUtil.addEqualSQL("PLAN_STAT", req.getParameter("PLAN_STAT")));
				sf.append(sqlUtil.addEqualSQL("LOAD_STAT", req.getParameter("LOAD_STAT")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_STAT", req.getParameter("UNLOAD_STAT")));
				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ODR_TYP")));
				sf.append(sqlUtil.addEqualSQL("CREATE_ORG_ID_NAME",req.getParameter("CREATE_ORG_ID_NAME")));
				sf.append(sqlUtil.addEqualSQL("ORD_PRO_LEVER",req.getParameter("ORD_PRO_LEVER")));
				sf.append(sqlUtil.addEqualSQL("CREATE_ORG_ID",req.getParameter("CREATE_ORG_ID")));
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ATTR",req.getParameter("CUSTOM_ATTR")));
				sf.append(sqlUtil.addFlagSQL("SLF_PICKUP_FLAG", req.getParameter("SLF_PICKUP_FLAG"),true));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),req.getParameter("C_RDC_FLAG")));				
//				sf.append(sqlUtil.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(sqlUtil.addTimeSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addALikeSQL("BTCH_NUM", req.getParameter("BTCH_NUM")));
				//sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
//				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),""));
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_NAME")));
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FORM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FORM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FORM")));
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FORM"),">="));
				}
				sf.append(sqlUtil.addLeftLikeSQL("SOURCE_NO", req.getParameter("SOURCE_NO")));
				sf.append(sqlUtil.addALikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addLeftLikeSQL("CUSTOMER_NAME", req.getParameter("CUSTOMER_NAME")));
				sf.append(sqlUtil.addLeftLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(" and odr_no in (select odr_no from trans_shipment_header where load_no = '");
					sf.append(req.getParameter("LOAD_NO"));
					sf.append("')");
				}
				sf.append(sqlUtil.addEqualSQL("BUK_FLAG", req.getParameter("BUK_FLAG"))); //SSS处理标识
				sf.append(" order by t.ADDTIME DESC");
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
				map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
				map.put("ODR_TIME", Hibernate.STRING);
				map.put("POD_FLAG", Hibernate.YES_NO);
				map.put("DISCOUNT", Hibernate.DOUBLE);
				map.put("COD_FLAG", Hibernate.YES_NO);
				map.put("BILL_PRICE_FLAG", Hibernate.YES_NO);
				map.put("BUK_FLAG", Hibernate.YES_NO);
				map.put("UPLOAD_FLAG", Hibernate.YES_NO);
				map.put("UNLOAD_FLAG", Hibernate.YES_NO);
				String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",BIZ_TYP_NAME,ODR_TYP_NAME,PLAN_STAT_NAME";
				query  = sqlUtil.getQuery(sf.toString(), field, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//isCustSize = true;
								
			}
			/*else if (dsid.equals("V_ORDER_HEADER")) {
				String keys = sqlUtil.getColName("V_ORDER_HEADER");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from V_ORDER_HEADER_H t");
				}
				else {
					sf.append(" from V_ORDER_HEADER t");
				}
				sf.append(" where 1=1 ");
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
//				sf.append(sqlUtil.addALikeSQL("EXEC_ORG_ID_NAME", req.getParameter("EXEC_ORG_ID_NAME")));
				sf.append(sqlUtil.addALikeSQL("LOAD_AREA_NAME", req.getParameter("LOAD_AREA_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
				sf.append(sqlUtil.addEqualSQL("UGRT_GRD", req.getParameter("UGRT_GRD")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("PLAN_STAT", req.getParameter("PLAN_STAT")));
				sf.append(sqlUtil.addEqualSQL("LOAD_STAT", req.getParameter("LOAD_STAT")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_STAT", req.getParameter("UNLOAD_STAT")));
				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ODR_TYP")));
				sf.append(sqlUtil.addFlagSQL("SLF_PICKUP_FLAG", req.getParameter("SLF_PICKUP_FLAG")));
//				if(!ObjUtil.isNotNull(req.getParameter("ICLUDE_CLOSE")) || "false".equals(req.getParameter("ICLUDE_CLOSE"))){
//					sf.append(" and NVL(STATUS) < 90");
//				}
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),req.getParameter("C_RDC_FLAG")));				
				//包含促销品
				if(!ObjUtil.isNotNull(req.getParameter("C_CX_FLAG")) || req.getParameter("C_CX_FLAG").equals("false")) {
					sf.append(" and substr(ODR_NO,0,2) <> 'CX'");
				}
//				sf.append(sqlUtil.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(sqlUtil.addTimeSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addEqualSQL("PLATE_NO", req.getParameter("PLeclipse-javadoc:%E2%98%82=SGPro/E:%5C/software%5C/eclipseGWT%5C/plugins%5C/com.google.gwt.eclipse.sdkbundle.2.0.1_2.0.1.v201002021445%5C/gwt-2.0.1%5C/gwt-user.jar%3Cjavax.servlet(ServletRequest.class%E2%98%83ServletRequestATE_NO")));
				sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
//				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),""));
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_NAME")));
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FORM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FORM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FORM")));
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FORM"),">="));
				}
				sf.append(sqlUtil.addLeftLikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addLeftLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				//fanglm 2011-4-2 暂时性处理
//				//yuanlei 2013-10-02
				//if(ObjUtil.isNotNull(req.getParameter("ORDER_BY_AREA"))){
					if(!ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO")) && !ObjUtil.isNotNull(req.getParameter("ODR_NO"))) {
						sf.append(" order by unload_area_id,addtime");
					}
				//}
				
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
				map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
				map.put("POD_FLAG", Hibernate.YES_NO);
				Query query  = sqlUtil.getQuery(sf.toString(), keys, map);
				if("true".equals(is_curr_page)){
					if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
						query.setFirstResult(start_row);
						query.setMaxResults(300);
					} else {
						start_row = (Integer.parseInt(CUR_PAGE) - 1) * 300;
						query.setFirstResult(start_row);
						query.setMaxResults(300);
					}
				}
				isCustSize = true;
				
				List<HashMap<String, String>> object = query.list();
		        Gson gson = new Gson();
		        String content = gson.toJson(object);
				p.print(content);
			}*/
			else if (dsid.equals("V_TRANS_CHARGE_TYPE")) {
				//费用种类
				String keys = sqlUtil.getColName("V_TRANS_CHARGE_TYPE");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_TRANS_CHARGE_TYPE ");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addEqualSQL("TRANS_FEE_TYP",req.getParameter("TRANS_FEE_TYP")));
				sf.append(sqlUtil.addEqualSQL("FEE_ATTR",req.getParameter("FEE_ATTR")));
				sf.append(sqlUtil.addFlagSQL("ENABLE_FLAG",req.getParameter("ENABLE_FLAG")));
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("QNTY", Hibernate.FLOAT);
				map.put("VOL", Hibernate.FLOAT);
				map.put("G_WGT", Hibernate.FLOAT);
				sf.append(" order by SHOW_SEQ asc");
				query  = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if (dsid.equals("V_ORDER_ITEM")) {
				String keys = sqlUtil.getColName("V_ORDER_ITEM");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from V_ORDER_ITEM_H ");
				}
				else {
					sf.append(" from V_ORDER_ITEM ");
				}
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));
//				HashMap<String, Type> map = new HashMap<String, Type>();
//				map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
//				map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
//				map.put("POD_FLAG", Hibernate.YES_NO);
				sf.append(" order by odr_row asc");
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

				page_record = 100;
				
			}
			else if (dsid.equals("TRANS_ORDER_ITEM")) {  //托运单明细
				String keys = "ODR_ROW,LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE,UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE,SKU_NAME,SKU,TEMPERATURE1,UOM,QNTY,VOL,G_WGT,NOTES,N_WGT,LOTATT01,REQ_LOAD_TIME,REQ_UNLOAD_TIME";
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(",t1.ID,t2.NAME_C as TEMPERATURE1_NAME, (select NVL(t3.VOL_GWT_RATIO,'0') from BAS_SKU t3 where t3.ID = t1.SKU_ID) as VOL_GWT_RATIO");
				sf.append(" from TRANS_ORDER_ITEM t1,BAS_CODES t2");
				sf.append(" where 1=1 and t1.TEMPERATURE1=t2.ID(+) ");
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(" order by odr_row asc");
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("VOL", Hibernate.FLOAT);
				map.put("G_WGT", Hibernate.FLOAT);
				map.put("N_GWT", Hibernate.FLOAT);
				map.put("LOTATT01", Hibernate.FLOAT);
				map.put("VOL_GWT_RATIO", Hibernate.FLOAT);
				String fields=keys+",ID,TEMPERATURE1_NAME,VOL_GWT_RATIO";
				query  = sqlUtil.getQuery(sf.toString(), fields, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

				page_record = 100;
				
			}
			else if(dsid.equals("V_TRANS_TRACK_TRACE")){
				String shop_no = req.getParameter("SHPM_NO");
				String keys = "";
				if(ObjUtil.isNotNull(shop_no)) {
					keys = sqlUtil.getColName("V_TRACK_TRACE");
				}
				else {
					keys = sqlUtil.getColName("TRANS_TRACK_ONE");
				}
				sf = new StringBuffer();
				sf.append("SELECT ");
				sf.append(keys);
				if(ObjUtil.isNotNull(shop_no))
					sf.append(" FROM V_TRACK_TRACE");
				else
					sf.append(" FROM TRANS_TRACK_ONE");
				sf.append(" WHERE 1 = 1 ");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO",req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("SHPM_NO",shop_no));
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO",req.getParameter("CUSTOM_ODR_NO")));
				sf.append(" ORDER BY TRACE_TIME DESC");
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("CUSTOM_ORDER_QUERY")){
				//客户服务-->订单动态监控-->托运单列表 
				String keys = sqlUtil.getColName("V_CUSTOM_ORDER_Q");
				keys = keys.replaceAll(",", ",t.");
				Object obj = req.getParameter("SKU_NAME");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_CUSTOM_ORDER_Q t ");
				//sf.append("select CUSTOM_ODR_NO,ODR_TIME,TRANS_UOM,TOT_QNTY,PRE_LOAD_TIME,PRE_UNLOAD_TIME,UNLOAD_TIME,UNLOAD_NAME,UNLOAD_ADDRESS,STATUS,PLAN_STAT_NAME,LOAD_STAT_NAME,UNLOAD_STAT_NAME,EXEC_ORG_ID_NAME,t.ODR_NO,t.SKU_NAME,t.NOTES,t.UNLOAD_STAT,t.MOBILE,t.ABNOMAL_STAT,t.ADDTIME,t.ADDWHO");
				if(ObjUtil.isNotNull(obj)) {
					
					sf.append(", TRANS_ORDER_ITEM t_itm");
				}
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(obj)) {
					sf.append(" and t.ODR_NO = t_itm.ODR_NO");
				}
				sf.append(sqlUtil.addALikeSQL("t.CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO"))); 
				sf.append(sqlUtil.addALikeSQL("t.unload_name", req.getParameter("UNLOAD_NAME")));
				sf.append(sqlUtil.addALikeSQL("t_itm.SKU_NAME", req.getParameter("SKU_NAME")));
				sf.append(sqlUtil.addTimeSQL("t.ODR_TIME", req.getParameter("ODR_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("t.ODR_TIME", req.getParameter("ODR_TIME_TO"),"<="));
				sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS"), "<"));
				
				if(ObjUtil.isNotNull(req.getParameter("LOGIN_USER")) 
						&& "经销商".equals(user.getROLE_ID_NAME())){
					sf.append(" and t.unload_id in (select addr_id from sys_user_addr where user_id='");
					sf.append(req.getParameter("LOGIN_USER"));
					sf.append("')");
				}
				
				
				if(ObjUtil.isNotNull(req.getParameter("LD_UNLD_FLAG")) && req.getParameter("LD_UNLD_FLAG").equals("true")) {
//					sf.append(" and t.SHPM_STAT = ");
//					sf.append("'");
//					sf.append(StaticRef.SHPM_LOAD_NAME);
//					sf.append("'");
					sf.append(" and t.status = ");
					sf.append(40);
				}
				if(ObjUtil.isNotNull(req.getParameter("NO_DISPATCH_FLAG")) && req.getParameter("NO_DISPATCH_FLAG").equals("true")) {
//					sf.append(" and t.SHPM_STAT = ");
//					sf.append("'");
//					sf.append(StaticRef.SHPM_CONFIRM_NAME);
//					sf.append("'");
					sf.append(" and t.status = ");
					sf.append(20);
				}
				if(ObjUtil.isNotNull(req.getParameter("DISPATCH_UNLD_FLAG")) && req.getParameter("DISPATCH_UNLD_FLAG").equals("true")) {
					sf.append(" and t.status = ");
					sf.append(30);
				
				}
				
				
//				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_STAT")) && req.getParameter("UNLOAD_STAT").equals("true")) {
//					sf.append(" and t.UNLOAD_STAT < ");
//					sf.append(StaticRef.UNLOADED);
//				}
//				if(ObjUtil.isNotNull(req.getParameter("LOAD_STAT")) && req.getParameter("LOAD_STAT").equals("true")) {
//					sf.append(" and t.LOAD_STAT < ");
//					sf.append(StaticRef.LOADED);
//				}
//				if(ObjUtil.isNotNull(req.getParameter("LOADED_FLAG")) && req.getParameter("LOADED_FLAG").equals("true")) {
//				sf.append(" and t.LOAD_STAT > ");
//					sf.append(StaticRef.NO_LOAD);
//					sf.append(" and t.UNLOAD_STAT   < ");
//					sf.append(StaticRef.UNLOADED);
//				}
//				if(ObjUtil.isNotNull(req.getParameter("NOPLAN_FLAG")) && req.getParameter("NOPLAN_FLAG").equals("true")) {
//					sf.append(" and t.STATUS = ");
//					sf.append(StaticRef.SO_CONFIRM);
//					sf.append(" and t.PLAN_STAT < ");
//					sf.append(StaticRef.PLANED);
//				}
//				if(ObjUtil.isNotNull(req.getParameter("PLANED_FLAG")) && req.getParameter("PLANED_FLAG").equals("true")) {
//					sf.append(" and t.PLAN_STAT > ");
//					sf.append(StaticRef.NO_PLAN);
//					sf.append(" and t.LOAD_STAT < ");
//					sf.append(StaticRef.LOADED);
//				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);				
			}
			
			else if(dsid.equals("V_SHIPMENT_ITEM_QS")) {
				//调度配载->作业单明细
				String keys = sqlUtil.getColName("V_SHIPMENT_ITEM_QS");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_SHIPMENT_ITEM_QS ");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("SHPM_NO", req.getParameter("SHPM_NO")));    //作业单号
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addEqualSQL("SHPM_ROW", req.getParameter("SHPM_ROW")));  //行号
				sf.append(sqlUtil.addEqualSQL("SKU_ID", req.getParameter("SKU_ID")));
				sf.append(sqlUtil.addEqualSQL("SKU_NAME", req.getParameter("SKU_NAME"))); 
				sf.append(sqlUtil.addEqualSQL("SKU_SPEC", req.getParameter("SKU_SPEC")));
				sf.append(sqlUtil.addEqualSQL("UOM", req.getParameter("UOM")));
				sf.append(sqlUtil.addEqualSQL("ODR_QNTY", req.getParameter("ODR_QNTY")));
				sf.append(sqlUtil.addEqualSQL("LD_QNTY", req.getParameter("LD_QNTY")));
				sf.append(sqlUtil.addEqualSQL("UNLD_QNTY", req.getParameter("UNLD_QNTY")));
				sf.append(sqlUtil.addEqualSQL("G_WGT", req.getParameter("G_WGT")));
				sf.append(sqlUtil.addEqualSQL("VOL", req.getParameter("VOL")));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
			}else if(dsid.equals("V_ODR_SHPM")) {
				//客户服务-->订单动态监控-->作业单列表
			//	SQLUtil util = new SQLUtil(false); 
				String keys = sqlUtil.getColName("V_ODR_SHPM");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_ODR_SHPM ");
				sf.append(" where 1=1");
				sf.append(ObjUtil.ifObjNull(req.getParameter("CONDITION"),""));
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				//query = util.getQuery(sf.toString(), keys, null);
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);				
				page_record = 100;
			}
			else if(dsid.equals("V_TRANSACT_LOG_ODR")) {
				//客户服务-->订单动态监控-->跟踪历史信息(托运单主界面下的)
			//	SQLUtil util = new SQLUtil(false); 
				String keys = sqlUtil.getColName("V_CUSTOMACT_LOG");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_CUSTOMACT_LOG ");
				sf.append(" where 1=1 and doc_typ = '");
				sf.append(StaticRef.ODR_NO);
				sf.append("' and ACTION_TYP IN (select CODE FROM BAS_CODES WHERE PROP_CODE = '");
				sf.append(StaticRef.LOG_CODE);
				sf.append("')");
				sf.append(sqlUtil.addEqualSQL("DOC_NO", req.getParameter("DOC_NO")));
				sf.append(" order by addtime desc");
			//	query = util.getQuery(sf.toString(), keys, null);
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);				
				page_record = 100;
			}
			else if(dsid.equals("V_TRANSACT_LOG_SHIP")) {
				//客户服务-->订单动态监控-->跟踪历史信息(作业单主界面下的)
				//SQLUtil util = new SQLUtil(false); 
				String keys = sqlUtil.getColName("V_CUSTOMACT_LOG");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_CUSTOMACT_LOG ");
				sf.append(" where 1=1 and doc_typ = '");
				sf.append(StaticRef.SHPM_NO);
				sf.append("' and ACTION_TYP IN (select CODE FROM BAS_CODES WHERE PROP_CODE = '");
				sf.append(StaticRef.LOG_CODE);
				sf.append("')");
				sf.append(sqlUtil.addEqualSQL("DOC_NO", req.getParameter("DOC_NO")));
				sf.append(" order by addtime desc");
				//query = util.getQuery(sf.toString(), keys, null);
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);		
				page_record = 100;
			}
			else if(dsid.equals("V_TRANS_TRACK_TRACE_SHMP")) {
				//客户服务-->订单动态监控-->在途信息(作业单主界面下的)
				//SQLUtil util = new SQLUtil(false); 
				String keys = sqlUtil.getColName("V_TRANS_TRACK_TRACE");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_TRANS_TRACK_TRACE ");
				sf.append(" where 1=1");
				sf.append(sqlUtil.addEqualSQL("SHPM_NO", req.getParameter("SHPM_NO")));    //原始托运单
				//query = util.getQuery(sf.toString(), keys, null);
				sf.append(" order by addtime_t desc");
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);	
				page_record = 100;
			}
			else if(dsid.equals("V_LOSS_DAMAGE_")){
				sqlUtil = new SQLUtil(false);
				String keys = sqlUtil.getColName("V_LOSS_DAMAGE");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(",SUPLR_ID_NAME as SUPLR_NAME");
				sf.append(" from V_LOSS_DAMAGE ");
				sf.append(" where 1=1");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID_NAME", req.getParameter("CUSTOMER_ID_NAME")));
				sf.append(sqlUtil.addEqualSQL("DUTYER", req.getParameter("DUTYER")));
				sf.append(sqlUtil.addEqualSQL("LOSS_DAMAGE_TYP", req.getParameter("LOSS_DAMAGE_TYP_NAME")));
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(sqlUtil.addEqualSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addALikeSQL("SKU_NAME", req.getParameter("SKU_NAME")));
//				sf.append(sqlUtil.addEqualSQL("CREATE_ORG_ID", req.getParameter("EXEC_ORG_ID")));
				String str=req.getParameter("EXEC_ORG_ID");
				if(ObjUtil.isNotNull(str)) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and CREATE_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(str);
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,");
						sf.append(str);
						sf.append(",%' ) ");
					}
					else {
						sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				}
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(sqlUtil.addALikeSQL("SKU", req.getParameter("SKU")));
//				sf.append(sqlUtil.addEqualSQL("LOSS_DAMAGE_TYP_NAME", req.getParameter("LOSS_DAMAGE_TYP_NAME")));
				sf.append(sqlUtil.addEqualSQL("TRANS_UOM", req.getParameter("UOM"))); 
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_LOSS_DAMAGE_ORDER")){
				sqlUtil = new SQLUtil(false);
				String keys = sqlUtil.getColName("V_LOSS_DAMAGE");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_LOSS_DAMAGE ");
				sf.append(" where 1=1");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID_NAME", req.getParameter("CUSTOMER_ID_NAME")));
				sf.append(sqlUtil.addEqualSQL("DUTYER", req.getParameter("DUTYER")));
				sf.append(sqlUtil.addEqualSQL("LOSS_DAMAGE_TYP", req.getParameter("LOSS_DAMAGE_TYP_NAME")));
				sf.append(sqlUtil.addALikeSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(sqlUtil.addALikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addALikeSQL("SKU_NAME", req.getParameter("SKU_NAME")));
				sf.append(sqlUtil.addALikeSQL("SKU", req.getParameter("SKU")));
//				sf.append(sqlUtil.addEqualSQL("LOSS_DAMAGE_TYP_NAME", req.getParameter("LOSS_DAMAGE_TYP_NAME")));
				sf.append(sqlUtil.addEqualSQL("TRANS_UOM", req.getParameter("UOM"))); 
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_SHPM_SKU")||dsid.equals("V_SHPM_SKU1")){
                //调度配载-->调度单信息-->右键【货品信息】				
				String keys = sqlUtil.getColName("V_SHPM_SKU");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from V_SHPM_SKU_H ");
				}
				else {
					sf.append(" from V_SHPM_SKU ");
				}
				sf.append(" where 1=1");
				sf.append(sqlUtil.addEqualSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			
			else if(dsid.equals("V_PAYDEDUCT")){
                //调度配载-->调度单信息-->右键【货品信息】				
				String keys = sqlUtil.getColName("V_PAYDEDUCT");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_PAYDEDUCT ");
				sf.append(" where 1=1");
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_ID_NAME"))) {
					sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH", req.getParameter("BELONG_MONTH")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))) {
					sf.append(sqlUtil.addEqualSQL("PLATE_NO", req.getParameter("PLATE_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("DEDUCT_TYPE"))) {
					sf.append(sqlUtil.addEqualSQL("DEDUCT_TYPE", req.getParameter("DEDUCT_TYPE")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("SHPM_NO"))) {
					sf.append(sqlUtil.addEqualSQL("SHPM_NO", req.getParameter("SHPM_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			//FANGLM 2011-3-24 17:23
			else if(dsid.equals("V_UNSHPM_SKU")){
                //调度配载-->调度单信息-->右键【作业单相关信息】				
				String keys = sqlUtil.getColName("V_SHPM_SKU");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_SHPM_SKU ");
				sf.append(" where 1=1");
					String shpm_no = req.getParameter("SHPM_NO");
					sf.append(" AND SHPM_NO='");
					sf.append(sqlUtil.decode(shpm_no));
					sf.append("'");
				
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			//FANGLM 2011-3-24 17:23
			else if(dsid.equals("V_UNSHPM_HEADER")){
                //调度配载-->调度单信息-->右键【作业单相关信息】				
				String keys = "SHPM_NO,CUSTOM_ODR_NO,LOAD_NAME,UNLOAD_NAME,STATUS_NAME,TOT_QNTY,TOT_QNTY_EACH";
				String load_no = sqlUtil.decode(req.getParameter("GET_GROUP"));
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from v_shipment_header ");
				sf.append(" where ODR_NO IN (SELECT DISTINCT ODR_NO FROM TRANS_SHIPMENT_HEADER where LOAD_NO='");
				sf.append(load_no);
				sf.append("') and NVL(load_no,' ') <> '");
				sf.append(load_no);
				sf.append("' and status < 90 and NVL(dispatch_stat,' ') <> '9EC53E33BDEB4806AAC8CD2904BFD1BC'");
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);			  
			}
			
			else if(dsid.equals("V_CUSTOMACT_LOG")){
                //fanglm 业务日志		
				sf = new StringBuffer();
				sf.append("select ");
				sf.append("ADDTIME,OP_TIME,NOTES,ADDWHO");
				sf.append(" from V_CUSTOMACT_LOG ");
				sf.append(" where 1=1");
				sf.append(sqlUtil.addEqualSQL("DOC_TYP", req.getParameter("DOC_TYP")));
				sf.append(sqlUtil.addEqualSQL("DOC_NO", req.getParameter("DOC_NO")));
				sf.append(" order by addtime asc");
				
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);				
				query  = sqlUtil.getQuery(sf.toString(), "ADDTIME,OP_TIME,NOTES,ADDWHO", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
			}
			else if(dsid.equals("V_CUSTOMACT_LOG_DISPATCH")){
                //wangjun 调度配载 业务日志		
				sf = new StringBuffer();
				sf.append("select ");
				sf.append("DOC_NO,ADDTIME,OP_TIME,NOTES,ADDWHO");
				sf.append(" from V_CUSTOMACT_LOG ");
				sf.append(" where 1=1");
				sf.append(sqlUtil.addEqualSQL("DOC_TYP", req.getParameter("DOC_TYP")));
				if(ObjUtil.isNotNull(req.getParameter("DOC_NO"))) {
					sf.append(" and DOC_NO in (select shpm_no from trans_shipment_header where load_no = '");
					sf.append(sqlUtil.decode(req.getParameter("DOC_NO")));
					sf.append("') or doc_no ='");
					sf.append(sqlUtil.decode(req.getParameter("DOC_NO")));
					sf.append("'");
				}
				sf.append(" order by addtime asc");
				query  = sqlUtil.getQuery(sf.toString(), "DOC_NO,ADDTIME,OP_TIME,NOTES,ADDWHO", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);				
			}
			else if(dsid.equals("U8_INTER_LOG")){
				//fanglm U8接口错误日志查询
				sf = new StringBuffer();
				sf.append("select T.ID,T.MID_ID,T.QNTY,t.DOC_NO,c.customer_cname as CUSTOMER_NAME,t.DTYPE,t.CTYPE,t.LOGS,to_char(t.ADDTIME,'yyyy-mm-dd hh24:mi') as ADDTIME from u8_alter_log t,bas_customer c where t.ctype = c.id(+) ");
				sf.append(sqlUtil.addALikeSQL("DOC_NO", req.getParameter("DOC_NO")));
				sf.append(sqlUtil.addALikeSQL("DTYPE", req.getParameter("DTYPE")));
				sf.append(sqlUtil.addALikeSQL("LOGS", req.getParameter("LOGS")));
				sf.append(sqlUtil.addEqualSQL("C.ID", req.getParameter("CUSTOMER_ID")));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(" order by addtime desc");
				
				Session curSession = LoginContent.getInstance().getSession();
				query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);	
			}
			else if(dsid.equals("v_loadJob_shpm")){

				//提货装车---作业单查询
				String keys = sqlUtil.getColName("v_vech_ship");
				sf = new StringBuffer();
				sf.append(" SELECT * ");
				sf.append(keys);
				sf.append(" FROM v_vech_ship ");
				sf.append(" WHERE 1 = 1 ");
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));
				sf.append(" AND WHSE_ID IN (SELECT WHSE_ID FROM SYS_USER_WHSE WHERE USER_ID = '");
				sf.append(req.getParameter("USER_ID") + "')");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(" ORDER BY UNLOAD_SEQ asc");
				query = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			    
				page_record = 100;
			}
			else if(dsid.equals("LOAD_JOB_LEAVE_WHSE")){
				String keys = sqlUtil.getColName("V_TRANS_LOAD_JOB_3");
				sf = new StringBuffer();
				sf.append(" SELECT ");
				sf.append(keys);
				sf.append(" FROM V_TRANS_LOAD_JOB_3");
				sf.append(" WHERE 1=1 ");
				sf.append(" AND LOAD_STATUS in (10,20,30)");
				sf.append(sqlUtil.addEqualSQL("PLATE_NO",req.getParameter("PLATE_NO_")));
				sf.append(sqlUtil.addEqualSQL("LOAD_WHSE", req.getParameter("LOAD_WHSE")));
				sf.append(sqlUtil.addEqualSQL("LOAD_STATUS",req.getParameter("LOAD_STATUS")));
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID",req.getParameter("EXEC_ORG_ID") ));
				if(req.getParameter("USER_ID") != null){
					sf.append(" and LOAD_WHSE IN (");
					sf.append(" SELECT WHSE_ID FROM SYS_USER_WHSE WHERE USER_ID ='");
					sf.append(sqlUtil.decode(req.getParameter("USER_ID")));
					sf.append("')");
				}
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		        //Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_TRANS_LOAD_JOB")){
				String keys = sqlUtil.getColName("V_TRANS_LOAD_JOB");
				sf = new StringBuffer();
				sf.append(" SELECT ");
				sf.append(keys);
				sf.append(" FROM V_TRANS_LOAD_JOB");
				sf.append(" WHERE 1=1 ");
				
				sf.append(sqlUtil.addEqualSQL("PLATE_NO",req.getParameter("PLATE_NO_")));
				sf.append(sqlUtil.addEqualSQL("LOAD_WHSE", req.getParameter("LOAD_WHSE")));
				sf.append(sqlUtil.addEqualSQL("LOAD_STATUS",req.getParameter("LOAD_STATUS")));
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID",req.getParameter("EXEC_ORG_ID") ));
			

				if(req.getParameter("USER_ID") != null){
					sf.append(" and LOAD_WHSE IN (");
					sf.append(" SELECT DISTINCT(LOAD_WHSE) FROM  TRANS_LOAD_JOB WHERE LOAD_WHSE IN ");
					sf.append(" (SELECT WHSE_ID FROM SYS_USER_WHSE WHERE USER_ID ='");
					sf.append(sqlUtil.decode(req.getParameter("USER_ID")));
					sf.append("'))");
				}
				if(req.getParameter("PLATE_NO") != null){
					sf.append(sqlUtil.addEqualSQL("PLATE_NO",req.getParameter("PLATE_NO")));
					sf.append(" AND LOAD_STATUS = '10'");
				}
				if("DOCK_NAME".equals(req.getParameter("ORDER_BY"))){
					sf.append(" ORDER BY DOCK_NAME ASC");
				}
				if("QUEUE_SEQ".equals(req.getParameter("ORDER_BY"))){
					sf.append(" AND QUEUE_SEQ > 0 ");
					sf.append(" ORDER BY QUEUE_SEQ ASC ");
				}
				if("ARRIVE_WHSE_TIME".equals(req.getParameter("ORDER_BY"))){
					sf.append(" AND to_date(ARRIVE_WHSE_TIME,'yyyy-mm-dd hh24:mi') >=TO_DATE('");
					sf.append(req.getParameter("ARRIVE_WHSE_TIME"));
					sf.append("','yyyy-mm-dd hh24:mi')");
					sf.append(" ORDER BY ARRIVE_WHSE_TIME asc");
				}
				
		        if(req.getParameter("STATUS") != null){
		        	sf.append(" AND LOAD_STATUS in (10,20,30)");
		        }
		        
		        query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		        //Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_TRANS_LOAD_JOB_2")){
				//提货装车--等待装车（左边列表）
				String keys = sqlUtil.getColName("V_TRANS_LOAD_JOB_1");
				sf = new StringBuffer();
				sf.append(" SELECT ");
				sf.append(keys);
				sf.append(" FROM V_TRANS_LOAD_JOB_1");
				sf.append(" WHERE 1=1 ");
				
				sf.append(sqlUtil.addEqualSQL("PLATE_NO",req.getParameter("PLATE_NO_")));
				sf.append(sqlUtil.addEqualSQL("LOAD_WHSE", req.getParameter("LOAD_WHSE")));
				sf.append(sqlUtil.addEqualSQL("LOAD_STATUS",req.getParameter("LOAD_STATUS")));
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID",req.getParameter("EXEC_ORG_ID") ));
				if(req.getParameter("USER_ID") != null){
					sf.append(" and LOAD_WHSE IN (");
//					sf.append(" SELECT DISTINCT(LOAD_WHSE) FROM  TRANS_LOAD_JOB WHERE LOAD_WHSE IN ");
					sf.append(" SELECT WHSE_ID FROM SYS_USER_WHSE WHERE USER_ID ='");
					sf.append(sqlUtil.decode(req.getParameter("USER_ID")));
					sf.append("')");
				}
				if(req.getParameter("PLATE_NO") != null){
					sf.append(sqlUtil.addEqualSQL("PLATE_NO",req.getParameter("PLATE_NO")));
					sf.append(" AND LOAD_STATUS = '10'");
				}
				if("DOCK_NAME".equals(req.getParameter("ORDER_BY"))){
					sf.append(" ORDER BY DOCK_NAME ASC");
				}
				if("QUEUE_SEQ".equals(req.getParameter("ORDER_BY"))){
					sf.append(" AND QUEUE_SEQ > 0 ");
					sf.append(" ORDER BY QUEUE_SEQ ASC ");
				}
				if("ARRIVE_WHSE_TIME".equals(req.getParameter("ORDER_BY"))){
					sf.append(" AND to_date(ARRIVE_WHSE_TIME,'yyyy-mm-dd hh24:mi') >=TO_DATE('");
					sf.append(req.getParameter("ARRIVE_WHSE_TIME"));
					sf.append("','yyyy-mm-dd hh24:mi')");
					sf.append(" ORDER BY ARRIVE_WHSE_TIME asc");
				}
		        if(req.getParameter("STATUS") != null){
		        	sf.append(" AND LOAD_STATUS in (10,20,30)");
		        }
		        
		        query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		        //Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

			}
			else if(dsid.equals("V_TRANS_LOAD_JOB_")){
				//生成排队号的查询
				String cols = "PLATE_NO,LOAD_STATUS_NAME,LOAD_WHSE_NAME,QUEUE_SEQ,ARRIVE_WHSE_TIME,START_LOAD_TIME,END_LOAD_TIME,LEAVE_WHSE_TIME,DRIVER,MOBILE,CUSTOM_ODR_NO";
				sf = new StringBuffer();
//				String keys = sqlUtil.getColName("V_TRANS_LOAD_JOB");
				sf.append(" SELECT PLATE_NO,MAX(LOAD_STATUS_NAME) LOAD_STATUS_NAME,LOAD_WHSE_NAME,MAX(QUEUE_SEQ) AS　QUEUE_SEQ,MAX(ARRIVE_WHSE_TIME) AS ARRIVE_WHSE_TIME,MAX(START_LOAD_TIME) AS START_LOAD_TIME," +
						"MAX(END_LOAD_TIME) AS END_LOAD_TIME,MAX(LEAVE_WHSE_TIME) AS LEAVE_WHSE_TIME, MAX(DRIVER) AS DRIVER ,MAX(MOBILE) AS MOBILE,CUSTOM_ODR_NO FROM V_TRANS_LOAD_JOB WHERE 1 = 1");
				sf.append(sqlUtil.addEqualSQL("LOAD_STATUS", req.getParameter("LOAD_STATUS")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(sqlUtil.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				sf.append(sqlUtil.addTimeSQL("START_LOAD_TIME", req.getParameter("START_LOAD_TIME_FROM"), ">=")); //wangjun 2011-4-20
				sf.append(sqlUtil.addTimeSQL("START_LOAD_TIME", req.getParameter("START_LOAD_TIME_TO"), "<=")); //wangjun 2011-4-20
				sf.append(sqlUtil.addTimeSQL("ARRIVE_WHSE_TIME", req.getParameter("ARRIVE_WHSE_TIME_FROM"), ">=")); //wangjun 2011-4-20
				sf.append(sqlUtil.addTimeSQL("ARRIVE_WHSE_TIME", req.getParameter("ARRIVE_WHSE_TIME_END"), "<=")); //wangjun 2011-4-20
				sf.append(" AND LOAD_WHSE IN (SELECT WHSE_ID FROM SYS_USER_WHSE WHERE USER_ID = '");
				sf.append(sqlUtil.decode(req.getParameter("USER_ID")));
				sf.append("') ");
				sf.append(" GROUP BY PLATE_NO,LOAD_WHSE_NAME,CUSTOM_ODR_NO");
				sf.append(" ORDER BY QUEUE_SEQ asc");
				
				query  = sqlUtil.getQuery(sf.toString(),cols,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
			}
			else if(dsid.equals("V_TRANS_LOAD_JOB_3")){
				//提货装车--> 正在装车（右边列表）
				String cols = "PLATE_NO,LOAD_WHSE_NAME,LOAD_WHSE,QUEUE_SEQ,ARRIVE_WHSE_TIME,UNLOAD_NAME,QNTY,LOAD_STATUS_NAME,SHPM_NO,MOBILE,CUSTOM_ODR_NO,LOAD_NO";
				sf = new StringBuffer();
				sf.append(" SELECT MAX(PLATE_NO) as PLATE_NO,LOAD_WHSE_NAME,LOAD_WHSE,QUEUE_SEQ,MAX(ARRIVE_WHSE_TIME) AS ARRIVE_WHSE_TIME,MAX(UNLOAD_NAME) AS UNLOAD_NAME," +
						"SUM(QNTY) AS QNTY,MAX(LOAD_STATUS_NAME) as LOAD_STATUS_NAME,MAX(SHPM_NO) SHPM_NO,MAX(MOBILE) AS MOBILE,CUSTOM_ODR_NO,LOAD_NO FROM V_TRANS_LOAD_JOB WHERE 1 = 1");
				sf.append(sqlUtil.addEqualSQL("LOAD_STATUS", req.getParameter("LOAD_STATUS")));
				sf.append(" AND LOAD_WHSE IN (SELECT WHSE_ID FROM SYS_USER_WHSE WHERE USER_ID = '");
				sf.append(sqlUtil.decode(req.getParameter("USER_ID")));
				sf.append("') ");
				sf.append(" GROUP BY LOAD_WHSE_NAME,LOAD_WHSE,UNLOAD_NAME,LOAD_NO,QUEUE_SEQ,CUSTOM_ODR_NO");
				sf.append(" ORDER BY QUEUE_SEQ ASC");
				
				query  = sqlUtil.getQuery(sf.toString(),cols,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
			}
			else if(dsid.equals("V_TRANS_LOAD_JOB_4")){
				sf = new StringBuffer();
				String keys = sqlUtil.getColName("V_TRANS_LOAD_JOB");
				sf.append(" SELECT " +keys +" FROM V_TRANS_LOAD_JOB WHERE 1 = 1  ");
//				sf.append(" SELECT MAX(PLATE_NO) as PLATE_NO,LOAD_WHSE_NAME,LOAD_WHSE,QUEUE_SEQ,MAX(ARRIVE_WHSE_TIME) AS ARRIVE_WHSE_TIME,MAX(UNLOAD_NAME) AS UNLOAD_NAME," +
//						"SUM(QNTY) AS QNTY,MAX(LOAD_STATUS_NAME) as LOAD_STATUS_NAME,MAX(SHPM_NO) AS SHPM_NO,MAX(VEHICLE_TYP_NAME) AS VEHICLE_TYP_NAME,MAX(ARRIVE_WHSE_TIME) AS ARRIVE_WHSE_TIME FROM V_TRANS_LOAD_JOB WHERE 1 = 1");
//				sf.append(sqlUtil.addEqualSQL("LOAD_STATUS", req.getParameter("LOAD_STATUS")));
				sf.append(" AND LOAD_STATUS in ('10','20','30')");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO",req.getParameter("LOAD_NO")));
				sf.append(" AND LOAD_WHSE IN (SELECT WHSE_ID FROM SYS_USER_WHSE WHERE USER_ID = '");
				sf.append(sqlUtil.decode(req.getParameter("USER_ID")));
				sf.append("') ");
				sf.append(" GROUP BY LOAD_WHSE_NAME,LOAD_WHSE,UNLOAD_NAME,LOAD_NO,QUEUE_SEQ");
				sf.append(" ORDER BY QUEUE_SEQ ASC");
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_TRANS_LOAD_SCREEN")){
				String keys = sqlUtil.getColName("V_TRANS_LOAD_JOB");
//					String keys = sqlUtil.getColName("max(LOAD_STATUS_NAME) as LOAD_STATUS_NAME,max(PLATE_NO) as PLATE_NO,max(UNLOAD_NAME) as UNLOAD_NAME,max(QNTY) as QNTY," +
//							"max(LOAD_WHSE_NAME) as LOAD_WHSE_NAME,max(DOCK_NAME) as DOCK_NAME,max(START_LOAD_TIME) as START_LOAD_TIME");
				sf = new StringBuffer();
				
				sf.append(" SELECT * FROM (");
				sf.append(" SELECT ROW_.*,ROWNUM ROWNUM_ FROM ( ");
				sf.append(" SELECT ");
				sf.append("(SELECT COUNT(1) FROM V_TRANS_LOAD_JOB WHERE 1=1 ");
				sf.append(sqlUtil.addEqualSQL("LOAD_STATUS", req.getParameter("LOAD_STATUS")));
				if(req.getParameter("USER_ID")!=null){
					sf.append(" AND LOAD_WHSE IN (SELECT WHSE_ID FROM SYS_USER_WHSE WHERE USER_ID = '");
					sf.append(sqlUtil.decode(req.getParameter("USER_ID")));
					sf.append("')");
				}
				sf.append(sqlUtil.addEqualSQL("LOAD_WHSE", req.getParameter("LOAD_WHSE")));
				sf.append(") AS NUM,");
				sf.append(keys);
				sf.append(" FROM V_TRANS_LOAD_JOB");
				sf.append(" WHERE 1=1 ");
				sf.append(sqlUtil.addEqualSQL("LOAD_WHSE", req.getParameter("LOAD_WHSE")));
				sf.append(sqlUtil.addEqualSQL("LOAD_STATUS",req.getParameter("LOAD_STATUS")));
				if(req.getParameter("USER_ID")!=null){
					sf.append(" AND LOAD_WHSE IN (SELECT WHSE_ID FROM SYS_USER_WHSE WHERE USER_ID = '");
					sf.append(sqlUtil.decode(req.getParameter("USER_ID")));
					sf.append("')");
				}
				sf.append(" ORDER BY QUEUE_SEQ ASC ) ROW_ ");
				sf.append(" WHERE ROWNUM <=" + req.getParameter("QUEUE")+")");
				sf.append(" WHERE ROWNUM_ > " +req.getParameter("QUEUE2"));
				
				Session curSession = LoginContent.getInstance().getSession();
				query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
				page_record = 100;
			}
			else if(dsid.equals("V_TRANS_LOAD_SCREEN_")){
				String keys = sqlUtil.getColName("V_TRANS_LOAD_JOB");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_TRANS_LOAD_JOB ");
				sf.append(" WHERE 1 = 1 ");
				sf.append(sqlUtil.addEqualSQL("LOAD_STATUS",req.getParameter("LOAD_STATUS")));
				sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID",req.getParameter("EXEC_ORG_ID") ));
//				if("QUEUE_SEQ".equals(req.getParameter("ORDER_BY"))){
//					sf.append(" ORDER BY QUEUE_SEQ ASC ");
//				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				page_record = 100;
			}
			else if(dsid.equals("V_SHIPMENT_HEADER_VECH")) {
//				//到库登记 wangjun
				String keys = sqlUtil.getColName("V_SHIPMENT_HEADER");
				sf = new StringBuffer();
				sf.append(" SELECT ");
				sf.append(keys);
				sf.append(" FROM V_SHIPMENT_HEADER ");
				sf.append(" WHERE 1 = 1 ");
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));
				sf.append(" AND WHSE_ID IN (SELECT WHSE_ID FROM SYS_USER_WHSE WHERE USER_ID = '");
				sf.append(sqlUtil.decode(req.getParameter("USER_ID")) + "')");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(" ORDER BY UNLOAD_SEQ DESC");
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				page_record = 100;			
			}
			else if(dsid.equals("PRE_LOAD_EXCEL")){
				//fanglm  应发未发预警
				String keys = sqlUtil.getColName("WARNING_SEND_TIMEOUT");
				sf = new StringBuffer();
				sf.append("select * from WARNING_SEND_TIMEOUT");
				sf.append(" where 1=1 ");
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("PRE_UNLOAD_EXCEL")){
				//fanglm  应收未收预警
				String keys = sqlUtil.getColName("WARNING_UNLOAD_TIMEOUT");
				sf = new StringBuffer();
				sf.append("select * from WARNING_UNLOAD_TIMEOUT");
				sf.append(" where 1=1 ");
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("PRE_POD_EXCEL")){
				//fanglm  应回未回预警
				String keys = sqlUtil.getColName("WARNING_POD_TIMEOUT");
				sf = new StringBuffer();
				sf.append("select * from WARNING_POD_TIMEOUT");
				sf.append(" where 1=1 ");
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}	
			else if(dsid.equals("WARNING_PRE_FEE")){
				//fanglm  预付款超额预警
				String keys = sqlUtil.getColName("WARNING_PRE_FEE");
				sf = new StringBuffer();
				sf.append("select * from WARNING_PRE_FEE");
				sf.append(" where 1=1 ");
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}	
			else if(dsid.equals("WARNING_SETT_FEE")){
				//fanglm  运费超额预警
				String keys = sqlUtil.getColName("WARNING_PRE_FEE");
				sf = new StringBuffer();
				sf.append("select * from WARNING_SETT_FEE");
				sf.append(" where 1=1 ");
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("WARNING_SITE")){
				//fanglm  运费超额预警
				String keys = sqlUtil.getColName("WARNING_SITE");
				sf = new StringBuffer();
				sf.append("select * from WARNING_SITE");
				sf.append(" where 1=1 ");
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}	
			else if(dsid.equals("R_TRANS_AB")){
				//wangjun 跟踪信息
				String keys = sqlUtil.getColName("R_TRANS_AB");
				sf = new StringBuffer();
				sf.append("select * from R_TRANS_AB");
				sf.append(" where 1=1 ");
				
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));   
				sf.append(sqlUtil.addLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("TRACK_TIME", req.getParameter("TRACE_TIME_FORM"), ">="));
				sf.append(sqlUtil.addTimeSQL("TRACK_TIME", req.getParameter("TRACE_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_NAME")));
				sf.append(sqlUtil.addEqualSQL("ABNOMAL_STAT", req.getParameter("ABNOMAL_STAT")));
				sf.append(sqlUtil.addEqualSQL("ADDWHO", req.getParameter("ADDWHO")));
				
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and EXEC_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(req.getParameter("EXEC_ORG_ID"));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,");
						sf.append(req.getParameter("EXEC_ORG_ID"));
						sf.append(",%' ) ");
					}
					else {
						sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				}
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
			}	
			else if(dsid.equals("V_EXT_DRCT_LOG")){
				//wangjun  手机定位
				String keys = sqlUtil.getColName("V_EXT_DRCT_LOG");
				sf=new StringBuffer();
				sf.append(" select ");
				sf.append(keys);
				sf.append(" from V_EXT_DRCT_LOG where 1=1 ");
				
				sf.append(sqlUtil.addLikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addLikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				sf.append(sqlUtil.addLikeSQL("MOBILE", req.getParameter("MOBILE")));
				sf.append(sqlUtil.addEqualSQL("OPERATOR_TYPE", req.getParameter("OPERATOR_TYPE")));   
				sf.append(sqlUtil.addTimeSQL("createtime", req.getParameter("ADDTIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("createtime", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));   
				sf.append(sqlUtil.addFlagSQL("SUCCESS_FLAG",req.getParameter("SUCCESS_FLAG")));

//				Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("SUCCESS_FLAG", Hibernate.YES_NO);
				query=sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);;
			}
			else if(dsid.equals("V_PRINT_SHIPMENT")){
				//wangjun  
				String keys = sqlUtil.getColName("V_SHIPMENT_HEADER");
				sf = new StringBuffer();
				sf.append("select * from V_SHIPMENT_HEADER");
				sf.append(" where 1=1 ");
				
				sf.append(sqlUtil.addLikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addLikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
			}
			else if(dsid.equals("V_VEH_REPORT1") || dsid.equals("V_VEH_REPORT2") || dsid.equals("V_VEH_REPORT3")) {
				
				String keys = sqlUtil.getColName("V_VEH_REPORT");
				
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_VEH_REPORT");
				sf.append(" where 1=1 ");
				
				if(dsid.equals("V_VEH_REPORT1")) {
					if(!ObjUtil.isNotNull(req.getParameter("REPORT_STATUS"))) {
						sf.append(" and PLATE_NO is null and REPORT_TIME is null");
					}
				}
				else if(dsid.equals("V_VEH_REPORT2")) {
					if(!ObjUtil.isNotNull(req.getParameter("REPORT_STATUS"))) {
						sf.append(" and PLATE_NO is not null and REPORT_TIME is null");
					}
				}
				else if(dsid.equals("V_VEH_REPORT3")) {
					sf.append(" and REPORT_TIME is not null");
				}
				sf.append(sqlUtil.addLikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addEqualSQL("VEH_TYP", req.getParameter("VEH_TYP")));
				sf.append(sqlUtil.addEqualSQL("REPORT_STATUS", req.getParameter("REPORT_STATUS")));
				sf.append(sqlUtil.addEqualSQL("AUDIT_STATUS", req.getParameter("AUDIT_STATUS")));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//Query query  = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("TRANS_DISPATCH_LOG")){   
				//yuanlei 2012-06-14 配载结果查询
				String keys = "ID,SHPM_NO,EXEC_SEQ,NOTES,EXEC_TIME";
				sf = new StringBuffer();
				sf.append("select ID,SHPM_NO,EXEC_SEQ,NOTES,to_char(EXEC_TIME,'yyyy/mm/dd hh24:mi') as EXEC_TIME from TRANS_DISPATCH_LOG");
				sf.append(" where 1=1 ");
				
				sf.append(sqlUtil.addTimeSQL("EXECTIME", req.getParameter("ADDTIME_FROM"), ">="));//wangjun 2011-4-21
				sf.append(sqlUtil.addTimeSQL("EXECTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				//sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
			}
			//fanglm 2012-07-31 仓库管理员对应作业单明细数据
			else if(dsid.equals("USER_WHSE_ORDER")){
				String keys = sqlUtil.getColName("v_user_SHPM_ITEM"); 
				sf = new StringBuffer();
				sf.append("select * from v_user_SHPM_ITEM");
				sf.append(" where ");
				sf.append(" load_id in(");
				sf.append("select id from bas_address where whse_id in( select whse_id from sys_user_whse where user_id='");
				sf.append( user.getUSER_ID());
				sf.append("'))");
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if (dsid.equals("SF_ORDER_HEADER")) {  //原始订单
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("SF_ORDER_HEADER",sqlUtil.getTableAlias());
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(",t1.NAME_C AS BIZ_TYP_NAME,t2.NAME_C AS ODR_TYP_NAME");
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from SF_ORDER_HEADER_H t");
				}
				else {
					sf.append(" from SF_ORDER_HEADER t");
				}
				sf.append(",BAS_CODES t1");
				sf.append(",BAS_CODES t2");
				sf.append(" where t.BIZ_TYP = t1.id and t1.prop_code = 'BIZ_TYP'");
				sf.append(" and t.ODR_TYP = t2.id and t2.prop_code = 'TRS_TYP'");
				sf.append(sqlUtil.addEqualSQL("ADDWHO",req.getParameter("ADDWHO")));
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
//				sf.append(sqlUtil.addALikeSQL("EXEC_ORG_ID_NAME", req.getParameter("EXEC_ORG_ID_NAME")));
				sf.append(sqlUtil.addALikeSQL("LOAD_AREA_NAME", req.getParameter("LOAD_AREA_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
				sf.append(sqlUtil.addEqualSQL("UGRT_GRD", req.getParameter("UGRT_GRD")));
				sf.append(sqlUtil.addEqualSQL("EXEC_STAT", req.getParameter("EXEC_STAT")));
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
				sf.append(sqlUtil.addEqualSQL("ADDWHO",req.getParameter("ADDWHO")));
				//sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_CONTACT", req.getParameter("UNLOAD_CONTACT")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_TEL", req.getParameter("UNLOAD_TEL")));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"),"<="));
				
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_TO"),"<="));
				
				sf.append(sqlUtil.addEqualSQL("PLAN_STAT", req.getParameter("PLAN_STAT")));
				sf.append(sqlUtil.addEqualSQL("LOAD_STAT", req.getParameter("LOAD_STAT")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_STAT", req.getParameter("UNLOAD_STAT")));
				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ODR_TYP")));
				sf.append(sqlUtil.addEqualSQL("CREATE_ORG_ID_NAME",req.getParameter("CREATE_ORG_ID_NAME")));
				sf.append(sqlUtil.addEqualSQL("ORD_PRO_LEVER",req.getParameter("ORD_PRO_LEVER")));
				sf.append(sqlUtil.addEqualSQL("CREATE_ORG_ID",req.getParameter("CREATE_ORG_ID")));
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ATTR",req.getParameter("CUSTOM_ATTR")));
				sf.append(sqlUtil.addFlagSQL("SLF_PICKUP_FLAG", req.getParameter("SLF_PICKUP_FLAG"),true));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),req.getParameter("C_RDC_FLAG")));				
//				sf.append(sqlUtil.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(sqlUtil.addTimeSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				//sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
//				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),""));
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_NAME")));
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FORM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FORM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FORM")));
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FORM"),">="));
				}
				sf.append(sqlUtil.addLeftLikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addLeftLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(" and odr_no in (select odr_no from trans_shipment_header where load_no = '");
					sf.append(req.getParameter("LOAD_NO"));
					sf.append("')");
				}
				sf.append(sqlUtil.addEqualSQL("BUK_FLAG", req.getParameter("BUK_FLAG"))); //SSS处理标识
				sf.append(" order by t.ADDTIME DESC");
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
				map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
				map.put("ARRIVABLE_FLAG",Hibernate.YES_NO);
				map.put("ODR_TIME", Hibernate.STRING);
				map.put("POD_FLAG", Hibernate.YES_NO);
				map.put("BUK_FLAG", Hibernate.YES_NO);
				map.put("DISCOUNT", Hibernate.DOUBLE);
				map.put("COD_FLAG", Hibernate.YES_NO);
				String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",BIZ_TYP_NAME,ODR_TYP_NAME";
				query  = sqlUtil.getQuery(sf.toString(), field, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				isCustSize = true;
			}
			else if (dsid.equals("SF_ORDER_ITEM")) {
				String keys = sqlUtil.getColName("SF_ORDER_ITEM");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from SF_ORDER_ITEM_H ");
				}
				else {
					sf.append(" from SF_ORDER_ITEM ");
				}
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(" order by odr_row asc");
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				page_record = 100;	
			}
			else if(dsid.equals("TRANS_CUSTOMERACT_LOG")){
				sf=new StringBuffer();
				sf.append("select ");
				sf.append("OP_TIME,NAME_C,NOTES ");
				sf.append(" from TRANS_CUSTOMERACT_LOG tcl,BAS_CODES bc");
				sf.append(" where 1=1 ");
				sf.append(" and tcl.action_typ=bc.code(+) ");
//				sf.append(" and DOC_NO= '"+req.getParameter("ODR_NO")+"'");
				sf.append(sqlUtil.addEqualSQL("DOC_NO", req.getParameter("DOC_NO")));
				sf.append(" and DOC_TYP='ODR_NO' ");
				sf.append(" and prop_code='TRANSACTION_TYP'");
				sf.append(" order by OP_TIME asc");
				query=sqlUtil.getQuery(sf.toString(), "OP_TIME,NAME_C,NOTES", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if (dsid.equals("SF_STATUS_LOG")){
				String keys="ODR_NO,REFENENCE1,ADDTIME,NOTES,STATUS_NAME,DESCR,ADDWHO,LOAD_NO,QNTY,TEMPERATURE,TEMPERATURE_NAME";
				sf=new StringBuffer();
				sf.append("select ");
				sf.append("T.ODR_NO,T.REFENENCE1,to_char(T.ADDTIME,'yyyy-mm-dd hh24:mi:ss') as ADDTIME,T.NOTES,T1.NAME_C AS STATUS_NAME,T.DESCR,T.ADDWHO,T.LOAD_NO,T.QNTY," +
						"T.TEMPERATURE,T2.NAME_C AS TEMPERATURE_NAME ");
				sf.append("from ");
				sf.append("SF_STATUS_LOG t,BAS_CODES t1,BAS_CODES t2 ");
				sf.append("where t.STATUS=t1.CODE(+) and t1.PROP_CODE(+)='SF_STATUS' and t.TEMPERATURE=t2.ID(+) ");
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(" order by t.ADDTIME asc");
				query=sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if (dsid.equals("V_SHIPMENT_HEADER3")){
				String keys="LOAD_AREA_ID2,LOAD_AREA_NAME2,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,QNTY,TOT_GROSS_W,TOT_VOL,STR_DEL,ADDTIME,TEMPERATURE_ID,TEMPERATURE";
				sf=new StringBuffer();
				sf.append("select ");
				sf.append("t1.LOAD_AREA_ID2,t1.LOAD_AREA_NAME2,t1.UNLOAD_AREA_ID2,t1.UNLOAD_AREA_NAME2,COUNT(distinct t1.ODR_NO) as QNTY,");
				sf.append("SUM(t2.G_WGT) as TOT_GROSS_W,SUM(t2.VOL) as TOT_VOL,");
				sf.append("(select case count(1) when 0 then '直送' else '非直送' end from BAS_ROUTE_HEAD t4 where t4.POINTS_FLAG <>'Y' and t4.ENABLE_FLAG = 'Y' and t4.START_AREA_ID2=t1.LOAD_AREA_ID2 and t4.END_AREA_ID2=t1.UNLOAD_AREA_ID2 and TRANS_SRVC_ID = '2') as STR_DEL,");
				sf.append("to_char(t1.addtime,'yyyy-mm-dd') as ADDTIME,");
				sf.append("t2.TEMPERATURE1 as TEMPERATURE_ID,t3.NAME_C as TEMPERATURE");
				sf.append(" from TRANS_ORDER_HEADER t1,TRANS_ORDER_ITEM t2,BAS_CODES t3");
				sf.append(" where t1.odr_no=t2.odr_no and t2.temperature1=t3.id(+) and t3.prop_code='TRANS_COND'");
				sf.append(" and 1=1 ");
				sf.append(" and t1.load_area_id2<>t1.unload_area_id2");
				sf.append(" and t1.STATUS>='"+StaticRef.SHPM_CONFIRM+"' and t1.STATUS<'"+StaticRef.SHPM_DIPATCH+"' ");
				sf.append(" and t1.PLAN_STAT <> '"+StaticRef.PLANED+"'");
				sf.append(" and not exists(select odr_no from trans_shipment_header t3 where ((trans_srvc_id=2 and status>20 and status<100 and status <> 92) or instr(t3.shpm_no,'S')>0) and t1.odr_no=t3.odr_no)");
//				if(ObjUtil.isNotNull(req.getParameter("BIZ_TYP"))){
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
//				}else{
//					sf.append(" and (BIZ_TYP='"+StaticRef.B2B+"' or BIZ_TYP='"+StaticRef.LD+"') ");
//				}
				sf.append(sqlUtil.addEqualSQL("t1.LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID2")));
				sf.append(sqlUtil.addEqualSQL("t1.UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));
				sf.append(sqlUtil.addTimeSQL("t1.ADDTIME", req.getParameter("DATE_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("t1.ADDTIME", req.getParameter("DATE_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("t2.TEMPERATURE1", req.getParameter("TEMPERATRUE")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and EXEC_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(req.getParameter("EXEC_ORG_ID"));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,");
						sf.append(req.getParameter("EXEC_ORG_ID"));
						sf.append(",%' ) ");
					}
					else {
						sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				}
				sf.append(" group by t1.LOAD_AREA_ID2,t1.UNLOAD_AREA_ID2,t1.LOAD_AREA_NAME2,t1.UNLOAD_AREA_NAME2,to_char(t1.addtime,'yyyy-mm-dd'),t2.temperature1,t3.name_c");
				sf.append(" order by addtime desc");
				
				query=sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if(dsid.equals("V_SHIPMENT_HEADER4")){
				//自提自送查询
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("TRANS_SHIPMENT_HEADER",sqlUtil.getTableAlias());
				//String fields = LoginContent.getInstance().getListCfg().get(StaticRef.V_SHIPMENT_HEADER_SHPM).get("FIELD");
				//String keys = HSQL.getInstance().setSQL(fields).addSQL("BIZ_TYP_NAME", "t1.NAME_C").addSQL("ODR_TYP_NAME", "t2.NAME_C")
				//        .addSQL("REFENENCE4_NAME", "t5.NAME_C").getSQL();
				
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(",t1.NAME_C AS BIZ_TYP_NAME,t2.NAME_C AS ODR_TYP_NAME,t5.NAME_C as REFENENCE4_NAME");
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from TRANS_SHIPMENT_HEADER_H t");
				}
				else {
					sf.append(" from TRANS_SHIPMENT_HEADER t");
				}
				sf.append(",BAS_CODES t1");
				sf.append(",BAS_CODES t2");
				sf.append(",BAS_CODES t5");
				//sf.append(",bas_address ba");
				//sf.append(",bas_address ba2");
				sf.append(" where t.BIZ_TYP = t1.id and t1.prop_code = 'BIZ_TYP'");
				sf.append(" and t.ODR_TYP = t2.id and t2.prop_code = 'TRS_TYP'");
				sf.append(" and t.REFENENCE4 = t5.id(+)");
				sf.append(" and t.trans_srvc_id<>2");
				//sf.append(" and t.LOAD_ID=ba.id and t.UNLOAD_ID=ba2.id");
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));     //调度单号条件：要么有值，要么值为NULL
				}
				boolean del=Boolean.valueOf(req.getParameter("SLF_DELIVER_FLAG"));
				boolean pic=Boolean.valueOf(req.getParameter("SLF_PICKUP_FLAG"));
				if(del && !pic){
					sf.append(" and SLF_DELIVER_FLAG='Y'");
				}else if(!del && pic){
					sf.append(" and SLF_PICKUP_FLAG='Y'");
				}else if(del && pic){
					sf.append(" and SLF_PICKUP_FLAG = 'Y' and SLF_DELIVER_FLAG = 'Y'");
				}else{
					sf.append(" and (SLF_PICKUP_FLAG = 'Y' or SLF_DELIVER_FLAG = 'Y')");
				}
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("SIGN_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG"), req.getParameter("EXEC_FLAG")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_NAME", req.getParameter("CUSTOMER_NAME")));//客户名称
				//sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
//				sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID_NAME", req.getParameter("EXEC_ORG_ID")));//执行机构
//				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_NAME2", req.getParameter("LOAD_AREA_NAME")));//发货区域
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID")));//发货区域
//				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_NAME2", req.getParameter("UNLOAD_AREA_NAME")));//收获区域
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID")));//收获区域
				sf.append(sqlUtil.addEqualSQL("ASSIGN_STAT", req.getParameter("ASSIGN_STAT_NAME")));//派发状态
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID",req.getParameter("TRANS_SRVC_ID")));//运输服务
				sf.append(sqlUtil.addEqualSQL("SUPLR_NAME", req.getParameter("SUPLR_NAME")));//供应商
//				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
//				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  //客户单号
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addALikeSQL("LOAD_ID", req.getParameter("LOAD_ID")));//发货方代码
				sf.append(sqlUtil.addALikeSQL("UNLOAD_ID", req.getParameter("UNLOAD_ID")));//收货方代码
				sf.append(sqlUtil.addALikeSQL("END_UNLOAD_NAME", req.getParameter("END_UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addEqualSQL("LOAD_REGION_NAME", req.getParameter("LOAD_REGION_NAME")));  //发货方代码
				sf.append(sqlUtil.addEqualSQL("UNLOAD_REGION_NAME", req.getParameter("UNLOAD_REGION_NAME")));  //发货方代码
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME"), ">="));  //订单时间从
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));  //订单时间从
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<=")); //订单时间到
//				sf.append(sqlUtil.addFlagSQL("UDF6", req.getParameter("UDF6"),false)); //订单时间到
				sf.append(sqlUtil.addFlagSQL("POD_FLAG", req.getParameter("POD_FLAG"),true));
				sf.append(sqlUtil.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));  //发货方代码
				sf.append(sqlUtil.addEqualSQL("REFENENCE4", req.getParameter("REFENENCE4")));
//				sf.append(sqlUtil.addEqualSQL("ADDTIME", req.getParameter("ADDTIME")));
				if(req.getParameter("ADDTIME")!= null){
					sf.append(" AND ADDTIME LIKE TO_DATE('"+req.getParameter("ADDTIME")+"','YYYY-MM-DD')");
				}
				if(!ObjUtil.isNotNull(req.getParameter("shoud_com_flag"))||"false".equals(req.getParameter("should_com_flag"))){
					//预达时间（从-->到）
					sf.append(sqlUtil.addTimeSQL("PRE_UNLOAD_TIME",req.getParameter("PRE_UNLOAD_TIME_FROM"), ">="));
					sf.append(sqlUtil.addTimeSQL("PRE_UNLOAD_TIME",req.getParameter("PRE_UNLOAD_TIME_TO"),"<="));
				}else{
					sf.append(" and sysdate > to_date(pre_unload_time,'yyyy-mm-dd hh24:mi') and status >=40 and status <50");
				}
				sf.append(sqlUtil.addEqualSQL("ABNOMAL_STAT", req.getParameter("ABNOMAL_STAT")));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID",req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID",req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID",req.getParameter("AREA_ID")));
				sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", req.getParameter("TO_UNLOAD_TIME"), ">="));  //预计到货时间从
				sf.append(sqlUtil.addTimeSQL("TO_UNLOAD_TIME", req.getParameter("TO_UNLOAD_TIME_TO"), "<=")); //预计到货时间到
				sf.append(sqlUtil.addALikeSQL("ADDWHO", req.getParameter("ADDWHO")));      //接单员
				sf.append(sqlUtil.addALikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addALikeSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addALikeSQL("SKU_NAME", req.getParameter("SKU_NAME"))); //货品
				sf.append(sqlUtil.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));//wangjun 2010-04-25
				sf.append(sqlUtil.addEqualSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME")));//计划回单时间
				sf.append(sqlUtil.addEqualSQL("POD_DELAY_REASON_NAME",req.getParameter("POD_DELAY_REASON_NAME")));//回单延迟原因
				sf.append(sqlUtil.addEqualSQL("DUTYER",req.getParameter("DUTYER") ));
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("SHPM_STATUS")));
//				if(req.getParameter("LOAD_STAT")!=null){
//					if(req.getParameter("LOAD_STAT").equals("10")){
//						sf.append(sqlUtil.addEqualSQL("LOAD_STAT", "未装车"));
//					}else if(req.getParameter("LOAD_STAT").equals("20")){
//						sf.append(sqlUtil.addEqualSQL("LOAD_STAT", "已装车"));
//					}else{
						sf.append(sqlUtil.addEqualSQL("LOAD_STAT", req.getParameter("LOAD_STAT")));
//					}
//				}
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_FROM"), ">=")); //wangjun 2011-4-2
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_TO"), "<=")); 
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_FROM"), ">=")); //wangjun 2011-6-21
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_TO"), "<=")); 
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));//业务类型
				/*if(ObjUtil.isNotNull(req.getParameter("LOAD_ID"))){
					sf.append(" AND ba.ADDR_CODE LIKE '%"+req.getParameter("LOAD_ID")+"%'");
				}
				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_ID"))){
					sf.append(" AND ba2.ADDR_CODE LIKE '%"+req.getParameter("UNLOAD_ID")+"%'");
				}*/
				
				if(ObjUtil.isNotNull(req.getParameter("PRINT_STATUS"))){
					if("48B6B1E0C06647D2AE3C180C1F486BC8".equals(req.getParameter("PRINT_STATUS"))){//wangjun 2011-5-15
						sf.append(" AND LOAD_PRINT_COUNT <> 0  ");
					}else if("B2DD62397ADA41DAB545A9BC648D8A00".equals(req.getParameter("PRINT_STATUS"))){//可打印
						sf.append(" AND PRINT_FLAG='Y' ");
					}else {
						sf.append(" AND LOAD_PRINT_COUNT = 0");
					}
				}
				sf.append(sqlUtil.addAddrRole(user.getROLE_ID_NAME(),user.getUSER_ID()));
				//yuanlei 2012-3-16 提高查询效率
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FROM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FROM")));  //状态
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FROM"), ">=")); //wangjun 2011-4-6
					if(ObjUtil.isNotNull(req.getParameter("STATUS_TO"))) {
						sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"), "<=")); 
					}
					else {
						sf.append(" and status < 92");
					}
				}
				//运输跟踪发运时间
				if(ObjUtil.isNotNull(req.getParameter("PLAN_STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("PLAN_STAT_FROM")) 
						&& req.getParameter("PLAN_STATUS_TO").equals(req.getParameter("PLAN_STAT_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("PLAN_STAT_FROM")));  //状态
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("PLAN_STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("PLAN_STAT_FROM"),">="));
				}
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME",req.getParameter("END_LOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME",req.getParameter("END_LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addLeftLikeSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));//运单号
				sf.append(sqlUtil.addALikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));        //作业单号
				sf.append(sqlUtil.addLeftLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  //客户单号
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));  //状态
				sf.append(sqlUtil.addEqualSQL("nvl(RECE_FLAG,'N')",req.getParameter("RECE_FLAG")));//运输服务
				//yuanlei 2012-09-13 自定义查询增加‘司机’和‘手机号'
				sf.append(sqlUtil.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));     
				//yuanlei
				if(!ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO")) && !ObjUtil.isNotNull(req.getParameter("SHPM_NO"))
						&& !ObjUtil.isNotNull(req.getParameter("ODR_NO"))) {
					sf.append(" order by t.addtime desc");
				}
			
				String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",BIZ_TYP_NAME,ODR_TYP_NAME,REFENENCE4_NAME";
				HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
	            map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
	            map.put("UDF5", Hibernate.YES_NO);
	            map.put("UDF6", Hibernate.YES_NO);
	            
				query  = sqlUtil.getQuery(sf.toString(), field, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(ObjUtil.ifNull(req.getParameter("ALL_FLAG"),"false").equals("true")) {
					isCustSize = true;
					page_record = LoginContent.getInstance().getDefPageSize();
				}
				
				//isCustSize = true;			
				
				//LoginContent.getInstance().setPageInfo(sqlUtil.getRecordByQuery(sf.toString()));
			}else if(dsid.equals("V_ORDER_HEADER2")){  //托运单
//				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("V_ORDER_HEADER");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_ORDER_HEADER t");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("ADDWHO",req.getParameter("ADDWHO")));
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
//				sf.append(sqlUtil.addALikeSQL("EXEC_ORG_ID_NAME", req.getParameter("EXEC_ORG_ID_NAME")));
				sf.append(sqlUtil.addALikeSQL("LOAD_AREA_NAME", req.getParameter("LOAD_AREA_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
				sf.append(sqlUtil.addEqualSQL("UGRT_GRD", req.getParameter("UGRT_GRD")));
				sf.append(sqlUtil.addEqualSQL("EXEC_STAT", req.getParameter("EXEC_STAT")));
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
				//sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_CONTACT", req.getParameter("UNLOAD_CONTACT")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_TEL", req.getParameter("UNLOAD_TEL")));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"),"<="));
				
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"),"<="));
				
				sf.append(sqlUtil.addEqualSQL("PLAN_STAT", req.getParameter("PLAN_STAT")));
				sf.append(sqlUtil.addEqualSQL("LOAD_STAT", req.getParameter("LOAD_STAT")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_STAT", req.getParameter("UNLOAD_STAT")));
				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ODR_TYP")));
				sf.append(sqlUtil.addEqualSQL("CREATE_ORG_ID_NAME",req.getParameter("CREATE_ORG_ID_NAME")));
				sf.append(sqlUtil.addEqualSQL("ORD_PRO_LEVER",req.getParameter("ORD_PRO_LEVER")));
				sf.append(sqlUtil.addEqualSQL("CREATE_ORG_ID",req.getParameter("CREATE_ORG_ID")));
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ATTR",req.getParameter("CUSTOM_ATTR")));
				sf.append(sqlUtil.addFlagSQL("SLF_PICKUP_FLAG", req.getParameter("SLF_PICKUP_FLAG"),true));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),req.getParameter("C_RDC_FLAG")));				
//				sf.append(sqlUtil.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(sqlUtil.addTimeSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addALikeSQL("BTCH_NUM", req.getParameter("BTCH_NUM")));
				//sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
//				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),""));
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_NAME")));
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FORM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FORM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FORM")));
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FORM"),">="));
				}
				sf.append(sqlUtil.addLeftLikeSQL("SOURCE_NO", req.getParameter("SOURCE_NO")));
				sf.append(sqlUtil.addLeftLikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addLeftLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(" and odr_no in (select odr_no from trans_shipment_header where load_no = '");
					sf.append(req.getParameter("LOAD_NO"));
					sf.append("')");
				}
				sf.append(sqlUtil.addEqualSQL("BUK_FLAG", req.getParameter("BUK_FLAG"))); //SSS处理标识
				sf.append(sqlUtil.addEqualSQL("UPLOAD_FLAG", req.getParameter("UPLOAD_FLAG")));
				if(ObjUtil.isNotNull(req.getParameter("LOSDAM_FLAG"))&&(req.getParameter("LOSDAM_FLAG").equals("true"))) {
					sf.append(sqlUtil.addEqualSQL("LOSDAM_FLAG", "Y"));
				}
				sf.append(" order by ADDTIME DESC");
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
				map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
				map.put("ODR_TIME", Hibernate.STRING);
				map.put("POD_FLAG", Hibernate.YES_NO);
				map.put("DISCOUNT", Hibernate.DOUBLE);
				map.put("COD_FLAG", Hibernate.YES_NO);
				map.put("UPLOAD_FLAG", Hibernate.YES_NO);
				map.put("LOSDAM_FLAG", Hibernate.YES_NO);
				query  = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//isCustSize = true;
								
			}else if(dsid.equals("TRANS_RDC_HEADER")){
				String keys = sqlUtil.getColName("TMS_CHANGE_RDC");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from TMS_CHANGE_RDC t");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("ORI_RDC_CODE", req.getParameter("ORI_RDC_CODE")));
				sf.append(sqlUtil.addEqualSQL("CURR_RDC_CODE", req.getParameter("CURR_RDC_CODE")));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(sqlUtil.addFlagSQL("CHANGED_FLAG", req.getParameter("CHANGED_FLAG")));
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));
				if (ObjUtil.isNotNull(req.getParameter("SHPM_NO"))) {
					sf.append(" and exists(select 'x' from trans_shipment_header t2 where t.rdc_no = t2.rdc_no");
					sf.append(sqlUtil.addALikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));
					sf.append(")");
				}
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(" order by ADDTIME desc");
				
				HashMap<String,Type> map = new HashMap<String, Type>();
				map.put("CHANGED_FLAG", Hibernate.YES_NO);
				query = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if (dsid.equals("RDC_SHIPMENT_HEADER")) {
				//调度配载->在途跟踪
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("TRANS_SHIPMENT_HEADER",sqlUtil.getTableAlias());
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(",t1.NAME_C AS BIZ_TYP_NAME,t2.NAME_C AS ODR_TYP_NAME,t5.NAME_C as REFENENCE4_NAME, t6.name_c as ASSIGN_STAT_NAME, t7.RDC_NAME as CURR_RDC_NAME");
				sf.append(",t.ABNOMAL_STAT as ABNOMAL_STAT_NAME,t.POD_DELAY_REASON as POD_DELAY_REASON_NAME,t.UNLOAD_DELAY_REASON as UNLOAD_DELAY_REASON_NAME");
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from TRANS_SHIPMENT_HEADER_H t");
				}
				else {
					sf.append(" from TRANS_SHIPMENT_HEADER t");
				}
				sf.append(",BAS_CODES t1");
				sf.append(",BAS_CODES t2");
				sf.append(",BAS_CODES t5");
				sf.append(",BAS_CODES t6");
				sf.append(",RDC_ADDRESS t7");
				sf.append(" where t.BIZ_TYP = t1.id and t1.prop_code = 'BIZ_TYP'");
				sf.append(" and t.ODR_TYP = t2.id and t2.prop_code = 'TRS_TYP'");
				sf.append(" and t.REFENENCE4 = t5.id(+)");
				sf.append(" and t.ASSIGN_STAT = t6.code(+) and t6.prop_code(+) = 'ASSIGN_STAT'");
				sf.append(" and t.TRANS_SRVC_ID = 3 and t.UNLOAD_ID = t7.TMS_ADDR_CODE(+) and t.LOAD_ID <> t7.RDC_CODE");
				sf.append(" and t.RDC_NO is null and t.status='20'");
				sf.append(sqlUtil.addALikeSQL("RDC_NO", req.getParameter("RDC_NO")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID2")));//发货区域
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));//收获区域
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));  //订单时间从
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<=")); //订单时间到
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));//业务类型
				sf.append(sqlUtil.addALikeSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addALikeSQL("REFENENCE1", req.getParameter("REFENENCE1")));//运单号
				sf.append(sqlUtil.addALikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));        //作业单号
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  //客户单号
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				if(!ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO")) && !ObjUtil.isNotNull(req.getParameter("SHPM_NO"))
						&& !ObjUtil.isNotNull(req.getParameter("ODR_NO"))) {
					sf.append(" order by t.addtime desc");
				}
			
				String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",BIZ_TYP_NAME" +
						",ODR_TYP_NAME,REFENENCE4_NAME,ASSIGN_STAT_NAME,ABNOMAL_STAT_NAME," +
						"POD_DELAY_REASON_NAME,UNLOAD_DELAY_REASON_NAME,CURR_RDC_NAME";
				HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
	            map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
	            map.put("UDF5", Hibernate.YES_NO);
	            map.put("UDF6", Hibernate.YES_NO);
	            
				query  = sqlUtil.getQuery(sf.toString(), field, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(ObjUtil.ifNull(req.getParameter("ALL_FLAG"),"false").equals("true")) {
					isCustSize = true;
					page_record = LoginContent.getInstance().getDefPageSize();
				}
			}
			else if(dsid.equals("TRANS_VEH_CHECK")){
				//运输执行--车辆检查--右边表
				String keys = sqlUtil.getColName("TRANS_VEH_CHECK");
				sf = new StringBuffer();
				sf.append("select t.*,'' as INSPECTION_NAME,'' as INSPECTION_FLAG,'' as REASON_CODE from TRANS_VEH_CHECK t,TRANS_LOAD_HEADER t1");
				sf.append(" where t.TRS_ID=t1.LOAD_NO ");
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ITEM1_FLAG", Hibernate.YES_NO);
				map.put("ITEM2_FLAG", Hibernate.YES_NO);
				map.put("ITEM3_FLAG", Hibernate.YES_NO);
				map.put("ITEM4_FLAG", Hibernate.YES_NO);
				map.put("ITEM5_FLAG", Hibernate.YES_NO);
				map.put("ITEM6_FLAG", Hibernate.YES_NO);
				map.put("ITEM7_FLAG", Hibernate.YES_NO);
				map.put("ITEM8_FLAG", Hibernate.YES_NO);
				query  = sqlUtil.getQuery(sf.toString(),keys,map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}	
			else if(dsid.equals("SYS_APPROVE_SET")){
				//运输执行--赔偿单审批
				String keys = sqlUtil.getColName("SYS_APPROVE_SET");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from SYS_APPROVE_SET");
				sf.append(" where 1=1 ");
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("TRANS_COMPLAINT")){
				//运输执行--投诉管理
				String keys = sqlUtil.getColName("V_COMPLAINT");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_COMPLAINT");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("ODR_NO"))){
					sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))){
					sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))){
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
				if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_ID"))){
					sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_NAME"))){
					sf.append(sqlUtil.addEqualSQL("CUSTOMER_NAME", req.getParameter("CUSTOMER_NAME")));
				}
				
				if(ObjUtil.isNotNull(req.getParameter("CUSTOM_SERVICE"))){
					sf.append(sqlUtil.addEqualSQL("CUSTOM_SERVICE", req.getParameter("CUSTOM_SERVICE")));
				}
				if(ObjUtil.isNotNull(req.getParameter("DUTY_TO"))){
					sf.append(sqlUtil.addEqualSQL("DUTY_TO", req.getParameter("DUTY_TO")));
				}
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_TRANS_VEH")){
				//运输执行--年审管理
				String keys = sqlUtil.getColName("V_TRANS_VEH");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_TRANS_VEH t");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))){
					sf.append(sqlUtil.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("VERIFY_GRADE"))){
					sf.append(sqlUtil.addEqualSQL("VERIFY_GRADE", req.getParameter("VERIFY_GRADE")));
				}
				if(ObjUtil.isNotNull(req.getParameter("VERIFY_DATE"))){
					//sf.append(" AND VERIFY_DATE LIKE TO_CHAR('"+req.getParameter("VERIFY_DATE")+"','YYYY-MM-DD')");
//					sf.append(sqlUtil.addEqualSQL("VERIFY_DATE", req.getParameter("VERIFY_DATE")));
					sf.append(sqlUtil.addTimeSQL("VERIFY_DATE", req.getParameter("VERIFY_DATE"), "="));
				}
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("TRANS_BILL_PAYMENT")){
				//运输执行--请款单管理--主表
				String keys = sqlUtil.getColName("TRANS_BILL_PAYMENT");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from TRANS_BILL_PAYMENT");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_NAME"))){
					sf.append(sqlUtil.addEqualSQL("SUPLR_NAME", req.getParameter("SUPLR_NAME")));
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONETH"))){
					sf.append(sqlUtil.addEqualSQL("BELONG_MONETH", req.getParameter("BELONG_MONETH")));
				}
				if(ObjUtil.isNotNull(req.getParameter("PAYMENT_NO"))){
					sf.append(sqlUtil.addEqualSQL("PAYMENT_NO", req.getParameter("PAYMENT_NO")));
				}
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("TRANS_BILL_ADJITEM")){
				//运输执行--请款单管理--主表
				String keys = sqlUtil.getColName("TRANS_BILL_ADJITEM");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from TRANS_BILL_ADJITEM");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("PAYMENT_NO"))){
					sf.append(sqlUtil.addEqualSQL("PAYMENT_NO", req.getParameter("PAYMENT_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("ADJUST_NO"))){
					sf.append(sqlUtil.addEqualSQL("ADJUST_NO", req.getParameter("ADJUST_NO")));
				}
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("TRANS_BILL_ADJUST")){
				//运输执行--调整单管理（主）
				String keys = sqlUtil.getColName("TRANS_BILL_ADJUST");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from TRANS_BILL_ADJUST");
				sf.append(" where 1=1 ");
//				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))){
//					sf.append(sqlUtil.addEqualSQL("PLATE_NO", req.getParameter("PLATE_NO")));
//				}
//				if(ObjUtil.isNotNull(req.getParameter("VERIFY_GRADE"))){
//					sf.append(sqlUtil.addEqualSQL("VERIFY_GRADE", req.getParameter("VERIFY_GRADE")));
//				}
//				if(ObjUtil.isNotNull(req.getParameter("VERIFY_DATE"))){
//					//sf.append(" AND VERIFY_DATE LIKE TO_CHAR('"+req.getParameter("VERIFY_DATE")+"','YYYY-MM-DD')");
////					sf.append(sqlUtil.addEqualSQL("VERIFY_DATE", req.getParameter("VERIFY_DATE")));
//					sf.append(sqlUtil.addTimeSQL("VERIFY_DATE", req.getParameter("VERIFY_DATE"), "="));
//				}
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("TRANS_BILL_RECADJUST")){
				//运输执行--应收调整单--主表
				String keys = sqlUtil.getColName("TRANS_BILL_RECADJUST");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from TRANS_BILL_RECADJUST");
				sf.append(" where 1=1 ");
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("TRANS_BILL_RECADJITEM")){
				//运输执行--应收调整单--从表
				String keys = sqlUtil.getColName("TRANS_BILL_RECADJITEM");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from TRANS_BILL_RECADJITEM");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("ADJUST_NO"))){
					sf.append(sqlUtil.addEqualSQL("ADJUST_NO", req.getParameter("ADJUST_NO")));
				}
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("v_customer")){
				//运输执行--应收调整单--从表
				String keys = sqlUtil.getColName("V_CUSTOMER");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_CUSTOMER");
				sf.append(" where 1=1 ");
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_TRANS_CHANGE_RECORD")){
				//运输执行--应收调整单--从表
				String keys = sqlUtil.getColName("V_TRANS_CHANGE_RECORD");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_TRANS_CHANGE_RECORD");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO1", req.getParameter("LOAD_NO1")));
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("ESB_GPS_POSITION")){
				//运输执行--应收调整单--从表
				String keys = sqlUtil.getColName("ESB_GPS_POSITION");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from ESB_GPS_POSITION");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("VEHICLE"))){
					sf.append(sqlUtil.addALikeSQL("VEHICLE", req.getParameter("VEHICLE")));
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))){
					sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				}
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("R_LOSS_DAMAGE_")){
				sqlUtil = new SQLUtil(false);
				String keys = sqlUtil.getColName("R_LOSS_DAMAGE");
				alias = "t";
				sqlUtil.setTableAlias("t");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from R_LOSS_DAMAGE t");
				sf.append(" where 1=1");				
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO"))); 				
				sf.append(sqlUtil.addEqualSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addALikeSQL("SKU_NAME", req.getParameter("SKU_NAME")));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_RECE_APPOINTMENT")){
				sqlUtil = new SQLUtil(false);
				String keys = sqlUtil.getColName("V_RECE_APPOINTMENT");
				sf = new StringBuffer();
				sf.append("select ODR_NO,CUSTOM_ODR_NO,CUSTOMER_NAME,VEHICLE_TYP_NAME,BIZ_TYP_NAME,CUSTOMER_ID,EXEC_ORG_ID,LOAD_NAME,UNLOAD_NAME,to_char(PRE_UNLOAD_TIME,'yyyy/mm/dd HH:mm') as PRE_UNLOAD_TIME,APPOINT_HOURS,REMAIN_HOURS,ODR_TYP_NAME");
				sf.append(" from V_RECE_APPOINTMENT t ");
				sf.append(" where 1=1");		
				if(ObjUtil.isNotNull(req.getParameter("ODR_NO"))){
				
					sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO"))); 	
				}
				if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_NAME"))){
					sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_NAME")));
				}
				if(ObjUtil.isNotNull(req.getParameter("REMAIN_HOURS"))){
					sf.append(sqlUtil.addMathSQL("REMAIN_HOURS", req.getParameter("REMAIN_HOURS"), "<"));
				}
				if(ObjUtil.isNotNull(req.getParameter("PRE_UNLOAD_TIME_FROM"))){
					sf.append(sqlUtil.addTimeSQL("PRE_UNLOAD_TIME", req.getParameter("PRE_UNLOAD_TIME_FROM"), ">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("PRE_UNLOAD_TIME_TO"))){
					sf.append(sqlUtil.addTimeSQL("PRE_UNLOAD_TIME", req.getParameter("PRE_UNLOAD_TIME_TO"), "<="));
				}
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_CONTACT_REMAIN")){
				sqlUtil = new SQLUtil(false);
				String keys = sqlUtil.getColName("V_CONTACT_REMAIN");
				sf = new StringBuffer();
				sf.append("select OBJECT_NAME,TFF_NAME,CONTACT_NO,to_char(START_DATE,'yyyy/mm/dd') as START_DATE,to_char(END_DATE,'yyyy/mm/dd') as END_DATE,REMAIN_DAYS");
				//sf.append(keys);
				sf.append(" from V_CONTACT_REMAIN  ");
				sf.append(" where 1=1");		
				if(ObjUtil.isNotNull(req.getParameter("OBJECT_NAME"))){				
					sf.append(sqlUtil.addALikeSQL("OBJECT_NAME", req.getParameter("OBJECT_NAME"))); 	
				}
				if(ObjUtil.isNotNull(req.getParameter("TFF_NAME"))){
					sf.append(sqlUtil.addALikeSQL("TFF_NAME", req.getParameter("TFF_NAME")));
				}
				if(ObjUtil.isNotNull(req.getParameter("CONTACT_NO"))){
					sf.append(sqlUtil.addALikeSQL("CONTACT_NO", req.getParameter("CONTACT_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("REMAIN_DAYS"))){
					sf.append(sqlUtil.addMathSQL("REMAIN_DAYS", req.getParameter("REMAIN_DAYS"), "<"));
				}
				if(ObjUtil.isNotNull(req.getParameter("END_DATE_FROM"))){
					sf.append(sqlUtil.addTimeSQL("END_DATE", req.getParameter("END_DATE_FROM"), ">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("END_DATE_TO"))){
					sf.append(sqlUtil.addTimeSQL("END_DATE", req.getParameter("END_DATE_TO"), "<="));
				}
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("V_ODR_COMPLAINT")){
				sqlUtil = new SQLUtil(false);
				String keys = sqlUtil.getColName("V_ODR_COMPLAINT");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_ODR_COMPLAINT  ");
				sf.append(" where 1=1");		
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))){				
					sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO"))); 	
				}
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_ID"))){
					sf.append(sqlUtil.addALikeSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))){
					sf.append(sqlUtil.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("VEHICLE_TYP_ID"))){
					sf.append(sqlUtil.addEqualSQL("VEHICLE_TYP_ID", req.getParameter("VEHICLE_TYP_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("ODR_NO"))){
					sf.append(sqlUtil.addALikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))){
					sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("DRIVER"))){
					sf.append(sqlUtil.addALikeSQL("DRIVER", req.getParameter("DRIVER")));
				}
				if(ObjUtil.isNotNull(req.getParameter("MOBILE"))){
					sf.append(sqlUtil.addALikeSQL("MOBILE", req.getParameter("MOBILE")));
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			
			
			}
			
			if (dsid.equals("V_LOAD_HEADER_Recl")) {   
				key = "TRANS_SRVC_ID_NAME,VEHICLE_TYP_ID_NAME,TEMP_NO1_NAME,TEMP_NO2_NAME,LOAD_STAT_NAME";
				value = "t1.SRVC_NAME,t2.VEHICLE_TYPE,t3.EQUIP_NO,t4.EQUIP_NO,t5.NAME_C";
				alias = "t";
				//调度管理->调度单
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("TRANS_LOAD_HEADER",sqlUtil.getTableAlias());
				sf = new StringBuffer();
				sf.append("select distinct ");
				sf.append(keys);
				sf.append(",t1.SRVC_NAME AS TRANS_SRVC_ID_NAME,t2.VEHICLE_TYPE AS VEHICLE_TYP_ID_NAME");
				sf.append(",t3.EQUIP_NO AS TEMP_NO1_NAME,t4.EQUIP_NO AS TEMP_NO2_NAME,t5.NAME_C AS LOAD_STAT_NAME,t6.EQUIP_NO as GPS_NO1_NAME");
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from TRANS_LOAD_HEADER_H t ");
				}
				else {
					sf.append(" from TRANS_LOAD_HEADER t ");
				}
				sf.append(",BAS_TRANS_SERVICE t1");
				sf.append(",BAS_VEHICLE_TYPE t2");
				sf.append(",BAS_TEMPEQ t3");
				sf.append(",BAS_TEMPEQ t4");
				sf.append(",BAS_CODES t5");
				sf.append(",BAS_GPSEQ t6");

				sf.append(" where t.TRANS_SRVC_ID = t1.id");
				sf.append(" and t.VEHICLE_TYP_ID = t2.id(+)");
				sf.append(" and t.TEMP_NO1 = t3.id(+)");
				sf.append(" and t.TEMP_NO2 = t4.id(+)");
				sf.append(" and t.LOAD_STAT = t5.CODE(+) and t5.PROP_CODE = 'PICKLOAD_STAT'");
				sf.append(" and t.GPS_NO1 = t6.id(+)");

				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));        //供应商
				//sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));            //状态 从 
				//sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),req.getParameter("C_RDC_FLAG")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						if(ObjUtil.isNotNull(req.getParameter("EXEC_FLAG")) && req.getParameter("EXEC_FLAG").equals("true")) {
			            	sf.append(" and exists ");
			            	sf.append("    (SELECT 'x' ");
			            	sf.append("     From bas_org ");
			            	sf.append("     ,trans_shipment_header t1");
							sf.append("     Where t1.load_no = t.load_no and t1.SIGN_ORG_ID = bas_org.id AND (bas_org.ORG_INDEX Like '%,"); //
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("%'");
							sf.append("))");
						}
						else {
							sf.append(" and exists ");
			            	sf.append("    (SELECT 'x' ");
			            	sf.append("     From bas_org ");
							sf.append("     Where t.EXEC_ORG_ID = bas_org.id AND (bas_org.ORG_INDEX Like '%,"); //
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("%'");
							sf.append("))");
						}
					}
					else {
						if(ObjUtil.isNotNull(req.getParameter("EXEC_FLAG")) && req.getParameter("EXEC_FLAG").equals("true")) {
							sf.append(" and (SIGN_ORG_ID = '");
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("'");
							sf.append(")");
						}
						else {
							sf.append(" and (EXEC_ORG_ID = '");
							sf.append(req.getParameter("EXEC_ORG_ID"));
							sf.append("'");
							sf.append(")");
						}
					}
				}
				//fanglm 2011-10-09 用户的客户权限
				/*if(ObjUtil.isNotNull(user.getUSER_CUSTOMER())){
					sf.append(" and t.load_no in ");
					sf.append("(select load_no from ");
					if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
						sf.append(" trans_shipment_header_h");
					}
					else {
						sf.append(" trans_shipment_header");
					}
					sf.append(" where customer_id ");
					sf.append(" in (");
					sf.append(user.getUSER_CUSTOMER());
					sf.append("))");
				}*/
				//yuanlei
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
//				sf.append(sqlUtil.addALikeSQL("START_AREA_NAME", req.getParameter("START_AREA_NAME")));
				sf.append(sqlUtil.addEqualSQL("START_AREA_ID", req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addEqualSQL("ADDWHO", req.getParameter("ADDWHO")));  //客户
//				sf.append(sqlUtil.addALikeSQL("END_AREA_NAME", req.getParameter("END_AREA_NAME"))); 
				sf.append(sqlUtil.addEqualSQL("END_AREA_ID", req.getParameter("END_AREA_ID")));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME"), ">="));//wangjun 2011-4-21
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("END_LOAD_TIME_FROM"), ">="));//发运时间从
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("END_LOAD_TIME_TO"),"<="));//发运时间到
				sf.append(sqlUtil.addTimeSQL("ACCOUNT_TIME", req.getParameter("ACCOUNT_TIME_FROM"), ">="));//发运时间从
				sf.append(sqlUtil.addTimeSQL("ACCOUNT_TIME", req.getParameter("ACCOUNT_TIME_TO"),"<="));//发运时间到
				sf.append(sqlUtil.addTimeSQL("FEEAUDIT_TIME", req.getParameter("AUDIT_TIME_FROM"), ">="));//发运时间从
				sf.append(sqlUtil.addTimeSQL("FEEAUDIT_TIME", req.getParameter("AUDIT_TIME_TO"),"<="));//发运时间到
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID",req.getParameter("SUPLR_ID")));//供应商
				sf.append(sqlUtil.addEqualSQL("ADDWHO",req.getParameter("ADDWHO")));
				sf.append(sqlUtil.addALikeSQL("SUPLR_NAME",req.getParameter("SUPLR_NAME")));//供应商
				sf.append(sqlUtil.addALikeSQL("PLATE_NO",req.getParameter("PLATE_NO")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //发货方
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addEqualSQL("DISPATCH_STAT_NAME",req.getParameter("DISPATCH_STAT_NAME")));
				sf.append(sqlUtil.addEqualSQL("DISPATCH_STAT",req.getParameter("DISPATCH_STAT")));//--wangjun 2010-2-27
				sf.append(sqlUtil.addEqualSQL("ACCOUNT_STAT",req.getParameter("ACCOUNT_STAT")));//--wangjun 2010-2-27
				sf.append(sqlUtil.addEqualSQL("FEEAUDIT_STAT",req.getParameter("FEEAUDIT_STAT")));//--wangjun 2010-2-27
				sf.append(sqlUtil.addTimeSQL("PRE_DEPART_TIME", req.getParameter("PRE_DEPART_TIME"), ">="));
				sf.append(sqlUtil.addTimeSQL("t.PRE_UNLOAD_TIME", req.getParameter("PRE_UNLOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("t.PRE_UNLOAD_TIME", req.getParameter("PRE_UNLOAD_TIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("PRE_DEPART_TIME", req.getParameter("PRE_DEPART_TIME_TO"), "<="));



				sf.append(sqlUtil.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));  //发货方代码
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				if(ObjUtil.isNotNull(req.getParameter("LOAD_STAT"))){
					sf.append(sqlUtil.addEqualSQL("LOAD_STAT", req.getParameter("LOAD_STAT")));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FROM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FROM")));
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FROM"),">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))){
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
//				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));    //原始托运单
				sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));           //调度单号
				if(ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))){
					sf.append(" and t.load_no");
	            	sf.append(" IN ");
	            	sf.append("    (select distinct load_no ");
	            	if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
	            		sf.append("     From TRANS_SHIPMENT_HEADER_H ");
	            	}
	            	else {
	            		sf.append("     From TRANS_SHIPMENT_HEADER ");
	            	}
					sf.append("     Where reverse(custom_odr_no) like ");
					sf.append(" reverse('%"); 
					sf.append(sqlUtil.decode(req.getParameter("CUSTOM_ODR_NO")));
					sf.append("')) ");
					
				}
				//sf.append(sqlUtil.addLikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));//作业单编号  --yuanlei 2011-2-18
				//yuanlei
				if(ObjUtil.isNotNull(req.getParameter("SHPM_NO"))){
					sf.append(" and t.load_no");
	            	sf.append(" IN ");
	            	sf.append("    (select distinct load_no ");
	            	sf.append("     From TRANS_SHIPMENT_HEADER ");
					sf.append("     Where reverse(shpm_no) like ");
					sf.append(" reverse('%"); 
					sf.append(sqlUtil.decode(req.getParameter("SHPM_NO")));
					sf.append("%' )) ");
				}
				
				if(ObjUtil.isNotNull(req.getParameter("REFENENCE1"))){
					sf.append(" and t.LOAD_NO");
					sf.append(" in");
					sf.append(" (select distinct load_no from TRANS_SHIPMENT_HEADER where reverse(refenence1) like reverse('%");
					sf.append(sqlUtil.decode(req.getParameter("REFENENCE1")));
					sf.append("%'))");
				}
				if(ObjUtil.isNotNull(req.getParameter("BIZ_TYP"))){
					sf.append(" and t.load_no in");
					sf.append(" (select distinct load_no from TRANS_SHIPMENT_HEADER where BIZ_TYP='");
					sf.append(req.getParameter("BIZ_TYP"));
					sf.append("')");
				}
				if(!ObjUtil.isNotNull(req.getParameter("SHPM_NO")) && !ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))
						&& !ObjUtil.isNotNull(req.getParameter("LOAD_NO"))){
					sf.append(" order by t.ADDTIME desc");      //wangjun 2011-4-21
				}

				String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",TRANS_SRVC_ID_NAME,VEHICLE_TYP_ID_NAME,LOAD_STAT_NAME,TEMP_NO1_NAME,TEMP_NO2_NAME,GPS_NO1_NAME";
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("CHECK_FLAG", Hibernate.YES_NO);
				query  = sqlUtil.getQuery(sf.toString(), field, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//isCustSize = true;			
				//LoginContent.getInstance().setPageInfo(sqlUtil.getRecordByQuery(sf.toString()));
			}
			else if(dsid.equals("TRANS_TRACK_TRACE")){
				//运输执行--应收调整单--从表
				String keys = sqlUtil.getColName("TRANS_TRACK_TRACE");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from TRANS_TRACK_TRACE");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_SHPM_TRACK")){
				alias = "t";
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("V_SHPM_TRACK");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_SHPM_TRACK t");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(sqlUtil.addEqualSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("SYS_APPROVE_HEAD")){
				alias = "t";
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("SYS_APPROVE_HEAD");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from SYS_APPROVE_HEAD t");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("DOC_NO", req.getParameter("DOC_NO")));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME"), ">="));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BAS_CUSTOMER")){
				String keys = sqlUtil.getColName("BAS_CUSTOMER");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BAS_CUSTOMER");
				query  = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			
			if(query != null) {
				if("true".equals(is_curr_page)){
					if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
						query.setFirstResult(start_row);
						query.setMaxResults(page_record);
					} else {
						start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
						query.setFirstResult(start_row);
						query.setMaxResults(page_record);
					}
				}
				
				if(ObjUtil.ifNull(CUR_PAGE,"1").equals("1") && !isCustSize) {
					ArrayList<String> recordList = sqlUtil.getRecordByQuery(sf.toString());
					int count = Integer.parseInt(recordList.get(0));
					int pages = 0;
					if(count%page_record == 0){
		    			pages = count/page_record;
		    		}else{
		    			pages = count/page_record + 1;
		    		}
					response.addCookie(new Cookie("TOTALROWS",recordList.get(0)));
					response.addCookie(new Cookie("SQLWHERE",recordList.get(1)));
					response.addCookie(new Cookie("TOTALPAGES",Integer.toString(pages)));
					response.addCookie(new Cookie("SQLFIELD1",key));
					response.addCookie(new Cookie("SQLFIELD2",value));
					response.addCookie(new Cookie("SQLALIAS",alias));
				}
				else {
					if(dsid.equals("V_SHPMCOMP_HEADER")){
						ArrayList<String> recordList = sqlUtil.getRecordByQuery(sf.toString());
						int count = Integer.parseInt(recordList.get(0));
						int pages = 0;
						if(count%page_record == 0){
			    			pages = count/page_record;
			    		}else{
			    			pages = count/page_record + 1;
			    		}
						response.addCookie(new Cookie("TOTALROWS",recordList.get(0)));
						response.addCookie(new Cookie("SQLWHERE",recordList.get(1)));
						response.addCookie(new Cookie("TOTALPAGES",Integer.toString(pages)));
						response.addCookie(new Cookie("SQLFIELD1",key));
						response.addCookie(new Cookie("SQLFIELD2",value));
						response.addCookie(new Cookie("SQLALIAS",alias));
					}
					else {
						ArrayList<String> recordList = sqlUtil.getRecordByQuery(sf.toString());
						int count = Integer.parseInt(recordList.get(0));
						int pages = 0;
						if(count%page_record == 0){
			    			pages = count/200;
			    		}else{
			    			pages = count/200 + 1;
			    		}
						response.addCookie(new Cookie("TOTALROWS",recordList.get(0)));
						response.addCookie(new Cookie("SQLWHERE",recordList.get(1)));
						response.addCookie(new Cookie("TOTALPAGES",Integer.toString(pages)));
						response.addCookie(new Cookie("SQLFIELD1",key));
						response.addCookie(new Cookie("SQLFIELD2",value));
						response.addCookie(new Cookie("SQLALIAS",alias));
					}
				}
			
				List<HashMap<String, String>> object = query.list();
		        Gson gson = new Gson();
		        String content = gson.toJson(object);
				p.print(content);
				
				
				/*if(ObjUtil.ifNull(CUR_PAGE,"1").equals("1") && !isCustSize) {
		            LoginContent.getInstance().setPageInfo(sqlUtil.getRecordByQuery(sf.toString()));
				}else{
					if(dsid.equals("V_SHPMCOMP_HEADER")){
						LoginContent.getInstance().setPageInfo(sqlUtil.getRecordByQuery(sf.toString()));
					}else{
						LoginContent.getInstance().setPageInfo(sqlUtil.getRecordByQuery(sf.toString()),200);
					}
				}*/
				
				LoginContent.getInstance().closeSession();
				//curSession = null;
				p.flush();
				p.close();
				p = null;
			}
			
			try {
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}