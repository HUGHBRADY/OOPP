package ie.gmit.sw;

/**
 * Array of strings populated by the file parser.
 * @author Hugh Brady
 *
 */

public class Shingle {
	private int docID;
	private int hashCode;
	
	public Shingle(int docID, int hashCode) {
		super();
		this.docID = docID;
		this.hashCode = hashCode;
	}
	public int getDocID() {
		return docID;
	}
	public void setDocID(int docID) {
		this.docID = docID;
	}
	public int getHashCode() {
		return hashCode;
	}
	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}
	
}
