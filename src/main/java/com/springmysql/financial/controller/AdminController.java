package com.springmysql.financial.controller;

import com.springmysql.financial.model.Stock;
import com.springmysql.financial.model.Trade;
import com.springmysql.financial.model.User;
import com.springmysql.financial.service.PortfolioService;
import com.springmysql.financial.service.StockService;
import com.springmysql.financial.service.TradeService;
import com.springmysql.financial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private StockService stockService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private PortfolioService portfolioService;


    @RequestMapping(value = "admin/users", method = RequestMethod.GET)
    public ModelAndView getUsers(){
        ModelAndView modelAndView = new ModelAndView("admin/users");
        List<User> users = new ArrayList<>();
        userService.findAll().forEach(users::add);
        modelAndView.addObject("users", users);

        return modelAndView;
    }

    @RequestMapping(value = "admin/securities", method = RequestMethod.GET)
    public ModelAndView getSecurities(){
        ModelAndView modelAndView = new ModelAndView("admin/securities");


        return modelAndView;
    }

    @RequestMapping(value = "admin/history", method = RequestMethod.GET)
    public ModelAndView getHistory(){
        ModelAndView modelAndView = new ModelAndView("admin/history");



        return modelAndView;
    }


    @RequestMapping(value="admin/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");

        Stock stock = new Stock();
        modelAndView.addObject("newStock", stock);

        List<Stock> stocks = new ArrayList<>();
        stockService.findAll().forEach(stocks::add);
        modelAndView.addObject("stocks", stocks);

        List<User> users = new ArrayList<>();
        userService.findAll().forEach(users::add);
        modelAndView.addObject("users", users);

        List<Trade> trades = tradeService.findAll();
        modelAndView.addObject("trades", trades);

        modelAndView.setViewName("admin/home");
        return modelAndView;
    }
}
