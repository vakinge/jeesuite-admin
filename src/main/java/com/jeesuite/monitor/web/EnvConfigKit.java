/**
 * 
 */
package com.jeesuite.monitor.web;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.jeesuite.monitor.constant.EnvType;

/**
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2016年12月9日
 */
public class EnvConfigKit {
	
	public static final String KAFKA_SERVER_CONFIG = "kafka.servers";
	public static final String ZK_SERVER_CONFIG = "zookeeper.servers";
	public static final String SCH_REGISTRY_CONFIG = "scheduler.registry";
	public static final String SCH_REGISTRY_SERVER_CONFIG = "scheduler.registry.servers";
	
	public static final List<String> ENVS = new ArrayList<>();

	private static Map<String, Map<String, String>> configs = new HashMap<>();
	
	public synchronized static void load(String configFile) {
		try {
            if(!configs.isEmpty())return;
			File file = new File(Thread.currentThread().getContextClassLoader().getResource(configFile).getPath());
			Properties p = new Properties();
			
			String envs = p.getProperty("env.options");
			if(envs != null){
				String[] strings = envs.split(",");
				for (String string : strings) {
					ENVS.add(string);
				}
			}else{
				EnvType[] envTypes = EnvType.values();
				for (EnvType envType : envTypes) {
					ENVS.add(envType.name());
				}
			}
			p.load(new FileReader(file));
			Set<Entry<Object, Object>> entrySet = p.entrySet();
			for (Entry<Object, Object> entry : entrySet) {
				if(entry.getKey().toString().indexOf(".")<= 0)continue;
				String env = entry.getKey().toString().substring(0,entry.getKey().toString().indexOf("."));
				if(!ENVS.contains(env))continue;
				Map<String, String> map = configs.get(env);
				if(map == null){
					configs.put(env, map = new HashMap<>());
				}
				String key = entry.getKey().toString().substring(entry.getKey().toString().indexOf(".") + 1);
				map.put(key, entry.getValue().toString());
			}
    
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String get(String env,String key){
		if(configs.containsKey(env)){
			return configs.get(env).get(key);
		}
		return null;
	}
	
	public static void main(String[] args) {
		String s = "dev.kafka.servers";
		System.out.println(s.substring(0,s.indexOf(".")));
		System.out.println(s.substring(s.indexOf(".") + 1));
	}
}
