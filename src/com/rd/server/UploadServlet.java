package com.rd.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.rd.client.common.util.ObjUtil;
import com.rd.server.util.InputExcel;
import com.rd.server.util.Upload;

/**
 * 上传文件
 * @author fanglm
 * @edittime 2010-12-29 15:57
 *
 */
public class UploadServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String requestpath;  //获取路径，即当在回单中点击某条记录时获取该单号，作为路径的一部分

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		requestpath = req.getParameter("unique_ID");
		String path = getServletContext().getRealPath("/");//获取当前的项目路径
		path = path+requestpath+"\\";   //拼装成当前需要上传的路径文件夹
		String name = req.getParameter("filer"); 
		PrintWriter p=resp.getWriter();
		p.println("<!doctype html>");
		p.println("<html>");
		p.println("<head>");
		p.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		p.println("</head>");
		p.println("<body>");
		try{
			ServletFileUpload upload = new ServletFileUpload();
	        FileItemIterator iter = upload.getItemIterator(req);
	        
	         while (iter.hasNext()) {
	             FileItemStream item = iter.next();	            
	             if(!item.isFormField()){ //普通表单
	            	 name = item.getName();
	            	 if(ObjUtil.isNotNull(req.getParameter("type"))){
	            		 name = req.getParameter("fileName");
	            	 }
	            	 
	            	 InputStream stream = item.openStream();
	            	 if(ObjUtil.isNotNull(req.getParameter("purpose")) && 
	            			 "inputExcel".equalsIgnoreCase(req.getParameter("purpose"))){
	            		 try {
	            			 resp.setCharacterEncoding("UTF-8");
//	            			 HttpSession httpSession = req.getSession();
//	            			 SYS_USER user = (SYS_USER)httpSession.getAttribute("USER_ID");
//	            			 String userId=user.getUSER_ID();
	            			 String result = InputExcel.input(stream, req.getParameter("typeName"), req.getParameter("ADDWHO"),req.getParameter("tabName"),req.getParameter("SpName"));
		            		 p.println("<div id=\"result\">"+toHtml(new String(result.getBytes("utf-8"), "iso-8859-1"))+"</div>");
						} catch (Exception e) {
							e.printStackTrace();
							p.println("<div id=\"result\">"+toHtml(new String(e.getMessage().getBytes("utf-8"), "iso-8859-1"))+"</div>");
							stream.close();
						}
	            		 
	            	 }else{
		             	Upload upload2 = new Upload(path,name,stream);
		     		 	upload2.doUploadFile();
	            	 }
	            	
	             }
	         }
	         /**
  		 	 * @param p 关闭弹出的当前窗口
  		 	 */
  			 p.print("<script language=javascript>window.close();");  
             p.println("</script>");
             p.println("</body>");
             p.println("</html>");
 			 p.flush();
 			 p.close();
 			 p = null;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String toHtml(String str){
		if(str != null){
			str = str.replaceAll("<", "&lt;");
			str = str.replaceAll(">", "&gt;");
		}
		return str;
	}
}

