﻿package com.rd.server;

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
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;

import com.google.gson.Gson;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.obj.system.SYS_USER;
import com.rd.server.util.Log4j;
import com.rd.server.util.LoginContent;
import com.rd.server.util.SQLUtil;
import com.rd.server.util.SUtil;

/**
 * 财务管理查询servlet
 * @author fanglm
 *
 */
@SuppressWarnings("unused")
public class SettQueryServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8176587733272940053L;
	private Query query;
	private final int page_record = LoginContent.getInstance().pageSize;
	private List<HashMap<String, String>> object;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
	throws ServletException, IOException {
		HttpSession session =req.getSession();
		session.setMaxInactiveInterval(24*60*60);
		SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
		String dsid = req.getParameter("ds_id"); // 数据源ID
		String flag = SUtil.iif(req.getParameter("OP_FLAG"), ""); // 操作标记
		String is_curr_page = SUtil.iif(req.getParameter("is_curr_page"),""); //分页查询
		SQLUtil sqlUtil = new SQLUtil(true);
		
		if (dsid != null && flag.equals(StaticRef.MOD_FLAG)) {
			response.setCharacterEncoding("utf-8");
			PrintWriter p = response.getWriter();
			if(user == null) {
				p.print("404");
				return;
			}
			Session curSession = LoginContent.getInstance().getSession();
			String CUR_PAGE = (String) req.getParameter("CUR_PAGE"); // 当前页码
			int start_row = 0; // 开始行，从0开始
			StringBuffer sf = new StringBuffer();
			
			if(dsid.equals("V_BILL_DETAIL")){
				String key = sqlUtil.getColName("V_BILL_DETAIL");
				sf.append("SELECT  *");
//				sf.append(key);
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from V_BILL_DETAIL_H");
					key = sqlUtil.getColName("V_BILL_DETAIL_H");
				}
				else {
					sf.append(" from V_BILL_DETAIL");
				}
				sf.append(" WHERE 1 = 1 ");
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addLikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(sqlUtil.addLikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("SHPM_STATUS_FROM"),">="));//wangjun 2010-07-19
				sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("SHPM_STATUS_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("ACCOUNT_STAT", req.getParameter("ACCOUNT_STAT")));
				sf.append(sqlUtil.addEqualSQL("AUDIT_STAT", req.getParameter("AUDIT_STAT")));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("END_LOAD_TIME_FROM"), ">="));//wangjun 2011-06-21
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("END_LOAD_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("ACCOUNT_TIME", req.getParameter("ACCOUNT_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ACCOUNT_TIME", req.getParameter("ACCOUNT_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("AUDIT_TIME", req.getParameter("AUDIT_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("AUDIT_TIME", req.getParameter("AUDIT_TIME_TO"), "<="));
				sf.append(sqlUtil.addMathSQL("SERIAL_NUM", req.getParameter("SERIAL_NUM_FROM"), ">="));
				sf.append(sqlUtil.addMathSQL("SERIAL_NUM", req.getParameter("SERIAL_NUM_TO"), "<="));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));
				sf.append(sqlUtil.addLikeSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));
				
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and EXEC_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(req.getParameter("EXEC_ORG_ID"));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(req.getParameter("EXEC_ORG_ID"));
						sf.append(",%' ) ");
					}
					else {
						sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				}
				if(ObjUtil.isNotNull(req.getParameter("shpm_no"))){//wangjun 2011-08-11
					sf.append(" and SHPM_NO <> '");
					sf.append(req.getParameter("shpm_no"));
					sf.append("'");
				}
				
				if(ObjUtil.isNotNull(req.getParameter("RDC"))){
					if(req.getParameter("RDC").equals("true")){
						sf.append(" and transorg_flag = 'Y'");
					}
				}
				if(ObjUtil.isNotNull(req.getParameter("CDC"))){
					if(req.getParameter("CDC").equals("true")){
						sf.append(" and transorg_flag = 'N'");
					}
				}
				sf.append(sqlUtil.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				if(ObjUtil.isNotNull(req.getParameter("ORDER_BY_NUM")) && "Y".equals(req.getParameter("ORDER_BY_NUM"))){
					sf.append(" order by SERIAL_NUM");
				}else{
					sf.append(" order by addtime ");
				}
//				object = SUtil.ResultToList(key, sf.toString(), CUR_PAGE);
				query = sqlUtil.getQuery(sf.toString(),key,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				int page_record = LoginContent.getInstance().pageSize;
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
			}
			
			else if(dsid.equals("V_BILL_GP_SKU")){
				sf.append(" SELECT * from V_BILL_GP_SKU");
				sf.append(" WHERE 1 = 1 ");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO",req.getParameter("LOAD_NO")));
				query = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

			}
			
			else if(dsid.equals("V_TARIFF_HEADER_PAY")||dsid.equals("V_TARIFF_HEADER_PAY1")){
				String keys = sqlUtil.getColName("V_TARIFF_HEADER");
				sf.append("SELECT ");
				sf.append(keys);
				sf.append(" FROM V_TARIFF_HEADER");
				sf.append(" WHERE 1=1 ");
//				sf.append(sqlUtil.addLikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addEqualSQL("TFF_TYP", req.getParameter("TFF_TYP")));
				sf.append(sqlUtil.addEqualSQL("OBJECT_ID", req.getParameter("OBJECT_ID")));
				sf.append(sqlUtil.addALikeSQL("TFF_NAME", req.getParameter("TFF_NAME")));
				sf.append(sqlUtil.addEqualSQL("COM_FLAG", req.getParameter("COM_FLAG")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and EXEC_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(req.getParameter("EXEC_ORG_ID"));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(req.getParameter("EXEC_ORG_ID"));
						sf.append(",%' ) ");
					}
					else {
						sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				}
//				boolean flag2=false;
//				sf.append(" and id in (select tff_id from V_TARIFF_RULE where 1=1 ");
//				if(ObjUtil.isNotNull(req.getParameter("WHR_START_AREA_ID"))){
//					sf.append(sqlUtil.addEqualSQL("WHR_START_AREA_ID", req.getParameter("WHR_START_AREA_ID")));
//					flag2=true;
//				}
//				if(ObjUtil.isNotNull(req.getParameter("WHR_END_AREA_ID"))){
//					sf.append(sqlUtil.addEqualSQL("WHR_END_AREA_ID", req.getParameter("WHR_END_AREA_ID")));
//					flag2=true;
//				}
//				if(ObjUtil.isNotNull(req.getParameter("TRANS_SRVC_ID"))){
//					sf.append(sqlUtil.addEqualSQL("WHR_TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
//					flag2=true;
//				}
//				if(ObjUtil.isNotNull(req.getParameter("BIZ_TYP"))){
//					sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
//					flag2=true;
//				}
//				if(flag2){
//					sf.append(" group by tff_id)");
//				}else{
//					sf.append(")");
//				}
//				flag2=false;
				sf.append(sqlUtil.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(sqlUtil.addFlagSQL("COM_FLAG", req.getParameter("COM_FLAG")));
				
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("INCLUDE_SUB_FLAG", Hibernate.YES_NO);
				map.put("COM_FLAG", Hibernate.YES_NO);
				query  = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			/*else if(dsid.equals("V_TARIFF_HEADER_REC")){
				String keys = sqlUtil.getColName("V_TARIFF_HEADER");
				sf.append("SELECT ");
				sf.append(keys);
				sf.append(" FROM V_TARIFF_HEADER");
				sf.append(" WHERE 1=1 AND TFF_FLAG='Y' and tff_typ='42666CA2DE904F6687FC172138CF3E51' ");
				sf.append(sqlUtil.addLikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addEqualSQL("TFF_TYP", req.getParameter("TFF_TYP")));
				sf.append(sqlUtil.addEqualSQL("OBJECT_ID", req.getParameter("OBJECT_ID")));
				sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
				sf.append(sqlUtil.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("INCLUDE_SUB_FLAG", Hibernate.YES_NO);
				query  = sqlUtil.getQuery(sf.toString(), keys, map);
			}*/
			else if(dsid.equals("V_POLE_HEADER")){
				String keys = sqlUtil.getColName("V_TARIFF_HEADER");
				sf.append("SELECT ");
				sf.append(keys);
				sf.append(" FROM V_TARIFF_HEADER");
				sf.append(" WHERE 1=1 AND TFF_FLAG='N' ");
				sf.append(sqlUtil.addLikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addEqualSQL("TFF_TYP", req.getParameter("TFF_TYP")));
				sf.append(sqlUtil.addEqualSQL("OBJECT_ID", req.getParameter("OBJECT_ID")));
				sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
				sf.append(sqlUtil.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("INCLUDE_SUB_FLAG", Hibernate.YES_NO);
				query  = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_TARIFF_RULE")||dsid.equals("V_TARIFF_RULE1")){
				String keys = sqlUtil.getColName("V_TARIFF_RULE");
				sf.append("SELECT ");
				sf.append(keys);
				sf.append(" FROM V_TARIFF_RULE");
				sf.append(" WHERE 1=1 ");
				sf.append(sqlUtil.addEqualSQL("TFF_ID", req.getParameter("TFF_ID")));
				sf.append(sqlUtil.addEqualSQL("WHR_START_AREA_ID", req.getParameter("WHR_START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("WHR_END_AREA_ID", req.getParameter("WHR_END_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
				sf.append(sqlUtil.addEqualSQL("WHR_TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
				sf.append(sqlUtil.addEqualSQL("TFF_TYP", req.getParameter("TFF_TYP")));
				sf.append(sqlUtil.addEqualSQL("FEE_ID", req.getParameter("FEE_ID")));
				sf.append(sqlUtil.addEqualSQL("COM_FLAG", req.getParameter("COM_FLAG")));
				sf.append(sqlUtil.addLikeSQL("WHR_UDF1", req.getParameter("WHR_UDF1")));
				sf.append(sqlUtil.addLikeSQL("WHR_UDF2", req.getParameter("WHR_UDF2")));
				sf.append(sqlUtil.addLikeSQL("WHR_UDF3", req.getParameter("WHR_UDF3")));
				sf.append(sqlUtil.addLikeSQL("WHR_UDF4", req.getParameter("WHR_UDF4")));
				sf.append(sqlUtil.addLikeSQL("WHR_UDF5", req.getParameter("WHR_UDF5")));
				sf.append(sqlUtil.addLikeSQL("WHR_UDF6", req.getParameter("WHR_UDF6")));
				sf.append(" order by TFF_ID");
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				//map.put("COM_FLAG", Hibernate.YES_NO);
				
				query  = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("TARIFF_CONDITION")||dsid.equals("TARIFF_CONDITION1")){
				String keys = sqlUtil.getColName("TARIFF_CONDITION");
				sf.append("SELECT ");
				sf.append(keys);
				sf.append(" FROM TARIFF_CONDITION");
				sf.append(" WHERE 1=1 ");
				sf.append(sqlUtil.addEqualSQL("RUL_ID", req.getParameter("RUL_ID")));
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("SETT_SHPM_FEE")){
				String keys = sqlUtil.getColName("V_BILL_PAY");
				sf.append("SELECT ");
				sf.append(keys);
				sf.append(" FROM V_BILL_PAY");
				sf.append(" WHERE 1=1 ");
				sf.append(sqlUtil.addLikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));
				sf.append(sqlUtil.addEqualSQL("REFENENCE2", req.getParameter("REFENENCE2")));//作业单组号
				sf.append(sqlUtil.addMathSQL("REFENENCE1", req.getParameter("SERIAL_NUM_FROM"), ">="));
				sf.append(sqlUtil.addMathSQL("REFENENCE1", req.getParameter("SERIAL_NUM_TO"), "<="));
				
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addLeftLikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(sqlUtil.addLikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("SHPM_STATUS_FROM"),">="));//wangjun 2010-07-19
				sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("SHPM_STATUS_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("ACCOUNT_STAT", req.getParameter("ACCOUNT_STAT")));
				sf.append(sqlUtil.addEqualSQL("AUDIT_STAT", req.getParameter("AUDIT_STAT")));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("END_LOAD_TIME_FROM"), ">="));//wangjun 2011-06-21
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("END_LOAD_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("ACCOUNT_TIME", req.getParameter("ACCOUNT_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ACCOUNT_TIME", req.getParameter("ACCOUNT_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("AUDIT_TIME", req.getParameter("AUDIT_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("AUDIT_TIME", req.getParameter("AUDIT_TIME_TO"), "<="));
				sf.append(sqlUtil.addMathSQL("SERIAL_NUM", req.getParameter("SERIAL_NUM_FROM"), ">="));
				sf.append(sqlUtil.addMathSQL("SERIAL_NUM", req.getParameter("SERIAL_NUM_TO"), "<="));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));
				sf.append(sqlUtil.addLikeSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));
				sf.append(sqlUtil.addLikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and EXEC_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(req.getParameter("EXEC_ORG_ID"));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(req.getParameter("EXEC_ORG_ID"));
						sf.append(",%' ) ");
					}
					else {
						sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				}
				sf.append(" order by odr_time desc");
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if(dsid.equals("SETT_SHPM_FEE_DETAIL")){
				String keys = sqlUtil.getColName("V_BILL_SHPM_PAY");
				sf.append("SELECT ");
				sf.append(keys);
				sf.append(" FROM V_BILL_SHPM_PAY");
				sf.append(" WHERE 1=1 ");
				sf.append(sqlUtil.addEqualSQL("DOC_NO", ObjUtil.ifNull(req.getParameter("SHPM_NO"),req.getParameter("SHPM_NO"))));//作业单组号
				sf.append(sqlUtil.addEqualSQL("FEE_NAME", req.getParameter("FEE_NAME")));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if(dsid.equals("V_BILL_LOAD_PAY")||dsid.equals("V_BILL_LOAD_PAY3")){//按调度单展现 费用 + 汇总费用
				String keys  = sqlUtil.getColName("V_BILL_PAY");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_PAY");
				sf.append(" where 1=1 and load_no = doc_no");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("DEPART_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("DEPART_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("START_AREA_ID", req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("END_AREA_ID", req.getParameter("END_AREA_ID")));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
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
			}
			else if(dsid.equals("V_BILL_LOAD_PAY1")){//按调度单展现 费用 + 汇总费用
				String keys  = sqlUtil.getColName("V_BMS_BILL_PAY");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BMS_BILL_PAY");
				sf.append(" where 1=1 and load_no = doc_no");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("DEPART_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("DEPART_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("START_AREA_ID", req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("END_AREA_ID", req.getParameter("END_AREA_ID")));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
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
			}
			else if(dsid.equals("V_BILL_LOAD_PAY2")){
				String keys  = sqlUtil.getColName("V_BILL_PAY");
				sf.append("select ");
				sf.append(keys);
				sf.append(",DUE_FEE*UDF2 as TAX_VAL,PAY_FEE-DUE_FEE*UDF2 as VAL_AFT_TAX");
				sf.append(" from V_BILL_PAY");
				sf.append(" where 1=1 and load_no = doc_no");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("DEPART_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("DEPART_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("START_AREA_ID", req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("END_AREA_ID", req.getParameter("END_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("PAY_STAT", req.getParameter("PAY_STAT")));
				sf.append(sqlUtil.addALikeSQL("LOAD_AREA_CODE2", req.getParameter("LOAD_AREA_CODE2")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_AREA_CODE2", req.getParameter("UNLOAD_AREA_CODE2")));
				String fields=keys+",TAX_VAL,VAL_AFT_TAX";
				query  = sqlUtil.getQuery(sf.toString(), fields, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
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
			}
			else if(dsid.equals("V_BILL_SHPM_PAY")||dsid.equals("V_BILL_SHPM_PAY1")){ //按调度单展现 费用 + 展开后数据
				String keys  = sqlUtil.getColName("V_BILL_SHPM_PAY");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_SHPM_PAY");
				sf.append(" where load_no <> doc_no ");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO",req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("DOC_NO",req.getParameter("DOC_NO")));
				sf.append(sqlUtil.addEqualSQL("FEE_NAME", req.getParameter("FEE_NAME")));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if (dsid.equals("V_BILL_ORDER_HEADER")) {
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("V_BMS_ORDER_HEADER");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(sqlUtil.getColName("V_BMS_ORDER_HEADER", "t"));
				sf.append(" from V_BMS_ORDER_HEADER t");
				sf.append(" where 1=1 ");
//				sf.append("and status>=40 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				sf.append(sqlUtil.addEqualSQL("PARENT_CUSTOMER_ID", req.getParameter("PARENT_CUSTOMER_ID")));
//				sf.append(sqlUtil.addALikeSQL("LOAD_AREA_NAME", req.getParameter("LOAD_AREA_NAME")));
//				sf.append(sqlUtil.addALikeSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID2")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
				sf.append(sqlUtil.addALikeSQL("ODR_NO", req.getParameter("ODR_NO")));
//				sf.append(sqlUtil.addALikeSQL("GEN_METHOD", req.getParameter("GEN_METHOD")));
//				sf.append(sqlUtil.addALikeSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));
				//sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),"false"));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"),"<="));
//				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_FROM"),">="));
//				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("DATE_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("DATE_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("PLAN_STAT", req.getParameter("PLAN_STAT")));
				sf.append(sqlUtil.addEqualSQL("LOAD_STAT", req.getParameter("LOAD_STAT")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_STAT", req.getParameter("UNLOAD_STAT")));
				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ODR_TYP")));
				//sf.append(sqlUtil.addFlagSQL("SLF_PICKUP_FLAG", req.getParameter("SLF_PICKUP_FLAG")));
				sf.append(sqlUtil.addTimeSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addEqualSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), "false"));
				//sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_NAME")));
				sf.append(sqlUtil.addEqualSQL("AUDIT_STAT", req.getParameter("AUDIT_STAT")));
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FORM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FORM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FORM")));
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FORM"),">="));
				}
//				String rece_stat=req.getParameter("RECE_STAT");
//				sf.append(sqlUtil.addEqualSQL("RECE_STAT", rece_stat));
//				if(ObjUtil.isNotNull(rece_stat) && "10".equals(rece_stat)){
//					sf.append(" and (");
//					sf.append("RECE_STAT="+rece_stat);
//					sf.append(" or RECE_STAT is null)");
//				}else{
//					sf.append(sqlUtil.addEqualSQL("RECE_STAT", req.getParameter("RECE_STAT")));
//				}
				
//				sf.append(sqlUtil.addLeftLikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addLeftLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(" order by odr_time desc");
				
				HashMap<String, Type> map = new HashMap<String, Type>();
				query  = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			}
			else if (dsid.equals("V_BMS_ORDER_HEADER1")) {
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("V_BMS_ORDER_HEADER1");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(sqlUtil.getColName("V_BMS_ORDER_HEADER1", "t"));
				sf.append(" from V_BMS_ORDER_HEADER1 t");
				sf.append(" where 1=1 ");
//				sf.append("and status>=40 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				sf.append(sqlUtil.addEqualSQL("PARENT_CUSTOMER_ID", req.getParameter("PARENT_CUSTOMER_ID")));
//				sf.append(sqlUtil.addALikeSQL("LOAD_AREA_NAME", req.getParameter("LOAD_AREA_NAME")));
//				sf.append(sqlUtil.addALikeSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID2")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
				sf.append(sqlUtil.addALikeSQL("ODR_NO", req.getParameter("ODR_NO")));
//				sf.append(sqlUtil.addALikeSQL("GEN_METHOD", req.getParameter("GEN_METHOD")));
//				sf.append(sqlUtil.addALikeSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));
				//sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),"false"));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"),"<="));
//				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_FROM"),">="));
//				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"),"<="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("DATE_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("DATE_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("PLAN_STAT", req.getParameter("PLAN_STAT")));
				sf.append(sqlUtil.addEqualSQL("LOAD_STAT", req.getParameter("LOAD_STAT")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_STAT", req.getParameter("UNLOAD_STAT")));
				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ODR_TYP")));
				//sf.append(sqlUtil.addFlagSQL("SLF_PICKUP_FLAG", req.getParameter("SLF_PICKUP_FLAG")));
				sf.append(sqlUtil.addTimeSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("PRE_POD_TIME", req.getParameter("PRE_POD_TIME_TO"),"<="));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addEqualSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), "false"));
				//sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
				sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_NAME")));
				sf.append(sqlUtil.addEqualSQL("AUDIT_STAT", req.getParameter("AUDIT_STAT")));
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FORM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FORM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FORM")));
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FORM"),">="));
				}
//				String rece_stat=req.getParameter("RECE_STAT");
//				sf.append(sqlUtil.addEqualSQL("RECE_STAT", rece_stat));
//				if(ObjUtil.isNotNull(rece_stat) && "10".equals(rece_stat)){
//					sf.append(" and (");
//					sf.append("RECE_STAT="+rece_stat);
//					sf.append(" or RECE_STAT is null)");
//				}else{
//					sf.append(sqlUtil.addEqualSQL("RECE_STAT", req.getParameter("RECE_STAT")));
//				}
				
//				sf.append(sqlUtil.addLeftLikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addLeftLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(" order by odr_time desc");
				
				HashMap<String, Type> map = new HashMap<String, Type>();
				query  = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			}
			else if(dsid.equals("V_BILL_REC1")){ //按调度单展现 费用 + 展开后数据
				sqlUtil.setTableAlias("t");
				String keys  = sqlUtil.getColName("V_BILL_REC1");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_REC1 t");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("DOC_NO",req.getParameter("DOC_NO")));
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO",req.getParameter("CUSTOM_ODR_NO")));
				if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_ID"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID",req.getParameter("CUSTOMER_ID")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("PARENT_CUSTOMER_ID"))) {
					sf.append(sqlUtil.addEqualSQL("PARENT_CUSTOMER_ID",req.getParameter("PARENT_CUSTOMER_ID")));	
				}
				/*else {
					sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
				}*/
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), "false"));
				sf.append(" order by ODR_NO ASC");
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("BAS_VALUE", Hibernate.FLOAT);
				map.put("DISCOUNT_RATE", Hibernate.FLOAT);
				map.put("PRICE", Hibernate.FLOAT);
				map.put("PRE_FEE", Hibernate.FLOAT);
				map.put("DUE_FEE", Hibernate.FLOAT);
				map.put("PAY_FEE", Hibernate.FLOAT);
				query  = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
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
			}
			else if(dsid.equals("V_BILL_REC2")){ //按调度单展现 费用 + 展开后数据
				sqlUtil.setTableAlias("t");
				String keys  = sqlUtil.getColName("V_BILL_REC1");
				sf.append("select ");
				sf.append(keys);
				sf.append(",DUE_FEE*UDF2 AS TAX_VAL,PAY_FEE-DUE_FEE*UDF2 AS VAL_AFT_TAX");
				sf.append(" from V_BILL_REC1 t");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addALikeSQL("DOC_NO",req.getParameter("DOC_NO")));
				sf.append(sqlUtil.addALikeSQL("REFENENCE1",req.getParameter("REFENENCE1")));
				if(!"0".equals(req.getParameter("RECE_STAT"))){
					sf.append(sqlUtil.addEqualSQL("RECE_STAT",req.getParameter("RECE_STAT")));
				}
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO",req.getParameter("CUSTOM_ODR_NO")));
				if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_ID"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID",req.getParameter("CUSTOMER_ID")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("PARENT_CUSTOMER_ID"))) {
					sf.append(sqlUtil.addEqualSQL("PARENT_CUSTOMER_ID",req.getParameter("PARENT_CUSTOMER_ID")));	
				}
				/*else {
					sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
				}*/
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ATTR", req.getParameter("CUSTOM_ATTR")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID2")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));
				sf.append(sqlUtil.addALikeSQL("LOAD_AREA_CODE2", req.getParameter("LOAD_AREA_CODE2")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_AREA_CODE2", req.getParameter("UNLOAD_AREA_CODE2")));
				sf.append(sqlUtil.addALikeSQL("UDF3", req.getParameter("UDF3")));
				sf.append(sqlUtil.addALikeSQL("UDF4", req.getParameter("UDF4")));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), "false"));
				sf.append(" order by ODR_NO ASC");
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("BAS_VALUE", Hibernate.FLOAT);
				map.put("DISCOUNT_RATE", Hibernate.FLOAT);
				map.put("PRICE", Hibernate.FLOAT);
				map.put("PRE_FEE", Hibernate.FLOAT);
				map.put("DUE_FEE", Hibernate.FLOAT);
				map.put("PAY_FEE", Hibernate.FLOAT);
				String fields=keys+",TAX_VAL,VAL_AFT_TAX";
				query  = sqlUtil.getQuery(sf.toString(), fields, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
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
			}
			else if(dsid.equals("V_BILL_GROUP")){ //按调度单展现 费用 + 展开后数据
				String keys  = sqlUtil.getColName("V_BILL_GROUP");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_GROUP");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("DOC_NO",req.getParameter("DOC_NO")));
				if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_ID"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID",req.getParameter("CUSTOMER_ID")));	
				}
				/*else {
					sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
				}*/
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
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
			}
			else if(dsid.equals("V_BILL_REC")){ //按调度单展现 费用 + 展开后数据
				String keys  = sqlUtil.getColName("V_BILL_REC");
				sf.append("select ");
				sf.append(keys);
//				sf.append("FEE_ID,FEE_BASE_NAME,PAY_FEE,CUSTOMER_NAME,DOC_NO,ID,CHARGE_TYPE,FEE_NAME,FEE_BAS,BAS_VALUE,PRICE,PRE_FEE,DUE_FEE,DISCOUNT_RATE,DISCOUNT_FEE,PROTOCOL_NO,NOTES," +
//						"AUDIT_STAT,AUDIT_TIME,AUDITOR,ADDWHO,EDITWHO,RECE_STAT,RECE_TIME,RECER,ACT_RECE_TIME,PRE_RECE_TIME");
				sf.append(" from V_BILL_REC");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("DOC_NO",req.getParameter("DOC_NO")));
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("BAS_VALUE", Hibernate.FLOAT);
				map.put("DISCOUNT_RATE", Hibernate.FLOAT);
				map.put("PRICE", Hibernate.FLOAT);
				map.put("PRE_FEE", Hibernate.FLOAT);
				map.put("DUE_FEE", Hibernate.FLOAT);
				map.put("PAY_FEE", Hibernate.FLOAT);
				query  = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if(dsid.equals("V_BILL_SETTLE_INFO_PAY")){ //结算单
				String keys  = sqlUtil.getColName("V_BILL_SETTLE_INFO");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_SETTLE_INFO");
				sf.append(" where SETT_TYPE='应付费用' ");
				sf.append(sqlUtil.addEqualSQL("INVO_GRP_ID", req.getParameter("INVO_GRP_ID")));
				sf.append(" order by addtime desc");
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if(dsid.equals("V_BILL_SETTLE_INFO_REC")){ //结算单
				String keys  = sqlUtil.getColName("V_BILL_SETTLE_INFO");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_SETTLE_INFO");
				sf.append(" where SETT_TYPE='应收费用' ");
				sf.append(sqlUtil.addEqualSQL("INVO_GRP_ID", req.getParameter("INVO_GRP_ID")));
				sf.append(" order by addtime desc");
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if(dsid.equals("V_BILL_SETTLE_INFO_")){ //结算单
				String keys  = sqlUtil.getColName("V_BILL_SETTLE_INFO");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_SETTLE_INFO");
				sf.append(" where 1=1 and (invo_grp_id is null or invo_grp_id ='') ");
				sf.append(sqlUtil.addEqualSQL("INVO_GRP_ID", req.getParameter("INVO_GRP_ID")));
				sf.append(" and sett_type='应收费用'");
				sf.append(sqlUtil.addEqualSQL("SETT_NAME", req.getParameter("SETT_NAME")));
				sf.append(sqlUtil.addEqualSQL("AUDIT_STAT", req.getParameter("AUDIT_STAT")));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("SETT_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("SETT_TIME_TO"),"<="));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_BILL_ALL")){ //结算单对应的费用明细
				String keys  = sqlUtil.getColName("V_BILL_ALL");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_ALL");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("SETT_TYPE", req.getParameter("SETT_TYPE")));
				sf.append(sqlUtil.addALikeSQL("DOC_NO", req.getParameter("DOC_NO")));
				sf.append(sqlUtil.addEqualSQL("SETT_NAME", req.getParameter("SETT_NAME")));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"),">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"),"<="));
				sf.append(" and (sett_no is null or sett_no ='')");
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_BILL_SETTLE_DETAIL")){ //结算单对应的费用明细
				String keys  = sqlUtil.getColName("V_BILL_ALL");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_ALL");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("SETT_NO", req.getParameter("SETT_NO")));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
			}else if(dsid.equals("V_BILL_VERIFI")){
				String keys  = sqlUtil.getColName("BILL_VERIFICATION");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BILL_VERIFICATION");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("SETT_NO", req.getParameter("SETT_NO")));
				sf.append(sqlUtil.addEqualSQL("INVO_GRP_ID", req.getParameter("INVO_GRP_ID")));
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if(dsid.equals("V_BILL_INVOICE")){ //发票信息
				String keys  = sqlUtil.getColName("V_BILL_INVOICE");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_INVOICE");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("GRP_ID", req.getParameter("GRP_ID")));
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_BILL_INVO_GRP")){ //发票组
				String keys  = sqlUtil.getColName("V_BILL_INVO_GRP");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_INVO_GRP");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("SETT_NO", req.getParameter("SETT_NO")));
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if (dsid.equals("TMP_FEE_SET")){
				String keys = sqlUtil.getColName("V_TMP_FEE_SET");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_TMP_FEE_SET");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))){
					sf.append(" and full_index like '%");
					sf.append(req.getParameter("FULL_INDEX"));
					sf.append("%'");
				}
				query = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
				
				List<HashMap<String, String>> object = query.list();
			}
			else if(dsid.equals("V_CUSTOM_MONTHLY")){ //按调度单展现 费用 + 展开后数据
				sqlUtil.setTableAlias("t");
				String keys  = sqlUtil.getColName("V_CUSTOM_MONTHLY");
				sf.append("select ");
				sf.append(keys);
				sf.append(",ROWNUM AS SEQ");
				sf.append(" from V_CUSTOM_MONTHLY t");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("ODR_NO",req.getParameter("DOC_NO")));
				sf.append(sqlUtil.addALikeSQL("REFENENCE1",req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO",req.getParameter("CUSTOM_ODR_NO")));
				String billing = req.getParameter("BILLING");
				if(!ObjUtil.isNotNull(billing)) ;
				else if(Boolean.parseBoolean(billing))
					sf.append(" and BILLING_FLAG_ID is not null");
				else if(!Boolean.parseBoolean(billing))
					sf.append(" and BILLING_FLAG_ID is null");
				if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_ID"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID",req.getParameter("CUSTOMER_ID")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("PARENT_CUSTOMER_ID"))) {
					sf.append(sqlUtil.addEqualSQL("PARENT_CUSTOMER_ID",req.getParameter("PARENT_CUSTOMER_ID")));	
				}
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), "false"));
				sf.append(" order by SEQ ASC");
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("DISCOUNT_RATE", Hibernate.FLOAT);
				map.put("PRE_FEE", Hibernate.FLOAT);
				map.put("DUE_FEE", Hibernate.FLOAT);
				query  = sqlUtil.getQuery(sf.toString(), keys+",SEQ", map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
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
			}
			else if(dsid.equals("V_FEE_WARN")){
				sqlUtil.setTableAlias("t");
				String keys="DOC_TYPE,DOC_NO,ID,ADDWHO,NOTES";
				sf=new StringBuffer();
				sf.append("select ");
				sf.append("t.DOC_TYPE,t.DOC_NO,t.ID,t.ADDWHO,t.NOTES");
				sf.append(",t2.NAME_C AS DOC_TYP_NAME");
				sf.append(" from TMP_FEE_WARN t,bas_codes t2");
				sf.append(" WHERE 1=1");
				sf.append(" AND t.DOC_TYPE=t2.CODE(+) AND t2.PROP_CODE='DOC_TYP'");
				sf.append(sqlUtil.addEqualSQL("DOC_TYPE",req.getParameter("DOC_TYPE")));
//				sf.append(sqlUtil.addALikeSQL("DOC_NO", req.getParameter("DOC_NO")));
				sf.append(sqlUtil.addALikeSQL("DOC_NO", req.getParameter("DOC_NO")));
//				sf.append(sqlUtil.addBatchSQL("DOC_NO",req.getParameter("DOC_NO")));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("t.ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(" ORDER BY t.addtime desc");
				query=sqlUtil.getQuery(sf.toString(), keys+",DOC_TYP_NAME", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			}
			else if (dsid.equals("T_SETTLEMENT_MANIFEST")){
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("T_SETTLEMENT_MANIFEST");
				sf.append("select ");
				sf.append(sqlUtil.getColName("T_SETTLEMENT_MANIFEST", "t"));
				sf.append(" from T_SETTLEMENT_MANIFEST t");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				sf.append(sqlUtil.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));
				sf.append(sqlUtil.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("DEPART_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("DEPART_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("RECEIPT_FLAG", req.getParameter("RECEIPT_FLAG")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					sf.append(" and 0 < (select count(1) from bas_customer_org o where o.customer_id=t.customer_id ");
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and o.ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(req.getParameter("EXEC_ORG_ID"));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(req.getParameter("EXEC_ORG_ID"));
						sf.append(",%' ) ");
					}else {
						sf.append("and o.org_id = '");
						sf.append(req.getParameter("EXEC_ORG_ID"));
						sf.append("' ");
					}
					sf.append(") ");
				}
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("RECEIPT_FLAG", Hibernate.YES_NO);
				query = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
				
				List<HashMap<String, String>> object = query.list();
			}
			else if(dsid.equals("TRANS_CLAIM_APPROVE")){ //发票组
				String keys  = sqlUtil.getColName("TRANS_CLAIM_APPROVE");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from TRANS_CLAIM_APPROVE");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_ID"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID",req.getParameter("CUSTOMER_ID")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH",req.getParameter("BELONG_MONTH")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(sqlUtil.addALikeSQL("LOAD_NO",req.getParameter("LOAD_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))) {
					sf.append(sqlUtil.addALikeSQL("PLATE_NO",req.getParameter("PLATE_NO")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("TRANS_BILL_SETTLE")){ 
				String keys  = sqlUtil.getColName("TRANS_BILL_SETTLE");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from TRANS_BILL_SETTLE");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_NAME"))) {
					sf.append(sqlUtil.addALikeSQL("CUSTOMER_NAME",req.getParameter("CUSTOMER_NAME")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("FEE_USAGE"))) {
					sf.append(sqlUtil.addEqualSQL("FEE_USAGE",req.getParameter("FEE_USAGE")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("PAYER"))) {
					sf.append(sqlUtil.addALikeSQL("PAYER",req.getParameter("PAYER")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("TRANS_BILL_RECE")){ 
				String keys  = sqlUtil.getColName("TRANS_BILL_RECE");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from TRANS_BILL_RECE");
				sf.append(" where 1=1 ");
				
				if(ObjUtil.isNotNull(req.getParameter("SETTLE_NO"))) {
					sf.append(sqlUtil.addEqualSQL("SETTLE_NO",req.getParameter("SETTLE_NO")));	
				}
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("TRANS_BILL_INVOICE")){ 
				String keys  = sqlUtil.getColName("TRANS_BILL_INVOICE");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from TRANS_BILL_INVOICE");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("SETTLE_ID"))) {
					sf.append(sqlUtil.addEqualSQL("SETTLE_ID",req.getParameter("SETTLE_ID")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("V_PAYALLOWANCE")){ //结算管理-应付补贴单
				String keys  = sqlUtil.getColName("V_PAYALLOWANCE");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_PAYALLOWANCE");
				sf.append(" where 1=1 ");
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
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("ALLOWANCE_TYPE"))) {
					sf.append(sqlUtil.addEqualSQL("ALLOWANCE_TYPE", req.getParameter("ALLOWANCE_TYPE")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("SHPM_NO"))) {
					sf.append(sqlUtil.addEqualSQL("SHPM_NO", req.getParameter("SHPM_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("V_BILL_REC_ALLOWANCE")){ //结算管理-应收补贴单
				String keys  = sqlUtil.getColName("V_BILL_REC_ALLOWANCE");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_REC_ALLOWANCE");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_ID_NAME"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH", req.getParameter("BELONG_MONTH")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("ALLOWANCE_TYPE"))) {
					sf.append(sqlUtil.addEqualSQL("ALLOWANCE_TYPE", req.getParameter("ALLOWANCE_TYPE")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))) {
					sf.append(sqlUtil.addEqualSQL("PLATE_NO", req.getParameter("PLATE_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("SHPM_NO"))) {
					sf.append(sqlUtil.addEqualSQL("SHPM_NO", req.getParameter("SHPM_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("V_REC_DEDUCT")){ //结算管理-应收扣款单
				String keys  = sqlUtil.getColName("V_REC_DEDUCT");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_REC_DEDUCT");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_ID_NAME"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH", req.getParameter("BELONG_MONTH")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("DEDUCT_TYPE"))) {
					sf.append(sqlUtil.addEqualSQL("DEDUCT_TYPE", req.getParameter("DEDUCT_TYPE")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))) {
					sf.append(sqlUtil.addEqualSQL("PLATE_NO", req.getParameter("PLATE_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("SHPM_NO"))) {
					sf.append(sqlUtil.addEqualSQL("SHPM_NO", req.getParameter("SHPM_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BILL_PAY_INITIAL")){ 
				//费用管理/结算管理/应付期初账单/主表
				String keys  = sqlUtil.getColName("V_BILL_PAY_INITIAL");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_PAY_INITIAL t");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_NAME"))) {
					sf.append(sqlUtil.addEqualSQL("SUPLR_NAME", req.getParameter("SUPLR_NAME")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH", req.getParameter("BELONG_MONTH")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("ACCOUNT_STAT"))) {
					sf.append(sqlUtil.addEqualSQL("ACCOUNT_STAT", req.getParameter("ACCOUNT_STAT")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_TYP"))) {
					sf.append(sqlUtil.addEqualSQL("SUPLR_TYP", req.getParameter("SUPLR_TYP")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BILL_PAY_INITDETAILS") || dsid.equals("BILL_PAY_INITDETAILS2")){ 
				//费用管理/结算管理/应付期初账单/从表
				String keys  = sqlUtil.getColName("V_PAY_INITDETAILS");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_PAY_INITDETAILS");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("INIT_NO"))) {
					sf.append(sqlUtil.addEqualSQL("INIT_NO", req.getParameter("INIT_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("INVOICE_STAT"))) {
					sf.append(sqlUtil.addEqualSQL("INVOICE_STAT",req.getParameter("INVOICE_STAT")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("ACCOUNT_STAT"))) {
					sf.append(sqlUtil.addEqualSQL("ACCOUNT_STAT",req.getParameter("ACCOUNT_STAT")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_REC_INITIAL")){ 
				String keys  = sqlUtil.getColName("V_BILL_REC_INITIAL");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BILL_REC_INITIAL");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("BUSS_NAME"))) {
					sf.append(sqlUtil.addEqualSQL("BUSS_NAME",req.getParameter("BUSS_NAME")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH",req.getParameter("BELONG_MONTH")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("ACCOUNT_STAT"))) {
					sf.append(sqlUtil.addEqualSQL("ACCOUNT_STAT",req.getParameter("ACCOUNT_STAT")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_REC_INITDETAILS")|| dsid.equals("V_REC_INITDETAILS2")){ 
				String keys  = sqlUtil.getColName("V_REC_INITDETAILS");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_REC_INITDETAILS");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("INIT_NO"))) {
					sf.append(sqlUtil.addEqualSQL("INIT_NO",req.getParameter("INIT_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("INVOICE_STAT"))) {
					sf.append(sqlUtil.addEqualSQL("INVOICE_STAT",req.getParameter("INVOICE_STAT")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("ACCOUNT_STAT"))) {
					sf.append(sqlUtil.addEqualSQL("ACCOUNT_STAT",req.getParameter("ACCOUNT_STAT")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			
			
			}
			else if(dsid.equals("V_REC_INIT")){ 
				String keys  = sqlUtil.getColName("V_REC_INIT");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_REC_INIT");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_CNAME"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOMER_CNAME",req.getParameter("CUSTOMER_CNAME")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("ODR_NO"))) {
					sf.append(sqlUtil.addEqualSQL("ODR_NO",req.getParameter("ODR_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("CUSTOM_ODR_NO"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO",req.getParameter("CUSTOM_ODR_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("FEE_TYPE"))) {
					sf.append(sqlUtil.addEqualSQL("FEE_TYPE",req.getParameter("FEE_TYPE")));	
				}
//				if(ObjUtil.isNotNull(req.getParameter("LOAD_NAME"))) {
//					sf.append(sqlUtil.addEqualSQL("LOAD_NAME",req.getParameter("LOAD_NAME")));	
//				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH",req.getParameter("BELONG_MONTH")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			}
			else if(dsid.equals("V_PAY_INIT")){ 
				String keys  = sqlUtil.getColName("V_PAY_INIT");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_PAY_INIT");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_CNAME"))) {
					sf.append(sqlUtil.addEqualSQL("SUPLR_CNAME",req.getParameter("SUPLR_CNAME")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(sqlUtil.addEqualSQL("LOAD_NO",req.getParameter("LOAD_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("FEE_TYPE"))) {
					sf.append(sqlUtil.addEqualSQL("FEE_TYPE",req.getParameter("FEE_TYPE")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))) {
					sf.append(sqlUtil.addEqualSQL("PLATE_NO",req.getParameter("PLATE_NO")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH",req.getParameter("BELONG_MONTH")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			}

			else if(dsid.equals("V_REC_ADJUST")||dsid.equals("V_REC_ADJUST1")){ 
				String keys  = sqlUtil.getColName("V_REC_ADJUST");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_REC_ADJUST");
				sf.append(" where 1=1");
				//sf.append(sqlUtil.addEqualSQL("ROLE_ID",req.getParameter("ROLE_ID")));	
				if(ObjUtil.isNotNull(req.getParameter("BUSS_ID"))) {
					sf.append(sqlUtil.addEqualSQL("BUSS_ID",req.getParameter("BUSS_ID")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH",req.getParameter("BELONG_MONTH")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS",req.getParameter("STATUS")));	
				}
				//if(ObjUtil.isNotNull(req.getParameter("ROLE_ID"))) {
					sf.append(sqlUtil.addEqualSQL("ROLE_ID",req.getParameter("ROLE_ID")));	
				//}
				sf.append(" order by addtime desc");
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_REC_DETAILS_ADJUST")||dsid.equals("V_REC_DETAILS_ADJUST1")){ 
				String keys  = sqlUtil.getColName("V_REC_DETAILS_ADJUST");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_REC_DETAILS_ADJUST");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("ADJ_NO", req.getParameter("ADJ_NO")));
				sf.append(" order by addtime desc");
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}

			else if(dsid.equals("BILL_PAY_ADJUST")){ 
				//费用管理/结算管理/应付调整账单/主表
				String keys  = sqlUtil.getColName("BILL_PAY_ADJUST");
				sf.append("select DISTINCT ");
				sf.append("t.ID,t.ADJ_NO,t.SUPLR_ID,t.SUPLR_NAME,t.BELONG_MONTH,t.INITITAL_AMOUNT,t.CONFIRM_AMOUNT,t.ADJ_AMOUNT," +
						"t.TAX_AMOUNT,t.SUBTAX_AMOUNT,t.STATUS,t.ROLE_ID,t.LISTER,t.LISTER_TIME,t.ADJ_REASON,t.NOTES,t.ADDTIME," +
						"t.ADDWHO,t.EDITTIME,t.EDITWHO,t.BILL_TO,t.INIT_NO,t.CUSTOMER_ID ");

				sf.append(" from BILL_PAY_ADJUST t inner join SYS_APPROVE_LOG l ");
				sf.append("on t.ADJ_NO = l.DOC_NO(+) ");
				if(ObjUtil.isNotNull(req.getParameter("USERID"))){
					sf.append(" and l.APPROVER='"+req.getParameter("USERID")+"'");
				}
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_NAME"))) {
					sf.append(sqlUtil.addEqualSQL("t.SUPLR_NAME", req.getParameter("SUPLR_NAME")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("t.BELONG_MONTH", req.getParameter("BELONG_MONTH")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(sqlUtil.addEqualSQL("t.STATUS", req.getParameter("STATUS")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("SYS_APPROVE_LOG")){
				//费用管理/结算管理/应付调整账单/主表/审批日志
				String keys  = sqlUtil.getColName("SYS_APPROVE_LOG");
				sf.append("select ");
				sf.append("l.ID,l.DOC_NO,l.APPROVER_RESULT,nvl(r.ROLE_NAME,l.role_id) as ROLE_ID,l.LISTER,to_char(l.LISTER_TIME,'YYYY-MM-DD HH24:MI') as LISTER_TIME," +
						"l.APPROVER,to_char(l.APPROVE_TIME,'YYYY-MM-DD HH24:MI') as APPROVE_TIME,l.NOTES,l.ADDTIME,l.ADDWHO,l.EDITTIME,l.EDITWHO,l.DOC_TYPE");
				sf.append(" from SYS_APPROVE_LOG l,SYS_ROLE r");
				sf.append(" where l.ROLE_ID=r.ID(+) and l.DOC_TYPE='PAY_ADJNO' ");
				if(ObjUtil.isNotNull(req.getParameter("DOC_NO"))){
					sf.append(sqlUtil.addEqualSQL("l.DOC_NO", req.getParameter("DOC_NO")));	
				}
				sf.append(" order by l.APPROVE_TIME");
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("SYS_APPROVE_LOG1")){
				//费用管理/结算管理/应付调整账单/主表/审批日志
				String keys  = sqlUtil.getColName("SYS_APPROVE_LOG");
				sf.append("select ");
				sf.append("l.ID,l.DOC_NO,l.APPROVER_RESULT,nvl(r.ROLE_NAME,l.role_id) as ROLE_ID,l.LISTER,to_char(l.LISTER_TIME,'YYYY-MM-DD HH24:MI') as LISTER_TIME," +
						"l.APPROVER,to_char(l.APPROVE_TIME,'YYYY-MM-DD HH24:MI') as APPROVE_TIME,l.NOTES,l.ADDTIME,l.ADDWHO,l.EDITTIME,l.EDITWHO,l.DOC_TYPE");
				sf.append(" from SYS_APPROVE_LOG l,SYS_ROLE r");
				sf.append(" where l.ROLE_ID=r.ID(+) and l.DOC_TYPE = 'REC_ADJNO' ");
				if(ObjUtil.isNotNull(req.getParameter("DOC_NO"))){
					sf.append(sqlUtil.addEqualSQL("l.DOC_NO", req.getParameter("DOC_NO")));	
				}
				sf.append(" order by l.APPROVE_TIME");
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BILL_PAY_ADJDETAILS")){ 
				//费用管理/结算管理/应付调整账单/从表
				String keys  = sqlUtil.getColName("BILL_PAY_ADJDETAILS");
				sf.append("select ");
				sf.append("ID,ADJ_NO,SUPLR_ID,SUPLR_NAME,LOAD_NO,PLATE_NO,DRIVER,VEHICLE_TYP_ID,LOAD_ID,LOAD_NAME," +
						"UNLOAD_ID,UNLOAD_NAME,TOT_QNTY,TOT_GROSS_W,TOT_NET_W,TOT_VOL,INITITAL_AMOUNT,CONFIRM_AMOUNT1," +
						"ADJ_AMOUNT1,ADJ_REASON1,CONFIRM_AMOUNT2,ADJ_AMOUNT2,ADJ_REASON2,TRANS_FEE,PRE_FEE,DEDUCT_FEE," +
						"ALLOWANCE_FEE,ADDTIME,ADDWHO,EDITTIME,EDITWHO,INIT_NO,NOTES,INIT_DETAIL_ID,CONFIRM_AMOUNT3,ADJ_AMOUNT3,ADJ_REASON3,MOBILE");
				sf.append(",to_char(LOAD_DATE,'YYYY-MM-DD HH24:MI') as LOAD_DATE,INIT_AMOUNT,");
				sf.append("to_char(UNLOAD_DATE,'YYYY-MM-DD HH24:MI') as UNLOAD_DATE");
				sf.append(" from BILL_PAY_ADJDETAILS");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("ADJ_NO"))) {
					sf.append(sqlUtil.addEqualSQL("ADJ_NO", req.getParameter("ADJ_NO")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BILL_PAY_ADJAUDIT")){ 
				//费用管理/结算管理/待审应付调整单/主表
				String keys  = sqlUtil.getColName("BILL_PAY_ADJUST");
				sf.append("select ");
				sf.append("ID,ADJ_NO,SUPLR_ID,SUPLR_NAME,BELONG_MONTH,INITITAL_AMOUNT,CONFIRM_AMOUNT,ADJ_AMOUNT," +
						"TAX_AMOUNT,SUBTAX_AMOUNT,STATUS,ROLE_ID,LISTER,to_char(LISTER_TIME,'YYYY-MM-DD HH24:MI') as LISTER_TIME,ADJ_REASON,NOTES,ADDTIME," +
						"ADDWHO,EDITTIME,EDITWHO,BILL_TO,INIT_NO,CUSTOMER_ID");
				sf.append(" from BILL_PAY_ADJUST where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("ROLE_ID"))) {
					sf.append(sqlUtil.addEqualSQL("ROLE_ID", req.getParameter("ROLE_ID")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_NAME"))) {
					sf.append(sqlUtil.addEqualSQL("SUPLR_NAME", req.getParameter("SUPLR_NAME")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH", req.getParameter("BELONG_MONTH")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BILL_PAY_ADJAUDITDETAILS")){ 
				//费用管理/结算管理/待审应付调整单/从表
				String keys  = sqlUtil.getColName("BILL_PAY_ADJDETAILS");
				sf.append("select ");
				sf.append("ID,ADJ_NO,SUPLR_ID,SUPLR_NAME,LOAD_NO,PLATE_NO,DRIVER,VEHICLE_TYP_ID,LOAD_ID,LOAD_NAME," +
						"UNLOAD_ID,UNLOAD_NAME,TOT_QNTY,TOT_GROSS_W,TOT_NET_W,TOT_VOL,INITITAL_AMOUNT,CONFIRM_AMOUNT1," +
						"ADJ_AMOUNT1,ADJ_REASON1,CONFIRM_AMOUNT2,ADJ_AMOUNT2,ADJ_REASON2,TRANS_FEE,PRE_FEE,DEDUCT_FEE," +
						"ALLOWANCE_FEE,ADDTIME,ADDWHO,EDITTIME,EDITWHO,INIT_NO,NOTES,INIT_DETAIL_ID,CONFIRM_AMOUNT3,ADJ_AMOUNT3,ADJ_REASON3,MOBILE,INIT_AMOUNT");
				sf.append(",to_char(LOAD_DATE,'YYYY-MM-DD HH24:MI') as LOAD_DATE,");
				sf.append("to_char(UNLOAD_DATE,'YYYY-MM-DD HH24:MI') as UNLOAD_DATE");
				sf.append(" from BILL_PAY_ADJDETAILS");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("ADJ_NO"))) {
					sf.append(sqlUtil.addEqualSQL("ADJ_NO", req.getParameter("ADJ_NO")));	
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_PAY_REQUEST")||dsid.equals("V_PAY_REQUEST1")){ 
				String keys  = sqlUtil.getColName("BILL_PAY_REQUEST");
				sf.append("select DISTINCT ");
				sf.append("p.act_amount,p.addtime,p.addwho,p.allowance_amount,p.BELONG_MONTH,p.bill_no,p.deduct_amount,p.bill_by,");
				sf.append("p.edittime,p.editwho,p.id,p.initital_amount,p.lister,p.lister_time,p.notes,p.pay_amount,b1.name_c as pay_status,p.pre_amount,");
				sf.append("to_char(p.rece_time,'yyyy/mm/dd') as rece_time,to_char(p.invoice_time,'yyyy/mm/dd') as invoice_time,");
				sf.append("to_char(p.bill_time,'yyyy/mm/dd') as bill_time,to_char(p.PAY_TIME,'yyyy/mm/dd') as PAY_TIME,");
				sf.append("p.req_no,p.role_id,p.status,p.subtax_amount,p.suplr_id,p.BILL_STATUS,p.pay_by,");
				sf.append("p.suplr_name,p.tax_amount,p.INIT_NO ,p.CUSTOMER_ID,");
				sf.append("to_char(p.LATEST_PAY_TIME,'yyyy/mm/dd') as LATEST_PAY_TIME,p.CUSTOMER_ID ");
				sf.append(" from BILL_PAY_REQUEST p ");
				sf.append(",BAS_CODES b1");
				sf.append(" where p.PAY_STATUS=b1.CODE(+) and b1.prop_code='PAY_STAT'");
				if(ObjUtil.isNotNull(req.getParameter("ROLE_ID"))) {
					sf.append(sqlUtil.addEqualSQL("p.ROLE_ID", req.getParameter("ROLE_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_ID_NAME"))) {
					sf.append(sqlUtil.addEqualSQL("p.SUPLR_NAME", req.getParameter("SUPLR_ID_NAME")));
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("p.BELONG_MONTH", req.getParameter("BELONG_MONTH")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
				if(ObjUtil.isNotNull(req.getParameter("REQ_ROLE_ID"))) {
					sf.append(sqlUtil.addEqualSQL("p.ROLE_ID", req.getParameter("REQ_ROLE_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("INVOICE_NUM"))){
					sf.append(" and p.REQ_NO");
	            	sf.append(" IN ");
	            	sf.append("    (select distinct REQ_NO ");
	            	sf.append("     From BILL_PAY_INVOICEINFO ");
					sf.append("     Where INVOICE_NUM like '%");
					sf.append(req.getParameter("INVOICE_NUM"));
					sf.append("%') ");
					
				}

				
				sf.append(" order by addtime desc");
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_PAY_REQDETAILS")||dsid.equals("V_PAY_REQDETAILS1")||dsid.equals("V_PAY_REQDETAILS2")){ 
				String keys  = sqlUtil.getColName("V_PAY_REQDETAILS");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_PAY_REQDETAILS");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("REQ_NO"))) {
					sf.append(sqlUtil.addEqualSQL("REQ_NO", req.getParameter("REQ_NO")));
				}
				sf.append(" order by addtime desc");
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("BILL_REC_INVOICE")||dsid.equals("BILL_REC_INVOICE1") || dsid.equals("BILL_REC_INVOICE2")){ 
				String keys  = sqlUtil.getColName("V_REC_INVOICE");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_REC_INVOICE");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("ROLE_ID"))) {
					sf.append(sqlUtil.addEqualSQL("ROLE_ID", req.getParameter("ROLE_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("BUSS_NAME"))) {
					sf.append(sqlUtil.addEqualSQL("BUSS_NAME", req.getParameter("BUSS_NAME")));
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH", req.getParameter("BELONG_MONTH")));
				}
				if(ObjUtil.isNotNull(req.getParameter("BILL_TO"))) {
					sf.append(sqlUtil.addALikeSQL("BILL_TO", req.getParameter("BILL_TO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
				if(ObjUtil.isNotNull(req.getParameter("BILL_STATUS"))) {
					sf.append(sqlUtil.addEqualSQL("BILL_STATUS", req.getParameter("BILL_STATUS")));
				}
				if(ObjUtil.isNotNull(req.getParameter("BILL_TIME_FROM"))) {
					sf.append(sqlUtil.addTimeSQL("BILL_TIME", req.getParameter("BILL_TIME_FROM"), ">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("BILL_TIME_TO"))) {
					sf.append(sqlUtil.addTimeSQL("BILL_TIME", req.getParameter("BILL_TIME_TO"), "<="));
				}
				if(ObjUtil.isNotNull(req.getParameter("REC_STATUS"))) {
					sf.append(sqlUtil.addEqualSQL("REC_STATUS", req.getParameter("REC_STATUS")));
				}
				if(ObjUtil.isNotNull(req.getParameter("RECE_TIME_FROM"))) {
					sf.append(sqlUtil.addTimeSQL("RECE_TIME", req.getParameter("RECE_TIME_FROM"), ">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("RECE_TIME_TO"))) {
					sf.append(sqlUtil.addTimeSQL("RECE_TIME", req.getParameter("RECE_TIME_TO"), "<="));
				}
				sf.append(" order by addtime desc");
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("BILL_REC_INVOICEDETAILS")){ 
				String keys  = sqlUtil.getColName("V_REC_INVOICEDETAILS");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_REC_INVOICEDETAILS");
				sf.append(" where 1=1 ");				
				sf.append(sqlUtil.addEqualSQL("INVOICE_NO", req.getParameter("INVOICE_NO")));

				sf.append(" order by addtime desc");
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("BILL_REC_INVOICEINFO")){ 
				String keys  = sqlUtil.getColName("V_REC_INVOICEINFO");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_REC_INVOICEINFO");
				sf.append(" where 1=1 ");			
				sf.append(sqlUtil.addEqualSQL("INVOICE_NO", req.getParameter("INVOICE_NO")));
				sf.append(" order by addtime desc");
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("BILL_REC_RECELOG")){ 
				String keys  = sqlUtil.getColName("V_REC_RECELOG");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_REC_RECELOG");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("INVOICE_NO", req.getParameter("INVOICE_NO")));
				sf.append(" order by rece_time");
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
//			else if(dsid.equals("BILL_REC_INVOICE1")){ 
//				String keys  = sqlUtil.getColName("BILL_REC_INVOICE");
//				sf.append("select ");
//				sf.append(keys);
//				sf.append(" from BILL_REC_INVOICE");
//				sf.append(" where 1=1 ");
//				if(ObjUtil.isNotNull(req.getParameter("ROLE_ID"))) {
//					sf.append(sqlUtil.addEqualSQL("ROLE_ID", req.getParameter("ROLE_ID")));
//				}
//				if(ObjUtil.isNotNull(req.getParameter("BUSS_NAME"))) {
//					sf.append(sqlUtil.addEqualSQL("BUSS_NAME", req.getParameter("BUSS_NAME")));
//				}
//				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
//					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH", req.getParameter("BELONG_MONTH")));
//				}
//				if(ObjUtil.isNotNull(req.getParameter("BILL_TO"))) {
//					sf.append(sqlUtil.addALikeSQL("BILL_TO", req.getParameter("BILL_TO")));
//				}
//				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
//					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));
//				}
//				if(ObjUtil.isNotNull(req.getParameter("BILL_STATUS"))) {
//					sf.append(sqlUtil.addEqualSQL("BILL_STATUS", req.getParameter("BILL_STATUS")));
//				}
//				if(ObjUtil.isNotNull(req.getParameter("BILL_TIME_FROM"))) {
//					sf.append(sqlUtil.addTimeSQL("BILL_TIME", req.getParameter("BILL_TIME_FROM"), ">="));
//				}
//				if(ObjUtil.isNotNull(req.getParameter("BILL_TIME_TO"))) {
//					sf.append(sqlUtil.addTimeSQL("BILL_TIME", req.getParameter("BILL_TIME_TO"), "<="));
//				}
//				if(ObjUtil.isNotNull(req.getParameter("REC_STATUS"))) {
//					sf.append(sqlUtil.addEqualSQL("REC_STATUS", req.getParameter("REC_STATUS")));
//				}
//				if(ObjUtil.isNotNull(req.getParameter("RECE_TIME_FROM"))) {
//					sf.append(sqlUtil.addTimeSQL("RECE_TIME", req.getParameter("RECE_TIME_FROM"), ">="));
//				}
//				if(ObjUtil.isNotNull(req.getParameter("RECE_TIME_TO"))) {
//					sf.append(sqlUtil.addTimeSQL("RECE_TIME", req.getParameter("RECE_TIME_TO"), "<="));
//				}
//				sf.append(" order by addtime desc");
//				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//			}
			else if(dsid.equals("BILL_REC_INVOICEDETAILS1")){ 
				String keys  = sqlUtil.getColName("BILL_REC_INVOICEDETAILS");
				sf.append("select ");
				sf.append("ID,INVOICE_NO,BUSS_ID,BUSS_NAME,CUSTOM_ODR_NO,ODR_NO,VEHICLE_TYP_ID,LOAD_ID,LOAD_NAME," +
						"UNLOAD_ID,UNLOAD_NAME,TOT_QNTY,TOT_GROSS_W,TOT_NET_W,TOT_VOL,ACT_AMOUNT,TAX_AMOUNT," +
						"SUBTAX_AMOUNT,ADDTIME,ADDWHO,EDITTIME,EDITWHO,NOTES");
				sf.append(",to_char(ODR_TIME,'YYYY-MM-DD') as ODR_TIME");
				sf.append(",to_char(LOAD_DATE,'YYYY-MM-DD') as LOAD_DATE");
				sf.append(",to_char(UNLOAD_DATE,'YYYY-MM-DD') as UNLOAD_DATE");
				sf.append(" from BILL_REC_INVOICEDETAILS");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("INVOICE_NO"))) {
					sf.append(sqlUtil.addEqualSQL("INVOICE_NO", req.getParameter("INVOICE_NO")));
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			//计费日志
			else if(dsid.equals("BILL_LOG")){ 
				String keys  = sqlUtil.getColName("BILL_LOG");
				sf.append("select ");
				sf.append(" l.ID,l.OBJECT_NAME,l.OBJECT_TYPE,l.DOC_NO,(case l.BILL_RESULT when 'Y' then '成功' else '失败' end) as BILL_RESULT,l.NOTES,l.ADDWHO,b.NAME_C as BILL_TYPE,l.OBJECT_ID");
				sf.append(",to_char(l.ADDTIME,'YYYY-MM-DD HH24:MI') as ADDTIME");
				sf.append(" from BILL_LOG l,BAS_CODES b");
				sf.append(" where l.BILL_TYPE = b.CODE(+)");
				if(ObjUtil.isNotNull(req.getParameter("OBJECT_NAME"))) { 	
					sf.append(sqlUtil.addALikeSQL("l.OBJECT_NAME", req.getParameter("OBJECT_NAME")));
				}
				if(ObjUtil.isNotNull(req.getParameter("BILL_RESULT"))) { 	
					sf.append(sqlUtil.addEqualSQL("l.BILL_RESULT", req.getParameter("BILL_RESULT")));
				}
				if(ObjUtil.isNotNull(req.getParameter("DOC_NO"))) { 	
					sf.append(sqlUtil.addALikeSQL("l.DOC_NO", req.getParameter("DOC_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("FROM_TIME"))) {
					sf.append(sqlUtil.addTimeSQL("l.ADDTIME", req.getParameter("FROM_TIME"), ">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("END_TIME"))) {
					sf.append(sqlUtil.addTimeSQL("l.ADDTIME", req.getParameter("END_TIME"), "<="));
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			//费用修改日志
			else if(dsid.equals("BILL_MODIFY_LOG")){ 
				String keys  = sqlUtil.getColName("BILL_MODIFY_LOG");
				sf.append("select ");
				sf.append(" DOC_NO,OPERATION_NAME,FROM_BASE,TO_BASE,FROM_PRICE,TO_PRICE,FROM_AMOUNT,TO_AMOUNT,ADDWHO,NOTES");
				sf.append(",to_char(ADDTIME,'YYYY-MM-DD HH24:MI') as ADDTIME");
				sf.append(" from BILL_MODIFY_LOG");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("DOC_NO"))) {
					sf.append(sqlUtil.addALikeSQL("DOC_NO", req.getParameter("DOC_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("FROM_TIME"))) {
					sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("FROM_TIME"), ">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("END_TIME"))) {
					sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("END_TIME"), "<="));
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			//折扣
			else if(dsid.equals("TARIFF_ADDITIONAL")){ 
				String keys  = sqlUtil.getColName("TARIFF_ADDITIONAL");
				sf.append("select ");
				sf.append(" ID,RUL_ID,TFF_ID,BIZ_DATEOBJ,ADJ_RATIO,AVAIL_BY,ADDWHO,EDITTIME,EDITWHO");
				sf.append(",to_char(ADDTIME,'YYYY-MM-DD HH24:MI') as ADDTIME");
				sf.append(",to_char(ADJ_DATEOBJ,'YYYY-MM-DD') as ADJ_DATEOBJ");
				sf.append(" from TARIFF_ADDITIONAL");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("RUL_ID"))) {
					sf.append(sqlUtil.addEqualSQL("RUL_ID", req.getParameter("RUL_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("TFF_ID"))) {
					sf.append(sqlUtil.addEqualSQL("TFF_ID", req.getParameter("TFF_ID")));
				}
//				if(ObjUtil.isNotNull(req.getParameter("END_TIME"))) {
//					sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("END_TIME"), "<="));
//				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			//计费要素管理
			else if(dsid.equals("TARIFF_FACTOR")){ 
				String keys  = sqlUtil.getColName("TARIFF_FACTOR");
				sf.append("select ");
				sf.append(" ID,FEE_FACTOR,FM_TABLE,FM_FIELD,FEE_TABLE,FEE_FIELD,OTHER_CONDITION,EDITTIME,EDITWHO,ADDWHO,ACTIVE_FLAG,FEE_TYPE,BIZ_OBJECT,OBJ_TYPE,DICT_PARAM,DATA_FROM,DATA_ID,DATA_NAME");
				sf.append(",to_char(ADDTIME,'YYYY-MM-DD') as ADDTIME");
				sf.append(" from TARIFF_FACTOR");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("FEE_FACTOR"))) {
					sf.append(sqlUtil.addALikeSQL("FEE_FACTOR", req.getParameter("FEE_FACTOR")));
				}
				if(ObjUtil.isNotNull(req.getParameter("ACTIVE_FLAG"))) {
					sf.append(sqlUtil.addEqualSQL("ACTIVE_FLAG", "Y"));
				}
				if(ObjUtil.isNotNull(req.getParameter("FROM_TIME"))) {
					sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("FROM_TIME"), ">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("END_TIME"))) {
					sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("END_TIME"), "<="));
				}
				if(ObjUtil.isNotNull(req.getParameter("FEE_TYPE"))) {
					sf.append(sqlUtil.addEqualSQL("FEE_TYPE", req.getParameter("FEE_TYPE")));
				}
				
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ACTIVE_FLAG", Hibernate.YES_NO);
				
				query  = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_BMS_ORDER_HEADER")){ 
				String keys  = sqlUtil.getColName("V_BMS_ORDER_HEADER");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BMS_ORDER_HEADER");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_NAME", req.getParameter("CUSTOMER_NAME")));
				sf.append(" order by ADDTIME DESC ");
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_PAY_REQDETAILS_NEW")){ 
				String keys  = sqlUtil.getColName("V_PAY_REQDETAILS");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_PAY_REQDETAILS");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("REQ_NO", req.getParameter("REQ_NO")));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BILL_PAY_INVOICEINFO")){ 
				String keys  = sqlUtil.getColName("BILL_PAY_INVOICEINFO");
				sf.append("select ");
				sf.append(" ID,INVOICE_NUM,INVOICE_TYPE,EXPRESS_NO,AMOUNT,TAX_RATIO,TAX_AMOUNT,");
				sf.append(" ACT_AMOUNT,INVOICE_BY,NOTES,REQ_NO,RECE_AMOUNT,RECE_BY,ADDTIME,ADDWHO,EDITTIME,EDITWHO,");
				sf.append(" SUBTAX_AMOUNT");
				sf.append(",to_char(INVOICE_RECTIME,'YYYY-MM-DD HH24:MI') as INVOICE_RECTIME");
				sf.append(",to_char(INVOICE_TIME,'YYYY-MM-DD HH24:MI') as INVOICE_TIME");
				sf.append(",to_char(RECE_TIME,'YYYY-MM-DD HH24:MI') as RECE_TIME");
				sf.append(" from BILL_PAY_INVOICEINFO");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("REQ_NO", req.getParameter("REQ_NO")));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BILL_PAY_PAYLOG")){ 
				String keys  = sqlUtil.getColName("BILL_PAY_PAYLOG");
				sf.append("select ");
				sf.append("ID,REQ_NO,INVOICE_NUM,RECE_BY,RECE_AMOUNT,ADDTIME,ADDWHO,EDITTIME,EDITWHO,VOUCHER_NO");
				sf.append(",to_char(COLLECTION_TIME,'YYYY-MM-DD') as COLLECTION_TIME");
				sf.append(",to_char(RECE_TIME,'YYYY-MM-DD HH24:MI') as RECE_TIME");
				sf.append(" from BILL_PAY_PAYLOG");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("REQ_NO", req.getParameter("REQ_NO")));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if (dsid.equals("BMS_ORDER_ITEM")) {  //客户费用管理
				String keys = "ODR_ROW,LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE,UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE,SKU_NAME,SKU,TEMPERATURE1,UOM,QNTY,VOL,G_WGT,NOTES,N_WGT,LOTATT01";
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(",t1.ID,t2.NAME_C as TEMPERATURE1_NAME, (select NVL(t3.VOL_GWT_RATIO,'0') from BAS_SKU t3 where t3.ID = t1.SKU_ID) as VOL_GWT_RATIO");
				sf.append(" from BMS_ORDER_ITEM t1,BAS_CODES t2");
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
			}
			else if(dsid.equals("BMS_SHIPMENT")){ 
				String keys  = sqlUtil.getColName("BMS_SHIPMENT");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BMS_SHIPMENT");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BILL_REC_DAMAGE")){ 
				String keys  = sqlUtil.getColName("BILL_REC_DAMAGE");
				sf.append("select b.NAME_C as STATUS_NAME,");
				sf.append("d.*");
				sf.append(" from BILL_REC_DAMAGE d,BAS_CODES b");
				sf.append(" where 1=1 and d.STATUS = b.CODE(+) and b.prop_code='APPROVE_STS' ");
				if(ObjUtil.isNotNull(req.getParameter("BUSS_ID"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("BUSS_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH", req.getParameter("BELONG_MONTH")));
				}
				if(ObjUtil.isNotNull(req.getParameter("DAMAGE_NO"))) {
					sf.append(sqlUtil.addALikeSQL("DAMAGE_NO", req.getParameter("DAMAGE_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
				query  = sqlUtil.getQuery(sf.toString(), keys+",STATUS_NAME", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("BILL_REC_DMGINVOICEINFO")){ 
				String keys  = sqlUtil.getColName("BILL_REC_DMGINVOICEINFO");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BILL_REC_DMGINVOICEINFO ");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("DAMAGE_NO"))) {
					sf.append(sqlUtil.addEqualSQL("DAMAGE_NO", req.getParameter("DAMAGE_NO")));
				}

				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}

			else if(dsid.equals("BILL_REC_DMGRECELOG")){ 
				String keys  = sqlUtil.getColName("BILL_REC_DMGRECELOG");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BILL_REC_DMGRECELOG ");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("DAMAGE_NO"))) {
					sf.append(sqlUtil.addEqualSQL("DAMAGE_NO", req.getParameter("DAMAGE_NO")));
				}

				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}

			else if(dsid.equals("BILL_REC_DAMAGE1")){ 
				String keys  = sqlUtil.getColName("BILL_REC_DAMAGE");
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BILL_REC_DAMAGE");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("BUSS_ID"))) {
					sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("BUSS_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("BELONG_MONTH"))) {
					sf.append(sqlUtil.addEqualSQL("BELONG_MONTH", req.getParameter("BELONG_MONTH")));
				}
				if(ObjUtil.isNotNull(req.getParameter("ROLE_ID"))) {
					sf.append(sqlUtil.addEqualSQL("ROLE_ID", req.getParameter("ROLE_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("SYS_APPROVE_LOG2")){
				//费用管理/结算管理/应付调整账单/主表/审批日志
				String keys  = sqlUtil.getColName("SYS_APPROVE_LOG");
				sf.append("select ");
				sf.append("l.ID,l.DOC_NO,l.APPROVER_RESULT,nvl(r.ROLE_NAME,l.role_id) as ROLE_ID,l.LISTER,to_char(l.LISTER_TIME,'YYYY-MM-DD HH24:MI') as LISTER_TIME," +
						"l.APPROVER,to_char(l.APPROVE_TIME,'YYYY-MM-DD HH24:MI') as APPROVE_TIME,l.NOTES,l.ADDTIME,l.ADDWHO,l.EDITTIME,l.EDITWHO,l.DOC_TYPE");
				sf.append(" from SYS_APPROVE_LOG l,SYS_ROLE r");
				sf.append(" where l.ROLE_ID=r.ID(+) and l.DOC_TYPE='PAY_REQNO'");
				if(ObjUtil.isNotNull(req.getParameter("DOC_NO"))){
					sf.append(sqlUtil.addEqualSQL("l.DOC_NO", req.getParameter("DOC_NO")));	
				}
				sf.append(" order by l.APPROVE_TIME");
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("SYS_APPROVE_LOG_INV")){
				//费用管理/结算管理/应付调整账单/主表/审批日志
				String keys  = sqlUtil.getColName("SYS_APPROVE_LOG");
				sf.append("select ");
				sf.append("l.ID,l.DOC_NO,l.APPROVER_RESULT,nvl(r.ROLE_NAME,l.role_id) as ROLE_ID,l.LISTER,to_char(l.LISTER_TIME,'YYYY-MM-DD HH24:MI') as LISTER_TIME," +
						"l.APPROVER,to_char(l.APPROVE_TIME,'YYYY-MM-DD HH24:MI') as APPROVE_TIME,l.NOTES,l.ADDTIME,l.ADDWHO,l.EDITTIME,l.EDITWHO,l.DOC_TYPE");
				sf.append(" from SYS_APPROVE_LOG l,SYS_ROLE r");
				sf.append(" where l.ROLE_ID=r.ID(+) and l.DOC_TYPE='REC_INVOICENO'");
				if(ObjUtil.isNotNull(req.getParameter("INVOICE_NO"))){
					sf.append(sqlUtil.addEqualSQL("l.DOC_NO", req.getParameter("INVOICE_NO")));	
				}
				sf.append(" order by l.APPROVE_TIME");
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
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
			
			if(ObjUtil.ifNull(CUR_PAGE,"1").equals("1") && object != null) {
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
			}
			else {
				int pos = sf.toString().toUpperCase().indexOf(" FROM");
		    	if(pos > 0) {
		    		LoginContent.getInstance().setWhere(sf.substring(pos));
		    	}
			}
			
			if(query != null){
				object = query.list();
				query = null;
			}
			Gson gson = new Gson();
			String content = gson.toJson(object);
			p.print(content);
			
			/*if(ObjUtil.ifNull(CUR_PAGE,"1").equals("1") && object != null) {
				LoginContent.getInstance().setPageInfo(sqlUtil.getRecordByQuery(sf.toString()));
			}else{
				int pos = sf.toString().toUpperCase().indexOf(" FROM");
		    	if(pos > 0) {
		    		LoginContent.getInstance().setWhere(sf.substring(pos));
		    	}
			}*/
			
			LoginContent.getInstance().closeSession();
			curSession = null;
			p.flush();
			p.close();
			p = null;
			
			//Log4j.info(StaticRef.SQL_LOG, sf.toString());
			//SUtil.insertlog(StaticRef.ACT_FETCH, StaticRef.ACT_SUCCESS, user.getUSER_NAME(), user.getDEFAULT_ORG_ID_NAME());
		}
	}

}