package com.mjoys.dao;

import com.mjoys.po.Account;
import com.mjoys.po.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findAllByUserId(@Param(value = "userId") String userId);
}
