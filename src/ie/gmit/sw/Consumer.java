package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.*;

public class Consumer {
	private BlockingQueue<Shingle> queue;
	private int k;
	private int[] minhashes; // the random stuff
	private int docCount = 2;
	private Map<Integer, List<Integer>> map = new HashMap<>();
	private ExecutorService pool;
	
	public Consumer(BlockingQueue<Shingle> q, int k, int poolSize) {
		this.queue = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(poolSize);
		init();
	}
	
	public void init() {
		Random random = new Random();
		minhashes = new int[k]; // k = 200 - 300
		for(int i = 0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt();
		}
	}
	
	public synchronized int hasher(Shingle s) {
		int minValue = Integer.MAX_VALUE;
		for (Integer hash : minHashes) {

			// XOR the Shingle hashcode with the generated minhash methods
			int minHashed = s.getShingleHashCode() ^ hash;
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
							} else if (s.getDocID() == 2) {
								list2.add(hasher(s));
							} else {
								voidList.add(hasher(s));
							}
						}
					});
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // blocking method. Won't behave properly if you use pull()
		}
	}
}