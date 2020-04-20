package com.hmk.javaweb.utils.redis;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

@Component
public class AbstractJedisClusterFactory {

    private static final Logger logger = LoggerFactory.getLogger(AbstractJedisClusterFactory.class);

    /**
     * redis配置
     */
    @Value("${spring.redis.redisNodes}")
    private String redisNodes;
    @Value("${spring.redis.timeout}")
    private Integer timeout;
    @Value("${spring.redis.maxRedirections}")
    private Integer maxRedirections;
    @Value("${spring.redis.soTimeout}")
    private Integer soTimeout;
    @Value("${spring.redis.isAuth}")
    private Integer isAuth;
    @Value("${spring.redis.auth}")
    private String auth;

    @Value("${spring.redis.genericObjectPoolConfig.maxTotal}")
    private Integer maxTotal;
    @Value("${spring.redis.genericObjectPoolConfig.minIdle}")
    private Integer minIdle;
    @Value("${spring.redis.genericObjectPoolConfig.maxIdle}")
    private Integer maxIdle;
    @Value("${spring.redis.genericObjectPoolConfig.maxWaitMillis}")
    private Long maxWaitMillis;


    /**
     * 返回为单例，使用时直接注入
     *
     * @return JedisCluster
     * @throws Exception Exception
     */
    @Bean
    public JedisCluster getJedisCluster() throws Exception {
        if (StringUtils.isBlank(redisNodes)) {
            throw new Exception("redis初始化失败！redis配置参数为空！");
        }
        JedisCluster jedisCluster = null;

        Set<HostAndPort> haps = this.parseHostAndPort();
        logger.info("初始化所有redis===================================");

        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMaxTotal(maxTotal);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxWaitMillis(maxWaitMillis);
        if (isAuth == null || isAuth == 1) {
            // 带认证的reids集群
            jedisCluster = new JedisCluster(haps, timeout, soTimeout,
                    maxRedirections, auth, genericObjectPoolConfig);
        } else if (isAuth != null || isAuth == 0) {
            jedisCluster = new JedisCluster(haps, timeout, soTimeout,
                    maxRedirections, genericObjectPoolConfig);
        }

        return jedisCluster;
    }

    private Set<HostAndPort> parseHostAndPort() throws Exception {
        try {

            if (StringUtils.isBlank(redisNodes)) {
                throw new Exception("redis初始化异常，redisNodes不能为空！");
            }
            Set<HostAndPort> haps = new HashSet<HostAndPort>();

            String[] nodes = redisNodes.split(";");

            for (String node : nodes) {
                String[] ipAndPort = node.split(":");
                HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                haps.add(hap);
            }
            return haps;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new Exception("解析 jedis 配置文件失败", ex);
        }
    }

}
