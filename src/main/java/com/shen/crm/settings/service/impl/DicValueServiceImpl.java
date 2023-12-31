package com.shen.crm.settings.service.impl;

import com.shen.crm.settings.domain.DicValue;
import com.shen.crm.settings.mapper.DicValueMapper;
import com.shen.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dicValueService")
public class DicValueServiceImpl implements DicValueService {

    @Autowired
    private DicValueMapper dicValueMapper;

    @Override
    public List<DicValue> queryDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValueByTypeCode(typeCode);
    }

    @Override
    public DicValue queryDicValueById(String id) {
        return dicValueMapper.selectDicValueById(id);
    }
}
