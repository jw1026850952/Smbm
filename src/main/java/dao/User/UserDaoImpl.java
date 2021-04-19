package dao.User;

import dao.BaseDao;
import pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao{

    //得到要登录的用户
    public User getLoginUser(Connection connection, String userCode) throws SQLException {

        PreparedStatement preparedStatement =null;
        ResultSet resultSet = null;
        User user = null;

        if (connection!=null){
            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};
            resultSet = BaseDao.execute(connection, sql, params, resultSet, preparedStatement);

            if (resultSet.next()){
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPassword(resultSet.getString("userPassword"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setCreatedBy(resultSet.getInt("createdBy"));
                user.setCreationDate(resultSet.getTimestamp("creationDate"));
                user.setModifyBy(resultSet.getInt("modifyBy"));
                user.setModifyDate(resultSet.getTimestamp("modifyDate"));
            }
            BaseDao.closeResource(null, preparedStatement, resultSet);

        }

        return user;

    }


    //修改当前用户密码
    public int updatePwd(Connection connection, int id, String password) throws SQLException {
        PreparedStatement preparedStatement = null;
        String sql = "update smbms_user set userPassword=? where id=?";
        System.out.println("DAOid=="+id);
        System.out.println("DAOpassword=="+password);

        int executeupdate = 0;
        if (connection!=null){
            Object params[]={password,id};
            executeupdate = BaseDao.executeupdate(connection, sql, params, preparedStatement);
            BaseDao.closeResource(null, preparedStatement, null);
        }else {
            System.out.println("DAO的connection没有连接");
        }

        return executeupdate;
    }
}
