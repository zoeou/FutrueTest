package com.bjsxt.height.Completable;

public class DiscountService {

    public static double apply(Quote quote) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String sout = String.format("\n【折后 ：%s:%s:%s:%s:%s",quote.shopName,quote.product,quote.price * quote.discountValue,quote.discountValue + "",quote.discountDesc);
        System.out.println(sout);
        return quote.price * quote.discountValue;
    }


}
