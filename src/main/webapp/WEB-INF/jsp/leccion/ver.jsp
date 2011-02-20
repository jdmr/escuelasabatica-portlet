<%@ page import="com.liferay.portlet.journal.model.JournalArticle"%>
<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<c:if test="${leccion != null}">
    <h1>${leccion.title}</h1>
    <div class="asset-content">
        <div class="journal-content-article">${contenido}</div>
    </div>
    <liferay-ui:ratings
        className="<%= JournalArticle.class.getName()%>"
        classPK="${leccion.resourcePrimKey}"
        />
</c:if>

