package com.yf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.entity.Category;
import com.yf.entity.Goods;
import com.yf.entity.Warehouse;
import com.yf.entity.dto.GoodsPageQueryDTO;
import com.yf.entity.vo.GoodVO;

import com.yf.mapper.GoodsMapper;
import com.yf.service.CategoryService;
import com.yf.service.GoodsService;
import com.yf.service.WarehouseService;
import com.yf.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WarehouseService warehouseService;

    @Override
    public PageResult<GoodVO> pageQuery(GoodsPageQueryDTO queryDTO) {

        // 1. 设置分页参数
        Page<Goods> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());

        // 2. 构建查询条件
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();

        // 添加货品名称条件（模糊查询）
        if (StringUtils.hasText(queryDTO.getName())) {
            queryWrapper.like(Goods::getName, queryDTO.getName());
        }

        // 添加类别条件
        if (StringUtils.hasText(queryDTO.getCategoryId())) {
            queryWrapper.eq(Goods::getCategoryId, queryDTO.getCategoryId());
        }

        // 添加仓库条件
        if (StringUtils.hasText(queryDTO.getWarehouseId())) {
            queryWrapper.eq(Goods::getWarehouseId, queryDTO.getWarehouseId());
        }

        // 添加价格范围条件
        if (queryDTO.getMinPrice() != null) {
            queryWrapper.ge(Goods::getPrice, queryDTO.getMinPrice());
        }
        if (queryDTO.getMaxPrice() != null) {
            queryWrapper.le(Goods::getPrice, queryDTO.getMaxPrice());
        }

        // 3. 执行分页查询
        Page<Goods> goodsPage = goodsMapper.selectPage(page, queryWrapper);

        // 4. 查询分类和仓库信息（用于映射名称）
        List<Category> categories = categoryService.list();
        List<Warehouse> warehouses = warehouseService.list();

        Map<String, String> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
        Map<String, String> warehouseMap = warehouses.stream()
                .collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));

        // 5. 转换为VO
        List<GoodVO> goodsVOList = goodsPage.getRecords().stream().map(goods -> {
            String categoryName = categoryMap.getOrDefault(goods.getCategoryId(), "未知类别");
            String warehouseName = warehouseMap.getOrDefault(goods.getWarehouseId(), "未知仓库");

            return GoodVO.builder()
                    .id(goods.getId())
                    .name(goods.getName())
                    .image(goods.getImageUrl())
                    .price(goods.getPrice())
                    .stock(goods.getStock())
                    .description(goods.getDescription())
                    .categoryId(goods.getCategoryId())
                    .categoryName(categoryName)
                    .warehouseId(goods.getWarehouseId())
                    .warehouseName(warehouseName)
                    .build();
        }).collect(Collectors.toList());


        // 7. 构建分页结果
        return new PageResult<>(
                goodsPage.getTotal(),
                goodsVOList,
                goodsPage.getPages(),
                goodsPage.getSize(),
                goodsPage.getCurrent()
        );

    }

    
    @Override
    public String uploadImage(MultipartFile file) {
        try {
            // 生成唯一文件名
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.')) : ".jpg";
            String fileName = UUID.randomUUID().toString() + extension;
            
            // 定义上传目录
            String uploadDir = "uploads/";
            Path uploadPath = Paths.get(uploadDir);
            
            // 创建上传目录
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 保存文件
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());
            
            // 返回文件访问路径
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }
}