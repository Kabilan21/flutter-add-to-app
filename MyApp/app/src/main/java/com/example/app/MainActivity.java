package com.example.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.flutter.embedding.android.FlutterFragment;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_FLUTTER_FRAGMENT = "flutter_fragment";
    private FlutterFragment flutterFragment;
    private FlutterEngine flutterEngine;
    private EventChannel.EventSink eventSink;
    private static final String METHOD_CHANNEL = "com.example.example/methods";
    private static final String EVENT_CHANNEL = "com.example.example/events";
    private  SharedViewModel sharedViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flutterEngine = new FlutterEngine(this);
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );
        FlutterEngineCache
                .getInstance()
                .put("my_app", flutterEngine);
        navigateToFragment();
        setupEventChannel();
        setupMethodChannel();
        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        sharedViewModel.setIndex(1);
        setupSharedNavigation();

    }

    private void setupSharedNavigation(){
            sharedViewModel.getIndex().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if(integer == 0){
                        navigateToFragment();
                        Log.d("Change","Change in data = 0");
                    }
                    Log.d("Change","Change in data");

                }
            });
    }

    private void navigateToNative() {
        Fragment nativeFragment = getSupportFragmentManager().findFragmentByTag("native_fragment");
        if (nativeFragment == null) {
            nativeFragment = new NativeFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_container, nativeFragment, "native_fragment").commit();
    }

    void navigateToFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        flutterFragment = (FlutterFragment) fragmentManager.findFragmentByTag(TAG_FLUTTER_FRAGMENT);
        if (flutterFragment == null) {
            flutterFragment = FlutterFragment.withCachedEngine("my_app")
                    .shouldAttachEngineToActivity(true)
                    .build();
            fragmentManager
                    .beginTransaction()
                    .add(
                            R.id.nav_container,
                            flutterFragment,
                            TAG_FLUTTER_FRAGMENT
                    )
                    .commit();
        }
    }

    private void setupEventChannel() {
        new EventChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), EVENT_CHANNEL).setStreamHandler(new EventChannel.StreamHandler() {
            @Override
            public void onListen(Object arguments, EventChannel.EventSink events) {
                eventSink = events;
            }

            @Override
            public void onCancel(Object arguments) {
                eventSink = null;
            }
        });
    }

    private void setupMethodChannel() {
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), METHOD_CHANNEL).setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
                if (call.method.equals("navigate_to_native")) {
                    Log.d("FLUTTER", "Moving to native");
                    Fragment nativeFragment = getSupportFragmentManager().findFragmentByTag("native_fragment");
                    if (nativeFragment == null) {
                        nativeFragment = new NativeFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_container, nativeFragment, "native_fragment").commit();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        flutterFragment.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onPostResume() {
        super.onPostResume();
        flutterFragment.onPostResume();
    }


    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        flutterFragment.onNewIntent(intent);
    }


    @Override
    public void onBackPressed() {
        if (flutterFragment.isVisible()) {
            flutterFragment.onBackPressed();

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        flutterFragment.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );
    }

    @Override
    public void onUserLeaveHint() {
        flutterFragment.onUserLeaveHint();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        flutterFragment.onTrimMemory(level);
    }
}