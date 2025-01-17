package com.sfs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sfs.db.Connection;
import com.sfs.db.SessionFactory;
import com.sfs.db.jdo.GameMemberList;
import com.sfs.event.LoginEventHandler;
import com.sfs.event.LogoutEventHandler;
import com.sfs.handler.SFSRequestHandler;
import com.sfs.player.SFSPlayer;
import com.sfs.util.Constants;
import com.sfs.util.Util;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;

/**
 * SFSMainExtension
 * Smartfox核心類別，一個遊戲(專案)只會建立一個
 */
public class SFSMainExtension extends SFSExtension
{
    /**
     * 儲存玩家的容器，所有登入上線的玩家都會儲存於此，索引值為玩家帳號(登入名稱)
     */
    private Map<String, SFSPlayer> playerMap;

    @Override
    public void init()
    {
        Util.logInfo("==========================================================");
        Util.logInfo("========== SFSMainExtension initialize start ==========");
        Util.logInfo("==========================================================");
        
        SessionFactory.initialize();
        Connection.init("game_member_list");
        
        initHandler();
        
        Util.logInfo("==========================================================");
        Util.logInfo("=========== SFSMainExtension initialize end ===========");
        Util.logInfo("==========================================================");
    }
    
    @Override
    public void destroy()
    {
        super.destroy();

        SessionFactory.closeSessionFactory();

        // remove StarPoker Handler
        removeRequestHandler(Constants.SFS_EXTENSION);

        // remove SFSEvent Handler
        removeEventHandler(SFSEventType.USER_LOGIN);
        removeEventHandler(SFSEventType.USER_DISCONNECT);
        
        Util.logInfo("SFSMainExtension destroy");
    } 
    
    /**
     * 初始化，註冊監聽事件
     */
    private void initHandler() {
        playerMap = new HashMap<String, SFSPlayer>();
        
        // 測試Code，從DB撈出所有玩家資訊，並且輸出總數量
        final List<?> list = Connection.getClass(GameMemberList.class);
        Util.logDebug("Member count " + list.size());

        // Add a new client request Handler
        addRequestHandler(Constants.SFS_EXTENSION, new SFSRequestHandler());

        // Add a new SFSEvent Handler
        addEventHandler(SFSEventType.USER_LOGIN, new LoginEventHandler());
        addEventHandler(SFSEventType.USER_DISCONNECT, new LogoutEventHandler());
    }
    
    /**
     * 監聽經由HTTP Request傳送來的訊息，並且由json格式轉換為ISFSObject
     * @param ISFSObject obj
     */
    public void handleSocketRequest(final ISFSObject obj) {
    }
    
    /**
     * 藉由登入帳號取得玩家實體，若找不到就建立新的
     * @param user
     * @return
     */
    public SFSPlayer getPlayerByUser(final User user) {
        final String userName = user.getName();
        if (playerMap.containsKey(userName) == true) {
            return playerMap.get(userName);
        }
        
        final SFSPlayer player = new SFSPlayer();
        player.initial(user);
        playerMap.put(userName, player);
        return player;
    }
    
    /**
     * 玩家離線，從容器中移除玩家
     * @param user
     * @return
     */
    public SFSPlayer removePlayerByUser(final User user) {
        final String userName = user.getName();
        if (playerMap.containsKey(userName) == false) {
            return null;
        }
        
        final SFSPlayer player = playerMap.remove(userName);
        player.destroy();
        return player;
    }
}
