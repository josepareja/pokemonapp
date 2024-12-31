package com.example.pokemonapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Adaptador para mostrar los Pokémon en el RecyclerView
public class PokemonPokedexAdapter extends RecyclerView.Adapter<PokemonPokedexAdapter.PokemonViewHolder> {

    private List<Pokemon> pokemonList; // Lista de Pokémon que se va a mostrar
    private OnItemClickListener listener; // Listener para manejar el clic en un Pokémon

    // Interfaz para el listener del clic en los ítems
    public interface OnItemClickListener {
        void onItemClick(Pokemon pokemon); // Método que se llama cuando se hace clic en un Pokémon
    }

    // Constructor del adaptador, recibe la lista de Pokémon y el listener para manejar los clics
    public PokemonPokedexAdapter(List<Pokemon> pokemonList, OnItemClickListener listener) {
        if (pokemonList == null) {
            throw new IllegalArgumentException("La lista de Pokémon no puede ser null");
        }
        this.pokemonList = pokemonList; // Asignamos la lista de Pokémon
        this.listener = listener; // Asignamos el listener
    }

    // Método para actualizar la lista de Pokémon y notificar al RecyclerView que hay cambios
    public void setPokemonList(List<Pokemon> newPokemonList) {
        pokemonList = newPokemonList;
        notifyDataSetChanged(); // Notifica al RecyclerView que se debe actualizar la vista
    }

    // Este método infla el layout de cada ítem en el RecyclerView
    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_pokedex, parent, false);
        return new PokemonViewHolder(view); // Devuelve un ViewHolder con el layout inflado
    }

    // Este método se llama para asociar los datos del Pokémon a las vistas en cada ítem
    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position); // Obtenemos el Pokémon de la lista según la posición
        holder.pokemonName.setText(pokemon.getName()); // Asignamos el nombre del Pokémon al TextView

        // Configuramos el clic en cada ítem del RecyclerView
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(pokemon); // Llamamos al método del listener cuando se hace clic
            }
        });
    }

    // Este método devuelve el número de ítems en la lista
    @Override
    public int getItemCount() {
        return pokemonList.size(); // Devuelve el tamaño de la lista de Pokémon
    }

    // ViewHolder que contiene las vistas de cada ítem (en este caso, solo el nombre del Pokémon)
    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        TextView pokemonName; // TextView para mostrar el nombre del Pokémon

        // Constructor del ViewHolder, se inicializan las vistas
        public PokemonViewHolder(View itemView) {
            super(itemView);
            pokemonName = itemView.findViewById(R.id.pokemonName); // Buscamos el TextView en el layout del ítem
            if (pokemonName == null) {
                throw new NullPointerException("El TextView pokemonName no se encontró en el layout.");
                // Si no se encuentra el TextView, lanzamos una excepción para evitar errores
            }
        }
    }
}
