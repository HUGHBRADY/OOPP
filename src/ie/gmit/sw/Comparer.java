package ie.gmit.sw;

/**
 * Compares the two sets of shingles, sees where they intersect and determines how similar the two sets are.
 * @author Hugh
 *
 */

public abstract class Comparer {
	private final int intersect;
	private final int setA;
	private final int setB;

	public Comparer(int intersect, int setA, int setB) {
		this.intersect = intersect;
		this.setA = setA;
		this.setB = setB;
	}

	public abstract float calcJac();

	public int getIntersect() {
		return intersect;
	}

	public int getSetA() {
		return setA;
	}

	public int getSetB() {
		return setB;
	}
}