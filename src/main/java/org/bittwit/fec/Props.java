package org.bittwit.fec;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Props {
		private static final Logger LOG = LoggerFactory.getLogger(Props.class);
        private static Properties props = null;

        private Props() {
                props = new Properties();
                try {
                    	InputStream is = Props.class.getClass().getResourceAsStream("/fec.properties");
                        props.load(is);
                }
                catch (IOException e) {
                        LOG.error(e.getMessage(), e);
                }
        }

        private static synchronized Properties getProperty() {
                if (props == null) {
                        new Props();
                        return Props.props;
                } else {
                        return props;
                }
        }

        public static String get(String key) {

                return getProperty().getProperty(key);
        }
}
