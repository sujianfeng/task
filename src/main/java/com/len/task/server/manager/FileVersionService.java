package com.len.task.server.manager;

import com.len.task.common.entity.DynamicFile;
import com.len.task.common.entity.FileVersion;
import com.len.task.common.repo.DynamicFileRepo;
import com.len.task.common.repo.FileVersionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/8 10:11
 */
@Slf4j
@Service
public class FileVersionService {
    @Autowired
    private FileVersionRepo fileVersionRepo;
    @Autowired
    private DynamicFileRepo dynamicFileRepo;

    @Value("${dir.file.upload}")
    private String path;

    @Value("${file.down.url}")
    private String downUrl;

    public List<FileVersion> getFileVersionList(int fileId) {
        DynamicFile dynamicFile = dynamicFileRepo.getOne(fileId);
        if (dynamicFile != null) {
            return fileVersionRepo.findAllByDynamicFileOrderByVersionDesc(dynamicFile);
        }
        return null;
    }

    public FileVersion getFileVersion(int id) {
        return fileVersionRepo.getOne(id);
    }

    public boolean checkFileVersion(FileVersion fileVersion) {
        List<FileVersion> list = fileVersionRepo.findAllByVersionAndDynamicFile(fileVersion.getVersion(), fileVersion.getDynamicFile());
        if (list.size() > 0) {
            if (fileVersion.getId() == null || fileVersion.getId().intValue() == 0) {
                return false;
            } else {
                for (FileVersion dbFileVersion : list) {
                    if (dbFileVersion.getId().intValue() != fileVersion.getId().intValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public FileVersion save(FileVersion fileVersion) {
        FileVersion dbFileVersion;
        if (fileVersion.getId() != null) {
            //编辑
            dbFileVersion = fileVersionRepo.getOne(fileVersion.getId());
            dbFileVersion.setVersion(fileVersion.getVersion());
            dbFileVersion.setDesc(fileVersion.getDesc());
            dbFileVersion.setUrl(fileVersion.getUrl());
            dbFileVersion = fileVersionRepo.saveAndFlush(dbFileVersion);
            log.info("编辑成功");
        } else {
            //新增
            dbFileVersion = FileVersion.builder().version(fileVersion.getVersion())
                    .desc(fileVersion.getDesc()).url(fileVersion.getUrl()).build();
            dbFileVersion.setDynamicFile(fileVersion.getDynamicFile());
            dbFileVersion = fileVersionRepo.saveAndFlush(dbFileVersion);
            log.info("新增成功");
        }
        return dbFileVersion;
    }

    public boolean save(FileVersion fileVersion, MultipartFile file) {
        //获取上传文件名,包含后缀
        String originalFilename = file.getOriginalFilename();
        //获取后缀
        String subFileName = originalFilename.substring(originalFilename.lastIndexOf("."));
        //保存的文件名
        String dFileName = System.currentTimeMillis() + subFileName;
        //生成保存文件
        File uploadFile = new File(path + dFileName);
        log.info(uploadFile.toString());
        //将上传文件保存到路径
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        String url = downUrl + dFileName;
        fileVersion.setUrl(url);

        FileVersion dbFileVersion = save(fileVersion);

        return null != dbFileVersion;
    }
}
