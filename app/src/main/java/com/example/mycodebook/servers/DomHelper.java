package com.example.mycodebook.servers;

import android.content.Context;
import android.util.Xml;

import com.example.mycodebook.pojo.DataItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * create by wyq on 2020/12/29
 */

public class DomHelper {
    File file;
    Context mContext;

    public boolean setFile(String fileName,String filePath,Context context) throws IOException {
        mContext=context;


        file=new File(filePath+"/codeBook",fileName);
        System.out.println(file.getPath());

        if(!file.exists()){
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdir();
            }

            if(file.createNewFile()){
                return file.exists();
            }
        }
        return true;

    }
    public  ArrayList<DataItem> queryXML()
    {
        ArrayList<DataItem> dataItems = new ArrayList<DataItem>();
        try {
            //①获得DOM解析器的工厂示例:
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            //②从Dom工厂中获得dom解析器
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            //③把要解析的xml文件读入Dom解析器
            Document doc = dbBuilder.parse(new FileInputStream(file));

            Node root=doc.getDocumentElement();


            //④获取所有dataItem节点的列表
            NodeList nList = root.getChildNodes();
            //⑤遍历该集合,显示集合中的元素以及子元素的名字
            for(int i = 0;i < nList.getLength();i++)
            {
                //先从Person元素开始解析
                Element personElement = (Element) nList.item(i);

                DataItem p = new DataItem();
                p.setId(Integer.valueOf(personElement.getAttribute("id")));

                //
                NodeList childNoList = personElement.getChildNodes();
                for(int j = 0;j < childNoList.getLength();j++)
                {
                    Node childNode = childNoList.item(j);
                    //判断子note类型是否为元素Note
                    if(childNode.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element childElement = (Element) childNode;
                        if("num".equals(childElement.getNodeName()))
                            p.setNum(childElement.getFirstChild().getNodeValue());
                        else if("pwd".equals(childElement.getNodeName()))
                            p.setPwd(childElement.getFirstChild().getNodeValue());
                        else if("info".equals(childElement.getNodeName())){
                            p.setInfo(childElement.getFirstChild().getNodeValue());
                        }
                    }
                }
                System.out.println(p);
                dataItems.add(p);
            }
        } catch (Exception e) {e.printStackTrace();}
        return dataItems;
    }

    public boolean writeXML(ArrayList<DataItem> dataItems) {
        try {
            // 如何创建xml文件，然后把这20条数据，写入xml文件里面去，一定是去看Android提供的API
            XmlSerializer serializer = Xml.newSerializer();
            // 指定流目录
            OutputStream os = new FileOutputStream(file);
            // 设置指定目录
            serializer.setOutput(os, "UTF-8");

            // 开始写入Xml格式数据
            // 开始文档
            // 参数一：指定编码格式   参数二：是不是独立的xml(这个xml与其他xml是否有关联)
            serializer.startDocument("UTF-8", true);

            // 开始根标签
            // 参数一：命名空间   参数二：标签名称
            serializer.startTag(null, "dataItems");

            for (DataItem dataItem : dataItems) {
                // 开始子标签
                serializer.startTag(null, "dataItem");

                // 设置属性
                serializer.attribute(null, "id", String.valueOf(dataItem.getId()));

                // 设置num
                // 开始num标签
                serializer.startTag(null, "num");
                // 写入值
                serializer.text(dataItem.getNum());
                // 结束num标签
                serializer.endTag(null, "num");

                // 设置info
                // 开始info标签
                serializer.startTag(null, "info");
                // 写入info值
                serializer.text(dataItem.getInfo());
                // 结束info标签
                serializer.endTag(null, "info");


                serializer.startTag(null,"pwd");
                serializer.text(dataItem.getPwd());
                serializer.endTag(null,"pwd");
                // 结束子标签
                serializer.endTag(null, "dataItem");
            }

            // 结束根标签
            serializer.endTag(null, "dataItems");

            // 结束文档
            serializer.endDocument();
        }catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }




}