package org.jboss.portal.filter.audit;

import org.apache.log4j.Logger;

/**
 * Logs the audit information to Log4J.
 * 
 * @author <a href="mailto:duncan.doyle@redhat.com">Duncan Doyle</a>
 */
public class Log4JPortletAuditingFilter extends AbstractPortletAuditingFilter {

	/**
	 * The Log4J {@link Logger}.
	 */
	private static final Logger LOGGER = Logger.getLogger(Log4JPortletAuditingFilter.class);

	/**
	 * The {@link AuditLogger} adapter.
	 */
	private final Log4JAuditLogger LOG4J_AUDIT_LOGGER = new Log4JAuditLogger();

	/**
	 * @return the {@link Log4JAuditLogger}.
	 */
	@Override
	public AuditLogger getLogger() {
		return LOG4J_AUDIT_LOGGER;
	}

	/**
	 * {@link AuditLogger} implementation which logs {@link PortletAuditEntity PortletAuditEntities} to <code>Log4J</code>.
	 * 
	 * @author <a href="mailto:duncan.doyle@redhat.com">Duncan Doyle</a>
	 */
	private static class Log4JAuditLogger implements AuditLogger {

		/**
		 * Logs the {@link PortletAuditEntity} to Log4J.
		 */
		@Override
		public void logAuditEntity(PortletAuditEntity entity) {
			LOGGER.debug(buildAuditLogLine(entity));
		}

		/**
		 * Creates an audit-log {@link String} from the given {@link PortletAuditEntity}.
		 * 
		 * @param entity
		 *            the {@link PortletAuditEntity} from which we create the audit-log {@link String}.
		 * @return the audit-log {@link String}.
		 */
		private String buildAuditLogLine(PortletAuditEntity entity) {
			StringBuilder lineBuilder = new StringBuilder();

			String username = entity.getUsername();
			lineBuilder.append("[principal: ").append(username).append("]");

			String contextPath = entity.getContextPath();
			lineBuilder.append(", ").append("[contextPath: ").append(contextPath).append("]");

			String portletName = entity.getPortletName();
			lineBuilder.append(", ").append("[portletName: ").append(portletName).append("]");

			String lifecyclePhase = entity.getLifecyclePhase();
			lineBuilder.append(", ").append("[lifecyclePhase: ").append(lifecyclePhase).append("]");

			return lineBuilder.toString();
		}

	}

}
