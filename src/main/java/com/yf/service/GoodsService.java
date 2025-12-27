package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.entity.Goods;
import com.yf.entity.dto.GoodsPageQueryDTO;
import com.yf.entity.vo.GoodVO;

import com.yf.util.PageResult;
import org.springframework.web.multipart.MultipartFile;

public interface GoodsService extends IService<Goods> {
    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    String uploadImage(MultipartFile file);


    PageResult<GoodVO> pageQuery(GoodsPageQueryDTO queryDTO);

}