package com.example.pokemonapp;

import java.io.Serializable;
import java.util.List;

public class CapturedPokemon implements Serializable {

    // docuId: Este campo sirve para guardar el identificador único del documento en la base de datos Firestore.
    private String docuId;

    // id: Este es el identificador único del Pokémon. Puede ser usado como referencia directa al Pokémon.
    private String id;

    // name: Almacena el nombre del Pokémon. Por ejemplo, "Pikachu", "Charmander", etc.
    private String name;

    // types: Aquí se guardan los tipos del Pokémon. Por ejemplo, "Electric", "Fire". Puede tener más de un tipo.
    private List<String> types;

    // weight: Este campo indica el peso del Pokémon en kilogramos.
    private double weight;

    // height: Representa la altura del Pokémon en metros.
    private double height;

    // imageUrl: Aquí se almacena la URL de la imagen del Pokémon. Esto sirve para mostrar la imagen desde una fuente externa.
    private String imageUrl;

    // Constructor vacío: Es necesario para que Firebase pueda crear instancias de esta clase automáticamente.
    public CapturedPokemon() {}

    // Constructor completo: Lo usamos para crear objetos de manera más sencilla asignando todos los valores desde el inicio.
    public CapturedPokemon(String id, String name, List<String> types, double weight, double height, String imageUrl) {
        this.id = id; // Asigna el identificador único del Pokémon.
        this.name = name; // Asigna el nombre del Pokémon.
        this.types = types; // Asigna la lista de tipos del Pokémon.
        this.weight = weight; // Asigna el peso del Pokémon.
        this.height = height; // Asigna la altura del Pokémon.
        this.imageUrl = imageUrl; // Asigna la URL de la imagen del Pokémon.
    }

    // Métodos getter y setter para cada campo. Sirven para obtener y modificar los valores.

    // Devuelve el identificador único del documento.
    public String getDocuId() {
        return docuId;
    }

    // Permite asignar un valor al identificador único del documento.
    public void setDocuId(String docuId) {
        this.docuId = docuId;
    }

    // Devuelve el identificador único del Pokémon.
    public String getId() {
        return id;
    }

    // Permite asignar un valor al identificador único del Pokémon.
    public void setId(String id) {
        this.id = id;
    }

    // Devuelve el nombre del Pokémon.
    public String getName() {
        return name;
    }

    // Permite asignar un valor al nombre del Pokémon.
    public void setName(String name) {
        this.name = name;
    }

    // Devuelve la lista de tipos del Pokémon.
    public List<String> getTypes() {
        return types;
    }

    // Permite asignar una lista de tipos al Pokémon.
    public void setTypes(List<String> types) {
        this.types = types;
    }

    // Devuelve el peso del Pokémon.
    public double getWeight() {
        return weight;
    }

    // Permite asignar un valor al peso del Pokémon.
    public void setWeight(double weight) {
        this.weight = weight;
    }

    // Devuelve la altura del Pokémon.
    public double getHeight() {
        return height;
    }

    // Permite asignar un valor a la altura del Pokémon.
    public void setHeight(double height) {
        this.height = height;
    }

    // Devuelve la URL de la imagen del Pokémon.
    public String getImageUrl() {
        return imageUrl;
    }

    // Permite asignar un valor a la URL de la imagen del Pokémon.
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}