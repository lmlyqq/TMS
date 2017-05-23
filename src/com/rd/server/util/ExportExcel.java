package com.rd.server.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import com.rd.client.util.ObjUtil;


/**

 * 利用开源组件POI3.0.2动态导出EXCEL文档

 * 转载时请保留以下信息，注明出处！

 * @author fanglm

 * @version v1.0

 * @param <T> 应用泛型，代表任意一个符合javabean风格的类

 * 注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()

 * byte[]表jpg格式的图片数据

 */

@SuppressWarnings("deprecation")
public class ExportExcel<T> {
 

   public void exportExcel(String headers, Collection<T> dataset,

         OutputStream out) {

      exportExcel("sheet1", headers, dataset, out, "yyyy-MM-dd");

   }
 

   public void exportExcel(String headers, Collection<T> dataset,

         OutputStream out, String pattern) {

      exportExcel("sheet1", headers, dataset, out, pattern);

   }

 

   /**

    * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上

    *

    * @param title

    *            表格标题名

    * @param headers

    *            表格属性列名数组

    * @param dataset

    *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的

    *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)

    * @param out

    *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中

    * @param pattern

    *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"

    */

   @SuppressWarnings("unchecked")

   public void exportExcel(String title, String headers,

         Collection<T> dataset, OutputStream out, String pattern) {

      // 声明一个工作薄

      HSSFWorkbook workbook = new HSSFWorkbook();

      // 生成一个表格

      HSSFSheet sheet = workbook.createSheet(title);

      // 设置表格默认列宽度为15个字节

      sheet.setDefaultColumnWidth(15);

      // 生成一个样式

      HSSFCellStyle style = workbook.createCellStyle();

      // 设置这些样式

      style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);

      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

      style.setBorderBottom(HSSFCellStyle.BORDER_THIN);

      style.setBorderLeft(HSSFCellStyle.BORDER_THIN);

      style.setBorderRight(HSSFCellStyle.BORDER_THIN);

      style.setBorderTop(HSSFCellStyle.BORDER_THIN);

      style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

      // 生成一个字体

      HSSFFont font = workbook.createFont();

      font.setColor(HSSFColor.VIOLET.index);

      font.setFontHeightInPoints((short) 12);

      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

      // 把字体应用到当前的样式

      style.setFont(font);

      // 生成并设置另一个样式

      HSSFCellStyle style2 = workbook.createCellStyle();

      style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);

      style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

      style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);

      style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);

      style2.setBorderRight(HSSFCellStyle.BORDER_THIN);

      style2.setBorderTop(HSSFCellStyle.BORDER_THIN);

      style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

      style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

      // 生成另一个字体

      HSSFFont font2 = workbook.createFont();

      font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

      // 把字体应用到当前的样式

      style2.setFont(font2);

      // 声明一个画图的顶级管理器

      HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

      // 定义注释的大小和位置,详见文档

      HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));

      // 设置注释内容

      comment.setString(new HSSFRichTextString("上海任大科技"));

      // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.

      comment.setAuthor("fanglm");

 

      //产生表格标题行

      HSSFRow row = sheet.createRow(0);
      String[] header = headers.split(",");

      for (int i = 0; i < header.length; i++) {

         HSSFCell cell = row.createCell(i);

         cell.setCellStyle(style);

         HSSFRichTextString text = new HSSFRichTextString(header[i]);

         cell.setCellValue(text);

      }

      //遍历集合数据，产生数据行

      Iterator<T> it = dataset.iterator();

      int index = 0;

//      for (Iterator i = dataset.iterator(); i.hasNext(); ) {  
//          Map record = (Map)i.next();  
//          String dateField = record.get("START_DATE").toString();  
//          if (dateField != null) {  
//              record.put("new_date", dateField);  
//          }  
//
//      }  
      while (it.hasNext()) {

         index++;

         row = sheet.createRow(index);

         T t = (T) it.next();

         //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值

         Field[] fields = t.getClass().getDeclaredFields();

         for (int i = 0; i < fields.length; i++) {

            HSSFCell cell = row.createCell(i);

            cell.setCellStyle(style2);

            Field field = fields[i];

            String fieldName = field.getName();

            String getMethodName = "get"

                   + fieldName.substring(0, 1).toUpperCase()

                   + fieldName.substring(1);

            try {

                Class tCls = t.getClass();

                Method getMethod = tCls.getMethod(getMethodName,

                      new Class[] {});

                Object value = getMethod.invoke(t, new Object[] {});

                //判断值的类型后进行强制类型转换

                String textValue = null;

                if (value instanceof Boolean) {

                   boolean bValue = (Boolean) value;

                   textValue = "是";

                   if (!bValue) {

                      textValue ="否";

                   }

                } else if (value instanceof Date) {

                   Date date = (Date) value;

                   SimpleDateFormat sdf = new SimpleDateFormat(pattern);

                    textValue = sdf.format(date);

                }  else if (value instanceof byte[]) {

                   // 有图片时，设置行高为60px;

                   row.setHeightInPoints(60);

                   // 设置图片所在列宽度为80px,注意这里单位的一个换算

                   sheet.setColumnWidth(i, (short) (35.7 * 80));

                   // sheet.autoSizeColumn(i);

                   byte[] bsValue = (byte[]) value;

                   HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,

                         1023, 255, (short) 6, index, (short) 6, index);

                   anchor.setAnchorType(2);

                   patriarch.createPicture(anchor, workbook.addPicture(

                         bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));

                } else{

                   //其它数据类型都当作字符串简单处理

                   textValue = value.toString();

                }

                //如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成

                if(textValue!=null){

                   Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");  

                   Matcher matcher = p.matcher(textValue);

                   if(matcher.matches()){

                      //是数字当作double处理

                      cell.setCellValue(Double.parseDouble(textValue));

                   }else{

                      HSSFRichTextString richString = new HSSFRichTextString(textValue);

                      HSSFFont font3 = workbook.createFont();

                      font3.setColor(HSSFColor.BLUE.index);

                      richString.applyFont(font3);

                      cell.setCellValue(richString);

                   }

                }

            } catch (SecurityException e) {

                e.printStackTrace();

            } catch (NoSuchMethodException e) {

                e.printStackTrace();

            } catch (IllegalArgumentException e) {

                e.printStackTrace();

            } catch (IllegalAccessException e) {

                e.printStackTrace();

            } catch (InvocationTargetException e) {

                e.printStackTrace();

            } finally {

            }

         }

      }

      try {

         workbook.write(out);

      } catch (IOException e) {

         e.printStackTrace();

      }

   }
   
   @SuppressWarnings("unchecked")
public HSSFWorkbook exportExcel(String title,String headers, String fieldNames,

	         List dataset, OutputStream out, String pattern) {

	      // 声明一个工作薄

	      HSSFWorkbook workbook = new HSSFWorkbook();

	      // 生成一个表格

	      HSSFSheet sheet = workbook.createSheet(title);

	      // 设置表格默认列宽度为15个字节

	      sheet.setDefaultColumnWidth(15);

	      // 生成一个样式

	      HSSFCellStyle style = workbook.createCellStyle();

	      // 设置这些样式

	      style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);

	      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

	      style.setBorderBottom(HSSFCellStyle.BORDER_THIN);

	      style.setBorderLeft(HSSFCellStyle.BORDER_THIN);

	      style.setBorderRight(HSSFCellStyle.BORDER_THIN);

	      style.setBorderTop(HSSFCellStyle.BORDER_THIN);

	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

	      // 生成一个字体

	      HSSFFont font = workbook.createFont();

	      font.setColor(HSSFColor.VIOLET.index);

	      font.setFontHeightInPoints((short) 12);

	      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

	      // 把字体应用到当前的样式

	      style.setFont(font);

	      // 生成并设置另一个样式

	      HSSFCellStyle style2 = workbook.createCellStyle();

	      style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);

	      style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

	      style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);

	      style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);

	      style2.setBorderRight(HSSFCellStyle.BORDER_THIN);

	      style2.setBorderTop(HSSFCellStyle.BORDER_THIN);

	      style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

	      style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

	      // 生成另一个字体

	      HSSFFont font2 = workbook.createFont();

	      font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

	      // 把字体应用到当前的样式

	      style2.setFont(font2);

	      // 声明一个画图的顶级管理器

	      HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

	      // 定义注释的大小和位置,详见文档

	      HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));

	      // 设置注释内容

	      comment.setString(new HSSFRichTextString("上海任大科技"));

	      // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.

	      comment.setAuthor("fanglm");

	 

	      //产生表格标题行

	      HSSFRow row = sheet.createRow(0);
	      
	      String[]  header = headers.split(",");

	      for (int i = 0; i < header.length; i++) {

	         HSSFCell cell = row.createCell(i);

	         cell.setCellStyle(style);

	         HSSFRichTextString text = new HSSFRichTextString(header[i]);

	         cell.setCellValue(text);

	      }

	      //遍历集合数据，产生数据行

	      int index = 0;
	      
	      String[] fooNames = fieldNames.split(",");
//	      Iterator i = dataset.iterator();

	      for (Iterator i = dataset.iterator(); i.hasNext(); ) {  
//	      for(int k =0;k<=2000;k++){
	    	  
	    	  index++;

	          row = sheet.createRow(index);
	    	  
	          Map record = (Map)i.next();
	          for(int j = 0;j<fooNames.length;j++){
	        	  
	        	  HSSFCell cell = row.createCell(j);

	              cell.setCellStyle(style2);
	              
	              try{
	  	                Object value = record.get(fooNames[j]);

	  	                //判断值的类型后进行强制类型转换

	  	                String textValue = null;

	  	                if (value instanceof Boolean) {

	  	                   boolean bValue = (Boolean) value;

	  	                   textValue = "是";

	  	                   if (!bValue) {

	  	                      textValue ="否";

	  	                   }

	  	                } else if (value instanceof Date) {

	  	                   Date date = (Date) value;

	  	                   SimpleDateFormat sdf = new SimpleDateFormat(pattern);

	  	                    textValue = sdf.format(date);

	  	                }else{

	  	                   //其它数据类型都当作字符串简单处理
	  	                	if(value == null){
	  	                		textValue = "";
	  	                	}else{
	  	                		textValue = value.toString();
	  	                	}
	  	                }

	  	                //如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成

	  	                if(textValue!=null){
	  	                	/**
	  	                   Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");  

	  	                   Matcher matcher = p.matcher(textValue);

	  	                   if(matcher.matches()){

	  	                      //是数字当作double处理

	  	                      cell.setCellValue(Double.parseDouble(textValue));

	  	                   }else{**/

	  	                      HSSFRichTextString richString = new HSSFRichTextString(textValue);

//	  	                      HSSFFont font3 = workbook.createFont();

//	  	                      font3.setColor(HSSFColor.BLUE.index);
//
//	  	                      richString.applyFont(font3);

	  	                      cell.setCellValue(richString);

//	  	                   }

	  	                }

	  	            } catch (SecurityException e) {

	  	                e.printStackTrace();

	  	            } catch (IllegalArgumentException e) {

	  	                e.printStackTrace();

	  	            }
	  	         }
	      }  
	      
	      return workbook;
	     
	   }
  @SuppressWarnings("unchecked")
