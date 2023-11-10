package com.shen.crm.workbench.service.impl;

import com.shen.crm.workbench.domain.TranHistory;
import com.shen.crm.workbench.mapper.TranHistoryMapper;
import com.shen.crm.workbench.service.TranHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tranHistoryService")
public class TranHistoryServiceImpl implements TranHistoryService {

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public List<TranHistory> queryTranHistoryForDetailByTranId(String tran) {
        return tranHistoryMapper.selectTranHistoryForDetailByTranId(tran);
    }
}
