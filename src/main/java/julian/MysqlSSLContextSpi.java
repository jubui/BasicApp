package julian;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class MysqlSSLContextSpi extends SSLContextSpi {
  private SSLContext delegate;

  private void addCert(KeyStore ks, String certPath, String alias) throws CertificateException, FileNotFoundException, KeyStoreException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    System.out.println("Loading: " + certPath);
    InputStream is = new FileInputStream(certPath);
    X509Certificate mycert = (X509Certificate) cf.generateCertificate(is);

    ks.setCertificateEntry(alias, mycert);
  }
  public MysqlSSLContextSpi() throws NoSuchAlgorithmException {
    // Initialize with a standard SSLContext implementation
    System.out.println("JULIAN");
//    this.delegate = SSLContext.getInstance("TLS");
//    SSLParameters sslParameters = delegate.getDefaultSSLParameters();
//    sslParameters.setProtocols(new String[]{"TLS"});

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
      delegate = sslContext;
    } catch (Exception e) {
      System.out.println("JULIAN, error: " + e);
      e.printStackTrace();
    }
  }

  @Override
  protected void engineInit(KeyManager[] km, TrustManager[] tm, SecureRandom sr)
      throws KeyManagementException {
    System.out.println("JULIAN");
    System.out.println("ALREADY INITIALIZED");
//    delegate.init(km, tm, sr);
  }

  @Override
  protected SSLSocketFactory engineGetSocketFactory() {
    System.out.println("JULIAN");
    return delegate.getSocketFactory();
  }

  @Override
  protected SSLServerSocketFactory engineGetServerSocketFactory() {
    System.out.println("JULIAN");
    return delegate.getServerSocketFactory();
  }

  @Override
  protected SSLEngine engineCreateSSLEngine() {
    System.out.println("JULIAN");
    return delegate.createSSLEngine();
  }

  @Override
  protected SSLEngine engineCreateSSLEngine(String host, int port) {
    System.out.println("JULIAN");
    return delegate.createSSLEngine(host, port);
  }

  @Override
  protected SSLSessionContext engineGetServerSessionContext() {
    System.out.println("JULIAN");
    return delegate.getServerSessionContext();
  }

  @Override
  protected SSLSessionContext engineGetClientSessionContext() {
    System.out.println("JULIAN");
    return delegate.getClientSessionContext();
  }

  @Override
  protected SSLParameters engineGetSupportedSSLParameters() {
    System.out.println("JULIAN");
    return delegate.getSupportedSSLParameters();
  }

  @Override
  protected SSLParameters engineGetDefaultSSLParameters() {
    System.out.println("JULIAN");
    return delegate.getDefaultSSLParameters();
  }
}