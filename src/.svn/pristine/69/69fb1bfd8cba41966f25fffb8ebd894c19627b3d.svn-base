package com.rd.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.rd.client.common.util.ObjUtil;
import com.rd.server.util.LoginContent;
import com.rd.server.util.SQLUtil;

/**
 * 系统登录
 * 根据输入用户获取此用户所具有的组织机构与仓库权限
 * @author fanglm
 * @create time 2011-1-12 16:00
 *
 */
public class SelectServlet extends HttpServlet {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Query query;
	/**
     * Constructor of the object.
     */
    public SelectServlet() {
        super();
    }

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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("utf-8");
        
        String username=request.getParameter("id").toString().trim();
        String xml_start="<selects>";
        String xml_end="</selects>";
        StringBuffer xml = new StringBuffer();
        
        try{
        	SQLUtil sqlUtil = new SQLUtil(true);
	        StringBuffer sql = new StringBuffer();
        	/*sql.append("select WHSE_ID as ID,WHSE_NAME as NAME,DEFAULT_FLAG from v_user_org_whse");
        	sql.append(" where use_flag='Y'");
        	sql.append(sqlUtil.addEqualSQL("user_id", username));
        	sql.append(" order by default_flag desc");	        
	        query = sqlUtil.getQuery(sql.toString(), null, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        List<HashMap<String, String>> object = query.list();*/
	        
	        sql = new StringBuffer();
	        if("wpsadmin".equals(username)) {
	        	sql.append("select CODE,NAME_C from BAS_CODES where prop_code = 'SYS_TYP'");
	        	sql.append(" order by show_seq asc");
	        }
	        else {
		        sql.append("select t1.CODE,t1.NAME_C from SYS_USER_SYSTEM t,BAS_CODES t1");
	        	sql.append(" where t.PRI_SYSTEM = t1.id and t1.prop_code = 'SYS_TYP' ");
	        	sql.append(sqlUtil.addEqualSQL("user_id", username));
	        	sql.append(" order by t.default_flag desc");
	        }
        	query = sqlUtil.getQuery(sql.toString(), null, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        List<HashMap<String, String>> sys_map = query.list();
	        
	        /*for(int i =0;i<object.size();i++){
	        	if( i==0 && "N".equals(ObjUtil.ifObjNull(object.get(0).get("DEFAULT_FLAG"),"N").toString())){
	        		xml.append("<select><value> </value><text> </text></select>");
	        	}
	        	
	        	xml.append("<select><value>");
	        	xml.append(object.get(i).get("ID"));
	        	xml.append("</value><text>");
	        	xml.append(object.get(i).get("NAME"));
	        	xml.append("</text></select>");
	        }*/
	        if(sys_map != null && sys_map.size() > 0) {
	        	HashMap map = null;
		        for(int i=0;i<sys_map.size();i++){
		        	map = sys_map.get(i);
		   		 	xml.append("<select><value>");
		        	xml.append(map.get("CODE"));
		        	xml.append("</value><text>");
		        	xml.append(map.get("NAME_C"));
		        	xml.append("</text></select>");
		        }
	        }
	        
	        
	        sql = new StringBuffer();
	        sql.append("select ID,ORG_CNAME as NAME,DEFAULT_FLAG from v_user_org");
        	sql.append(" where 1=1 ");
        	sql.append(sqlUtil.addEqualSQL("user_id", username));
        	sql.append(" order by default_flag desc");
        	query = sqlUtil.getQuery(sql.toString(), null, null).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	        List<HashMap<String, String>> org_map = query.list();
	        
	        /*for(int i =0;i<object.size();i++){
	        	if( i==0 && "N".equals(ObjUtil.ifObjNull(object.get(0).get("DEFAULT_FLAG"),"N").toString())){
	        		xml.append("<select><value> </value><text> </text></select>");
	        	}
	        	
	        	xml.append("<select><value>");
	        	xml.append(object.get(i).get("ID"));
	        	xml.append("</value><text>");
	        	xml.append(object.get(i).get("NAME"));
	        	xml.append("</text></select>");
	        }*/
	        
	        for(int i=0;i<org_map.size();i++){
	        	if( i==0 && "N".equals(ObjUtil.ifObjNull(org_map.get(0).get("DEFAULT_FLAG"),"N").toString())){
	        		xml.append("<select><value> </value><text> </text></select>");
	        	}
	        	
	        	xml.append("<input><value>");
	        	xml.append(org_map.get(i).get("ID"));
	        	xml.append("</value><text>");
	        	xml.append(org_map.get(i).get("NAME"));
	        	xml.append("</text></input>");
	        }
	        
	        String last_xml=xml_start+xml+xml_end;
    		LoginContent.getInstance().closeSession();
	        response.getWriter().write(last_xml);
        }catch (Exception e) {
			e.printStackTrace();
		}
        
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
