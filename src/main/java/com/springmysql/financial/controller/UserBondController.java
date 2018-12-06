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
public class UserBondController {


    @Autowired
    UserService userService;
    @Autowired
    BondService bondService;
    @Autowired
    BondTradeService bondTradeService;
    @Autowired
    BondPortfolioService bondPortfolioService;

    @RequestMapping(value = "user/market/bond", method = RequestMethod.GET)
    public ModelAndView bond(){
        ModelAndView modelAndView = new ModelAndView("user/market/bond");
        List<Bond> bonds = new ArrayList<>();
        bondService.findAll().forEach(bonds::add);
        modelAndView.addObject("bonds", bonds);

        return modelAndView;
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
            bondPortfolioService.save(new BondPortfolio(auth.getName(), buyNewBond.getBondName(), 1, buyNewBond.getValue()));
        } else {
            bondPortfolio.setQuantity(bondPortfolio.getQuantity() + 1);
            bondPortfolio.setAsset(bondPortfolio.getAsset() + buyNewBond.getValue());
            bondPortfolioService.save(bondPortfolio);
        }
    }

    @RequestMapping(value = "user/market/bonds", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Bond> getBonds() {
        List<Bond> bonds = new ArrayList<>();
        bondService.findAll().forEach(bonds::add);
        return bonds;
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
            bondPortfolio.setAsset(bondPortfolio.getAsset() - bondTrade.getValue());
        }
        bondTrade.setReturned(bondTrade.getReturned() + 1);
        bondTradeService.save(bondTrade);
        bondPortfolio.setQuantity(bondPortfolio.getQuantity() - 1);
        userService.save(currUser);
        bondPortfolioService.save(bondPortfolio);
    }


    @RequestMapping(value = "user/bondnportfolio/quantity", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public int getBondQuantity(@RequestParam("bondName") String bondName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        BondPortfolio bondPortfolio = bondPortfolioService.findByUsernameAndBondname(auth.getName(), bondName);
        if (bondPortfolio != null) return bondPortfolio.getQuantity();
        else return 0;
    }

    @RequestMapping(value = "user/history/filterBond", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<BondTrade> filterBond(@RequestParam("bondName") String bondName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<BondTrade> bondTrades = new ArrayList<>();
        bondTradeService.findAllByUserNameAndBondName(auth.getName(), bondName).forEach(bondTrades::add);
        return bondTrades;
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

    @RequestMapping(value = "user/history/bond", method = RequestMethod.GET)
    public ModelAndView bondHistory() {
        ModelAndView modelAndView = new ModelAndView("user/history/bond");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<BondTrade> bondTrades = new ArrayList<>();
        bondTradeService.findAllByUsernameOrderByDatetimeDesc(auth.getName()).forEach(bondTrades::add);
        modelAndView.addObject("bondTrades", bondTrades);

        List<BondPortfolio> bondPortfolios = new ArrayList<>();
        bondPortfolioService.findAllByUsername(auth.getName()).forEach(bondPortfolios::add);
        modelAndView.addObject("bondPortfolios", bondPortfolios);
        return modelAndView;
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
                return 0.0;
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
            return 0.0;
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
