/**
 * 
 */
package com.jeesuite.monitor.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeesuite.kafka.monitor.KafkaMonitor;
import com.jeesuite.monitor.web.EnvConfigKit;
import com.jfinal.kit.PropKit;

/**
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2016年12月10日
 */
public class KafkaMonitorContext {

	private static Map<String, KafkaMonitor> kafkaMonitors = new HashMap<>();
	
	public static KafkaMonitor get(String env){
		if(kafkaMonitors.isEmpty()){
			init();
		}
		return kafkaMonitors.get(env);
	}
    private synchronized static void init(){
    	if(!kafkaMonitors.isEmpty())return;
		List<String> envs = EnvConfigKit.ENVS;
		int latThreshold = PropKit.getInt("topic.lat.threshold", 1000);
		for (String env : envs) {
			String kafkaServers = EnvConfigKit.get(env, EnvConfigKit.KAFKA_SERVER_CONFIG);
			String zkServers = EnvConfigKit.get(env, EnvConfigKit.ZK_SERVER_CONFIG);
			kafkaMonitors.put(env, new KafkaMonitor(zkServers, kafkaServers, latThreshold));
		}
    }
}
