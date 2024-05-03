package com.nlmp.accounts.service.impl;

import com.nlmp.accounts.constants.AccountsConstants;
import com.nlmp.accounts.dto.AccountsDto;
import com.nlmp.accounts.dto.CustomerDto;
import com.nlmp.accounts.entity.Accounts;
import com.nlmp.accounts.entity.Customer;
import com.nlmp.accounts.exception.CustomerAlreadyExistsException;
import com.nlmp.accounts.exception.ResourceNotFoundException;
import com.nlmp.accounts.mapper.AccountsMapper;
import com.nlmp.accounts.mapper.CustomerMapper;
import com.nlmp.accounts.repository.AccountRepository;
import com.nlmp.accounts.repository.CustomerRepository;
import com.nlmp.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;


    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto,new Customer());
        Optional<Customer> optionalCustomer =  customerRepository.findByMobileNumber(customer.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer is already existing with this number:"+customer.getMobileNumber());
        }
        Customer savedCustomer = customerRepository.save(customer);
        accountRepository.save(createNewAccount(savedCustomer));
    }

    @Override
    public CustomerDto fetchAccount(String mobile) {
        Customer customer = customerRepository.findByMobileNumber(mobile).orElseThrow(()-> new ResourceNotFoundException("Customer","Mobile",mobile));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(()-> new ResourceNotFoundException("Customer","Mobile",mobile));
        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());
        customerDto.setAccountsDto(accountsDto);
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto != null){
            Accounts accounts = accountRepository.findById(accountsDto.getAccountNumber())
                    .orElseThrow(()-> new ResourceNotFoundException("Account","Account Number",accountsDto.getAccountNumber().toString()));
            AccountsMapper.mapToAccounts(accountsDto,accounts);
            accounts = accountRepository.save(accounts);

            Long customerId = accounts.getCustomerId();

            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer","CustomerId",customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
       return  isUpdated;

    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        boolean isUpdated = true;
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(()-> new ResourceNotFoundException("Customer","Mobile",mobileNumber));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(()-> new ResourceNotFoundException("Customer","Mobile",mobileNumber));
        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());
        customerDto.setAccountsDto(accountsDto);
        customerRepository.delete(customer);
        accountRepository.delete(accounts);
        return isUpdated;
    }

    private Accounts createNewAccount(Customer customer){
        Accounts account = new Accounts();
        account.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L+new Random().nextInt(900000000);
        account.setAccountNumber(randomAccNumber);

        account.setAccountType(AccountsConstants.SAVINGS);
        account.setBranchAddress(AccountsConstants.ADDRESS);
        account.setCreatedAt(LocalDateTime.now());
        account.setCreatedBy("Manish");
        return account;
    }


}
