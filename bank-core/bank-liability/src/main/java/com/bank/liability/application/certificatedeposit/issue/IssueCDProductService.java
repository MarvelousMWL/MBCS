package com.bank.liability.application.certificatedeposit.issue;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositProduct;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositProductRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssueCDProductService {

    private final CertificateDepositProductRepository productRepository;

    @Transactional
    public CertificateDepositProduct issue(IssueCDProductCommand command) {
        if (productRepository.existsByProductCode(command.getProductCode())) {
            throw new BusinessException("产品编码已存在");
        }
        if (command.getIssueEndDate().isBefore(command.getIssueStartDate())) {
            throw new BusinessException("发行结束日期不能早于开始日期");
        }

        CertificateDepositProduct product = new CertificateDepositProduct();
        product.setProductCode(command.getProductCode());
        product.setProductName(command.getProductName());
        product.setTotalQuota(command.getTotalQuota());
        product.setRemainingQuota(command.getTotalQuota());
        product.setMinSubscriptionAmount(command.getMinSubscriptionAmount());
        product.setMaxSubscriptionAmountPerCustomer(command.getMaxSubscriptionAmountPerCustomer());
        product.setTermMonths(command.getTermMonths());
        product.setInterestRate(command.getInterestRate());
        product.setIssueStartDate(command.getIssueStartDate());
        product.setIssueEndDate(command.getIssueEndDate());
        product.setMaturityDate(command.getIssueStartDate().plusMonths(command.getTermMonths()));
        product.setStatus("ACTIVE");
        product.setCustomerType(command.getCustomerType() != null ? command.getCustomerType() : "CORPORATE");
        product.setCurrency(command.getCurrency() != null ? command.getCurrency() : "156");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);
        return product;
    }
}
