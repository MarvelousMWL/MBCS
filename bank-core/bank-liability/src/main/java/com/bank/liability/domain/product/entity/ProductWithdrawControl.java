package com.bank.liability.domain.product.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data @TableName("kdpf_zhiqkz")
public class ProductWithdrawControl {
    private Long id; private String productCode;
    private BigDecimal minBalance; private String withdrawMethod;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}