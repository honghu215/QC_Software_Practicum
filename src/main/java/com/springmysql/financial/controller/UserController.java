package com.springmysql.financial.controller;

import com.springmysql.financial.model.*;
import com.springmysql.financial.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    @Autowired
    BondService bondService;
    @Autowired
    OptionService optionService;
    @Autowired
    IndexService indexService;


    @RequestMapping(value = "user/home", method = RequestMethod.GET)
    public ModelAndView userHome() {
        ModelAndView modelAndView = new ModelAndView("user/home");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentUser", user);
        return modelAndView;
    }

    @RequestMapping(value = "user/market", method = RequestMethod.GET)
    public ModelAndView userMarket() {
        ModelAndView modelAndView = new ModelAndView("user/market");

        List<Stock> stocks = new ArrayList<>();
        stockService.findAll().forEach(stocks::add);
        modelAndView.addObject("stocks", stocks);

        List<Bond> bonds = new ArrayList<>();
        bondService.findAll().forEach(bonds::add);
        modelAndView.addObject("bonds", bonds);

        List<Option> options = new ArrayList<>();
        optionService.findAll().forEach(options::add);
        modelAndView.addObject("options", options);

        List<Index> indexes = new ArrayList<>();
        indexService.findAll().forEach(indexes::add);
        modelAndView.addObject("indexes", indexes);

        return modelAndView;
    }

    @RequestMapping(value = "user/portfolio", method = RequestMethod.GET)
    public ModelAndView userPortfolio() {
        ModelAndView modelAndView = new ModelAndView("user/portfolio");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Portfolio> portfolios = new ArrayList<>();
        portfolioService.findAllByUserNameAndQUantityNot(auth.getName(), 0).forEach(portfolios::add);
        modelAndView.addObject("portfolios", portfolios);

        return modelAndView;
    }

    @RequestMapping(value = "user/history", method = RequestMethod.GET)
    public ModelAndView userHistory() {
        ModelAndView modelAndView = new ModelAndView("user/history");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByEmail(auth.getName());

        List<Portfolio> portfolios = new ArrayList<>();
        portfolioService.findAllByUserName(auth.getName()).forEach(portfolios::add);
        modelAndView.addObject("portfolios", portfolios);

        List<Trade> trades = new ArrayList<>();
        tradeService.findAllByUserNameOrderByDatetimeDesc(auth.getName()).forEach(trades::add);
        modelAndView.addObject("trades", trades);

        return modelAndView;
    }

    @RequestMapping(value = "user/history/filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Trade> filter(@RequestParam("stockName") String stockName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Trade> trades = new ArrayList<>();
        tradeService.findAllByUserNameAndStockNameOrderByDatetimeDesc(auth.getName(), stockName).forEach(trades::add);
        return trades;
    }
    @RequestMapping(value = "user/market/trade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void buyStock(@RequestBody Trade newTrade) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        tradeService.save(newTrade);
        Portfolio portfolio = portfolioService.findByUserNameAndStockName(newTrade.getUserName(), newTrade.getStockName());
        if (portfolio != null) {
            portfolio.setQuantity(portfolio.getQuantity() + newTrade.getQuantity());
            portfolioService.save(portfolio);
        } else {
            portfolioService.save(new Portfolio(newTrade.getUserName(), newTrade.getStockName(), newTrade.getQuantity()));
        }
        double totalCost = newTrade.getQuantity() * newTrade.getStockPrice();
        if (newTrade.getQuantity() > 0) {
            currentUser.setBalance((double)Math.round((currentUser.getBalance() - totalCost) * 100) / 100);
        }
        else currentUser.setBalance((double)Math.round((currentUser.getBalance() - totalCost) * 100) / 100);
        userService.saveUser(currentUser);
    }



    @RequestMapping(value = "user/market/getStockPrice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getStockPrice(@RequestParam("stockName") String stockName){
        Stock stock = stockService.findStockByStockName(stockName);
        return String.valueOf(stock.getPrice());
    }

    @RequestMapping(value = "user/market/getBalance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getBalance() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        System.out.println("Got user's balance = " + currentUser.getBalance());
        return String.valueOf(currentUser.getBalance());
    }

    @RequestMapping(value = "user/market/calculate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String calculateYield(@RequestParam("bondName") String bondName,
                                 @RequestParam("yield") String yield,
                                 @RequestParam("method") String method) {
        Bond currBond = bondService.findByBondName(bondName);
        LocalDate now = LocalDate.now();
        Period intervalPeriod = Period.between(currBond.getCreatedOn(), now);
        int diffMonths = intervalPeriod.getMonths() + intervalPeriod.getYears() * 12;
        if (method.equals("yield")) {
            return String.valueOf((double) Math.round((new calYield().sentBack(diffMonths, currBond.getCoupon(), currBond.getBondValue())) * 10000) / 10000);
        } else {
            return String.valueOf((double) Math.round((new calYield().func(Double.parseDouble(yield), diffMonths, currBond.getCoupon())) * 10000) / 10000);

        }
    }

    private class calYield {
        double func(double x, int n,double c) {
            double Sum = 0.0;
            double down = 1.0 + 0.5 * x;
            double top = 0.5 * c;
            for (int i = 0; i <= n; i++) {
                if (i == n) {
                    Sum = Sum + (100 + top) / Math.pow(down, i);
                    break;
                }
                Sum = Sum + (top / Math.pow(down, i));
            }
            return Sum;
        }

        double root_bisection(double target, double tol_f, double tol_x, int max_iter,
                              double x_low, double x_high, int n, double c) {
            double x = 0.0;
            double y_low = func(x_low, n, c);
            double diff_y_low = y_low - target;
            if (Math.abs(diff_y_low) <= tol_f) {
                x = x_low;
                return x;
            }
            double y_high = func(x_high, n, c);
            double diff_y_high = y_high - target;
            if (Math.abs(diff_y_high) <= tol_f) {
                x = x_high;
                return x;
            }
            if (diff_y_low * diff_y_high > 0.0) {
                x = 0;
                return -1.0;
            }
            for (int i = 1; i < max_iter; ++i) {
                x = (x_low + x_high) / 2.0;
                double y = func(x, n, c);
                double diff_y = y - target;
                if (Math.abs(diff_y) <= tol_f)  return x;
                if (diff_y * diff_y_low > 0.0) x_low = x;
                else x_high = x;

                if (Math.abs(x_high - x_low) <= tol_x) return x;
            }
            return -1.0;
        }

        double sentBack(int n, double coupon,double BondValue) {

            int max_iter = 100;
            double tol_f =  1.0e-4;
            double tol_x =  1.0e-4;
            double x_low = 0.0;
            double x_high = 100.0;

            double yield = 0;

            yield = root_bisection(BondValue, tol_f, tol_x, max_iter,
                    x_low, x_high,n,coupon);
            return yield*0.1;
        }
    }
}
