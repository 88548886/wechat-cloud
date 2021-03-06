package com.mjoys.service.impl;

import com.mjoys.dao.AccountRepository;
import com.mjoys.dao.TaskRepository;
import com.mjoys.po.Account;
import com.mjoys.po.Task;
import com.mjoys.service.IAccountService;
import com.mjoys.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public List<Account> findByUserIdAndBussinessId(String userId, int bussinessId) {
        return accountRepository.findByUserIdAndBussinessId(userId, bussinessId);
    }

    @Override
    public List<Account> findByUserId(String userId) {
        return accountRepository.findByUserIdAndStatus(userId, 1);
    }
}
