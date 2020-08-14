package edu.buffalo.cse.cse486586.groupmessenger2;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import android.view.View.OnClickListener;

/**
 * GroupMessengerActivity is the main Activity for the assignment.
 *
 * @author stevko
 *
 */
public class GroupMessengerActivity extends Activity {
    static final String TAG = GroupMessengerActivity.class.getSimpleName();
    static final Uri URI = Uri.parse("content://edu.buffalo.cse.cse486586.groupmessenger2.provider");
    static final int[] REMOTE_PORT_ARRAY = new int[] {11108, 11112, 11116, 11120, 11124};
    static final int SERVER_PORT = 10000;
    static int count =0;
    static int lmid=0;
    int sseq=0;
    int seqq=0;
    static String myPort = "";
    HashMap<String,Integer> msgdict1 = new HashMap<String, Integer>();
    TreeMap<Integer,String> sortedMap = new TreeMap<Integer,String>();
    ArrayList<Integer> list = new ArrayList<Integer>();
    Set<String> hash_Set = new HashSet<String>();
    HashMap<String,Integer> msgdict2 = new HashMap<String, Integer>();
    int failureFlag = 0;
    Integer failurePort = 0;
    int Port=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messenger);
        /*
         * TODO: Use the TextView to display your messages. Though there is no grading component
         * on how you display the messages, if you implement it, it'll make your debugging easier.
         */
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());

        /*
         * Registers OnPTestClickListe````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  ner for "button1" in the layout, which is the "PTest" button.
         * OnPTestClickListener demonstrates how to access a ContentProvider.
         */
        findViewById(R.id.button1).setOnClickListener(
                new OnPTestClickListener(tv, getContentResolver()));
        TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        myPort = String.valueOf((Integer.parseInt(portStr) * 2));


        try {
            /*
             * Create a server socket as well as a thread (AsyncTask) that listens on the server
             * port.
             *
             * AsyncTask is a simplified thread construct that Android provides. Please make sure
             * you know how it works by reading
             * http://developer.android.com/reference/android/os/AsyncTask.html
             */
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
        } catch (IOException e) {


        }

        findViewById(R.id.button4).setOnClickListener( new OnClickListener(){
            @Override
            public void onClick(View v) {
                final EditText editText = (EditText) findViewById(R.id.editText1);
                String msg = editText.getText().toString() + "\n";
                editText.setText(""); // This is one way to reset the input box.
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg);


            }
        });
        /*
         * Please read http://developer.android.com/tools/debugging/debugging-projects.html
         * and http://developer.android.com/tools/debugging/debugging-log.html
         * for more information on debugging.
         */
        return;




        /*
         * TODO: You need to register and implement an OnClickListener for the "Send" button.
         * In your implementation you need to get the message from the input box (EditText)
         * and send it to other AVDs.
         */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
        return true;
    }

    private class ServerTask extends AsyncTask<ServerSocket, String, Void> {

        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];


            /*
             * TODO: Fill in your server code that receives messages and passes them
             * to onProgressUpdate().
             */
           // try {

                while(true) {
                    try {


                        Socket socket = serverSocket.accept();
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                        String gotmessage = "";
                        String myseqnum = "";
                        int max = 0;
                        int mypid = 0;

                        String check = "Final_proposal";
                        String smid = "";
                        String messagetosend = "";
                        gotmessage = input.readUTF();
                        Log.i(TAG, "Message!!!!!!! is : " + gotmessage);

                        String[] msg_array = gotmessage.split(",");
                        String message = msg_array[0];

                        Log.i(TAG, "Message is : " + gotmessage);
                        Log.i(TAG, "before entering fail if" + msgdict1);

                        if (message.equals(check)) {
                            Log.i(TAG, "inside agreed");
                            smid = msg_array[1] + "," + msg_array[2] + "," + msg_array[3];
                            Log.i(TAG, "put in server ---------1----- ");
                            Port = Integer.parseInt(msg_array[3]);
                            msgdict1.put(smid, Integer.parseInt(msg_array[4]) - 1);
                            Log.i(TAG, "inside agreed" + msgdict1);
                            list.clear();
                            for (Map.Entry<String, Integer> entry : msgdict1.entrySet()) {
                                list.add(entry.getValue());
                            }
                            Collections.sort(list);
                            sortedMap.clear();
                            for (int str1 : list) {
                                for (Entry<String, Integer> entry : msgdict1.entrySet()) {
                                    if ((entry.getValue() == str1) && !(hash_Set.contains(entry.getKey()))) {
                                        sortedMap.put(str1, entry.getKey());
                                    }
                                }
                            }
                            Log.i(TAG, "sortedMap be4 sync" + sortedMap);
                            Log.i(TAG, "before sync");

                            synchronized (sortedMap) {
                                while (!sortedMap.isEmpty()) {
                                    int key = 0;
                                    if (!sortedMap.isEmpty())
                                        key = sortedMap.firstKey();
                                    Log.i(TAG, "key" + key);
                                    String value = sortedMap.get(key);
                                    Log.i(TAG, "key" + key);
                                    String[] checker = value.split(",");
                                    int check_key = Integer.parseInt(checker[2]);

                                    Log.i(TAG, "checking port------------1----" + failurePort);

                                    if ((key != 0) && (key % 2 == 0) && check_key == failurePort) {
                                        sortedMap.remove(key);
                                        hash_Set.add(value);

                                    } else if (key % 2 == 0) {
                                        Log.i(TAG, "even num" + key);
                                        break;
                                    } else {
                                        Log.i(TAG, "odd num" + key);
                                        hash_Set.add(value);
                                        Log.i(TAG, "hash set size" + hash_Set.size());
                                        String[] val_array = value.split(",");
                                        Log.e(TAG, "before publish progress SortedMap" + sortedMap);
                                        sortedMap.remove(key);

                                        Log.e(TAG, "after publish progress SortedMap removed" + sortedMap);
                                        Log.e(TAG, "Value to publish" + val_array[1]);


                                        publishProgress(val_array[1]);
                                    }
                                }

                            }
                            output.writeUTF("no");
                        } else {
                            Log.i(TAG, "inside main");
                            String[] str_array = gotmessage.split(",");
                            Port = Integer.parseInt(str_array[3]);
                            smid = str_array[0] + "," + str_array[1];
                            if (msgdict1.size() != 0) {
                                for (Integer i : msgdict1.values()) {
                                    if (i >= max) {
                                        max = i;
                                    }
                                }
                            } else {
                                max = 0;
                            }
                            synchronized (this) {

                                seqq = max / 100000;
                                seqq = seqq + 1;
                            }
                            myseqnum = String.valueOf(seqq);
                            messagetosend = smid + "," + myseqnum;
                            smid = str_array[0] + "," + str_array[1] + "," + str_array[3];
                            mypid = Integer.parseInt(myseqnum + str_array[4]);
                            Log.i(TAG, " put in server 2 Main ---------");
                            msgdict1.put(smid, mypid);
                            Log.i(TAG, "3" + msgdict1);
                            output.writeUTF(messagetosend);
                            socket.close();
                        }
                    }


                    catch (UnknownHostException ne) {
                        Log.e(TAG, "ServerTask UnknownHostException"+ne.getMessage());
                        failureFlag = 1;
                        failurePort =Port;
                    } catch (IOException ke) {
                        Log.e(TAG, "ServerTask socket IOException"+ke.getMessage());
                        failureFlag = 1;
                        failurePort = Port;
                    }

                }
           // }


          //  return null;
        }


        protected void onProgressUpdate(String...strings) {
            /*
             * The following code displays what is received in doInBackground().
             */
            String strReceived = strings[0].trim();
            TextView remoteTextView = (TextView) findViewById(R.id.textView1);
            remoteTextView.append(strReceived + "\t\n");

            /*
             * The following code creates a file in the AVD's internal storage and stores a file.
             *
             * For more information on file I/O on Android, please take a look at
             * http://developer.android.com/training/basics/data-storage/files.html
             */

            ContentValues content = new ContentValues();
            content.put("key", count++);
            content.put("value",strReceived);
            Uri uri = getContentResolver().insert(URI, content);
            return;
        }
    }

    /***
     * ClientTask is an AsyncTask that should send a string over the network.
     * It is created by ClientTask.executeOnExecutor() call whenever OnKeyListener.onKey() detects
     * an enter key press event.
     *
     * @author stevko
     *
     */
    private class ClientTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... msgs) {
            int item=0;
            lmid=lmid+1;
            int countnum =0;
            String idmsg = "";
            for (item = 0 ;( item < REMOTE_PORT_ARRAY.length ) ; item ++) {
                try {
                    if (failureFlag == 1 && failurePort == REMOTE_PORT_ARRAY[item])
                        continue;
                    int remotePort = REMOTE_PORT_ARRAY[item];
                    int pronumport = 0;
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), remotePort);
                    socket.setSoTimeout(2500);
                    String msgToSend = lmid + "," + msgs[0] + "," + sseq + "," + myPort + ","  + remotePort;

                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    output.writeUTF(msgToSend);

                    String message = null;
                    DataInputStream inp = new DataInputStream(socket.getInputStream());
                    message = inp.readUTF();

                    String[] str_array1 = message.split(",");
                    idmsg = str_array1[0] + "," + str_array1[1] + "," + myPort;
                    pronumport = Integer.parseInt(str_array1[2] + remotePort);
                    countnum = countnum + 1;
                    if (countnum >= 2) {
                        if (pronumport >= msgdict2.get(idmsg)) {
                            Log.i(TAG, " put in client---------1-----"  );
                            msgdict2.put(idmsg, pronumport);
                        }
                    }  else {
                        Log.i(TAG, " put in client---------2-----"  );
                        msgdict2.put(idmsg, pronumport);
                    }
                    if (message.equals(msgToSend)) {
                        Log.i(TAG, message);
                        socket.close();
                    }
                } catch (UnknownHostException e) {
                    failureFlag = 1;
                    failurePort = REMOTE_PORT_ARRAY[item];
                    Log.e(TAG, "ClientTask UnknownHostException");
                } catch (IOException e ) {
                    failureFlag = 1;
                    failurePort = REMOTE_PORT_ARRAY[item];
                    Log.e(TAG, "ClientTask socket IOException yessssss-------------1----" + failurePort);


                }
            }
            Log.i(TAG,"after failure flag");
            String maximum = "";
            maximum =String.valueOf(msgdict2.get(idmsg));
            Log.i(TAG,"after failure flag1");
            for (item = 0 ;item < REMOTE_PORT_ARRAY.length; item ++) {
                try {
                    Log.i(TAG,"after failure flag2");
                    if(failureFlag == 1 && failurePort == REMOTE_PORT_ARRAY[item])
                        continue;
                    int remotePort =  REMOTE_PORT_ARRAY[item];
                    Log.i(TAG,"after failure flag3");
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), remotePort);
                    Log.i(TAG,"after failure flag4");
                    socket.setSoTimeout(2500);
                    String maxseqnum = idmsg+","+maximum;
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    Log.i(TAG,"after failure flag5");
                    String propmsgToSend = "Final_proposal" + "," + maxseqnum;
                    output.writeUTF(propmsgToSend);
                    String msg9 = "";
                    DataInputStream inp = new DataInputStream(socket.getInputStream());
                    msg9 = inp.readUTF();
                    if (msg9.equals(propmsgToSend)) {
                        Log.i(TAG, msg9);
                        socket.close();
                    }

                } catch (UnknownHostException e) {
                    failureFlag = 1;
                    failurePort = REMOTE_PORT_ARRAY[item];
                    Log.e(TAG, "ClientTask UnknownHostException");
                } catch (IOException e) {
                    failureFlag = 1;
                    failurePort = REMOTE_PORT_ARRAY[item];
                    Log.e(TAG, "ClientTask socket IOException YESSSSSSSS--------2-----" + failurePort);

                }
            }




            return null;
        }
    }
}
