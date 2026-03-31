# AndroidDemo 项目功能点总结

这是一个包含 **20 个模块、350+ Activity、100+ 自定义 View、20+ Service** 的综合性 Android 学习/演示项目，覆盖了 Android 开发的方方面面。

---

## 1. APP 主模块

**包名**: `com.example.helloworld`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| 闹钟管理 | AlarmActivity | 定时闹钟调度与时间变更监听 |
| 时间变更监听 | TimeChangeReceiver | 系统时间变更广播接收 |
| 股票分析系统 | StockAnalysis 等 | 数据解析、回测、技术指标计算、预测算法、交易管理 |
| 自定义圆角 View | CircleCornerView | 圆角视图组件 |
| 资源生成工具 | GenerateValueFiles | 资源文件生成工具类 |

---

## 2. JUNIOR — 基础 UI 组件

**包名**: `com.example.junior`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| 像素/DP 转换 | PxActivity | 像素与 DP 单位转换演示 |
| 颜色使用 | ColorActivity | 颜色设置示例 |
| 屏幕信息 | ScreenActivity | 屏幕参数显示 |
| 边距演示 | MarginActivity | Margin/Padding 使用示例 |
| 重力布局 | GravityActivity | Layout Gravity 示例 |
| 滚动演示 | ScrollActivity | 滚动视图使用 |
| 跑马灯文字 | MarqueeActivity | 文字跑马灯效果 |
| BBS 文字 | BbsActivity | BBS 风格文字展示 |
| 截屏功能 | CaptureActivity | 屏幕截图 |
| 点击事件 | ClickActivity | 点击事件处理 |
| 图片缩放 | ScaleActivity | 图片缩放操作 |
| 图标管理 | IconActivity | 图标展示与管理 |
| 状态管理 | StateActivity | View 状态管理 |
| Shape 图形 | ShapeActivity | Shape Drawable 使用 |
| .9 图使用 | NineActivity | Nine-Patch 图片使用 |
| 计算器 | CalculatorActivity | 完整计算器实现 |

---

## 3. MIDDLE — 中级组件

**包名**: `com.example.middle`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| RelativeLayout | RelativeXmlActivity, RelativeCodeActivity | XML/代码两种方式使用 |
| FrameLayout | FrameActivity | 帧布局演示 |
| Checkbox | CheckboxActivity | 复选框使用 |
| Switch 开关 | SwitchDefaultActivity, SwitchIOSActivity | 默认/iOS 风格开关 |
| RadioButton | RadioHorizontalActivity, RadioVerticalActivity | 水平/垂直单选组 |
| Spinner 下拉 | SpinnerDropdownActivity, SpinnerDialogActivity, SpinnerIconActivity | 多种 Spinner 样式 |
| EditText 基础 | EditSimpleActivity | 基本文本输入 |
| 光标定制 | EditCursorActivity | 光标样式自定义 |
| 边框样式 | EditBorderActivity | 输入框边框定制 |
| 显示/隐藏 | EditHideActivity | 输入框显示隐藏 |
| 字段跳转 | EditJumpActivity | 多输入框跳转 |
| 自动补全 | EditAutoActivity | AutoCompleteTextView |
| Activity 跳转 | ActJumpActivity, ActNextActivity | 页面跳转与传值 |
| 旋转处理 | ActRotateActivity | 屏幕旋转状态保存 |
| Home 导航 | ActHomeActivity | 返回主页处理 |
| URI 处理 | ActUriActivity | Intent URI 解析 |
| 请求/响应 | ActRequestActivity, ActResponseActivity | startActivityForResult |
| 对话框 | AlertActivity | AlertDialog 使用 |
| 文本校验 | TextCheckActivity | 输入合法性验证 |
| 房贷计算器 | MortgageActivity | 房贷月供计算 |
| 登录系统 | LoginMainActivity, LoginForgetActivity | 登录 + 密码找回 |

---

## 4. SENIOR — 高级组件

