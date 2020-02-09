import java.util.List;
import java.util.Random;

public class Bidder_jk_proportional_factor implements Bidder {

	private double maxValue = Auctioneer.defaultConfig.getMaxValue();
	
	// set bid proportional to our value
	public double getBid(double dailyValue) {
		return (dailyValue/maxValue)*dailyValue;
	}

	public void addResults(List<Double> bids, int myBid, double myPayment) { }

}
