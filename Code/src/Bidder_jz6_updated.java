// Sample code for PS4 problem 4
// COS 445 SD4, Spring 2018
// Created by Jonathan Zhang
// Modified by Andrew Wonnacott

import java.util.Arrays;
import java.util.List;

public class Bidder_jz6_updated implements Bidder {
  double cash = 5000;
  double[] avrgBids = new double[10];
  int rounds = 0;
  int roundsLeft = 10000;
  int totalRounds = 10000;

  public double getBid(double v) {
    double[] weights = {
      0.8981463627835975,
      -0.8306496476992359,
      0.01900849603219046,
      -0.32724026825739494,
      0.7497329839063056,
      0.7937431886826185,
      -0.8025398495098759
    };
    double valueMult = Math.pow(v * weights[0], weights[1]);
    double roundsMult = Math.pow(rounds * weights[2], weights[3]);
    double end = roundsMult * valueMult;
    double averages = 0;
    if (v > avrgBids[avrgBids.length - 1]) {
      averages = weights[4];
    } else {
      averages = weights[5];
    }
    end *= averages;
    double power = Math.pow(end, weights[6]);

    return Math.max(0, Math.min(cash, power));
  }

  public void addResults(List<Double> lbids, int k, double price) {
    double[] bids = lbids.stream().mapToDouble(Double::doubleValue).toArray();
    Arrays.sort(bids);
    if (k >= 0) {
      assert !Double.isNaN(price) : k;
      cash -= price;
    }

    for (int i = 0; i < avrgBids.length; i++) {
      assert !Double.isNaN(bids[i]);
      avrgBids[i] = (avrgBids[i] * rounds + bids[i]) / (rounds + 1);
    }

    rounds++;
    roundsLeft--;
  }
}
