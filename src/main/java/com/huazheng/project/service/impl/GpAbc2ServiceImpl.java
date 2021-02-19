package com.huazheng.project.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.huazheng.project.greenplum.mapper.GpAbc2Mapper;
import com.huazheng.project.greenplum.model.GpAbc2;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Service
public class GpAbc2ServiceImpl {

	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private GpAbc2Mapper mapper;

	@KafkaListener(topics = "sqlserver.test2.dbo.abc2")
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
				GpAbc2 bean = JSONUtil.toBean(after, GpAbc2.class);
				mapper.insert(bean);
				log.info("mssql insert --> " + bean);
			} else if (op.equals("u")) {
				GpAbc2 bean = JSONUtil.toBean(after, GpAbc2.class);
				mapper.updateById(bean);
				log.info("mssql update --> " + bean);
			} else if (op.equals("d")) {
				GpAbc2 bean = JSONUtil.toBean(before, GpAbc2.class);
				mapper.deleteById(bean.getId());
				log.info("mssql delete --> " + bean);
			}
		}
	}
	
}
