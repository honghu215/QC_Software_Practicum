package com.springmysql.financial.controller;

public class BinomialModel {
    private int n_tree;
    private double [][] stock_nodes;
    private double [][] value_nodes;


    public BinomialModel(int n)
    {
        n_tree = n;
        stock_nodes = new double[n_tree + 1][n_tree + 1];
        value_nodes= new double[n_tree + 1][n_tree + 1];

    }


    static double FairValue(int n,Options op, double S, double t0)
    {
        double V = 0;
        if (n < 1)
            return 1;
        if (S <= 0)
            return 1;
        if (op.T <= t0)
            return 1;
        if (op.sigma <= 0.0)
            return 1;

        double dt = (op.T - t0) /n;
        double df = Math.exp(-op.r*dt);
        double growth = Math.exp((op.r - op.q)*dt);
        double u = Math.exp(op.sigma* Math.sqrt(dt));
        double d = 1.0 / u;
        double p_prob = (growth - d) / (u - d);
        if (p_prob < 0.0 || p_prob > 1.0)
            return 1;
        double q_prob = 1.0 - p_prob;
        BinomialModel BM = new BinomialModel(n);

        double S_tmp[] = BM.stock_nodes[0];
        S_tmp[0] = S;
        for (int i = 1; i <= n; ++i) {
            double prev[] = BM.stock_nodes[i - 1];
            S_tmp = BM.stock_nodes[i];
            S_tmp[0] = prev[0] * d;

            for (int j = 1; j <= n; ++j)
            {
                S_tmp[j] = S_tmp[j - 1] * u*u;
            }
        }

        int i = n;
        S_tmp = BM.stock_nodes[i];
        double V_tmp[] = BM.value_nodes[i];
        for (int j = 0; j <= n; ++j)
        {
            V_tmp[j] = op.TerminalPayoff(S_tmp[j]);

        }

        for (i = n - 1; i >= 0; --i) {
            S_tmp = BM.stock_nodes[i];
            V_tmp = BM.value_nodes[i];
            double V_next[] = BM.value_nodes[i + 1];
            for (int j = 0; j <= i; ++j)
            {
                V_tmp[j] = df*(p_prob*V_next[j + 1] + q_prob*V_next[j]);
                V_tmp[j]= op.ValuationTests(S_tmp[j], V_tmp[j]);
            }
        }

        V_tmp = BM.value_nodes[0];
        V = V_tmp[0];

        return V;
    }


    static double ImpliedVolatility(int n, Options op,double S, double t0, double target)
    {
        double tol = 1.0e-4;
        int max_iter = 100;
        double implied_vol = 0;
        double sigma_low = 0.01;
        double sigma_high = 3.0;
        double FV_low = 0;
        double FV_high = 0;
        double FV = 0;

        op.sigma = sigma_low;
        FV_low = FairValue(n, op, S, t0);
        double diff_FV_low = FV_low - target;
        double absolute = Math.abs(diff_FV_low);

        if(absolute <= tol)
        {
            implied_vol = op.sigma;
            return implied_vol;
        }



        op.sigma = sigma_high;
        FV_high =FairValue(n, op, S, t0);
        double diff_FV_high = FV_high - target;
        if (Math.abs(diff_FV_high) <= tol) {
            implied_vol = op.sigma;
            return implied_vol;
        }

        if (diff_FV_low * diff_FV_high > 0) {
            implied_vol = 0;
            return 0;
        }

        for (int i = 0; i < max_iter; i++)
        {
            op.sigma = 0.5*(sigma_low + sigma_high);
            FV = FairValue(n, op, S, t0);
            double diff_FV = FV - target;

            if (Math.abs(diff_FV) <= tol)
            {
                implied_vol = op.sigma;
                return implied_vol;
            }

            if ((diff_FV_low * diff_FV) > 0)
            {
                sigma_low = op.sigma;
            }

            else sigma_high = op.sigma;

            if (Math.abs(sigma_low - sigma_high) <= tol)
            {
                implied_vol = op.sigma;
                return implied_vol;
            }



        }


        implied_vol = 0;
        return 0;


    }
}
