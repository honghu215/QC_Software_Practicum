package com.springmysql.financial.controller;

import com.springmysql.financial.model.*;
import com.springmysql.financial.service.*;
import org.decimal4j.util.DoubleRounder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    @Autowired
    IndexService indexService;
    @Autowired
    BondService bondService;

    private final int faceValue = 100;

    @RequestMapping(value = "admin/users", method = RequestMethod.GET)
    public ModelAndView getUsers(){
        ModelAndView modelAndView = new ModelAndView("admin/users");
        List<User> users = new ArrayList<>();
        userService.findAll().forEach(users::add);
        modelAndView.addObject("users", users);

        return modelAndView;
    }

    @RequestMapping(value = "admin/deleteUser", method = RequestMethod.GET)
    @Transactional
    public ModelAndView deleteUser(@RequestParam("id") int userId) {
        userService.deleteUser(userId);
        return new ModelAndView("redirect:/admin/users");
    }
//    @RequestMapping(value = "admin/addStock", method = RequestMethod.GET)
//    public ModelAndView addStock() {
//        ModelAndView modelAndView = new ModelAndView("/admin/addStock");
//        Stock newStock = new Stock();
//        modelAndView.addObject("newStock", newStock);
//        return modelAndView;
//    }
    @RequestMapping(value = "/admin/securities", method = RequestMethod.GET)
    public ModelAndView getSecurities(){
        ModelAndView modelAndView = new ModelAndView();

        Stock newStock = new Stock();
        modelAndView.addObject("newStock", newStock);

        Option newOption = new Option();
        modelAndView.addObject("newOption", newOption);

        Index newIndex = new Index();
        modelAndView.addObject("newIndex", newIndex);

        Bond newBond = new Bond();
        modelAndView.addObject("newBond", newBond);

        List<Bond> bonds = new ArrayList<>();
        bondService.findAll().forEach(bonds::add);
        modelAndView.addObject("bonds", bonds);

        List<Index> indexes = new ArrayList<>();
        indexService.findAll().forEach(indexes::add);
        modelAndView.addObject("indexes", indexes);

        List<Option> options = new ArrayList<>();
        optionService.findAll().forEach(options::add);
        modelAndView.addObject("options", options);

        List<Stock> stocks = new ArrayList<>();
        stockService.findAll().forEach(stocks::add);
        modelAndView.addObject("stocks", stocks);

        modelAndView.setViewName("admin/securities");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/securities/stock", method = RequestMethod.GET)
    public ModelAndView allStocks() {
        ModelAndView modelAndView = new ModelAndView("admin/securities/stock");

        Stock stock = new Stock();
        modelAndView.addObject("newStock", stock);

        List<Stock> stocks = new ArrayList<>();
        stockService.findAll().forEach(stocks::add);
        modelAndView.addObject("stocks", stocks);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/securities/option", method = RequestMethod.GET)
    public ModelAndView adminOption(){
        ModelAndView modelAndView = new ModelAndView("admin/securities/option");
        Option newOption = new Option();
        modelAndView.addObject("newOption", newOption);

        List<Option> options = new ArrayList<>();
        optionService.findAll().forEach(options::add);
        modelAndView.addObject("options", options);

        List<Stock> stocks = new ArrayList<>();
        stockService.findAll().forEach(stocks::add);
        modelAndView.addObject("stocks", stocks);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/securities/bond", method = RequestMethod.GET)
    public ModelAndView adminBond() {
        ModelAndView modelAndView = new ModelAndView("admin/securities/bond");
        Bond newBond = new Bond();
        modelAndView.addObject("newBond", newBond);
        List<Bond> bonds = new ArrayList<>();
        bondService.findAll().forEach(bonds::add);
        modelAndView.addObject("bonds", bonds);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/securities/index", method = RequestMethod.GET)
    public ModelAndView adminIndex() {
        ModelAndView modelAndView = new ModelAndView("admin/securities/index");
        Index newIndex = new Index();
        modelAndView.addObject("newIndex", newIndex);
        List<Index> indexes = new ArrayList<>();
        indexService.findAll().forEach(indexes::add);
        modelAndView.addObject("indexes", indexes);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/securities/addStock", method = RequestMethod.POST)
    public ModelAndView addStock(@ModelAttribute("newStock") Stock newStock){
        newStock.setPrice( (double)(Math.round( ((Double) Math.random() * 50 + 50) * 100)) / 100 );
        stockService.saveStock(newStock);
        return new ModelAndView("redirect:/admin/securities/stock");
    }

    @RequestMapping(value = "admin/securities/addOption", method = RequestMethod.POST)
    public ModelAndView addOption(@ModelAttribute("newOption") Option newOption) {
        System.out.println(newOption);
        Calendar cal = Calendar.getInstance();
        cal.setTime(newOption.getExpiration());
        String priceStr = String.format("%08d", (int)(newOption.getStrikePrice()*100));

        newOption.setOccCode(newOption.getUnderlying().substring(0, 4).toUpperCase() + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH) + cal.get(Calendar.YEAR)
                + newOption.getPutCall().toUpperCase().charAt(0) + priceStr);
        optionService.save(newOption);
        return new ModelAndView("redirect:/admin/securities");
    }

    @RequestMapping(value = "/admin/securities/addIndex", method = RequestMethod.POST)
    public ModelAndView addIndex(@ModelAttribute("newIndex") Index newIndex){
        newIndex.setIndexValue( (double)(Math.round( ((Double) Math.random() * 50 + 50) * 100)) / 100 );
        indexService.saveIndex(newIndex);
        return new ModelAndView("redirect:/admin/securities");
    }

    @RequestMapping(value = "/admin/securities/addBond", method = RequestMethod.POST)
    public ModelAndView addBond(@ModelAttribute("newBond") Bond newBond) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(newBond.getCreatedOn());
//        LocalDate createdOn = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
//        newBond.setCreatedOn(createdOn);
//        //        cal.set(cal.get(Calendar.YEAR)+newBond.getMaturityLength(), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
//        System.out.println(cal);
//        newBond.setMaturity(cal.getTime());
        newBond.setBondValue( (double)(Math.round( ((Double) Math.random() * 50 + 50) * 100)) / 100 );
        newBond.setMaturity(LocalDate.of(newBond.getCreatedOn().getYear()+newBond.getMaturityLength(), newBond.getCreatedOn().getMonth(), newBond.getCreatedOn().getDayOfMonth()));
        bondService.save(newBond);
        return new ModelAndView("redirect:/admin/securities");
    }

    @RequestMapping(value = "/admin/securities/deleteBond", method = RequestMethod.GET)
    @Transactional
    public ModelAndView deleteBond(@RequestParam("id") String id) {
        bondService.deleteByBondId(Integer.parseInt(id));
        return new ModelAndView("redirect:/admin/securities");
    }


    @RequestMapping(value = "/admin/securities/deleteOption", method = RequestMethod.GET)
    @Transactional
    public ModelAndView deleteOption(@RequestParam("OCC") String OCC) {
        optionService.deleteByOCC(OCC);
        return new ModelAndView("redirect:/admin/securities");
    }

    @RequestMapping(value = "/admin/securities/deleteStock", method = RequestMethod.GET)
    @Transactional
    public ModelAndView deleteStock(@RequestParam("id") String id){
        stockService.deleteByStockId(Integer.parseInt(id));
        return new ModelAndView("redirect:/admin/securities");
    }

    @RequestMapping(value = "/admin/securities/deleteIndex", method = RequestMethod.GET)
    @Transactional
    public ModelAndView deleteIndex(@RequestParam("id") String id){
        indexService.deleteByIndexId(Integer.parseInt(id));
        return new ModelAndView("redirect:/admin/securities");
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
