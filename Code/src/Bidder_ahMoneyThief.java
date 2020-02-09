import java.util.List;

public class Bidder_ahMoneyThief implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();
  private static final double factor = 1.5;
  private int day = 1;

  // given your value for the day, determine an action
  public double getBid(double v) {
    if (day < 1000){
        return Math.min(v * factor, budget);
    }
    return Math.min(0.5*v, budget);
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
