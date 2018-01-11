package ie.gmit.sw;

import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
/**
 * A runnable implementation that parses the files
 * @author Hugh Brady
 */
public class DocumentParser implements Runnable {

	private BlockingQueue<Shingle> queue;
	private String file;
	private int shingleSize, k, docId;
	private Deque<String> buffer = new LinkedList<>();
	
	public DocumentParser(String file, BlockingQueue<Shingle> queue, int shingleSize, int k) {
		this.file = file;
		this.queue = queue;
		this.shingleSize = shingleSize;
		this.k = k;
	}


	public void run() {
		System.out.println("Beginning parsing file...");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			
			while((line = br.readLine()) != null) {
				if (line.length() > 0){
					String uLine = line.toUpperCase();
					uLine.replaceAll("[^a-zA-Z]", "");
					String[] words = uLine.split("\\s+"); 
					
					addWordsToBuffer(words);
				}
			}
			
			while(buffer.size() != 0){
				Shingle s = getNextShingle();
				if(s != null){					
					queue.put(s); // Blocking method. Add is not a blocking method
				}
			}
			System.out.println("... Finished parsing");
			
			flushBuffer();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error! File " + file + "not found");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}// run

	private void addWordsToBuffer(String [] words) {
		for(String s : words) {
			buffer.add(s);
		}
	}

  	private Shingle getNextShingle() {
		StringBuffer sb = new StringBuffer();
		int counter = 0;
		while(counter < shingleSize) {
			if(buffer.peek() != null) {
				sb.append(buffer.poll());
				counter++;
			}
			else {
				counter = shingleSize;
			}
		}
		if (sb.length() > 0) {
			return(new Shingle(docId, sb.toString().hashCode()));
		}
		else {
			return(null);
		}
  	} // Next shingle	

	private void flushBuffer() {
		while(buffer.size() > 0) {
			Shingle s = getNextShingle();
			if(s != null) {
				try {
					queue.put(s);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				queue.put(new Poison(0, 0));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}