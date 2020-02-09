// Sample code for PS4 problem 4
// COS 445 SD4, Spring 2019
// Created by Andrew Wonnacott

import java.util.List;

public class Bidder_cb_startlater implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();
  private static final double factor = 1;
  private int day = 0;
  private boolean notify = true;
  private int number_won_auctions = 0;

  // given your value for the day, determine an action
  public double getBid(double v) {
    if (day <= 1000) {
      return 0;
    }
    return Math.min(v * factor, budget);
  }

  // callback function with results
  public void addResults(List<Double> bids, int mySlot, double myPayment) {
    // record my utility and budget
    day++;
    if (mySlot >= 0) {
      budget -= myPayment;
      number_won_auctions++;
/*      if (notify && myPayment > 0 && budget <=5) {
        notify = false;
        System.out.println("Round "+day+" late starter");
        System.out.println("Number of auctions won "+number_won_auctions);
      }
*/
    }
  }
}
