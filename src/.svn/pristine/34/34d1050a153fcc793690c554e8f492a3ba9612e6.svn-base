package com.rd.server.timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 定时器监听器，需要在WEB.XML里注册监听
 * @author fanglm
 * @create time 2010-12-23 15:06
 */

public class TimerListener implements ServletContextListener {

	private YHTimer myTimer = new YHTimer();
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		myTimer.timerStop();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		myTimer.timerStart();
	}

}
