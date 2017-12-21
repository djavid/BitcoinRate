package com.djavid.bitcoinrate.view.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.annimon.stream.IntStream;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BaseFragment;
import com.djavid.bitcoinrate.core.Router;
import com.djavid.bitcoinrate.presenter.instancestate.RateFragmentInstanceState;
import com.djavid.bitcoinrate.presenter.interfaces.RateFragmentPresenter;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.util.RateChart;
import com.djavid.bitcoinrate.view.adapter.CurrenciesAdapter;
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import butterknife.BindView;

import static com.djavid.bitcoinrate.util.Codes.country_coins;
import static com.djavid.bitcoinrate.util.Codes.crypto_coins_array;


public class RateFragment extends BaseFragment implements RateFragmentView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.topPanel)
    TextView topPanel;
    @BindView(R.id.leftPanel)
    Spinner leftPanel;
    @BindView(R.id.rightPanel)
    Spinner rightPanel;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipe_container;
    @BindView(R.id.optionFirst)
    TextView optionFirst;
    @BindView(R.id.optionSecond)
    TextView optionSecond;
    @BindView(R.id.optionThird)
    TextView optionThird;
    @BindView(R.id.optionFourth)
    TextView optionFourth;

    OnFragmentInteractionListener mListener;
    RateFragmentPresenter presenter;
    RateChart chart;
    Codes.ChartOption selectedChartOption;


    public RateFragment() { }
    public static RateFragment newInstance() {
        System.out.println("NEW INSTANCE");
        return new RateFragment();
    }


    @Override
    public void loadData() {

    }

    @Override
    public View setupView(View view) {

        swipe_container.setOnRefreshListener(this);
        swipe_container.setColorSchemeColors(
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorChart),
                getResources().getColor(R.color.colorLabelBackground));

        view.findViewById(R.id.optionFirst).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionSecond).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionThird).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionFourth).setOnClickListener(onChartOptionClick);

        chart = new RateChart(view);
        chart.initialize();

        setCurrenciesSpinner();

        getSelectedChartOption();
        setChartLabelSelected(getSelectedChartLabelView());

        return view;
    }

    @Override
    public void setCurrenciesSpinner() {

        ArrayAdapter<String> adapterLeft = new CurrenciesAdapter(getActivity(), R.layout.row,
                crypto_coins_array, getActivity().getLayoutInflater(), R.layout.row_item);
        leftPanel.setAdapter(adapterLeft);

        int id_btc = IntStream.range(0, crypto_coins_array.length)
                .filter(i -> crypto_coins_array[i].equals("BTC")).findFirst().getAsInt(); //TODO settings to choose default value
        leftPanel.setSelection(id_btc, false);
        leftPanel.setOnItemSelectedListener(itemSelectedListener);


        ArrayAdapter<String> adapterRight = new CurrenciesAdapter(getActivity(), R.layout.row, country_coins,
                        getActivity().getLayoutInflater(), R.layout.row_item);
        rightPanel.setAdapter(adapterRight);

        int id_usd = IntStream.range(0, country_coins.length)
                .filter(i -> country_coins[i].equals("USD")).findFirst().getAsInt();
        rightPanel.setSelection(id_usd);
        rightPanel.setOnItemSelectedListener(itemSelectedListener);

    }

    private AdapterView.OnItemSelectedListener itemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //TODO decide whether to load chart every time
                    presenter.showRate(false);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    View.OnClickListener onChartOptionClick = v -> {

        //set previous chart label unselected
        setChartLabelUnselected(getSelectedChartLabelView());
        //set new one selected
        setChartLabelSelected(v);
        getChart(getSelectedChartOption());

    };

    public void setAllChartLabelsUnselected() {
        setChartLabelUnselected(optionFirst);
        setChartLabelUnselected(optionSecond);
        setChartLabelUnselected(optionThird);
        setChartLabelUnselected(optionFourth);
    }

    public void setSelectedChartOption(Codes.ChartOption chartOption) {
        selectedChartOption = chartOption;
    }

    public void setChartLabelSelected(View view) {

        switch (view.getId()) {
            case R.id.optionFirst:
                selectedChartOption = Codes.chart_options[0];
                break;
            case R.id.optionSecond:
                selectedChartOption = Codes.chart_options[1];
                break;
            case R.id.optionThird:
                selectedChartOption = Codes.chart_options[2];
                break;
            case R.id.optionFourth:
                selectedChartOption = Codes.chart_options[3];
                break;
        }

        ((TextView) view).setTextColor(getResources().getColor(R.color.colorChartLabelSelectedText));
        view.setBackground(getResources().getDrawable(R.drawable.rounded_corner_border));
        ((TextView) view).setTypeface(((TextView) view).getTypeface(), Typeface.NORMAL);
    }

    private void setChartLabelUnselected(View view) {
        ((TextView) view).setTextColor(getResources().getColor(R.color.colorChartLabelUnselectedText));
        view.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
        ((TextView) view).setTypeface(((TextView) view).getTypeface(), Typeface.BOLD);
    }

    private void getChart(Codes.ChartOption chartOption) {

        long end = LocalDateTime.now(DateTimeZone.UTC).toDateTime().getMillis() / 1000;
        long start = end - chartOption.days * 86400;

        String pair = ((String) leftPanel.getSelectedItem()).toLowerCase() +
                ((String) rightPanel.getSelectedItem()).toLowerCase();

        presenter.showChart(pair, chartOption.intervals, start, true);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_rate, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                presenter.refresh();
                presenter.showRateCMC(true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        presenter = getPresenter(RateFragmentPresenter.class);
        presenter.setView(this);
        presenter.setRouter((Router) getActivity());
        presenter.onStart();

        super.onStart();
    }

    @Override
    public void onStop() {
        String price = "";
        if (topPanel != null) {
            price = topPanel.getText().toString();
        }

        presenter.saveInstanceState(new RateFragmentInstanceState(selectedChartOption, price));
        presenter.setView(null);
        presenter.onStop();

        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTickerInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        presenter.refresh();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_rate;
    }

    @Override
    public String getPresenterId() {
        return "rate_fragment";
    }

    @Override
    public Spinner getLeftSpinner() {
        return leftPanel;
    }

    @Override
    public Spinner getRightSpinner() {
        return rightPanel;
    }

    @Override
    public TextView getTopPanel() {
        return topPanel;
    }

    @Override
    public void showProgressbar() {
        super.showProgressbar();
        swipe_container.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressbar() {
        super.hideProgressbar();
        swipe_container.setVisibility(View.VISIBLE);
    }

    @Override
    public SwipeRefreshLayout getRefreshLayout() {
        return swipe_container;
    }

    @Override
    public RateChart getRateChart() {
        return chart;
    }


    public Codes.ChartOption getSelectedChartOption() {
        if (selectedChartOption == null) selectedChartOption = Codes.chart_options[0];

        return selectedChartOption;
    }

    public TextView getSelectedChartLabelView() {

        if (selectedChartOption.equals(Codes.chart_options[0]))
            return optionFirst;

        else if (selectedChartOption.equals(Codes.chart_options[1]))
            return optionSecond;

        else if (selectedChartOption.equals(Codes.chart_options[2]))
            return optionThird;

        else if (selectedChartOption.equals(Codes.chart_options[3]))
            return optionFourth;

        else return null;
    }
}












