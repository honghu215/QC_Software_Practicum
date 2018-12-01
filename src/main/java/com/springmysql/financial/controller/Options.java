package com.springmysql.financial.controller;

public class Options {
    public double r,q,sigma,T,K;
    public boolean isCall,isAmerican;
    public Options()
    {
        r = 0;
        q = 0;
        sigma = 0;
        T = 0;
        K = 0;
        isCall = false;
        isAmerican = false;
    }


    public double TerminalPayoff(double S){
        double intrinsic = 0;
        if (isCall)
        {
            if (S > K)
                intrinsic = S - K;
        }
        else
        {
            if (S < K)
                intrinsic = K - S;
        }
        return intrinsic;
    }



    public double ValuationTests(double S, double val) {

        if (isAmerican)
        {
            double intrinsic = 0;
            if (isCall)
            {
                if (S > K)
                    intrinsic = S - K;
            }
            else
            {
                if (S < K)
                    intrinsic = K - S;
            }
            val = Math.max(val, intrinsic);

            return val;
        }

        return val;

    }
}
