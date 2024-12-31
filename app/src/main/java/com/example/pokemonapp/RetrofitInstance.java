package com.example.pokemonapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    // Creamos una instancia estática de Retrofit para configurar la conexión con la API
    private static Retrofit retrofit = new Retrofit.Builder()
            // Establecemos la URL base de la API de Pokémon, que se utilizará para todas las peticiones
            .baseUrl("https://pokeapi.co/api/v2/")
            // Añadimos un convertidor para que Retrofit pueda convertir las respuestas de la API a objetos de Java
            .addConverterFactory(GsonConverterFactory.create())
            // Construimos la instancia de Retrofit
            .build();

    // Método estático para obtener una instancia de la interfaz PokemonApi
    public static PokemonApi getPokemonApi() {
        // Usamos Retrofit para crear una implementación de la interfaz PokemonApi
        return retrofit.create(PokemonApi.class);
    }
}
