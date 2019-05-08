package microservice.integration.common.lock;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: haochencheng
 * @create: 2018-12-06 11:32
 **/
@ConditionalOnBean(RedisOperations.class)
public class RedisDistributedLock {

    @Autowired
    private Environment env;

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    private ThreadLocal<String> tokenLocal = new ThreadLocal<>();

    private static final String DELIMITER = "&";

    private static final int WAIT_MAX = 1000;

    public boolean tryLock(String key) throws InterruptedException {
        return acquire(key,1);
    }

    private boolean acquire(String key,int expireSeconds) throws InterruptedException {
        key = getKey(key);
        String token = UUID.randomUUID().toString();
        long expire = LocalDateTime.now().plusSeconds(expireSeconds).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String value=token+DELIMITER+expire;
        int waitAlready=0;
        while (stringRedisTemplate.opsForValue().setIfAbsent(key, value) != true && waitAlready < WAIT_MAX) {
            int i = new Random().nextInt(200);
            Thread.sleep(i);
            waitAlready += i;
        }
        if (waitAlready < WAIT_MAX) {
            stringRedisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
        }else {
            stringRedisTemplate.opsForValue().getAndSet(key,value);
            stringRedisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
        }
        tokenLocal.set(token);
        return true;
    }

    private String getKey(String key) {
        return env.getProperty("spring.application.name", String.class, this.getClass().getPackage().getName())+(key);
    }

    public void releaseLock(String key){
        try {
            key = getKey(key);
            String oldVal = stringRedisTemplate.opsForValue().get(key);
            if (!Strings.isNullOrEmpty(oldVal)){
                final String[] oldValues = oldVal.split(DELIMITER);
                if (tokenLocal.get().equals(oldValues[0])&&Long.parseLong(oldValues[1])+ new Random().nextInt(500) >= System.currentTimeMillis()) {
                    stringRedisTemplate.delete(key);
                }
            }
        }finally {
            // 清除掉ThreadLocal中的数据，避免内存溢出
            tokenLocal.remove();
        }
    }

}
