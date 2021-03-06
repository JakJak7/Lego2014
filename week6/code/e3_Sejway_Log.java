import lejos.nxt.*;
import lejos.robotics.Color;

/**
 * A controller for a self-balancing Lego robot with a light sensor
 * on port 2. The two motors should be connected to port B and C.
 *
 * Building instructions in Brian Bagnall: Maximum Lego NXTBuilding 
 * Robots with Java Brains</a>, Chapter 11, 243 - 284
 * 
 * @author Brian Bagnall
 * @version 26-2-13 by Ole Caprani for leJOS version 0.9.1
 */


public class e3_Sejway_Log {

    // PID constants
    final int KP = 28;
    final int KI = 4;
    final int KD = 33;
    final int SCALE = 18;

    // Global vars:
    int offset;
    int prev_error;
    float int_error;
	
    //%LightSensor ls;
    ColorSensor cs;	
    DataLogger dl1,dl2,dl3,dl4;

    public e3_Sejway_Log() {
        //%ls = new LightSensor(SensorPort.S2, true);
        cs = new ColorSensor(SensorPort.S1);
        cs.setFloodlight(Color.WHITE);
	dl1 = new DataLogger("SampleR.txt");
	dl2 = new DataLogger("SampleG.txt");
	dl3 = new DataLogger("SampleB.txt");
	dl4 = new DataLogger("SampleL.txt");
    }
	
    public void getBalancePos() {
        // Wait for user to balance and press orange button
        while (!Button.ENTER.isDown()) {
	    // NXTway must be balanced.
	    //%offset = ls.readNormalizedValue();
	    offset = cs.getNormalizedLightValue();
	    LCD.clear();
	    LCD.drawInt(offset, 2, 4);
	    LCD.refresh();
	}
    }
	
    public void pidControl() throws Exception {
	dl1.start();
	dl2.start();
	dl3.start();
	dl4.start();

        while (!Button.ESCAPE.isDown()) {
            ColorSensor.Color rawVals = cs.getRawColor();
	    dl1.writeSample(rawVals.getRed());
	    dl2.writeSample(rawVals.getGreen());
	    dl3.writeSample(rawVals.getBlue());
      
	    //%int normVal = ls.readNormalizedValue();
	    int normVal = cs.getNormalizedLightValue();
	    dl4.writeSample(normVal);
            // Proportional Error:
            int error = normVal - offset;
            // Adjust far and near light readings:
            if (error < 0) error = (int)(error * 1.8F);
			
            // Integral Error:
            int_error = ((int_error + error) * 2)/3;
			
            // Derivative Error:
            int deriv_error = error - prev_error;
            prev_error = error;
			
            int pid_val = (int)(KP * error + KI * int_error + KD * deriv_error) / SCALE;
			
            if (pid_val > 100)
                pid_val = 100;
            if (pid_val < -100)
                pid_val = -100;

            // Power derived from PID value:
            int power = Math.abs(pid_val);
            power = 55 + (power * 45) / 100; // NORMALIZE POWER

	    Thread.sleep(50);
        }
    }
	
    public void shutDown()
    {
	dl1.close();
	dl2.close();
	dl3.close();
	dl4.close();

        // Shut down light sensor, motors
        //Motor.B.flt();
        //Motor.C.flt();
        //%ls.setFloodlight(false);
    }
	
    public static void main(String[] args) throws Exception{
        e3_Sejway_Log sej = new e3_Sejway_Log();
        sej.getBalancePos();
        sej.pidControl();
        sej.shutDown();
    }
}