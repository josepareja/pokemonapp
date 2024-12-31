package com.example.pokemonapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

/**
 * Actividad que muestra los detalles de un Pokémon seleccionado.
 */
public class PokemonDetailActivity extends AppCompatActivity {

    // Declaración de las vistas para mostrar la información del Pokémon
    private TextView nameTextView; // Nombre del Pokémon
    private TextView typeTextView; // Tipos del Pokémon
    private TextView weightTextView; // Peso del Pokémon
    private TextView heightTextView; // Altura del Pokémon
    private ImageView pokemonImageView; // Imagen del Pokémon

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail);

        // Configurar el botón de vuelta en la barra de acciones
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detalles del Pokémon");

        // Obtener los datos del Pokémon enviados desde la actividad anterior mediante un Intent
        Intent intent = getIntent();
        CapturedPokemon pokemon = (CapturedPokemon) intent.getSerializableExtra("pokemon");
        // Se asume que `CapturedPokemon` implementa `Serializable`.

        // Enlazar las vistas del diseño XML con el código Java
        nameTextView = findViewById(R.id.pokemonName);
        typeTextView = findViewById(R.id.pokemonTypes);
        weightTextView = findViewById(R.id.pokemonWeight);
        heightTextView = findViewById(R.id.pokemonHeight);
        pokemonImageView = findViewById(R.id.pokemonImage);

        // Mostrar los datos del Pokémon en los TextViews
        nameTextView.setText(pokemon.getName());

        // Convertir la lista de tipos en una cadena separada por comas
        StringBuilder typesString = new StringBuilder();
        for (int i = 0; i < pokemon.getTypes().size(); i++) {
            typesString.append(pokemon.getTypes().get(i)); // Obtener el tipo por su índice
            if (i < pokemon.getTypes().size() - 1) { // Añadir una coma si no es el último tipo
                typesString.append(", ");
            }
        }
        typeTextView.setText(typesString.toString()); // Asignar los tipos al TextView

        // Mostrar el peso y la altura con los textos de recursos localizados
        weightTextView.setText(getString(R.string.peso) + " " + pokemon.getWeight());
        heightTextView.setText(getString(R.string.altura) + " " + pokemon.getHeight());

        // Cargar la imagen del Pokémon usando Glide
        Glide.with(this).load(pokemon.getImageUrl()).into(pokemonImageView);
    }

    /**
     * Habilita la acción del botón "volver" en la barra de navegación.
     *
     * @return true después de ejecutar la acción.
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Regresa a la actividad anterior
        return true;
    }
}