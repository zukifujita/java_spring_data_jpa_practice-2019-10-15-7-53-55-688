package com.tw.apistackbase.controller;

import com.tw.apistackbase.core.Company;
import com.tw.apistackbase.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    @Autowired
    CompanyRepository companyRepository;

    @GetMapping(produces = {"application/json"})
    public Iterable<Company> list() {
        return companyRepository.findAll();
    }

    @PostMapping(produces = {"application/json"})
    public Company add(@RequestBody Company company) {
        return companyRepository.save(company);
    }

    @PutMapping(path = "/edit/{id}", produces = {"application/json"})
    public Company edit(@PathVariable Long id, @RequestBody Company company) {
        Company companyFromDb = companyRepository.findById(id).orElse(null);

        if(companyFromDb != null){
            companyFromDb.setProfile(company.getProfile());
            companyFromDb.setEmployees(company.getEmployees());
            companyFromDb.setName(company.getName());

            return companyRepository.save(companyFromDb);
        }
        return null;
    }
}
