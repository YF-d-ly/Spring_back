package com.yf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.constant.stockConstant;
import com.yf.entity.Goods;
import com.yf.entity.StockLog;
import com.yf.entity.Warehouse;
import com.yf.entity.dto.InboundDTO;
import com.yf.entity.dto.OutboundDTO;
import com.yf.entity.dto.StockLogQueryDTO;
import com.yf.entity.dto.TransferDTO;
import com.yf.entity.vo.StockLogVO;
import com.yf.entity.vo.TransferVO;
import com.yf.mapper.StockLogCustomMapper;
import com.yf.mapper.StockLogMapper;
import com.yf.service.GoodsService;
import com.yf.service.StockLogService;
import com.yf.service.WarehouseService;
import com.yf.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StockLogServiceImpl extends ServiceImpl<StockLogMapper, StockLog> implements StockLogService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private WarehouseService  warehouseService;
    
    @Autowired
    private StockLogCustomMapper stockLogCustomMapper;

    @Override
    public List<TransferVO> getTransferLog(){
        // 使用自定义Mapper查询调拨日志
        return stockLogCustomMapper.selectTransferLogs();
    }


    @Override
    public void addInbound(InboundDTO inboundDTO) {
        // 构建库存日志对象
        StockLog stockLog = StockLog.builder()
                .goodsId(inboundDTO.getGoodsId())
                .warehouseId(inboundDTO.getWarehouseId())
                .type(stockConstant.inbound)
                .num(inboundDTO.getNum())
                .operator(inboundDTO.getOperator())
                .createTime(LocalDateTime.now())
                .transferId(inboundDTO.getTransferId())
                .remark(inboundDTO.getRemark())
                .build();

        // 保存入库记录到数据库
        this.save(stockLog);
    }

    @Override
    public void addOutbound(OutboundDTO outboundDTO) {
        // 构建库存日志对象
        StockLog stockLog = StockLog.builder()
                .goodsId(outboundDTO.getGoodsId())
                .warehouseId(outboundDTO.getWarehouseId())
                .type(stockConstant.outbound)
                .num(outboundDTO.getNum())
                .operator(outboundDTO.getOperator())
                .createTime(LocalDateTime.now())
                .remark(outboundDTO.getRemark())
                .transferId(outboundDTO.getTransferId())
                .build();

        // 保存出库记录到数据库
        this.save(stockLog);
    }

    @Override
    public void transferGoods(TransferDTO transferDTO) {
        // 生成唯一的调货ID，如果未提供的话
        if (transferDTO.getTransferId() == null || transferDTO.getTransferId().isEmpty()) {
            transferDTO.setTransferId("TRANSFER_" + System.currentTimeMillis());
        }

        // 创建出库记录（从源仓库出库）
        StockLog outboundLog = StockLog.builder()
                .goodsId(transferDTO.getGoodsId())
                .warehouseId(transferDTO.getSourceWarehouseId())
                .type(stockConstant.outbound) // 出库类型
                .num(transferDTO.getNum())
                .operator(transferDTO.getOperator())
                .createTime(LocalDateTime.now())
                .transferId(transferDTO.getTransferId()) // 关联到同一次调货
                .remark(transferDTO.getRemark() != null ? transferDTO.getRemark() + "-" + stockConstant.TRANSFER_OUTBOUND_REMARK : stockConstant.TRANSFER_OUTBOUND_REMARK)
                .build();

        // 创建入库记录（到目标仓库入库）
        StockLog inboundLog = StockLog.builder()
                .goodsId(transferDTO.getGoodsId())
                .warehouseId(transferDTO.getTargetWarehouseId())
                .type(stockConstant.inbound) // 入库类型
                .num(transferDTO.getNum())
                .operator(transferDTO.getOperator())
                .createTime(LocalDateTime.now())
                .transferId(transferDTO.getTransferId()) // 关联到同一次调货
                .remark(transferDTO.getRemark() != null ? transferDTO.getRemark() + "-" + stockConstant.TRANSFER_INBOUND_REMARK : stockConstant.TRANSFER_INBOUND_REMARK)
                .build();

        // 保存出库和入库记录到数据库
        this.save(outboundLog);
        this.save(inboundLog);

        // 更新货品库存：从源仓库减少库存，目标仓库增加库存
        goodsService.updateStockForTransfer(transferDTO.getGoodsId(), 
                transferDTO.getSourceWarehouseId(), 
                transferDTO.getTargetWarehouseId(), 
                transferDTO.getNum());
    }



    @Override
    public PageResult<StockLogVO> pageQuery(StockLogQueryDTO queryDTO) {
            // 创建分页对象
            Page<StockLog> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());

            // 构建查询条件
            LambdaQueryWrapper<StockLog> queryWrapper = new LambdaQueryWrapper<>();


            // 根据货品名称查询
            if (queryDTO.getGoodsName() != null && !queryDTO.getGoodsName().isEmpty()) {
                // 根据货物名称查询货物信息，获取货物ID列表
                LambdaQueryWrapper<Goods> goodsQueryWrapper = new LambdaQueryWrapper<>();
                goodsQueryWrapper.like(Goods::getName, queryDTO.getGoodsName());
                List<Goods> goodsList = goodsService.list(goodsQueryWrapper);

                if (!goodsList.isEmpty()) {
                    // 提取货物ID列表
                    List<String> goodsIds = goodsList.stream()
                            .map(Goods::getId)
                            .collect(Collectors.toList());
                    // 在库存日志中查询对应货物ID的记录
                    queryWrapper.in(StockLog::getGoodsId, goodsIds);
                } else {
                    // 如果没有找到匹配的货物，设置一个不存在的ID以返回空结果
                    queryWrapper.eq(StockLog::getGoodsId, "-1");
                }
            }

            // 根据仓库ID查询
            if (queryDTO.getWarehouseId() != null && !queryDTO.getWarehouseId().isEmpty()) {
                queryWrapper.eq(StockLog::getWarehouseId, queryDTO.getWarehouseId());
            }


            // 根据操作人查询
            if (queryDTO.getOperator() != null && !queryDTO.getOperator().isEmpty()) {
                queryWrapper.like(StockLog::getOperator, queryDTO.getOperator());
            }

            // 根据创建时间范围查询
            if (queryDTO.getStartTime() != null) {
                queryWrapper.ge(StockLog::getCreateTime, queryDTO.getStartTime());
            }

            if (queryDTO.getEndTime() != null) {
                queryWrapper.le(StockLog::getCreateTime, queryDTO.getEndTime());
            }

            // 执行分页查询
            IPage<StockLog> result = this.page(page, queryWrapper);

            // 获取所有货品ID，批量查询货品名称
            List<String> goodsIds = result.getRecords().stream()
                    .map(StockLog::getGoodsId)
                    .distinct()
                    .collect(Collectors.toList());

            // 批量查询货品信息
            Map<String, String> goodsNameMap = goodsIds.isEmpty() ? null :
                    goodsService.listByIds(goodsIds).stream()
                            .collect(Collectors.toMap(Goods::getId, Goods::getName));

            // 将StockLog实体列表转换为StockLogVO列表，并填充货品名称
            List<StockLogVO> stockLogVOList = result.getRecords().stream()
                    .map(stockLog -> {
                        StockLogVO stockLogVO = StockLogVO.builder()
                                .id(stockLog.getId())
                                .goodsId(stockLog.getGoodsId())
                                .warehouseId(stockLog.getWarehouseId())
                                .type(stockLog.getType())
                                .num(stockLog.getNum())
                                .operator(stockLog.getOperator())
                                .createTime(stockLog.getCreateTime())
                                .transferId(stockLog.getTransferId())
                                .remark(stockLog.getRemark())
                                .goodsName(goodsNameMap != null ? goodsNameMap.get(stockLog.getGoodsId()) : null)
                                .warehouseName(warehouseService.getById(stockLog.getWarehouseId()).getName())
                                .build();
                        return stockLogVO;
                    })
                    .collect(Collectors.toList());

            // 封装并返回PageResult
            return new PageResult<>(
                    result.getTotal(),           // 总记录数
                    stockLogVOList,              // 当前页数据列表（VO类型）
                    result.getPages(),           // 总页数
                    result.getSize(),            // 每页大小
                    result.getCurrent()          // 当前页码
            );
        }



    }