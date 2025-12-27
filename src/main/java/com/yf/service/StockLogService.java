package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.entity.StockLog;
import com.yf.entity.dto.InboundDTO;
import com.yf.entity.dto.OutboundDTO;
import com.yf.entity.dto.StockLogQueryDTO;
import com.yf.entity.dto.TransferDTO;
import com.yf.entity.vo.StockLogVO;
import com.yf.util.PageResult;

public interface StockLogService extends IService<StockLog> {
     void addInbound(InboundDTO inboundDTO);
    
    void addOutbound(OutboundDTO outboundDTO);
    
    void transferGoods(TransferDTO transferDTO);
    
    PageResult<StockLogVO> pageQuery(StockLogQueryDTO queryDTO);
}