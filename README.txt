PortletAuditingFilter Installation:

The project has not been 'Mavenized' yet, so a JAR needs to be created from, for example, a JBoss Developer Studio (or plain Eclipse project).
It's required to have the JBoss EPP server runtime and portlet libraries on the classpath in order to be able to compile and package the JAR file.

* Deploy the JAR file in the '{jboss-epp}/jboss-as/server/{profile}/lib' directory to.
* Configure the portlet filter for all portlets. This can be done by configuring the filter in the '{jboss-epp}/jboss-as/server/{profile}/conf/gatein/portlet.xml' file:

<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright (C) 2009 eXo Platform SAS.
    
    This is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation; either version 2.1 of
    the License, or (at your option) any later version.
    
    This software is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
    Lesser General Public License for more details.
    
    You should have received a copy of the GNU Lesser General Public
    License along with this software; if not, write to the Free
    Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
    02110-1301 USA, or see the FSF site: http://www.fsf.org.

-->
<portlet-app version="1.0" xmlns="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd">

  <!-- This filter collects runtime request statistics -->
  <filter>
    <filter-name>org.exoplatform.portal.application.ApplicationMonitoringFilter</filter-name>
    <filter-class>org.exoplatform.portal.application.ApplicationMonitoringFilter</filter-class>
    <lifecycle>ACTION_PHASE</lifecycle>
    <lifecycle>RENDER_PHASE</lifecycle>
    <lifecycle>EVENT_PHASE</lifecycle>
    <lifecycle>RESOURCE_PHASE</lifecycle>
  </filter>
  <!-- ddoyle: If we can filter application statistics, we can also do audit logging :-) -->
  <filter>
    <filter-name>org.jboss.portal.filter.audit.Log4JPortletAuditingFilter</filter-name>
    <filter-class>org.jboss.portal.filter.audit.Log4JPortletAuditingFilter</filter-class>
    <lifecycle>ACTION_PHASE</lifecycle>
    <lifecycle>RENDER_PHASE</lifecycle>
    <lifecycle>EVENT_PHASE</lifecycle>
    <lifecycle>RESOURCE_PHASE</lifecycle>
  </filter>

</portlet-app>


* Our current implementation provides and AuditFilter which logs to Log4J. Configure the Log4J Logger for 'org.jboss.portal.filter.audit.Log4JPortletAuditingFilter' to log on 'DEBUG' level.
In JBoss Enterprise Portal Platform this can be done in the '{jboss-epp}/jboss-as/server/{profile}/congf/jboss-log4j.xml'. In this example we log to a dedicated 'AUDIT' appender:

<appender name="AUDIT" class="org.jboss.logging.appender.DailyRollingFileAppender">
	<errorHandler class="org.jboss.logging.util.OnlyOnceErrorHandler"/>
	<param name="File" value="${jboss.server.log.dir}/audit.log"/>
	<param name="Append" value="true"/>
	<param name="DatePattern" value="'.'yyyy-MM-dd"/>
	<layout class="org.apache.log4j.PatternLayout">
		<param name="ConversionPattern" value="%d %-5p [%c] (%t:%x) %m%n"/>
	</layout>
</appender>

<category name="org.jboss.portal.filter.audit.Log4JPortletAuditingFilter" additivity="false">
	<priority value="DEBUG"/>
	<appender-ref ref="AUDIT"/>
</category>

The audit log lines will appear in the file '{jboss-epp}/jboss-as/server/{profile}/log/audit.log:

 2012-08-08 17:43:22,677 DEBUG [org.jboss.portal.filter.audit.Log4JPortletAuditingFilter] (http-127.0.0.1-8080-2:) [principal: duncan], [contextPath: /exoadmin], [portletName: StarToolbarPortlet], [lifecyclePhase: RENDER_PHASE]
 2012-08-08 17:43:22,679 DEBUG [org.jboss.portal.filter.audit.Log4JPortletAuditingFilter] (http-127.0.0.1-8080-2:) [principal: duncan], [contextPath: /exoadmin], [portletName: UserToolbarSitePortlet], [lifecyclePhase: RENDER_PHASE]
 2012-08-08 17:43:22,684 DEBUG [org.jboss.portal.filter.audit.Log4JPortletAuditingFilter] (http-127.0.0.1-8080-2:) [principal: duncan], [contextPath: /exoadmin], [portletName: UserToolbarGroupPortlet], [lifecyclePhase: RENDER_PHASE]
 2012-08-08 17:43:22,690 DEBUG [org.jboss.portal.filter.audit.Log4JPortletAuditingFilter] (http-127.0.0.1-8080-2:) [principal: duncan], [contextPath: /exoadmin], [portletName: UserToolbarDashboardPortlet], [lifecyclePhase: RENDER_PHASE]
 2012-08-08 17:43:22,693 DEBUG [org.jboss.portal.filter.audit.Log4JPortletAuditingFilter] (http-127.0.0.1-8080-2:) [principal: duncan], [contextPath: /exoadmin], [portletName: AdminToolbarPortlet], [lifecyclePhase: RENDER_PHASE]
 2012-08-08 17:43:22,695 DEBUG [org.jboss.portal.filter.audit.Log4JPortletAuditingFilter] (http-127.0.0.1-8080-2:) [principal: duncan], [contextPath: /exoadmin], [portletName: UserInfoPortlet], [lifecyclePhase: RENDER_PHASE]
 2012-08-08 17:43:22,707 DEBUG [org.jboss.portal.filter.audit.Log4JPortletAuditingFilter] (http-127.0.0.1-8080-2:) [principal: duncan], [contextPath: /web], [portletName: LogoPortlet], [lifecyclePhase: RENDER_PHASE]
 2012-08-08 17:43:22,930 DEBUG [org.jboss.portal.filter.audit.Log4JPortletAuditingFilter] (http-127.0.0.1-8080-2:) [principal: duncan], [contextPath: /web], [portletName: NavigationPortlet], [lifecyclePhase: RENDER_PHASE]
 2012-08-08 17:43:22,947 DEBUG [org.jboss.portal.filter.audit.Log4JPortletAuditingFilter] (http-127.0.0.1-8080-2:) [principal: duncan], [contextPath: /web], [portletName: IFramePortlet], [lifecyclePhase: RENDER_PHASE]
 2012-08-08 17:43:23,028 DEBUG [org.jboss.portal.filter.audit.Log4JPortletAuditingFilter] (http-127.0.0.1-8080-2:) [principal: duncan], [contextPath: /web], [portletName: FooterPortlet], [lifecyclePhase: RENDER_PHASE]



