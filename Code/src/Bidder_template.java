// Sample code for PS4 problem 4
// COS 445 SD4, Spring 2019
// Created by Andrew Wonnacott

import java.util.List;

public abstract class Bidder_template implements Bidder {
    protected double budget = Auctioneer.defaultConfig.getBudget();
    protected double factor = 0.5;
    private int currentDay = 0;
    private static boolean[] dayCalculationsDone = new boolean[Auctioneer.defaultConfig.getDays()];
    protected static double topBids[][] = new double[10][Auctioneer.defaultConfig.getDays()];
    protected static double averageTopBids[] = new double[10];


    // given your value for the day, determine an action
    public double getBid(double v) {
        return Math.min(v * factor, budget);
    }

    // callback function with results
    public void addResults(List<Double> bids, int myBid, double myPayment) {
        // record my utility and budget
        if (myBid >= 0) {
            budget -= myPayment;
        }
        if (!dayCalculationsDone[currentDay]) {
            // record top bids
            for (int player = 0; player < bids.size(); player++) {
                topBids[player][currentDay] = bids.get(player);
            }
            updateAverageTopBids();
            dayCalculationsDone[currentDay] = true;
        }
        // run strategy
        strategize(bids, myBid, myPayment, currentDay);
        currentDay++;
    }

    public abstract void strategize(List<Double> bids, int myBid, double myPayment, int currentDay);

    protected boolean IWon(double payment) {
        return payment > 0;
    }

    private void updateAverageTopBids() {
        for (int player = 0; player < 10; player++) {
            double sum = 0;
            for (int day = 0; day <= currentDay; day++) {
                sum += topBids[player][day];
            }
            averageTopBids[player] = sum / (currentDay + 1);
        }
    }

    protected int winningPosition(double myBid, List<Double> bids) {
        int position = -1;
        for (int i = 0; i < bids.size(); i++) {
            if (myBid == bids.get(i)) {
                position = i;
                break;
            }
        }
        return position;
    }
}
