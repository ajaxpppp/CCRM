package com.shen.crm.workbench.web.controller;

import com.shen.crm.commons.constants.Contants;
import com.shen.crm.commons.domain.ReturnObject;
import com.shen.crm.commons.utils.DateUtils;
import com.shen.crm.commons.utils.UUIDUtils;
import com.shen.crm.settings.domain.DicValue;
import com.shen.crm.settings.domain.User;
import com.shen.crm.settings.service.DicValueService;
import com.shen.crm.settings.service.UserService;
import com.shen.crm.workbench.domain.Activity;
import com.shen.crm.workbench.domain.Clue;
import com.shen.crm.workbench.domain.ClueActivityRelation;
import com.shen.crm.workbench.domain.ClueRemark;
import com.shen.crm.workbench.service.ActivityService;
import com.shen.crm.workbench.service.ClueActivityRelationService;
import com.shen.crm.workbench.service.ClueRemarkService;
import com.shen.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {

    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    /**
     * 跳转到线索界面
     *
     * @param request 当前页面请求
     * @return 跳转界面
     */
    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request) {
        // 查询线索模块中所有下拉列表中的动态数据
        List<User> userList = userService.queryAllUsers();
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");

        // 封装到request域中
        request.setAttribute("userList", userList);
        request.setAttribute("appellationList", appellationList);
        request.setAttribute("clueStateList", clueStateList);
        request.setAttribute("sourceList", sourceList);
        return "workbench/clue/index";
    }

    /**
     * 保存创建的线索
     *
     * @param clue    前端传来的参数
     * @param session 当前页面session对象
     * @return 发送给前端解析信息
     */
    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
//        封装参数
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));
        clue.setCreateBy(user.getId());
        /*调用service*/
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueService.saveCreateClue(clue);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重试");
            }
        } catch (Exception e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后重试");
        }
        return returnObject;
    }

    /**
     * 由条件查询线索
     *
     * @param fullname 姓名
     * @param company  公司
     * @param phone    公司号码
     * @param source   来源
     * @param owner    所有者
     * @param mphone   手机号
     * @param state    状态
     * @param pageNo   起始页
     * @param pageSize 每个页面显示条数
     * @return 发送给前端解析信息
     */
    @RequestMapping("/workbench/clue/queryClueByConditionForPage.do")
    @ResponseBody
    public Object queryClueByConditionForPage(String fullname, String company, String phone, String source, String owner, String mphone, String state, int pageNo, int pageSize) {
        // 封装前端传来的参数
        Map<String, Object> map = new HashMap<>();
        map.put("fullname", fullname);
        map.put("company", company);
        map.put("phone", phone);
        map.put("source", source);
        map.put("owner", owner);
        map.put("mphone", mphone);
        map.put("state", state);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        // 查询数据
        List<Clue> clueList = clueService.queryClueByConditionForPage(map);
        int totalRows = clueService.queryCountOfClueByCondition(map);
        // 封装查询参数，传给前端操作
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("clueList", clueList);
        resultMap.put("totalRows", totalRows);
        return resultMap;
    }

    /**
     * 跳转到线索详情界面
     *
     * @param id      线索id
     * @param request 发送的请求
     * @return 跳转界面
     */
    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id, HttpServletRequest request) {
        // 查询对应id的线索详细信息
        Clue clue = clueService.queryClueForById(id);
        // 查询对应id的线索的所有备注
        List<ClueRemark> clueRemarkList = clueRemarkService.queryClueRemarkForDetailByClueId(id);
        // 查询对应id的线索的所有关联市场活动
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);
        // 存到request域中
        request.setAttribute("clue", clue);
        request.setAttribute("clueRemarkList", clueRemarkList);
        request.setAttribute("activityList", activityList);
        return "workbench/clue/detail";
    }

    /**
     * 在线索详情页面绑定市场活动中通过市场活动名模糊查询市场活动
     *
     * @param activityName 市场活动名
     * @param clueId       当前线索id
     * @return 查询到的市场活动集合
     */
    @RequestMapping("/workbench/clue/queryActivityForDetailByNameAndClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameAndClueId(String activityName, String clueId) {
        // 封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);
        System.err.println(clueId);
        List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);
        return activityList;
    }


    /**
     * 保存市场活动和线索的绑定
     *
     * @param activityId 市场活动id数组
     * @param clueId     线索id
     * @return 发送给前端解析信息
     */
    @RequestMapping("/workbench/clue/saveBound.do")
    @ResponseBody
    public Object saveBound(String[] activityId, String clueId) {
        /*封装参数*/
        ClueActivityRelation car = null;
        List<ClueActivityRelation> relationList = new ArrayList<>();
        for (String ai : activityId) {
            car = new ClueActivityRelation();
            car.setActivityId(ai);
            car.setClueId(clueId);
            car.setId(UUIDUtils.getUUID());
            relationList.add(car);
        }
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueActivityRelationService.saveCreateClueActivityRelationByList(relationList);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetData(activityList);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重试..");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后重试..");
        }
        return returnObject;
    }

    /**
     * 解除线索和市场活动的绑定
     *
     * @param relation
     * @return
     */
    @RequestMapping("/workbench/clue/saveUnbound.do")
    @ResponseBody
    public Object saveUnbound(ClueActivityRelation relation) {
        /*调用service方法，删除市场活动*/
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后重试");
        }
        return returnObject;
    }

    /**
     * 跳转到转换界面
     *
     * @param id      当前线索id
     * @param request 请求
     * @return 前端界面
     */
    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id, HttpServletRequest request) {
        // 查询convert页面所需的数据
        Clue clue = clueService.queryClueForById(id);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage"); // 查询stage的字典值
        // 存入request域中
        request.setAttribute("clue", clue);
        request.setAttribute("stageList", stageList);
        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/queryActivityForConvertByNameAndClueId.do")
    @ResponseBody
    public Object queryActivityForConvertByNameAndClueId(String activityName, String clueId) {
        /*封装参数*/
        HashMap<String, Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);
        /*调用service,查询市场活动*/
        List<Activity> activityList = activityService.queryActivityForConvertByNameClueId(map);
        return activityList;
    }

    @RequestMapping("/workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(String clueId, String money, String name, String expectedDate, String stage, String activityId, String isCreateTran, HttpSession session) {
        // 封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("clueId", clueId);
        map.put("money", money);
        map.put("name", name);
        map.put("expectedDate", expectedDate);
        map.put("stage", stage);
        map.put("activityId", activityId);
        map.put("isCreateTran", isCreateTran);
        map.put(Contants.SESSION_USER, session.getAttribute(Contants.SESSION_USER));
        ReturnObject returnObject = new ReturnObject();
        try {
            // 保存线索转换
            clueService.saveConvertClue(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }
        return returnObject;
    }
}
