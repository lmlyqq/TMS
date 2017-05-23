package com.rd.server.timer;

import java.util.TimerTask;

import com.rd.server.util.SUtil;


/**
 * 定时器功能实现类，预警--应到未到
 * @author fanglm
 * @create time 2010-12-23 15:00
 *
 */
public class PreUnloadTask extends TimerTask {
	
    
	@Override
	public void run() {
		SUtil.execProcedure("SP_PRE_UNLOAD_ALTER(?)");
	}
    
}
