package com.nlmp.accounts.service;

import com.nlmp.accounts.dto.CustomerDto;

public interface IAccountsService {

    /**
     *
     * @param customerDto
     */
    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobile);

    /**
    * @param customerDto
     */
    boolean updateAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);

}
