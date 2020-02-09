// Sample code for PS4 problem 4
// COS 445 SD4, Spring 2019
// Created by Andrew Wonnacott

import java.util.List;

public class Bidder_gb_pessimistic implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();
  private double maxVal = Auctioneer.defaultConfig.getMaxValue();
  private int numDays = Auctioneer.defaultConfig.getDays();
  private static final double factor = 0.5;
  private static final double over_factor = 1.5;
  private double avg;

  public Bidder_gb_pessimistic(){
    double sum = 0;
    double num_reps = 10000;
    for (int i = 0; i < num_reps; ++i) {
      sum += Auctioneer.ER(maxVal);
    }

    avg = sum / num_reps;
  }

  // given your value for the day, determine an action
  public double getBid(double v) {
    if (v < avg){
      return 0.0;
    }

    return Math.min(v * factor, budget);
  }

  // callback function with results
  public void addResults(List<Double> bids, int myBid, double myPayment) {
    // record my utility and budget

    if (myBid >= 0) {
      budget -= myPayment;
    }
  }
}
