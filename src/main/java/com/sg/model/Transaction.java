package com.sg.model;

import com.sg.utils.Operation;
import lombok.*;

import javax.persistence.*;

import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transactionId;

    @NonNull
    @ManyToOne
    @JoinColumn(name="accountId", nullable=false)
    private Account account;

    @NonNull
    private Date transactionDate;

    @NonNull
    private Operation operation;

    @NonNull
    private float amount;

    @NonNull
    private float balance;
}
