package com.rd.server.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


//生成bean

public class CreateDS<T> {
	
   
	public void CreateDsXml(ArrayList<HashMap<String,String>> list,HashMap<String,String> map) throws ParserConfigurationException, TransformerException {
			
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder(); 
			Document  document = docBuilder.newDocument();  
//	   
//			DOMImplementation dom=DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
//			
//			DocumentType docType=dom.createDocumentType(arg1, arg2, arg3);
//	        
//			Document document =dom.createDocument(null,"DataSource",docType);
			
			Element root = document.createElement("DataSource");  
	       
			document.appendChild(root);  
			
			String path=this.getClass().getResource("").getPath();	
			
			System.out.println(path);
			
			String [] paths=path.split("/com");
			
			System.out.println(map.get("PackagePath"));
			
			String packagePath=map.get("PackagePath");
//			
			if(packagePath!=null&&!packagePath.equals("")){
				
				String str=packagePath.replace(".", "/");
				
				String [] packages=str.split("/");
				
				for(int i=0;i<packages.length;i++){
					
					paths[0]=paths[0]+"/"+packages[i];
					
					File outfile = new File(paths[0]);                 
					  //如果文件不存在，则创建一个新文件
					 
					if(!outfile.isFile()){
						
						 outfile.mkdirs();
						 
						 System.out.println( paths[0]);
					}
					
				}
//				 
//				
//		            //根节点  
				//root.setAttribute("ID", map.get("ID"));
				
		        root.setAttribute("ID",map.get("ID"));  
		        root.setAttribute("serverType","sql");  
		        root.setAttribute("TableName",map.get("TABLENAME"));  
		        root.setAttribute("dataFormat","json");  
		        root.setAttribute("showPrompt","false");
		        
		       
		            //子节点  
		        Element fields = document.createElement("fields");
		        root.appendChild(fields); 
		        
		       
		        for(int i=0;i<list.size();i++){
		        
		        	HashMap<String,String> fmap=list.get(i);
		        	
		        	Element field = document.createElement("field");
		        	fields.appendChild(field); 
		        	
		        	field.setAttribute( "name", fmap.get("name") );  
		       
		        	field.setAttribute( "type", fmap.get("type") );  
		        
		        	field.setAttribute( "title", fmap.get("title") );  
		        	
		        	field.setAttribute( "width", fmap.get("width") );  
		        	
		        	if(fmap.get("hidden").equals("true")){
		        		
		        		field.setAttribute( "hidden", "true");
		        		
		        	}

		        }
		        
		        Element serverObject = document.createElement("serverObject");
		        root.appendChild(serverObject);
		        serverObject.setAttribute("lookupStyle","spring"); 
		        serverObject.setAttribute("bean",map.get("BEAN")); 
		        
		        Element operationBindings = document.createElement("operationBindings");
		        root.appendChild(operationBindings);  
		       
		        Element operationBinding = document.createElement("operationBindings");
		        operationBindings.appendChild(operationBinding);
		        operationBinding.setAttribute("operationType","fetch");  
		      
		        Element selectClause = document.createElement("selectClause");
		        operationBinding.appendChild(selectClause);
		        selectClause.appendChild(document.createTextNode(map.get("SELECT")));
		        
		        Element tableClause = document.createElement("tableClause");
		        operationBinding.appendChild(tableClause);  
		        tableClause.appendChild(document.createTextNode(map.get("TABLE")));
		        
		        Element orderClause = document.createElement("orderClause");
		        operationBinding.appendChild(orderClause);  
		        orderClause.appendChild(document.createTextNode(map.get("ORDERBY")));
		            //添加  
//		        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
//                
//		        outputFormat.setEncoding("UTF-8");
//		        outputFormat.setLineSeparator("\n"); 
//		        outputFormat.setTrimText(false); 
//		        outputFormat.setIndent("	");
//		        outputFormat.setIndentSize(2); 
//		        outputFormat.setNewlines(true); 
//		        outputFormat.setTrimText(true); 
//		        outputFormat.setPadText(true);
//		        //设置XML文档输出格式
//		        outputFormat.setNewlines(true);    //设置是否换行
//		        outputFormat.setSuppressDeclaration(true);
//		        outputFormat.setIndent(true);     //设置是否缩进
//		        outputFormat.setIndent("    ");    //以空格方式实现缩进
//		        //outputFormat.setLineSeparator("\n\r");
		        

                FileOutputStream outputStream = new FileOutputStream(paths[0]+"/"+map.get("ID")+".ds.xml");
                
                TransformerFactory tf = TransformerFactory.newInstance();  
                Transformer transformer = tf.newTransformer();  
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");             
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                StreamResult xmlResult = new StreamResult(outputStream);                 
                transformer.transform(new DOMSource(document), xmlResult);  
			
			}
			else if(map.get("FilePath")!=null&&!map.get("FilePath").equals("")){
				
				File outfile = new File(map.get("FilePath"));                 
				  //如果文件不存在，则创建一个新文件
				 
				if(!outfile.isFile()){
					
					 outfile.mkdirs();
					 
					 System.out.println(map.get("FilePath"));
				}
			       //根节点  
			    root.setAttribute("ID",map.get("ID"));  
		        root.setAttribute("serverType","sql");  
		        root.setAttribute("TableName",map.get("TABLENAME"));  
		        root.setAttribute("dataFormat","json");  
		        root.setAttribute("showPrompt","false");
		        
		       
		            //子节点  
		        Element fields = document.createElement("fields");
		        root.appendChild(fields); 
		        
		       
		        for(int i=0;i<list.size();i++){
		        
		        	HashMap<String,String> fmap=list.get(i);
		        	
		        	Element field = document.createElement("field");
		        	fields.appendChild(field); 
		        	
		        	field.setAttribute( "name", fmap.get("name") );  
		       
		        	field.setAttribute( "type", fmap.get("type") );  
		        
		        	field.setAttribute( "title", fmap.get("title") );  
		        	
		        	field.setAttribute( "width", fmap.get("width") );  
		        	
		        	if(fmap.get("hidden").equals("true")){
		        		
		        		field.setAttribute( "hidden", "true");
		        		
		        	}

		        }
		        
		        Element serverObject = document.createElement("serverObject");
		        root.appendChild(serverObject);
		        serverObject.setAttribute("lookupStyle","spring"); 
		        serverObject.setAttribute("bean",map.get("BEAN")); 
		        
		        Element operationBindings = document.createElement("operationBindings");
		        root.appendChild(operationBindings);  
		       
		        Element operationBinding = document.createElement("operationBindings");
		        operationBindings.appendChild(operationBinding);
		        operationBinding.setAttribute("operationType","fetch");  
		      
		        Element selectClause = document.createElement("selectClause");
		        operationBinding.appendChild(selectClause);
		        selectClause.appendChild(document.createTextNode(map.get("SELECT")));
		        
		        Element tableClause = document.createElement("tableClause");
		        operationBinding.appendChild(tableClause);  
		        tableClause.appendChild(document.createTextNode(map.get("TABLE")));
		        
		        Element orderClause = document.createElement("orderClause");
		        operationBinding.appendChild(orderClause);  
		        orderClause.appendChild(document.createTextNode(map.get("ORDERBY")));
			 

	            FileOutputStream outputStream = new FileOutputStream(map.get("FilePath")+"/"+map.get("ID")+".ds.xml");
	            TransformerFactory tf = TransformerFactory.newInstance();  
                Transformer transformer = tf.newTransformer();  
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");             
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                StreamResult xmlResult = new StreamResult(outputStream);                 
                transformer.transform(new DOMSource(document), xmlResult);  
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	
	
}