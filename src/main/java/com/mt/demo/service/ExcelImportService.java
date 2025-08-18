package com.mt.demo.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.context.AnalysisContext;
import com.mt.demo.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Excel导入服务，用于导入商品分类数据
 */
@Service
public class ExcelImportService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelImportService.class);

    @Autowired
    private TikTokFirstlevelCategoryService firstlevelCategoryService;
    @Autowired
    private TikTokSecondlevelCategoryService secondlevelCategoryService;
    @Autowired
    private TikTokThirdlevelCategoryService thirdlevelCategoryService;
    @Autowired
    private TikTokFourthlevelCategoryService fourthlevelCategoryService;
    @Autowired
    private TikTokFifthlevelCategoryService fifthlevelCategoryService;

    // 用于存储已导入的分类，避免重复导入
    private final Map<String, Integer> categoryMap = new ConcurrentHashMap<>();

    /**
     * 从Excel文件导入分类数据
     * @param inputStream Excel文件输入流
     */
    public void importCategoryData(InputStream inputStream) {
        try {
            logger.info("开始导入分类数据");
            // 清空映射表，避免重复导入
            categoryMap.clear();
            
            // 使用EasyExcel读取Excel文件
            EasyExcel.read(inputStream, CategoryData.class, new CategoryDataListener())
                    .sheet() // 默认读取第一个sheet
                    .headRowNumber(1) // 表头行号，从0开始
                    .doRead();
            
            logger.info("分类数据导入完成，共导入 {} 条数据", categoryMap.size());
        } catch (Exception e) {
            logger.error("导入分类数据失败", e);
            throw new RuntimeException("导入分类数据失败", e);
        }
    }

    /**
     * 分类数据监听器，用于处理Excel读取到的数据
     */
    private class CategoryDataListener implements ReadListener<CategoryData> {

        @Override
        public void invoke(CategoryData data, AnalysisContext context) {
            try {
                processCategoryData(data);
            } catch (Exception e) {
                logger.error("处理分类数据失败: {}", data, e);
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            logger.info("Excel文件解析完成");
        }
    }

    /**
     * 处理分类数据，将其导入到数据库中
     * @param data 分类数据
     */
    private void processCategoryData(CategoryData data) {
        Date now = new Date();
        String createUser = "system";
        String updateUser = "system";

        // 处理一级分类
        if (data.getLevel1() != null && !data.getLevel1().trim().isEmpty()) {
            String level1Key = "LEVEL1_" + data.getLevel1();
            Integer level1Id = categoryMap.get(level1Key);
            
            if (level1Id == null) {
                TikTokFirstlevelCategory firstlevelCategory = new TikTokFirstlevelCategory();
                firstlevelCategory.setName(data.getLevel1());
                firstlevelCategory.setCreateTime(now);
                firstlevelCategory.setUpdateTime(now);
                firstlevelCategory.setCreateUser(createUser);
                firstlevelCategory.setUpdateUser(updateUser);
                
                firstlevelCategoryService.save(firstlevelCategory);
                level1Id = firstlevelCategory.getId();
                categoryMap.put(level1Key, level1Id);
                logger.debug("导入一级分类: {}, ID: {}", data.getLevel1(), level1Id);
            }

            // 处理二级分类
            if (data.getLevel2() != null && !data.getLevel2().trim().isEmpty()) {
                String level2Key = "LEVEL2_" + level1Id + "_" + data.getLevel2();
                Integer level2Id = categoryMap.get(level2Key);
                
                if (level2Id == null) {
                    TikTokSecondlevelCategory secondlevelCategory = new TikTokSecondlevelCategory();
                    secondlevelCategory.setName(data.getLevel2());
                    secondlevelCategory.setTiktokFirstlevelCategoryId(level1Id);
                    secondlevelCategory.setCategoryId(data.getCategoryId());
                    secondlevelCategory.setCreateTime(now);
                    secondlevelCategory.setUpdateTime(now);
                    secondlevelCategory.setCreateUser(createUser);
                    secondlevelCategory.setUpdateUser(updateUser);
                    
                    secondlevelCategoryService.save(secondlevelCategory);
                    level2Id = secondlevelCategory.getId();
                    categoryMap.put(level2Key, level2Id);
                    logger.debug("导入二级分类: {}, ID: {}", data.getLevel2(), level2Id);
                }

                // 处理三级分类
                if (data.getLevel3() != null && !data.getLevel3().trim().isEmpty()) {
                    String level3Key = "LEVEL3_" + level2Id + "_" + data.getLevel3();
                    Integer level3Id = categoryMap.get(level3Key);
                    
                    if (level3Id == null) {
                        TikTokThirdlevelCategory thirdlevelCategory = new TikTokThirdlevelCategory();
                        thirdlevelCategory.setName(data.getLevel3());
                        thirdlevelCategory.setTiktokSecondlevelCategoryId(level2Id);
                        thirdlevelCategory.setCategoryId(data.getCategoryId());
                        thirdlevelCategory.setCreateTime(now);
                        thirdlevelCategory.setUpdateTime(now);
                        thirdlevelCategory.setCreateUser(createUser);
                        thirdlevelCategory.setUpdateUser(updateUser);
                        
                        thirdlevelCategoryService.save(thirdlevelCategory);
                        level3Id = thirdlevelCategory.getId();
                        categoryMap.put(level3Key, level3Id);
                        logger.debug("导入三级分类: {}, ID: {}", data.getLevel3(), level3Id);
                    }

                    // 处理四级分类
                    if (data.getLevel4() != null && !data.getLevel4().trim().isEmpty()) {
                        String level4Key = "LEVEL4_" + level3Id + "_" + data.getLevel4();
                        Integer level4Id = categoryMap.get(level4Key);
                        
                        if (level4Id == null) {
                            TikTokFourthlevelCategory fourthlevelCategory = new TikTokFourthlevelCategory();
                            fourthlevelCategory.setName(data.getLevel4());
                            fourthlevelCategory.setTiktokThirdlevelCategoryId(level3Id);
                            fourthlevelCategory.setCategoryId(data.getCategoryId());
                            fourthlevelCategory.setCreateTime(now);
                            fourthlevelCategory.setUpdateTime(now);
                            fourthlevelCategory.setCreateUser(createUser);
                            fourthlevelCategory.setUpdateUser(updateUser);
                            
                            fourthlevelCategoryService.save(fourthlevelCategory);
                            level4Id = fourthlevelCategory.getId();
                            categoryMap.put(level4Key, level4Id);
                            logger.debug("导入四级分类: {}, ID: {}", data.getLevel4(), level4Id);
                        }

                        // 处理五级分类
                        if (data.getLevel5() != null && !data.getLevel5().trim().isEmpty()) {
                            String level5Key = "LEVEL5_" + level4Id + "_" + data.getLevel5();
                            Integer level5Id = categoryMap.get(level5Key);
                            
                            if (level5Id == null) {
                                TikTokFifthlevelCategory fifthlevelCategory = new TikTokFifthlevelCategory();
                                fifthlevelCategory.setName(data.getLevel5());
                                fifthlevelCategory.setTiktokFourthlevelCategoryId(level4Id);
                                fifthlevelCategory.setCategoryId(data.getCategoryId());
                                fifthlevelCategory.setCreateTime(now);
                                fifthlevelCategory.setUpdateTime(now);
                                fifthlevelCategory.setCreateUser(createUser);
                                fifthlevelCategory.setUpdateUser(updateUser);
                                
                                fifthlevelCategoryService.save(fifthlevelCategory);
                                level5Id = fifthlevelCategory.getId();
                                categoryMap.put(level5Key, level5Id);
                                logger.debug("导入五级分类: {}, ID: {}", data.getLevel5(), level5Id);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Excel数据模型类，用于映射Excel中的一行数据
     */
    public static class CategoryData {
        private Integer categoryId; // A列：category_id
        private String level1; // B列：一级分类
        private String level2; // C列：二级分类
        private String level3; // D列：三级分类
        private String level4; // E列：四级分类
        private String level5; // F列：五级分类

        // getter和setter方法
        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getLevel1() {
            return level1;
        }

        public void setLevel1(String level1) {
            this.level1 = level1;
        }

        public String getLevel2() {
            return level2;
        }

        public void setLevel2(String level2) {
            this.level2 = level2;
        }

        public String getLevel3() {
            return level3;
        }

        public void setLevel3(String level3) {
            this.level3 = level3;
        }

        public String getLevel4() {
            return level4;
        }

        public void setLevel4(String level4) {
            this.level4 = level4;
        }

        public String getLevel5() {
            return level5;
        }

        public void setLevel5(String level5) {
            this.level5 = level5;
        }

        @Override
        public String toString() {
            return "CategoryData{" +
                    "categoryId=" + categoryId +
                    ", level1='" + level1 + '\'' +
                    ", level2='" + level2 + '\'' +
                    ", level3='" + level3 + '\'' +
                    ", level4='" + level4 + '\'' +
                    ", level5='" + level5 + '\'' +
                    '}';
        }
    }
}