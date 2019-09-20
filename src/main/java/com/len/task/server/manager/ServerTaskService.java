package com.len.task.server.manager;

import com.len.task.common.bean.TaskExcelBean;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.Area;
import com.len.task.common.entity.Module;
import com.len.task.common.entity.Task;
import com.len.task.common.entity.User;
import com.len.task.common.repo.AreaRepo;
import com.len.task.common.repo.ModuleRepo;
import com.len.task.common.repo.TaskRepo;
import com.len.task.common.util.CloudUtil;
import com.len.task.common.util.FantasticUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.len.task.common.constant.ServerConstant.ENABLE;
import static com.len.task.common.constant.ServerConstant.ROLE_ADMIN;

/**
 * @author sujianfeng
 * @date 2019-08-11 00:26
 */
@Service
@Slf4j
public class ServerTaskService {
    @Value("${dir.file.upload}")
    private String path;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private ModuleRepo moduleRepo;
    @Autowired
    private AreaRepo areaRepo;

    public List<Task> getTaskList(Module module) {
        return taskRepo.findAllByModule(module);
    }

    public Task getTask(int id) {
        return taskRepo.getOne(id);
    }

    public boolean checkTask(Task task) {
        List<Task> list = taskRepo.findAllByTaskName(task.getTaskName());
        if (list.size() > 0) {
            if (task.getId() == null || task.getId().intValue() == 0) {
                return false;
            } else {
                for (Task dbTask : list) {
                    if (dbTask.getId().intValue() != task.getId().intValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Task save(Task task, User user) {
        if (task.getId() == null) {
            task.setCreateUser(user);
        }
        task.setUpdateUser(user);
        return taskRepo.saveAndFlush(task);
    }

    public List<Task> getTaskList(User user) {
        List<Task> taskList;
        if (user.getRole().getRoleLevel() == ROLE_ADMIN) {
            taskList = taskRepo.findAll();
        } else {
            List<Module> moduleList = moduleRepo.getBindModules(user.getId());
            List<Task> finalTaskList = new ArrayList<>();
            moduleList.stream().forEach(module -> {
                List<Task> list = taskRepo.findAllByModule(module);
                finalTaskList.addAll(list);
            });
            taskList = finalTaskList;
        }
        return taskList;
    }


    public List<Area> loadSelectedAreas(int id, int type) {
        Task task = taskRepo.getOne(id);
        if (type == ServerConstant.AREA_TYPE_ALLOW) {
            if (StringUtils.isNotBlank(task.getAllowArea())) {
                return areaRepo.getSelectAreas(task.getAllowArea());
            }
        }
        if (type == ServerConstant.AREA_TYPE_LIMIT) {
            if (StringUtils.isNotBlank(task.getLimitArea())) {
                return areaRepo.getSelectAreas(task.getLimitArea());
            }
        }
        return null;
    }

    public List<Area> loadCitys(int id) {
        return areaRepo.findAllByParentId(id);
    }

    public List<Area> loadAreaList(int level) {
        return areaRepo.findAllByAreaLevel(level);
    }

    public List<Task> getTaskList(Module module, String[] condition) {
        List<Task> taskList = taskRepo.findAllByModule(module);
        if (condition.length == 2) {
            if (StringUtils.isNotBlank(condition[0])) {
                //匹配名称
                Iterator<Task> iterator = taskList.iterator();
                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    if (!task.getTaskName().contains(condition[0])) {
                        iterator.remove();
                    }
                }
            }
            if (StringUtils.isNotBlank(condition[1])) {
                //匹配状态
                int status = Integer.parseInt(condition[1]);
                if (status != -1) {
                    Iterator<Task> iterator = taskList.iterator();
                    while (iterator.hasNext()) {
                        Task task = iterator.next();
                        if (task.getStatus() != status) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
        return taskList;
    }

    public String parseExcel(MultipartFile file, int moduleId) {
        //获取上传文件名,包含后缀
        String originalFilename = file.getOriginalFilename();
        //获取后缀
        String subFileName = originalFilename.substring(originalFilename.lastIndexOf("."));
        log.info(subFileName);
        if (!subFileName.toLowerCase().equals(".xls") && !subFileName.toLowerCase().equals(".xlsx")) {
            return "不支持的文件类型";
        }
        //保存的文件名
        String dFileName = System.currentTimeMillis() + subFileName;
        //生成保存文件
        File uploadFile = new File(path + dFileName);
        //将上传文件保存到路径
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
            return "文件上传失败";
        }

        try {
            InputStream is = new FileInputStream(uploadFile);
            //根据版本选择创建Workbook的方式
            Workbook wb = null;
            //根据文件名判断文件是2003版本还是2007版本
            if (originalFilename.endsWith("xls")) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            //根据excel里面的内容读取信息
            return readExcel(wb, moduleId);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readExcel(Workbook wb, int moduleId) {
        //得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        //得到Excel的行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        //总列数
        int totalCells = 0;
        //得到Excel的列数(前提是有行数)，从第二行算起
        if (totalRows >= 2 && sheet.getRow(1) != null) {
            totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
        }
        List<TaskExcelBean> dataList = new ArrayList<>();
        TaskExcelBean tempData;

        //循环Excel行数,从第二行开始。标题不入库
        for (int r = 1; r < totalRows; r++) {
            String rowMessage = "";
            Row row = sheet.getRow(r);
            if (row == null) {
                log.info("第" + (r + 1) + "行数据有问题，请仔细检查！");
                return "第" + (r + 1) + "行数据有问题，请仔细检查！";
            }
            tempData = TaskExcelBean.builder().build();
            //循环Excel的列
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    switch (c) {
                        case 0:
                            double taskId = cell.getNumericCellValue();
                            tempData.setTaskId((int) taskId);
                            break;
                        case 1:
                            String taskName;
                            try {
                                taskName = cell.getStringCellValue();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return "id为" + tempData.getTaskId() + "的任务名称格式错误";
                            }
                            tempData.setTaskName(taskName);
                            break;
                        case 2:
                            String detail = cell.getStringCellValue();
                            if (!CloudUtil.isJson(detail)) {
                                return "id为" + tempData.getTaskId() + "的任务详情json格式错误";
                            }
                            tempData.setDetail(detail);
                            break;
                        default:
                            break;
                    }
                } else {
                    return "第" + (c + 1) + "列数据有问题，请仔细检查；";
                }
            }
            dataList.add(tempData);
        }

        Module module = moduleRepo.getOne(moduleId);

        List<Task> tasks = new ArrayList<>();

        for (TaskExcelBean taskExcelBean : dataList) {
            log.info(taskExcelBean.toString());
            Task task = null;
            if (taskExcelBean.getTaskId() == 0) {
                try {
                    task = Task.builder().module(module).status(ENABLE).priority(40).period(1).periodUnit(1)
                            .price(BigDecimal.valueOf(0.001)).total(10000).random(80)
                            .startDate(FantasticUtil.TL_SDF_2_S.get().parse("20190831000000"))
                            .endDate(FantasticUtil.TL_SDF_2_S.get().parse("20231231000000"))
                            .startTime(FantasticUtil.TL_SDF_2_S.get().parse("19700101000000"))
                            .endTime(FantasticUtil.TL_SDF_2_S.get().parse("19700101235959"))
                            .taskName(taskExcelBean.getTaskName()).detail(taskExcelBean.getDetail()).build();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                task = taskRepo.getOne(taskExcelBean.getTaskId());
                if (task == null) {
                    return "未找到id为" + taskExcelBean.getTaskId() + "的任务";
                }
                task.setTaskName(taskExcelBean.getTaskName());
                task.setDetail(taskExcelBean.getDetail());
            }

            if (task != null) {
                tasks.add(task);
            }
        }

        if (tasks.size() == 0) {
            return "未找到能够导入的数据";
        }

        taskRepo.saveAll(tasks);

        return "导入成功";
    }

}
