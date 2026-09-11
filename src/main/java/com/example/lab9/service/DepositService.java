package com.example.lab9.service;

import com.example.lab9.model.Account;
import com.example.lab9.model.DepositTransaction;
import com.example.lab9.repository.AccountRepository;
import com.example.lab9.repository.DepositRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepositService {

    private final AccountRepository accountRepository;
    private final DepositRepository depositRepository;

    public DepositService(AccountRepository accountRepository, DepositRepository depositRepository) {
        this.accountRepository = accountRepository;
        this.depositRepository = depositRepository;
    }

    @Transactional
    public void deposit(Long accountId, Double amount) {
        // 1. ค้นหา Account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));

        // 2. เพิ่ม balance แล้วบันทึก Account
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        // 3. สร้าง DepositTransaction ผูกกับ Account แล้วบันทึก
        DepositTransaction tx = new DepositTransaction();
        tx.setAmount(amount);
        tx.setAccount(account);
        depositRepository.save(tx);

        // ข้อ 12: จงใจโยน Error หลัง save ครบทั้งสองอย่าง เพื่อทดสอบ ROLLBACK
        // throw new RuntimeException("Test Rollback");
    }
}
