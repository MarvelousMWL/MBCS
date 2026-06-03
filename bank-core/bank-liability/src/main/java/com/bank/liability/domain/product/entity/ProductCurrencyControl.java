package com.bank.liability.domain.product.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("kdpf_bizhkz")
public class ProductCurrencyControl {
    private Long id; private String productCode; private String currencyCode; private String currencyName;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}