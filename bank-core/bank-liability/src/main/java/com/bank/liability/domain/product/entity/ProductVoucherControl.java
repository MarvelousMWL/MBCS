package com.bank.liability.domain.product.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("kdpf_pngzkz")
public class ProductVoucherControl {
    private Long id; private String productCode; private String voucherType; private String customerAccountType;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}