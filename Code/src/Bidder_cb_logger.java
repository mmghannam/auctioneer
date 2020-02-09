// Sample code for PS4 problem 4
// COS 445 SD4, Spring 2019
// Created by Andrew Wonnacott

import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class Bidder_cb_logger implements Bidder {
  private double budget;
  private static final double factor = 1;
  private int day = 0;
  private boolean notify = false;
  private int number_won_auctions = 0;
  private FileWriter writer;
  public Bidder_cb_logger() {
    budget = Auctioneer.defaultConfig.getBudget();
    try {
      writer = new FileWriter("bids.csv");
    }
    catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  // given your value for the day, determine an action
  public double getBid(double v) {
    return 0;
  }

  // callback function with results
  public void addResults(List<Double> bids, int mySlot, double myPayment){
    // record my utility and budget
    day++;
    try {
      writer.append(Integer.toString(day));
      for (int i = 0; i<bids.size();i++)
      {
        writer.append(","+Double.toString(bids.get(i)));
      }
      writer.append("\n");
      if (day == 10000) {
        writer.flush();
        writer.close();
      }
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }
}
