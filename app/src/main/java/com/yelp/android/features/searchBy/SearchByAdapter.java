package com.yelp.android.features.searchBy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yelp.android.R;
import com.yelp.android.data.model.response.SearchedItem;
import com.yelp.android.features.detailsPage.DetailsActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class SearchByAdapter extends RecyclerView.Adapter<SearchByAdapter.SearchByViewHolder> {

    private List<SearchedItem> mList;
    Context context;
    private Subject<String> expenseItemClick;
    private List<SearchedItem> searchedList = new ArrayList<>();
    private String completeLocation;
    private String itemLocation;

    @Inject
    SearchByAdapter() {
        mList = new ArrayList<>();
        expenseItemClick = PublishSubject.create();
    }

    @Inject
    SearchByPresenter expenseApprovalPresenter;

    public void setExpenses(List<SearchedItem> expenseItems){
        this.mList = expenseItems;
        notifyDataSetChanged();
    }


    @Override
    public SearchByViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_searched, parent, false);
        context  = parent.getContext();
        return new SearchByViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchByViewHolder holder, int position) {
        SearchedItem searchedItem = this.mList.get(position);
        holder.onBind(searchedItem, position);
        holder.item_holder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                ((SearchByActivity)context).hideSearchLocationEditText();
                String address1, city, state, zipcode, review;
                address1 = searchedItem.location.address1;
                city = searchedItem.location.city;
                state = searchedItem.location.state;
                zipcode = searchedItem.location.zip_code;
                itemLocation = address1 + ", " + city + ", " + state + " " + zipcode;

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("name", searchedItem.name);
                intent.putExtra("categories", searchedItem.categories.get(0).title);
                intent.putExtra("location", itemLocation);
                intent.putExtra("rating", String.valueOf(searchedItem.rating));
                intent.putExtra("review_count", String.valueOf(searchedItem.review_count));
                intent.putExtra("display_phone", searchedItem.display_phone);
                intent.putExtra("id", searchedItem.id);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public List<SearchedItem> getSearchByResponseList(){
        return searchedList;
    }


    Observable<String> getExpenseClick() {

        return expenseItemClick;
    }


    public class SearchByViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.business_name) public TextView businessName;
        @BindView(R.id.category) public TextView category;
        @BindView(R.id.location) public TextView location;
        @BindView(R.id.rating_bar) public RatingBar ratingBar;
        @BindView(R.id.review_count) public TextView reviewCount;
        @BindView(R.id.item_holder) public LinearLayout item_holder;

        private SearchedItem searchedItem;

        SearchByViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v ->
            {


            });

        }

        void onBind(SearchedItem searchedItem, int position) {
            this.searchedItem = searchedItem;
            businessName.setText(searchedItem.name);
            category.setText(searchedItem.categories.get(0).title);
            String address1, city, state, zipcode, review;
            address1 = searchedItem.location.address1;
            city = searchedItem.location.city;
            state = searchedItem.location.state;
            zipcode = searchedItem.location.zip_code;
            completeLocation = address1 + ", " + city + ", " + state + " " + zipcode;
            location.setText(completeLocation);
            ratingBar.setRating((float) searchedItem.rating);
            review = String.valueOf(searchedItem.review_count);
            reviewCount.setText(review + " Reviews");


        }
    }

    public void clearData() {
        this.mList.clear();
        this.notifyDataSetChanged();
    }

  


}
