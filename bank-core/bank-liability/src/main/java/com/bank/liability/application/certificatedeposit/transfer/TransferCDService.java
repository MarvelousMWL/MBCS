package com.bank.liability.application.certificatedeposit.transfer;

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
                .orElseThrow(() -> new BusinessException("存单账户不存在"));

        if (!account.isActive()) {
            throw new BusinessException("存单状态不允许转让");
        }

        if (account.isAbnormalStatus()) {
            throw new BusinessException("存单账户状态异常（冻结/挂失/质押），不允许转让");
        }

        if (!account.getCustomerAccountNo().equals(command.getTransferorAccountNo())) {
            throw new BusinessException("转出人账号与存单持有人不匹配");
        }

        if (command.getTransferorAccountNo().equals(command.getTransfereeAccountNo())) {
            throw new BusinessException("转出人与转入人不能相同");
        }

        if (!"SYSTEM".equals(command.getPricingType()) && !"CUSTOMER".equals(command.getPricingType())) {
            throw new BusinessException("定价方式无效，必须为SYSTEM或CUSTOMER");
        }

        if ("SYSTEM".equals(command.getPricingType())) {
            BigDecimal calculatedPrice = account.getPrincipal().add(account.calculateInterest());
            if (command.getTransferPrice().compareTo(calculatedPrice) != 0) {
                throw new BusinessException("系统定价模式下转让价格必须为 " + calculatedPrice);
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
