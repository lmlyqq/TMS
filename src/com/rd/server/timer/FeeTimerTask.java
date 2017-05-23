package com.rd.server.timer;

import java.util.TimerTask;


/**
 * 定时器功能实现类，计算费用定时器
 * @author fanglm
 * @create time 2012-06-24 15:00
 *
 */
public class FeeTimerTask extends TimerTask {
	
	private FeeTimerRun feeTimerRun;
    
	@Override
	public void run() {
		
		/**
		 * 自动触发获取数据
		 */
		
		if(feeTimerRun == null){
			feeTimerRun = new FeeTimerRun();
		}
		feeTimerRun.run();
		
	}
    
}
