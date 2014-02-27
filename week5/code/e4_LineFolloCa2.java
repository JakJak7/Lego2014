import lejos.nxt.*;

public class e4_LineFolloCa2
{
    public static void main (String[] aArg) throws Exception {
	final int power = 80;
	
	Button.ESCAPE.addButtonListener(new ButtonListener() {
		public void buttonPressed(Button b) {
		    LCD.drawString("ENTER pressed", 0, 0);
		    System.exit(1);
		}

		public void buttonReleased(Button b) {
		    LCD.clear();
		}
	    });

	ThreeColorSensor2 sensor = new ThreeColorSensor2(SensorPort.S1);
	 
	sensor.calibrate();
	//sensor.set(41,65,50);
	Thread.sleep(500);
	LCD.clear();
	LCD.drawString("Light: ", 0, 2); 

	while (! Button.ESCAPE.isDown()) {
	    LCD.drawInt(sensor.light(),4,10,2);
	    LCD.refresh();
	     
	    if (sensor.green()) {
		Thread.sleep(5);
		if (sensor.green()) 
		    Car.stop();
	    }
	    else {
		if (sensor.white())
		    Car.forward(0, power);
		else if (sensor.black())
		    Car.forward(power, 0);
		//		else 
		//  Car.forward(power, power);
	    }
	    //Thread.sleep(5);
	}

	Car.stop();
	LCD.clear();
	LCD.drawString("Program stopped", 0, 0);
	LCD.refresh();
    }
}
