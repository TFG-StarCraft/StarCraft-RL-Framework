package qLearning.enviroment;

public interface AbstractGridEnviroment {

	public double getReward(int x, int y);
	public boolean isValid(int x, int y);

	public int getSizeX();
	public int getSizeY();

	public int getStartX();
	public int getStartY();
	
	public int getEndX();
	public int getEndY();

	public boolean isStart(int x, int y);
	public boolean isEnd(int x, int y);
}
