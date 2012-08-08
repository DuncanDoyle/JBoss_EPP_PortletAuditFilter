package org.jboss.portal.filter.audit;

/**
 * Logs the {@link PortletAuditEntity}.
 * 
 * @author <a href="mailto:duncan.doyle@redhat.com">Duncan Doyle</a>
 */
public interface AuditLogger {

	/**
	 * Logs the passed {@link PortletAuditEntity}.
	 * 
	 * @param entity
	 *            the {@link PortletAuditEntity} to be logged.
	 */
	public abstract void logAuditEntity(PortletAuditEntity entity);

}
