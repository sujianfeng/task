package com.len.task.server.interceptor;

import com.len.task.common.constant.ServerConstant;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashSet;

/**
 * @author sujianfeng
 * @date 2019-08-17 13:00
 */
@Slf4j
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        ServletContext application = session.getServletContext();

        // 在application范围由一个HashSet集保存所有的session
        HashSet<HttpSession> sessions = (HashSet<HttpSession>) application.getAttribute(ServerConstant.SESSIONS);
        if (sessions == null) {
            sessions = new HashSet();
            application.setAttribute(ServerConstant.SESSIONS, sessions);
        }

        // 新创建的session均添加到HashSet集中
        sessions.add(session);

        // 可以在别处从application范围中取出sessions集合
        // 然后使用sessions.size()获取当前活动的session数，即为“在线人数”
        log.info("当前session数量:" + sessions.size());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        ServletContext application = session.getServletContext();
        HashSet sessions = (HashSet) application.getAttribute("sessions");

        // 销毁的session均从HashSet集中移除
        sessions.remove(session);
    }


}
