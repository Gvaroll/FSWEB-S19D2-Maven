package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/customer") // Prompt'taki endpoint path: /workintech/customers (context-path + controller)
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer find(@PathVariable long id) {
        return customerService.find(id);
    }

    @PostMapping
    public CustomerResponse save(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.save(customer);
        return new CustomerResponse(savedCustomer.getId(), savedCustomer.getEmail(), savedCustomer.getSalary());
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable long id, @RequestBody Customer customer) {
        Customer existingCustomer = customerService.find(id);
        if (existingCustomer != null) {
            existingCustomer.setFirstName(customer.getFirstName());
            existingCustomer.setLastName(customer.getLastName());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setSalary(customer.getSalary());
            existingCustomer.setAddress(customer.getAddress());
            Customer saved = customerService.save(existingCustomer);
            return new CustomerResponse(saved.getId(), saved.getEmail(), saved.getSalary());
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public CustomerResponse delete(@PathVariable long id) {
        Customer customer = customerService.find(id);
        if (customer != null) {
            customerService.delete(id);
            return new CustomerResponse(customer.getId(), customer.getEmail(), customer.getSalary());
        }
        return null;
    }
}