package com.shen.crm.workbench.service;

import com.shen.crm.workbench.domain.FunnelVO;
import com.shen.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    Tran queryTransactionById(String id);
    void saveCreateTran(Tran tran);

    Tran queryTranForDetailById(String id);

    List<Tran> queryTransactionByConditionForPage(Map<String, Object> map);

    int queryCountOfTransactionByCondition(Map<String, Object> map);

    void saveEditTransaction(Tran tran);

    void deleteTranByIds(String[] ids);

    List<FunnelVO> queryCountOfTranGroupByStage();
}
