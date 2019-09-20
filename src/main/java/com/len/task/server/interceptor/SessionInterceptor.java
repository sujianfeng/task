package com.len.task.server.interceptor;

import com.len.task.common.config.SystemConfig;
import com.len.task.common.constant.ServerConstant;
import com.len.task.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author len
 * @date 2016/12/20
 */
@Slf4j
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //log.info("请求uri：" + request.getRequestURI());
        String uri = request.getRequestURI();
        if (uri.startsWith("/task/server/")) {
            if (!(uri.indexOf("login") > 0)) {
                return checkSession(request, response);
            }
        }
        return true;
    }

    private boolean checkSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute(ServerConstant.USER);
        if (user == null || user.getStatus() == 0 || (!request.getSession().getId().equals(SystemConfig.sessionMap.get(user.getId())))) {
            log.info("session用户不可用" + request.getContextPath());
            if (request.getSession().getAttribute(ServerConstant.USER) != null) {
                request.getSession().removeAttribute(ServerConstant.USER);
            }
            request.setCharacterEncoding("UTF8");
            response.setCharacterEncoding("UTF8");
            PrintWriter out = response.getWriter();
            StringBuilder builder = new StringBuilder();
            builder.append("<script type=\"text/javascript\">");
            builder.append("alert('Web page expired, please login again!');");
            builder.append("window.location.href='");
            builder.append(request.getContextPath() + "/");
            builder.append("';");
            builder.append("</script>");
            out.print(builder.toString());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
