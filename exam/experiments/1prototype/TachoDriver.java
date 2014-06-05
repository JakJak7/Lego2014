import lejos.nxt.*;
import java.lang.Math;

public class TachoDriver{
	static float width = 19.6f; // vehicle width in cm
	static float wheelSize = 5.5f; // diameter of wheel
	public static void main(String[] args){
		forward(width/2 - 4 + 28.0f );
		right();
		forward(96.5f);
		right();
		forward(29);
		left();
		forward(29.5f);
		forward(29.5f);
		forward(29.5f); //at the end of first line of solar panels
		left();
		left();
		forward(29.5f);
		forward(29.5f);
		forward(29.5f);
		right();
		forward(29);
		right();
		forward(29.5f);
		forward(29.5f);
		forward(29.5f); //at the end of second line of solar panels
		left();
		left();
		forward(29.5f);
		forward(29.5f);
		forward(29.5f);
		right();
		forward(29);
		right();
		forward(29.5f);
		forward(29.5f);
		forward(29.5f); //at the end of third line of solar panels
		left();
		left();
		forward(29.5f);
		forward(29.5f);
		forward(29.5f);
		left();
		forward(29);
		right();
		forward(96.5f);
		left();
		forward(28 + width); //back in the green
	}
	
	public static void forward(float distance){ 
		int degrees = (int)(360*distance / (wheelSize * Math.PI));
		Motor.B.rotate(degrees, true);
		Motor.C.rotate(degrees);
	}
	
	public static void right(){
		double degrees = 360*((width*2 * Math.PI)/4) / (wheelSize * Math.PI); //wheel rotations to one 90 degree turn
		degrees = degrees * 1.05f; //error correction because of inaccuracies
		Motor.B.rotate((int)degrees/2, true);
		Motor.C.rotate((int)-degrees/2);
	}
	
	public static void left(){
		double degrees = 360*((width*2 * Math.PI)/4) / (wheelSize * Math.PI); //wheel rotations to one 90 degree turn
		degrees = degrees * 1.05f;
		Motor.C.rotate((int)degrees/2, true);
		Motor.B.rotate((int)-degrees/2);
	}
}
