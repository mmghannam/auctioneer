//Author: Yannick Vogt
//Increase bids over time (when other agents are exhausted)

import java.util.List;

public class Bidder_yv_3 implements Bidder {
private double budget = Auctioneer.defaultConfig.getBudget();
private int totalRounds = Auctioneer.defaultConfig.getDays();
//private static final double factor = 1;
double round = 1;

// given your value for the day, determine an action
public double getBid(double v) {
	round++;
	double fac = (1500+round)/(1500+totalRounds);
	return Math.min( v * fac , budget);
}

// callback function with results
public void addResults(List<Double> bids, int myBid, double myPayment) {
 // record my utility and budget
 if (myBid >= 0) {
   budget -= myPayment;
 }
}
}
