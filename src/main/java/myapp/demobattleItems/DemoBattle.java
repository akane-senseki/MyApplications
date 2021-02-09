package myapp.demobattleItems;

import java.util.List;

import myapp.forms.PcEntityForm;

public class DemoBattle {
    static int damage;
    static int dbDamage;
    static int roll;
    static String beforeHp;
    static String afterHp;

    public static void attake(PcEntityForm attacker , PcEntityForm defender ,String attackerDb ,List<String> log) {

        damage = attacker.attack_roll();
        dbDamage = attacker.getDb_Roll();
        log.add(attacker.attack() + attackerDb + ":[" + damage + "," + dbDamage + "] >" + (damage + dbDamage));
        log.add("");
        beforeHp = defender.getHp()+"";
        defender.setHp(defender.getHp() - (damage + dbDamage));
        log.add(defender.getName() + "の体力");
        log.add(beforeHp + " → " + defender.getHp());
        log.add("");

        //気絶判定
        if(defender.getHp() < 3) {
            log.add(defender.getName()+"は気絶した……");
            log.add("");
            defender.setHp(0);
        }else if(Integer.parseInt(beforeHp) <= (damage + dbDamage)*2) {
            log.add(defender.getName()+"の気絶判定");
            log.add("CON*5:1D100<=" + defender.getCon()*5);
            roll = (int) Math.ceil(Math.random() * 100);
            log.add("1D100<="+roll);

            if(defender.getCon()*5 >= roll) {  //気絶回避----------------------
                log.add("CON*5成功！");
                log.add("");
            } else {
                log.add(defender.getName() + "は気絶した……");
                defender.setHp(0);
            }
        }else {
            log.add("");
        }
    }

}
