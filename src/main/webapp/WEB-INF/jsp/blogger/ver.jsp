<%@ include file="/WEB-INF/jsp/include.jsp" %>
<c:choose>
    <c:when test="${imageUrl != null && not empty imageUrl}">
        <img alt='<liferay-ui:message key="user-portrait" />' class="user-profile-image" src="${imageUrl}" style="float: left; padding: 0 10px 10px 0;" />

        <c:if test="${not empty blogger}">
            <p style="font-weight: bold;">${detalle.fullName}</p> 
            <liferay-ui:message key="posts" />: ${blogger.entryCount}<br/>
            <liferay-ui:message key="stars" />: ${blogger.ratingsTotalEntries}<br/>
            <liferay-ui:message key="lastPost" />: ${ultimoPost}<br/>
        </c:if>

        <hr style="margin-top: 20px;"/>
        <c:if test="${not empty detalle.contact.jobTitle }">
            <p>
                <span class="user-job-title"><liferay-ui:message key="job-title" />:</span><br/>
                ${detalle.contact.jobTitle}
            </p>
        </c:if>

        <c:if test="${not empty detalle.contact.twitterSn}">
            <p>
                <span class="user-twitter-sn"><liferay-ui:message key="twitter" />:</span>
                <a class="user-twitter-link" href="http://twitter.com/${detalle.contact.twitterSn}">${detalle.contact.twitterSn}</a>
            </p>
        </c:if>

        <c:if test="${not empty detalle.contact.facebookSn}">
            <p>
                <span class="user-facebook-sn"><liferay-ui:message key="facebook" />:</span>
                ${detalle.contact.facebookSn}
            </p>
        </c:if>

        <c:if test="${not empty aboutMe}">
            <p>
                ${aboutMe}
            </p>
        </c:if>
        <c:if test="${not empty direccion}">
            <p>
                <a href="${direccion}"><liferay-ui:message key="verPosts" /></a>
            </p>
        </c:if>
        <c:if test="${not empty profileUrl}">
            <p>
                <a href="${profileUrl}"><liferay-ui:message key="verPerfil" /></a>
            </p>
        </c:if>
    </c:when>
    <c:otherwise>
        <c:forEach items="${resultados}" var="resultado">
            <liferay-ui:user-display userId="${resultado.statsUser.userId}" url="${resultado.url}">
                <liferay-ui:message key="posts" />: ${resultado.statsUser.entryCount}<br />
                <liferay-ui:message key="stars" />: ${resultado.statsUser.ratingsTotalEntries}<br />
                <liferay-ui:message key="date" />: ${resultado.fecha}
            </liferay-ui:user-display>
        </c:forEach>
    </c:otherwise>
</c:choose>
