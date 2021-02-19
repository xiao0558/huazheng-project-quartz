package com.huazheng.project.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.huazheng.project.greenplum.mapper.GpTest3Mapper;
import com.huazheng.project.greenplum.model.GpTest3;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Service
public class GpTest3ServiceImpl {

	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private GpTest3Mapper mapper;
	
	@KafkaListener(topics = "test_topic_4")
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
				GpTest3 bean = JSONUtil.toBean(after, GpTest3.class);
				mapper.insert(bean);
				log.info("hana insert --> " + bean);
			} else if (op.equals("u")) {
				after.set("times", after.getStr("times").substring(0, 19));
				GpTest3 bean = JSONUtil.toBean(after, GpTest3.class);
				mapper.updateById(bean);
				log.info("hana update --> " + bean);
			} else if (op.equals("d")) {
				before.set("times", before.getStr("times").substring(0, 19));
				GpTest3 bean = JSONUtil.toBean(before, GpTest3.class);
				mapper.deleteById(bean.getId());
				log.info("hana delete --> " + bean);
			}
		}
	}
	
}
