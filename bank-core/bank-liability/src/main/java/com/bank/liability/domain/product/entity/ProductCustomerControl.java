package com.bank.liability.domain.product.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("kdpf_kehukz")
public class ProductCustomerControl {
    private Long id; private String productCode; private String controlType; private String controlValue;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}