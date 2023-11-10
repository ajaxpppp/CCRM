package com.shen.crm.workbench.service.impl;

import com.shen.crm.commons.utils.DateUtils;
import com.shen.crm.commons.utils.UUIDUtils;
import com.shen.crm.workbench.domain.Customer;
import com.shen.crm.workbench.domain.FunnelVO;
import com.shen.crm.workbench.domain.Tran;
import com.shen.crm.workbench.domain.TranHistory;
import com.shen.crm.workbench.mapper.CustomerMapper;
import com.shen.crm.workbench.mapper.TranHistoryMapper;
import com.shen.crm.workbench.mapper.TranMapper;
import com.shen.crm.workbench.mapper.TranRemarkMapper;
import com.shen.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("tranService")
public class TranServiceImpl implements TranService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranRemarkMapper tranRemarkMapper;


    @Override
    public Tran queryTransactionById(String id) {
        return tranMapper.selectTransactionById(id);

    }

    @Override
    public void saveCreateTran(Tran tran) {
        // 获取前端传来的用户对应的id（前端传来的是用户名称：tran.getCustomerId()，而数据库需要存放该用户的id）
        String customerId = customerMapper.selectCustomerIdByName(tran.getCustomerId());
        // 如果存在该用户，则将tran中的用户名改为对应的用户id
        if (customerId != null) {
            tran.setCustomerId(customerId);
        } else {
            // 不存在该用户，则新创建用户，并将tran中的用户名改为新创建的用户id
            Customer customer = new Customer();
            customer.setOwner(tran.getCreateBy());
            customer.setName(tran.getCustomerId());
            customer.setId(UUIDUtils.getUUID());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setCreateBy(tran.getCreateBy());
            customerMapper.insertCustomer(customer); // 新增用户
            tran.setCustomerId(customer.getId()); // 修改联系人的用户为该用户id
        }
        // 新增线索
        tranMapper.insertTran(tran);
        // 新增线索历史记录
        TranHistory tranHistory = new TranHistory();
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(DateUtils.formateDateTime(new Date()));
        tranHistory.setTranId(tran.getId());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setId(UUIDUtils.getUUID());
        // 新增历史记录
        tranHistoryMapper.insertTranHistory(tranHistory);
    }

    @Override
    public Tran queryTranForDetailById(String id) {
        return tranMapper.selectTranForDetailById(id);
    }

    @Override
    public List<Tran> queryTransactionByConditionForPage(Map<String, Object> map) {
        return tranMapper.selectTransactionByConditionForPage(map);
    }

    @Override
    public int queryCountOfTransactionByCondition(Map<String, Object> map) {
        return tranMapper.selectCountOfTransactionByCondition(map);
    }

    @Override
    public void saveEditTransaction(Tran tran) {
        // 获取前端传来的用户对应的id（前端传来的是用户名称：tran.getCustomerId()，而数据库需要存放该用户的id）
        String customerId = customerMapper.selectCustomerIdByName(tran.getCustomerId());
        // 如果存在该用户，则将tran中的用户名改为对应的用户id
        if (customerId != null) {
            tran.setCustomerId(customerId);
        } else {
            // 不存在该用户，则新创建用户，并将tran中的用户名改为新创建的用户id
            Customer customer = new Customer();
            customer.setOwner(tran.getEditBy());
            customer.setName(tran.getCustomerId());
            customer.setId(UUIDUtils.getUUID());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setCreateBy(tran.getEditBy());
            customerMapper.insertCustomer(customer); // 新增用户
            tran.setCustomerId(customer.getId()); // 修改联系人的用户为该用户id
        }
        // 更新交易
        tranMapper.updateByPrimaryKey(tran);
        // 新增更新线索历史记录
        TranHistory tranHistory = new TranHistory();
        tranHistory.setCreateBy(tran.getEditBy());
        tranHistory.setCreateTime(DateUtils.formateDateTime(new Date()));
        tranHistory.setTranId(tran.getId());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setId(UUIDUtils.getUUID());
        // 新增历史记录
        tranHistoryMapper.insertTranHistory(tranHistory);
    }

    @Override
    public void deleteTranByIds(String[] ids) {
        // 删除备注
        tranRemarkMapper.deleteTranRemarkByTranIds(ids);
        // 删除历史记录
        tranHistoryMapper.deleteTranHistoryByTranIds(ids);
        // 删除交易
        tranMapper.deleteTranByIds(ids);
    }

    @Override
    public List<FunnelVO> queryCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }
}
