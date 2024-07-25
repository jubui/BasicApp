package julian;

import java.security.Security;
import java.sql.Driver;
import java.util.Properties;

public class MysqlCustomProviderTest {

  public void execute() {
    Security.addProvider(new MysqlCustomProvider());
    try {
    Driver d = (Driver) Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
      // allow all socket factory
//      String connectionUrl = "jdbc:mysql://database-1.c5bl3ymevjvd.us-east-1.rds.amazonaws.com:3306/?useOldAliasMetadataBehavior=true&sslMode=VERIFY_CA";
    String connectionUrl = "jdbc:mysql://database-1.c5bl3ymevjvd.us-east-1.rds.amazonaws.com:3306/?useOldAliasMetadataBehavior=true&sslMode=VERIFY_CA&sslContextProvider=MysqlCustomProvider";
//      Connection conn = DriverManager.getConnection(connectionUrl, "admin", "utf8mb4utf8mb4");
      Properties properties = new Properties();
      properties.put("user", "admin");
      properties.put("password", "utf8mb4utf8mb4");
      d.connect(connectionUrl, properties);
      System.out.println("Connection successful");
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
