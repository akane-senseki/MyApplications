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
    //Csアクセスキー
    @Value("${acKey}")
    private String acKey;

    public String getAcKey() {
        return acKey;
    }

    public void setAcKey(String acKey) {
        this.acKey = acKey;
    }

    //Csシークレットアクセスキー
    @Value("${scKey}")
    private String scKey;

    public String getScKey() {
        return scKey;
    }

    public void setScKey(String scKey) {
        this.scKey = scKey;
    }





}
