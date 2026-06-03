package com.bank.liability.domain.product.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("kdpf_hshkz")
public class ProductAccountingControl {
    private Long id; private String productCode; private String currencyCtrl; private String classifyCtrl; private String termCtrl;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}