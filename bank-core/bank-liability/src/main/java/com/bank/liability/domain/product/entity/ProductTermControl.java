package com.bank.liability.domain.product.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("kdpf_cunqikz")
public class ProductTermControl {
    private Long id; private String productCode;
    private String termCode; private String termName;
    private Integer termDays;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}