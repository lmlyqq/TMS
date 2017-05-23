package com.rd.server.timer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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

/**
 * 手动接口->拉取订单
 * @createTime 2013-12-13
 * @author wangJun
 *
 */
public class U8SGManualAction{
	
	protected static Configuration hibernateConfig;
    protected static SessionFactory sessionFactory;
    protected static Properties properties;
    
    protected static Configuration hibernateConfig2;
    protected static Properties properties2;
    protected final static ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
    
    private Query query;
    private Query itemQuery;
    private Session session;
    
    private Statement stmt;
	
	private StringBuffer insertSQL ;
	
	private StringBuffer msg = new StringBuffer();

    /**
     * 中间库配置文件
     */
    private static synchronized void createConfig() {
        hibernateConfig = new Configuration();
        Configuration config = hibernateConfig.configure("hibernate.cfg_u8.xml");
        sessionFactory = config.buildSessionFactory();
    }
    
	public String doSth(String custom_odr_no){
		msg = new StringBuffer();
		msg.append("拉取双沟订单开始！\n");
		try{
			if(custom_odr_no.indexOf(",") > 0) {
				custom_odr_no = "'" + custom_odr_no.replaceAll(",", "','") + "'";
				String[] odrlist = custom_odr_no.split(",");
				msg.append("共" + odrlist.length + "个发货单号! \n");
			}
			else {
				custom_odr_no = "'" + custom_odr_no + "'";
				msg.append("共1个发货单号! \n");
			}
			msg.append("开始获取订单头信息 ...\n");
			String result = orderHeader(custom_odr_no);
			if(result.length() > 0) {
				msg.append("开始获取订单明细信息...\n");
				orderItem(result.substring(0,result.length()-1));
			}
			msg.append("开始生成托运单...\n");
			execProcedure();//生成托运单
			msg.append("生成托运单完毕!\n");
		}catch(Exception e){
			e.printStackTrace();
			Log4j.error(StaticRef.USER_LOG, e.getMessage());
			msg.append("拉取订单失败:" + e.getMessage() + " \n");
		}
		msg.append("拉取双沟订单结束！\n");
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
    	if(session  ==  null || !session.isConnected() || !session.isOpen())  {
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
    private boolean excuteListSQL(ArrayList<String> sql){
    	boolean isSuccess = false;
    	Connection conn = null;
    	try {
    		conn = LoginContent.getInstance().getConnection();
	        stmt = conn.createStatement();
			for (int i = 0; i < sql.size(); i++) {
				stmt.execute(sql.get(i).toString());
			}
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
			Log4j.error(StaticRef.USER_LOG, e.getMessage());
			isSuccess = true;
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
	                Log4j.error(StaticRef.USER_LOG, e.getMessage());
	                isSuccess = true;
	            }
		}
		return isSuccess;
    }
    
    /**
     *  u8 数据库的SAP_ORDER_HEADER表
     *  TMS 数据库的 SAP_ORDER_HEADER
     *  SAP_ORDER_HEADER_BAK 备份表
     *  @insertSQL 插入oracle的拼接SQL语句
     */
    @SuppressWarnings("unchecked")
	private String orderHeader(String custom_odr_no) throws Exception{
    	int count = 0;
    	StringBuffer sql = new StringBuffer();
    	StringBuffer headerID = new StringBuffer();
    	ArrayList<String> sqlList = new ArrayList<String>();
    	sql.append("select ID,CTYPE,CVOUCHTYPE,DLID,CDLCODE,DDATE,CCUSCODE,CSHIPADDRESS,CCUSPERSON,CCUSPHONE,CCUSHAND,BRETURNFLAG");
    	sql.append(",COWHCODE,CIWHCODE,DMDATE,CMAKER,CADDCODE,CDELIVERYCODE,FH01_FHXX,CDEFINE6,CSAPSOCODE,CCUSCITY from SGDispatchList_bak");
    	sql.append(" where cdlcode in (");
    	sql.append(custom_odr_no);
    	sql.append(")");
    	query = getSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    	List<HashMap<String, Object>> object = query.list();
    	
    	session.flush();
    	
    	if(object.size() > 0) {
	    	for(int i = 0; i<object.size(); i++){
	    		int id = Integer.parseInt(object.get(i).get("ID")+"");
	            String ctype =	object.get(i).get("CTYPE").toString();
	            String cvouchtype = ObjUtil.ifObjNull(object.get(i).get("CVOUCHTYPE"),"").toString();
	            String dlid =	ObjUtil.ifObjNull(object.get(i).get("DLID"),"").toString();
	            String cdlcode = ObjUtil.ifObjNull(object.get(i).get("CDLCODE"),"").toString();
	            String ddate =	ObjUtil.ifObjNull(object.get(i).get("DDATE"),"").toString();
	            String ccuscode = ObjUtil.ifObjNull(object.get(i).get("CCUSCODE"),"").toString();
	            String cshipaddress = ObjUtil.ifObjNull(object.get(i).get("CSHIPADDRESS"),"").toString();
	            String ccusPerson = ObjUtil.ifObjNull(object.get(i).get("CCUSPERSON"),"").toString();
	            String ccusPhone = ObjUtil.ifObjNull(object.get(i).get("CCUSPHONE"),"").toString();
	            String ccusHand = ObjUtil.ifObjNull(object.get(i).get("CCUSHAND"),"").toString();
	            String breturnflag = ObjUtil.ifObjNull(object.get(i).get("BRETURNFLAG"),"").toString();
	            String cowhcode = ObjUtil.ifObjNull(object.get(i).get("COWHCODE"),"").toString();
	            String ciwhcode = ObjUtil.ifObjNull(object.get(i).get("CIWHCODE"),"").toString();
	            String dmdate = ObjUtil.ifObjNull(object.get(i).get("DMDATE"),"").toString();
	            String cmaker = ObjUtil.ifObjNull(object.get(i).get("CMAKER"),"").toString();
	            String caddCode = ObjUtil.ifObjNull(object.get(i).get("CADDCODE"),"").toString();
	            String cDeveryCode = ObjUtil.ifObjNull(object.get(i).get("CDELIVERYCODE"),"").toString();
	            String fhxx = ObjUtil.ifObjNull(object.get(i).get("FH01_FHXX"),"").toString();
	            String cDefine6 = ObjUtil.ifObjNull(object.get(i).get("CDEFINE6"),"").toString();
	            String CSAPSOCODE = ObjUtil.ifObjNull(object.get(i).get("CSAPSOCODE"),"").toString();
	            String CCUSCITY = ObjUtil.ifObjNull(object.get(i).get("CCUSCITY"),"").toString();
	            String returnFlag = "0";
	            if("false".equals(breturnflag)){
	            	returnFlag = "0";
	            }else if("true".equals(breturnflag)){
	            	returnFlag = "1";
	            }
	            insertSQL = new StringBuffer();
	            insertSQL.append(" insert into U8_SG_ORDER_HEADER(ID,CTYPE,CVOUCHTYPE,DLID,CDLCODE,DDATE,CCUSCODE,CSHIPADDRESS,BRETURNFLAG,COWHCODE,CIWHCODE,DMDATE,CMAKER,CCUSPERSON,CCUSPHONE,CCUSHAND,CADDCODE,CDEVERYCODE,FHXX,CSAPSOCODE,CCUSCITY,CDEFINE6)  values(");
	            insertSQL.append(id+",'"+ctype+"','"+cvouchtype+"','"+dlid+"','"+cdlcode
	           		          +"',to_timestamp('"+ddate+"','yyyy_mm_dd hh24:mi:ss.ff'),'"+ccuscode
	           		          +"','"+cshipaddress+"','" + returnFlag + "','"+cowhcode+"','"
	           		          +ciwhcode+"',to_timestamp('"+dmdate+"','yyyy_mm_dd hh24:mi:ss.ff'),'"+cmaker 
	           		          +"','"+ccusPerson +"','" + ccusPhone+"','"+ccusHand+ "','"+ caddCode +"','" + cDeveryCode + "','" + fhxx
	           		          + "','" + CSAPSOCODE + "','" + CCUSCITY
	           		          + "',to_timestamp('"+cDefine6+"','yyyy_mm_dd hh24:mi:ss.ff'))");
	            sqlList.add(insertSQL.toString());
	            
	            headerID.append(id+",");
	            
	            count ++ ;
	         }
	    	 if(excuteListSQL(sqlList)) {
	    		 msg.append("获取订单头信息完成,共找到" + count + "条符合条件的记录!");
	    	 }
    	}
    	else {
    		msg.append("获取订单头信息完成,未找到符合条件的记录!");
    	}
        closeSession();
		query = null;
        return headerID.toString();
    }
    
    /**
     * U8接口订单明细处理
     * @author lijun
     */
    @SuppressWarnings("unchecked")
	private void orderItem(String fid)  throws Exception{
    	
    	int count=0;
    	StringBuffer itemSQL = new StringBuffer();
    	StringBuffer orderItemID = new StringBuffer();
    	ArrayList<String> itemList = new ArrayList<String>();
    	itemSQL.append("select iID,ID,CVOUCHTYPE,DLID,AUTOID,CINVCODE,CWHCODE,IQUANTITY,CBATCH,CFREE2,CSBVCODE,DBVDATE from sgDispatchLists_bak");
    	itemSQL.append(" where id in (");
    	itemSQL.append(fid);
    	itemSQL.append(")");
    	itemQuery = getSession().createSQLQuery(itemSQL.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    	List<HashMap<String,Object>>  itemObject = itemQuery.list();
    	
    	session.flush();
    	
    	if(itemObject.size() > 0) {
	        for(int i = 0 ; i< itemObject.size(); i++){
	        	String iId = ObjUtil.ifObjNull(itemObject.get(i).get("iID"),"").toString();
	        	String FID = ObjUtil.ifObjNull(itemObject.get(i).get("ID"),"").toString();
	        	String CVOUCHTYPE = ObjUtil.ifObjNull(itemObject.get(i).get("CVOUCHTYPE").toString(), "").toString();
	        	String DLID = ObjUtil.ifObjNull(itemObject.get(i).get("DLID"),"").toString();
	        	String AUTOID = ObjUtil.ifObjNull(itemObject.get(i).get("AUTOID"),"").toString();;
	        	String CINVCODE = ObjUtil.ifObjNull(itemObject.get(i).get("CINVCODE"),"").toString();
	        	String CWHCODE = ObjUtil.ifObjNull(itemObject.get(i).get("CWHCODE"),"").toString();
	        	String IQUANTITY = ObjUtil.ifObjNull(itemObject.get(i).get("IQUANTITY")," ").toString();
	        	String CBATCH = ObjUtil.ifObjNull(itemObject.get(i).get("CBATCH"),"").toString();
	        	String CFREE2 = ObjUtil.ifObjNull(itemObject.get(i).get("CFREE2"),"").toString();
	        	String CSBVCODE = ObjUtil.ifObjNull(itemObject.get(i).get("CSBVCODE"),"").toString();
	        	String DBVDATE = ObjUtil.ifObjNull(itemObject.get(i).get("DBVDATE"),"").toString();
	        	
	        	orderItemID.append(iId+",");
	        	
	        	insertSQL = new StringBuffer();
	        	insertSQL.append(" insert into U8_SG_ORDER_ITEM(ID,CVOUCHTYPE,DLID,AUTOID,CINVCODE" +
	        			",CWHCODE,IQUANTITY,CBATCH,CFREE2,FID,CSBVCODE,DBVDATE) values(");
	        	insertSQL.append(iId+",'"+CVOUCHTYPE+"','"+DLID+"','"+AUTOID+"','"+CINVCODE+"','"
	        			+CWHCODE+"','"+IQUANTITY+"','"+CBATCH+"','"+CFREE2+"',"+FID+",'"+CSBVCODE+"','"+DBVDATE+"')");
	        	
	        	itemList.add(insertSQL.toString());
	        	count ++;
	        }
	        if(excuteListSQL(itemList)) {
	    		 msg.append("获取订单明细信息完成,共找到" + count + "条符合条件的记录!");
	    	 }
    	}
    	else {
    		msg.append("获取订单明细信息完成,未找到符合条件的记录!");
    	}
        closeSession();
        return;
    }
    
    /**
     * U8接口数据，托运单生成
     * @author fanglm
     * @createtime 2011-01-06 16:21
     */
    @SuppressWarnings("deprecation")
	public String execProcedure() throws Exception{
       	String result = StaticRef.SUCCESS_CODE;
		String sql = "{call " + "U8_SG_ALTER(?,?,?,?)" + "}";
		Session session = LoginContent.getInstance().getSession();
		Connection conn = null;
		CallableStatement cs = null;
		try{
			conn = session.connection();
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			result = cs.getString(1);
			//System.out.println(result);
		}catch (Exception ee) {
			try{
				result = "01" + ee.getMessage();
				System.out.println(result);
				Log4j.error(StaticRef.USER_LOG, ee.getMessage());
				LoginContent.getInstance().closeSession();
				session = null;
			}catch (Exception e) {
				e.printStackTrace();
			}
			ee.printStackTrace();
		}
		finally {
			try {
				if(cs != null) {
					cs.close();
				}
				if(conn != null) {
					conn.close();
				}
				if(session != null) {
					LoginContent.getInstance().closeSession();
					session = null;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
    }
}
