package com.shen.crm.workbench.web.controller;

import com.shen.crm.commons.constants.Contants;
import com.shen.crm.commons.domain.ReturnObject;
import com.shen.crm.commons.utils.DateUtils;
import com.shen.crm.commons.utils.UUIDUtils;
import com.shen.crm.settings.domain.User;
import com.shen.crm.workbench.domain.TranRemark;
import com.shen.crm.workbench.service.TranRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class TranRemarkController {
    @Autowired
    private TranRemarkService tranRemarkService;

    @RequestMapping("/workbench/tran/saveCreateTranRemark.do")
    @ResponseBody
    public Object saveCreateTranRemark(TranRemark tranRemark, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        // 封装参数
        tranRemark.setCreateBy(user.getId());
        tranRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
        tranRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_FALSE);
        tranRemark.setId(UUIDUtils.getUUID());
        ReturnObject returnObject = new ReturnObject();
        // 插入操作
        try {
            // 保存交易备注
            int res = tranRemarkService.saveCreateTranRemark(tranRemark);
            if (res > 0) { // 插入成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(tranRemark); // 将备注也传到前端响应到页面
            } else { // 插入失败，服务器端出了问题，为了不影响顾客体验，最好不要直接说出问题
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试...");
            }
        } catch (Exception e) { // 发生了某些异常，捕获后返回信息
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/tran/deleteTranRemarkById.do")
    @ResponseBody
    public Object deleteTranRemarkById(String id) {
        ReturnObject returnObject = new ReturnObject();
        // 删除操作
        try {
            // 删除备注
            int res = tranRemarkService.deleteTranRemarkById(id);
            if (res > 0) { // 删除成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else { // 删除失败，服务器端出了问题，为了不影响顾客体验，最好不要直接说出问题
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试...");
            }
        } catch (Exception e) { // 发生了某些异常，捕获后返回信息
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/tran/saveEditTranRemark.do")
    @ResponseBody
    public Object saveEditTranRemark(TranRemark tranRemark, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        // 封装参数
        tranRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_TRUE);
        tranRemark.setEditBy(user.getId());
        tranRemark.setEditTime(DateUtils.formateDateTime(new Date()));
        ReturnObject returnObject = new ReturnObject();
        try {
            int res = tranRemarkService.saveEditTranRemark(tranRemark);
            if (res > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(tranRemark);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }
        return returnObject;
    }
}
