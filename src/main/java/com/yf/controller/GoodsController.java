package com.yf.controller;


import com.yf.entity.Goods;
import com.yf.entity.Category;
import com.yf.entity.dto.GoodsDTO;
import com.yf.entity.dto.page.GoodsPageQueryDTO;
import com.yf.entity.vo.select.GoodNameVO;
import com.yf.entity.vo.PageResult.GoodVO;
import com.yf.service.GoodsService;
import com.yf.service.CategoryService;
import com.yf.service.WarehouseService;
import com.yf.util.PageResult;
import com.yf.util.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/goods")
@Tag(name = "货品管理", description = "货品管理相关接口")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private WarehouseService warehouseService;
    // 获取货品列表
    @PostMapping("/all")
    public Result<List<GoodVO>> getGoodsList(){
        List<Goods> goods = goodsService.list();
        List<Category> categorys = categoryService.list();
        

        Map<String, String> categoryMap = categorys.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        List<GoodVO> goodsVO = goods.stream().map(good -> {
            String categoryName = categoryMap.getOrDefault(good.getCategoryId(), "未知类别");
            return GoodVO.builder()
                    .id(good.getId())
                    .name(good.getName())
                    .description(good.getDescription())
                    .image(good.getImageUrl())
                    .price(good.getPrice())
                    .stock(good.getStock())
                    .categoryName(categoryName)
                    .build();
        }).collect(Collectors.toList());
        
        return Result.success(goodsVO, "获取货品列表成功");

    }

    //根据仓库查货物
    @GetMapping("select/{warehouseId}")
    public Result<List<GoodNameVO>> getGoodsByWarehouse(@PathVariable String warehouseId) {
        List<GoodNameVO> goodList = goodsService.getGoodsByWarehouse(warehouseId);
        return Result.success(goodList, "根据仓库查货物成功");
    }



    @GetMapping("/list")
    public Result<List<GoodNameVO>> getGoodsNameList() {
        List<Goods> goods = goodsService.list();
        List<GoodNameVO>goodsVO= goods.stream().map(good -> {
            return GoodNameVO.builder()
                    .id(good.getId())
                    .name(good.getName())
                    .stock(good.getStock())
                    .build();
        }).collect(Collectors.toList());
        return Result.success(goodsVO, "获取货品列表成功");
    }

    @GetMapping("/page")
    public Result<PageResult<GoodVO>> pageQuery( GoodsPageQueryDTO queryDTO) {
        // 参数验证
        if (queryDTO.getPage() == null || queryDTO.getPage() < 1) {
            queryDTO.setPage(1);
        }
        if (queryDTO.getSize() == null || queryDTO.getSize() < 1) {
            queryDTO.setSize(10);
        }
        PageResult<GoodVO> pageResult = goodsService.pageQuery(queryDTO);

        return Result.success(pageResult, "获取货品列表成功");

    }


    // 添加货品
    @PostMapping("/add")
    public Result<String> addGoods(@RequestBody GoodsDTO goodsDTO) {
        if (goodsDTO.getName() == null || goodsDTO.getName().trim().isEmpty()) {
            return Result.error("货品名称不能为空");
        }

        Goods good = new Goods();
        good.setName(goodsDTO.getName());
        good.setCategoryId(goodsDTO.getCategoryId());
        good.setWarehouseId(goodsDTO.getWarehouseId());
        good.setStock(goodsDTO.getStock());
        good.setPrice(goodsDTO.getPrice());
        good.setImageUrl(goodsDTO.getImageUrl());
        good.setDescription(goodsDTO.getDescription());

        boolean result = goodsService.save(good);
        if (result) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }

    // 更新货品
    @PutMapping("/update")
    public Result<String> updateGoods(@RequestBody GoodsDTO goodsDTO) {
        if (goodsDTO.getId() == null) {
            return Result.error("货品ID不能为空");
        }

        Goods goods = new Goods();
        goods.setId(goodsDTO.getId());
        goods.setName(goodsDTO.getName());
        goods.setCategoryId(goodsDTO.getCategoryId());
        goods.setWarehouseId(goodsDTO.getWarehouseId());
        goods.setStock(goodsDTO.getStock());
        goods.setPrice(goodsDTO.getPrice());
        goods.setImageUrl(goodsDTO.getImageUrl());
        goods.setDescription(goodsDTO.getDescription());

        boolean result = goodsService.updateById(goods);
        if (result) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    // 删除货品
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteGoods(@PathVariable String id) {
        boolean result = goodsService.removeById(id);
        if (result) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    

    // 上传图片
    @PostMapping("/upload")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error("上传文件不能为空");
            }
            String fileName = file.getOriginalFilename();
            String url = "/upload/" + java.net.URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
            // 这里应该保存文件到服务器或云存储
            return Result.success(url, "上传成功");
        } catch (Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}