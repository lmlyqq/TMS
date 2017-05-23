package com.rd.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;



public class ExcelTitleServlet extends HttpServlet{
		private static final long serialVersionUID = 1L;
	       
	   
	    public ExcelTitleServlet() {
	        super();
	    }
	    
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			request.setCharacterEncoding("utf-8"); 
			
			response.setContentType("text/html;charset=utf-8"); 
			
			String fileName=request.getParameter("fileName");
			
			String result="";
			
			
			if(fileName!=null&&!fileName.equals("")){
				
				ServletContext sctx = getServletContext(); 
				
				String path = sctx.getRealPath("/upload"); 

			    File excel=new File(path+"\\"+fileName); 

				InputStream is=new FileInputStream(excel);
				
				Workbook workbook = null;
				
				try {
					workbook = WorkbookFactory.create(is);
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				}
	            
				Sheet sheet = workbook.getSheetAt(0);
				 
				Row row = sheet.getRow(0);
				
				for (int i = 0; i <row.getLastCellNum(); i++) {  
		                    
					Cell cell = row.getCell(i);		
					
					//System.out.println(cell.getRichStringCellValue());
	
					result=result+","+cell.getRichStringCellValue();
				}  
				
			}
			
			PrintWriter p=response.getWriter();
               
			p.println(result);
    		
			p.flush();
    		
			p.close();
    		
			p = null;
					
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doGet(request,response);
		}
		
}