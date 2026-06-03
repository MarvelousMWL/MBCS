package com.bank.liability.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("kdpf_cpdxkz")
public class ProductControl {
    private Long id;
    private String productCode;
    private String controlObject;
    private String currencyRule;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
