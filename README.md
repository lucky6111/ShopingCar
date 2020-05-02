# ShoppingCart  購物商店功能測試
仿中國網友 github 提供購物商店原始碼 , 以 Android Studio 3.6 編寫成符合台灣的格式及提升版本

如下 :
1. android.support 更新到 AndroidX
2. 修改地址三級聯動PickerView符合台灣的市鎮鄉區街路格式
3. 修改成台灣手機格式 10碼 09開頭
4. 提升部分第三方API版本build.gradle
   
   //okhttp連線
   
   implementation project(':okhttputils') 目前版本 okhttp:4.2.0 (okhttp:3.9.0 以上才支援 https://)
   
    //黄油刀 
    
    implementation 'com.jakewharton:butterknife:10.2.0' 
    
    annotationProcessor 'com.jakewharton:butterknife-compiler:10.2.0 

    //沉浸式狀態欄 
    
    implementation 'com.readystatesoftware.systembartint:systembartint:1.0.3' 

    //三級聯動
    
    implementation 'com.contrarywind:Android-PickerView:3.2.6'
    
    implementation 'org.greenrobot:eventbus:3.1.1'

    //輪播圖
    
    implementation 'com.youth.banner:banner:1.4.10'
    
    implementation 'com.github.bumptech.glide:glide:4.7.1'
    
    implementation 'com.google.code.gson:gson:2.8.5'

    //recyclerview
    
    implementation "androidx.recyclerview:recyclerview:1.1.0"
    
    implementation "androidx.recyclerview:recyclerview-selection:1.1.0-rc01"

    //recyclerviewhelper   需另外加入 allprojects {repositories { maven { url "https://jitpack.io" }}}
    
    implementation 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.50'

    implementation 'androidx.cardview:cardview:1.0.0'

    //下拉刷新
    
    implementation 'com.cjj.materialrefeshlayout:library:1.3.0'

    //跑馬燈view
    
    implementation 'com.sunfusheng:marqueeview:1.3.3'

    //數據庫
    
    implementation 'org.greenrobot:greendao:3.2.2'
    
    implementation 'org.greenrobot:greendao-generator:3.2.2'
