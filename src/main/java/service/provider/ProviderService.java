package service.provider;

import pojo.Provider;

import java.sql.Connection;
import java.util.List;

public interface ProviderService {
    //根据供应商信息，添加供应商
    public boolean add(Provider provider);

    //通过供应商名称、编码获取供应商列表-模糊查询-providerList（可批量查询）
    public List<Provider> getProviderList(String proName, String proCode);


    //根据供应商ID，删除对应供应商
    public int deleteProviderById(String delId);

    //通过proId获取Provider（单个查询）
    public Provider getProviderById(String id);

    //根据传入的供应商信息，修改供应商信息
    public boolean modify(Provider provider);
}
