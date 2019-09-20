package com.len.task.server.manager;

import com.len.task.common.entity.DynamicFile;
import com.len.task.common.repo.DynamicFileRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/7 15:57
 */
@Slf4j
@Service
public class FileService {

    @Autowired
    private DynamicFileRepo dynamicFileRepo;

    public List<DynamicFile> getFileList() {
        return dynamicFileRepo.findAll();
    }

    public DynamicFile getFile(int id) {
        return dynamicFileRepo.getOne(id);
    }

    public DynamicFile save(DynamicFile file) {
        DynamicFile dbFile;
        if (file.getId() != null) {
            //编辑
            dbFile = dynamicFileRepo.getOne(file.getId());
            dbFile.setFileName(file.getFileName());
            dbFile = dynamicFileRepo.saveAndFlush(dbFile);
            log.info("编辑成功");
        } else {
            //新增
            dbFile = DynamicFile.builder().fileName(file.getFileName()).build();
            dbFile = dynamicFileRepo.saveAndFlush(dbFile);
            log.info("新增成功");
        }
        return dbFile;
    }

    public boolean checkFile(DynamicFile file) {
        List<DynamicFile> list = dynamicFileRepo.findAllByFileName(file.getFileName());
        if (list.size() > 0) {
            if (file.getId() == null || file.getId().intValue() == 0) {
                return false;
            } else {
                for (DynamicFile dbFile : list) {
                    if (dbFile.getId().intValue() != file.getId().intValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
