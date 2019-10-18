package com.tw.apistackbase.controller;

import com.tw.apistackbase.core.Company;
import com.tw.apistackbase.service.CompanyService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private static final String COMPANY_NOT_FOUND = "Company not found";
    private static final String VALUE_NAME_CANNOT_BE_NULL = "Value 'name' cannot be null.";
    @Autowired
    CompanyService companyService;

    @GetMapping(value = "/all", produces = {"application/json"})
    public ResponseEntity<Iterable<Company>> list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        Iterable<Company> companyFromService = companyService.findAllByPage(page, pageSize);
        return new ResponseEntity<>(companyFromService, HttpStatus.OK);
    }

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<Iterable<Company>> listById(@RequestParam String name) {
        if (name != null) {
            Iterable<Company> companyFromService = companyService.findAllLikeName(name);
            return new ResponseEntity<>(companyFromService, HttpStatus.OK);
        }

        throw new NullPointerException(VALUE_NAME_CANNOT_BE_NULL);
    }

    @PostMapping(produces = {"application/json"})
    public ResponseEntity<Company> add(@RequestBody Company company) {
        Company companyFromService = companyService.addCompany(company);
        return new ResponseEntity<>(companyFromService, HttpStatus.CREATED);
    }

    @PutMapping(path = "/edit/{id}", produces = {"application/json"})
    public ResponseEntity<Company> edit(@PathVariable Long id, @RequestBody Company company) throws NotFoundException {
        Company companyFromService = companyService.editCompany(id, company);

        if (companyFromService != null) {
            return new ResponseEntity<>(companyFromService, HttpStatus.OK);
        }

        throw new NotFoundException(COMPANY_NOT_FOUND);
    }

    @DeleteMapping(path = "/delete/{id}", produces = {"application/json"})
    public ResponseEntity<Company> delete(@PathVariable Long id) throws NotFoundException {
        Company companyFromService = companyService.deleteCompany(id);

        if (companyFromService != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new NotFoundException(COMPANY_NOT_FOUND);
    }

}