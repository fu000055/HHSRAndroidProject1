package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import algonquin.cst2335.hhsrandroidproject1.R;

public class SoccerGameListFragment extends Fragment {
    final String NEWS_URL = "https://www.goal.com/en/feeds/news?fmt=rss";
    RecyclerView soccer_game_recylerView;
    ArrayList<ArticleInfo> newsList = new ArrayList<>();
    List<ArticleInfo> articleInfos = new ArrayList<>();
    //List<ArticleInfo> savedArticleInfos = new ArrayList<>();
    // HashSet to store the urls of all the saved articles
    Set<String> savedArticleUrls = new HashSet<>();
    SoccerGameAdapter adt = null;
    SQLiteDatabase db;
    Button searchbtn;
    Button favBtn;
    EditText searchbox;

//    String title[], description[], date[];
//    int images[] = {R.drawable.bus_icon, R.drawable.charging_station, R.drawable.movie_icon, R.drawable.soccer_icon};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use AsyncTask to query NEWS_URL to download all the articles
        ArticleInfosQuery query = new ArticleInfosQuery();
        query.execute(NEWS_URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listLayout = inflater.inflate(R.layout.soccer_games_list, container, false);
        soccer_game_recylerView = listLayout.findViewById(R.id.Soccer_Game_RecylerView);
        searchbtn = listLayout.findViewById(R.id.searchbtn);
        searchbox = listLayout.findViewById(R.id.searchbox);
        favBtn = listLayout.findViewById(R.id.favBtn);

        //adapter
        adt = new SoccerGameAdapter(getContext(), articleInfos, savedArticleUrls);
        soccer_game_recylerView.setAdapter(adt);
        soccer_game_recylerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        MyOpenHelper opener = new MyOpenHelper(getContext());
        db = opener.getWritableDatabase();

        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);

//        int _idCol = results.getColumnIndex("_id");
        int titleCol = results.getColumnIndex( MyOpenHelper.col_title);
        int dateCol = results.getColumnIndex( MyOpenHelper.col_date);
        int imgUrlCol = results.getColumnIndex( MyOpenHelper.col_image_url);
        int urlCol = results.getColumnIndex( MyOpenHelper.col_article_url);
        int descriptionCol = results.getColumnIndex( MyOpenHelper.col_description);

//        SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());

        while(results.moveToNext()) {
//            long id = results.getInt(_idCol);
            String title = results.getString( titleCol);
            String time = results.getString( dateCol);
            String imgUrl = results.getString( imgUrlCol);
            String url = results.getString( urlCol);
            String description = results.getString( descriptionCol);
            articleInfos.add( new ArticleInfo(title, time, description, url, imgUrl) );
            savedArticleUrls.add(url);
        }

        favBtn.setOnClickListener(clk -> {
            boolean filterFavorite = adt.isFavoriteList();
            // Set the button text when switching lists
            if (filterFavorite) {
                favBtn.setText("FAVORITE");
            } else {
                favBtn.setText("FEEDS");
            }
            // Switch between favorite and feed lists
            adt.setFavoriteList(!filterFavorite);
            adt.performFilter();
        });

        // Toast message shows the searching keyword
        searchbtn.setOnClickListener(clk -> {
            Context context = getActivity().getApplicationContext();
            String keyword = searchbox.getText().toString();
            CharSequence text = "You are searching :" + keyword;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            adt.setKeyword(keyword.toLowerCase());
            adt.performFilter();
            searchbox.setText("");
        });

