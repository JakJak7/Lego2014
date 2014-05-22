import lejos.nxt.*;

public class GridAccuracy {
    private LightSensor sensor = new LightSensor(SensorPort.S1,false);
    private int green = -1;
    private int black = -1;
    private int white = -1;

    public static void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3);
    }
    public int rnv() {
	return sensor.readNormalizedValue();
    }
    public void calibrate() {
	controlMotor(MotorPort.B,-100);
	controlMotor(MotorPort.C,-100);
	// Green
	green = rnv();
	LCD.drawInt(green, 2, 7, 0);
	while (rnv() < green+20);
	black = rnv();
	LCD.drawInt(black, 2, 7, 1);
	while (rnv() < black+20);
	white = rnv();
	LCD.drawInt(white, 2, 7, 2);
	
    }
    public PID() {
	LCD.drawString("green: ", 0, 0);
	LCD.drawString("black: ", 0, 1);
	LCD.drawString("white: ", 0, 2);
	calibrate();
	while (!Button.ESCAPE.isDown()) {
	    LCD.drawInt(green, 2, 7, 0);
	    LCD.drawInt(black, 2, 7, 1);
	    LCD.drawInt(white, 2, 7, 2);
	    LCD.drawInt(rnv(), 2, 7, 5);
	}
    }
    public static void main (String[] aArg) throws Exception {
	new GridAccuracy();
    }
}
