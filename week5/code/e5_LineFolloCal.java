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

	float Kc = 250, Pc = 1.2f, dT = 0.020f;
	float Kp = 250*0.6f;
	float Ki = 2*Kp*dT/Pc, Kd = Kp*Pc/(8*dT);
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
	    controlMotor(MotorPort.C, Tp + Turn);
	    lastError = error;
	    Thread.sleep(20);
	}

	LCD.clear();
	LCD.drawString("Program stopped", 0, 0);
	LCD.refresh();
    }
}
