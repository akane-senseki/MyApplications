package myapp.forms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.Part;

import org.springframework.web.multipart.MultipartFile;

import myapp.models.Pc_Entity;

public class Pc_EntityForm extends Pc_Entity implements Part {
    private String token;

    private List<MultipartFile> cs_img;

    private int hp ;

    private String db;

    private int db_roll ;

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

    public List<MultipartFile> getCs_img() {
        return cs_img;
    }
    public void setCs_img(List<MultipartFile> cs_img) {
        this.cs_img = cs_img;
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
            db_roll = -(int)((Math.ceil(Math.random()*6)));
            return db_roll;
        }else if(dbnum < 17){
            db_roll = -(int)((Math.ceil(Math.random()*4)));
            return db_roll;
        }else if(dbnum < 25){
            db_roll = 0;
            return db_roll;
        }else if(dbnum < 33){
            db_roll = (int)((Math.ceil(Math.random()*4)));
            return db_roll;
        }else if(dbnum < 41){
            db_roll = (int)((Math.ceil(Math.random()*6)));
            return db_roll;
        }else if(dbnum < 57){
            db_roll = (int)((Math.ceil(Math.random()*12)));
            return db_roll;
        }else{
            db_roll = 0;
            return db_roll;
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
    @Override
    public InputStream getInputStream() throws IOException {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }
    @Override
    public String getContentType() {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }
    @Override
    public String getSubmittedFileName() {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }
    @Override
    public long getSize() {
        // TODO 自動生成されたメソッド・スタブ
        return 0;
    }
    @Override
    public void write(String fileName) throws IOException {
        // TODO 自動生成されたメソッド・スタブ

    }
    @Override
    public void delete() throws IOException {
        // TODO 自動生成されたメソッド・スタブ

    }
    @Override
    public String getHeader(String name) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }
    @Override
    public Collection<String> getHeaders(String name) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }
    @Override
    public Collection<String> getHeaderNames() {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }





}
