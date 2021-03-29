package projeto.algorithms_process_mining.inductive_miner.mining;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.google.common.util.concurrent.MoreExecutors;

public class MinerState {
	public final MiningParameters parameters;
	private final ExecutorService minerPool;
	private final ExecutorService satPool;

	public MinerState(MiningParameters parameters) {
		this.parameters = parameters;

		if (!parameters.isUseMultithreading()) {
			minerPool = MoreExecutors.sameThreadExecutor();
			satPool = MoreExecutors.sameThreadExecutor();
		} else {
			minerPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
				public Thread newThread(Runnable r) {
					return new Thread(r, "IM miner pool thread");
				}
			});
			satPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
				public Thread newThread(Runnable r) {
					return new Thread(r, "IM sat pool thread");
				}
			});
		}
	}

	public ExecutorService getMinerPool() {
		return minerPool;
	}

	public ExecutorService getSatPool() {
		return satPool;
	}

	public boolean isCancelled() {
		return canceller.isCancelled();
	}

	public void shutdownThreadPools() {
		minerPool.shutdownNow();
		satPool.shutdownNow();
	}
}
