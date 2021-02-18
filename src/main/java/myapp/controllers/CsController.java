package myapp.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;

import myapp.config.SecurityData;
import myapp.forms.PcEntityForm;
import myapp.models.PcEntity;
import myapp.models.PcEntityLike;
import myapp.models.User;
import myapp.repositories.PcLikeRepository;
import myapp.repositories.PcRepository;

@Controller
public class CsController {
    // 分割サイズ
    private static final long PART_SIZE = 5 * 1024L * 1024L;

    @Autowired
    HttpSession session; // セッション

    @Autowired
    PcRepository pcrepository;

    @Autowired
    SecurityData securitydate;

    @Autowired
    PcLikeRepository pcLikerepository;

    @RequestMapping(value = "/cs/user", method = RequestMethod.GET)
    public ModelAndView csUserIndex(@RequestParam(name = "page", required = false) Integer page, ModelAndView mv) {
        if (session.getAttribute("login_user") != null) {
            if (page == null) {
                page = 1;
            }
            Page<PcEntity> pc = pcrepository.findByUserAndDeleteFlag((User) session.getAttribute("login_user"), 0,
                    (PageRequest.of(page - 1, 15, Sort.by("id").descending())));
            long allCount = pcrepository.countByUserAndDeleteFlag((User) session.getAttribute("login_user"), 0);
            mv.addObject("count", allCount);

            mv.addObject("pc", pc);
            mv.addObject("page", page);
            mv.setViewName("views/charactersheet/user");

        } else {
            //ログインしていないなら通常のindexへ
            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト
        }
        return mv;

    }

    @RequestMapping(value = "/cs/userLike", method = RequestMethod.GET)
    public ModelAndView csUserLikeIndex(ModelAndView mv) {
        List<PcEntityLike> likes = pcLikerepository.findByUser((User) session.getAttribute("login_user"));
        List<PcEntity> pc = new ArrayList<PcEntity>();
        for (int i = 0; i < likes.size(); i++) {
            if (likes.get(i).getPc_entity().getDeleteFlag() == 0) {
                pc.add(likes.get(i).getPc_entity());
            }
        }

        mv.addObject("pc", pc);
        mv.setViewName("views/charactersheet/like");

        return mv;
    }

