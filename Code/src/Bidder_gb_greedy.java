// Sample code for PS4 problem 4
// COS 445 SD4, Spring 2019
// Created by Andrew Wonnacott

import java.util.*;

public class Bidder_gb_greedy implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();
  private double[] rates = Auctioneer.defaultConfig.getRates();
  private List<Double> previous_bids;

  private int getIdMaxVal(Map<Integer, Double> passedMap) {
    int id = 0;
    double max_val = passedMap.get(id);
    for (int i = 1; i < passedMap.size(); ++i) {
      if (passedMap.get(i) > max_val){
        id = i;
        max_val = passedMap.get(i);
      }
    }
    return id;
  }

  // given your value for the day, determine an action
  public double getBid(double v) {
    if (previous_bids == null) {
      return Math.min(v, budget);
    }

    Map<Integer, Double> payoffs = new HashMap<Integer, Double>();

    for (int i = 0; i < rates.length; ++i) {
      double prev_bid = previous_bids.get(i);
      payoffs.put(i, (v - prev_bid) * rates[i]);
    }

    int idMaxVal = getIdMaxVal(payoffs);

    return Math.min(previous_bids.get(idMaxVal), budget);
  }

  // callback function with results
  public void addResults(List<Double> bids, int myBid, double myPayment) {
    // record my utility and budget
    this.previous_bids = bids;

    if (myBid >= 0) {
      budget -= myPayment;
    }
  }
}
