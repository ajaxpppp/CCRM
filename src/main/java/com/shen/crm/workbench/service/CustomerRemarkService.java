package com.shen.crm.workbench.service;

import com.shen.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkService {
    List<CustomerRemark> queryCustomerRemarkForDetailByCustomerId(String customerId);

    int saveCreateCustomerRemark(CustomerRemark customerRemark);

    int deleteCustomerRemarkById(String id);

    int saveEditCustomerRemark(CustomerRemark customerRemark);
}
