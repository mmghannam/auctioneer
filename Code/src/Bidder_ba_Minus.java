import java.util.List;

public class Bidder_ba_Minus implements Bidder {

	private double budget = Auctioneer.defaultConfig.getBudget();
	private int m = 4;

	public double getBid(double v) {

		return Math.min(Math.max((v-m),5./100.) , budget);
	}


	public void addResults(List<Double> bids, int myBid, double myPayment) {

		if (myBid >= 0) {
			budget -= myPayment;
		}
	}
}
