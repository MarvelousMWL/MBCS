package com.bank.liability.application.certificatedeposit.redemption;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositAccountRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RedeemCDService {

    private final CertificateDepositAccountRepository accountRepository;

    @Transactional
    public CertificateDepositAccount redeem(RedeemCDCommand command) {
        CertificateDepositAccount account = accountRepository
                .findByCdAccountNo(command.getCdAccountNo())
                .orElseThrow(() -> new BusinessException("存单账户不存在"));

        if (!account.isActive()) {
            throw new BusinessException("存单状态不允许兑付");
        }

        BigDecimal interestAmount = account.calculateInterest();
        account.setInterest(interestAmount);
        account.setTotalAmount(account.getPrincipal().add(interestAmount));
        account.setStatus("REDEEMED");
        account.setRedeemDate(LocalDate.now());
        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.update(account);
        return account;
    }
}
