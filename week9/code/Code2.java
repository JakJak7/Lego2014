import lejos.nxt.*;
// 40, 24, 8 ... 40/24 = 1,66 .. 40/8 = 5x .. 24/8 = 3
// NXT motor, en class for at få dem til at køre frem
// For at teste gear og hjul

public class Code2 {
    private static MotorPort leftMotor = MotorPort.A;
    private static MotorPort rightMotor= MotorPort.B;

    public static void main(String[] args) {
        MotorPort.A.controlMotor(100,1);
        MotorPort.B.controlMotor(100,1);
        while (!Button.ESCAPE.isDown()) {}
    }
}
