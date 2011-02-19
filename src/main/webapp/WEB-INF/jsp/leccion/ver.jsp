<%@ page import="com.liferay.portlet.journal.model.JournalArticle"%>
<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<div class="ES">
    HOLA MUNDO!!!
    <c:choose>
        <c:when test="${articleDisplay != null}">
            <h1>${articleDisplay.title}</h1>

            <div class="asset-content">

                <c:if test="${availableLocales != null}">
                    <div>
                        <liferay-ui:language languageIds="${availableLocales}" displayStyle="${0}" />
                    </div>
                </c:if>

                <div class="journal-content-article">${articleDisplay.content}</div>

                <br />

                <liferay-ui:ratings
                    className="<%= JournalArticle.class.getName()%>"
                    classPK="${articleDisplay.resourcePrimKey}"
                />

                <c:if test="${discussionMessages != null}">
                    <br />
                    <liferay-ui:tabs names="comments" />
                </c:if>

                <portlet:actionURL var="discussionURL">
                    <portlet:param name="action" value="discusion" />
                    <portlet:param name="cursoId" value="${curso.id}" />
                    <portlet:param name="contenidoId" value="${contenidoId}" />
                </portlet:actionURL>

                <liferay-ui:discussion
                    formName="fm${articleDisplay.resourcePrimKey}"
                    formAction="${discussionURL}"
                    className="<%= JournalArticle.class.getName()%>"
                    classPK="${articleDisplay.resourcePrimKey}"
                    userId="${articleDisplay.userId}"
                    subject="${articleDisplay.title}"
                    redirect="${currentURL}"
                    ratingsEnabled="true"
                />

                <br />


            </div>
        </c:when>
        <c:when test="${image != null}">
            <h1>${contenido.title}</h1>
            <img border="1" src="${imageURL}" />
            <p>${image.description}</p>
        </c:when>
        <c:when test="${document != null}">
            <h1>${document.title}</h1>
            <a href="${documentURL}">${document.name}</a>
            <p>${document.description}</p>
        </c:when>
    </c:choose>
</div>

