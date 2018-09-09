package com.hzy.controller;

import com.hzy.helper.BroadcastHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@ResponseBody
public class TestController {

    @Autowired
    private BroadcastHelper broadcastHelper;

    @RequestMapping(value="/update")
    public String update(){
        broadcastHelper.update();
        return "ok";
    }
}