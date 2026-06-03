package com.bank.liability.domain.product.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("kdpf_daoqkz")
public class ProductMaturityControl {
    private Long id; private String productCode;
    private String processMethod; private String renewFlag;
    private Integer renewTimes;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}