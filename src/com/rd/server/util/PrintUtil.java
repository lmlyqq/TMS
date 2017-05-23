package com.rd.server.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.eclipse.core.runtime.IProgressMonitor;

import sh.cn.com.flux.client.actions.common.WMSPrintViewer;

import com.rd.client.common.util.StaticRef;

/**
 * 
 * Project Name:com.flux.tms
 * 
 * @author fanglm
 * 
 * Purpose: 打印报表类工具
 * 
 * Create Time: 2010-8-10 10:40:20
 * 
 * Create Specification:
 * 
 * Modified Time:
 * 
 * Modified by:
 * 
 * Modified Specification:
 * 
 * Version: 1.0
 */
@SuppressWarnings("deprecation")
public class PrintUtil {
	
	public static final String PRINT_OK = "1"; // fanglm: 打印成功

	public static final String PRINT_ERR = "0"; // fanglm：打印失败


	/**
	 * 功能： 直接在默认的打印机上打印报表（自动打印时用）
	 * @author fanglm	
	 * Create Time: 2010-8-10 13:14:37	
	 * @param RM
	 * @param parameters
	 * @param PartList
	 * @param monitor
	 * @param printNum
	 * @return	
	 * Version: 1.0
	 */
    @SuppressWarnings("unchecked")
	public static ArrayList printActPS2DefaultPrinter(String RM,HashMap parameters,ArrayList PartList,
    		IProgressMonitor monitor,int printNum) {
    	//
    	return printActPS(RM, parameters, PartList, monitor, printNum, true, false);
    }
    
    /**
     * 功能： 打印/预览打印报表
     * @author fanglm
     * 2010-8-10
     * 
     * @param RM 报表模板名称
     * @param parameters 参数表
     * @param PartList 明细记录
     * @param monitor 需要更新进度条时使用，否则传入null
     * @param printNum 打印份数(直接打印时有效)
     * @param isOpendiag 是否弹出打印对话框(直接打印时有效)
     * @param isView 是否预览
     * @return 第一个元素说明成功/失败(1 成功 0 失败),如果失败，第二个元素说明失败原因(字符串)
     */
    @SuppressWarnings("unchecked")
	public static ArrayList printActPS(String RM,HashMap parameters,ArrayList PartList,
    		IProgressMonitor monitor,int printNum,boolean isOpendiag,boolean isView){
    	//
    	try{
            
            File jasperFile = getReportFile(RM);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
            
            JRBeanCollectionDataSource jrrsds = new JRBeanCollectionDataSource(PartList);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrrsds);
            
            
            JasperExportManager.exportReportToPdfFile(jasperPrint, "F:\\fanglm.pdf");
            if (RM != null) {
//            	jasperPrint.setOrientation(JRReport.ORIENTATION_PORTRAIT);
            }
                
            if(isView){
//            	MessageInfoBox.updateProgressDialog(monitor,ConstantLogin.getValueByKey("wms.reports.print.ViewPrint","正在预览 ..."),1);
            	
            	try{
            		WMSPrintViewer pv = new WMSPrintViewer(jasperPrint,false);
            		pv.setVisible(true);
            		pv.setZoomRatio(1);
            		pv.setTitle("RENDA TMS PrintViewer");
            	
            	}catch(Exception et){
            		et.printStackTrace();
            	}
            }else{
            	
            	for(int i = 0; i < printNum; i++){
                    JasperPrintManager.printPages(jasperPrint, 0,jasperPrint.getPages().size() - 1,isOpendiag);
            	}
            }
    		ArrayList reList = new ArrayList();
    		reList.add("1");
    		return reList;
    	}catch(Exception err){
    		//System.out.println("打印/预览报表时遇到异常：" + err.getMessage());
    		err.printStackTrace();
    		ArrayList reList = new ArrayList();
    		reList.add(err.getMessage());
    		return reList;
    	}
    }
    
    /**
     * Purpose: 获取打印模板、数据源配置文件
     * 
     * @author fanglm
    
     * Create Time: 2010-8-10 16:03:38
    
     * @param RM
     * @return
    
     * Version: 1.0
     */
    private static File getReportFile(String RM) throws Exception {
        String FileName = null;
        File jasperFile = null;
        FileName =RM;
        jasperFile = new File(FileName);
        
        if (jasperFile.exists()) {
        	return jasperFile;
        }else{
        	jasperFile.createNewFile();
        }
        
        if(jasperFile==null || !jasperFile.exists()){
        	//System.out.println("Not Found Report Model File<" + RM + ">.");
        }
        return jasperFile;
    }
    
    /**
     * 功能： 预览打印报表 在同一个打印预览窗口中预览多份报表 要求这多份报表的模板是同一个模板
     * 
     * fanglm 2010-08-10
     * @param RM 报表模板名称
     * @param parametersList 参数表(列表)
     * @param PartListList 明细记录(列表)
     * @param monitor 需要更新进度条时使用，否则传入null
     * @return 第一个元素说明成功/失败(1 成功 0 失败),如果失败，第二个元素说明失败原因(字符串)
     */
	@SuppressWarnings({ "unchecked"})
	public static String viewPrintActPS(String RM,ArrayList parametersList,ArrayList PartListList,String fileName,HttpServletResponse response){
    	
        try{
            ArrayList jasperPrintList = new ArrayList();
            
            File jasperFile = getReportFile(RM);
            
            File file = new File(fileName);
            if(!file.exists()){
            	file.createNewFile();
            }
            
            for(int i = 0; i < parametersList.size(); i++){
                HashMap parameters = (HashMap)parametersList.get(i);
                ArrayList PartList = (ArrayList)PartListList.get(i);

                JRBeanCollectionDataSource jrrsds = new JRBeanCollectionDataSource(PartList);
                
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrrsds);
                
                if (RM != null){
//                	jasperPrint.setOrientation(JRReport.ORIENTATION_PORTRAIT);
                }
                
                jasperPrintList.add(jasperPrint);
                
            }
            
            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName);

            exporter.exportReport();
            
            /*net.sf.jasperreports.engine.export.JRHtmlExporter exporter2 = new net.sf.jasperreports.engine.export.JRHtmlExporter();
            exporter2.setParameter(JRHtmlExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
            exporter2.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, true);
            exporter2.setParameter(JRHtmlExporterParameter.IMAGES_URI, "/user/wpsadmin/LoadmentPrint.html_files/");
            exporter2.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporter2.setParameter(JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
            exporter2.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName.replaceAll(".pdf", ".html"));
            exporter2.exportReport();*/
             
            System.out.println("正在预览 ...");
             
