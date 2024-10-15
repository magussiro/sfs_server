package com.sfs.handler;

import com.sfs.util.Constants;
import com.sfs.util.Constants.SFS_COMMAND;
import com.sfs.util.Util;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class TaskRunnable implements Runnable {
	
	private final SFSRequestHandler requestHandler;
	private final User sender;
	private final ISFSObject params;
	
	public TaskRunnable(final User sender, final ISFSObject params, final SFSRequestHandler requestHandler) {
        this.sender = sender;
        this.params = Util.copySFSObject(params);
        this.requestHandler = requestHandler;
	}

	@Override
	public void run() {
		try
		{
			handleRequest();
		} catch (RuntimeException e)
		{
			Util.logException(e);
		} catch (AssertionError e) {
			Util.logError("[AssertionError] " + e.getMessage());
		}
	}

	private void handleRequest() {
        final String actionStr = params.getUtfString(Constants.ACTION);
        Util.logInfo("[TaskRunnable]handleRequest " + sender.getName() + " action " + actionStr + " params " + params.toJson());
        SFS_COMMAND action = SFS_COMMAND.valueOf(actionStr);
        switch (action) {
    	default:
    		Util.logError("Unknown action " + actionStr);
    		break;
        }
        
        requestHandler.sendMsgToClient(sender, params);
	}
}
