package com.sfs.event;

import com.sfs.SFSMainExtension;
import com.sfs.util.Util;
import com.smartfoxserver.v2.annotations.Instantiation;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

/**
 * Handle client logout event 
 */
@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class LogoutEventHandler extends BaseServerEventHandler {

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
        final User user = (User) event.getParameter(SFSEventParam.USER);
        final String userName = user.getName();
		final SFSMainExtension sfsMainExtension = (SFSMainExtension) getParentExtension();
		sfsMainExtension.removePlayerByUser(user);
        Util.logInfo("Logout Success " + userName);
	}
}
