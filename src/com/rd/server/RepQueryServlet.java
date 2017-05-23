package com.rd.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
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
import com.rd.server.util.LoginContent;
import com.rd.server.util.SQLUtil;
import com.rd.server.util.SUtil;

@SuppressWarnings("serial")
public class RepQueryServlet extends HttpServlet{
	private Query query;
	private final int page_record = LoginContent.getInstance().pageSize;
	private List<HashMap<String, String>> object;
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
	throws ServletException, IOException {
		HttpSession session =req.getSession();
		session.setMaxInactiveInterval(24*60*60);
		SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
		String dsid = req.getParameter("ds_id"); // 数据源ID
		String flag = SUtil.iif(req.getParameter("OP_FLAG"), ""); // 操作标记
		String is_curr_page = SUtil.iif(req.getParameter("is_curr_page"),""); //分页查询
		SQLUtil sqlUtil = new SQLUtil(true);
		String name = req.getParameter("name");
		String where = req.getParameter("where");
		String tab = req.getParameter("table");
		
		if (dsid != null && flag.equals(StaticRef.MOD_FLAG)) {
			//SQLUtil sqlUtil = new SQLUtil(true);
			response.setCharacterEncoding("utf-8");
//			req.setCharacterEncoding("utf-8");
			PrintWriter p = response.getWriter();
			if(user == null) {
				p.print("404");
				return;
			}
			Session curSession = LoginContent.getInstance().getSession();
			String CUR_PAGE = (String) req.getParameter("CUR_PAGE"); // 当前页码
			int start_row = 0; // 开始行，从0开始
			StringBuffer sf = null;
			
			if(dsid.equals("R_KPI_LOAD_RATE")){
				sf = new StringBuffer();
				sf.append(" SELECT suplr_name,suplr_id,");
				sf.append(" sum(days0) as num, ");
				sf.append(" to_char(round(sum(days) * 100/ sum(days0),2)) || '%' as per,");
				sf.append(" sum(days1) as day1,");
				sf.append(" sum(days2) as day2,");
				sf.append(" sum(days3) as day3,");
				sf.append(" sum(days4) as day4,");
				sf.append(" sum(days5) as day5,");
				sf.append(" sum(days6) as day6,");
				sf.append(" EXEC_ORG_ID,EXEC_ORG_ID_NAME");
				sf.append(" from r_kpi_load_rate t");
				sf.append(" where 1 = 1 ");
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(" group by suplr_name,suplr_id,EXEC_ORG_ID,EXEC_ORG_ID_NAME");
				
				query = sqlUtil.getQuery(sf.toString(),"suplr_name,suplr_id,num,per,day1,day2,day3,day4,day5,day6,EXEC_ORG_ID,EXEC_ORG_ID_NAME", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			}else if(dsid.equals("R_KPI_DMG_RATE")){
				sf = new StringBuffer();
				sf.append(" SELECT suplr_name,");
				sf.append(" sum(num) as num,");
				sf.append(" sum(losdam) as losdam, ");
				sf.append(" sum(dmg1_qnty) as dmg1_qnty,");
				sf.append(" sum(dmg2_qnty) as dmg2_qnty,");
				sf.append(" sum(dmg3_qnty) as dmg3_qnty,");
				sf.append(" sum(los_qnty) as los_qnty,");
				//sf.append(" trans_uom ,");
				sf.append(" sum(los_amount) as los_amount,");
				sf.append(" to_char(round(sum(losdam) * 100/ sum(num),2)) || '%' as dmg_rate");
				sf.append(" from r_kpi_dmg_rate where 1 =1 ");
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(" group by suplr_name");
				
				query = sqlUtil.getQuery(sf.toString(),"suplr_name,num,losdam,dmg1_qnty,dmg2_qnty,dmg3_qnty,los_qnty,los_amount,dmg_rate", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
				
			}else if(dsid.equals("R_KPI_COLL")){
				sf = new StringBuffer();
				sf.append(" select suplr_name,");
				sf.append(" sum(S_FATHER) as NUM,");
				sf.append(" sum(tot_qnty) as tot_qnty,");
				sf.append(" sum(tot_qnty_each) as tot_qnty_each,");
				sf.append(" trans_uom,");
				sf.append(" sum(tot_vol) as tot_vol,");
				sf.append(" sum(tot_gross_w) as tot_grossw,");
				sf.append(" to_char(round(sum(S_CHILD) * 100/ sum(S_FATHER),2)) || '%' as load_rate,");
				sf.append(" to_char(round(sum(R_CHILD) * 100/ sum(S_FATHER),2)) || '%' as unload_rate,");
				sf.append(" sum(S_FATHER) - sum(R_CHILD) as unload_count,");
				sf.append(" to_char(round(sum(P_CHILD) * 100/ sum(S_FATHER),2)) || '%' as pod_rate,");
				sf.append(" sum(S_FATHER) - sum(P_CHILD) as pod_count,");
				sf.append(" to_char(round(sum(LOS_COUNT) * 100/ sum(S_FATHER),2)) || '%' as loss_rate,");
				sf.append(" sum(LOS_QNTY) as LOS_QNTY");
				sf.append(" from R_KPI_COLL where 1 = 1");
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(" group by suplr_name,trans_uom");
				
				query = sqlUtil.getQuery(sf.toString(),"suplr_name,NUM,tot_qnty,tot_qnty_each,trans_uom,tot_vol,tot_grossw,load_rate,unload_rate,unload_count,pod_rate,pod_count,loss_rate,LOS_QNTY", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
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
			}else if(dsid.equals("R_KPI_POD_RATE")){
				sf = new StringBuffer();
				sf.append(" SELECT suplr_name,suplr_id,");
				sf.append(" sum(days0) as num, ");
				sf.append(" to_char(round(sum(days) * 100/ sum(days0),2)) || '%' as per,");
				sf.append(" sum(days1) as day1,");
				sf.append(" sum(days2) as day2,");
				sf.append(" sum(days3) as day3,");
				sf.append(" sum(days4) as day4,");
				sf.append(" sum(days5) as day5,");
				sf.append(" sum(days6) as day6,");
				sf.append(" EXEC_ORG_ID,EXEC_ORG_ID_NAME");
				sf.append(" from r_kpi_pod_rate t");
				sf.append(" where 1 = 1 ");
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(" group by suplr_name,suplr_id,EXEC_ORG_ID,EXEC_ORG_ID_NAME");
				
				query = sqlUtil.getQuery(sf.toString(),"suplr_name,suplr_id,num,per,day1,day2,day3,day4,day5,day6,EXEC_ORG_ID,EXEC_ORG_ID_NAME", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
				
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
			}else if(dsid.equals("R_KPI_UNLOAD_RATE")){
				sf = new StringBuffer();
				sf.append(" SELECT suplr_name,suplr_id,");
				sf.append(" sum(days0) as num, ");
				sf.append(" to_char(round(sum(days) * 100/ sum(days0),2)) || '%' as per,");
				sf.append(" sum(days1) as day1,");
				sf.append(" sum(days2) as day2,");
				sf.append(" sum(days3) as day3,");
				sf.append(" sum(days4) as day4,");
				sf.append(" sum(days5) as day5,");
				sf.append(" sum(days6) as day6,");
				sf.append(" EXEC_ORG_ID,EXEC_ORG_ID_NAME");
				sf.append(" from r_kpi_unload_rate t");
				sf.append(" where 1 = 1 ");
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(" group by suplr_name,suplr_id,EXEC_ORG_ID,EXEC_ORG_ID_NAME");
				
				query = sqlUtil.getQuery(sf.toString(),"suplr_name,suplr_id,num,per,day1,day2,day3,day4,day5,day6,EXEC_ORG_ID,EXEC_ORG_ID_NAME", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			else if(dsid.equals("R_KPI_LOADWH_RATE")){
				sf = new StringBuffer();
				sf.append(" SELECT SUPLR_NAME,SUPLR_ID,");
				sf.append(" sum(days0) as NUM, ");
				sf.append(" to_char(round(sum(days) * 100/ sum(days0),2)) || '%' as PER,");
				sf.append(" sum(days1) as DAY1,");
				sf.append(" sum(days2) as DAY2,");
				sf.append(" sum(days3) as DAY3,");
				sf.append(" sum(days4) as DAY4,");
				sf.append(" sum(days5) as DAY5,");
				sf.append(" sum(days6) as DAY6,");
				sf.append(" EXEC_ORG_ID,EXEC_ORG_ID_NAME");
				sf.append(" FROM r_kpi_loadwh_rate t");
				sf.append(" where 1 = 1 ");
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(" group by SUPLR_NAME,SUPLR_ID,EXEC_ORG_ID,EXEC_ORG_ID_NAME");
				
				query = sqlUtil.getQuery(sf.toString(),"suplr_name,suplr_id,num,per,day1,day2,day3,day4,day5,day6,EXEC_ORG_ID,EXEC_ORG_ID_NAME", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
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
			}else if(dsid.equals("R_MNG_ORG_SUM")){
				sf = new StringBuffer();
				String keys="EXEC_ORG_ID_NAME,LD_QNTY,LD_VOL,LD_GWGT";
				sf.append(" SELECT EXEC_ORG_ID_NAME,");
//				sf.append(" trans_uom , ");
				sf.append(" sum(qnty) as ld_qnty,");
//				sf.append(" sum(qnty) as dam_qnty,");
				sf.append(" sum(vol) as ld_vol,");
				sf.append(" sum(g_wgt) as ld_gwgt");
//				sf.append(" sum(pre_fee) as pre_fee,");
//				sf.append(" to_char(round(sum(pre_fee) * 100/ all_fee,2)) || '%' as per ");
				sf.append(" from R_MNG_ORG_SUM_T t ");
//				sf.append(" (select sum(pre_fee) as all_fee from R_MNG_ORG_SUM WHERE 1 = 1 ");
//				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
//				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
//				sf.append(" ) t1");
				sf.append(" where 1 = 1 ");
//				sf.append(sqlUtil.addALikeSQL("SUPLR_NAME", req.getParameter("SUPLR_NAME")));
//				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ORD_TYP")));
//				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), null));
				sf.append(" group by exec_org_id_name");
				
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
			
				
			}else if(dsid.equals("R_MNG_SUPLR_SUM")){
				sf = new StringBuffer();
				String keys="SUPLR_NAME,LD_QNTY,LD_VOL,LD_GWGT";
				sf.append(" SELECT suplr_name,");
//				sf.append(" trans_uom , ");
				sf.append(" sum(qnty) as ld_qnty,");
//				sf.append(" sum(qnty_) as dam_qnty,");
				sf.append(" sum(vol) as ld_vol,");
				sf.append(" sum(g_wgt) as ld_gwgt ");
//				sf.append(" sum(pre_fee) as pre_fee,");
//				sf.append(" to_char(round(sum(pre_fee) * 100/ all_fee,2)) || '%' as per ");
				sf.append(" from R_MNG_SUPLR_SUM_T t ");
//				sf.append(" (select sum(pre_fee) as all_fee from R_MNG_SUPLR_SUM WHERE 1 = 1 ");
//				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
//				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
//				sf.append(" ) t1");
				sf.append(" where 1 = 1 ");
//				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), null));
				sf.append(" group by suplr_name");
				
				query = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//				query = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			
				
			}else if(dsid.equals("R_MNG_SKU_SUM")){
				sf = new StringBuffer();
				sf.append(" SELECT descr_c,");
//				sf.append(" trans_uom , ");
				sf.append(" sum(ld_qnty) as ld_qnty,");
				sf.append(" sum(loss_qnty) as loss_qnty,");
				sf.append(" sum(ld_vol) as ld_vol,");
				sf.append(" sum(ld_gwgt) as ld_gwgt,");
				sf.append(" sum(pre_fee) as pre_fee,");
				sf.append(" to_char(round(sum(pre_fee) * 100/ all_fee,2)) || '%' as per ");
				sf.append(" from R_MNG_SKU_SUM t,");
				sf.append(" (select sum(pre_fee) as all_fee from R_MNG_SKU_SUM");
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" WHERE EXEC_ORG_ID");
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
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));

				sf.append(") t1");
				sf.append(" where 1 = 1 ");
				sf.append(sqlUtil.addALikeSQL("CUSTOMR_NAME", req.getParameter("CUSTOMR_NAME")));
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
				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ORD_TYP")));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(" group by descr_c,all_fee");
				
				query = sqlUtil.getQuery(sf.toString(),"descr_c,ld_qnty,loss_qnty,ld_vol,ld_gwgt,pre_fee,per", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			
				
			}else if(dsid.equals("R_MNG_AREA_SUM")){
				sf = new StringBuffer();
				String keys="LOAD_AREA_NAME2,UNLOAD_AREA_NAME2,LD_QNTY,LD_VOL,LD_GWGT";
				sf.append(" SELECT LOAD_AREA_NAME2,UNLOAD_AREA_NAME2 ,");
//				sf.append(" provence ,");
				sf.append(" sum(qnty) as LD_QNTY,");
//				sf.append(" sum(loss_qnty) as loss_qnty,");
				sf.append(" sum(vol) as LD_VOL,");
				sf.append(" sum(g_wgt) as LD_GWGT ");
//				sf.append(" sum(pre_fee) as pre_fee,");
//				sf.append(" to_char(round(sum(pre_fee) * 100/ all_fee,2)) || '%' as per ");
				sf.append(" from R_MNG_AREA_SUM_T t ");
//				sf.append(" (select sum(pre_fee) as all_fee from R_MNG_AREA_SUM  WHERE 1=1");
//				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
//					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
//						sf.append(" AND EXEC_ORG_ID");
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
//						sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
//					}
//				}
//				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
//				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
//				sf.append(") t1");
				sf.append(" where 1 = 1 ");
//				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ORD_TYP")));
//				sf.append(sqlUtil.addEqualSQL("CUSTOMER_NAME", req.getParameter("CUSTOMER_NAME")));
//				sf.append(sqlUtil.addALikeSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
//				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
//					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
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
//						sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
//					}
//				}
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID2")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), null));
				sf.append(" group by LOAD_AREA_NAME2,UNLOAD_AREA_NAME2");
				
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
			
				
			}else if(dsid.equals("R_MNG_ODR_SUM")){
				sf = new StringBuffer();
				String keys="BIZ_TYP_NAME,LD_QNTY,LD_VOL,LD_GWGT";
				sf.append(" SELECT BIZ_TYP_NAME ,");
//				sf.append(" trans_uom, ");
				sf.append(" sum(qnty) as LD_QNTY,");
//				sf.append(" sum(loss_qnty) as loss_qnty,");
				sf.append(" sum(vol) as LD_VOL,");
				sf.append(" sum(g_wgt) as LD_GWGT ");
//				sf.append(" sum(pre_fee) as pre_fee,");
//				sf.append(" to_char(round(sum(pre_fee) * 100/ all_fee,2)) || '%' as per ");
				sf.append(" from R_MNG_BIZ_SUM t ");
//				sf.append(" (select sum(pre_fee) as all_fee from R_MNG_ODR_SUM");
//				if(ObjUtil.isNotNull(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID")))) { //执行机构
//					sf.append(" WHERE EXEC_ORG_ID");
//	            	sf.append(" IN ");
//	            	sf.append("    (SELECT ID ");
//	            	sf.append("     From BAS_ORG ");
//					sf.append("     Where id ='");
//					sf.append(req.getParameter("EXEC_ORG_ID"));
//					sf.append("'");
//					sf.append("or ORG_INDEX Like '%,"); //
//					sf.append(req.getParameter("EXEC_ORG_ID"));
//					sf.append(",%' )");
//					
//				}
//				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
//				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));

//				sf.append(") t1");
				sf.append(" where 1 = 1 ");
//				sf.append(sqlUtil.addALikeSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
//				sf.append(sqlUtil.addALikeSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
//				sf.append(sqlUtil.addALikeSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID")));
//				if(ObjUtil.isNotNull(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID")))) { //执行机构
//					sf.append(" and EXEC_ORG_ID");
//	            	sf.append(" IN ");
//	            	sf.append("    (SELECT ID ");
//	            	sf.append("     From BAS_ORG ");
//					sf.append("     Where id ='");
//					sf.append(req.getParameter("EXEC_ORG_ID"));
//					sf.append("'");
//					sf.append("or ORG_INDEX Like '%,"); //
//					sf.append(req.getParameter("EXEC_ORG_ID"));
//					sf.append(",%' ) ");
//				}
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), null));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
				sf.append(" group by biz_typ_name");
				
				query = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//				query = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			
				
			}else if(dsid.equals("R_JOB_OPER_HEADER")) {
				//
				//sqlUtil.setTableAlias("t");
				String keys = "BIZ_TYP_NAME,EXEC_ORG_ID_NAME,LOAD_AREA_NAME2,UNLOAD_AREA_NAME2,SUPLR_NAME,TOT_QNTY,TOT_VOL,TOT_GROSS_W";
				sf = new StringBuffer();
				sf.append(keys);
				sf.append(" from R_JOB_OPER_HEADER ");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));   
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));  
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));   
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("LOAD_TIME_TO"), "<="));
//				sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID_NAME", req.getParameter("EXEC_ORG_ID_NAME")));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_NAME")));
				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ODR_TYP_NAME")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_NAME2", req.getParameter("LOAD_AREA_NAME2")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_NAME2", req.getParameter("UNLOAD_AREA_NAME2")));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), "false"));
				sf.append(" GROUP BY EXEC_ORG_ID_NAME,BIZ_TYP_NAME,LOAD_AREA_NAME2,UNLOAD_AREA_NAME2,SUPLR_NAME ORDER BY EXEC_ORG_ID_NAME,BIZ_TYP_NAME,LOAD_AREA_NAME2,UNLOAD_AREA_NAME2");
				query = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			}else if(dsid.equals("R_JOB_OPER_ITEM")) {
				//
//				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("R_JOB_OPER_ITEM");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from R_JOB_OPER_ITEM t ");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
//				sf.append(sqlUtil.addLikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));   
//				sf.append(util.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
//				sf.append(util.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("LOAD_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
//				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ODR_TYP_NAME")));
//				sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FROM"), ">=")); //wangjun 2011-4-6
//				sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"), "<=")); 
//				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_FROM"), ">=")); //wangjun 2011-4-7
//				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
				sf.append(sqlUtil.addALikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addALikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				sf.append(sqlUtil.addALikeSQL("REFENENCE1", req.getParameter("REFENENCE1")));
//				sf.append(sqlUtil.addAddrRole(user.getROLE_ID_NAME(),user.getUSER_ID()));

//				if(ObjUtil.isNotNull(req.getParameter("LOAD_FLAG")) && req.getParameter("LOAD_FLAG").equals("true")){
//					sf.append(sqlUtil.addMathSQL("STATUS", "40", ">="));
//					sf.append(sqlUtil.addMathSQL("STATUS", "90", "<="));
//				}
//				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_FLAG")) && req.getParameter("UNLOAD_FLAG").equals("true")){
//					sf.append(sqlUtil.addMathSQL("STATUS", "40", "<"));
//					
//				}
//				sf.append(util.addCustomerRole(user.getUSER_CUSTOMER()));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				//不包含促销品
//				if(ObjUtil.isNotNull(req.getParameter("C_CX_FLAG")) && req.getParameter("C_CX_FLAG").equals("true")) {
//					sf.append(" and substr(ODR_NO,0,2) <> 'CX'");
//				}
//				if(!ObjUtil.isNotNull(req.getParameter("C_SORT_FLAG")) || req.getParameter("C_SORT_FLAG").equals("false")) {
//					sf.append(" and picking_stat <> '15'");
//				}
				sf.append(" order by depart_time desc");
//				System.out.println(sf.toString());
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
			}else if(dsid.equals("R_JOB_TRANS_TRACK")) {
				//
				String keys = sqlUtil.getColName("R_JOB_TRANS_TRACK");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from R_JOB_TRANS_TRACK t");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));   
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));   
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("END_LOAD_TIME", req.getParameter("END_LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("END_LOAD_TIME", req.getParameter("END_LOAD_TIME_TO"), "<="));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ODR_TYP_NAME")));
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("POD_TIME", req.getParameter("POD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("POD_TIME", req.getParameter("POD_TIME_TO"), "<="));
				sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(" order by addtime desc");
				//query = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
				
			}else if(dsid.equals("R_ARE_CUS_ANAY")) {
				//报表管理-->财务报表-->区域/客户运费分析表
//				String keys = sqlUtil.getColName("R_ARE_CUS_ANAY");
				sf = new StringBuffer();
				sf.append("select v.*,(case when f.fee=0 then 0 else ROUND(v.DUE_FEE/f.FEE*100)end) ||'%' AS R_PRCENT,order_ct.num as ORDER_NUM from ");
				sf.append("(select v.PROVICE_NAME,v.UNLOAD_AREA_NAME,v.UNLOAD_NAME,sum(v.QNTY) as QNTY,SUM(v.QNTY_EACH) QNTY_EACH,SUM(v.VOL) VOL,SUM(v.GROSS_W) GROSS_W,SUM(v.DUE_FEE) DUE_FEE,"
						 + "(case when SUM(v.qnty) =0 then 0 else round(SUM(v.due_fee)/SUM(v.qnty),4) end) as CS_FEE" 
						 + ",round(SUM(v.due_fee)/SUM(v.qnty_each),4) AS EA_FEE"
						 + ",v.customer_id");
				sf.append(" from R_ARE_CUS_ANAY v");
				sf.append(" where 1= 1 ");
				sf.append(sqlUtil.addEqualSQL("v.CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));   
				sf.append(sqlUtil.addEqualSQL("v.CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));   
				sf.append(sqlUtil.addTimeSQL("v.ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("v.ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("v.LOAD_TIME", req.getParameter("LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("v.LOAD_TIME", req.getParameter("LOAD_TIME_TO"), "<="));
//				sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID", req.getParameter("EXEC_ORG_ID_NAME")));
				if(ObjUtil.isNotNull(req.getParameter("EXEC_ORG_ID"))) {
					if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
						sf.append(" and v.EXEC_ORG_ID");
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
				};
				sf.append(" group by v.provice_name,v.unload_area_name,v.unload_name,v.customer_id) v");
				sf.append(",(SELECT SUM(DUE_FEE) AS FEE FROM TRANS_BILL_DETAIL ) F");
				sf.append(",(select unload_area_name,unload_name,sum(num) NUM from (select distinct odr_no,unload_area_name,unload_name,1 as num from v_shipment_header) group by unload_area_name,unload_name) order_ct");
				sf.append(" where v.unload_area_name = order_ct.unload_area_name(+)");
				sf.append(" and v.unload_name = order_ct.unload_name(+)");
//				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ORD_TYP")));
				query = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
				
			}else if(dsid.equals("R_SKU_TRANS_ANAY")) {
				//报表管理-->财务报表-->货品运费分析表
//				String keys = sqlUtil.getColName("R_SKU_TRANS_ANAY");
				sf = new StringBuffer();
				sf.append("select v.*,(case when f.fee=0 then 0 else ROUND(v.DUE_FEE/f.FEE*100)end) ||'%' AS R_PRCENT,sku_ct.qty as NUM from ");
				sf.append("(select v.DESCR_C AS SKU_CLS_NAME,v.SKU,v.SKU_NAME,v.SKU_SPEC,sum(v.QNTY) as QNTY,sum(v.VOL) as VOL,sum(v.G_WGT) G_WGT,sum(v.DUE_FEE) DUE_FEE,SUM(v.QNTY_EACH) QNTY_EACH,"
						+ "(case when SUM(v.qnty) =0 then 0 else round(SUM(v.due_fee)/SUM(v.qnty),4) end) as CS_FEE" 
						 + ",round(SUM(v.due_fee)/SUM(v.qnty_each),4) AS EA_FEE");
				sf.append(",v.customer_id ");
				sf.append(" from R_SKU_TRANS_ANAY v");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));   
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));   
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("LOAD_TIME", req.getParameter("LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("LOAD_TIME", req.getParameter("LOAD_TIME_TO"), "<="));
				//sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID_NAME", req.getParameter("EXEC_ORG_ID_NAME")));
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
				};
//				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ORD_TYP")));
				sf.append(" group by v.descr_c,v.sku,v.sku_name,v.sku_spec,v.customer_id) v");
				sf.append(",(select sum(t.qty) as qty,sku from (select distinct shpm_no,sku,1 as qty from trans_shipment_item) t group by sku) sku_ct");
				sf.append(",(SELECT SUM(due_FEE) AS FEE FROM TRANS_BILL_DETAIL ) F");
				sf.append(" where v.sku = sku_ct.sku(+)");
				query = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			}else if(dsid.equals("R_TRANS_PAY_SUM")) {
				//报表管理-->财务报表-->运费支付汇总
				String keys = sqlUtil.getColName("R_TRANS_PAY_SUM");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from R_TRANS_PAY_SUM ");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));   
				sf.append(sqlUtil.addEqualSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));   
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("END_LOAD_TIME", req.getParameter("LOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("END_LOAD_TIME", req.getParameter("LOAD_TIME_TO"), "<="));
				//sf.append(sqlUtil.addEqualSQL("EXEC_ORG_ID_NAME", req.getParameter("EXEC_ORG_ID_NAME")));
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
				};
				sf.append(sqlUtil.addEqualSQL("ODR_TYP", req.getParameter("ORD_TYP")));
				query = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			}else if(dsid.equals("SORTING_WH")) {
				String keys = sqlUtil.getColName("R_SORTING_WH");
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from R_SORTING_WH ");
				sf.append(" where 1=1 ");
				sf.append(" order by addtime desc");
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
			}else if(dsid.equals("R_CUSTOMER_ORDER")){
				//wangjun  
				sf = new StringBuffer();
				String keys = sqlUtil.getColName("R_CUSTOMER_ORDER");
				sf.append(" select ");
				sf.append(keys);
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from R_CUSTOMER_ORDER_H t where 1=1 ");
				}
				else {
					sf.append(" from R_CUSTOMER_ORDER t where 1=1 ");
				}
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID", req.getParameter("AREA_ID")));//收获区域
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));//收获区域
				sf.append(sqlUtil.addEqualSQL("UNLOAD_ID", req.getParameter("UNLOAD_ID")));  //收货方
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addLikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addLikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_TEL", req.getParameter("UNLOAD_TEL")));
				sf.append(sqlUtil.addFlagSQL("SLF_PICKUP_FLAG", req.getParameter("SLF_PICKUP_FLAG")));
				//fanglm 2011-10-7 客户权限，组织机构权限
				sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));			
				sf.append(sqlUtil.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));//通过自定义方式查询
				sf.append(" order by t.odr_no desc,t.shpm_no");
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
				
			}else if(dsid.equals("R_UNLOAD_RATE")){
				//wangjun  
				sf = new StringBuffer();
				String keys = sqlUtil.getColName("R_UNLOAD_RATE");
				sf.append(" select ");
				sf.append(keys);
				sf.append(" from R_UNLOAD_RATE t where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));//收获区域
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));  //收货方
				sf.append(sqlUtil.addALikeSQL("LOAD_AREA_NAME", req.getParameter("LOAD_AREA_NAME")));//收获区域
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));  //收货方
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ASSIGN_TIME", req.getParameter("ASSIGN_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("DEPART_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("DEPART_TIME_TO"), "<="));
				sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addALikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				sf.append(sqlUtil.addMathSQL("SHPM_STATUS", req.getParameter("SHPM_STATUS_FROM"), ">="));
				sf.append(sqlUtil.addMathSQL("SHPM_STATUS", req.getParameter("SHPM_STATUS_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_TO"), "<="));
				sf.append(sqlUtil.addTimeSQL("UNLOAD_OPER_TIME", req.getParameter("UNLOAD_OPER_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("UNLOAD_OPER_TIME", req.getParameter("UNLOAD_OPER_TIME_TO"), "<="));
				//yuanlei 2012-09-11 到货及时率报表查询和自定义查询增加“到库时间”查询条件
				sf.append(sqlUtil.addTimeSQL("ARRIVE_WHSE_TIME", req.getParameter("ARRIVE_WHSE_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ARRIVE_WHSE_TIME", req.getParameter("ARRIVE_WHSE_TIME_TO"), "<="));
				sf.append(sqlUtil.addCriteriaSQL(req.getParameter("operator"), req.getParameterValues("criteria")));//通过自定义方式查询
				//yuanlei
				//wangJun 2011-10-30
				if(ObjUtil.isNotNull(req.getParameter("ARRIVE_WHSE_FLAG")) && req.getParameter("ARRIVE_WHSE_FLAG").equals("true")){
					sf.append(sqlUtil.addMathSQL("ARRIVE_WHSE_FLAG","0",">"));  //未准时到库
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_FLAG")) && req.getParameter("LOAD_FLAG").equals("true")){
					sf.append(sqlUtil.addMathSQL("LOAD_FLAG","0",">"));  //未准时发货
				}
				if(ObjUtil.isNotNull(req.getParameter("UNLOAD_FLAG")) && req.getParameter("UNLOAD_FLAG").equals("true")){
					sf.append(sqlUtil.addMathSQL("UNLOAD_FLAG","0",">"));  //未准时到货
				}
				if(ObjUtil.isNotNull(req.getParameter("POD_FLAG")) && req.getParameter("POD_FLAG").equals("true")){
					sf.append(sqlUtil.addMathSQL("POD_FLAG","0",">"));  //未准时回单
				}
				
				sf.append(sqlUtil.addCustomerRole(user.getUSER_CUSTOMER()));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
			
					
				if(ObjUtil.isNotNull(req.getParameter("shoud_com_flag")) && req.getParameter("shoud_com_flag").equals("true")) { //执行机构
					sf.append(" and SHPM_STATUS = ");
					sf.append(" '");
					sf.append("40");
					sf.append("' ");
					sf.append("and");
//					sf.append(" to_date(pre_unload_time,'yyyy/mm/dd hh24:mi') ");
//					sf.append("< to_date((select to_char(sysdate,'YYYY-MM-DD HH24-MI') from dual),'yyyy/mm/dd hh24:mi')");
					sf.append(" to_date(pre_unload_time,'yyyy/mm/dd hh24:mi') ");
					sf.append("< to_date(to_char(sysdate,'YYYY-MM-DD HH24-MI'),'yyyy/mm/dd hh24:mi')");
//					sf.append(" pre_unload_time ");
//					sf.append("<(select sysdate from dual)");

				}
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			}else if (dsid.equals("TMP_CUSTOMER_ACC")){
//				String keys = sqlUtil.getColName("R_UNLOAD_RATE");
				sf = new StringBuffer();
				sf.append("select * from v_bill_detail ");
				sf.append("where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));  //客户
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));//收获区域
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));//收获区域
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				sf.append(sqlUtil.addMathSQL("SHPM_STAT", req.getParameter("SHPM_STAT_FROM"),">="));
				sf.append(sqlUtil.addMathSQL("SHPM_STAT", req.getParameter("SHPM_STAT_FROM"),"<="));
				sf.append(sqlUtil.addEqualSQL("AUDIT_STAT", req.getParameter("AUDIT_STAT")));//审核状态
				sf.append(sqlUtil.addEqualSQL("SUPLR_ID",req.getParameter("SUPLR_ID")));//供应商
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
				};
				
				String stringQueny = "{call QUERY_CUSTOMER_ACC(?,?,?,?,?)}";
				object  = new ArrayList<HashMap<String, String>>();
				Session sessio = LoginContent.getInstance().getSession();
				Connection conn = null;
				@SuppressWarnings("unused")
				CallableStatement cs = null;
				try {
//					Transaction tx = sessio.beginTransaction();
					conn = sessio.connection();
					CallableStatement cstmt = conn.prepareCall(stringQueny);
					cstmt.setInt(1, Integer.parseInt(( CUR_PAGE == null || CUR_PAGE.equals("0")) ? "1":CUR_PAGE));
					cstmt.setInt(2, 100);
					cstmt.setString(3, sf.toString());
					cstmt.registerOutParameter(4, Types.VARCHAR);
					cstmt.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
					cstmt.execute(); 
					ResultSet rs = (ResultSet) cstmt.getObject(5);
					String code = cstmt.getString(4);
					    
					HashMap<String, String> map = new HashMap<String, String>();
					    while(rs.next()){
					     map = new HashMap<String, String>();
					     map.put("SERIAL_NUM", rs.getString(1));
					     map.put("CUSTOM_ODR_NO", rs.getString(2));
					     map.put("LOAD_NO", rs.getString(3));
					     map.put("SHPM_NO", rs.getString(4));
					     map.put("UNLOAD_NAME", rs.getString(5));
					     map.put("SKU", rs.getString(6));
					     map.put("SKU_NAME", rs.getString(7));
					     map.put("SKU_SPEC", rs.getString(8));
					     map.put("UOM", rs.getString(9));
					     map.put("ODR_QNTY", rs.getString(10));
					     map.put("LOAD_QNTY", rs.getString(11));
					     map.put("UNLOAD_QNTY", rs.getString(12));
					     map.put("NO_UNLOAD_QNTY", rs.getString(13));
					     map.put("SHPM_STAT_NAME", rs.getString(14));
					     
					     map.put("ACCOUNT_STAT_NAME", rs.getString(15));
					     map.put("AUDIT_STAT_NAME", rs.getString(16));
					     map.put("ODR_TIME", rs.getString(17));
					     map.put("LOAD_TIME", rs.getString(18));
					     map.put("UNLOAD_TIME", rs.getString(19));
					     map.put("POD_TIME", rs.getString(20));
					     map.put("SUPLR_ID_NAME", rs.getString(21));
					     map.put("EXEC_ORG_ID_NAME", rs.getString(22));
					     map.put("PLATE_NO", rs.getString(23));
					     map.put("UNLOAD_AREA_NAME", rs.getString(25));
					     map.put("LOAD_NAME", rs.getString(29));
					     map.put("TRANS_NOTES", rs.getString(30));
					     object.add(map);
					 } 
//					 tx.commit();
					 
					 LoginContent.getInstance().setPageInfo(code, 100);

				}catch (Exception e) {
					e.printStackTrace();
				}

			}else if(dsid.equals("R_CUSTOM_REPORT")){ //发票组
				String table = req.getParameter("tableName");
				String keys  = sqlUtil.getColName(table);
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from ");
				sf.append(table);
				sf.append(" where 1=1 ");
				
				query  = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
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
			else if (dsid.equals("V_ORDER_HEADER")) {  //托运单
				sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("TRANS_ORDER_HEADER",sqlUtil.getTableAlias());
				sf = new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(",t1.NAME_C AS BIZ_TYP_NAME,t2.NAME_C AS ODR_TYP_NAME");
				if(ObjUtil.isNotNull(req.getParameter("HISTORY_FLAG")) && req.getParameter("HISTORY_FLAG").equals("true")) {
					sf.append(" from TRANS_ORDER_HEADER_H t");
				}
				else {
					sf.append(" from TRANS_ORDER_HEADER t");
				}
				sf.append(",BAS_CODES t1");
				sf.append(",BAS_CODES t2");
				sf.append(" where t.BIZ_TYP = t1.id and t1.prop_code = 'BIZ_TYP'");
				sf.append(" and t.ODR_TYP = t2.id and t2.prop_code = 'TRS_TYP'");
//				sf.append(sqlUtil.addALikeSQL("FULL_INDEX", req.getParameter("FULL_INDEX")));
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
//				sf.append(sqlUtil.addALikeSQL("EXEC_ORG_ID_NAME", req.getParameter("EXEC_ORG_ID_NAME")));
//				sf.append(sqlUtil.addALikeSQL("LOAD_AREA_NAME", req.getParameter("LOAD_AREA_NAME")));
//				sf.append(sqlUtil.addALikeSQL("LOAD_AREA_NAME2", req.getParameter("LOAD_AREA_NAME2")));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("LOAD_AREA_ID")));
//				sf.append(sqlUtil.addALikeSQL("UNLOAD_AREA_NAME", req.getParameter("UNLOAD_AREA_NAME")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("UNLOAD_AREA_ID2")));
//				sf.append(sqlUtil.addALikeSQL("UNLOAD_AREA_NAME2", req.getParameter("UNLOAD_AREA_NAME2")));
				sf.append(sqlUtil.addEqualSQL("COD_FLAG",req.getParameter("COD_FLAG")));
				sf.append(sqlUtil.addEqualSQL("TRANS_SRVC_ID", req.getParameter("TRANS_SRVC_ID")));
				sf.append(sqlUtil.addEqualSQL("UGRT_GRD", req.getParameter("UGRT_GRD")));
				sf.append(sqlUtil.addEqualSQL("EXEC_STAT", req.getParameter("EXEC_STAT")));
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
				sf.append(sqlUtil.addEqualSQL("ADDWHO",req.getParameter("ADDWHO")));
				sf.append(sqlUtil.addEqualSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NAME", req.getParameter("LOAD_NAME")));
				sf.append(sqlUtil.addALikeSQL("UNLOAD_NAME", req.getParameter("UNLOAD_NAME")));
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
				sf.append(sqlUtil.addEqualSQL("REFENENCE3",req.getParameter("REFENENCE3")));
				sf.append(sqlUtil.addFlagSQL("SLF_PICKUP_FLAG", req.getParameter("SLF_PICKUP_FLAG"),true));
				if(req.getParameter("REFENENCE1")==null||req.getParameter("REFENENCE1").length()==0){
					sf.append(sqlUtil.addOrgRole(user.getUSER_ID(),req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"),req.getParameter("C_RDC_FLAG")));
					if(ObjUtil.isNotNull(req.getParameter("CREATE_ORG_ID"))) {
						if(ObjUtil.isNotNull(req.getParameter("C_ORG_FLAG")) && req.getParameter("C_ORG_FLAG").equals("true")) { //执行机构
							sf.append(" and CREATE_ORG_ID");
			            	sf.append(" IN ");
			            	sf.append("    (SELECT ID ");
			            	sf.append("     From BAS_ORG ");
							sf.append("     Where id ='");
							sf.append(req.getParameter("CREATE_ORG_ID"));
							sf.append("'");
							sf.append("or ORG_INDEX Like '%,"); //
							sf.append(req.getParameter("CREATE_ORG_ID"));
							sf.append(",%' ) ");
						}
						else {
							sf.append(sqlUtil.addEqualSQL("CREATE_ORG_ID", req.getParameter("CREATE_ORG_ID")));
						}
					}
				}
				
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
				sf.append(" order by t.ADDTIME DESC");
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
				map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
				map.put("ODR_TIME", Hibernate.STRING);
				map.put("POD_FLAG", Hibernate.YES_NO);
				map.put("DISCOUNT", Hibernate.DOUBLE);
				map.put("COD_FLAG", Hibernate.YES_NO);
				String field = keys.replace(sqlUtil.getTableAlias() + ".", "") + ",BIZ_TYP_NAME,ODR_TYP_NAME";
				query  = sqlUtil.getQuery(sf.toString(), field, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}				
			}else if (dsid.equals("R_KPI_ORDER")) {
				String keys = sqlUtil.getColName("R_KPI_ORDER");
				sf= new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from R_KPI_ORDER t");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				sf.append(sqlUtil.addALikeSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addALikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("AUDIT_FLAG", Hibernate.YES_NO);
				map.put("LOAD_FLAG", Hibernate.YES_NO);
				map.put("UNLOAD_FLAG", Hibernate.YES_NO);
				map.put("POD_FLAG", Hibernate.YES_NO);
				
				query = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}else if (dsid.equals("R_PICKUP_SHPM")) {
				String keys = sqlUtil.getColName("R_PICKUP_SHPM");
				sf= new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from R_PICKUP_SHPM t");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				sf.append(sqlUtil.addALikeSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addALikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addALikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(sqlUtil.addFlagSQL("SLF_PICKUP_FLAG", req.getParameter("SLF_PICKUP_FLAG")));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("SLF_PICKUP_FLAG", Hibernate.YES_NO);
				
				query = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}else if (dsid.equals("R_DELIVER_SHPM")) {
				String keys = sqlUtil.getColName("R_DELIVER_SHPM");
				sf= new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from R_DELIVER_SHPM t");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				sf.append(sqlUtil.addALikeSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addALikeSQL("ODR_NO", req.getParameter("ODR_NO")));
				sf.append(sqlUtil.addALikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				sf.append(sqlUtil.addALikeSQL("LOAD_NO", req.getParameter("LOAD_NO")));
				sf.append(sqlUtil.addEqualSQL("BIZ_TYP", req.getParameter("BIZ_TYP")));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(sqlUtil.addFlagSQL("SLF_DELIVER_FLAG", req.getParameter("SLF_DELIVER_FLAG")));
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				
				HashMap<String, Type> map = new HashMap<String, Type>();
				map.put("SLF_DELIVER_FLAG", Hibernate.YES_NO);
				
				query = sqlUtil.getQuery(sf.toString(), keys, map).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}else if(dsid.equals("R_TRUNK_MOVEMENT_VIEW")) {
				//
				sqlUtil.setTableAlias("t");
				String keys = "REFENENCE1,LOAD_AREA_NAME2,UNLOAD_AREA_NAME2,ADDTIME,TOT_QNTY,TOT_GROSS_W,TOT_VOL,STATUS_NAME";
				sf = new StringBuffer();
				sf.append("select REFENENCE1,LOAD_AREA_NAME2,UNLOAD_AREA_NAME2,ADDTIME,TOT_QNTY,TOT_GROSS_W,TOT_VOL,STATUS_NAME");
				sf.append(" from R_TRUNK_MOVEMENT_PREVIEW t");
				sf.append(" where 1=1 ");  
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("END_AREA_ID")));
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FROM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FROM")));  //状态
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FROM"), ">=")); //wangjun 2011-4-6
				    sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"), "<=")); 
				}
				//sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), "false"));
				query = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			}else if(dsid.equals("R_VEH_LOAD_VIEW")) {
				//
				sqlUtil.setTableAlias("t");
				String keys = "LOAD_NO,PLATE_NO,REMAIN_GROSS_W,LOAD_AREA_NAME2,UNLOAD_AREA_NAME2,ADDTIME,TOT_QNTY,TOT_GROSS_W,TOT_VOL";
				sf = new StringBuffer();
				sf.append("select LOAD_NO,PLATE_NO,REMAIN_GROSS_W,LOAD_AREA_NAME2,UNLOAD_AREA_NAME2,ADDTIME,TOT_QNTY,TOT_GROSS_W,TOT_VOL");
				sf.append(" from R_VEH_LOAD_VIEW t");
				sf.append(" where 1=1 ");  
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ADDTIME_TO"), "<="));
				sf.append(sqlUtil.addEqualSQL("LOAD_AREA_ID2", req.getParameter("START_AREA_ID")));
				sf.append(sqlUtil.addEqualSQL("UNLOAD_AREA_ID2", req.getParameter("END_AREA_ID")));
				sf.append(sqlUtil.addALikeSQL("PLATE_NO",req.getParameter("PLATE_NO")));
				if(ObjUtil.isNotNull(req.getParameter("STATUS_TO")) && ObjUtil.isNotNull(req.getParameter("STATUS_FROM")) 
						&& req.getParameter("STATUS_TO").equals(req.getParameter("STATUS_FROM"))) {
					sf.append(sqlUtil.addEqualSQL("STATUS", req.getParameter("STATUS_FROM")));
				}
				else {
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_TO"),"<="));
					sf.append(sqlUtil.addMathSQL("STATUS", req.getParameter("STATUS_FROM"),">="));
				}
				//sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), "false"));
				query = sqlUtil.getQuery(sf.toString(),keys,null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			
			else if(dsid.equals("R_KPI_POD_RATE")){
				sf = new StringBuffer();
				sf.append(" SELECT suplr_name,");
				sf.append(" sum(days0) as num, ");
				sf.append(" to_char(round(sum(days) * 100/ sum(days0),2)) || '%' as per,");
				sf.append(" sum(days1) as day1,");
				sf.append(" sum(days2) as day2,");
				sf.append(" sum(days3) as day3,");
				sf.append(" sum(days4) as day4,");
				sf.append(" sum(days5) as day5,");
				sf.append(" sum(days6) as day6,");
				sf.append(" EXEC_ORG_ID,EXEC_ORG_ID_NAME");
				sf.append(" from r_kpi_pod_rate_t t");
				sf.append(" where 1 = 1 ");
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ADDTIME", req.getParameter("ODR_TIME_TO"), "<="));
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_ID"))){
					sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				}
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), req.getParameter("C_RDC_FLAG")));
				sf.append(" group by suplr_name,EXEC_ORG_ID,EXEC_ORG_ID_NAME");
				
				//query = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				query = sqlUtil.getQuery(sf.toString(),"suplr_name,num,per,day1,day2,day3,day4,day5,day6,EXEC_ORG_ID,EXEC_ORG_ID_NAME", null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
			}else if (dsid.equals("R_SERVICE_DAILY")) {
				//sqlUtil.setTableAlias("t");
				String keys = sqlUtil.getColName("R_SERVICE_DAILY");
				sf= new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from R_SERVICE_DAILY t");
				sf.append(" where 1=1 ");
				sf.append(sqlUtil.addEqualSQL("CUSTOMER_ID", req.getParameter("CUSTOMER_ID")));
				sf.append(sqlUtil.addALikeSQL("CUSTOM_ODR_NO", req.getParameter("CUSTOM_ODR_NO")));
				sf.append(sqlUtil.addALikeSQL("REFENENCE1", req.getParameter("REFENENCE1")));
				sf.append(sqlUtil.addALikeSQL("SHPM_NO", req.getParameter("SHPM_NO")));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("ODR_TIME", req.getParameter("ODR_TIME_TO"), "<="));
				
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("DEPART_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("DEPART_TIME", req.getParameter("DEPART_TIME_TO"), "<="));
				
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_FROM"), ">="));
				sf.append(sqlUtil.addTimeSQL("UNLOAD_TIME", req.getParameter("UNLOAD_TIME_TO"), "<="));
				
				sf.append(sqlUtil.addOrgRole(user.getUSER_ID(), req.getParameter("EXEC_ORG_ID"), req.getParameter("C_ORG_FLAG"), "false"));
				
				//HashMap<String, Type> map = new HashMap<String, Type>();
				
				query = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}else if (dsid.equals("CustoReport")) {
				//sqlUtil.setTableAlias("t");
				sf= new StringBuffer();
				sf.append("select ");
				sf.append(name);
				sf.append(" from "+tab);
				sf.append(where);
				query = sqlUtil.getQuery(sf.toString(), null, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			else if (dsid.equals("B_RECPAYCOMPARE")) {
				String keys = sqlUtil.getColName("B_RECPAYCOMPARE");
				sf= new StringBuffer();
				sf.append("select ");
				sf.append(keys);
				sf.append(" from B_RECPAYCOMPARE ");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("SUPLR_ID"))){
					sf.append(sqlUtil.addEqualSQL("SUPLR_ID", req.getParameter("SUPLR_ID")));
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_DATE_FROM"))){
				    sf.append(sqlUtil.addTimeSQL("LOAD_DATE", req.getParameter("LOAD_DATE_FROM"), ">="));
				}
				if(ObjUtil.isNotNull(req.getParameter("LOAD_DATE_TO"))){
				    sf.append(sqlUtil.addTimeSQL("LOAD_DATE", req.getParameter("LOAD_DATE_TO"), "<="));
				}
				query = sqlUtil.getQuery(sf.toString(), keys, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				
				if (SUtil.iif(CUR_PAGE, "0").equals("0")) {
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				} else {
					start_row = (Integer.parseInt(CUR_PAGE) - 1) * page_record;
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
				}
			}
			if(query != null){
				object = query.list();
				query = null;
			}
			
			if(ObjUtil.ifNull(CUR_PAGE,"1").equals("1") && !dsid.equals("TMP_CUSTOMER_ACC")) {
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
			
			Gson gson = new Gson();
			String content = gson.toJson(object);
			p.print(content);
			
			/*if(ObjUtil.ifNull(CUR_PAGE,"1").equals("1") && !dsid.equals("TMP_CUSTOMER_ACC")) {
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
			try {
				//SUtil.insertlog(StaticRef.ACT_FETCH, StaticRef.ACT_SUCCESS, user.getUSER_NAME(), user.getDEFAULT_ORG_ID_NAME());
				
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

}