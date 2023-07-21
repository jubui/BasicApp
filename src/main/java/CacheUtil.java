import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

public class CacheUtil {

  private static final LoadingCache<String,String> databaseTypeCache = CacheBuilder.newBuilder()
      .maximumSize(1000)
      .refreshAfterWrite(5, TimeUnit.SECONDS)
      .build(new DatabaseTypeCacheLoader());

  public String getString(String key) throws Exception {
    return databaseTypeCache.get(key);
  }
}
