package rsvr;

public class Outer {
    private int x = 100;

    class Inner{
        private int y = 200;
        public void aaa(){
            //Outer클래스의 멤버변수 호출가능
            System.out.println("x = "+x);
            System.out.println("y = "+y);
        }
    }
}
