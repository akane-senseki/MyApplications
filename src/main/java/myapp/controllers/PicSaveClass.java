package myapp.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;

import myapp.config.SecurityData;
import myapp.models.PicData;
import myapp.models.User;
import myapp.repositories.PicDateRepository;

public class PicSaveClass {

    static public String PicSavePath(User u, String img_path, MultipartFile imgFile, PicDateRepository pdrepository) {

        String imgName = imgFile.getOriginalFilename();
        String extension = imgName.substring(imgName.lastIndexOf("."));//最後の.より右側の文字(拡張子)を取得
        img_path = (int) (Math.floor(Math.random() * 1000000000)) + extension;

            List<PicData> pic = pdrepository.findByUserAndDeleteFlag(u, 0);
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

    static public void PicSaveFile(User u, MultipartFile imgFile, String img_path, Long count , SecurityData securitydate, String deletePath) throws IOException {
        //MultipartFile型をInputStream型にキャストしてる(入出力出来るように)
        byte[] b = imgFile.getBytes();
        InputStream inputStream = new ByteArrayInputStream(b);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int data;
            while ((data = inputStream.read()) != -1) {
                bout.write(data);
            }
            b = bout.toByteArray();
            // 長さ
            long contentLength = b.length;

            // 設定情報
            BasicAWSCredentials credentials;
            credentials = new BasicAWSCredentials(securitydate.getAcKey(), securitydate.getScKey());

            // AWSのクライアント取得
            AmazonS3 s3 = new AmazonS3Client(credentials);

            // パスを指定することで指定の場所へ配置できる(ファイルがない場合は作成してくれるし同名だったら上書してくれる。)
            String objectPath = "pd/" + u.getId() + "/" + count + "/" + img_path;

            // TransferManagerを利用
            TransferManager manager = new TransferManager(s3);

            // 分割サイズを設定
            TransferManagerConfiguration c = new TransferManagerConfiguration();
            c.setMinimumUploadPartSize(5 * 1024L * 1024L);
            manager.setConfiguration(c);

            // メタデータに分割したデータのサイズを指定
            ObjectMetadata putMetaData = new ObjectMetadata();
            putMetaData.setContentLength(contentLength);

            // upload
            PutObjectRequest object = new PutObjectRequest("picdemo", objectPath, new ByteArrayInputStream(b),
                    putMetaData);
            s3.putObject(object);

            if(!deletePath.equals("") && deletePath != null) {
                s3.deleteObject("picdemo", deletePath);
            }

    }

}
