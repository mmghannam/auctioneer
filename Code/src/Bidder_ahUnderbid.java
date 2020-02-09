import java.util.List;

public class Bidder_ahUnderbid implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();
  private static final double factor = 0.5;
  private int round = 0;

  // given your value for the day, determine an action
  public double getBid(double v) {
    //round += 1;
    if (v > 0){
        return Math.min(v * factor, budget);
    }
    return Math.min(v, budget);
  }

  // callback function with results
  public void addResults(List<Double> bids, int myBid, double myPayment) {
    // record my utility and budget
    if (myBid >= 0) {
      budget -= myPayment;
    }
  }
}
