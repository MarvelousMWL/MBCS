package com.bank.liability.domain.product.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data @TableName("kdpf_xtaizy")
public class ProductFormTransfer {
    private Long id; private String productCode;
    private String transferType; private String startDateMethod;
    private Integer transferCycle; private String interestFlag;
    private BigDecimal limitAmount; private String dormantToNormalFlag;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}