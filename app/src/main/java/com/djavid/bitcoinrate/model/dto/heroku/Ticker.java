package com.djavid.bitcoinrate.model.dto.heroku;


public class Ticker {

    private long id;
    private long tokenId;
    private String cryptoId;
    private String countryId;
    private double price;


    public Ticker(long tokenId, String cryptoId, String countryId) {
        this.tokenId = tokenId;
        this.cryptoId = cryptoId;
        this.countryId = countryId;
    }

    public Ticker(long id, long tokenId, String cryptoId, String countryId) {
        this.id = id;
        this.tokenId = tokenId;
        this.cryptoId = cryptoId;
        this.countryId = countryId;
    }


    @Override
    public String toString() {
        return "Ticker{" +
                "id=" + id +
                ", tokenId=" + tokenId +
                ", cryptoId='" + cryptoId + '\'' +
                ", countryId='" + countryId + '\'' +
                ", price=" + price +
                '}';
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getTokenId() {
        return tokenId;
    }
    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public String getCryptoId() {
        return cryptoId;
    }
    public void setCryptoId(String cryptoId) {
        this.cryptoId = cryptoId;
    }

    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}