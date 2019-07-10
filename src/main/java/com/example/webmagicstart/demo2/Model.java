package com.example.webmagicstart.demo2;

import lombok.Data;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.example.GithubRepo;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * 2、注解方式爬取
 * 注解模式的入口是OOSpider，它继承了Spider类，提供了特殊的创建方法，其他的方法是类似的。
 *
 * TargetUrl是我们最终要抓取的URL，最终想要的数据都来自这里；
 * 而HelpUrl则是为了发现这个最终URL，我们需要访问的页面.
 * 将这里的正则进行修改：
 * 将URL中常用的字符.默认做了转义，变成了\.
 * 将"*"替换成了".*"，直接使用可表示通配符。
 * @Author 彬
 * @Date 2019/7/10
 */
@Data
@TargetUrl("https://blog.csdn.net/\\w+/\\w+/\\w+/\\w+")
@HelpUrl("https://blog.csdn.net/\\w+")
public class Model {

    //定义这个字段使用什么方式进行抽取。
    //除了XPath，我们还可以使用其他抽取方式来进行抽取，包括CSS选择器、正则表达式和JsonPath，在注解中指明type之后即可。 @ExtractBy(value = "div.BlogContent", type = ExtractBy.Type.Css)
    @ExtractBy(value = "//h1/text()", notNull = true)
    private String name;

    //从URL中进行抽取
    @ExtractByUrl("https://blog\\.csdn\\.net/(\\w+)/.*")
    private String author;

    @ExtractBy("//div[@class='blog-content-box']/tidyText()")
    private String readme;


    public static void main(String[] args) {
        OOSpider.create(Site.me().setSleepTime(1000)
                , new ConsolePageModelPipeline(), Model.class)
                .addUrl("https://blog.csdn.net/Hello_World_QWP/article/details/82459915").thread(5).run();
    }
}
