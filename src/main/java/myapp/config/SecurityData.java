package myapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityData {

    //ペッパー番号
    @Value("${pepper}")
    private String pepper;

    public String getPepper() {
        return pepper;
    }

    public void setPepper(String pepper) {
        this.pepper = pepper;
    }
    //キャラシ画像の保存場所
    @Value("${img_path}")
    private String img_path;

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
    //PcDiceの画像の保存場所
    @Value("${picPath}")
    private String picPath;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    //S3用ユーザー
    @Value("${user}")
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    //S3用アクセスキー
    @Value("${acKey}")
    private String acKey;

    public String getAcKey() {
        return acKey;
    }

    public void setAcKey(String acKey) {
        this.acKey = acKey;
    }

    //S3用アクセスキー
    @Value("${scKey}")
    private String scKey;

    public String getScKey() {
        return scKey;
    }

    public void setScKey(String scKey) {
        this.scKey = scKey;
    }



}
