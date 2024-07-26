package julian;

import org.mariadb.jdbc.Configuration;
import org.mariadb.jdbc.HostAddress;
import org.mariadb.jdbc.export.ExceptionFactory;
import org.mariadb.jdbc.plugin.TlsSocketPlugin;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.SQLException;

public class MariaTlsSocketPlugin implements org.mariadb.jdbc.plugin.TlsSocketPlugin {

  private SSLContext delegate;
  private TrustManagerFactory tmf;
  private void addCert(KeyStore ks, String certPath, String alias) throws CertificateException, FileNotFoundException, KeyStoreException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    System.out.println("Loading: " + certPath);
    InputStream is = new FileInputStream(certPath);
    X509Certificate mycert = (X509Certificate) cf.generateCertificate(is);

    ks.setCertificateEntry(alias, mycert);
  }

  public MariaTlsSocketPlugin() {
    try {

      tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
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
      delegate = sslContext;
    } catch (Exception e) {
      System.out.println("JULIAN, error: " + e);
      e.printStackTrace();
    }
  }
  @Override
  public String type() {
    return "MariaTlsSocketPlugin";
  }

  @Override
  public TrustManager[] getTrustManager(Configuration conf, ExceptionFactory exceptionFactory, HostAddress hostAddress) throws SQLException {
    return tmf.getTrustManagers();
  }

  @Override
  public KeyManager[] getKeyManager(Configuration conf, ExceptionFactory exceptionFactory) throws SQLException {
    return new KeyManager[0];
  }

  @Override
  public SSLSocket createSocket(Socket socket, SSLSocketFactory sslSocketFactory) throws IOException {
    return TlsSocketPlugin.super.createSocket(socket, delegate.getSocketFactory());
  }

  @Override
  public void verify(String host, SSLSession sslSession, long serverThreadId) throws SSLException {
    try {
      Certificate[] certs = sslSession.getPeerCertificates();
      X509Certificate cert = (X509Certificate) certs[0];
      org.mariadb.jdbc.client.tls.HostnameVerifier.verify(host, cert, serverThreadId);
    } catch (SSLException ex) {
      System.out.println("JULIAN got exception: " + ex);
      ex.printStackTrace();
      throw ex;
    }
  }
}
