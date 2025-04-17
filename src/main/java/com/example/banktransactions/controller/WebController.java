package com.example.banktransactions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(Model model) {
        // 可以在这里添加模型数据，供模板使用
        model.addAttribute("title", "Bank Transaction Management");
        return "transactions";
    }
}