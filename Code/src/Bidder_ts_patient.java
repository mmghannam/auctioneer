import java.util.List;

public class Bidder_ts_patient implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();
  private double midbudget=0;   //own budget after 5500 rounds
  private int t=0;      //current round
  public double getBid(double v) {
  
    if(t==5500)
        midbudget=budget;
    //bid truthful if valuation is high
    if(v>10)   
         //this case will only occur roughly 1000 times over 10,000 rounds
        return Math.min(budget,v);  
    else if(t>=5500) 
         //start bidding on everything after 5,500 rounds, truthful will have almost no budget at this point
        return Math.min(budget,midbudget*20/4500);       
    else 
        return 0;
  }

  public void addResults(List<Double> bids, int myBid, double myPayment) {
    t++;
    if (myBid >= 0) {
      budget -= myPayment;
    }
  }
}
