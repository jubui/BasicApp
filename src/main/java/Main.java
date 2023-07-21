
public class Main {
  private static CacheUtil cacheUtil = new CacheUtil();


  public static void main(String[] args) {
    Thread[] threads = new Thread[100];


    int numIters = 0;
    while (true) {
//      System.out.print(".");

      for (int i = 0; i < 100; i++) {
        threads[i] = createNewThread();
      }

      for (int i = 0; i < 100; i++) {
        threads[i].start();
      }

      for (int i = 0; i < 100; i++) {
        try {
          threads[i].join();
        } catch (InterruptedException e) {
          System.out.println("Could not join thread " + i);
          System.exit(1);
        }
      }
    }
  }

  public static Thread createNewThread() {
    return new Thread(() -> {
      try {
        String val = getFromConnection();
      } catch (Exception e) {
        System.out.println("Got exception: " + e.getMessage());
      }
    });
  }

  private static String getFromConnection() throws Exception {
    String val = cacheUtil.getString("a");
    return val;
  }
}
