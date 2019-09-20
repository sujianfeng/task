package com.len.task.server.controller;

import com.len.task.common.bean.ResultBean;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.DynamicFile;
import com.len.task.common.enums.PageEnum;
import com.len.task.server.manager.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/7 15:56
 */
@Slf4j
@Controller
@RequestMapping("/server/file")
public class FileController {
    @Autowired
    private FileService fileService;

    @RequestMapping("/list")
    public String list(HttpServletRequest request) {

        List<DynamicFile> fileList = fileService.getFileList();
        //页面数据存储
        request.setAttribute(ServerConstant.FILE_LIST, fileList);

        return PageEnum.FILE_LIST.getPage();
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, @RequestParam(value = "id", defaultValue = "0") int id) {
        DynamicFile dynamicFile = id == 0 ? DynamicFile.builder().build() : fileService.getFile(id);
        log.info(dynamicFile.toString());
        request.setAttribute(ServerConstant.DYNAMICFILE, dynamicFile);
        return PageEnum.FILE_EDIT.getPage();
    }

    @RequestMapping("/save")
    @ResponseBody
    public ResultBean saveFile(DynamicFile dynamicFile) {
        String operation = dynamicFile.getId() != null ? "编辑" : "新增";
        log.info(dynamicFile.toString());
        if (!fileService.checkFile(new DynamicFile(dynamicFile.getId(), dynamicFile.getFileName()))) {
            return ResultBean.fail(operation + "动态文件失败:动态文件名称重复");
        }
        return fileService.save(dynamicFile) != null ? ResultBean.success(operation + "动态文件成功") : ResultBean.fail(operation + "动态文件失败");
    }
}
