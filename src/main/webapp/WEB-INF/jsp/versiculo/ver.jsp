<%@ include file="/WEB-INF/jsp/include.jsp" %>

<c:if test="${versiculo != null}">
    <h1 style="color:#58585A; font-size: 2em; letter-spacing: 3px; font-weight: normal;">${versiculo.title}</h1>
    <div class="asset-content">
        <div style="font-size: 1.5em; letter-spacing: 3px;color:#58585A;text-align: justify;">${contenido}</div>
    </div>
</c:if>
