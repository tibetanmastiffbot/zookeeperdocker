package com.me.zktreeutil;

import com.me.ZookeeperServer;

public class TreeDumpMain {

	public static void main(String[] args) throws InterruptedException {
		String znode = "";
		new zkExportToFS(ZookeeperServer.ZKSERVER, znode, "tests/output").go();
		new zkExportToXmlFile(ZookeeperServer.ZKSERVER, znode, "tests/output/output.xml").go();
	}

}
