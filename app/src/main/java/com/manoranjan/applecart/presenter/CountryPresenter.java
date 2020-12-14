package com.manoranjan.applecart.presenter;


import com.manoranjan.applecart.model.Country;
import com.manoranjan.applecart.model.Data;
import com.manoranjan.applecart.service.CountryService;
import com.manoranjan.applecart.view.CountryView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * This class represents the country view interface.
 *
 * @author Jean Carlos (Github: @jeancsanchez)
 * @date 29/07/16.
 * Jesus loves you.
 */
public class CountryPresenter {

    private CountryView countryView;
    private CountryService countryService;

    public CountryPresenter(CountryView view) {
        this.countryView = view;

        if (this.countryService == null) {
            this.countryService = new CountryService();
        }
    }

    public void getCountries() {
        countryService
                .getAPI()
                .getResults()
                .enqueue(new Callback<Data>() {
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        Data data = response.body();

                        if (data != null && data.getRestResponse() != null) {
                            List<Country> result = data.getRestResponse().getResult();
                            countryView.countriesReady(result);
                        }
                    }

                    @Override
                    public void onFailure(Call<Data> call, Throwable t) {
                        try {
                            throw new InterruptedException("Something went wrong!");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
