package mx.edu.um.portlets.escuelasabatica.util;

import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.model.BlogsStatsUser;

/**
 *
 * @author jdmr
 */
public class Resultado {

    private BlogsStatsUser statsUser;
    private String url;
    private String fecha;

    public Resultado() {
    }

    public Resultado(BlogsStatsUser statsUser, String url, String fecha) {
        this.statsUser = statsUser;
        this.url = url;
        this.fecha = fecha;
    }

    public BlogsStatsUser getStatsUser() {
        return statsUser;
    }

    public void setStatsUser(BlogsStatsUser statsUser) {
        this.statsUser = statsUser;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
