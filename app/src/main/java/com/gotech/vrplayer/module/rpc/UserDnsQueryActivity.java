package com.gotech.vrplayer.module.rpc;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.utils.GRPCTaskWrapper;
import com.socks.library.KLog;

import HelloWorld.HelloWorldGrpc;
import HelloWorld.HelloWorldProto;
import io.grpc.ManagedChannel;

public class UserDnsQueryActivity extends AppCompatActivity {
    private Button mSendButton;
    private EditText mHostEdit;
    private EditText mPortEdit;
    private EditText mMessageEdit;
    private TextView mResultText;
    private GRPCTaskWrapper.OnLoadListener mGRPCListener;
    private GRPCTaskWrapper<String, Void, HelloWorldProto.HelloReply> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helloworld);
        initGRPCListener();
        mSendButton = (Button)findViewById(R.id.send_button);
        mHostEdit = (EditText)findViewById(R.id.host_edit_text);
        mPortEdit = (EditText)findViewById(R.id.port_edit_text);
        mMessageEdit = (EditText)findViewById(R.id.message_edit_text);
        mResultText = (TextView)findViewById(R.id.grpc_response_text);
        mResultText.setMovementMethod(new ScrollingMovementMethod());
    }

    public void sendMessage(View view) {
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mHostEdit.getWindowToken(), 0);
        String host = mHostEdit.getText().toString();
        String message = mMessageEdit.getText().toString();
        String port = mPortEdit.getText().toString();
        // Task
        mTask = new GRPCTaskWrapper<>();
        mTask.setHost(host);
        mTask.setPort(port);
        mTask.setOnTaskListener(mGRPCListener);
        mTask.executeOnExecutor(GRPCTaskWrapper.THREAD_POOL_CACHED, message);
    }

    private void initGRPCListener() {
        mGRPCListener = new GRPCTaskWrapper.OnLoadListener<String, Void, HelloWorldProto.HelloReply>() {
            @Override
            public void onStart(Object taskTag) {
                KLog.i("onStart");
                mResultText.setText("");
                mSendButton.setEnabled(false);
            }

            @Override
            public void onResult(Object taskTag, HelloWorldProto.HelloReply helloReply) {
                KLog.i("onResult");
                if (helloReply != null) {
                    mResultText.setText(helloReply.getMessage());
                }
                mSendButton.setEnabled(true);
            }

            @Override
            public HelloWorldProto.HelloReply onWorkerThread(Object taskTag, ManagedChannel channel, String... params) {
                HelloWorldProto.HelloReply reply = null;
                try {
                    HelloWorldGrpc.HelloWorldBlockingStub stub = HelloWorldGrpc.newBlockingStub(channel);
                    HelloWorldProto.HelloRequest.Builder builder = HelloWorldProto.HelloRequest.newBuilder();
                    KLog.i("onWorkerThread message=" + params[0]);
                    builder.setName(params[0]);
                    builder.setAge("100");
                    builder.setZouhaiping("200");
                    HelloWorldProto.HelloRequest request = builder.build();
                    reply = stub.sayHello(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return reply;
            }
        };
    }
}
