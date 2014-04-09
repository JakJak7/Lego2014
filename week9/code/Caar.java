import lejos.nxt.*;
import lejos.util.Delay;

public class Caar extends Thread {
    private static NXTRegulatedMotor Ml = Motor.A;
    private static NXTRegulatedMotor Mr = Motor.B;
    private int i = -1;
    protected void control(int i) {
	this.i = i;
    }

    public void run() {
	while (true) {
	    if (i == 0) {//stop
		Ml.stop();
		Mr.stop();
	    }
	    else if (i == 1) {//forward
		Ml.forward();
		Mr.forward();
	    }
	    Delay.msDelay(10);
	}
    }

}