    @RequestMapping(value = "/cs/index", method = RequestMethod.GET)
    public ModelAndView csIndex(@RequestParam(name = "page", required = false) Integer page, ModelAndView mv)
            throws IOException {
        if (page == null) {
            page = 1;
        }

        Page<PcEntity> pc = pcrepository.findByDeleteFlagAndReleaseFlag(0, 0,
                PageRequest.of(page - 1, 10, Sort.by("id").descending()));
        long allCount = pcrepository.countByDeleteFlagAndReleaseFlag(0, 0);
        mv.addObject("pc", pc);
        mv.addObject("page", page);
        mv.addObject("count", allCount);
        mv.setViewName("views/charactersheet/index");

        if (session.getAttribute("flush") != null) {
            mv.addObject("flush", session.getAttribute("flush"));
            session.removeAttribute("flush");
        }
        if (session.getAttribute("error") != null) {
            mv.addObject("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }

        return mv;

    }

    @RequestMapping(value = "/cs/new", method = RequestMethod.GET)
    public ModelAndView csNew(@ModelAttribute PcEntityForm peForm, ModelAndView mv) {

        peForm.setToken(session.getId());
        mv.addObject("pc", peForm);
        mv.setViewName("views/charactersheet/new");

        return mv;

    }

    @RequestMapping(value = "/cs/create", method = RequestMethod.POST)
    @Transactional // メソッド開始時にトランザクションを開始、終了時にコミットする
    public ModelAndView csCerate(@ModelAttribute PcEntityForm peForm, ModelAndView mv) throws IOException {

        if (peForm.getToken() != null && peForm.getToken().equals(session.getId())) {
            PcEntity p = new PcEntity();

            p.setUser((User) session.getAttribute("login_user"));
            p.setName(peForm.getName());
            p.setNameRuby(peForm.getNameRuby());
            p.setReleaseFlag(peForm.getReleaseFlag());
            p.setDeleteFlag(0);

            p.setStr(peForm.getStr());
            p.setCon(peForm.getCon());
            p.setDex(peForm.getDex());
            p.setSiz(peForm.getSiz());

            p.setAvoidanceAdd(peForm.getAvoidanceAdd());
            p.setKickAdd(peForm.getKickAdd());
            p.setFistAdd(peForm.getFistAdd());
            p.setHeadbuttAdd(peForm.getHeadbuttAdd());

            User u = (User) session.getAttribute("login_user");

            //ここから画像のパス作成------------------------------------

            //画像がアップロードされていたら
            if (!peForm.getCsImg().get(0).getOriginalFilename().equals("")
                    && peForm.getCsImg().get(0).getOriginalFilename() != null) {

                MultipartFile imgFile = peForm.getCsImg().get(peForm.getCsImg().size() - 1);

                String imgName = imgFile.getOriginalFilename();
                String extension = imgName.substring(imgName.lastIndexOf("."));//最後の.より右側の文字(拡張子)を取得
                String img_path = (int) (Math.floor(Math.random() * 1000000000)) + extension;
                List<PcEntity> pc = pcrepository.findByUserAndDeleteFlag(u, 0);
                //画像名が既存のものと被っていた場合変更する(変更後はもう一度確認する)
                for (int i = 0; i < pc.size(); i++) {
                    if (img_path.equals(pc.get(i).getImgPath())) {
                        img_path = "96" + img_path;
                        for (int n = 0; n < pc.size(); n++) {
                            if (img_path.equals(pc.get(n).getImgPath())) {
                                img_path = "96" + img_path;
                            }
                        }
                    }
                }
                p.setImgPath(img_path);

                //S3に画像アップロード。
                byte[] b = imgFile.getBytes();

                // ファイルを読み込み
                InputStream inputStream = new ByteArrayInputStream(b);
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                int data1;
                while ((data1 = inputStream.read()) != -1) {
                    bout.write(data1);
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
                String objectPath = "cs/" + u.getId() + "/" + img_path;

                // TransferManagerを利用
                TransferManager manager = new TransferManager(s3);

                // 分割サイズを設定
                TransferManagerConfiguration c = new TransferManagerConfiguration();
                c.setMinimumUploadPartSize(PART_SIZE);
                manager.setConfiguration(c);

                // メタデータに分割したデータのサイズを指定
                ObjectMetadata putMetaData = new ObjectMetadata();
                putMetaData.setContentLength(contentLength);

                // upload
                PutObjectRequest object = new PutObjectRequest("picdemo", objectPath, new ByteArrayInputStream(b),
                        putMetaData);
                s3.putObject(object);

            } else {
                p.setImgPath(null);
            }

            peForm.setToken(session.getId());

            mv.addObject("pc", peForm);
            pcrepository.save(p);
            session.setAttribute("flush", "登録しました");
            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト
            return mv;

        } else {

            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト
            session.setAttribute("error", "登録に失敗しました");

            return mv;

        }
    }

    @RequestMapping(path = "/cs/show", method = RequestMethod.GET)
    public ModelAndView csShow(@ModelAttribute PcEntityForm peForm, ModelAndView mv) {

        Optional<PcEntity> oppc = pcrepository.findById(peForm.getId());
        ModelMapper modelMapper = new ModelMapper();
        peForm = modelMapper.map(oppc.orElse(new PcEntity()), PcEntityForm.class);
        String pd = "--";
        if (peForm.getCon() != 0 && peForm.getSiz() != 0) {
            pd = peForm.getDb(peForm.getCon(), peForm.getSiz());
        }

        mv.addObject("pc", peForm);
        mv.addObject("pd", pd);
        mv.setViewName("views/charactersheet/show");

        //お気に入りの確認
        PcEntity pc = oppc.get();
        if (session.getAttribute("login_user") != null) {
            PcEntityLike p = pcLikerepository.findByUserAndPcEntity((User) session.getAttribute("login_user"), pc);
            if (p != null) {
                mv.addObject("like", p);
            }
        }

        return mv;

    }

    @RequestMapping(path = "/cs/edit", method = RequestMethod.GET)
    public ModelAndView csEdit(@ModelAttribute PcEntityForm peForm, ModelAndView mv) {

        Optional<PcEntity> p = pcrepository.findById(peForm.getId());

        //ModelMapperでEntity→Formオブジェクトへマッピングする。(変換する)
        ModelMapper modelMapper = new ModelMapper();
        peForm = modelMapper.map(p.orElse(new PcEntity()), PcEntityForm.class);
        peForm.setToken(session.getId());
        String pd = "--";
        if (peForm.getCon() != 0 && peForm.getSiz() != 0) {
            pd = peForm.getDb(peForm.getCon(), peForm.getSiz());
        }
        System.out.println(pd);
        mv.addObject("pc", peForm);
        mv.addObject("pd", pd);
        session.setAttribute("pcId", peForm.getId());
        mv.setViewName("views/charactersheet/edit");
        return mv;

    }

    @RequestMapping(path = "/cs/update", method = RequestMethod.POST)
    @Transactional
    public ModelAndView csUpdate(@ModelAttribute PcEntityForm peForm, ModelAndView mv) throws IOException {
        if (peForm.getToken() != null && peForm.getToken().equals(session.getId())) {
            Optional<PcEntity> opp = pcrepository.findById((Integer) session.getAttribute("pcId"));
            PcEntity p = opp.orElse(null);

            p.setName(peForm.getName());
            p.setNameRuby(peForm.getNameRuby());
            p.setReleaseFlag(peForm.getReleaseFlag());

            p.setStr(peForm.getStr());
            p.setCon(peForm.getCon());
            p.setDex(peForm.getDex());
            p.setSiz(peForm.getSiz());

            p.setAvoidanceAdd(peForm.getAvoidanceAdd());
            p.setKickAdd(peForm.getKickAdd());
            p.setFistAdd(peForm.getFistAdd());
            p.setHeadbuttAdd(peForm.getHeadbuttAdd());

            User u = (User) session.getAttribute("login_user");

            //ここから画像のパス作成------------------------------------

            //画像がアップロードされていたら更新。無かったら何もしない。
            if (!peForm.getCsImg().get(0).getOriginalFilename().equals("")
                    && peForm.getCsImg().get(0).getOriginalFilename() != null) {

                //登録されていた画像の削除(ローカル)
                File delete_images = new File( u.getId() + "/" + p.getImgPath());
                String deletePath = "cs/" + u.getId() + "/" + p.getImgPath();
                delete_images.delete();

                //ここからcreate時と同様の処理
                MultipartFile imgFile = peForm.getCsImg().get(peForm.getCsImg().size() - 1);

                String img_name = imgFile.getOriginalFilename();
                String extension = img_name.substring(img_name.lastIndexOf("."));//最後の.より右側の文字(拡張子)を取得
                String img_path = (int) (Math.floor(Math.random() * 1000000000)) + extension;
                List<PcEntity> pc = pcrepository.findByUserAndDeleteFlag(u, 0);
                //画像名が既存のものと被っていた場合変更する(変更後はもう一度確認する)
                for (int i = 0; i < pc.size(); i++) {
                    if (img_path.equals(pc.get(i).getImgPath())) {
                        img_path = "96" + img_path;
                        for (int n = 0; n < pc.size(); n++) {
                            if (img_path.equals(pc.get(n).getImgPath())) {
                                img_path = "96" + img_path;
                            }
                        }
                    }
                }
                p.setImgPath(img_path);

                //S3に画像アップロード。
                byte[] b = imgFile.getBytes();

                // ファイルを読み込み
                InputStream inputStream = new ByteArrayInputStream(b);
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                int data1;
                while ((data1 = inputStream.read()) != -1) {
                    bout.write(data1);
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
                String objectPath = "cs/" + u.getId() + "/" + img_path;

                // TransferManagerを利用
                TransferManager manager = new TransferManager(s3);

                // 分割サイズを設定
                TransferManagerConfiguration c = new TransferManagerConfiguration();
                c.setMinimumUploadPartSize(PART_SIZE);
                manager.setConfiguration(c);

                // メタデータに分割したデータのサイズを指定
                ObjectMetadata putMetaData = new ObjectMetadata();
                putMetaData.setContentLength(contentLength);

                // upload
                PutObjectRequest object = new PutObjectRequest("picdemo", objectPath, new ByteArrayInputStream(b),
                        putMetaData);
                s3.putObject(object);
                s3.deleteObject("picdemo", deletePath);

            }

            peForm.setToken(session.getId());

            mv.addObject("pc", peForm);
            pcrepository.save(p);
            session.setAttribute("flush", "更新しました");
            session.removeAttribute("pcId");
            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト
            return mv;

        } else {
            session.setAttribute("error", "更新に失敗しました");
            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト
            return mv;
        }
    }

    @RequestMapping(path = "/cs/destroy", method = RequestMethod.POST)
    @Transactional
    public ModelAndView csDestroy(@ModelAttribute PcEntityForm peForm, ModelAndView mv) {

        if (peForm.getToken() != null && peForm.getToken().equals(session.getId())) {

            Optional<PcEntity> opp = pcrepository.findById((Integer) session.getAttribute("pcId"));
            PcEntity p = opp.orElse(null);

            p.setDeleteFlag(1);

            //保存している画像の削除
            User u = (User) session.getAttribute("login_user");
            File delete_images = new File(securitydate.getImg_path() + u.getId() + "/" + p.getImgPath());
            delete_images.delete();

            pcrepository.save(p);
            session.removeAttribute("pcId");
            session.setAttribute("flush", "削除しました");
            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト

            return mv;
        } else {
            session.setAttribute("error", "削除に失敗しました");
            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト
            return mv;
        }
    }
}
