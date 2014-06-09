public class Drive {
    private Move m;
    private Navigator n;
    private final int LEFT=-1,RIGHT=1,FRONT=0,BACK=1;

    public void solarTask() throws Exception{
	int i = m.getColor();
	if (i == 0)//return defect
	    n.switche();
	else if (i == 1) // Do nothing.. pass by
	    n.passSolar();
	else if (i == 2) //turn solar
	    n.turnSolar();
    }
    public Drive() throws Exception {
	m = new Move();
	n = new Navigator(m);

	//# Calibrate
	m.calibrate();
	
	//# Go out into space
	for (int i=1;i<=3;i++) {
	    n.navigateTo(i);
	    solarTask();
	}
    }

    public static void main (String[] aArg) throws Exception {
	new Drive();
    }
}
