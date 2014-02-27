import lejos.nxt.*;

public class e5_LineFolloCal
{
    public static void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3);
    }
    public static void main (String[] aArg) throws Exception {
	LightSensor sensor = new LightSensor(SensorPort.S1);

	float Kp = 250, Ki = 5, Kd = 1500;
	int offset = 50,
	    Tp = 60,
	    integral = 0,
	    lastError = 0,
	    derivative = 0;
       

	LCD.clear();
	LCD.drawString("Light: ", 0, 2); 

	while (! Button.ESCAPE.isDown()) {
	    LCD.drawInt(sensor.readValue(),4,10,2);
	    LCD.refresh();
	    
	    int LightValue = sensor.readValue();
	    int error = LightValue - offset;
	    integral = integral + error;
	    derivative = error - lastError;
	    int Turn = (int) (Kp*error + Ki*integral + Kd*derivative)/100;
	    controlMotor(MotorPort.B, Tp - Turn);
	    controlMotor(MotorPort.C, Tp + Turn+2); //den trækker ikke ligeså godt
	    lastError = error;
	    Thread.sleep(10);
	}

	LCD.clear();
	LCD.drawString("Program stopped", 0, 0);
	LCD.refresh();
    }
}
