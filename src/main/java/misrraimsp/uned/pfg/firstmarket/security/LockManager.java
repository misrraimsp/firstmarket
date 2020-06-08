package misrraimsp.uned.pfg.firstmarket.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.SecurityLockProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LockManager {

    private final LoadingCache<String, Integer> loginFailuresCache;
    private final SecurityLockProperties securityLockProperties;

    @Autowired
    public LockManager(SecurityLockProperties securityLockProperties) {
        super();
        this.securityLockProperties = securityLockProperties;
        // specify the load-new-key operation
        CacheLoader<String, Integer> loader = new CacheLoader<>() {
            public Integer load(String key) {
                return 0;
            }
        };
        // build cache data structure
        loginFailuresCache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(securityLockProperties.getLockingMinutes(), TimeUnit.MINUTES)
                .build(loader)
        ;
    }

    public void loginSuccess(String key) {
        loginFailuresCache.invalidate(key);
    }

    public void loginFail(String key) {
        int attempts;
        try {
            attempts = loginFailuresCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        loginFailuresCache.put(key, attempts);
    }

    public boolean isLocked(String key) {
        try {
            return loginFailuresCache.get(key) >= securityLockProperties.getNumOfAttempts();
        } catch (ExecutionException e) {
            return false;
        }
    }

    public Set<String> getLocked() {
        Set<String> mailsLocked = new HashSet<>();
        loginFailuresCache.asMap().forEach((key, value) -> {
            if (value >= securityLockProperties.getNumOfAttempts()) mailsLocked.add(key);
        });
        return mailsLocked;
    }

    //debug
    public void print() {
        System.out.println(loginFailuresCache.asMap());
    }
}
