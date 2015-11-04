package qLearning.enviroment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import qLearning.Const;

public class FileEnviroment implements AbstractGridEnviroment {

	private int sizeX;
	private int sizeY;

	private int startX;
	private int startY;
	private int endX;
	private int endY;

	private int[][] tablero;
	private double[][] recomp;

	public FileEnviroment(String fileName) {

		// Lectura del fichero
		Scanner sc = null;
		try {
			sc = new Scanner(new FileInputStream(fileName));
			this.sizeX = sc.nextInt();
			this.sizeY = sc.nextInt();
			this.tablero = new int[sizeX][sizeY];
			this.recomp = new double[sizeX][sizeY];
			sc.nextLine();

			int j = sizeY - 1;
			// Por cada fila (a la inversa)
			while (sc.hasNext()) {
				char[] ss = sc.nextLine().toCharArray();
				if (ss.length != this.sizeX)
					throw new RuntimeException("Error de formato de archivo");
				// Por cada columna
				for (int i = 0; i < ss.length; i++) {
					int code;
					double reward = 0;

					switch (ss[i]) {
					case Const.CTABLERO_VACIO:
						code = Const.TABLERO_VACIO;
						reward = Const.RECOMPENSA_GENERAL;
						break;
					case Const.CTABLERO_PARED:
						code = Const.TABLERO_PARED;
						break;
					case Const.CTABLERO_INI:
						code = Const.TABLERO_INI;
						reward = Const.RECOMPENSA_GENERAL;
						this.startX = i;
						this.startY = j;
						break;
					case Const.CTABLERO_FINAL:
						code = Const.TABLERO_FINAL;
						reward = Const.RECOMPENSA_FINAL;
						this.endX = i;
						this.endY = j;
						break;
					default:
						throw new RuntimeException("Error de formato de archivo");
					}
					this.tablero[i][j] = code;
					this.recomp[i][j] = reward;
				}
				j--;
			}

			if (j != -1) {
				throw new RuntimeException("Error de formato de archivo");
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error, el archivo no existe");
		} finally {
			if (sc != null)
				sc.close();
		}
	}

	@Override
	public double getReward(int x, int y) {
		return this.recomp[x][y];
	}

	@Override
	public boolean isValid(int x, int y) {
		return (x >= 0) && (x < sizeX) && (y >= 0) && (y < sizeY) && this.tablero[x][y] != Const.TABLERO_PARED;
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
