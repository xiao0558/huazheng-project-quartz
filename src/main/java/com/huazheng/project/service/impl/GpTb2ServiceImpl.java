package com.huazheng.project.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.huazheng.project.greenplum.mapper.GpTb2Mapper;
import com.huazheng.project.greenplum.model.GpTb2;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Service
public class GpTb2ServiceImpl {

	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private GpTb2Mapper mapper;
	
	@KafkaListener(topics = "mysql.abc.abc.tb2")
	public void mysqlData(ConsumerRecord<?, ?> record) {
		Object value = record.value();
		if (value != null) {
			String data = value.toString();
			JSONObject json = JSONUtil.parseObj(data);
			JSONObject payload = json.getJSONObject("payload");
			String op = payload.getStr("op");
			JSONObject before = payload.getJSONObject("before");
			JSONObject after = payload.getJSONObject("after");
			if (op.equals("c")) {
				GpTb2 bean = JSONUtil.toBean(after, GpTb2.class);
				mapper.insert(bean);
				log.info("mysql insert --> " + bean);
			} else if (op.equals("u")) {
				GpTb2 bean = JSONUtil.toBean(after, GpTb2.class);
				mapper.updateById(bean);
				log.info("mysql update --> " + bean);
			} else if (op.equals("d")) {
				GpTb2 bean = JSONUtil.toBean(before, GpTb2.class);
				mapper.deleteById(bean.getId());
				log.info("mysql delete --> " + bean);
			}
		}
	}
	
}
