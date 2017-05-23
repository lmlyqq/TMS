package com.rd.server.util;

import java.util.Date;
import java.util.Hashtable;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 日志消息类
 * @author sandy
 *
 */
public class Log4j {
	  private static Hashtable<String, Logger> loggerTable = new Hashtable<String, Logger>();
	  private static boolean isDebugFlag = true;

	  public static void init(String LogCfgFile) {
	    try {
	      PropertyConfigurator.configure(LogCfgFile);
	    } catch (Exception err) {
	      err.printStackTrace();
	    }
	  }

	  private static boolean existsLogger(String name) {
	    boolean isExists = false;
	    try {
	      Logger loggerTmp = LogManager.exists(name);
	      if (loggerTmp != null)
	        isExists = true;
	    } catch (Exception err) {
	      StringBuffer LogBuff = new StringBuffer();
	      LogBuff.append(new Date().toString());
	      LogBuff.append(" -- ");
	      LogBuff
	        .append("existsLogger(String name) has error，Msg：");
	      LogBuff.append(err.getMessage());
	      System.err.println(LogBuff.toString());
	      err.printStackTrace();
	    }
	    return isExists;
	  }

	  private static boolean isLevelEnabled(String loggerName, int logLevel) {
	    boolean isEnabled = false;
	    try {
	      boolean isExistsLogger = existsLogger(loggerName);
	      if (isExistsLogger) {
	        if (!loggerTable.containsKey(loggerName)) {
	          Logger newLogger = Logger.getLogger(loggerName);
	          loggerTable.put(loggerName, newLogger);
	        }
	        Logger LoggerTmp = (Logger)loggerTable.get(loggerName);
	        switch (logLevel)
	        {
	        case 1:
	          isEnabled = LoggerTmp.isDebugEnabled();
	          break;
	        case 2:
	          isEnabled = LoggerTmp.isInfoEnabled();
	          break;
	        case 3:
	          isEnabled = LoggerTmp.isEnabledFor(Level.WARN);
	          break;
	        case 4:
	          isEnabled = LoggerTmp.isEnabledFor(Level.ERROR);
	          break;
	        case 5:
	          isEnabled = LoggerTmp.isEnabledFor(Level.FATAL);
	        }
	      }
	    }
	    catch (Exception localException) {
	    }
	    return isEnabled;
	  }

	  public static boolean isDebugEnabled(String loggerName) {
	    return isLevelEnabled(loggerName, 1);
	  }

	  public static boolean isInfoEnabled(String loggerName) {
	    return isLevelEnabled(loggerName, 2);
	  }

	  public static boolean isWarnEnabled(String loggerName) {
	    return isLevelEnabled(loggerName, 3);
	  }

	  public static boolean isErrorEnabled(String loggerName) {
	    return isLevelEnabled(loggerName, 4);
	  }

	  public static boolean isFatalEnabled(String loggerName) {
	    return isLevelEnabled(loggerName, 5);
	  }

      private static void writeLog(String loggerName, int logLevel, String logMesg)
      {
        try
        {
            boolean isExistsLogger = existsLogger(loggerName);
            if(isExistsLogger)
            {
                if(!loggerTable.containsKey(loggerName))
                {
                    Logger newLogger = Logger.getLogger(loggerName);
                    loggerTable.put(loggerName, newLogger);
                }
                Logger LoggerTmp = (Logger)loggerTable.get(loggerName);
                //logMesg = "[" + user_name + "]" + logMesg;
                switch(logLevel)
                {
                case 1: // '\001'
                    LoggerTmp.debug(logMesg);
                    break;

                case 2: // '\002'
                    LoggerTmp.info(logMesg);
                    break;

                case 3: // '\003'
                    LoggerTmp.warn(logMesg);
                    break;

                case 4: // '\004'
                    LoggerTmp.error(logMesg);
                    break;

                case 5: // '\005'
                    LoggerTmp.fatal(logMesg);
                    break;
                }
            } else
            if(isDebugFlag)
                System.err.println("The logger named \"" + loggerName + "\" is not exists.");
          }
          catch(Exception err)
          {
              StringBuffer LogBuff = new StringBuffer();
              LogBuff.append((new Date()).toString());
              LogBuff.append(" -- ");
              LogBuff.append("输出日志时遇到错误，Msg：");
              LogBuff.append(err.getMessage());
              System.err.println(LogBuff.toString());
              err.printStackTrace();
          }
      }
	  public static void debug(String loggerName, String logMesg) {
	    writeLog(loggerName, 1, logMesg);
	  }

	  public static void info(String loggerName, String logMesg) {
	    writeLog(loggerName, 2, logMesg);
	  }

	  public static void warn(String loggerName, String logMesg) {
	    writeLog(loggerName, 3, logMesg);
	  }

	  public static void error(String loggerName, String logMesg) {
	    writeLog(loggerName, 4, logMesg);
	  }
}
