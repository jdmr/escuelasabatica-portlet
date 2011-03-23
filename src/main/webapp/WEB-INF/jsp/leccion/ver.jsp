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
<portlet:actionURL var="anterior" >
    <portlet:param name="dias" value="${dias - 1}" />
    <portlet:param name="action" value="navega" />
</portlet:actionURL>
<portlet:actionURL var="siguiente" >
    <portlet:param name="dias" value="${dias + 1}" />
    <portlet:param name="action" value="navega" />
</portlet:actionURL>
<div style="float:left;font-size: 1.2em; letter-spacing: 2px; padding-bottom: 20px;width:150px;">
    <a href="${anterior}"><< <liferay-ui:message key="leccion.anterior" /></a>
</div>
<div style="float:left;width: 56%;text-align: center;font-size: 1.2em; color:#58585A;letter-spacing: 3px;">
    ${fecha}
</div>
<div style="font-size: 1.2em; letter-spacing: 2px; padding-bottom: 20px; text-align: right;">
    <a href="${siguiente}"><liferay-ui:message key="leccion.siguiente" /> >></a>
</div>
<c:if test="${leccion != null}">
    <div class="asset-content">
        <div class="journal-content-article" style="font-size: 1.2em; letter-spacing: 2px;">${contenido}</div>
    </div>
    <liferay-ui:ratings
        className="<%= JournalArticle.class.getName()%>"
        classPK="${leccion.resourcePrimKey}"
        />
</c:if>