public HSSFWorkbook appendData(String fieldNames,List dataset,int index,HSSFWorkbook book){
	//遍历集合数据，产生数据行

	  HSSFRow row  = null;
      
      String[] fooNames = fieldNames.split(",");

      for (Iterator i = dataset.iterator(); i.hasNext(); ) {  
    	  
    	  index++;

          row = book.getSheet("sheet1").createRow(index);
    	  
          Map record = (Map)i.next();
          for(int j = 0;j<fooNames.length;j++){
        	  
        	  HSSFCell cell = row.createCell(j);
              cell.setCellStyle(book.getCellStyleAt((short)2));
              try{
  	                Object value = record.get(fooNames[j]);

  	                //判断值的类型后进行强制类型转换

  	                String textValue = null;

  	                if (value instanceof Boolean) {

  	                   boolean bValue = (Boolean) value;

  	                   textValue = "是";

  	                   if (!bValue) {

  	                      textValue ="否";

  	                   }

  	                }else{

  	                   //其它数据类型都当作字符串简单处理
  	                	if(value == null){
  	                		textValue = "";
  	                	}else{
  	                		textValue = value.toString();
  	                	}
  	                }

  	                //如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成

  	                if(textValue!=null){

  	                      HSSFRichTextString richString = new HSSFRichTextString(textValue);

  	                      cell.setCellValue(richString);

  	                }

  	            } catch (SecurityException e) {

  	                e.printStackTrace();

  	            } catch (IllegalArgumentException e) {

  	                e.printStackTrace();

  	            }
  	         }
          
      }  
      return book;
  }
  
  public void writeFile(HSSFWorkbook workbook,File file){
	  try {
    	  
	    	 FileOutputStream outP = new FileOutputStream(file);
	         workbook.write(outP);
	
	      } catch (IOException e) {

	         e.printStackTrace();

	      }

  }
   
   /**
    * ResultSet 存储过程导出excel方法
    * @create time 2011-7-13 16:00
    * @author fanglm
    * @param title
    * @param headers
    * @param fieldNames
    * @param dataset
    * @param out
    * @param pattern
    * @param file
    */
   public void exportExcel(String title,String headers, String fieldNames,

   	         ResultSet dataset, OutputStream out, String pattern,File file) {

   	      // 声明一个工作薄

   	      HSSFWorkbook workbook = new HSSFWorkbook();

   	      // 生成一个表格

   	      HSSFSheet sheet = workbook.createSheet(title);

   	      // 设置表格默认列宽度为15个字节

   	      sheet.setDefaultColumnWidth(15);

   	      // 生成一个样式

   	      HSSFCellStyle style = workbook.createCellStyle();

   	      // 设置这些样式

   	      style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);

   	      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

   	      style.setBorderBottom(HSSFCellStyle.BORDER_THIN);

   	      style.setBorderLeft(HSSFCellStyle.BORDER_THIN);

   	      style.setBorderRight(HSSFCellStyle.BORDER_THIN);

   	      style.setBorderTop(HSSFCellStyle.BORDER_THIN);

   	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

   	      // 生成一个字体

   	      HSSFFont font = workbook.createFont();

   	      font.setColor(HSSFColor.VIOLET.index);

   	      font.setFontHeightInPoints((short) 12);

   	      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

   	      // 把字体应用到当前的样式

   	      style.setFont(font);

   	      // 生成并设置另一个样式

   	      HSSFCellStyle style2 = workbook.createCellStyle();

   	      style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);

   	      style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

   	      style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);

   	      style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);

   	      style2.setBorderRight(HSSFCellStyle.BORDER_THIN);

   	      style2.setBorderTop(HSSFCellStyle.BORDER_THIN);

   	      style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

   	      style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

   	      // 生成另一个字体

   	      HSSFFont font2 = workbook.createFont();

   	      font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

   	      // 把字体应用到当前的样式

   	      style2.setFont(font2);

   	      // 声明一个画图的顶级管理器

   	      HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

   	      // 定义注释的大小和位置,详见文档

   	      HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));

   	      // 设置注释内容

   	      comment.setString(new HSSFRichTextString("上海任大科技"));

   	      // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.

   	      comment.setAuthor("fanglm");

   	 

   	      //产生表格标题行

   	      HSSFRow row = sheet.createRow(0);
   	      
   	      String[]  header = headers.split(",");

   	      for (int i = 0; i < header.length; i++) {

   	         HSSFCell cell = row.createCell(i);

   	         cell.setCellStyle(style);

   	         HSSFRichTextString text = new HSSFRichTextString(header[i]);

   	         cell.setCellValue(text);

   	      }

   	      //遍历集合数据，产生数据行

   	      int index = 0;
   	      
   	      String[] fooNames = fieldNames.split(",");
   	      try{
	   	      while(dataset.next()){
	   	    	  index++;
	   	          row = sheet.createRow(index);
	   	          for(int j = 0;j<fooNames.length;j++){
	   	        	  
	        	    HSSFCell cell = row.createCell(j);
	
	                cell.setCellStyle(style2);
	                Object value = dataset.getObject(fooNames[j]);
	
	                //判断值的类型后进行强制类型转换
	
	                String textValue = null;
	
	                if (value instanceof Boolean) {
	
	                   boolean bValue = (Boolean) value;
	
	                   textValue = "是";
	
	                   if (!bValue) {
	
	                      textValue ="否";
	
	                   }
	
	                } else if (value instanceof Date) {
	
	                   Date date = (Date) value;
	
	                   SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	
	                    textValue = sdf.format(date);
	
	                }else{
	
	                   //其它数据类型都当作字符串简单处理
	                	if(value == null){
	                		textValue = "";
	                	}else{
	                		textValue = value.toString();
	                	}
	                }
	
	                //如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
	
	                if(textValue!=null){
	                      HSSFRichTextString richString = new HSSFRichTextString(textValue);
	                      cell.setCellValue(richString);
	                }
	
	   	  	           
	   	          }  
	   	      }
   	    	 FileOutputStream outP = new FileOutputStream(file);
   	         workbook.write(outP);
     	
   	      } catch (Exception e) {

   	         e.printStackTrace();

   	      }

   	   }
   
   @SuppressWarnings("unchecked")
   public HSSFWorkbook sfOrderExportExcel(String title,String headers, String fieldNames,

	         List dataset, OutputStream out, String pattern) {
	   HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("sheet1");
		
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 8));
		sheet.addMergedRegion(new CellRangeAddress(4, 4, 3, 5));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 10, 10));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 11, 11));
		sheet.addMergedRegion(new CellRangeAddress(4, 5, 10, 10));
		sheet.addMergedRegion(new CellRangeAddress(4, 5, 11, 11));
		
		sheet.addMergedRegion(new CellRangeAddress(9, 9, 1, 2));
		sheet.addMergedRegion(new CellRangeAddress(9, 9, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(9, 9, 5, 6));
		sheet.addMergedRegion(new CellRangeAddress(9, 9, 9, 10));
		
		
		sheet.setColumnWidth(11,"需要返还托盘数量".getBytes().length*256);
		//sheet.set
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(1);
	
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		HSSFCell cell = row1.createCell(3);
		// 设置单元格内容
		cell.setCellValue("物流订单（无锡工厂出发）");
		
		HSSFCellStyle cellStyle=wb.createCellStyle();  
	
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
		//cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 24);
		font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		//font.setCharSet(1);
		// 把字体 应用到当前样式
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
	   
		Map map=(Map)dataset.get(0);
		
		HSSFCellStyle cellsty=wb.createCellStyle();  
		HSSFFont fontred=wb.createFont();
		fontred.setColor(HSSFColor.RED.index);
		cellsty.setFont(fontred);
		
		
		
		HSSFRow row2 = sheet.createRow(4);
		row2.createCell(2).setCellValue("分配时间");
		HSSFCell fpsjCell=row2.createCell(3);
		fpsjCell.setCellValue(ObjUtil.ifNull(map.get("FROM_LOAD_TIME")," ").toString());
		fpsjCell.setCellStyle(cellsty);
		
		row2.createCell(6).setCellValue("品项");		
		HSSFCell pmCell=row2.createCell(7);
		pmCell.setCellValue(ObjUtil.ifNull(map.get("SKU_NAME")," ").toString());
		pmCell.setCellStyle(cellsty);
		
		
		
		HSSFRow row3 = sheet.createRow(2);
		HSSFCell cellOrder = row3.createCell(10);
		HSSFRow row4 = sheet.createRow(3);
		HSSFCell cellOrder1 = row4.createCell(10);
		cellOrder.setCellValue("订单号");
		
		HSSFCellStyle cellStyle1=wb.createCellStyle();  		
		cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle1.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle1.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle1.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyle1.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short)14);
		font1.setColor(HSSFColor.BLACK.index);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font1.setFontName("宋体");
		cellStyle1.setFont(font1);
		
		
		HSSFCellStyle cellStyle2=wb.createCellStyle();  		
		cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle2.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle2.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle2.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyle2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short)14);
		font2.setColor(HSSFColor.RED.index);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font2.setFontName("宋体");
		cellStyle2.setFont(font2);
		
		
		cellOrder.setCellStyle(cellStyle1);
		cellOrder1.setCellStyle(cellStyle1);
		

		HSSFCell cellOrderData = row3.createCell(11);
		cellOrderData.setCellValue(ObjUtil.ifNull(map.get("CUSTOM_ODR_NO")," ").toString());
		cellOrderData.setCellStyle(cellStyle2);
		
		HSSFCell cellOrderData1 = row4.createCell(11);
		cellOrderData1.setCellStyle(cellStyle2);
		
		HSSFCell cellDate = row2.createCell(10);
		HSSFRow row5=sheet.createRow(5);
		HSSFCell cellDate1 = row5.createCell(10);
		cellDate.setCellValue("出货日期");
		cellDate.setCellStyle(cellStyle1);
		cellDate1.setCellStyle(cellStyle1);
		
		
		
		HSSFCell cellDateD = row2.createCell(11);
		HSSFCell cellDateD1 = row5.createCell(11);
		cellDateD.setCellValue(ObjUtil.ifNull(map.get("PRE_LOAD_TIME")," ").toString());
		cellDateD.setCellStyle(cellStyle2);
		cellDateD1.setCellStyle(cellStyle2);
		
		sheet.addMergedRegion(new CellRangeAddress(8, 8, 1, 11));
		HSSFRow row6=sheet.createRow(8);
		HSSFCell cellTitle=row6.createCell(1);
		cellTitle.setCellValue("直达");
		
		HSSFCellStyle cellStylet=wb.createCellStyle();  
		cellStylet.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStylet.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStylet.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStylet.setRightBorderColor(HSSFColor.BLACK.index);
		cellStylet.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		cellStylet.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		cellStylet.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		cellStylet.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		
		HSSFFont fontt = wb.createFont();
		fontt.setFontHeightInPoints((short) 12);
		fontt.setColor(HSSFColor.BLACK.index);
		fontt.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fontt.setFontName("宋体");
		cellStylet.setFont(fontt);
		cellTitle.setCellStyle(cellStylet);
		
		for(int i=2;i<12;i++){
			HSSFCell cellzz = row6.createCell(i);			
			
			HSSFCellStyle cellStylez=wb.createCellStyle();  
			cellStylez.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStylez.setRightBorderColor(HSSFColor.BLACK.index);
			cellStylez.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			cellStylez.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			cellStylez.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			cellStylez.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			
			cellzz.setCellStyle(cellStylez);
			
		}
		
		
		for(int i=0;i<dataset.size();i++){
			
			sheet.addMergedRegion(new CellRangeAddress(10+i, 10+i, 1, 2));
			//sheet.addMergedRegion(new CellRangeAddress(10+i, 10+i, 3, 4));
			//sheet.addMergedRegion(new CellRangeAddress(10+i, 10+i, 5, 6));
			sheet.addMergedRegion(new CellRangeAddress(10+i, 10+i, 9, 10));
			
		}
		
		HSSFRow row7=sheet.createRow(9);
		HSSFCell celladd=row7.createCell(1);
		HSSFCell celladd1=row7.createCell(2);
		celladd.setCellValue("目的地");
		HSSFCellStyle cellst=wb.createCellStyle();
		
		cellst.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellst.setRightBorderColor(HSSFColor.BLACK.index);
		cellst.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		cellst.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		cellst.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		cellst.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		
		celladd.setCellStyle(cellst);
		celladd1.setCellStyle(cellst);
		
		HSSFCell cellnum=row7.createCell(3);
		HSSFCell cellnum1=row7.createCell(4);
		cellnum.setCellValue("出货量");
		cellnum.setCellStyle(cellst);
		cellnum1.setCellStyle(cellst);
		
		
		HSSFCell cellcha=row7.createCell(5);
		HSSFCell cellcha1=row7.createCell(6);
		cellcha.setCellValue("修改为");
		cellcha.setCellStyle(cellst);
		cellcha1.setCellStyle(cellst);
		
		HSSFCell cellout=row7.createCell(7);
		cellout.setCellValue("到货日期");
		cellout.setCellStyle(cellst);
		
		HSSFCell cellcar=row7.createCell(8);
		cellcar.setCellValue("车辆安排");
		cellcar.setCellStyle(cellst);
		
		
		HSSFCell cellnote=row7.createCell(9);
		HSSFCell cellnote1=row7.createCell(10);
		cellnote.setCellValue("备注");
		cellnote.setCellStyle(cellst);
		cellnote1.setCellStyle(cellst);
		
		HSSFCell cellback=row7.createCell(11);
		cellback.setCellValue("需要返还托盘数量");
		cellback.setCellStyle(cellst);
		

		HSSFCellStyle cellstd=wb.createCellStyle();
		
		cellstd.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellstd.setRightBorderColor(HSSFColor.BLACK.index);
		cellstd.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		cellstd.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		cellstd.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		cellstd.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		
		HSSFFont fontd = wb.createFont();
		fontd.setFontHeightInPoints((short) 12);
		fontd.setColor(HSSFColor.RED.index);
		fontd.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fontd.setFontName("宋体");
		cellstd.setFont(fontd);
		
		for(int i=0;i<dataset.size();i++){
			Map map1=(Map)dataset.get(i);
			
			HSSFRow rowdate=sheet.createRow(10+i);
			HSSFCell cella=rowdate.createCell(1);
			HSSFCell cellb=rowdate.createCell(2);
			cella.setCellValue(ObjUtil.ifNull(map1.get("UNLOAD_NAME")," ").toString());
			cella.setCellStyle(cellstd);
			cellb.setCellStyle(cellstd);
			
			HSSFCell cellc=rowdate.createCell(3);
			HSSFCell celld=rowdate.createCell(4);
			cellc.setCellValue(ObjUtil.ifNull(map1.get("QNTY")," ").toString());
			celld.setCellValue(ObjUtil.ifNull(map1.get("QNTY_EACH")," ")+"板".toString());
			
			HSSFCellStyle cellstc=wb.createCellStyle();	
			cellstc.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellstc.setRightBorderColor(HSSFColor.BLACK.index);
			cellstc.setBorderRight(HSSFCellStyle.BORDER_DOUBLE);
			cellstc.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
			cellstc.setBorderLeft(HSSFCellStyle.BORDER_DOUBLE);
			cellstc.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
			
			HSSFFont fontstc = wb.createFont();
			fontstc.setFontHeightInPoints((short) 12);
			fontstc.setColor(HSSFColor.RED.index);
			fontstc.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			fontstc.setFontName("宋体");
			cellstc.setFont(fontd);
			
			cellc.setCellStyle(cellstc);
			celld.setCellStyle(cellstc);
			
			HSSFCell celle=rowdate.createCell(5);
			HSSFCell cellf=rowdate.createCell(6);
			cellf.setCellValue("板");
			
			HSSFCellStyle cellstf=wb.createCellStyle();	
			cellstf.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			cellstf.setRightBorderColor(HSSFColor.BLACK.index);
			cellstf.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			cellstf.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			cellstf.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			cellstf.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);	
			
			celle.setCellStyle(cellstc);
			cellf.setCellStyle(cellstf);
			
			HSSFCell cellg=rowdate.createCell(7);
			cellg.setCellValue(ObjUtil.ifNull(map1.get("PRE_UNLOAD_TIME")," ").toString());
			cellg.setCellStyle(cellstd);		
			HSSFCell cellh=rowdate.createCell(8);
			cellh.setCellValue("低糖与原味同车*1");
			cellh.setCellStyle(cellst);
			
			
			HSSFCell celli=rowdate.createCell(9);
			HSSFCell cellj=rowdate.createCell(10);
			celli.setCellValue(ObjUtil.ifNull(map1.get("NOTES")," ").toString());
			celli.setCellStyle(cellstd);
			cellj.setCellStyle(cellst);
			
			HSSFCell cellk=rowdate.createCell(11);
			cellk.setCellValue("0.00000000");
			cellk.setCellStyle(cellst);						
		}
	   return wb;	   
   }  
   
   
   
   @SuppressWarnings({"unchecked" })
   public HSSFWorkbook RecAdjBillExportExcel(String title,String headers, String fieldNames,

	         List dataset, OutputStream out, String pattern) {
	   HSSFWorkbook wb = new HSSFWorkbook();
	   
	   
	   if(dataset!=null&&dataset.size()>0){
			// 建立新的sheet对象（excel的表单）
		   Map map=(Map)dataset.get(0);
		   HSSFSheet sheet = wb.createSheet("sheet1");
		   sheet.setColumnWidth(0,(short)2800);
		   sheet.setColumnWidth((short)1, (short)6800);
		   sheet.setColumnWidth((short)2, (short)6400);
		   sheet.setColumnWidth((short)3, (short)7000);
		   sheet.setColumnWidth((short)4, (short)6410);
		   sheet.setColumnWidth((short)5, (short)8080);
		   sheet.setColumnWidth((short)6, (short)6060);
		   sheet.setColumnWidth((short)7, (short)8000);
		   sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
		   sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 7));
		   sheet.addMergedRegion(new CellRangeAddress(8, 8, 3, 5));
		   sheet.addMergedRegion(new CellRangeAddress(8, 9, 0, 0));
		   sheet.addMergedRegion(new CellRangeAddress(8, 9, 1, 1));
		   sheet.addMergedRegion(new CellRangeAddress(8, 9, 2, 2));
		   sheet.addMergedRegion(new CellRangeAddress(8, 9, 6, 6));
		   sheet.addMergedRegion(new CellRangeAddress(8, 9, 7, 7));
		   HSSFRow row1 = sheet.createRow(1);
		   row1.setHeight((short)600);
			// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		   HSSFCell cell21 = row1.createCell(0);
			// 设置单元格内容
		   cell21.setCellValue("中外运普菲斯亿达（上海）物流有限公司");
		   HSSFCellStyle cellStyle=wb.createCellStyle(); 
		   cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
		   cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   HSSFFont font = wb.createFont();
		   font.setFontHeightInPoints((short) 18);
		   font.setColor(HSSFColor.BLACK.index);
		   font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   font.setFontName("宋体");
		   cellStyle.setFont(font);
		   cell21.setCellStyle(cellStyle);
	
		   HSSFRow row4 = sheet.createRow(3);
		   row4.setHeight((short)600);
		   HSSFCell cell41 = row4.createCell(0);
		   cell41.setCellValue("收款调整账单");
		   HSSFCellStyle cellStyle1=wb.createCellStyle();  		  
		   cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   HSSFFont font1 = wb.createFont();
		   font1.setFontHeightInPoints((short) 12);
		   font1.setColor(HSSFColor.BLACK.index);
		   font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   font1.setFontName("宋体");
		   cellStyle1.setFont(font1);
		   cell41.setCellStyle(cellStyle1);
		   
		   HSSFRow row6 = sheet.createRow(5);
		   row6.setHeight((short)600);
		   HSSFCell cell67 = row6.createCell(6);
		   cell67.setCellValue("所属期");
		   HSSFCellStyle cellStyle2=wb.createCellStyle();  		   
		   cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   cellStyle2.setTopBorderColor((short)10);
		   HSSFFont font2 = wb.createFont();
		   font2.setFontHeightInPoints((short) 10);
		   font2.setColor(HSSFColor.BLACK.index);
		   font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   font2.setFontName("宋体");
		   cellStyle2.setFont(font2);
		   cell67.setCellStyle(cellStyle2);
		   
		   HSSFCell cell68 = row6.createCell(7);
		   cell68.setCellValue(ObjUtil.ifNull(map.get("BELONG_MONTH"),""));
		   cell68.setCellStyle(cellStyle2);
		   
		   HSSFCellStyle style=wb.createCellStyle();  
		   style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   style.setBorderBottom(CellStyle.BORDER_THIN);  
		   style.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
		   style.setBorderLeft(CellStyle.BORDER_THIN);  
		   style.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
		   style.setBorderRight(CellStyle.BORDER_THIN);  
		   style.setRightBorderColor(IndexedColors.BLACK.getIndex());  
		   style.setBorderTop(CellStyle.BORDER_THIN);  
		   style.setTopBorderColor(IndexedColors.BLACK.getIndex());  
		   style.setFont(font2);
		   	   
		   HSSFRow row9 = sheet.createRow(8);
		   row9.setHeight((short)600);
		   HSSFCell cell91 = row9.createCell(0);
		   cell91.setCellValue("客户简称");
		   cell91.setCellStyle(style);
		   
		   HSSFCell cell92 = row9.createCell(1);
		   cell92.setCellValue("月初账单");
		   cell92.setCellStyle(style);
		   
		   HSSFCell cell93 = row9.createCell(2);
		   cell93.setCellValue("确认账单");
		   cell93.setCellStyle(style);
		   	   
		   HSSFCell cell94 = row9.createCell(3);
		   cell94.setCellValue("调整账单");
		   cell94.setCellStyle(style);
		   		   
		   HSSFCell cell95 = row9.createCell(4);
		   HSSFCell cell96 = row9.createCell(5);
		   HSSFCellStyle styled=wb.createCellStyle(); 
		   styled.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   styled.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   styled.setBorderTop(CellStyle.BORDER_THIN);  
		   styled.setTopBorderColor(IndexedColors.BLACK.getIndex());
		   cell95.setCellStyle(styled);
		   cell96.setCellStyle(styled);
		     
		   HSSFRow row10 = sheet.createRow(9);
		   row10.setHeight((short)600);
		   HSSFCell cell101 = row10.createCell(0);
		   HSSFCell cell102 = row10.createCell(1);
		   HSSFCell cell103= row10.createCell(2);
		   HSSFCell cell107= row10.createCell(6);
		   HSSFCell cell108= row10.createCell(7);
		   HSSFCellStyle styles=wb.createCellStyle(); 
		   styles.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   styles.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   styles.setBorderRight(CellStyle.BORDER_THIN);  
		   styles.setRightBorderColor(IndexedColors.BLACK.getIndex()); 
		   cell101.setCellStyle(styles);
		   cell102.setCellStyle(styles);
		   cell103.setCellStyle(styles);
		   cell107.setCellStyle(styles);
		   cell108.setCellStyle(styles);
		   
		   
		   HSSFCell cell104 = row10.createCell(3);
		   cell104.setCellValue("金额");
		   cell104.setCellStyle(style);
		   
		   HSSFCell cell105 = row10.createCell(4);
		   cell105.setCellValue("税金");
		   cell105.setCellStyle(style);
		   
		   HSSFCell cell106 = row10.createCell(5);
		   cell106.setCellValue("合计");
		   cell106.setCellStyle(style);
		   
		   HSSFCell cell97 = row9.createCell(6);
		   cell97.setCellValue("项目");
		   cell97.setCellStyle(style);
		   
		   HSSFCell cell98 = row9.createCell(7);
		   cell98.setCellValue("原因");
		   cell98.setCellStyle(style);
		   
