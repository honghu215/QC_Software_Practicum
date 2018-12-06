package com.springmysql.financial.controller;


import com.springmysql.financial.model.*;
import com.springmysql.financial.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class UserOptionController {

    @Autowired
    UserService userService;
    @Autowired
    OptionService optionService;
    @Autowired
    PortfolioService portfolioService;
    @Autowired
    OptionTradeService optionTradeService;
    @Autowired
    OptionPortfolioService optionPortfolioService;
    @Autowired
    StockService stockService;

    @RequestMapping(value = "user/market/option", method = RequestMethod.GET)
    public ModelAndView option() {
        ModelAndView modelAndView = new ModelAndView("user/market/option");
        List<Option> options = new ArrayList<>();
        optionService.findAll().forEach(options::add);
        modelAndView.addObject("options", options);
        return modelAndView;
    }

    @RequestMapping(value = "user/market/optionTrade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void buyOption(@RequestBody OptionTrade newTrade){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        OptionPortfolio optionPortfolio = optionPortfolioService.findByUserNameAndOptionName(auth.getName(), newTrade.getOptionName());
        if (optionPortfolio == null) {
            optionPortfolioService.save(new OptionPortfolio(auth.getName(), newTrade.getOptionName(), 1, newTrade.getOptionValue()));
        }
        else {
            optionPortfolio.setQuantity(optionPortfolio.getQuantity() + 1);
            optionPortfolio.setAsset(optionPortfolio.getAsset() + newTrade.getOptionValue());
        }

        optionTradeService.save(newTrade);
        System.out.println(newTrade);
        currentUser.setBalance((double)Math.round((currentUser.getBalance() - newTrade.getOptionValue())*10000)/10000);
        userService.save(currentUser);
    }

    @RequestMapping(value = "user/market/options", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Option> getOptions() {
        List<Option> options = new ArrayList<>();
        optionService.findAll().forEach(options::add);
        return options;
    }


    @RequestMapping(value = "user/optionportfolio/quantity", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public int getOptionQuantity(@RequestParam("optionName") String optionName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OptionPortfolio optionPortfolio =  optionPortfolioService.findByUserNameAndOptionName(auth.getName(), optionName);
        if (optionPortfolio != null) return optionPortfolio.getQuantity();
        else return 0;
    }

    @RequestMapping(value = "user/history/filterOption", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<OptionTrade> filterOption(@RequestParam("optionName") String optionName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<OptionTrade> optionTrades = new ArrayList<>();
        optionTradeService.findAllByUserNameAndOptionName(auth.getName(), optionName).forEach(optionTrades::add);
        return optionTrades;
    }

    @RequestMapping(value = "user/exercise", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void exercise(@RequestParam("tradeId") int tradeId,
                         @RequestParam("putCall") String putCall) {
        System.out.println("Id: " + tradeId + ", Put/Call: " + putCall);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OptionTrade trade = optionTradeService.findByUsernameAndId(auth.getName(), tradeId);
        OptionPortfolio optionPortfolio = optionPortfolioService.findByUserNameAndOptionName(auth.getName(), trade.getOptionName());
        StockPortfolio stockPortfolio = portfolioService.findByUserNameAndStockName(auth.getName(), trade.getUnderlying());
        User currUser = userService.findUserByEmail(auth.getName());
        Stock stock = stockService.findStockByStockName(trade.getUnderlying());
        if (putCall.equals("Put")) {
            System.out.println("Exercising put!");
            currUser.setBalance((double)Math.round((currUser.getBalance() + trade.getStrikePrice()) * 100) / 100);
            stockPortfolio.setQuantity(stockPortfolio.getQuantity() - 1);
            stockPortfolio.setAsset(stockPortfolio.getAsset() - stock.getPrice());
        }
        else if (putCall.equals("Call")) {
            System.out.println("Exercising call!");
            currUser.setBalance((double)Math.round((currUser.getBalance() - trade.getStrikePrice()) * 100) / 100);
            stockPortfolio.setQuantity(stockPortfolio.getQuantity() + 1);
            stockPortfolio.setAsset(stockPortfolio.getAsset() + stock.getPrice());
        }
        userService.save(currUser);
        portfolioService.save(stockPortfolio);
        optionPortfolio.setQuantity(optionPortfolio.getQuantity() - 1);
        optionPortfolio.setAsset(optionPortfolio.getAsset() - optionService.findByOptionName(trade.getOptionName()).getOptionValue());
        optionPortfolioService.save(optionPortfolio);
        portfolioService.save(stockPortfolio);
    }


    @RequestMapping(value = "/user/option/deleteExpired", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deleteExpired(@RequestParam("optionTradeId") int optionTradeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OptionTrade optionTrade = optionTradeService.findByUsernameAndId(auth.getName(), optionTradeId);
        OptionPortfolio optionPortfolio = optionPortfolioService.findByUserNameAndOptionName(auth.getName(), optionTrade.getOptionName());
        if (optionPortfolio != null) optionPortfolioService.delete(optionPortfolio);
    }

    @RequestMapping(value = "user/history/option", method = RequestMethod.GET)
    public ModelAndView optionHistory(){
        ModelAndView modelAndView = new ModelAndView("user/history/option");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<OptionTrade> optionTrades = new ArrayList<>();
        optionTradeService.findAllByUsernameOrderByDatetimeDesc(auth.getName()).forEach(optionTrades::add);
        modelAndView.addObject("optionTrades", optionTrades);

        List<OptionPortfolio> optionPortfolios = new ArrayList<>();
        optionPortfolioService.findAllByUsername(auth.getName()).forEach(optionPortfolios::add);
        modelAndView.addObject("optionPortfolios", optionPortfolios);
        return modelAndView;
    }

    @RequestMapping(value = "user/market/calculate1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String calculateYield1(@RequestParam("optionName") String optionName,
                                  @RequestParam("method") String method,
                                  @RequestParam("riskFree1") String riskFree1,
                                  @RequestParam("stockDividend1") String stockDividend1,
                                  @RequestParam("target") String Target,
                                  @RequestParam("step1") String step1,
                                  @RequestParam("stockPrice1") String stockPrice1,
                                  @RequestParam("riskFree") String riskFree,
                                  @RequestParam("stockDividend") String stockDividend,
                                  @RequestParam("stockVolatility") String stockVolatility,
                                  @RequestParam("step") String step,
                                  @RequestParam("stockPrice") String stockPrice
    ) {
        Option currOption = optionService.findByOptionName(optionName);
        LocalDate now = LocalDate.now();
        Date t_0=java.sql.Date.valueOf( now );

        Date expiration = currOption.getExpiration();
        Date create = currOption.getCreatedOn();

        long diff = t_0.getTime() - create.getTime();
        long diffi = expiration.getTime() - create.getTime();

        double days = (double) ((diff) / (1000 * 60 * 60 * 24));
        double days1 = (double) ((diffi) / (1000 * 60 * 60 * 24));

        double ExT = days1/365;
        double t0 = days/365;

        //double ExT = 1;
        //double t0 = 0;


        String type = currOption.getAmeEur();
        //System.out.print(type);
        boolean isAmerican1;
        if(type.equals("Ame"))
        {
            isAmerican1 = true;
            //System.out.print(type);
        }
        else
        {
            isAmerican1 = false;
        }

        String type1 = currOption.getPutCall();
        boolean isCall1;
        if(type1.equals("Call"))
        {
            isCall1 = true;
        }
        else
        {
            isCall1 = false;
        }

        int n = Integer.parseInt(step);
        double S = Double.parseDouble(stockPrice);
        int n1 = Integer.parseInt(step1);
        double S1 = Double.parseDouble(stockPrice1);

        Options op = new Options();
        op.isAmerican = isAmerican1;
        op.isCall = isCall1;
        op.K = currOption.getStrikePrice();
        op.q = Double.parseDouble(stockDividend);
        op.r = Double.parseDouble(riskFree);
        op.sigma = Double.parseDouble(stockVolatility);
        op.T = ExT;



        Options op1 = new Options();
        op1.isAmerican = isAmerican1;
        op1.isCall = isCall1;
        op1.K = currOption.getStrikePrice();
        op1.q = Double.parseDouble(stockDividend1);
        op1.r = Double.parseDouble(riskFree1);
        double target = Double.parseDouble(Target);
        op1.T = ExT;



        if (method.equals("option")) {
            return String.valueOf((double) Math.round((new BinomialModel(n).FairValue(n,op,S,t0)) * 10000) / 10000);
        } else {
            return String.valueOf((double) Math.round((new BinomialModel(n).ImpliedVolatility(n1,op1,S1,t0,target)) * 10000) / 10000);

        }
    }
}
