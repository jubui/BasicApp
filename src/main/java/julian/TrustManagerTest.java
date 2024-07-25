package julian;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class TrustManagerTest {
  public void execute() {
  try {
    // You could get a resource as a stream instead.

    /*
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
      if (tm instanceof X509TrustManager) {
        System.out.println("Found x509 trust manager");
        X509TrustManager xtm = (X509TrustManager) tm;
      }
    }

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, tmf.getTrustManagers(), null);
    SSLContext.setDefault(sslContext);
     */

    Driver d = (Driver) Class.forName("org.mariadb.jdbc.Driver").getDeclaredConstructor().newInstance();
//    Driver d = (Driver) Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

     String connectionUrl = "jdbc:mariadb://database-1.c5bl3ymevjvd.us-east-1.rds.amazonaws.com:3306/?useOldAliasMetadataBehavior=true&useSSL=true&sslMode=verify-ca";
//    String connectionUrl = "jdbc:mariadb://database-1.c5bl3ymevjvd.us-east-1.rds.amazonaws.com:3306/?useOldAliasMetadataBehavior=true&useSSL=true&sslMode=verify-ca&socketFactory=julian.AllowAllCustomSocketFactory&enablePacketDebug=true&trustServerCertificate=true";
    // allow all socket factory
//    String connectionUrl = "jdbc:mariadb://database-1.c5bl3ymevjvd.us-east-1.rds.amazonaws.com:3306/?useOldAliasMetadataBehavior=true&useSSL=true&sslMode=verify-ca&socketfactory=julian.AllowAllCustomSocketFactory";
    Connection conn = DriverManager.getConnection(connectionUrl, "admin", "utf8mb4utf8mb4");
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
}
