package com.huazheng.project.greenplum.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("hana_test2")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="GpTest2对象", description="测试表")
public class GpTest2 extends Model<GpTest2> {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "主键")
    @TableId("id")
	private Integer id;
	
	@ApiModelProperty(value = "用户")
	@TableField("\"user\"")
	private String user;
	
	@ApiModelProperty(value = "密码")
	@TableField("pswd")
	private String pswd;
	
	@ApiModelProperty(value = "时间")
	@TableField("times")
	private Date times;
	
	@Override
    protected Serializable pkVal() {
        return this.id;
    }
	
}
