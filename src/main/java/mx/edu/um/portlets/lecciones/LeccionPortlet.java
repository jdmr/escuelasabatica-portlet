package mx.edu.um.portlets.lecciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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
    public String ver() {
        log.debug("Viendo la leccion");
        return "leccion/ver";
    }
}
