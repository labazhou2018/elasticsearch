package com.elastic.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Date: 2019/2/28 15:39
 **/
public class ConfigUtils {

    /**
     * 配置文件
     * */
    private static String esConfigFileName = "elasticsearch.properties";

    /**
     * es集群名
     * */
    private static String esClusterName;
    /**
     * es集群ip地址
     * */
    private static String esClusterDiscoverHostName;
    /**
     * es集群是否加入嗅探功能
     * */
    private static String clientTransportSniff;

    private static Properties properties = new Properties();

    static{
        try {
            ClassLoader classLoader = ConfigUtils.class.getClassLoader();
            InputStream resourceAsStream = classLoader.getResourceAsStream(esConfigFileName);
            properties.load(resourceAsStream);
            init();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    static void init() {

        esClusterName = properties.getProperty("elastic.cluster.name");
        esClusterDiscoverHostName = properties.getProperty("elastic.cluster.discover.hostname");
        clientTransportSniff = properties.getProperty("elastic.cluster.clientTransportSniff");

        if ("".equals(esClusterName)||"".equals(esClusterName)||"".equals(clientTransportSniff)){
            throw new RuntimeException("elasticsearch 集群参数为空异常");
        }
    }

    public static String getEsClusterName() {
        return esClusterName;
    }

    public static String getEsClusterDiscoverHostName() {
        return esClusterDiscoverHostName;
    }

    public static void setEsClusterDiscoverHostName(String esClusterDiscoverHostName) {
        ConfigUtils.esClusterDiscoverHostName = esClusterDiscoverHostName;
    }

    public static String getClientTransportSniff() {
        return clientTransportSniff;
    }

    public static void setClientTransportSniff(String clientTransportSniff) {
        ConfigUtils.clientTransportSniff = clientTransportSniff;
    }
}
