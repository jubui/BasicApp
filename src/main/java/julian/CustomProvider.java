package julian;

import java.security.Provider;

public class CustomProvider extends Provider {
  public CustomProvider() {
    super("CustomProvider", 1.0, "Custom SSLContext Provider");
//    put("SSLContext.MysqlCustomProvider", MysqlSSLContextSpi.class.getName());
    put("SSLContext.TLS", CustomSSLContextSpi.class.getName());
  }
}