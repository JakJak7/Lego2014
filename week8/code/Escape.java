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
				car.forward(power,0); // turn left
				Delay.msDelay(ms);
			}
			else if(touchLeft.isPressed()){
				car.backward(0,power);
				Delay.msDelay(ms);
			}
			else if(touchRight.isPressed()){
				car.backward(power,0);
				Delay.msDelay(ms);
			}
			else{
	    		car.noCommand();
			}
        }
    }
}
