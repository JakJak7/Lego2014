public class Drive {
    private Move m;
    private Navigator n;

    public void solarTask() {
	int i = m.getColor();
	if (i == 0)//return defect
	    n.switche();
	else if (i == 1) // Do nothing.. pass by
	    n.passSolar();
	else if (i == 2) //turn solar
	    n.turnSolar();
    }
    public Drive() {
	m = new Move();
	n = new Navigator(m);

	//# Calibrate
	m.calibrate();
	
	//# Go out into space
	for (int i=1;i<=1;i++) {
	    n.navigateTo(i);
	    //solarTask();
	}
    }

    public static void main (String[] aArg){
	new Drive();
    }
}
