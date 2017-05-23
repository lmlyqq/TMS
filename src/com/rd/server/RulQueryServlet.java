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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import com.google.gson.Gson;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.obj.system.SYS_USER;
import com.rd.server.util.LoginContent;
import com.rd.server.util.SQLUtil;
import com.rd.server.util.SUtil;

/**
 * 业务规则下所有模块对应的servlet
 * 
 * @author yuanlei 
 * 
 */
@SuppressWarnings("serial")
public class RulQueryServlet extends HttpServlet {
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
			PrintWriter p = response.getWriter();
			if(user == null) {
				p.print("404");
				return;
			}
			Session curSession = LoginContent.getInstance().getSession();
			String CUR_PAGE = (String) req.getParameter("CUR_PAGE"); // 当前页码
			int start_row = 0; // 开始行，从0开始
			StringBuffer sf = null;
			
			if (dsid.equals("V_RUL_HEADER")) {
				//配载规则
				String keys = util.getColName("V_RUL_HEADER");
				
				sf = new StringBuffer();
				sf.append("select "); 
				sf.append(keys);
				sf.append(" from V_RUL_HEADER ");
				sf.append(" where 1=1");
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
			}
			else if(dsid.equals("RUL_DISPATCH_GROUP")) {
				//配载规则->分组方式
				sf = new StringBuffer();
				sf.append("SELECT ID,SHOW_SEQ,GROUP_CONDITION,RUL_ID from RUL_DISPATCH_GROUP");
				sf.append(" where 1=1 ");
				if(ObjUtil.isNotNull(req.getParameter("RUL_ID"))) {
					sf.append(util.addEqualSQL("RUL_ID", req.getParameter("RUL_ID")));
				}
				sf.append(" order by SHOW_SEQ ASC");
	            query = util.getQuery(sf.toString(),"ID,SHOW_SEQ,GROUP_CONDITION,RUL_ID",null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	            if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
					
				}else {
					start_row=(Integer.parseInt(CUR_PAGE)-1)*page_record;
					query.setFirstResult(start_row);
				    query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("RUL_DISPATCH_PRIOR")){
				//配载规则->优先级
				sf = new StringBuffer();
				sf.append("select ID,SHOW_SEQ,CONDITION,SORT_BY,RUL_ID from RUL_DISPATCH_PRIOR");
				sf.append(" WHERE 1 = 1 ");
				if(ObjUtil.isNotNull(req.getParameter("RUL_ID"))) {
					sf.append(util.addEqualSQL("RUL_ID", req.getParameter("RUL_ID")));
				}
				sf.append(" order by SHOW_SEQ ASC");
				query = util.getQuery(sf.toString(),"ID,SHOW_SEQ,CONDITION,SORT_BY,RUL_ID",null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
					
				}else {
					start_row=(Integer.parseInt(CUR_PAGE)-1)*page_record;
					query.setFirstResult(start_row);
				    query.setMaxResults(page_record);
				}
			}
			else if(dsid.equals("TRANS_NODE_RULE")) {
				//运输规则->节点规则
				sf = new StringBuffer();
				sf.append("SELECT ID,NODE_MODEL,SMS_FLAG,REC_FEE_FLAG,PAY_FEE_FLAG,TIME_FLAG,BIZ_LOG_FLAG,OPT_LOG_FLAG,ENABLE_FLAG from TRANS_NODE_RULE");
				sf.append(" where 1=1 ");
				sf.append(util.addEqualSQL("NODE_MODEL", req.getParameter("NODE_MODEL")));
				sf.append(util.addFlagSQL("ENABLE_FLAG",req.getParameter("ENABLE_FLAG")));
	            query = curSession.createSQLQuery(sf.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	            if(SUtil.iif(CUR_PAGE, "0").equals("0")){
					query.setFirstResult(start_row);
					query.setMaxResults(page_record);
					
				}else {
					start_row=(Integer.parseInt(CUR_PAGE)-1)*page_record;
					query.setFirstResult(start_row);
				    query.setMaxResults(page_record);
				}
			}
			
			if(ObjUtil.ifNull(CUR_PAGE,"1").equals("1")) {
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
			
			/*if(ObjUtil.ifNull(CUR_PAGE,"1").equals("1")) {
	            LoginContent.getInstance().setPageInfo(util.getRecordByQuery(sf.toString()));
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
