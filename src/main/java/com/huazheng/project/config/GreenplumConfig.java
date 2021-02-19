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
@MapperScan(basePackages = "com.huazheng.project.greenplum.mapper", sqlSessionFactoryRef = "greenplumSqlSessionFactory")
public class GreenplumConfig {

	//主数据源配置 ds2数据源
    @Primary
    @Bean(name = "greenplumDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.greenplum")
    public DataSourceProperties ds2DataSourceProperties() {
        return new DataSourceProperties();
    }

    //主数据源 ds2数据源
    @Primary
    @Bean(name = "greenplumDataSource")
    public DataSource ds2DataSource(@Qualifier("greenplumDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
    
	// 主数据源 ds2数据源
	@Primary
	@Bean("greenplumSqlSessionFactory")
	public SqlSessionFactory ds2SqlSessionFactory(@Qualifier("greenplumDataSource") DataSource dataSource) throws Exception {
		MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/greenplum/*.xml"));
		sqlSessionFactory.setPlugins(new Interceptor[] { new PaginationInterceptor(),
//                new PerformanceInterceptor()
//                        .setFormat(true),
		});
		sqlSessionFactory.setGlobalConfig(new GlobalConfig().setBanner(false));
		sqlSessionFactory.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
		sqlSessionFactory.setTypeAliasesPackage("com.huazheng.project.greenplum.model");
		return sqlSessionFactory.getObject();
	}

	@Primary
	@Bean(name = "greenplumTransactionManager")
	public DataSourceTransactionManager ds2TransactionManager(@Qualifier("greenplumDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Primary
	@Bean(name = "greenplumSqlSessionTemplate")
	public SqlSessionTemplate ds2SqlSessionTemplate(@Qualifier("greenplumSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
