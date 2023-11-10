package com.shen.crm.workbench.service;

import com.shen.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    List<Integer> queryCountOfClueGroupByClueStage();

    List<String> queryClueStageOfClueGroupByClueStage();
    int saveCreateClue(Clue clue);

    List<Clue> queryClueByConditionForPage(Map<String, Object> map);

    int queryCountOfClueByCondition(Map<String, Object> map);

    Clue queryClueForById(String id);

    void saveConvertClue(Map<String, Object> map);
}
