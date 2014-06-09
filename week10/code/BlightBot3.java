import lejos.nxt.*;
import lejos.robotics.navigation.*;
public class BlightBot3 {
	public static void main(String[] args) {
		DifferentialPilot robot = new DifferentialPilot(5.6F, 16.2F,
			Motor.B, Motor.A, true);
		robot.travel(-100);
	}
}
