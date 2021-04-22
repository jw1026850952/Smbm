package dao.Provider;

import pojo.Provider;

import java.sql.Connection;
import java.util.List;

public interface ProviderDao {
    //根据供应商信息，添加供应商
    public int add(Connection connection, Provider provider)throws Exception;

    //通过供应商名称、编码获取供应商列表-模糊查询-providerList（可批量查询）
    public List<Provider> getProviderList(Connection connection, String proName, String proCode)throws Exception;


    //根据供应商ID，删除对应供应商
    public int deleteProviderById(Connection connection, String delId)throws Exception;

    //通过proId获取Provider（单个查询）
    public Provider getProviderById(Connection connection, String id)throws Exception;

    //根据传入的供应商信息，修改供应商信息
    public int modify(Connection connection, Provider provider)throws Exception;
}
