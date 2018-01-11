package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.*;

import ie.gmit.sw.Shingle;

/**
 *  Consumer takes in shingles and converts them into minhashes and stores them into a map.
 * @author Hugh Brady
 */

public class Consumer implements Runnable {
	private BlockingQueue<Shingle> queue;
	private int k;
	private int[] minhashes; // the random stuff
	private int docCount = 2;
	private Map<Integer, List<Integer>> map = new HashMap<>();
	private ExecutorService pool;
	
	public Consumer(BlockingQueue<Shingle> q, int k) {
		this.queue = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(k);
		init();
	}
	
	public void init() {
		Random random = new Random();
		minhashes = new int[k]; // k = 200 - 300
		for(int i = 0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt();
		}
	}
	/**
	 * 
	 * @param s
	 * @return
	 */
	public synchronized int hasher(Shingle s) {
		int minValue = Integer.MAX_VALUE;
		for (Integer hash : minhashes) {

			// XOR the Shingle hashcode with the generated minhash methods
			int minHashed = s.getHashCode() ^ hash;
			if (minHashed < minValue) {
				minValue = minHashed;
			}
		}
		return minValue;
	}
	
	public void run() {
		
		List<Integer> list1 = new ArrayList<>();
		List<Integer> list2 = new ArrayList<>();
		List<Integer> voidList = new ArrayList<>();
		
		while(docCount > 0) {
			try {
				Shingle s = queue.take();
				
				if(s instanceof Poison)
					docCount--;
				else {
					pool.execute(new Runnable(){
						@Override
						public void run() {
							if (s.getDocID() == 1) {
								list1.add(hasher(s));
							} 
							else if (s.getDocID() == 2) {
								list2.add(hasher(s));
							} 
							else {
								voidList.add(hasher(s));
							}
						}
					});
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // blocking method. Won't behave properly if you use pull()
		}
		
		shutdownAndAwaitTermination(pool);
		
		int setA = list1.size();
		int setB = list2.size();

		// put file1List, file2List to map at index
		map.put(1, list1);
		map.put(2, list2);

		List<Integer> intersect = map.get(1);
		intersect.retainAll(map.get(2));

		// compute Jacard index
		Comparer jac = new Jaccard(intersect.size(), setA, setB);
		Menu.displayResults(jac);
	}
	
	void shutdownAndAwaitTermination(ExecutorService pool) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait for tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel executions
				// Wait for tasks to respond
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			pool.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
}