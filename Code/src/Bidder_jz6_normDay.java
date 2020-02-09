// Sample code for PS4 problem 4
// COS 445 SD4, Spring 2018
// Created by Jonathan Zhang
// Modified by Andrew Wonnacott

import java.util.ArrayList;
import java.util.List;

public class Bidder_jz6_normDay implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();
  private int maxDays = Auctioneer.defaultConfig.getDays();
  private int roundsLeft = Auctioneer.defaultConfig.getDays();
  private List<Double> topValuesFromPrevRounds = new ArrayList<Double>();
  private static final double w0 = 0.286;
  private static final double w1 = -0.985;
  private static final double w2 = 0.031;
  private static final double w3 = -0.327;
  private static final double w4 = 0.96;
  private static final double w5 = 0.13;
  private static final double w6 = -0.986;

  // given your value for the day, determine an action
  public double getBid(double v) {
    // day is zero indexed and increasing
    int day = maxDays - roundsLeft;

    roundsLeft--;

    double averageTopValueFromPrevRounds =
        topValuesFromPrevRounds.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(Double.NaN);

    if (!Double.isNaN(averageTopValueFromPrevRounds) && v > averageTopValueFromPrevRounds) {
      double bid = Math.pow(Math.pow(v * w0, w1) * Math.pow(day * w2, w3) * w4, w6);
      return Math.min(Math.min(v, bid), budget);
    } else {
      double bid = Math.pow(Math.pow(v * w0, w1) * Math.pow(day * w2, w3) * w5, w6);
      return Math.min(Math.min(v, bid), budget);
    }
  }

  // callback function with results
  public void addResults(List<Double> bids, int myBid, double myPayment) {
    // record my utility and budget
    if (myBid >= 0) {
      budget -= myPayment;
    }

    // compute average bid
    topValuesFromPrevRounds.add(bids.get(0));
  }
}
