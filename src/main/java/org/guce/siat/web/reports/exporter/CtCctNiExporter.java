package org.guce.siat.web.reports.exporter;

import java.util.Collections;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.guce.siat.core.ct.model.InterceptionNotification;
import org.guce.siat.web.reports.vo.CtCctNiVo;

/**
 *
 * @author tadzotsa
 */
public class CtCctNiExporter extends AbstractReportInvoker {

    private final InterceptionNotification interceptionNotification;

    public CtCctNiExporter(InterceptionNotification interceptionNotification) {
        super("NOTIFICATION_INTERCEPTION", "NOTIFICATION_INTERCEPTION");
        this.interceptionNotification = interceptionNotification;
    }

    @Override
    protected JRBeanCollectionDataSource getReportDataSource() {
        CtCctNiVo niVo = new CtCctNiVo();

        return new JRBeanCollectionDataSource(Collections.singleton(niVo));
    }

    public InterceptionNotification getInterceptionNotification() {
        return interceptionNotification;
    }

}

