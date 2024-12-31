package com.example.pokemonapp;

import android.os.Bundle;
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

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Pokemon_Capturados extends Fragment {
    // Declaración de variables principales: RecyclerView, adaptador, lista de Pokémon y FirebaseFirestore
    private RecyclerView recyclerView;
    private RecyclerViewCapturados.Adapter adapter; // Adaptador para mostrar los Pokémon capturados
    private List<CapturedPokemon> pokemonList; // Lista de Pokémon capturados
    private FirebaseFirestore db; // Referencia a Firestore

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout del fragmento que contiene el RecyclerView
        View view = inflater.inflate(R.layout.fragment__pokemon__capturados, container, false);

        // Inicialización de la vista RecyclerView y la lista de Pokémon
        recyclerView = view.findViewById(R.id.recyclerViewCapturados);
        pokemonList = new ArrayList<>();

        // Inicialización de la instancia de Firestore
        db = FirebaseFirestore.getInstance();

        // Consulta a Firestore para obtener la colección de Pokémon capturados
        db.collection("Pokemons")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    // Si la consulta fue exitosa, procesamos los datos recibidos
                    if (querySnapshot != null) {
                        pokemonList.clear(); // Limpiamos la lista local para evitar duplicados
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            // Convertimos cada documento en un objeto CapturedPokemon
                            CapturedPokemon pokemon = document.toObject(CapturedPokemon.class);
                            if (pokemon != null) {
                                // Asignamos el ID único del documento al objeto Pokémon
                                pokemon.setDocuId(document.getId());
                                pokemonList.add(pokemon); // Agregamos el Pokémon a la lista
                            }
                        }

                        // Configuración del adaptador y del RecyclerView
                        adapter = new RecyclerViewCapturados.Adapter(getContext(), pokemonList, new RecyclerViewCapturados.Adapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(CapturedPokemon pokemon) {
                                // Aquí se define qué hacer cuando se hace clic en un Pokémon
                                // Ejemplo: mostrar los detalles del Pokémon en un nuevo fragmento o actividad
                            }

                            @Override
                            public void onItemDelete(CapturedPokemon pokemon) {
                                // Cuando se solicita eliminar un Pokémon, obtenemos su ID único
                                String pokemonDocuId = pokemon.getDocuId();

                                // Validamos que el ID del documento no sea nulo o vacío
                                if (pokemonDocuId == null || pokemonDocuId.isEmpty()) {
                                    Toast.makeText(getContext(), "Error: ID del Pokémon no válido", Toast.LENGTH_SHORT).show();
                                    return; // Salimos si el ID no es válido
                                }

                                // Eliminamos el Pokémon de Firestore usando su ID
                                db.collection("Pokemons")
                                        .document(pokemonDocuId) // Referencia al documento por su ID
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            // Si se eliminó con éxito, lo eliminamos también de la lista local
                                            pokemonList.remove(pokemon);
                                            adapter.notifyItemRemoved(pokemonList.indexOf(pokemon)); // Actualizamos el adaptador
                                            Toast.makeText(getContext(), "Pokémon eliminado", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Si hubo un error al eliminar, mostramos un mensaje
                                            Toast.makeText(getContext(), "Error al eliminar el Pokémon", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        });

                        // Configuramos el RecyclerView con un LayoutManager y el adaptador
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(e -> {
                    // Si la consulta falla, mostramos un mensaje de error
                    Toast.makeText(getContext(), "Error al cargar los Pokémon", Toast.LENGTH_SHORT).show();
                });

        return view; // Retornamos la vista del fragmento
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cambiamos el título de la ActionBar para reflejar el contenido actual
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.pokemon_capturados));
        }
    }
}