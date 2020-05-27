package com.example.myprofile_20200526;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myprofile_20200526.databinding.ActivityMainBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {
        binding.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoViewActivity.class);
                intent.putExtra("imgUrl","https://i.pinimg.com/564x/0b/2f/8a/0b2f8a51314ab1ebe0505aee843a33b1.jpg");
                startActivity(intent);
            }
        });
        binding.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PermissionListener pl = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
//                        실제 전화 걸기를 하도록 하는 코드
                        String phoneNum = binding.phoneNumTxt.getText().toString();
                        Uri myUri = Uri.parse(String.format("tel:%s",phoneNum));
                        Intent myIntent = new Intent(Intent.ACTION_CALL, myUri);
                        startActivity(myIntent);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
//                        최종 거부되었을 때 처리할 행동
                        Toast.makeText(mContext, "권한이 거부되어 통화가 불가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                };
//                실제 전화 걸기 => 권한 허가가 x, 앱이 강제 종료 => TedPermission 권한 획득 후 실행가능
                TedPermission.with(mContext)
                        .setPermissionListener(pl)
                        .setDeniedMessage("거부하면 통화가 불가능합니다.\n 설정에서 권한을 켜주세요.")
                        .setPermissions(Manifest.permission.CALL_PHONE)
                        .check();
//                만약 권한 x => 얼럿으로 허용할지?
//                권한 이전 허용 => 곧바로 granted 실행
            }
        });
    }

    @Override
    public void setValues() {
        Glide.with(mContext).load("https://i.pinimg.com/564x/0b/2f/8a/0b2f8a51314ab1ebe0505aee843a33b1.jpg").into(binding.profileImg);
    }
}
