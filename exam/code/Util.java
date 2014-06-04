public class Util {

	//0 = Nothing, 1 = Black, 2 = Red, 3 = Blue
	public static int determineColor(int red, int green, int blue) {
		if(red < 400 && green < 400 && blue < 400) {
			return 1; //Black
		}
		else if(red < 450 && green < 440 && blue < 455) {
			return 3; //Blue
		}
		else if(red < 515 && green < 490 && blue < 490) {
			return 2; //Red
		}
		else {
			return 0; //Nothing
		}
	}

}
