package com.geblog.mhl.service;

import com.geblog.mhl.dao.BillDAO;
import com.geblog.mhl.dao.MultiTableDAO;
import com.geblog.mhl.domain.Bill;
import com.geblog.mhl.domain.MultiTableBean;

import java.util.List;
import java.util.UUID;

/**
 * @description:处理和账单相关的业务
 * @author: ge
 * @date: 2022/07/26
 **/
public class BillService {

    //定义BillDAO属性
    private BillDAO billDAO = new BillDAO();
    //定义MenuService 属性
    private MenuService menuService = new MenuService();
    //定义DiningTableService属性
    private DiningTableService diningTableService = new DiningTableService();

    private MultiTableDAO multiTableDAO = new MultiTableDAO();



    //编写点餐的方法
    //1.生成账单
    //2.需要更新对应餐桌的状态
    //3.如果成功返回true，否则返回false
    public boolean orderMenu(int menuId,int nums,int diningTableId) {
        //生成一个账单号，UUID
        String billID = UUID.randomUUID().toString();

        //将账单生成到bill表,要求直接计算账单金额
        int update = billDAO.update("insert into bill values(null,?,?,?,?,?,now(),'未结账')",
                billID, menuId, nums, menuService.getMenuById(menuId).getPrice() * nums, diningTableId);

        if(update <=0) {
            return false;
        }

        //需要更新对应餐桌的状态
        return diningTableService.updateDiningTableState(diningTableId,"就餐中");
    }

    //返回所有的账单，提供给View调用
    public List<Bill> list() {
        return  billDAO.queryMulti("select * from bill", Bill.class);
    }

    //返回所有的账单并带有菜品名，提供给View调用
    public List<MultiTableBean> list2() {
        return  multiTableDAO.queryMulti("select bill.*, NAME " +
                "FROM bill, menu " +
                "WHERE bill.menuId = menu.id", MultiTableBean.class);
    }

    //查看某个餐桌是否有未结账的账单
    public boolean hasPayBillByDiningTableId(int diningTableId) {
        Bill bill = billDAO.querySingle("SELECT * FROM bill WHERE diningTableId=? AND state = '未结账' LIMIT 0,1", Bill.class, diningTableId);
        return bill !=null;
    }

    //完成结账【如果餐桌存在，并且该餐桌有未结账的账单】
    public boolean payBill(int diningTableId, String payMode) {
        //1. 修改bill表
        int update = billDAO.update("update bill set state=? where diningTableId=? and state='未结账'", payMode, diningTableId);
        if(update <= 0) {//如果更新没有成功，则表示失败。。。
            return false;
        }
        //2. 修改diningTable表
        //注意：不要直接在这里操作，而应该调用diningTableService方法
        if(!diningTableService.updateDiningTableState(diningTableId,"空")) {
            return false;
        }
        
        return true;
    }



    //返回需要结账的账单
    public List<Bill> showck(int id) {
        return billDAO.queryMulti("select * from bill WHERE diningTableId = ?",Bill.class,id);
    }

    //修改账单状态
    public boolean updateBillState(int id) {
        int zz = billDAO.update("update bill set state ='已付款' where diningTableId=? ",id);
        if(zz>0) {
            diningTableService.updateDiningTable(id,"空","","");
        }
        return zz>0;
    }


}
