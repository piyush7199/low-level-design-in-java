package org.lld.practice.design_logger_system.improved_solution.appenders;

import org.lld.practice.design_logger_system.improved_solution.formatters.LogFormatter;
import org.lld.practice.design_logger_system.improved_solution.formatters.PlainTextFormatter;
import org.lld.practice.design_logger_system.improved_solution.models.LogLevel;
import org.lld.practice.design_logger_system.improved_solution.models.LogMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Appender that writes logs to a file.
 */
public class FileAppender implements LogAppender {
    private final LogFormatter formatter;
    private LogLevel minimumLevel;
    private PrintWriter writer;
    
    public FileAppender(String filePath, LogLevel minimumLevel) {
        this(filePath, minimumLevel, new PlainTextFormatter());
    }
    
    public FileAppender(String filePath, LogLevel minimumLevel, LogFormatter formatter) {
        this.minimumLevel = minimumLevel;
        this.formatter = formatter;
        try {
            this.writer = new PrintWriter(new FileWriter(filePath, true));
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize file appender: " + filePath, e);
        }
    }
    
    @Override
    public synchronized void append(LogMessage logMessage) {
        if (!logMessage.getLevel().shouldLog(minimumLevel)) {
            return;
        }
        
        if (writer == null) {
            return;
        }
        
        String formattedMessage = formatter.format(logMessage);
        writer.println(formattedMessage);
        writer.flush(); // Ensure immediate write
    }
    
    @Override
    public LogLevel getMinimumLevel() {
        return minimumLevel;
    }
    
    @Override
    public void setMinimumLevel(LogLevel level) {
        this.minimumLevel = level;
    }
    
    @Override
    public void close() {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }
}

