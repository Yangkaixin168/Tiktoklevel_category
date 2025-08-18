package com.mt.demo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 一级类目表
 */
@Data
@TableName("tiktok_firstlevel_category")
public class TikTokFirstlevelCategory {
    private Integer id; // 商品一级类目主键ID
    private String name; // 商品一级类目名称
    private Date createTime; // 创建时间
    private Date updateTime; // 修改时间
    private String createUser; // 创建人id
    private String updateUser; // 修改人id
    private String remark; // 备注
}