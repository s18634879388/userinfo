/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realmadrid.util;

import com.realmadrid.mapper.MysqlTestTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 *
 * @author Shenjizhe
 */
@Component
public class MysqlTestTask {

    private static final Logger LOG = Logger.getLogger(MysqlTestTask.class.getName());
    @Autowired
    private MysqlTestTaskMapper config;
    @Autowired
    MysqlTestTaskRepository repository;

    @Scheduled(fixedRate = 3600000)
    public void reportCurrentTime() {
        LOG.info("DB heartbeat.");
        config.test();
        repository.count();
    }

}
