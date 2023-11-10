package com.shen.crm.settings.web.controller;

import com.shen.crm.commons.constants.Contants;
import com.shen.crm.commons.domain.ReturnObject;
import com.shen.crm.commons.utils.DateUtils;
import com.shen.crm.commons.utils.UUIDUtils;
import com.shen.crm.settings.domain.User;
import com.shen.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /*去登录页*/
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin() {
        /*请求转发页面*/
        return "settings/qx/user/login";
    }


    @RequestMapping("settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        /*封装参数*/
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        /*调用用户*/
        User user = userService.queryUserByLoginActAndPwd(map);
        ReturnObject returnObject = new ReturnObject();
        if (user == null) {
            /*登录失败*/
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户或密码错误");
        } else {
            if (DateUtils.formateDateTime(new Date()).compareTo(user.getExpireTime()) > 0) {
                /*compareTo 比较函数*/
                /*登录失败,账号过期*/
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            } else if ("0".equals(user.getLockState())) {
                /*登录失败,状态被锁定*/
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁定");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                /*contains 包含*/
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");
            } else {
                /*登录成功*/
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                session.setAttribute(Contants.SESSION_USER, user);
                /*如果需要记住密码，则往外写cookie*/
                if ("true".equals(isRemPwd)) {
                    Cookie c1 = new Cookie("loginAct", user.getLoginAct());
                    c1.setMaxAge(10 * 24 * 60 * 60);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", user.getLoginPwd());
                    c2.setMaxAge(10 * 24 * 60 * 60);
                    response.addCookie(c2);
                } else {
                    /*把没有过期的cookie删除*/
                    Cookie c1 = new Cookie("loginAct", "1");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", "1");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session) {
        /*把没有过期的cookie删除*/
        Cookie c1 = new Cookie("loginAct", "1");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd", "1");
        c2.setMaxAge(0);
        response.addCookie(c2);
        /*销毁session*/
        session.invalidate();
        return "redirect:/";
    }

    /**
     * 跳转到注册界面
     *
     * @return 注册界面静态资源
     */
    @RequestMapping("/settings/qx/user/toRegister.do")
    public String toRegister() {
        return "settings/qx/user/register";
    }

    /**
     * 注册功能
     *
     * @param user    注册用户
     * @param request 请求
     * @return 后端响应给前端的信息
     */
    @RequestMapping("/settings/qx/user/register.do")
    @ResponseBody
    public Object register(User user, HttpServletRequest request) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 2); // 在当前日期的基础上增加两年
        // 补充参数
        user.setId(UUIDUtils.getUUID());
        user.setExpireTime(DateUtils.formateDateTime(calendar.getTime())); // 设置过期时间：两年后
        user.setLockState(Contants.ACT_LOCK_STATE_FALSE); // 锁定状态为非锁定
        user.setAllowIps(request.getRemoteAddr()); // 默认当前本机登录ip
        user.setCreatetime(DateUtils.formateDateTime(new Date()));
        ReturnObject returnObject = new ReturnObject();
        // 保存用户
        try {
            int res = userService.saveNewUser(user);
            if (res > 0) { // 保存成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else { // 保存失败，服务器端出了问题，为了不影响顾客体验，最好不要直接说出问题
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

    @RequestMapping("/workbench/changePassword.do")
    @ResponseBody
    public Object changePassword(String oldPwd, String loginPwd, String confirmPwd, HttpSession session) {

        User user = (User) session.getAttribute(Contants.SESSION_USER);

        String id = user.getId();
        System.out.println("--------------------------" + id);
        ReturnObject returnObject = new ReturnObject();

        try {

            int ret = userService.changeUserPassword(id, loginPwd);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("新密码或旧密码错误,请重新输入");
            }
        } catch (Exception e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("新密码或旧密码错误,请重新输入");
        }
        return returnObject;
    }

}
