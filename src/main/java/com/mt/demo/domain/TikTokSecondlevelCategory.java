package com.mt.demo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 二级类目表
 */
@Data
@TableName("tiktok_secondlevel_category")
public class TikTokSecondlevelCategory {
    private Integer id; // 商品二级类目主键ID
    private String name; // 商品二级类目名称
    private Date createTime; // 创建时间
    private Date updateTime; // 修改时间
    private String createUser; // 创建人id
    private String updateUser; // 修改人id
    private Integer tiktokFirstlevelCategoryId; // 商品一级类目id
    private Integer categoryId; // TikTok 类目 ID
    private String remark; // 备注
}