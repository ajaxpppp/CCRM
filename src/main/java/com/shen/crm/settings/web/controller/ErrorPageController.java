package com.shen.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController {

    @RequestMapping("/settings/error/nopage.do")
    public String nopage() {
        return "settings/error/nopage";
    }
}
