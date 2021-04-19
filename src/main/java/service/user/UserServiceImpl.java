package service.user;

import dao.BaseDao;
import dao.User.UserDao;
import dao.User.UserDaoImpl;
import org.junit.Test;
import pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public class UserServiceImpl implements UserService{

    //业务层都会调用DAO层，所以我们要引入DAO层
    private UserDao userDao;
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

    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
        User admin = userService.login("admin","21312312");
        if (admin!=null){
            System.out.println(admin.getUserPassword());
        }else {
            System.out.println("11111111111111111111111111");
        }


    }
}
