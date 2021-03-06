package servlet.user;

import pojo.User;
import service.user.UserService;
import service.user.UserServiceImpl;
import util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    //Servlet：控制层，调用业务层代码
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet--start");

        //获取用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        System.out.println(userCode);
        System.out.println(userPassword);

        //和数据库中的密码进行对比，调用业务层
        UserService userService = new UserServiceImpl();
        User user = userService.login(userCode, userPassword);

        if (user!=null&&userPassword.equals(user.getUserPassword())){//查有此人，可以登录
            //将用户的全部信息放到Session中
            req.getSession().setAttribute(Constant.USER_SESSTION, user);
            //跳转到主页
            resp.sendRedirect("/jsp/frame.jsp");
        }else {//查无此人，无法登录
            //转发回登录页面，顺带提示它，用户名或者密码错误
            req.setAttribute("error", "用户名或者密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }


}
