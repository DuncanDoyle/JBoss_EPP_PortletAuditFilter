package com.redhat.jboss.portal.filter;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.EventFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.ResourceFilter;

import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SecurityContextUtil;

/**
 * JBoss Enterprise Portal Platform auditing filter.
 * <p/>
 * Audits who accesses which portlets on the system.
 * <p/>
 * Subclasses of this class should implement the {@link #getLogger()} AbstractFactoryMethod to provide a {@link AuditLogger} implementation
 * that implements the fucntionality to log to a specific system (i.e. Log4J, Database, custom auditting system, etc.).
 * 
 * @author <a href="mailto:duncan.doyle@redhat.com">Duncan Doyle</a>
 */
public abstract class AbstractPortletAuditingFilter implements ActionFilter, RenderFilter, EventFilter, ResourceFilter {

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws PortletException {
	}

	/**
	 * Abstract Factory Method. Returns the logger with which {@link PortletAuditEntity PortletAuditEntities} can be logged.
	 * 
	 * @return the {@link AuditLogger}
	 */
	public abstract AuditLogger getLogger();

	/**
	 * Action-Filter implementation.
	 */
	public void doFilter(ActionRequest req, ActionResponse resp, FilterChain chain) throws IOException, PortletException {
		getLogger().logAuditEntity(buildAuditEntity(req));
		chain.doFilter(req, resp);
	}

	/**
	 * Event-Filter implementation.
	 */
	public void doFilter(EventRequest req, EventResponse resp, FilterChain chain) throws IOException, PortletException {
		getLogger().logAuditEntity(buildAuditEntity(req));
		chain.doFilter(req, resp);
	}

	/**
	 * Render-filter implementation.
	 */
	public void doFilter(RenderRequest req, RenderResponse resp, FilterChain chain) throws IOException, PortletException {
		getLogger().logAuditEntity(buildAuditEntity(req));
		chain.doFilter(req, resp);
	}

	/**
	 * Resource-filter implementation.
	 */
	public void doFilter(ResourceRequest req, ResourceResponse resp, FilterChain chain) throws IOException, PortletException {
		getLogger().logAuditEntity(buildAuditEntity(req));
		chain.doFilter(req, resp);
	}

	/**
	 * Creates an {@link PortletAuditEntity} instance from the passed {@link PortletRequest}. This {@link PortletAuditEntity} can be logged
	 * by the various {@link AuditLogger AuditLoggers}.
	 * 
	 * @param request
	 *            the {@link PortletRequest} from which we build the {@link PortletAuditEntity}.
	 * @return the built {@link PortletAuditEntity}.
	 */
	protected PortletAuditEntity buildAuditEntity(PortletRequest request) {
		PortletAuditEntity entity = new PortletAuditEntity();
		entity.setUsername(getRemoteUser(request));
		entity.setPortletName(getPortletName(request));
		entity.setLifecyclePhase(getLifecyclePhase(request));
		entity.setContextPath(request.getContextPath());
		return entity;
	}

	/**
	 * Uses the JBoss SX SecurityContext to retrieve the username of the logged in user.
	 * 
	 * @return the username of the logged in user.
	 */
	protected String getUserPrincipalFromSecurityContext() {
		// Retrieve the SecurityContext and SecurityContextUtil which we will use to retrieve the username and credentials.
		SecurityContext secContext = SecurityContextAssociation.getSecurityContext();
		SecurityContextUtil securityContextUtil = secContext.getUtil();
		// Retrieve the username that was passed in the WS-Security UsernameToken.
		String username = securityContextUtil.getUserName();
		return username;
	}

	/**
	 * Retrieves the username of the user making the request.
	 * 
	 * @param request
	 *            the {@link PortletRequest} made by the user.
	 * @return the username of the user that issued the request.
	 */
	protected String getRemoteUser(PortletRequest request) {
		return request.getRemoteUser();
	}

	/**
	 * Retrieves the name of the portlet from the {@link PortletRequest}.
	 * 
	 * @param request
	 *            the {@link PortletRequest} from which the name of the accessed portlet is determined.
	 * @return the name of the portlet being accessed.
	 */
	protected String getPortletName(PortletRequest request) {
		PortletConfig portletConfig = (PortletConfig) request.getAttribute("javax.portlet.config");
		return portletConfig.getPortletName();
	}

	/**
	 * Retrieves the name of the <code>Lifecycle Phase</code> from the {@link PortletRequest}.
	 * 
	 * @param request
	 *            the {@link PortletRequest} from which the lifecycle-phase is retrieved.
	 * @return the lifecycle-phase.
	 */
	protected String getLifecyclePhase(PortletRequest request) {
		return (String) request.getAttribute(PortletRequest.LIFECYCLE_PHASE);
	}

}