import lejos.nxt.*;
// Gamle motor får bilen til kun at køre frem af
// For at teste gear og hjul

public class Code1 {
    public static void main(String[] args) {
	Motor.A.forward();
	Motor.B.forward();
        while (!Button.ESCAPE.isDown()) {}
    }
}
