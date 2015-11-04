package qLearning.agent.estados;

import com.Com;

import qLearning.enviroment.AbstractGridEnviroment;

public class StateContainer {

	private State[][] states;
	
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	
	public StateContainer(AbstractGridEnviroment enviroment, Com com) {
		int sizex = enviroment.getSizeX();
		int sizey = enviroment.getSizeY();
		this.states = new State[sizex][sizey];

		for (int i = 0; i < sizex; i++) {
			for (int j = 0; j < sizey; j++) {
				states[i][j] = new State(i, j, this, enviroment, com);
			}
		}

		this.startX = enviroment.getStartX();
		this.startY = enviroment.getStartY();
		this.endX = enviroment.getEndX();
		this.endY = enviroment.getEndY();
		
	}

	public State getEstado(int x, int y) {
		return states[x][y];
	}

	public State getEstadoInicial() {
		return states[startX][startY];
	}

	public State getEstadoFinal() {
		return states[endX][endY];
	}
}
