package com.rd.server;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.rd.client.common.util.ObjUtil;
import com.rd.server.util.Upload;

public class FileManageServlet extends HttpServlet{

	private static final long serialVersionUID = -35743396809354996L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		HttpSession session = null;
		session = req.getSession();
		session.setMaxInactiveInterval(24*60*60);
//		SYS_USER user = (SYS_USER)session.getAttribute("USER_ID");
		PrintWriter p = resp.getWriter();
		
		String isUpload = req.getParameter("IS_UPLOAD");
		String parentId = req.getParameter("PARENT_ID");
		String filePath = req.getParameter("FILE_PATH");
//		String dsid = req.getParameter("ds_id");
		
		if("Y".equals(isUpload)){
			upload(req, resp, p);
		}else{
			List<Map<String, String>> resultList = null; 
			if(ObjUtil.isNotNull(filePath)){
				resultList = getSubFile(filePath);
			}else if(ObjUtil.isNotNull(parentId)){
				resultList = getSubDir(parentId);
			}else{
				resultList = getRoot();
			}
			
			Gson gson = new Gson();
	        String content = gson.toJson(resultList);
			p.print(content);
			
			p.flush();
			p.close();
			p = null;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	private void upload(HttpServletRequest req, HttpServletResponse resp, PrintWriter p){
		String name = req.getParameter("filer");
		String filePath = req.getParameter("PATH");
		filePath += File.separator;
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
	            	 Upload upload2 = new Upload(filePath,name,stream);
	            	 upload2.upLoadFile(name, filePath, false);

	            	 p.println("<div id=\"result\">00</div>");
	             }
	         }
	        
		}catch (Exception e) {
			e.printStackTrace();
			p.println("<div id=\"result\">01</div>");  
		}finally{
			p.println("</body>");
			p.println("</html>");
			p.flush();
			p.close();
			p = null;
		}
		
	}
	
	private List<Map<String, String>> getRoot(){
		List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
		String realPath = this.getServletContext().getRealPath("/");
		File rootFile = new File(realPath);
		
		Map<String, String> fileMap = new HashMap<String, String>();
		fileMap.put("ID", rootFile.getAbsolutePath());
		fileMap.put("PARENT_ID", "Root");
		fileMap.put("FILE_NAME", rootFile.getName());
		fileMap.put("FILE_PATH", rootFile.getAbsolutePath());
		resultList.add(fileMap);
		
		readSubDir(rootFile, resultList);
		
		return resultList;
	}
	
	private List<Map<String, String>> getSubDir(String parentId){
		List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
		File parentFile = new File(parentId);
		
		readSubDir(parentFile, resultList);
		
		return resultList;
	}
	
	private List<Map<String, String>> getSubFile(String dir) {
		List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
		File dirFile = new File(dir);
		
		readSubFile(dirFile, resultList);
		
		return resultList;
	}
	
	private void readSubDir(File parentDir, List<Map<String, String>> resultList) {
		if(parentDir.canRead() && parentDir.isDirectory()){
			File[] subFile = parentDir.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File filter) {
					return filter.isDirectory();
				}
			});
			if(subFile != null && subFile.length > 0){
				Map<String, String> fileMap = null;
				for (File file : subFile) {
					fileMap = new HashMap<String, String>();
					fileMap.put("ID", file.getAbsolutePath());
					fileMap.put("PARENT_ID", parentDir.getAbsolutePath());
					fileMap.put("FILE_NAME", file.getName());
					fileMap.put("FILE_PATH", file.getAbsolutePath());
					resultList.add(fileMap);
				}
			}
		}
	}
	
	private void readSubFile(File dir, List<Map<String, String>> resultList){
		if(dir.canRead() && dir.isDirectory()){
			File[] subFile = dir.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File filter) {
					return filter.isFile();
				}
			});
			if(subFile != null && subFile.length > 0){
				Map<String, String> fileMap = null;
				for (File file : subFile) {
					fileMap = new HashMap<String, String>();
					fileMap.put("ID", file.getAbsolutePath());
					fileMap.put("PARENT_ID", dir.getAbsolutePath());
					fileMap.put("PICTURE", "file.ico");
					fileMap.put("FILE_NAME", file.getName());
					fileMap.put("FILE_PATH", file.getAbsolutePath());
					fileMap.put("FILE_SIZE", bytes2kb(file.length()));
					fileMap.put("LAST_MODIFIED", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.lastModified())));
					resultList.add(fileMap);
				}
				Collections.sort(resultList, new Comparator<Map<String, String>>() {

					@Override
					public int compare(Map<String, String> o1,
							Map<String, String> o2) {
						return o1.get("FILE_NAME").compareToIgnoreCase(o2.get("FILE_NAME"));
					}
				});
			}
		}
	}
	
	private String bytes2kb(long bytes){
		BigDecimal filesize = new BigDecimal(bytes);
		BigDecimal megabyte = new BigDecimal(1024 * 1024);
		float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
		if(returnValue > 1){
			return returnValue + "MB";
		}
		megabyte = new BigDecimal(1024);
		returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
		return returnValue + "KB";
	}

}
