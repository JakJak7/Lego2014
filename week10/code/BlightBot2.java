import lejos.nxt.*;
import lejos.robotics.navigation.*;
public class BlightBot2 {
	public static void main(String[] args) {
		DifferentialPilot pilot = new DifferentialPilot(5.6F, 16.2F,
			Motor.B, Motor.A, true);
		pilot.setTravelSpeed((double) pilot.getMaxTravelSpeed()/2);
		pilot.setAcceleration((int) pilot.getMaxTravelSpeed()*2);
		Navigator robot = new Navigator(pilot);
		round(robot);
		round(robot);
		round(robot);
		round(robot);
	}

	public static void round(Navigator robot) {
		robot.goTo(50,0);
		robot.waitForStop();
		robot.goTo(25,25);
		robot.waitForStop();
		robot.goTo(25,-12.5f);
		robot.waitForStop();
		robot.goTo(0,0);
		robot.waitForStop();

	}
}
