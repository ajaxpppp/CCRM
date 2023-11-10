package com.shen.crm.workbench.service;

import com.shen.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    int saveCreateClueRemark(ClueRemark clueRemark);

    List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId);

    int deleteClueRemarkById(String id);

    int saveEditClueRemark(ClueRemark clueRemark);
}
