# fluentd4log4j
_A Log4J appender to push messages to a fluentd server._

## How to Use

### Configuration
| property      | default value    | Description  |
| ------------- |------------------| -------------|
| mdcKeys | "" | The MDC keys that should be added to the log structure. |
| tagPrefix | ""| The fluentd tag prefix to be used |
| tag | "log" | The fluentd tag to be used in all the log messages sent there |
| host | "localhost" | The fluentd server host to where to send the log messages. |
| port | 24224 | The fluentd server port to where to send the log messages. |
| timeout | 15000 (15s) | The timeout (in milliseconds) to connect to the fluentd server|
| bufferCapacity | 1048576 (1Mb) | The socket buffer capacity to connect to the fluentd server |
| useConstantDelayReconnector| false | Switch from the default Exponential Delay reconnector to a constant delay reconnector |

### Example

## License
This is available in the Apache Licence 2.0
http://www.tldrlegal.com/license/apache-license-2.0-(apache-2.0)
