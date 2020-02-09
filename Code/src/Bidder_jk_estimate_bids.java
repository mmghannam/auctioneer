import java.util.List;

public class Bidder_jk_estimate_bids implements Bidder {

	private double budget = Auctioneer.defaultConfig.getBudget();
	private double[] rates = Auctioneer.defaultConfig.getRates();
	private double days = Auctioneer.defaultConfig.getDays();
	private double maxValue = Auctioneer.defaultConfig.getMaxValue();

	int last_days_into_account = 30;
	double epsilon = 0.001;
	double day = -1;
	double v;
	double utility;

	private double[][] winning_bid_chance = new double[last_days_into_account][rates.length];
	private double[] correcture = new double[rates.length];

	boolean firstday = true;

	int reccomended_spot;
	double my_bid;

	double error = 0;

	public double getBid(double dailyValue) {
		day++;
		v = dailyValue;

		if(firstday) {
			firstday = false;
			return dailyValue;
		}
		else {

			// calculate best spot regarding estimation of new bids
			double[] rewards = new double[rates.length];
			double max_reward = 0;
			reccomended_spot = -1;
			for(int i = 0; i < rewards.length; i++) {
				// how much we would get if we have not participated and now want to get the
				// highest reward afterwards (add small epsilon)
				rewards[i] = dailyValue*rates[i] - rates[i]*(winning_bid_chance[0][i]+correcture[i]+epsilon*budget);
				if(rewards[i] > max_reward) {
					max_reward = rewards[i];
					reccomended_spot = i;
				}
			}
			if(reccomended_spot == -1) {	// no spot gives us a positive reward => make v as save bid
				my_bid = dailyValue;
				return dailyValue;
			}
			else {				// we want to take the spot at index => make slightly higher bid
				my_bid = winning_bid_chance[0][reccomended_spot] + epsilon*budget;
				my_bid = Math.min(my_bid, dailyValue);
				if(my_bid < 0) {return 0;} else {return my_bid;}
			}
		}
	}

	public void addResults(List<Double> bids, int myBid, double myPayment) {
		if(day > 0) {
			for(int i = 0; i < rates.length; i++) {
				error += Math.pow(bids.get(i)-winning_bid_chance[0][i], 2);
				correcture[i] += (bids.get(i)-winning_bid_chance[0][i])*1/(day+1);
			}
		}


		if (myBid >= 0) {
			budget -= myPayment;
		}
		if(myBid != -1) {
			utility = rates[myBid] * v - myPayment;
		}

		// estimate how big the bids for the corresponding rates are in the next round
		for(int i = 0; i < rates.length; i++) {
			double new_value = 0;
			int sum = 0;

			new_value += bids.get(i);
			sum++;

			for(int j = 0; j < Math.min(last_days_into_account,day-1); j++) {
				new_value += 1/(j+1) * winning_bid_chance[j][i];
				sum+= 1/(j+1);
			}
			new_value /= sum;

			// shift each value one place next and at the first position set the new estimation
			for(int j = (int)Math.min(last_days_into_account-1,day-1)-1; j >= 0; j--) {
				winning_bid_chance[j+1][i] = winning_bid_chance[j][i];
			}
			winning_bid_chance[0][i] = new_value+correcture[i];
		}
	}
}
