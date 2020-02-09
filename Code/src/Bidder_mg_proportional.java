import java.util.List;

public class Bidder_mg_proportional extends Bidder_template {

    @Override
    public void strategize(List<Double> bids, int myBid, double myPayment, int currentDay) {
        factor = 1.0 * currentDay / Auctioneer.defaultConfig.getDays();
    }
}
