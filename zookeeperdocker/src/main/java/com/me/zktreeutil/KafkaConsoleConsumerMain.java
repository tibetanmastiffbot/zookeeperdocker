package com.me.zktreeutil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZKUtil;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.me.ZookeeperServer;

/**
 *
 * When a Kafka Console consumer was used, such as the command below
 *
 * /opt/kafka_2.11-0.9.0.0/bin/kafka-console-consumer.sh --zookeeper zookeeperserver:2181 --topic
 * mytopic --property print.key=true --property key.separator=, --from-beginning
 *
 * A node will be created under Zookeeper's /consumers/ e.g. /consumers/console-consumer-21205 or
 * /consumers/console-consumer-71743
 *
 * When you CTRL+C the command above, the created node(s) will not be erased automatically
 *
 * This utility is devised to erase them to clean up.
 */
public class KafkaConsoleConsumerMain implements Watcher {

  private static final String TARGETZNODE = "/consumers";

  private static final String PATTERN_OF_DELETION = "console-consumer.+";

  private final static Logger LOG = LoggerFactory.getLogger(KafkaConsoleConsumerMain.class);

  public static void main(String[] args) throws Exception {
    Watcher watcher = new KafkaConsoleConsumerMain();
    ZooKeeper zk = new ZooKeeper(ZookeeperServer.ZKSERVER, 10000, watcher);

    LOG.info("=====================R=====================");
    Pattern r = Pattern.compile(PATTERN_OF_DELETION);
    List<String> list = zk.getChildren(TARGETZNODE, true);
    for (String string : list) {
      LOG.info(string);
      Matcher m = r.matcher(string);
      if (m.find()) {
        String nodeToDelete = String.format("%s/%s", TARGETZNODE, string);
        LOG.info("deleting... {}", nodeToDelete);
        ZKUtil.deleteRecursive(zk, nodeToDelete);
      } else {
        // Skip
      }
    }

    zk.close();
  }

  @Override
  public void process(WatchedEvent event) {
    LOG.info("Watcher received at path {}, of type {}, of state {}", event.getPath(),
        event.getType().toString(), event.getState());
  }

}
