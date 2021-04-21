package dao.User;

import pojo.Role;
import pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //得到要登录的用户
    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    //修改当前用户密码
    public int updatePwd(Connection connection,int id,String password)throws SQLException;

    //根据用户名或者角色查询用户总数
    public int getUserCount(Connection connection,String username,int userRole)throws SQLException;

    //通过条件查询用户列表
    public List<User> getUserList(Connection connection,String username,int userRole,int currentPageNo,int pageSize)throws Exception;

    //通过用户信息添加用户
    public int add(Connection connection, User user)throws Exception;

    //根据id删除用户
    public int deleteUserById(Connection connection, Integer delId)throws Exception;

    //根据id查询用户
    public User getUserById(Connection connection, String id)throws Exception;

    //修改用户信息
    public int modify(Connection connection, User user)throws Exception;

}
