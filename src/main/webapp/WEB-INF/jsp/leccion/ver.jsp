<%@ page import="com.liferay.portlet.journal.model.JournalArticle"%>
<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<h1 class="importante">
    <c:choose>
        <c:when test="${leccion != null}">
            ${leccion.title}
        </c:when>
        <c:otherwise>
            Esta lecci�n no ha sido cargada (favor de avisar al administrador)
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
    <div>
        <div>
            <c:choose>
                <c:when test="${assetEntry.viewCount eq 1}">
                    ${assetEntry.viewCount} <liferay-ui:message key="view" />,&nbsp;
                </c:when>
                <c:when test="${assetEntry.viewCount gt 1}">
                    ${assetEntry.viewCount} <liferay-ui:message key="views" />,&nbsp;
                </c:when>
            </c:choose>
            <liferay-ui:flags
                className="<%= JournalArticle.class.getName() %>"
                classPK="${leccion.resourcePrimKey}"
                contentTitle="${leccion.title}"
                reportedUserId="${leccion.userId}"
                />
            <liferay-ui:ratings
                className="<%= JournalArticle.class.getName() %>"
                classPK="${leccion.resourcePrimKey}"
                />
        </div>
        <div style="padding: 10px 0;">
            <!-- AddThis Button BEGIN -->
            <div class="addthis_toolbox addthis_default_style ">
                <a class="addthis_button_facebook_like" fb:like:layout="button_count"></a>
                <a class="addthis_button_tweet"></a>
                <a class="addthis_counter addthis_pill_style"></a>
            </div>
            <script type="text/javascript">var addthis_config = {"data_track_clickback":true};</script>
            <script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=ra-4d8a78014d97ad87"></script>
            <!-- AddThis Button END -->
        </div>
        <div style="padding: 10px 0;">
            <c:if test="${discussionMessages != null}">
                <liferay-ui:tabs names="comments" />
            </c:if>

            <portlet:actionURL var="discussionURL">
                <portlet:param name="action" value="discusion" />
                <portlet:param name="entradaId" value="${leccion.resourcePrimKey}" />
                <portlet:param name="assetId" value="${assetEntry.primaryKey}" />
            </portlet:actionURL>

            <liferay-ui:discussion
                formName="fm${leccion.resourcePrimKey}"
                formAction="${discussionURL}"
                className="<%= JournalArticle.class.getName() %>"
                classPK="${leccion.resourcePrimKey}"
                userId="${leccion.userId}"
                subject="${leccion.title}"
                redirect="${currentURL}"
                ratingsEnabled="true"
                />
        </div>
    </div>

</c:if>

