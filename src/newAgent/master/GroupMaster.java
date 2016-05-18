package newAgent.master;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.Com;

import bwapi.Unit;
import newAgent.AbstractEnvironment;
import newAgent.agent.GenericAgent;
import newAgent.agent.group.MarineGroupAgent;
import newAgent.decisionMaker.DecisionMakerPrams;
import newAgent.decisionMaker.Shared_LambdaQE;
import newAgent.state.State;
import utils.DebugEnum;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class GroupMaster extends GenericMaster {

	private int numGroups;

	public GroupMaster(Com com, DecisionMakerPrams params, int numGroups) {
		super(com, params);
		this.numGroups = numGroups;
	}

	@Override
	public void onFirstFrame() {
		halted = false;

		if (shared == null)
			shared = new Shared_LambdaQE(params, new AbstractEnvironment() {

				@Override
				public int getNumDims() {
					// TODO group environment
					// return DataMarine.getNumDims();
					return 0;
				}

				@Override
				public ArrayList<Integer> getNumValuesPerDims() {
					// TODO group environment
					// return DataMarine.getNumValuesPerDims();
					return null;
				}

				@Override
				public State getInitState() {
					// TODO
					throw new RuntimeException();
				}
			});

		try {
			List<Unit> units = com.bot.self.getUnits();
			Map<Integer, List<Unit>> groups = makeGroups(units, numGroups);
			
			if (groups.size() != numGroups) {
				throw new AssertionError("Error, groups.size() != numGroups");
			}
			
			for (Entry<Integer, List<Unit>> entry : groups.entrySet()) {
				GenericAgent a = new MarineGroupAgent(this, com, com.bot, entry.getValue(), entry.getKey());
				this.agentsNotFinished.add(a);
				this.allAgents.add(a);
			}
			
		} catch (Exception | AssertionError  e) {
			e.printStackTrace();
			com.onError("Error creating groups at " + this.getClass().getName(), true);
		}
		
		learningAgent = (learningAgent + 1) % numGroups;

		com.onDebugMessage("Agent num" + learningAgent + ", " + allAgents.get(learningAgent).getClass() + ", learning",
				DebugEnum.AGENT_LEARNING);
		
		shared.getQE().resetE();

		for (GenericAgent genericAgent : agentsNotFinished) {
			genericAgent.onFirstFrame();
			Thread t = new Thread(genericAgent);
			t.start();
			threads.add(t);
		}
	}

	private static final String HEAD = "@relation test\n@attribute x integer\n@attribute y integer\n@data\n";
	private static final String WEKA_CONFIG = "-init 0 -max-candidates 100 -periodic-pruning 10000 -min-density 2.0 -t1 -1.25 -t2 -1.0 -A \"weka.core.EuclideanDistance -R first-last\" -I 500 -num-slots 1 -S 10";

	private Map<Integer, List<Unit>> makeGroups(List<Unit> units, int numClusters) throws Exception {
		String s = HEAD;
		Map<Integer, List<Unit>> r = new HashMap<>();

		for (Unit unit : units) {
			s += unit.getPosition().getX() + "," + unit.getPosition().getY() + "\n";
		}

		StringReader sr = new StringReader(s);
		Instances data = new Instances(sr);

		SimpleKMeans km = new SimpleKMeans();

		String[] options = weka.core.Utils.splitOptions(WEKA_CONFIG);

		km.setOptions(options);
		km.setNumClusters(numClusters);
		km.setPreserveInstancesOrder(true);
		km.buildClusterer(data);

		int[] assignments = km.getAssignments();

		for (int i = 0; i < assignments.length; i++) {
			if (r.containsKey(assignments[i])) {
				List<Unit> a = r.get(assignments[i]);
				a.add(units.get(i));
				r.put(assignments[i], a);
			} else {
				ArrayList<Unit> a = new ArrayList<>();
				a.add(units.get(i));
				r.put(assignments[i], a);
			}
		}

		return r;
	}

}
