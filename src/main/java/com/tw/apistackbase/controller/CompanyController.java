package com.tw.apistackbase.controller;

import com.tw.apistackbase.core.Company;
import com.tw.apistackbase.repository.CompanyRepository;
import com.tw.apistackbase.service.CompanyService;
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
    CompanyService companyService;

    @GetMapping(value = "/all", produces = {"application/json"})
    public Iterable<Company> list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        return companyService.findAllByPage(page, pageSize);
    }

    @GetMapping(produces = {"application/json"})
    public Iterable<Company> listById(@RequestParam String name) {
        return companyService.findAllLikeName(name);
    }

    @PostMapping(produces = {"application/json"})
    @ResponseStatus(code = HttpStatus.CREATED)
    public Company add(@RequestBody Company company) {
        return companyService.addCompany(company);
    }

    @PutMapping(path = "/edit/{id}", produces = {"application/json"})
    public ResponseEntity<Company> edit(@PathVariable Long id, @RequestBody Company company) {
        return companyService.editCompany(id, company);
    }

    @DeleteMapping(path = "/delete/{id}", produces = {"application/json"})
    public ResponseEntity<Company> delete(@PathVariable Long id) {
        return companyService.deleteCompany(id);
    }
}