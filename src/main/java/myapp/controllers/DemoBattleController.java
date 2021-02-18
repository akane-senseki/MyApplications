package myapp.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import myapp.demobattleItems.DemoBattle;
import myapp.forms.PcEntityForm;
import myapp.models.PcEntity;
import myapp.repositories.PcRepository;

@Controller
public class DemoBattleController {

    @Autowired
    HttpSession session;

    @Autowired
    PcRepository pcrepository;

    @RequestMapping(value = "/db/index", method = RequestMethod.GET)
    public ModelAndView db1vs1Select(ModelAndView mv) {
        List<PcEntity> p = pcrepository.findByDeleteFlagAndReleaseFlag(0, 0);
        mv.addObject("pc", p);
        mv.setViewName("views/demobattle/index");
        return mv;
    }


    @RequestMapping(value = "db/log", method = RequestMethod.POST)
    public ModelAndView dbLog(@RequestParam(name = "pc1") Integer p1, @RequestParam(name = "pc2") Integer p2, ModelAndView mv) {
        Optional<PcEntity> oppc1 = pcrepository.findById(p1);
        Optional<PcEntity> oppc2 = pcrepository.findById(p2);

        ModelMapper modelMapper1 = new ModelMapper();
        ModelMapper modelMapper2 = new ModelMapper();
        PcEntityForm pc1 = modelMapper1.map(oppc1.orElse(new PcEntity()), PcEntityForm.class);
        PcEntityForm pc2 = modelMapper2.map(oppc2.orElse(new PcEntity()), PcEntityForm.class);

        PcEntityForm first;  //先攻
        PcEntityForm latter; //後攻
        int roll;
        int round = 0;
        pc1.setHp((pc1.getCon() + pc1.getSiz())/2);
        pc2.setHp((pc2.getCon() + pc2.getSiz())/2);

        List<String> log = new ArrayList<String>();
        List<String> errorlog = new ArrayList<String>();

        if (pc1.getDex() == pc2.getDex()) {
            first = pc1;
            latter = pc2;
        } else if (pc1.getDex() > pc2.getDex()) {
            first = pc1;
            latter = pc2;
        } else {
            first = pc2;
            latter = pc1;
        }

        String firstDb = first.getDb(first.getStr(), first.getSiz());
        String latterDb = latter.getDb(latter.getStr(), latter.getSiz());

        //ここからログ作成-----------------------------------------------

        while (first.getHp() > 0 && latter.getHp() > 0 && round < 100) {
            log.add("▼"+first.getName() + "の行動");
            roll = (int) Math.ceil(Math.random() * 100);

            if (roll < 6) {   //クリティカルだった場合-------------------------------------
                log.add(first.getName() + " : " + "1D100<=" + first.attackSkill() +"【 " + first.attackName() +" 】" + "(1D100<=" + first.attackSkill() + ")＞ " + roll + " ＞ " +"<span style='color:#4682b4;'>クリティカル！</span>");
                log.add(latter.getName() + "は回避出来ない");

                //攻撃～気絶判定--------------------------------------------------------
                DemoBattle.attake(first, latter, firstDb , log);

            } else if (first.attackSkill() >= roll) { //攻撃通常成功----------------
                log.add(first.getName() + " : " + "1D100<=" + first.attackSkill() +"【 " + first.attackName() +" 】" + "(1D100<=" + first.attackSkill() + ")＞ " + roll + " ＞ " + "成功！");

                log.add("▽"+latter.getName() + "の回避");
                roll = (int) Math.ceil(Math.random() * 100);

                //回避
                if(roll < 6) {  //回避クリティカル
                    log.add(latter.getName()+ " : " + "1D100<=" + latter.getAvoidance() + "【 回避 】(1D100<=" + latter.getAvoidance() + ")＞ " + roll + " ＞ " + "<span style='color:#4682b4;'>クリティカル！</span>");
                    log.add("▽"+latter.getName() + "の反撃！");
                    //攻撃～気絶判定--------------------------------------------------------
                    DemoBattle.attake(latter, first, latterDb,log);
                }else if(roll > 95) { //回避ファンブル
                    log.add(latter.getName()+ " : " + "1D100<=" + latter.getAvoidance() + "【 回避 】(1D100<=" + latter.getAvoidance() + ")＞ " + roll + " ＞ " + "<span style='color:#dc143c;'>ファンブル！</span>");
                    log.add("▼"+first.getName() + "の連続攻撃！");
                    DemoBattle.attake(first, latter, firstDb,log);
                    DemoBattle.attake(first, latter, firstDb,log);
                } else if(latter.getAvoidance() >= roll) {
                    log.add(latter.getName()+ " : " + "1D100<=" + latter.getAvoidance() + "【 回避 】(1D100<=" + latter.getAvoidance() + ")＞ " + roll + " ＞ " + "回避成功！");
                } else {
                    log.add(latter.getName()+ " : " + "1D100<=" + latter.getAvoidance() + "【 回避 】(1D100<=" + latter.getAvoidance() + ")＞ " + roll + " ＞ " + "回避失敗");
                    //攻撃～気絶判定--------------------------------------------------------
                    DemoBattle.attake(first, latter, firstDb,log);
                }

            } else if (roll > 95) { //攻撃が96以上(ファンブル)の場合---------------------------------------
                log.add(first.getName() + " : " + "1D100<=" + first.attackSkill() +"【 " + first.attackName() +" 】" + "(1D100<=" + first.attackSkill() + ")＞ " + roll + " ＞ " + "<span style='color:#dc143c;'>ファンブル！</span>");
                log.add("▽"+latter.getName() + "の反撃！");
                //攻撃～気絶判定--------------------------------------------------------
                DemoBattle.attake(latter, first, latterDb,log);
            } else { //通常失敗
                log.add(first.getName() + " : " + "1D100<=" + first.attackSkill() +"【 " + first.attackName() +" 】" + "(1D100<=" + first.attackSkill() + ")＞ " + roll + " ＞ " + "<span>失敗</span>");
 }

            //後攻の攻撃へ----------------------------------------------------------
            if (first.getHp() > 0 && latter.getHp() > 0) {
                log.add("▼"+latter.getName() + "の行動");
                roll = (int) Math.ceil(Math.random() * 100);

                if (roll < 6) { //5以下で攻撃に成功した場合(クリティカルだった場合)-------------------------------------
                    log.add(latter.getName() + "1D100<=" + latter.attackSkill() +"【 " + latter.attackName() +" 】" + "(1D100<=" + latter.attackSkill() + ")＞ " + roll + " ＞ " + "<span style='color:#4682b4;'>クリティカル！</span>");
                    log.add(first.getName() + "は回避出来ない");

                    //攻撃～気絶判定--------------------------------------------------------
                    DemoBattle.attake(latter, first, latterDb,log);
                } else if (latter.attackSkill() >= roll) { //攻撃通常成功----------------
                    log.add(latter.getName() + "1D100<=" + latter.attackSkill() +"【 " + latter.attackName() +" 】" + "(1D100<=" + latter.attackSkill() + ")＞ " + roll + " ＞ " + "<span>成功！</span>");

                    log.add("▽"+first.getName() + "の回避");
                    roll = (int) Math.ceil(Math.random() * 100);

                    //回避
                    if(roll < 6) {  //回避クリティカル
                        log.add(first.getName()+ " : " + "1D100<=" + first.getAvoidance() + "【 回避 】(1D100<=" + first.getAvoidance() + ")＞ " + roll + " ＞ " + "<span style='color:#4682b4;'>クリティカル！</span>");
                        log.add("▽"+first.getName() + "の反撃！");
                        //攻撃～気絶判定--------------------------------------------------------
                        DemoBattle.attake(first, latter, firstDb,log);
                    }else if(roll > 95) { //回避ファンブル
                        log.add(first.getName()+ " : " + "1D100<=" + first.getAvoidance() + "【 回避 】(1D100<=" + first.getAvoidance() + ")＞ " + roll + " ＞ " + "<span style='color:#dc143c;'>ファンブル！</span>");
                        log.add("▼"+latter.getName() + "の連続攻撃！");
                        DemoBattle.attake(latter, first, latterDb,log);
                        DemoBattle.attake(latter, first, latterDb,log);
                    } else if(first.getAvoidance() >= roll) {
                        log.add(first.getName()+ " : " + "1D100<=" + first.getAvoidance() + "【 回避 】(1D100<=" + first.getAvoidance() + ")＞ " + roll + " ＞ " + "回避成功！");
                    } else {
                        log.add(first.getName()+ " : " + "1D100<=" + first.getAvoidance() + "【 回避 】(1D100<=" + first.getAvoidance() + ")＞ " + roll + " ＞ " + "回避失敗");
                        //攻撃～気絶判定--------------------------------------------------------
                        DemoBattle.attake(latter, first, latterDb,log);
                    }

                } else if (roll > 95) { //攻撃が96以上(ファンブル)の場合---------------------------------------
                    log.add(latter.getName() + "1D100<=" + latter.attackSkill() +"【 " + latter.attackName() +" 】" + "(1D100<=" + latter.attackSkill() + ")＞ " + roll + " ＞ " + "<span style='color:#dc143c;'>ファンブル！</span>");

                    log.add("▽"+first.getName() + "の反撃！");
                    //攻撃～気絶判定
                    DemoBattle.attake(first, latter, firstDb,log);
                } else { //通常失敗----------------------------------------------------------------
                    log.add(latter.getName() + "1D100<=" + latter.attackSkill() +"【 " + latter.attackName() +" 】" + "(1D100<=" + latter.attackSkill() + ")＞ " + roll + " ＞ " + "<span>失敗</span>");
                }

            }
            round++;
            log.add("&nbsp;");
            log.add("---------------" + round +"ラウンド目終了---------------");
            log.add("&nbsp;");
        }

        if(round < 100) {
            if (first.getHp() < 3) {
                log.add("勝者：" + latter.getName());
            } else {
                log.add("勝者：" + first.getName());
            }

            mv.addObject("log" , log);
        }else {
            errorlog.add("無限ループしている可能性があります");
            errorlog.add("技能値等の確認を行ってください");
            mv.addObject("log" , errorlog);
        }


        mv.setViewName("/views/demobattle/log");

        return mv;
    }

}
