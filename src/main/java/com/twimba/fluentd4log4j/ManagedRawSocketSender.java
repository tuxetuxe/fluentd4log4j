package com.twimba.fluentd4log4j;

import org.fluentd.logger.sender.RawSocketSender;
import org.fluentd.logger.sender.Reconnector;

public class ManagedRawSocketSender extends RawSocketSender
{

  private boolean closed = false;

  public ManagedRawSocketSender()
  {
    super();
  }

  public ManagedRawSocketSender(String host, int port, int timeout, int bufferCapacity, Reconnector reconnector)
  {
    super(host, port, timeout, bufferCapacity, reconnector);
    if (isClosed())
    {
      throw new RuntimeException("Unable to start the fluentd sender.");
    }
  }

  public ManagedRawSocketSender(String host, int port, int timeout, int bufferCapacity)
  {
    super(host, port, timeout, bufferCapacity);
  }

  public ManagedRawSocketSender(String host, int port)
  {
    super(host, port);
  }

  @Override
  public void close()
  {
    super.close();
    closed = true;
  }

  public boolean isClosed()
  {
    return closed;
  }

  public void setClosed(boolean closed)
  {
    this.closed = closed;
  }

}
