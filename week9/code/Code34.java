import lejos.nxt.*;
import lejos.nxt.addon.RCXMotor;
import lejos.robotics.Color;

public class Code34 {

	RCXMotor left = new RCXMotor(MotorPort.A);
	RCXMotor right = new RCXMotor(MotorPort.B);

    public Code34() {
        while (true) {
	    if (Button.RIGHT.isDown()) {
		left.forward();
		right.forward();
	    }

	    while (Button.LEFT.isDown()){
		LCD.drawInt(-666,4,10,0);
		left.stop();
		left.stop();
	    }
	}

    }

    public static void main(String[] args) {
	Button.ESCAPE.addButtonListener(new ButtonListener() {
		public void buttonPressed(Button b) {System.exit(1);}
		public void buttonReleased(Button b) {} 
	    });
	
	new Code34();
    }
}
