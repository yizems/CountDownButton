# CountDownButton
倒计时按钮-每次做发送验证码之类的倒计时按钮都 要写半天,要么就是复制,偶尔还复制错误,这次直接封装起来,以后不用那么麻烦了

## 1 效果图

![image](https://github.com/yizeliang/CountDownButton/raw/master/img/1.png)
![image](https://github.com/yizeliang/CountDownButton/raw/master/img/2.png)
![image](https://github.com/yizeliang/CountDownButton/raw/master/img/3.png)
![image](https://github.com/yizeliang/CountDownButton/raw/master/img/4.png)
## 2 使用方法

- 和正常button一样使用,只不过文字和背景色不再生效
- 有四种状态,正常,准备,计时中,计时结束
- 点击事件的监听设置和平常Button一样
- 如果在计时中点击事件不会执行,只有正常和计时结束状态才会执行

```xml
 <cn.yzl.countdownbutton.CountDownButton
        android:layout_width="120dp"
        android:layout_height="50dp"
        app:end_text="重新发送"
        app:end_text_color="#228899"
        app:has_prepare="false"
        app:normal_bg="@drawable/bg_count_button_default"
        app:normal_text="发送"
        app:normal_text_color="#000000"
        app:prepare_bg="@drawable/bg_count_button_default"
        app:prepare_text="正在发送"
        app:prepare_text_color="#88ff11"
        app:space_time="1"
        app:max_count="10"
        app:timer_bg="@drawable/bg_count_button_aa"
        app:timer_text="重新获取(00)s"
        app:timer_text_color="#3F51B5" />
```
 - 如果有准备状态,准备结束后通过下面的方法可以开始计时
 - start() 开始计时
 - restart() 计时;
 - 添加进度监听 CountDownListener
 
## 3 属性说明

- has_prepare是否显示准备阶段,false点击直接进入计时
- max_count 可以理解为 从多少开始倒计时,单位秒
- space_time:多少秒更新一下UI
- timer_text:00代表着要替换的数字,结合效果图理解
- 其他的看名字就知道什么意思了

## 4 依赖

```gradle

//工程gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
//module
 dependencies {
	        compile 'com.github.yizeliang:CountDownButton:1.4'
	}

```
