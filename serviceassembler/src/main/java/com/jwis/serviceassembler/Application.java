package com.jwis.serviceassembler;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.jwis.service.curater.ZookeeperServiceAutoRegist;
import com.jwis.service.mytask.model.AssemblerConfig;

/**
 * serviceassembler application main class
 */
@Configuration
@ComponentScan(basePackages={"com.jwis"})
@EnableConfigurationProperties({AssemblerConfig.class})
@SpringBootApplication
public class Application 
{
	private static Logger logger = Logger.getLogger(Application.class);
    public static void main( String[] args )
    {
    	SpringApplication.run(Application.class, args);
    	String rootPath = Application.class.getClass().getResource("/").getFile().toString();
    	String newPath = rootPath + "service.json";
    	File file = new File(newPath);
    	if(file.exists()){
    		logger.info("Loading service.json for regist microservice to Zookeeper.");
    		ZookeeperServiceAutoRegist regist = new ZookeeperServiceAutoRegist();
        	regist.registServices(file);
    	}else
    		logger.error("service.json does not exist");
    }
}
