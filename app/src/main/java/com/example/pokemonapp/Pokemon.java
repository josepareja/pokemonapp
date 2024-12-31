package com.example.pokemonapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un Pokémon con información como nombre, ID, tipos, peso, altura,
 * URL y sprites (imágenes asociadas).
 */
public class Pokemon {

    // Campos principales de la clase Pokemon
    private String name; // Nombre del Pokémon
    private int id; // ID del Pokémon
    private List<Type> types; // Lista de tipos asociados al Pokémon
    private double weight; // Peso del Pokémon
    private double height; // Altura del Pokémon (en decímetros)
    private String back_default; // URL de la imagen trasera por defecto
    private String url; // URL de la API para obtener más información del Pokémon
    private Sprites sprites; // Campo que contiene los sprites del Pokémon

    /**
     * Clase anidada que representa los tipos del Pokémon.
     */
    public static class Type {
        private TypeDetail type; // Detalle del tipo (nombre)

        public TypeDetail getType() {
            return type;
        }

        public void setType(TypeDetail type) {
            this.type = type;
        }
    }

    /**
     * Clase anidada que contiene el detalle del tipo (nombre del tipo).
     */
    public static class TypeDetail {
        private String name; // Nombre del tipo (por ejemplo, "agua", "fuego")

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * Clase anidada que representa los sprites (imágenes) asociados al Pokémon.
     */
    public static class Sprites {
        @SerializedName("back_default") // Asegura que esta propiedad coincida con el campo JSON.
        private String backDefault; // URL de la imagen trasera por defecto del Pokémon.

        public String getBackDefault() {
            return backDefault;
        }

        public void setBackDefault(String backDefault) {
            this.backDefault = backDefault;
        }
    }

    // Getters y setters para los campos principales de la clase.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    // Devuelve la altura del Pokémon en metros.
    public double getHeight() {
        return height / 10.0; // Convierte la altura de decímetros a metros.
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getBack_default() {
        return back_default;
    }

    public void setBack_default(String back_default) {
        this.back_default = back_default;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    /**
     * Método para obtener los nombres de los tipos del Pokémon como una lista de Strings.
     *
     * @return Lista de nombres de tipos.
     */
    public List<String> getTypeNames() {
        List<String> typeNames = new ArrayList<>();
        if (types != null) {
            for (Type type : types) {
                if (type != null && type.getType() != null) {
                    typeNames.add(type.getType().getName());
                }
            }
        }
        return typeNames;
    }
}

