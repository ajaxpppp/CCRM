package com.shen.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("visitPageController")
public class VisitPageController {

    @RequestMapping("workbench/visit/index.do")
    public String index() {
        return "workbench/visit/index";
    }
}
