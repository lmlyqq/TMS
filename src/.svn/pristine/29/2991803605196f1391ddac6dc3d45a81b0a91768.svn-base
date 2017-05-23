package com.rd.server.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.rd.client.common.obj.SysParam;
import com.rd.client.obj.system.SYS_USER;
import com.rd.server.DBServiceImpl;
public class LoginContent {
	
	
    protected static Configuration hibernateConfig;
    protected static SessionFactory sessionFactory;
    protected final static ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
    protected static Properties properties;
    
	private static LoginContent instance;
    private static String DB_DRIVER = "org.hsqldb.jdbcDriver";
    private static String PASSWORD_PROPERTY = "sql";
    private static String USER_PROPERTY = "sa";
    
    private static String URL;
//    private static Properties URL_PROPERTY;
    public int pageSize = 50;
    private static String sessionId="";

    private SYS_USER userInfo;                           //登录用户的缓存系信息
    private LinkedHashMap<String, String> client_prop;   //客户户配置文件的缓存信息
    private LinkedHashMap<String, LinkedHashMap<String, String>> cache_codes; //数据字典的缓存信息
    private LinkedHashMap<String, LinkedHashMap<String, String>> stat_codes; //状态相关的缓存信息
    private LinkedHashMap<String,SysParam> sys_param;  //系统参数的缓存信息
    private LinkedHashMap<String, LinkedHashMap<String, String>> listMap;
//    private String user_customer = "SYSTEM";

	private int count = 0;
    private int pages = 0;
    private String swhere = "";
    private int defPageSize = 100;
    private static DataSource dataSource;
    static{
    	if(dataSource == null){
    		Context ctx;
			try {
				ctx = new InitialContext();
				dataSource = (DataSource)ctx.lookup("java:jdbc/tmsDB");
			} catch (NamingException e) {
				System.out.println("数据源加载失败: "+e.getMessage());
			}
    	}
    }
    
    public static DataSource getDataSource(){
    	return dataSource;
    }
	
    public static void setSessionId(String sID){
    	sessionId = sID;
    }
    
    public static String getSessionId(){
    	return sessionId;
    }
    
	public synchronized static LoginContent getInstance() {
		if (instance == null) {
			instance = new LoginContent();
		}
		return instance;
	}
	
    private static synchronized void createConfig() {
        hibernateConfig = new Configuration();
        Configuration config = hibernateConfig.configure("hibernate.cfg.xml");
        DB_DRIVER = config.getProperty("connection.driver_class");
        URL = config.getProperty("connection.url");
        USER_PROPERTY = config.getProperty("connection.username");
        PASSWORD_PROPERTY = config.getProperty("connection.password");
//        URL_PROPERTY = config.getProperties();
        sessionFactory = config.buildSessionFactory();
    }
    
    public Session getSession() {
    	if (hibernateConfig == null) {
            createConfig();
        }
    	Session session = sessionThread.get();
    	if(session  ==  null || !session.isOpen())  {
    		session  =  sessionFactory.openSession();
    		sessionThread.set(session);
        }
    	return session;
    } 
    
    public void closeSession() {
        Session s = sessionThread.get();
        if(s != null) {
            s.close();
        }
        sessionThread.set(null);
    }
    
    public Connection getConnection() {
    	Connection conn = null;
    	try {
    		if(dataSource != null){
    			return dataSource.getConnection();
        	}
    		if(conn == null){
	    		//Lang added
	    		if (hibernateConfig == null) {
	    			hibernateConfig = new Configuration();
	    	        Configuration config = hibernateConfig.configure("hibernate.cfg.xml");
	    	        DB_DRIVER = config.getProperty("connection.driver_class");
	    	        URL = config.getProperty("connection.url");
	    	        USER_PROPERTY = config.getProperty("connection.username");
	    	        PASSWORD_PROPERTY = config.getProperty("connection.password");
	    		}
		    	Class.forName(DB_DRIVER);
	            conn = DriverManager.getConnection(URL, USER_PROPERTY, PASSWORD_PROPERTY);
    		}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	return conn;
    }
    
    public void setLoginUser(SYS_USER user) {
    	this.userInfo = user;
    }
    
    public SYS_USER getLoginUser() {
    	return this.userInfo;
    }
    
    public void setClientProp(LinkedHashMap<String, String> prop) {
    	client_prop = prop;
    }
    
    public LinkedHashMap<String, String> getClientProp() {
    	return this.client_prop;
    }
    
    public void setBizCodes(LinkedHashMap<String, LinkedHashMap<String, String>> map) {
    	cache_codes = map;
    }
    
    public LinkedHashMap<String, LinkedHashMap<String, String>> getBizCodes() {
    	return cache_codes;
    }
    
    public void setStatCodes(LinkedHashMap<String, LinkedHashMap<String, String>> map) {
    	stat_codes = map;
    }
    
    public LinkedHashMap<String, LinkedHashMap<String, String>> getStatCodes() {
    	return stat_codes;
    }
    
    public LinkedHashMap<String, SysParam> getSys_param() {
    	if(sys_param == null) {
    		DBServiceImpl db = new DBServiceImpl();
    		sys_param = db.getSysParam();
    	}
		return sys_param;
	}

	public void setSys_param(LinkedHashMap<String, SysParam> sysParam) {
		sys_param = sysParam;
	}
    
    public void setPageInfo(ArrayList<String> p_list) {
    	if(p_list != null && p_list.size() > 0) {
    		count = Integer.parseInt(p_list.get(0));
    		if(count%pageSize == 0){
    			pages = count/pageSize;
    		}else{
    			pages = count/pageSize + 1;
    		}
    		swhere = p_list.get(1);
    	}
    	
    }
    public void setWhere(ArrayList<String> p_list) {
    	if(p_list != null && p_list.size() > 0) {
    		swhere = p_list.get(1);
    	}
    }
    public void setWhere(String where) {
    		swhere = where;
    }
    
    public void setPageInfo(ArrayList<String> p_list,int pageSize) {
    	if(p_list != null && p_list.size() > 0) {
    		count = Integer.parseInt(p_list.get(0));
    		if(count%pageSize == 0){
    			pages = count/pageSize;
    		}else{
    			pages = count/pageSize + 1;
    		}
    		swhere = p_list.get(1);
    	}
    }
    
    public void setPageInfo(String cc,int pageSize) {
		count = Integer.parseInt(cc);
		if(count%pageSize == 0){
			pages = count/pageSize;
		}else{
			pages = count/pageSize + 1;
		}

    }
    
    public ArrayList<String> getPageInfo() {
    	ArrayList<String> list = new ArrayList<String>();
    	list.add(Integer.toString(count));
    	list.add(Integer.toString(pages));
    	list.add(swhere);
    	return list;
    }
    
    public void setDefPageSize(int pageSize) {
    	this.defPageSize = pageSize;
    }
    
    public int getDefPageSize() {
    	return defPageSize;
    }
    
    public void setListCfg(LinkedHashMap<String, LinkedHashMap<String, String>> map) {
    	listMap = map;
    }
    
    public LinkedHashMap<String, LinkedHashMap<String, String>> getListCfg() {
    	return listMap;
    }
}