**包名**: `com.example.senior`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| 日期选择器 | DatePickerActivity | 日期选择 |
| 时间选择器 | TimePickerActivity | 时间选择 |
| 月份选择器 | MonthPickerActivity | 自定义月份选择 |
| 完整日历 | CalendarActivity, CalendarFragment | 含农历、节气的日历系统 |
| BaseAdapter | BaseAdapterActivity | 适配器使用示例 |
| ListView | ListViewActivity | 列表视图 |
| GridView | GridViewActivity | 网格视图 |
| ViewPager | ViewPagerActivity | 基础 ViewPager |
| Tab 指示器 | PagerTabStripActivity, PagerTitleStripActivity | 页签指示器 |
| 静态 Fragment | FragmentStaticActivity | 静态 Fragment 使用 |
| 动态 Fragment | FragmentDynamicActivity | 动态 Fragment 管理 |
| 启动页 | LaunchSimpleActivity, LaunchImproveActivity | 闪屏页实现 |
| 广播接收器 | BroadTempActivity, BroadSystemActivity | 自定义/系统广播 |
| 闹钟管理 | AlarmActivity | 闹钟调度 |
| 振动控制 | VibratorActivity | 振动模式设置 |
| 购物车系统 | ShoppingCartActivity, ShoppingChannelActivity, ShoppingDetailActivity | 含 SQLite 持久化的购物系统 |
| 日程管理 | ScheduleActivity, ScheduleDetailActivity | 事件日程 + 闹钟提醒 |

---

## 5. CUSTOM — 自定义 View 与服务

**包名**: `com.example.custom`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| 自定义属性 | CustomPropertyActivity | 自定义 View 属性设置 |
| 文本测量 | MeasureTextActivity | 文本测量 API |
| 布局测量 | MeasureLayoutActivity | 布局测量 API |
| onMeasure 过程 | OnMeasureActivity1/2/3 | 测量流程演示 |
| onLayout 过程 | OnLayoutActivity | 布局流程演示 |
| onDraw 绘制 | ShowDrawActivity | Canvas 绘制演示 |
| 自定义 TextView | CustomTextView | 自定义文本渲染 |
| 二阶贝塞尔曲线 | BezierQuadActivity | 二次贝塞尔绘制 |
| 三阶贝塞尔曲线 | BezierCubicActivity | 三次贝塞尔绘制 |
| 自定义贝塞尔 | BezierCustomActivity | 自定义贝塞尔曲线 |
| 水波纹效果 | BezierRippleActivity | 贝塞尔水波纹 |
| 圆形动画 | CircleAnimationActivity | 圆形路径动画 |
| 动画时钟 | CircleClockActivity | 时钟动画效果 |
| 涟漪效果 | RippleAnimActivity | 涟漪扩散动画 |
| 圆角图片 | RoundImageViewActivity | 3 种圆角实现 |
| 增强图片 View | NiceImageView | 功能增强的 ImageView |
| 基础通知 | NotifySimpleActivity | 简单通知发送 |
| 计数通知 | NotifyCounterActivity | 带计数的通知 |
| 进度通知 | NotifyProgressActivity | 带进度条的通知 |
| 自定义通知 | NotifyCustomActivity | 自定义通知布局 |
| 服务通知 | NotifyServiceActivity | 基于 Service 的通知 |
| 普通服务 | ServiceNormalActivity, NormalService | startService 使用 |
| 绑定服务 | BindImmediateActivity, BindDelayActivity | bindService 使用 |
| 音乐服务 | MusicService | 后台音乐播放 |
| 流量监控 | TrafficInfoActivity, TrafficService | 网络流量统计 |
| 应用信息 | AppInfoActivity | 应用包信息展示 |
| 手机助手 | MobileAssistantActivity, MobileConfigActivity | 系统管理功能 |
| 窗口管理 | WindowActivity | Window 参数设置 |
| 自定义对话框 | DialogDateActivity, DialogMultiActivity | 定制化对话框 |
| 下拉刷新 | PullRefreshActivity | 下拉刷新组件 |

