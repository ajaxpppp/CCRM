package com.shen.crm.workbench.mapper;

import com.shen.crm.workbench.domain.FunnelVO;
import com.shen.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranMapper {
    /**
     * 查询交易表中各个阶段的数据量
     * @return 数据集合
     */
    List<FunnelVO> selectCountOfTranGroupByStage();

    Tran selectTransactionById(String id);

    int deleteTranByIds(String[] ids);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon Jan 09 21:19:52 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon Jan 09 21:19:52 CST 2023
     */
    int insert(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon Jan 09 21:19:52 CST 2023
     */
    int insertSelective(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon Jan 09 21:19:52 CST 2023
     */
    Tran selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon Jan 09 21:19:52 CST 2023
     */
    int updateByPrimaryKeySelective(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Mon Jan 09 21:19:52 CST 2023
     */
    int updateByPrimaryKey(Tran record);

    /**
     * 保存创建的交易
     *
     * @param tran
     * @return
     */
    int insertTran(Tran tran);

    /**
     * 根据id查询交易明细信息
     *
     * @param id
     * @return
     */
    Tran selectTranForDetailById(String id);

    List<Tran> selectTransactionByConditionForPage(Map<String, Object> map);

    int selectCountOfTransactionByCondition(Map<String, Object> map);


}