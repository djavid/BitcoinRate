package com.djavid.bitcoinrate.interactor;

import com.djavid.bitcoinrate.model.DataRepository;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


public class TickerFragmentUseCase implements TickerFragmentInteractor {

    private DataRepository dataRepository;

    public TickerFragmentUseCase(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public TickerFragmentUseCase() {
        this(new RestDataRepository());
    }

    @Override
    public Single<CryptonatorTicker> getRate(String curr1, String curr2) {
        return dataRepository.getRate(curr1, curr2)
                .doOnError(Throwable::printStackTrace);
    }

    @Override
    public Single<List<CoinMarketCapTicker>> getRateCMC(String crypto_id, String country_id) {
        return dataRepository.getRateCMC(crypto_id, country_id)
                .doOnError(Throwable::printStackTrace);
    }

    @Override
    public Single<List<Ticker>> getTickersByTokenId(long token_id) {
        return dataRepository.getTickersByTokenId(token_id)
                .doOnError(Throwable::printStackTrace);
    }

    @Override
    public Single<List<Subscribe>> getSubscribesByTokenId(long token_id) {
        return dataRepository.getSubscribesByTokenId(token_id)
                .doOnError(Throwable::printStackTrace);
    }

    @Override
    public Completable deleteTicker(long ticker_id) {
        return dataRepository.deleteTicker(ticker_id)
                .doOnError(Throwable::printStackTrace);
    }
}
