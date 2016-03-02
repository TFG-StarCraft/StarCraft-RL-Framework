package qLearning;

public interface Const {
	public final static double ALPHA = 0.7;
	public final static double GAMMA = 0.9;
	public static final double EPSLLON_EGREEDY = 0.9;

	public static final double RECOMPENSA_GENERAL = 0;
	public static final double RECOMPENSA_FINAL = 3;
	public static final double RECOMPENSA_ERROR = 0;
	
	public static final double Q_GENERAL = 1;
	
	public static final int NUM_EPISODIOS = 4000000;
}
