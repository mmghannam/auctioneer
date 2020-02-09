import java.util.List;

public class Bidder_ahFlexible implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();
  private static final double factor = 1.5;
  private int day = 1;

  // given your value for the day, determine an action
  public double getBid(double v) {
    if (day <= 1000){
        return Math.min(v, budget);
    }
    else if (day <= 9000){
        return Math.min(v * 0.5, budget);
    }
    return Math.min(v , budget);
  }

  // callback function with results
  public void addResults(List<Double> bids, int myBid, double myPayment) {
    day += 1;
    // record my utility and budget
    if (myBid >= 0) {
      budget -= myPayment;
    }
  }
}
