<%@ page import="com.liferay.portlet.journal.model.JournalArticle"%>
<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

    <h1 class="importante">
        <c:choose>
            <c:when test="${leccion != null}">
                ${leccion.title}
            </c:when>
            <c:otherwise>
                Esta lección no ha sido cargada (favor de avisar al administrador)
            </c:otherwise>
        </c:choose>
    </h1>
    <portlet:renderURL var="anterior" >
        <portlet:param name="dias" value="${dias - 1}" />
    </portlet:renderURL>
    <portlet:renderURL var="siguiente" >
        <portlet:param name="dias" value="${dias + 1}" />
    </portlet:renderURL>
    <div id="nav" style="font-weight: 1.2em; letter-spacing: 2px; padding-bottom: 10px;">
    <a href="${anterior}"><< <liferay-ui:message key="leccion.anterior" /></a>
    <span style="font-weight: 2.5em !important; padding: 0 10px !important; color:#58585A;">${fecha}</span>
    <a href="${siguiente}"><liferay-ui:message key="leccion.siguiente" /> >></a>
    </div>
<c:if test="${leccion != null}">
    <div class="asset-content">
        <div class="journal-content-article" style="font-weight: 1.2em; letter-spacing: 2px;">${contenido}</div>
    </div>
    <liferay-ui:ratings
        className="<%= JournalArticle.class.getName()%>"
        classPK="${leccion.resourcePrimKey}"
        />
</c:if>

