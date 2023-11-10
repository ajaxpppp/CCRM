package com.shen.crm.commons.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReturnObject {
    private String code; //处理成功或者失败的标记 1.成功   0.失败
    private String message; //提示信息
    private Object retData; //其他数据
}
