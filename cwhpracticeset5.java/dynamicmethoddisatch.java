class phone {
    public void showtime() {
        System.out.println("time is 8 am");
    }

    public void on() {
        System.out.println("turning on");
    }

}

class smartphone extends phone {
    public void music() {
        System.out.println(("playing music"));
    }

    public void on() {
        System.out.println("turning on smartphone");
    }
}

public class dynamicmethoddisatch {
    public static void main(String[] args)

    {

        // phone ph = new phone();
        // smartphone sm = new smartphone();
        phone ph = new smartphone();
        // smarthone sm = new smartpphone();//jb bhi new ho runtime pe object bnta h
        ph.showtime();
        ph.on();
        // ph.music();// not alloe illegal because isme smart
    }
}
