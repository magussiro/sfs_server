package com.sfs.tests;

import com.sfs.db.Connection;
import com.sfs.db.SessionFactory;
import com.sfs.db.jdo.GameMemberList;

/**
 * Created by Magus on 2017/4/13.
 */


public class TestHib {
    //測試DB連線 可以修改hibernateLocal.cfg.xml連線到自己的DB來測試 要有game_member_list table


    public static void main(String[] args) {


        SessionFactory.initializeLocal();
        Connection.initLocal();
        //   System.out.println(123);
        GameMemberList gameMemberList = Connection.getClassByKeySingle(GameMemberList.class, "gmlId", 1);

        System.out.println(gameMemberList);



        /*
        requestHandler.getAllianceHashManager().addMembersId(157,"aaa");
        //requestHandler.getAllianceHashManager().addMembersId(157,"aaa");
        HashMap<String,TexasPokerPlayer> AllianceMap = requestHandler.getAllianceHashManager().getName(157);

        System.out.println(AllianceMap.get("aaa"));





      //  AllianceMember
        /*
//        if (gameMemberList != null)
//            System.out.println(gameMemberList.toString());
        ArrayList<Integer> alIds = Connection.getAllAllianceIds();
        System.out.println(alIds);
        String MemberID = "123";
        AllianceHashManager testhash = new AllianceHashManager();


if(alIds!=null)

    for (int i=0;i<alIds.size();i++){


    }


          final  FakeTaxasPokerPlayer player = new FakeTaxasPokerPlayer();
          player.setAlliancdId(543);
          final  FakeTaxasPokerPlayer player2 = new FakeTaxasPokerPlayer();
          player2.setAlliancdId(413);
          final  FakeTaxasPokerPlayer player3 = new FakeTaxasPokerPlayer();
          player2.setAlliancdId(452);





      //  final TexasPokerPlayer player = requestHandler.getPlayerByName("aaa");

*/
/*
ArrayList<String> strs = new ArrayList<String>();
strs.add("1");
strs.add("2");
 strs.add("1");
        strs.add("3");
        strs.add("2");
        strs.add("4");

        strs.remove("2");
        strs.remove("2");
        System.out.println(strs);

        HashSet h = new HashSet(strs);
        strs.clear();
        strs.addAll(h);
        System.out.println(strs);
*/

    }

    public void getPlayer() {

    }


}
