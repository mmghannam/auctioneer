import java.util.List;

public class Bidder_ja_truthful_factor implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();
  private static final double factor = 0.5;

  public double getBid(double v) {
    return v * factor;
  }

  // callback function with results
  public void addResults(List<Double> bids, int myBid, double myPayment) {

  }
}
