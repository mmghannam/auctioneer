import java.util.ArrayList;
import java.util.List;

public class Bidder_ahConsiderHistory implements Bidder {
    private double budget = Auctioneer.defaultConfig.getBudget();
    private static final double factor = 1.5;
    private int day = 1;
    private List<Double> bids1;
    private List<Double> bids2;
    private List<Double> bids3;
    private List<Double> avg;
    private int ind;
    private double eps = 0.5;

    // given your value for the day, determine an action
    public double getBid(double v) {
        if (day <= 3) {
            return Math.min(v * 0.5, budget);
        } else {
            ind = -1;
            for (int i = 0; i < 10; i++) {
                avg.set(i, (1 / 3) * (bids1.get(i) + bids2.get(i) + bids3.get(i)));
                if (avg.get(i) <= v) {
                    ind = i;
                }
            }
            if (ind == -1) {
                return Math.min(v * 0.5, budget);
            }
            return Math.min(avg.get(ind) + eps, budget);
        }
    }

    // callback function with results
    public void addResults(List<Double> bids, int myBid, double myPayment) {
        if (day % 3 == 1) {
            bids1 = new ArrayList<>(bids);
        }
        if (day % 3 == 2) {
            bids1 = new ArrayList<>(bids);
        } else {
            bids1 = new ArrayList<>(bids);
        }
        // record my utility and budget
        if (myBid >= 0) {
            budget -= myPayment;
        }
    }
}