//		   HSSFCellStyle cellStylef = wb.createCellStyle();  
//		   cellStylef.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		   cellStylef.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//		   cellStylef.setBorderBottom(CellStyle.BORDER_THIN);  
//		   cellStylef.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
//		   cellStylef.setBorderLeft(CellStyle.BORDER_THIN);  
//		   cellStylef.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
//		   cellStylef.setBorderRight(CellStyle.BORDER_THIN);  
//		   cellStylef.setRightBorderColor(IndexedColors.BLACK.getIndex());  
//		   cellStylef.setBorderTop(CellStyle.BORDER_THIN);  
//		   cellStylef.setTopBorderColor(IndexedColors.BLACK.getIndex());  		   
//           cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));  
		   DecimalFormat df = new DecimalFormat("#.00");
		   
		   for(int i=0;i<dataset.size();i++){
			   Map mapD=(Map)dataset.get(i);
			   HSSFRow rowData = sheet.createRow(i+10);
			   rowData.setHeight((short)600);
			   HSSFCell cellD0 = rowData.createCell(0);
			   cellD0.setCellValue(ObjUtil.ifNull(mapD.get("BUSS_NAME"), ""));
			   cellD0.setCellStyle(style);
				
			
	           HSSFCell cellD1 = rowData.createCell(1);
			   if(mapD.get("INITITAL_AMOUNT")!=null){
			   cellD1.setCellValue(Double.parseDouble(df.format(Double.parseDouble(mapD.get("INITITAL_AMOUNT").toString()))));
			   }else{
				   cellD1.setCellValue(0);
			   }			   
			   cellD1.setCellStyle(style);
			   
			   HSSFCell cellD2 = rowData.createCell(2);
			   if(mapD.get("CONFIRM_AMOUNT")!=null){
			   cellD2.setCellValue(Double.parseDouble(df.format(Double.parseDouble(mapD.get("CONFIRM_AMOUNT").toString()))));
			   }else{
				   cellD2.setCellValue(0); 
			   }
			   cellD2.setCellStyle(style);
			   
			   HSSFCell cellD3 = rowData.createCell(3);
			   if(mapD.get("SUBTAX_AMOUNT")!=null){
			   cellD3.setCellValue(Double.parseDouble(df.format(Double.parseDouble(mapD.get("SUBTAX_AMOUNT").toString()))));
			   }else{
				   cellD3.setCellValue(0);
			   }
			   cellD3.setCellStyle(style);
			   
			   HSSFCell cellD4 = rowData.createCell(4);
			   if(mapD.get("TAX_AMOUNT")!=null){
			   cellD4.setCellValue(Double.parseDouble(df.format(Double.parseDouble(mapD.get("TAX_AMOUNT").toString()))));
			   }else{
				   cellD4.setCellValue(0);
			   }
			   cellD4.setCellStyle(style);
			   
			   HSSFCell cellD5 = rowData.createCell(5);
			   if(mapD.get("ADJ_AMOUNT")!=null){
			   cellD5.setCellValue(Double.parseDouble(df.format(Double.parseDouble(mapD.get("ADJ_AMOUNT").toString()))));
			   }else{
				   cellD5.setCellValue(0);
			   }
			   cellD5.setCellStyle(style);
			   
			   HSSFCell cellD6 = rowData.createCell(6);
			   if(mapD.get("SHORT_NAME")!=null){
			   cellD6.setCellValue(mapD.get("SHORT_NAME").toString());
			   }else{
				   cellD6.setCellValue("");
			   }
			   cellD6.setCellStyle(style);
			   
			   HSSFCell cellD7 = rowData.createCell(7);
			   if(mapD.get("ADJ_REASON")!=null){
			   cellD7.setCellValue(ObjUtil.ifNull(mapD.get("ADJ_REASON"), ""));
			   }else{
				   cellD7.setCellValue("");
			   }
			   cellD7.setCellStyle(style); 
			   
		   }
		   int size=dataset.size()+10;	
		   
		   HSSFRow rowAdd = sheet.createRow(size);
		   rowAdd.setHeight((short)600);
		   HSSFCell cellAdd = rowAdd.createCell(0);
		   cellAdd.setCellValue("合计");
		   cellAdd.setCellStyle(style);   
		   
		   HSSFCell cellAdd1 = rowAdd.createCell(1);
		   cellAdd1.setCellFormula("SUBTOTAL(9,B11:B"+size+")");
		   cellAdd1.setCellStyle(style);   
		   
		   HSSFCell cellAdd2 = rowAdd.createCell(2);
		   cellAdd2.setCellFormula("SUBTOTAL(9,C11:C"+size+")");
		   cellAdd2.setCellStyle(style);  
		   
		   HSSFCell cellAdd3 = rowAdd.createCell(3);
		   cellAdd3.setCellFormula("SUBTOTAL(9,D11:D"+size+")");
		   cellAdd3.setCellStyle(style); 
		   
		   HSSFCell cellAdd4 = rowAdd.createCell(4);
		   cellAdd4.setCellFormula("SUBTOTAL(9,E11:E"+size+")");
		   cellAdd4.setCellStyle(style); 
		   
		   HSSFCell cellAdd5 = rowAdd.createCell(5);
		   cellAdd5.setCellFormula("SUBTOTAL(9,F11:F"+size+")");
		   cellAdd5.setCellStyle(style); 
		      
		   HSSFCell cellAdd6 = rowAdd.createCell(6);
		   cellAdd6.setCellStyle(style); 
		   HSSFCell cellAdd7 = rowAdd.createCell(7);
		   cellAdd7.setCellStyle(style); 
		   
		   HSSFRow rowDown = sheet.createRow(size+2);
		   rowDown.setHeight((short)600);
		   HSSFCell cellDown5 = rowDown.createCell(5);
		   cellDown5.setCellValue("经办人:");
		   cellDown5.setCellStyle(cellStyle2);   
		  
		   HSSFRow rowDown15 = sheet.createRow(size+3);
		   rowDown15.setHeight((short)600);
		   HSSFCell cellDown15 = rowDown15.createCell(5);
		   cellDown15.setCellValue("项目负责人:");
		   cellDown15.setCellStyle(cellStyle2);  
		   
		   HSSFRow rowDown25 = sheet.createRow(size+4);
		   rowDown25.setHeight((short)600);
		   HSSFCell cellDown25 = rowDown25.createCell(5);
		   cellDown25.setCellValue("总经理：");
		   cellDown25.setCellStyle(cellStyle2);  
		   
		   return wb;
	   
	   }else{
		   return null;
	   }
   }
   
   @SuppressWarnings({"unchecked" })
   public HSSFWorkbook PayAdjBillExportExcel(String title,String headers, String fieldNames,

	         List dataset, OutputStream out, String pattern) {
	   HSSFWorkbook wb = new HSSFWorkbook();
	   
	   
	   if(dataset!=null&&dataset.size()>0){
			// 建立新的sheet对象（excel的表单）
		   Map map=(Map)dataset.get(0);
		   HSSFSheet sheet = wb.createSheet("sheet1");
		   sheet.setDisplayGridlines(false);
		   sheet.setColumnWidth((short)0,(short)5700);
		   sheet.setColumnWidth((short)1, (short)5700);
		   sheet.setColumnWidth((short)2, (short)5700);
		   sheet.setColumnWidth((short)3, (short)5300);
		   sheet.setColumnWidth((short)4, (short)5300);
		   sheet.setColumnWidth((short)5, (short)5300);
		   sheet.setColumnWidth((short)6, (short)5700);
		 //  sheet.setColumnWidth((short)7, (short)6000);
		   sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
		   sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 7));
		   sheet.addMergedRegion(new CellRangeAddress(8, 8, 3, 5));
		   sheet.addMergedRegion(new CellRangeAddress(8, 9, 0, 0));
		   sheet.addMergedRegion(new CellRangeAddress(8, 9, 1, 1));
		   sheet.addMergedRegion(new CellRangeAddress(8, 9, 2, 2));
		   sheet.addMergedRegion(new CellRangeAddress(8, 9, 6, 6));
		   HSSFRow row1 = sheet.createRow(1);
			
			// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		   HSSFCell cell21 = row1.createCell(0);
			// 设置单元格内容
		   cell21.setCellValue("中外运普菲斯亿达（上海）物流有限公司");
		   HSSFCellStyle cellStyle=wb.createCellStyle(); 
		   cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
		   cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   HSSFFont font = wb.createFont();
		   font.setFontHeightInPoints((short) 18);
		   font.setColor(HSSFColor.BLACK.index);
		   font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   font.setFontName("宋体");
		   cellStyle.setFont(font);
		   cell21.setCellStyle(cellStyle);
	
		   HSSFRow row4 = sheet.createRow(3);
		   HSSFCell cell41 = row4.createCell(0);
		   cell41.setCellValue("承运商付款调整账单");
		   HSSFCellStyle cellStyle1=wb.createCellStyle();  		  
		   cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   HSSFFont font1 = wb.createFont();
		   font1.setFontHeightInPoints((short) 12);
		   font1.setColor(HSSFColor.BLACK.index);
		   font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   font1.setFontName("宋体");
		   cellStyle1.setFont(font1);
		   cell41.setCellStyle(cellStyle1);
		   
		   HSSFCellStyle cellStyle2=wb.createCellStyle();  		   
		   cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   cellStyle2.setTopBorderColor((short)10);
		   HSSFFont font2 = wb.createFont();
		   font2.setFontHeightInPoints((short) 10);
		   font2.setColor(HSSFColor.BLACK.index);
		   font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   font2.setFontName("宋体");
		   cellStyle2.setFont(font2);
		   
		   HSSFRow row6 = sheet.createRow(5);
		   HSSFCell cell66 = row6.createCell(5);
		   cell66.setCellValue("所属期:");
		   cell66.setCellStyle(cellStyle2);
		   
		   HSSFCell cell67 = row6.createCell(6);
		   cell67.setCellValue(ObjUtil.ifNull(map.get("BELONG_MONTH"),""));
		   cell67.setCellStyle(cellStyle2);
		   
		   HSSFCellStyle style=wb.createCellStyle();  
		   style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   style.setBorderBottom(CellStyle.BORDER_THIN);  
		   style.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
		   style.setBorderLeft(CellStyle.BORDER_THIN);  
		   style.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
		   style.setBorderRight(CellStyle.BORDER_THIN);  
		   style.setRightBorderColor(IndexedColors.BLACK.getIndex());  
		   style.setBorderTop(CellStyle.BORDER_THIN);  
		   style.setTopBorderColor(IndexedColors.BLACK.getIndex());  
		   style.setFont(font2);
		   	   
		   HSSFRow row9 = sheet.createRow(8);
		   row9.setHeight((short)500);
		   HSSFCell cell91 = row9.createCell(0);
		   cell91.setCellValue("承运商简称");
		   cell91.setCellStyle(style);
		   
		   HSSFCell cell92 = row9.createCell(1);
		   cell92.setCellValue("月初账单");
		   cell92.setCellStyle(style);
		   
		   HSSFCell cell93 = row9.createCell(2);
		   cell93.setCellValue("确认账单");
		   cell93.setCellStyle(style);
		   	   
		   HSSFCell cell94 = row9.createCell(3);
		   cell94.setCellValue("调整账单");
		   cell94.setCellStyle(style);
		   		   
		   HSSFCell cell95 = row9.createCell(4);
		   HSSFCell cell96 = row9.createCell(5);
		   HSSFCellStyle styled=wb.createCellStyle(); 
		   styled.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   styled.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   styled.setBorderTop(CellStyle.BORDER_THIN);  
		   styled.setTopBorderColor(IndexedColors.BLACK.getIndex());
		   cell95.setCellStyle(styled);
		   cell96.setCellStyle(styled);
		     
		   HSSFRow row10 = sheet.createRow(9);
		   row10.setHeight((short)500);
		   HSSFCell cell101 = row10.createCell(0);
		   HSSFCell cell102 = row10.createCell(1);
		   HSSFCell cell103= row10.createCell(2);
		   HSSFCell cell107= row10.createCell(6);
		  // HSSFCell cell108= row10.createCell(7);
		   HSSFCellStyle styles=wb.createCellStyle(); 
		   styles.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   styles.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   styles.setBorderRight(CellStyle.BORDER_THIN);  
		   styles.setRightBorderColor(IndexedColors.BLACK.getIndex()); 
		   cell101.setCellStyle(styles);
		   cell102.setCellStyle(styles);
		   cell103.setCellStyle(styles);
		   cell107.setCellStyle(styles);
		  // cell108.setCellStyle(styles);
		   
		   
		   HSSFCell cell104 = row10.createCell(3);
		   cell104.setCellValue("金额");
		   cell104.setCellStyle(style);
		   
		   HSSFCell cell105 = row10.createCell(4);
		   cell105.setCellValue("税金");
		   cell105.setCellStyle(style);
		   
		   HSSFCell cell106 = row10.createCell(5);
		   cell106.setCellValue("小计");
		   cell106.setCellStyle(style);
		
		   
		   HSSFCell cell98 = row9.createCell(6);
		   cell98.setCellValue("原因");
		   cell98.setCellStyle(style);
		    
		   DecimalFormat df = new DecimalFormat("#.00");
		   double initA=0.00;
		   double initB=0.00;
		   double initC=0.00;
		   double initD=0.00;
		   double initE=0.00;
		   for(int i=0;i<dataset.size();i++){
			   
			   Map mapD=(Map)dataset.get(i);
			   HSSFRow rowData = sheet.createRow(i+10);
			   rowData.setHeight((short)500);
			   HSSFCell cellD0 = rowData.createCell(0);
			   cellD0.setCellValue(ObjUtil.ifNull(mapD.get("SUPLR_NAME"), ""));
			   cellD0.setCellStyle(style);
				
			   HSSFCell cellD1 = rowData.createCell(1);
			   if(mapD.get("INITITAL_AMOUNT")!=null){
				   initA=initA+Double.parseDouble(mapD.get("INITITAL_AMOUNT").toString());
				   cellD1.setCellValue(df.format(Double.parseDouble(mapD.get("INITITAL_AMOUNT").toString())));
			   }else{
				   cellD1.setCellValue(0.00);
			   }			   
			   cellD1.setCellStyle(style);
			   
			   HSSFCell cellD2 = rowData.createCell(2);
			   if(mapD.get("CONFIRM_AMOUNT")!=null){
				   initB=initB+Double.parseDouble(mapD.get("CONFIRM_AMOUNT").toString());
			   
				   cellD2.setCellValue(df.format(Double.parseDouble(mapD.get("CONFIRM_AMOUNT").toString())));
			   }else{
				   cellD2.setCellValue(0.00); 
			   }
			   cellD2.setCellStyle(style);
			   
			   HSSFCell cellD3 = rowData.createCell(3);
			   if(mapD.get("SUBTAX_AMOUNT")!=null){
				
				   initC=initC+Double.parseDouble(mapD.get("SUBTAX_AMOUNT").toString());
			   
				   cellD3.setCellValue(df.format(Double.parseDouble(mapD.get("SUBTAX_AMOUNT").toString())));
			   }else{
				   cellD3.setCellValue((short)0);
			   }
			   cellD3.setCellStyle(style);
			   
			   HSSFCell cellD4 = rowData.createCell(4);
			   if(mapD.get("TAX_AMOUNT")!=null){
				   initD=initD+Double.parseDouble(mapD.get("TAX_AMOUNT").toString());
				   cellD4.setCellValue(df.format(Double.parseDouble(mapD.get("TAX_AMOUNT").toString())));
			   }else{
				   cellD4.setCellValue((short)0);
			   }
			   cellD4.setCellStyle(style);
			   
			   HSSFCell cellD5 = rowData.createCell(5);
			   if(mapD.get("ADJ_AMOUNT")!=null){
				   initE=initE+Double.parseDouble(mapD.get("ADJ_AMOUNT").toString());
			   
				   cellD5.setCellValue(df.format(Double.parseDouble(mapD.get("ADJ_AMOUNT").toString())));
			   }else{
				   cellD5.setCellValue((short)0);
			   }
			   cellD5.setCellStyle(style);
			  
			   
			   HSSFCell cellD6 = rowData.createCell(6);
			   if(mapD.get("ADJ_REASON")!=null){
			   cellD6.setCellValue(ObjUtil.ifNull(mapD.get("ADJ_REASON"), ""));
			   }else{
				   cellD6.setCellValue("");
			   }
			   cellD6.setCellStyle(style); 
			   
		   }
		  	   
		   int size=dataset.size()+10;	
		   
		   HSSFRow rowAdd = sheet.createRow(size);
		   rowAdd.setHeight((short)500);
		   HSSFCell cellAdd = rowAdd.createCell(0);
		   cellAdd.setCellValue("合计");
		   cellAdd.setCellStyle(style);   
		   
		   HSSFCell cellAdd1 = rowAdd.createCell(1);
		   cellAdd1.setCellValue(df.format(initA));
		   cellAdd1.setCellStyle(style);   
		   
		   HSSFCell cellAdd2 = rowAdd.createCell(2);
		   cellAdd2.setCellValue(df.format(initB));
		   cellAdd2.setCellStyle(style);  
		   
		   HSSFCell cellAdd3 = rowAdd.createCell(3);
		   cellAdd3.setCellValue(df.format(initC));
		   cellAdd3.setCellStyle(style); 
		   
		   HSSFCell cellAdd4 = rowAdd.createCell(4);
		   cellAdd4.setCellValue(df.format(initD));
		   cellAdd4.setCellStyle(style); 
		   
		   HSSFCell cellAdd5 = rowAdd.createCell(5);
		   cellAdd5.setCellValue(df.format(initE));
		   cellAdd5.setCellStyle(style); 
		      
		   HSSFCell cellAdd6 = rowAdd.createCell(6);
		   cellAdd6.setCellStyle(style); 
		   
		   HSSFCellStyle style1=wb.createCellStyle();  
		   style1.setBorderBottom(CellStyle.BORDER_THIN);  
		   style1.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		   
		   HSSFRow rowDown = sheet.createRow(size+2);
		   rowDown.setHeight((short)500);
		   HSSFCell cellDown5 = rowDown.createCell(5);
		   cellDown5.setCellValue("经办人:");
		   cellDown5.setCellStyle(cellStyle2);   
		  
		   HSSFCell cellDown6 = rowDown.createCell(6);
		   cellDown6.setCellStyle(style1);   
		   
		   HSSFRow rowDown15 = sheet.createRow(size+3);
		   rowDown15.setHeight((short)500);
		   HSSFCell cellDown15 = rowDown15.createCell(5);
		   cellDown15.setCellValue("项目负责人:");
		   cellDown15.setCellStyle(cellStyle2);  
		   
		   HSSFCell cellDown16 = rowDown15.createCell(6);
		   cellDown16.setCellStyle(style1);  
		   
		   HSSFRow rowDown25 = sheet.createRow(size+4);
		   rowDown25.setHeight((short)500);
		   HSSFCell cellDown25 = rowDown25.createCell(5);
		   cellDown25.setCellValue("总经理：");
		   cellDown25.setCellStyle(cellStyle2);  
		   
		   HSSFCell cellDown26 = rowDown25.createCell(6);
		   cellDown26.setCellStyle(style1); 
		   
		   return wb;
	   
	   }else{
		   return null;
	   }
   }
   
   @SuppressWarnings({"unchecked" })
   public HSSFWorkbook PayReqBillExportExcel(String title,String headers, String fieldNames,

	         List dataset, OutputStream out, String pattern) {
	   HSSFWorkbook wb = new HSSFWorkbook();
	   
	   
	   if(dataset!=null&&dataset.size()>0){
			// 建立新的sheet对象（excel的表单）
		   Map map=(Map)dataset.get(0);
		   HSSFSheet sheet = wb.createSheet("sheet1");
		   sheet.setDisplayGridlines(false);
		   sheet.setColumnWidth(0,(short)1500);
		   sheet.setColumnWidth((short)1, (short)4000);
		   sheet.setColumnWidth((short)2, (short)4000);
		   sheet.setColumnWidth((short)3, (short)3200);
		   sheet.setColumnWidth((short)4, (short)6500);
		   sheet.setColumnWidth((short)5, (short)3200);
		   sheet.setColumnWidth((short)6, (short)4000);
		   sheet.setColumnWidth((short)7, (short)6000);
		   sheet.setColumnWidth((short)9, (short)5000);
		   sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 9));
		   sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 9));
		   sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 3));
		   sheet.addMergedRegion(new CellRangeAddress(3, 3, 4, 9));
		   sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 3));
		   sheet.addMergedRegion(new CellRangeAddress(4, 4, 4, 9));
		   sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 2));
		   sheet.addMergedRegion(new CellRangeAddress(5, 5, 3, 5));
		
		   for(int i=6;i<11;i++){
			   sheet.addMergedRegion(new CellRangeAddress(i, i, 1, 2));	   
			   sheet.addMergedRegion(new CellRangeAddress(i, i, 3, 5));
	       }
		   sheet.addMergedRegion(new CellRangeAddress(7, 7, 1, 2));
		   sheet.addMergedRegion(new CellRangeAddress(7, 7, 3, 5));
		   //sheet.addMergedRegion(new CellRangeAddress(8, 10, 1, 9));
		   HSSFRow row0 = sheet.createRow(0);
			
			// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		   HSSFCell cell21 = row0.createCell(1);
			// 设置单元格内容
		   cell21.setCellValue("中外运普菲斯亿达（上海）物流有限公司(Sinotrans PFSYiDA)");
		   HSSFCellStyle cellStyle=wb.createCellStyle(); 
		   cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
		   cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   HSSFFont font = wb.createFont();
		   font.setFontHeightInPoints((short) 18);
		   font.setColor(HSSFColor.BLACK.index);
		   font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   font.setFontName("宋体");
		   cellStyle.setFont(font);
		   cell21.setCellStyle(cellStyle);
	
		   HSSFRow row1 = sheet.createRow(1);
		   HSSFCell cell11 = row1.createCell(1);
		   cell11.setCellValue("付款申请单（Payment）");
		   HSSFCellStyle cellStyle1=wb.createCellStyle();  		  
		   cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   HSSFFont font1 = wb.createFont();
		   font1.setFontHeightInPoints((short) 12);
		   font1.setColor(HSSFColor.BLACK.index);
		   font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   font1.setFontName("宋体");
		   cellStyle1.setFont(font1);
		   cell11.setCellStyle(cellStyle1);
		   
		   HSSFRow row2 = sheet.createRow(2);
		   HSSFCell cellDate= row2.createCell(1);
		   SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
		   cellDate.setCellValue("日期（Date）:"+sdf.format(new Date()));
		   
		   HSSFCell cellDate7= row2.createCell(7);
		   cellDate7.setCellValue("发票日期（Date of Invoice）:");
		   
		   HSSFCell cellDate9= row2.createCell(9);
		   if(map.get("INVOICE_TIME")!=null){
			   cellDate9.setCellValue(map.get("INVOICE_TIME").toString());
		   }else{
			   cellDate9.setCellValue("");
		   }
		   
		   HSSFCellStyle style=wb.createCellStyle();  
		   style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   style.setBorderBottom(CellStyle.BORDER_THIN);  
		   style.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
