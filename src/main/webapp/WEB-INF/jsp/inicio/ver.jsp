<%@ include file="/WEB-INF/jsp/include.jsp" %>

<div style="letter-spacing: 1px;">
    <div style="float:right;width: 190px;">
        <!-- AddThis Button BEGIN -->
        <div class="addthis_toolbox addthis_default_style addthis_32x32_style">
            <a class="addthis_button_preferred_1"></a>
            <a class="addthis_button_preferred_2"></a>
            <a class="addthis_button_preferred_3"></a>
            <a class="addthis_button_preferred_4"></a>
            <a class="addthis_button_compact"></a>
        </div>
        <script type="text/javascript">var addthis_config = {"data_track_clickback":true};</script>
        <script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=ra-4d8a78014d97ad87"></script>
        <!-- AddThis Button END -->
    </div>
    <div>
        <h1 class="importante">${tituloPrincipal}</h1>
    </div>
    <p>
        <img alt="" src="/image/image_gallery?uuid=23748d46-c2a8-49e6-b6c8-993e33ba24be&amp;groupId=15711&amp;t=1296830006464" style="padding-top: 0px; padding-right: 10px; padding-bottom: 10px; padding-left: 0px; float: left; width: 552px; height: 270px; " />
    </p>
    <h2 class="importante">${titulo}</h2>
    <p>${fecha}</p>
    <p>${contenido}</p>
    <p style="text-align: center;padding:15px 0 20px 0;">
        <a class="importante" href="/estudia">ENTRAR</a>
    </p>
    <hr />    
</div>