package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
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

import algonquin.cst2335.hhsrandroidproject1.MainActivity;
import algonquin.cst2335.hhsrandroidproject1.R;

/**
 * @author Minghui Liao
 * @version 1.0
 */
public class SoccerGameListFragment extends Fragment {
    final String NEWS_URL = "https://www.goal.com/en/feeds/news?fmt=rss";
    RecyclerView soccer_game_recylerView;

    List<ArticleInfo> articleInfos = new ArrayList<>();

    // HashSet to store the urls of all the saved articles
    Set<String> savedArticleUrls = new HashSet<>();
    SoccerGameAdapter adt = null;
    SQLiteDatabase db;
    Button searchbtn;
//    Button favBtn;
    EditText searchbox;

//    String title[], description[], date[];
//    int images[] = {R.drawable.bus_icon, R.drawable.charging_station, R.drawable.movie_icon, R.drawable.soccer_icon};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        // Use AsyncTask to query NEWS_URL to download all the articles
        ArticleInfosQuery query = new ArticleInfosQuery();
        query.execute(NEWS_URL);
    }


    /**
     * This function passes a layoutflater object.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listLayout = inflater.inflate(R.layout.soccer_game_list, container, false);
        soccer_game_recylerView = listLayout.findViewById(R.id.Soccer_Game_RecylerView);
        searchbtn = listLayout.findViewById(R.id.searchbtn);
        searchbox = listLayout.findViewById(R.id.searchbox);
        //favBtn = listLayout.findViewById(R.id.favBtn);

        // Find and load the toolbar
        Toolbar myToolbar = listLayout.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);

        DrawerLayout drawer = listLayout.findViewById(R.id.myDrawerLayout);
        // Create a "Hamburger button" that sits in the top left of the Toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, myToolbar, R.string.open, R.string.close);
        // Make the button and pop-out menu synchronize so that the hamburger button sho the open/close state correctly
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = listLayout.findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener((item)-> {
            onOptionsItemSelected(item);  // call the function for the other Toolbar
            drawer.closeDrawer(GravityCompat.START); // close the NavigationDrawer when clicking on the menuItems
            return false;
        });

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

//        favBtn.setOnClickListener(clk -> {
//            boolean filterFavorite = adt.isFavoriteList();
//            // Set the button text when switching lists
//            if (filterFavorite) {
//                favBtn.setText((String)getString(R.string.soccer_news_favourite));
//            } else {
//                favBtn.setText((String)getString(R.string.soccer_news_feeds));
//            }
//            // Switch between favorite and feed lists
//            adt.setFavoriteList(!filterFavorite);
//            adt.performFilter();
//        });

        SharedPreferences prefs = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String searchKeyword = prefs.getString("SearchKeyword", "");
        searchbox.setText(searchKeyword);


        // Toast message shows the searching keyword
        searchbtn.setOnClickListener(clk -> {
            Context context = getActivity().getApplicationContext();
            String keyword = searchbox.getText().toString();

            CharSequence text = "You are searching :" + keyword;
            int duration = Toast.LENGTH_LONG;

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("SearchKeyword",keyword);
            editor.apply();

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            adt.setKeyword(keyword.toLowerCase());
            adt.performFilter();
            //searchbox.setText("");
        });

        return listLayout;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * @param item The menu item that was selected. This value cannot be null.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.index:
                Intent indexIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(indexIntent);
                break;
            case R.id.id_soccer_games:
//                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentRoom);
//                SoccerGameListFragment soccerGameFragment = new SoccerGameListFragment();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentRoom, soccerGameFragment).addToBackStack(null).commit();
//                return true;
                Intent soccerGame = new Intent(getActivity(), SoccerGameActivity.class);
                startActivity(soccerGame);
                break;
            case R.id.soccer_game_fav_menu:
                boolean filterFavorite = adt.isFavoriteList();
                // Set the button text when switching lists
                if (filterFavorite) {
                    item.setIcon(R.drawable.soccer_favourite);
                    item.setTitle("FAVOURITE");
                } else {
                    item.setIcon(R.drawable.soccer);
                    item.setTitle("FEEDS");
                }
                // Switch between favorite and feed lists
                adt.setFavoriteList(!filterFavorite);
                adt.performFilter();
                break;
            case R.id.help:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.soccer_games_help)
                        .setTitle("Help")
                        .show();


        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * @param menu The options menu in which you place your items.
     * @param inflater The MenuInflater object.
     */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.soccer_game_main_activity_actions, menu);
    }

    /**
     * This method shows a alert dialog of removing articles
     * @param chosenArticle The chosen article.
     * @param chosenPosition The chosen position.
     */
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

    /**
     * This method shows a alert dialog of saving articles
     * @param chosenArticle The chosen article.
     * @param chosenPosition The chosen position.
     */
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

    /**
     * This function requests articleInfos from url.
     * @param urlString The url string.
     * @return Return the list of articles.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws SAXException Basic error or warning information from either the XML parser or the application.
     * @throws ParserConfigurationException Indicates a serious configuration error.
     */
    private List<ArticleInfo> requestArticleInfosFromUrl(String urlString)
            throws IOException, SAXException, ParserConfigurationException {

        List<ArticleInfo> articleInfos = new ArrayList<>();

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

    /**
     * This funtion gets ArticleInfo from each <item>.
     * @param node, an <item> in the list
     * @return ArticleInfo object parsed from the <item>
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
     * This class is AsyncTask to request articleInfos from the url of RSS feeds
     */
    class ArticleInfosQuery extends AsyncTask<String, Void, List<ArticleInfo>> {
        /**
         * Call requestArticleInfosFromUrl in background
         * @param urls the url of RSS feeds
         * @return the articleInfos
         */
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("We are downloading the articles")
                .setTitle("Downloading")
                .setView( new ProgressBar(getContext()))
                .show();

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
         * Runs on the UI thread after publishProgress(Progress...) is invoked.
         * This method is used to display any form of progress in the user interface while the background computation is still executing.
         * @param values The values indicating progress.
         */
        @Override
        protected void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);

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
            //Log.e("Debug:", Integer.toString(articleInfos.size()));
            if (adt != null) {
                adt = new SoccerGameAdapter(getContext(), articleInfos, savedArticleUrls);
                soccer_game_recylerView.setAdapter(adt);
                soccer_game_recylerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }

            if(dialog != null){
                dialog.dismiss();
            }

        }
    }

}
