package com.tw.apistackbase.service;

import com.tw.apistackbase.core.Company;
import com.tw.apistackbase.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

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

    public Company editCompany(Long id, Company company) {
        Company companyFromDB = companyRepository.findById(id).orElse(null);
        if (companyFromDB != null) {
            companyFromDB.setName(company.getName());
            companyRepository.save(companyFromDB);
            return companyFromDB;
        }

        return null;
    }

    public Company deleteCompany(Long id) {
        Company companyFromDB = companyRepository.findById(id).orElse(null);

        if (companyFromDB != null) {
            companyRepository.deleteById(id);
            return companyFromDB;
        }

        return null;
    }
}
