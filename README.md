# TikTok商品分类导入工具使用指南

## 概述

本工具使用EasyExcel库，将Excel文件中的商品分类数据导入到MySQL数据库的五张分类表中。支持导入一级到五级的商品分类，并维护各级分类之间的父子关系。

## 准备工作

1. 确保项目中已添加以下依赖：
   - EasyExcel: 用于读取Excel文件
   - MyBatis-Plus: 简化数据库操作
   - MySQL驱动: 连接数据库
   - Spring Boot: 提供Web服务和依赖管理

2. 确保数据库中已创建以下五张表：
   - `tiktok_firstlevel_category`: 一级类目表
   - `tiktok_secondlevel_category`: 二级类目表
   - `tiktok_thirdlevel_category`: 三级类目表
   - `tiktok_fourthlevel_category`: 四级类目表
   - `tiktok_fifthlevel_category`: 五级类目表

3. 确保Excel文件格式正确，包含以下列：
   - A列: `category_id` (唯一标识符)
   - B列: 一级分类名称
   - C列: 二级分类名称
   - D列: 三级分类名称
   - E列: 四级分类名称
   - F列: 五级分类名称

## 项目结构

```
src/main/java/com/mt/demo/
├── DemoApplication.java       # 应用启动类
├── controller/                # 控制器
│   └── CategoryImportController.java  # 分类导入接口
├── domain/                    # 实体类
│   ├── TikTokFirstlevelCategory.java  # 一级分类实体
│   ├── TikTokSecondlevelCategory.java # 二级分类实体
│   ├── TikTokThirdlevelCategory.java  # 三级分类实体
│   ├── TikTokFourthlevelCategory.java # 四级分类实体
│   └── TikTokFifthlevelCategory.java  # 五级分类实体
├── mapper/                    # Mapper接口
│   ├── TikTokFirstlevelCategoryMapper.java  # 一级分类Mapper
│   ├── TikTokSecondlevelCategoryMapper.java # 二级分类Mapper
│   ├── TikTokThirdlevelCategoryMapper.java  # 三级分类Mapper
│   ├── TikTokFourthlevelCategoryMapper.java # 四级分类Mapper
│   └── TikTokFifthlevelCategoryMapper.java  # 五级分类Mapper
├── service/                   # 服务层
│   ├── ExcelImportService.java            # Excel导入服务
│   ├── TikTokFirstlevelCategoryService.java  # 一级分类服务接口
│   ├── TikTokSecondlevelCategoryService.java # 二级分类服务接口
│   ├── TikTokThirdlevelCategoryService.java  # 三级分类服务接口
│   ├── TikTokFourthlevelCategoryService.java # 四级分类服务接口
│   ├── TikTokFifthlevelCategoryService.java  # 五级分类服务接口
│   └── impl/                  # 服务实现
│       ├── TikTokFirstlevelCategoryServiceImpl.java  # 一级分类服务实现
│       ├── TikTokSecondlevelCategoryServiceImpl.java # 二级分类服务实现
│       ├── TikTokThirdlevelCategoryServiceImpl.java  # 三级分类服务实现
│       ├── TikTokFourthlevelCategoryServiceImpl.java # 四级分类服务实现
│       └── TikTokFifthlevelCategoryServiceImpl.java  # 五级分类服务实现
```

## 工具类说明

### ExcelImportService

这是核心的Excel导入服务类，负责读取Excel文件并将数据导入到数据库中。主要功能包括：

1. `importCategoryData(InputStream inputStream)`: 导入分类数据的主方法
   - 参数: Excel文件的输入流
   - 返回值: 无，导入成功或抛出异常

2. `processCategoryData(CategoryData data)`: 处理单条分类数据
   - 参数: 包含各级分类信息的数据对象
   - 功能: 按层级处理分类数据，先保存父分类，再保存子分类，并维护它们之间的关系

3. `CategoryData` 内部类: 用于映射Excel中的一行数据
   - 属性: categoryId, level1, level2, level3, level4, level5
   - 对应Excel中的A列到F列

### CategoryImportController

