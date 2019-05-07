package microservice.integration.gateway.service;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class RedisService<T> {

    @Resource
    private RedisTemplate<String, T> redisTemplate;

    /**
     * 字符串类型:通过key值获取对应的value对象
     *
     * @param key
     * @return
     */
    public T get(final String key,Class<T> type) {
        return redisTemplate.execute((RedisCallback<T>) redisConnection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            byte keys[] = serializer.serialize(key);
            byte values[] = redisConnection.get(keys);
            if (values == null) {
                return null;
            }
            String value = serializer.deserialize(values);
            //获取泛型类型
            return JSON.parseObject(value, type);
        });
    }

    public String getString(final String key) {
        return redisTemplate.execute((RedisCallback<String>) redisConnection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            byte keys[] = serializer.serialize(key);
            byte values[] = redisConnection.get(keys);
            if (values == null) {
                return null;
            }
            String value = serializer.deserialize(values);
            return value;
        });
    }

    /**
     * 字符串类型:存入key-value对象，如果key存在，那么默认更新value
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean set(final String key, final T value) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            byte keys[] = serializer.serialize(key);
            byte values[] = serializer.serialize(JSON.toJSONString(value));
            redisConnection.set(keys, values);
            return true;
        });
    }

    public Boolean setEx(final String key, final T value,final  long seconds) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            byte keys[] = serializer.serialize(key);
            byte values[] = serializer.serialize(JSON.toJSONString(value));
            redisConnection.setEx(keys,seconds, values);
            return true;
        });
    }

    /**
     * 字符串类型:通过key删除对应的key和value
     *
     * @param key
     * @return
     */
    public Long delete(final String key) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            RedisSerializer<String> redisSerializer = getRedisSerializer();
            byte keys[] = redisSerializer.serialize(key);
            return redisConnection.del(keys);
        });
    }

    /**
     * 字符串类型:通过key判断对象是否存在
     *
     * @param key
     * @return
     */
    public Boolean exists(final String key) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.exists(keys);
        });
    }

    /**
     * 字符串类型:设置key对应的超时时间
     *
     * @param key
     * @param expireTime
     * @return
     */
    public Boolean expire(final String key, final Long expireTime) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.expire(keys, expireTime);
        });
    }


    public Boolean expireAt(final String key, final Date expireTime) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.expireAt(keys, expireTime.getTime());
        });
    }

    /**
     * 字符串类型:根据key设置value值,如果key中的value存在,那么返回false
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean setnx(final String key, final String value) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            byte values[] = getRedisSerializer().serialize(JSON.toJSONString(value));
            return redisConnection.setNX(keys, values);
        });
    }

    /**
     * 字符串类型:设置key和value的超时时间(设置成String返回类型,不然要设置成Void)
     *
     * @param key
     * @param timeout
     * @param value
     * @return
     */
    public Void setex(final String key, final Long timeout, final T value) {
        return redisTemplate.execute((RedisCallback<Void>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            byte values[] = getRedisSerializer().serialize(JSON.toJSONString(value));
            redisConnection.setEx(keys, timeout, values);
            return null;
        });
    }

    /**
     * 字符串类型:
     * 覆盖key对应的string的一部分，从指定的offset开始，覆盖value的长度。
     * 如果offset比当前key对应string还长，那么这个string后面就补0以达到offset。
     * 不存在的keys被认为是空字符串，所以这个命令可以确保key有一个足够大的字符串，能在offset处设置value。
     *
     * @param key
     * @param value
     * @param offset
     * @return
     */
    public Void setrange(final String key, final T value, final Long offset) {
        return redisTemplate.execute((RedisCallback<Void>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            byte values[] = getRedisSerializer().serialize(JSON.toJSONString(value));
            redisConnection.setRange(keys, values, offset);
            return null;
        });
    }

    /**
     * 字符串类型:
     * 返回key对应的字符串value的子串，自个子串是由start和end位移决定的（两者都在string内）。
     * 可以用负的位移来表示从string尾部开始数的下标。所以-1就是最后一个字符，-2就是倒数第二个，以此类推。
     * 这个函数超出范围的请求时，都把结果限制在string内。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public byte[] getrange(final String key, final Long start, final Long end) {
        return redisTemplate.execute((RedisCallback<byte[]>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.getRange(keys, start, end);
        });
    }

    /**
     * 字符串类型:
     * 对存储在指定key的数值执行原子的加1操作。
     * 如果指定的key不存在，那么在执行incr操作之前，会先把它的值设定为0.
     * 如果指定的key中存储的值不是字符串类型或者存储的字符串类型不能表示为一个整数，那么执行这个命令时服务器会返回一个错误。
     * 注意：由于redis并没有一个明确的类型来表示整型数据，所以这个操作是一个字符串操作。
     * 执行这个操作的时候，key对应存储的字符串被解析为10进制的64位有符号整型数据。
     * 这个操作仅限于64位的有符号整型数据。
     * 事实上，redis内部采用整数形式来存储对应的整数值，所以对该类字符串值实际上是用整数保存，也就不存在存储整数的字符串表示所带来的额外消耗。
     * incr的原子操作是说即使多个客户端对同一个key发出incr命令，也决不会导致竞争的情况，
     * 例如如下情况永远不可能发生：客户端1和客户端2同时读出10，他们俩都对其加到11，然后将新值设置为11，最终值一定为12
     *
     * @param key
     * @return
     */
    public Long incr(final String key) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.incr(keys);
        });
    }

    /**
     * 字符串类型:
     * 对key对应的数字做减一操作。如果key不存在，那么在操作之前，这个key对应的值会被设定为0。
     * 如果key有一个错误类型的value或者是一个不能表示成数字的字符串，就返回错误。这个操作最大支持在64位有符号的整型数字。
     *
     * @param key
     * @return
     */
    public Long decr(final String key) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.decr(keys);
        });
    }

    /**
     * 字符串类型:
     * 将key进行递增。如果key不存在，操作之前，key就会被置为0.如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。
     * 这个操作最多支持64位有符号的整形数字。
     *
     * @param key
     * @param offset
     * @return
     */
    public Long incrby(final String key, final Long offset) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.incrBy(keys, offset);
        });
    }

    /**
     * 字符串类型:
     * 将key对应的数字递减。如果key不存在，操作之前，key就会被置为0.如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。
     * 这个操作最多支持64位有符号的整形数字。
     *
     * @param key
     * @param offset
     * @return
     */
    public Long decrby(final String key, final Long offset) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.decrBy(keys, offset);
        });
    }

    /**
     * 字符串类型:设置多个key和value
     * 对应给定的keys到他们对应的values上。Mset会用新的value替代已经存在的value，就像普通的set命令一样。
     * 如果你不想覆盖已经存在的values，那么需要参考msetnx.
     * mset是原子性的，所以所有给定的keys是一次性set的。
     * 客户端不可能看到这种一部分keys被更新而另外的没有改变的情况。
     *
     * @param map
     * @return
     */
    public Void mset(final Map<byte[], byte[]> map) {
        return redisTemplate.execute((RedisCallback<Void>) redisConnection -> {
            redisConnection.mSet(map);
            return null;
        });
    }

    /**
     * 字符串类型:一次性获取多个key对应的value列表
     *
     * @param bytes
     * @return
     */
    public List<byte[]> mget(final byte[]... bytes) {
        return redisTemplate.execute((RedisCallback<List<byte[]>>) redisConnection -> redisConnection.mGet(bytes));
    }

    /**
     * 字符串类型:
     * 对应给定的keys到他们相应的values上。只要有一个values已经存在，MSETNX一个操作都不会执行。
     * 由于这种特性，MSETNX可以实现要么所有的操作都成功，
     * 要么一个都不执行，这个可以用来设置不同的key，来表示一个唯一的对象的不同字段。
     * MSETNX是原子的，所以所有给定的keys是一次性set的。客户端不可能看到这种一部分keys被更新而另外的没有改变的情况。
     *
     * @param map
     * @return
     */
    public Boolean msetnx(final Map<byte[], byte[]> map) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.mSetNX(map));
    }

    /**
     * list列表类型:先进后出栈形式,单个值插入
     *
     * @param key
     * @param value
     * @return
     */
    public Long lpush(final String key, final T value) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            byte values[] = getRedisSerializer().serialize(JSON.toJSONString(value));
            return redisConnection.lPush(keys, values);
        });
    }

    /**
     * list列表类型:先进先出队列形式,单个值插入
     *
     * @param key
     * @param value
     * @return
     */

    public Long rpush(final String key, final T value) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            byte values[] = getRedisSerializer().serialize(JSON.toJSONString(value));
            return redisConnection.rPush(keys, values);
        });
    }

    /**
     * list列表类型:
     * 返回存储在key的列表里指定范围内的元素。Start和end偏移量都是基于0的下标，
     * 即list的第一个元素下标是0(list的开头)，第二个元素是下标1，以此类推。
     * 偏移量也可以是负数，表示偏移量是从list尾部开始计数。例如，-1表示列表的最后一个元素，-2是倒数第二个，以此类推。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */

    public List<byte[]> lrange(final String key, final Long start, final Long end) {
        return redisTemplate.execute((RedisCallback<List<byte[]>>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.lRange(keys, start, end);
        });
    }

    /**
     * list列表类型:返回名称为key的list中index位置的元素
     *
     * @param key
     * @param offset
     * @return
     */

    public byte[] lindex(final String key, final Long offset) {
        return redisTemplate.execute((RedisCallback<byte[]>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.lIndex(keys, offset);
        });
    }

    /**
     * list列表类型:返回list中的元素个数
     *
     * @param key
     * @return
     */

    public Long llen(final String key) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.lLen(keys);
        });
    }

    /**
     * set无序集合类型:
     * 添加一个或多个指定的member元素到集合的key中
     *
     * @param key
     * @param bytes
     * @return
     */

    public Long sadd(final String key, final byte[]... bytes) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.sAdd(keys, bytes);
        });
    }

    /**
     * set无序集合类型:
     * 返回key集合所有的元素
     *
     * @param key
     * @return
     */

    public Set<byte[]> smembers(final String key) {
        return redisTemplate.execute((RedisCallback<Set<byte[]>>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.sMembers(keys);
        });
    }

    /**
     * set无序集合类型:删除key中的指定元素
     *
     * @param key
     * @param bytes
     * @return
     */

    public Long srem(final String key, final byte[]... bytes) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.sRem(keys, bytes);
        });
    }

    /**
     * set无序集合类型:
     * 返回指定的所有集合的成员的交集。如果key不存在则被认为是一个空的集合，当给定的集合为空的时候，结果也为空(一个集合为空，结果一直为空)
     *
     * @param bytes
     * @return
     */

    public Set<byte[]> sinter(final byte[]... bytes) {
        return redisTemplate.execute((RedisCallback<Set<byte[]>>) redisConnection -> redisConnection.sInter(bytes));
    }

    /**
     * set无序集合类型:
     * 把两个集合的交集结果集结果保存到一个新的集合中。如果这个新的集合是已经存在的，那么这个新的集合则会被重写。
     * 返回值为结果集中成员的个数。
     *
     * @param key
     * @param bytes
     * @return
     */

    public Long sinterstore(final String key, final byte[]... bytes) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.sInterStore(keys, bytes);
        });
    }

    /**
     * set无序集合类型:
     * 返回给定的多个集合的并集中的所有成员，不存在的key可以认为是空的集合。
     * 返回值为并集的成员列表。
     *
     * @param bytes
     * @return
     */

    public Set<byte[]> sunion(final byte[]... bytes) {
        return redisTemplate.execute((RedisCallback<Set<byte[]>>) redisConnection -> redisConnection.sUnion(bytes));
    }

    /**
     * set无序集合类型:
     * 将集合结果并集存储在新的集合中，如果新的集合已经存在，那么会覆盖这个新的集合。
     * 返回值为结果集中元素的个数。
     *
     * @param key
     * @param bytes
     * @return
     */

    public Long sunionstore(final String key, final byte[]... bytes) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte keys[] = getRedisSerializer().serialize(key);
            return redisConnection.sUnionStore(keys, bytes);
        });
    }

    /**
     * set无序集合类型:
     *
     * @param key1  被移除的集合
     * @param key2  接收移除元素的集合
     * @param field 要移除的元素
     * @return
     */

    public Boolean smove(final String key1, final String key2, final String field) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.sMove(
                getRedisSerializer().serialize(key1),
                getRedisSerializer().serialize(key2),
                getRedisSerializer().serialize(field)
        ));
    }

    /**
     * set无序集合类型:返回集合中的元素个数
     *
     * @param key
     * @return
     */

    public Long scard(final String key) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte[] keys = getRedisSerializer().serialize(key);
            return redisConnection.sCard(keys);
        });
    }

    /**
     * set无序集合类型:判断bytes2是否是bytes1中的元素
     *
     * @param key
     * @param value
     * @return
     */

    public Boolean sismember(final String key, final T value) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.sIsMember(
                getRedisSerializer().serialize(key),
                getRedisSerializer().serialize(JSON.toJSONString(value))
        ));
    }

    /**
     * zset有序集合类型:
     *
     * @param key
     * @param offset 分数值是一个双精度的浮点型数字字符串
     * @param bytes2 value
     * @return
     */

    public Boolean zadd(final String key, final Double offset, final byte[] bytes2) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.zAdd(getRedisSerializer().serialize(key), offset, bytes2));
    }

    /**
     * zset有序集合类型:根据集合中指定的index返回成员列表
     *
     * @param key
     * @param start
     * @param end
     * @return
     */

    public Set<byte[]> zrange(final String key, final Long start, final Long end) {
        return redisTemplate.execute((RedisCallback<Set<byte[]>>) redisConnection -> redisConnection.zRange(getRedisSerializer().serialize(key), start, end));
    }

    /**
     * zset有序集合类型:
     * 从排序的集合中删除一个或多个成员
     * 返回值为从有序集合中删除的成员个数，不包括不存在的成员
     *
     * @param key
     * @param bytes
     * @return
     */

    public Long zrem(final String key, final byte[]... bytes) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte[] keys = getRedisSerializer().serialize(key);
            return redisConnection.zRem(keys, bytes);
        });
    }

    /**
     * zset有序集合类型:为有序集key的成员member的offset值加上增量increment
     *
     * @param key    key
     * @param offset 增量increment
     * @param field  集合成员
     * @return member成员的新offset值
     */

    public Double zincrby(final String key, final Double offset, final String field) {
        return redisTemplate.execute((RedisCallback<Double>) redisConnection -> redisConnection.zIncrBy(
                getRedisSerializer().serialize(key),
                offset,
                getRedisSerializer().serialize(field)));
    }

    /**
     * zset有序集合类型:找到指定区间范围的数据进行返回
     *
     * @param key
     * @param start
     * @param end
     * @return
     */

    public Set<byte[]> zrangebyscore(final String key, final Double start, final Double end) {
        return redisTemplate.execute((RedisCallback<Set<byte[]>>) redisConnection -> redisConnection.zRangeByScore(getRedisSerializer().serialize(key), start, end));
    }

    /**
     * zset有序集合类型:移除有序集key中，指定排名(rank)区间内的所有成员。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */

    public Long zremrangebyrank(final String key, final Double start, final Double end) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte[] keys = getRedisSerializer().serialize(key);
            return redisConnection.zRemRangeByScore(keys, start, end);
        });
    }

    /**
     * zset有序集合类型:返回有序集key中，score值在min和max之间(默认包括score值等于min或max)的成员。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */

    public Long zcount(final String key, final Double start, final Double end) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte[] keys = getRedisSerializer().serialize(key);
            return redisConnection.zCount(keys, start, end);
        });
    }

    /**
     * hash类型:
     * 设置 key 指定的哈希集中指定字段的值
     * 如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key 关联
     * 如果字段在哈希集中存在，它将被重写
     *
     * @param key
     * @param field
     * @param value
     * @return
     */

    public Boolean hset(final String key, final String field, final T value) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.hSet(
                getRedisSerializer().serialize(key),
                getRedisSerializer().serialize(field),
                getRedisSerializer().serialize(JSON.toJSONString(value))
        ));
    }

    /**
     * hash类型:返回 key 指定的哈希集中该字段所关联的值
     *
     * @param key
     * @param field
     * @return
     */

    public byte[] hget(final String key, final String field) {
        return redisTemplate.execute((RedisCallback<byte[]>) redisConnection -> redisConnection.hGet(getRedisSerializer().serialize(key),
                getRedisSerializer().serialize(field)));
    }

    /**
     * hash类型:返回 key 指定的哈希集中所有字段的名字
     *
     * @param key
     * @return
     */

    public Set<byte[]> hkeys(final String key) {
        return redisTemplate.execute((RedisCallback<Set<byte[]>>) redisConnection -> redisConnection.hKeys(getRedisSerializer().serialize(key)));
    }

    /**
     * hash类型:从 key 指定的哈希集中移除指定的域。
     *
     * @param key
     * @param fields
     * @return
     */

    public Long hdel(final String key, final byte[]... fields) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.hDel(getRedisSerializer().serialize(key), fields));
    }

    /**
     * hash类型:返回 key 指定的哈希集包含的字段的数量
     *
     * @param key
     * @return
     */

    public Long hlen(final String key) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.hLen(getRedisSerializer().serialize(key)));
    }

    /**
     * hash类型:可同时对key设置多个值，hset只能一次设置一个
     *
     * @param key
     * @param map
     * @return
     */

    public Void hmset(final String key, final Map<byte[], byte[]> map) {
        return redisTemplate.execute((RedisCallback<Void>) redisConnection -> {
            redisConnection.hMSet(getRedisSerializer().serialize(key), map);
            return null;
        });
    }

    /**
     * hash类型:返回 key 指定的哈希集中指定多个字段的值。
     *
     * @param key
     * @param fields
     * @return
     */

    public List<byte[]> hmget(final String key, final byte[]... fields) {
        return redisTemplate.execute((RedisCallback<List<byte[]>>) redisConnection -> redisConnection.hMGet(getRedisSerializer().serialize(key), fields));
    }

    /**
     * hash类型:
     * 增加 key 指定的哈希集中指定字段的数值。
     * 如果 key 不存在，会创建一个新的哈希集并与 key 关联。如果字段不存在，则字段的值在该操作执行前被设置为 0
     * HINCRBY 支持的值的范围限定在 64位 有符号整数
     *
     * @param key
     * @param field
     * @param val
     * @return
     */

    public Double hincrby(final String key, final String field, final Double val) {
        return redisTemplate.execute((RedisCallback<Double>) redisConnection -> redisConnection.hIncrBy(
                getRedisSerializer().serialize(key),
                getRedisSerializer().serialize(field),
                val
        ));
    }

    /**
     * hash类型:返回hash里面key是否存在的标志
     *
     * @param key
     * @param field
     * @return
     */

    public Boolean hexists(final String key, final String field) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.hExists(
                getRedisSerializer().serialize(key),
                getRedisSerializer().serialize(field)
        ));
    }

    /**
     * hash类型:返回 key 指定的哈希集中所有字段的值。
     *
     * @param key
     * @return
     */

    public List<byte[]> hvals(final String key) {
        return redisTemplate.execute((RedisCallback<List<byte[]>>) redisConnection -> redisConnection.hVals(getRedisSerializer().serialize(key)));
    }

    /**
     * hash类型:
     * 只在 key 指定的哈希集中不存在指定的字段时，设置字段的值。
     * 如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key 关联。
     * 如果字段已存在，该操作无效果。
     *
     * @param key
     * @param field
     * @param value
     * @return
     */

    public Boolean hsetnx(final String key, final String field, final String value) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.hSetNX(getRedisSerializer().serialize(key),
                getRedisSerializer().serialize(field),
                getRedisSerializer().serialize(value)));
    }

    /**
     * hash类型:
     * 返回 key 指定的哈希集中所有的字段和值。返回值中，每个字段名的下一个是它的值，所以返回值的长度是哈希集大小的两倍
     *
     * @param key
     * @return
     */

    public Map<byte[], byte[]> hgetall(final String key) {
        return redisTemplate.execute((RedisCallback<Map<byte[], byte[]>>) redisConnection -> redisConnection.hGetAll(getRedisSerializer().serialize(key)));
    }

    /**
     * 获取主库的字符串序列化对象
     *
     * @return
     */
    protected RedisSerializer<String> getRedisSerializer() {
        RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
        return redisSerializer;
    }

}
