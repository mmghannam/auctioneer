
import java.util.List;

public class Bidder_ahTruthfulHighValues implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();

  // given your value for the day, determine an action
  public double getBid(double v) {
    if (v > 80){
        return Math.min(v, budget);
    }
    return Math.min(v * 0.5,budget);
  }

  // callback function with results
  public void addResults(List<Double> bids, int myBid, double myPayment) {
    // record my utility and budget
    if (myBid >= 0) {
      budget -= myPayment;
    }
  }
}
