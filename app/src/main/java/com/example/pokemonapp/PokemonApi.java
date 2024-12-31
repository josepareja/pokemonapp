package com.example.pokemonapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface PokemonApi {
    @GET("pokemon?offset=0&limit=150")
    Call<PokemonResponse> getPokemons();

    // Método para obtener detalles adicionales de un Pokémon usando su URL
    @GET
    Call<Pokemon> getPokemonDetails(@Url String url);
}
