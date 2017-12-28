package com.djavid.bitcoinrate.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.model.dto.LabelItemDto;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;
import com.djavid.bitcoinrate.util.Codes;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


@NonReusable
@Layout(R.layout.recycler_item_ticker)
public class TickerItem {

    @View(R.id.tv_ticker_title)
    private TextView tv_ticker_title;
    @View(R.id.tv_price_change)
    private TextView tv_price_change;
    @View(R.id.iv_ticker_icon)
    private ImageView iv_ticker_icon;
    @View(R.id.tickerValue)
    private TextView tickerValue;
    @View(R.id.label_container)
    private PlaceHolderView label_container;
    @View(R.id.v_divider2)
    private android.view.View v_divider2;

    private Ticker tickerItem;
    private List<LabelItemDto> labels;

    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private PlaceHolderView mPlaceHolderView;
    private String tv_price;
    private int price_change_color;
    private String price_change;
    Subject<String> mObservable = PublishSubject.create();


    public TickerItem(Context mContext, PlaceHolderView mPlaceHolderView, Ticker tickerItem,
                      List<Subscribe> subscribes) {
        this.mContext = mContext;
        this.mPlaceHolderView = mPlaceHolderView;
        this.tickerItem = tickerItem;

        labels = new ArrayList<>();
        for (Subscribe item : subscribes) {
            labels.add(new LabelItemDto(item.getId(), item.getValue(),
                    item.isTrendingUp(), item.getChange_percent()));
        }
    }

    public TickerItem(Context mContext, PlaceHolderView mPlaceHolderView, Ticker tickerItem) {
        this.tickerItem = tickerItem;
        this.mContext = mContext;
        this.mPlaceHolderView = mPlaceHolderView;
        labels = new ArrayList<>();
    }

    @Resolve
    private void onResolved() {
        tv_ticker_title.setText(Codes.getCryptoCurrencyId(tickerItem.getCryptoId()));

        iv_ticker_icon.setImageResource(Codes.getCurrencyImage(tickerItem.getCryptoId()));
        tickerValue.setText(tv_price);
        tv_price_change.setTextColor(price_change_color);
        tv_price_change.setText(price_change);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        label_container.setLayoutManager(layoutManager);

        for (LabelItemDto item : getLabels()) {
            label_container.addView(new LabelItem(mContext, label_container, item, this));
        }
        label_container.addView(new LabelItem(mContext, label_container, new LabelItemDto(), this));
    }


    public Ticker getTickerItem() {
        return tickerItem;
    }
    public List<LabelItemDto> getLabels() {
        return labels;
    }


    public void addLabelItem(LabelItemDto new_item) {

        labels.add(new_item);

        label_container.removeAllViews(); //is it needed?
        for (LabelItemDto item : labels) {
            label_container.addView(new LabelItem(mContext, label_container, item, this));
        }
        label_container.addView(new LabelItem(mContext, label_container, new LabelItemDto(), this));

    }

    void deleteLabel(LabelItem labelItem) {
        labels.remove(labelItem.labelItemDto);
        label_container.removeView(labelItem);
    }

    public void setPrice(String price) {
        if (tickerItem != null)
            tv_price = price + " " + tickerItem.getCountryId();

        if (tickerValue != null) {
            tickerValue.setText(tv_price);
        }
    }

    public void setPriceChange(double change) {

        Log.i(TAG, "setPriceChange(" + change + ")");

        if (change > 0) {
            price_change = "+" + change + "%";
            price_change_color = App.getContext().getResources().getColor(R.color.colorPriceChangePos);
        } else {
            price_change = change + "%";
            price_change_color = App.getContext().getResources().getColor(R.color.colorPriceChangeNeg);
        }

        if (tv_price_change != null) {
            tv_price_change.setTextColor(price_change_color);
            tv_price_change.setText(price_change);
        }
    }

}
