public class Drive {
    private Move m;
    private Navigator n;


    public Drive() {
	m = new Move();
	n = new Navigator(m);

	//# Calibrate
	m.calibrate();
	
	//# Go out into space
	for (int i = 1;i<=4;i++) {
	    n.gotoGrid(i);
	    if (i < 4)
		n.fixRow();
	}

    }

    public static void main (String[] aArg){
	new Drive();
    }
}
