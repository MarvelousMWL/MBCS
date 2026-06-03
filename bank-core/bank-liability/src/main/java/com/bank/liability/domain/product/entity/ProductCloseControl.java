package com.bank.liability.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data @TableName("kdpf_xiohkz")
public class ProductCloseControl {
    private Long id; private String productCode;
    private String currencyCode;
    private String closeCtrlMethod;
    private String customCtrlMethod;
    private String earlyCloseCtrl;
    private String penaltyType;
    private String signCheckMethod;
    private String arrearsCheckMethod;
    private String closeFundTo;
    private String transferAccountNature;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}
