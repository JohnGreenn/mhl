package com.geblog.mhl.view;

import com.geblog.mhl.domain.*;
import com.geblog.mhl.service.BillService;
import com.geblog.mhl.service.DiningTableService;
import com.geblog.mhl.service.EmployeeService;
import com.geblog.mhl.service.MenuService;
import com.geblog.mhl.utils.Utility;

import java.util.List;

/**
 * @description: 主界面
 * @author: ge
 * @date: 2022/7/15
 **/
public class MHLView {
    //控制是否退出菜单
    private boolean loop = true;
    private String key = "";//接收用户的选择
    //定义EmployeeService 属性
    private EmployeeService employeeService = new EmployeeService();
    //调用DiningTable的属性
    private DiningTableService diningTableService = new DiningTableService();
    //定义MenuService属性
    private MenuService menuService = new MenuService();
    //定义BillService属性
    private BillService billService = new BillService();

    public static void main(String[] args) {
        new MHLView().mainMenu();
    }

    //完成结账
    public void payBill() {
        System.out.println("=================结账服务==================");
        System.out.print("请选择要结账的餐桌编号（-1退出）：");
        int diningTableId = Utility.readInt();
        if (diningTableId == -1) {
            System.out.println("=================取消结账==================");
            return;
        }
        //验证餐桌是否存在
        DiningTable diningTable = diningTableService.getDiningTableById(diningTableId);
        if (diningTable == null) {
            System.out.println("=================结账的餐桌不存在==================");
            return;
        }
        //验证餐桌是否有需要结账的账单
        if (!billService.hasPayBillByDiningTableId(diningTableId)){
            System.out.println("=================该餐位没有未结账账单==================");
            return;
        };
        System.out.print("结账的方式（现金/支付宝/微信）回车表示退出：");
        String payMode = Utility.readString(20,"");//说明如果回车，就是返回
        if ("".equals(payMode)) {
            System.out.println("=================取消结账==================");
            return;
        }
        char key = Utility.readConfirmSelection();
        if (key == 'Y') { //结账

            //调用我们写的方法
            if (billService.payBill(diningTableId,payMode)) {
                System.out.println("=================完成结账==================");
            } else {
                System.out.println("=================结账失败==================");
            }

        } else {
            System.out.println("=================取消结账==================");
            return;
        }

    }

    //结账
    public void checkout() {
        System.out.print("请选择要结账的餐桌编号（-1退出）：");
        int ckId = Utility.readInt();
        if (ckId == -1) {
            System.out.println("==============退出结账===============");
        }

        List<Bill> showck = billService.showck(ckId);
        double sum = 0; //总金额
        for (int i = 0; i < showck.size(); i++) {
            sum+=showck.get(i).getMoney();
        }
        System.out.println("您需要支付："+sum);

        System.out.print("结账的方式（现金/支付宝/微信）回车表示退出：");

        System.out.print("确认是否结账（Y/N）");
        char c = Utility.readConfirmSelection();
        if(c=='Y'){
            if(billService.updateBillState(ckId)){

                System.out.println("==============成功结账===============");
            }
        } else {
            System.out.println("==============取消结账===============");
        }


        //System.out.println("==============结账完成===============");
    }


    //显示账单信息
    public void listBill() {
//        List<Bill> allBills = billService.list();
//        System.out.println("\n编号\t\t菜品号\t\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态");
//        for (Bill allBill : allBills) {
//            System.out.println(allBill);
//        }
//        System.out.println("============显示完毕===========");

        List<MultiTableBean> multiTableBeans = billService.list2();
        System.out.println("\n编号\t\t菜品号\t\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态\t\t菜品名");
        for (MultiTableBean allBill : multiTableBeans) {
            System.out.println(allBill);
        }
        System.out.println("============显示完毕===========");

    }



    //完成点餐
    public void orderMenu() {
        System.out.println("===========点餐服务============");
        System.out.print("请输入点餐的桌号（-1退出）");
        int orderDiningId = Utility.readInt();
        if (orderDiningId == -1) {
            System.out.println("==========取消点餐==========");
        }
        System.out.print("请输入点餐的菜品号（-1退出）:");
        int orderMenuId = Utility.readInt();
        if (orderMenuId == -1) {
            System.out.println("==========取消点餐==========");
        }

        System.out.print("请输入点餐的菜品量（-1退出）:");
        int orderNums = Utility.readInt();
        if (orderNums == -1) {
            System.out.println("==========取消点餐==========");
        }

        //验证餐桌号是否存在
        DiningTable diningTable = diningTableService.getDiningTableById(orderDiningId);
        if (diningTable == null) {
            System.out.println("============餐桌号不存在===========");
            return;
        }
        //验证菜品编号
        Menu menu = menuService.getMenuById(orderMenuId);
        if (menu == null) {
            System.out.println("============菜品不存在===========");
            return;
        }

        //点餐
        if (billService.orderMenu(orderMenuId, orderNums, orderDiningId)) {
            System.out.println("===========点餐成功==========");
        } else {
            System.out.println("===========点餐失败=========");
        }


    }

