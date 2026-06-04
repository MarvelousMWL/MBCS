package com.bank.liability.application.certificatedeposit.subscription;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositProduct;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositAccountRepository;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositProductRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscribeCDService {

    private final CertificateDepositProductRepository productRepository;
    private final CertificateDepositAccountRepository accountRepository;

    @Transactional
    public CertificateDepositAccount subscribe(SubscribeCDCommand command) {
        CertificateDepositProduct product = productRepository
                .findByProductCode(command.getProductCode())
                .orElseThrow(() -> new BusinessException("产品不存在"));

        if (!"ACTIVE".equals(product.getStatus())) {
            throw new BusinessException("产品状态不允许认购");
        }
        if (!product.isInIssuePeriod()) {
            throw new BusinessException("不在发行期内");
        }
        if (!product.hasSufficientQuota(command.getAmount())) {
            throw new BusinessException("产品额度不足");
        }
        if (command.getAmount().compareTo(product.getMinSubscriptionAmount()) < 0) {
            throw new BusinessException("认购金额低于最低认购金额: " + product.getMinSubscriptionAmount());
        }
        if (product.getMaxSubscriptionAmountPerCustomer() != null
                && command.getAmount().compareTo(product.getMaxSubscriptionAmountPerCustomer()) > 0) {
            throw new BusinessException("认购金额超过单人限额: " + product.getMaxSubscriptionAmountPerCustomer());
        }

        String interestAccount = command.getInterestTransferAccount() != null
                ? command.getInterestTransferAccount() : command.getCustomerAccountNo();

        CertificateDepositAccount account = new CertificateDepositAccount();
        account.setCdAccountNo("CD" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase());
        account.setCustomerAccountNo(command.getCustomerAccountNo());
        account.setProductCode(command.getProductCode());
        account.setPrincipal(command.getAmount());
        account.setInterest(BigDecimal.ZERO);
        account.setTotalAmount(command.getAmount());
        account.setStatus("ACTIVE");
        account.setSubscribeDate(LocalDate.now());
        account.setMaturityDate(LocalDate.now().plusMonths(product.getTermMonths()));
        account.setInterestTransferAccount(interestAccount);
        account.setInterestRate(product.getInterestRate());
        account.setTermMonths(product.getTermMonths());
        account.setVersion(0);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        product.deductQuota(command.getAmount());
        productRepository.update(product);
        accountRepository.save(account);

        return account;
    }
}
