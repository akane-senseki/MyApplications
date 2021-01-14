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



}
