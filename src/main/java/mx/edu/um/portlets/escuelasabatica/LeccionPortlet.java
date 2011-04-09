package mx.edu.um.portlets.escuelasabatica;

import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryServiceUtil;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBMessageServiceUtil;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import mx.edu.um.portlets.escuelasabatica.util.Resultado;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author jdmr
 */
@Controller
@RequestMapping("VIEW")
public class LeccionPortlet {

    private static final Logger log = LoggerFactory.getLogger(LeccionPortlet.class);
    /** Cache of old zone IDs to new zone IDs */
    private static Map<String, String> cZoneIdConversion;

    public LeccionPortlet() {
        log.debug("Se ha creado una nueva instancia del portlet de lecciones");
    }

    @RequestMapping
    public String ver(RenderRequest request, RenderResponse response, Model model) {
        log.debug("Viendo la leccion");
        TimeZone tz = null;
        DateTimeZone zone = null;
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        model.addAttribute("currentURL", themeDisplay.getURLCurrent());
        try {
            tz = themeDisplay.getTimeZone();
            zone = DateTimeZone.forID(tz.getID());
        } catch (IllegalArgumentException e) {
            zone = DateTimeZone.forID(LeccionPortlet.getConvertedId(tz.getID()));
        }
        try {
            long scopeGroupId = themeDisplay.getScopeGroupId();

            AssetEntryQuery assetEntryQuery = new AssetEntryQuery();

            DateTime hoy = (DateTime) request.getPortletSession().getAttribute("hoy", PortletSession.APPLICATION_SCOPE);
            if (hoy == null) {
                log.info("No encontre el atributo hoy");
                hoy = new DateTime(zone);
                request.getPortletSession().setAttribute("hoy", hoy, PortletSession.APPLICATION_SCOPE);
            }

            long[] assetTagIds = AssetTagLocalServiceUtil.getTagIds(scopeGroupId, getTags(hoy));

            assetEntryQuery.setAllTagIds(assetTagIds);

            List<AssetEntry> results = AssetEntryServiceUtil.getEntries(assetEntryQuery);

            for (AssetEntry asset : results) {
                log.debug("Asset: " + asset.getTitle() + " : " + asset.getDescription() + " : " + asset.getMimeType() + " : " + asset.getClassName());
                if (asset.getClassName().equals("com.liferay.portlet.journal.model.JournalArticle")) {
                    JournalArticle ja = JournalArticleLocalServiceUtil.getLatestArticle(asset.getClassPK());
                    AssetEntryServiceUtil.incrementViewCounter(asset.getClassName(), ja.getResourcePrimKey());
                    String contenido = JournalArticleLocalServiceUtil.getArticleContent(ja.getGroupId(), ja.getArticleId(), "view", "" + themeDisplay.getLocale(), themeDisplay);
                    model.addAttribute("leccion", ja);
                    model.addAttribute("contenido", contenido);
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("EEEE dd/MM/yyyy");
                    DateTimeFormatter fmt2 = fmt.withLocale(themeDisplay.getLocale());
                    StringBuilder sb = new StringBuilder(fmt2.print(hoy));
                    sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
                    model.addAttribute("fecha", sb.toString());
                    model.addAttribute("assetEntry", asset);
                    int discussionMessagesCount = MBMessageLocalServiceUtil.getDiscussionMessagesCount(PortalUtil.getClassNameId(JournalArticle.class.getName()), ja.getPrimaryKey(), WorkflowConstants.STATUS_APPROVED);
                    if (discussionMessagesCount > 0) {
                        model.addAttribute("discussionMessages", true);
                    }

                    break;
                }
            }

        } catch (Exception e) {
            log.error("No se pudo cargar el contenido", e);
            throw new RuntimeException("No se pudo cargar el contenido", e);
        }



        return "leccion/ver";
    }

    @RequestMapping(params = "action=navega")
    public void navega(ActionRequest request, ActionResponse response,
            @ModelAttribute("resultado") Resultado resultado, BindingResult result,
            Model model, SessionStatus sessionStatus, @RequestParam(required = false) Integer dias) {
        log.debug("Navegando");
        TimeZone tz = null;
        DateTimeZone zone = null;
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        try {
            tz = themeDisplay.getTimeZone();
            zone = DateTimeZone.forID(tz.getID());
        } catch (IllegalArgumentException e) {
            zone = DateTimeZone.forID(LeccionPortlet.getConvertedId(tz.getID()));
        }
        DateTime hoy = (DateTime) request.getPortletSession().getAttribute("hoy", PortletSession.APPLICATION_SCOPE);
        if (hoy == null) {
            hoy = new DateTime(zone);
        }

        DateTime inicio = new DateTime(hoy.getYear(), 3, 26, 0, 0, 0, 0, hoy.getZone());

        log.debug("Dias: {}", dias);
        if (dias != null && dias < 0) {
            hoy = hoy.minusDays(dias * (-1));
        } else if (dias != null && dias > 0) {
            hoy = hoy.plusDays(dias);
        }
        if (hoy.isBefore(inicio)) {
            hoy = hoy.withDayOfMonth(26);
        }
        request.getPortletSession().setAttribute("hoy", hoy, PortletSession.APPLICATION_SCOPE);
        sessionStatus.setComplete();

    }

