import os, sys, glob

base = r'E:\Agent\MyProject\MBCS\bank-core\bank-liability'

def write_file(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f"Written: {path}")

# ============= TransferCDCommand.java =============
write_file(os.path.join(base, 'src', 'main', 'java', 'com', 'bank', 'liability', 'application', 'certificatedeposit', 'transfer', 'TransferCDCommand.java'),
'''package com.bank.liability.application.certificatedeposit.transfer;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransferCDCommand {
    @NotBlank(message = "\u5b58\u5355\u8d26\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    private String cdAccountNo;

    @NotBlank(message = "\u8f6c\u51fa\u4eba\u8d26\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    private String transferorAccountNo;

    @NotBlank(message = "\u8f6c\u5165\u4eba\u8d26\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    private String transfereeAccountNo;

    @NotNull(message = "\u8f6c\u8ba9\u4ef7\u683c\u4e0d\u80fd\u4e3a\u7a7a")
    @Positive(message = "\u8f6c\u8ba9\u4ef7\u683c\u5fc5\u987b\u5927\u4e8e0")
    private BigDecimal transferPrice;

    @NotBlank(message = "\u5b9a\u4ef7\u65b9\u5f0f\u4e0d\u80fd\u4e3a\u7a7a")
    private String pricingType;

    private BigDecimal handlingFee;

    private String operatorNo;
}
''')

# ============= CertificateDepositTransferRecord.java =============
write_file(os.path.join(base, 'src', 'main', 'java', 'com', 'bank', 'liability', 'domain', 'certificatedeposit', 'entity', 'CertificateDepositTransferRecord.java'),
'''package com.bank.liability.domain.certificatedeposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("cd_transfer_record")
public class CertificateDepositTransferRecord {
    private Long id;
    private String cdAccountNo;
    private String transferorAccountNo;
    private String transfereeAccountNo;
    private BigDecimal transferPrice;
    private String pricingType;
    private BigDecimal handlingFee;
    private String status;
    private LocalDate transferDate;
    private String operatorNo;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
''')

# ============= CertificateDepositTransferRecordRepository.java =============
write_file(os.path.join(base, 'src', 'main', 'java', 'com', 'bank', 'liability', 'domain', 'certificatedeposit', 'repository', 'CertificateDepositTransferRecordRepository.java'),
'''package com.bank.liability.domain.certificatedeposit.repository;

import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositTransferRecord;
import java.util.List;
import java.util.Optional;

public interface CertificateDepositTransferRecordRepository {
    void save(CertificateDepositTransferRecord record);
    void update(CertificateDepositTransferRecord record);
    Optional<CertificateDepositTransferRecord> findById(Long id);
    List<CertificateDepositTransferRecord> findByCdAccountNo(String cdAccountNo);
}
''')

# ============= CertificateDepositTransferRecordMapper.java =============
write_file(os.path.join(base, 'src', 'main', 'java', 'com', 'bank', 'liability', 'infrastructure', 'persistence', 'CertificateDepositTransferRecordMapper.java'),
'''package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositTransferRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CertificateDepositTransferRecordMapper extends BaseMapper<CertificateDepositTransferRecord> {
}
''')

# ============= CertificateDepositTransferRecordRepositoryImpl.java =============
write_file(os.path.join(base, 'src', 'main', 'java', 'com', 'bank', 'liability', 'infrastructure', 'persistence', 'CertificateDepositTransferRecordRepositoryImpl.java'),
'''package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositTransferRecord;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositTransferRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CertificateDepositTransferRecordRepositoryImpl implements CertificateDepositTransferRecordRepository {

    private final CertificateDepositTransferRecordMapper mapper;

    @Override
    public void save(CertificateDepositTransferRecord record) {
        mapper.insert(record);
    }

    @Override
    public void update(CertificateDepositTransferRecord record) {
        LambdaUpdateWrapper<CertificateDepositTransferRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CertificateDepositTransferRecord::getId, record.getId())
               .set(CertificateDepositTransferRecord::getStatus, record.getStatus())
               .set(CertificateDepositTransferRecord::getVersion, record.getVersion() + 1)
               .set(CertificateDepositTransferRecord::getUpdatedAt, record.getUpdatedAt());
        mapper.update(null, wrapper);
    }

    @Override
    public Optional<CertificateDepositTransferRecord> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public List<CertificateDepositTransferRecord> findByCdAccountNo(String cdAccountNo) {
        LambdaQueryWrapper<CertificateDepositTransferRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositTransferRecord::getCdAccountNo, cdAccountNo);
        return mapper.selectList(wrapper);
    }
}
''')

