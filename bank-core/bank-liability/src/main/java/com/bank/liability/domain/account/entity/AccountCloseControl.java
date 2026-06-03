package com.bank.liability.domain.account.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;import java.time.LocalDateTime;
@Data @TableName("kdaf_xiohkz")
public class AccountCloseControl {
    private Long id;
    private String accountNo;
    private String closeCtrlMethod;
    private String customCtrlMethod;
    private String earlyCloseCtrl;
    private String penaltyType;
    private String signCheckMethod;
    private String arrearsCheckMethod;
    private String closeFundTo;
    private String transferAccountNature;
    private String source;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}