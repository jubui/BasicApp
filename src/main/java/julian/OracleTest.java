package julian;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Driver;
import java.util.Properties;

public class OracleTest {
  public void execute() {
    System.out.println(System.getProperty("javax.net.ssl.trustStore"));
//    setup();
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
      properties.setProperty("javax.net.ssl.trustStore",
          "/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/lib/security/cacerts");
      properties.setProperty("javax.net.ssl.trustStoreType","JKS");
      properties.setProperty("javax.net.ssl.trustStorePassword","changeit");
      d.connect(connectionUrl, properties);
      System.out.println("Connection successful");
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void addCert(KeyStore ks, String certPath, String alias) throws CertificateException, FileNotFoundException, KeyStoreException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    System.out.println("Loading: " + certPath);
    InputStream is = new FileInputStream(certPath);
    X509Certificate mycert = (X509Certificate) cf.generateCertificate(is);

    ks.setCertificateEntry(alias, mycert);
  }

  public void setup() {
    try {

      TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
      ks.load(null); // You don't need the KeyStore instance to come from a file.

      for (int i = 1; i <= 5; i++) {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        String path = "/Users/julian.bui/Downloads/us-east-1-bundle-" + i + ".der";
        System.out.println("Loading: " + path);
        InputStream is = new FileInputStream(path);
        X509Certificate mycert = (X509Certificate) cf.generateCertificate(is);

        ks.setCertificateEntry("mycert" + i, mycert);
      }

      addCert(ks, "/Users/julian.bui/Downloads/global-bundle.pem", "global-bundle");

      tmf.init(ks);
      for (TrustManager tm : tmf.getTrustManagers()) {
        System.out.println(tm);
      }
      // Create SSLContext with desired SSL/TLS configuration
      SSLContext sslContext = SSLContext.getInstance("TLS");
      // Initialize SSLContext. Example: using default TrustManager and KeyManager
      sslContext.init(null, tmf.getTrustManagers(), null);

      SSLContext.setDefault(sslContext);
    } catch (Exception e) {
      System.out.println("JULIAN, error: " + e);
      e.printStackTrace();
    }
  }
}
