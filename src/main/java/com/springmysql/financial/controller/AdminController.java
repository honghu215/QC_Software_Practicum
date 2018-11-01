package com.springmysql.financial.controller;

import com.springmysql.financial.model.Option;
import com.springmysql.financial.model.Stock;
import com.springmysql.financial.model.Trade;
import com.springmysql.financial.model.User;
import com.springmysql.financial.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import javax.validation.Valid;
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
    @Autowired
    OptionService optionService;


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

        List<Option> options = new ArrayList<>();
        optionService.findAll().forEach(options::add);
        modelAndView.addObject("options", options);

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

    @RequestMapping(value = "/admin/home", method = RequestMethod.POST)
    public ModelAndView createStock(@Valid Stock newStock, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Stock stockExists = stockService.findByStockId(newStock.getStockId());
        if (stockExists != null) {
            bindingResult
                    .rejectValue("stockId", "error.stock",
                            "There is already a stock added");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/admin/home");
        } else {
            stockService.saveStock(newStock);
            modelAndView.addObject("successMessage", "Stock has been added successfully");
            modelAndView.setViewName("/admin/home");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/admin/deleteUser", method = RequestMethod.GET)
    @Transactional
    public ModelAndView deleteUser(@RequestParam("id") String id){
        userService.deleteUser(Integer.parseInt(id));
        return new ModelAndView("redirect:/admin/home");
    }

    @RequestMapping(value = "/admin/deleteStock", method = RequestMethod.GET)
    @Transactional
    public ModelAndView deleteStock(@RequestParam("id") String id) {
        stockService.deleteByStockId(Integer.parseInt(id));
        return new ModelAndView("redirect:/admin/home");
    }

    @RequestMapping(value = "/admin/addStock", method = RequestMethod.POST)
    public ModelAndView addStock(@ModelAttribute("newStock") Stock newStock){
        stockService.saveStock(newStock);
        return new ModelAndView("redirect:/admin/home");
    }
}