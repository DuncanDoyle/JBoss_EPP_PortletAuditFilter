package org.jboss.portal.filter.audit;

/**
 * Entity class which contains portlet auditting information.
 * 
 * @author <a href="mailto:duncan.doyle@redhat.com">Duncan Doyle</a>
 */
public class PortletAuditEntity {
	
	
	private String username;
	
	private String portletName;
	
	private String lifecyclePhase;
	
	private String contextPath;

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getPortletName() {
		return portletName;
	}

	public void setPortletName(String portletName) {
		this.portletName = portletName;
	}

	public String getLifecyclePhase() {
		return lifecyclePhase;
	}

	public void setLifecyclePhase(String lifecyclePhase) {
		this.lifecyclePhase = lifecyclePhase;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	

}
