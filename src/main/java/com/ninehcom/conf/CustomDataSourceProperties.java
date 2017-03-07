package com.ninehcom.conf;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 获取第二个数据库的连接信息，在application.properties中配置，并指定特定的前缀
 *
 * @author shenjizhe
 * @version 1.0.0
 */
@Component
@ConfigurationProperties(prefix = CustomDataSourceProperties.PREFIX)
public class CustomDataSourceProperties implements BeanClassLoaderAware {

    public static final String PREFIX = "userinfo.datasource";
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private String mappers;
    private String path;
    private String version;
    private ClassLoader classLoader;

    public CustomDataSourceProperties() {
    }

    
    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the driverClassName
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * @param driverClassName the driverClassName to set
     */
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    /**
     * @return the classLoader
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;

    }

    /**
     * @return the mappers
     */
    public String getMappers() {
        return mappers;
    }

    /**
     * @param mappers the mappers to set
     */
    public void setMappers(String mappers) {
        this.mappers = mappers;
    }

    /**
     * @return the mapperpath
     */
    public String getPath() {
        return path;
    }

    /**
     * @param mapperpath the mapperpath to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

}
