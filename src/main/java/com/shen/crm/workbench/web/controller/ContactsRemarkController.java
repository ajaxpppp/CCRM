package com.shen.crm.workbench.web.controller;

import com.shen.crm.commons.constants.Contants;
import com.shen.crm.commons.domain.ReturnObject;
import com.shen.crm.commons.utils.DateUtils;
import com.shen.crm.commons.utils.UUIDUtils;
import com.shen.crm.settings.domain.User;
import com.shen.crm.workbench.domain.ContactsRemark;
import com.shen.crm.workbench.service.ContactsRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ContactsRemarkController {
    @Autowired
    private ContactsRemarkService contactsRemarkService;

    @RequestMapping("/workbench/contacts/saveCreateContactsRemark.do")
    @ResponseBody
    public Object saveCreateContactsRemark(ContactsRemark contactsRemark, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        // 封装参数
        contactsRemark.setCreateBy(user.getId());
        contactsRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
        contactsRemark.setId(UUIDUtils.getUUID());
        contactsRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_FALSE);
        ReturnObject returnObject = new ReturnObject();
        // 插入操作
        try {
            // 保存联系人备注
            int res = contactsRemarkService.saveCreateContactsRemark(contactsRemark);
            if (res > 0) { // 插入成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(contactsRemark); // 将备注也传到前端响应到页面
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

    @RequestMapping("/workbench/contacts/deleteContactsRemarkById.do")
    @ResponseBody
    public Object deleteContactsRemarkById(String id) {
        ReturnObject returnObject = new ReturnObject();
        // 删除操作
        try {
            // 删除联系人备注
            int res = contactsRemarkService.deleteContactsRemarkById(id);
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

    @RequestMapping("/workbench/contacts/saveEditContactsRemark.do")
    @ResponseBody
    public Object saveEditContactsRemark(ContactsRemark contactsRemark, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        // 封装参数
        contactsRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_TRUE);
        contactsRemark.setEditBy(user.getId());
        contactsRemark.setEditTime(DateUtils.formateDateTime(new Date()));
        ReturnObject returnObject = new ReturnObject();
        try {
            int res = contactsRemarkService.saveEditContactsRemark(contactsRemark);
            if (res > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(contactsRemark);
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
