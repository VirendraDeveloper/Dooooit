package cl.activaresearch.android_app.Dooit.models;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 22 Jun,2018
 */
public class CategoryBean {
    /**
     * id : 1
     * name : Farmacia
     * iconPath : /img/categorias/icon-category-pharmacy.png
     * color : #ff5c47
     */

    private int id;
    private String name;
    private String iconPath;
    private String color;
    private boolean select;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
