package com.shen.crm.workbench.web.controller;

import com.shen.crm.commons.constants.Contants;
import com.shen.crm.commons.domain.ReturnObject;
import com.shen.crm.commons.utils.DateUtils;
import com.shen.crm.commons.utils.HSSFUtils;
import com.shen.crm.commons.utils.UUIDUtils;
import com.shen.crm.settings.domain.User;
import com.shen.crm.settings.service.UserService;
import com.shen.crm.workbench.domain.Activity;
import com.shen.crm.workbench.domain.ActivityRemark;
import com.shen.crm.workbench.service.ActivityRemarkService;
import com.shen.crm.workbench.service.ActivityService;
import com.sun.deploy.net.HttpResponse;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;


    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        /*调用service层方法*/
        List<User> userList = userService.queryAllUsers();
        System.out.println(userList);
        /*把数据保存到request中*/
        request.setAttribute("userList", userList);
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        /*封装参数*/
        activity.setId(UUIDUtils.getUUID()); // 主键id
        activity.setCreateTime(DateUtils.formateDateTime(new Date()));
        activity.setCreateBy(user.getId()); // 用户id（外键，一个用户可以创建多个市场活动）
        ReturnObject returnObject = new ReturnObject();
        try {
            /*调用添加方法*/
            int ret = activityService.saveCreateActivity(activity);
            System.out.println(ret);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重试 ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后重试 ");
        }

        return returnObject;
    }

    /**
     * 分页查询市场活动数据响应到前端
     *
     * @param name      名称
     * @param owner     所有者
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageNo    当前页码
     * @param pageSize  分页大小（每页数据量）
     * @return 封装的查询参数
     */
    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate, int pageNo, int pageSize) {
        // 封装前端传来的参数
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("beginNo", (pageNo - 1) * pageSize); // 分页计算起始数据的位置
        map.put("pageSize", pageSize);
        // 由前端传来的条件查询数据
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map); // 查询当前分页要显示数据集合
        int totalRows = activityService.queryCountOfActivityByCondition(map); // 查询总条数
        // 封装查询参数，传给前端操作
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("activityList", activityList);
        resultMap.put("totalRows", totalRows);
        return resultMap;
    }

    /**
     * 删除市场活动把执行信息响应到前端
     *
     * @param id 删除的市场活动id数组
     * @return 封装的查询参数
     */
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        // 注意：查找一般不会出问题，但是增删改有可能会出问题，所以需要一个异常捕获机制，及时捕获异常
        try {
            activityService.deleteActivity(id);// 删除市场活动以及市场活动所绑定的所有信息
            System.out.println(id);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) { // 发生了某些异常，捕获后返回信息
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        Activity activity = activityService.queryActivityById(id);
        /*根据查询结果，返回响应信息*/
        return activity;
    }

    /**
     * 文件修改
     *
     * @param activity
     * @param session
     * @return
     */
    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        /*封装参数*/
        activity.setEditTime(DateUtils.formateDateTime(new Date()));
        activity.setEditBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityService.saveEditActivity(activity);
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
     * 文件下载
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws IOException {
        /*设置响应类型*/
        response.setContentType("application/octet-stream;charset=UTF-8");
        /*输出字节流*/
        OutputStream out = response.getOutputStream();

        //浏览器收到响应信息之后,默认其情况下打开响应信息，打不开就下载
        response.addHeader("Content-Disposition", "attachment;filename=mystudentListList.xls");

        /*读excel文件(inputStream)，然后输出outputStream*/
        FileInputStream is = new FileInputStream("D:\\Java\\excel\\studentList.xls");
        byte[] buff = new byte[256];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            out.write(buff, 0, len);
        }

        /*关闭资源*/
        is.close();
        out.flush();
    }

    @RequestMapping("/workbench/activity/exportAllActivity.do")
    public void exportAllActivitys(HttpServletResponse response) throws IOException {
        List<Activity> activities = activityService.queryAllActivitys();
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        //遍历activityList,创建HSSFRow对象
        if (activities != null && activities.size() > 0) {
            Activity activity = null;
            for (int i = 0; i < activities.size(); i++) {
                activity = activities.get(i);
                //每遍历出一个activity,生成一行
                sheet.createRow(i + 1);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

     /*   FileOutputStream os = new FileOutputStream("D:\\Java\\excel\\activityList.xls");
        wb.write(os);
        os.close();
        wb.close();*/

        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=activityList.xls");
        OutputStream out = response.getOutputStream();
   /*     InputStream is = new FileInputStream("D:\\Java\\excel\\activityList.xls");
        byte[] bytes = new byte[256];
        int len = 0;
        while ((len = is.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }
        is.close();*/

        wb.write(out);
        wb.close();
        out.flush();
    }

    /**
     * 选择导出市场活动excel表格
     *
     * @param id       选择的市场活动id
     * @param response 响应
     * @throws Exception 输出流异常
     */
    @RequestMapping("/workbench/activity/exportCheckedActivity.do")
    public void exportCheckedActivity(String[] id, HttpServletResponse response) throws Exception {
        // 调用service层方法，查询所有的市场活动
        List<Activity> activityList = activityService.queryCheckedActivity(id);
        // 创建excel文件，并且把activityList写入到excel文件中
        HSSFUtils.createExcelByActivityList(activityList, Contants.FILE_NAME_ACTIVITY, response);
    }

    /**
     * 配置springmvc的文件上传解析器
     *
     * @param userName
     * @param myFile
     * @return
     */
    @RequestMapping("/workbench/activity/fileUpload.do")
    @ResponseBody
    public Object fileUpload(String userName, MultipartFile myFile) throws IOException {
        System.out.println("userName=" + userName);
        /*把文件在服务指定的目录中生成一个同样的文件*/
        String originalFilename = myFile.getOriginalFilename();
        File file = new File("D:\\Java\\excel\\", originalFilename);
        myFile.transferTo(file);

        ReturnObject returnObject = new ReturnObject();
        returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        returnObject.setMessage("上传成功");
        return returnObject;
    }

    /**
     * 实现文件导入市场活动功能
     *
     * @param activityFile 导入的文件
     * @param session      当前用户的session对象
     * @return 后端响应给前端的信息
     */
    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();
        try {
            InputStream is = activityFile.getInputStream();
            HSSFWorkbook wb = new HSSFWorkbook(is);
            // 根据wb获取HSSFSheet对象，封装了一页的所有信息
            HSSFSheet sheet = wb.getSheetAt(0); // 页的下标，下标从0开始，依次增加
            // 根据sheet获取HSSFRow对象，封装了一行的所有信息
            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            List<Activity> activityList = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // sheet.getLastRowNum()：最后一行的下标
                row = sheet.getRow(i); // 行的下标，下标从0开始，依次增加
                activity = new Activity();
                // 补充部分参数
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formateDateTime(new Date()));
                activity.setCreateBy(user.getId());
                for (int j = 0; j < row.getLastCellNum(); j++) { // row.getLastCellNum():最后一列的下标+1
                    // 根据row获取HSSFCell对象，封装了一列的所有信息
                    cell = row.getCell(j); // 列的下标，下标从0开始，依次增加
                    // 获取列中的数据
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if (j == 0) {
                        activity.setName(cellValue);
                    } else if (j == 1) {
                        activity.setStartDate(cellValue);
                    } else if (j == 2) {
                        activity.setEndDate(cellValue);
                    } else if (j == 3) {
                        activity.setCost(cellValue);
                    } else if (j == 4) {
                        activity.setDescription(cellValue);
                    }
                }
                //每一行中所有列都封装完成之后，把activity保存到list中
                activityList.add(activity);
            }
            // 调用service层方法，保存市场活动
            int res = activityService.saveCreateActivityByList(activityList);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(res);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id, HttpServletRequest request) {
        Activity activity = activityService.queryActivityById(id);
        List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkFoDetailByActivityId(id);
        /*把数据保存到request中*/
        request.setAttribute("activity", activity);
        request.setAttribute("remarkList", remarkList);
        return "workbench/activity/detail";
    }
}
