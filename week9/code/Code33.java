import lejos.nxt.*;
import lejos.robotics.Color;

public class Code33 {

    public Code33() {
        while (true) {
	    if (Button.RIGHT.isDown()) {
		Motor.A.forward();
		Motor.B.forward();
	    }

	    while (Button.LEFT.isDown()){
		LCD.drawInt(-666,4,10,0);
		Motor.A.stop();
		Motor.B.stop();
	    }
	}

    }

    public static void main(String[] args) {
	Button.ESCAPE.addButtonListener(new ButtonListener() {
		public void buttonPressed(Button b) {System.exit(1);}
		public void buttonReleased(Button b) {} 
	    });
	
	new Code33();
    }
}
