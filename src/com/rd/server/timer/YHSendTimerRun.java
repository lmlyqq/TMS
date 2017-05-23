package com.rd.server.timer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.transform.Transformers;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.server.util.Log4j;
import com.rd.server.util.LoginContent;
import com.rd.server.util.SUtil;


/**
 * U8接口功能实现类，发货接口数据交互操作
 * @author fanglm
 * @create time 2011-4-26 21:00
 *
 */
public class YHSendTimerRun {
	
	protected static Configuration hibernateConfig;
    protected static SessionFactory sessionFactory;
    protected static Properties properties;
    protected static Configuration config;
    
    protected static Configuration hibernateConfig2;
    protected static Properties properties2;
    protected final static ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
    
    private Query rdQuery;
    
    private Connection conn;
    private Statement stmt;
    private ArrayList<String> excuteSqlList;
    private Session session;
	
	private StringBuffer insertSQL ;
	private StringBuffer insertServerSQL;
	
	private StringBuffer deleteSQL;
	
	
	private static String jdbcName;
	private static String dbURL;//test为数据库名
	private static String userName;//sa为SQL用户名
	private static String userPwd;//SQL密码
	
    /**
     * 中间库配置文件
     */
    private static synchronized void createConfig() {
        hibernateConfig = new Configuration();
        config = hibernateConfig.configure("hibernate.cfg_u8.xml");
        jdbcName = config.getProperty("connection.driver_class");
        dbURL = config.getProperty("connection.url");
        userName = config.getProperty("connection.username");
        userPwd = config.getProperty("connection.password");
        sessionFactory = config.buildSessionFactory();
        
    }
    
	public void run() {
		doSth();
	}
		
	public String doSth(){
		StringBuffer msg = new StringBuffer();
		msg.append("连接数据库成功！\n");
		try{
			String result = orderHeaderRD();
			msg.append(result);
			SUtil.execProcedure("U8_SEND_CONFIRM(?)");//发货确认
		}catch(Exception e){
			e.printStackTrace();
			return "接口服务器连接失败，请通知管理员！\n";
		}
		msg.append("处理完毕！\n");
		System.out.println(msg.toString());
		return msg.toString();
	}
    