    @RequestMapping(params = "action=discusion")
    public void discusion(ActionRequest request, ActionResponse response,
            @ModelAttribute("resultado") Resultado resultado, BindingResult result,
            Model model, SessionStatus sessionStatus,
            @RequestParam("entradaId") Long entradaId,
            @RequestParam("assetId") Long assetId) {
        log.debug("Ver discusion");
        log.debug("EntradaId: " + entradaId);

        try {
            String cmd = ParamUtil.getString(request, Constants.CMD);
            if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
                MBMessage message = updateMessage(request);
            } else if (cmd.equals(Constants.DELETE)) {
                deleteMessage(request);
            }
        } catch (Exception e) {
            log.error("Error al intentar actualizar el mensaje", e);
        }

        response.setRenderParameter("action", "completo");
        response.setRenderParameter("entradaId", entradaId.toString());
        response.setRenderParameter("assetId", assetId.toString());
    }

    protected void deleteMessage(ActionRequest actionRequest) throws Exception {
        long groupId = PortalUtil.getScopeGroupId(actionRequest);

        String className = ParamUtil.getString(actionRequest, "className");
        long classPK = ParamUtil.getLong(actionRequest, "classPK");

        String permissionClassName = ParamUtil.getString(
                actionRequest, "permissionClassName");

        long permissionClassPK = ParamUtil.getLong(
                actionRequest, "permissionClassPK");

        long messageId = ParamUtil.getLong(actionRequest, "messageId");


        MBMessageServiceUtil.deleteDiscussionMessage(
                groupId, className, classPK, permissionClassName, permissionClassPK,
                messageId);
    }

    protected MBMessage updateMessage(ActionRequest actionRequest)
            throws Exception {

        String className = ParamUtil.getString(actionRequest, "className");
        long classPK = ParamUtil.getLong(actionRequest, "classPK");
        String permissionClassName = ParamUtil.getString(
                actionRequest, "permissionClassName");
        long permissionClassPK = ParamUtil.getLong(
                actionRequest, "permissionClassPK");

        long messageId = ParamUtil.getLong(actionRequest, "messageId");

        long threadId = ParamUtil.getLong(actionRequest, "threadId");
        long parentMessageId = ParamUtil.getLong(
                actionRequest, "parentMessageId");
        String subject = ParamUtil.getString(actionRequest, "subject");
        String body = ParamUtil.getString(actionRequest, "body");

        ServiceContext serviceContext = ServiceContextFactory.getInstance(
                MBMessage.class.getName(), actionRequest);

        MBMessage message = null;

        if (messageId <= 0) {

            // Add message

            message = MBMessageServiceUtil.addDiscussionMessage(
                    serviceContext.getScopeGroupId(), className, classPK,
                    permissionClassName, permissionClassPK, threadId,
                    parentMessageId, subject, body, serviceContext);
        } else {

            // Update message

            message = MBMessageServiceUtil.updateDiscussionMessage(
                    className, classPK, permissionClassName, permissionClassPK,
                    messageId, subject, body, serviceContext);
        }

        return message;
    }

    private String[] getTags(DateTime hoy) {
        String[] tags = new String[4];
        DateTime inicio = new DateTime(hoy.getYear(), 3, 26, 0, 0, 0, 0, hoy.getZone());
        log.debug("HOY: {}", hoy);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(2);
        tags[0] = new Integer(hoy.getYear()).toString();
        tags[1] = "t2";
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
                tags[3] = "sabado";
                break;
            case 7:
                tags[3] = "domingo";
                break;
        }
        log.debug("TAGS: {} {} {} {}", tags);

        return tags;
    }

    private static synchronized String getConvertedId(String id) {
        Map<String, String> map = cZoneIdConversion;
        if (map == null) {
            // Backwards compatibility with TimeZone.
            map = new HashMap<String, String>();
            map.put("GMT", "UTC");
            map.put("MIT", "Pacific/Apia");
            map.put("HST", "Pacific/Honolulu");
            map.put("AST", "America/Anchorage");
            map.put("PST", "America/Los_Angeles");
            map.put("MST", "America/Denver");
            map.put("PNT", "America/Phoenix");
            map.put("CST", "America/Chicago");
            map.put("EST", "America/New_York");
            map.put("IET", "America/Indianapolis");
            map.put("PRT", "America/Puerto_Rico");
            map.put("CNT", "America/St_Johns");
            map.put("AGT", "America/Buenos_Aires");
            map.put("BET", "America/Sao_Paulo");
            map.put("WET", "Europe/London");
            map.put("ECT", "Europe/Paris");
            map.put("ART", "Africa/Cairo");
            map.put("CAT", "Africa/Harare");
            map.put("EET", "Europe/Bucharest");
            map.put("EAT", "Africa/Addis_Ababa");
            map.put("MET", "Asia/Tehran");
            map.put("NET", "Asia/Yerevan");
            map.put("PLT", "Asia/Karachi");
            map.put("IST", "Asia/Calcutta");
            map.put("BST", "Asia/Dhaka");
            map.put("VST", "Asia/Saigon");
            map.put("CTT", "Asia/Shanghai");
            map.put("JST", "Asia/Tokyo");
            map.put("ACT", "Australia/Darwin");
            map.put("AET", "Australia/Sydney");
            map.put("SST", "Pacific/Guadalcanal");
            map.put("NST", "Pacific/Auckland");
            cZoneIdConversion = map;
        }
        return (String) map.get(id);
    }
}
