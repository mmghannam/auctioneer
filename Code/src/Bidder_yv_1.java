//Author: Yannick Vogt
//Bid equally

import java.util.List;

public class Bidder_yv_1 implements Bidder {
private double budget = Auctioneer.defaultConfig.getBudget();
private int totalRounds = Auctioneer.defaultConfig.getDays();
//private static final double factor = 1;
private int roundsPlayed = -1;


//given your value for the day, determine an action
public double getBid(double v) {
	roundsPlayed++;
	double x = budget / (totalRounds - roundsPlayed) ;
	if (x<v) return x;
	else return v;

}

//callback function with results
public void addResults(List<Double> bids, int myBid, double myPayment) {
if (myBid >= 0) {
	budget -= myPayment;
}


}
}
