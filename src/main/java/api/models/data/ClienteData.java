package api.models.data;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes_data")
public class ClienteData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(nullable = false, columnDefinition = "json")
    private String tiquetes = "[]";

    public ClienteData() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTiquetes() {
        return tiquetes;
    }

    public void setTiquetes(String tiquetes) {
        this.tiquetes = tiquetes;
    }
}
