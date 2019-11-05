package pfg.firstmarket.model;

public class CatPath {

	private String ancestor;
	private String descendant;
	private int path_length;
	
	public CatPath(String ancestor_id, String descendant_id, int depth) {
		super();
		this.ancestor = ancestor_id;
		this.descendant = descendant_id;
		this.path_length = depth;
	}

	public String getAncestor_id() {
		return ancestor;
	}

	public void setAncestor_id(String ancestor_id) {
		this.ancestor = ancestor_id;
	}

	public String getDescendant_id() {
		return descendant;
	}

	public void setDescendant_id(String descendant_id) {
		this.descendant = descendant_id;
	}

	public int getDepth() {
		return path_length;
	}

	public void setDepth(int depth) {
		this.path_length = depth;
	}
	
	
}
