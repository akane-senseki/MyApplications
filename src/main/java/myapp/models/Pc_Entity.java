package myapp.models;

import java.util.Date;

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
public class Pc_Entity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "name",nullable = false )
    private String name;

    @Column(name = "name_ruby") //ふりがな
    private String name_ruby;

    @Column(name = "release_flag") //公開可否
    private Integer release_flag;

    @Column(name = "img_path") //画像パス
    private String img_path;

    @Column(name = "update_date")
    private Date update_date;

    @Column(name = "delete_flag" , nullable = false)
    private Integer delete_flag;

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
    private int avoidance_add ;  //回避上乗せ分

    @Column(name = "kick_add")
    private int kick_add; //キック上乗せ分

    @Column(name="fist_add")
    private int fist_add;


    @Column(name="headbutt_add")
    private int headbutt_add;



  //名前----------------------------
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getImg_path() {
        return img_path;
    }


    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
    public Integer getRelease_flag() {
        return release_flag;
    }


    public void setRelease_flag(Integer release_flag) {
        this.release_flag = release_flag;
    }

    public Date getUpdate_date() {
        return update_date;
    }
    public void setUpdate_at(Date update_date) {
        this.update_date = update_date;
    }

    public Integer getDelete_flag() {
        return delete_flag;
    }
    public void setDelete_flag(Integer delete_flag) {
        this.delete_flag = delete_flag;
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

    public void setAvoidance_add(int avoidance_add) {   //回避の追加分-----------------
        this.avoidance_add = avoidance_add;
    }
    public int getAvoidance_add(){
        return  avoidance_add;
    }

    public int getKick_add() {       //キック-----------------------------
        return kick_add;
    }
    public void setKick_add(int kick_add) {
        this.kick_add = kick_add;
    }


    public int getFist_add() {        //こぶし----------------------------
        return fist_add ;
    }
    public void setFist_add(int fist_add) {
        this.fist_add = fist_add;
    }



    public int getHeadbutt_add() {         //頭突き-----------------------------
        return headbutt_add;
    }
    public void setHeadbutt_add(int headbutt_add) {
        this.headbutt_add = headbutt_add;
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


    public String getName_ruby() {
        return name_ruby;
    }
   public void setName_ruby(String name_ruby) {
        this.name_ruby = name_ruby;
    }

//ここまでgetter/setter---------------------------------------------------------------------








}
