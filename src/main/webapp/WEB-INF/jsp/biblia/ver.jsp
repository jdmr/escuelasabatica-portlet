<%@ include file="/WEB-INF/jsp/include.jsp" %>

<h1>${ubicacion}</h1>
<div style="letter-spacing: 1px;">${texto}</div>
<c:if test="${not empty texto}">
    <portlet:renderURL var="anterior" >
        <portlet:param name="vid" value="${vid - 5}" />
    </portlet:renderURL>
    <portlet:renderURL var="siguiente" >
        <portlet:param name="vid" value="${vid + 5}" />
    </portlet:renderURL>
    <div id="navegacion" style="font-weight: 1.2em; letter-spacing: 2px; padding-bottom: 10px;">
        <div style="float:left;">
            <a href="${anterior}"><< <liferay-ui:message key="biblia.anterior" /></a>
        </div>
        <div style="text-align: right;">
            <a href="${siguiente}"><liferay-ui:message key="biblia.siguiente" /> >></a>
        </div>
    </div>
</c:if>
