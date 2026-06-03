package com.bank.liability.domain.product.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("kdpf_kaihkz")
public class ProductOpenControl {
    private Long id; private String productCode;
    private String customerAccountGenRule; private String restrictFlag;
    private String restrictType; private Integer priority;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}