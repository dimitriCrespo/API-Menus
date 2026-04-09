package fr.univamu.iut.menus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Client HTTP utilisé pour enrichir les menus avec les APIs externes.
 * Version minimale sans appels réels aux APIs.
 */
public class ExternalApiClient {

    /**
     * Constructeur par défaut
     */
    public ExternalApiClient() {
    }

    /**
     * Récupère les noms des utilisateurs par leur identifiant
     * @return une Map contenant les noms des utilisateurs
     */
    public Map<Integer, String> fetchUserNamesById() {
        return new HashMap<>();
    }

    /**
     * Récupère les détails des plats par leur identifiant
     * @return une Map contenant les données des plats
     */
    public Map<Integer, DishData> fetchDishesById() {
        return new HashMap<>();
    }

    /**
     * Classe représentant les données d'un plat récupérées depuis une API externe
     */
    public static class DishData {
        private String name;
        private BigDecimal price;

        /**
         * Constructeur par défaut
         */
        public DishData() {
        }

        /**
         * Constructeur avec nom et prix
         * @param name le nom du plat
         * @param price le prix du plat
         */
        public DishData(String name, BigDecimal price) {
            this.name = name;
            this.price = price;
        }

        /**
         * Getter pour le nom du plat
         * @return le nom du plat
         */
        public String getName() {
            return name;
        }

        /**
         * Setter pour le nom du plat
         * @param name le nom du plat
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Getter pour le prix du plat
         * @return le prix du plat
         */
        public BigDecimal getPrice() {
            return price;
        }

        /**
         * Setter pour le prix du plat
         * @param price le prix du plat
         */
        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}

