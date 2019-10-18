package com.tw.apistackbase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.apistackbase.core.Company;
import com.tw.apistackbase.core.Employee;
import com.tw.apistackbase.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    public void should_return_all_company_when_no_specified_string() throws Exception {
        Company company = buildCompany("Test");

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("page", "1");
        requestParams.add("pageSize", "5");

        companyList.add(company);

        when(companyService.findAllByPage(anyInt(), anyInt())).thenReturn(companyList);

        ResultActions resultOfExecution = mvc.perform(get("/companies/all").params(requestParams));

        resultOfExecution.andExpect(status().isOk()).andExpect(jsonPath("$[0].name", is("Test")));
    }

    @Test
    public void should_return_CREATED_when_given_new_company_to_add() throws Exception {
        Company company = buildCompany("PostTest");

        when(companyService.addCompany(company)).thenReturn(company);

        ResultActions resultOfExecution = mvc.perform(post("/companies")
                .content(asJsonString(company))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultOfExecution.andExpect(status()
                .isCreated());
    }

    @Test
    public void should_return_OK_when_given_updated_company_to_put() throws Exception {
        Company company = buildCompany("OldTest");
        Company newCompany = buildCompany("NewTest");

        when(companyService.editCompany(anyLong(), any())).thenReturn(newCompany);

        ResultActions resultOfExecution = mvc.perform(put("/companies/edit/{id}", 1L)
                .content(asJsonString(company))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultOfExecution.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newCompany.getName())));
    }

    @Test
    public void should_return_NOT_FOUND_when_no_given_updated_company_to_put() throws Exception {
        Company company = buildCompany("OldTest");

        when(companyService.editCompany(1L, company)).thenReturn(null);

        ResultActions resultOfExecution = mvc.perform(put("/companies/edit/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(company)));

        resultOfExecution.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.errorMessage", is("Company not found")));
    }

    @Test
    public void should_return_OK_when_given_company_to_delete() throws Exception {
        Company company = buildCompany("DeleteTest");

        when(companyService.deleteCompany(anyLong())).thenReturn(company);

        ResultActions resultOfExecution = mvc.perform(delete("/companies/delete/{id}", 1L));

        resultOfExecution.andExpect(status().isOk());
    }

    @Test
    public void should_return_NOT_FOUND_when_no_given_company_to_delete() throws Exception {
        when(companyService.deleteCompany(1L)).thenReturn(null);

        ResultActions resultOfExecution = mvc.perform(delete("/companies/delete/{id}", 1L));

        resultOfExecution.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.errorMessage", is("Company not found")));
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

    public static String asJsonString(final Company obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}