提供REST API接口，让用户可以通过HTTP请求上传Excel文件并导入数据。主要接口：

1. `POST /api/category/import`: 上传Excel文件并导入分类数据
   - 请求参数: `file` (MultipartFile类型)
   - 返回值: JSON格式的导入结果

2. `GET /api/category/test`: 测试接口，用于验证API是否正常工作

## 导入流程详解

1. **接收文件**: 通过Controller接收用户上传的Excel文件
2. **文件校验**: 检查文件是否为空，以及文件类型是否为Excel
3. **初始化导入**: 调用ExcelImportService的importCategoryData方法，清空缓存Map
4. **读取Excel**: 使用EasyExcel读取Excel文件，逐行处理数据
5. **数据处理**: 
   - 对于每一行数据，先检查一级分类是否已存在
   - 如果不存在，则创建新的一级分类并保存到数据库
   - 然后处理二级分类，同样先检查是否已存在，不存在则创建
   - 以此类推，处理三级、四级、五级分类
   - 每创建一个分类，都将其ID缓存到Map中，以便后续处理子分类时使用
6. **完成导入**: 所有数据处理完成后，记录导入结果

## 导入逻辑说明

1. **重复数据处理**: 使用ConcurrentHashMap缓存已导入的分类，避免重复导入
2. **父子关系维护**: 
   - 二级分类关联一级分类的ID
   - 三级分类关联二级分类的ID
   - 四级分类关联三级分类的ID
   - 五级分类关联四级分类的ID
3. **Category ID处理**: 从Excel的A列读取category_id，保存到各级分类表中
4. **创建时间和创建人**: 导入时会设置当前时间为创建时间，创建人为"system"

## 使用示例

### 方法一：通过API接口导入

1. 启动Spring Boot应用
2. 使用Postman或其他HTTP工具发送POST请求到 `http://localhost:8084/api/category/import`
3. 在请求中添加`file`参数，选择要导入的Excel文件
4. 发送请求，等待导入完成
5. 查看返回结果，确认导入是否成功

### 方法二：在代码中直接调用

如果需要在代码中直接调用导入功能，可以按照以下方式：

```java
@Autowired
private ExcelImportService excelImportService;

public void importExcel() {
    try {
        // 读取Excel文件
        File file = new File("/path/to/google_product_category_双语版.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        
        // 调用导入方法
        excelImportService.importCategoryData(inputStream);
        
        // 关闭流
        inputStream.close();
        
        System.out.println("分类数据导入成功");
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("分类数据导入失败: " + e.getMessage());
    }
}
```

## 注意事项

1. Excel文件必须包含正确的列结构（A到F列）
2. 确保数据库连接配置正确，能够正常连接到MySQL数据库
3. 导入过程中会占用一定的系统资源，对于大规模数据导入，可能需要调整JVM参数
4. 导入过程中会记录详细的日志，可以通过调整日志级别来控制日志输出量
5. 如果Excel文件中有重复的分类数据，工具会自动处理，不会创建重复记录

## 常见问题排查

1. **导入失败，提示"文件类型不正确"**
   - 检查文件是否为.xlsx或.xls格式
   - 检查文件名是否正确

2. **导入失败，提示数据库连接错误**
   - 检查application.yaml中的数据库配置是否正确
   - 确认MySQL服务是否正常运行

3. **导入成功，但部分数据未导入**
   - 检查Excel文件中是否有空行或格式不正确的行
   - 检查日志文件，查看是否有异常信息

4. **导入后数据不完整或关系错误**
   - 检查Excel文件中的层级关系是否正确
   - 确认每一行数据的各级分类名称是否完整

## 扩展建议

1. **批量导入优化**: 对于大量数据，可以考虑使用批量插入来提高性能
2. **导入进度监控**: 可以添加进度条或进度报告功能，实时显示导入进度
3. **数据校验增强**: 添加更复杂的数据校验逻辑，确保数据质量
4. **导入结果报告**: 生成详细的导入结果报告，包括成功和失败的记录信息
5. **定时任务**: 可以配置定时任务，定期自动导入分类数据
