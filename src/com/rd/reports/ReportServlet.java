package com.rd.reports;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

import com.rd.server.util.LoginContent;

public class ReportServlet extends HttpServlet {
	private boolean isProduction;
	private String logoName;
	private static final long serialVersionUID = -414316755183527346L;
	public static final String PARAMETER_REPORT_TEMPLATE_NAME = "reportModel".intern();
	public static final String REPORT_TEMPLATE_FOLDER = "reportModel".intern();
	public static final String JASPER_FILE_SUFFIX = ".jasper";
	public static final String JRXML_FILE_SUFFIX = ".jrxml";
	public static final String PARAMETER_LOGO_FILENAME = "logoFileName";
	public static final String DEFAULT_LOGO_FOLDER = "images";
	public static final String DEFAULT_LOGO_FILENAME = "sf-logo.jpg";
	public static final String PARAM_PART_MARK = ",";
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String paramValue1 = config.getInitParameter("isProduction");
		String paramValue2 = config.getInitParameter("logoName");
		isProduction =  (paramValue1 != null && "true".equalsIgnoreCase(paramValue1));
		logoName = (paramValue2 == null || "".endsWith(paramValue2.trim())) ? 
					DEFAULT_LOGO_FILENAME : paramValue2;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String encoding = req.getCharacterEncoding();
		if(encoding == null || "".equals(encoding)){
			req.setCharacterEncoding("UTF-8");
		}
		this.handleReport(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}

	@SuppressWarnings("unchecked")
	public void handleReport(HttpServletRequest request,
			HttpServletResponse response){
		ServletContext context = getServletContext();
		String rptTemplate = request.getParameter(PARAMETER_REPORT_TEMPLATE_NAME);
		String subReport = request.getParameter("subReport");//子报表
		Enumeration<String> paramArray = request.getParameterNames();
		String logoFileName = request.getParameter(PARAMETER_LOGO_FILENAME);
		PrintWriter out = null;
		try {
			//out = response.getWriter();
			if(!(subReport == null || "".equals(subReport))){
				// Fill report begin
				String subJasperFileName = context.getRealPath(File.separator
						+ REPORT_TEMPLATE_FOLDER + File.separator + subReport
						+ JASPER_FILE_SUFFIX);
				File subJasperFile = new File(subJasperFileName);
				if (!subJasperFile.exists()) {
					String subJrxmlFileName = context.getRealPath(File.separator
							+ REPORT_TEMPLATE_FOLDER + File.separator + subReport
							+ JRXML_FILE_SUFFIX);
					File subJrxmlFile = new File(subJrxmlFileName);
		
					if (!subJrxmlFile.exists()) {
						throw new Exception("Report template file "
								+ subJrxmlFile.getName() + " doesn't exists!");
					}
					JasperCompileManager.compileReportToFile(subJrxmlFileName);
				}
			}
			String jasperFileName = context.getRealPath(File.separator
					+ REPORT_TEMPLATE_FOLDER + File.separator + rptTemplate
					+ JASPER_FILE_SUFFIX);
			File jasperFile = new File(jasperFileName);
			if(!isProduction && jasperFile.exists()){
				jasperFile.delete();
			}
			if (!jasperFile.exists()) {
				String jrxmlFileName = context.getRealPath(File.separator
						+ REPORT_TEMPLATE_FOLDER + File.separator + rptTemplate
						+ JRXML_FILE_SUFFIX);
				File jrxmlFile = new File(jrxmlFileName);
		
				if (!jrxmlFile.exists()) {
					throw new Exception("Report template file "
							+ jrxmlFile.getName() + " doesn't exists!");
				}
				JasperCompileManager.compileReportToFile(jrxmlFileName);
				
			}
		
			Map<String, Object> rptParams = new HashMap<String, Object>();
			if(!(subReport == null || "".equals(subReport))){
				rptParams.put("SUBREPORT_DIR", context.getRealPath(File.separator+ REPORT_TEMPLATE_FOLDER + File.separator+ subReport+ JASPER_FILE_SUFFIX));
			}
			while (paramArray.hasMoreElements()) {
				String paramName = paramArray.nextElement();
				if (PARAMETER_REPORT_TEMPLATE_NAME.equals(paramName) || 
						PARAMETER_LOGO_FILENAME.equals(paramName)) {
					continue;
				}
				rptParams.put(paramName, getParamValue(request.getParameter(paramName)));
			}
			if(!(logoFileName == null || "".equals(logoFileName.trim()))){
				logoName = logoFileName;
			}
			rptParams.put("logo", context.getRealPath(File.separator+ REPORT_TEMPLATE_FOLDER + File.separator+ DEFAULT_LOGO_FOLDER + File.separator + logoName));
			
			Connection connection = LoginContent.getInstance().getConnection();;
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperFileName, rptParams, connection);
		
			request.getSession().setAttribute(
					BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
					jasperPrint);
			if(!(connection == null || connection.isClosed())){
				connection.close();
			}
			
//			response.sendRedirect(request.getContextPath() + "/servlets/pdf");
			
			printReport(response, jasperPrint);
			
		} catch (Exception e) {
			try {
				out = response.getWriter();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			response.setContentType("text/html");
			response.setCharacterEncoding("iso-8859-1");
			
			out.println("<html>");
			out.println("<head>");
			out.println("<title>错误信息</title>");
			out.println("</head>");
		
			out.println("<body bgcolor=\"white\">");
			out.println("<span><font color=\"red\">错误</font></span>");
			out.println("<pre>");
		
			e.printStackTrace(out);
		
			out.println("</pre>");
		
			out.println("</body>");
			out.println("</html>");
		} 
		
	}
	private void printReport(HttpServletResponse resp, JasperPrint jasperPrint)throws Exception{
		if(jasperPrint == null)
			throw new Exception("报表为空");
		List<JasperPrint> jps = new ArrayList<JasperPrint>();
		jps.add(jasperPrint);

		resp.setContentType("application/pdf");

		JRPdfExporter exporter = new JRPdfExporter(DefaultJasperReportsContext.getInstance());
		exporter.setExporterInput(SimpleExporterInput.getInstance(jps));
	
		OutputStream outputStream = resp.getOutputStream();
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
		try{
			exporter.exportReport();
		}catch (JRException e){
	        throw new ServletException(e);
		}finally{
			if (outputStream != null){
	          try{
	        	  outputStream.close();
	          }catch (IOException localIOException2){
	          }
	        }
	      }
	}
	
	public String getParamValue(String value){
		if(value == null) return value;
		if(value.indexOf(PARAM_PART_MARK) > -1){
			value = (value+PARAM_PART_MARK).replaceAll("(?:([^,\\s]*),{1})", "'$1',");
			return value.substring(0, value.length()-1);
		}
		return value;
	}

}
