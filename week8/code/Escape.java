import lejos.nxt.*;
import lejos.util.Delay;

class Escape extends Thread{
	private SharedCar car = new SharedCar();

	private int power = 70, ms = 500;
	TouchSensor touchLeft = new TouchSensor(SensorPort.S1);
	TouchSensor touchRight = new TouchSensor(SensorPort.S4);
	int stopThreshold = 50;

	public Escape(SharedCar car){
		this.car = car;
	}

	public void run(){
			       
        while (true){
			if(touchLeft.isPressed() && touchRight.isPressed()){ // both bumpers are pressed
				car.backward(power,power);
				Delay.msDelay(1000);
				car.forward(0,power) // turn left
				Delay.msDelay(ms);
			}
			else if(touchLeft.isPressed())
        }
    }
}
