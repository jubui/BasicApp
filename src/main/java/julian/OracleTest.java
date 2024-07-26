package julian;

import java.security.Security;
import java.sql.Driver;
import java.util.Properties;

public class OracleTest {
  public void execute() {
    System.out.println(System.getProperty("javax.net.ssl.trustStore"));
//    setup();
    Security.insertProviderAt(new MysqlCustomProvider(), 1);
    try {
//      System.setProperty("javax.net.ssl.trustStore", "/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/lib/security/jssecacerts");
//      System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
//      System.setProperty("javax.net.debug", "ssl");
      Driver d = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver").getDeclaredConstructor().newInstance();
      // allow all socket factory
//      String connectionUrl = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcps)(HOST=mousumi-oracle.c5bl3ymevjvd.us-east-1.rds.amazonaws.com)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=ORCL)))";
      String connectionUrl = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcps)(HOST=mousumi-oracle.c5bl3ymevjvd.us-east-1.rds.amazonaws.com)(PORT=2484))(CONNECT_DATA=(SERVICE_NAME=ORCL)))";
      Properties properties = new Properties();
      properties.setProperty("user", "admin");
      properties.setProperty("password", "utf8mb3utf8mb3");
      // USING JSSECACERTS WORKS
//      properties.setProperty("javax.net.ssl.trustStore",
//          "/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/lib/security/jssecacerts");

      // USING CACERTS ALSO WORKS, as long as we specify it
//      properties.setProperty("javax.net.ssl.trustStore",
//          "/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/lib/security/cacerts");
//      properties.setProperty("javax.net.ssl.trustStoreType","JKS");
//      properties.setProperty("javax.net.ssl.trustStorePassword","changeit");
      d.connect(connectionUrl, properties);
      System.out.println("Connection successful");
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
