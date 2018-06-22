package com.whaley.lib.sample;import android.util.Log;import java.io.BufferedReader;import java.io.BufferedWriter;import java.io.IOException;import java.io.InputStreamReader;import java.io.OutputStreamWriter;import java.net.HttpURLConnection;import java.net.MalformedURLException;import java.net.ProtocolException;import java.net.URL;public class HttpUtil {    private static void setHttpCommonParams(HttpURLConnection conn){    conn.setReadTimeout(30000);    conn.setConnectTimeout(30000);    conn.setDoInput(true);    conn.setDoOutput(true);    conn.setUseCaches(false);    conn.setRequestProperty("Connection", "Keep-Alive");    conn.setRequestProperty("Accept-Charset", "UTF-8");    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  }    /**   * HTTP POST通信   *    * @param server_url 服务器URL   * @param message 报文   * @return   */  public static String post(String server_url,String message){    BufferedWriter bw=null;    BufferedReader br=null;    HttpURLConnection conn=null;        String temp=null;    StringBuffer resp=new StringBuffer();    try {      URL url=new URL(server_url);      conn=(HttpURLConnection)url.openConnection();      setHttpCommonParams(conn);      conn.setRequestMethod("POST");      conn.connect();      bw=new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));      bw.write(message);      bw.flush();      if (conn.getResponseCode()==HttpURLConnection.HTTP_OK) {        br=new BufferedReader(new InputStreamReader(conn.getInputStream()));        while ((temp=br.readLine())!=null) {          resp.append(temp);        }      }else{        Log.i("网络异常", "响应码:"+conn.getResponseCode());        conn.disconnect();        return null;      }    } catch (MalformedURLException e) {      Log.e("net error", "网络地址解析错误");      return null;    } catch (ProtocolException e) {      Log.e("net error", "HTTP方法名设置错误");      return null;    } catch (IOException e) {      Log.e("net error", "网络输出流打开失败");      return null;    }finally{      bw=null;      br=null;      conn.disconnect();    }        return resp.toString();  }    }