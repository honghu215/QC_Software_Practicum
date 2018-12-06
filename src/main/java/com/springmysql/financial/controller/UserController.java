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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    @Autowired
    OptionPortfolioService optionPortfolioService;
    @Autowired
    BondPortfolioService bondPortfolioService;


    @RequestMapping(value = "user/home", method = RequestMethod.GET)
    public ModelAndView userHome() {
        ModelAndView modelAndView = new ModelAndView("user/home");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("currentUser", user);

        List<OptionTrade> optionTrades = new ArrayList<>();
        optionTradeService.findAllByUsernameOrderByDatetimeDesc(auth.getName()).forEach(optionTrades::add);
        modelAndView.addObject("optionTrades", optionTrades);

        List<BondTrade> bondTrades = new ArrayList<>();
        bondTradeService.findAllByUsernameOrderByDatetimeDesc(auth.getName()).forEach(bondTrades::add);
        modelAndView.addObject("bondTrades", bondTrades);

        return modelAndView;
    }



    @RequestMapping(value = "user/adjustBalance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void adjust(@RequestParam("value") double value,
                       @RequestParam("bondName") String bondName,
                       @RequestParam("frequency") int frequency){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currUser = userService.findUserByEmail(auth.getName());
        currUser.setBalance((double)Math.round((currUser.getBalance() + value)*100)/100);
        userService.save(currUser);
        BondTrade bondTrade = bondTradeService.findByUsernameAndBondName(auth.getName(), bondName);
        bondTrade.setReturned(frequency);
        bondTradeService.save(bondTrade);
    }


    @RequestMapping(value = "user/market/getBalance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getBalance() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        return String.valueOf(currentUser.getBalance());
    }

    @RequestMapping(value = "user/getAsset", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public double getAsset() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        double asset = 0.0;
        asset += portfolioService.findAllByUserName(auth.getName()).stream().mapToDouble(StockPortfolio::getAsset).sum();
        asset += bondPortfolioService.findAllByUsername(auth.getName()).stream().mapToDouble(BondPortfolio::getAsset).sum();
        asset += optionPortfolioService.findAllByUsername(auth.getName()).stream().mapToDouble(OptionPortfolio::getAsset).sum();
        return asset;
    }


    @RequestMapping(value = "user/portfolio", method = RequestMethod.GET)
    public ModelAndView userPortfolio() {
        ModelAndView modelAndView = new ModelAndView("user/portfolio");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<StockPortfolio> stockPortfolios = new ArrayList<>();
        portfolioService.findAllByUserNameAndQUantityNot(auth.getName(), 0).forEach(stockPortfolios::add);
        modelAndView.addObject("stockPortfolios", stockPortfolios);

        List<OptionPortfolio> optionPortfolios = new ArrayList<>();
        optionPortfolioService.findAllByUsername(auth.getName()).forEach(optionPortfolios::add);
        modelAndView.addObject("optionPortfolios", optionPortfolios);

        List<BondPortfolio> bondPortfolios = new ArrayList<>();
        bondPortfolioService.findAllByUsername(auth.getName()).forEach(bondPortfolios::add);
        modelAndView.addObject("bondPortfolios", bondPortfolios);
        return modelAndView;
    }





}



