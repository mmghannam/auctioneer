// PS5_problem4.java: implementation for Bidder
// COS 445, Spring 2017
// Created by:
// Andrew  Wonnacott ajw4
// Maryam  Bahrani   mbahrani
// Andreea Magalie   amagalie

import java.util.List;

public class Bidder_ajw4_amagalie_mbahrani implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();
  private int roundsLeft = Auctioneer.defaultConfig.getDays();

  // given your value for the day, determine an action
  public double getBid(double v) {
    roundsLeft--;

    // try to put a bunch of money into something few people would bid on
    if (v > 500) return Math.min(budget, v);

    // P(Any future round Above v) = P(Not All rounds Not Above v)
    double p = 1 - Math.pow(1 - 1 / v, roundsLeft);
    // v * likelihood that we ever get a value above v again
    double bid = p * v;

    // never bid above budget, never bid below Double.MIN_NORMAL
    return Math.min(Math.max(bid, Double.MIN_NORMAL), budget);
  }

  // callback function with results
  public void addResults(List<Double> bids, int k, double price) {
    // record my utility and budget
    if (k != -1) {
      budget -= price;
    }
  }
}
