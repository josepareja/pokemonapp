package com.example.pokemonapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RecyclerViewCapturados {

    // Adapter que se encarga de gestionar los elementos dentro del RecyclerView de la lista de Pokémon capturados
    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private Context context;  // Contexto de la aplicación para acceder a recursos y funcionalidades
        private List<CapturedPokemon> pokemonList;  // Lista de los Pokémon capturados que se mostrarán en el RecyclerView
        private OnItemClickListener onItemClickListener;  // Listener para manejar los clics en los elementos
        private FirebaseFirestore db;  // Referencia a Firestore para interactuar con la base de datos

        // Constructor del Adapter, inicializa el contexto, la lista de Pokémon y el listener
        public Adapter(Context context, List<CapturedPokemon> pokemonList, OnItemClickListener onItemClickListener) {
            this.context = context;
            this.pokemonList = pokemonList;
            this.onItemClickListener = onItemClickListener;
            this.db = FirebaseFirestore.getInstance();  // Inicializa la instancia de Firestore
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflamos el layout de cada item del RecyclerView (cada tarjeta de Pokémon)
            View view = LayoutInflater.from(context).inflate(R.layout.cardview_capturados, parent, false);
            return new ViewHolder(view);  // Devolvemos el ViewHolder con la vista inflada
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // Obtenemos el Pokémon en la posición actual
            CapturedPokemon pokemon = pokemonList.get(position);

            // Establecemos el nombre del Pokémon en el TextView correspondiente
            holder.nameTextView.setText(pokemon.getName());

            // Convertimos la lista de tipos del Pokémon a una cadena separada por comas
            List<String> types = pokemon.getTypes();
            StringBuilder typesString = new StringBuilder();
            for (int i = 0; i < types.size(); i++) {
                typesString.append(types.get(i));
                if (i < types.size() - 1) {
                    typesString.append(", ");
                }
            }
            holder.typeTextView.setText(typesString.toString());  // Mostramos los tipos del Pokémon

            // Usamos Glide para cargar la imagen del Pokémon desde la URL y mostrarla en el ImageView
            Glide.with(context).load(pokemon.getImageUrl()).into(holder.pokemonImageView);

            // Configuramos el clic en el Pokémon, para abrir la actividad de detalles del Pokémon
            holder.itemView.setOnClickListener(v -> {
                // Creamos un Intent para abrir la actividad de detalles
                Intent intent = new Intent(context, PokemonDetailActivity.class);
                intent.putExtra("pokemon", pokemon);  // Pasamos el Pokémon seleccionado a la actividad
                context.startActivity(intent);  // Iniciamos la actividad
            });

            // Configuramos el clic en el botón de eliminar
            holder.deleteButton.setOnClickListener(v -> onItemClickListener.onItemDelete(pokemon));  // Llamamos al método de eliminación
        }

        @Override
        public int getItemCount() {
            return pokemonList.size();  // Devolvemos el tamaño de la lista para que el RecyclerView sepa cuántos elementos mostrar
        }

        // Método para eliminar un Pokémon de la base de datos y de la lista
        public void onItemDelete(CapturedPokemon pokemon) {
            String pokemonDocuId = pokemon.getDocuId();  // Obtenemos el 'docuId' del Pokémon para identificarlo en Firestore

            // Verificamos si el 'docuId' es válido
            if (pokemonDocuId == null || pokemonDocuId.isEmpty()) {
                Toast.makeText(context, "Error: ID del Pokémon no válido", Toast.LENGTH_SHORT).show();  // Mostramos un error si el 'docuId' no es válido
                return;  // Terminamos si el 'docuId' no es válido
            }

            // Eliminamos el Pokémon de la base de datos en Firestore usando el 'docuId'
            db.collection("Pokemons")
                    .document(pokemonDocuId)  // Usamos el 'docuId' para identificar el documento que queremos eliminar
                    .delete()  // Eliminamos el documento
                    .addOnSuccessListener(aVoid -> {
                        // Si la eliminación es exitosa, eliminamos el Pokémon de la lista local
                        pokemonList.remove(pokemon);
                        notifyDataSetChanged();  // Actualizamos la vista del RecyclerView
                        Toast.makeText(context, "Pokémon eliminado", Toast.LENGTH_SHORT).show();  // Mostramos un mensaje de éxito
                    })
                    .addOnFailureListener(e -> {
                        // Si ocurre un error al eliminar, mostramos un mensaje de error
                        Toast.makeText(context, "Error al eliminar el Pokémon: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

        // Interfaz que define los métodos que se deben implementar para manejar clics en los Pokémon y en el botón de eliminar
        public interface OnItemClickListener {
            void onItemClick(CapturedPokemon pokemon);  // Método para manejar el clic en el Pokémon
            void onItemDelete(CapturedPokemon pokemon);  // Método para manejar el clic en el botón de eliminar
        }

        // Clase ViewHolder que mantiene las referencias a las vistas de cada item en el RecyclerView
        public static class ViewHolder extends RecyclerView.ViewHolder {

            TextView nameTextView;  // TextView para mostrar el nombre del Pokémon
            TextView typeTextView;  // TextView para mostrar los tipos del Pokémon
            ImageView pokemonImageView;  // ImageView para mostrar la imagen del Pokémon
            ImageView deleteButton;  // Botón para eliminar el Pokémon

            public ViewHolder(View itemView) {
                super(itemView);
                // Inicializamos las vistas usando findViewById
                nameTextView = itemView.findViewById(R.id.pokemonName);
                typeTextView = itemView.findViewById(R.id.pokemonTypes);
                pokemonImageView = itemView.findViewById(R.id.pokemonImage);
                deleteButton = itemView.findViewById(R.id.buttonDelete);
            }
        }
    }
}
