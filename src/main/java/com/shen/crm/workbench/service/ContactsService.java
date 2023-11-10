package com.shen.crm.workbench.service;

import com.shen.crm.workbench.domain.Contacts;
import com.shen.crm.workbench.domain.FunnelVO;

import java.util.List;
import java.util.Map;

public interface ContactsService {
    List<Contacts> queryContactsByConditionForPage(Map<String, Object> map);

    int queryCountOfContactsByCondition(Map<String, Object> map);

    void saveCreateContacts(Contacts contacts);

    Contacts queryContactsById(String id);

    void saveEditContacts(Contacts contacts);

    void deleteContacts(String[] contactsIds);

    Contacts queryContactsForDetailById(String id);

    List<Contacts> queryContactsByFuzzyName(String contactsName);

    List<FunnelVO> queryCountOfCustomerAndContactsGroupByCustomer();
}
