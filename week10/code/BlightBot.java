import lejos.nxt.*;
import lejos.robotics.navigation.*;
public class BlightBot {
	public static void main(String[] args) {
		DifferentialPilot robot = new DifferentialPilot(5.6F, 16.2F,
			Motor.B, Motor.A, true);
		round(robot);
		round(robot);
		round(robot);
	}

	public static void round(DifferentialPilot robot) {
		robot.travel(-25.0);
		robot.rotate(90.0);
		robot.travel(-25.0);
		robot.rotate(90.0);
		robot.travel(-25.0);
		robot.rotate(90.0);
		robot.travel(-25.0);
		robot.rotate(90.0);
	}
}
