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

    @Column(name = "load")
    private String load;

    @Column(name = "critical")
    private String critical;

    @Column(name = "fumble")
    private String fumble;

    @Column(name = "hover")
    private String hover;

    @Column(name = "active")
    private String active;

    @Column(name = "release_flag") //公開可否
    private int release_flag;

}
