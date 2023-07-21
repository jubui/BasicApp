import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

final class DatabaseTypeUtilsThreadPool {
  private static final AppianThreadFactory appianThreadFactory = new AppianThreadFactory("DatabaseTypeUtilsThreadPool");
  private static volatile ExecutorService executorServicePool;

  private DatabaseTypeUtilsThreadPool() {
  }

  static ListeningExecutorService getExecutorServicePool() {
    if (executorServicePool == null || executorServicePool.isShutdown()) {
      synchronized (DatabaseTypeUtilsThreadPool.class) {
        if (executorServicePool == null || executorServicePool.isShutdown()) {
          executorServicePool = Executors.newScheduledThreadPool(2, appianThreadFactory);
        }
      }
    }
    return MoreExecutors.listeningDecorator(executorServicePool);
  }
}
