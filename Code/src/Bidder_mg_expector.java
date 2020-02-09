import java.util.List;

public class Bidder_mg_expector extends Bidder_template {

    @Override
    public double getBid(double v) {
        return Math.min(averageTopBids[9], budget);
    }

    @Override
    public void strategize(List<Double> bids, int myBid, double myPayment, int currentDay) {
    }
}
