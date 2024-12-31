package com.example.pokemonapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Aquí no inflamos un layout porque esta actividad solo se encarga del flujo de inicio de sesión.
        // Si hay un usuario autenticado, irá directamente a MainActivity.
    }

    private void startSignIn() {
        // Elegimos los proveedores de autenticación que queremos permitir (Email y Google en este caso).
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Creamos el Intent para iniciar el flujo de inicio de sesión usando FirebaseUI.
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers) // Proveedores configurados.
                .setLogo(R.drawable.logopokemon) // Logo personalizado para el flujo de inicio de sesión.
                .setTheme(R.style.Theme_PokemonApp) // Tema personalizado para la pantalla.
                .build();

        // Lanza el Intent para el inicio de sesión.
        signInLauncher.launch(signInIntent);
    }

    // Creamos un lanzador para manejar el resultado del flujo de inicio de sesión.
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    // Llamamos a un método que procesa el resultado del inicio de sesión.
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        // Obtenemos la respuesta del flujo de autenticación.
        IdpResponse response = result.getIdpResponse();

        if (result.getResultCode() == RESULT_OK) {
            // Si el resultado es OK, significa que el usuario se autenticó correctamente.
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Podríamos mostrar un mensaje o hacer algo con los datos del usuario aquí.
            goToMainActivity(); // Pasamos a la actividad principal.
        } else {
            // Si la autenticación falla.
            if (response == null) {
                // Caso en el que el usuario canceló el inicio de sesión (pulsó atrás).
                // Muestra un mensaje o toma acción si es necesario.
            } else {
                // Manejo de otros errores, como problemas de conexión.
                // Podemos obtener más detalles usando `response.getError()`.
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verificamos si ya hay un usuario autenticado.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Si hay un usuario autenticado, pasamos a la actividad principal.
            goToMainActivity();
        } else {
            // Si no hay sesión activa, iniciamos el flujo de inicio de sesión.
            startSignIn();
        }
    }

    private void goToMainActivity() {
        // Creamos un Intent para redirigir al usuario a la actividad principal.
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i); // Iniciamos la nueva actividad.
        finish(); // Cerramos esta actividad para que no quede en la pila.
    }
}
