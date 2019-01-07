package com.hnair.wallet.admincenter.configuration;

import cn.aegisa.selext.dao.service.impl.CommonServiceImpl;
import cn.aegisa.selext.dao.spi.impl.CommonDaoImpl;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/21/2018 10:54 AM
 */
@Configuration
@Slf4j
public class DatabaseConfiguration {

    @Value("${spring.datasource.slave.url}")
    private String dbUrl_slave;

    @Value("${spring.datasource.slave.username}")
    private String username_slave;

    @Value("${spring.datasource.slave.password}")
    private String password_slave;

    @Value("${spring.datasource.master.url}")
    private String dbUrl_master;

    @Value("${spring.datasource.master.username}")
    private String username_master;

    @Value("${spring.datasource.master.password}")
    private String password_master;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.profiles.active}")
    private String currentEnv;

    private boolean hasInitServerDb = false;

    {
        String SPRING_BOOT = " :: Spring Boot :: ";
        String version = SpringBootVersion.getVersion();
        String s = SPRING_BOOT + version + "\n    __  ___ ____                    __  ___ ___              \n" +
                "   / / / (_) __ \\____ ___  __      / / / (_)   |  ____  ____ \n" +
                "  / /_/ / / /_/ / __ `/ / / /_____/ /_/ / / /| | / __ \\/ __ \\\n" +
                " / __  / / ____/ /_/ / /_/ /_____/ __  / / ___ |/ /_/ / /_/ /\n" +
                "/_/ /_/_/_/    \\__,_/\\__, /     /_/ /_/_/_/  |_/ .___/ .___/ \n" +
                "                    /____/                    /_/   /_/      ";
        log.info(s);
    }

    private DruidDataSource parentDatasource() {
        final DruidDataSource druid = new DruidDataSource();
        druid.setDriverClassName(driverClassName);
        druid.setInitialSize(initialSize);
        druid.setMinIdle(minIdle);
        druid.setMaxActive(maxActive);
        druid.setMaxWait(maxWait);
        druid.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druid.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druid.setValidationQuery(validationQuery);
        druid.setTestWhileIdle(testWhileIdle);
        druid.setTestOnBorrow(testOnBorrow);
        druid.setTestOnReturn(testOnReturn);
        return druid;
    }

    private void initDatabaseInfo() {
        log.info("初始化{}环境数据库|{}", currentEnv, currentEnv.equals("local") ? "从classPath加载数据库配置" : "从服务器加载数据库配置");
        InputStreamReader inputStreamReader = null;
        if (currentEnv.equals("local")) {
            return;
        }
        try {
            Properties properties = new Properties();
            inputStreamReader = new InputStreamReader(new FileInputStream(new File("/etc/hnaconf/consumer/config.properties")), StandardCharsets.UTF_8);
            properties.load(inputStreamReader);
            this.username_master = properties.getProperty("hipay.master.jdbc.username");
            this.password_master = properties.getProperty("hipay.master.jdbc.password");
            this.dbUrl_master = properties.getProperty("hipay.master.jdbc.url");
            this.username_slave = properties.getProperty("hipay.slave.jdbc.username");
            this.password_slave = properties.getProperty("hipay.slave.jdbc.password");
            this.dbUrl_slave = properties.getProperty("hipay.slave.jdbc.url");
            inputStreamReader.close();
        } catch (Exception e) {
            log.error("读取服务器配置文件错误", e);
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (Exception e) {
                    log.error("关闭转换流错误", e);
                }
            }
        }
    }

    @Bean
    public DataSource masterDbDatasource() {
        if (!hasInitServerDb) {
            initDatabaseInfo();
            hasInitServerDb = true;
        }
        return getDataSource(dbUrl_master, username_master, password_master);
    }

    @Bean
    public DataSource slaveDbDatasource() {
        if (!hasInitServerDb) {
            initDatabaseInfo();
            hasInitServerDb = true;
        }
        return getDataSource(dbUrl_slave, username_slave, password_slave);
    }

    private DataSource getDataSource(String dbUrl_slave, String username_slave, String password_slave) {
        final DruidDataSource dataSource = parentDatasource();
        dataSource.setUrl(dbUrl_slave);
        dataSource.setUsername(username_slave);
        dataSource.setPassword(password_slave);
        return dataSource;
    }

    /**
     * 配置从库
     *
     * @return 从库SqlSession
     */
    @Bean
    public SqlSessionFactoryBean slaveSqlSessionFactoryBean() throws IOException {
        final SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(slaveDbDatasource());
        PathMatchingResourcePatternResolver multiResolver_self = new PathMatchingResourcePatternResolver();
        Resource[] mapperLocationResource = multiResolver_self.getResources("mybatis/mapper/*.xml");
        PathMatchingResourcePatternResolver multiResolver_dependency = new PathMatchingResourcePatternResolver();
        List<Resource> list = Arrays.stream(mapperLocationResource).collect(Collectors.toList());
        int size = list.size();
        Resource[] arr = new Resource[size];
        arr = list.toArray(arr);
        factoryBean.setMapperLocations(arr);
        return factoryBean;
    }

    /**
     * 主库模板
     *
     * @return
     */
    @Bean
    public JdbcTemplate contentJdbcTemplate() {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(masterDbDatasource());
        return jdbcTemplate;
    }

    /**
     * 从库模板
     *
     * @return
     */
    @Bean
    public JdbcTemplate contentJdbcQueryTemplate() {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(slaveDbDatasource());
        return jdbcTemplate;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(masterDbDatasource());
        return transactionManager;
    }


    /**
     * 配置主库
     *
     * @return 主库qlSession
     */
    @Bean
    public SqlSessionFactoryBean masterSqlSessionFactoryBean() throws IOException {
        final SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(masterDbDatasource());
        final ClassPathResource configLocationResource = new ClassPathResource("mybatis/sql-map-config.xml");
        factoryBean.setConfigLocation(configLocationResource);
        PathMatchingResourcePatternResolver multiResolver_self = new PathMatchingResourcePatternResolver();
        Resource[] mapperLocationResource = multiResolver_self.getResources("mybatis/mapper/*.xml");
        PathMatchingResourcePatternResolver multiResolver_dependency = new PathMatchingResourcePatternResolver();
        List<Resource> list = Arrays.stream(mapperLocationResource).collect(Collectors.toList());
        int size = list.size();
        Resource[] arr = new Resource[size];
        arr = list.toArray(arr);
        factoryBean.setMapperLocations(arr);
        return factoryBean;
    }

    @Bean
    public SqlSessionTemplate masterSqlSession() throws Exception {
        return new SqlSessionTemplate(Objects.requireNonNull(masterSqlSessionFactoryBean().getObject()));
    }

    @Bean
    public SqlSessionTemplate slaveSqlSession() throws Exception {
        return new SqlSessionTemplate(Objects.requireNonNull(slaveSqlSessionFactoryBean().getObject()));
    }

    @Bean
    public CommonDaoImpl modelCommonDao() throws Exception {
        final CommonDaoImpl commonDao = new CommonDaoImpl();
        commonDao.setSqlSession(masterSqlSession());
        commonDao.setSqlSessionQurey(slaveSqlSession());
        return commonDao;
    }

    @Bean
    public CommonServiceImpl modelCommonService() throws Exception {
        final CommonServiceImpl service = new CommonServiceImpl();
        service.setCommonDao(modelCommonDao());
        return service;
    }


}