---

## 6. STORAGE — 数据持久化

**包名**: `com.example.storage`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| SP 写入 | ShareWriteActivity | SharedPreferences 写 |
| SP 读取 | ShareReadActivity | SharedPreferences 读 |
| SP 登录 | LoginShareActivity | 基于 SP 的登录 |
| SQLite 概览 | DatabaseActivity | 数据库总览 |
| SQLite 写入 | SQLiteWriteActivity | 数据库插入/更新 |
| SQLite 读取 | SQLiteReadActivity | 数据库查询 |
| SQLite 登录 | LoginSQLiteActivity | 基于 SQLite 的登录 |
| 文件基础 | FileBasicActivity | 基本文件操作 |
| 文件路径 | FilePathActivity | 文件路径管理 |
| 文本读写 | TextWriteActivity, TextReadActivity | 文本文件 I/O |
| 图片读写 | ImageWriteActivity, ImageReadActivity | 图片文件 I/O |
| ContentProvider | ContentProviderActivity, UserInfoProvider | 内容提供器实现 |
| ContentResolver | ContentResolverActivity | 内容解析器使用 |
| ContentObserver | ContentObserverActivity | 内容观察者使用 |
| 应用数据读写 | AppWriteActivity, AppReadActivity | Application 级存储 |
| 选项菜单 | MenuOptionActivity | Options Menu |
| 上下文菜单 | MenuContextActivity | Context Menu |
| 购物系统 | ShoppingCartActivity 等 | 含持久化的购物系统 |
| 通讯工具 | CommunicationUtil | 联系人/短信/通话记录访问 |

---

## 7. DEVICE — 硬件与传感器

**包名**: `com.example.device`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| 相机信息 | CameraInfoActivity | 摄像头参数信息 |
| 拍照 | PhotographActivity, TakePictureActivity | 照片拍摄 |
| 录像 | ShootingActivity, TakeShootingActivity | 视频录制 |
| Camera/Camera2 | CameraView, Camera2View | 两代相机 API |
| 旋转显示 | TurnViewActivity, TurnSurfaceActivity, TurnTextureActivity | 3 种旋转方案 |
| 音量控制 | VolumeActivity | 系统音量调节 |
| 音频录放 | AudioActivity, AudioPlayer, AudioRecorder | 录音与播放 |
| 视频播放 | VideoActivity, VideoPlayer, VideoRecorder | 视频录制与播放 |
| 加速度传感器 | AccelerationActivity | 加速度检测 |
| 光线传感器 | LightActivity | 环境光感应 |
| 方向传感器 | DirectionActivity, CompassView | 指南针/方向 |
| 计步器 | StepActivity | 步数统计 |
| 陀螺仪 | GyroscopeActivity | 陀螺仪数据 |
| GPS 定位 | LocationSettingActivity, LocationActivity | 位置获取 |
| 导航 | NavigationActivity | 导航功能 |
| NFC 读卡 | NfcActivity | 公交卡等 NFC 卡片读取 |
| 红外遥控 | InfraredActivity | 红外发射控制 |
| 蓝牙 | BluetoothActivity, BluetoothUtil | 蓝牙连接管理 |
| 仿微信 UI | WeChatActivity 等 | 微信主界面复刻 |
| 二维码扫描 | FindScanActivity | ZXing 扫码集成 |
| 摇一摇 | FindShakeActivity | 摇动手势检测 |
| 崩溃处理 | CrashHandlerActivity | 全局异常捕获 |
| 屏幕适配 | AutoSizeActivity | 自动尺寸适配 |

---

## 8. NETWORK — 网络通信

