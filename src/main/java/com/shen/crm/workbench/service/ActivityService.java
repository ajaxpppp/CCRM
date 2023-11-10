package com.shen.crm.workbench.service;

import com.shen.crm.workbench.domain.Activity;
import com.shen.crm.workbench.domain.FunnelVO;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    List<FunnelVO> queryCountOfActivityGroupByOwner();
    int saveCreateActivity(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String, Object> map);

    int queryCountOfActivityByCondition(Map<String, Object> map);

    int deleteActivity(String[] ids);

    Activity queryActivityById(String id);

    int saveEditActivity(Activity activity);

    List<Activity> queryAllActivitys();

    List<Activity> queryCheckedActivity(String[] id);

    int saveCreateActivityByList(List<Activity> activityList);

    Activity queryActivityForDetailById(String id);

    List<Activity> queryActivityForDetailByClueId(String clueId);

    List<Activity> queryActivityForDetailByNameClueId(Map<String, Object> map);

    List<Activity> queryActivityForDetailByIds(String[] ids);

    List<Activity> queryActivityForConvertByNameClueId(Map<String, Object> map);
    List<Activity> queryActivityForDetailByContactsId(String contactsId);

    List<Activity> queryActivityForDetailByNameAndContactsId(Map<String, Object> map);

    List<Activity> queryActivityByFuzzyName(String activityName);
}
