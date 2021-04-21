package service.user;

import pojo.User;

import java.util.List;

public interface UserService {
    //用户登录
    public User login(String userCode, String password);

    //根据用户ID修改密码
    public boolean updatePwd(int id,String pwd);

    //查询记录数
    public int getUserCount(String username,int userRole);

    //根据条件查询用户列表
    public List<User> getUserList(String queryUserName,int queryUserRole,int currentPageNo,int pageSize);

    //添加用户
    public boolean add(User user);

    //根据用户编码，查询相应用户
    public User selectUserCodeExist(String userCode);

    //根据用户id删除用户
    public boolean deleteUserById(Integer delId);

    //根据用户id查询用户
    public User getUserById(String id);

    //根据用户信息，修改用户
    public boolean modify(User user);
}
