import lejos.nxt.*;
import lejos.util.Delay;

class Escape extends Thread {
    private final SharedCar car;

    private int power = 100;
    private TouchSensor touchLeft = new TouchSensor(SensorPort.S1);
    private TouchSensor touchRight = new TouchSensor(SensorPort.S4);
    private int stopThreshold = 50;

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