**包名**: `com.example.network`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| HTTP 请求 | HttpRequestActivity, HttpRequestUtil | 基础 HTTP 请求 |
| 图片下载 | HttpImageActivity | 网络图片加载 |
| APK 下载 | DownloadApkActivity | APK 文件下载 |
| 图片下载 | DownloadImageActivity | 图片文件下载 |
| 文件上传 | UploadHttpActivity, HttpUploadUtil | HTTP 文件上传 |
| JSON 解析 | JsonParseActivity | JSON 数据解析 |
| JSON 转换 | JsonConvertActivity | 对象与 JSON 互转 |
| 网络状态 | ConnectActivity | 网络连接检测 |
| IP 解析 | NetAddressActivity | IP 地址获取 |
| 进度对话框 | ProgressDialogActivity | 加载进度弹窗 |
| 文字进度 | ProgressTextActivity | 文字进度条 |
| 圆形进度 | ProgressCircleActivity | 圆形进度条 |
| AsyncTask | AsyncTaskActivity | 异步任务使用 |
| IntentService | IntentServiceActivity, AsyncService | 后台服务 |
| Socket 通信 | SocketActivity | TCP/UDP Socket |
| 聊天应用 | ChatMainActivity, MessageActivity | 即时通讯 |
| 文件保存 | FileSaveActivity | 文件保存对话框 |
| 文件选择 | FileSelectActivity | 文件选择对话框 |
| APK 信息 | ApkInfoActivity | 安装包信息 |
| 应用商店 | AppStoreActivity, AutoInstallService | 应用管理+自动安装 |
| 仿 QQ 应用 | QQLoginActivity, QQChatActivity 等 | QQ 界面复刻（登录/聊天/关注/联系人/发现） |
| 可折叠列表 | FoldListActivity | ExpandableListView |

---

## 9. EVENT — 事件处理与触摸

**包名**: `com.example.event`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| 软键盘 | KeySoftActivity | 软键盘控制 |
| 硬件按键 | KeyHardActivity | 物理键事件 |
| 音量键 | VolumeSetActivity | 音量键拦截 |
| 手势识别 | GestureDetectorActivity | GestureDetector 使用 |
| 单指触摸 | TouchSingleActivity | 单点触控 |
| 多指触摸 | TouchMultipleActivity | 多点触控 |
| 手写签名 | SignatureActivity | 手写签名板 |
| 事件分发 | EventDispatchActivity | 事件分发机制 |
| 事件拦截 | EventInterceptActivity | 事件拦截机制 |
| ViewFlipper | ViewFlipperActivity | 视图翻页 |
| Banner 轮播 | BannerFlipperActivity | 广告轮播 |
| 自定义滚动 | CustomScrollActivity | 自定义 Scroller |
| 禁止滚动 | DisallowScrollActivity | 滚动冲突处理 |
| 下拉刷新 | PullRefreshActivity | 下拉刷新组件 |
| 抽屉导航 | DrawerLayoutActivity | 侧滑抽屉 |
| 图片变换 | ImageChangeActivity | 图片矩阵变换 |
| 图片裁剪 | ImageCutActivity, CropImageView | 图片裁剪功能 |
| 美图滤镜 | MeituActivity, MeituView | 美颜滤镜效果 |
| OpenGL 线条 | GlLineActivity | OpenGL ES 线绘制 |
| 3D 地球 | GlGlobeActivity | OpenGL 地球渲染 |
| 全景查看 | GlPanoramaActivity, PanoramaView | 全景图查看器 |

---

## 10. ANIMATION — 动画

