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

public class SoccerGameAdapter extends RecyclerView.Adapter<SoccerGameAdapter.SoccerGameViewHolder> implements Filterable {
    List<ArticleInfo> articleInfos;
    List<ArticleInfo> oriArticleInfos;
    Set<String> savedArticleUrls;
    // Indicate if it is on Favorite page or feed page
    private boolean isFavoriteList = false;
    // Keyword to search
    private CharSequence keyword = "";
    Context context;
    //private ArrayList<SoccerGameListFragment.SoccerGame> newsList = new ArrayList<>();\
    //private OnItemListener onItemListener;
    Filter filter = new Filter() {
        /*
         *  Perform filter according to isFavoriteList and keyword
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final List<ArticleInfo> list = oriArticleInfos;
            FilterResults results = new FilterResults();
            int count = list.size();
            final List<ArticleInfo> fList = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                ArticleInfo articleInfo = list.get(i);
                // If keyword is not null, check if title or description contains keyword
                if (constraint != null &&
                        !(articleInfo.title.toLowerCase().contains(constraint)
                        || articleInfo.description.toLowerCase().contains(constraint))) {
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

    public SoccerGameAdapter(Context ct, List<ArticleInfo> articleInfos, Set<String> savedArticleUrls) {
        context = ct;
        this.articleInfos = oriArticleInfos = articleInfos;
        this.savedArticleUrls = savedArticleUrls;
    }

    public boolean isFavoriteList() {
        return isFavoriteList;
    }

    public void setFavoriteList(boolean favoriteList) {
        this.isFavoriteList = favoriteList;
    }

    public void setKeyword(CharSequence keyword) {
        this.keyword = keyword;
    }

    /*
     *  Perform filter according to isFavoriteList and keyword
     */
    public void performFilter() {
        this.getFilter().filter(keyword);
    }

    @Override
    public SoccerGameAdapter.SoccerGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.soccer_game_item, parent, false);
        return new SoccerGameViewHolder(view);
    }

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
            ImageQuery query = new ImageQuery(holder.soccer_game_image, true);
            query.execute(articleInfo.imgUrl);
        } else {
            // If thumbnail in cache, put it in the imageView
            holder.soccer_game_image.setImageBitmap(thumbnail);
        }

    }

    @Override
    public int getItemCount() {
        return articleInfos.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class SoccerGameViewHolder extends RecyclerView.ViewHolder {
        TextView soccer_game_title, soccer_game_date, soccer_game_description;
        ImageView soccer_game_image;
        int position = -1;

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
