package misrraimsp.uned.pfg.firstmarket.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.SecurityLockProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private LoadingCache<String, Integer> loginFailuresCache;
    private SecurityLockProperties securityLockProperties;

    @Autowired
    public LoginAttemptService(SecurityLockProperties securityLockProperties) {
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

    public void loginSucceeded(String key) {
        loginFailuresCache.invalidate(key);
    }

    public void loginFailed(String key) {
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

    //debug
    public void print() {
        System.out.println(loginFailuresCache.asMap());
    }
}
