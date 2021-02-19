package com.huazheng.project.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.huazheng.project.greenplum.mapper.GpTest2Mapper;
import com.huazheng.project.greenplum.model.GpTest2;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Service
public class GpTest2ServiceImpl {

	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private GpTest2Mapper mapper;
	
	@KafkaListener(topics = "test_topic_3")
	public void hanaData(ConsumerRecord<?, ?> record) {
		Object value = record.value();
		if (value != null) {
			String data = value.toString();
			JSONObject json = JSONUtil.parseObj(data);
			JSONObject payload = json.getJSONObject("payload");
			String op = payload.getStr("TYPE");
			JSONObject before = payload.getJSONObject("BEFORE");
			JSONObject after = payload.getJSONObject("AFTER");
			if (op.equals("c")) {
				after.set("times", after.getStr("times").substring(0, 19));
				GpTest2 bean = JSONUtil.toBean(after, GpTest2.class);
				mapper.insert(bean);
				log.info("hana insert --> " + bean);
			} else if (op.equals("u")) {
				after.set("times", after.getStr("times").substring(0, 19));
				GpTest2 bean = JSONUtil.toBean(after, GpTest2.class);
				mapper.updateById(bean);
				log.info("hana update --> " + bean);
			} else if (op.equals("d")) {
				before.set("times", before.getStr("times").substring(0, 19));
				GpTest2 bean = JSONUtil.toBean(before, GpTest2.class);
				mapper.deleteById(bean.getId());
				log.info("hana delete --> " + bean);
			}
		}
	}
	
}
