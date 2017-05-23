package com.rd.server.timer;


import com.rd.server.util.SUtil;


/**
 * 计费引擎，定时器自动计算费用
 * @author fanglm
 * @create time 2012-06-23 15:00
 *
 */
public class FeeTimerRun {
	
    
	public void run() {
		doSth();
	}
		
	public String doSth(){
		StringBuffer msg = new StringBuffer();
		msg.append("连接数据库成功！\n");
		System.out.println("开始计算应收费用！");
		try{
			SUtil.execProcedure("SP_FEE_TIMER(?)");
		}catch(Exception e){
			e.printStackTrace();
			return "接口服务器连接失败，请通知管理员！\n";
		}
		msg.append("处理完毕！\n");
		System.out.println(msg.toString());
		return msg.toString();
	}
    
}
