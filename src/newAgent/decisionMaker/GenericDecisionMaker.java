package newAgent.decisionMaker;

public abstract class GenericDecisionMaker implements Runnable {

	public static final int TYPE_LAMBDA_QE = 1;
/*
	public static GenericDecisionMaker factory(int type, Object... args) {
		switch (type) {
		case TYPE_LAMBDA_QE:
			if (args.length == 6 && args[0] instanceof Com && args[1] instanceof Bot && args[2] instanceof Double
					&& args[3] instanceof Double && args[4] instanceof Double && args[5] instanceof Double) {
				return new DM_LambdaQE((Com) args[0], (Bot) args[1], (Double) args[2], (Double) args[3],
						(Double) args[4], (Double) args[5]);
			} else {
				throw new IllegalArgumentException(
						"Expeted args (Com com, Bot bot, Double alpha, Double gamma, Double epsilon, Double lambda");
			}

		default:
			throw new IllegalArgumentException();
		}

	}
*/
	public abstract void run();

}
