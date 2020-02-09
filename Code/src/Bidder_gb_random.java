// Sample code for PS4 problem 4
// COS 445 SD4, Spring 2019
// Created by Andrew Wonnacott

import java.util.List;

public class Bidder_gb_random implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();

  // given your value for the day, determine an action
  public double getBid(double v) {
    double min = 0;
    double max = budget;
    double range = max - min + 1;
    double rand = Math.random() * range + min;
    return Math.min(rand, budget);
  }

  // callback function with results
  public void addResults(List<Double> bids, int myBid, double myPayment) {
    // record my utility and budget
    if (myBid >= 0) {
      budget -= myPayment;
    }
  }
}
