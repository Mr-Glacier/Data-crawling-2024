package zedata.com.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Mr-Glacier
 */
public class LoggingConfig {

    private static final Logger logger = LogManager.getLogger(LoggingConfig.class);

    public static void main(String[] args) {
        // 配置 Log4j 2.x，通常在 classpath 下的 log4j2.xml 文件中配置
        // 你可以使用默认的 log4j2.xml 文件配置，也可以创建自定义配置

        // 你的程序代码
        logger.info("This is an info message");
        logger.error("This is an error message");
    }
}

