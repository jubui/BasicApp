package julian;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class CertTest {

  public void execute() {
    System.setProperty("javax.net.ssl.trustStore", "/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/lib/security/jssecacerts");
    System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

    try {
      Thread.sleep(60000);
    } catch (Exception e) {

    }
    Driver d = null;
    try {
      // Maria
      d = (Driver) Class.forName("org.mariadb.jdbc.Driver").newInstance();
      // Works by specifying the pem
      // String connectionUrl = "jdbc:mariadb://database-1.c5bl3ymevjvd.us-east-1.rds.amazonaws.com:3306/?useOldAliasMetadataBehavior=true&useSSL=true&sslMode=verify-ca&serverSslCert=/Users/julian.bui/Downloads/global-bundle.pem";
      // Works if you add the certs to the trust store
      String connectionUrl = "jdbc:mariadb://database-1.c5bl3ymevjvd.us-east-1.rds.amazonaws.com:3306/?useOldAliasMetadataBehavior=true&useSSL=true&sslMode=verify-ca";
      Connection conn = DriverManager.getConnection(connectionUrl, "admin", "utf8mb4utf8mb4");

      System.out.println("Done connecting");
    } catch (Exception e) {
      System.out.println("Could not connect");
      e.printStackTrace();
    }
  }
}
