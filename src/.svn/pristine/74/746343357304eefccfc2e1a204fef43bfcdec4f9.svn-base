package com.rd.server.timer;

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
import com.rd.server.GreetingServiceImpl;
import com.rd.server.InterfaceUtil;
import com.rd.server.util.LoginContent;


/**
 * 手机定位定时器功能
 * @author fanglm
 * @create time 2011-5-5 21:00
 *
 */
public class PositionTimerRun {
	
	protected static Configuration hibernateConfig;
    protected static SessionFactory sessionFactory;
    protected static Properties properties;
    
    protected static Configuration hibernateConfig2;
    protected static Properties properties2;
    protected final static ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
    
    private GreetingServiceImpl gs = new GreetingServiceImpl();
	private InterfaceUtil intUtil;
	private HashMap<String, String> loadMap = new HashMap<String, String>();;
	public int row = 0;
	
    
	@SuppressWarnings("unchecked")
	public void run() {
		//定时器时间设置
		StringBuffer sql = new StringBuffer();
		sql.append("select * from sys_timer where timer = 'POSITION_JK' ");
		sql.append("and sysdate > = to_date(to_char(sysdate,'yyyy-mm-dd') || ' ' || run_time,'yyyy-mm-dd hh24:mi') ");
		sql.append("and sysdate < = to_date(to_char(sysdate,'yyyy-mm-dd') || ' ' || run_time1,'yyyy-mm-dd hh24:mi')");
		Session session = LoginContent.getInstance().getSession();
		Query query = session.createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String, Object>> object = query.list();
		
		if(object.size() > 0){
			doSth();
		}
	}
		
	@SuppressWarnings("unchecked")
	public void doSth(){
		if(intUtil == null){
			intUtil = new InterfaceUtil();
		}
		StringBuffer sql = new StringBuffer();
//		sql.append("select distinct t.MOBILE1 from trans_load_header t,bas_staff s where t.mobile1 = s.staff_code(+) and t.status >=40 and t.status <=45 and s.RGST_DRCT_FLAG = 'Y'");
		sql.append("select distinct t.MOBILE as MOBILE1 from trans_shipment_header t where  t.status =40 and t.mobile is not null and t.suplr_name <>'自提物流' and length(t.mobile) = 11 and t.depart_time >= sysdate - 31");

		Query query = LoginContent.getInstance().getSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String, Object>> object = query.list();
		
		if(object.size() > 0){
			String msid = "";
			try{
				//登录远程服务器
//				intUtil.connect();
				ArrayList<HashMap<String, String>> list = null;
				for(int i =0 ;i < object.size() ; i++){
					if(!ObjUtil.isNotNull(loadMap.get(object.get(i).get("MOBILE1")))){
						msid = object.get(i).get("MOBILE1").toString();
						list = intUtil.mobilePosition2(msid);
						excutePro(list);
						msid = "";
						loadMap = new HashMap<String, String>();
					}
//					intUtil.heartBeat();
				}

				
				//断开连接
//				intUtil.disConnect();
				LoginContent.getInstance().closeSession();
			}catch (Exception e) {
				e.printStackTrace();	
			}
		}
		
	}
	
	private void excutePro(ArrayList<HashMap<String, String>> list){
		String oper_type = "定时";
		String login_user = "SYSTEM";
		ArrayList<String> valueList;
		HashMap<String, String> map;
//		for(int i = 0;i<list.size();i++){
			valueList = new ArrayList<String>();
			map = list.get(0);
			String url = map.get("img_url");
			String longitude = "0";
			String latitude = "0";
			row++;
			String mids = map.get("msid");
			if(map.get("result_id").equals("3001")){

				//定位成功，写定位日志与跟踪日志
				if(ObjUtil.isNotNull(url)){
					String[] st = url.split("longitude=");
					longitude = st[1].split("&")[0];
					latitude = st[1].split("latitude=")[1].split("&")[0];
				}
				valueList.add(mids);
				valueList.add(map.get("area_name"));
				valueList.add(oper_type);
				valueList.add(longitude);
				valueList.add(latitude);
				valueList.add(map.get("result_info"));
				valueList.add(login_user);
				
				gs.execProcedure(valueList, "SP_SHPM_POSITION(?,?,?,?,?,?,?,?)");
			}else{//定位失败，写定位日志，错误日志
				valueList.add(mids);
				valueList.add(oper_type);
				valueList.add("N");
				valueList.add(map.get("result_id"));
				valueList.add(map.get("result_info"));
				valueList.add(login_user);
				gs.execProcedure(valueList, "SP_POSITION_LOG(?,?,?,?,?,?,?)");
				
				
			}
//		}
	
	}
    
    
    
}
