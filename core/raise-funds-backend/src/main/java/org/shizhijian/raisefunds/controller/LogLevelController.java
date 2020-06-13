package org.shizhijian.raisefunds.controller;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "changeLogLevel")
public class LogLevelController {
    private static Logger logger = LoggerFactory.getLogger(LogLevelController.class);


    @GetMapping(value = "/changeLevel/{level}")
    public String changeLevel(@PathVariable String level) {
        LoggerContext loggerContext= (LoggerContext) LoggerFactory.getILoggerFactory();
            ch.qos.logback.classic.Logger logger=loggerContext.getLogger("root");
            logger.setLevel(Level.toLevel(level));


        return "change root log level: " + level + " success";
    }

}