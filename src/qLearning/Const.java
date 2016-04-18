package qLearning;

public interface Const {
	public final static double ALPHA = 0.3;
	public final static double GAMMA = 0.9;
	public static final double EPSLLON_EGREEDY = 0.9;
	public static final double LAMBDA = 0.5;

	public static final double REWARD_MULT_FACTOR = 100;
	public static final double REWARD_SUCCESS = 100;
	public static final double REWARD_FAIL = 0;
	
	public static final double Q_GENERAL = 1;
	
	public static final int NUM_EPISODIOS = 4000000;
}
