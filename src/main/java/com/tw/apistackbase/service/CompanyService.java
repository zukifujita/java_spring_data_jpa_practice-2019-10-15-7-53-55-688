package com.tw.apistackbase.service;

import com.tw.apistackbase.core.Company;
import com.tw.apistackbase.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    public Iterable<Company> findAll() {
        return companyRepository.findAll();
    }

    public Iterable<Company> findAllByPage(Integer page, Integer pageSize) {
        if (page == null && pageSize == null) {
            return companyRepository.findAll();
        }

        return companyRepository.findAll(PageRequest.of(page, pageSize));
}

    public Iterable<Company> findAllLikeName(String name) {
        return companyRepository.findByNameContaining(name);
    }

    public Company addCompany(Company company) {
        return companyRepository.save(company);
    }

    public ResponseEntity<Company> editCompany(Long id, Company company) {
        Company companyFromDB = companyRepository.findById(id).orElse(null);
        if (companyFromDB != null) {
            companyFromDB.setName(company.getName());
            companyRepository.save(companyFromDB);
            return new ResponseEntity<>(companyFromDB, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Company> deleteCompany(Long id) {
        Company companyFromDB = companyRepository.findById(id).orElse(null);

        if (companyFromDB != null) {
            companyRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
