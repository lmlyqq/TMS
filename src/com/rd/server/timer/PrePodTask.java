package com.rd.server.timer;

import java.util.TimerTask;

import com.rd.server.util.SUtil;


/**
 * 定时器功能实现类  ---预警--应回未回
 * @author fanglm
 * @create time 2010-1-17 21:00
 *
 */
public class PrePodTask extends TimerTask {
    
	@Override
	public void run() {
		
		SUtil.execProcedure("SP_PRE_POD_ALTER(?)");
	}
    
}
