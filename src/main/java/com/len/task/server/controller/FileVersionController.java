package com.len.task.server.controller;

import com.len.task.common.bean.ResultBean;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.DynamicFile;
import com.len.task.common.entity.FileVersion;
import com.len.task.common.enums.PageEnum;
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
@RequestMapping("/server/fileversion")
public class FileVersionController {
    @Autowired
    private FileVersionService fileVersionService;
    @Autowired
    private FileService fileService;

    @RequestMapping("/list")
    public String list(HttpServletRequest request, int fileId, String msg) {
        DynamicFile dynamicFile = fileService.getFile(fileId);
        List<FileVersion> fileVersionList = fileVersionService.getFileVersionList(fileId);
        //页面数据存储
        request.setAttribute(ServerConstant.FILEVERSION_LIST, fileVersionList);
        request.setAttribute(ServerConstant.DYNAMICFILE, dynamicFile);
        log.info("msg:" + msg);
        if (StringUtils.isNotBlank(msg)) {
            request.setAttribute(ServerConstant.MSG, msg);
        }
        return PageEnum.FILEVERSION_LIST.getPage();
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, @RequestParam(value = "id", defaultValue = "0") int id, int fileId) {
        DynamicFile dynamicFile = fileService.getFile(fileId);
        FileVersion fileVersion = id == 0 ? FileVersion.builder().dynamicFile(dynamicFile).build() : fileVersionService.getFileVersion(id);
        log.info(fileVersion.toString());
        request.setAttribute(ServerConstant.FILEVERSION, fileVersion);
        return PageEnum.FILEVERSION_EDIT.getPage();
    }

    @RequestMapping("/saveFile")
    @ResponseBody
    public ResultBean saveFile(FileVersion fileVersion) {
        String operation = fileVersion.getId() != null ? "编辑" : "新增";
        log.info(fileVersion.toString());
        if (!fileVersionService.checkFileVersion(new FileVersion(fileVersion.getId(), fileVersion.getVersion(),fileVersion.getDynamicFile()))) {
            return ResultBean.fail(operation + "版本失败:版本重复");
        }
        return fileVersionService.save(fileVersion) != null ? ResultBean.success(operation + "版本成功") : ResultBean.fail(operation + "版本失败");
    }

    @RequestMapping("/save")
    public String save(HttpServletRequest request, FileVersion fileVersion, @RequestParam("file") MultipartFile file) {
        log.info(fileVersion.toString());
        String operation = fileVersion.getId() != null ? "编辑" : "新增";
        log.info(fileVersion.toString());
        if (!fileVersionService.checkFileVersion(new FileVersion(fileVersion.getId(), fileVersion.getVersion(), fileVersion.getDynamicFile()))) {
            return list(request, fileVersion.getDynamicFile().getId(), operation + "失败,版本号重复");
        }

        boolean result = fileVersionService.save(fileVersion, file);

        return list(request, fileVersion.getDynamicFile().getId(), result ? operation + "成功" : operation + "失败");
    }
}
