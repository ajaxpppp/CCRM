package com.shen.crm.workbench.service;

import com.shen.crm.workbench.domain.ContactsRemark;

import java.util.List;

public interface ContactsRemarkService{

    List<ContactsRemark> queryContactsRemarkForDetailByContactsId(String contactsId);

    int saveCreateContactsRemark(ContactsRemark contactsRemark);

    int deleteContactsRemarkById(String id);

    int saveEditContactsRemark(ContactsRemark contactsRemark);
}
