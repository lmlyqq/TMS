package com.rd.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;



@SuppressWarnings("serial")
public class ExcelUploadServlet extends HttpServlet{

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {			
			request.setCharacterEncoding("utf-8"); 
			
			response.setContentType("text/html;charset=utf-8"); 

			//获得存放文件的物理路径 
			//upload下的某个文件夹 得到当前在线的用户 找到对应的文件夹 
			ServletContext sctx = getServletContext(); 
			//System.out.println("1:"+sctx);
			
			String path = sctx.getRealPath("/upload"); 
			//System.out.println("2:"+path);
			//List<String> list=new ArrayList<String> ();
			String fileName="";
			try { 
				ServletFileUpload upload = new ServletFileUpload();
		        FileItemIterator iter = upload.getItemIterator(request);
//				//每个表单域中数据会封装到一个对应的FileItem对象上 
//				DiskFileItemFactory factory = new DiskFileItemFactory(); 
//				System.out.println("factory:"+factory);
//				//创建解析类的实例 
//				ServletFileUpload sfu = new ServletFileUpload(factory); 
//				System.out.println("sfu:"+sfu);
//				//开始解析 
//				sfu.setFileSizeMax(1024*400); 
//				List<FileItem> items = sfu.parseRequest(request); 
//				System.out.println("3:"+items);
				//区分表单域 
		         while (iter.hasNext()) {
		             
		        	 FileItemStream item = iter.next();	  
				 
					//FileItem item = items.get(i); 
				
					//isFormField为true，表示这不是文件上传表单域 
				
					//System.out.println("4:"+item);
					
					if(!item.isFormField()){ 
		
						//System.out.println(path); 
						//获得文件名 
						fileName= item.getName(); 
						
						//System.out.println("5:"+fileName); 
						//该方法在某些平台(操作系统),会返回路径+文件名 
						fileName = fileName.substring(fileName.lastIndexOf("\\")+1); 

						File filePath = new File(path);   //文件路径不一定存在，不存在就创建新的路径
						if(!filePath.exists()){
							filePath.mkdirs();
						}
						
						File file = new File(path+"\\"+fileName); 
						//File file = new File(path+newFileName);  //查看文件是否存在，不存在创建新文件
						if(!file.exists()){
							file.createNewFile();
						}
						
						byte[] buffer = new byte[4096];
						InputStream input = null;
						OutputStream output = null;
						
						try {
					        input = item.openStream();
					        output = new BufferedOutputStream(new FileOutputStream(file));
					        for (;;) {
					            int n = input.read(buffer);
					            if (n==(-1))
					                break;
					            output.write(buffer, 0, n);
					        }
					    }catch(Exception e ){
					    	
					    }finally{
					    	 if (input!=null) {
					             try {
					                 input.close();
					             }
					             catch (IOException e) {}
					         }
					         if (output!=null) {
					             try {
					                 output.close();
					             }
					             catch (IOException e) {}
					         }
					    }
		
					} 

				} 
			
				File excel=new File(path+"\\"+fileName); 

				InputStream is=new FileInputStream(excel);
				
				Workbook workbook = null;
				
				workbook = WorkbookFactory.create(is);
	            
				Sheet sheet = workbook.getSheetAt(0);
				 
				Row row = sheet.getRow(0);
				
				
				PrintWriter p=response.getWriter();
				p.println("<!doctype html>");
				p.println("<html>");
				p.println("<head>");
				p.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
				p.println("</head>");
				p.println("<body>");
				p.println("<table>");
				for (int i = 0; i <row.getLastCellNum(); i++) {  
		                    
					Cell cell = row.getCell(i);					
					//System.out.println(cell.getRichStringCellValue());
					p.println("<tr><td>");    
					p.println(""+cell.getRichStringCellValue());
					p.println("<td></tr>");
				} 
				p.println("</table>");
                p.println("</body>");
                p.println("</html>");
    			p.flush();
    			p.close();
    			p = null;	
			
			} catch (Exception e) { 			
				e.printStackTrace(); 	
				//System.out.println("888888888888");
			}finally{
				//System.out.println("9999999999999"); 	
			}
	}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doGet(request,response);
		}
}