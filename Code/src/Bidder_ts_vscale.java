import java.util.List;

public class Bidder_ts_vscale implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();

  
  public double getBid(double v) {

    return budget*(v/1000);  //bid part of your budget proportional to v
        //extreme case: v=200, return budget/5=100 in round 1
        // so we bid less than the truthful strategy, which leaves us with more budget over the course of the rounds.
  }


  public void addResults(List<Double> bids, int myBid, double myPayment) {

    if (myBid >= 0) 
      budget -= myPayment;
    
  }
}
