package cl.activaresearch.android_app.Dooit.models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 19 Jun,2018
 */
public class TaskBean implements Serializable {

    /**
     * cod_shopper : 0
     * descripcion : test 1
     * cod_tarea : 802111
     * nombre : Test 3 Android
     * categoria : Tecnología
     * icono : /img/categorias/icon-category-tech.png
     * color : #2467bb
     * cliente : 8
     * foto : /img/clientes/cmrfalabella.png
     * pago : 5000
     * fecha_inicio : 2018-06-28T00:00:00.000Z
     * fecha_termino : 2018-07-31T00:00:00.000Z
     * horas : 3
     * mins : 2
     * cod_sucursal : 3610
     * status : 1
     * paso1 : test 2
     * paso2 : test 3
     * paso3 : test 4
     * completado_paso1 : null
     * completado_paso2 : null
     * completado_paso3 : null
     * lat : 22.753285
     * lon : 75.893696
     * distancia : 0.02887344366353983
     */

    private String cod_shopper;
    private String descripcion;
    private int cod_tarea;
    private String cod_cuestionario;
    private int cod_estudio;
    private String nombre;
    private String categoria;
    private String icono;
    private String color;
    private String cliente;
    private String foto;
    private String pago;
    private String fecha_inicio;
    private String fecha_termino;
    private int horas;
    private int mins;
    private int cod_sucursal;
    private int status;
    private String paso1;
    private String paso2;
    private String paso3;
    private Object completado_paso1;
    private Object completado_paso2;
    private Object completado_paso3;
    private String lat;
    private String lon;
    private double distancia;
    private double progress;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getCod_shopper() {
        return cod_shopper;
    }

    public void setCod_shopper(String cod_shopper) {
        this.cod_shopper = cod_shopper;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCod_tarea() {
        return cod_tarea;
    }

    public void setCod_tarea(int cod_tarea) {
        this.cod_tarea = cod_tarea;
    }

    public String getCod_cuestionario() {
        return cod_cuestionario;
    }

    public void setCod_cuestionario(String cod_cuestionario) {
        this.cod_cuestionario = cod_cuestionario;
    }

    public int getCod_estudio() {
        return cod_estudio;
    }

    public void setCod_estudio(int cod_estudio) {
        this.cod_estudio = cod_estudio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_termino() {
        return fecha_termino;
    }

    public void setFecha_termino(String fecha_termino) {
        this.fecha_termino = fecha_termino;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getMins() {
        return mins;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }

    public int getCod_sucursal() {
        return cod_sucursal;
    }

    public void setCod_sucursal(int cod_sucursal) {
        this.cod_sucursal = cod_sucursal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPaso1() {
        return paso1;
    }

    public void setPaso1(String paso1) {
        this.paso1 = paso1;
    }

    public String getPaso2() {
        return paso2;
    }

    public void setPaso2(String paso2) {
        this.paso2 = paso2;
    }

    public String getPaso3() {
        return paso3;
    }

    public void setPaso3(String paso3) {
        this.paso3 = paso3;
    }

    public Object getCompletado_paso1() {
        return completado_paso1;
    }

    public void setCompletado_paso1(Object completado_paso1) {
        this.completado_paso1 = completado_paso1;
    }

    public Object getCompletado_paso2() {
        return completado_paso2;
    }

    public void setCompletado_paso2(Object completado_paso2) {
        this.completado_paso2 = completado_paso2;
    }

    public Object getCompletado_paso3() {
        return completado_paso3;
    }

    public void setCompletado_paso3(Object completado_paso3) {
        this.completado_paso3 = completado_paso3;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
