package server.models;

import java.io.Serializable;

/**
 * Classe représentant un modèle de cours.
 */
public class ModeleCourse implements Serializable{

    private String name;
    private String code;
    private String session;

    /**
     * Constructeur pour créer un objet ModeleCourse avec les informations fournies.
     *
     * @param name    Le nom du cours.
     * @param code    Le code du cours.
     * @param session La session du cours.
     */
    public ModeleCourse(String name, String code, String session) {
        this.name = name;
        this.code = code;
        this.session = session;
    }

    /**
     * Récupère le nom du cours.
     *
     * @return Le nom du cours.
     */
    public String getName() {
        return name;
    }

    /**
     * Définit le nom du cours.
     *
     * @param name Le nouveau nom du cours.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Récupère le code du cours.
     *
     * @return Le code du cours.
     */
    public String getCode() {
        return code;
    }

    /**
     * Définit le code du cours.
     *
     * @param code Le nouveau code du cours.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Récupère la session du cours.
     *
     * @return La session du cours.
     */
    public String getSession() {
        return session;
    }

    /**
     * Définit la session du cours.
     *
     * @param session La nouvelle session du cours.
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'objet ModeleCourse.
     *
     * @return Une chaîne de caractères représentant l'objet ModeleCourse.
     */
    @Override
    public String toString() {
        return  code + " " + name;
    }
}
