<%@ include file="/WEB-INF/jsp/include.jsp" %>

<c:if test="${versiculo != null}">
    <h1>${versiculo.title}</h1>
    <div class="asset-content">
        <div class="journal-content-article">${contenido}</div>
    </div>
</c:if>