//		   style.setBorderLeft(CellStyle.BORDER_THIN);  
//		   style.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
//		   style.setBorderRight(CellStyle.BORDER_THIN);  
//		   style.setRightBorderColor(IndexedColors.BLACK.getIndex());  
		   style.setBorderTop(CellStyle.BORDER_THIN);  
		   style.setTopBorderColor(IndexedColors.BLACK.getIndex());  
	
		   
		   
		   
		   HSSFCellStyle cellStyle2=wb.createCellStyle();  		   
		   cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   cellStyle2.setTopBorderColor((short)10);
		   HSSFFont font2 = wb.createFont();
		   font2.setFontHeightInPoints((short) 10);
		   font2.setColor(HSSFColor.BLACK.index);
		   font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   font2.setFontName("宋体");
		   cellStyle2.setFont(font2);
		   
		   
		   HSSFCellStyle cellStyle3=wb.createCellStyle();  		   
		   cellStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   cellStyle3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		   cellStyle3.setBorderBottom(CellStyle.BORDER_THIN);  
		   cellStyle3.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
		   cellStyle3.setBorderLeft(CellStyle.BORDER_THIN);  
		   cellStyle3.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
		   cellStyle3.setBorderRight(CellStyle.BORDER_THIN);  
		   cellStyle3.setRightBorderColor(IndexedColors.BLACK.getIndex());  
		   cellStyle3.setBorderTop(CellStyle.BORDER_THIN);  
		   cellStyle3.setTopBorderColor(IndexedColors.BLACK.getIndex());
		   
		   
		   HSSFCellStyle cellStyle4=wb.createCellStyle();  		   
		   cellStyle4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   cellStyle4.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		   cellStyle4.setBorderBottom(CellStyle.BORDER_THIN);  
		   cellStyle4.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
//		   cellStyle4.setBorderLeft(CellStyle.BORDER_THIN);  
//		   cellStyle4.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
		   cellStyle4.setBorderRight(CellStyle.BORDER_THIN);  
		   cellStyle4.setRightBorderColor(IndexedColors.BLACK.getIndex());  
		   cellStyle4.setBorderTop(CellStyle.BORDER_THIN);  
		   cellStyle4.setTopBorderColor(IndexedColors.BLACK.getIndex());
		   
		   HSSFRow row3 = sheet.createRow(3);
		   HSSFCell cell31 = row3.createCell(1);
		   cell31.setCellValue("成本中心(Cost Center)：");
		   cell31.setCellStyle(cellStyle3);
		   
		 
		   
		   HSSFCell cell34 = row3.createCell(4);
		   cell34.setCellValue("001  各BU的运营、控制室、维修维护、电工、制冷、保安及安全专员");
		   cell34.setCellStyle(cellStyle3);
		   
		   HSSFCell cell32 = row3.createCell(2);
		   cell32.setCellStyle(style);
		   HSSFCell cell33 = row3.createCell(3);
		   cell33.setCellStyle(style);	   
		   HSSFCell cell35 = row3.createCell(5);
		   cell35.setCellStyle(style);
		   HSSFCell cell36 = row3.createCell(6);
		   cell36.setCellStyle(style);
		   HSSFCell cell37 = row3.createCell(7);
		   cell37.setCellStyle(style);
		   HSSFCell cell38 = row3.createCell(8);
		   cell38.setCellStyle(style);
		   HSSFCell cell39 = row3.createCell(9);
		   cell39.setCellStyle(cellStyle3);

		   HSSFRow row4 = sheet.createRow(4);
		   HSSFCell cell41 = row4.createCell(1);
		   cell41.setCellValue("收 款 方(Company Name)：");
		   cell41.setCellStyle(cellStyle3);
		     
		  
		   HSSFCell cell44 = row4.createCell(4);
		   if(map.get("SUPLR_NAME")!=null){
		   cell44.setCellValue(map.get("SUPLR_NAME").toString());
		   }else{
			   cell44.setCellValue("");
		   }
		   cell44.setCellStyle(cellStyle3);
		   
		   HSSFCell cell42 = row4.createCell(2);
		   cell42.setCellStyle(style);
		   HSSFCell cell43 = row4.createCell(3);
		   cell43.setCellStyle(style);	   
		   HSSFCell cell45 = row4.createCell(5);
		   cell45.setCellStyle(style);
		   HSSFCell cell46 = row4.createCell(6);
		   cell46.setCellStyle(style);
		   HSSFCell cell47 = row4.createCell(7);
		   cell47.setCellStyle(style);
		   HSSFCell cell48 = row4.createCell(8);
		   cell48.setCellStyle(style);
		   HSSFCell cell49 = row4.createCell(9);
		   cell49.setCellStyle(cellStyle3);
		   
		   
		   
		   HSSFRow row5 = sheet.createRow(5);
		   HSSFCell cell51 = row5.createCell(1);
		   cell51.setCellValue("项目名称(Project)");
		   cell51.setCellStyle(cellStyle3);
		    
		   HSSFCell cell53 = row5.createCell(3);
		   cell53.setCellValue("摘要（对实际发生业务进行描述）(Description)");
		   cell53.setCellStyle(cellStyle3);
		   
		   HSSFCell cell52 = row5.createCell(2);
		   cell52.setCellStyle(style);
		   HSSFCell cell54 = row5.createCell(4);
		   cell54.setCellStyle(style);
		   HSSFCell cell55 = row5.createCell(5);
		   cell55.setCellStyle(style);
		   
		   HSSFCell cell56 = row5.createCell(6);
		   cell56.setCellValue("币种Curr.");
		   cell56.setCellStyle(cellStyle3);
		   
		   HSSFCell cell57= row5.createCell(7);
		   cell57.setCellValue("不含税金额(Cost/Expense)");
		   cell57.setCellStyle(cellStyle3);
		   
		   HSSFCell cell58 = row5.createCell(8);
		   cell58.setCellValue("税金(VAT)");
		   cell58.setCellStyle(cellStyle3);
		   
		   HSSFCell cell59 = row5.createCell(9);
		   cell59.setCellValue("价税合计金额（Total）");
		   cell59.setCellStyle(cellStyle3);
		   
		   
		   
		   
		   
		   HSSFRow row6 = sheet.createRow(6);
		   HSSFCell cell61 = row6.createCell(1);
		   cell61.setCellValue("辛普劳-麦当劳");
		   cell61.setCellStyle(cellStyle3);
		   
		   HSSFCell cell62 = row6.createCell(2);
		   cell62.setCellStyle(style);
		   HSSFCell cell64 = row6.createCell(4);
		   cell64.setCellStyle(style);
		   HSSFCell cell65 = row6.createCell(5);
		   cell65.setCellStyle(style);
		   
		   HSSFCell cell63 = row6.createCell(3);
		   if(map.get("SUPLR_NAME")!=null&&map.get("BELONG_MONTH")!=null){
			   cell63.setCellValue(map.get("SUPLR_NAME").toString()+"-"+map.get("BELONG_MONTH").toString()+"运费");
		   }else{
		   cell63.setCellValue("");
		   }
		   cell63.setCellStyle(cellStyle3);
		   
		   HSSFCell cell66 = row6.createCell(6);
		   cell66.setCellValue("CNY");
		   cell66.setCellStyle(cellStyle3);
		   
		   HSSFCell cell67= row6.createCell(7);
		   if(map.get("SUBTAX_AMOUNT")!=null){
			   cell67.setCellValue(map.get("SUBTAX_AMOUNT").toString());
		   }else{
		   cell67.setCellValue((short)0);
		   }
		   cell67.setCellStyle(cellStyle3);
		   
		   HSSFCell cell68 = row6.createCell(8);
		   if(map.get("TAX_AMOUNT")!=null){
			   cell68.setCellValue(map.get("TAX_AMOUNT").toString());
		   }else{
		   cell68.setCellValue((short)0);
		   }
		   cell68.setCellStyle(cellStyle3);
		   
		   HSSFCell cell69 = row6.createCell(9);
		   if(map.get("PAY_AMOUNT")!=null){
			   cell69.setCellValue(map.get("PAY_AMOUNT").toString());
		   }else{
		   cell69.setCellValue((short)0);
		   }
		   cell69.setCellStyle(cellStyle3);
		  		   	   
		   HSSFRow row7 = sheet.createRow(7);
		   HSSFCell cell71 = row7.createCell(1);
		   cell71.setCellValue("合计(大写）：");
		   cell71.setCellStyle(cellStyle3);
		   
		   HSSFCell cell72 = row7.createCell(2);
		   cell72.setCellStyle(style);
		   HSSFCell cell74 = row7.createCell(4);
		   cell74.setCellStyle(style);
		   HSSFCell cell75 = row7.createCell(5);
		   cell75.setCellStyle(style);
		   
		   NumberToCN ntc=new NumberToCN();
		   
		   HSSFCell cell73 = row7.createCell(3);
		  // cell73.setCellFormula("SUBSTITUTE(SUBSTITUTE(IF(J7>-0.5%,,\"负\")&TEXT(INT(FIXED(ABS(J7),2)),\"[dbnum2]G/通用格式元;;\")&TEXT(RIGHT(FIXED(J7),2),\"[dbnum2]0角0分;;\"&IF(ABS(J7)>1%,\"整\",)),\"零角\",IF(ABS(J7)<1,,\"零\")),\"零分\",\"整\")");
		   String money="0";
		   if(map.get("PAY_AMOUNT")!=null){
			 money=map.get("PAY_AMOUNT").toString(); 
		   }
		   double money1 = Double.parseDouble(money); 
	       BigDecimal numberOfMoney = new BigDecimal(money1);
		   cell73.setCellValue(ntc.number2CNMontrayUnit(numberOfMoney));
		   cell73.setCellStyle(cellStyle3);
		   
		   HSSFCell cell76 = row7.createCell(6);
		   cell76.setCellValue("CNY");
		   cell76.setCellStyle(cellStyle3);
		   	   
		   HSSFCell cell77 = row7.createCell(7);
		   cell77.setCellFormula("H7");
		   cell77.setCellStyle(cellStyle3);
		   		   
		   HSSFCell cell78 = row7.createCell(8);
		   cell78.setCellFormula("I7");
		   cell78.setCellStyle(cellStyle3);
		   
 		   
		   HSSFCell cell79 = row7.createCell(9);
		   cell79.setCellFormula("J7");
		   cell79.setCellStyle(cellStyle3);
     
		   HSSFCellStyle cellStyle5=wb.createCellStyle();  		   
		   //cellStyle5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   //cellStyle5.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		   //cellStyle5.setBorderBottom(CellStyle.BORDER_THIN);  
		   //cellStyle5.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
		   cellStyle5.setBorderLeft(CellStyle.BORDER_THIN);  
		   cellStyle5.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
