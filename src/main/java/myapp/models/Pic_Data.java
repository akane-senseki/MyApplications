package myapp.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "pic_data")
@Entity
public class Pic_Data {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "defa")
    private String defa;

    @Column(name = "load_img")
    private String load_img;

    @Column(name = "critical_img")
    private String critical_img;

    @Column(name = "fumble_img")
    private String fumble_img;

    @Column(name = "hover_img")
    private String hover_img;

    @Column(name = "active_img")
    private String active_img;

    @Column(name = "release_flag") //公開可否
    private int release_flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefa() {
        return defa;
    }

    public void setDefa(String defa) {
        this.defa = defa;
    }

    public String getLoad_img() {
        return load_img;
    }

    public void setLoad_img(String load_img) {
        this.load_img = load_img;
    }

    public String getCritical_img() {
        return critical_img;
    }

    public void setCritical_img(String critical_img) {
        this.critical_img = critical_img;
    }

    public String getFumble_img() {
        return fumble_img;
    }

    public void setFumble_img(String fumble_img) {
        this.fumble_img = fumble_img;
    }

    public String getHover_img() {
        return hover_img;
    }

    public void setHover_img(String hover_img) {
        this.hover_img = hover_img;
    }

    public String getActive_img() {
        return active_img;
    }

    public void setActive_img(String active_img) {
        this.active_img = active_img;
    }

    public int getRelease_flag() {
        return release_flag;
    }

    public void setRelease_flag(int release_flag) {
        this.release_flag = release_flag;
    }



}
