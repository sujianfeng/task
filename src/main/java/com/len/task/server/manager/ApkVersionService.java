package com.len.task.server.manager;

import com.len.task.common.bean.ResultBean;
import com.len.task.common.entity.ApkVersion;
import com.len.task.common.entity.FileVersion;
import com.len.task.common.repo.ApkVersionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/9/2 11:29
 */
@Service
@Slf4j
public class ApkVersionService {
    @Autowired
    private ApkVersionRepo apkVersionRepo;


    @Value("${dir.file.upload}")
    private String path;

    @Value("${file.down.url}")
    private String downUrl;

    public List<ApkVersion> findAll() {
        return apkVersionRepo.findAll(new Sort(Sort.Direction.DESC, "version"));
    }

    public ApkVersion getApkVersion(int id) {
        return apkVersionRepo.getOne(id);
    }

    public boolean checkApkVersion(ApkVersion apkVersion) {
        List<ApkVersion> list = apkVersionRepo.findAllByVersion(apkVersion.getVersion());
        if (list.size() > 0) {
            if (apkVersion.getId() == null || apkVersion.getId().intValue() == 0) {
                return false;
            } else {
                for (ApkVersion dbApkVersion : list) {
                    if (dbApkVersion.getId().intValue() != apkVersion.getId().intValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public ApkVersion save(ApkVersion apkVersion) {

        ApkVersion dbApkVersion;
        if (apkVersion.getId() != null) {
            //编辑
            dbApkVersion = apkVersionRepo.getOne(apkVersion.getId());
            dbApkVersion.setVersion(apkVersion.getVersion());
            dbApkVersion.setDesc(apkVersion.getDesc());
            dbApkVersion.setUrl(apkVersion.getUrl());
            dbApkVersion = apkVersionRepo.saveAndFlush(dbApkVersion);
            log.info("编辑成功");
        } else {
            //新增
            dbApkVersion = ApkVersion.builder().version(apkVersion.getVersion())
                    .desc(apkVersion.getDesc()).url(apkVersion.getUrl()).build();
            dbApkVersion = apkVersionRepo.saveAndFlush(dbApkVersion);
            log.info("新增成功");
        }
        return dbApkVersion;
    }

    public boolean save(ApkVersion apkVersion, MultipartFile file) {
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
        apkVersion.setUrl(url);

        ApkVersion dbApkVersion = save(apkVersion);

        return null != dbApkVersion;
    }
}
