package servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import pojo.Role;
import pojo.User;
import service.role.RoleService;
import service.role.RoleServiceImpl;
import service.user.UserService;
import service.user.UserServiceImpl;
import util.Constant;
import util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("savepwd")&&method!=null){
            this.updatePwd(req, resp);
        }else if (method.equals("pwdmodify")&&method!=null){
            this.pwdmodify(req, resp);
        }else if (method.equals("query")&&method!=null) {
            this.query(req, resp);
        }else if (method.equals("add")&&method!=null) {
            this.add(req, resp);
        }else if(method != null && method.equals("getrolelist")){
            this.getRoleList(req, resp);
        }else if(method != null && method.equals("ucexist")){
            this.userCodeExist(req, resp);
        }else if(method != null && method.equals("deluser")){
            this.delUser(req, resp);
        }else if(method != null && method.equals("view")){
            this.getUserById(req, resp,"userview.jsp");
        }else if(method != null && method.equals("modify")){
            this.getUserById(req, resp,"usermodify.jsp");
        }else if(method != null && method.equals("modifyexe")){
            this.modify(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //????????????
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //???Session?????????ID
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
                req.setAttribute("message", "??????????????????????????????????????????????????????");
                //?????????????????????????????????Session
                req.getSession().removeAttribute(Constant.USER_SESSTION);
            }else {
                req.setAttribute("message", "??????????????????");
            }
        }else {
            req.setAttribute("message", "??????????????????");
        }
        req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);

    }

    //???????????????,session?????????????????????
    public void pwdmodify(HttpServletRequest req, HttpServletResponse resp){
        Object o = req.getSession().getAttribute(Constant.USER_SESSTION);
        String oldpassword = req.getParameter("oldpassword");

        //?????????Map????????????
        HashMap<String, String> ResultMap = new HashMap<String, String>();

        if (o==null){                                       //Session???????????????
            ResultMap.put("result", "sessionerror");

        }else if (StringUtils.isNullOrEmpty(oldpassword)){//??????????????????
            ResultMap.put("result", "error");
        }else {
            String userPassword = ((User)o).getUserPassword();
            if (oldpassword.equals(userPassword)){//??????????????????
                ResultMap.put("result", "true");
            }else {                               //??????????????????
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

    //?????????????????????????????????
    public void query(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String queryUserName = req.getParameter("queryname");//?????????????????????
        String queryUserRole_temp = req.getParameter("queryUserRole");//????????????????????????(????????????
        String pageIndex = req.getParameter("pageIndex");//????????????
        int queryUserRole = 0; //????????????????????????(int)

        //??????????????????
        UserService userService = new UserServiceImpl();

        //????????????????????????????????????????????????????????????
        //??????????????????
        int pageSize =Constant.pageSize;
        //????????????
        int currentPageNo = 1;

        System.out.println("queryUserName servlet--------"+queryUserName);
        System.out.println("queryUserRole servlet--------"+queryUserRole);
        System.out.println("query pageIndex--------- > " + pageIndex);
        if (queryUserName==null){
            queryUserName="";
        }
        if (queryUserRole_temp!=null && !queryUserRole_temp.equals("")){
            queryUserRole=Integer.parseInt(queryUserRole_temp);//???????????????????????????
        }

        if (pageIndex!=null){
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                resp.sendRedirect("error.jsp");
            }
        }

        //?????????
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);

        //?????????
        PageSupport pages = new PageSupport();

        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);

        int totalPageCount = pages.getTotalPageCount();

        //?????????????????????
        if (currentPageNo<1){
            currentPageNo = 1;
        }else if (currentPageNo>totalPageCount){
            currentPageNo = totalPageCount;
        }

        //???????????????????????????
        List<User> userList =null;
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);

        List<Role> roleList =null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);

        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.getRequestDispatcher("userlist.jsp").forward(req, resp);

    }

    //????????????
    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //???????????????????????????
        System.out.println("add()================");
        String userCode = request.getParameter("userCode");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        //??????????????????User????????????
        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)request.getSession().getAttribute(Constant.USER_SESSTION)).getId());

        //?????????userService?????????service??????add??????
        UserService userService = new UserServiceImpl();
        if(userService.add(user)){//???????????????????????????????????? ???????????? ??????
            response.sendRedirect(request.getContextPath()+"/jsp/user.do?method=query");
        }else{
            request.getRequestDispatcher("useradd.jsp").forward(request, response);
        }
    }

    //??????????????????
    private void getRoleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        //???roleList?????????json????????????
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(roleList));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    //????????????????????????
    private void userCodeExist(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //??????????????????????????????
        String userCode = request.getParameter("userCode");

        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(userCode)){
            //userCode == null || userCode.equals("")
            resultMap.put("userCode", "exist");
        }else{
            UserService userService = new UserServiceImpl();
            User user = userService.selectUserCodeExist(userCode);
            if(null != user){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode", "notexist");
            }
        }

        //???resultMap??????json????????????json???????????????
        //??????????????????????????????
        response.setContentType("application/json");
        //???response??????????????????????????????writer??????
        PrintWriter outPrintWriter = response.getWriter();
        //???resultMap??????json????????? ??????
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();//??????
        outPrintWriter.close();//?????????
    }

    //????????????
    private void delUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("uid");
        Integer delId = 0;
        try{
            delId = Integer.parseInt(id);
        }catch (Exception e) {
            delId = 0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(delId <= 0){
            resultMap.put("delResult", "notexist");
        }else{
            UserService userService = new UserServiceImpl();
            if(userService.deleteUserById(delId)){
                resultMap.put("delResult", "true");
            }else{
                resultMap.put("delResult", "false");
            }
        }

        //???resultMap?????????json????????????
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    //????????????id??????????????????????????????
    private void getUserById(HttpServletRequest request, HttpServletResponse response,String url)
            throws ServletException, IOException {
        String id = request.getParameter("uid");
        if(!StringUtils.isNullOrEmpty(id)){
            //????????????????????????user??????
            UserService userService = new UserServiceImpl();
            User user = userService.getUserById(id);
            request.setAttribute("user", user);
            request.getRequestDispatcher(url).forward(request, response);
        }

    }

    //????????????id????????????
    private void modify(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("uid");
        String userName = request.getParameter("userName");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User)request.getSession().getAttribute(Constant.USER_SESSTION)).getId());
        user.setModifyDate(new Date());

        UserService userService = new UserServiceImpl();
        if(userService.modify(user)){
            response.sendRedirect(request.getContextPath()+"/jsp/user.do?method=query");
        }else{
            request.getRequestDispatcher("usermodify.jsp").forward(request, response);
        }

    }
}
