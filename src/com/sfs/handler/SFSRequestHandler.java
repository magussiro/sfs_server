package com.sfs.handler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.sfs.player.SFSPlayer;
import com.sfs.util.Constants;
import com.sfs.util.Constants.SFS_COMMAND;
import com.sfs.util.Util;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.annotations.Instantiation;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.util.TaskScheduler;
import com.smartfoxserver.v2.util.executor.SmartThreadPoolExecutor;

/**
 * SFSRequestHandler
 * receive & handle request from client
 */
@Instantiation(Instantiation.InstantiationMode.SINGLE_INSTANCE)
public class SFSRequestHandler extends BaseClientRequestHandler {

    private LinkedBlockingQueue<TaskData> taskQueue;

    private final class TaskData {
        public TaskData(User sender, ISFSObject obj) {
            this.sender = sender;
            this.obj = obj;
        }

        public ISFSObject obj;
        public User sender;
    }

    ;

    public SFSRequestHandler() {
        SmartFoxServer sfsServer = SmartFoxServer.getInstance();
        final SmartThreadPoolExecutor sfsExecutor = (SmartThreadPoolExecutor) sfsServer.getEventManager().getThreadPool();
        final int maxThreadCount = sfsExecutor.getCoreThreads();
        Util.logInfo("Init SFSRequestHandler coreThreads " + maxThreadCount);

        taskQueue = new LinkedBlockingQueue<TaskData>();
        final TaskScheduler taskScheduler = sfsServer.getTaskScheduler();
        taskScheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    handleRequest();
                } catch (Exception e) {
                    Util.logException(e);
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);

        Util.logInfo("Init SFSRequestHandler successful!");
    }

    //====SFS COMMAND API FOR CLIENT====//
    public void jsonTest(User sender, ISFSObject params) {
        final ISFSObject resObj = SFSObject.newInstance();
        resObj.putUtfString(Constants.ACTION, SFS_COMMAND.JsonTest.toString());



        resObj.putSFSObject("input", params);
        sendMsgToClient(sender, resObj);
    }


    private void handleRequest() {
        if (taskQueue.isEmpty())
            return;

        SmartFoxServer sfsServer = SmartFoxServer.getInstance();
        final SmartThreadPoolExecutor sfsExecutor = (SmartThreadPoolExecutor) sfsServer.getEventManager().getThreadPool();
        final int maxThreadCount = sfsExecutor.getCoreThreads();
        final int activeCount = sfsExecutor.getActiveCount();
        Util.logInfo("activeCount " + activeCount + " / coreThreads " + maxThreadCount);
        if (activeCount >= maxThreadCount)
            return;

        final TaskData taskData = taskQueue.poll();
        sfsExecutor.submit(new TaskRunnable(taskData.sender, taskData.obj, this));
    }

    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        final String actionStr = params.getUtfString(Constants.ACTION);
        Util.logInfo("[SFSRequestHandler]handleClientRequest " + sender.getName() + " action " + actionStr + " params " + params.toJson());
        SFS_COMMAND action = SFS_COMMAND.valueOf(actionStr);
        switch (action) {
            case Ping:
                break;
            case JsonTest:
                jsonTest(sender, params);
                break;
            default:
                Util.logError("Unknown action " + actionStr);
                break;
        }
    }

    private void addQueueTask(User sender, ISFSObject params) {
        final TaskData taskData = new TaskData(sender, params);
        taskQueue.add(taskData);
    }

    public void sendMsgToClient(User sender, ISFSObject resObj) {
        // Send it back
        Util.logDebug("sendMsgToClient " + sender.getName() + " msg: " + resObj.toJson());
        send(Constants.SFS_EXTENSION, resObj, sender);
    }
}