    //显示所有菜品
    public void listMenu() {
        List<Menu> list = menuService.list();
        System.out.println("\n菜品编号\t\t菜品名\t\t\t\t类别\t\t\t\t价格");
        for (Menu menu : list) {
            System.out.println(menu);
        }
        System.out.println("===========显示完毕===========");
    }

    //完成订座
    public void orderDiningTable() {
        System.out.println("============预定餐桌============");
        System.out.print("请选择要预定的餐桌编号（-1退出）：");
        int orderId = Utility.readInt();
        if(orderId == -1) {
            System.out.println("============取消预定============");
            return;
        }
        //该方法得到的是Y或者N
        char key = Utility.readConfirmSelection();
        if (key == 'Y') { //要预定
            //根据orderId 返回对应DinningTable对象，如果为null,说明对象不存在
            DiningTable diningTable = diningTableService.getDiningTableById(orderId);
            if (diningTable == null) {
                System.out.println("==========预定餐桌不存在=========");
                return;
            }
            //判断该餐桌的状态是否"空"
            if(!("空".equals(diningTable.getState()))) {//说明当前这个餐桌不是“空”状态
                System.out.println("=======该餐桌已经预定或者就餐中======");
                return;
            }
            //接收预定信息
            System.out.print("预订人的名字：");
            String orderName = Utility.readString(50);
            System.out.print("预订人的电话：");
            String orderTel =Utility.readString(50);

            //更新餐桌状态
            if(diningTableService.orderDiningTable(orderId, orderName, orderTel)) {
                System.out.println("==============预定餐桌成功===============");
            } else {
                System.out.println("==============预定餐桌失败===============");
            }
        } else {
            System.out.println("==============取消预定餐桌=============");
        }

    }



    //显示主菜单
    public void mainMenu() {

        while (loop) {
            System.out.println("==============System==============");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 2 退出系统");
            System.out.print("请输入你的选择：");
            key = Utility.readString(1);
            switch (key) {
                case "1" :
                    //System.out.print("请输入员工号：");
                    //String empId = Utility.readString(50);
                    //System.out.print("请输入密  码：");
                    //String pwd = Utility.readString(50);
                    Employee employee = employeeService.getEmployeeByIdAndPwd("00001", "123456");


                    //到数据库判断密码
                    if(employee != null) { //说明用户存在
                        System.out.println("============系统登录成功["+employee.getName()+"]============");
                        //显示二级菜单,这里二级菜单是循环操作，所以做成while
                        while (loop) {
                            System.out.println("\t\t 1 显示餐桌状态");
                            System.out.println("\t\t 2 预定餐桌");
                            System.out.println("\t\t 3 显示所有菜品");
                            System.out.println("\t\t 4 点餐服务");
                            System.out.println("\t\t 5 查看账单");
                            System.out.println("\t\t 6 结账");
                            System.out.println("\t\t 9 exit");
                            System.out.print("请输入你的选择：");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    listDiningTable();
                                    break;
                                case "2":
                                    orderDiningTable();
                                    break;
                                case "3":
                                    listMenu();
                                    break;
                                case "4":
                                    orderMenu();
                                    break;
                                case "5":
                                    listBill();
                                    break;
                                case "6":
                                    //System.out.println("结账");
                                    //checkout();
                                    payBill();
                                    break;
                                case "9":
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("你输入有误,请重新输入");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("==============登录失败===============");
                    }
                    break;
                case "2":
                    loop = false;//
                    break;
                default:
                    System.out.println("你输入有误,请重新输入");
            }
        }
        System.out.println("你退出了系统~~~~");
    }

    //display table states
    public void listDiningTable() {
        List<DiningTable> list = diningTableService.list();
        System.out.println("\n餐桌编号\t\t餐桌状态\t\t\t预定者\t\t\t电话");
        for (DiningTable diningTable : list) {
            System.out.println(diningTable);
        }
        System.out.println("==============display finish===============");
    }

}
