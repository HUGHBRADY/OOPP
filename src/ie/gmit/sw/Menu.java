package ie.gmit.sw;

import java.util.Scanner;

public class Menu {
	private boolean running = true;
	private int choice;
	private Scanner sc = new Scanner(System.in);
	private String fName1;
	private String fName2;
	private int shingleSize, k, bqSize;
	
		// doc1, doc2, shingleSize, k (number of minhashes), blockingQueueSize initialized here
	public void show(){
		do {
			System.out.println("---- Main Menu ----\n1) Compare Documents\n2) Quit Application");
			choice = sc.nextInt();
			
			switch (choice) {
			case 1: 
				compareDocs();
				break;
			
			case 2:
				running = false;
				System.out.println("Exiting");
				break;
			}
		} while(running);
	}
	
	private void compareDocs() {
		System.out.println("Document Comparison Service");
		
		System.out.println("Enter file 1 name: ");
		fName1 = sc.next();
		System.out.println("Enter file 2 name: ");
		fName2 = sc.next();
		System.out.println("Enter shingle size: ");
		shingleSize = sc.nextInt();
		System.out.println("Enter k (number of minhashes): ");
		k = sc.nextInt();
		System.out.println("Enter blocking queue size: ");
		bqSize = sc.nextInt();
		
		Launcher.Launch(fName1, fName2, shingleSize, k, bqSize);

	}
	
}
