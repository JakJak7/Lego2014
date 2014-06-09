import lejos.nxt.*;
import lejos.robotics.navigation.*;
public class BlightBot4 {
	public static void main(String[] args) {
		DifferentialPilot pilot = new DifferentialPilot(5.6F, 16.2F,
			Motor.B, Motor.A, true);
		pilot.setTravelSpeed((double) pilot.getMaxTravelSpeed()/2);
		pilot.setAcceleration((int) pilot.getMaxTravelSpeed()*2);
		Navigator robot = new Navigator(pilot);
		round(robot);
	}

	public static void round(Navigator robot) {
		robot.goTo(50,0);
		avoidance(robot);
		robot.goTo(25,25);
		avoidance(robot);
		robot.goTo(25,-12.5f);
		avoidance(robot);
		robot.goTo(0,0);
		avoidance(robot);

	}

	public static void avoidance(Navigator robot) {
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		Waypoint currentTarget = robot.getWaypoint();
		while(robot.isMoving()) {
			//Check for avoidance
			int distance = us.getDistance();
			if(distance < 10) {
				robot.stop();
				//Get robot's current heading and coordinates
				Pose pose = robot.getPoseProvider().getPose();
				float heading = pose.getHeading();
				Waypoint avoidancePoint = new Waypoint(pose.pointAt(10, heading-90));
				robot.goTo(avoidancePoint);
				robot.waitForStop();
				robot.goTo(currentTarget);
				avoidance(robot);
			}
		}
	}
}
