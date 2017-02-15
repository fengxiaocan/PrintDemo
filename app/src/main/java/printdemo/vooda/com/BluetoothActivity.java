package printdemo.vooda.com;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.fxc.lib.pmsion.AfterPermissionGranted;
import com.fxc.lib.pmsion.EasyPermissions;

import java.util.List;

import static com.fxc.lib.utils.AppUtils.openSettingActivity;

/**
 *  @项目名： PrintDemo
 *  @包名： printdemo.vooda.com
 *  @创建者: Noah.冯
 *  @时间: 18:29
 *  @描述： TODO
 */
public class BluetoothActivity extends Activity implements EasyPermissions.PermissionCallbacks, ActivityCompat.OnRequestPermissionsResultCallback {
    private Context context = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setTitle("蓝牙打印");
        setContentView(R.layout.bluetooth_layout);
        this.initListener();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsSuccess(int requestCode, List<String> perms) {
        /*申请权限成功*/
        try {

        } catch (Exception e) {
            Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT)
                 .show();
            openSettingActivity(this);
        }
    }

    @Override
    public void onPermissionsDafeat(int requestCode, List<String> perms) {
        Toast.makeText(this, "请到设置界面打开权限设置", Toast.LENGTH_SHORT)
             .show();
         /*申请权限失败,跳到设置界面*/
        openSettingActivity(this);
    }

    /**
     * 请求打开二维码
     */
    @AfterPermissionGranted(101)
    private void requestPermissionToZxing() {
        String[] perms = {Manifest.permission.BLUETOOTH,

                          Manifest.permission.BLUETOOTH_ADMIN};
        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "申请使用蓝牙权限打印", 101, perms);
            //进入到这里代表没有权限.
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermissionToZxing();
    }

    private void initListener() {
        ListView unbondDevices = (ListView) this
                .findViewById(R.id.unbondDevices);
        ListView bondDevices   = (ListView) this.findViewById(R.id.bondDevices);
        Button   switchBT      = (Button) this.findViewById(R.id.openBluetooth_tb);
        Button   searchDevices = (Button) this.findViewById(R.id.searchDevices);

        BluetoothAction bluetoothAction = new BluetoothAction(this.context,
                                                              unbondDevices, bondDevices, switchBT, searchDevices,
                                                              BluetoothActivity.this);

        Button returnButton = (Button) this
                .findViewById(R.id.return_Bluetooth_btn);
        bluetoothAction.setSearchDevices(searchDevices);
        bluetoothAction.initView();

        switchBT.setOnClickListener(bluetoothAction);
        searchDevices.setOnClickListener(bluetoothAction);
        returnButton.setOnClickListener(bluetoothAction);
    }
    //屏蔽返回键的代码:
    public boolean onKeyDown(int keyCode,KeyEvent event)
    {
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
