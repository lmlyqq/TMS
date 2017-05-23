package com.rd.server.timer;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.rd.client.common.util.ObjUtil;
import com.rd.server.util.LoginContent;


/**
 * 监听器类
 * @author fanglm
 * @create time 2010-12-23 10:38
 */

public class YHTimer {

	public Timer timer;
	public Timer timer1;
	public Timer timer2;
	public Timer timer3;
	public Timer timer4;
	public Timer feeTimer;
	    
	
	
	@SuppressWarnings("unchecked")
	public void timerStart(){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from sys_timer where enable_flag='Y'");
		Session session = LoginContent.getInstance().getSession();
		Query query = session.createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<HashMap<String, Object>> object = query.list();
		
		timer = new Timer();
		timer1 = new Timer();
		timer2 = new Timer();
		timer3 = new Timer();
		timer4 = new Timer();
		feeTimer = new Timer();
		//立即执行，然后每隔30秒执行一次
		for(int i=0;i<object.size();i++){
			int mi = Integer.parseInt(ObjUtil.ifObjNull(object.get(i).get("RUN_TIME2"),"0").toString());
			String[] time = ObjUtil.ifObjNull(object.get(i).get("RUN_TIME"),"").toString().split(":");
			Date date = null;
			long interval = 0;
			if(time.length == 2){
				Calendar calender = Calendar.getInstance();
				calender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
				calender.set(Calendar.MINUTE, Integer.parseInt(time[1]));
				
				date = calender.getTime();
				Date now = new Date();
				interval = date.getTime() - now.getTime();
				
				//超过起始时间点，等待下一次运行时间点
				if(interval < 0){
					calender.add(Calendar.DAY_OF_MONTH, 1);
					date = calender.getTime();
					
					interval = date.getTime() - now.getTime();
				}
			}
			//u8洋河接口   fanglm 2011-4-26
			if("U8_ALTER".equals(object.get(i).get("TIMER"))){
				if(mi > 0){
					YHTimerTask u8Task = new YHTimerTask();
					if(date == null){
						timer.schedule(u8Task, 0, (long)mi*60*1000);
					}else{
						timer.schedule(u8Task,interval,(long)mi*60*1000);		
					}
				}
			}
			//u8洋河发货接口
			if("U8_SEND".equals(object.get(i).get("TIMER"))){
				if(mi > 0){
					YHSendTimerTask u8SendTask = new YHSendTimerTask();
					if(date == null){
						timer1.schedule(u8SendTask, 0, (long)mi*60*1000);
					}else{
						timer1.schedule(u8SendTask,interval,(long)mi*60*1000);
					}
				}
			}
			
			//u8双沟接口   fanglm 2011-4-26
			if("U8_SG_ALTER".equals(object.get(i).get("TIMER"))){
				if(mi > 0){
					SGTimerTask u8Task = new SGTimerTask();
					if(date == null){
						timer2.schedule(u8Task, 0, (long)mi*60*1000);
					}else{
						timer2.schedule(u8Task,interval,(long)mi*60*1000);		
					}
				}
			}
			//u8双沟发货接口
			if("U8_SG_SEND".equals(object.get(i).get("TIMER"))){
				if(mi > 0){
					SGSendTimerTask u8SendTask = new SGSendTimerTask();
					if(date == null){
						timer3.schedule(u8SendTask, 0, (long)mi*60*1000);
					}else{
						timer3.schedule(u8SendTask,interval,(long)mi*60*1000);
					}
				}
			}
			//预警--应发未发
			if("SP_PRE_LOAD_ALTER".equals(object.get(i).get("TIMER"))){
				if(mi > 0){
					PreLoadTask loadTask = new PreLoadTask();
					if(date == null){
						timer.schedule(loadTask, 0, (long)mi*60*1000);
					}else{
						timer.schedule(loadTask,interval,(long)mi*60*1000);
					}
				}
			}
			
			//预警--应收未收
			if("SP_PRE_UNLOAD_ALTER".equals(object.get(i).get("TIMER"))){
				if(mi > 0){
					PreUnloadTask unloadTask = new PreUnloadTask();
					if(date == null){
						timer.schedule(unloadTask, 0, (long)mi*60*1000);  //yuanlei 2011-2-14
					}else{
						timer.schedule(unloadTask,interval,(long)mi*60*1000);
					}
				}
			}
			
			//预警--应回未回
			if("SP_PRE_UNLOAD_ALTER".equals(object.get(i).get("TIMER"))){
				if(mi > 0){
					PrePodTask podTask = new PrePodTask();
					if(date == null){
						timer.schedule(podTask, 0, (long)mi*60*1000);
					}else{
						timer.schedule(podTask,interval,(long)mi*60*1000);
					}
				}
			}
			//车辆定位
			if("POSITION_JK".equals(object.get(i).get("TIMER"))){
				if(mi > 0){
					PositionTimerTask podTask = new PositionTimerTask();
					timer4.schedule(podTask, 0, (long)mi*60*1000);
				}
			}
			if("SP_PRE_RECEIPT".equals(object.get(i).get("TIMER"))){
				if(mi > 0){
					PreReceiptTimerTask feeTask = new PreReceiptTimerTask();
					feeTimer.schedule(feeTask, 0,(long)mi*60*1000);
					
				}
			}
		}
		LoginContent.getInstance().closeSession();
		session = null;
	}
	
	public void timerStop(){
		if(timer != null)
			timer.cancel();
		if(timer4 != null)
			timer4.cancel();
	}
}
