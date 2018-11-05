package com.springmysql.financial.controller;

import com.springmysql.financial.model.Portfolio;
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
public class UserController {


    @Autowired
    UserService userService;
    @Autowired
    StockService stockService;
    @Autowired
    TradeService tradeService;
    @Autowired
    PortfolioService portfolioService;

    @RequestMapping(value = "user/home", method = RequestMethod.GET)
    public ModelAndView userHome() {

        ModelAndView modelAndView = new ModelAndView("user/home");

        Trade newTrade = new Trade();
        modelAndView.addObject("newTrade", newTrade);

        List<Stock> stocks = new ArrayList<>();
        stockService.findAll().forEach(stocks::add);
        modelAndView.addObject("stocks", stocks);

        List<Portfolio> portfolios = new ArrayList<>();
        portfolioService.findAll().forEach(portfolios::add);
        modelAndView.addObject("portfolios", portfolios);

        List<Trade> trades = new ArrayList<>();
        tradeService.findAll().forEach(trades::add);
        modelAndView.addObject("trades", trades);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentUser", user);
        return modelAndView;
    }

    @RequestMapping(value = "user/history", method = RequestMethod.GET)
    public ModelAndView userHistory() {
        ModelAndView modelAndView = new ModelAndView("user/home");


        return modelAndView;
    }
}