**包名**: `com.example.animation`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| 补间动画 | TweenAnimActivity | Tween 动画基础 |
| 摇摆动画 | SwingAnimActivity | 摆动效果 |
| 动画集合 | AnimSetActivity | AnimationSet 使用 |
| 渐变动画 | FadeAnimActivity | 淡入淡出 |
| 帧动画 | FrameAnimActivity | 逐帧动画 |
| GIF 播放 | GifActivity, GifView | GIF 图片播放 |
| 属性动画 | ObjectAnimActivity | ObjectAnimator |
| 分组动画 | ObjectGroupActivity | AnimatorSet |
| XML 动画 | ObjectXmlActivity | XML 定义属性动画 |
| 插值器 | InterpolatorActivity | 各类插值器演示 |
| 矢量图 | VectorDrawableActivity | VectorDrawable 使用 |
| 微笑动画 | VectorSmileActivity | 矢量动画示例 |
| 打勾动画 | VectorHookActivity | 矢量路径动画 |
| 支付成功 | PaySuccessActivity | 支付完成动效 |
| 饼图动画 | PieActivity | 饼图绘制动画 |
| 马赛克 | MosaicActivity | 马赛克效果 |
| 快门效果 | ShutterActivity | 相机快门动效 |
| 微信动画 | WeixinAnimActivity | 仿微信交互动画 |
| Banner 动画 | BannerAnimActivity | 轮播动画 |
| 展开/收起 | ExpandActivity | 展开折叠动画 |
| Scroller | ScrollerActivity | Scroller 使用 |
| 滚动文字 | ScrollTextView | 文字滚动组件 |

---

## 11. MEDIA — 多媒体

**包名**: `com.example.media`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| 图片画廊 | GalleryActivity | Gallery 组件 |
| 图片切换 | ImageSwitcherActivity | ImageSwitcher |
| CardView | CardViewActivity | 卡片式布局 |
| 调色板 | PaletteActivity | Palette 颜色提取 |
| RecyclerView 图片 | RecyclerViewActivity | 图片列表 |
| 铃声选择 | RingtoneActivity | 系统铃声选取 |
| 音效池 | SoundPoolActivity | SoundPool 短音效 |
| AudioTrack | AudioTrackActivity | 低级音频播放 |
| 音乐播放器 | MusicPlayerActivity, MusicDetailActivity | 完整音乐播放 |
| 歌词加载 | LyricsLoader | LRC 歌词解析 |
| 音频控制器 | AudioController | 自定义播放控件 |
| 视频播放 | VideoViewActivity | VideoView 基础 |
| 自定义控制器 | VideoControllerActivity, CustomControllerActivity | 视频控制面板 |
| 电影播放器 | MoviePlayerActivity, MovieDetailActivity | 完整视频播放应用 |
| 分屏模式 | SplitScreenActivity | 多窗口支持 |
| 画中画 | PicInPicActivity | PiP 模式 |
| 屏幕截图 | ScreenCaptureActivity, CaptureService | 截屏功能 |
| 屏幕录制 | ScreenRecordActivity, RecordService | 录屏功能 |
| 悬浮窗 | FloatWindowActivity, FloatWindow | 系统悬浮窗 |
| 富文本 | Spannable1Activity, Spannable2Activity | Spannable 文本 |

---

## 12. PERFORMANCE — 性能优化

**包名**: `com.example.performance`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| Include 布局 | IncludeOneActivity, IncludeTwoActivity | 布局复用 |
| 屏幕适配 | ScreenSuitableActivity | 多分辨率适配 |
| 窗口样式 | WindowStyleActivity | 窗口定制 |
| 强引用 | ReferStrongActivity | 强引用管理 |
| 弱引用 | ReferWeakActivity | 弱引用防泄漏 |
| LruCache | LruCacheActivity | 内存缓存策略 |
| 图片缓存 | ImageCacheActivity | 图片三级缓存 |
| 线程池 | ThreadPoolActivity | 线程池管理 |
| 定时任务 | SchedulePoolActivity | ScheduledExecutorService |
| 电池信息 | BatteryInfoActivity | 电池状态监控 |
| 省电模式 | PowerSavingActivity | 电量优化 |
| Doze 处理 | AlarmIdleActivity | Doze 模式闹钟 |
| 任务管理 | RemoveTaskActivity | 后台任务清理 |
| 服务注销 | LogoutServiceActivity | Service 生命周期 |

---

## 13. WEIXIN — 微信 SDK 集成

