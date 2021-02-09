package myapp.forms;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import myapp.models.PicData;

public class Pic_DataForm extends PicData{
    private String token;

    private List<MultipartFile> defa;

    private List<MultipartFile> load;

    private List<MultipartFile> critical;

    private List<MultipartFile> fumble;

    private List<MultipartFile> hover;

    private List<MultipartFile> active;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<MultipartFile> getDefa() {
        return defa;
    }

    public void setDefa(List<MultipartFile> defa) {
        this.defa = defa;
    }

    public List<MultipartFile> getLoad() {
        return load;
    }

    public void setLoad(List<MultipartFile> load) {
        this.load = load;
    }

    public List<MultipartFile> getCritical() {
        return critical;
    }

    public void setCritical(List<MultipartFile> critical) {
        this.critical = critical;
    }

    public List<MultipartFile> getFumble() {
        return fumble;
    }

    public void setFumble(List<MultipartFile> fumble) {
        this.fumble = fumble;
    }

    public List<MultipartFile> getHover() {
        return hover;
    }

    public void setHover(List<MultipartFile> hover) {
        this.hover = hover;
    }

    public List<MultipartFile> getActive() {
        return active;
    }

    public void setActive(List<MultipartFile> active) {
        this.active = active;
    }




}
