package com.geblog.mhl.service;

import com.geblog.mhl.dao.MenuDAO;
import com.geblog.mhl.domain.Menu;

import java.util.List;

/**
 * @description:
 * @author: ge
 * @date: 2022/07/26
 **/
public class MenuService {

    //定义MenuDAO属性
    private MenuDAO menuDAO = new MenuDAO();

    //返回所有的菜品，返回给界面使用
    public List<Menu> list() {
        return menuDAO.queryMulti("select * from menu", Menu.class);
    }

    //需要方法，根据id，返回Menu对象
    public Menu getMenuById(int id) {
        return menuDAO.querySingle("select * from menu where id = ?", Menu.class, id);
    }

}