# ============= CertificateDepositAccount.java =============
write_file(os.path.join(base, 'src', 'main', 'java', 'com', 'bank', 'liability', 'domain', 'certificatedeposit', 'entity', 'CertificateDepositAccount.java'),
'''package com.bank.liability.domain.certificatedeposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("cd_account")
public class CertificateDepositAccount {
    private Long id;
    private String cdAccountNo;
    private String customerAccountNo;
    private String productCode;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal totalAmount;
    private String status;
    private LocalDate subscribeDate;
    private LocalDate maturityDate;
    private LocalDate redeemDate;
    private String interestTransferAccount;
    private BigDecimal interestRate;
    private Integer termMonths;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    public boolean isRedeemed() {
        return "REDEEMED".equals(this.status);
    }

    public boolean isFrozen() {
        return "FROZEN".equals(this.status);
    }

    public boolean isPledged() {
        return "PLEDGED".equals(this.status);
    }

    public boolean isLost() {
        return "LOST".equals(this.status);
    }

    public boolean isAbnormalStatus() {
        return isFrozen() || isPledged() || isLost();
    }

    public BigDecimal calculateInterest() {
        return principal.multiply(interestRate).multiply(BigDecimal.valueOf(termMonths))
            .divide(BigDecimal.valueOf(1200), 2, BigDecimal.ROUND_HALF_UP);
    }
}
''')

# ============= CertificateDepositController.java =============
write_file(os.path.join(base, 'src', 'main', 'java', 'com', 'bank', 'liability', 'api', 'CertificateDepositController.java'),
'''package com.bank.liability.api;

import com.bank.common.result.Result;
import com.bank.liability.application.certificatedeposit.issue.IssueCDProductCommand;
import com.bank.liability.application.certificatedeposit.issue.IssueCDProductService;
import com.bank.liability.application.certificatedeposit.subscription.SubscribeCDCommand;
import com.bank.liability.application.certificatedeposit.subscription.SubscribeCDService;
import com.bank.liability.application.certificatedeposit.redemption.RedeemCDCommand;
import com.bank.liability.application.certificatedeposit.redemption.RedeemCDService;
import com.bank.liability.application.certificatedeposit.transfer.TransferCDCommand;
import com.bank.liability.application.certificatedeposit.transfer.TransferCDService;
import com.bank.liability.application.certificatedeposit.query.CDQueryService;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositProduct;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/liability/cd")
@RequiredArgsConstructor
public class CertificateDepositController {

    private final IssueCDProductService issueCDProductService;
    private final SubscribeCDService subscribeCDService;
    private final RedeemCDService redeemCDService;
    private final TransferCDService transferCDService;
    private final CDQueryService cdQueryService;

    @PostMapping("/product")
    public Result<CertificateDepositProduct> issueProduct(@Valid @RequestBody IssueCDProductCommand command) {
        return Result.success(issueCDProductService.issue(command));
    }

    @PostMapping("/subscribe")
    public Result<CertificateDepositAccount> subscribe(@Valid @RequestBody SubscribeCDCommand command,
                                                        @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        command.setOperatorNo(operatorNo);
        return Result.success(subscribeCDService.subscribe(command));
    }

    @PostMapping("/redeem")
    public Result<CertificateDepositAccount> redeem(@Valid @RequestBody RedeemCDCommand command,
                                                     @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        command.setOperatorNo(operatorNo);
        return Result.success(redeemCDService.redeem(command));
    }

    @PostMapping("/transfer")
    public Result<CertificateDepositAccount> transfer(@Valid @RequestBody TransferCDCommand command,
                                                       @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        command.setOperatorNo(operatorNo);
        return Result.success(transferCDService.transfer(command));
    }

    @GetMapping("/products")
    public Result<List<CertificateDepositProduct>> listProducts() {
        return Result.success(cdQueryService.listAllProducts());
    }

    @GetMapping("/product/{productCode}")
    public Result<CertificateDepositProduct> getProduct(@PathVariable String productCode) {
        return Result.success(cdQueryService.findProductByCode(productCode));
    }

    @GetMapping("/accounts")
    public Result<List<CertificateDepositAccount>> listAccounts(
            @RequestParam(required = false) String customerAccountNo,
            @RequestParam(required = false) String productCode) {
        if (customerAccountNo != null) {
            return Result.success(cdQueryService.listAccountsByCustomer(customerAccountNo));
        }
        if (productCode != null) {
            return Result.success(cdQueryService.listAccountsByProduct(productCode));
        }
        return Result.success(cdQueryService.listAllAccounts());
    }

    @GetMapping("/account/{cdAccountNo}")
    public Result<CertificateDepositAccount> getAccount(@PathVariable String cdAccountNo) {
        return Result.success(cdQueryService.findAccountByNo(cdAccountNo));
    }
}
''')

