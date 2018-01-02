package com.trasen.top.controller;

import com.google.common.collect.ImmutableMap;
import com.trasen.top.service.TestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * Created by zhangxiahui on 17/5/26.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    private static final Logger logger = Logger.getLogger(TestController.class);




    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Map<String, Object> getTestID(@PathVariable String id) {
        //String name = testService.getTestName(id);
        return new ImmutableMap.Builder<String, Object>().put("status", 1)
                .put("msg", "成功").put("data","").build();
    }


}
