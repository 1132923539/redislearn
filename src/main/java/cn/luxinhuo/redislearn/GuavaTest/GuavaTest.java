package cn.luxinhuo.redislearn.GuavaTest;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaTest {

    private Cache<Object, Object> cache = CacheBuilder.newBuilder().recordStats().build();

    private Cache<String, String> listenerCache = CacheBuilder.newBuilder().removalListener((RemovalListener<String, String>)
            removalNotification -> System.out.println(removalNotification.getKey()+":"+removalNotification.getValue()+"---已被移除")).build();

    @Test
    public void testGuava() {
        cache.put("a", "aValue");
        listenerCache.put("a", "aValue");
        listenerCache.invalidate("a");
        System.out.println(getGuava());
    }

    private String getGuava() {
        CacheBuilder.newBuilder().maximumSize(2).expireAfterWrite(3, TimeUnit.SECONDS).build();

        return Objects.requireNonNull(cache.getIfPresent("a")).toString();
    }

    @Test
    public void testAutoLoading() throws ExecutionException {
//        cache.put("a","hha");
        Object o = cache.get("a", () -> {
            System.out.println("test");
            return "test";
        });
        Object a = cache.getIfPresent("a");
        System.out.println(a);
        System.out.println(cache.stats());
    }
}
