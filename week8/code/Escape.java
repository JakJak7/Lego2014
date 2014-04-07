import lejos.nxt.*;
import lejos.util.Delay;

class Escape extends Thread {
    private final SharedCar car;

<<<<<<< HEAD
    private int power = 100;
    private TouchSensor touchLeft = new TouchSensor(SensorPort.S1);
    private TouchSensor touchRight = new TouchSensor(SensorPort.S4);
    private int stopThreshold = 50;
=======
	private int power = 100, ms = 200;
	TouchSensor touchLeft = new TouchSensor(SensorPort.S1);
	TouchSensor touchRight = new TouchSensor(SensorPort.S4);
	int stopThreshold = 50;
>>>>>>> 112c21e4978c446770e8ae2398aa12deee7f71ff

    public Escape(SharedCar car) {
	this.car = car;
    }

<<<<<<< HEAD
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
=======
	public void run(){
			       
        while (true){
			if(touchLeft.isPressed() && touchRight.isPressed()){ // both bumpers are pressed
				car.backward(power,power);
				Delay.msDelay(ms);
				car.forward(power,-power); //rotate left
				Delay.msDelay(3*ms);
			}
			else if(touchLeft.isPressed()){
				car.backward(power,power); //same behavior as when both are pressed
				Delay.msDelay(ms);
				car.forward(-power,power);
				Delay.msDelay(3*ms);
			}
			else if(touchRight.isPressed()){
				car.backward(power,power);
				Delay.msDelay(ms);
				car.forward(power,0); //turn left
				Delay.msDelay(ms);
			}
			else{
	    		car.noCommand();
			}
        }
>>>>>>> 112c21e4978c446770e8ae2398aa12deee7f71ff
    }
}
