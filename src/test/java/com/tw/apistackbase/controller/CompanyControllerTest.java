package com.tw.apistackbase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.apistackbase.core.Company;
import com.tw.apistackbase.core.Employee;
import com.tw.apistackbase.repository.CompanyRepository;
import com.tw.apistackbase.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CompanyController.class)
@ActiveProfiles(profiles = "test")
public class CompanyControllerTest {

    @MockBean
    CompanyService companyService;

    @Autowired
    private CompanyController companyController;

    @Autowired
    private MockMvc mvc;

    private List<Company> companyList = new ArrayList<>();

    @Test
    public void should_return_company_when_given_part_of_string() throws Exception {
        Company company = buildCompany("Test");

        companyList.add(company);

        when(companyService.findAllLikeName(anyString())).thenReturn(companyList);

        ResultActions resultOfExecution = mvc.perform(get("/companies")
                .param("name", "name"));

        resultOfExecution.andExpect(status().isOk()).andExpect(jsonPath("$[0].name", is("Test")));
    }

    private Company buildCompany(String companyName) {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setAge(1);
        employee.setName(companyName);

        Company company = new Company();
        company.setId(1L);
        company.setName(companyName);
        company.setEmployees(Collections.singletonList(employee));

        return company;
    }
}