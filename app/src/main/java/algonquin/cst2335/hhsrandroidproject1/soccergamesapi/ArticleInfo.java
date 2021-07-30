package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import java.util.Date;

/**
 * This class is the article information.
 * @author Minghui Liao
 * @version 1.0
 */
public class ArticleInfo {
    /** This is the title of the a soccer news*/
    String title;
    /** This is the published date of the a soccer news*/
    String date;
    /** This is the description of the a soccer news*/
    String description;
    /** This is the article url of the a soccer news that you can load in the browser*/
    String articleUrl;
    /** This is the image url of the a soccer news*/
    String imgUrl;

    /**
     * Create an article information which contains titles, date, description, article url and image url.
     * @param title String object that is the title of the article.
     * @param date String object that is the publicshed date of the article.
     * @param description String object that is the description of the article.
     * @param articleUrl String object that is the article url of the article.
     * @param imgUrl String object that is the image url of the article.
     */
    public ArticleInfo(String title, String date, String description, String articleUrl, String imgUrl) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.articleUrl = articleUrl;
        this.imgUrl = imgUrl;
    }
}
