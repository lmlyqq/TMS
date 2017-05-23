package com.rd.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import com.google.gson.Gson;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.obj.system.SYS_USER;

public class ServerFilter implements Filter{
	
	private String loginURL = "http://127.0.0.1:8080/SSOServer/login.jsp";
	private String authURL  = "http://127.0.0.1:8080/SSOServer/auth";
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = ObjUtil.ifObjNull(request.getParameter("token"), "").toString();
        HttpSession session = request.getSession();
        String json = "";
        //System.out.println(token);
        if(session.getAttribute("USER_ID") == null) {
        	if(ObjUtil.isNotNull(token)) {
        		PostMethod postMethod = new PostMethod(authURL);
                postMethod.addParameter("token", token);
                HttpClient httpClient = new HttpClient();
                try {
                    httpClient.executeMethod(postMethod);
                    json = postMethod.getResponseBodyAsString();
                    //System.out.println(json);
                    postMethod.releaseConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (null != json && !"".equals(json)) {
                	try {
	                    Gson gson = new Gson();  
	                    SYS_USER user =  gson.fromJson(json, SYS_USER.class); 
	                    session.setAttribute("USER_ID", user);
	                    arg2.doFilter(request, response);
                	}
                	catch(Exception e) {
                		e.printStackTrace();
                	}
                } else {
                    response.sendRedirect(loginURL + "?service=tms");
                }
        	}
        	else {
        		//System.out.println(loginURL + "?service=tms");
        		response.sendRedirect(loginURL + "?service=tms");
        	}
        }
        else {
        	arg2.doFilter(request, response);
        }
	}

	@Override
	public void init(FilterConfig config) throws ServletException {      
        authURL = config.getInitParameter("authURL");
        loginURL = config.getInitParameter("loginURL");
	}
}
