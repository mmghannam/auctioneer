import java.util.List;

public class Bidder_mg_adapter extends Bidder_template {
    private static double learningRate = 1.0 / Auctioneer.defaultConfig.getDays();


    @Override
    public void strategize(List<Double> bids, int myBid, double myPayment, int currentDay) {
        if (IWon(myPayment)) {
            factor -= learningRate;
        } else {
            factor += learningRate;
        }
    }
}
