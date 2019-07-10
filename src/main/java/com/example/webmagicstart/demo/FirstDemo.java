package com.example.webmagicstart.demo;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * webmagic 第一个demo实现
 * @Author 彬
 * @Date 2019/7/10
 */
public class FirstDemo implements PageProcessor{

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    /**
     * process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
     * @param page
     */
    @Override
    public void process(Page page) {
        // 部分三：从页面发现后续的url地址来抓取 http://webmagic.io/
        //获取所有匹配正则的链接，并将这些链接放到待抓取队列中
        page.addTargetRequests(page.getHtml().links().regex("(http://webmagic.\\.io/[\\w\\-]+/[\\w\\-]+)").all());
        page.addTargetRequests(page.getHtml().links().regex("(http://webmagic.\\.io//[\\w\\-])").all());
        // 部分二：定义如何抽取页面信息，并保存下来
        page.putField("author", page.getUrl().regex("http://webmagic.\\.io//(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//h2[@id='特性']/text()").toString());
        if (page.getResultItems().get("name")==null){
            //skip this page 设置skip之后，这个页面的结果不会被Pipeline处理
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div"));
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        //开启5个线程爬取----目前0.7.3版本bug：目前webmagic最新版是0.7.3版本，在爬取只支持TLS1.2的https站点会报错（SSLException）
        // 解决方案：（1）下载源码，本地打包，替换掉本地依赖---本地maven 不能是私有镜像
        //（2）这里暂时将要爬取的页面地址换一下--并将抽取逻辑改造
        //追加方法：addPipeline(new JsonFilePipeline("D:\\webmagic\\")) 保存爬取内容 也是pipeline爬取数据展现的一种方法
        Spider.create(new FirstDemo()).addUrl("http://webmagic.io/").addPipeline(new ConsolePipeline()).thread(5).run();
    }
}
