/**
 * 
 */
package data;

/**
 * The different groups
 */
public enum Group {
	ACCOUNT_REQUEST_REVIEWERS(1);
	
	public final int id;
	
	Group(int id) {
		this.id = id;
	}
}
