package com.example.demo.service.impl;

import com.example.demo.dao.AccessLogRepository;
import com.example.demo.dao.PersonRepository;
import com.example.demo.entity.AccessLog;
import com.example.demo.entity.Person;
import com.example.demo.service.ExecutorService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class ExecutorServiceImpl implements ExecutorService {

    private static Logger logger = LoggerFactory.getLogger(ExecutorServiceImpl.class);

    @Value("${executor.file.path}")
    private String path;

    @Value("${executor.file.person.report.name}")
    private String personReportName;

    @Value("${executor.file.accesslog.report.name}")
    private String accessLogReportName;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Async
    @Override
    public void generateAccessLogReport() {
        List<AccessLog> accessLogs = accessLogRepository.findAll();
        String file = path + accessLogReportName;

        try {
            FileWriter fileWriter = new FileWriter(file);
            for (AccessLog accessLog : accessLogs) {
                fileWriter.write(accessLog.formatDataReport());
                fileWriter.write(StringUtils.CR);
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("generateAccessLogReport -- end");
    }

    @Async
    @Override
    public void generatePersonListReport() {
        List<Person> findAll = personRepository.findAll();
        String file = path + personReportName;

        try {
            FileWriter fileWriter = new FileWriter(file);
            for (Person person : findAll) {
                fileWriter.write(person.formatDataReport());
                fileWriter.write(StringUtils.CR);
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("generatePersonListReport -- end");
    }
}
