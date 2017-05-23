package com.rd.server.util;



public class HSQL {
	
	private static HSQL instance;
	private String sql;

	public synchronized static HSQL getInstance() {
		if (instance == null) {
			instance = new HSQL();
		}
		return instance;
	}
	
	public HSQL addSQL(String colName,String replaceName) {
		HSQL hsql = getInstance();
		if(sql.indexOf(colName) >= 0) {
			sql = sql.replaceAll(colName, replaceName + " AS " + colName);
		}
		else {
			sql = sql + "," + replaceName + " AS " + colName;
		}
		hsql.setSQL(sql);
		return hsql;
	}
	
	public String getSQL() {
		return this.sql;
	}
	
	public HSQL setSQL(String in_sql) {
		HSQL hsql = getInstance();
		hsql.initSQL(in_sql);
		return hsql;
	}
	
	protected void initSQL(String in_sql) {
		this.sql = in_sql;
	}
}
