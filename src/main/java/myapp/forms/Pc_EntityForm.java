package myapp.forms;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import myapp.models.Pc_Entity;

public class Pc_EntityForm extends Pc_Entity{
    private String token;

    private List<MultipartFile> csImg;

    private int hp ;

    private String db;

    private int dbRoll ;

    private int avoidance ;

    private int fist; //こぶし

    private int kick; //キック

    private int headbutt; //頭突き

//getter/setter---------------------------------------

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public List<MultipartFile> getCsImg() {
        return csImg;
    }
    public void setCsImg(List<MultipartFile> csImg) {
        this.csImg = csImg;
    }

    public int getHp() {
        hp = this.getCon() + this.getSiz()/2;
        return hp;
    }

    public String getDb(int str , int siz)  {
        int dbnum = str + siz;

        if(dbnum > 2 && dbnum < 13){
         db = "-1d6";
         return db;
        }else if(dbnum < 17){
            String db = "-1d4";
            return db;
        }else if(dbnum < 25){
            String db = "0";
            return db;
        }else if(dbnum < 33){
            String db = "+1d4";
            return db;
        }else if(dbnum < 41){
            String db = "+1d6";
            return db;
        }else if(dbnum < 57){
            String db = "+2d6";
            return db;
        }else{
            String db = "未設定";
            return db ;
        }
    }
    public int getDb_Roll() {
        int dbnum = this.getStr() + this.getSiz();

        if(dbnum > 2 && dbnum < 13){
            dbRoll = -(int)((Math.ceil(Math.random()*6)));
            return dbRoll;
        }else if(dbnum < 17){
            dbRoll = -(int)((Math.ceil(Math.random()*4)));
            return dbRoll;
        }else if(dbnum < 25){
            dbRoll = 0;
            return dbRoll;
        }else if(dbnum < 33){
            dbRoll = (int)((Math.ceil(Math.random()*4)));
            return dbRoll;
        }else if(dbnum < 41){
            dbRoll = (int)((Math.ceil(Math.random()*6)));
            return dbRoll;
        }else if(dbnum < 57){
            dbRoll = (int)((Math.ceil(Math.random()*12)));
            return dbRoll;
        }else{
            dbRoll = 0;
            return dbRoll;
        }
    }

    public int getKick() {
        return this.getKickAdd() + 25;
    }

    public int getHeadbutt() {
        return headbutt + 30;
    }

    public int getFist() {
        return this.getFistAdd() + 50;
    }

    public int getAvoidance() {                          //回避そのもの
        this.avoidance = (int)(Math.ceil(this.getDex() * 2) + this.getAvoidanceAdd()) ;
        return avoidance;
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





}
