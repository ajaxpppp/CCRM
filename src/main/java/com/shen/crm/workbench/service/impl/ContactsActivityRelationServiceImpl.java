package com.shen.crm.workbench.service.impl;

import com.shen.crm.workbench.domain.ContactsActivityRelation;
import com.shen.crm.workbench.mapper.ContactsActivityRelationMapper;
import com.shen.crm.workbench.service.ContactsActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("contactsActivityRelationService")
public class ContactsActivityRelationServiceImpl implements ContactsActivityRelationService {
    @Autowired
    ContactsActivityRelationMapper activityRelationMapper;

    @Override
    public int saveCreateContactsActivityRelationByList(List<ContactsActivityRelation> contactsActivityRelationList) {
        return activityRelationMapper.insertContactsActivityRelationByList(contactsActivityRelationList);
    }

    @Override
    public int deleteContactsActivityRelationByContactsIdAndActivityId(ContactsActivityRelation contactsActivityRelation) {
        return activityRelationMapper.deleteContactsActivityRelationByContactsIdAndActivityId(contactsActivityRelation);
    }
}
