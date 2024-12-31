package com.example.pokemonapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// Clase que representa la respuesta de la API de Pokémon
public class PokemonResponse {

    // Esta lista contiene los Pokémon obtenidos de la respuesta de la API
    @SerializedName("results")  // Usamos SerializedName para asegurar que el nombre de la propiedad coincida con el del JSON
    public List<Pokemon> pokemons;  // Lista pública de Pokémon
}
