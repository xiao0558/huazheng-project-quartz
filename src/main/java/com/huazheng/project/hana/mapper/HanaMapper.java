package com.huazheng.project.hana.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.huazheng.project.hana.model.HanaTest1;

public interface HanaMapper {
	
	List<HanaTest1> selectHanaTest1List(@Param("rowids") Long rowids);
	
}
