package com.len.task.server.controller;

import com.len.task.common.bean.ResultBean;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.ApkVersion;
import com.len.task.common.entity.DynamicFile;
import com.len.task.common.entity.FileVersion;
import com.len.task.common.enums.PageEnum;
import com.len.task.server.manager.ApkVersionService;
import com.len.task.server.manager.FileService;
import com.len.task.server.manager.FileVersionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/8 10:09
 */
@Slf4j
@Controller
@RequestMapping("/server/apkversion")
public class ApkVersionController {
    @Autowired
    private ApkVersionService apkVersionService;
    @Autowired
    private FileService fileService;

    @RequestMapping("/list")
    public String list(HttpServletRequest request, String msg) {
        List<ApkVersion> apkVersionList = apkVersionService.findAll();
        //页面数据存储
        request.setAttribute(ServerConstant.APKVERSION_LIST, apkVersionList);
        log.info("msg:" + msg);
        if (StringUtils.isNotBlank(msg)) {
            request.setAttribute(ServerConstant.MSG, msg);
        }
        return PageEnum.APKVERSION_LIST.getPage();
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, @RequestParam(value = "id", defaultValue = "0") int id) {
        ApkVersion apkVersion = id == 0 ? ApkVersion.builder().build() : apkVersionService.getApkVersion(id);
        log.info(apkVersion.toString());
        request.setAttribute(ServerConstant.APKVERSION, apkVersion);
        return PageEnum.APKVERSION_EDIT.getPage();
    }

    @RequestMapping("/saveFile")
    @ResponseBody
    public ResultBean saveFile(ApkVersion apkVersion) {
        String operation = apkVersion.getId() != null ? "编辑" : "新增";
        log.info(apkVersion.toString());
        if (!apkVersionService.checkApkVersion(new ApkVersion(apkVersion.getId(), apkVersion.getVersion()))) {
            return ResultBean.fail(operation + "版本失败:版本重复");
        }
        return apkVersionService.save(apkVersion) != null ? ResultBean.success(operation + "版本成功") : ResultBean.fail(operation + "版本失败");
    }

    @RequestMapping("/save")
    public String save(HttpServletRequest request, ApkVersion apkVersion, @RequestParam("file") MultipartFile file) {
        log.info(apkVersion.toString());
        String operation = apkVersion.getId() != null ? "编辑" : "新增";
        log.info(apkVersion.toString());
        if (!apkVersionService.checkApkVersion(new ApkVersion(apkVersion.getId(), apkVersion.getVersion()))) {
            return list(request, operation + "失败,版本号重复");
        }

        boolean result = apkVersionService.save(apkVersion, file);

        return list(request, result ? operation + "成功" : operation + "失败");
    }
}
