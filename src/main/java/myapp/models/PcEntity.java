package myapp.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "pc_entity")
@Entity
public class PcEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false )
    private String name;

    @Column(name = "name_ruby") //ふりがな
    private String nameRuby;

    @Column(name = "release_flag") //公開可否
    private Integer releaseFlag;

    @Column(name = "img_path") //画像パス
    private String imgPath;

    @Column(name = "delete_flag" , nullable = false)
    private Integer deleteFlag;

    //能力値-------------------------------------------------------------------

    @Column(name="str")
    private int str;

    @Column(name="con")
    private int con;

    @Column(name="dex")
    private int dex;

    @Column(name="siz")
    private int siz;

    //技能値--------------------------------------------------------------------
    @Column(name = "avoidance_add")
    private int avoidanceAdd ;  //回避上乗せ分

    @Column(name = "kick_add")
    private int kickAdd; //キック上乗せ分

    @Column(name="fist_add")
    private int fistAdd;


    @Column(name="headbutt_add")
    private int headbuttAdd;



  //名前----------------------------
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getImgPath() {
        return imgPath;
    }


    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
    public Integer getReleaseFlag() {
        return releaseFlag;
    }

    public void setReleaseFlag(Integer releaseFlag) {
        this.releaseFlag = releaseFlag;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }
    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }


    public int getStr() {                //STR-------------------------------
        return str;
    }
    public void setStr(int str) {
        this.str = str;
    }

    public int getCon() {                 //CON-------------------------------
        return con;
    }
    public void setCon(int con) {
        this.con = con;
    }


    public int getDex() {
        return dex;
    }
    public void setDex(int dex) {
        this.dex = dex;
    }


    public int getSiz() {               //SIZ-------------------------------
        return siz;
    }
    public void setSiz(int siz) {
        this.siz = siz;
    }

    public void setAvoidanceAdd(int avoidanceAdd) {   //回避の追加分-----------------
        this.avoidanceAdd = avoidanceAdd;
    }
    public int getAvoidanceAdd(){
        return  avoidanceAdd;
    }

    public int getKickAdd() {       //キック-----------------------------
        return kickAdd;
    }
    public void setKickAdd(int kickAdd) {
        this.kickAdd = kickAdd;
    }


    public int getFistAdd() {        //こぶし----------------------------
        return fistAdd ;
    }
    public void setFistAdd(int fistAdd) {
        this.fistAdd = fistAdd;
    }

    public int getHeadbuttAdd() {         //頭突き-----------------------------
        return headbuttAdd;
    }
    public void setHeadbuttAdd(int headbuttAdd) {
        this.headbuttAdd = headbuttAdd;
    }

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


    public String getNameRuby() {
        return nameRuby;
    }
   public void setNameRuby(String nameRuby) {
        this.nameRuby = nameRuby;
    }

//ここまでgetter/setter---------------------------------------------------------------------








}
