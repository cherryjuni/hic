package rsvr;

public class Outer {
    private int x = 100;

    class Inner{
        private int y = 200;
        public void aaa(){
            //OuterŬ������ ������� ȣ�Ⱑ��
            System.out.println("x = "+x);
            System.out.println("y = "+y);
        }
    }
}
