package com.huazheng.project.mssql.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.huazheng.project.mssql.model.MsSqlAbc;

public interface MsSqlMapper {

	List<MsSqlAbc> selectMsSqlAbcList(@Param(Constants.WRAPPER) Wrapper<MsSqlAbc> queryWrapper);
	
}
