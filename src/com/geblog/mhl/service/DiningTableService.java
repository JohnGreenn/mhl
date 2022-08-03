package com.geblog.mhl.service;

import com.geblog.mhl.dao.DiningTableDAO;
import com.geblog.mhl.domain.DiningTable;

import java.util.List;

/**
 * @description:
 * @author: ge
 * @date: 2022/07/19
 **/
public class DiningTableService { //业务层
    //定义一个DiningTableDAO
    private DiningTableDAO diningTableDAO = new DiningTableDAO();

    //返回所有餐桌的信息
    public List<DiningTable> list() {
        return diningTableDAO.queryMulti("select id, state, orderName, orderTel from diningTable",DiningTable.class);
    }

    //根据id查询对应餐桌DiningTable对象
    //如果返回null 表示id编号对应得餐桌不存在
    public DiningTable getDiningTableById(int id) {

        //可以把sql语句放在查询分析器去测试一下
        return diningTableDAO.querySingle("select * from diningTable where id = ?",DiningTable.class,id);
    }

    //如果餐桌可以预定，调用方法，对其状态进行更新（包括预定人的名字和电话）
    public boolean orderDiningTable(int id, String orderName, String orderTel) {
        int update = diningTableDAO.update("update diningTable set state='已经预定',orderName=?,orderTel=? where id=?",orderName,orderTel,id);
        return update > 0;
    }

    //需要提供一个更新 餐桌状态的方法
    public boolean updateDiningTableState(int id, String state) {
        int update = diningTableDAO.update("update diningTable set state=? where id=?", state, id);
        return update > 0;
    }

    //提供方法，将指定

    //更新餐桌状态和预定者信息、电话
    public boolean updateDiningTable(int id, String state,String name,String tel) {
        int update = diningTableDAO.update("update diningTable set state=?,orderName=?,orderTel=? where id=?", state,name,tel, id);
        return update > 0;
    }

}
