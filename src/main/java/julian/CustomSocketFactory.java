package julian;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.*;

public class CustomSocketFactory extends SocketFactory {

  private final SSLSocketFactory sslSocketFactory;

  class InsecureTrustManager implements X509TrustManager {
    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType){
      // BAD: Does not verify the certificate chain, allowing any certificate.
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType){

    }
  }

  public CustomSocketFactory() {
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
//      sslContext.init(null, new TrustManager[] { new InsecureTrustManager() }, null);
      // Get SSLSocketFactory from SSLContext
      sslSocketFactory = sslContext.getSocketFactory();
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize SSLContext", e);
    }
  }

  private void addCert(KeyStore ks, String certPath, String alias) throws CertificateException, FileNotFoundException, KeyStoreException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    System.out.println("Loading: " + certPath);
    InputStream is = new FileInputStream(certPath);
    X509Certificate mycert = (X509Certificate) cf.generateCertificate(is);

    ks.setCertificateEntry(alias, mycert);
  }

  @Override
  public Socket createSocket() throws IOException {
    return sslSocketFactory.createSocket();
  }

  @Override
  public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
    return sslSocketFactory.createSocket(host, port);
  }

  @Override
  public Socket createSocket(InetAddress address, int port) throws IOException {
    return sslSocketFactory.createSocket(address, port);
  }

  @Override
  public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
      throws IOException {
    return sslSocketFactory.createSocket(address, port, localAddress, localPort);
  }

  @Override
  public Socket createSocket(String host, int port, InetAddress localAddress, int localPort) throws IOException, UnknownHostException {
    return sslSocketFactory.createSocket(host, port, localAddress, localPort);
  }
}
