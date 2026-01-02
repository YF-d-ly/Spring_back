package com.yf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.entity.Goods;
import com.yf.entity.dto.page.GoodsPageQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("select * from goods where name like concat('%',#{name},'%')")
    Page<Goods> pageQuery(GoodsPageQueryDTO queryDTO);
}