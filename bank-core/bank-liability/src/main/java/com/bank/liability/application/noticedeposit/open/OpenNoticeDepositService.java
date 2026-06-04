package com.bank.liability.application.noticedeposit.open;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.noticedeposit.entity.NoticeDepositAccount;
import com.bank.liability.domain.noticedeposit.repository.NoticeDepositAccountRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OpenNoticeDepositService {

    private static final BigDecimal MIN_AMOUNT = BigDecimal.valueOf(500000);

    private final NoticeDepositAccountRepository accountRepository;

    @Transactional
    public NoticeDepositAccount open(OpenNoticeDepositCommand command) {
        if (!"DAY1".equals(command.getNoticeType()) && !"DAY7".equals(command.getNoticeType())) {
            throw new BusinessException("通知类型无效，必须为DAY1或DAY7");
        }
        if (command.getAmount().compareTo(MIN_AMOUNT) < 0) {
            throw new BusinessException("通知存款最低起存金额为50万元");
        }

        NoticeDepositAccount account = new NoticeDepositAccount();
        account.setNoticeDepositAccountNo("ND" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase());
        account.setCustomerAccountNo(command.getCustomerAccountNo());
        account.setCurrentAccountNo(command.getCurrentAccountNo());
        account.setPrincipal(command.getAmount());
        account.setInterest(BigDecimal.ZERO);
        account.setTotalAmount(command.getAmount());
        account.setNoticeType(command.getNoticeType());
        account.setStatus("ACTIVE");
        account.setOpenDate(LocalDate.now());
        account.setInterestRate(command.getInterestRate());
        account.setCurrentInterestRate(command.getCurrentInterestRate() != null
                ? command.getCurrentInterestRate() : BigDecimal.valueOf(0.35));
        account.setVersion(0);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(account);
        return account;
    }
}