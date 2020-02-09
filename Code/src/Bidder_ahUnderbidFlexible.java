import java.util.List;

public class Bidder_ahUnderbidFlexible implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();
  private static final double factor = 1.5;
  private int day = 1;

  // given your value for the day, determine an action
  public double getBid(double v) {
    if (budget >= 30){
       return Math.min(v * 0.5, budget);
    }
    return Math.min(v , budget);
  }

  // callback function with results
  public void addResults(List<Double> bids, int myBid, double myPayment) {
    // record my utility and budget
	day += 1;
    if (myBid >= 0) {
      budget -= myPayment;
    }
  }
}