        return listLayout;
    }

    public void notifyNewsRemoved(ArticleInfo chosenArticle, int chosenPosition) {
        // AlertDialog displays alert message to confirm if the article will be remove
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("are you sure you want to remove the news from favorite: " + chosenArticle.title)
                .setTitle("Remove")
                .setNegativeButton("Cancel", (dialog, cl) -> {
                })
                .setPositiveButton("Remove", (dialog, cl) -> {

                    //position = getAbsoluteAdapterPosition();

                    ArticleInfo savedArticle = articleInfos.get(chosenPosition);

                    // Delete the saved articleInfos from database
                    db.delete(MyOpenHelper.TABLE_NAME, "ArticleUrl=?", new String[]{savedArticle.articleUrl});
                    // Remove the saved articleInfos from HashSet
                    savedArticleUrls.remove(chosenArticle.articleUrl);

                    //Toast shows "the article has been removed" message
                    Context context = getActivity().getApplicationContext();
                    String articleTitle = chosenArticle.title;
                    CharSequence text = articleTitle + " has been removed";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    // Update the list
                    adt.performFilter();
                })
                .create().show();
    }

    public void notifyNewsSaved(ArticleInfo chosenArticle, int chosenPosition) {
        // AlertDialog displays message if you want to save this articles
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("are you sure you want to save the news to favorite: " + chosenArticle.title)
                .setTitle("Save")
                .setNegativeButton("Cancel", (dialog, cl) -> {
                })
                .setPositiveButton("Save", (dialog, cl) -> {

                    //position = getAbsoluteAdapterPosition();

                    ArticleInfo savedArticle = articleInfos.get(chosenPosition);

                    // Add the saved articleInfos from HashSet
                    savedArticleUrls.add(savedArticle.articleUrl);
                    // Update the list
                    adt.performFilter();

                    // Replace the single quote marks of title and description
                    String escapedTitle = savedArticle.title.replace("'", "''");
                    String escapedDescription = savedArticle.description.replace("'", "''");

                    // Insert the saved articleInfos from database
                    db.execSQL("Insert into " + MyOpenHelper.TABLE_NAME + " values('" +
                            escapedTitle + "','" + savedArticle.date + "','" +
                            escapedDescription + "','" + savedArticle.articleUrl + "','" +
                            savedArticle.imgUrl + "');");


                    // Snackbar to ask users that whether users want to undo the action
                    Snackbar.make(searchbtn, "You saved article " + chosenArticle.title, Snackbar.LENGTH_LONG)
                            .setAction("Undo", clk -> {

                                savedArticleUrls.remove(savedArticle.articleUrl);
                                adt.performFilter();
                                db.delete(MyOpenHelper.TABLE_NAME, "title=?", new String[]{savedArticle.title});
                            })
                            .show();
                })
                .create().show();
    }

    /*
     * Request articleInfos from url
     */
    private List<ArticleInfo> requestArticleInfosFromUrl(String urlString)
            throws IOException, SAXException, ParserConfigurationException {

        List<ArticleInfo> articleInfos = new ArrayList<>();

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setReadTimeout(10000);
//        conn.setConnectTimeout(15000);
//        conn.setRequestMethod("GET");
//        conn.setDoInput(true);
        conn.connect();
        if (conn.getResponseCode() != 200) {
            // If failing to connect, return empty articleInfos
            Log.e("HttpConnection:", "Fail to connect");
            return articleInfos;
        }

        // Create DocumentBuilderFactory to obtain a parser that produces DOM object trees from XML documents
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Get Inputstream from connection
        InputStream is = conn.getInputStream();
        // Parse Inputstream to XML document
        Document doc = builder.parse(is);

        // Get articles list from xml
        NodeList nList = doc.getElementsByTagName("item");

        for (int i = 0; i < nList.getLength(); i++) {
            // Parse each <item> to articleInfo and add into ArrayList
            articleInfos.add(getArticleInfo(nList.item(i)));
        }

        return articleInfos;
    }

    /*
     *  Get ArticleInfo from each <item>
     */
    private ArticleInfo getArticleInfo(Node node) {
        Element element = (Element)node;
        // Extract the information of article
        String title = element.getElementsByTagName("title").item(0).getTextContent();
        String date = element.getElementsByTagName("pubDate").item(0).getTextContent();
        String description = element.getElementsByTagName("description").item(0).getTextContent();
        String articleUrl = element.getElementsByTagName("link").item(0).getTextContent();
        // Extract image url
        NodeList mediaContent = element.getElementsByTagName("media:content");
        Element imageElement = (Element)mediaContent.item(0);
        String imgUrl = imageElement.getAttribute("url");
        if ("http:".equals(imgUrl.substring(0,5))) {
            imgUrl = "https:" + imgUrl.substring(5);
        }

        return new ArticleInfo(title, date, description, articleUrl, imgUrl);
    }

    /**
     * AsyncTask to request articleInfos from the url of RSS feeds
     */
    class ArticleInfosQuery extends AsyncTask<String, Void, List<ArticleInfo>> {
        /**
         * Call requestArticleInfosFromUrl in background
         * @param urls the url of RSS feeds
         * @return the articleInfos
         */

        @Override
        protected List<ArticleInfo> doInBackground(String... urls) {
            List<ArticleInfo> articleInfos = null;
            try {
                articleInfos = requestArticleInfosFromUrl(urls[0]);
                return articleInfos;
            } catch (IOException e) {
                Log.e("IOException:", e.toString());
            } catch (SAXException e) {
                Log.e("SAXException:", e.toString());
            } catch (ParserConfigurationException e) {
                Log.e("ParserConfException:", e.toString());
            } catch (Exception e) {
                Log.e("Exception:", e.toString());
            } finally {
                return articleInfos;
            }
        }

        /**
         * After requesting articleInfos as result, update the existing articleInfos in main thread
         * @param result articleInfos returned from doInBackground
         *
         */
        @Override
        protected void onPostExecute(List<ArticleInfo> result) {
            // Append the existing articleInfos after new downloaded articleInfos
            result.addAll(articleInfos);
            articleInfos = result;
            Log.e("Debug:", Integer.toString(articleInfos.size()));
            if (adt != null) {
                adt = new SoccerGameAdapter(getContext(), articleInfos, savedArticleUrls);
                soccer_game_recylerView.setAdapter(adt);
                soccer_game_recylerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }

        }
    }

