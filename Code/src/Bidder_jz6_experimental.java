// Sample code for PS4 problem 4
// COS 445 SD4, Spring 2018
// Created by Jonathan Zhang

import java.util.Arrays;
import java.util.List;

public class Bidder_jz6_experimental implements Bidder {
  double cash = Auctioneer.defaultConfig.getBudget();
  double[] avrgBids = new double[Auctioneer.defaultConfig.getRates().length];
  int rounds = 0;
  int roundsLeft = Auctioneer.defaultConfig.getDays();
  int totalRounds = Auctioneer.defaultConfig.getDays();
  // This one is more experimental and eventually was simplified down to the v/2 model
  public double getBid(double v) {
    double[] weights = {
      0.6601863864966815,
      -0.44050297744775085,
      -0.3288461116450685,
      0.7354115471274265,
      0.6528564967694372,
      -0.021270821816517627,
      -0.751090383677677
    };
    if (roundsLeft == 0) {
      return Math.min(v * weights[0], cash);
    }
    if (roundsLeft / totalRounds < weights[1] * 333) {
      if (v > avrgBids[avrgBids.length - 1]) {
        return Math.min(v * weights[2], cash);
      } else if (v > avrgBids[avrgBids.length - 2]) {
        return Math.min(avrgBids[avrgBids.length - 2] - 1, cash);
      } else if (v > avrgBids[0] && v < avrgBids[1]) {
        return Math.min(v * weights[3], cash);
      }
    }
    return Math.min(v * weights[4], cash);
  }

  public void addResults(List<Double> lbids, int k, double price) {
    double[] bids = lbids.stream().mapToDouble(Double::doubleValue).toArray();
    Arrays.sort(bids);
    if (k >= 0) {
      cash -= price;
    }

    for (int i = 0; i < avrgBids.length; i++) {
      avrgBids[i] = (avrgBids[i] * rounds + bids[i]) / (rounds + 1);
    }

    rounds++;
    roundsLeft--;
  }
}