**包名**: `net.sourceforge.simcpux` / `com.example.weixin`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| 微信分享 | ShareWXActivity, ShareGridDialog | 分享到微信好友/朋友圈 |
| 微信支付 | WxpayActivity, WXPayEntryActivity | 微信支付集成 |
| 微信授权 | WXEntryActivity, GetAccessTokenTask | 微信登录授权 |
| QQ 集成 | - | QQ 登录/分享 |

---

## 14. MIXTURE — 混合技术

**包名**: `com.example.mixture`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| 文本 Assets | AssetsTextActivity | Assets 文本资源 |
| 图片 Assets | AssetsImageActivity | Assets 图片资源 |
| 本地 HTML | WebLocalActivity | 加载本地网页 |
| HTML Span | WebSpanActivity | HTML 富文本 |
| 网页浏览器 | WebBrowserActivity | WebView 浏览器 |
| JS 交互 | WebScriptActivity | Java 与 JS 互调 |
| JNI CPU 信息 | JniCpuActivity | NDK 获取 CPU 信息 |
| JNI 加密 | JniSecretActivity | NDK 加密实现 |
| WiFi 信息 | WifiInfoActivity | WiFi 状态信息 |
| WiFi 连接 | WifiConnectActivity | WiFi 连接管理 |
| WiFi 列表 | WifiList2Activity | 扫描 WiFi 列表 |
| WiFi 热点 | WifiApActivity | 创建 WiFi 热点 |
| WiFi 分享 | WifiShareActivity | WiFi 密码分享 |
| 蓝牙文件传输 | BluetoothTransActivity | 蓝牙文件收发 |
| NetBIOS 发现 | NetbiosActivity | 局域网设备发现 |
| PDF 渲染 | PdfRenderActivity | PDF 文件显示 |
| PDF 滑动 | PdfSliderActivity | PDF 滑动浏览 |
| PDF 翻页 | PdfTurnActivity | 仿真翻页效果 |
| eBook 阅读 | EbookReaderActivity | 电子书阅读器 |
| EPUB 阅读 | EpubActivity | EPUB 格式支持 |
| 多格式文档 | VudroidActivity | 多格式文档查看 |
| 翻页效果 | CurlView | 卷页翻书效果 |

---

## 15. GROUP — UI 组件与三方库

**包名**: `com.example.group`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| Tab 按钮 | TabButtonActivity | 自定义 Tab 按钮 |
| TabHost | TabHostActivity | TabHost 使用 |
| Tab + Fragment | TabFragmentActivity | Fragment 选项卡 |
| TabLayout | TabLayoutActivity | Material TabLayout |
| 自定义 Tab | TabCustomActivity | 定制选项卡 |
| Toolbar + PopWindow | ToolbarAndPopWindowActivity | 工具栏 + 弹窗 |
| 自定义 Toolbar | ToolbarCustomActivity | 工具栏定制 |
| 溢出菜单 | OverflowMenuActivity | Overflow Menu |
| SearchView | SearchViewActivity, SearchResultActivity | 搜索功能 |
| Banner 指示器 | BannerIndicatorActivity, LoopViewPager | 自动轮播 Banner |
| RecyclerView 线性 | RecyclerLinearActivity | LinearLayoutManager |
| RecyclerView 网格 | RecyclerGridActivity | GridLayoutManager |
| RecyclerView 组合 | RecyclerCombineActivity | 混合布局 |
| RecyclerView 拖拽 | RecyclerDragActivity | 拖拽排序 |
| RecyclerView 瀑布流 | RecyclerStaggeredActivity | StaggeredGridLayout |
| RecyclerView 动态 | RecyclerDynamicActivity | 动态增删 |
| CoordinatorLayout | CoordinatorActivity | 协调布局 |
| AppBar + RecyclerView | AppbarRecyclerActivity | AppBar 联动 |
| AppBar + NestedScroll | AppbarNestedActivity | 嵌套滚动 |
| 固定折叠 | CollapsePinActivity | Pin 模式折叠 |
| 视差折叠 | CollapseParallaxActivity | Parallax 效果 |
| 滚动标志 | ScrollFlagActivity, ScrollAlipayActivity | Scroll Flags |
| 图片渐变 | ImageFadeActivity | 图片过渡效果 |
| 点击效果 | ClickEffectActivity | Material 点击波纹 |
| 百货商城 | DepartmentStoreActivity 等 | 电商首页+分类+购物车 |
| 大图加载 | BigPictureActivity | 超大图片加载 |
| Lifecycle | LifecycleActivity | 生命周期感知 |
| LiveData | LiveDataActivity | 数据观察 |
| Transformations | LiveDataTransformationsActivity | 数据变换 |
| Timber 日志 | TimberActivity | Timber 日志框架 |
| MMKV 存储 | MMKVActivity | 高性能 KV 存储 |
| Log4j | Log4jActivity | Log4j 集成 |
| Retrofit+OkHttp+RxJava | RxJavaRetrofitOkHttpActivity | 网络框架组合 |
| ARouter 路由 | ARouterActivity | 组件化路由导航 |
| View 拖动 | ViewDragActivity1/2 | ViewDragHelper |
| 窗口对话框 | WindowDialogActivity | 窗口级对话框 |
| 本地广播 | LocalBroadcastActivity | LocalBroadcastManager |
| 屏幕方向 | OrientationActivity | 方向切换处理 |

