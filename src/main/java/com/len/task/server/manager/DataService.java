package com.len.task.server.manager;

import com.len.task.common.bean.DataBean;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.repo.ModuleRepo;
import com.len.task.common.repo.TaskCompleteRepo;
import com.len.task.common.repo.TaskRepo;
import com.len.task.common.repo.TaskRequestRepo;
import com.len.task.common.util.FantasticUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/15 14:29
 */
@Service
public class DataService {
    @Autowired
    private TaskRequestRepo taskRequestRepo;
    @Autowired
    private TaskCompleteRepo taskCompleteRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private ModuleRepo moduleRepo;

    public List<DataBean> getDataDeviceList(int type, String startTime, String endTime) {
        List<Object[]> objects = new ArrayList<>();
        List<DataBean> list = new ArrayList<>();
        if (type == ServerConstant.BY_HOUR) {
            GenerateTimeByHour generateTimeByHour = new GenerateTimeByHour(startTime, endTime).invoke();
            startTime = generateTimeByHour.getStartTime();
            endTime = generateTimeByHour.getEndTime();
            objects = taskRequestRepo.getDataDeviceByHour(startTime, endTime);
        }
        if (type == ServerConstant.BY_DAY) {
            GenerateTimeByDay generateTimeByDay = new GenerateTimeByDay(startTime, endTime).invoke();
            startTime = generateTimeByDay.getStartTime();
            endTime = generateTimeByDay.getEndTime();
            objects = taskRequestRepo.getDataDeviceByDay(startTime, endTime);
        }
        if (objects != null && objects.size() > 0) {
            objects.stream().forEach(object -> {
                list.add(DataBean.builder().dateStr(object[0].toString()).
                        reqCount(Integer.parseInt(object[1].toString()))
                        .deviceCount(Integer.parseInt(object[2].toString())).build());
            });
        }
        return list;
    }

    public List<DataBean> getDataBeanList(int type, String dataType, String startTime, String endTime, String id) {
        List<Object[]> objects = new ArrayList<>();
        List<DataBean> list = new ArrayList<>();


        if (dataType.equals(ServerConstant.MODULE)) {
            if (type == ServerConstant.BY_HOUR) {
                GenerateTimeByHour generateTimeByHour = new GenerateTimeByHour(startTime, endTime).invoke();
                startTime = generateTimeByHour.getStartTime();
                endTime = generateTimeByHour.getEndTime();
                objects = taskCompleteRepo.getDataModuleByHour(startTime, endTime, id);
            }
            if (type == ServerConstant.BY_DAY) {
                GenerateTimeByDay generateTimeByDay = new GenerateTimeByDay(startTime, endTime).invoke();
                startTime = generateTimeByDay.getStartTime();
                endTime = generateTimeByDay.getEndTime();
                objects = taskCompleteRepo.getDataModuleByDay(startTime, endTime, id);
            }
        }
        if (dataType.equals(ServerConstant.TASK)) {
            if (type == ServerConstant.BY_HOUR) {
                GenerateTimeByHour generateTimeByHour = new GenerateTimeByHour(startTime, endTime).invoke();
                startTime = generateTimeByHour.getStartTime();
                endTime = generateTimeByHour.getEndTime();
                objects = taskCompleteRepo.getDataTaskByHour(startTime, endTime, id);
            }
            if (type == ServerConstant.BY_DAY) {
                GenerateTimeByDay generateTimeByDay = new GenerateTimeByDay(startTime, endTime).invoke();
                startTime = generateTimeByDay.getStartTime();
                endTime = generateTimeByDay.getEndTime();
                objects = taskCompleteRepo.getDataTaskByDay(startTime, endTime, id);
            }
        }
        if (objects != null && objects.size() > 0) {
            objects.stream().forEach(object -> {
                String beanName = "";
                if (id.equals("0")) {
                    beanName = "";
                } else {
                    if (dataType.equals(ServerConstant.MODULE)) {
                        beanName = moduleRepo.getOne(Integer.parseInt(id)).getModuleName();
                    }
                    if (dataType.equals(ServerConstant.TASK)) {
                        beanName = taskRepo.getOne(Integer.parseInt(id)).getTaskName();
                    }
                }

                list.add(DataBean.builder().dateStr(object[0].toString())
                        .beanName(beanName)
                        .reqCount(Integer.parseInt(object[1].toString()))
                        .price(BigDecimal.valueOf(Double.parseDouble(object[2].toString()))).build());
            });
        }
        return list;
    }

    private class GenerateTimeByHour {
        private String startTime;
        private String endTime;

        public GenerateTimeByHour(String startTime, String endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public GenerateTimeByHour invoke() {
            if (StringUtils.isBlank(startTime)) {
                //未指定日期，默认当前24小时
                Date startDate = new Date();
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(startDate);
                rightNow.add(Calendar.HOUR, -23);
                Date endDate = rightNow.getTime();
                startTime = FantasticUtil.SDF_2_HOUR_00.get().format(endDate);
                endTime = FantasticUtil.SDF_2_HOUR_59.get().format(startDate);
            } else {
                //指定日期，查询指定日期的数据
                try {
                    Date date = FantasticUtil.SDF_2_D.get().parse(startTime);
                    startTime = FantasticUtil.SDF_2_DAY_00.get().format(date);
                    endTime = FantasticUtil.SDF_2_DAY_59.get().format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }
    }

    private class GenerateTimeByDay {
        private String startTime;
        private String endTime;

        public GenerateTimeByDay(String startTime, String endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public GenerateTimeByDay invoke() {
            if (StringUtils.isBlank(startTime)) {
                //未指定日期，默认当前30天
                Date startDate = new Date();
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(startDate);
                rightNow.add(Calendar.DATE, -29);
                Date endDate = rightNow.getTime();
                startTime = FantasticUtil.SDF_2_DAY_00.get().format(endDate);
                endTime = FantasticUtil.SDF_2_DAY_59.get().format(startDate);
            } else {
                //指定日期，查询指定日期的数据
                try {
                    Date startDate = FantasticUtil.SDF_2_D.get().parse(startTime);
                    Date endDate = FantasticUtil.SDF_2_D.get().parse(endTime);
                    startTime = FantasticUtil.SDF_2_DAY_00.get().format(startDate);
                    endTime = FantasticUtil.SDF_2_DAY_59.get().format(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }
    }
}
