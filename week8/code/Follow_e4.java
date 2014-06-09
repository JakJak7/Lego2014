import lejos.nxt.*;
import lejos.util.Delay;

class Follow_e4 extends Thread {
    private final SharedCar car;
    private int power = 70, ms = 500;
    LightSensor light = new LightSensor(SensorPort.S3,false);
	
    int frontLight, leftLight, rightLight, delta;
    int lightThreshold;
	
    public Follow_e4(SharedCar car) {
	this.car = car;	
	this.lightThreshold = light.getLightValue();
    }
    
    public void run() {				       
        while (true) {
	    // Monitor the light in front of the car and start to follow
	    // the light if light level is above the threshold
	    frontLight = light.getLightValue();
	    while (frontLight <= lightThreshold) {
		car.noCommand();
		frontLight = light.getLightValue();
	    }

	    // Follow light as long as the light level is above the threshold
	    while (frontLight > lightThreshold) {

		// Get the light to the left
		Motor.A.rotateTo(-45);
		leftLight = light.getLightValue();
		// Get the light to the right
		Motor.A.rotateTo(45);
		rightLight = light.getLightValue();
		Motor.A.rotateTo(0);

		// Follow light for a while
		delta = leftLight-rightLight;
		
		car.forward(power-delta, power+delta);
		Delay.msDelay(ms);
    		
		frontLight = light.getLightValue();
	    }
	    	
	    car.stop();
	    Delay.msDelay(ms);
 			
	}
    }
}