//		   cellStyle5.setBorderRight(CellStyle.BORDER_THIN);  
//		   cellStyle5.setRightBorderColor(IndexedColors.BLACK.getIndex());  
//		   cellStyle5.setBorderTop(CellStyle.BORDER_THIN);  
//		   cellStyle5.setTopBorderColor(IndexedColors.BLACK.getIndex());
		   
		   HSSFCellStyle cellStyle8=wb.createCellStyle(); 
		   cellStyle8.setBorderRight(CellStyle.BORDER_THIN);  
		   cellStyle8.setRightBorderColor(IndexedColors.BLACK.getIndex());  
		   
		   HSSFRow row8 = sheet.createRow(8);
		   HSSFCell cell81 = row8.createCell(1);
		   cell81.setCellStyle(cellStyle5);
		   HSSFCell cell89 = row8.createCell(9);
		   cell89.setCellStyle(cellStyle8);
		   
		   
		   HSSFRow row9 = sheet.createRow(9);
		   HSSFCell cell91 = row9.createCell(1);
		   cell91.setCellValue("备注：现金   支票  电汇  本票  冲借款");
		   cell91.setCellStyle(cellStyle5);
		   HSSFCell cell95 = row9.createCell(5);
		   cell95.setCellValue("附件张数：");
		   
		   HSSFCell cell99 = row9.createCell(9);
		   cell99.setCellStyle(cellStyle8);
		   
		   
		   HSSFCellStyle cellStyle6=wb.createCellStyle();  	
		   cellStyle6.setBorderBottom(CellStyle.BORDER_THIN);  
		   cellStyle6.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
		   cellStyle6.setBorderLeft(CellStyle.BORDER_THIN);  
		   cellStyle6.setLeftBorderColor(IndexedColors.BLACK.getIndex()); 
		   
		   HSSFCellStyle cellStyle7=wb.createCellStyle();  	
		   cellStyle7.setBorderBottom(CellStyle.BORDER_THIN);  
		   cellStyle7.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
		   
		   
		   HSSFCellStyle cellStyle12=wb.createCellStyle();  	
		   cellStyle12.setBorderBottom(CellStyle.BORDER_THIN);  
		   cellStyle12.setBottomBorderColor(IndexedColors.BLACK.getIndex());  
		   cellStyle12.setBorderRight(CellStyle.BORDER_THIN);  
		   cellStyle12.setRightBorderColor(IndexedColors.BLACK.getIndex());  
		   
		   HSSFRow row10 = sheet.createRow(10);
		   HSSFCell cell101 = row10.createCell(1);
		   cell101.setCellStyle(cellStyle6);
		   for(int i=2;i<9;i++){
			   HSSFCell cell102 = row10.createCell(i);
			   cell102.setCellStyle(cellStyle7);
		   }
		   HSSFCell cell109 = row10.createCell(9);
		   cell109.setCellStyle(cellStyle12);
		      
		 
		   HSSFRow row13 = sheet.createRow(13);
		  	   
		   sheet.addMergedRegion(new CellRangeAddress(13, 13, 1, 9));
		   HSSFCell cell131 = row13.createCell(1);
		   cell131.setCellValue("中外运普菲斯亿达（上海）物流有限公司");
		   cell131.setCellStyle(cellStyle);
		   
		   sheet.addMergedRegion(new CellRangeAddress(14, 14, 1, 9));
		   HSSFRow row14 = sheet.createRow(14);
		   HSSFCell cell141 = row14.createCell(1);
		   cell141.setCellValue("付款申请书");
		   cell141.setCellStyle(cellStyle1);
		   
		   HSSFCellStyle cellStyle10=wb.createCellStyle(); 
		   cellStyle10.setBorderLeft(CellStyle.BORDER_THIN);  
		   cellStyle10.setLeftBorderColor(IndexedColors.BLACK.getIndex());  
		   cellStyle10.setBorderTop(CellStyle.BORDER_THIN);  
		   cellStyle10.setTopBorderColor(IndexedColors.BLACK.getIndex());
		   
		   HSSFCellStyle cellStyle9=wb.createCellStyle(); 
		   cellStyle9.setBorderRight(CellStyle.BORDER_THIN);  
		   cellStyle9.setRightBorderColor(IndexedColors.BLACK.getIndex());  
		   cellStyle9.setBorderTop(CellStyle.BORDER_THIN);  
		   cellStyle9.setTopBorderColor(IndexedColors.BLACK.getIndex());
		   
		   HSSFCellStyle cellStyle11=wb.createCellStyle(); 
		   cellStyle11.setBorderTop(CellStyle.BORDER_THIN);  
		   cellStyle11.setTopBorderColor(IndexedColors.BLACK.getIndex());
		   
		   HSSFCellStyle cellStyle13=wb.createCellStyle(); 
		   cellStyle13.setBorderRight(CellStyle.BORDER_THIN);  
		   cellStyle13.setRightBorderColor(IndexedColors.BLACK.getIndex()); 
		   
		   HSSFRow row16 = sheet.createRow(16);
		   HSSFCell cell161 = row16.createCell(1);
		   cell161.setCellStyle(cellStyle10);
		   
		   for(int i=2;i<9;i++){
			   HSSFCell cell162 = row16.createCell(i);
			   cell162.setCellStyle(cellStyle11);
		   }
		   HSSFCell cell169 = row16.createCell(9);
		   cell169.setCellStyle(cellStyle9);
		   
		   
		   
		   
		   HSSFRow row17 = sheet.createRow(17);
		   HSSFCell cell171 = row17.createCell(1);
		   cell171.setCellValue("请付");
		   cell171.setCellStyle(cellStyle5);
		   HSSFCell cell179 = row17.createCell(9);
		   cell179.setCellStyle(cellStyle13);
	
		   HSSFRow row18 = sheet.createRow(18);
		   HSSFCell cell181 = row18.createCell(1);
		   cell181.setCellStyle(cellStyle5);
		   HSSFCell cell189 = row18.createCell(9);
		   cell189.setCellStyle(cellStyle13);
		   
		   HSSFRow row19 = sheet.createRow(19);
		   HSSFCell cell191 = row19.createCell(1);
		   cell191.setCellStyle(cellStyle5);
		   HSSFCell cell199 = row19.createCell(9);
		   cell199.setCellStyle(cellStyle13);
		   
		   HSSFRow row20 = sheet.createRow(20);
		   HSSFCell cell201 = row20.createCell(1);
		   cell201.setCellStyle(cellStyle5);
		   HSSFCell cell209 = row20.createCell(9);
		   cell209.setCellStyle(cellStyle13);
		   
		   
		   HSSFRow row21 = sheet.createRow(21);
		   HSSFCell cell211 = row21.createCell(1);
		   cell211.setCellStyle(cellStyle5);
		   HSSFCell cell219 = row21.createCell(9);
		   cell219.setCellStyle(cellStyle13);
		   
		   
		   HSSFRow row22= sheet.createRow(22);
		   HSSFCell cell221 = row22.createCell(1);
		   cell221.setCellStyle(cellStyle5);
		   HSSFCell cell229 = row22.createCell(9);
		   cell229.setCellStyle(cellStyle13);
		   
		   HSSFRow row23= sheet.createRow(23);
		   HSSFCell cell231 = row23.createCell(1);
		   cell231.setCellStyle(cellStyle5);
		   HSSFCell cell239 = row23.createCell(9);
		   cell239.setCellStyle(cellStyle13);
		   

		   HSSFRow row24 = sheet.createRow(24);
		   HSSFCell cell241 = row24.createCell(1);
		   cell241.setCellStyle(cellStyle6);
		   
		   for(int i=0;i<7;i++){
			   HSSFCell cell242 = row24.createCell(i+2);
			   cell242.setCellStyle(cellStyle7);  
		   }
		   HSSFCell cell249 = row24.createCell(9);
		   cell249.setCellStyle(cellStyle12);
		   
		   
		   HSSFCell cell172 = row17.createCell(2);
		   cell172.setCellValue("收款方：");
		   //cell172.setCellStyle(cellStyle3);
		   
		   HSSFCell cell174 = row17.createCell(4);
		   if(map.get("SUPLR_NAME")!=null){
			   cell174.setCellValue(map.get("SUPLR_NAME").toString());
		   }else{
		   cell174.setCellValue("");
		   }
		   cell174.setCellStyle(cellStyle7);
		   
		   HSSFCell cell177 = row17.createCell(7);
		   cell177.setCellValue("Detailed bank information of the received party");
		   //cell177.setCellStyle(cellStyle3);
		   
		  // HSSFRow row18 = sheet.createRow(18);
		   HSSFCell cell182 = row18.createCell(2);
		   cell182.setCellValue("开户银行:");
		  // cell182.setCellStyle(style);
		   
		   HSSFCell cell184 = row18.createCell(4);
		   if(map.get("BANK")!=null){
			   cell184.setCellValue(map.get("BANK").toString());
		   }else{
		   cell184.setCellValue("");
		   }
		   cell184.setCellStyle(cellStyle7);
		   
		  // HSSFRow row19 = sheet.createRow(19);
		   HSSFCell cell192 = row19.createCell(2);
		   cell192.setCellValue("账号：");
		  // cell192.setCellStyle(style);
		   
		   HSSFCell cell194 = row19.createCell(4);
		   if(map.get("ACC_NUM")!=null){
			   cell194.setCellValue(map.get("ACC_NUM").toString());
		   }else{
		   cell194.setCellValue("");
		   }
		   cell194.setCellStyle(cellStyle7);
		   
		  // HSSFRow row20 = sheet.createRow(20);
		   HSSFCell cell202 = row20.createCell(2);
		   cell202.setCellValue("小写金额：");
		  // cell202.setCellStyle(style);
		   
		   HSSFCell cell204 = row20.createCell(4);
		   if(map.get("PAY_AMOUNT")!=null){
			   cell204.setCellValue(map.get("PAY_AMOUNT").toString());
		   }else{
		   cell204.setCellValue("");
		   }
		   cell204.setCellStyle(cellStyle7);
		   
		  // HSSFRow row21 = sheet.createRow(21);
		   HSSFCell cell212 = row21.createCell(2);
		   cell212.setCellValue("大写金额：");
		  //cell212.setCellStyle(style);
		   
		   HSSFCell cell214 = row21.createCell(4);
		   cell214.setCellFormula("D8");
		   cell214.setCellStyle(cellStyle7);
		   
		  // HSSFRow row22 = sheet.createRow(22);
		   HSSFCell cell222 = row22.createCell(2);
		   cell222.setCellValue("款项用途:");
		   //cell222.setCellStyle(style);
		   
		   HSSFCell cell224 = row22.createCell(4);
		   cell224.setCellFormula("D7");
		   cell224.setCellStyle(cellStyle7);
		   
		 //  HSSFRow row23 = sheet.createRow(23);
		   HSSFCell cell232 = row23.createCell(2);
		   cell232.setCellValue("最迟付款日：");
		   //cell232.setCellStyle(style);
		   
		   HSSFCell cell234 = row23.createCell(4);
		   if(map.get("LATEST_PAY_TIME")!=null){
			   cell234.setCellValue(map.get("LATEST_PAY_TIME").toString());
		   }else{
			   cell234.setCellValue("");
		   }		   
		   cell234.setCellStyle(cellStyle7);
		   
		   HSSFCell cell237 = row23.createCell(7);
		   cell237.setCellValue("Deadline of bank paying out");
		   //cell237.setCellStyle(style);
		   
		   HSSFRow row26 = sheet.createRow(26);
		   HSSFCell cell261 = row26.createCell(1);
		   cell261.setCellValue("经办人(Applicant)：");
		   //cell261.setCellStyle(style);
		   
		   HSSFCell cell266 = row26.createCell(6);
		   cell266.setCellValue("部门主管(Depart. Manager):");
		   //cell266.setCellStyle(style);
		   
		   HSSFRow row29 = sheet.createRow(29);
		   HSSFCell cell291 = row29.createCell(1);
		   cell291.setCellValue("部门经理(Depart. Manager)：");
		   //cell291.setCellStyle(style);
		   
		   HSSFCell cell296 = row29.createCell(6);
		   cell296.setCellValue("总经理（GM）：");
		   //cell296.setCellStyle(style);
		   
		   
		   HSSFRow row32 = sheet.createRow(32);
		   HSSFCell cell321 = row32.createCell(1);
		   cell321.setCellValue("财务经办(Accountant):");
		   //cell321.setCellStyle(style);
		   
		   HSSFCell cell326 = row32.createCell(6);
		   cell326.setCellValue("财务主管（Finance Supervisor）：");
		   //cell326.setCellStyle(style);
		   
		   return wb;
	   
	   }else{
		   return null;
	   }
   }
   
   public HSSFWorkbook RecCheckBillExportExcel(List<HashMap<String, Object>> dataset1,List<HashMap<String, Object>> dataset2,HashMap<Object, Object> Hmap) {
	   HSSFWorkbook wb = new HSSFWorkbook(); 
	   if(dataset1!=null&&dataset1.size()>0){
		   HSSFSheet sheet=wb.createSheet();
		   sheet.setColumnWidth((short)0, (short)(97*250));
		   sheet.setColumnWidth((short)1, (short)(6*250));
		   sheet.setColumnWidth((short)2, (short)(11*250));
		   sheet.setColumnWidth((short)3, (short)(19*250));
		   sheet.setColumnWidth((short)4, (short)(17*250));
		   sheet.setColumnWidth((short)5, (short)(8*250));
		   sheet.setColumnWidth((short)6, (short)(32*250));
		   sheet.setColumnWidth((short)7, (short)(36*250));
		   sheet.setColumnWidth((short)8, (short)(8*250));
		   sheet.setColumnWidth((short)9, (short)(13*250));
		   sheet.setColumnWidth((short)10, (short)(8*250));
		   sheet.setColumnWidth((short)11, (short)(8*250));
		   sheet.setColumnWidth((short)12, (short)(22*250));
		   sheet.setColumnWidth((short)13, (short)(8*250));
		   sheet.setColumnWidth((short)14, (short)(12*250));
		   sheet.setColumnWidth((short)15, (short)(8*250));
		   sheet.setColumnWidth((short)16, (short)(8*250));
		   sheet.setColumnWidth((short)17, (short)(8*250));
		   sheet.setColumnWidth((short)18, (short)(8*250));
		   sheet.setColumnWidth((short)19, (short)(8*250));
		   sheet.setColumnWidth((short)20, (short)(17*250));
		   sheet.setColumnWidth((short)21, (short)(12*250));
		   sheet.setColumnWidth((short)22, (short)(8*250));
		   sheet.setColumnWidth((short)23, (short)(8*250));
		   sheet.setColumnWidth((short)24, (short)(6*250));
		   HSSFRow row0=sheet.createRow(0);
		   row0.setHeight((short)(600));
		   HSSFCell cell00=row0.createCell(0);
		   StringBuffer data=new StringBuffer();
		   data.append("*导入须知:1.表格中不能增、删、改列及固有内容2.所有内容必须为文本格式;表格中有多个档案名称字段是为了实现多语,如果没有多语只录第一个名称字段即可");
		   data.append("3.枚举项输入错误时，则按默认值处理;勾选框的导入需输入N、Y");
		   data.append("4.导入带有子表的档案时,表格中主表与子表之间必须有一空行,且主、子表对应数据需加上相同的行号");
		   cell00.setCellValue(data.toString());
		   HSSFCellStyle cellStyle=wb.createCellStyle();  
		   cellStyle.setWrapText(true); 
		   cell00.setCellStyle(cellStyle);
		   HSSFRow row1=sheet.createRow(1);
		   HSSFCell cell10=row1.createCell(0);
		   cell10.setCellValue("\"recbill_$head,billno,pk_org,billdate,busidate,objtype,customer,pk_deptid_v,pk_psndoc,pk_deptid,pk_currtype,pk_tradetype\"");
		   
		   HSSFCell cell11=row1.createCell(1);
		   cell11.setCellValue("* 单据号");
		   
		   HSSFCell cell12=row1.createCell(2);
		   cell12.setCellValue("* 应收财务组织");
		   
		   HSSFCell cell13=row1.createCell(3);
		   cell13.setCellValue("* 单据日期");
		   
		   HSSFCell cell14=row1.createCell(4);
		   cell14.setCellValue("* 起算日期");
		   
		   HSSFCell cell15=row1.createCell(5);
		   cell15.setCellValue("* 往来对象");
		   
		   HSSFCell cell16=row1.createCell(6);
		   cell16.setCellValue("* 客户");
		   
		   HSSFCell cell17=row1.createCell(7);
		   cell17.setCellValue("部门");
		   
		   HSSFCell cell18=row1.createCell(8);
		   cell18.setCellValue("* 业务员");
		   
		   HSSFCell cell19=row1.createCell(9);
		   cell19.setCellValue("* 部门");
		   
		   HSSFCell cell1110=row1.createCell(10);
		   cell1110.setCellValue("* 币种");
		   
		   HSSFCell cell111=row1.createCell(11);
		   cell111.setCellValue("* 应收类型code");
		   
		   for(int i=0;i<dataset1.size();i++){
			   HashMap<String, Object> map=dataset1.get(i);
			   HSSFRow row2=sheet.createRow(2+i);
			   HSSFCell cell20=row2.createCell(0);
			   if(map.get("COL1")!=null){
				   cell20.setCellValue(map.get("COL1").toString());
			   }else{
				   cell20.setCellValue("");   
			   }
			   
			   HSSFCell cell21=row2.createCell(1);
			   if(map.get("COL2")!=null){
				   cell21.setCellValue(map.get("COL2").toString());
			   }else{
				   cell21.setCellValue("");   
			   }
			   
			   HSSFCell cell22=row2.createCell(2);
			   if(map.get("COL3")!=null){
				   cell22.setCellValue(map.get("COL3").toString());
			   }else{
				   cell22.setCellValue("");   
			   }
			   
			   HSSFCell cell23=row2.createCell(3);
			   if(map.get("COL4")!=null){
				   cell23.setCellValue(map.get("COL4").toString());
			   }else{
				   cell23.setCellValue("");   
			   }
			   
			   HSSFCell cell24=row2.createCell(4);
			   if(map.get("COL5")!=null){
				   cell24.setCellValue(map.get("COL5").toString());
			   }else{
				   cell24.setCellValue("");   
			   }
			   
			   HSSFCell cell25=row2.createCell(5);
			   if(map.get("COL6")!=null){
				   cell25.setCellValue(map.get("COL6").toString());
			   }else{
				   cell25.setCellValue("");   
			   }
			   
			   HSSFCell cell26=row2.createCell(6);
			   if(map.get("COL7")!=null){
				   cell26.setCellValue(map.get("COL7").toString());
			   }else{
				   cell26.setCellValue("");   
			   }
			   
			   HSSFCell cell27=row2.createCell(7);
			   if(map.get("COL8")!=null){
				   cell27.setCellValue(map.get("COL8").toString());
			   }else{
				   cell27.setCellValue("");   
			   }
			   
			   HSSFCell cell28=row2.createCell(8);
			   if(map.get("COL9")!=null){
				   cell28.setCellValue(map.get("COL9").toString());
			   }else{
				   cell28.setCellValue("");   
			   }
			   
			   HSSFCell cell29=row2.createCell(9);
			   if(map.get("COL10")!=null){
				   cell29.setCellValue(map.get("COL10").toString());
			   }else{
				   cell29.setCellValue("");   
			   }
			   
			   HSSFCell cell210=row2.createCell(10);
			   if(map.get("COL11")!=null){
				   cell210.setCellValue(map.get("COL11").toString());
			   }else{
				   cell210.setCellValue("");   
			   }
			   
			   HSSFCell cell211=row2.createCell(11);
			   if(map.get("COL12")!=null){
				   cell211.setCellValue(map.get("COL12").toString());
			   }else{
				   cell211.setCellValue("");   
			   }
		   }
		   
		   HSSFRow row=sheet.createRow(dataset1.size()+3);		   
		   HSSFCell cellq0=row.createCell(0);
		   cellq0.setCellValue("\"bodys,contractno,purchaseorder,invoiceno,scomment,pk_payterm,objtype,customer,pk_deptid,subjcode,pk_psndoc,pk_currtype,money_de,rate,local_money_de,taxcodeid,taxrate,local_tax_de,notax_de,local_notax_de,def4,def1,def2,def3,buysellflag\"");
		   
		   HSSFCell cellq1=row.createCell(1);
		   cellq1.setCellValue("合同号");
		   
		   HSSFCell cellq2=row.createCell(2);
		   cellq2.setCellValue("提单号");
		   
		   HSSFCell cellq3=row.createCell(3);
		   cellq3.setCellValue("发票号");
		   
		   HSSFCell cellq4=row.createCell(4);
		   cellq4.setCellValue("摘要");
		   
		   HSSFCell cellq5=row.createCell(5);
		   cellq5.setCellValue("收款协议");
		   
		   HSSFCell cellq6=row.createCell(6);
		   cellq6.setCellValue("* 往来对象");
		   
		   HSSFCell cellq7=row.createCell(7);
		   cellq7.setCellValue("* 客户");
		   
		   HSSFCell cellq8=row.createCell(8);
		   cellq8.setCellValue("* 部门");
		   
		   HSSFCell cellq9=row.createCell(9);
		   cellq9.setCellValue("贷方科目");
		   
		   HSSFCell cellq10=row.createCell(10);
		   cellq10.setCellValue("* 业务员");
		   
		   HSSFCell cellq11=row.createCell(11);
		   cellq11.setCellValue("* 币种");
		   
		   HSSFCell cellq12=row.createCell(12);
		   cellq12.setCellValue("* 借方原币金额");
		   
		   HSSFCell cellq13=row.createCell(13);
		   cellq13.setCellValue("* 组织本币汇率");
		   
		   HSSFCell cellq14=row.createCell(14);
		   cellq14.setCellValue("组织本币金额");
		   
		   HSSFCell cellq15=row.createCell(15);
		   cellq15.setCellValue("* 税码");
		   
		   HSSFCell cellq16=row.createCell(16);
		   cellq16.setCellValue("* 税率");
		   
		   HSSFCell cellq17=row.createCell(17);
		   cellq17.setCellValue("税额");
		   
		   HSSFCell cellq18=row.createCell(18);
		   cellq18.setCellValue("借方原币无税金额");
		   
		   HSSFCell cellq19=row.createCell(19);
		   cellq19.setCellValue("组织本币无税金额");
		   
		   HSSFCell cellq20=row.createCell(20);
		   cellq20.setCellValue("* 起算日期");
		   
		   HSSFCell cellq21=row.createCell(21);
		   cellq21.setCellValue("项目");
		   
		   HSSFCell cellq22=row.createCell(22);
		   cellq22.setCellValue("所属期");
		   
		   HSSFCell cellq23=row.createCell(23);
		   cellq23.setCellValue("费用类型");
		   
		   HSSFCell cellq24=row.createCell(24);
		   cellq24.setCellValue("* 购销类型");
		   
		   
		   for(int i=0;i<dataset2.size();i++){
			   HashMap<String, Object> map=dataset2.get(i);
			   HSSFRow row3=sheet.createRow(i+dataset1.size()+4);
			   HSSFCell cell30=row3.createCell(0);
			   if(map.get("COL1")!=null){
				   cell30.setCellValue(map.get("COL1").toString());
			   }else{
				   cell30.setCellValue("");   
			   }
			   
			   HSSFCell cell31=row3.createCell(1);
			   if(map.get("COL2")!=null){
				   cell31.setCellValue(map.get("COL2").toString());
			   }else{
				   cell31.setCellValue("");   
			   }
			   
			   HSSFCell cell32=row3.createCell(2);
			   if(map.get("COL3")!=null){
				   cell32.setCellValue(map.get("COL3").toString());
			   }else{
				   cell32.setCellValue("");   
			   }
			   
			   HSSFCell cell33=row3.createCell(3);
			   if(map.get("COL4")!=null){
				   cell33.setCellValue(map.get("COL4").toString());
			   }else{
				   cell33.setCellValue("");   
			   }
			   
			   HSSFCell cell34=row3.createCell(4);
			   if(map.get("COL5")!=null){
				   cell34.setCellValue(map.get("COL5").toString());
			   }else{
				   cell34.setCellValue("");   
			   }
			   
			   HSSFCell cell35=row3.createCell(5);
			   if(map.get("COL6")!=null){
				   cell35.setCellValue(map.get("COL6").toString());
			   }else{
				   cell35.setCellValue("");   
			   }
			   
			   HSSFCell cell36=row3.createCell(6);
			   if(map.get("COL7")!=null){
				   cell36.setCellValue(map.get("COL7").toString());
			   }else{
				   cell36.setCellValue("");   
			   }
			   
			   HSSFCell cell37=row3.createCell(7);
			   if(map.get("COL8")!=null){
				   cell37.setCellValue(map.get("COL8").toString());
			   }else{
				   cell37.setCellValue("");   
			   }
			   
			   HSSFCell cell38=row3.createCell(8);
			   if(map.get("COL9")!=null){
				   cell38.setCellValue(map.get("COL9").toString());
			   }else{
				   cell38.setCellValue("");   
			   }
			   
			   HSSFCell cell39=row3.createCell(9);
			   if(map.get("COL10")!=null){
				   cell39.setCellValue(map.get("COL10").toString());
			   }else{
				   cell39.setCellValue("");   
			   }
			   
			   HSSFCell cell310=row3.createCell(10);
			   if(map.get("COL11")!=null){
				   cell310.setCellValue(map.get("COL11").toString());
			   }else{
				   cell310.setCellValue("");   
			   }
			   
			   HSSFCell cell311=row3.createCell(11);
			   if(map.get("COL12")!=null){
				   cell311.setCellValue(map.get("COL12").toString());
			   }else{
				   cell311.setCellValue("");   
			   }
			   
			   HSSFCell cell312=row3.createCell(12);
			   if(Hmap.get(map.get("COL26"))!=null){
				   cell312.setCellValue(Hmap.get(map.get("COL26")).toString());
			   }else{
				   cell312.setCellValue("0.00");   
			   }
			   
			   HSSFCell cell313=row3.createCell(13);
			   if(map.get("COL14")!=null){
				   cell313.setCellValue(map.get("COL14").toString());
			   }else{
				   cell313.setCellValue("");   
			   }
			   
			   HSSFCell cell314=row3.createCell(14);
			   if(Hmap.get(map.get("COL26"))!=null){
				   cell314.setCellValue(Hmap.get(map.get("COL26")).toString());
			   }else{
				   cell314.setCellValue("");   
			   }
			   
			   HSSFCell cell315=row3.createCell(15);
			   if(map.get("COL16")!=null){
				   cell315.setCellValue(map.get("COL16").toString());
			   }else{
				   cell315.setCellValue("");   
			   }
			   
			   HSSFCell cell316=row3.createCell(16);
			   if(map.get("COL17")!=null){
				   cell316.setCellValue(map.get("COL17").toString());
			   }else{
				   cell316.setCellValue("");   
			   }
			   
			   HSSFCell cell317=row3.createCell(17);
			   if(map.get("COL18")!=null){
				   cell317.setCellValue(map.get("COL18").toString());
			   }else{
				   cell317.setCellValue("");   
			   }
			   
			   HSSFCell cell318=row3.createCell(18);
			   if(map.get("COL19")!=null){
				   cell318.setCellValue(map.get("COL19").toString());
			   }else{
				   cell318.setCellValue("");   
			   }
			   
			   
			   HSSFCell cell319=row3.createCell(19);
			   if(map.get("COL20")!=null){
				   cell319.setCellValue(map.get("COL20").toString());
			   }else{
				   cell319.setCellValue("");   
			   }
			   
			   HSSFCell cell320=row3.createCell(20);
			   if(map.get("COL21")!=null){
				   cell320.setCellValue(map.get("COL21").toString());
			   }else{
				   cell320.setCellValue("");   
			   }
			   
			   HSSFCell cell321=row3.createCell(21);
			   if(map.get("COL22")!=null){
				   cell321.setCellValue(map.get("COL22").toString());
			   }else{
				   cell321.setCellValue("");   
			   }
			  
			   HSSFCell cell322=row3.createCell(22);
			   if(map.get("COL23")!=null){
				   cell322.setCellValue(map.get("COL23").toString());
			   }else{
				   cell322.setCellValue("");   
			   }
			   
			   HSSFCell cell323=row3.createCell(23);
			   if(map.get("COL24")!=null){
				   cell323.setCellValue(map.get("COL24").toString());
			   }else{
				   cell323.setCellValue("");   
			   }
			   
			   HSSFCell cell324=row3.createCell(24);
			   if(map.get("COL25")!=null){
				   cell324.setCellValue(map.get("COL25").toString());
			   }else{
				   cell324.setCellValue("");   
			   }
			   
		   }
		   		return wb;   
	   }
	   
	   return null;
   }
   
   public HSSFWorkbook InvoiceExportExcel( List<HashMap<String, Object>> dataset) {
	  
	   HSSFWorkbook wb = new HSSFWorkbook();
	   
	   if(dataset!=null&&dataset.size()>0){
			// 建立新的sheet对象（excel的表单）
		   HashMap<String, Object> map=dataset.get(0);
		   HSSFSheet sheet = wb.createSheet("sheet1");
		   sheet.setColumnWidth((short)0,(short)2800);
		   sheet.setColumnWidth((short)1, (short)9000);
		   sheet.setColumnWidth((short)2, (short)5000);
		   sheet.setColumnWidth((short)3, (short)5500);
		   sheet.setColumnWidth((short)4, (short)3500);
		   sheet.setColumnWidth((short)5, (short)3500);
		   sheet.setColumnWidth((short)6, (short)4000);
		   sheet.setColumnWidth((short)7, (short)4500);
		   sheet.setColumnWidth((short)8, (short)4000);
		   sheet.setColumnWidth((short)9, (short)3000);
		   sheet.setColumnWidth((short)10, (short)4500);
		   sheet.setColumnWidth((short)11, (short)5000);
		   sheet.setColumnWidth((short)12, (short)5500);
		   sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 12));
		   sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 12));
		   HSSFRow row0 = sheet.createRow(0);
		   row0.setHeight((short)600);
			// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		   HSSFCell cell01 = row0.createCell(0);
			// 设置单元格内容
		   cell01.setCellValue("中外运普菲斯亿达（上海）物流有限公司");
		   HSSFCellStyle cellStyle=wb.createCellStyle(); 
		   cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
		   cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   HSSFFont font = wb.createFont();
		   font.setFontHeightInPoints((short) 18);
		   font.setColor(HSSFColor.BLACK.index);
		   font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   font.setFontName("宋体");
		   cellStyle.setFont(font);
		   cell01.setCellStyle(cellStyle);
	
		   HSSFRow row1 = sheet.createRow(1);
		   row1.setHeight((short)600);
		   HSSFCell cell10 = row1.createCell(0);
		   cell10.setCellValue(ObjUtil.ifNull(map.get("BELONG_MONTH"),""));
		   HSSFCellStyle cellStyle1=wb.createCellStyle();  		  
		   cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   HSSFFont font1 = wb.createFont();
		   font1.setFontHeightInPoints((short) 12);
		   font1.setColor(HSSFColor.BLACK.index);
		   font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   font1.setFontName("宋体");
		   cellStyle1.setFont(font1);
		   cell10.setCellStyle(cellStyle1);
		   
		   HSSFRow row2 = sheet.createRow(2);
		   row2.setHeight((short)600);
		   HSSFCell cell20 = row2.createCell(0);
		   cell20.setCellValue("开票张数");
		   HSSFCellStyle cellStyle2=wb.createCellStyle();  		   
		   cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   cellStyle2.setTopBorderColor((short)10);
		   HSSFFont font2 = wb.createFont();
		   font2.setFontHeightInPoints((short) 10);
		   font2.setColor(HSSFColor.BLACK.index);
		   font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   font2.setFontName("宋体");
		   cellStyle2.setFont(font2);
		   cell20.setCellStyle(cellStyle2);
		   
		   HSSFCellStyle cellStyley=wb.createCellStyle();  
		   cellStyley.setFillBackgroundColor(HSSFColor.LIGHT_YELLOW.index);
		   cellStyley.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   cellStyley.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   cellStyley.setFont(font2);
		   HSSFCell cell21 = row2.createCell(1);
		   cell21.setCellValue("购货单位名称");
		   cell21.setCellStyle(cellStyley);
		   
		   HSSFCell cell22 = row2.createCell(2);
		   cell22.setCellValue("税号");
		   cell22.setCellStyle(cellStyley);
		   	   
		   HSSFCell cell23 = row2.createCell(3);
		   cell23.setCellValue("货物或应税劳务名称");
		   cell23.setCellStyle(cellStyley);
		   
		   HSSFCell cell24 = row2.createCell(4);
		   cell24.setCellValue("规格型号");
		   cell24.setCellStyle(cellStyle2);
		   
		   HSSFCell cell25 = row2.createCell(5);
		   cell25.setCellValue("单位");
		   cell25.setCellStyle(cellStyle2);
		   
		   HSSFCell cell26 = row2.createCell(6);
		   cell26.setCellValue("数量（车）");
		   cell26.setCellStyle(cellStyley);
		   		   
		   HSSFCell cell27 = row2.createCell(7);
		   cell27.setCellValue("单价");
		   cell27.setCellStyle(cellStyley);
		   
		   HSSFCell cell28 = row2.createCell(8);
		   cell28.setCellValue("金额");
		   cell28.setCellStyle(cellStyley);
		   
		   HSSFCell cell29 = row2.createCell(9);
		   cell29.setCellValue("税率");
		   cell29.setCellStyle(cellStyley); 
		   
		   HSSFCell cell210 = row2.createCell(10);
		   cell210.setCellValue("税额");
		   cell210.setCellStyle(cellStyley); 
		   
		   
		   HSSFCell cell211 = row2.createCell(11);
		   cell211.setCellValue("价税合计");
		   cell211.setCellStyle(cellStyley); 
		   
		   HSSFCell cell212 = row2.createCell(12);
		   cell212.setCellValue("备注");
		   cell212.setCellStyle(cellStyle2); 
		   		    
		   HSSFCellStyle cellStyleNum=wb.createCellStyle();  
		   cellStyleNum.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));  
		   cellStyleNum.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		   cellStyleNum.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   cellStyleNum.setFont(font2);
		   
		   DecimalFormat df = new DecimalFormat("######0.00");
		   for(int i=0;i<dataset.size();i++){
			   HashMap<String, Object> mapD=dataset.get(i);
			   HSSFRow rowData = sheet.createRow(i+3);
			   rowData.setHeight((short)600);
			   HSSFCell cellD0 = rowData.createCell(0);
			   cellD0.setCellValue(ObjUtil.ifNull(mapD.get("BILL_COUNT"), ""));
			   cellD0.setCellStyle(cellStyle2);
				
			   HSSFCell cellD1 = rowData.createCell(1);
			   cellD1.setCellValue(ObjUtil.ifNull(mapD.get("BELONG_BUSS_NAME"), ""));	  			   
			   cellD1.setCellStyle(cellStyle2);
			   
			   HSSFCell cellD2 = rowData.createCell(2);
			   cellD2.setCellValue(ObjUtil.ifNull(mapD.get("TAX_RATION"), ""));
			   cellD2.setCellStyle(cellStyle2);
			   
			   HSSFCell cellD3 = rowData.createCell(3);
			   if(mapD.get("BELONG_MONTH")!=null){
				   String month=mapD.get("BELONG_MONTH").toString();
				   String m=month.substring(4);
				   if(m.startsWith("1")){
					   cellD3.setCellValue(m+"月运费");
				   }else{
					   cellD3.setCellValue(m.substring(1)+"月运费");
				   }
			   }else{
				   cellD3.setCellValue("");
			   }
			   cellD3.setCellStyle(cellStyle2);
			   
			   HSSFCell cellD6 = rowData.createCell(6);
			   if(mapD.get("BILL_SUM")!=null){
			   cellD6.setCellValue(Double.parseDouble((mapD.get("BILL_SUM").toString())));
			   }else{
				   cellD6.setCellValue((double)0);
			   }
			   cellD6.setCellStyle(cellStyle2);
			   
			   HSSFCell cellD7 = rowData.createCell(7);
			   if(mapD.get("ACT_AMOUNT")!=null){
			   cellD7.setCellValue(Double.parseDouble(df.format(Double.parseDouble(mapD.get("ACT_AMOUNT").toString()))));
			   }else{
				   cellD7.setCellValue((double)0.00);
			   }
			   cellD7.setCellStyle(cellStyle2);
			   
			   HSSFCell cellD8 = rowData.createCell(8);
			   if(mapD.get("SUBTAX_AMOUNT")!=null){
			   cellD8.setCellValue(Double.parseDouble(df.format(Double.parseDouble(mapD.get("SUBTAX_AMOUNT").toString()))));
			   }else{
				   cellD8.setCellValue((double)0.00);
			   }
			   cellD8.setCellStyle(cellStyle2);
			   
			   HSSFCell cellD9 = rowData.createCell(9);
			   if(mapD.get("TAX_RATION")!=null){
			   cellD9.setCellValue((Double.parseDouble(mapD.get("TAX_RATION").toString()))/100);
			   }else{
				   cellD9.setCellValue((double)0.00);
			   }
			   cellD9.setCellStyle(cellStyleNum); 
			   
			   HSSFCell cellD10 = rowData.createCell(10);
			   if(mapD.get("TAX_AMOUNT")!=null){
			   cellD10.setCellValue(Double.parseDouble(df.format(Double.parseDouble(mapD.get("TAX_AMOUNT").toString()))));
			   }else{
				   cell10.setCellValue((double)0.00);
			   }
			   cellD10.setCellStyle(cellStyle2);
			   
			   HSSFCell cellD11 = rowData.createCell(11);
			   if(mapD.get("ACT_AMOUNT")!=null){
			   cellD11.setCellValue(Double.parseDouble(df.format(Double.parseDouble(mapD.get("ACT_AMOUNT").toString()))));
			   }else{
				   cellD11.setCellValue((double)0.00);
			   }
			   cellD11.setCellStyle(cellStyle2);
			   
			   HSSFCell cellD12 = rowData.createCell(12);
			   cellD12.setCellValue(ObjUtil.ifNull(mapD.get("NOTES"), ""));	 
			   cellD12.setCellStyle(cellStyle2);
			   
		   }
		   
		   
		   int size=dataset.size()+3;	
		   HashMap<String, Object> mapD=dataset.get(0);
		   
		   HSSFRow rowAdd = sheet.createRow(size);
		   rowAdd.setHeight((short)600);
		   HSSFCell cellAdd = rowAdd.createCell(0);
		   cellAdd.setCellValue("合计");
		   cellAdd.setCellStyle(cellStyle2);   
		   
		   HSSFCell cellAdd1 = rowAdd.createCell(1);
		   cellAdd1.setCellValue(ObjUtil.ifNull(mapD.get("BELONG_BUSS_NAME"), ""));	  			   
		   cellAdd1.setCellStyle(cellStyle2);
		   
		   HSSFCell cellAdd2 = rowAdd.createCell(2);
		   cellAdd2.setCellValue(ObjUtil.ifNull(mapD.get("TAX_RATION"), ""));
		   cellAdd2.setCellStyle(cellStyle2);
		   
		   HSSFCell cellAdd3 = rowAdd.createCell(3);
		   if(mapD.get("BELONG_MONTH")!=null){
			   String month=mapD.get("BELONG_MONTH").toString();
			   String m=month.substring(4);
			   if(m.startsWith("1")){
				   cellAdd3.setCellValue(m+"月运费");
			   }else{
				   cellAdd3.setCellValue(m.substring(1)+"月运费");
			   }
		   }else{
			   cellAdd3.setCellValue("");
		   }
		   cellAdd3.setCellStyle(cellStyle2);
		   
		   HSSFCell cellAdd6 = rowAdd.createCell(6);
		   cellAdd6.setCellFormula("SUBTOTAL(9,G4:G"+size+")");
		   cellAdd6.setCellStyle(cellStyle2);   
		   
		   HSSFCell cellAdd7 = rowAdd.createCell(7);
		   cellAdd7.setCellFormula("SUBTOTAL(9,H4:H"+size+")");
		   cellAdd7.setCellStyle(cellStyle2);  
		   
		   HSSFCell cellAdd8 = rowAdd.createCell(8);
		   cellAdd8.setCellFormula("SUBTOTAL(9,I4:I"+size+")");
		   cellAdd8.setCellStyle(cellStyle2); 
		   
		   HSSFCell cellAdd9 = rowAdd.createCell(9);
		   cellAdd9.setCellFormula("SUBTOTAL(9,J4:J"+size+")");
		   cellAdd9.setCellStyle(cellStyleNum); 
		   
		   HSSFCell cellAdd10 = rowAdd.createCell(10);
		   cellAdd10.setCellFormula("SUBTOTAL(9,K4:K"+size+")");
		   cellAdd10.setCellStyle(cellStyle2); 
		      
		   HSSFCell cellAdd11 = rowAdd.createCell(11);
		   cellAdd11.setCellFormula("SUBTOTAL(9,L4:L"+size+")");
		   cellAdd11.setCellStyle(cellStyle2); 

		   return wb;
	   
	   }else{
		   return null;
	   }
   }
   
   public HSSFWorkbook PayInitBillExportExcel1(List<HashMap<String, Object>> dataset1,List<HashMap<String, Object>> dataset2,HashMap<String, Object> obj,List<HashMap<String, Object>> dataset3) {
	   HSSFWorkbook wb = new HSSFWorkbook(); 
	   if(dataset1!=null&&dataset1.size()>0){
		   HSSFSheet sheet=wb.createSheet();
		   sheet.setColumnWidth((short)0, (short)(97*250));
		   sheet.setColumnWidth((short)1, (short)(6*250));
		   sheet.setColumnWidth((short)2, (short)(11*250));
		   sheet.setColumnWidth((short)3, (short)(19*250));
		   sheet.setColumnWidth((short)4, (short)(17*250));
		   sheet.setColumnWidth((short)5, (short)(8*250));
		   sheet.setColumnWidth((short)6, (short)(32*250));
		   sheet.setColumnWidth((short)7, (short)(36*250));
		   sheet.setColumnWidth((short)8, (short)(8*250));
		   sheet.setColumnWidth((short)9, (short)(13*250));
		   sheet.setColumnWidth((short)10, (short)(8*250));
		   sheet.setColumnWidth((short)11, (short)(8*250));
		   sheet.setColumnWidth((short)12, (short)(22*250));
		   sheet.setColumnWidth((short)13, (short)(8*250));
		   sheet.setColumnWidth((short)14, (short)(12*250));
		   sheet.setColumnWidth((short)15, (short)(8*250));
		   sheet.setColumnWidth((short)16, (short)(8*250));
		   sheet.setColumnWidth((short)17, (short)(8*250));
		   sheet.setColumnWidth((short)18, (short)(8*250));
		   sheet.setColumnWidth((short)19, (short)(8*250));
		   sheet.setColumnWidth((short)20, (short)(17*250));
		   sheet.setColumnWidth((short)21, (short)(12*250));
		   sheet.setColumnWidth((short)22, (short)(8*250));
		   sheet.setColumnWidth((short)23, (short)(8*250));
		   sheet.setColumnWidth((short)24, (short)(6*250));
		   sheet.setColumnWidth((short)25, (short)(6*250));
		   sheet.setColumnWidth((short)26, (short)(9*250));
		   HSSFRow row0=sheet.createRow(0);
		   row0.setHeight((short)(16*87));
		   HSSFCell cell00=row0.createCell(0);
		   StringBuffer data=new StringBuffer();
		   data.append("*导入须知:1.表格中不能增、删、改列及固有内容2.所有内容必须为文本格式;表格中有多个档案名称字段是为了实现多语,如果没有多语只录第一个名称字段即可");
		   data.append("3.枚举项输入错误时，则按默认值处理;勾选框的导入需输入N、Y");
		   data.append("4.导入带有子表的档案时,表格中主表与子表之间必须有一空行,且主、子表对应数据需加上相同的行号");
		   cell00.setCellValue(data.toString());
		   HSSFCellStyle cellStyle=wb.createCellStyle();  
		   cellStyle.setWrapText(true); 
		   cell00.setCellStyle(cellStyle);
		   HSSFRow row1=sheet.createRow(1);
		   HSSFCell cell10=row1.createCell(0);
		   cell10.setCellValue("\"payablebill_$head,pk_org,billno,billdate,busidate,objtype,supplier,pk_deptid_v,pk_psndoc,pk_deptid,pk_currtype,pk_tradetype\"");
		   
		   HSSFCell cell11=row1.createCell(1);
		   cell11.setCellValue("* 应收财务组织");
		   
		   HSSFCell cell12=row1.createCell(2);
		   cell12.setCellValue("* 单据号");
		   
		   HSSFCell cell13=row1.createCell(3);
		   cell13.setCellValue("* 单据日期");
		   
		   HSSFCell cell14=row1.createCell(4);
		   cell14.setCellValue("* 起算日期");
		   
		   HSSFCell cell15=row1.createCell(5);
		   cell15.setCellValue("* 往来对象");
		   
		   HSSFCell cell16=row1.createCell(6);
		   cell16.setCellValue("* 供应商");
		   
		   HSSFCell cell17=row1.createCell(7);
		   cell17.setCellValue("部门");
		   
		   HSSFCell cell18=row1.createCell(8);
		   cell18.setCellValue("* 业务员");
		   
		   HSSFCell cell19=row1.createCell(9);
		   cell19.setCellValue("* 部门");
		   
		   HSSFCell cell1110=row1.createCell(10);
		   cell1110.setCellValue("* 币种");
		   
		   HSSFCell cell111=row1.createCell(11);
		   cell111.setCellValue("* 应收类型code");
		   
		   for(int i=0;i<1;i++){
			   HashMap<String, Object> map=dataset1.get(i);
			   HSSFRow row2=sheet.createRow(2+i);
			   HSSFCell cell20=row2.createCell(0);
			   if(map.get("COL1")!=null){
				   cell20.setCellValue(map.get("COL1").toString());
			   }else{
				   cell20.setCellValue("");   
			   }
			   
			   HSSFCell cell21=row2.createCell(1);
			   if(map.get("COL2")!=null){
				   cell21.setCellValue(map.get("COL2").toString());
			   }else{
				   cell21.setCellValue("");   
			   }
			   
			   HSSFCell cell22=row2.createCell(2);
			   if(map.get("COL3")!=null){
				   cell22.setCellValue(map.get("COL3").toString());
			   }else{
				   cell22.setCellValue("");   
			   }
			   
			   HSSFCell cell23=row2.createCell(3);
			   if(map.get("COL4")!=null){
				   cell23.setCellValue(map.get("COL4").toString());
			   }else{
				   cell23.setCellValue("");   
			   }
			   
			   HSSFCell cell24=row2.createCell(4);
			   if(map.get("COL5")!=null){
				   cell24.setCellValue(map.get("COL5").toString());
			   }else{
				   cell24.setCellValue("");   
			   }
			   
			   HSSFCell cell25=row2.createCell(5);
			   if(map.get("COL6")!=null){
				   cell25.setCellValue(map.get("COL6").toString());
			   }else{
				   cell25.setCellValue("");   
			   }
			   
			   HSSFCell cell26=row2.createCell(6);
			   if(map.get("COL7")!=null){
				   cell26.setCellValue(map.get("COL7").toString());
			   }else{
				   cell26.setCellValue("");   
			   }
			   
			   HSSFCell cell27=row2.createCell(7);
			   if(map.get("COL8")!=null){
				   cell27.setCellValue(map.get("COL8").toString());
			   }else{
				   cell27.setCellValue("");   
			   }
			   
			   HSSFCell cell28=row2.createCell(8);
			   if(map.get("COL9")!=null){
				   cell28.setCellValue(map.get("COL9").toString());
			   }else{
				   cell28.setCellValue("");   
			   }
			   
			   HSSFCell cell29=row2.createCell(9);
			   if(map.get("COL10")!=null){
				   cell29.setCellValue(map.get("COL10").toString());
			   }else{
				   cell29.setCellValue("");   
			   }
			   
			   HSSFCell cell210=row2.createCell(10);
			   if(map.get("COL11")!=null){
				   cell210.setCellValue(map.get("COL11").toString());
			   }else{
				   cell210.setCellValue("");   
			   }
			   
			   HSSFCell cell211=row2.createCell(11);
			   if(map.get("COL12")!=null){
				   cell211.setCellValue(map.get("COL12").toString());
			   }else{
				   cell211.setCellValue("");   
			   }
		   }
		   
		   HSSFRow row=sheet.createRow(1+3);		   
		   HSSFCell cellq0=row.createCell(0);
		   cellq0.setCellValue("\"bodys,contractno,purchaseorder,invoiceno,scomment,pk_payterm,objtype,supplier,pk_deptid,pk_psndoc,subjcode,def5,pk_currtype,rate,money_cr,local_money_cr,taxcodeid,taxrate,local_tax_cr,notax_cr,local_notax_cr,busidate,def3,def2,def1,def4,buysellflag\"");
		   
		   HSSFCell cellq1=row.createCell(1);
		   cellq1.setCellValue("合同号");
		   
		   HSSFCell cellq2=row.createCell(2);
		   cellq2.setCellValue("提单号");
		   
		   HSSFCell cellq3=row.createCell(3);
		   cellq3.setCellValue("发票号");
		   
		   HSSFCell cellq4=row.createCell(4);
		   cellq4.setCellValue("摘要");
		   
		   HSSFCell cellq5=row.createCell(5);
		   cellq5.setCellValue("付款协议");
		   
		   HSSFCell cellq6=row.createCell(6);
		   cellq6.setCellValue("* 往来对象");
		   
		   HSSFCell cellq7=row.createCell(7);
		   cellq7.setCellValue("* 供应商");
		   
		   HSSFCell cellq8=row.createCell(8);
		   cellq8.setCellValue("* 部门");
		   
		   HSSFCell cellq9=row.createCell(9);
		   cellq9.setCellValue("* 业务员");
		   
		   HSSFCell cellq10=row.createCell(10);
		   cellq10.setCellValue("* 借方科目");
		   
		   HSSFCell cellq11=row.createCell(11);
		   cellq11.setCellValue("客户（结转成本）");
		   
		   HSSFCell cellq12=row.createCell(12);
		   cellq12.setCellValue("* 币种");
		   
		   HSSFCell cellq13=row.createCell(13);
		   cellq13.setCellValue("* 组织本币汇率");
		   
		   HSSFCell cellq14=row.createCell(14);
		   cellq14.setCellValue("* 贷方原币金额");
		   
		   HSSFCell cellq15=row.createCell(15);
		   cellq15.setCellValue("组织本币金额");
		   
		   HSSFCell cellq16=row.createCell(16);
		   cellq16.setCellValue("* 税码");
		   
		   HSSFCell cellq17=row.createCell(17);
		   cellq17.setCellValue("* 税率");
		   
		   HSSFCell cellq18=row.createCell(18);
		   cellq18.setCellValue("税额");
		   
		   HSSFCell cellq19=row.createCell(19);
		   cellq19.setCellValue("贷方原币无税金额");
		   
		   HSSFCell cellq20=row.createCell(20);
		   cellq20.setCellValue("组织本币无税金额");
		   
		   HSSFCell cellq21=row.createCell(21);
		   cellq21.setCellValue("* 起算日期");
		   
		   HSSFCell cellq22=row.createCell(22);
		   cellq22.setCellValue("费用类型");
		   
		   HSSFCell cellq23=row.createCell(23);
		   cellq23.setCellValue("所属期");
		   
		   HSSFCell cellq24=row.createCell(24);
		   cellq24.setCellValue("项目");
		   
		   HSSFCell cellq25=row.createCell(25);
		   cellq25.setCellValue("备注");
		   
		   HSSFCell cellq26=row.createCell(26);
		   cellq26.setCellValue("* 购销类型");
		   
		   HashMap<String, Object> amount= dataset3.get(0);
		   
		   HashMap<String, Object> mapK=dataset2.get(0);
		   HSSFRow rowk=sheet.createRow(1+4);		   
		   HSSFCell cellk0=rowk.createCell(0);
		   cellk0.setCellValue("0");
		   
		   HSSFCell cellk1=rowk.createCell(1);
		   cellk1.setCellValue("");
		   
		   HSSFCell cellk2=rowk.createCell(2);
		   if(mapK.get("COL3")!=null){
			   cellk2.setCellValue(mapK.get("COL3").toString());
		   }else{
			   cellk2.setCellValue("");
		   }
		   
		   
		   HSSFCell cellk3=rowk.createCell(3);
		   if(mapK.get("COL3")!=null){
			   cellk3.setCellValue(mapK.get("COL3").toString());
		   }else{
			   cellk3.setCellValue("");
		   }
		   
		   HSSFCell cellk4=rowk.createCell(4);
		   if(mapK.get("COL3")!=null){
			   cellk4.setCellValue(mapK.get("COL3").toString());
		   }else{
			   cellk4.setCellValue("");
		   }
		  // cellk4.setCellValue("摘要");
		   
		   HSSFCell cellk5=rowk.createCell(5);
		   if(mapK.get("COL6")!=null){
			   cellk5.setCellValue(mapK.get("COL6").toString());
		   }else{
			   cellk5.setCellValue("");
		   }
		   
		   HSSFCell cellk6=rowk.createCell(6);
		   cellk6.setCellValue("供应商");
		   
		   HSSFCell cellk7=rowk.createCell(7);
		   cellk7.setCellValue("S0001");
		   
		   HSSFCell cellk8=rowk.createCell(8);
		   if(mapK.get("COL9")!=null){
			   cellk8.setCellValue(mapK.get("COL9").toString());
		   }else{
			   cellk8.setCellValue("");
		   }
		   
		   HSSFCell cellk9=rowk.createCell(9);
		   if(mapK.get("COL10")!=null){
			   cellk9.setCellValue(mapK.get("COL10").toString());
		   }else{
			   cellk9.setCellValue("");
		   }
		   
		   HSSFCell cellk10=rowk.createCell(10);
		   cellk10.setCellValue("199903");
		   
		   HSSFCell cellk11=rowk.createCell(11);
		   cellk11.setCellValue("");
		   
		   HSSFCell cellk12=rowk.createCell(12);
		   if(mapK.get("COL13")!=null){
			   cellk12.setCellValue(mapK.get("COL13").toString());
		   }else{
			   cellk12.setCellValue("");
		   }
		   
		   HSSFCell cellk13=rowk.createCell(13);
		   if(mapK.get("COL14")!=null){
			   cellk13.setCellValue(mapK.get("COL14").toString());
		   }else{
			   cellk13.setCellValue("");
		   }
		   
		   HSSFCell cellk14=rowk.createCell(14);
		   if(amount.get("ALL_AMOUNT")!=null){
			   cellk14.setCellValue(amount.get("ALL_AMOUNT").toString());
		   }else{
			   cellk14.setCellValue("");
		   }
		   
		   HSSFCell cellk15=rowk.createCell(15);
		   if(amount.get("ALL_AMOUNT")!=null){
			   cellk15.setCellValue(amount.get("ALL_AMOUNT").toString());
		   }else{
			   cellk15.setCellValue("");
		   }
		   
		   HSSFCell cellk16=rowk.createCell(16);
		   if(mapK.get("COL17")!=null){
			   cellk16.setCellValue(mapK.get("COL17").toString());
		   }else{
			   cellk16.setCellValue("");
		   }
		   
		   HSSFCell cellk17=rowk.createCell(17);
		   if(mapK.get("COL18")!=null){
			   cellk17.setCellValue(mapK.get("COL18").toString());
		   }else{
			   cellk17.setCellValue("0");
		   }
		   
		   HSSFCell cellk18=rowk.createCell(18);
		   cellk18.setCellValue("");
		   
		   HSSFCell cellk19=rowk.createCell(19);
		   cellk19.setCellValue("");
		   
		   HSSFCell cellk20=rowk.createCell(20);
		   cellk20.setCellValue("");
		   
		   HSSFCell cellk21=rowk.createCell(21);
		   if(mapK.get("COL22")!=null){
			   cellk21.setCellValue(mapK.get("COL22").toString());
		   }else{
			   cellk21.setCellValue("");
		   }
		   
		   HSSFCell cellk22=rowk.createCell(22);
		   cellk22.setCellValue("");
		   
		   HSSFCell cellk23=rowk.createCell(23);
		   if(mapK.get("COL24")!=null){
			   cellk23.setCellValue(mapK.get("COL24").toString());
		   }else{
			   cellk23.setCellValue("");
		   }
		   
		   HSSFCell cellk24=rowk.createCell(24);
		   if(mapK.get("COL25")!=null){
			   cellk24.setCellValue(mapK.get("COL25").toString());
		   }else{
			   cellk24.setCellValue("");
		   }
		   
		   HSSFCell cellk25=rowk.createCell(25);
		   cellk25.setCellValue("");
		   
		   HSSFCell cellk26=rowk.createCell(26);
		   if(mapK.get("COL27")!=null){
			   cellk26.setCellValue(mapK.get("COL27").toString());
		   }else{
			   cellk26.setCellValue("");
		   }
		   
		   for(int i=0;i<dataset2.size();i++){
			   HashMap<String, Object> map=dataset2.get(i);
			   HSSFRow row3=sheet.createRow(i+1+5);
			   HSSFCell cell30=row3.createCell(0);
//			   if(map.get("COL1")!=null){
//				   cell30.setCellValue(map.get("COL1").toString());
//			   }else{
//				   cell30.setCellValue("");   
//			   }
			   cell30.setCellValue("0"); 
			   
			   HSSFCell cell31=row3.createCell(1);
			   if(map.get("COL2")!=null){
				   cell31.setCellValue(map.get("COL2").toString());
			   }else{
				   cell31.setCellValue("");   
			   }
			   
			   HSSFCell cell32=row3.createCell(2);
			   if(map.get("COL3")!=null){
				   cell32.setCellValue(map.get("COL3").toString());
			   }else{
				   cell32.setCellValue("");   
			   }
			   
			   HSSFCell cell33=row3.createCell(3);
			   if(map.get("COL4")!=null){
				   cell33.setCellValue(map.get("COL4").toString());
			   }else{
				   cell33.setCellValue("");   
			   }
			   
			   HSSFCell cell34=row3.createCell(4);
			   if(map.get("COL5")!=null){
				   cell34.setCellValue(map.get("COL5").toString());
			   }else{
				   cell34.setCellValue("");   
			   }
			   
			   HSSFCell cell35=row3.createCell(5);
			   if(map.get("COL6")!=null){
				   cell35.setCellValue(map.get("COL6").toString());
			   }else{
				   cell35.setCellValue("");   
			   }
			   
			   HSSFCell cell36=row3.createCell(6);
			   if(map.get("COL7")!=null){
				   cell36.setCellValue(map.get("COL7").toString());
			   }else{
				   cell36.setCellValue("");   
			   }
			   
			   HSSFCell cell37=row3.createCell(7);
			   if(map.get("COL8")!=null){
				   cell37.setCellValue(map.get("COL8").toString());
			   }else{
				   cell37.setCellValue("");   
			   }
			   
			   HSSFCell cell38=row3.createCell(8);
			   if(map.get("COL9")!=null){
				   cell38.setCellValue(map.get("COL9").toString());
			   }else{
				   cell38.setCellValue("");   
			   }
			   
			   HSSFCell cell39=row3.createCell(9);
			   if(map.get("COL10")!=null){
				   cell39.setCellValue(map.get("COL10").toString());
			   }else{
				   cell39.setCellValue("");   
			   }
			   
			   HSSFCell cell310=row3.createCell(10);
			   if(map.get("COL11")!=null){
				   cell310.setCellValue(map.get("COL11").toString());
			   }else{
				   cell310.setCellValue("");   
			   }
			   
			   HSSFCell cell311=row3.createCell(11);
			   if(map.get("COL12")!=null){
				   cell311.setCellValue(map.get("COL12").toString());
			   }else{
				   cell311.setCellValue("");   
			   }
			   
			   HSSFCell cell312=row3.createCell(12);
			   if(map.get("COL13")!=null){
				   cell312.setCellValue(map.get("COL13").toString());
			   }else{
				   cell312.setCellValue("");   
			   }
			   
			   HSSFCell cell313=row3.createCell(13);
			   if(map.get("COL14")!=null){
				   cell313.setCellValue(map.get("COL14").toString());
			   }else{
				   cell313.setCellValue("");   
			   }
			   
			   HSSFCell cell314=row3.createCell(14);
			   if(obj.get(map.get("COL5"))!=null){
				   cell314.setCellValue(obj.get(map.get("COL5")).toString());
			   }else{
				   cell314.setCellValue("");   
			   }
			   
			   HSSFCell cell315=row3.createCell(15);
			   if(obj.get(map.get("COL5"))!=null){
				   cell315.setCellValue(obj.get(map.get("COL5")).toString());
			   }else{
				   cell315.setCellValue("");   
			   }
			   
			   HSSFCell cell316=row3.createCell(16);
			   if(map.get("COL17")!=null){
				   cell316.setCellValue(map.get("COL17").toString());
			   }else{
				   cell316.setCellValue("");   
			   }
			   
			   HSSFCell cell317=row3.createCell(17);
//			   if(map.get("COL18")!=null){
//				   cell317.setCellValue(map.get("COL18").toString());
//			   }else{
				   cell317.setCellValue("0");   
			   //}
			   
			   HSSFCell cell318=row3.createCell(18);
			   if(map.get("COL19")!=null){
				   cell318.setCellValue(map.get("COL19").toString());
			   }else{
				   cell318.setCellValue("");   
			   }
			   
			   
			   HSSFCell cell319=row3.createCell(19);
			   if(map.get("COL20")!=null){
				   cell319.setCellValue(map.get("COL20").toString());
			   }else{
				   cell319.setCellValue("");   
			   }
			   
			   HSSFCell cell320=row3.createCell(20);
			   if(map.get("COL21")!=null){
				   cell320.setCellValue(map.get("COL21").toString());
			   }else{
				   cell320.setCellValue("");   
			   }
			   
			   HSSFCell cell321=row3.createCell(21);
			   if(map.get("COL22")!=null){
				   cell321.setCellValue(map.get("COL22").toString());
			   }else{
				   cell321.setCellValue("");   
			   }
			  
			   HSSFCell cell322=row3.createCell(22);
			   if(map.get("COL23")!=null){
				   cell322.setCellValue(map.get("COL23").toString());
			   }else{
				   cell322.setCellValue("");   
			   }
			   
			   HSSFCell cell323=row3.createCell(23);
			   if(map.get("COL24")!=null){
				   cell323.setCellValue(map.get("COL24").toString());
			   }else{
				   cell323.setCellValue("");   
			   }
			   
			   HSSFCell cell324=row3.createCell(24);
			   if(map.get("COL25")!=null){
				   cell324.setCellValue(map.get("COL25").toString());
			   }else{
				   cell324.setCellValue("");   
			   }
			   
			   HSSFCell cell325=row3.createCell(25);
			   if(map.get("COL26")!=null){
				   cell325.setCellValue(map.get("COL26").toString());
			   }else{
				   cell325.setCellValue("");   
			   } 
			   
			   HSSFCell cell326=row3.createCell(26);
			   if(map.get("COL27")!=null){
				   cell326.setCellValue(map.get("COL27").toString());
			   }else{
				   cell326.setCellValue("");   
			   }
			   
		   }
		   		return wb;   
	   }
	   
	   return null;
   }
   
   public HSSFWorkbook PayInitBillExportExcel2(List<HashMap<String, Object>> dataset1,List<HashMap<String, Object>> dataset2,HashMap<String, Object> mapJ) {
	   HSSFWorkbook wb = new HSSFWorkbook(); 
	   if(dataset1!=null&&dataset1.size()>0){
		   HSSFSheet sheet=wb.createSheet();
		   sheet.setColumnWidth((short)0, (short)(97*250));
		   sheet.setColumnWidth((short)1, (short)(6*250));
		   sheet.setColumnWidth((short)2, (short)(11*250));
		   sheet.setColumnWidth((short)3, (short)(19*250));
		   sheet.setColumnWidth((short)4, (short)(17*250));
		   sheet.setColumnWidth((short)5, (short)(8*250));
		   sheet.setColumnWidth((short)6, (short)(32*250));
		   sheet.setColumnWidth((short)7, (short)(36*250));
		   sheet.setColumnWidth((short)8, (short)(8*250));
		   sheet.setColumnWidth((short)9, (short)(13*250));
		   sheet.setColumnWidth((short)10, (short)(8*250));
		   sheet.setColumnWidth((short)11, (short)(8*250));
		   sheet.setColumnWidth((short)12, (short)(22*250));
		   sheet.setColumnWidth((short)13, (short)(8*250));
		   sheet.setColumnWidth((short)14, (short)(12*250));
		   sheet.setColumnWidth((short)15, (short)(8*250));
		   sheet.setColumnWidth((short)16, (short)(8*250));
		   sheet.setColumnWidth((short)17, (short)(8*250));
		   sheet.setColumnWidth((short)18, (short)(8*250));
		   sheet.setColumnWidth((short)19, (short)(8*250));
		   sheet.setColumnWidth((short)20, (short)(17*250));
		   sheet.setColumnWidth((short)21, (short)(12*250));
		   sheet.setColumnWidth((short)22, (short)(8*250));
		   sheet.setColumnWidth((short)23, (short)(8*250));
		   sheet.setColumnWidth((short)24, (short)(6*250));
		   sheet.setColumnWidth((short)25, (short)(6*250));
		   sheet.setColumnWidth((short)26, (short)(9*250));
		   HSSFRow row0=sheet.createRow(0);
		   row0.setHeight((short)(16*87));
		   HSSFCell cell00=row0.createCell(0);
		   StringBuffer data=new StringBuffer();
		   data.append("*导入须知:1.表格中不能增、删、改列及固有内容2.所有内容必须为文本格式;表格中有多个档案名称字段是为了实现多语,如果没有多语只录第一个名称字段即可");
		   data.append("3.枚举项输入错误时，则按默认值处理;勾选框的导入需输入N、Y");
		   data.append("4.导入带有子表的档案时,表格中主表与子表之间必须有一空行,且主、子表对应数据需加上相同的行号");
		   cell00.setCellValue(data.toString());
		   HSSFCellStyle cellStyle=wb.createCellStyle();  
		   cellStyle.setWrapText(true); 
		   cell00.setCellStyle(cellStyle);
		   HSSFRow row1=sheet.createRow(1);
		   HSSFCell cell10=row1.createCell(0);
		   cell10.setCellValue("\"payablebill_$head,pk_org,billno,billdate,busidate,objtype,supplier,pk_deptid_v,pk_psndoc,pk_deptid,pk_currtype,pk_tradetype\"");
		   
		   HSSFCell cell11=row1.createCell(1);
		   cell11.setCellValue("* 应收财务组织");
		   
		   HSSFCell cell12=row1.createCell(2);
		   cell12.setCellValue("* 单据号");
		   
		   HSSFCell cell13=row1.createCell(3);
		   cell13.setCellValue("* 单据日期");
		   
		   HSSFCell cell14=row1.createCell(4);
		   cell14.setCellValue("起算日期");
		   
		   HSSFCell cell15=row1.createCell(5);
		   cell15.setCellValue("* 往来对象");
		   
		   HSSFCell cell16=row1.createCell(6);
		   cell16.setCellValue("* 供应商");
		   
		   HSSFCell cell17=row1.createCell(7);
		   cell17.setCellValue("部门");
		   
		   HSSFCell cell18=row1.createCell(8);
		   cell18.setCellValue("* 业务员");
		   
		   HSSFCell cell19=row1.createCell(9);
		   cell19.setCellValue("* 部门");
		   
		   HSSFCell cell1110=row1.createCell(10);
		   cell1110.setCellValue("* 币种");
		   
		   HSSFCell cell111=row1.createCell(11);
		   cell111.setCellValue("* 应收类型code");
		   
		   for(int i=0;i<dataset1.size();i++){
			   HashMap<String, Object> map=dataset1.get(i);
			   HSSFRow row2=sheet.createRow(2+i);
			   HSSFCell cell20=row2.createCell(0);
			   if(map.get("COL1")!=null){
				   cell20.setCellValue(map.get("COL1").toString());
			   }else{
				   cell20.setCellValue("");   
			   }
			   
			   HSSFCell cell21=row2.createCell(1);
			   if(map.get("COL2")!=null){
				   cell21.setCellValue(map.get("COL2").toString());
			   }else{
				   cell21.setCellValue("");   
			   }
			   
			   HSSFCell cell22=row2.createCell(2);
			   if(map.get("COL3")!=null){
				   cell22.setCellValue(map.get("COL3").toString());
			   }else{
				   cell22.setCellValue("");   
			   }
			   
			   HSSFCell cell23=row2.createCell(3);
			   if(map.get("COL4")!=null){
				   cell23.setCellValue(map.get("COL4").toString());
			   }else{
				   cell23.setCellValue("");   
			   }
			   
			   HSSFCell cell24=row2.createCell(4);
			   if(map.get("COL5")!=null){
				   cell24.setCellValue(map.get("COL5").toString());
			   }else{
				   cell24.setCellValue("");   
			   }
			   
			   HSSFCell cell25=row2.createCell(5);
			   if(map.get("COL6")!=null){
				   cell25.setCellValue(map.get("COL6").toString());
			   }else{
				   cell25.setCellValue("");   
			   }
			   
			   HSSFCell cell26=row2.createCell(6);
			   if(map.get("COL7")!=null){
				   cell26.setCellValue(map.get("COL7").toString());
			   }else{
				   cell26.setCellValue("");   
			   }
			   
			   HSSFCell cell27=row2.createCell(7);
			   if(map.get("COL8")!=null){
				   cell27.setCellValue(map.get("COL8").toString());
			   }else{
				   cell27.setCellValue("");   
			   }
			   
			   HSSFCell cell28=row2.createCell(8);
			   if(map.get("COL9")!=null){
				   cell28.setCellValue(map.get("COL9").toString());
			   }else{
				   cell28.setCellValue("");   
			   }
			   
			   HSSFCell cell29=row2.createCell(9);
			   if(map.get("COL10")!=null){
				   cell29.setCellValue(map.get("COL10").toString());
			   }else{
				   cell29.setCellValue("");   
			   }
			   
			   HSSFCell cell210=row2.createCell(10);
			   if(map.get("COL11")!=null){
				   cell210.setCellValue(map.get("COL11").toString());
			   }else{
				   cell210.setCellValue("");   
			   }
			   
			   HSSFCell cell211=row2.createCell(11);
			   if(map.get("COL12")!=null){
				   cell211.setCellValue(map.get("COL12").toString());
			   }else{
				   cell211.setCellValue("");   
			   }
		   }
		   
		   HSSFRow row=sheet.createRow(dataset1.size()+3);		   
		   HSSFCell cellq0=row.createCell(0);
		   cellq0.setCellValue("\"bodys,contractno,purchaseorder,invoiceno,scomment,pk_payterm,objtype,supplier,pk_deptid,pk_psndoc,subjcode,def5,pk_currtype,rate,money_cr,local_money_cr,taxcodeid,taxrate,local_tax_cr,notax_cr,local_notax_cr,busidate,def3,def2,def1,def4,buysellflag\"");
		   
		   HSSFCell cellq1=row.createCell(1);
		   cellq1.setCellValue("合同号");
		   
		   HSSFCell cellq2=row.createCell(2);
		   cellq2.setCellValue("提单号");
		   
		   HSSFCell cellq3=row.createCell(3);
		   cellq3.setCellValue("发票号");
		   
		   HSSFCell cellq4=row.createCell(4);
		   cellq4.setCellValue("摘要");
		   
		   HSSFCell cellq5=row.createCell(5);
		   cellq5.setCellValue("付款协议");
		   
		   HSSFCell cellq6=row.createCell(6);
		   cellq6.setCellValue("* 往来对象");
		   
		   HSSFCell cellq7=row.createCell(7);
		   cellq7.setCellValue("* 供应商");
		   
		   HSSFCell cellq8=row.createCell(8);
		   cellq8.setCellValue("* 部门");
		   
		   HSSFCell cellq9=row.createCell(9);
		   cellq9.setCellValue("* 业务员");
		   
		   HSSFCell cellq10=row.createCell(10);
		   cellq10.setCellValue("* 借方科目");
		   
		   HSSFCell cellq11=row.createCell(11);
		   cellq11.setCellValue("客户（结转成本）");
		   
		   HSSFCell cellq12=row.createCell(12);
		   cellq12.setCellValue("* 币种");
		   
		   HSSFCell cellq13=row.createCell(13);
		   cellq13.setCellValue("* 组织本币汇率");
		   
		   HSSFCell cellq14=row.createCell(14);
		   cellq14.setCellValue("* 贷方原币金额");
		   
		   HSSFCell cellq15=row.createCell(15);
		   cellq15.setCellValue("组织本币金额");
		   
		   HSSFCell cellq16=row.createCell(16);
		   cellq16.setCellValue("* 税码");
		   
		   HSSFCell cellq17=row.createCell(17);
		   cellq17.setCellValue("* 税率");
		   
		   HSSFCell cellq18=row.createCell(18);
		   cellq18.setCellValue("税额");
		   
		   HSSFCell cellq19=row.createCell(19);
		   cellq19.setCellValue("贷方原币无税金额");
		   
		   HSSFCell cellq20=row.createCell(20);
		   cellq20.setCellValue("组织本币无税金额");
		   
		   HSSFCell cellq21=row.createCell(21);
		   cellq21.setCellValue("* 起算日期");
		   
		   HSSFCell cellq22=row.createCell(22);
		   cellq22.setCellValue("费用类型");
		   
		   HSSFCell cellq23=row.createCell(23);
		   cellq23.setCellValue("所属期");
		   
		   HSSFCell cellq24=row.createCell(24);
		   cellq24.setCellValue("项目");
		   
		   HSSFCell cellq25=row.createCell(25);
		   cellq25.setCellValue("备注");
		   
		   HSSFCell cellq26=row.createCell(26);
		   cellq26.setCellValue("* 购销类型");
		   
		   for(int i=0;i<dataset2.size();i++){
			   HashMap<String, Object> map=dataset2.get(i);
			   HSSFRow row3=sheet.createRow(i+dataset1.size()+4);
			   HSSFCell cell30=row3.createCell(0);
//			   if(map.get("COL1")!=null){
//				   cell30.setCellValue(map.get("COL1").toString());
//			   }else{
				   cell30.setCellValue(String.valueOf(i));   
			   //}
			   
			   HSSFCell cell31=row3.createCell(1);
			   if(map.get("COL2")!=null){
				   cell31.setCellValue(map.get("COL2").toString());
			   }else{
				   cell31.setCellValue("");   
			   }
			   
			   HSSFCell cell32=row3.createCell(2);
			   if(map.get("COL3")!=null){
				   cell32.setCellValue(map.get("COL3").toString());
			   }else{
				   cell32.setCellValue("");   
			   }
			   
			   HSSFCell cell33=row3.createCell(3);
			   if(map.get("COL4")!=null){
				   cell33.setCellValue(map.get("COL4").toString());
			   }else{
				   cell33.setCellValue("");   
			   }
			   
			   HSSFCell cell34=row3.createCell(4);
			   if(map.get("COL5")!=null){
				   cell34.setCellValue(map.get("COL5").toString());
			   }else{
				   cell34.setCellValue("");   
			   }
			   
			   HSSFCell cell35=row3.createCell(5);
			   if(map.get("COL6")!=null){
				   cell35.setCellValue(map.get("COL6").toString());
			   }else{
				   cell35.setCellValue("");   
			   }
			   
			   HSSFCell cell36=row3.createCell(6);
			   if(map.get("COL7")!=null){
				   cell36.setCellValue(map.get("COL7").toString());
			   }else{
				   cell36.setCellValue("");   
			   }
			   
			   HSSFCell cell37=row3.createCell(7);
			   if(map.get("COL8")!=null){
				   cell37.setCellValue(map.get("COL8").toString());
			   }else{
				   cell37.setCellValue("");   
			   }
			   
			   HSSFCell cell38=row3.createCell(8);
			   if(map.get("COL9")!=null){
				   cell38.setCellValue(map.get("COL9").toString());
			   }else{
				   cell38.setCellValue("");   
			   }
			   
			   HSSFCell cell39=row3.createCell(9);
			   if(map.get("COL10")!=null){
				   cell39.setCellValue(map.get("COL10").toString());
			   }else{
				   cell39.setCellValue("");   
			   }
			   
			   HSSFCell cell310=row3.createCell(10);
			   if(map.get("COL11")!=null){
				   cell310.setCellValue(map.get("COL11").toString());
			   }else{
				   cell310.setCellValue("");   
			   }
			   
			   HSSFCell cell311=row3.createCell(11);
			   if(map.get("COL12")!=null){
				   cell311.setCellValue(map.get("COL12").toString());
			   }else{
				   cell311.setCellValue("");   
			   }
			   
			   HSSFCell cell312=row3.createCell(12);
			   if(map.get("COL13")!=null){
				   cell312.setCellValue(map.get("COL13").toString());
			   }else{
				   cell312.setCellValue("");   
			   }
			   
			   HSSFCell cell313=row3.createCell(13);
			   if(map.get("COL14")!=null){
				   cell313.setCellValue(map.get("COL14").toString());
			   }else{
				   cell313.setCellValue("");   
			   }
			   
			   HSSFCell cell314=row3.createCell(14);
			   if(mapJ.get(map.get("COL8"))!=null){
				   cell314.setCellValue(mapJ.get(map.get("COL8")).toString());
			   }else{
				   cell314.setCellValue("0.00");   
			   }
			   
			   HSSFCell cell315=row3.createCell(15);
			   if(mapJ.get(map.get("COL8"))!=null){
				   cell315.setCellValue(mapJ.get(map.get("COL8")).toString());
			   }else{
				   cell315.setCellValue("0.00");   
			   }
			   
			   HSSFCell cell316=row3.createCell(16);
			   if(map.get("COL17")!=null){
				   cell316.setCellValue(map.get("COL17").toString());
			   }else{
				   cell316.setCellValue("");   
			   }
			   
			   HSSFCell cell317=row3.createCell(17);
			   if(map.get("COL18")!=null){
				   cell317.setCellValue(map.get("COL18").toString());
			   }else{
				   cell317.setCellValue("");   
			   }
			   
			   HSSFCell cell318=row3.createCell(18);
			   if(map.get("COL19")!=null){
				   cell318.setCellValue(map.get("COL19").toString());
			   }else{
				   cell318.setCellValue("");   
			   }
			   
			   
			   HSSFCell cell319=row3.createCell(19);
			   if(map.get("COL20")!=null){
				   cell319.setCellValue(map.get("COL20").toString());
			   }else{
				   cell319.setCellValue("");   
			   }
			   
			   HSSFCell cell320=row3.createCell(20);
			   if(map.get("COL21")!=null){
				   cell320.setCellValue(map.get("COL21").toString());
			   }else{
				   cell320.setCellValue("");   
			   }
			   
			   HSSFCell cell321=row3.createCell(21);
			   if(map.get("COL22")!=null){
				   cell321.setCellValue(map.get("COL22").toString());
			   }else{
				   cell321.setCellValue("");   
			   }
			  
			   HSSFCell cell322=row3.createCell(22);
			   if(map.get("COL23")!=null){
				   cell322.setCellValue(map.get("COL23").toString());
			   }else{
				   cell322.setCellValue("");   
			   }
			   
			   HSSFCell cell323=row3.createCell(23);
			   if(map.get("COL24")!=null){
				   cell323.setCellValue(map.get("COL24").toString());
			   }else{
				   cell323.setCellValue("");   
			   }
			   
			   HSSFCell cell324=row3.createCell(24);
			   if(map.get("COL25")!=null){
				   cell324.setCellValue(map.get("COL25").toString());
			   }else{
				   cell324.setCellValue("");   
			   }
			   
			   HSSFCell cell325=row3.createCell(25);
			   if(map.get("COL26")!=null){
				   cell325.setCellValue(map.get("COL26").toString());
			   }else{
				   cell325.setCellValue("");   
			   } 
			   
			   HSSFCell cell326=row3.createCell(26);
			   if(map.get("COL27")!=null){
				   cell326.setCellValue(map.get("COL27").toString());
			   }else{
				   cell326.setCellValue("");   
			   }
			   
		   }
		   		return wb;   
	   }
	   
	   return null;
   }
}