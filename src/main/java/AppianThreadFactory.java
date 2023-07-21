import java.util.concurrent.ThreadFactory;


/**
 * AppianThreadFactory
 *
 * ThreadFactory that names everything Appian.
 *
 * @author brian.sullivan
 */
public class AppianThreadFactory  implements ThreadFactory {


  private static int counter;

  private final String annotatedBasename;
  private final boolean daemon;

  /**
   * Daemon=true AppianThreadFactory
   *
   * @param basename, e.g., "Object Selection"
   */
  public AppianThreadFactory(String basename) {
    this(basename, true);
  }

  /**
   * @param basename, e.g., "Object Selection"
   * @param daemon at true to force all Threads to be daemons (not stop AppServer from halting)
   */
  public AppianThreadFactory(final String basename, final boolean daemon) {
    this.annotatedBasename = "Appian " + basename + " ";
    this.daemon = daemon;
  }

  @Override
  public Thread newThread(Runnable r) {
    return newThread(null, r);
  }

  public Thread newThread(String appendName, Runnable r) {
    final int count = ++counter;
    final String name;
    if(appendName == null || appendName.length() == 0) {
      name = annotatedBasename + count;
    } else {
      name = annotatedBasename + appendName + " " + count;
    }
    final Runnable logRunnable = new LogRunnable(r, name);
    final Thread t = new Thread(logRunnable, name);
    t.setDaemon(daemon);
    return t;
  }

  private static class LogRunnable implements Runnable {

    private final Runnable runnable;
    private final String name;

    public LogRunnable(final Runnable runnable, final String name) {
      if(runnable == null) {
        throw new NullPointerException("AppianThreadFactory requested null Runnable");
      }
      this.name = name;
      this.runnable = runnable;
    }

    @Override
    public void run() {
      String runnableClassName = "";
      // This allows passing thread local scope from the starting thread to this one
      try {
        runnable.run();
      } catch(ThreadDeath threadDeath) {
        // ignore entirely, as this is the expected exception
        throw threadDeath;
      } catch(RuntimeException exception) {
        // note, run() has no declared Exceptions
        throw exception;
      } catch(Error error) {
        // always make this visible
        throw error;
      } catch(Throwable t) {
        // always make this visible
        throw t;
      }
    }
  }
}
