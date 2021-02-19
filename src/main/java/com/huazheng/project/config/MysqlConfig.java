package com.huazheng.project.config;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

@Configuration
@MapperScan(basePackages = "com.huazheng.project.mysql.mapper", sqlSessionFactoryRef = "mysqlSqlSessionFactory")
public class MysqlConfig {

	//主数据源配置 ds2数据源
    @Primary
    @Bean(name = "mysqlDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSourceProperties ds2DataSourceProperties() {
        return new DataSourceProperties();
    }

    //主数据源 ds2数据源
    @Primary
    @Bean(name = "mysqlDataSource")
    public DataSource ds2DataSource(@Qualifier("mysqlDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
    
	// 主数据源 ds2数据源
	@Primary
	@Bean("mysqlSqlSessionFactory")
	public SqlSessionFactory ds2SqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource) throws Exception {
		MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/mysql/*.xml"));
		sqlSessionFactory.setPlugins(new Interceptor[] { new PaginationInterceptor(),
//                new PerformanceInterceptor()
//                        .setFormat(true),
		});
		sqlSessionFactory.setGlobalConfig(new GlobalConfig().setBanner(false));
		sqlSessionFactory.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
		sqlSessionFactory.setTypeAliasesPackage("com.huazheng.project.mysql.model");
		return sqlSessionFactory.getObject();
	}

	@Primary
	@Bean(name = "mysqlTransactionManager")
	public DataSourceTransactionManager ds2TransactionManager(@Qualifier("mysqlDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Primary
	@Bean(name = "mysqlSqlSessionTemplate")
	public SqlSessionTemplate ds2SqlSessionTemplate(@Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
