package com.sg.dao;

import com.sg.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Integer> {

    @Override
    Optional<Account> findById(Integer integer);

}