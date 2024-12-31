package com.example.pokemonapp;

// Importaciones necesarias para las funcionalidades que se implementan en este fragmento
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pokemonapp.databinding.FragmentAjustesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;

import java.util.Locale;

// Clase del fragmento de ajustes
public class Fragment_Ajustes extends Fragment {
    // Binding para acceder a los elementos del layout de forma sencilla
    private FragmentAjustesBinding binding;
    // SharedPreferences para guardar configuraciones del usuario
    private SharedPreferences sharedPreferences;
    // Firebase Firestore para manejar la base de datos de Pokémon
    private FirebaseFirestore firestore;
    private CollectionReference pokemonCollection;

    // Constructor vacío necesario para instanciar el fragmento
    public Fragment_Ajustes() {}

    // Método que infla la vista del fragmento
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAjustesBinding.inflate(inflater, container, false);

        // Inicializo las preferencias compartidas para manejar configuraciones locales
        sharedPreferences = requireActivity().getSharedPreferences("Ajustes", getContext().MODE_PRIVATE);

        // Configuro el botón para cerrar sesión
        binding.btnLogout.setOnClickListener(v -> showDialog(
                "Cerrar sesión",
                "¿Estás seguro de que deseas cerrar sesión?",
                (dialog, which) -> logout()));

        // Configuro la sección para cambiar el idioma de la app
        binding.linearIdioma.setOnClickListener(v -> onIdiomaClick());

        // Manejo el interruptor para activar/desactivar la eliminación de Pokémon
        binding.switchEliminarPokemon.setChecked(sharedPreferences.getBoolean("EliminarPokemon", false));
        binding.switchEliminarPokemon.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleEliminarPokemon(isChecked);
            if (isChecked) {
                eliminarPokemonDeFirestore();
            }
        });

        // Configuro el botón para mostrar información de "Acerca de"
        binding.btnAbout.setOnClickListener(v -> showDialog(
                "Acerca de",
                "Desarrollador: Jose Manuel Pareja Arrebola\nVersión: 1.0.0",
                (dialog, which) -> dialog.dismiss()));

        // Inicializo la referencia a Firestore y a la colección de Pokémon
        firestore = FirebaseFirestore.getInstance();
        pokemonCollection = firestore.collection("Pokemons"); // Cambia la ruta si es necesario

        return binding.getRoot();
    }

    // Método para cerrar sesión del usuario
    private void logout() {
        try {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
            // Redirige al usuario a la pantalla de inicio de sesión
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error al cerrar sesión", Toast.LENGTH_SHORT).show();
        }
    }

    // Muestra un diálogo para seleccionar el idioma de la app
    private void onIdiomaClick() {
        String[] idiomas = {"Español", "Inglés"};
        int idiomaSeleccionado = isLanguageEnglish() ? 1 : 0; // 0 para Español, 1 para Inglés

        new AlertDialog.Builder(requireContext())
                .setTitle("Seleccionar idioma")
                .setSingleChoiceItems(idiomas, idiomaSeleccionado, (dialog, which) -> {
                    // Cambiar el idioma según la selección del usuario
                    changeLanguage(which == 1); // 1 = Inglés, 0 = Español
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }

    // Cambia el idioma de la app y reinicia la actividad actual
    private void changeLanguage(boolean isEnglish) {
        String languageCode = isEnglish ? "en" : "es";
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics());

        sharedPreferences.edit().putBoolean("IdiomaIngles", isEnglish).apply();

        // Reinicia la actividad para aplicar los cambios de idioma
        Intent intent = requireActivity().getIntent();
        requireActivity().finish();
        startActivity(intent);
    }

    // Verifica si el idioma actual es inglés
    private boolean isLanguageEnglish() {
        return sharedPreferences.getBoolean("IdiomaIngles", false);
    }

    // Activa o desactiva la funcionalidad de eliminar Pokémon y guarda el estado
    private void toggleEliminarPokemon(boolean isEnabled) {
        sharedPreferences.edit().putBoolean("EliminarPokemon", isEnabled).apply();
        String message = isEnabled ? "Eliminación de Pokémon activada" : "Eliminación de Pokémon desactivada";
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Elimina todos los Pokémon de la colección en Firestore
    private void eliminarPokemonDeFirestore() {
        pokemonCollection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(requireContext(), "Pokémon eliminados de la base de datos", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(requireContext(), "Error al eliminar Pokémon", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error al obtener los Pokémon", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Muestra un cuadro de diálogo genérico con un mensaje
    private void showDialog(String title, String message, DialogInterface.OnClickListener positiveListener) {
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", positiveListener)
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Limpia el binding al destruir la vista para evitar fugas de memoria
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Establece el título de la barra de acción cuando la vista está creada
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.ajustes));
        }
    }
}