package com.geblog.mhl.service;


import com.geblog.mhl.dao.EmployeeDAO;
import com.geblog.mhl.domain.Employee;

/**
 * @description: 该类完成对employee表的各种操作
 * @author: ge
 * @date: 2022/07/18
 **/
public class EmployeeService {
    //定义一个Employee 属性
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    //方法，根据empId 和pwd 返回一个Employee对象
    //如果查询不到，就返回null
    public Employee getEmployeeByIdAndPwd(String empId, String pwd) {

       return employeeDAO.querySingle("select * from employee where empId=? and pwd=md5(?)", Employee.class, empId, pwd);

    }
}
