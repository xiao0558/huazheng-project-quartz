package com.huazheng.project.mysql.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.huazheng.project.mysql.model.MySqlTb1;

public interface MySqlMapper {

	List<MySqlTb1> selectMySqlTb1List(@Param(Constants.WRAPPER) Wrapper<MySqlTb1> queryWrapper);
	
}
