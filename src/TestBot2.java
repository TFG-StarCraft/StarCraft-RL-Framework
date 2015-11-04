import bwapi.*;
import bwta.BWTA;

public class TestBot2 extends DefaultBWListener {

	private Mirror mirror = new Mirror();

	private Game game;

	private Player self;

	public void run() {
		mirror.getModule().setEventListener(this);
		mirror.startGame();
	}

	@Override
	public void onUnitCreate(Unit unit) {
		System.out.println("New unit " + unit.getType());
	}

	@Override
	public void onStart() {
		game = mirror.getGame();
		//game.enableFlag(1);
		self = game.self();

		// Use BWTA to analyze map
		// This may take a few minutes if the map is processed first time!
		System.out.println("Analyzing map...");
		BWTA.readMap();
		BWTA.analyze();
		System.out.println("Map data ready");

	}

	private int mov = -1;
	private int step = 20;
	
	private int endx = 0, endy = 0;
	
	@Override
	public void onFrame() {
		
		game.drawTextScreen(10, 10, "Mov to " + endx + "  " + endy);
		for (Unit myUnit : self.getUnits()) {
			if (myUnit.getType() == UnitType.Terran_Marine) {
				if ((myUnit.getX() == endx && myUnit.getY() == endy) || mov == -1) {
					switch (mov) {
					case -1:
					case 0:
						endx = myUnit.getX() + step;
						endy = myUnit.getY();
						myUnit.move(new Position(endx, endy));
						System.out.println("Mov " + mov);
						mov = 1;
						break;
					case 1:
						endx = myUnit.getX();
						endy = myUnit.getY() + step;
						myUnit.move(new Position(endx, endy));
						System.out.println("Mov " + mov);
						mov = 2;
						break;
					case 2:
						endx = myUnit.getX() - step;
						endy = myUnit.getY();
						myUnit.move(new Position(endx, endy));
						System.out.println("Mov " + mov);
						mov = 3;
						break;
					case 3:
						endx = myUnit.getX();
						endy = myUnit.getY() - step;
						myUnit.move(new Position(endx, endy));
						System.out.println("Mov " + mov);
						mov = 0;
						break;
					default:
						break;
					}
					
				}
			}
		}
		
		printUnitsInfo();
	}

	private void printUnitsInfo() {
		for (Unit myUnit : self.getUnits()) {
			if (myUnit.getType() == UnitType.Terran_Marine) {
				game.drawTextMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(),
						"Order: " + myUnit.getOrder() + " (" + myUnit.getTilePosition().getX() + ","
								+ myUnit.getTilePosition().getY() + ") " + myUnit.getPosition().toString());
				game.drawLineMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(),
						myUnit.getOrderTargetPosition().getX(), myUnit.getOrderTargetPosition().getY(),
						bwapi.Color.Red);
			}
		}
	}

	public static void main(String[] args) {
		new TestBot2().run();
	}
}