package com.me.zktreeutil;

import java.util.Random;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.me.ZookeeperServer;

public class ZCrudMain implements Watcher {

  private static final String TARGETZNODE = "/ZCrudMainDemo";

  private final static Logger LOG = LoggerFactory.getLogger(ZCrudMain.class);

  private ZooKeeper zk;

  private Random rand = new Random();

  public static void main(String[] args) throws Exception {
    new ZCrudMain().crud();
  }

  private void crud() throws Exception {
    zk = new ZooKeeper(ZookeeperServer.ZKSERVER, 10000, this);

    LOG.info("=====================C=====================");
    String dataToInitWith = Long.toHexString(Math.abs(rand.nextLong()));
    LOG.info("Data to initialize with is {}", dataToInitWith);
    zk.create(TARGETZNODE, dataToInitWith.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

    LOG.info("=====================R=====================");
    String readFromZK = new String(zk.getData(TARGETZNODE, true, zk.exists(TARGETZNODE, true)));
    LOG.info("String read from zk is {}", readFromZK);
    Assert.assertEquals(dataToInitWith, readFromZK);

    LOG.info("=====================U=====================");
    String dataToWrite = Long.toHexString(Math.abs(rand.nextLong()));
    LOG.info("Data to write is {}", dataToWrite);
    Stat stat = zk.exists(TARGETZNODE, true);
    zk.setData(TARGETZNODE, dataToWrite.getBytes(), stat.getVersion());

    LOG.info("=====================R=====================");
    readFromZK = new String(zk.getData(TARGETZNODE, true, zk.exists(TARGETZNODE, true)));
    LOG.info("String read from zk is {}", readFromZK);
    Assert.assertEquals(dataToWrite, readFromZK);

    LOG.info("=====================D=====================");
    stat = zk.exists(TARGETZNODE, true);
    zk.delete(TARGETZNODE, stat.getVersion());

    zk.close();
  }

  @Override
  public void process(WatchedEvent event) {
    LOG.info("Watcher received at path {}, of type {}, of state {}", event.getPath(),
        event.getType().toString(), event.getState());
  }
}
