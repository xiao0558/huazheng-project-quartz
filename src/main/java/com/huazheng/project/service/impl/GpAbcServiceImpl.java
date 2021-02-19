package com.huazheng.project.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.huazheng.project.greenplum.mapper.GpAbcMapper;
import com.huazheng.project.greenplum.model.GpAbc;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Service
public class GpAbcServiceImpl {

	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private GpAbcMapper mapper;

	@KafkaListener(topics = "sqlserver.test2.dbo.abc")
	public void mssqlData(ConsumerRecord<?, ?> record) {
		Object value = record.value();
		if (value != null) {
			String data = value.toString();
			JSONObject json = JSONUtil.parseObj(data);
			JSONObject payload = json.getJSONObject("payload");
			String op = payload.getStr("op");
			JSONObject before = payload.getJSONObject("before");
			JSONObject after = payload.getJSONObject("after");
			if (op.equals("c")) {
				GpAbc bean = JSONUtil.toBean(after, GpAbc.class);
				mapper.insert(bean);
				log.info("mssql insert --> " + bean);
			} else if (op.equals("u")) {
				GpAbc bean = JSONUtil.toBean(after, GpAbc.class);
				mapper.updateById(bean);
				log.info("mssql update --> " + bean);
			} else if (op.equals("d")) {
				GpAbc bean = JSONUtil.toBean(before, GpAbc.class);
				mapper.deleteById(bean.getId());
				log.info("mssql delete --> " + bean);
			}
		}
	}
	
}
