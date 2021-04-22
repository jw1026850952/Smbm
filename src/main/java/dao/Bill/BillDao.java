package dao.Bill;

import pojo.Bill;

import java.sql.Connection;
import java.util.List;

public interface BillDao {

    //添加订单
    public int add(Connection connection, Bill bill)throws Exception;

    //获取订单列表
    public List<Bill> getBillList(Connection connection, Bill bill)throws Exception;

    //删除订单
    public int deleteBillById(Connection connection, String delId)throws Exception;

    //通过订单id，获取某个订单信息
    public Bill getBillById(Connection connection, String id)throws Exception;

    //修改订单
    public int modify(Connection connection, Bill bill)throws Exception;

    //通过供应商id，查询该供应商的订单数
    public int getBillCountByProviderId(Connection connection, String providerId)throws Exception;
}
