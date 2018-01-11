package ie.gmit.sw;
/**
 * Jaccard class inherits the Comparer class.
 * @author Hugh Brady 
 */
public class Jaccard extends Comparer {
	private final int intersect;
	private final int setA;
	private final int setB;
	private float jaccard;

	public Jaccard(int intersect, int setA, int setB) {
		super(intersect, setA, setB);
		this.intersect = intersect;
		this.setA = setA;
		this.setB = setB;
	}

	@Override
	public float calcJac() {
		jaccard = (float) intersect / (((float) setA + (float) setB) - (float) intersect);
		return jaccard;
	}

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