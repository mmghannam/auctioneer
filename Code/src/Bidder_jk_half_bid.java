import java.util.List;

public class Bidder_jk_half_bid implements Bidder {
	// bid half of daily value
	public double getBid(double dailyValue) {
		return dailyValue/2.0;
	}

	public void addResults(List<Double> bids, int myBid, double myPayment) { }
}
