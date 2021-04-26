package org.guce.siat.web.reports.exporter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author tadzotsa
 */
public class CtCctCpEExporterTest {

    @Test
    public void testCount() {
        List<Container> containers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            containers.add(new Container(RandomStringUtils.random(11), RandomStringUtils.random(10)));
        }
        final List<String> containers1 = new ArrayList<>();
        final List<String> containers2 = new ArrayList<>();
        final int maxContainersNumber = 7;
        final AtomicInteger counter = new AtomicInteger(0);
        Collection<String> containerNumbers = CollectionUtils.collect(containers, new Transformer() {
            @Override
            public String transform(Object input) {
                Container container = (Container) input;
                if (counter.intValue() < maxContainersNumber) {
                    containers1.add(String.format("%s/%s", container.getContNumber(), container.getContSeal1()));
                } else {
                    containers2.add(String.format("%s/%s", container.getContNumber(), container.getContSeal1()));
                }
                counter.addAndGet(1);
                return String.format("%s/%s", container.getContNumber(), container.getContSeal1());
            }
        });
        Assert.assertEquals(maxContainersNumber, containers1.size());
        Assert.assertEquals(containers.size() - maxContainersNumber, containers2.size());
    }

    private class Container implements Serializable {

        private static final long serialVersionUID = -212745789515592123L;

        private String contNumber;
        private String contSeal1;

        public Container() {
        }

        public Container(String contNumber, String contSeal1) {
            this.contNumber = contNumber;
            this.contSeal1 = contSeal1;
        }

        public String getContNumber() {
            return contNumber;
        }

        public void setContNumber(String contNumber) {
            this.contNumber = contNumber;
        }

        public String getContSeal1() {
            return contSeal1;
        }

        public void setContSeal1(String contSeal1) {
            this.contSeal1 = contSeal1;
        }

    }

}
