package com.example.pokemonapp;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.firebase.FirebaseApp;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.pokemonapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Declaración de variables para el binding (para acceder a los elementos del diseño) y el controlador de navegación.
    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializamos el binding para asociar la vista con esta actividad.
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Buscamos el fragmento de navegación (NavHostFragment) en el diseño.
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            // Obtenemos el controlador de navegación asociado al NavHostFragment.
            navController = NavHostFragment.findNavController(navHostFragment);

            // Configuramos la barra de navegación inferior para que funcione con el controlador de navegación.
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

            // Configuramos la barra de acción para que responda a los eventos del controlador de navegación.
            NavigationUI.setupActionBarWithNavController(this, navController);
        }

        // Establecemos un listener para manejar las selecciones del menú de navegación inferior.
        binding.bottomNavigationView.setOnItemSelectedListener(this::onMenuSelected);

        // Establecemos la vista raíz como el contenido de esta actividad.
        setContentView(binding.getRoot());
    }

    // Método para manejar la selección de elementos del menú de navegación inferior.
    private boolean onMenuSelected(MenuItem menuItem) {
        // Verificamos qué elemento del menú fue seleccionado y navegamos al fragmento correspondiente.
        if (menuItem.getItemId() == R.id.mis_pokemon_menu) {
            navController.navigate(R.id.fragment_Pokemon_Capturados); // Navega al fragmento "Pokémon Capturados".
        } else if (menuItem.getItemId() == R.id.ajustes_menu) {
            navController.navigate(R.id.fragment_Ajustes); // Navega al fragmento "Ajustes".
        } else if (menuItem.getItemId() == R.id.pokedex_menu) {
            navController.navigate(R.id.fragment_Pokedex); // Navega al fragmento "Pokédex".
        }
        return true; // Indicamos que el evento fue manejado.
    }
}