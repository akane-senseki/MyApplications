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

    @Column(name = "defaImg")
    private String defaImg;

    @Column(name = "loadImg")
    private String loadImg;

    @Column(name = "criticalImg")
    private String criticalImg;

    @Column(name = "fumbleImg")
    private String fumbleImg;

    @Column(name = "hoverImg")
    private String hoverImg;

    @Column(name = "activeImg")
    private String activeImg;

    @Column(name = "releaseFlag") //公開可否
    private int releaseFlag;

    @Column(name = "deleteFlag") //公開可否
    private int deleteFlag;

    @Column(name = "xAxis" , nullable = false)
    private Integer xAxis;

    @Column(name = "yAxis" , nullable = false)
    private Integer yAxis;

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

    public String getDefaImg() {
        return defaImg;
    }

    public void setDefaImg(String defaImg) {
        this.defaImg = defaImg;
    }

    public String getLoadImg() {
        return loadImg;
    }

    public void setLoadImg(String loadImg) {
        this.loadImg = loadImg;
    }

    public String getCriticalImg() {
        return criticalImg;
    }

    public void setCriticalImg(String criticalImg) {
        this.criticalImg = criticalImg;
    }

    public String getFumbleImg() {
        return fumbleImg;
    }

    public void setFumbleImg(String fumbleImg) {
        this.fumbleImg = fumbleImg;
    }

    public String getHoverImg() {
        return hoverImg;
    }

    public void setHoverImg(String hoverImg) {
        this.hoverImg = hoverImg;
    }

    public String getActiveImg() {
        return activeImg;
    }

    public void setActiveImg(String activeImg) {
        this.activeImg = activeImg;
    }

    public int getReleaseFlag() {
        return releaseFlag;
    }

    public void setReleaseFlag(int releaseFlag) {
        this.releaseFlag = releaseFlag;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getxAxis() {
        return xAxis;
    }

    public void setxAxis(Integer xAxis) {
        this.xAxis = xAxis;
    }

    public Integer getyAxis() {
        return yAxis;
    }

    public void setyAxis(Integer yAxis) {
        this.yAxis = yAxis;
    }



}
