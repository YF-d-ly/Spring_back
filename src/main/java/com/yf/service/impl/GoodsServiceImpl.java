package com.yf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.entity.Category;
import com.yf.entity.Goods;
import com.yf.entity.UserWarehouse;
import com.yf.entity.Warehouse;
import com.yf.entity.dto.UserDTO;
import com.yf.entity.dto.page.GoodsPageQueryDTO;
import com.yf.entity.vo.select.GoodNameVO;
import com.yf.entity.vo.PageResult.GoodVO;
import com.yf.handler.BusinessException;
import com.yf.mapper.GoodsMapper;
import com.yf.service.CategoryService;
import com.yf.service.GoodsService;
import com.yf.service.UserWarehouseService;
import com.yf.service.WarehouseService;

import com.yf.util.PageResult;
import com.yf.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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

    @Autowired
    private UserWarehouseService userWarehouseService;

    @Override
    public void updateStockForTransfer(String goodsId, String sourceWarehouseId, String targetWarehouseId, Integer num) {
        // 构建库存日志对象
        Goods goods = new Goods();
        goods.setId(goodsId);
        goods.setWarehouseId(targetWarehouseId);
        goods.setStock(num);
        goods.setUpdatedAt(LocalDateTime.now());

        // 更新库存
        this.updateById(goods);
    }

    @Override
    public List<GoodNameVO> getGoodsByWarehouse(String warehouseId) {
        // 创建查询条件，根据仓库ID查询货品
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goods::getWarehouseId, warehouseId);

        // 查询货品列表
        List<Goods> goodsList = goodsMapper.selectList(queryWrapper);

        // 将Goods实体列表转换为GoodNameVO列表
        return goodsList.stream()
                .map(goods -> GoodNameVO.builder()
                        .id(goods.getId())
                        .name(goods.getName())
                        .stock(goods.getStock())
                        .build())
                .collect(Collectors.toList());
    }

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
                    .imageUrl(goods.getImageUrl())
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
        // 此方法不再使用，现在使用UploadFileUtil进行文件上传
        throw new UnsupportedOperationException("请使用FileUploadController直接上传文件");
    }



    @Override
    public PageResult<GoodVO> pageQueryWithUserPermission(GoodsPageQueryDTO queryDTO) {
        // 1. 权限检查与数据准备
        List<String> warehouseIds = getAuthorizedWarehouseIds();

        // 2. 使用PageHelper进行分页
        com.github.pagehelper.PageHelper.startPage(queryDTO.getPage(), queryDTO.getSize());
        
        // 3. 构建查询条件并执行查询
        LambdaQueryWrapper<Goods> queryWrapper = buildQueryWrapperForPermission(queryDTO, warehouseIds);
        List<Goods> goodsList = goodsMapper.selectList(queryWrapper);

        // 4. 转换为分页结果
        com.github.pagehelper.PageInfo<Goods> pageInfo = new com.github.pagehelper.PageInfo<>(goodsList);
        
        // 5. 转换为VO并返回
        return convertToPageResultWithPageInfo(pageInfo, goodsList);
    }

    /**
     * 获取当前用户有权限的仓库ID列表（超级管理员返回空列表代表所有仓库）
     */
    private List<String> getAuthorizedWarehouseIds() {
        UserDTO currentUser = UserHolder.getUser();

        // 超级管理员直接返回空列表，在查询时不加仓库限制
        if (isSuperAdmin(currentUser)) {
            return List.of();
        }

        return userWarehouseService.lambdaQuery()
                .eq(UserWarehouse::getUserId, currentUser.getId())
                .list()
                .stream()
                .map(UserWarehouse::getWarehouseId)
                .collect(Collectors.toList());
    }

    /**
     * 判断是否为超级管理员
     */
    private boolean isSuperAdmin(UserDTO user) {
        return user != null && user.getRoleId() != null && "ROLE_001".equals(user.getRoleId());
    }

    /**
     * 构建查询条件（用于权限控制）
     */
    private LambdaQueryWrapper<Goods> buildQueryWrapperForPermission(GoodsPageQueryDTO queryDTO, List<String> warehouseIds) {
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();

        // 只有非超级管理员（warehouseIds不为空）才添加仓库限制
        if (!warehouseIds.isEmpty()) {
            queryWrapper.in(Goods::getWarehouseId, warehouseIds);
        }

        // 链式设置查询条件
        queryWrapper.like(StringUtils.hasText(queryDTO.getName()), Goods::getName, queryDTO.getName())
                .eq(StringUtils.hasText(queryDTO.getCategoryId()), Goods::getCategoryId, queryDTO.getCategoryId())
                .ge(queryDTO.getMinPrice() != null, Goods::getPrice, queryDTO.getMinPrice())
                .le(queryDTO.getMaxPrice() != null, Goods::getPrice, queryDTO.getMaxPrice());

        // 处理指定仓库的权限验证
        if (StringUtils.hasText(queryDTO.getWarehouseId())) {
            if (warehouseIds.isEmpty() || warehouseIds.contains(queryDTO.getWarehouseId())) {
                queryWrapper.eq(Goods::getWarehouseId, queryDTO.getWarehouseId());
            } else {
                throw new BusinessException("无该仓库的操作权限");
            }
        }

        return queryWrapper;
    }

    /**
     * 转换为分页结果（使用PageInfo）
     */
    private PageResult<GoodVO> convertToPageResultWithPageInfo(com.github.pagehelper.PageInfo<Goods> pageInfo, List<Goods> goodsList) {
        if (goodsList.isEmpty()) {
            return new PageResult<>(0L, List.of(), 0L, Long.valueOf(pageInfo.getPageSize()), Long.valueOf(pageInfo.getPageNum()));
        }

        // 批量查询分类和仓库信息（避免N+1查询问题）
        Map<String, String> categoryMap = getCategoryMap();
        Map<String, String> warehouseMap = getWarehouseMap();

        // 使用Stream转换记录
        List<GoodVO> voList = goodsList.stream()
                .map(goods -> convertToGoodVO(goods, categoryMap, warehouseMap))
                .collect(Collectors.toList());

        return new PageResult<GoodVO>(pageInfo.getTotal(), voList, Long.valueOf(pageInfo.getPages()),
                Long.valueOf(pageInfo.getPageSize()), Long.valueOf(pageInfo.getPageNum()));
    }

    /**
     * 转换为GoodVO
     */
    private GoodVO convertToGoodVO(Goods goods, Map<String, String> categoryMap, Map<String, String> warehouseMap) {
        return GoodVO.builder()
                .id(goods.getId())
                .name(goods.getName())
                .imageUrl(goods.getImageUrl())
                .price(goods.getPrice())
                .stock(goods.getStock())
                .description(goods.getDescription())
                .categoryId(goods.getCategoryId())
                .categoryName(categoryMap.getOrDefault(goods.getCategoryId(), "未知类别"))
                .warehouseId(goods.getWarehouseId())
                .warehouseName(warehouseMap.getOrDefault(goods.getWarehouseId(), "未知仓库"))
                .build();
    }

    /**
     * 获取分类映射
     */
    private Map<String, String> getCategoryMap() {
        return categoryService.list().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
    }

    /**
     * 获取仓库映射
     */
    private Map<String, String> getWarehouseMap() {
        return warehouseService.list().stream()
                .collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
    }
}