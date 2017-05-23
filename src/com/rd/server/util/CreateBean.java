package com.rd.server.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


//生成bean

public class CreateBean<T> {
	
	private String classname = "";
	private String[] colnames;
	private String[] colTypes;

   
	public void tableToEntity(String className,String[] fields,String packagePath,String absolutelyPath,String Implements,String Extends) {
			
		colTypes=new String [fields.length];;
		classname=className;
		colnames=fields;
		for(int i=0;i<fields.length;i++){
			colTypes[i]="varchar";
		}
		
		String content = parse(colnames,packagePath,Extends,Implements);
		
		try {
			String path=this.getClass().getResource("").getPath();	
			
			System.out.println(path);
			
			String [] paths=path.split("/com");
			
			System.out.println(packagePath);
			
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
				
				
				FileWriter fw = new FileWriter(paths[0]+"/"+initcap(classname)+".java");
				
				PrintWriter pw = new PrintWriter(fw);
				
				pw.println(content);
				
				System.out.println(fw);
				
				pw.flush();
				
				pw.close();
			
			}
			else if(absolutelyPath!=null&&!absolutelyPath.equals("")){
				if(packagePath==null||packagePath.equals("")){
					
					File outfile = new File(absolutelyPath);                 
					  //如果文件不存在，则创建一个新文件
					 
					if(!outfile.isFile()){
						
						 outfile.mkdirs();
						 
						 System.out.println(absolutelyPath);
					}
					
					
					FileWriter fw = new FileWriter(absolutelyPath+"/"+initcap(classname)+".java");
					
					PrintWriter pw = new PrintWriter(fw);
					
					pw.println(content);
					
					System.out.println(fw);
					
					pw.flush();
					
					pw.close();
						
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	private String parse(String[] colNames,String packagePath,String Extends,String Implements) {
		StringBuffer sb = new StringBuffer();
	//	for(int i=0;i<packagePath.length();i++){
			
	    String newPath=packagePath.replace("/", ".");
			
//		}
		sb.append("\r\npackage "+newPath+";\r\n");
		sb.append("\r\nimport java.io.Serializable;\r\n");
		sb.append("\r\nimport com.rd.ilp.server.util.BasModel;\r\n");
		
		processColnames(sb);
		sb.append("public class " + initcap(classname));
		if(Extends!=null&&!Extends.equals("")){
			sb.append(" extends "+Extends);
		}
		if(Implements!=null&&!Implements.equals("")){
			sb.append(" implements "+Implements);
		}
		sb.append(" {\r\n");
		processAllAttrs(sb);
		processAllMethod(sb);
		sb.append("}\r\n");
		System.out.println(sb.toString());
		return sb.toString();

	}
	private void processAllAttrs(StringBuffer sb) {
		sb.append("\tprivate static final long serialVersionUID = 1L;\r\n");
		for (int i = 0; i < colnames.length; i++) {
			sb.append("\tprivate "
					+ oracleSqlType2JavaType(colTypes[i])+ " " + colnames[i] + ";\r\n");
		}
		sb.append("\r\n");
	}
	
	
	//大写
	private String initcap(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}
	
	private void processColnames(StringBuffer sb) {
		sb.append("\r\n/** " + classname + "\r\n");
	
		for (int i = 0; i < colnames.length; i++) {
	
			sb.append("\t" + colnames[i].toUpperCase() + "    "
					+ colTypes[i].toUpperCase()+ "\r\n");
			char[] ch = colnames[i].toCharArray();
			char c = 'a';
			if (ch.length > 3) {
				for (int j = 0; j < ch.length; j++) {
					c = ch[j];
					if (c == '_') {
						if (ch[j + 1] >= 'a' && ch[j + 1] <= 'z') {
							ch[j + 1] = (char) (ch[j + 1] - 32);
						}
					}
				}
			}
			String str = new String(ch);
			colnames[i] = str.replaceAll("_", "");
		}
		sb.append("*/\r\n");
	}
	
	private void processAllMethod(StringBuffer sb) {
		for (int i = 0; i < colnames.length; i++) {
			sb.append("\tpublic void set"
					+ initcap(colnames[i])
					+ "("
					+ oracleSqlType2JavaType(colTypes[i]) + " " + colnames[i] + "){\r\n");
			sb.append("\t\tthis." + colnames[i] + "=" + colnames[i] + ";\r\n");
			sb.append("\t}\r\n");

			sb.append("\tpublic "
					+ oracleSqlType2JavaType(colTypes[i]) + " get" + initcap(colnames[i])
					+ "(){\r\n");
			sb.append("\t\treturn " + colnames[i] + ";\r\n");
			sb.append("\t}\r\n");
		}
	}
	private String oracleSqlType2JavaType(String sqlType) {
		if (sqlType.equals("integer")) {
			return "Integer";
		} else if (sqlType.equals("long")) {
			return "Long";
		} else if (sqlType.equals("float") || sqlType.equals("float precision"))
			return "float";
		else if (sqlType.equals("double") || sqlType.equals("double precision")) {
			return "Double";
		} else if (sqlType.equals("varchar") || sqlType.equals("varchar2")
				|| sqlType.equals("char") || sqlType.equals("nvarchar")
				|| sqlType.equals("nchar")) {
			return "String";
		} else if (sqlType.equals("datetime") || sqlType.equals("date")
				|| sqlType.equals("timestamp")) {
			return "Date";
		}
		return null;
	}
	
}