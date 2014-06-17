public class Drive {
    private Move m;
    private Navigator n;


    public Drive() {
	m = new Move();
	n = new Navigator(m);

	//# Calibrate
	m.calibrate();
	
	//# Go out into space
	n.gotoGrid(1);
	n.fixRow();

    }

    public static void main (String[] aArg){
	new Drive();
    }
}
