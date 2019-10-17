package com.tw.apistackbase.controller;

import com.tw.apistackbase.core.Company;
import com.tw.apistackbase.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    @Autowired
    CompanyRepository companyRepository;

    @GetMapping(value = "/all", produces = {"application/json"})
    public Iterable<Company> list(@RequestParam(required = false) Integer page, Integer pageSize) {
        if (page == null && pageSize == null) {
            return companyRepository.findAll();
        }

        return companyRepository.findAll(PageRequest.of(page, pageSize));
}

    @GetMapping(produces = {"application/json"})
    public Iterable<Company> listById(@RequestParam String name) {
        return companyRepository.findByNameContaining(name);
    }

    @PostMapping(produces = {"application/json"})
    @ResponseStatus(code = HttpStatus.CREATED)
    public Company add(@RequestBody Company company) {
        return companyRepository.save(company);
    }

    @PutMapping(path = "/edit/{id}", produces = {"application/json"})
    public ResponseEntity<Company> edit(@PathVariable Long id, @RequestBody Company company) {
        Company companyFromDB = companyRepository.findById(id).orElse(null);

        if(companyFromDB != null){
            companyFromDB.setName(company.getName());
            companyRepository.save(companyFromDB);
            return new ResponseEntity<>(companyFromDB, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/delete/{id}", produces = {"application/json"})
    public ResponseEntity<Company> delete(@PathVariable Long id) {
        Company companyFromDB = companyRepository.findById(id).orElse(null);

        if(companyFromDB != null) {
            companyRepository.deleteById(id);
            return new ResponseEntity<>(companyFromDB, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
