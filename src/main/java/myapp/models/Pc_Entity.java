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
    private int release_flag;

    //能力値-------------------------------------------------------------------

    @Column(name="str")
    private int str;

    @Column(name="con")
    private int con;

    @Column(name="dex")
    private int dex;

    @Column(name="siz")
    private int siz;

    private int hp ;

    private String db;
    private int db_roll ;

    //技能値--------------------------------------------------------------------
    @Column(name = "avoidance_add")
    private int avoidance_add ;  //回避上乗せ分

    private int avoidance ;

    @Column(name = "kick_add")
    private int kick_add; //キック上乗せ分
    private int kick; //キック

    @Column(name="fist_add")
    private int fist_add;
    private int fist; //こぶし

    @Column(name="headbutt_add")
    private int headbutt_add;
    private int headbutt; //頭突き



    public int getDb_Roll() {
        if(this.str + this.siz > 2 && this.str + this.siz < 13){
            db_roll = -(int)((Math.ceil(Math.random()*6)));
            return db_roll;
        }else if(this.str + this.siz < 17){
            db_roll = -(int)((Math.ceil(Math.random()*4)));
            return db_roll;
        }else if(this.str + this.siz < 25){
            db_roll = 0;
            return db_roll;
        }else if(this.str + this.siz < 33){
            db_roll = (int)((Math.ceil(Math.random()*4)));
            return db_roll;
        }else if(this.str + this.siz < 41){
            db_roll = (int)((Math.ceil(Math.random()*6)));
            return db_roll;
        }else if(this.str + this.siz < 57){
            db_roll = (int)((Math.ceil(Math.random()*12)));
            return db_roll;
        }else{
            db_roll = 0;
            return db_roll;
        }
    }


    public String getDb(int str , int siz)  {
        int db_number = str + siz;

        if(db_number > 2 && db_number < 13){
         db = "-1d6";
         return db;
        }else if(db_number < 17){
            String db = "-1d4";
            return db;
        }else if(db_number < 25){
            String db = "0";
            return db;
        }else if(db_number < 33){
            String db = "+1d4";
            return db;
        }else if(db_number < 41){
            String db = "+1d6";
            return db;
        }else if(db_number < 57){
            String db = "+2d6";
            return db;
        }else{
            String db = "未設定";
            return db ;
        }
    }





    public String getName() {              //名前----------------------------
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public int getDex() {               //DEX-------------------------------
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
    public int getHp() {                   //HP-------------------------------
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setAvoidance_add(int avoidance_add) {   //回避の追加分-----------------
        this.avoidance_add = avoidance_add;
    }
    public int getAvoidance_add(){
        return  avoidance_add;
    }


    public int getAvoidance() {                          //回避そのものgetter-------------------
        this.avoidance = (int)(Math.ceil(dex * 2) + avoidance_add) ;
        return avoidance;
    }





    public int getKick_add() {       //キック-----------------------------
        return kick_add;
    }
    public void setKick_add(int kick_add) {
        this.kick_add = kick_add;
    }

    public int getKick() {
        return kick + 25;
    }
    public void setKick(int kick) {
        this.kick = kick;
    }


    public int getFist_add() {        //こぶし----------------------------
        return fist_add ;
    }
    public void setFist_add(int fist_add) {
        this.fist_add = fist_add;
    }
    public int getFist() {
        return fist + 50;
    }
    public void setFist(int fist) {
        this.fist = fist;
    }

    public int getHeadbutt_add() {         //頭突き-----------------------------
        return headbutt_add;
    }
    public void setHeadbutt_add(int headbutt_add) {
        this.headbutt = headbutt_add;
    }
    public int getHeadbutt() {
        return headbutt + 30;
    }
    public void setHeadbutt(int headbutt) {
        this.headbutt = headbutt;
    }


    public String pc_attack(){     //一番技能値が高い戦闘技能---------------------
        String attack ;
        if(kick > fist && kick > headbutt){
            attack = "キック";
            return attack;
        }else if(fist > headbutt){ //等しい場合は頭突きに-----------------------
            attack = "こぶし";
            return attack;
        }else{
            attack = "頭突き";
            return attack;
        }
    }


    public int pc_attack_roll(){    //↑の数値
        int attack ;
        if(kick > fist && kick > headbutt){
            attack = (int)((Math.ceil(Math.random()*6)));
            return attack;
        }else if(fist > headbutt){ //等しい場合は頭突きに-----------------------
            attack = (int)((Math.ceil(Math.random()*3)));
            return attack;
        }else{
            attack = (int)((Math.ceil(Math.random()*4)));
            return attack;
        }
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
