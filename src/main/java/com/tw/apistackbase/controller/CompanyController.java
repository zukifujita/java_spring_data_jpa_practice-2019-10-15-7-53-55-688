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
    public ResponseEntity<Iterable<Company>> list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        Iterable<Company> companyFromService = companyService.findAllByPage(page, pageSize);
        return new ResponseEntity<>(companyFromService, HttpStatus.OK);
    }

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<Iterable<Company>> listById(@RequestParam String name) {
        Iterable<Company> companyFromService = companyService.findAllLikeName(name);
        return new ResponseEntity<>(companyFromService, HttpStatus.OK);
    }

    @PostMapping(produces = {"application/json"})
    public ResponseEntity<Company> add(@RequestBody Company company) {
        companyService.addCompany(company);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(path = "/edit/{id}", produces = {"application/json"})
    public ResponseEntity<Company> edit(@PathVariable Long id, @RequestBody Company company) {
        Company companyFromService = companyService.editCompany(id, company);

        if (companyFromService != null) {
            return new ResponseEntity<>(companyFromService, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/delete/{id}", produces = {"application/json"})
    public ResponseEntity<Company> delete(@PathVariable Long id) {
        Company companyFromService = companyService.deleteCompany(id);

        if (companyFromService != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}