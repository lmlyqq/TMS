package com.rd.server.timer;

import java.util.TimerTask;

import com.rd.server.util.SUtil;


/**
 * 定时器功能实现类  ---U8数据接口
 * @author fanglm
 * @create time 2010-1-17 20:34
 *
 */
public class PreLoadTask extends TimerTask {
    
	@Override
	public void run() {
		SUtil.execProcedure("SP_PRE_LOAD_ALTER(?)");
	}
    
}
