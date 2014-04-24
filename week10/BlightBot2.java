import lejos.nxt.*;
import lejos.robotics.navigation.*;
public class BlightBot2 {
	public static void main(String[] args) {
		DifferentialPilot pilot = new DifferentialPilot(5.6F, 16.2F,
			Motor.B, Motor.A, true);
		Navigator robot = new Navigator(pilot);
		robot.goTo(100,0);
		robot.waitForStop();
		robot.goTo(50,50);
		robot.waitForStop();
		robot.goTo(50,-25);
		robot.waitForStop();
		robot.goTo(0,0);
		robot.waitForStop();
	}
}
