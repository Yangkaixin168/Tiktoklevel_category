package com.mt.demo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 五级类目表
 */
@Data
@TableName("tiktok_fifthlevel_category")
public class TikTokFifthlevelCategory {
    private Integer id; // 商品五级类目主键ID
    private String name; // 商品五级类目名称
    private Integer categoryId; // TikTok 类目 ID
    private Integer tiktokFourthlevelCategoryId; // 商品四级类目id
    private Date createTime; // 创建时间
    private Date updateTime; // 修改时间
    private String createUser; // 创建人id
    private String updateUser; // 修改人id
    private String remark; // 备注
}