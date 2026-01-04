package com.yf.util;// PageResult.java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageResult<T> {
    private Long total;     // 总记录数
    private List<T> records; // 当前页数据列表
    private Long pages;     // 总页数
    private Long size;      // 每页大小
    private Long current;   // 当前页码

    public PageResult(Long total, List<T> records) {
        this.total = total;
        this.records = records;
    }
    
    /**
     * 创建空的分页结果
     */
    public static <T> PageResult<T> empty(Long page, Long size) {
        return new PageResult<>(0L, List.of(), 0L, size, page);
    }
}