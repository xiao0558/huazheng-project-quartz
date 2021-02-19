package com.huazheng.project.greenplum.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.huazheng.project.greenplum.model.GpAbc;
import com.huazheng.project.greenplum.model.GpTb1;
import com.huazheng.project.greenplum.model.GpTest1;

public interface GreenplumMapper {
	
	List<GpAbc> selectGpAbcList(@Param(Constants.WRAPPER) Wrapper<GpAbc> queryWrapper);
	List<GpTb1> selectGpTb1List(@Param(Constants.WRAPPER) Wrapper<GpTb1> queryWrapper);
	List<GpTest1> selectGpTest1List(@Param(Constants.WRAPPER) Wrapper<GpTest1> queryWrapper);
	
}
