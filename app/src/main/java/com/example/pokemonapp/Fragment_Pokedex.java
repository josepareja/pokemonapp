package com.example.pokemonapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Pokedex extends Fragment {

    // Declaración de variables para el RecyclerView, el adaptador y la lista de Pokémon
    private RecyclerView recyclerView;
    private PokemonPokedexAdapter adapter;
    private List<Pokemon> pokemonList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Aquí se infla el layout del fragmento, que contiene el RecyclerView
        View view = inflater.inflate(R.layout.fragment__pokedex, container, false);

        // Configuración del RecyclerView con un LayoutManager para que sea una lista vertical
        recyclerView = view.findViewById(R.id.recyclerViewPokedex);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicialización de la lista de Pokémon y el adaptador
        pokemonList = new ArrayList<>();
        adapter = new PokemonPokedexAdapter(pokemonList, pokemon -> {
            // Cuando se haga clic en un Pokémon, llamamos a capturarlo
            capturarPokemon(pokemon);
        });
        recyclerView.setAdapter(adapter);

        // Llamo al método para obtener los Pokémon desde la API
        obtenerPokemons();

        return view;
    }

    // Método para obtener la lista de Pokémon desde la API
    private void obtenerPokemons() {
        // Obtengo la instancia de la API configurada con Retrofit
        PokemonApi pokemonApi = RetrofitInstance.getPokemonApi();

        // Llamo al endpoint que devuelve la lista de Pokémon
        pokemonApi.getPokemons().enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                // Verifico si la respuesta es exitosa y no está vacía
                if (response.isSuccessful() && response.body() != null) {
                    // Recorro los Pokémon recibidos y obtengo más detalles de cada uno
                    for (Pokemon pokemon : response.body().pokemons) {
                        obtenerDetallesPokemon(pokemon);
                    }
                } else {
                    // Si hubo un error, muestro un mensaje al usuario
                    Toast.makeText(getContext(), "Error al obtener Pokémon", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                // Si la llamada falló, muestro un mensaje con la razón del fallo
                Toast.makeText(getContext(), "Error al obtener Pokémon: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obtener los detalles de un Pokémon específico
    private void obtenerDetallesPokemon(Pokemon pokemon) {
        // Otra llamada a la API, ahora para los detalles de cada Pokémon
        PokemonApi pokemonApi = RetrofitInstance.getPokemonApi();
        pokemonApi.getPokemonDetails(pokemon.getUrl()).enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                // Verifico si la respuesta es exitosa y tiene datos
                if (response.isSuccessful() && response.body() != null) {
                    // Guardo los detalles completos del Pokémon en una variable
                    Pokemon pokemonDetalles = response.body();

                    // Obtengo la URL de la imagen desde la clase Sprites
                    String imageUrl = null;
                    if (pokemonDetalles.getSprites() != null && pokemonDetalles.getSprites().getBackDefault() != null) {
                        imageUrl = pokemonDetalles.getSprites().getBackDefault();
                    }

                    // Verifico si la URL de la imagen es válida y la muestro en el log
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Log.d("Pokemon", "Image URL: " + imageUrl);
                    } else {
                        Log.d("Pokemon", "No image URL available");
                    }

                    // Creo un nuevo objeto de tipo Pokémon con todos los datos que obtuve
                    Pokemon nuevoPokemon = new Pokemon();
                    nuevoPokemon.setName(pokemonDetalles.getName());
                    nuevoPokemon.setId(pokemonDetalles.getId());
                    nuevoPokemon.setTypes(pokemonDetalles.getTypes());
                    nuevoPokemon.setWeight(pokemonDetalles.getWeight());
                    nuevoPokemon.setHeight(pokemonDetalles.getHeight());
                    nuevoPokemon.setBack_default(imageUrl); // Asigno la imagen

                    // Agrego este Pokémon a la lista y notifico al adaptador que la lista cambió
                    pokemonList.add(nuevoPokemon);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                // Si hubo un error al obtener los detalles, muestro un mensaje
                Toast.makeText(getContext(), "Error al obtener detalles del Pokémon: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para capturar un Pokémon y guardarlo en Firestore
    private void capturarPokemon(Pokemon pokemon) {
        // Obtengo la instancia de Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference pokemonsRef = db.collection("Pokemons");

        // Creo un mapa con los datos del Pokémon para guardarlo
        Map<String, Object> pokemonData = new HashMap<>();
        pokemonData.put("name", pokemon.getName());
        pokemonData.put("index", pokemon.getId());
        pokemonData.put("types", pokemon.getTypeNames()); // Uso la lista de tipos como Strings
        pokemonData.put("weight", pokemon.getWeight());
        pokemonData.put("height", pokemon.getHeight());
        pokemonData.put("imageUrl", pokemon.getBack_default()); // Guardo también la URL de la imagen

        // Agrego los datos a Firestore y muestro un mensaje dependiendo del resultado
        pokemonsRef.add(pokemonData)
                .addOnSuccessListener(documentReference -> Toast.makeText(getContext(), "¡Pokémon capturado!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Log.e("Firebase", "Error al capturar Pokémon", e));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cambio el título de la barra de acción para que sea el nombre de la Pokédex
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.pokedex));
        }
    }
}