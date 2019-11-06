package pfg.firstmarket.model;

public class CatPath {

	private String ancestor;
	private String descendant;
	private int path_length;
	
	public CatPath(String ancestor, String descendant, int path_length) {
		super();
		this.ancestor = ancestor;
		this.descendant = descendant;
		this.path_length = path_length;
	}

	public String getAncestor() {
		return ancestor;
	}

	public void setAncestor(String ancestor) {
		this.ancestor = ancestor;
	}

	public String getDescendant() {
		return descendant;
	}

	public void setDescendant(String descendant) {
		this.descendant = descendant;
	}

	public int getPath_length() {
		return path_length;
	}

	public void setPath_length(int path_length) {
		this.path_length = path_length;
	}
	
	
}
