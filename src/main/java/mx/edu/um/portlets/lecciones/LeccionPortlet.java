package mx.edu.um.portlets.lecciones;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryServiceUtil;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import java.text.NumberFormat;
import java.util.List;
import java.util.TimeZone;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Weeks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author jdmr
 */
@Controller
@RequestMapping("VIEW")
public class LeccionPortlet {

    private static final Logger log = LoggerFactory.getLogger(LeccionPortlet.class);

    public LeccionPortlet() {
        log.debug("Se ha creado una nueva instancia del portlet de lecciones");
    }

    @RequestMapping
    public String ver(RenderRequest request, RenderResponse response, Model model) {
        log.debug("Viendo la leccion");
        try {
            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
            PortletPreferences preferences = request.getPreferences();
            String portletResource = ParamUtil.getString(request, "portletResource");

            if (Validator.isNotNull(portletResource)) {
                preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
            }

            long scopeGroupId = themeDisplay.getScopeGroupId();

            AssetEntryQuery assetEntryQuery = new AssetEntryQuery();

            TimeZone tz = themeDisplay.getTimeZone();
            long[] assetTagIds = AssetTagLocalServiceUtil.getTagIds(scopeGroupId, getTags(tz));

            assetEntryQuery.setAllTagIds(assetTagIds);

            List<AssetEntry> results = AssetEntryServiceUtil.getEntries(assetEntryQuery);

            for (AssetEntry asset : results) {
                log.debug("Asset: " + asset.getTitle() + " : " + asset.getDescription() + " : " + asset.getMimeType() + " : " + asset.getClassName());
                if (asset.getClassName().equals("com.liferay.portlet.journal.model.JournalArticle")) {
                    JournalArticle ja = JournalArticleLocalServiceUtil.getLatestArticle(asset.getClassPK());
                    String contenido = JournalArticleLocalServiceUtil.getArticleContent(ja.getGroupId(), ja.getArticleId(), "view", ""+themeDisplay.getLocale(), themeDisplay);
                    model.addAttribute("leccion",ja);
                    model.addAttribute("contenido",contenido);
                }
            }
            
        } catch (Exception e) {
            log.error("No se pudo cargar el contenido", e);
            throw new RuntimeException("No se pudo cargar el contenido", e);
        }



        return "leccion/ver";
    }

    private String[] getTags(TimeZone tz) {
        DateTimeZone zone = DateTimeZone.forID(tz.getID());
        String[] tags = new String[4];
        DateTime inicio = null;
        DateTime hoy = new DateTime(zone);
        log.debug("HOY: {}",hoy);
        tags[0] = new Integer(hoy.getYear()).toString();
        if (hoy.getMonthOfYear() < 4) {
            inicio = new DateTime(hoy.getYear(), 1, 1, 0, 0, 0, 0, zone);
            tags[1] = "t1";
        } else if (hoy.getMonthOfYear() < 7) {
            inicio = new DateTime(hoy.getYear(), 4, 1, 0, 0, 0, 0, zone);
            tags[1] = "t2";
        } else if (hoy.getMonthOfYear() < 10) {
            inicio = new DateTime(hoy.getYear(), 7, 1, 0, 0, 0, 0, zone);
            tags[1] = "t3";
        } else {
            inicio = new DateTime(hoy.getYear(), 10, 1, 0, 0, 0, 0, zone);
            tags[1] = "t4";
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(2);
        Weeks weeks = Weeks.weeksBetween(inicio, hoy);
        tags[2] = "l" + nf.format(weeks.getWeeks() + 1);
        switch (hoy.getDayOfWeek()) {
            case 1:
                tags[3] = "lunes";
                break;
            case 2:
                tags[3] = "martes";
                break;
            case 3:
                tags[3] = "miercoles";
                break;
            case 4:
                tags[3] = "jueves";
                break;
            case 5:
                tags[3] = "viernes";
                break;
            case 6:
                tags[2] = "l" + nf.format(weeks.getWeeks() + 2);
                tags[3] = "sabado";
                break;
            case 7:
                tags[2] = "l" + nf.format(weeks.getWeeks() + 2);
                tags[3] = "domingo";
                break;
        }
        log.debug("TAGS: {} {} {} {}", tags);

        return tags;
    }

}
