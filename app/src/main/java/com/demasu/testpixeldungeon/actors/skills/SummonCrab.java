package com.demasu.testpixeldungeon.actors.skills;


// --Commented out by Inspection START (8/28/18, 4:51 PM):
///**
// * Created by Moussa on 20-Jan-17.
// */
//public class SummonCrab extends ActiveSkill2 {
//
//
//    {
//        name = "Summon Crab";
//        castText = "Fight for me!";
//        tier = 2;
//        image = 42;
//        mana = 3;
//    }
//
//// --Commented out by Inspection START (8/28/18, 4:50 PM):
////    @Override
////    public ArrayList<String> actions(Hero hero) {
////        ArrayList<String> actions = new ArrayList<>();
////        if (level > 0 && hero.MP >= getManaCost())
////            actions.add(AC_SUMMON);
////        return actions;
////    }
//// --Commented out by Inspection STOP (8/28/18, 4:50 PM)
//
//// --Commented out by Inspection START (8/28/18, 4:50 PM):
////    @Override
////    public void execute(Hero hero, String action) {
////        if (Objects.equals(action, Skill.AC_SUMMON)) {
////            boolean spawned = false;
////            for (int nu = 0; nu < 1; nu++) {
////                int newPos = hero.pos;
////                if (Actor.findChar(newPos) != null) {
////                    ArrayList<Integer> candidates = new ArrayList<>();
////                    boolean[] passable = Level.passable;
////
////                    for (int n : Level.NEIGHBOURS4) {
////                        int c = hero.pos + n;
////                        if (c < 0 || c >= Level.passable.length)
////                            continue;
////                        if (passable[c] && Actor.findChar(c) == null) {
////                            candidates.add(c);
////                        }
////                    }
////                    if ( candidates.size() > 0 ) {
////                        if ( Random.element(candidates) != null ) {
////                            //noinspection ConstantConditions
////                            newPos = Random.element(candidates);
////                        }
////                    }
////                    else {
////                        newPos = -1;
////                    }
////                    if (newPos != -1) {
////                        spawned = true;
////                        SummonedPet crab = new SummonedPet(SummonedPet.PET_TYPES.CRAB);
////                        crab.spawn(level);
////                        crab.pos = newPos;
////                        GameScene.add(crab);
////                        Actor.addDelayed(new Pushing(crab, hero.pos, newPos), -1);
////                        crab.sprite.alpha(0);
////                        crab.sprite.parent.add(new AlphaTweener(crab.sprite, 1, 0.15f));
////                    }
////                }
////            }
////
////            if (spawned) {
////                hero.MP -= getManaCost();
////                StatusPane.manaDropping += getManaCost();
////                castTextYell();
////                hero.spend(TIME_TO_USE);
////                hero.busy();
////                hero.sprite.operate(hero.pos);
////            }
////            Dungeon.hero.heroSkills.lastUsed = this;
////        }
////    }
//// --Commented out by Inspection STOP (8/28/18, 4:50 PM)
//
//    @Override
//    public int getManaCost() {
//        return (int) Math.ceil(mana * (1 + 0.55 * level));
//    }
//
//// --Commented out by Inspection START (8/28/18, 4:50 PM):
////    @Override
////    protected boolean upgrade() {
////        return true;
////    }
//// --Commented out by Inspection STOP (8/28/18, 4:50 PM)
//
//
//// --Commented out by Inspection START (8/28/18, 4:50 PM):
////    @Override
////    public String info() {
////        return "Summons Crabs for your service.\n"
////                + costUpgradeInfo();
////    }
//// --Commented out by Inspection STOP (8/28/18, 4:50 PM)
//
//}
// --Commented out by Inspection STOP (8/28/18, 4:51 PM)
