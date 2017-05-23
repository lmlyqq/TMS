package com.rd.server.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

//生成bean

public class CreateHBM<T> {
	
   
	public void CreateHbmXml(ArrayList<HashMap<String,String>> list,HashMap<String,String> map) throws ParserConfigurationException {
			
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder(); 
			Document  document = docBuilder.newDocument();
	        Element root = document.createElement("hibernate-mapping");  
	        
	        document.appendChild(root);  
			
			String arg1="hibernate-mapping";
			
			String arg2="-//Hibernate/Hibernate Mapping DTD 3.0//EN";
			
			String arg3="http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd";
		
			DOMImplementation domImpl = document.getImplementation();
			
			DocumentType doctype =  domImpl.createDocumentType(arg1,arg2,arg3);
	
			String path=this.getClass().getResource("").getPath();	
			
			System.out.println(path);
			
			String [] paths=path.split("/com");
			
			System.out.println(map.get("PackagePath"));
			
			String packagePath=map.get("PackagePath");
			

				List<HashMap<String,String>> pList=new ArrayList<HashMap<String,String>>();
				List<HashMap<String,String>> qList=new ArrayList<HashMap<String,String>>();
				for(int i=0;i<list.size();i++){
					
					HashMap<String,String> fmap=list.get(i);
					
					 if(fmap.get("prk").equals("true")){
						 HashMap<String,String> m=new HashMap<String,String>();
						 m.put("name",fmap.get("name"));
						 m.put("type",fmap.get("type"));
						 m.put("insert",fmap.get("insert"));
						 m.put("update",fmap.get("update"));
						 
						 pList.add(m);				 
					 }else{
						 HashMap<String,String> m=new HashMap<String,String>();
						 m.put("name",fmap.get("name"));
						 m.put("type",fmap.get("type"));
						 m.put("insert",fmap.get("insert"));
						 m.put("update",fmap.get("update"));
						 
						 qList.add(m);	
					 }				 
				}
							 
				Element hbmClass = document.createElement("class");
				root.appendChild(hbmClass); 
				
				hbmClass.setAttribute("name", map.get("CLASSNAME") );
				hbmClass.setAttribute("table", map.get("TABLENAME") );
				hbmClass.setAttribute("dynamic-update","true");
				
				if(pList.size()==1){
					
					Element id = document.createElement("id");
					hbmClass.appendChild(id);  
					id.setAttribute("name",pList.get(0).get("name")); 
					
					if(pList.get(0).get("column")!=null&&!pList.get(0).get("column").equals("")){
					id.setAttribute("column",pList.get(0).get("column")); 
					}
					if(pList.get(0).get("type")!=null&&!pList.get(0).get("type").equals("")){
					id.setAttribute("type",pList.get(0).get("type")); 
					}
					Element generator = document.createElement("generator");
					id.appendChild(generator);  
					generator.setAttribute("class","assigned"); 
				
					for(int i=0;i<qList.size();i++){
						
						Element property = document.createElement("property");
						hbmClass.appendChild(property);
						property.setAttribute("name",qList.get(i).get("name"));
						if(qList.get(i).get("type")!=null&&!qList.get(i).get("type").equals("")){
						
							property.setAttribute("type",qList.get(i).get("type"));
						}
						if(qList.get(i).get("insert")!=null&&qList.get(i).get("insert").equals("false")){
							
							property.setAttribute("insert","false"); 
							
						}
						
						if(qList.get(i).get("update")!=null&&qList.get(i).get("update").equals("false")){
							
							property.setAttribute("update","false"); 
							
						}
					}
					
				}
				if(pList.size()>1){
					
					Element composite_id = document.createElement("composite");
					hbmClass.appendChild(composite_id); 
					for(int i=0;i<pList.size();i++){
						
						Element key_property = document.createElement("key_property");
						composite_id.appendChild(key_property);
						key_property.setAttribute("name",pList.get(i).get("name"));
						
						if(pList.get(i).get("column")!=null&&!pList.get(i).get("column").equals("")){
						key_property.setAttribute("column",pList.get(i).get("column"));
						}
						if(pList.get(i).get("type")!=null&&!pList.get(i).get("type").equals("")){
						key_property.setAttribute("type",pList.get(i).get("type"));
						}
					}

					for(int i=0;i<qList.size();i++){
						
						Element property = document.createElement("property");
						hbmClass.appendChild(property); 
						property.setAttribute("name",qList.get(i).get("name"));
						if(qList.get(i).get("type")!=null&&!qList.get(i).get("type").equals("")){
						property.setAttribute("type",qList.get(i).get("type"));
						}
						if(qList.get(i).get("insert")!=null&&qList.get(i).get("insert").equals("false")){
							
							property.setAttribute("insert","false"); 
							
						}
						
						if(qList.get(i).get("update")!=null&&qList.get(i).get("update").equals("false")){
							
							property.setAttribute("update","false"); 
							
						}
						
					}
					
				}
				if(pList.size()==0){
				

					for(int i=0;i<qList.size();i++){
						
						Element property = document.createElement("property");
						
						hbmClass.appendChild(property); 
						if(qList.get(i).get("name")!=null&&!qList.get(i).get("name").equals("")){
						property.setAttribute("name",qList.get(i).get("name"));
						}
						if(qList.get(i).get("type")!=null&&!qList.get(i).get("type").equals("")){
						property.setAttribute("type",qList.get(i).get("type"));
						}
						if(qList.get(i).get("insert")!=null&&qList.get(i).get("insert").equals("false")){
							
							property.setAttribute("insert","false"); 
							 
						}
						
						if(qList.get(i).get("update")!=null&&qList.get(i).get("update").equals("false")){
							
							property.setAttribute("update","false"); 
							
						}
						
					}
			
				
				}
				
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
					 
					FileOutputStream outputStream = new FileOutputStream(paths[0]+"/"+map.get("ID")+".hbm.xml");
			           
					TransformerFactory tf = TransformerFactory.newInstance();  
		                
					Transformer transformer = tf.newTransformer();  
		                
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");             
		               
					transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		               
					transformer.setOutputProperty(OutputKeys.METHOD, " xml ");
					
					transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());

					transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
					
					StreamResult xmlResult = new StreamResult(outputStream);                 
		                
					transformer.transform(new DOMSource(document), xmlResult);  
			
			}
//				
				else if(map.get("FilePath")!=null&&!map.get("FilePath").equals("")){
				
				
					File outfile = new File(map.get("FilePath"));                 
				  //如果文件不存在，则创建一个新文件				 
				
					if(!outfile.isFile()){
			
						outfile.mkdirs();
						
						System.out.println(map.get("FilePath"));
			
					}
					
				FileOutputStream outputStream = new FileOutputStream(map.get("FilePath")+"/"+map.get("ID")+".hbm.xml");

				TransformerFactory tf = TransformerFactory.newInstance();  
                
				Transformer transformer = tf.newTransformer();  
	                
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");             
	               
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	               
				transformer.setOutputProperty(OutputKeys.METHOD, " xml ");
				
				transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());

				transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
				
				StreamResult xmlResult = new StreamResult(outputStream);                 
	                
				transformer.transform(new DOMSource(document), xmlResult);
				
			}
//			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	
	
}