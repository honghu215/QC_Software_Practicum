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

    @RequestMapping(value = "user/history", method = RequestMethod.GET)
    public ModelAndView userHistory() {
        ModelAndView modelAndView = new ModelAndView("user/history");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<StockPortfolio> stockPortfolios = new ArrayList<>();
        portfolioService.findAllByUserName(auth.getName()).forEach(stockPortfolios::add);
        modelAndView.addObject("portfolios", stockPortfolios);

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

        List<StockPortfolio> stockPortfolios = new ArrayList<>();
        portfolioService.findAllByUserName(auth.getName()).forEach(stockPortfolios::add);
        modelAndView.addObject("stockPortfolios", stockPortfolios);

        return modelAndView;
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

    @RequestMapping(value = "user/history/bond", method = RequestMethod.GET)
    public ModelAndView bondHistory() {
        ModelAndView modelAndView = new ModelAndView("user/history/bond");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<BondTrade> bondTrades = new ArrayList<>();
        bondTradeService.findAllByUsernameOrderByDatetimeDesc(auth.getName()).forEach(bondTrades::add);
        modelAndView.addObject("bondTrades", bondTrades);
        return modelAndView;
    }

    @RequestMapping(value = "user/bondTrades", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<BondTrade> allBonds() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<String> bondPortfolioNames = new ArrayList<>();
        bondPortfolioService.findAllByUsernameAndQuantityNot(auth.getName(), 0).forEach(bondPortfolio -> bondPortfolioNames.add(bondPortfolio.getBondName()));

        List<BondTrade> bondTrades = new ArrayList<>();
        bondTradeService.findAllByUsernameOrderByDatetimeDesc(auth.getName()).forEach(bondTrade -> {
            if (bondPortfolioNames.contains(bondTrade.getBondName())) {
                bondTrades.add(bondTrade);
            }
        });

        return bondTrades;

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
        if (putCall.equals("Put")) {
            System.out.println("Exercising put!");
            currUser.setBalance((double)Math.round((currUser.getBalance() + trade.getStrikePrice()) * 100) / 100);
            stockPortfolio.setQuantity(stockPortfolio.getQuantity() - 1);
        }
        else if (putCall.equals("Call")) {
            System.out.println("Exercising call!");
            currUser.setBalance((double)Math.round((currUser.getBalance() - trade.getStrikePrice()) * 100) / 100);
            stockPortfolio.setQuantity(stockPortfolio.getQuantity() + 1);
        }
        userService.save(currUser);
        portfolioService.save(stockPortfolio);
        optionPortfolio.setQuantity(optionPortfolio.getQuantity() - 1);
        optionPortfolioService.save(optionPortfolio);
    }

    @RequestMapping(value = "user/history/filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Trade> filter(@RequestParam("stockName") String stockName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Trade> trades = new ArrayList<>();
        tradeService.findAllByUserNameAndStockNameOrderByDatetimeDesc(auth.getName(), stockName).forEach(trades::add);
        return trades;
    }

    @RequestMapping(value = "user/history/filterOption", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<OptionTrade> filterOption(@RequestParam("optionName") String optionName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<OptionTrade> optionTrades = new ArrayList<>();
        optionTradeService.findAllByUserNameAndOptionName(auth.getName(), optionName).forEach(optionTrades::add);
        return optionTrades;
    }

    @RequestMapping(value = "user/optionportfolio/quantity", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public int getOptionQuantity(@RequestParam("optionName") String optionName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return optionPortfolioService.findByUserNameAndOptionName(auth.getName(), optionName).getQuantity();
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

    @RequestMapping(value = "user/market/optionTrade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void buyOption(@RequestBody OptionTrade newTrade){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        OptionPortfolio optionPortfolio = optionPortfolioService.findByUserNameAndOptionName(auth.getName(), newTrade.getOptionName());
        if (optionPortfolio == null) {
            optionPortfolioService.save(new OptionPortfolio(auth.getName(), newTrade.getOptionName(), 1));
        }
        else {
            optionPortfolio.setQuantity(optionPortfolio.getQuantity() + 1);
        }

        optionTradeService.save(newTrade);
        System.out.println(newTrade);
        currentUser.setBalance((double)Math.round((currentUser.getBalance() - newTrade.getOptionValue())*10000)/10000);
        userService.save(currentUser);
    }

    @RequestMapping(value = "user/market/bondTrade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void buyBond(@RequestBody BondTrade buyNewBond){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        bondTradeService.save(buyNewBond);
        currentUser.setBalance((double)Math.round((currentUser.getBalance() - buyNewBond.getValue())*10000)/10000);
        userService.save(currentUser);

        BondPortfolio bondPortfolio = bondPortfolioService.findByUsernameAndBondname(auth.getName(), buyNewBond.getBondName());
        if (bondPortfolio == null) {
            bondPortfolioService.save(new BondPortfolio(auth.getName(), buyNewBond.getBondName(), 1));
        } else {
            bondPortfolio.setQuantity(bondPortfolio.getQuantity() + 1);
            bondPortfolioService.save(bondPortfolio);
        }
    }

    @RequestMapping(value = "user/redeemCoupon", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void redeem(@RequestParam("bondTradeId") int bondTradeId,
                       @RequestParam("expire") boolean expire) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currUser = userService.findUserByEmail(auth.getName());
        BondTrade bondTrade = bondTradeService.findByUsernameAndTradeId(auth.getName(), bondTradeId);
        BondPortfolio bondPortfolio = bondPortfolioService.findByUsernameAndBondname(auth.getName(), bondTrade.getBondName());
        if (! expire) {
            currUser.setBalance((double)Math.round((currUser.getBalance() + bondTrade.getCoupon())*100)/100);
        } else {
            currUser.setBalance((double)Math.round((currUser.getBalance() + 100.0)*100)/100);
        }
        bondTrade.setReturned(bondTrade.getReturned() + 1);
        bondTradeService.save(bondTrade);
        bondPortfolio.setQuantity(bondPortfolio.getQuantity() - 1);
        userService.save(currUser);
        bondPortfolioService.save(bondPortfolio);
    }


    @RequestMapping(value = "user/market/trade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void buyStock(@RequestBody Trade newTrade) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        tradeService.save(newTrade);
        StockPortfolio stockPortfolio = portfolioService.findByUserNameAndStockName(newTrade.getUserName(), newTrade.getStockName());
        if (stockPortfolio != null) {
            stockPortfolio.setQuantity(stockPortfolio.getQuantity() + newTrade.getQuantity());
            portfolioService.save(stockPortfolio);
        } else {
            portfolioService.save(new StockPortfolio(newTrade.getUserName(), newTrade.getStockName(), newTrade.getQuantity()));
        }
        double totalCost = newTrade.getQuantity() * newTrade.getStockPrice();
        if (newTrade.getQuantity() > 0) {
            currentUser.setBalance((double)Math.round((currentUser.getBalance() - totalCost) * 100) / 100);
        }
        else currentUser.setBalance((double)Math.round((currentUser.getBalance() - totalCost) * 100) / 100);
        userService.save(currentUser);
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