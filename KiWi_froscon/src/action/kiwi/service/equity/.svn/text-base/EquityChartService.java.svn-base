/*
 * File : EquiEquityChartService.java Date : Feb 22, 2010 DO NOT ALTER OR REMOVE
 * COPYRIGHT NOTICES OR THIS HEADER. Copyright 2008 The KiWi Project. All rights
 * reserved. http://www.kiwi-project.eu The contents of this file are subject to
 * the terms of either the GNU General Public License Version 2 only ("GPL") or
 * the Common Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the License.
 * You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html or nbbuild/licenses/CDDL-GPL-2-CP.
 * See the License for the specific language governing permissions and
 * limitations under the License. When distributing the software, include this
 * License Header Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP. KiWi designates this particular file as
 * subject to the "Classpath" exception as provided by Sun in the GPL Version 2
 * section of the License file that accompanied this code. If applicable, add
 * the following below the License Header, with the fields enclosed by brackets
 * [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]" If you wish your
 * version of this file to be governed by only the CDDL or only the GPL Version
 * 2, indicate your decision by adding "[Contributor] elects to include this
 * software in this distribution under the [CDDL or GPL Version 2] license." If
 * you do not indicate a single choice of license, a recipient has the option to
 * distribute your version of this file under either the CDDL, the GPL Version 2
 * or to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL Version
 * 2 license, then the option applies only if the new code is made subject to
 * such option by the copyright holder. Contributor(s):
 */


package kiwi.service.equity;


import java.awt.Font;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import kiwi.api.equity.EquityService;
import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * this service returns a chart image of
 * 
 * @author tkurz
 */
@Name("equityChartService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class EquityChartService {

    @In(create = true)
    private EquityService equityService;

    // simple date formater
    private static final DateFormat DF = new SimpleDateFormat(
            "dd.MM.yy HH:mm:ss", Locale.GERMAN);

    // width and height of resulting picture (in px)
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;

    /**
     * get chart for ContentItem ci from creation until now
     * 
     * @param ci
     * @return
     */
    public BufferedImage getChart(ContentItem ci, int steps) {

        final Map<Date, Long> equityValues = equityService.getEquityValues(ci,
                steps);
        final Date created = ci.getCreated();
        final JFreeChart chart = getChart(equityValues, created, new Date());

        return chart.createBufferedImage(WIDTH, HEIGHT);
    }

    /**
     * get chart for ContentItem ci from Date start until Date end
     * 
     * @param ci
     * @param start
     * @param end
     * @return
     */
    public BufferedImage getChart(ContentItem ci, Date start, Date end,
            int steps) {

        final Map<Date, Long> equityValues = equityService.getEquityValues(ci,
                start, end, steps);
        final JFreeChart chart = getChart(equityValues, start, end);

        return chart.createBufferedImage(WIDTH, HEIGHT);
    }

    /**
     * get chart from a map of time-value pairs from start until end
     * 
     * @param values
     * @param start
     * @param end
     * @return
     */
    private JFreeChart getChart(Map<Date, Long> values, Date start, Date end) {

        final TimeSeries pop = new TimeSeries("Population", Millisecond.class);
        for (final Map.Entry<Date, Long> v : values.entrySet()) {
            pop.add(new Millisecond(v.getKey()), v.getValue());
        }
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(pop);

        JFreeChart chart = ChartFactory.createTimeSeriesChart("", "time",
                "value", dataset, false, false, false);
        
        chart.setTitle(new TextTitle("Equity Values since "
                + DF.format(start) + " until " + DF.format(end),
        		new Font("SansSerif", Font.BOLD, 14)
        ));
   
        return chart;
    }

}
