package com.example.libreria_api.model.experienciausuarios;


import jakarta.persistence.*;

@Entity
@Table
public class PortafolioInspiracion {

    public PortafolioInspiracion() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int por_id;

    @Column(name = "por_titulo")
    private String porTitulo;

    @Column(name = "por_descripcion")
    private String porDescripcion;

    @Lob
    @Column(name = "por_imagen")
    private String porImagen;

    @Lob
    @Column(name = "por_video")
    private String porVideo;

    @Column(name = "por_categoria")
    private String porCategoria;

    @Column(name = "por_fecha")
    private String porFecha;

    @Column(name = "usu_id")
    private Integer usuId;

    public PortafolioInspiracion(int por_id, String porTitulo, String porDescripcion, String porImagen, String porVideo, String porCategoria, String porFecha, Integer usuId) {
        this.por_id = por_id;
        this.porTitulo = porTitulo;
        this.porDescripcion = porDescripcion;
        this.porImagen = porImagen;
        this.porVideo = porVideo;
        this.porCategoria = porCategoria;
        this.porFecha = porFecha;
        this.usuId = usuId;
    }

    public int getPor_id() {
        return por_id;
    }

    public void setPor_id(int por_id) {
        this.por_id = por_id;
    }

    public String getPorTitulo() {
        return porTitulo;
    }

    public void setPorTitulo(String porTitulo) {
        this.porTitulo = porTitulo;
    }

    public String getPorDescripcion() {
        return porDescripcion;
    }

    public void setPorDescripcion(String porDescripcion) {
        this.porDescripcion = porDescripcion;
    }

    public String getPorImagen() {
        return porImagen;
    }

    public void setPorImagen(String porImagen) {
        this.porImagen = porImagen;
    }

    public String getPorVideo() {
        return porVideo;
    }

    public void setPorVideo(String porVideo) {
        this.porVideo = porVideo;
    }

    public String getPorCategoria() {
        return porCategoria;
    }

    public void setPorCategoria(String porCategoria) {
        this.porCategoria = porCategoria;
    }

    public String getPorFecha() {
        return porFecha;
    }

    public void setPorFecha(String porFecha) {
        this.porFecha = porFecha;
    }

    public Integer getUsuId() {
        return usuId;
    }

    public void setUsuId(Integer usuId) {
        this.usuId = usuId;
    }

}

