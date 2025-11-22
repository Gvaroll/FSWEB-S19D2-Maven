package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.AccountResponse;
import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Account;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.AccountService;
import com.workintech.s18d4.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/account") // Testlerde /workintech context path zaten var, burası relative path
public class AccountController {

    private final AccountService accountService;
    private final CustomerService customerService;

    @GetMapping
    public List<Account> findAll() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    public Account find(@PathVariable long id) {
        return accountService.find(id);
    }

    @PostMapping("/{customerId}")
    public AccountResponse save(@PathVariable("customerId") long customerId, @RequestBody Account account) {
        Customer customer = customerService.find(customerId);
        if (customer != null) {
            customer.getAccounts().add(account);
            account.setCustomer(customer);
            Account savedAccount = accountService.save(account);

            return new AccountResponse(savedAccount.getId(), savedAccount.getAccountName(), savedAccount.getMoneyAmount(),
                    new CustomerResponse(customer.getId(), customer.getEmail(), customer.getSalary()));
        }
        throw new RuntimeException("Customer not found");
    }

    @PutMapping("/{customerId}")
    public AccountResponse update(@PathVariable("customerId") long customerId, @RequestBody Account account) {
        Customer customer = customerService.find(customerId);
        Account existingAccount = accountService.find(account.getId());

        if (customer != null && existingAccount != null) {
            existingAccount.setAccountName(account.getAccountName());
            existingAccount.setMoneyAmount(account.getMoneyAmount());
            existingAccount.setCustomer(customer);

            Account savedAccount = accountService.save(existingAccount);

            return new AccountResponse(savedAccount.getId(), savedAccount.getAccountName(), savedAccount.getMoneyAmount(),
                    new CustomerResponse(customer.getId(), customer.getEmail(), customer.getSalary()));
        }
        throw new RuntimeException("Customer or Account not found");
    }

    @DeleteMapping("/{id}")
    public AccountResponse delete(@PathVariable long id) {
        Account account = accountService.find(id);
        if (account != null) {
            accountService.delete(id);
            return new AccountResponse(account.getId(), account.getAccountName(), account.getMoneyAmount(),
                    new CustomerResponse(account.getCustomer().getId(), account.getCustomer().getEmail(), account.getCustomer().getSalary()));
        }
        throw new RuntimeException("Account not found");
    }
}