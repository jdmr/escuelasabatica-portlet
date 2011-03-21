package mx.edu.um.portlets.escuelasabatica;

import com.liferay.portal.kernel.servlet.ImageServletTokenUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.model.BlogsStatsUser;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.blogs.service.BlogsStatsUserLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import mx.edu.um.portlets.escuelasabatica.util.Resultado;
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
public class BloggerPortlet {

    private static final Logger log = LoggerFactory.getLogger(BloggerPortlet.class);

    public BloggerPortlet() {
        log.debug("Se ha creado una nueva instancia del portlet de versiculos");
    }

    @RequestMapping
    public String ver(RenderRequest request, RenderResponse response, Model model) {
        log.debug("Mostrando al blogger");
        try {
            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
            BlogsEntry entry = (BlogsEntry) request.getAttribute("BLOGS_ENTRY");
            if (entry != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Long groupId = entry.getGroupId();
                Long userId = entry.getUserId();
                BlogsStatsUser user = BlogsStatsUserLocalServiceUtil.getStatsUser(groupId, userId);
                User detalle = UserLocalServiceUtil.getUser(userId);
                String aboutMe = HtmlUtil.escape(ExpandoValueLocalServiceUtil.getData(user.getCompanyId(), User.class.getName(), "SN", "aboutMe", detalle.getUserId(), StringPool.BLANK));

                String imageUrl = themeDisplay.getPathImage() + "/user_portrait?img_id=" + detalle.getPortraitId() + "&t=" + ImageServletTokenUtil.getToken(detalle.getPortraitId());
                String profileUrl = "/web/" + detalle.getScreenName();

                StringBundler sb = new StringBundler(4);

                sb.append(themeDisplay.getPathMain());
                sb.append("/blogs/find_entry?entryId=");
                sb.append(entry.getEntryId());
                sb.append("&showAllEntries=1");

                String rowHREF = sb.toString();

                request.setAttribute("blogger", user);
                request.setAttribute("direccion", rowHREF);
                request.setAttribute("ultimoPost", sdf.format(user.getLastPostDate()));
                request.setAttribute("detalle", detalle);
                request.setAttribute("aboutMe", aboutMe);
                request.setAttribute("imageUrl", imageUrl);
                request.setAttribute("profileUrl", profileUrl);
                request.removeAttribute("entries");

            } else {
                log.debug("Mostrando bloggers recientes");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                Long groupId = themeDisplay.getScopeGroupId();
                List<BlogsStatsUser> statsUsers = BlogsStatsUserLocalServiceUtil.getGroupStatsUsers(groupId, 0, 10);
                List<Resultado> resultados = new ArrayList<Resultado>();
                for (int i = 0; i < statsUsers.size(); i++) {
                    BlogsStatsUser statsUser = (BlogsStatsUser) statsUsers.get(i);
                    Group group = GroupLocalServiceUtil.getGroup(statsUser.getGroupId());
                    User user2 = UserLocalServiceUtil.getUserById(statsUser.getUserId());

                    int entryCount = BlogsEntryLocalServiceUtil.getGroupUserEntriesCount(group.getGroupId(), user2.getUserId(), WorkflowConstants.STATUS_APPROVED);

                    List<BlogsEntry> entries = BlogsEntryLocalServiceUtil.getGroupUserEntries(group.getGroupId(), user2.getUserId(), WorkflowConstants.STATUS_APPROVED, 0, 1);

                    //request.setAttribute("entries", entries);
                    for (BlogsEntry blog : entries) {
                        StringBundler sb = new StringBundler(4);

                        sb.append(themeDisplay.getPathMain());
                        sb.append("/blogs/find_entry?entryId=");
                        sb.append(blog.getEntryId());
                        sb.append("&showAllEntries=1");

                        String rowHREF = sb.toString();
                        
                        resultados.add(new Resultado(statsUser, rowHREF, sdf.format(statsUser.getLastPostDate())));
                    }
                    

                }
                request.setAttribute("resultados", resultados);
            }
        } catch (Exception e) {
            log.error("Excepcion al intentar buscar al blogger", e);
        }
        return "blogger/ver";
    }
}
