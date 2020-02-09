//Author: Yannick Vogt
//Bid only if valuation = 200


import java.util.List;

public class Bidder_yv_2 implements Bidder {

//given your value for the day, determine an action
public double getBid(double v) {
	if(v==200) return 200;
	else return 0;
}

//callback function with results
public void addResults(List<Double> bids, int myBid, double myPayment) {

}
}
