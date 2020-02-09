import java.util.List;

public class Bidder_ts_oppurtunist implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();

  public double getBid(double v) {

    if (v>7){
        //roughly 1500 occurrences in 10000 rounds
        //so we will save our budget for valuations with a higher potential payoff
        //bidding more than v is never good, it is definitely worse for you but only maybe worse for the others
        //bidding more than v can cause us to pay more than v which is bad
        return Math.min(budget*19,v);}  //19 because 20 may cause us to overbid(rounding errors)
        
    else
        return 0;
  }

  public void addResults(List<Double> bids, int myBid, double myPayment) {

    if (myBid >= 0) {
      budget -= myPayment;
    }
  }
}
