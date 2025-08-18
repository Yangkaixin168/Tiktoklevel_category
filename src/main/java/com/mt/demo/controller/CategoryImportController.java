package com.mt.demo.controller;

import com.mt.demo.service.ExcelImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 分类导入Controller，提供文件上传和导入接口
 */
@RestController
@RequestMapping("/api/category")
public class CategoryImportController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryImportController.class);

    @Autowired
    private ExcelImportService excelImportService;

    /**
     * 上传Excel文件并导入分类数据
     * @param file Excel文件
     * @return 导入结果
     */
    @PostMapping("/import")
    public ResponseEntity<String> importCategory(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                logger.warn("上传的文件为空");
                return new ResponseEntity<>("上传的文件为空", HttpStatus.BAD_REQUEST);
            }

            // 检查文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
                logger.warn("文件类型不正确: {}", fileName);
                return new ResponseEntity<>("文件类型不正确，请上传Excel文件", HttpStatus.BAD_REQUEST);
            }

            logger.info("开始处理文件: {}", fileName);
            // 调用服务层导入数据
            excelImportService.importCategoryData(file.getInputStream());
            
            logger.info("文件导入成功: {}", fileName);
            return new ResponseEntity<>("分类数据导入成功", HttpStatus.OK);
        } catch (IOException e) {
            logger.error("文件读取失败", e);
            return new ResponseEntity<>("文件读取失败", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("导入数据失败", e);
            return new ResponseEntity<>("导入数据失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 测试接口
     * @return 测试结果
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("Category Import API is working", HttpStatus.OK);
    }
}