    /**
     * 中间库session连接
     * @return
     */
    public Session getSession() {
    	if (hibernateConfig == null || sessionFactory.isClosed()) {
            createConfig();
        }
    	session = sessionThread.get();
    	if(session  ==  null || !session.isOpen() || !session.isConnected())  {
    		//System.out.println("建立连接！************************************************************");
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
    
    /**
     * oracle 数据库操作
     * @author fanglm
     * @createtime 2011-01-06 16:12
     * @param sql
     * @throws ClassNotFoundException 
     */
    private void excuteListSQL(ArrayList<String> sql){
    	Connection conn = null;
    	try {
    		conn = LoginContent.getInstance().getConnection();
	        stmt = conn.createStatement();
			for (int i = 0; i < sql.size(); i++) {
				stmt.execute(sql.get(i).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log4j.info(StaticRef.ERROR_LOG, e.getMessage());
		}finally {
			   try {
	                if(stmt != null) {
	                    stmt.close();
	                    stmt = null;
	                }
	                if(conn != null) {
	                    conn.close();
	                    conn = null;
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	                Log4j.info(StaticRef.ERROR_LOG, e.getMessage());
	            }
		}
    }
    
    /**
     * SQL Server 2005数据库操作
     * @author lijun
     * @param sql
     */
    private void excuteSQL(ArrayList<String> sql){
    	try {
    		Class.forName(jdbcName);//加载驱动
    		conn = DriverManager.getConnection(dbURL,userName,userPwd);
			stmt = conn.createStatement();
			for(int i = 0 ; i< sql.size() ; i++){
				stmt.executeUpdate(sql.get(i));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Log4j.info(StaticRef.ERROR_LOG, e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			Log4j.info(StaticRef.ERROR_LOG, e.getMessage());
		}finally {
			   try {
	                if(stmt != null) {
	                    stmt.close();
	                    stmt = null;
	                }
	                if(conn != null) {
	                    conn.close();
	                    conn = null;
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	                Log4j.info(StaticRef.ERROR_LOG, e.getMessage());
	            }
		}
    }
    
   
    
    /**
     * U8接口确认订单处理
     * @author lijun
     */
    @SuppressWarnings("unchecked")
    private String orderHeaderRD(){
    	int count =0;
		StringBuffer rdSQL = new StringBuffer();
		StringBuffer order_RD_ID = new StringBuffer();
		ArrayList<String> sqlList = new ArrayList<String>();
		
		rdSQL.append(" select top 1000 ID,CTYPE,AUTOID,CINVCODE,DDATE,IQUANTITY,CWHCODE,CBATCH,CFREE2,DMDATE,CMAKER,DLID,CDLCODE,CACCID from RdRecord");
		rdQuery = getSession().createSQLQuery(rdSQL.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String,Object>>  rdObject = rdQuery.list();
		
		session.flush();
		
        for(int i = 0;i < rdObject.size(); i++){
	       	 String ID = ObjUtil.ifObjNull(rdObject.get(i).get("ID"),"").toString();
	       	 String CTYPE = ObjUtil.ifObjNull(rdObject.get(i).get("CTYPE"),"").toString();
	       	 String AUTOID = ObjUtil.ifObjNull(rdObject.get(i).get("AUTOID"),"").toString();
	       	 String CINVCODE = ObjUtil.ifObjNull(rdObject.get(i).get("CINVCODE"),"").toString();
	       	 String DDATE = ObjUtil.ifObjNull(rdObject.get(i).get("DDATE"),"").toString();
	       	 String IQUANTITY = ObjUtil.ifObjNull(rdObject.get(i).get("IQUANTITY"),"").toString();
	       	 String CWHCODE = ObjUtil.ifObjNull(rdObject.get(i).get("CWHCODE"),"").toString();
	       	 String CBATCH = ObjUtil.ifObjNull(rdObject.get(i).get("CBATCH"), "").toString();
	       	 String CFREE2 = ObjUtil.ifObjNull(rdObject.get(i).get("CFREE2"),"").toString();
	       	 String DMDATE = ObjUtil.ifObjNull(rdObject.get(i).get("DMDATE"),"").toString();
	       	 String CMAKER = ObjUtil.ifObjNull(rdObject.get(i).get("CMAKER"),"").toString();
	       	 String DLID = ObjUtil.ifObjNull(rdObject.get(i).get("DLID"), "").toString();
	       	 String CDLCODE = ObjUtil.ifObjNull(rdObject.get(i).get("CDLCODE"), "").toString();
	       	 String CACCID = ObjUtil.ifObjNull(rdObject.get(i).get("CACCID"),"").toString();
	       	 
	       	 order_RD_ID.append(ID+",");
	       	 DMDATE = "to_timestamp('" + DMDATE + "','yyyy-mm-dd hh24:mi:ss.ff')";
	       	 DDATE = "to_timestamp('"+DDATE+"','yyyy-mm-dd hh24:mi:ss.ff')";
	       	 insertSQL = new StringBuffer();
	       	 insertSQL.append(" insert into U8_ORDER_ITEM_RD(ID,CTYPE,AUTOID,CINVCODE,DDATE,IQUANTITY" +
	       	 		",CWHCODE,CBATCH,CFREE2,DMDATE,CMAKER,DLID,CACCID,CDLCODE) values( ");
	       	 insertSQL.append(ID + "," + CTYPE + "," + AUTOID + ",'" + CINVCODE
	       			          +"',"+DDATE+","+IQUANTITY+",'" + CWHCODE+"','"
	       			          +CBATCH + "','" + CFREE2+"'," + DMDATE + ",'"
	       			          +CMAKER+"','"+ DLID +"','"+ CACCID +"','" + CDLCODE +"')"); 
	       	 sqlList.add(insertSQL.toString());
	       	 count ++;
        }
        
        excuteListSQL(sqlList);
        
        if(order_RD_ID.length() > 1){
        	insertServerSQL = new StringBuffer();
        	insertServerSQL.append("insert into RdRecord_BAK select * from RdRecord where id in(" +
        			order_RD_ID.substring(0,order_RD_ID.length()-1)+")");
        	deleteSQL = new StringBuffer();
        	excuteSqlList = new ArrayList<String>();
        	deleteSQL.append("delete from RdRecord where id in ("+order_RD_ID.substring(0,order_RD_ID.length()-1)+")");
        	excuteSqlList.add(insertServerSQL.toString());
        	excuteSqlList.add(deleteSQL.toString());
        	excuteSQL(excuteSqlList);
        }
        closeSession();
        rdQuery = null;
        return "获取洋河发货确认数据" + count + "条。\n";
    }
    
}
