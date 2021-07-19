package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import java.util.Date;

public class ArticleInfo {
    String title;
    String date;
    String description;
    String articleUrl;
    String imgUrl;

    public ArticleInfo(String title, String date, String description, String articleUrl, String imgUrl) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.articleUrl = articleUrl;
        this.imgUrl = imgUrl;
    }
}
