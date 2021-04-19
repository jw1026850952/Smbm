package dao.User;

import com.mysql.jdbc.StringUtils;
import dao.BaseDao;
import org.junit.Test;
import pojo.Role;
import pojo.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    //根据用户名或者角色查询用户总数
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int count = 0 ;


        if (connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("select COUNT(1) as count from smbms_user as u,smbms_role as r where u.userRole=r.id");
            ArrayList<Object> list = new ArrayList<Object>();

            if (!StringUtils.isNullOrEmpty(username)){
                sql.append(" and u.userName like ?");
                list.add("%"+username+"%");
            }

            if (userRole>0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }


            //把list转换成数组
            Object[] params = list.toArray();


            System.out.println("UserDaoImpl--->getUserCount"+sql.toString());//输出最后的SQL语句

            rs = BaseDao.execute(connection, sql.toString(), params, rs, preparedStatement);

            if (rs.next()){
                count = rs.getInt("count");//从结果集中获取最终的数量
            }

        }
        BaseDao.closeResource(null, preparedStatement, rs);
        return count;
    }

    //获取用户表
    public List<User> getUserList(Connection connection, String username, int userRole, int currentPageNo, int pageSize) throws Exception {
        PreparedStatement preparedStatement =null ;
        ResultSet rs = null ;
        List<User> userList = new ArrayList<User>();

        if (connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user as u,smbms_role as r where u.userRole=r.id");
            ArrayList<Object> list = new ArrayList<Object>();

            if (!StringUtils.isNullOrEmpty(username)){
                sql.append(" and u.userName like ?");
                list.add("%"+username+"%");
            }

            if (userRole>0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }

            //在数据库中，分页使用Limit startIndex,pagesize;
            //0,4  1 01234
            //5,5  2 56789
            //10,5 3 10~14

            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params =list.toArray();
            System.out.println("UserDaoImpl---->getUserList"+sql.toString());
            rs = BaseDao.execute(connection, sql.toString(), params, rs, preparedStatement);

            while(rs.next()){//从结果集中获取最终的结果
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.closeResource(null, preparedStatement, rs);

        }
        return userList;
    }



}
