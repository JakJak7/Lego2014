import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.robotics.Color;
import java.io.*;

/**
 * Receive data from a PC, a phone, 
 * or another Bluetooth device.
 * 
 * Waits for a Bluetooth connection, receives two integers that are
 * interpreted as power and duration for a forward command to a
 * differential driven car. The resulting tacho counter
 * value is send to the initiator of the connection.
 * 
 * Based on Lawrie Griffiths BTSend
 * 
 * @author Ole Caprani
 * @version 26-2-13
 */
public class BTSejway implements ButtonListener
{
    private String connected = "Connected";
    private String waiting = "Waiting...";
    private String closing = "Closing...";

    private BTConnection btc;
    private DataInputStream dis;
    private DataOutputStream dos;

	int KP, KI, KD, offset;
	final int SCALE = 18;
	int prev_error;
	float int_error;

	ColorSensor cs;
	
    public void perform() 
    {		
        cs = new ColorSensor(SensorPort.S1);
        cs.setFloodlight(Color.WHITE);
        BTSejway listener = new BTSejway();
        Button.ESCAPE.addButtonListener(listener);
		
        LCD.drawString(waiting,0,0);

        btc = Bluetooth.waitForConnection();
        
        LCD.clear();
        LCD.drawString(connected,0,0);	

        dis = btc.openDataInputStream();
        dos = btc.openDataOutputStream();
        
        while (true)
        {
            try 
            {
		offset = dis.readInt();
		KP = dis.readInt();
		KI = dis.readInt();
		KD = dis.readInt();
  
                LCD.drawInt(offset,7,0,1);
                LCD.refresh();
		pidControl();
            }
            catch (Exception e)
            {}
        }
    }
	
    public void buttonPressed(Button b) {}
		   
    public void buttonReleased(Button b) 
    {
        LCD.clear();
        LCD.drawString(closing,0,0);
    	try 
    	{
    	    dis.close();
            dos.close();
            Thread.sleep(100); // wait for data to drain
            btc.close();    	
    	}
        catch (Exception e)
        {}
        try {Thread.sleep(1000);}catch (Exception e){}
        System.exit(0);
    }

    public void pidControl() throws Exception {


        while (!Button.ENTER.isDown()) {

	    int normVal = cs.getNormalizedLightValue();

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





	    if (pid_val > 0) {

		MotorPort.B.controlMotor(power, BasicMotorPort.FORWARD);

		MotorPort.C.controlMotor(power, BasicMotorPort.FORWARD);

	    } else {

		MotorPort.B.controlMotor(power, BasicMotorPort.BACKWARD);

		MotorPort.C.controlMotor(power, BasicMotorPort.BACKWARD);

	    }

	    

	    

        }

	shutDown();

    }

    public void shutDown()
    {
        // Shut down light sensor, motors
        Motor.B.flt();
        Motor.C.flt();
        //%ls.setFloodlight(false);
    }
    
    public static void main(String [] args)  
    {
        new BTSejway().perform();
    }

}

