package com.bank.liability.domain.product.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("kdpf_jigokz")
public class ProductInstitutionControl {
    private Long id; private String productCode; private String institutionType; private String institutionCode;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}