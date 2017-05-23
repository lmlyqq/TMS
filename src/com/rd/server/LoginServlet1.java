package com.rd.server;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.obj.system.SYS_USER;
import com.rd.server.util.LoginContent;
import com.rd.server.util.SQLUtil;
import com.rd.server.util.SUtil;

/**
 * 系统登录
 * @author fanglm
 * @create time 2011-1-12 15:38
 *
 */
public class LoginServlet1 extends HttpServlet{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * Constructor of the object.
     */
    public LoginServlet1() {
        super();
    }
    
//    private String login = "location.href ='/test/login.jsp'";
//    private String main = "/mainform.html?gwt.codesvr=127.0.0.1:9997";
//    private String main = "/test/mainform.html";
      private String login = "location.href ='/tms/index.jsp'";
      private String main = "/mainform.html?gwt.codesvr=127.0.0.1:9997";
      //private String main = "/pfs/mainform.html";


    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest request,final HttpServletResponse response)
            throws ServletException, IOException {
		
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=gb2312"); 
        String msgString = "00";
        
        String username=ObjUtil.ifNull(request.getParameter("username"),"").toString().trim();
        String passwd=ObjUtil.ifNull(request.getParameter("password"),"").toString();
        //String whse_id=ObjUtil.ifNull(request.getParameter("warehouse"),"").toString();
        String org_id=ObjUtil.ifNull(request.getParameter("org"),"").toString();
        
        SYS_USER user = null;
		//Session session = LoginContent.getInstance().getSession();
		
      
//		String s=request.getRequestedSessionId();
//		if(s.equals(LoginContent.getSessionId())){
//			response.getWriter().write("<script>alert('已有账户登录，请先退出登录!');" + login + "</script>");
//			return;
//		}
		
		SQLUtil sqlUtil = new SQLUtil(true);
	    StringBuffer sf = new StringBuffer();
	    sf.append("select USER_ID,USER_NAME,ACTIVE_FLAG,USERGRP_ID,USERGRP_ID_NAME,DEFAULT_ORG_ID,DEFAULT_ORG_ID_NAME,ROLE_ID,ROLE_ID_NAME,CUR_STATUS,CUR_STATUS_NAME,PASSWORD,ERROR_LOGIN,SALT ");
	    sf.append(",(select org_cname from bas_org where 1=1 ");
	    sf.append(sqlUtil.addEqualSQL("id", org_id));
	    sf.append(") as ORG_NAME,(select parent_org_id from bas_org where 1=1 ");
	    sf.append(sqlUtil.addEqualSQL("id", org_id));
	    sf.append(") as DEFAULT_ORG_PARENTID");
	    sf.append(" from V_USER where 1=1");
	    sf.append(sqlUtil.addEqualSQL("USER_ID", username));
	    //System.out.println(sf.toString());
	    Query query = sqlUtil.getQuery(sf.toString(), null, null).addScalar("USER_ID",Hibernate.STRING).addScalar("USER_NAME",Hibernate.STRING)
	    .addScalar("ACTIVE_FLAG",Hibernate.STRING).addScalar("USERGRP_ID",Hibernate.STRING).addScalar("USERGRP_ID_NAME",Hibernate.STRING)
	    .addScalar("DEFAULT_ORG_ID",Hibernate.STRING).addScalar("DEFAULT_ORG_PARENTID", Hibernate.STRING).addScalar("DEFAULT_ORG_ID_NAME",Hibernate.STRING)
	    .addScalar("ROLE_ID",Hibernate.STRING).addScalar("ROLE_ID_NAME",Hibernate.STRING)
	    .addScalar("CUR_STATUS",Hibernate.STRING).addScalar("CUR_STATUS_NAME",Hibernate.STRING).addScalar("PASSWORD",Hibernate.STRING)
	    //.addScalar("DEFAULT_WHSE_ID", Hibernate.STRING).addScalar("DEFAULT_WHSE_NAME", Hibernate.STRING)
	    //.addScalar("WHSE_NAME", Hibernate.STRING)
	    .addScalar("ORG_NAME", Hibernate.STRING).addScalar("ERROR_LOGIN", Hibernate.STRING)
	    .addScalar("SALT",Hibernate.STRING).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	   
	    List<HashMap<String, Object>> object = query.list();
	    
        if(object != null && object.size() > 0) {
        	user = (SYS_USER)SUtil.convertMapToModel(object.get(0),SYS_USER.class);
        	String flag=user.getACTIVE_FLAG();
        	if("Y".equalsIgnoreCase(flag) || "TRUE".equalsIgnoreCase(flag)){
        		String pwd = SUtil.getMd5(passwd, user.getSALT());
            	if(!pwd.equals(user.getPASSWORD())) {
            		if(Integer.parseInt(user.getERROR_LOGIN()) >= 4) {
                		sf = new StringBuffer();
                		sf.append(" update SYS_USER set ACTIVE_FLAG = 'N',ERROR_LOGIN = 0");
                		sf.append(" where USER_ID = ?");
                		excuteSQL(user.getUSER_ID(),sf.toString());
                		msgString = "密码联系5次输入错误，帐号已被冻结，请联系管理员激活！";
            		}
            		else {
            			String ERROR_COUNT = Integer.toString(Integer.parseInt(user.getERROR_LOGIN()) + 1);
            			String REMAIN_COUNT = Integer.toString(4 - Integer.parseInt(user.getERROR_LOGIN()));
	            		sf = new StringBuffer();
	            		sf.append(" update SYS_USER set ERROR_LOGIN = '");
	            		sf.append(ERROR_COUNT);
	            		sf.append("' where USER_ID = ?");
	            		excuteSQL(user.getUSER_ID(),sf.toString());
	            		msgString = "用户密码不正确，请重新输入,你还有" + REMAIN_COUNT + "次机会！";
            		}
            	}
            	else if(ObjUtil.isNotNull(org_id) && !"1".equals(org_id)) {
            		
            		//fanglm 用户客户权限
            		/*sf = new StringBuffer();
            		sf.append("select customer_id AS CUSTOMER_ID");
            		sf.append(" from sys_user_customer ");
            		sf.append(" where user_id = '");
            		sf.append(user.getUSER_ID());
            		sf.append("'");
            		query = LoginContent.getInstance().getSession().createSQLQuery(sf.toString())
            		    .addScalar("CUSTOMER_ID",Hibernate.STRING)
            		    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            	    object = query.list();
            		
            	    StringBuffer custBuff = new StringBuffer();
            	    for(int i=0;i<object.size();i++){
            	    	custBuff.append("'");
            	    	custBuff.append(object.get(i).get("CUSTOMER_ID"));
            	    	custBuff.append("',");
            	    }
            	    user.setUSER_CUSTOMER(ObjUtil.isNotNull(custBuff.toString())?custBuff.substring(0,custBuff.length()-1):"");*/
            	    
            		user.setCUR_STATUS(StaticRef.ON_LINE);
            		user.setCUR_STATUS_NAME(StaticRef.ON_LINE_NAME);
            		//user.setDEFAULT_ORG_ID_NAME(org_id);
            		//user.setDEFAULT_WHSE_NAME(whse_id);
            		//LoginContent.getInstance().setLoginUser(user);
            		HttpSession httpSession = request.getSession();           		
            		httpSession.setAttribute("USER_ID", user);
            		
            		sf = new StringBuffer();
            		sf.append(" update SYS_USER set CUR_STATUS = '");
            		sf.append(StaticRef.ON_LINE);
            		sf.append("',IP_ADDR = '");
            		sf.append(request.getRemoteAddr());
            		sf.append("',LOGIN_TIME = sysdate,ERROR_LOGIN = 0 where USER_ID = ?");
            		excuteSQL(user.getUSER_ID(),sf.toString());
            		
            		
            		response.addCookie(new Cookie("CUR_STATUS", StaticRef.ON_LINE));
            		response.addCookie(new Cookie("CUR_STATUS_NAME", URLEncoder.encode(StaticRef.ON_LINE_NAME,"UTF-8")));
            		response.addCookie(new Cookie("DEFAULT_ORG_ID", ObjUtil.ifNull(org_id,user.getDEFAULT_ORG_ID())));
            		//response.addCookie(new Cookie("DEFAULT_ORG_PARENTID", ObjUtil.ifNull((String)object.get(0).get("DEFAULT_ORG_PARENTID"),user.getDEFAULT_ORG_PARENTID())));
            		response.addCookie(new Cookie("DEFAULT_ORG_ID_NAME", URLEncoder.encode(ObjUtil.ifNull(user.getORG_NAME(),user.getDEFAULT_ORG_ID_NAME()),"UTF-8")));
            		response.addCookie(new Cookie("MAIL", user.getMAIL()));
            		response.addCookie(new Cookie("MODIFY_FLAG", user.getMODIFY_FLAG()));
            		//response.addCookie(new Cookie("PASSWORD", user.getPASSWORD()));
               		if(ObjUtil.isNotNull(user.getROLE_ID())) {
               			response.addCookie(new Cookie("ROLE_ID", user.getROLE_ID()));
            			response.addCookie(new Cookie("ROLE_ID_NAME", URLEncoder.encode(user.getROLE_ID_NAME(),"UTF-8")));
               		}
            		//response.addCookie(new Cookie("SALT", URLEncoder.encode(user.getSALT(),"UTF-8")));
            		response.addCookie(new Cookie("TEL", user.getTEL()));
            		response.addCookie(new Cookie("USER_ID", user.getUSER_ID()));
            		response.addCookie(new Cookie("USER_NAME", URLEncoder.encode(user.getUSER_NAME(),"UTF-8")));
            		if(ObjUtil.isNotNull(user.getUSERGRP_ID())) {
            			response.addCookie(new Cookie("USERGRP_ID", user.getUSERGRP_ID()));
            			response.addCookie(new Cookie("USERGRP_ID_NAME", URLEncoder.encode(ObjUtil.ifNull(user.getUSERGRP_ID_NAME(),""),"UTF-8")));
            		}
            		//response.addCookie(new Cookie("DEFAULT_WHSE_ID", ObjUtil.ifNull(whse_id,user.getDEFAULT_WHSE_ID())));
            		//response.addCookie(new Cookie("DEFAULT_WHSE_NAME", URLEncoder.encode(ObjUtil.ifNull(user.getWHSE_NAME(),user.getDEFAULT_WHSE_NAME()),"UTF-8")));
            		response.addCookie(new Cookie("IP_ADDR", request.getRemoteAddr()));

    				//response.addCookie(new Cookie("USER_CUSTOMER",user.getUSER_CUSTOMER()));
    				response.sendRedirect(main);
    			        	
            	}
            	else{
            		  msgString = "组织机构不能为空！";
            	}
        	}else{
        		msgString="用户未激活，不能登录";
        	}
        	
        }else{
        	msgString = "用户名不存在，请重新输入！";
        }
		LoginContent.getInstance().closeSession();
		//session = null;
        response.addCookie(new Cookie("USER_NAME", username));
        
        response.getWriter().write("<script>alert('"+ msgString +"');" + login + "</script>");
       
    }
	
	
	public String excuteSQL(String user_id,String sql) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String userStr = StaticRef.SUCCESS_CODE;
		try {
			conn = LoginContent.getInstance().getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, user_id);
	        int result = pstmt.executeUpdate();
	        if(result < 1) {
	        	userStr = StaticRef.FAILURE_CODE;
	        }
		}
		catch(Exception e) {
			e.printStackTrace();
			userStr = StaticRef.FAILURE_CODE + e.getMessage();
		}
		finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
		    		conn.close();
		    	}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return userStr;
	}

    /**
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to post.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request,response);
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occure
     */
    public void init() throws ServletException {
        // Put your code here
    }
}
