package com.sfs.handler;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.sfs.SFSMainExtension;
import com.sfs.util.Util;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.util.TaskScheduler;

public class SocketHandler {
	
	private SFSMainExtension mainExtension;
	private ServerSocket serverSocket;
	private int listenPort;
	private ScheduledFuture<?> task;
	
	public SocketHandler(final SFSMainExtension mainExtension, final int listenPort) {
		this.mainExtension = mainExtension;
		this.serverSocket = null;
		this.listenPort = listenPort;
		
    	SmartFoxServer sfsServer = SmartFoxServer.getInstance();
        final TaskScheduler taskScheduler = sfsServer.getTaskScheduler();
        task = taskScheduler.scheduleAtFixedRate(new Runnable() {
        	public void run() {
        		handleSocketRequest();
    			task.cancel(false);
        	}
        }, 0, 100, TimeUnit.MILLISECONDS);
	}
	
	public void destroy() {
		if (serverSocket != null ) {
            try {
                serverSocket.close();
            } catch ( IOException e ) {
                Util.logException(e);
	        } finally {
	        	serverSocket = null;
	        }
		}
        Util.logDebug("SocketHandler::destroy");
	}
	
	public void sendHttpRequest(final String urlStr, final String sendData) {
        try {
        	Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        	while(e.hasMoreElements())
        	{
        	    NetworkInterface n = (NetworkInterface) e.nextElement();
        	    Enumeration<InetAddress> ee = n.getInetAddresses();
        	    while (ee.hasMoreElements())
        	    {
        	        InetAddress i = (InetAddress) ee.nextElement();
        	        Util.logDebug("HostAddress " + i.getHostName() + ": " + i.getHostAddress());
        	    }
        	}
        	
        	final String fullUrlStr = (sendData.length() > 0) ? urlStr + "?" + sendData : urlStr;
			final URL url = new URL(fullUrlStr);
			final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();  
	        httpURLConnection.setDoOutput(true);  
	        httpURLConnection.setDoInput(true);  
	        httpURLConnection.setRequestMethod("GET");         
	        httpURLConnection.setConnectTimeout(30*1000);  
	        httpURLConnection.setReadTimeout(30*1000);
	        httpURLConnection.setRequestProperty("Connection", "keep-alive");
	        httpURLConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
	        HttpURLConnection.setFollowRedirects(true); 
	        httpURLConnection.setInstanceFollowRedirects(true);
	        
	        // Add post data
        	DataOutputStream dos = null;
	        try {
	        	dos = new DataOutputStream(httpURLConnection.getOutputStream());
			} catch (IOException e1) {
				Util.logError("sendHttpRequestError");
				Util.logException(e1);
	        } finally {
	        	if (dos != null) 
	        		dos.close();
	        }
	        
			Util.logDebug("sendData " + sendData);
		} catch (IOException e2) {
			Util.logError("sendHttpRequestError");
			Util.logException(e2);
		}      
	}

	private void handleSocketRequest() {
        ExecutorService threadExecutor = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket( listenPort );
            Util.logDebug("Start listening requests at port " + listenPort + " ...");
            while ( true ) {
                Socket socket = serverSocket.accept();
                threadExecutor.execute( new RequestThread( socket ) );
            }
        } catch ( IOException e ) {
            Util.logException(e);
        } finally {
        	if ( threadExecutor != null )
                threadExecutor.shutdown();
            
            if ( serverSocket != null ) {
                try {
                    serverSocket.close();
                } catch ( IOException e ) {
                    Util.logException(e);
		        } finally {
		        	serverSocket = null;
		        }
            }
        }
        Util.logDebug("End of handleSocketRequest...");
    }
    
    class RequestThread implements Runnable {
        private Socket clientSocket;
        
        public RequestThread( Socket clientSocket ) {
            this.clientSocket = clientSocket;
        }
        
        @Override
        public void run() {
        	Util.logDebug("Create connect from " + clientSocket.getRemoteSocketAddress().toString());
            DataInputStream input = null;
            DataOutputStream output = null;
            BufferedReader reader = null;
            
            try {
                input = new DataInputStream( this.clientSocket.getInputStream() );
                reader = new BufferedReader(new InputStreamReader(input));
                output = new DataOutputStream( this.clientSocket.getOutputStream() );
                final String msg = reader.readLine();
                final StringTokenizer tokenizer = new StringTokenizer(msg);
                final String httpMethod = tokenizer.nextToken();
                final String httpQueryString = tokenizer.nextToken();
                Util.logDebug("httpMethod " + httpMethod + " httpQueryString " + httpQueryString);
                final int startIdx = httpQueryString.indexOf("?") + 1;
                final ISFSObject obj = SFSObject.newInstance();
                final String[] queryStrArray = httpQueryString.substring(startIdx).split("&");
                for (int i=0;i<queryStrArray.length;i++) {
                	final String[] params = queryStrArray[i].split("=");
                	final String key = params[0];
                	final String value = params[1];
                	obj.putUtfString(key, value);
            		Util.logDebug("key " + key + " value " + value);
                }
                mainExtension.handleSocketRequest(obj);
                
                while ( true ) {
                    output.writeUTF( String.format("Hi, %s!\n", clientSocket.getRemoteSocketAddress() ) );
                    output.flush();
                    break;
                }
            } catch ( Exception e ) {
                Util.logException(e);
            } finally {
                try {
                    if ( input != null )
                        input.close();
                    
                    if ( output != null )
                        output.close();
                    
                    if ( this.clientSocket != null && !this.clientSocket.isClosed() ) {
                        this.clientSocket.close();
                        Util.logDebug("Client socket closed!");
                    }
                } catch ( Exception e ) {
                    Util.logException(e);
                }
            }
        }
    }
}