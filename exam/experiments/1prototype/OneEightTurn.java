import lejos.nxt.*;

public class OneEightTurn {
    public static void main(String[] arg) {
	MotorPort.C.controlMotor(100,1);
	MotorPort.B.controlMotor(100,2);
	while(true);
    }
}
