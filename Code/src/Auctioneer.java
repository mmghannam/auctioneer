// Auctioneer.java: Testing code for auctions assignment
// COS 445 SD5, Spring 2019
// Created by Cyril Zhang
// Modified by Andrew Wonnacott

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Auctioneer extends Tournament<Bidder, AuctionConfig> {
    Auctioneer(List<String> bidderNames) {
        super(Bidder.class, bidderNames);
    }

    public static double ER(double maxValue) {
        return 1.0 / Math.max(1.0 / maxValue, rand.nextDouble());
    }

    public double[] runTrial(List<Class<? extends Bidder>> strategies, AuctionConfig config) {
        final int NUM_DAYS = config.getDays();
        final double[] RATES = config.getRates();
        final double INITIAL_BUDGET = config.getBudget();
        final double MAX_VALUE = config.getMaxValue();

        // initiate data serializer
        TrialData data = new TrialData(strategies, config);

        if (strategies.size() <= RATES.length) {
            throw new RuntimeException(
                "Too few bidders: "
                    + Integer.toString(strategies.size())
                    + " (need "
                    + Integer.toString(RATES.length + 1)
                    + ")");
        }

        List<Bidder> bidders = new ArrayList<Bidder>();
        for (Class<? extends Bidder> bidderClass : strategies) {
            try {
                bidders.add(bidderClass.getDeclaredConstructor().newInstance());
            } catch (ReflectiveOperationException roe) {
                throw new RuntimeException(roe);
            }
        }

        double[] budget = new double[bidders.size()];
        double[] utility = new double[bidders.size()];
        // Set up budgets and current payoffs
        for (int i = 0; i < bidders.size(); ++i) {
            budget[i] = INITIAL_BUDGET;
            utility[i] = INITIAL_BUDGET;
        }

        for (int t = 0; t < NUM_DAYS; ++t) {
            // draw each player's value; get their bid
            double[] values = new double[bidders.size()];
            double[] bids = new double[bidders.size()];

            // get bids and values
            for (int i = 0; i < bidders.size(); i++) {
                values[i] = ER(MAX_VALUE);
                bids[i] = bidders.get(i).getBid(values[i]);
                assert !Double.isNaN(bids[i]) : bidders.get(i).getClass().getSimpleName() + ": NaN bid";
                assert bids[i] >= 0
                    : bidders.get(i).getClass().getSimpleName() + ": negative bid: " + bids[i];
                if (Double.isNaN(bids[i]) || bids[i] < 0) {
                    // When running without assertions, keep going in a sane way
                    bids[i] = 0;
                }
                if (bids[i] * RATES[0] > budget[i]) {
                    if (Auctioneer.class.desiredAssertionStatus()) {
                        System.err.println(
                            bidders.get(i).getClass().getSimpleName()
                                + ": bid ("
                                + bids[i]
                                + ") potentially above budget ("
                                + budget[i]
                                + ") at maximum rate ("
                                + RATES[0]
                                + ")");
                    }
                    bids[i] = budget[i] / RATES[0];
                }
            }

            // save bids and values
            data.days[t].bids = bids;
            data.days[t].values = values;

            // argsort bids
            Integer[] idx = new Integer[bidders.size()];
            for (int i = 0; i < bidders.size(); i++) {
                idx[i] = i;
            }
            Collections.shuffle(Arrays.asList(idx));
            Arrays.sort(
                idx,
                new Comparator<Integer>() {
                    @Override
                    public int compare(final Integer o1, final Integer o2) {
                        return Double.compare(bids[o2], bids[o1]);
                    }
                });

            // save sorted ids
            data.days[t].sortedIDsByBids = idx;

            // determine winners
            List<Double> winning_bids =
                Collections.unmodifiableList(
                    IntStream.range(0, RATES.length)
                        .boxed()
                        .map(i -> bids[idx[i]])
                        .collect(Collectors.toList()));

            // give feedback
            for (int i = 0; i < bidders.size(); i++) {
                if (i < RATES.length) {
                    double paid = RATES[i] * bids[idx[i + 1]];
                    data.days[t].payments[idx[i]] = paid;

                    bidders.get(idx[i]).addResults(winning_bids, i, paid);

                    utility[idx[i]] += RATES[i] * values[idx[i]] - paid;
                    budget[idx[i]] -= paid;
                } else {
                    bidders.get(idx[i]).addResults(winning_bids, -1, Double.NaN);
                }
            }

            // save budgets and utilities
            data.days[t].budgets = Arrays.copyOf(budget, budget.length);
            data.days[t].utilities = Arrays.copyOf(utility, utility.length);
        }

        try {
            data.toCSV("x");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return utility;
    }

    public static final AuctionConfig defaultConfig;

    static {
        defaultConfig =
            new AuctionConfig(
                new double[]{.050, .035, .030, .015, .015, .015, .015, .015, .015, .015},
                500,
                10000,
                200);
    }


    // ported functions
    private static void printScores(List<String> strategyNames, double[] res, Integer N) {
        Benchmarks.printScores(strategyNames, res, N);
    }

    private static List<String> loadStrategies(String fileName) throws FileNotFoundException {
        return Benchmarks.loadStrategies(fileName);
    }

    private static double[] getResults(List<String> strategyNames, int numTrials) {
        return Benchmarks.getResults(strategyNames, numTrials);
    }

    public static void main(String[] args) throws java.io.FileNotFoundException {
        final int numTrials = 1;
        assert args.length >= 1 : "Expected filename of strategies as first argument";
        final List<String> strategyNames = loadStrategies(args[0]);
        final int N = strategyNames.size();
        double[] res = getResults(strategyNames, numTrials);


        String[] strategies = {
            "truthful",
            "ba_Communism",
            "ba_Divided",
            "FourFifths",
            "MinNormal",
            "ba_Minus",
            "One",
            "OneFifth",
            "OneHalf",
            "ThreeFifths",
            "TwoFifths",
            "ahConsiderHistory",
            "ahFlexible",
            "ahLastRound",
            "ahMoneyThief",
            "ahTruthfulHighValues",
            "ahUnderbid",
            "ahUnderbidFlexible",
            "ajw4_amagalie_mbahrani",
            "cb_logger",
            "cb_overunder",
            "cb_startlater",
            "cb_truthfulmin50",
            "cb_under",
            "gb_divrandom",
            "gb_greedy",
            "gb_increasing",
            "gb_overbidder",
            "gb_pessimistic",
            "gb_random",
            "gb_truthful05",
            "ja_probability",
            "ja_probability_improved",
            "ja_statistics",
            "ja_truthful_factor",
            "jk_estimate_bids",
            "jk_half_bid",
            "jk_proportional_factor",
            "jk_truthful_inverse",
            "jz6_experimental",
            "jz6_normBoth",
            "jz6_normDay",
            "jz6_normValue",
            "jz6_raw",
            "jz6_updated",
            "mg_adapter",
            "mg_expector",
            "mg_proportional",
            "ts_oppurtunist",
            "ts_patient",
            "ts_vscale",
            "yv_1",
            "yv_2",
            "yv_3",
        };

        printScores(strategyNames, res, N);
//    nOnNTest(strategies, numTrials, 1);
//    homogenousTest(strategies, numTrials);
//    survivalOfTheFittestTest(strategies, 15, 0.1, numTrials);
    }

}
