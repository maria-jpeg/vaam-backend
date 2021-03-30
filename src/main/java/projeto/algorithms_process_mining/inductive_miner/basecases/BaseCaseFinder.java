package projeto.algorithms_process_mining.inductive_miner.basecases;

import projeto.algorithms_process_mining.inductive_miner.efficienttree.EfficientTree;
import projeto.algorithms_process_mining.inductive_miner.loginfo.IMLogInfo;
import projeto.algorithms_process_mining.inductive_miner.logs.IMLog;
import projeto.algorithms_process_mining.inductive_miner.mining.MinerState;

public interface BaseCaseFinder {

	EfficientTree findBaseCases(IMLog log, IMLogInfo logInfo, MinerState minerState);

}
