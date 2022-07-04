package com.sg.service;

import com.sg.SgApplication;
import com.sg.dao.AccountRepository;
import com.sg.dao.TransactionRepository;
import com.sg.exception.BadRequestException;
import com.sg.exception.NotAllowedException;
import com.sg.exception.NotFoundException;
import com.sg.model.Account;
import com.sg.utils.Operation;
import com.sg.utils.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SgApplication.class)
public class AccountTest {

    private Account account1, account2;

    @Autowired
    AccountService accountService;

    @MockBean
    private AccountRepository accountRepositoryMock;

    @MockBean
    TransactionRepository transactionRepository;

    @Before
    public void init(){

        Date date = new Date();
        account1 = Account.builder()
                .accountId(1)
                .accountNumber("30003 01199 11150000182 66")
                .clientName("MM Y A")
                .balance(100)
                .status(Status.BLOCKED)
                .createdAt(date)
                .lastUpdate(date)
                .build();

        account2 = Account.builder()
                .accountId(2)
                .accountNumber("30003 01199 11150000182 66")
                .clientName("Mona Lisa")
                .balance(100)
                .status(Status.ACTIVE)
                .createdAt(date)
                .lastUpdate(date)
                .build();

        when(accountRepositoryMock.findById(1)).thenReturn(Optional.ofNullable(account1));
        when(accountRepositoryMock.findById(2)).thenReturn(Optional.ofNullable(account2));
    }

    @Test
    public void testDepositAmount(){
        float initialBalance = accountService.history(account2.getAccountId()).getBalance();
        float amount = 10;
        accountService.deposit(account2.getAccountId(), amount);
        assertEquals((amount+initialBalance), accountService.history(account2.getAccountId()).getBalance(), 0.0d);
    }

    @Test
    public void testWithdrawal(){
        float initialBalance = accountService.history(account2.getAccountId()).getBalance();
        float amount = 10;
        accountService.withdraw(account2.getAccountId(), 10, Operation.WITHDRAWAL);
        assertEquals((initialBalance-amount), accountService.history(account2.getAccountId()).getBalance(), 0.0d);
    }

    @Test
    public void testGetHistory(){
        assertEquals(accountService.history(account2.getAccountId()).getStatus(),accountService.history(account2.getAccountId()).getStatus());
    }

    @Test(expected = BadRequestException.class)
    public void testWithdrawHighAmount(){
        accountService.withdraw(account2.getAccountId(),1000, Operation.WITHDRAWAL);
    }

    @Test(expected = NotAllowedException.class)
    public void testWithdrawalBlockedAcc(){
        accountService.withdraw(account1.getAccountId(), 10, Operation.WITHDRAWAL);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNonExistAccount(){
        accountService.history(13);
    }

    @Test(expected = BadRequestException.class)
    public void testDepositNegativeAmount(){
        accountService.deposit(account1.getAccountId(), -1000);
    }

}
