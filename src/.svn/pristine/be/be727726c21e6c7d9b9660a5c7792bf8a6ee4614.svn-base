package com.rd.server.timer;

import java.util.TimerTask;


/**
 * 定时器功能实现类，数据交互操作  ---U8数据接口
 * @author fanglm
 * @create time 2010-12-23 15:00
 *
 */
public class SGTimerTask extends TimerTask {
	
	private SGTimerRun sgTimerRun;
    
	@Override
	public void run() {
		
		/**
		 * 自动触发获取数据
		 */
		
		if(sgTimerRun == null){
			sgTimerRun = new SGTimerRun();
		}
		
		sgTimerRun.run();
		
	}
    
}
