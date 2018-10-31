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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
public class Controller {

    @Autowired
    private UserService userService;
    @Autowired
    private StockService stockService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private PortfolioService portfolioService;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

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

//    @RequestMapping(value = "/user/newTrade", method = RequestMethod.POST)
//    @Transactional
//    public ModelAndView newTrade(
//            @RequestParam("stockName") String stockName,
//            @RequestParam("stockPrice") String stockPrice,
//            @RequestParam("quantity") int quantity,
//            @RequestParam("buy") Boolean buy){
//        if(!buy) quantity = 0 - quantity;
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByEmail(auth.getName());
//        Date date = new Date();
//        System.out.println("Adding new trade: " + user.getId() + " " + stockName + " " + stockPrice + " " + quantity + " " + date);
//
//        Trade newTrade = new Trade(user.getId(), stockName, stockPrice, quantity, date);
//        tradeService.save(newTrade);
//
//        Portfolio existPortfolio = portfolioService.findByUserIdAndStockName(user.getId(), stockName);
//        if (existPortfolio != null)
//            existPortfolio.setQuantity(quantity + existPortfolio.getQuantity());
//        else {
//            portfolioService.save(new Portfolio(user.getId(), stockName, quantity));
//        }
//
//        return new ModelAndView("redirect:/user/home");
//
//    }

}
