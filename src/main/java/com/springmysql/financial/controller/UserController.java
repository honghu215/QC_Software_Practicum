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
import java.time.temporal.ChronoUnit;
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
    @Autowired
    OptionTradeService optionTradeService;
    @Autowired
    BondTradeService bondTradeService;


    @RequestMapping(value = "user/home", method = RequestMethod.GET)
    public ModelAndView userHome() {
        ModelAndView modelAndView = new ModelAndView("user/home");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentUser", user);

        List<Option> options = new ArrayList<>();
        optionService.findAll().forEach(options::add);
        modelAndView.addObject("options", options);

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

    @RequestMapping(value = "user/market/option", method = RequestMethod.GET)
    public ModelAndView option() {
        ModelAndView modelAndView = new ModelAndView("user/market/option");
        List<Option> options = new ArrayList<>();
        optionService.findAll().forEach(options::add);
        modelAndView.addObject("options", options);
        return modelAndView;
    }

    @RequestMapping(value = "user/market/stock", method = RequestMethod.GET)
    public ModelAndView stock(){
        ModelAndView modelAndView = new ModelAndView("user/market/stock");
        List<Stock> stocks = new ArrayList<>();
        stockService.findAll().forEach(stocks::add);
        modelAndView.addObject("stocks", stocks);

        return modelAndView;
    }

    @RequestMapping(value = "user/market/bond", method = RequestMethod.GET)
    public ModelAndView bond(){
        ModelAndView modelAndView = new ModelAndView("user/market/bond");
        List<Bond> bonds = new ArrayList<>();
        bondService.findAll().forEach(bonds::add);
        modelAndView.addObject("bonds", bonds);

        return modelAndView;
    }

    @RequestMapping(value = "user/market/index", method = RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("user/market/index");
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

        List<Portfolio> portfolios = new ArrayList<>();
        portfolioService.findAllByUserName(auth.getName()).forEach(portfolios::add);
        modelAndView.addObject("portfolios", portfolios);

        List<Trade> trades = new ArrayList<>();
        tradeService.findAllByUserNameOrderByDatetimeDesc(auth.getName()).forEach(trades::add);
        modelAndView.addObject("trades", trades);

        return modelAndView;
    }

    @RequestMapping(value = "user/history/stock", method = RequestMethod.GET)
    public ModelAndView stockHistory() {
        ModelAndView modelAndView = new ModelAndView("user/history/stock");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<Trade> trades = new ArrayList<>();
        tradeService.findAllByUserNameOrderByDatetimeDesc(auth.getName()).forEach(trades::add);
        modelAndView.addObject("trades", trades);

        List<Portfolio> portfolios = new ArrayList<>();
        portfolioService.findAllByUserName(auth.getName()).forEach(portfolios::add);
        modelAndView.addObject("portfolios", portfolios);

        return modelAndView;
    }

    @RequestMapping(value = "user/history/option", method = RequestMethod.GET)
    public ModelAndView optionHistory(){
        ModelAndView modelAndView = new ModelAndView("user/history/option");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<OptionTrade> optionTrades = new ArrayList<>();
        optionTradeService.findAllByUsernameOrderByDatetimeDesc(auth.getName()).forEach(optionTrades::add);
        modelAndView.addObject("optionTrades", optionTrades);

        return modelAndView;
    }

    @RequestMapping(value = "user/history/bond", method = RequestMethod.GET)
    public ModelAndView bondHistory() {
        ModelAndView modelAndView = new ModelAndView("user/history/bond");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<BondTrade> bondTrades = new ArrayList<>();
        bondTradeService.findAllByUsernameOrderByDatetimeDesc(auth.getName()).forEach(bondTrades::add);
        modelAndView.addObject("bondTrades", bondTrades);
        return modelAndView;
    }

    @RequestMapping(value = "user/bonds", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<BondTrade> allBonds() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return bondTradeService.findAllByUsernameOrderByDatetimeDesc(auth.getName());
    }

    @RequestMapping(value = "user/adjustBalance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void adjust(@RequestParam("value") double value,
                       @RequestParam("bondName") String bondName,
                       @RequestParam("frequency") int frequency){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currUser = userService.findUserByEmail(auth.getName());
        currUser.setBalance((double)Math.round((currUser.getBalance() + value)*10000)/10000);
        userService.saveUser(currUser);
        BondTrade bondTrade = bondTradeService.findByUsernameAndBondName(auth.getName(), bondName);
        bondTrade.setReturned(frequency);
        bondTradeService.save(bondTrade);
    }

    @RequestMapping(value = "user/history/filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Trade> filter(@RequestParam("stockName") String stockName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Trade> trades = new ArrayList<>();
        tradeService.findAllByUserNameAndStockNameOrderByDatetimeDesc(auth.getName(), stockName).forEach(trades::add);
        return trades;
    }



    @RequestMapping(value = "user/market/optionTrade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void buyOption(@RequestBody OptionTrade newTrade){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        optionTradeService.save(newTrade);
        currentUser.setBalance((double)Math.round((currentUser.getBalance() - newTrade.getStrikePrice())*10000)/10000);
        userService.saveUser(currentUser);
    }

    @RequestMapping(value = "user/market/bondTrade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void buyBond(@RequestBody BondTrade buyNewBond){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        bondTradeService.save(buyNewBond);
        currentUser.setBalance((double)Math.round((currentUser.getBalance() - buyNewBond.getValue())*10000)/10000);
        userService.saveUser(currentUser);
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
        return String.valueOf(currentUser.getBalance());
    }

    @RequestMapping(value = "user/market/calculate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String calculateYield(@RequestParam("bondName") String bondName,
                                 @RequestParam("yield") String yield,
                                 @RequestParam("couponValue") String couponValue,
                                 @RequestParam("couponValue1") String couponValue1,
                                 @RequestParam("bondValue") String bondValue,
                                 @RequestParam("method") String method) {
        Bond currBond = bondService.findByBondName(bondName);
        LocalDate now = LocalDate.now();
        LocalDate createON = currBond.getCreatedOn();
        int issueTimes = currBond.getMaturityLength()*2;
        LocalDate issueDate = createON;
        LocalDate [] issueDateT = new LocalDate[issueTimes];
        int count = 0;
        long add = 6;

        for (int i =1; i <= issueTimes; i++)
        {
            //issueDate = LocalDate.of(createON.getYear(), now.getMonth().plus(add), now.getDayOfMonth());
            issueDate = issueDate.plusMonths(add);
            if(now.isBefore(issueDate))
            {
                issueDateT[count] = issueDate;
                count ++;
                //System.out.print("test");
            }

        }
        double [] dateValue = new double[count];

        for (int i =0; i < count; i++)
        {
            long daydiff = ChronoUnit.DAYS.between(now,issueDateT[i]);
            dateValue[i] = daydiff/ 365.25;

        }


        if (method.equals("yield")) {
            return String.valueOf((double) Math.round((new calYield().sentBack(Double.parseDouble(couponValue), Double.parseDouble(bondValue),dateValue,count)) * 10000) / 10000);
        } else {
            return String.valueOf((double) Math.round((new calYield().func(Double.parseDouble(yield),Double.parseDouble(couponValue1),dateValue,count)) * 10000) / 10000);

        }
    }

    private class calYield {
        double func(double x, double c,double[]dateValue,int len) {
            double Sum = 0.0;
            double down = 1.0 + 0.5 * x;
            double top = 0.5 * c;
            for (int i = 0; i < len; i++) {
                if (i == len-1) {
                    Sum = Sum + (100 + top) / Math.pow(down, dateValue[i]*2);
                    break;
                }
                Sum = Sum + (top / Math.pow(down, dateValue[i]*2));
            }
            return Sum;
        }

        double root_bisection(double target, double tol_f, double tol_x, int max_iter,
                              double x_low, double x_high, double c,double[]dateValue,int len) {
            double x = 0.0;
            double y_low = func(x_low, c,dateValue,len);
            double diff_y_low = y_low - target;
            if (Math.abs(diff_y_low) <= tol_f) {
                x = x_low;
                return x;
            }
            double y_high = func(x_high, c,dateValue,len);
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
                double y = func(x, c,dateValue,len);
                double diff_y = y - target;
                if (Math.abs(diff_y) <= tol_f)  return x;
                if (diff_y * diff_y_low > 0.0) x_low = x;
                else x_high = x;

                if (Math.abs(x_high - x_low) <= tol_x) return x;
            }
            return -1.0;
        }

        double sentBack(double coupon,double BondValue,double[]dateValue,int count) {

            int max_iter = 100;
            double tol_f =  1.0e-4;
            double tol_x =  1.0e-4;
            double x_low = 0.0;
            double x_high = 100.0;



            double yield = 0;

            yield = root_bisection(BondValue, tol_f, tol_x, max_iter,
                    x_low, x_high,coupon,dateValue,count);
            return yield;
        }
    }
}