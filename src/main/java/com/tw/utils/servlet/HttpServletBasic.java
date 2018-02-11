/**
 * HttpServletBasic.java
 * com.tw.utils.servlet
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   ver1.0  2018年2月9日 		torreswang
 *
 * Copyright (c) 2018, b-i All Rights Reserved.
*/

package com.tw.utils.servlet;
/**
 * ClassName:HttpServletBasic（Describe this Class）
 * @author   torreswang
 * @version  Ver 1.0
 * @Date	 2018年2月9日		下午11:35:27
 * @see 	 
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class HttpServletBasic extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected WebApplicationContext wac = null;

    public void init() throws ServletException {
        super.init();
        ServletContext sc = this.getServletContext();
        wac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
    }

    /**
     * 方法转发器，通过方法名调用相应的方法 。 可以看出，不能分出方法同名而参数不同的方法 。
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) {

        String methodName = request.getParameter("methodName");

        // 当前类所有的方法名称
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                try {
                    m.invoke(this, new Object[] { request, response });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void destroy() {
        wac = null;
    }

}