# ============= CertificateDepositAccountRepositoryImpl.java =============
write_file(os.path.join(base, 'src', 'main', 'java', 'com', 'bank', 'liability', 'infrastructure', 'persistence', 'CertificateDepositAccountRepositoryImpl.java'),
'''package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CertificateDepositAccountRepositoryImpl implements CertificateDepositAccountRepository {

    private final CertificateDepositAccountMapper mapper;

    @Override
    public void save(CertificateDepositAccount account) {
        mapper.insert(account);
    }

    @Override
    public void update(CertificateDepositAccount account) {
        LambdaUpdateWrapper<CertificateDepositAccount> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CertificateDepositAccount::getCdAccountNo, account.getCdAccountNo())
               .set(CertificateDepositAccount::getStatus, account.getStatus())
               .set(CertificateDepositAccount::getCustomerAccountNo, account.getCustomerAccountNo())
               .set(CertificateDepositAccount::getInterestTransferAccount, account.getInterestTransferAccount())
               .set(CertificateDepositAccount::getInterest, account.getInterest())
               .set(CertificateDepositAccount::getTotalAmount, account.getTotalAmount())
               .set(CertificateDepositAccount::getRedeemDate, account.getRedeemDate())
               .set(CertificateDepositAccount::getUpdatedAt, account.getUpdatedAt())
               .set(CertificateDepositAccount::getVersion, account.getVersion() + 1);
        mapper.update(null, wrapper);
    }

    @Override
    public Optional<CertificateDepositAccount> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public Optional<CertificateDepositAccount> findByCdAccountNo(String cdAccountNo) {
        LambdaQueryWrapper<CertificateDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositAccount::getCdAccountNo, cdAccountNo);
        return Optional.ofNullable(mapper.selectOne(wrapper));
    }

    @Override
    public List<CertificateDepositAccount> findByCustomerAccountNo(String customerAccountNo) {
        LambdaQueryWrapper<CertificateDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositAccount::getCustomerAccountNo, customerAccountNo);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<CertificateDepositAccount> findByProductCode(String productCode) {
        LambdaQueryWrapper<CertificateDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositAccount::getProductCode, productCode);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<CertificateDepositAccount> findAll() {
        return mapper.selectList(null);
    }

    @Override
    public boolean existsByCdAccountNo(String cdAccountNo) {
        LambdaQueryWrapper<CertificateDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositAccount::getCdAccountNo, cdAccountNo);
        return mapper.exists(wrapper);
    }

    @Override
    public Long countByProductCode(String productCode) {
        LambdaQueryWrapper<CertificateDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositAccount::getProductCode, productCode);
        return mapper.selectCount(wrapper);
    }
}
''')

