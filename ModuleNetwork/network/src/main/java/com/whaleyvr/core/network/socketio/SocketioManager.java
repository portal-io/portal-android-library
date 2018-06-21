package com.whaleyvr.core.network.socketio;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;

/**
 * Created by dell on 2017/5/8.
 */

public class SocketioManager {

    private Socket socket;

    private IScocketio iScocketio;

    private String url;

    private IO.Options opts;

    public SocketioManager(){
        this(null);
    }

    public SocketioManager(String url){
        this(url, null);
    }

    public SocketioManager(String url, IScocketio iScocketio){
        this(url, iScocketio, null);
    }

    public SocketioManager(String url, IScocketio iScocketio, IO.Options opts){
        this.url = url;
        this.iScocketio = iScocketio;
        this.opts = opts;
    }

    public void initSocket() throws URISyntaxException {
        if(opts == null){
            opts = new IO.Options();
            opts.transports = new String[]{ WebSocket.NAME};
        }
        socket = IO.socket(url, opts);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                if(iScocketio!=null) {
                    iScocketio.onConnected(args);
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                if(iScocketio!=null) {
                    iScocketio.onDisconnected(args);
                }
            }
        });
    }

    public void connectSocket(){
        if(socket != null){
            socket.connect();
        }
    }

    public void closeSocket(){
        if(socket!=null){
            socket.off();
            socket.close();
        }
    }

    public boolean isConnected(){
        if(socket !=null)
            return socket.connected();
        else
            return false;
    }

    public void registerEvent(String event, EventListener listener){
        if(socket!=null){
            socket.on(event, listener);
        }
    }

    public void unRegisterEvent(String event, EventListener listener){
        if(socket!=null){
            socket.off(event, listener);
        }
    }

    public void unRegisterEvent(String event){
        if(socket!=null){
            socket.off(event);
        }
    }

    public void sendMsg(Object... args){
        if(socket!=null){
            socket.send(args);
        }
    }

    public void emitMsg(String event, Object... args){
        if(socket!=null){
            socket.emit(event, args);
        }
    }

    public void emitMsg(String event, Object[] args, EmitCallback emitCallback){
        if(socket!=null){
            socket.emit(event, args, emitCallback);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public IScocketio getIScocketio() {
        return iScocketio;
    }

    public void setIScocketio(IScocketio iScocketio) {
        this.iScocketio = iScocketio;
    }

}
