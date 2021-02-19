package com.huazheng.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.huazheng.project.greenplum.mapper.GpTest1Mapper;
import com.huazheng.project.greenplum.model.GpTest1;
import com.huazheng.project.hana.mapper.HanaMapper;
import com.huazheng.project.hana.model.HanaTest1;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Service
public class GpTest1ServiceImpl {

	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private GpTest1Mapper mapper;
	
	@Autowired
	private HanaMapper hanaMapper;
	
	@Resource
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private RedisTemplate<String, String> redis1Template;
	
	@KafkaListener(topics = "test_topic_2")
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
				GpTest1 bean = JSONUtil.toBean(after, GpTest1.class);
				mapper.insert(bean);
				log.info("hana insert --> " + bean);
			} else if (op.equals("u")) {
				after.set("times", after.getStr("times").substring(0, 19));
				GpTest1 bean = JSONUtil.toBean(after, GpTest1.class);
				mapper.updateById(bean);
				log.info("hana update --> " + bean);
			} else if (op.equals("d")) {
				before.set("times", before.getStr("times").substring(0, 19));
				GpTest1 bean = JSONUtil.toBean(before, GpTest1.class);
				mapper.deleteById(bean.getId());
				log.info("hana delete --> " + bean);
			}
		}
	}
	
	// 方式2，调度器方式，读取rowids的方式抓取历史数据，丢入kafka
	@XxlJob("hanaFullData")
	public ReturnT<String> hanaFullData(String param) {
		ValueOperations<String, String> opsForValue = redis1Template.opsForValue();
		opsForValue.setIfAbsent("huazheng:full:HanaTest1:rowids", "0");
		Long rowids = Long.parseLong(opsForValue.get("huazheng:full:HanaTest1:rowids"));
		
		List<HanaTest1> list = hanaMapper.selectHanaTest1List(rowids);
		if (list.size() == 0) {
			// TODO 此处改为停止任务
			redis1Template.opsForValue().set("huazheng:full:HanaTest1:rowids", "0"); // 计数器复位
		}
		
		for (HanaTest1 object : list) {
			String times = DateUtil.format(object.getTimes(), "yyyy-MM-dd HH:mm:ss");
			StringBuffer sb = new StringBuffer();
			sb.append("\"" + object.getId() + "\",");
			sb.append("\"" + object.getPswd() + "\",");
			sb.append("\"" + times + "\"");
			kafkaTemplate.send("test_topic_2_merge", sb.toString());
			log.info("hana merge --> " + sb.toString());
			redis1Template.opsForValue().set("huazheng:full:HanaTest1:rowids", object.getRowids().toString());
		}
//		XxlJobLogger.log("");
		return ReturnT.SUCCESS;
	}

	// 用于测试，发布前注释掉
//	@Scheduled(cron = "* * * * * ?")
//	public void hanaFullDataTest() {
//		hanaFullData(null);
//	}
	
}
