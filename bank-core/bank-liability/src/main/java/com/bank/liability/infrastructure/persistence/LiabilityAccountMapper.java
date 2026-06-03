package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LiabilityAccountMapper extends BaseMapper<LiabilityAccount> {
    @Select("SELECT * FROM acc_liability_account WHERE customer_account_no = #{customerAccountNo} AND account_type = #{accountType} ORDER BY sub_account_seq DESC LIMIT 1")
    LiabilityAccount findMaxSubAccountSeqByCustomerAccountNoAndAccountType(@Param("customerAccountNo") String customerAccountNo, @Param("accountType") String accountType);
}
