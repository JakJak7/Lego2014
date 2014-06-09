import lejos.nxt.*;
import lejos.robotics.Color;

public class e6_LineFolloCal {
    public static void main (String[] aArg) throws Exception {
	final int power = 90;
	
        ColorSensor cs = new ColorSensor(SensorPort.S1);
        String colorNames[] = {"None", "Red", "Green", "Blue", "Yellow",
			       "Megenta", "Orange", "White", "Black", "Pink",
			       "Grey", "Light Grey", "Dark Grey", "Cyan"};

        cs.setFloodlight(Color.WHITE);

	while (! Button.ESCAPE.isDown()) {
	    ColorSensor.Color vals = cs.getColor();
            ColorSensor.Color rawVals = cs.getRawColor();

	    if (vals.getColor() == 1) {
		Thread.sleep(10);
		if (vals.getColor() == 1) 
		    Car.stop();
	    }
	    else {
		if (vals.getColor() == 6){
		    Car.forward(0, power);

		}
		else if (vals.getColor() == 7){
		    Car.forward(power, 0);
		}

	    }
	}

	Car.stop();
	LCD.clear();
	LCD.drawString("Program stopped", 0, 0);
	LCD.refresh();
    }
}