# ============= TransferCDService.java =============
write_file(os.path.join(base, 'src', 'main', 'java', 'com', 'bank', 'liability', 'application', 'certificatedeposit', 'transfer', 'TransferCDService.java'),
'''package com.bank.liability.application.certificatedeposit.transfer;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositTransferRecord;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositAccountRepository;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositTransferRecordRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferCDService {

    private final CertificateDepositAccountRepository accountRepository;
    private final CertificateDepositTransferRecordRepository transferRecordRepository;

    @Transactional
    public CertificateDepositAccount transfer(TransferCDCommand command) {
        CertificateDepositAccount account = accountRepository
                .findByCdAccountNo(command.getCdAccountNo())
                .orElseThrow(() -> new BusinessException("\u5b58\u5355\u8d26\u6237\u4e0d\u5b58\u5728"));

        if (!account.isActive()) {
            throw new BusinessException("\u5b58\u5355\u72b6\u6001\u4e0d\u5141\u8bb8\u8f6c\u8ba9");
        }

        if (account.isAbnormalStatus()) {
            throw new BusinessException("\u5b58\u5355\u8d26\u6237\u72b6\u6001\u5f02\u5e38\uff08\u51bb\u7ed3/\u6302\u5931/\u8d28\u62bc\uff09\uff0c\u4e0d\u5141\u8bb8\u8f6c\u8ba9");
        }

        if (!account.getCustomerAccountNo().equals(command.getTransferorAccountNo())) {
            throw new BusinessException("\u8f6c\u51fa\u4eba\u8d26\u53f7\u4e0e\u5b58\u5355\u6301\u6709\u4eba\u4e0d\u5339\u914d");
        }

        if (command.getTransferorAccountNo().equals(command.getTransfereeAccountNo())) {
            throw new BusinessException("\u8f6c\u51fa\u4eba\u4e0e\u8f6c\u5165\u4eba\u4e0d\u80fd\u76f8\u540c");
        }

        if (!"SYSTEM".equals(command.getPricingType()) && !"CUSTOMER".equals(command.getPricingType())) {
            throw new BusinessException("\u5b9a\u4ef7\u65b9\u5f0f\u65e0\u6548\uff0c\u5fc5\u987b\u4e3aSYSTEM\u6216CUSTOMER");
        }

        if ("SYSTEM".equals(command.getPricingType())) {
            BigDecimal calculatedPrice = account.getPrincipal().add(account.calculateInterest());
            if (command.getTransferPrice().compareTo(calculatedPrice) != 0) {
                throw new BusinessException("\u7cfb\u7edf\u5b9a\u4ef7\u6a21\u5f0f\u4e0b\u8f6c\u8ba9\u4ef7\u683c\u5fc5\u987b\u4e3a " + calculatedPrice);
            }
        }

        account.setCustomerAccountNo(command.getTransfereeAccountNo());
        account.setInterestTransferAccount(command.getTransfereeAccountNo());
        account.setUpdatedAt(LocalDateTime.now());

        CertificateDepositTransferRecord record = new CertificateDepositTransferRecord();
        record.setCdAccountNo(command.getCdAccountNo());
        record.setTransferorAccountNo(command.getTransferorAccountNo());
        record.setTransfereeAccountNo(command.getTransfereeAccountNo());
        record.setTransferPrice(command.getTransferPrice());
        record.setPricingType(command.getPricingType());
        record.setHandlingFee(command.getHandlingFee() != null ? command.getHandlingFee() : BigDecimal.ZERO);
        record.setStatus("TRANSFERRED");
        record.setTransferDate(LocalDate.now());
        record.setOperatorNo(command.getOperatorNo());
        record.setVersion(0);
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());

        accountRepository.update(account);
        transferRecordRepository.save(record);

        return account;
    }
}
''')

print("All main source files written successfully")
