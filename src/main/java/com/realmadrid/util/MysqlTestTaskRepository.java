/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realmadrid.util;

import com.realmadrid.entity.Editconfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Administrator
 */
public interface MysqlTestTaskRepository extends JpaRepository<Editconfig, String>{
    
}
