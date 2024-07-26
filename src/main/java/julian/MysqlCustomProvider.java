package julian;

import java.security.Provider;

public class MysqlCustomProvider extends Provider {
  public MysqlCustomProvider() {
    super("MysqlCustomProvider", 1.0, "Custom SSLContext Provider");
//    put("SSLContext.MysqlCustomProvider", MysqlSSLContextSpi.class.getName());
    put("SSLContext.TLS", MysqlSSLContextSpi.class.getName());
//    put("algorithm.MysqlCustomProvider", "TLS");
//    put("protocol.MysqlCustomProvider", "TLS");
//    put("algorithm", "TLS");
//    put("protocol", "TLS");

  }
}