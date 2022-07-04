package com.sg.model;

import com.sg.utils.Status;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int accountId;
    private String accountNumber;
    private String clientName;
    private float balance;
    private Status status;
    private Date createdAt;
    private Date lastUpdate;
    @OneToMany(mappedBy="account", fetch = FetchType.LAZY)
    private List<Transaction> transactions;


    public void addTransaction(Transaction transaction){
        if(transactions == null){
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);
    }

    @Override
    public String toString() {
        return "Account{" +
                "  accountNumber='" + accountNumber + '\'' +
                ", client=" + clientName +
                ", balance=" + balance +
                ", transactions=" + transactions +
                '}';
    }

}


