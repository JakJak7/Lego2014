import lejos.nxt.*;
import lejos.util.Delay;

class Escape extends Thread {
    private final SharedCar car;

    private final int power = 100;
    private final TouchSensor touchLeft = new TouchSensor(SensorPort.S1);
    private final TouchSensor touchRight = new TouchSensor(SensorPort.S4);

    public Escape(SharedCar car) {
	this.car = car;
    }

    public void run() {		       
        while (true) {
	    boolean left = touchLeft.isPressed();
	    boolean right = touchRight.isPressed();
	    if (left || right) {
		car.backward(power,power);
		Delay.msDelay(200);
		if(!left)
		    car.forward(power,-power);
		else
		    car.forward(-power,power);
		Delay.msDelay(400);
	    }
	    else
		car.noCommand();
	}
    }
}
