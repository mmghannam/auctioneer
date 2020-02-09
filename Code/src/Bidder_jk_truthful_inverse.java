import java.util.List;

public class Bidder_jk_truthful_inverse implements Bidder {

	private double budget = Auctioneer.defaultConfig.getBudget();
	private double days = Auctioneer.defaultConfig.getDays();
	
	private double current_day = 0;

	// the more days we have played, the more we play thruthful
	public double getBid(double v) {
		current_day++;
		return Math.min(v*(current_day/days), budget);
	}

	public void addResults(List<Double> bids, int myBid, double myPayment) {
		if (myBid >= 0) {
			budget -= myPayment;
		}	
	}

}