//    private class MyRowViews extends RecyclerView.ViewHolder {
//        TextView soccer_game_title, soccer_game_date, soccer_game_description;
//        ImageView soccer_game_image;
//        int position = -1;
//
//        public MyRowViews(View itemView) {
//            super(itemView);
//
//            itemView.setOnClickListener( click -> {
//
//                SoccerGameAdapter.SoccerGameViewHolder newRow = adt.onCreateViewHolder(null, adt.getItemViewType(position));
//
//                SoccerGameActivity parentActivity = (SoccerGameActivity) getContext();
//                int position = getAbsoluteAdapterPosition();
//                parentActivity.userClickedMessage( newsList.get(position), position );
//
//            });
//
//            soccer_game_title = itemView.findViewById(R.id.soccer_game_title);
//            soccer_game_date = itemView.findViewById(R.id.soccer_game_date);
//            soccer_game_description = itemView.findViewById(R.id.soccer_game_description);
//            soccer_game_image = itemView.findViewById(R.id.soccer_game_images);
//        }
//
//        public void setPosition(int p) { position = p; }
//    }
//
//    private class MyChatAdapter extends RecyclerView.Adapter<MyRowViews>{
//        String data1[], data2[], data3[];
//        int images[];
//        Context context;
//
//        public MyChatAdapter(Context ct, String title[], String date[], String description[], int img[]) {
//            context = ct;
//            data1 = title;
//            data2 = date;
//            data3 = description;
//            images = img;
//        }
//
//        @Override
//        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            View view = inflater.inflate(R.layout.soccer_game_item, parent, false);
//            return new MyRowViews(view);
//        }
//
//        @Override
//        public void onBindViewHolder(MyRowViews holder, int position) {
//            holder.soccer_game_title.setText(data1[position]);
//            holder.soccer_game_date.setText(data2[position]);
//            holder.soccer_game_description.setText(data3[position]);
//            holder.soccer_game_image.setImageResource(images[position]);
//            holder.setPosition(position);
//        }
//
//        @Override
//        public int getItemCount() {
//            return images.length;
//        }
//
//    }

//    class SoccerGame {
//        long id;
//        String news;
//        Date date;
//        String url;
//        int image;
//        String description;
//
//        public SoccerGame(String news, Date date, String url, int image, String description) {
//            this.news = news;
//            this.date = date;
//            this.url = url;
//            this.image = image;
//            this.description = description;
//        }
//
//        public SoccerGame(String news, Date date) {
//            this.news = news;
//            this.date = date;
//        }
//
//
//        public void setId(long l) {
//            id = l;
//        }
//
//        public long getId() {
//            return id;
//        }
//
//
//        public void setNews(String news) {
//            this.news = news;
//        }
//
//        public String getNews() {
//            return news;
//        }
//
//        public Date getDate() {
//            return date;
//        }
//
//        public void setDate(Date date) {
//            this.date = date;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            url = url;
//        }
//
//        public int getImage() {
//            return image;
//        }
//
//        public void setImage(int image) {
//            this.image = image;
//        }
//
//
//        public String getDescription() {
//            return description;
//        }
//
//        public void setDescription(String description) {
//            this.description = description;
//        }
//
//    }
}
