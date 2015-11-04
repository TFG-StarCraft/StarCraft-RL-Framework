package qLearning.enviroment;

import qLearning.Const;

public class BasicEnviroment implements AbstractGridEnviroment {

	private int sizeX;
	private int sizeY;

	private int startX = 0;
	private int startY = 0;
	private int endX = Const.TAM_X-1;
	private int endY = Const.TAM_Y-1;

	private int[][] grid;
	private double[][] reward;

	public BasicEnviroment() {

		this.sizeX = Const.TAM_X;
		this.sizeY = Const.TAM_Y;

		grid = new int[Const.TAM_X][Const.TAM_Y];
		reward = new double[Const.TAM_X][Const.TAM_Y];

		for (int i = 0; i < Const.TAM_X; i++) {
			for (int j = 0; j < Const.TAM_Y; j++) {
				grid[i][j] = Const.TABLERO_VACIO;
				reward[i][j] = Const.RECOMPENSA_GENERAL;
			}
		}

		grid[startX][startY] = Const.TABLERO_INI;

		grid[endX][endY] = Const.TABLERO_FINAL;
		reward[endX][endY] = Const.RECOMPENSA_FINAL;
	}

	public double getReward(int x, int y) {
		return reward[x][y];
	}

	public boolean isValid(int x, int y) {
		return (x >= 0) && (x < Const.TAM_X) && (y >= 0) && (y < Const.TAM_Y);
	}

	@Override
	public int getSizeX() {
		return sizeX;
	}

	@Override
	public int getSizeY() {
		return sizeY;
	}

	@Override
	public int getStartX() {
		return startX;
	}

	@Override
	public int getStartY() {
		return startY;
	}

	@Override
	public int getEndX() {
		return endX;
	}

	@Override
	public int getEndY() {
		return endY;
	}

	@Override
	public boolean isEnd(int x, int y) {
		return x == endX && y == endY;
	}

	@Override
	public boolean isStart(int x, int y) {
		return x == startX && y == startY;
	}

}
