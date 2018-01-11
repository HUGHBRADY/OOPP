package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Launcher {
	
	
	public static void Launch(String f1, String f2, int shingleSize, int bqSize, int k) {
		
		BlockingQueue<Shingle> q = new LinkedBlockingQueue<>(bqSize);
		Thread t1 = new Thread(new DocumentParser(f1, q, shingleSize, k), "t1");
		Thread t2 = new Thread(new DocumentParser(f2, q, shingleSize, k), "t2");
		t1.start();
		t2.start();			// C:/my/path/to/file.txt 	<-- Absolute Path
		try {				// ../file.txt 				<-- Relative Path (do this)
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
