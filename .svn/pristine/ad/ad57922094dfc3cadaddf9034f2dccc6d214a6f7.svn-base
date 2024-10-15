package com.sfs.event;

import com.sfs.util.Util;
import com.smartfoxserver.v2.annotations.Instantiation;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

/**
 * Handle client login event
 * 玩家登入遊戲後第一個觸發的事件摱
 */
@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class LoginEventHandler extends BaseServerEventHandler
{
	@Override
    public void handleServerEvent(ISFSEvent event) throws SFSException
    {
		final String userName = (String) event.getParameter(SFSEventParam.LOGIN_NAME);
		final User oldUser = this.getApi().getUserByName(userName);
		// 處理重複登入的玩家，採取後(登入)踢前(登入)的機制
		if (oldUser != null) {
			this.getApi().disconnectUser(oldUser);
			Util.logWarn("Duplicate login " + userName);
		}
        Util.logInfo("Login Success " + userName);
	}
}