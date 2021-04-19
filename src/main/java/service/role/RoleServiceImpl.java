package service.role;

import dao.BaseDao;
import dao.Role.RoleDao;
import dao.Role.RoleDaoImpl;
import org.junit.Test;
import pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService{

    //引入Dao
    private RoleDao roleDao;

    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    public List<Role> getRoleList() {
        Connection connection =null;
        List<Role> roleList = null;


        try {
            connection= BaseDao.getConnection();
            roleList=roleDao.getRoleList(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }

        return roleList;
    }

    @Test
    public void A(){
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        for (Role role : roleList) {
            System.out.println(role.getId());
            System.out.println(role.getRoleName());
            System.out.println(role.getRoleCode());
        }
    }
}