//            ArrayList reList = new ArrayList();
//            reList.add("1");
            return StaticRef.SUCCESS_CODE;
        }catch(Exception err){
        	//System.out.println("打印/预览报表时遇到异常：" + err.getMessage());
    		err.printStackTrace();
//    		ArrayList reList = new ArrayList();
//    		reList.add(err.getMessage());
    		return StaticRef.FAILURE_CODE + err.getMessage();
        }
    }
    /**
     * Purpose:
     * @author fanglm
    
     * Create Time: 2010-08-10 12:51:20
    
     * @param RM
     * @param parametersList
     * @param PartListList
     * @param monitor
     * @param isOpendiag
     * @param orientation  设置打印方向
     * @return
    
     * Version: 1.0
     */
    @SuppressWarnings("unchecked")
	public static ArrayList printActDoPS(String RM,ArrayList parametersList,ArrayList PartListList,
    		boolean isOpendiag,boolean orientation){
    	try{
            File jasperFile = getReportFile(RM);
            
            for(int i = 0; i < parametersList.size(); i++){
                
                HashMap parameters = (HashMap)parametersList.get(i);
                ArrayList PartList = (ArrayList)PartListList.get(i);
                
                JRBeanCollectionDataSource jrrsds = new JRBeanCollectionDataSource(PartList);
                
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrrsds);
                
                if (RM != null){
                	if(orientation){//纵向
//                		jasperPrint.setOrientation(JRReport.ORIENTATION_PORTRAIT);
                	}else{//横向
//                		jasperPrint.setOrientation(JRReport.ORIENTATION_LANDSCAPE);
                	}
                }
//                MessageInfoBox.updateProgressDialog(monitor,ConstantLogin.getValueByKey(
//                		"wms.reports.print.Printing","正在打印 ..."),1);
                JasperPrintManager.printPages(jasperPrint, 0,jasperPrint.getPages().size() - 1,isOpendiag);
            }

            ArrayList reList = new ArrayList();
            reList.add("1");
            reList.add("OK!");
            return reList;
        }catch(Exception err){
        	err.printStackTrace();
			//System.out.println(err.getMessage());
			ArrayList reList = new ArrayList();
            reList.add(err.getMessage());
            return reList;
        }
    }
    
}
