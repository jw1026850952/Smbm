package servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import pojo.User;
import service.user.UserService;
import service.user.UserServiceImpl;
import util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("savepwd")&&method!=null){
            this.updatePwd(req, resp);
        }else if (method.equals("pwdmodify")&&method!=null){
            this.pwdmodify(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从Session里面拿ID
        Object ob = req.getSession().getAttribute(Constant.USER_SESSTION);
        String newpassword = req.getParameter("newpassword");
        boolean flag=false;

        System.out.println(newpassword);

        if (ob!=null&& !StringUtils.isNullOrEmpty(newpassword)){
            UserService userService = new UserServiceImpl();
            System.out.println(flag+"111111111111");

            flag=userService.updatePwd(((User)ob).getId(), newpassword);

            System.out.println(flag+"2222222222222222");

            if (flag){
                req.setAttribute("message", "修改密码成功，请退出，使用新密码登录");
                //密码修改成功，移除当前Session
                req.getSession().removeAttribute(Constant.USER_SESSTION);
            }else {
                req.setAttribute("message", "修改密码失败");
            }
        }else {
            req.setAttribute("message", "新密码有问题");
        }
        req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);

    }

    //验证旧密码,session中有用户的密码
    public void pwdmodify(HttpServletRequest req, HttpServletResponse resp){
        Object o = req.getSession().getAttribute(Constant.USER_SESSTION);
        String oldpassword = req.getParameter("oldpassword");

        //万能的Map：结果集
        HashMap<String, String> ResultMap = new HashMap<String, String>();

        if (o==null){                                       //Session已经过期了
            ResultMap.put("result", "sessionerror");

        }else if (StringUtils.isNullOrEmpty(oldpassword)){//输入密码为空
            ResultMap.put("result", "error");
        }else {
            String userPassword = ((User)o).getUserPassword();
            if (oldpassword.equals(userPassword)){//输入密码正确
                ResultMap.put("result", "true");
            }else {                               //输入密码错误
                ResultMap.put("result", "false");
            }
        }

        resp.setContentType("application/json");
        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(ResultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
