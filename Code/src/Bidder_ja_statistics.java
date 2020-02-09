import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bidder_ja_statistics implements Bidder {
  private double budget = Auctioneer.defaultConfig.getBudget();
  int num_day=0;
  double sum_bid10=0;
  double[] sum_Bids = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  double[] var =  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  double[] won = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

  // given your value for the day, determine an action
  public double getBid(double v) {
    num_day += 1;
    if (sum_bid10 == 0){
      return 0.5 * v;
    }
    double mu10 = sum_Bids[9]/num_day;
    double mu1 = sum_Bids[0]/num_day;
    double bid = Math.min(mu10 + (mu1-mu10)*v/200, v);

    return bid;
  }

  // callback function with results
  public void addResults(List<Double> bids, int myBid, double myPayment) {
    won[myBid+1] ++;
    for (int i = 0; i < 10; i++){
      sum_Bids[i] += bids.get(i);
      var[i] += bids.get(i)*bids.get(i);
    }

    sum_bid10 += bids.get(8);
    // record my utility and budget
    if (myBid >= 0) {
      budget -= myPayment;
    }
  }
}
