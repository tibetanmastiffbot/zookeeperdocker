
package com.me.zktreeutil;

import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class zkExportToXmlFile implements Job {
    private String zkServer;
    private String output_file;
    private String start_znode;
    private TreeNode<zNode> zktree;
    private final org.slf4j.Logger logger;


    public zkExportToXmlFile(String zkServer, String znode, String output_file) {
        logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
        this.zkServer = zkServer;
        this.output_file = output_file;
        this.start_znode = znode;
    }

    public void go() {
        zkDumpZookeeper dump = new zkDumpZookeeper(zkServer, start_znode);
        try {
            zktree = dump.getZktree();
            writeFile();
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }

    private void writeFile() {
        logger.info("begin write zookeeper tree to file " + output_file);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element root = doc.createElement("root");
            root.setAttribute("path", start_znode);
            doc.appendChild(root);

            fillXml(doc, root, zktree);

            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(output_file)));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.info("end write zookeeper tree to file " + output_file);
    }

    private void fillXml(Document doc, Node root, TreeNode<zNode> zktree) {
        for (TreeNode<zNode> znode : zktree.children) {
            Element node = doc.createElement("zknode");
            node.setAttribute("name", znode.data.name);
            if (znode.data.data != null && znode.data.data.length > 0) {
                String str = new String(znode.data.data);
                if (!str.equals("null")) {
                    node.setAttribute("value", str);
                }
            }
            if (znode.data.stat.getEphemeralOwner() != 0) {
                node.setAttribute("ephemeral", "true");
            }

            root.appendChild(node);
            fillXml(doc, node, znode);
        }
    }
}
