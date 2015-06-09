/**
 * 
 */
package data;

/** The different Roles */
public enum Role {
	BROTHER(1),
	PRESIDENT(2),
	VPPROGRAMMING(3);
	
	public final int id;
	
	Role(int id) {
		this.id = id;
	}
}
