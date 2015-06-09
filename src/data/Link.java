/**
 * 
 */
package data;


/**
 * Contains information about a link
 */
public class Link {
	public final String pageName;
	public final String href;
	
	/** Creates a new link */
	public Link(String pageName, String href) {
		this.pageName = pageName;
		this.href = href;
	}

	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}
}
