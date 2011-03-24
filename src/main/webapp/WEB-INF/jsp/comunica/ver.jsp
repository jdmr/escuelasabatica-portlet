<%@ include file="/WEB-INF/jsp/include.jsp" %>
<h1>Comunica</h1>
<c:forEach items="${entries}" var="entrada">
    <div>${entrada.contenido}</div>
    <div style="padding-bottom: 20px;">
        <portlet:renderURL var="completo" >
            <portlet:param name="action" value="completo" />
            <portlet:param name="entradaId" value="${entrada.id}" />
            <portlet:param name="assetId" value="${entrada.assetId}" />
        </portlet:renderURL>
        <a class="importante" href="${completo}">Leer más...</a>
    </div>
</c:forEach>