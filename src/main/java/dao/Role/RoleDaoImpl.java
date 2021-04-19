package dao.Role;

import dao.BaseDao;
import pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{

    //获取角色表
    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement preparedStatement =null;
        ResultSet rs = null;
        List<Role> roleList = new ArrayList<Role>();

        if (connection!=null){
            String sql = "select * from smbms_role";
            Object[] params = {};
            rs = BaseDao.execute(connection, sql, params, rs, preparedStatement);

            while (rs.next()){
                Role _role =new Role();
                _role.setId(rs.getInt("id"));
                _role.setRoleCode(rs.getString("roleCode"));
                _role.setRoleName(rs.getString("roleName"));
                roleList.add(_role);
            }
            BaseDao.closeResource(null, preparedStatement, rs);
        }
        return  roleList;
    }
}
