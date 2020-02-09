// Sample code for PS4 problem 4
// COS 445 SD4, Spring 2019
// Created by Andrew Wonnacott

import java.util.List;

public class Bidder_gb_overbidder implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();
  private int numDays = Auctioneer.defaultConfig.getDays();
  private double start_factor = 1.1;

  private int currDay = 1;

  // given your value for the day, determine an action
  public double getBid(double v) {
    if (numDays == 1) {
      return Math.min(v, budget);
    }

    if (currDay < (numDays * 1.0) / 2){
      return Math.min(v * start_factor, budget);
    } else {
      double factor = (currDay * 1.0) / numDays;
      return Math.min(v * factor, budget);
    }

  }

  // callback function with results
  public void addResults(List<Double> bids, int myBid, double myPayment) {
    // record my utility and budget
    if (myBid >= 0) {
      budget -= myPayment;
    }
    currDay++;
  }
}
