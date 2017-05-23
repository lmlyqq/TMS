package com.rd.server.timer;

import java.util.TimerTask;


/**
 * 定时器功能实现类，自动车辆定位操作
 * @author fanglm
 * @create time 2011-5-6 22:00
 *
 */
public class PositionTimerTask extends TimerTask {
	
	private PositionTimerRun sGPositionTimerRun;
    
	@Override
	public void run() {
		
		/**
		 * 自动触发获取数据
		 */
		
		
		if(sGPositionTimerRun == null){
			sGPositionTimerRun = new PositionTimerRun();
		}
		sGPositionTimerRun.run();
	}
    
}
