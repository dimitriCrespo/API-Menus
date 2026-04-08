package fr.univamu.iut.menus;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/*
  Ressource REST pour gérer les menus.
  Implémente tous les endpoints CRUD pour la gestion des menus.
  Cette ressource expose l'API Menus qui sera consommée par le composant "Commandes"
 */
@Path("/menus")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MenuResource {
    private static final Logger LOGGER = Logger.getLogger(MenuResource.class.getName());
    private final MenuDAO menuDAO;

    /*
      Constructeur de la ressource Menu.
     */
    public MenuResource() {
        this.menuDAO = new MenuDAO();
    }

    /*
      Récupère tous les menus.
     */
    @GET
    public Response getTousLesMenus() {
        LOGGER.info("GET /api/menus - Récupération de tous les menus");
        List<Menu> menus = menuDAO.getTousLesMenus();
        return Response.ok(menus).build();
    }

    /*
      Récupère un menu par son identifiant.
     */
    @GET
    @Path("/{id}")
    public Response getMenuById(@PathParam("id") int id) {
        LOGGER.info("GET /api/menus/" + id + " - Récupération du menu " + id);
        Menu menu = menuDAO.getMenuById(id);
        if (menu != null) {
            return Response.ok(menu).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"erreur\": \"Menu non trouvé\"}")
                    .build();
        }
    }

    /*
      Crée un nouveau menu.
     */
    @POST
    public Response creerMenu(Menu menu) {
        LOGGER.info("POST /api/menus - Création d'un nouveau menu: " + menu.getNom());
        if (menu.getNom() == null || menu.getNom().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erreur\": \"Le nom du menu est obligatoire\"}")
                    .build();
        }

        Menu menuCree = menuDAO.creerMenu(menu);
        if (menuCree != null) {
            return Response.status(Response.Status.CREATED).entity(menuCree).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erreur\": \"Erreur lors de la création du menu\"}")
                    .build();
        }
    }

    /*
      Met à jour un menu existant.
     */
    @PUT
    @Path("/{id}")
    public Response mettreAjourMenu(@PathParam("id") int id, Menu menu) {
        LOGGER.info("PUT /api/menus/" + id + " - Mise à jour du menu " + id);
        menu.setId(id);

        // Vérifier que le menu existe
        Menu menuExistant = menuDAO.getMenuById(id);
        if (menuExistant == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"erreur\": \"Menu non trouvé\"}")
                    .build();
        }

        if (menuDAO.mettreAjourMenu(id, menu)) {
            return Response.ok(menu).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erreur\": \"Erreur lors de la mise à jour du menu\"}")
                    .build();
        }
    }

    /*
      Ajoute un plat à un menu existant.
    */
    @POST
    @Path("/{id}/plats/{platId}")
    public Response ajouterPlatAuMenu(@PathParam("id") int id, @PathParam("platId") int platId) {
        LOGGER.info("POST /api/menus/" + id + "/plats/" + platId + " - Ajout du plat " + platId + " au menu " + id);
        
        Menu menu = menuDAO.getMenuById(id);
        if (menu == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"erreur\": \"Menu non trouvé\"}")
                    .build();
        }

        // Récupérer le plat depuis le service Plats (API Plats & Utilisateurs)
        PlatService platService = new PlatService();
        Plat plat = platService.getPlatById(platId);

        if (plat == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"erreur\": \"Plat non trouvé\"}")
                    .build();
        }

        menu.ajouterPlat(plat);
        if (menuDAO.mettreAjourMenu(id, menu)) {
            return Response.ok(menu).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erreur\": \"Erreur lors de l'ajout du plat\"}")
                    .build();
        }
    }

    /*
      Retire un plat d'un menu existant.
     */
    @DELETE
    @Path("/{id}/plats/{platId}")
    public Response retirerPlatDuMenu(@PathParam("id") int id, @PathParam("platId") int platId) {
        LOGGER.info("DELETE /api/menus/" + id + "/plats/" + platId + " - Suppression du plat " + platId + " du menu " + id);
        
        Menu menu = menuDAO.getMenuById(id);
        if (menu == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"erreur\": \"Menu non trouvé\"}")
                    .build();
        }

        if (menu.retirerPlatParId(platId)) {
            if (menuDAO.mettreAjourMenu(id, menu)) {
                return Response.ok(menu).build();
            }
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"erreur\": \"Erreur lors de la suppression du plat\"}")
                .build();
    }

    /*
      Supprime un menu.
     */
    @DELETE
    @Path("/{id}")
    public Response supprimerMenu(@PathParam("id") int id) {
        LOGGER.info("DELETE /api/menus/" + id + " - Suppression du menu " + id);
        
        Menu menu = menuDAO.getMenuById(id);
        if (menu == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"erreur\": \"Menu non trouvé\"}")
                    .build();
        }

        if (menuDAO.supprimerMenu(id)) {
            return Response.ok("{\"message\": \"Menu supprimé avec succès\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erreur\": \"Erreur lors de la suppression du menu\"}")
                    .build();
        }
    }
}

