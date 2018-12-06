package com.springmysql.financial.controller;

import com.springmysql.financial.model.Stock;
import com.springmysql.financial.model.StockPortfolio;
import com.springmysql.financial.model.Trade;
import com.springmysql.financial.model.User;
import com.springmysql.financial.service.PortfolioService;
import com.springmysql.financial.service.StockService;
import com.springmysql.financial.service.TradeService;
import com.springmysql.financial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserStockController {


    @Autowired
    UserService userService;
    @Autowired
    StockService stockService;
    @Autowired
    TradeService tradeService;
    @Autowired
    PortfolioService portfolioService;

    @RequestMapping(value = "user/market/stock", method = RequestMethod.GET)
    public ModelAndView stock(){
        ModelAndView modelAndView = new ModelAndView("user/market/stock");
        List<Stock> stocks = new ArrayList<>();
        stockService.findAll().forEach(stocks::add);
        modelAndView.addObject("stocks", stocks);

        return modelAndView;
    }

    @RequestMapping(value = "user/market/trade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void buyStock(@RequestBody Trade newStockTrade) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        tradeService.save(newStockTrade);
        double totalCost = (double)Math.round( (newStockTrade.getQuantity() * newStockTrade.getStockPrice()) * 100) / 100;
        StockPortfolio stockPortfolio = portfolioService.findByUserNameAndStockName(newStockTrade.getUserName(), newStockTrade.getStockName());
        if (stockPortfolio != null) {
            stockPortfolio.setQuantity(stockPortfolio.getQuantity() + newStockTrade.getQuantity());
            stockPortfolio.setAsset(stockPortfolio.getAsset() + totalCost);
        } else {
            stockPortfolio = new StockPortfolio(newStockTrade.getUserName(), newStockTrade.getStockName(), newStockTrade.getQuantity(), totalCost);
        }

        currentUser.setBalance((double)Math.round((currentUser.getBalance() - totalCost) * 100) / 100);
        userService.save(currentUser);
        portfolioService.save(stockPortfolio);
    }

    @RequestMapping(value = "user/market/getStockPrice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getStockPrice(@RequestParam("stockName") String stockName){
        Stock stock = stockService.findStockByStockName(stockName);
        return String.valueOf(stock.getPrice());
    }

    @RequestMapping(value = "user/stockportfolio/quantity", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public int getStockQuantity(@RequestParam("stockName") String stockName){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        StockPortfolio stockPortfolio = portfolioService.findByUserNameAndStockName(auth.getName(), stockName);
        if (stockPortfolio == null) return 0;
        System.out.println("Quantity: " + stockPortfolio.getQuantity());
        return stockPortfolio.getQuantity();
    }

    @RequestMapping(value = "user/market/stocks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Stock> getStocks() {
        List<Stock> stocks = new ArrayList<>();
        stockService.findAll().forEach(stocks::add);
        return stocks;
    }

    @RequestMapping(value = "user/history/filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Trade> filter(@RequestParam("stockName") String stockName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Trade> trades = new ArrayList<>();
        tradeService.findAllByUserNameAndStockNameOrderByDatetimeDesc(auth.getName(), stockName).forEach(trades::add);
        return trades;
    }

    @RequestMapping(value = "user/history/stock", method = RequestMethod.GET)
    public ModelAndView stockHistory() {
        ModelAndView modelAndView = new ModelAndView("user/history/stock");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<Trade> trades = new ArrayList<>();
        tradeService.findAllByUserNameOrderByDatetimeDesc(auth.getName()).forEach(trades::add);
        modelAndView.addObject("trades", trades);

        List<StockPortfolio> stockPortfolios = new ArrayList<>();
        portfolioService.findAllByUserName(auth.getName()).forEach(stockPortfolios::add);
        modelAndView.addObject("stockPortfolios", stockPortfolios);

        return modelAndView;
    }

}
