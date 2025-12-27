package com.yf.controller;

import com.yf.entity.Category;
import com.yf.entity.vo.CategoryVO;
import com.yf.service.CategoryService;
import com.yf.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@Tag(name = "类别管理", description = "类别管理相关接口")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 获取货品类别列表 - 修复版本
    @GetMapping("/names")
    @Operation(summary = "获取类别名称列表", description = "获取所有类别的ID和名称")
    public Result<List<CategoryVO>> getCategoryNames() {
        List<Category> categoryList = categoryService.list();
        List<CategoryVO> categoryVOList = categoryList.stream().map(category -> {
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setId(category.getId());
            categoryVO.setName(category.getName());
            return categoryVO;
        }).collect(Collectors.toList());
        return Result.success(categoryVOList, "获取类别名称成功");
    }

    // 添加货品类别
    @PostMapping("/add")
    @Operation(summary = "添加类别", description = "添加一个新的商品类别")
    public Result<String> addCategory(@RequestBody Category category) {
        boolean result = categoryService.save(category);
        if (result) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }

    // 更新货品类别
    @PutMapping("/update")
    @Operation(summary = "更新类别", description = "更新商品类别信息")
    public Result<String> updateCategory(@RequestBody Category category) {
        boolean result = categoryService.updateById(category);
        if (result) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    // 删除货品类别
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除类别", description = "根据ID删除商品类别")
    public Result<String> deleteCategory(@PathVariable String id) {
        boolean result = categoryService.removeById(id);
        if (result) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
}