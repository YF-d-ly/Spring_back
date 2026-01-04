package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.entity.Goods;
import com.yf.entity.dto.page.GoodsPageQueryDTO;
import com.yf.entity.vo.select.GoodNameVO;
import com.yf.entity.vo.PageResult.GoodVO;

import com.yf.util.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GoodsService extends IService<Goods> {
    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    String uploadImage(MultipartFile file);


    List<GoodNameVO> getGoodsByWarehouse(String warehouseId);


    PageResult<GoodVO> pageQuery(GoodsPageQueryDTO queryDTO);

    /**
     * 更新调货库存
     * @param goodsId 货品ID
     * @param sourceWarehouseId 源仓库ID
     * @param targetWarehouseId 目标仓库ID
     * @param num 调货数量
     */
    void updateStockForTransfer(String goodsId, String sourceWarehouseId, String targetWarehouseId, Integer num);

    /**
     * 根据用户仓库权限分页查询货品列表
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    PageResult<GoodVO> pageQueryWithUserPermission(GoodsPageQueryDTO queryDTO);
}