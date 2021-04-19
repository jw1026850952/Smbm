package service.user;

import dao.BaseDao;
import dao.User.UserDao;
import dao.User.UserDaoImpl;
import org.junit.Test;
import pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService{

    //业务层都会调用DAO层，所以我们要引入DAO层
    private UserDao userDao;

    //构造器实例化userDao
    public UserServiceImpl(){
        userDao =new UserDaoImpl();
    }

    public User login(String userCode, String password) {
        Connection connection =null;
        User user = null;

        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return user;
    }

    //根据用户ID修改密码
    public boolean updatePwd(int id, String pwd) {
       Connection connection = null;
       boolean flag = false;

        System.out.println("Serverid=="+id);
        System.out.println("Serverpassword=="+pwd);
       //修改密码
        try {
            connection = BaseDao.getConnection();

            //test
            if (connection==null){
                System.out.println("Service的connection没有连接");
            }

            if(userDao.updatePwd(connection, id, pwd)>0){
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }

        return flag;
    }

    //查询记录数
    public int getUserCount(String username, int userRole) {
        Connection connection =null;
        int count = 0 ;

        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, username, userRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return count;
    }

    //根据条件查询用户列表
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection =null;
        List<User> userList = null;
        System.out.println("queryUserName--->"+queryUserName);
        System.out.println("queryUserRole--->"+queryUserRole);
        System.out.println("currentPageNo--->"+currentPageNo);
        System.out.println("pageSize--->"+pageSize);


        try {
            connection=BaseDao.getConnection();
            userList=userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return userList;
    }

    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = userService.getUserList("邓超", 0, 1, 5);
        for (User user : userList) {
            System.out.println(user.getId());
            System.out.println(user.getUserCode());
            System.out.println(user.getUserName());
            System.out.println(user.getAge());
            System.out.println(user.getPhone());

        }

    }
}
