package julian;

import java.io.IOException;

public class Main {

  public static void main(String[] args) throws InterruptedException, IOException {
    OracleTest oracleTest = new OracleTest();
    oracleTest.execute();

//    MysqlCustomProviderTest mysqlCustomProviderTest = new MysqlCustomProviderTest();
//    mysqlCustomProviderTest.execute();

//    CertTest certTest = new CertTest();
//    certTest.execute();

//    TrustManagerTest trustManagerTest = new TrustManagerTest();
//    trustManagerTest.execute();

    /*
    Process p1 = Runtime.getRuntime().exec("chown -R 1001:1001 /tmp/data_dir_snapshot_extraction");
    p1.waitFor();

    BufferedReader stdInput = new BufferedReader(new
        InputStreamReader(p1.getInputStream()));

    BufferedReader stdError = new BufferedReader(new
        InputStreamReader(p1.getErrorStream()));

    // Read the output from the command
    System.out.println("Here is the standard output of the command:\n");
    String s = null;
    while ((s = stdInput.readLine()) != null) {
      System.out.println(s);
    }

// Read any errors from the attempted command
    System.out.println("Here is the standard error of the command (if any):\n");
    while ((s = stdError.readLine()) != null) {
      System.out.println(s);
    }

     */

//    EsCluster esCluster = new EsCluster(1);
//    esCluster.execute();

//    EsAppNode node = new EsAppNode();
//    node.execute();

//    EsApp esApp = new EsApp();
//    esApp.execute();

//    JettyServer js = new JettyServer();
//    js.execute();

//    Thread.sleep(100000);

//    System.out.println("START");
//    JsonPathTest jpt = new JsonPathTest();
//    jpt.execute();

//    SqlServer ss = new SqlServer();
//    ss.execute();

//    SqlServerServicePrincipalBug ss = new SqlServerServicePrincipalBug();
//    ss.execute();
//    Boolean b = (Long.valueOf(1)).longValue() == 1;
//    System.out.println("b: " + b);
//
//    b = (Long.valueOf(0)).longValue() == 1;
//    System.out.println("b: " + b);

//    Parent p = new Child();
//    System.out.println(((Parent)p).getClass());


//    Thread[] threads = new Thread[100];


//    int numIters = 0;
//    while (true) {
////      System.out.print(".");
//
//      for (int i = 0; i < 100; i++) {
//        threads[i] = createNewThread();
//      }
//
//      for (int i = 0; i < 100; i++) {
//        threads[i].start();
//      }
//
//      for (int i = 0; i < 100; i++) {
//        try {
//          threads[i].join();
//        } catch (InterruptedException e) {
//          System.out.println("Could not join thread " + i);
//          System.exit(1);
//        }
//      }
    }
//  }
}
