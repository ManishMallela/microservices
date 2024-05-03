package com.nlmp.accounts.mapper;

import com.nlmp.accounts.dto.AccountsDto;
import com.nlmp.accounts.entity.Accounts;

public class AccountsMapper {

    public static AccountsDto mapToAccountsDto(Accounts accounts,
                                               AccountsDto accountsDto){
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setAccountNumber(accounts.getAccountNumber());
        accountsDto.setBranchAddress(accounts.getBranchAddress());
        return accountsDto;
    }

    public static  Accounts mapToAccounts(AccountsDto accountsDto,Accounts accounts){
        accounts.setAccountType(accountsDto.getAccountType());
        accounts.setAccountNumber(accountsDto.getAccountNumber());
        accounts.setBranchAddress(accountsDto.getBranchAddress());
        return accounts;
    }

}
