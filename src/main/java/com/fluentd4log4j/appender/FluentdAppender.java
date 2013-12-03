package com.fluentd4log4j.appender;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.fluentd.logger.FluentLogger;
import org.fluentd.logger.sender.ConstantDelayReconnector;
import org.fluentd.logger.sender.ExponentialDelayReconnector;
import org.fluentd.logger.sender.Reconnector;

public class FluentdAppender extends AppenderSkeleton
{

  private FluentLogger fluentLogger;

  /**
   * The MDC keys that should be added to the log structure.
   * <b>Default:</b> none
   */
  private String       mdcKeys                     = "";

  /**
   * The fluentd tag prefix to be used
   * <b>Default:</b> "" (empty string)
   */
  private String       tagPrefix                   = "";

  /**
   * The fluentd tag to be used in all the log messages sent there.
   * <b>Default:</b> "log"
   */
  private String       tag                         = "log";
  /**
   * The fluentd server host to where to send the log messages.
   * <b>Default:</b> "localhost"
   */
  private String       host                        = "localhost";
  /**
   * The fluentd server port to where to send the log messages.
   * <b>Default:</b> 24224
   */
  private int          port                        = 24224;
  /**
   * The timeout (in milliseconds) to connect to the fluentd server
   * <b>Default:</b> 15000 (15s)
   */
  private int          timeout                     = 15 * 1000;  // 15s
  /**
   * The socket buffer capacity to connect to the fluentd server
   * <b>Default:</b> 1048576 (1Mb)
   */
  private int          bufferCapacity              = 1024 * 1024; // 1M

  /**
   * Switch from the default Exponential Delay reconnector to a constant delay reconnector
   * <b>Default:</b> false (use the Exponential Delay reconnector)
   */
  private boolean      useConstantDelayReconnector = false;

  @Override
  public void activateOptions()
  {
    Reconnector reconnector = null;

    if (useConstantDelayReconnector)
    {
      reconnector = new ConstantDelayReconnector();
    }
    else
    {
      reconnector = new ExponentialDelayReconnector();
    }

    fluentLogger = FluentLogger.getLogger(tagPrefix, host, port, timeout, bufferCapacity, reconnector);

    LogLog.warn("FluentdAppender connected to fluentd! (host=" + host + ", port=" + port + ",timeout=" + timeout + ",bufferCapacity="
                + bufferCapacity + ",tagPrefix=" + tagPrefix + ")");
  }

  public void close()
  {
    if (fluentLogger != null)
    {
      fluentLogger.flush();
      fluentLogger.close();
    }
  }

  public boolean requiresLayout()
  {
    return false;
  }

  @Override
  protected void append(LoggingEvent event)
  {
    if (fluentLogger == null)
    {
      // Ups! Something very wrong is going on here! Bail out!
      LogLog.warn("FluentdAppender has no fluentLogger. Please check your configuration and/or logs for errors!");
      return;
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("message", event.getMessage());
    data.put("loggerClass", event.getFQNOfLoggerClass());
    data.put("level", event.getLevel().toString());
    data.put("locationInformation", event.getLocationInformation().fullInfo);
    data.put("logger", event.getLoggerName());
    data.put("threadName", event.getThreadName());
    data.put("throwableInformation", event.getThrowableStrRep());
    data.put("NDC", event.getNDC());
    for (String mdcKey : mdcKeys.split(","))
    {
      data.put(mdcKey, event.getMDC(mdcKey));
    }
    fluentLogger.log(tag, data);
  }

}
