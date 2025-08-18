package com.mt.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * ExcelImportService的测试类，用于测试TikTok商品分类Excel文件导入功能
 */
@SpringBootTest
class ExcelImportServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ExcelImportServiceTest.class);

    @Autowired
    private ExcelImportService excelImportService;

    /**
     * 测试导入Google商品分类Excel文件数据到数据库
     */
    @Test
    void testImportCategoryData() {
        try {
            logger.info("开始测试Excel导入功能");
            
            // 读取resource目录下的Excel文件
            ClassPathResource resource = new ClassPathResource("google_product_category_双语版.xlsx");
            try (InputStream inputStream = resource.getInputStream()) {
                // 调用导入方法
                excelImportService.importCategoryData(inputStream);
                logger.info("Excel文件导入测试完成");
            }
        } catch (Exception e) {
            logger.error("Excel文件导入测试失败", e);
            // 如果测试失败，抛出异常使测试失败
            throw new RuntimeException("Excel文件导入测试失败", e);
        }
    }
}