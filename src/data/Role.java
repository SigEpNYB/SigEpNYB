/**
 * 
 */
package data;

/** The different Roles */
public enum Role {
	SUPERUSER(1),
	BROTHER(2),
	PRESIDENT(3),
	VPPROGRAMMING(4);
	
	public final int id;
	
	Role(int id) {
		this.id = id;
	}
}
