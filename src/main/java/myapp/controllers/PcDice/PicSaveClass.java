package myapp.controllers.PcDice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import myapp.config.SecurityData;
import myapp.models.Pic_Data;
import myapp.models.User;
import myapp.repositories.PicDateRepository;

public class PicSaveClass {

    static public String PicSavePath(User u, String img_path, MultipartFile imgFile, PicDateRepository pdrepository) {

        String imgName = imgFile.getOriginalFilename();
        System.out.println(imgName);
        String extension = imgName.substring(imgName.lastIndexOf("."));//最後の.より右側の文字(拡張子)を取得
        img_path = (int) (Math.floor(Math.random() * 1000000000)) + extension;
        System.out.println("saveの中："+img_path);

            List<Pic_Data> pic = pdrepository.findByUserAndDeleteFlag(u, 0);
            //画像名が既存のものと被っていた場合変更する(変更後はもう一度確認する)
            for (int i = 0; i < pic.size(); i++) {
                if (img_path.equals(pic.get(i).getDefaImg())) {
                    img_path = "96" + img_path;
                    for (int n = 0; n < pic.size(); n++) {
                        if (img_path.equals(pic.get(n).getDefaImg())) {
                            img_path = "96" + img_path;
                        }
                    }
                }
            }
            return img_path;
        }

    static public void PicSaveFile(User u, MultipartFile imgFile, String img_path, Long count , SecurityData securitydate) throws IOException {
        //MultipartFile型をInputStream型にキャストしてる(入出力出来るように)
        byte[] byteArr = imgFile.getBytes();
        InputStream image = new ByteArrayInputStream(byteArr);
        BufferedInputStream reader = new BufferedInputStream(image);

        try (
                FileOutputStream img = new FileOutputStream(
                        securitydate.getPicPath() + u.getId() + "/" + count + "/" + img_path);
                BufferedOutputStream writer = new BufferedOutputStream(img);) {
            int data;
            while ((data = reader.read()) != -1) {
                writer.write(data);
            }

        } catch (FileNotFoundException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IOException e1) {
            // TODO 自動生成された catch ブロック
            e1.printStackTrace();
        }
    }

}
