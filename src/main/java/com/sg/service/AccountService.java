package com.sg.service;

import com.sg.dao.AccountRepository;

import com.sg.exception.BadRequestException;
import com.sg.exception.NotAllowedException;
import com.sg.exception.NotFoundException;
import com.sg.model.Account;
import com.sg.utils.Operation;
import com.sg.utils.Status;
import com.sg.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AccountService {

    public static final String ACCOUNT_NOT_FOUND = "ACCOUNT NOT FOUND";
    public static final String INVALID_AMOUNT = "INVALID AMOUNT";
    public static final String NOT_ENOUGH_FUNDS = "NOT ENOUGH FUNDS";
    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountRepository accountRepository;

    public Account createAccount(Account account){
        return accountRepository.save(account);
    }

    public void deposit(int id, float amount) {
        Account account = accountRepository.findById(id).get();
        if(account != null){
            if(amount>0){
                float updatedBalance = account.getBalance() + amount;
                Transaction transaction = new Transaction(account, new Date(), Operation.DEPOSIT, amount, updatedBalance);
                account.setBalance(updatedBalance);
                account.addTransaction(transaction);
                accountRepository.save(account);
            }
            else
                throw new BadRequestException(INVALID_AMOUNT);
        }
        else
            throw new NotFoundException(ACCOUNT_NOT_FOUND);
    }

    public void withdraw(int id, float amount, Operation type) {
        Account account = accountRepository.findById(id).get();
        if(account != null) {
            if (account.getStatus()!= Status.ACTIVE){
                throw new NotAllowedException(String.format("%s ACCOUNT", account.getStatus()));
            }

            float amountToRetrieve = amount;
            if (type.equals(Operation.WITHDRAWAL_ALL)){
                amountToRetrieve = account.getBalance();
            }

            if(amountToRetrieve <= 0){
                throw new BadRequestException(INVALID_AMOUNT);
            }


            if(amountToRetrieve > account.getBalance()){
                throw new BadRequestException(NOT_ENOUGH_FUNDS);
            }


            if (amountToRetrieve <= account.getBalance()) {
                transactionService.addTransaction(account, Operation.WITHDRAWAL, amount);
                account.setBalance(account.getBalance() - amount);
                account.setLastUpdate(new Date());
                accountRepository.save(account);
            }
        }
        else {
            throw  new BadRequestException(ACCOUNT_NOT_FOUND);
        }
    }

    public Account history(int id) {
        Optional<Account> account  = accountRepository.findById(id);
        if(account.isPresent())
            return account.get();
        else
         throw new NotFoundException(ACCOUNT_NOT_FOUND);
    }

}

