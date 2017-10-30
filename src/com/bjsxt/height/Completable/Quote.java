package com.bjsxt.height.Completable;

public class Quote {
	 String shopName;
	    String product;
	    double price;
	    double discountValue;
	    String discountDesc;

	    static Quote parse(String s){
	        String[] split = s.split(":");
	        Quote quote = new Quote();
	        quote.shopName = split[0];
	        quote.product = split[1];
	        quote.price = Double.valueOf(split[2]);
	        quote.discountValue = Double.valueOf(split[3]);
	        quote.discountDesc = split[4];
	        return quote;
	    }

}
