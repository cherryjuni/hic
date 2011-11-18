package rsvr;

import rsvr.Outer.Inner;

public class Round02 {
    public static void main(String[] args) {
        Outer ot = new Outer();
        Outer.Inner oi = ot.new Inner();
        oi.aaa();

        Outer.Inner oi2 = new Outer().new Inner();
        oi2.aaa();
        oi2.aaa();
    }
}
