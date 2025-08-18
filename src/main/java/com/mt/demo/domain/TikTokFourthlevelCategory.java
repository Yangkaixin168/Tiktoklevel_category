package com.mt.demo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 四级类目表
 */
@Data
@TableName("tiktok_fourthlevel_category")
public class TikTokFourthlevelCategory {
    private Integer id; // 商品四级类目主键ID
    private String name; // 商品四级类目名称
    private Date createTime; // 创建时间
    private Date updateTime; // 修改时间
    private String createUser; // 创建人id
    private String updateUser; // 修改人id
    private Integer tiktokThirdlevelCategoryId; // 商品三级类目id
    private Integer categoryId; // TikTok 类目 ID
    private String remark; // 备注
}