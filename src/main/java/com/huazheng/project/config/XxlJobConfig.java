package com.huazheng.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
public class XxlJobConfig {

	@Value("${xxl.job.admin.addresses}")
	private String adminAddresses;

	@Value("${xxl.job.executor.appname}")
	private String appName;

	@Value("${xxl.job.executor.port}")
	private int port;
	
	@Bean
	public XxlJobSpringExecutor xxlJobExecutor() {
		log.info(">>>>>>>>>>> xxl-job config init.");
		XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
		xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
		xxlJobSpringExecutor.setAppname(appName);
		xxlJobSpringExecutor.setPort(port);
		return xxlJobSpringExecutor;
	}

}
