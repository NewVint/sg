package com.sg.service;

import com.sg.dao.TransactionRepository;
import com.sg.exception.NotAllowedException;
import com.sg.exception.NotFoundException;
import com.sg.model.Account;
import com.sg.utils.Operation;
import com.sg.utils.Status;
import com.sg.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    public void addTransaction(Account account, Operation operation, float amount){
        if(account!=null){
            Transaction transaction = null;
            if(operation == Operation.DEPOSIT){
                transaction = new Transaction(account, new Date(), operation, amount, amount + account.getBalance());
            }
            else if(operation == Operation.WITHDRAWAL){
                if (account.getStatus()!= Status.ACTIVE){

                    throw new NotAllowedException(String.format("%s ACCOUNT", account.getStatus()));
                }
                transaction = new Transaction(account, new Date(), operation, amount, amount - account.getBalance());
            }
            transactionRepository.save(transaction);
        }
        else
            throw new NotFoundException("ACCOUNT NOT FOUND");
    }
}
