#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

#parse("File Header.java")
public class ${NAME} {

    private static final Logger log = LoggerFactory.getLogger(${NAME}.class);
    
}
