import java.util.List;

public class Bidder_ba_Communism implements Bidder {

	private double budget = Auctioneer.defaultConfig.getBudget();

	public double getBid(double v) {
		return Math.min(v/10000., budget);
	}

	//Here we only need to track our budget, although it is very unlikely it will be depleted (depending on T)
	public void addResults(List<Double> bids, int myBid, double myPayment) {

		if (myBid >= 0) {
			budget -= myPayment;
		}
	}
}
