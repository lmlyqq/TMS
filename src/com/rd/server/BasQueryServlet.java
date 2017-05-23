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
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import com.google.gson.Gson;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.obj.system.SYS_USER;
import com.rd.server.util.LoginContent;
import com.rd.server.util.SQLUtil;
import com.rd.server.util.SUtil;

/**
 * 基础资料下所有模块对应的servlet
 * 
 * @author fanglm 
 * 
 */
@SuppressWarnings("serial")
public class BasQueryServlet extends HttpServlet {
	private Query query;
	private final int page_record = LoginContent.getInstance().pageSize;

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session =req.getSession();
		session.setMaxInactiveInterval(24*60*60);
		SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
		req.setCharacterEncoding("utf-8");
		String dsid = req.getParameter("ds_id"); // 数据源ID	
		String flag = SUtil.iif(req.getParameter("OP_FLAG"), ""); // 操作标记
		String is_curr_page = SUtil.iif(req.getParameter("is_curr_page"),""); //分页查询
		//SQLUtil sqlUtil = new SQLUtil(true);
		if (dsid != null && flag.equals(StaticRef.MOD_FLAG)) {
			SQLUtil util = new SQLUtil(true);
			response.setCharacterEncoding("utf-8");
//			req.setCharacterEncoding("utf-8");
			PrintWriter p = response.getWriter();
			if(user == null) {
				p.print("404");
				return;
			}
			String CUR_PAGE = (String) req.getParameter("CUR_PAGE"); // 当前页码
			int start_row = 0; // 开始行，从0开始
			StringBuffer sf = null;
			
			//行政区域下拉框初始化数据源、弹出二级窗口数据源
			if (dsid.equals("VC_BAS_AREA")) {
				//行政区域 fanglm
				String cols = "ID,AREA_CODE,AREA_CNAME,AREA_LEVEL,PROVICE_NAME,SHORT_NAME,HINT_CODE,show_name,PARENT_AREA_ID,SHOW_SEQ";
				sf = new StringBuffer();
				sf.append("select ID,AREA_CODE,AREA_CNAME,AREA_LEVEL,PROVICE_NAME,SHORT_NAME,HINT_CODE,show_name,PARENT_AREA_ID,SHOW_SEQ");
				sf.append(" from VC_AREA");
				/*if(ObjUtil.isNotNull(req.getParameter("OP_INDEX"))){
					sf.append(",VC_AREA t_area1  ");
				}*/
				sf.append(" where 1=1");
				if(ObjUtil.isNotNull(req.getParameter("CONTENT"))){
					sf.append(util.addALikeSQL("FULL_INDEX",req.getParameter("CONTENT").toUpperCase()));
				}
				/*if(ObjUtil.isNotNull(req.getParameter("OP_INDEX"))){
					sf.append(" and substr(t.OP_INDEX,1,length(t_area1.op_index)) = t_area1.op_index");
					sf.append(" and t_area1.FULL_INDEX like '%");
					sf.append(req.getParameter("OP_INDEX"));
					sf.append("%'");
				}*/
				sf.append(" order by AREA_CODE,SHOW_SEQ");
				query = util.getQuery(sf.toString(),cols,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			if (dsid.equals("BAS_AREA")) {
				//行政区域 fanglm
				String cols = "ID,AREA_CODE,AREA_CNAME,AREA_ENAME,SHORT_NAME,SHOW_NAME,PARENT_AREA_ID,AREA_TYPE,AREA_LEVEL,HINT_CODE," +
						"SHOW_SEQ,NOTES,ENABLE_FLAG,MODIFY_FLAG,PROVICE_NAME,OP_INDEX";
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(cols);
				sf.append(" from VC_AREA t");
				sf.append(" where 1=1");
				sf.append(util.addEqualSQL("PARENT_AREA_ID", req.getParameter("PARENT_AREA_ID")));
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))){
					sf.append(util.addALikeSQL("FULL_INDEX",req.getParameter("FULL_INDEX")));
				}
				if(ObjUtil.isNotNull(req.getParameter("CONTENT"))){
					sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("CONTENT")));
				}
				sf.append(util.addLikeSQL("AREA_ENAME",req.getParameter("AREA_ENAME")));
				sf.append(util.addLikeSQL("AREA_CODE",req.getParameter("AREA_CODE")));
				sf.append(util.addLikeSQL("HINT_CODE",req.getParameter("HINT_CODE")));
				sf.append(util.addLikeSQL("SHORT_NAME",req.getParameter("SHORT_NAME")));
				sf.append(util.addEqualSQL("AREA_TYPE",req.getParameter("AREA_TYPE")));
				if(req.getParameter("ENABLE_FLAG") != null){	
					sf.append(util.addFlagSQL("ENABLE_FLAG", ObjUtil.iif(req.getParameter("ENABLE_FLAG"), "true", "Y")));
				}
				if(req.getParameter("ENABLE_FLAG") != null){
					sf.append(util.addFlagSQL("MODIFY_FLAG", ObjUtil.iif(req.getParameter("MODIFY_FLAG"), "true", "Y")));
				}
	            sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));                                       //通过自定义方式查询	
				sf.append(" order by AREA_CODE,SHOW_SEQ");
				query = util.getQuery(sf.toString(),cols,null)
				.addScalar("ID",Hibernate.STRING).addScalar("AREA_CODE", Hibernate.STRING).addScalar("AREA_CNAME", Hibernate.STRING)
				.addScalar("AREA_ENAME", Hibernate.STRING).addScalar("SHORT_NAME", Hibernate.STRING).addScalar("SHOW_NAME", Hibernate.STRING)
				.addScalar("PARENT_AREA_ID", Hibernate.STRING).addScalar("AREA_TYPE", Hibernate.STRING)
				.addScalar("AREA_LEVEL", Hibernate.BIG_INTEGER).addScalar("HINT_CODE", Hibernate.STRING)
				.addScalar("SHOW_SEQ", Hibernate.BIG_INTEGER).addScalar("NOTES", Hibernate.STRING).addScalar("OP_INDEX",Hibernate.STRING)
				.addScalar("ENABLE_FLAG",Hibernate.YES_NO).addScalar("MODIFY_FLAG",Hibernate.YES_NO).addScalar("PROVICE_NAME",Hibernate.STRING)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			if (dsid.equals("BAS_AREA1")) {
				//行政区域 fanglm
				String cols = "ID,AREA_CODE,AREA_CNAME,AREA_ENAME,SHORT_NAME,SHOW_NAME,PARENT_AREA_ID,AREA_TYPE,AREA_LEVEL,HINT_CODE," +
						"SHOW_SEQ,NOTES,ENABLE_FLAG,MODIFY_FLAG,PROVICE_NAME,OP_INDEX";
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(cols);
				sf.append(" from VC_AREA");
				sf.append(" where 1=1");
				sf.append(util.addEqualSQL("PARENT_AREA_ID", req.getParameter("PARENT_AREA_ID")));
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))){
					sf.append(util.addALikeSQL("FULL_INDEX",req.getParameter("FULL_INDEX")));
				}
				if(ObjUtil.isNotNull(req.getParameter("CONTENT"))){
					sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("CONTENT")));
				}
				sf.append(util.addLikeSQL("AREA_ENAME",req.getParameter("AREA_ENAME")));
				sf.append(util.addLikeSQL("AREA_CODE",req.getParameter("AREA_CODE")));
				sf.append(util.addLikeSQL("HINT_CODE",req.getParameter("HINT_CODE")));
				sf.append(util.addLikeSQL("SHORT_NAME",req.getParameter("SHORT_NAME")));
				sf.append(util.addEqualSQL("AREA_TYPE",req.getParameter("AREA_TYPE")));
				if(req.getParameter("ENABLE_FLAG") != null){	
					sf.append(util.addFlagSQL("ENABLE_FLAG", ObjUtil.iif(req.getParameter("ENABLE_FLAG"), "true", "Y")));
				}
				if(req.getParameter("ENABLE_FLAG") != null){
					sf.append(util.addFlagSQL("MODIFY_FLAG", ObjUtil.iif(req.getParameter("MODIFY_FLAG"), "true", "Y")));
				}
	            sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));                                       //通过自定义方式查询	
				sf.append(" order by AREA_CODE,SHOW_SEQ");
				query = util.getQuery(sf.toString(),cols,null)
				.addScalar("ID",Hibernate.STRING).addScalar("AREA_CODE", Hibernate.STRING).addScalar("AREA_CNAME", Hibernate.STRING)
				.addScalar("AREA_ENAME", Hibernate.STRING).addScalar("SHORT_NAME", Hibernate.STRING).addScalar("SHOW_NAME", Hibernate.STRING)
				.addScalar("PARENT_AREA_ID", Hibernate.STRING).addScalar("AREA_TYPE", Hibernate.STRING)
				.addScalar("AREA_LEVEL", Hibernate.BIG_INTEGER).addScalar("HINT_CODE", Hibernate.STRING)
				.addScalar("SHOW_SEQ", Hibernate.BIG_INTEGER).addScalar("NOTES", Hibernate.STRING).addScalar("OP_INDEX",Hibernate.STRING)
				.addScalar("ENABLE_FLAG",Hibernate.YES_NO).addScalar("MODIFY_FLAG",Hibernate.YES_NO).addScalar("PROVICE_NAME",Hibernate.STRING)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			else if(dsid.equals("BAS_ORG")) {
				//组织机构  yuanlei
				String keys = util.getColName("V_ORG");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_ORG");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("AREA_ID"))) {
					sf.append(util.addEqualSQL("AREA_ID", req.getParameter("AREA_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("PARENT_ORG_ID"))) {
					if((ObjUtil.isNotNull(req.getParameter("INCLUDE_PARENT")) && 
							"Y".equals(req.getParameter("INCLUDE_PARENT"))) || 
							(ObjUtil.isNotNull(req.getParameter("IS_BASORG_VIEW")) && 
									"true".equals(req.getParameter("IS_BASORG_VIEW")))){
						sf.append(util.addEqualSQL("ID", req.getParameter("PARENT_ORG_ID")));
						sf.append(" UNION ALL ");
						sf.append("select ");
						sf.append(keys);
						sf.append(" from V_ORG start with PARENT_ORG_ID = '");
						sf.append(req.getParameter("PARENT_ORG_ID"));
						sf.append("' connect by prior ID = PARENT_ORG_ID");
					}else{
						sf.append(util.addALikeSQL("ORG_INDEX", req.getParameter("PARENT_ORG_ID")));
					}
				}
				if(ObjUtil.isNotNull(req.getParameter("ENABLE_FLAG"))) {
					sf.append(util.addFlagSQL("ENABLE_FLAG", ObjUtil.iif(req.getParameter("ENABLE_FLAG"), "true", "Y")));
				}
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))) {
					sf.append(util.addALikeSQL("ORG_CNAME", req.getParameter("FULL_INDEX")));
				}
	            sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));                                       //通过自定义方式查询	
	            HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("DEFAULT_FLAG", Hibernate.YES_NO);
	            query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);;
			}
			//组织机构----->客户列表  lijun
			else if(dsid.equals("V_ORG_CUSTOMER")){
				String cols = util.getColName("V_ORG_CUSTOMER");
				sf = new StringBuffer();
				sf.append("SELECT ");
				sf.append(cols);
				sf.append(" from V_ORG_CUSTOMER WHERE 1 = 1 ");
				sf.append(util.addEqualSQL("ID", req.getParameter("ID")));
				query = util.getQuery(sf.toString(),cols,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
			}
			//组织机构----->供应商列表 lijun
			else if(dsid.equals("V_ORG_SUPPLIER")){
				String cols = util.getColName("V_ORG_SUPPLIER");
				sf = new StringBuffer();
				sf.append("SELECT ");
				sf.append(cols);
				sf.append(" from V_ORG_SUPPLIER WHERE 1 = 1 ");
				sf.append(util.addEqualSQL("ID", req.getParameter("ID")));
				
				query = util.getQuery(sf.toString(),cols,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BAS_MSRMNT")) {
				//度量衡  yuanlei
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(util.getColName("BAS_MSRMNT"));
				sf.append(" from BAS_MSRMNT t");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("MSRMNT_CODE"))) {
					sf.append(util.addLikeSQL("MSRMNT_CODE", req.getParameter("MSRMNT_CODE")));
				}
				if(ObjUtil.isNotNull(req.getParameter("MSRMNT_NAME"))) {
					sf.append(util.addLikeSQL("MSRMNT_NAME", req.getParameter("MSRMNT_NAME")));
				}
				if(ObjUtil.isNotNull(req.getParameter("ENABLE_FLAG"))) {
					sf.append(util.addFlagSQL("ENABLE_FLAG", ObjUtil.iif(req.getParameter("ENABLE_FLAG"), "true", "Y")));
				}
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))) {
					sf.append(util.addALikeSQL("MSRMNT_CODE||MSRMNT_NAME", req.getParameter("FULL_INDEX")));
				}
	            sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));//通过自定义方式查询	
	            sf.append(" order by show_seq");
	            
	            query = util.getQuery(sf.toString(),null,null)
	            .addScalar("ID",Hibernate.STRING).addScalar("MSRMNT_CODE",Hibernate.STRING).addScalar("MSRMNT_NAME",Hibernate.STRING)
	            .addScalar("UDF1",Hibernate.STRING).addScalar("UDF2",Hibernate.YES_NO)
	            .addScalar("HINT_CODE",Hibernate.STRING).addScalar("SHOW_SEQ",Hibernate.STRING)
	            .addScalar("ENABLE_FLAG",Hibernate.YES_NO).addScalar("MODIFY_FLAG",Hibernate.YES_NO)
	            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("BAS_MSRMNT_UNIT")) {
				//度量衡  yuanlei
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(util.getColName("BAS_MSRMNT_UNIT"));
				sf.append(" from BAS_MSRMNT_UNIT");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("MSRMNT_CODE"))) {
					sf.append(util.addEqualSQL("MSRMNT_CODE", req.getParameter("MSRMNT_CODE")));
				}
	            sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));//通过自定义方式查询	
	            sf.append(" order by show_seq");
	            query = util.getQuery(sf.toString(),null,null)
	            .addScalar("ID",Hibernate.STRING).addScalar("UNIT",Hibernate.STRING).addScalar("UNIT_NAME",Hibernate.STRING)
	            .addScalar("CONVERT_SCALE",Hibernate.STRING).addScalar("BASUNIT_FLAG",Hibernate.YES_NO)
	            .addScalar("HINT_CODE",Hibernate.STRING).addScalar("SHOW_SEQ",Hibernate.STRING).addScalar("MSRMNT_CODE",Hibernate.STRING)
	            .addScalar("TRANS_UOM_FLAG",Hibernate.YES_NO)
	            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_BAS_SKU_CLS")){
				//货品分类 fanglm
				String key = util.getColName("V_BAS_SKU_CLS");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(key);
				sf.append(" from V_BAS_SKU_CLS");
				sf.append(" where 1=1");
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))){
					sf.append(" and upper(FULL_INDEX) like upper('%"+req.getParameter("FULL_INDEX")+"%')");
				}
					sf.append(util.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
					sf.append(util.addEqualSQL("DESCR_C", req.getParameter("DESCR_C")));
					sf.append(util.addEqualSQL("DESCR_E", req.getParameter("DESCR_E")));
					sf.append(util.addEqualSQL("SKUCLS", req.getParameter("SKUCLS")));
					sf.append(util.addFlagSQL("MIX_FLAG", ObjUtil.iif(req.getParameter("MIX_FLAG"), "true", "Y")));
					sf.append(util.addFlagSQL("ENABLE_FLAG", ObjUtil.iif(req.getParameter("ENABLE_FLAG"), "true", "Y")));
					sf.append(util.addFlagSQL("MODIFY_FLAG", ObjUtil.iif(req.getParameter("MODIFY_FLAG"), "true", "Y")));
					sf.append(util.addEqualSQL("FACTOR", req.getParameter("FACTOR")));
	            sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));                                       //通过自定义方式查询	
				sf.append(" ORDER BY SHOW_SEQ asc");
				query = util.getQuery(sf.toString(),key,null).addScalar("ID",Hibernate.STRING).addScalar("CUSTOMER_ID",Hibernate.STRING)
					.addScalar("SKUCLS",Hibernate.STRING).addScalar("DESCR_C", Hibernate.STRING).addScalar("DESCR_E", Hibernate.STRING)
					.addScalar("FACTOR",Hibernate.STRING).addScalar("MIX_FLAG", Hibernate.YES_NO).addScalar("ENABLE_FLAG", Hibernate.YES_NO)
					.addScalar("MODIFY_FLAG",Hibernate.YES_NO).addScalar("UDF1", Hibernate.STRING).addScalar("UDF2", Hibernate.STRING)
					.addScalar("ADDWHO", Hibernate.STRING).addScalar("EDITWHO", Hibernate.STRING)
					.addScalar("SHOW_SEQ", Hibernate.BIG_INTEGER).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			
			}
			else if(dsid.equals("BAS_PACKAGE")) {
				//包装 yuanlei
				String keys = util.getColName("V_PACKAGE");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_PACKAGE t");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))){
					sf.append(util.addALikeSQL("PACK||DESCR", req.getParameter("FULL_INDEX")));
				}
				/*if(ObjUtil.isNotNull(req.getParameter("PACK"))) {
					sf.append(util.addEqualSQL("PACK", req.getParameter("PACK")));
				}
				if(ObjUtil.isNotNull(req.getParameter("DESCR"))) {
					sf.append(util.addEqualSQL("DESCR", req.getParameter("DESCR")));
				}*/
	            sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));                                       //通过自定义方式查询	
	            sf.append(" order by addtime desc");
	            query = util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("SF_SKU")) {  //货品查询二级窗口
				String keys = "ID,SKU_CNAME,SKU,TRANS_COND,PACK_ID,TRANS_UOM,VOLUME,GROSSWEIGHT,COMMON_FLAG,VOL_GWT_RATIO";
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BAS_SKU ");
				Object obj = util.decode(req.getParameter("SHPM_NO"));
				
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(obj)){
					sf.append(" AND ID IN(");
					sf.append("SELECT SKU_ID FROM TRANS_SHIPMENT_ITEM WHERE SHPM_NO = '");
					sf.append(obj);
					sf.append("')");
				}
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))) {
					sf.append(" and (upper(SKU) like upper('%");
					sf.append(util.decode(req.getParameter("FULL_INDEX")));
					sf.append("%') or SKU_CNAME like '%");
					sf.append(util.decode(req.getParameter("FULL_INDEX")));
					sf.append("%')");
				}
				if(req.getParameter("INIT_FLAG").equals("N")) {
					if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_ID"))) {
						sf.append(" and customer_id = '");
						sf.append(util.decode(req.getParameter("CUSTOMER_ID")));
						sf.append("'");
					}
				}
				else {
					if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_ID"))) {
//						ArrayList<String> sqlList = util.getRecordByQuery("select ID from BAS_SKU WHERE customer_id = '" + req.getParameter("CUSTOMER_ID") + "'");
//						if(Integer.parseInt(sqlList.get(0)) > 0) {
							sf.append(" and (customer_id = '");
							sf.append(util.decode(req.getParameter("CUSTOMER_ID")));
							sf.append("'  or common_flag = 'Y')");
//						}
//						else {
//							sf.append(" and common_flag = 'Y'");
//						}
					}
					
				}

				HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("COMMON_FLAG", Hibernate.YES_NO);
	            query = util.getQuery(sf.toString(),keys , map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("BAS_SKU")) {  //基础资料货品查询
				//货品管理 fanglm
				String keys = util.getColName("V_SKU");
//				keys = keys.replaceAll(",", ",bas.");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_SKU t");
				Object obj = util.decode(req.getParameter("SHPM_NO"));
				
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(obj)){
//					sf.append(" and bas.id in(item.sku_id)");
					sf.append(" AND ID IN(");
					sf.append("SELECT SKU_ID FROM TRANS_SHIPMENT_ITEM WHERE SHPM_NO = '");
					sf.append(obj);
					sf.append("')");
				}
				
				//sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				if(ObjUtil.isNotNull(req.getParameter("CUSTOMER_ID"))){
					sf.append(util.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				}
				sf.append(util.addEqualSQL("SKU_CLS", req.getParameter("SKU_CLS")));
				sf.append(util.addEqualSQL("SKU", req.getParameter("SKU")));
				
				sf.append(util.addEqualSQL("SKU_CNAME", req.getParameter("SKU_CNAME")));
				sf.append(util.addEqualSQL("SKU_ENAME", req.getParameter("SKU_ENAME")));
				sf.append(util.addEqualSQL("SKU_ATTR", req.getParameter("SKU_ATTR")));
				sf.append(util.addEqualSQL("TRANS_COND", req.getParameter("TRANS_COND")));
				sf.append(util.addEqualSQL("PHYSICAL_FORM", req.getParameter("PHYSICAL_FORM")));
				sf.append(util.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(util.addFlagSQL("COMMON_FLAG", req.getParameter("COMMON_FLAG")));
				
				/*if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {//2010-06-30 wangjun
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
						sf.append(util.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				};*/
				//sf.append(util.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),req.getParameter("C_RDC_FLAG")));
				
	            sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));//通过自定义方式查询	
	            HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("ENABLE_FLAG", Hibernate.YES_NO);
	            map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
	            map.put("COMMON_FLAG", Hibernate.YES_NO);
	            query = util.getQuery(sf.toString(),keys , map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("BAS_ADDRESS")){
				//地址点管理 fanglm
				String keys = util.getColName("V_ADDRESS");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_ADDRESS t");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))) {
					sf.append(" and upper(FULL_INDEX) like upper('%"+req.getParameter("FULL_INDEX")+"%')");
				}
				if(ObjUtil.isNotNull(req.getParameter("ISLIST")) && "Y".equals(req.getParameter("ISLIST")))  {
				if(ObjUtil.isNotNull( req.getParameter("CUSTOMER_ID"))){
					sf.append(" and (CUSTOMER_ID = '");
					sf.append(req.getParameter("CUSTOMER_ID"));
					sf.append("' or CUSTOMER_ID IS NULL) ");
				}
				}
				else {
				    sf.append(util.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				}
				
				sf.append(util.addEqualSQL("WHSE_ID", req.getParameter("WHSE_ID")));
				if(ObjUtil.isNotNull(req.getParameter("BIZ_CODE"))){
					sf.append(" and ADDR_CODE IN(select distinct tms_addr_code from trans_sss_addr t where unit_code = '");
					sf.append(req.getParameter("BIZ_CODE"));
					sf.append("' or sf_addr_code = '");
					sf.append(req.getParameter("BIZ_CODE"));
					sf.append("')");
				}
				if(ObjUtil.isNotNull(req.getParameter("NULL_AREA_FLAG")) && req.getParameter("NULL_AREA_FLAG").toString().equals("true")){
					sf.append(" and area_id is null");
				}else{
					sf.append(util.addLikeSQL("AREA_NAME2", req.getParameter("AREA_ID_NAME")));
				}
				//fanglm 2010-12-16 过滤已存在地址点 用户管理--经销商
				if(ObjUtil.isNotNull(req.getParameter("AREA_IDS"))){
					sf.append(" and id not in(");
					sf.append(req.getParameter("AREA_IDS"));
					sf.append(")");
				}
				
				if((ObjUtil.isNotNull(req.getParameter("LOAD_FLAG")) && "true".equals(req.getParameter("LOAD_FLAG"))) 
						|| (ObjUtil.isNotNull(req.getParameter("RECV_FLAG")) && "true".equals(req.getParameter("RECV_FLAG")))
						|| (ObjUtil.isNotNull(req.getParameter("TRANSFER_FLAG")) && "true".equals(req.getParameter("TRANSFER_FLAG")))){
					sf.append(" and ( 1 <> 1 ");
					sf.append(util.addFlagORSQL("LOAD_FLAG",req.getParameter("LOAD_FLAG")));
					sf.append(util.addFlagORSQL("RECV_FLAG", req.getParameter("RECV_FLAG")));
					sf.append(util.addFlagORSQL("TRANSFER_FLAG", req.getParameter("TRANSFER_FLAG")));
					sf.append(")");
				}
				sf.append(util.addFlagSQL("LOAD_FLAG",req.getParameter("AND_LOAD_FLAG")));
				sf.append(util.addFlagSQL("RECV_FLAG",req.getParameter("AND_RECV_FLAG")));
				sf.append(util.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(util.addEqualSQL("AREA_ID", req.getParameter("AREA_ID")));
				sf.append(util.addALikeSQL("ADDR_CODE", req.getParameter("ADDR_CODE")));
				sf.append(util.addALikeSQL("ADDR_NAME", req.getParameter("ADDR_NAME")));
				if(ObjUtil.isNotNull(req.getParameter("ADDR_TYP"))){
					String[] addrTyps=req.getParameter("ADDR_TYP").split(",");
					if(addrTyps == null || addrTyps.length < 2){
						sf.append(util.addEqualSQL("ADDR_TYP", req.getParameter("ADDR_TYP")));
					}else{
						sf.append(" and ADDR_TYP IN (");
						for (int i = 0; i < addrTyps.length; i++) {
							if(i > 0){
								sf.append(",");
							}
							sf.append("'");
							sf.append(util.decode(addrTyps[i]));
							sf.append("'");
						}
						sf.append(") ");
					}
				}
				sf.append(util.addEqualSQL("AREA_ID2", req.getParameter("AREA_ID2")));
				
				//顺丰网点管理，添加冷运网点时，对冷运网点做执行机构过滤（执行机构为空的待商量）
				if(ObjUtil.isNotNull(req.getParameter("DEF_ORG_ID"))){
					sf.append(" and EXEC_ORG_ID");
	            	sf.append(" IN ");
	            	sf.append("    (SELECT ID ");
	            	sf.append("     From BAS_ORG ");
					sf.append("     Where id ='");
					sf.append(util.decode(req.getParameter("DEF_ORG_ID")));
					sf.append("'");
					sf.append("or ORG_INDEX Like '%,"); //
					sf.append(util.decode(req.getParameter("DEF_ORG_ID")));
					sf.append(",%' ) ");
				}
				
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and EXEC_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append(",%' ) ");
					}
					else {
						sf.append(util.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				};
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));  
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("LOAD_FLAG", Hibernate.YES_NO);
				map.put("RECV_FLAG", Hibernate.YES_NO);
				map.put("TRANSFER_FLAG", Hibernate.YES_NO);
				map.put("SELF_PKUP_FLAG", Hibernate.YES_NO);
				map.put("SELF_DLVR_FLAG", Hibernate.YES_NO);
				map.put("MIX_FLAG", Hibernate.YES_NO);
				map.put("RQAPPT_FLAG", Hibernate.YES_NO);
				map.put("DIRECT_FLAG", Hibernate.YES_NO);
				map.put("DEF_RDC", Hibernate.YES_NO);
				sf.append(" order by addtime desc");
				//System.out.println(sf.toString());
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);;
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("BAS_ADDRESSsf")){
				//地址点管理 fanglm
				String keys = util.getColName("V_ADDRESS");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_ADDRESS");
				sf.append(" where 1=1 ");
				//sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))) {
					sf.append(" and upper(FULL_INDEX) like upper('%"+req.getParameter("FULL_INDEX")+"%')");
				}
				if(ObjUtil.isNotNull(req.getParameter("ISLIST")) && "Y".equals(req.getParameter("ISLIST")))  {
				//if(ObjUtil.isNotNull( req.getParameter("CUSTOMER_ID"))){
					sf.append(" and (CUSTOMER_ID = '");
					sf.append(req.getParameter("CUSTOMER_ID"));
					sf.append("' or CUSTOMER_ID IS NULL) ");
				//}
				}
				else {
					sf.append(" and (CUSTOMER_ID = '");
					sf.append(req.getParameter("CUSTOMER_ID"));
					sf.append("' or CUSTOMER_NAME IS NULL) ");
				}
				
				sf.append(util.addEqualSQL("WHSE_ID", req.getParameter("WHSE_ID")));
				if(ObjUtil.isNotNull(req.getParameter("BIZ_CODE"))){
					sf.append(" and ADDR_CODE IN(select distinct tms_addr_code from trans_sss_addr t where unit_code = '");
					sf.append(req.getParameter("BIZ_CODE"));
					sf.append("' or sf_addr_code = '");
					sf.append(req.getParameter("BIZ_CODE"));
					sf.append("')");
				}
				if(ObjUtil.isNotNull(req.getParameter("NULL_AREA_FLAG")) && req.getParameter("NULL_AREA_FLAG").toString().equals("true")){
					sf.append(" and area_id is null");
				}else{
					sf.append(util.addLikeSQL("AREA_NAME2", req.getParameter("AREA_ID_NAME")));
				}
				//fanglm 2010-12-16 过滤已存在地址点 用户管理--经销商
				if(ObjUtil.isNotNull(req.getParameter("AREA_IDS"))){
					sf.append(" and id not in(");
					sf.append(req.getParameter("AREA_IDS"));
					sf.append(")");
				}
				
				if((ObjUtil.isNotNull(req.getParameter("LOAD_FLAG")) && "true".equals(req.getParameter("LOAD_FLAG"))) 
						|| (ObjUtil.isNotNull(req.getParameter("RECV_FLAG")) && "true".equals(req.getParameter("RECV_FLAG")))
						|| (ObjUtil.isNotNull(req.getParameter("TRANSFER_FLAG")) && "true".equals(req.getParameter("TRANSFER_FLAG")))){
					sf.append(" and ( 1 <> 1 ");
					sf.append(util.addFlagORSQL("LOAD_FLAG",req.getParameter("LOAD_FLAG")));
					sf.append(util.addFlagORSQL("RECV_FLAG", req.getParameter("RECV_FLAG")));
					sf.append(util.addFlagORSQL("TRANSFER_FLAG", req.getParameter("TRANSFER_FLAG")));
					sf.append(")");
				}
				sf.append(util.addFlagSQL("LOAD_FLAG",req.getParameter("AND_LOAD_FLAG")));
				sf.append(util.addFlagSQL("RECV_FLAG",req.getParameter("AND_RECV_FLAG")));
				sf.append(util.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(util.addEqualSQL("AREA_ID", req.getParameter("AREA_ID")));
				sf.append(util.addALikeSQL("ADDR_CODE", req.getParameter("ADDR_CODE")));
				sf.append(util.addALikeSQL("ADDR_NAME", req.getParameter("ADDR_NAME")));
				if(ObjUtil.isNotNull(req.getParameter("ADDR_TYP"))){
					String[] addrTyps=req.getParameter("ADDR_TYP").split(",");
					if(addrTyps == null || addrTyps.length < 2){
						sf.append(util.addEqualSQL("ADDR_TYP", req.getParameter("ADDR_TYP")));
					}else{
						sf.append(" and ADDR_TYP IN (");
						for (int i = 0; i < addrTyps.length; i++) {
							if(i > 0){
								sf.append(",");
							}
							sf.append("'");
							sf.append(util.decode(addrTyps[i]));
							sf.append("'");
						}
						sf.append(") ");
					}
				}
				sf.append(util.addEqualSQL("AREA_ID2", req.getParameter("AREA_ID2")));
				
				//顺丰网点管理，添加冷运网点时，对冷运网点做执行机构过滤（执行机构为空的待商量）
				if(ObjUtil.isNotNull(req.getParameter("DEF_ORG_ID"))){
					sf.append(" and EXEC_ORG_ID");
	            	sf.append(" IN ");
	            	sf.append("    (SELECT ID ");
	            	sf.append("     From BAS_ORG ");
					sf.append("     Where id ='");
					sf.append(util.decode(req.getParameter("DEF_ORG_ID")));
					sf.append("'");
					sf.append("or ORG_INDEX Like '%,"); //
					sf.append(util.decode(req.getParameter("DEF_ORG_ID")));
					sf.append(",%' ) ");
				}
				
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and EXEC_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append(",%' ) ");
					}
					else {
						sf.append(util.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				};
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));  
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("LOAD_FLAG", Hibernate.YES_NO);
				map.put("RECV_FLAG", Hibernate.YES_NO);
				map.put("TRANSFER_FLAG", Hibernate.YES_NO);
				map.put("SELF_PKUP_FLAG", Hibernate.YES_NO);
				map.put("SELF_DLVR_FLAG", Hibernate.YES_NO);
				map.put("MIX_FLAG", Hibernate.YES_NO);
				map.put("RQAPPT_FLAG", Hibernate.YES_NO);
				map.put("DIRECT_FLAG", Hibernate.YES_NO);
				map.put("DEF_RDC", Hibernate.YES_NO);
				sf.append(" order by addtime desc");
				//System.out.println(sf.toString());
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);;
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("BAS_CUSTOMER")||dsid.equals("BAS_CUSTOMER1")||dsid.equals("BAS_CUSTOMER2")){
				//客户管理 fanglm
				String keys = util.getColName("V_CUSTOMER");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_CUSTOMER t");
				sf.append(" where 1=1 ");
				sf.append(util.addALikeSQL("CUSTOMER_CODE", req.getParameter("CUSTOMER_CODE")));
				sf.append(util.addALikeSQL("CUSTOMER_CNAME", req.getParameter("CUSTOMER_CNAME")));
				//sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				if(ObjUtil.isNotNull(req.getParameter("ID"))){
					sf.append(util.addEqualSQL("ID", req.getParameter("ID")));
				}
