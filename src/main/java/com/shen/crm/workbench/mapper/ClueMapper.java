package com.shen.crm.workbench.mapper;

import com.shen.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueMapper {

    List<Integer> selectCountOfClueGroupByClueStage();

    List<String> selectClueStageOfClueGroupByClueStage();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Mon Jan 09 10:26:35 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Mon Jan 09 10:26:35 CST 2023
     */
    int insert(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Mon Jan 09 10:26:35 CST 2023
     */
    int insertSelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Mon Jan 09 10:26:35 CST 2023
     */
    Clue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Mon Jan 09 10:26:35 CST 2023
     */
    int updateByPrimaryKeySelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Mon Jan 09 10:26:35 CST 2023
     */
    int updateByPrimaryKey(Clue record);

    /**
     * 保存创建的线索
     *
     * @param record
     * @return
     */
    int insertClue(Clue record);

    /**
     * 根据条件分页查询线索列表
     *
     * @param map 查询条件
     * @return 查询到的线索
     */
    List<Clue> selectClueByConditionForPage(Map<String, Object> map);

    /**
     * 根据条件查询线索总条数
     *
     * @param map 查询条件
     * @return 线索总条数
     */
    int selectCountOfClueByCondition(Map<String, Object> map);

    /**
     * 根据id查询线索的明细信息
     *
     * @param id
     * @return
     */
    Clue selectClueForDetailById(String id);

    Clue selectClueById(String id);

    /**
     * 根据id删除姓名
     *
     * @param clueIds
     * @return
     */
    int deleteClueById(String[] clueIds);
}