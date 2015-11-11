package qLearning;

public interface Const {
	public final static int TAM_X = 1000;
	public final static int TAM_Y = 1000;
	
	public final static int TABLERO_INI = 0;
	public final static int TABLERO_VACIO = 1;
	public final static int TABLERO_PARED = 2;
	public final static int TABLERO_FINAL = 3;
	
	public final static char CTABLERO_INI = '0';
	public final static char CTABLERO_VACIO = ' ';
	public final static char CTABLERO_PARED = '*';
	public final static char CTABLERO_FINAL = 'X';

	public final static double ALPHA = 0.8;
	public final static double GAMMA = 0.8;
	public static final double EPSLLON_EGREEDY = 0.9;

	public static final double RECOMPENSA_GENERAL = 0;
	public static final double RECOMPENSA_FINAL = 1000;
	public static final double RECOMPENSA_ERROR = 0;
	
	public static final double Q_GENERAL = 1;
	
	public static final int NUM_EPISODIOS = 400;
}