//				if(ObjUtil.isNotNull(req.getParameter("PARENT_CUSTOMER_ID"))){
//					sf.append(util.addEqualSQL("PARENT_CUSTOMER_ID", req.getParameter("PARENT_CUSTOMER_ID")));
//				}
//				sf.append(util.addEqualSQL("MAINT_ORG_ID_NAME", req.getParameter("MAINT_ORG_ID_NAME")));
				sf.append(util.addFlagSQL("TRANSPORT_FLAG", req.getParameter("TRANSPORT_FLAG")));
				sf.append(util.addFlagSQL("WAREHOUSE_FLAG", req.getParameter("WAREHOUSE_FLAG")));
				sf.append(util.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(util.addFlagSQL("PAYER_FLAG", req.getParameter("PAYER_FLAG")));
				if(req.getParameter("CUSTOMER_FLAG").equals("true") || req.getParameter("CUSTOMER_FLAG").equals("Y")){
					sf.append(util.addFlagSQL("CUSTOMER_FLAG", req.getParameter("CUSTOMER_FLAG")));
				}else{
					sf.append(" and CUSTOMER_FLAG <> 'Y'");
				}
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //wangjun 2010-6-30
						sf.append(" and (EXEC_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append(",%' ) ");
						sf.append(" or MAINT_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append(",%' )) ");
					}
					else {
						sf.append(util.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				};
//				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
//					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //wangjun 2010-6-30
//						sf.append(" and EXEC_ORG_ID");
//		            	sf.append(" IN ");
//		            	sf.append("    (SELECT ID ");
//		            	sf.append("     From BAS_ORG ");
//						sf.append("     Where id ='");
//						sf.append(req.getParameter("EXEC_ORG_ID"));
//						sf.append("'");
//						sf.append("or ORG_INDEX Like '%,"); //
//						sf.append(req.getParameter("EXEC_ORG_ID"));
//						sf.append(",%' ) ");
//					}
//					else {
//						sf.append(util.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
//					}
//				};
				
//				 query = curSession.createSQLQuery(sf.toString())
//				 .addScalar("ID").addScalar("TRANSPORT_FLAG",Hibernate.YES_NO).addScalar("WAREHOUSE_FLAG",Hibernate.YES_NO)
//				 .addScalar("ENABLE_FLAG",Hibernate.YES_NO)
//				 .addScalar("PAYER_FLAG",Hibernate.YES_NO).addScalar("CONTACTER_FLAG",Hibernate.YES_NO)
//				 .addScalar("CUSTOMER_FLAG",Hibernate.YES_NO)
//				 .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("TRANSPORT_FLAG", Hibernate.YES_NO);
				map.put("WAREHOUSE_FLAG", Hibernate.YES_NO);
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("CUSTOMER_FLAG", Hibernate.YES_NO);
				map.put("PAYER_FLAG", Hibernate.YES_NO);
				map.put("CONTACTER_FLAG", Hibernate.YES_NO);
				map.put("INVOICE_FLAG", Hibernate.YES_NO);
				map.put("MATCHROUTE_FLAG", Hibernate.YES_NO);
				map.put("UNIQ_CONO_FLAG", Hibernate.YES_NO);
				map.put("UNIQ_ADDR_FLAG", Hibernate.YES_NO);
				map.put("SKU_EDIT_FLAG", Hibernate.YES_NO);
				map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
				map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
				map.put("COD_FLAG", Hibernate.YES_NO);
				map.put("POD_FLAG", Hibernate.YES_NO);
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("ADDR_EDIT_FLAG", Hibernate.YES_NO);
				
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);;
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("BAS_CUSTOMER_ORG")){
				//客户管理--执行机构分配 fanglm
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(util.getColName("BAS_CUSTOMER_ORG"));
				sf.append(" from BAS_CUSTOMER_ORG");
				sf.append(" where 1=1 ");
				
				sf.append(util.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				
				query = util.getQuery(sf.toString(),null,null).addScalar("ID").addScalar("CUSTOMER_ID")
							.addScalar("ORG_ID").addScalar("ORG_NAME").addScalar("DEFAULT_FLAG",Hibernate.YES_NO)
							.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BAS_CUSTOMER_ORD_TYP")){
				//客户管理--订单类型 fanglm
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(util.getColName("BAS_CUSTOMER_ORD_TYP"));
				sf.append(" from BAS_CUSTOMER_ORD_TYP");
				sf.append(" where 1=1 ");
				
				sf.append(util.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				
				query = util.getQuery(sf.toString(),null,null).addScalar("ID").addScalar("CUSTOMER_ID")
							.addScalar("ORD_TYP").addScalar("ORD_NAME").addScalar("DEFAULT_FLAG",Hibernate.YES_NO)
							.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BAS_CUSTOMER_TRANS_SRVC")){
				//客户管理--运输服务 fanglm
				String cust_id = util.decode(req.getParameter("CUSTOMER_ID"));
				sf = new StringBuffer();
				sf.append("select ID,TRANS_SRVC_ID,CUSTOMER_ID,SERVICE_NAME,DEFAULT_FLAG,'Y' as USE_FLAG");
				sf.append(" from V_CUST_TRANS_SRVC");
				sf.append(" where CUSTOMER_ID ='");
				sf.append(cust_id);
				sf.append("' union all ");
				sf.append("select '' as ID,ID AS TRANS_SRVC_ID,'' AS CUSTOMER_ID,SRVC_NAME AS SERVICE_NAME,'N' as DEFAULT_FLAG,'N' as USE_FLAG");
				sf.append(" from bas_trans_service WHERE id not in(");
				sf.append("select trans_srvc_id from bas_customer_trans_srvc where CUSTOMER_ID='");
				sf.append(cust_id);
				sf.append("')");
				
				query = util.getQuery(sf.toString(),null,null).addScalar("ID").addScalar("CUSTOMER_ID")
							.addScalar("TRANS_SRVC_ID").addScalar("SERVICE_NAME").addScalar("DEFAULT_FLAG",Hibernate.YES_NO)
							.addScalar("USE_FLAG",Hibernate.YES_NO)
							.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//				HashMap<String, Type> map = new HashMap<String, Type>();
//				map.put("DEFAULT_FLAG", Hibernate.YES_NO);
//				query = util.getQuery(sf.toString(), keys, map);
			}
			else if(dsid.equals("BAS_SUPPLIER")){
				//供应商管理  -- lijun
				String keys = util.getColName("V_SUPPLIER");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(util.getColName("V_SUPPLIER"));
				sf.append(" from V_SUPPLIER t where 1=1 ");
				//sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))) {
					sf.append(" and upper(FULL_INDEX) like upper('%"+req.getParameter("FULL_INDEX")+"%')");
				}
				sf.append(util.addEqualSQL("SUPLR_TYP", req.getParameter("SUPLR_TYP")));
//				sf.append(util.addEqualSQL("MAINT_ORG_ID", req.getParameter("MAINT_ORG_ID")));
				sf.append(util.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(util.addFlagSQL("INTL_FLAG_FLAG", req.getParameter("INTL_FLAG_FLAG")));
//				sf.append(util.addFlagSQL("TRANSPORT_FLAG", req.getParameter("TRANSPORT_FLAG")));
//				sf.append(util.addFlagSQL("WAREHOUSE_FLAG", req.getParameter("WAREHOUSE_FLAG")));
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_CODE"))){
					sf.append(util.addALikeSQL("SUPLR_CODE", req.getParameter("SUPLR_CODE")));					
				} 
				if(ObjUtil.isNotNull(req.getParameter("BLACKLIST_FLAG"))&&req.getParameter("BLACKLIST_FLAG").equals("true")){
					sf.append(util.addEqualSQL("BLACKLIST_FLAG", "Y"));					
				} 
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_CNAME"))){
					sf.append(util.addALikeSQL("SUPLR_CNAME", req.getParameter("SUPLR_CNAME")));					
				} 

				if((ObjUtil.isNotNull(req.getParameter("OR_TRANSPORT_FLAG")) && "true".equals(req.getParameter("OR_WAREHOUSE_FLAG")))){
					sf.append(" and ( 1 <> 1 ");
					sf.append(util.addFlagORSQL("TRANSPORT_FLAG",req.getParameter("OR_TRANSPORT_FLAG")));
					sf.append(util.addFlagORSQL("WAREHOUSE_FLAG", req.getParameter("OR_WAREHOUSE_FLAG")));
					sf.append(")");
				}
				
				if(ObjUtil.isNotNull(req.getParameter("MAINT_ORG_ID"))) { //wangjun 2010-6-30
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and MAINT_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(util.decode(req.getParameter("MAINT_ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("MAINT_ORG_ID")));
						sf.append(",%' ) ");
					}
					else {
						sf.append(util.addEqualSQL("MAINT_ORG_ID", req.getParameter("MAINT_ORG_ID")));
					}
				};
				
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));//自定义查询
//				query = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("TRANSPORT_FLAG", Hibernate.YES_NO);
				map.put("WAREHOUSE_FLAG", Hibernate.YES_NO);
				map.put("INTL_FLAG", Hibernate.YES_NO);
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("BLACKLIST_FLAG", Hibernate.YES_NO);
				map.put("VEHICLE_FOR_FLAG", Hibernate.YES_NO);
				map.put("INVOICE_FLAG", Hibernate.YES_NO);
				map.put("CONTRACT_FLAG", Hibernate.YES_NO);
				map.put("INS_FLAG", Hibernate.YES_NO);
				map.put("BLACKLIST_FLAG", Hibernate.YES_NO);
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
				
			}
			else if(dsid.equals("BAS_SUPPLIER_ORG")){
				//供应商管理--执行机构分配 fanglm
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(util.getColName("BAS_SUPPLIER_ORG"));
				sf.append(" from BAS_SUPPLIER_ORG");
				sf.append(" where 1=1 ");
				
				sf.append(util.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(util.addFlagSQL("DEFAULT_FLAG", req.getParameter("DEFAULT_FLAG")));
				
				query = util.getQuery(sf.toString(),null,null).addScalar("ID").addScalar("SUPLR_ID")
							.addScalar("ORG_ID").addScalar("ORG_NAME").addScalar("DEFAULT_FLAG",Hibernate.YES_NO)
							.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("BAS_SUPLR_TRANS_SRVC")){
				//供应商管理--运输服务 fanglm
				String suplr_id = util.decode(req.getParameter("SUPLR_ID"));
				sf = new StringBuffer();
				sf.append("select ID,TRANS_SRVC_ID,SUPLR_ID,SERVICE_NAME,DEFAULT_FLAG,'Y' as USE_FLAG");
				sf.append(" from V_SUPLR_TRANS_SRVC");
				sf.append(" where SUPLR_ID ='");
				sf.append(suplr_id);
				sf.append("' union all ");
				sf.append("select '' as ID,ID AS TRANS_SRVC_ID,'' AS SUPLR_ID,SRVC_NAME AS SERVICE_NAME,'N' as DEFAULT_FLAG,'N' as USE_FLAG");
				sf.append(" from bas_trans_service WHERE id not in(");
				sf.append("select trans_srvc_id from bas_suplr_trans_srvc where suplr_id='");
				sf.append(suplr_id);
				sf.append("')");
				
				query = util.getQuery(sf.toString(),null,null).addScalar("ID").addScalar("SUPLR_ID")
							.addScalar("TRANS_SRVC_ID").addScalar("SERVICE_NAME").addScalar("DEFAULT_FLAG",Hibernate.YES_NO)
							.addScalar("USE_FLAG",Hibernate.YES_NO)
							.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_VERIFY_REMIND")){
				//车型管理：lijun
				String keys = util.getColName("V_VERIFY_REMIND");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_VERIFY_REMIND where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))) { 
				sf.append(util.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				}

				if(ObjUtil.isNotNull(req.getParameter("WITHIN_FEW_DAYS"))) { 
				sf.append(util.addMathSQL("REMAIN_DAYS", req.getParameter("WITHIN_FEW_DAYS"),"<="));
				}
				if(ObjUtil.isNotNull(req.getParameter("VEHICLE_ATTR"))) { 
				sf.append(util.addEqualSQL("VEHICLE_ATTR",req.getParameter("VEHICLE_ATTR")));
				}
				//sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				query = util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
				
			}else if(dsid.equals("V_INS_REMIND")){
				//车型管理：lijun
				String keys = util.getColName("V_INS_REMIND");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_INS_REMIND where 1=1 ");
				sf.append(util.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
//				sf.append(util.addEqualSQL("PLATE_NO",req.getParameter("PLATE_NO")));
				sf.append(util.addEqualSQL("VEHICLE_ATTR",req.getParameter("VEHICLE_ATTR")));
				sf.append(util.addMathSQL("REMAIN_DAYS", req.getParameter("WITHIN_FEW_DAYS"),"<="));
				
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				query = util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
				
			}
			else if(dsid.equals("V_BAS_VEHICLE")){
				//运力管理--lijun
				String keys = util.getColName("V_BAS_VEHICLE");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BAS_VEHICLE t WHERE  1=1 " );
//				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))) {
//					sf.append(" and upper(FULL_INDEX) like upper('%"+req.getParameter("FULL_INDEX")+"%')");
//				}
				//sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(util.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_ID"))) {
					sf.append(util.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				}
				sf.append(util.addEqualSQL("ORG_ID", req.getParameter("ORG_ID")));
				sf.append(util.addEqualSQL("VEHICLE_TYP_ID", req.getParameter("VEHICLE_TYP_ID")));
				sf.append(util.addEqualSQL("CURRENT_AREA", req.getParameter("CURRENT_AREA")));
				sf.append(util.addEqualSQL("VEHICLE_STAT", req.getParameter("VEHICLE_STAT")));
				sf.append(util.addEqualSQL("VEHICLE_ATTR", req.getParameter("VEHICLE_ATTR")));
				sf.append(util.addTimeSQL("PRE_UNLOAD_TIME", req.getParameter("PRE_UNLOAD_TIME_FROM"), ">=")); //wangjun 2011-4-2
				sf.append(util.addTimeSQL("PRE_UNLOAD_TIME", req.getParameter("PRE_UNLOAD_TIME_TO"), "<=")); 
				sf.append(util.addEqualSQL("AVAIL_ATTR", req.getParameter("AVAIL_ATTR")));
				sf.append(util.addMathSQL("TMP_ATTR", req.getParameter("TMP_ATTR"),">="));
				sf.append(util.addMathSQL("MAX_WEIGHT",req.getParameter("MAX_WEIGHT"),">="));
				sf.append(util.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(util.addFlagSQL("BLACKLIST_FLAG", req.getParameter("BLACKLIST_FLAG")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("  (SELECT ID ");
		            	sf.append("  From BAS_ORG ");
						sf.append("  Where id ='");
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append(",%' ) ");
					}
					else {
						sf.append(util.addEqualSQL("ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				}
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				
				HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("AVAIL_FLAG", Hibernate.YES_NO);
	            map.put("ENABLE_FLAG", Hibernate.YES_NO);
	            map.put("BLACKLIST_FLAG", Hibernate.YES_NO);
				query = util.getQuery(sf.toString(),keys,map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row = (Integer.parseInt(CUR_PAGE)-1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("V_ROUTE_HEAD")){
				//线路管理
				String keys = util.getColName("V_ROUTE_HEAD");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_ROUTE_HEAD where 1=1 ");
				//sf.append(util.addALikeSQL("SHORT_NAME||ROUTE_NAME||HINT_CODE", req.getParameter("FULL_INDEX")));
				if(ObjUtil.isNotNull(req.getParameter("FULL_IN"))) {
					sf.append(" and ID in (select route_id from v_route_detail where addr_code like '%");
					sf.append(req.getParameter("FULL_IN"));
					sf.append("%')");
				}
				if(ObjUtil.isNotNull(req.getParameter("ADDR_NAME"))) {
					sf.append(" and ID in (select route_id from v_route_detail where addr_id_name like '%");
					sf.append(req.getParameter("ADDR_NAME"));
					sf.append("%')");
				}
				sf.append(util.addEqualSQL("START_AREA_ID", req.getParameter("START_AREA_ID")));
				sf.append(util.addEqualSQL("END_AREA_ID", req.getParameter("END_AREA_ID")));
				sf.append(util.addEqualSQL("START_AREA_ID2", req.getParameter("START_AREA_ID2")));
				sf.append(util.addEqualSQL("END_AREA_ID2", req.getParameter("END_AREA_ID2")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) {
						sf.append(" and EXEC_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append(",%' ) ");
					}
					else {
						sf.append(util.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				}
				if(ObjUtil.isNotNull(req.getParameter("ADDR_ID"))){
					sf.append(" AND ID IN (SELECT ROUTE_ID FROM V_ROUTE_DETAIL");
					sf.append(" WHERE ADDR_ID = '");
					sf.append(util.decode(req.getParameter("ADDR_ID")));
					sf.append("') ");
				}
				sf.append(util.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
				sf.append(util.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(util.addFlagSQL("POINTS_FLAG", req.getParameter("POINTS_FLAG")));
				sf.append(util.addFlagSQL("BILL_LINE_FLAG", req.getParameter("BILL_LINE_FLAG")));
				sf.append(util.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("POINTS_FLAG", Hibernate.YES_NO);
				map.put("BILL_LINE_FLAG", Hibernate.YES_NO);
				
				query = util.getQuery(sf.toString(),keys,map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
				
			}
			else if(dsid.equals("V_ROUTE_DETAIL")){
				//线路管理
				String keys = util.getColName("V_ROUTE_DETAIL");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_ROUTE_DETAIL where 1=1 ");
				sf.append(util.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));
				sf.append(" order by show_seq asc");
				
				query = util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
				
			}
			else if(dsid.equals("ROUTE_HALFWAY")){
				//中途点信息
				sf = new StringBuffer();
				sf.append("select rownum as SHOW_SEQ,ADDR_ID,AREA_ID as ID,AREA_NAME,exec_org_id,addr_id_name as ADDR_NAME,ADDRESS");
				sf.append(" from V_ROUTE_DETAIL where area_id is not null ");
				sf.append(util.addEqualSQL("ROUTE_ID", req.getParameter("ROUTE_ID")));
				
				query = util.getQuery(sf.toString(),null,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
				
			}
			else if(dsid.equals("V_WAREHOUSE")){
				//仓库管理
				String keys = util.getColName("V_WAREHOUSE");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_WAREHOUSE where 1=1 ");
				sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(util.addALikeSQL("WHSE_CODE", req.getParameter("WHSE_CODE")));
//				sf.append(util.addEqualSQL("ORG_ID", req.getParameter("ORG_ID")));
				if(ObjUtil.isNotNull(req.getParameter("ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) {
						sf.append(" and ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(util.decode(req.getParameter("ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("ORG_ID")));
						sf.append(",%' ) ");
					}
					else {
						sf.append(util.addEqualSQL("ORG_ID", req.getParameter("ORG_ID")));
					}
				}
				sf.append(util.addEqualSQL("AREA_ID_NAME", req.getParameter("AREA_ID_NAME")));
				sf.append(util.addTimeSQL("START_TIME", req.getParameter("START_TIME"), ">="));
				sf.append(util.addTimeSQL("END_TIME", req.getParameter("END_TIME"), "<="));
				sf.append(util.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				
//				query = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
				
			}
			else if(dsid.equals("V_CHARGE_REGION")){
				//结算区域 lijun
				String keys = util.getColName("V_CHARGE_REGION");
				sf = new StringBuffer();
				sf.append(" select ");
				sf.append(util.getColName("V_CHARGE_REGION"));
				sf.append(" from V_CHARGE_REGION where 1=1 ");
				sf.append(util.addALikeSQL("FULL_INDEX",req.getParameter("FULL_INDEX")));
				sf.append(util.addFlagSQL("ENABLE_FLAG",req.getParameter("ENABLE_FLAG")));
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				sf.append(" order by SHOW_SEQ asc");
				
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				
//				query = curSession.createSQLQuery(sf.toString())
//				        .addScalar("ID",Hibernate.STRING).addScalar("CHARGE_REGION_NAME",Hibernate.STRING)
//				        .addScalar("CUSTOMER_ID",Hibernate.STRING)
//				        .addScalar("CHARGE_REGION_ENAME",Hibernate.STRING).addScalar("CUSTOMER_NAME",Hibernate.STRING)
//				        .addScalar("AREA_NAME",Hibernate.STRING).addScalar("HINT_CODE",Hibernate.STRING)
//				        .addScalar("SHOW_SEQ",Hibernate.INTEGER).addScalar("ENABLE_FLAG",Hibernate.YES_NO)
//				        .addScalar("UDF1",Hibernate.STRING).addScalar("UDF2",Hibernate.STRING).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				query=util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row = (Integer.parseInt(CUR_PAGE) - 1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("V_BAS_STAFF")){
				String keys = util.getColName("V_BAS_STAFF");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BAS_STAFF t where 1=1 ");
				sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
//				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				sf.append(util.addEqualSQL("STAFF_TYP", req.getParameter("STAFF_TYP")));
				sf.append(util.addEqualSQL("DEP_ID", req.getParameter("DEP_ID")));
				sf.append(util.addFlagSQL("BLACKLIST_FLAG", req.getParameter("BLACKLIST_FLAG")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) {
						sf.append(" and ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append(",%' ) ");
					}
					else {
						sf.append(util.addEqualSQL("ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				}
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("DIRECTION_FLAG", Hibernate.YES_NO);
				map.put("RGST_DRCT_FLAG", Hibernate.YES_NO);
				map.put("BLACKLIST_FLAG", Hibernate.YES_NO);
		
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row = (Integer.parseInt(CUR_PAGE) - 1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
				
			}
			else if(dsid.equals("V_BAS_STAFF1")){
				String keys = util.getColName("V_BAS_STAFF");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BAS_STAFF where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))){
					sf.append(" and upper(FULL_INDEX) like upper('%"+req.getParameter("FULL_INDEX")+"%')");
				}
				if(ObjUtil.isNotNull(req.getParameter("STAFF_CODE"))) {
					sf.append(util.addALikeSQL("STAFF_CODE", req.getParameter("STAFF_CODE")));
				}

				if(ObjUtil.isNotNull(req.getParameter("STAFF_NAME"))) {
					sf.append(util.addALikeSQL("STAFF_NAME", req.getParameter("STAFF_NAME")));
				}

				if(ObjUtil.isNotNull(req.getParameter("MOBILE"))) {
					sf.append(util.addALikeSQL("MOBILE", req.getParameter("MOBILE")));
				}
				
				if(ObjUtil.isNotNull(req.getParameter("ID_NO"))) {
					sf.append(util.addALikeSQL("ID_NO", req.getParameter("ID_NO")));
				}
				
				if(ObjUtil.isNotNull(req.getParameter("TEL"))) {
					sf.append(util.addALikeSQL("TEL", req.getParameter("TEL")));
				}
				
				if(ObjUtil.isNotNull(req.getParameter("DRVR_LIC_NUM"))) {
					sf.append(util.addALikeSQL("DRVR_LIC_NUM", req.getParameter("DRVR_LIC_NUM")));
				}
				
				if(ObjUtil.isNotNull(req.getParameter("BLACKLIST_FLAG"))&&(req.getParameter("BLACKLIST_FLAG").equals("true"))) {
					sf.append(util.addEqualSQL("BLACKLIST_FLAG", "Y"));
				}
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) {
						sf.append(" and ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append(",%' ) ");
					}
					else {
						sf.append(util.addEqualSQL("ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				}
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("DIRECTION_FLAG", Hibernate.YES_NO);
				map.put("RGST_DRCT_FLAG", Hibernate.YES_NO);
				map.put("BLACKLIST_FLAG", Hibernate.YES_NO);
				
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row = (Integer.parseInt(CUR_PAGE) - 1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
				
			}
			else if(dsid.equals("V_BAS_TRANS_SERVICE")){
				//运输服务 wangjun
				String keys = util.getColName("V_BAS_TRANS_SERVICE");
				sf=new StringBuffer();
				sf.append(" select ");
				sf.append(keys);
				sf.append(" from V_BAS_TRANS_SERVICE t where 1=1 ");
				sf.append(util.addALikeSQL("FULL_INDEX",req.getParameter("FULL_INDEX")));
				sf.append(util.addFlagSQL("ENABLE_FLAG",req.getParameter("ENABLE_FLAG")));
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				sf.append(" order by SHOW_SEQ asc");
				
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("DEFAULT_FLAG", Hibernate.YES_NO);
				
				query=util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
					
				}else {
					start_row=(Integer.parseInt(CUR_PAGE)-1)*page_record;
					query.setFirstResult(start_row);
				    query.setMaxResults(page_record);
				}
			}else if(dsid.equals("V_SUPLR_AREA")){
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(util.getColName("V_SUPLR_AREA"));
				sf.append(" from V_SUPLR_AREA where 1=1 ");
				sf.append(util.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(util.addALikeSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));
				sf.append(util.addALikeSQL("PROVICE_NAME", req.getParameter("PROVICE_NAME")));
				sf.append(util.addALikeSQL("TRANS_SRVC_ID_NAME", req.getParameter("TRANS_SRVC_ID_NAME")));
				query=util.getQuery(sf.toString(),null,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("VC_CUSTOMER")){
				String keys = util.getColName("VC_CUSTOMER");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from VC_CUSTOMER where 1=1");
				if(!"wpsadmin".equals(req.getParameter("LOGIN_USER"))){
					sf.append(" and id in(");
					sf.append("select customer_id from V_USER_CUSTOMER where user_id='");
					sf.append(req.getParameter("LOGIN_USER"));
					sf.append("')");
				}
				sf.append(" and enable_flag='Y' and customer_flag='Y'");
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))){
					sf.append(" and  upper(FULL_INDEX) like upper('%"+req.getParameter("FULL_INDEX")+"%')");
				}	
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
				map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
				map.put("POD_FLAG", Hibernate.YES_NO);
				map.put("ADDR_EDIT_FLAG", Hibernate.YES_NO);
				map.put("UNIQ_CONO_FLAG", Hibernate.YES_NO);
				map.put("SKU_EDIT_FLAG", Hibernate.YES_NO);
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
    		}else if(dsid.equals("V_TIME_TYPE")){
				/**
				 * 时间管理--主信息
				 */
				String keys = util.getColName("V_TIME_TYPE");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_TIME_TYPE where 1=1 ");
				sf.append(util.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				sf.append(util.addEqualSQL("TIME_TYPE", req.getParameter("TIME_TYPE")));
				sf.append(util.addEqualSQL("DOC_TYPE", req.getParameter("DOC_TYPE")));
				sf.append(util.addTimeSQL("FROM_DATE", req.getParameter("FROM_DATE"), ">="));
				sf.append(util.addTimeSQL("TO_DATE", req.getParameter("TO_DATE"), "<="));
//				sf.append(util.addEqualSQL("IN_PRIOR", req.getParameter("IN_PRIOR")));
				sf.append(util.addFlagSQL("ENABLE_FLAG",req.getParameter("ENABLE_FLAG")));
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				sf.append(" order by EXEC_SEQ asc");
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				query=util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row = (Integer.parseInt(CUR_PAGE) - 1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			
			}else if(dsid.equals("V_TIME_FORMULA")){
				/**
				 * 时间管理--表达式
				 */
				String keys = util.getColName("V_TIME_FORMULA");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(util.getColName("V_TIME_FORMULA"));
				sf.append(" from V_TIME_FORMULA where 1=1 ");
				sf.append(util.addEqualSQL("TYP_ID", req.getParameter("TYP_ID")));
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				sf.append(" order by SEQ asc");
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row = (Integer.parseInt(CUR_PAGE) - 1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}else if(dsid.equals("V_TIME_CONDITION")){
				/**
				 * 时间管理--过滤条件
				 */
				String keys = util.getColName("V_TIME_CONDITION");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_TIME_CONDITION where 1=1 ");
				sf.append(util.addEqualSQL("FORMULA_ID", req.getParameter("FORMULA_ID")));
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				sf.append(" order by addtime asc");
				query=util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row = (Integer.parseInt(CUR_PAGE) - 1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("BAS_WAREHOUSE_DOCK")){
				// wangjun
				String keys = util.getColName("BAS_WAREHOUSE_DOCK");
				sf=new StringBuffer();
				sf.append(" select ");
				sf.append(keys);
				sf.append(" from BAS_WAREHOUSE_DOCK where 1=1 ");
				sf.append(util.addEqualSQL("WHSE_ID", req.getParameter("WHSE_ID")));
				sf.append(" order by SHOW_SEQ asc");
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				query=util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
					
				}else {
					start_row=(Integer.parseInt(CUR_PAGE)-1)*page_record;
					query.setFirstResult(start_row);
				    query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("BAS_RANGE")) {
				//服务范围 yuanlei
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(util.getColName("BAS_RANGE"));
				sf.append(" from BAS_RANGE");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("RANGE_NAME"))) {
					sf.append(util.addEqualSQL("RANGE_NAME", req.getParameter("RANGE_NAME")));
				}
	            sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));//通过自定义方式查询	
	            query = util.getQuery(sf.toString(),null,null)
	            .addScalar("ID",Hibernate.STRING).addScalar("FROM_PROVINCE_ID",Hibernate.STRING).addScalar("FROM_PROVINCE_NAME",Hibernate.STRING)
	            .addScalar("FROM_AREA_ID",Hibernate.STRING).addScalar("FROM_AREA_NAME",Hibernate.STRING).addScalar("RANGE_NAME",Hibernate.STRING)
	            .addScalar("REQ_HOURS",Hibernate.STRING).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("TRANS_TRANSACTION_LOG")){
				//业务日志查询
				String keys = util.getColName("TRANS_TRANSACTION_LOG");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from TRANS_TRANSACTION_LOG where 1=1 ");
//				sf.append(util.addLikeSQL("DOC_NO||DOC_TYP||NOTES", req.getParameter("FULL_INDEX")));
				sf.append(util.addEqualSQL("DOC_TYP", req.getParameter("DOC_TYP")));
				sf.append(util.addALikeSQL("DOC_NO", req.getParameter("DOC_NO")));
				sf.append(util.addEqualSQL("ACTION_TYP", req.getParameter("ACTION_TYP")));
				sf.append(util.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_FROM"), ">="));
				sf.append(util.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(" order by addtime desc");
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				
				query = util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
				
			}
			else if(dsid.equals("SFLSCM_LOG")){
				//业务日志查询
				String keys = util.getColName("SFLSCM_LOG");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from SFLSCM_LOG where 1=1 ");
				sf.append(util.addEqualSQL("LGINTERFACE", req.getParameter("INTERFACE_NAME")));
				sf.append(util.addEqualSQL("LGMODULE", req.getParameter("INTERFACE_TYPE")));
				sf.append(util.addALikeSQL("LGATT1", req.getParameter("LGATT1")));
//				sf.append(util.addLikeSQL("DOC_NO||DOC_TYP||NOTES", req.getParameter("FULL_INDEX")));
				if(ObjUtil.isNotNull(req.getParameter("LGDATETIME_FROM"))){
					sf.append(" and to_date(to_char(INSERT_DATE, 'yyyy/mm/dd'), 'yyyy/mm/dd') >= TO_DATE('");
					sf.append(req.getParameter("LGDATETIME_FROM"));
					sf.append("', 'yyyy/mm/dd')");
				}
//				sf.append(util.addTimeSQL("INSERT_DATE", req.getParameter("LGDATETIME_FROM"), ">="));
				if(ObjUtil.isNotNull(req.getParameter("LGDATETIME_TO"))){
					sf.append(" and to_date(to_char(INSERT_DATE,'yyyy/mm/dd'), 'yyyy/mm/dd') <= TO_DATE('");
					sf.append(req.getParameter("LGDATETIME_TO"));
					sf.append("', 'yyyy/mm/dd')");
				}
//				sf.append(util.addTimeSQL("INSERT_DATE", req.getParameter("LGDATETIME_TO"), "<="));
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				sf.append(" order by insert_date asc");
				
				query = util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
				
			}
			else if(dsid.equals("V_ADDRESS_DIST")){
				//地址点管理 fanglm
				String keys = util.getColName("BAS_ADDRESS_DIST");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BAS_ADDRESS_DIST");
				sf.append(" where 1=1 ");
				sf.append(util.addEqualSQL("ADDR_ID1", req.getParameter("ADDR_ID1")));
//				sf.append(util.addALikeSQL("ADDR_NAME1", req.getParameter("ADDR_NAME1")));
				sf.append(util.addEqualSQL("ADDR_ID2", req.getParameter("ADDR_ID2")));
//				sf.append(util.addALikeSQL("ADDR_NAME2", req.getParameter("ADDR_NAME2")));
				sf.append(util.addEqualSQL("AREA_ID21", req.getParameter("AREA_ID1")));
//				sf.append(util.addALikeSQL("AREA_NAME1", req.getParameter("AREA_NAME1")));
				sf.append(util.addEqualSQL("AREA_ID22", req.getParameter("AREA_ID2")));
//				sf.append(util.addALikeSQL("AREA_NAME2", req.getParameter("AREA_NAME2")));
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ADDR_FLAG", Hibernate.YES_NO);
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);;
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			if(dsid.equals("SSS_ADDR")){
				//顺丰地址点查询
				String keys="ID,ADDR_CODE,ADDR_NAME,ADDRESS,AREA_ID,AREA_NAME,AREA_ID2,AREA_NAME2,AREA_ID3,AREA_NAME3," +
						"ADDR_TYPE,NAME_C,LEVEL1_CODE,LEVEL2_CODE,LEVEL3_CODE,ORG_CNAME,EXEC_ORG_ID_NAME,ENABLE_FLAG,HINT_CODE,EXEC_ORG_ID,ADDWHO,EDITWHO";
				sf=new StringBuffer();
				sf.append("select ");
				sf.append(" sa.ID,sa.ADDR_CODE,sa.ADDWHO,sa.EDITWHO,sa.EXEC_ORG_ID,sa.ADDR_NAME,sa.ADDRESS,sa.AREA_ID,sa.AREA_NAME,sa.HINT_CODE,sa.AREA_ID2,sa.AREA_NAME2,sa.AREA_ID3,sa.AREA_NAME3");
				sf.append(",sa.ADDR_TYPE,bc.NAME_C as NAME_C");
				sf.append(",sa.LEVEL1_CODE,sa.LEVEL2_CODE,sa.LEVEL3_CODE");
				sf.append(",bo.ORG_CNAME as ORG_CNAME, bo.ORG_CNAME as EXEC_ORG_ID_NAME");
				sf.append(",sa.ENABLE_FLAG");
				sf.append(" from SSS_ADDR sa,BAS_ORG bo,BAS_CODES bc ");
				sf.append(" where 1=1 ");
				sf.append(" and sa.EXEC_ORG_ID=bo.ID and sa.ADDR_TYPE=bc.ID(+) ");
				sf.append(" and bc.PROP_CODE(+)='SFADDR_TYP' ");
				sf.append(util.addALikeSQL("ADDR_CODE", req.getParameter("ADDR_CODE")));
				sf.append(util.addALikeSQL("ADDR_NAME", req.getParameter("ADDR_NAME")));
				sf.append(util.addEqualSQL("AREA_ID2", req.getParameter("AREA_ID2")));
				sf.append(util.addALikeSQL("LEVEL1_CODE", req.getParameter("LEVEL1_CODE")));
				sf.append(util.addALikeSQL("LEVEL2_CODE", req.getParameter("LEVEL2_CODE")));
				sf.append(util.addALikeSQL("LEVEL3_CODE", req.getParameter("LEVEL3_CODE")));
//				sf.append(util.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),null));
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
						sf.append(util.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				}
				if(req.getParameter("ENABLE_FLAG").equals("true")){
					sf.append(" and sa.ENABLE_FLAG='Y'");
				}
				if(req.getParameter("ENABLE_FLAG").equals("false")){
					sf.append(" and sa.ENABLE_FLAG='N'");
				}
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				query=util.getQuery(sf.toString(),keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row=(Integer.parseInt(CUR_PAGE)-1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			
			else if(dsid.equals("TRANS_SSS_ADDR")){
				//顺丰地址点查询
				String vFlag=req.getParameter("VDIRECT");
				String keys=util.getColName("TRANS_SSS_ADDR");
				if(vFlag.equals("SF")){
					keys="TMS_ADDR_CODE,TMS_ADDR_NAME,UNIT_CODE";
				}else if(vFlag.equals("TMS")){
					keys="SF_ADDR_CODE,SF_ADDR_NAME,SF_AREA_NAME";
				}
				sf=new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from TRANS_SSS_ADDR t where 1=1 ");
				if(vFlag.equals("SF")){
					sf.append(util.addEqualSQL("SF_ADDR_CODE", req.getParameter("ADDR_CODE")));
				}else if(vFlag.equals("TMS")){
					sf.append(util.addEqualSQL("TMS_ADDR_CODE", req.getParameter("TMS_ADDR_CODE")));
				}
				query=util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row=(Integer.parseInt(CUR_PAGE)-1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("RDC_ADDRESS")){
				util.setTableAlias("t1");
				String keys = "";
				sf = new StringBuffer();
				if (Boolean.valueOf(req.getParameter("ISRDC"))) {
					keys = "TMS_ADDR_ID,TMS_ADDR_CODE,TMS_ADDR_NAME,ADDR_TYP,ADDR_TYP_NAME";
					sf.append("select ");
					sf.append("t1.TMS_ADDR_CODE as TMS_ADDR_ID,t1.TMS_ADDR_NAME,t2.ADDR_CODE as TMS_ADDR_CODE,t2.ADDR_TYP,t3.NAME_C as ADDR_TYP_NAME");
					sf.append(" from RDC_ADDRESS t1,BAS_ADDRESS t2,BAS_CODES t3");
					sf.append(" where 1 = 1 and t1.TMS_ADDR_CODE = t2.ID(+) and t2.ADDR_TYP = t3.ID(+) and t3.PROP_CODE = 'ADDR_TYP'");
					sf.append(util.addEqualSQL("RDC_CODE", req.getParameter("RDC_ID")));
				}else {
					keys = "TMS_ADDR_ID,TMS_ADDR_CODE,TMS_ADDR_NAME,ADDR_TYP,ADDR_TYP_NAME";
					sf.append("select ");
					sf.append("t1.RDC_CODE as TMS_ADDR_ID,t1.RDC_NAME as TMS_ADDR_NAME,t2.ADDR_CODE as TMS_ADDR_CODE,t2.ADDR_TYP,t3.NAME_C as ADDR_TYP_NAME");
					sf.append(" from RDC_ADDRESS t1,BAS_ADDRESS t2,BAS_CODES t3");
					sf.append(" where 1 = 1 and t1.RDC_CODE = t2.ID(+) and t2.ADDR_TYP = t3.ID(+) and t3.PROP_CODE = 'ADDR_TYP'");
					sf.append(util.addEqualSQL("TMS_ADDR_CODE", req.getParameter("TMS_ADDR_ID")));
				}
				
//				keys = util.getColName("RDC_ADDRESS",util.getTableAlias());
//				keys = keys.replace(util.getTableAlias()+".RDC_CODE,", "").replace(util.getTableAlias()+".TMS_ADDR_CODE,", "");
//				sf = new StringBuffer();
//				sf.append("select ");
//				sf.append(keys+",t.RDC_CODE AS RDC_ID,t2.ADDR_CODE AS RDC_CODE,t.TMS_ADDR_CODE AS TMS_ADDR_ID,t3.ADDR_CODE AS TMS_ADDR_CODE");
//				sf.append(" from RDC_ADDRESS t,BAS_ADDRESS t2,BAS_ADDRESS t3");
//				sf.append(" where 1=1 and t.RDC_CODE = t2.id(+) and t.TMS_ADDR_CODE = t3.id(+)");
//				sf.append(util.addEqualSQL("RDC_CODE", req.getParameter("RDC_ID")));
				
//				keys = keys.replace(util.getTableAlias()+".", "");
				query = util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row=(Integer.parseInt(CUR_PAGE)-1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("BAS_ADDRESS5")){
				String keys = util.getColName("V_ADDRESS");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_ADDRESS");
				sf.append(" where 1=1");
				sf.append(" and addr_typ <> '"+StaticRef.RDC+"'");
				sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(util.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
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
				
				HashMap<String,Type> map = new HashMap<String,Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row=(Integer.parseInt(CUR_PAGE)-1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("BAS_ADDRESS_AREA")){
				String keys = util.getColName("BAS_ADDRESS_AREA");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BAS_ADDRESS_AREA");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("ADDR_ID"))){
					sf.append(util.addEqualSQL("ADDR_ID", req.getParameter("ADDR_ID")));
				}else{
					sf.append(" and 1<>1 ");
				}
				
				query = util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}

			else if(dsid.equals("BAS_TEMPEQ")){
				String keys = util.getColName("BAS_TEMPEQ");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BAS_TEMPEQ t");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("EQUIP_NO"))) {
					sf.append(util.addEqualSQL("EQUIP_NO", req.getParameter("EQUIP_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(util.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))) {
					sf.append(util.addEqualSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(util.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
				if(ObjUtil.isNotNull(req.getParameter("USE_COMPANY"))) {
					sf.append(util.addEqualSQL("USE_COMPANY", req.getParameter("USE_COMPANY")));
				}
				if(ObjUtil.isNotNull(req.getParameter("EQUIP_MANAGER"))) {
					sf.append(util.addEqualSQL("EQUIP_MANAGER", req.getParameter("EQUIP_MANAGER")));
				}
				HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("ACTIVE_FLAG", Hibernate.YES_NO);
	            query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	           
	            if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row=(Integer.parseInt(CUR_PAGE)-1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			
			else if(dsid.equals("BAS_TEMPEQ1")){
				String keys = util.getColName("BAS_TEMPEQ");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BAS_TEMPEQ");
				sf.append(" where 1=1 ");
				sf.append(util.addALikeSQL("EQUIP_NO", req.getParameter("EQUIP_NO")));
				sf.append(util.addEqualSQL("STATUS", req.getParameter("STATUS")));
				query = util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("BAS_TEMP_RECORDS")) {
				//使用记录
				String keys = util.getColName("BAS_TEMP_RECORDS");
				sf = new StringBuffer();
				sf.append("select ID,LOAD_NO,PLATE_NO,SUPLR_NAME,DRIVER,MOBILE,LOAD_ADDRESS,UNLOAD_ADDRESS,ADDWHO,EDITTIME,EDITWHO,EQUIP_NO,NOTES");
				sf.append(",to_char(ADDTIME,'yyyy-mm-dd hh24:mi') as ADDTIME ");
				sf.append(" from BAS_TEMP_RECORDS");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("EQUIP_NO"))) {
					sf.append(util.addEqualSQL("EQUIP_NO", req.getParameter("EQUIP_NO")));
				}
				sf.append(" order by ADDTIME");
	            query = util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_BAS_TEMP_RECLAIM")) {
				//回收记录
				String keys = util.getColName("V_BAS_TEMP_RECLAIM");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BAS_TEMP_RECLAIM");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("EQUIP_NO"))) {
					sf.append(util.addEqualSQL("EQUIP_NO", req.getParameter("EQUIP_NO")));
				}
				HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("ABNORMAL_FLAG", Hibernate.YES_NO);
	            query = util.getQuery(sf.toString(),keys,map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}	
			else if(dsid.equals("V_BAS_TEMP_RECLAIM1")) {
				//回收记录
				String keys = util.getColName("V_BAS_TEMP_RECLAIM");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BAS_TEMP_RECLAIM");
				sf.append(" where 1=1 ");
				sf.append(" and EQUIP_NO in ('"+req.getParameter("EQUIP_NO1")+"','"+req.getParameter("EQUIP_NO2")+"')");
				sf.append(util.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("ABNORMAL_FLAG", Hibernate.YES_NO);
	            query = util.getQuery(sf.toString(),keys,map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BAS_OILCARD_RECHARGE")) {
				//充值记录
				String keys = util.getColName("BAS_OILCARD_RECHARGE");
				sf = new StringBuffer();
				sf.append("select OILCARD,RECHARGER,RECHAGE_TIME,RECHARGE_AMOUNT,PRE_AMOUNT,ADDWHO,EDITTIME,EDITWHO,ID");
				sf.append(",to_char(ADDTIME,'yyyy-mm-dd hh24:mi') as ADDTIME ");
				sf.append(" from BAS_OILCARD_RECHARGE");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("OILCARD"))) {
					sf.append(util.addEqualSQL("OILCARD", req.getParameter("OILCARD")));
				}

	            query = util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	            if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row=(Integer.parseInt(CUR_PAGE)-1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("BAS_OILCARD")) {
				//油卡信息
				String keys = util.getColName("BAS_OILCARD");
				sf = new StringBuffer();
				sf.append("select OILCARD,PLATE_NO,STATUS,DRIVER,ADDTIME,ADDWHO,EDITTIME,EDITWHO,ID");
				sf.append(" from BAS_OILCARD");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("OILCARD"))) {
					sf.append(util.addLikeSQL("OILCARD", req.getParameter("OILCARD")));	
				}
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))){
					sf.append(util.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))){
					sf.append(util.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
				if(ObjUtil.isNotNull(req.getParameter("DRIVER"))){
					sf.append(util.addEqualSQL("DRIVER", req.getParameter("DRIVER")));
				}
				
				
	            query = util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}	
			else if(dsid.equals("BAS_GPSEQ")) {
				//GPS设备管理
				String keys = util.getColName("BAS_GPSEQ");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BAS_GPSEQ t");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("EQUIP_NO"))) {
					sf.append(util.addEqualSQL("EQUIP_NO", req.getParameter("EQUIP_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))) {
					sf.append(util.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))) {
					sf.append(util.addEqualSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(util.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
				if(ObjUtil.isNotNull(req.getParameter("USE_COMPANY"))) {
					sf.append(util.addEqualSQL("USE_COMPANY", req.getParameter("USE_COMPANY")));
				}
				if(ObjUtil.isNotNull(req.getParameter("EQUIP_MANAGER"))) {
					sf.append(util.addEqualSQL("EQUIP_MANAGER", req.getParameter("EQUIP_MANAGER")));
				}
	            sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));//通过自定义方式查询	
	            HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("ACTIVE_FLAG", Hibernate.YES_NO);
	            query = util.getQuery(sf.toString(),keys,map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			
	            if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}else{
					start_row=(Integer.parseInt(CUR_PAGE)-1)*page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("BAS_GPSEQ1")) {
				//GPS设备管理
				String keys = util.getColName("BAS_GPSEQ");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BAS_GPSEQ");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("EQUIP_NO"))) {
					sf.append(util.addALikeSQL("EQUIP_NO", req.getParameter("EQUIP_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))) {
					sf.append(util.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
				query = util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
			}
			else if(dsid.equals("BAS_GPS_RECORDS")) {
				//GPS设备管理/使用记录
				String keys = util.getColName("BAS_GPS_RECORDS");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from BAS_GPS_RECORDS");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("EQUIP_NO"))) {
					sf.append(util.addEqualSQL("EQUIP_NO", req.getParameter("EQUIP_NO")));
				}
				sf.append(" order by ADDTIME");
	            query = util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_BAS_GPS_RECLAIM")) {
				//GPS设备管理/回收记录
				String keys = util.getColName("V_BAS_GPS_RECLAIM");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BAS_GPS_RECLAIM");
				sf.append(" where 1=1 ");
				sf.append(util.addEqualSQL("EQUIP_NO", req.getParameter("EQUIP_NO")));
				HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("ABNORMAL_FLAG", Hibernate.YES_NO);
	            query = util.getQuery(sf.toString(),keys,map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_BAS_GPS_RECLAIM1")) {
				//GPS设备管理/回收记录
				String keys = util.getColName("V_BAS_GPS_RECLAIM");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BAS_GPS_RECLAIM");
				sf.append(" where 1=1 ");
				sf.append(util.addEqualSQL("EQUIP_NO", req.getParameter("EQUIP_NO")));
				sf.append(util.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				HashMap<String, Type> map = new HashMap<String, Type>();
	            map.put("ABNORMAL_FLAG", Hibernate.YES_NO);
	            query = util.getQuery(sf.toString(),keys,map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("INSUR_PURCHASE_RECORD")){
				//保险购买记录
				String keys = util.getColName("V_PURCHASE_RECORD");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_PURCHASE_RECORD t");
				sf.append(" where 1=1 ");
				sf.append(util.addLikeSQL("INS_NO", req.getParameter("INS_NO")));
				sf.append(util.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				sf.append(util.addEqualSQL("INS_TYPE_NAME", req.getParameter("INS_TYPE")));
				sf.append(util.addEqualSQL("INS_CLS_NAME", req.getParameter("INS_CLS")));
				sf.append(util.addEqualSQL("INS_COMPANY_NAME", req.getParameter("INS_COMPANY")));
				sf.append(util.addTimeSQL("INS_FROM", req.getParameter("INS_FROM"), ">="));
				sf.append(util.addTimeSQL("INS_TO", req.getParameter("INS_TO"), "<="));
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));//通过自定义方式查询
				query = util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("INS_OIL_RECORD")){
				String keys = util.getColName("V_OIL_RECORD");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_OIL_RECORD");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("PLATENO"))){
					sf.append(util.addALikeSQL("PLATENO", req.getParameter("PLATENO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("TRS_ID"))){
					sf.append(util.addALikeSQL("TRS_ID", req.getParameter("TRS_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("ODR_TIME_FROM"))){
					sf.append(util.addTimeSQL("OIL_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("ODR_TIME_FROM"))){
					sf.append(util.addTimeSQL("OIL_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				}
				query = util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("BAS_RECE_ACCOUNT")){
				String keys = util.getColName("V_RECE_ACCOUNT");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_RECE_ACCOUNT t");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("PLATENO"))){
					sf.append(util.addALikeSQL("PLATENO", req.getParameter("PLATENO")));
				}
				
				query = util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			     
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
						query.setFirstResult(start_row);
						query.setMaxResults(page_record);
					}else{
						start_row=(Integer.parseInt(CUR_PAGE)-1)*page_record;
						query.setFirstResult(start_row);
						query.setMaxResults(page_record);
					}
			
			}
			else if(dsid.equals("INSUR_ACCIDENT_LIST")){
				String keys = util.getColName("V_ACCIDENT_ORG");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_ACCIDENT_ORG");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("ORG_ID"))){
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(util.decode(req.getParameter("ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("ORG_ID")));
						sf.append(",%' ) ");
					}
					else {
						sf.append(util.addEqualSQL("ORG_ID", req.getParameter("ORG_ID")));
					}
					//sf.append(util.addEqualSQL("ORG_ID", req.getParameter("ORG_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))){
					sf.append(util.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("GOODS_NAME"))){
					sf.append(util.addALikeSQL("GOODS_NAME", req.getParameter("GOODS_NAME")));
				}
				if(ObjUtil.isNotNull(req.getParameter("STATUS"))){
					sf.append(util.addEqualSQL("STATUS", req.getParameter("STATUS")));
				}
				if(ObjUtil.isNotNull(req.getParameter("REPORT_NO"))){
					sf.append(util.addALikeSQL("REPORT_NO", req.getParameter("REPORT_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("ODR_TIME_FROM"))){
					sf.append(util.addTimeSQL("INSUR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("ODR_TIME_TO"))){
					sf.append(util.addTimeSQL("INSUR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				}
				
				query = util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("INSUR_ACCIDENT_LOG")){
				String keys = util.getColName("V_ACCIDENT_CODES");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_ACCIDENT_CODES");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("INSUR_ID"))){
					sf.append(util.addEqualSQL("INSUR_ID", req.getParameter("INSUR_ID")));
				}
				sf.append(" order by ADDTIME asc");
				query = util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("TRANS_VEH_REPAIR")){
				String keys = util.getColName("V_REPAIR_CODE");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_REPAIR_CODE t");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))){
					sf.append(util.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("DRIVER"))){
					sf.append(util.addALikeSQL("DRIVER", req.getParameter("DRIVER")));
				}
				if(ObjUtil.isNotNull(req.getParameter("REPAIR_TYPE"))){
					sf.append(util.addEqualSQL("REPAIR_TYPE", req.getParameter("REPAIR_TYPE")));
				}
				if(ObjUtil.isNotNull(req.getParameter("REPAIR_OBJECT"))){
					sf.append(util.addEqualSQL("REPAIR_OBJECT", req.getParameter("REPAIR_OBJECT")));
				}
				if(ObjUtil.isNotNull(req.getParameter("PRE_START_TIME"))){
					sf.append(util.addTimeSQL("PRE_START_TIME", req.getParameter("PRE_START_TIME"), ">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("PRE_END_TIME"))){
					sf.append(util.addTimeSQL("PRE_END_TIME", req.getParameter("PRE_END_TIME"), "<="));
				}
				if(ObjUtil.isNotNull(req.getParameter("REPAIR_COMPANY"))){
					sf.append(util.addALikeSQL("REPAIR_COMPANY", req.getParameter("REPAIR_COMPANY")));
				}
				sf.append(" order by ADDTIME asc");
				query = util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("TRANS_VEH_OIL")){
				String keys = util.getColName("V_OIL");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_OIL t");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))){
					sf.append(util.addALikeSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_NO"))){
					sf.append(util.addEqualSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				}
				if(ObjUtil.isNotNull(req.getParameter("ODR_TIME_FROM"))){
					sf.append(util.addTimeSQL("OIL_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("ODR_TIME_TO"))){
					sf.append(util.addTimeSQL("OIL_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				}
				sf.append(" order by ADDTIME asc");
				query = util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("SYS_IMPORT_CONFIG")){
				String keys = util.getColName("SYS_IMPORT_CONFIG");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from SYS_IMPORT_CONFIG");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("TABLE_NAME"))){
					sf.append(util.addEqualSQL("TABLE_NAME", req.getParameter("TABLE_NAME")));
				}
				query = util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_BAS_VEHICLEds")){
				String keys = util.getColName("V_BAS_VEHICLE");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_BAS_VEHICLE");
				if(ObjUtil.isNotNull(req.getParameter("PLATE_NO"))){
					sf.append(util.addEqualSQL("PLATE_NO", req.getParameter("PLATE_NO")));
				}
				query = util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			
			else if(dsid.equals("TRACK_SF_SKU")){
				String keys = "SKU_ID,SKU_NAME,PACK_ID,UOM";
				sf = new StringBuffer();
				sf.append("select distinct ");
				sf.append("SKU_ID,SKU_NAME,PACK_ID,UOM");
				sf.append(" from trans_shipment_item  where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("shpm_no"))){
					sf.append(util.addEqualSQL("shpm_no", req.getParameter("shpm_no")));
				}
				query = util.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}
			else if(dsid.equals("V_VEHICLE_TYPE")){
				//车型管理：lijun
				String keys = util.getColName("V_VEHICLE_TYPE");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_VEHICLE_TYPE t where 1=1 ");
				sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				query = util.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
				
			}
			else if(dsid.equals("VC_SUPPLIER")){
				String keys = util.getColName("V_SUPPLIER");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_SUPPLIER where 1=1");
//				if(!"wpsadmin".equals(req.getParameter("LOGIN_USER"))){
//					sf.append(" and id in(");
//					sf.append("select customer_id from V_USER_CUSTOMER where user_id='");
//					sf.append(req.getParameter("LOGIN_USER"));
//					sf.append("')");
//				}
				sf.append(" and enable_flag='Y'");
				sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
    		}
			else if(dsid.equals("BAS_Veh_SUPPLIER")||dsid.equals("BAS_VEH_SUPPLIER1")||dsid.equals("BAS_VEH_SUPPLIER2")){
				//供应商管理  -- lijun
				String keys = util.getColName("V_SUPPLIER");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(util.getColName("V_SUPPLIER"));
				sf.append(" from V_SUPPLIER where 1=1 ");
				//sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				if(ObjUtil.isNotNull(req.getParameter("FULL_INDEX"))) {
					sf.append(" and upper(FULL_INDEX) like upper('%"+req.getParameter("FULL_INDEX")+"%')");
				}
				sf.append(util.addEqualSQL("SUPLR_TYP", req.getParameter("SUPLR_TYP")));
//				sf.append(util.addEqualSQL("MAINT_ORG_ID", req.getParameter("MAINT_ORG_ID")));
				sf.append(util.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(util.addFlagSQL("INTL_FLAG_FLAG", req.getParameter("INTL_FLAG_FLAG")));
//				sf.append(util.addFlagSQL("TRANSPORT_FLAG", req.getParameter("TRANSPORT_FLAG")));
//				sf.append(util.addFlagSQL("WAREHOUSE_FLAG", req.getParameter("WAREHOUSE_FLAG")));
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_CODE"))){
					sf.append(util.addALikeSQL("SUPLR_CODE", req.getParameter("SUPLR_CODE")));					
				} 
				if(ObjUtil.isNotNull(req.getParameter("BLACKLIST_FLAG"))&&req.getParameter("BLACKLIST_FLAG").equals("true")){
					sf.append(util.addEqualSQL("BLACKLIST_FLAG", "Y"));					
				} 
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_CNAME"))){
					sf.append(util.addALikeSQL("SUPLR_CNAME", req.getParameter("SUPLR_CNAME")));					
				} 

				if((ObjUtil.isNotNull(req.getParameter("OR_TRANSPORT_FLAG")) && "true".equals(req.getParameter("OR_WAREHOUSE_FLAG")))){
					sf.append(" and ( 1 <> 1 ");
					sf.append(util.addFlagORSQL("TRANSPORT_FLAG",req.getParameter("OR_TRANSPORT_FLAG")));
					sf.append(util.addFlagORSQL("WAREHOUSE_FLAG", req.getParameter("OR_WAREHOUSE_FLAG")));
					sf.append(")");
				}
				
				if(ObjUtil.isNotNull(req.getParameter("MAINT_ORG_ID"))) { //wangjun 2010-6-30
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and MAINT_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(util.decode(req.getParameter("MAINT_ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("MAINT_ORG_ID")));
						sf.append(",%' ) ");
					}
					else {
						sf.append(util.addEqualSQL("MAINT_ORG_ID", req.getParameter("MAINT_ORG_ID")));
					}
				};
				
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));//自定义查询
//				query = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("TRANSPORT_FLAG", Hibernate.YES_NO);
				map.put("WAREHOUSE_FLAG", Hibernate.YES_NO);
				map.put("INTL_FLAG", Hibernate.YES_NO);
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("BLACKLIST_FLAG", Hibernate.YES_NO);
				map.put("VEHICLE_FOR_FLAG", Hibernate.YES_NO);
				map.put("INVOICE_FLAG", Hibernate.YES_NO);
				map.put("CONTRACT_FLAG", Hibernate.YES_NO);
				map.put("INS_FLAG", Hibernate.YES_NO);
				map.put("BLACKLIST_FLAG", Hibernate.YES_NO);
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
				
			}
			else if(dsid.equals("BAS_CUSTOMERList")){
				//客户管理 fanglm
				String keys = util.getColName("V_CUSTOMER");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from V_CUSTOMER t");
				sf.append(" where 1=1 ");
				sf.append(util.addALikeSQL("CUSTOMER_CODE", req.getParameter("CUSTOMER_CODE")));
				sf.append(util.addALikeSQL("CUSTOMER_CNAME", req.getParameter("CUSTOMER_CNAME")));
				sf.append(util.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				if(ObjUtil.isNotNull(req.getParameter("ID"))){
					sf.append(util.addEqualSQL("ID", req.getParameter("ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("PARENT_CUSTOMER_ID"))){
					sf.append(util.addEqualSQL("PARENT_CUSTOMER_ID", req.getParameter("PARENT_CUSTOMER_ID")));
				}
				sf.append(util.addFlagSQL("TRANSPORT_FLAG", req.getParameter("TRANSPORT_FLAG")));
				sf.append(util.addFlagSQL("WAREHOUSE_FLAG", req.getParameter("WAREHOUSE_FLAG")));
				sf.append(util.addFlagSQL("ENABLE_FLAG", req.getParameter("ENABLE_FLAG")));
				sf.append(util.addFlagSQL("PAYER_FLAG", req.getParameter("PAYER_FLAG")));
				if(req.getParameter("CUSTOMER_FLAG").equals("true") || req.getParameter("CUSTOMER_FLAG").equals("Y")){
					sf.append(util.addFlagSQL("CUSTOMER_FLAG", req.getParameter("CUSTOMER_FLAG")));
				}else{
					sf.append(" and CUSTOMER_FLAG <> 'Y'");
				}
				sf.append(util.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //wangjun 2010-6-30
						sf.append(" and (EXEC_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append(",%' ) ");
						sf.append(" or MAINT_ORG_ID");
		            	sf.append(" IN ");
		            	sf.append("    (SELECT ID ");
		            	sf.append("     From BAS_ORG ");
						sf.append("     Where id ='");
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append("'");
						sf.append("or ORG_INDEX Like '%,"); //
						sf.append(util.decode(req.getParameter("EXEC_ORG_ID")));
						sf.append(",%' )) ");
					}
					else {
						sf.append(util.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
					}
				};
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("TRANSPORT_FLAG", Hibernate.YES_NO);
				map.put("WAREHOUSE_FLAG", Hibernate.YES_NO);
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("CUSTOMER_FLAG", Hibernate.YES_NO);
				map.put("PAYER_FLAG", Hibernate.YES_NO);
				map.put("CONTACTER_FLAG", Hibernate.YES_NO);
				map.put("INVOICE_FLAG", Hibernate.YES_NO);
				map.put("MATCHROUTE_FLAG", Hibernate.YES_NO);
				map.put("UNIQ_CONO_FLAG", Hibernate.YES_NO);
				map.put("UNIQ_ADDR_FLAG", Hibernate.YES_NO);
				map.put("SKU_EDIT_FLAG", Hibernate.YES_NO);
				map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
				map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
				map.put("COD_FLAG", Hibernate.YES_NO);
				map.put("POD_FLAG", Hibernate.YES_NO);
				map.put("ENABLE_FLAG", Hibernate.YES_NO);
				map.put("ADDR_EDIT_FLAG", Hibernate.YES_NO);
				
				query = util.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);;
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			
			
			if(ObjUtil.ifNull(CUR_PAGE,"1").equals("1") && !dsid.equals("VC_BAS_AREA")) {
				ArrayList<String> recordList = util.getRecordByQuery(sf.toString());
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
            List<HashMap<String, String>> object = query.list();
	        Gson gson = new Gson();
	        String content = gson.toJson(object);
			p.print(content);
			
			/*if(ObjUtil.ifNull(CUR_PAGE,"1").equals("1") && !dsid.equals("VC_BAS_AREA")) {
	            LoginContent.getInstance().setPageInfo(util.getRecordByQuery(sf.toString()));
			}*/
			
			LoginContent.getInstance().closeSession();
			p.flush();
			p.close();
			p = null;
			
			
		}
	}
}
