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
@MapperScan(basePackages = "com.huazheng.project.hana.mapper", sqlSessionTemplateRef = "hanaSqlSessionTemplate")
public class HanaConfig {

	//主数据源配置 ds1数据源
    @Primary
    @Bean(name = "hanaDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.hana")
    public DataSourceProperties ds1DataSourceProperties() {
        return new DataSourceProperties();
    }

    //主数据源 ds1数据源
    @Primary
    @Bean(name = "hanaDataSource")
    public DataSource ds1DataSource(@Qualifier("hanaDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
    
	// 主数据源 ds1数据源
	@Primary
	@Bean("hanaSqlSessionFactory")
	public SqlSessionFactory ds1SqlSessionFactory(@Qualifier("hanaDataSource") DataSource dataSource) throws Exception {
		MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/hana/*.xml"));
		sqlSessionFactory.setPlugins(new Interceptor[] { new PaginationInterceptor(),
//                new PerformanceInterceptor()
//                        .setFormat(true),
		});
		sqlSessionFactory.setGlobalConfig(new GlobalConfig().setBanner(false));
		sqlSessionFactory.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
		sqlSessionFactory.setTypeAliasesPackage("com.huazheng.project.hana.model");
		return sqlSessionFactory.getObject();
	}

	@Primary
	@Bean(name = "hanaTransactionManager")
	public DataSourceTransactionManager ds1TransactionManager(@Qualifier("hanaDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Primary
	@Bean(name = "hanaSqlSessionTemplate")
	public SqlSessionTemplate ds1SqlSessionTemplate(@Qualifier("hanaSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
