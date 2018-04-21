package com.mjoys.service;

import com.mjoys.po.Account;

import java.util.List;

public interface IAccountService {
    List<Account> findByUserId(String userId);
}