---

## 16. CUSTOMVIEW — 高级自定义 View

**包名**: `com.example.customview`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| 贝塞尔波浪 | WaveByBezierActivity, WaveViewByBezier | 贝塞尔曲线波浪 |
| 正弦波浪 | WaveBySinCosActivity, WaveViewBySinCos | 三角函数波浪 |
| 气泡视图 | BubbleViewActivity | 气泡动画 |
| 拖拽球 | DragBallActivity | 可拖拽粘性球 |
| 自定义进度条 | ProgressBarActivity | 定制进度条 |
| Loading 动画 | LoadingViewActivity | 加载动画集合 |
| Mac 风格加载 | LoadingViewMacActivity | macOS 风格加载 |
| 多种加载 View | LVCircular, LVBattery, LVGears 等 | 多种加载动画组件 |
| 雷达扫描 | RadarActivity, RadarWaveView | 雷达扫描效果 |
| 支付密码框 | PayPsdViewActivity | 密码输入键盘 |
| 仿支付宝首页 | AliPayHomeActivity | 支付宝界面复刻 |
| 自定义 Banner | BannerActivity, BannerView | 轮播组件 |
| 动画按钮 | AnimationBtnActivity, AnimationButton | 按钮动效 |
| View 动画 | AnimationViewActivity | View 动画效果 |
| 揭露动画 | RevealAnimationActivity | Reveal 效果 |
| 加载按钮 | LoadingButton | 带加载状态的按钮 |
| RecyclerView 吸顶 | HoverItemActivity/2/3 | Sticky Header 效果 |
| 仿 QQ 列表 | RecyclerQQActivity | QQ 消息列表样式 |
| 指纹认证 | FingerprintActivity | 生物识别验证 |
| 标签布局 | TagActivity | Tag 标签展示 |
| 流式布局 | FlowActivity, FlowLayout | 自动换行布局 |
| 文字切换 | TextSwitcherActivity, TextSwitcherView | 文字切换动效 |
| 侧边索引栏 | IndexView | 字母索引条 |

---

## 17. UTIL — 工具库

**包名**: `com.example.util`

60+ 工具类，覆盖以下领域：

