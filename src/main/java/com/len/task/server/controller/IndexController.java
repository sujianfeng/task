package com.len.task.server.controller;

import com.len.task.common.enums.PageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author len
 * @date 2016/12/20
 */
@Slf4j
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return PageEnum.LOGIN.getPage();
    }

    @RequestMapping("/server/page/{url}.html")
    public String redirectUrl(@PathVariable("url") String url) {
        log.info("redirect to :" + url);
        return url;
    }
}
