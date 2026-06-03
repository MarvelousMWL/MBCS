package com.bank.liability.domain.product.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("kdpf_qudokz")
public class ProductChannelControl {
    private Long id; private String productCode; private String channelCode; private String channelName;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}