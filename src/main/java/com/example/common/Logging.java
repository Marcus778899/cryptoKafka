package com.example.common;

import com.example.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.logging.*;
import java.text.SimpleDateFormat;

public class Logging {
    private static final Logger logger = Logger.getLogger(Logging.class.getName());
    private static final String processGuid;
    private static final String logDir;
    private static final String dailyDir;

    static{
        processGuid = UUID.randomUUID().toString().split("-")[0];
        logDir = Config.WORKDIR + File.separator + "logs";
        File logPath = new File(logDir);
        if(!logPath.exists()){
            logPath.mkdir();
        }

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        dailyDir = logDir + File.separator + "logs_" + today;
        File dailyPath = new File(dailyDir);

        if(!dailyPath.exists()){
            dailyPath.mkdir();
        }
        // instance
        initLogger();
    }

    public static void initLogger(){
        try{
            // remove default handler
            logger.setUseParentHandlers(false);
            for( Handler handler : logger.getHandlers() ){
                logger.removeHandler(handler);
            }

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new CustomFormatter());
            logger.addHandler(consoleHandler);

            addFileHandler(Level.INFO, "info.log");
            addFileHandler(Level.WARNING, "warning.log");
            addFileHandler(Level.SEVERE, "error.log");

            logger.setLevel(Level.ALL);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private static void addFileHandler(Level level, String filename) throws IOException{
        FileHandler filehandler = new FileHandler(
            dailyDir + File.separator + filename, 
            true
            );
        filehandler.setLevel(level);
        filehandler.setFormatter(new CustomFormatter());

        filehandler.setFilter(record -> record.getLevel().equals(level));
        logger.addHandler(filehandler);
    }

    private static class CustomFormatter extends Formatter{
        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public String format(LogRecord record){
            String time = sdf.format(new Date(record.getMillis()));
            String level = record.getLevel().getName();
            String message = formatMessage(record);

            return String.format(
                "(%s)=> [%s][%s] %s%n",
                time,
                processGuid,
                level,
                message);
        }
    }

    // Encapsulation
    public static void info_message(String msg) { logger.log(Level.INFO, msg); }
    public static void warn_message(String msg) { logger.log(Level.WARNING, msg); }
    public static void error_message(String msg) { logger.log(Level.SEVERE, msg); }

}