| 类别 | 工具类 | 说明 |
|------|--------|------|
| Activity 管理 | ActivityManagerUtil, ActivityUtil | Activity 栈管理 |
| 应用信息 | AppInfoUtil, AppUtils | 包名/版本等信息 |
| 图片处理 | BitmapFillet, BlurUtils | 圆角/模糊处理 |
| 日期时间 | DateUtils, TimeUtils | 日期格式化/计算 |
| 屏幕密度 | DensityUtil, ScreenUtils | DP/PX/屏幕参数 |
| 文件操作 | FileUtils, SystemFileUtils | 文件读写/路径管理 |
| 图片加载 | GlideUtil, FrescoUtil | Glide/Fresco 封装 |
| 键盘控制 | KeyboardUtil | 软键盘显示/隐藏 |
| 日志 | LogUtil | 日志工具 |
| 加密 | MD5Util | MD5 摘要 |
| 网络 | NetworkUtils, IpUtil | 网络状态/IP 工具 |
| 权限 | PermissionsUtil | 运行时权限管理 |
| SharedPreferences | SPUtils, PreferenceUtils | SP 封装 |
| 字符串 | StringUtils, StringBitmap | 字符串处理/转换 |
| Toast | ToastUtil | Toast 封装 |
| Socket | TcpSocket, UdpSocket | TCP/UDP 通信 |
| 压缩 | ZipUtil | 文件压缩/解压 |
| 视频 | VideoUtils | 视频处理 |

---

## 18. TEST — 加密与安全

**包名**: `com.example.test`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| AES 加密 | AesUtil | AES 对称加密 |
| 3DES 加密 | Des3Util | Triple DES 加密 |
| RSA 加密 | RSAUtil | RSA 非对称加密 |
| MD5 摘要 | MD5Util | MD5 哈希 |
| Base64 编码 | Base64Util | Base64 编解码 |
| SM3 摘要 | SM3 | 国密 SM3 哈希算法 |
| 加密演示 | EncryptActivity | 各加密算法演示界面 |

---

## 19. FILEDIALOG — 文件对话框库

**包名**: `com.aqi00.lib.dialog`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| 文件保存 | FileSaveFragment | 文件保存对话框 |
| 文件选择 | FileSelectFragment | 文件选取对话框 |
| 提示对话框 | HintDialogFragment | 提示信息弹窗 |
| 目录工具 | DirUtil | 目录操作工具 |
| 图片工具 | BitmapUtil | 图片处理工具 |

---

## 20. CUSTOMTOAST — 自定义 Toast 库

**包名**: `com.example.customtoast`

| 功能点 | 关键类 | 说明 |
|--------|--------|------|
| Toast 入口 | TastyToast | 自定义 Toast 主类 |
| 默认风格 | DefaultToastView | 默认样式 Toast |
| 成功提示 | SuccessToastView | 成功状态 Toast |
| 错误提示 | ErrorToastView | 错误状态 Toast |
| 信息提示 | InfoToastView | 信息状态 Toast |
| 警告提示 | WarningToastView | 警告状态 Toast |
| 特效提示 | ConfusingToastView | 特殊效果 Toast |

---

## 总计统计

| 指标 | 数量 |
|------|------|
| 模块数 | 20 |
| Activity 数 | 350+ |
| 自定义 View 数 | 100+ |
| Service 数 | 20+ |
| 工具类数 | 60+ |

## 技术领域覆盖

- **基础 UI**: 布局、控件、样式、主题
- **高级 UI**: 自定义 View、Material Design、CoordinatorLayout
- **数据持久化**: SharedPreferences、SQLite、文件、ContentProvider
- **网络通信**: HTTP、Socket、JSON、文件上传下载
- **多媒体**: 音频、视频、图片、录屏、画中画
- **动画**: 补间动画、帧动画、属性动画、矢量动画
- **硬件传感器**: 相机、GPS、NFC、蓝牙、红外、陀螺仪
- **事件处理**: 触摸、手势、事件分发拦截
- **性能优化**: 内存管理、线程池、电量优化
- **安全加密**: AES、RSA、3DES、SM3、MD5
- **三方集成**: 微信SDK、Retrofit、OkHttp、RxJava、Glide、ARouter、MMKV
- **原生开发**: JNI/NDK、OpenGL ES
- **文档阅读**: PDF、EPUB、多格式文档
