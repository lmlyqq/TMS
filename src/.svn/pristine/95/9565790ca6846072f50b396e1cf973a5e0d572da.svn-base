package com.rd.server.timer;

import java.util.TimerTask;


/**
 * 定时器功能实现类，发货数据交互操作  ---U8数据接口
 * @author fanglm
 * @create time 2011-4-26 22:00
 *
 */
public class SGSendTimerTask extends TimerTask {
	
	private SGSendTimerRun sgSendTimerRun;
    
	@Override
	public void run() {
		
		/**
		 * 自动触发获取数据
		 */
		
		if(sgSendTimerRun == null){
			sgSendTimerRun = new SGSendTimerRun();
		}
		sgSendTimerRun.run();
	}
    
}
