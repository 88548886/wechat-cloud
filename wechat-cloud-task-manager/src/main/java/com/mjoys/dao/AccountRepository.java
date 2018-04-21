package com.mjoys.dao;

import com.mjoys.po.Account;
import com.mjoys.po.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findByUserIdAndBussinessId(@Param(value = "userId") String userId, @Param(value = "bussinessId")
            int bussinessId);

    List<Account> findByUserId(@Param(value = "userId") String userId);
}
