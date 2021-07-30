package algonquin.cst2335.hhsrandroidproject1.soccergamesapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import algonquin.cst2335.hhsrandroidproject1.R;

/**
 * Adapters provide a binding from an app-specific data set to views that are displayed within a RecyclerView.
 * @author Minghui Liao
 * @version 1.0
 */
public class SoccerGameAdapter extends RecyclerView.Adapter<SoccerGameAdapter.SoccerGameViewHolder> implements Filterable {
    /**The list of articles*/
    List<ArticleInfo> articleInfos;
    /**The list of articles*/
    List<ArticleInfo> oriArticleInfos;
    /**The String set of saved article urls.*/
    Set<String> savedArticleUrls;
    /**Indicate if it is on Favorite page or feed page*/
    private boolean isFavoriteList = false;
    /**The keyword to search.*/
    private CharSequence keyword = "";
    /**The activity context.*/
    Context context;

    Filter filter = new Filter() {
        /**
         * Perform filter according to isFavoriteList and keyword.
         * @param keyword The search keyword.
         * @return Return the filter Results.
         */
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            final List<ArticleInfo> list = oriArticleInfos;
            FilterResults results = new FilterResults();
            int count = list.size();
            final List<ArticleInfo> fList = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                ArticleInfo articleInfo = list.get(i);
                // If keyword is not null, check if title or description contains keyword
                if (keyword != null &&
                        !(articleInfo.title.toLowerCase().contains(keyword)
                        || articleInfo.description.toLowerCase().contains(keyword))) {
                    continue;
                }

                if (isFavoriteList) {
                    // If on favorite page, shows only articles which are saved
                    if (savedArticleUrls.contains(articleInfo.articleUrl)) {
                        fList.add(articleInfo);
                    }
                } else {
                    // If on feed page, show all the eligible articles
                    fList.add(articleInfo);
                }

            }
            results.values = fList;
            results.count = fList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Update the articleInfos and notify the change
            articleInfos = (ArrayList<ArticleInfo>)results.values;
            notifyDataSetChanged();
        }
    };

    /**
     * This creates an adapter with specified characteristics.
     * @param ct The context of the current Activity
     * @param articleInfos The list of ArticleInfo
     * @param savedArticleUrls The set of urls of saved/favorite articles
     */
    public SoccerGameAdapter(Context ct, List<ArticleInfo> articleInfos, Set<String> savedArticleUrls) {
        context = ct;
        this.articleInfos = oriArticleInfos = articleInfos;
        this.savedArticleUrls = savedArticleUrls;
    }

    /**
     * This method is to check if current list is the favorite list.
     * @return Return true if the list is favorite list.
     */
    public boolean isFavoriteList() {
        return isFavoriteList;
    }

    /**
     * This method is to set the favourite list.
     * @param favoriteList The favourite list of articles.
     */
    public void setFavoriteList(boolean favoriteList) {
        this.isFavoriteList = favoriteList;
    }

    /**
     * This method is to set the keyword.
     * @param keyword The search keyword.
     */
    public void setKeyword(CharSequence keyword) {
        this.keyword = keyword;
    }

    /*
     *  Perform filter according to isFavoriteList and keyword
     */
    public void performFilter() {
        this.getFilter().filter(keyword);
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @Override
    public SoccerGameAdapter.SoccerGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.soccer_game_item, parent, false);
        return new SoccerGameViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(SoccerGameViewHolder holder, int position) {

        ArticleInfo articleInfo = articleInfos.get(position);
        holder.soccer_game_title.setText(articleInfo.title);
        holder.soccer_game_date.setText(articleInfo.date);
        holder.soccer_game_description.setText(articleInfo.description);

        // Try to get thumbnail from cache
        Bitmap thumbnail = ImageCache.getthumbnailFromMemCache(articleInfo.imgUrl);
        if (thumbnail == null) {
            // If thumbnail not in cache, request the image from url
            ImageQuery query = new ImageQuery(context, holder.soccer_game_image, true);
            query.execute(articleInfo.imgUrl);
        } else {
            // If thumbnail in cache, put it in the imageView
            holder.soccer_game_image.setImageBitmap(thumbnail);
        }

    }

    /**
     * This method is to get the amount of articles.
     * @return Return the amount of articles.
     */
    @Override
    public int getItemCount() {
        return articleInfos.size();
    }

    /**
     * This method is to get the filter.
     * @return Return the filter.
     */
    @Override
    public Filter getFilter() {
        return filter;
    }

    /**
     * This class represents a row that inherits from RecyclerView.ViewHolder.
     */
    public class SoccerGameViewHolder extends RecyclerView.ViewHolder {
        TextView soccer_game_title, soccer_game_date, soccer_game_description;
        ImageView soccer_game_image;
        int position = -1;

        /**
         * This creates the view of the row.
         * @param itemView The item view。
         */
        public SoccerGameViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(click -> {

                //SoccerGameAdapter.SoccerGameViewHolder newRow = adt.onCreateViewHolder(null, adt.getItemViewType(position));

                SoccerGameActivity parentActivity = (SoccerGameActivity) itemView.getContext();
                int position = getAbsoluteAdapterPosition();
                ArticleInfo articleInfo = articleInfos.get(position);
                parentActivity.userClickedMessage(articleInfo, position, savedArticleUrls.contains(articleInfo.articleUrl));

            });

            soccer_game_title = itemView.findViewById(R.id.soccer_game_title);
            soccer_game_date = itemView.findViewById(R.id.soccer_game_date);
            soccer_game_description = itemView.findViewById(R.id.soccer_game_description);
            soccer_game_image = itemView.findViewById(R.id.soccer_game_images);
            //this.onItemListener = onItemListener;

            //itemView.setOnClickListener(this);
        }
    }
}
