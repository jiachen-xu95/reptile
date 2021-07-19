package com.xjc.reptile.util;

import com.alibaba.fastjson.JSON;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author jiachenxu
 * @Date 2021/7/19
 * @Descripetion
 */
@Slf4j
public class ReptileUtil {

    /**
     * 请求网页
     *
     * @param url
     * @return
     */
    public static Document request(final String url) {
        HtmlPage htmlPage = null;
        String result = StringUtils.EMPTY;
        try {
            WebClient client = new WebClient();
            client.getOptions().setJavaScriptEnabled(true);
            client.getOptions().setCssEnabled(false);
            client.getOptions().setActiveXNative(false);
            client.getOptions().setThrowExceptionOnScriptError(false);
            client.getOptions().setThrowExceptionOnFailingStatusCode(false);
            client.getOptions().setTimeout(8000);
            client.waitForBackgroundJavaScript(2000);
            htmlPage = client.getPage(url);
            result = htmlPage.asXml();
            if (StringUtils.isBlank(result)) {
                log.error("ReptileUtil request ");
                return null;
            }
        } catch (Exception e) {
            log.error("ReptileUtil request error, e={0}", e);
        }

        return Jsoup.parse(result);
    }

    public static void main(String[] args) {
        String url = "http://search.ccgp.gov.cn/bxsearch?searchtype=1&page_index=1&bidSort=0&buyerName=&projectId=&pinMu=0&bidType=1&dbselect=bidx&kw=%E5%86%9C%E4%B8%9A&start_time=2021%3A07%3A12&end_time=2021%3A07%3A19&timeType=2&displayZone=&zoneId=&pppStatus=0&agentName=";
        Document document = ReptileUtil.request(url);
        System.out.println(JSON.toJSONString(document));
        if (Objects.nonNull(document)) {
            Elements elementsByClass = document.getElementsByClass("vT-srch-result-list");
            Element element = elementsByClass.get(0);
            System.out.println(element);
            Elements ul = element.getElementsByTag("ul");
            for (Element elementUl : ul) {
                Elements provinceEl = elementUl.getElementsByTag("li");
                for (Element elementLi : provinceEl) {
                    System.out.println(elementLi);
                    Element a = elementLi.select("a").first();
                    String href = a != null ? a.attr("href") : StringUtils.EMPTY;
                    System.out.println("href:" + href);
                    String text = a != null ? a.text() : StringUtils.EMPTY;
                    System.out.println("title:"+ text);
                    Elements span = elementLi.select("span");
                    System.out.println(span);
                }
            }
        }


    }


}
