package service.bill;

import pojo.Bill;

import java.sql.Connection;
import java.util.List;

public interface BillService {

    //添加订单
    public boolean add(Bill bill);

    //获取订单列表
    public List<Bill> getBillList(Bill bill);

    //删除订单
    public boolean deleteBillById(String delId);

    //通过订单id，获取某个订单信息
    public Bill getBillById(String id);

    //修改订单
    public boolean modify(Bill bill);

}
