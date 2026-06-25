# Kotlin 核心知识点速查文档

本文档面向 Java 代码迁移 Kotlin 的学习和查阅。全书按「先能看懂 → 再能写对 → 再能安全迁移 → 最后实战」的学习路线组织，共 14 章，分四个阶段。

## 目录

**阶段一：基础语法（先能看懂）**

| 章 | 内容 |
| --- | --- |
| [1](#1-kotlin-与-java-核心区别) | 核心区别速查、类型推断、特殊符号、Math |
| [2](#2-变量类型字符串与集合) | val/var、集合、字符串、数字、Pair |
| [3](#3-空安全) | `?.` `!!` `?:`、可空位置、泛型可空性、平台类型 |
| [4](#4-控制流区间与异常) | `when`、区间、智能转换、异常处理 |

**阶段二：写出 Kotlin 风格（函数与对象）**

| 章 | 内容 |
| --- | --- |
| [5](#5-函数lambda-与扩展) | 函数、Lambda、高阶函数、扩展 |
| [6](#6-作用域函数与标准库工具) | `apply/let/run…`、`takeIf`、`Result` |
| [7](#7-类继承构造) | 构造、继承、枚举、密封类、泛型 |
| [8](#8-属性lateinitlazy) | 属性、延迟初始化、委托 |
| [9](#9-object接口伴生对象与数据类) | `object`、接口、`companion`、`data class` |

**阶段三：与 Java 协同（安全迁移）**

| 章 | 内容 |
| --- | --- |
| [10](#10-java-互操作与安全迁移) | JVM 注解、混编签名、易错点、编译错误排错 |
| [11](#11-kotlin-进阶实用能力) | 委托、`reified`、值类、K2、KTX、反射 |

**阶段四：Android 异步与工程化**

| 章 | 内容 |
| --- | --- |
| [12](#12-协程基础与结构化并发) | `suspend`、作用域、结构化并发、异常处理、共享状态竞态 |
| [13](#13-flowstateflow-与-android-ui-状态) | 三种流、UI 状态、生命周期 |
| [14](#14-测试代码风格与练习闭环) | 测试、代码审查、练习项目 |

**按问题查找**（Java 开发者高频痛点）：

| 你遇到的问题 | 去哪一节 |
| --- | --- |
| 第一次写 Kotlin 就遇到一堆编译错误 | [10.10](#1010-java-开发者常见编译错误排错速查) |
| `Int` 不能直接当 `Long` 用 | [2.4](#24-数字类型和显式转换) |
| 分不清 `List` 和 `MutableList` | [2.2](#22-可变集合和只读集合) |
| 平台类型、Java 传来的 null 导致崩溃 | [10.5](#105-平台类型-t)、[3](#3-空安全) |
| 分不清 `List<String>?` 和 `List<String?>` | [3.4](#34-可空性写在什么位置类型本身-vs-泛型参数) |
| 冷流被重复收集、网络请求发两遍 | [13.1](#131-三种类型怎么选) |
| 协程里 `runCatching` 吞掉取消异常 | [6.7](#67-作用域函数之外的标准库工具) |
| `launch` 的异常 try/catch 抓不到、`async` 异常何时抛 | [12.5](#125-协程异常处理launch-与-async-不一样) |
| 多个协程同时改变量、`counter++` 丢数据 | [12.6](#126-共享状态与阻塞调用两个进阶坑) |
| 阻塞 IO 取消不掉、占满线程池 | [12.6](#126-共享状态与阻塞调用两个进阶坑) |
| "只读 List"和"真正不可变集合"的区别 | [2.2](#22-可变集合和只读集合)（只读≠不可变的提示框） |
| `@JvmField` / `@JvmStatic` 怎么用 | [10.1](#101-java-互操作注解速查表) |
| 公共 API 发布前要给 Java 验证什么 | [10.9](#109-java-互操作的常见签名问题) |
| ViewBinding / `@Parcelize` / `bundleOf` | [11.10](#1110-android-常用-ktx-与模型类速查) |

> 目录链接在支持 Markdown 锚点的阅读器中可直接跳转；纯文本环境按章节号搜索定位即可。

## 先跑起来：3 分钟看懂一个完整例子

如果你已经会 Java，学 Kotlin 最快的办法不是从头背语法，而是**先看一个能跑的完整例子，再逐个搞懂里面的符号**。把下面这段代码贴进 Android Studio 的任意 `.kt` 文件（或用 Kotlin 在线 Playground）就能运行：

```kotlin
data class User(val name: String, val age: Int)   // 一行定义一个数据类，自动生成 getter/equals/toString

fun main() {
    val user = User("小明", 18)                   // 创建对象不用 new
    println("姓名：${user.name}，年龄：${user.age}") // $ 和 ${} 把变量拼进字符串

    val group = when {                             // when 像 switch，但能直接返回值
        user.age < 18 -> "未成年"
        user.age < 60 -> "成年人"
        else           -> "老年人"
    }
    println(group)

    val nickname: String? = null                   // 类型后加 ? 表示可能是 null
    println("昵称长度：${nickname?.length ?: 0}")    // ?. 安全调用，?: 给默认值
}
```

运行结果是：

```
姓名：小明，年龄：18
成年人
昵称长度：0
```

同样的逻辑，用 Java 写大概是这样——对照着看，你会发现 Kotlin 省掉的主要是样板代码：

```java
public final class User {                          // 需要手写构造器、getter、equals……
    private final String name;
    private final int age;
    public User(String name, int age) { this.name = name; this.age = age; }
    public String getName() { return name; }
    public int getAge() { return age; }
}

User user = new User("小明", 18);                  // Kotlin 去掉了 new
System.out.println("姓名：" + user.getName() + "，年龄：" + user.getAge());

String group;                                      // when 对应 switch，但要自己拼返回值
if (user.getAge() < 18) group = "未成年";
else if (user.getAge() < 60) group = "成年人";
else group = "老年人";

String nickname = null;                            // Java 任何引用都可能是 null，编译器不提醒
int len = (nickname != null) ? nickname.length() : 0;
```

光看这一屏，其实已经藏着 Kotlin 最核心的 5 个特点：**没有 `new`**、`val`/`var` 代替 `final`/普通变量、`$` 拼字符串、`when` 能返回值、`?` 把 null 写进类型。下面这张表帮你把刚才那段代码里的符号"认个脸熟"，后面各章会逐个展开：

| 符号 | 念作 / 记住 | 一句话作用 |
| --- | --- | --- |
| `val` / `var` | "瓦尔" / "瓦" | 不可重新赋值 / 可以重新赋值的变量 |
| `data class` | "数据类" | 一行搞定 Java 里要写几十行的 POJO |
| `${}` / `$` | "字符串模板" | 把变量或表达式的结果拼进字符串 |
| `when` | "当" | 加强版 switch，而且本身是一个表达式（有返回值） |
| `->` | "箭头" | "左边是条件/参数，右边是结果" |
| `?` | "可空" | 跟在类型后面，表示这个值可能是 `null` |
| `?.` | "安全调用" | 不为空才调用，为空就跳过，不崩溃 |
| `?:` | "默认值" | 左边是 null 就用右边顶上 |

> 第一遍读不懂每个符号没关系。你的目标只是"知道代码大概在干嘛、能照着改"。真正搞懂它们，是后面 14 章的任务。

## 阅读方法：Java 开发者的 Kotlin 成长路线

不要从第一页开始死记所有语法。建议按下面阶段推进，每个阶段都写一小段能运行的代码。

> **完全新手的最短路径**：先照着上一节把那个完整例子跑通 → 读第 1 章建立整体印象 → 重点吃透第 2、3 章（变量集合 + 空安全，日常 80% 时间都在用这两章）→ 再按下面的阶段表逐步推进。协程、Flow 那些可以等真正写 Android 异步时再学，不用一开始啃。

| 阶段 | 先学什么 | 对应章节 | 学会的判断标准 |
| --- | --- | --- | --- |
| 1. 看懂语法 | `val/var`、类型、字符串、空安全、`when/for`、函数 | 1～5 | 能读懂简单 Kotlin 类，不再逐行翻译成 Java |
| 2. 写出 Kotlin 风格 | 集合操作、Lambda、扩展、属性、作用域函数 | 2、5、6、8、11 | 能用清晰的 `map/filter/let`，不乱嵌套 |
| 3. 建立对象模型 | 构造、继承、接口、`object`、`data/sealed`、泛型 | 7、9、11 | 能按业务语义选普通类、数据类、接口、密封类型 |
| 4. 安全迁移 Java | 平台类型、Java 集合、异常、JVM 注解 | 4、10 | 混编时能主动检查 null、可变性和调用签名 |
| 5. Android 异步开发 | 协程、生命周期、`Flow/StateFlow` | 12、13 | 不阻塞主线程，能正确启动、取消、收集异步任务 |
| 6. 工程化成长 | 测试、代码风格、重构和练习项目 | 14 | 每次迁移都有测试或可验证结果 |

建议的学习循环：

1. 先写出与 Java 逻辑等价的 Kotlin。
2. 为关键分支补一个测试或可重复验证的输入。
3. 再用空安全、集合函数、数据类等 Kotlin 能力重构。
4. 比较重构前后的行为，确认没有偷偷改变 null、相等、异常或线程语义。
5. 最后才追求简洁；看不懂的「一行流」应拆回有名字的局部变量。

> 学习目标不是「代码越短越像 Kotlin」，而是利用类型系统让错误更早暴露，同时保持代码容易阅读和验证。

## 1. Kotlin 与 Java 核心区别

> **先说人话**：Kotlin 是 Java 的"语法升级版"，跑在同样的 JVM 上，能和 Java 代码互相调用。它多做的事主要是——帮你少写样板代码、把 `null` 和可变性写进类型里让编译器帮你检查。你熟悉的 Java 概念几乎都还在，只是换了个写法。下面这张表先让你对"写法差异"有整体印象，看不懂的条目后面都有单独展开。

### 1.1 核心区别速查表

| 主题 | Java | Kotlin | 记忆点 |
| --- | --- | --- | --- |
| 文件 | 一个 public 类通常对应一个 `.java` 文件 | 一个 `.kt` 文件可放多个类、顶层函数、顶层常量 | Kotlin 文件更自由 |
| 分号 | 通常写 `;` | 通常不写 `;` | Kotlin 省略分号 |
| 创建对象 | `new User()` | `User()` | Kotlin 没有 `new` |
| 变量 | `String name = "a";` | `var name = "a"` | `var` 可变 |
| 常量 / 只读 | `final String name = "a";` | `val name = "a"` | `val` 不能重新赋值 |
| 类型位置 | `String name` | `name: String` | 类型写在变量名后 |
| 默认可见性 | 普通类成员不写通常是 package-private | 不写是 `public` | Kotlin 没有 Java package-private |
| 空值 | 引用类型默认可为 `null` | 类型默认非空 | 可空必须写 `?` |
| 继承 | 普通类默认可继承；非 `final/private/static` 方法通常可重写 | 类和成员默认 final | Kotlin 需要按需添加 `open` |
| 静态 | `static` | `companion object` / 顶层声明 / `object` | Kotlin 没有 `static` 关键字 |
| 相等 | `==` 比引用；`equals()` 表示逻辑相等，但默认实现仍比较引用 | `==` 空安全地调用 `equals()`，`===` 比引用 | 是否“按内容相等”取决于类型如何实现 `equals()` |
| 类型判断 | `instanceof` | `is` / `!is` | Kotlin 判断后可智能转换 |
| 类型转换 | `(String) obj` | `obj as String` / `obj as? String` | `as?` 失败返回 `null` |
| switch | `switch` | `when` | `when` 可返回值 |

### 1.2 类型推断与显式类型

Kotlin 会根据等号右边的值自动猜出变量类型（这叫**类型推断**，原理见 2.1），所以很多时候可以省略类型。但有几种情况建议把类型写清楚：函数要暴露给别人用时、右边是空集合猜不出元素类型时、泛型比较复杂时、或者不写会让人看不懂时。简单说——**能让读代码的人一眼明白就省略，可能产生歧义就写明**：

```kotlin
val count = 10                         // Int
val names = listOf("Jack", "Lucy")    // List<String>
val emptyNames: List<String> = emptyList()
val callback: (String) -> Unit = { name -> println(name) }
```

类型推断不等于类型可以随意变化：

```kotlin
var number = 1       // 推断为 Int
// number = "one"    // 编译错误，变量类型不会随赋值改变

val values: List<Any> = listOf("text", 1, true)
```

需要接收多个具体类型时，可以使用公共父类型或泛型；不要因为暂时不知道类型就全部改成 `Any`。

### 1.3 常用特殊符号

| 符号 | 名称 | 示例 | 说明 |
| --- | --- | --- | --- |
| `->` | Lambda 分隔符 | `{ v -> v.length }` | 左边参数，右边函数体 |
| `::` | 函数 / 属性引用 | `val f = ::println` | 把函数当变量 |
| `$` | 字符串模板 | `"name=$name"` | 拼接变量 |
| `${}` | 字符串表达式 | `"len=${name.length}"` | 拼接表达式 |
| `==` | 结构相等 | `a == b` | 空安全地调用 `equals()`；结果取决于该类型的实现 |
| `===` | 引用相等 | `a === b` | 类似 Java `==` |
| `is` | 类型判断 | `obj is String` | 类似 Java `instanceof` |
| `!is` | 反向类型判断 | `obj !is String` | 不是某类型 |
| `as` | 强制转换 | `obj as String` | 失败抛异常 |
| `as?` | 安全转换 | `obj as? String` | 失败返回 `null` |
| `in` | 包含判断 | `5 in 1..10` | 判断是否在范围 / 集合中 |
| `!in` | 不包含判断 | `"a" !in list` | 判断不在其中 |
| `_` | 忽略参数 | `{ _, value -> value }` | 不使用的参数 |
| `@` | 标签 | `break@outer` | 跳出指定循环或指定 `this` |
| `*` | 展开数组 | `test(*arr)` | 传给 `vararg` |

### 1.4 `Math.java` 和 `MathJVM.kt`

Java 数学工具来自：

```java
java.lang.Math
```

Java：

```java
double x = Math.cos(angle);
double y = Math.sin(angle);
double p = Math.PI;
```

Kotlin 推荐使用：

```kotlin
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

val x = cos(angle)
val y = sin(angle)
val p = PI
```

IDE 可能跳到 `MathJVM.kt`。它是 Kotlin 标准库在 JVM 平台上的实现封装，很多函数最终仍然转调 Java 的 `java.lang.Math`。

| 项 | `Math.java` | `MathJVM.kt` / `kotlin.math` |
| --- | --- | --- |
| 来源 | Java JDK | Kotlin 标准库 |
| 包名 | `java.lang.Math` | `kotlin.math` |
| 写法 | `Math.sin(x)` | `sin(x)` |
| 平台 | JVM / Java | Kotlin 多平台统一 API 的 JVM 实现 |
| Kotlin 推荐 | 一般不优先 | 推荐 |

Android Kotlin 代码建议保持显式导入。即使同一文件使用多个数学函数，也不要为了少写几行而使用 `import kotlin.math.*`；明确的导入更容易看出符号来源，也符合常见 Android Kotlin 代码风格。

## 2. 变量、类型、字符串与集合

### 2.1 `val` 和 `var`

```kotlin
val name = "jack" // 只读，不能重新赋值
var age = 18      // 可变，可以重新赋值

age = 20
// name = "tom" // 编译报错
```

先解释一个 Java 里没有、但全篇都会遇到的概念——**类型推断**：编译器看右边的 `"jack"` 就知道 `name` 是 `String`，看 `18` 就知道 `age` 是 `Int`，所以类型可以省略。想写明也完全可以：`val name: String = "jack"`。这不是动态类型，Kotlin 仍是静态类型语言，类型在编译期就确定了，只是不用你手写。这个特性后面看代码时会反复遇到。

`val` / `var` 对应 Java 的 `final` / 普通变量：

| Java | Kotlin | 含义 |
| --- | --- | --- |
| `final String name = "jack";` | `val name = "jack"` | 引用不可再指向别的对象 |
| `int age = 18;` | `var age = 18` | 变量可重新赋值 |

`val` 只是不允许“变量重新指向新对象”，不代表对象内部不能变；它相当于对引用本身只读，不等于对象深度不可变。

```kotlin
val datas = mutableListOf<String>()
datas.add("a") // 可以，修改的是列表内容

// datas = mutableListOf() // 不可以，val 不能重新赋值
```

`val` 不等于编译期常量。真正的编译期常量使用 `const val`，只能声明在顶层、`object` 或 `companion object` 中，并且类型只能是基本类型或 `String`；不能使用运行时函数调用、普通属性访问或自定义 getter 作为初始值，但可以引用另一个 `const val`：

```kotlin
const val MAX_COUNT = 100

class Config {
    companion object {
        const val DEFAULT_NAME = "unknown"
    }
}
```

普通 `val` 可以保存运行时计算结果，`const val` 必须在编译时就能确定值。`const val` 也不能声明为可空类型。

### 2.2 可变集合和只读集合

| 写法 | 变量能否重新赋值 | 集合内容能否增删 | 说明 |
| --- | --- | --- | --- |
| `val list = mutableListOf<String>()` | 否 | 是 | 常用，列表对象固定，内容可变 |
| `var list = mutableListOf<String>()` | 是 | 是 | 既能换列表，也能改内容 |
| `val list = listOf<String>()` | 否 | 当前引用不能增删 | 只读接口；不保证底层对象绝对不可变 |
| `var list = listOf<String>()` | 是 | 否，但可整体替换 | 偏函数式写法 |

先给 Java 读者一个锚点：`List`/`MutableList` 分别近似 Java 的 `Collections.unmodifiableList(...)`（只读视图）和 `ArrayList`（可增删改）。注意"只读"不等于"底层绝对不可变"，这一点下文会反复强调。

刚开始最容易混的是：这里其实有**两个独立的开关**——①变量本身能不能换成另一个列表（`val`/`var` 决定），②列表里的内容能不能增删（`List`/`MutableList` 决定）。它俩互不干涉，所以才组合出上面 4 种写法。先记住"两个开关"这个说法，看任何一行都能自己推出来。

另外，从这一节开始代码里会出现 **`it`**——它是 Lambda（匿名函数）里单个参数的默认名字，可以粗略理解成 Java foreach 里的循环变量。看到 `{ it.startsWith("D") }` 就知道 `it` 是当前正在处理的那个元素，后面 2.2.1 有完整的符号速查表。

`MutableList` 常见的 5 种追加写法如下。其中前 4 种是原地修改，最后 1 种是创建新列表后重新赋值：

```kotlin
var datas = mutableListOf<String>()

datas += listOf("a", "b")        // 1. += 批量追加，原地修改
datas.addAll(listOf("c", "d"))   // 2. addAll 批量追加，原地修改
datas += "e"                     // 3. += 追加一个，原地修改
datas.add("f")                   // 4. add 追加一个，原地修改
datas = (datas + "g").toMutableList() // 5. 新建列表后重新赋值

println(datas) // [a, b, c, d, e, f, g]
```

前 4 种不会更换 `datas` 引用；第 5 种中的 `+` 返回公开类型为只读 `List`，所以不要强制转换成 `MutableList`。即使某次运行时具体对象碰巧是可变列表，也不应依赖实现细节；确实要生成新列表时，应使用 `toMutableList()`。

如果是 `var datas = listOf<String>()`，则这两种追加写法基本等价：

```kotlin
var datas = listOf<String>()

datas += listOf("d") // 等价于 datas = datas + listOf("d")
datas += "a"         // 等价于 datas = datas + "a"
datas = datas + "a"
```

这里的 `datas` 类型是只读接口 `List<String>`。`+=` 不能原地修改它，只是创建新列表后重新赋值，因此必须是 `var`；如果声明为 `val datas`，这几种 `+=` 写法都会因无法重新赋值而编译失败。

#### 2.2.0 核心规则与常用模板

先用一句话区分：`val/var` 决定“变量能不能换一个集合对象”，`List/MutableList`、`Set/MutableSet`、`Map/MutableMap` 决定“当前引用能不能修改集合内容”。两组概念彼此独立：

```kotlin
val fixed = mutableListOf("A") // 引用不能换，但内容可以改
fixed.add("B")
// fixed = mutableListOf("C") // 编译错误：val 不能重新赋值

var replaceable = listOf("A")  // 引用可以换，但当前 List 不能增删
replaceable = listOf("B")
// replaceable.add("C")       // 编译错误：List 没有 add()
```

常见声明方式：

```kotlin
val names: List<String> = listOf("Jack", "Lucy")
val editableNames: MutableList<String> = mutableListOf("Jack", "Lucy")

val ids: Set<Long> = setOf(1L, 2L, 2L) // 实际只有 1、2
val editableIds: MutableSet<Long> = mutableSetOf(1L, 2L)

val ages: Map<String, Int> = mapOf("Jack" to 18)
val editableAges: MutableMap<String, Int> = mutableMapOf("Jack" to 18)
```

集合常见操作可按下面的模式记忆：

```kotlin
val list = mutableListOf("A", "B")
list.add("C")                    // 末尾添加一个
list.addAll(listOf("D", "E"))   // 追加多个
list[0] = "AA"                   // 按下标修改
list.remove("B")                 // 删除第一个等于 B 的元素
list.removeAt(0)                 // 按下标删除
list.removeAll { it.startsWith("D") } // 按条件删除
list.retainAll { it.isNotEmpty() }    // 只保留满足条件的元素
list.clear()                     // 清空

val set = mutableSetOf("A", "B")
set.add("C")                     // 已存在时不会重复添加
set.addAll(listOf("C", "D"))
set.remove("A")
set.retainAll { it != "D" }

val map = mutableMapOf("Jack" to 18)
map["Lucy"] = 20                 // 添加或覆盖
map.putAll(mapOf("Tom" to 22))
map["Jack"] = map.getValue("Jack") + 1
map.remove("Tom")                // 按 Key 删除
map.remove("Lucy", 20)           // Key 和 Value 都匹配才删除
map.getOrPut("Rose") { 19 }      // 不存在时计算、保存并返回默认值
```

只读暴露与复制：

"内部可改、外部只读"是常见封装模式（原理见 8.1）：

```kotlin
class UserStore {
    private val mutableUsers = mutableListOf<String>()

    // 外部只能读取，不能通过 users.add() 修改
    val users: List<String>
        get() = mutableUsers

    fun add(name: String) {
        mutableUsers += name
    }
}
```

只读视图与快照的区别：

```kotlin
val source = mutableListOf("A")
val view: List<String> = source       // 只读视图，source 改变时 view 也会看到变化
val snapshot: List<String> = source.toList() // 当前内容的只读快照
source += "B"
println(view)     // [A, B]
println(snapshot) // [A]
```

这里的 `toList()` 只是集合结构的浅拷贝；如果元素本身是可变对象，元素内部状态仍可能被共享。对外返回 `List<T>` 能限制调用方通过该引用修改集合，但不能替代线程同步、深拷贝或不可变数据设计。

#### 2.2.1 常用集合类型总览

先记住几个代码中会反复出现的符号：

| 写法 | 小白理解 |
| --- | --- |
| `<T>` | `T` 表示集合里元素的类型，例如 `List<String>` 只能存字符串 |
| `list[index]` | 按下标取元素；下标从 `0` 开始 |
| `{ it.xxx }` | `it` 表示当前正在处理的那个元素 |
| `{ item -> ... }` | 给当前元素起名为 `item`，比 `it` 更直观 |
| `"Jack" to 18` | 创建一个键值对，相当于“Jack 对应 18” |
| `String?` | 结果可能是字符串，也可能是 `null` |

| 类型 | 特点 | 常用创建 | 需要修改内容时 | 常用场景 |
| --- | --- | --- | --- | --- |
| `List<T>` | 有序、允许重复、可按下标访问 | `listOf()` | `mutableListOf()` | 页面列表、Adapter 数据源 |
| `Set<T>` | 元素不重复；具体判重规则取决于实现 | `setOf()` | `mutableSetOf()` | 去重、成员判断 |
| `Map<K, V>` | 按 Key 保存键值对，Key 不重复 | `mapOf()` | `mutableMapOf()` | ID 到对象、配置项映射 |
| `Array<T>` | 长度固定，元素可替换 | `arrayOf()` | 不区分只读/可变接口 | Java API、固定长度数据 |
| `Sequence<T>` | 惰性计算，中间操作不会立即执行 | `sequenceOf()` / `asSequence()` | 通常不按“可变集合”使用 | 大量数据的连续筛选、转换 |

Kotlin 集合接口分为两组（下列为类型记号对照，非可执行语句）：

```kotlin
List<T>          // 只读接口
MutableList<T>   // 可变接口

Set<T>
MutableSet<T>

Map<K, V>
MutableMap<K, V>
```

这里的“只读”表示当前引用只能调用读取方法，不一定代表底层对象绝对不可变。

```kotlin
// 创建一个内容可以修改的列表
val mutable = mutableListOf("a")

// readonly 只能使用 List 的读取方法
// 但它和 mutable 指向的是同一个列表对象
val readonly: List<String> = mutable

// 通过 mutable 修改底层列表
mutable.add("b")

// 所以 readonly 也能看到新增的 "b"
println(readonly) // [a, b]
```

因此不要把 `List` 简单理解成线程安全、不可变集合。同理，`Set`、`Map` 的只读接口也不等于深度不可变，更不自动具备线程安全能力。只读视图与快照的完整对比见 2.2.0。

> **延伸：真正不可变的集合**。如果你需要的是"创建后任何人、任何地方都无法修改"的集合（而不是仅仅"这个引用只读"），Kotlin 标准库没有内置，需要额外依赖 `kotlinx-collections-immutable`，用 `persistentListOf()` / `persistentMapOf()` 等创建。它内部用结构共享实现，复制成本低，适合 Compose 状态、多线程共享等场景。日常业务里 `List` 的只读接口通常够用，不必引入这个库——这里只是提醒你二者不是一回事，别把"只读"当成"不可变"的保证。

#### 2.2.2 `List`：有序列表

创建列表：

```kotlin
// 创建一个空的只读字符串列表
val empty = emptyList<String>()

// 只读列表：有顺序，可以包含重复元素
val readonly = listOf("A", "B", "A")

// 可变列表：可以 add、remove、修改指定位置
val mutable = mutableListOf("A", "B")

// 底层明确使用 ArrayList
val arrayList = arrayListOf("A", "B")

// 创建 5 个元素
// index 会依次得到 0、1、2、3、4
// 最终结果为 [0, 2, 4, 6, 8]
val numbers = List(5) { index -> index * 2 }

// 创建包含 3 个元素的可变列表
// 最终结果为 [item-0, item-1, item-2]
val items = MutableList(3) { index -> "item-$index" }
```

读取元素：

```kotlin
val list = listOf("A", "B", "C")

val first = list[0]              // 下标 0 是第一个元素："A"
val second = list.get(1)         // 等价于 list[1]，结果为 "B"
val safeValue = list.getOrNull(10) // 下标越界，不崩溃，返回 null
val firstOrNull = list.firstOrNull() // 取第一个；空列表返回 null
val lastOrNull = list.lastOrNull()   // 取最后一个；空列表返回 null
```

不确定列表是否为空或下标是否合法时，优先使用 `firstOrNull()`、`lastOrNull()`、`getOrNull()`。

修改可变列表：

```kotlin
val list = mutableListOf("A", "B")

list.add("C")          // 末尾添加 C：[A, B, C]
list.add(1, "X")       // 在下标 1 插入 X：[A, X, B, C]
list += "D"            // 末尾添加 D
list.addAll(listOf("E", "F")) // 一次添加多个元素

list[0] = "AA"         // 把下标 0 的 A 修改为 AA

list.remove("B")       // 删除第一个内容等于 B 的元素
list.removeAt(0)       // 删除下标 0 的元素
list.removeAll {
    // it 表示当前遍历到的字符串
    // 返回 true 代表删除该元素
    it.startsWith("E")
}
list.clear()           // 删除全部元素，列表变为空
```

判断和查找：

```kotlin
val list = listOf("A", "B", "C")

val contains = "B" in list          // 是否包含 B，结果为 true
val index = list.indexOf("B")       // B 的下标是 1；找不到返回 -1
val item = list.find {
    // 依次判断每个元素，找到第一个 B 后停止
    it == "B"
} // 找不到返回 null
```

#### 2.2.3 `Set`：不重复集合

```kotlin
// Set 会自动去重，所以最终只有 A、B
val readonly = setOf("A", "B", "A")
println(readonly) // [A, B]，重复的 A 只保留一份

val mutable = mutableSetOf("A", "B")
mutable.add("C")    // 添加 C
mutable.add("C")    // C 已经存在，不会重复添加
mutable.remove("A") // 删除 A

val contains = "B" in mutable // 判断是否包含 B
```

`setOf()`、`mutableSetOf()` 当前常见实现会保留插入顺序，但 `Set` 接口本身主要保证“不重复”，业务逻辑不要无条件依赖遍历顺序。明确需要顺序时，可使用 `linkedSetOf()`；需要排序时，可使用 `sortedSetOf()`（JVM）。

常见的哈希 Set 使用 `equals()/hashCode()` 判重；排序 Set 则使用元素的自然顺序或传入的 `Comparator` 判重。自定义类型用于 Set 元素时，要正确且稳定地实现相应规则。

列表去重：

```kotlin
val list = listOf("A", "B", "A")

// 去重后仍返回 List，结果为 [A, B]
val uniqueList = list.distinct()

// 转成 Set，利用 Set 不重复的特性去重
val uniqueSet = list.toSet()
```

按某个字段去重：

```kotlin
data class User(val id: Int, val name: String)

val users = listOf(
    User(1, "Jack"),
    User(1, "Tom"),
    User(2, "Lucy")
)

// 按用户 id 去重
// 两个 id=1 的用户只保留第一个 Jack
val uniqueUsers = users.distinctBy { user ->
    user.id
}
```

集合运算：

```kotlin
val a = setOf(1, 2, 3)
val b = setOf(3, 4, 5)

val union = a union b         // 并集：两个集合的全部元素
val intersect = a intersect b // 交集：两个集合都有的元素 [3]
val subtract = a subtract b   // 差集：a 有但 b 没有的元素 [1, 2]
```

#### 2.2.4 `Map`：键值对集合

创建 Map：

```kotlin
// 空 Map，Key 是 String，Value 是 Int
val empty = emptyMap<String, Int>()

// "Jack" 是 Key，18 是 Value
val readonly = mapOf(
    "Jack" to 18,
    "Lucy" to 20
)

// 可变 Map，可以添加、删除和覆盖键值对
val mutable = mutableMapOf(
    "Jack" to 18,
    "Lucy" to 20
)
```

`"Jack" to 18` 会创建一个 `Pair<String, Int>`。

读取值：

```kotlin
val ages = mapOf("Jack" to 18)

val age1: Int? = ages["Jack"] // 找到 Jack，结果为 18
val age2: Int? = ages.get("Jack") // 和 ages["Jack"] 相同

// Tom 不存在时返回默认值 0
val age3: Int = ages.getOrDefault("Tom", 0)

// Tom 不存在时执行大括号，并把最后一行 0 作为结果
val age4: Int = ages.getOrElse("Tom") { 0 }
```

`Map` 通过 `[]` 读取时返回可空类型，因为指定 Key 可能不存在。如果 Value 本身也允许为 `null`，仅看读取结果无法区分“Key 不存在”和“Key 存在但 Value 为 null”，需要配合 `containsKey()` 判断。

如果确定 Key 必须存在，可以使用 `getValue()`；普通 Map 的 Key 不存在时会抛出 `NoSuchElementException`：

```kotlin
val age = ages.getValue("Jack")
```

使用 `withDefault { ... }` 包装过的 Map 是例外：它的 `getValue()` 会在 Key 不存在时调用默认值函数。

修改 MutableMap：

```kotlin
val ages = mutableMapOf("Jack" to 18)

ages["Lucy"] = 20             // 添加 Lucy -> 20
ages.put("Tom", 22)           // put 与 [] 赋值作用相同
ages.putAll(mapOf("Rose" to 19)) // 一次添加多个键值对

ages["Jack"] = 21 // Jack 已存在，所以把 18 覆盖成 21
ages.remove("Tom") // 根据 Key 删除 Tom
ages.clear()       // 清空 Map
```

判断 Key 和 Value：

```kotlin
val hasJack = "Jack" in ages          // 判断是否存在 Key：Jack
val hasLucy = ages.containsKey("Lucy") // 同样是判断 Key
val hasAge20 = ages.containsValue(20)  // 判断是否存在 Value：20
```

遍历 Map：

```kotlin
// (name, age) 会自动拆开每个键值对
for ((name, age) in ages) {
    println("$name -> $age")
}

// forEach 的写法，作用与上面的 for 循环相似
ages.forEach { (name, age) ->
    println("$name -> $age")
}

// 只遍历所有 Key
for (key in ages.keys) {
    println(key)
}

// 只遍历所有 Value
for (value in ages.values) {
    println(value)
}
```

#### 2.2.5 数组和基本类型数组

普通数组：

```kotlin
// 普通字符串数组，长度固定为 2
val names = arrayOf("Jack", "Lucy")

// 创建长度为 3 的数组，初始元素都是 null
val nullableNames = arrayOfNulls<String>(3)

// index 依次为 0..4，最终得到 [0, 1, 4, 9, 16]
val squares = Array(5) { index -> index * index }

names[0] = "Tom" // 修改第一个位置的元素
println(names[1]) // 读取第二个位置的元素：Lucy
```

基本类型数组：

```kotlin
val ints = intArrayOf(1, 2, 3)          // IntArray，对应 Java int[]
val longs = longArrayOf(1L, 2L)         // LongArray，对应 Java long[]
val floats = floatArrayOf(1f, 2f)       // FloatArray
val doubles = doubleArrayOf(1.0, 2.0)   // DoubleArray
val booleans = booleanArrayOf(true, false) // BooleanArray
val chars = charArrayOf('A', 'B')       // CharArray
```

`IntArray` 在 JVM 上对应高效的 `int[]`，而 `Array<Int>` 更接近装箱后的 `Integer[]`。

数组没有像 List 那样实现按元素的结构相等。比较数组内容时不要直接使用 `==`：

```kotlin
val first = intArrayOf(1, 2)
val second = intArrayOf(1, 2)

println(first == second) // false，数组沿用引用相等语义
println(first.contentEquals(second)) // true，逐个比较元素
```

嵌套数组可按需使用 `contentDeepEquals()`。List、Set、Map 则按照各自的集合相等规则实现 `equals()`。

数组和集合互转：

```kotlin
val array = arrayOf("A", "B")

val list = array.toList()               // 数组转只读 List
val mutableList = array.toMutableList() // 数组转可变 MutableList

val newArray = list.toTypedArray()      // List 转回 Array
```

调用 Java 可变参数方法或 Kotlin `vararg` 函数时，用 `*` 展开数组：

```kotlin
// vararg 表示调用者可以传入任意数量的字符串
fun printAll(vararg values: String) {
    values.forEach(::println)
}

val values = arrayOf("A", "B")

// * 会把数组展开，相当于 printAll("A", "B")
printAll(*values)
```

#### 2.2.6 遍历集合

普通遍历：

```kotlin
val list = listOf("A", "B", "C")

// item 依次为 A、B、C
for (item in list) {
    println(item)
}
```

同时获取下标和值：

```kotlin
// withIndex() 同时提供下标和元素
for ((index, item) in list.withIndex()) {
    println("$index -> $item")
}
```

使用 `indices` 遍历下标：

```kotlin
// indices 表示列表的合法下标范围，这里是 0..2
for (index in list.indices) {
    println(list[index])
}
```

使用 `forEach`：

```kotlin
// 只需要元素
list.forEach { item ->
    println(item)
}

// 同时需要下标和元素
list.forEachIndexed { index, item ->
    println("$index -> $item")
}
```

需要 `break`、`continue` 或复杂流程控制时，普通 `for` 循环通常比 `forEach` 更清晰。

> **Java 迁移者先找这个**：Kotlin **没有** C 风格三段式 `for (int i = 0; i < n; i++)`。想要同样的效果，用区间 `for (i in 0 until n)`（`until` 不含右端点，等价于 `i < n`），或直接 `for (i in list.indices)` 遍历下标；确需手动控制步进时用 `while`。`for (i in 0..n)` 是闭区间，包含 `n`，别和 `until` 混用。

#### 2.2.7 筛选、转换和排序

> **Java 8 读者快速锚定**：下面这些操作符和你熟悉的 Stream API 几乎一一对应——`filter`≈`stream().filter()`、`map`≈`stream().map()`、`sortedBy`≈`sorted(Comparator.comparing(...))`、`fold/reduce`≈`Stream.reduce()`、`groupBy`≈`Collectors.groupingBy()`。区别在于 Kotlin 集合直接调用，不用先 `.stream()`、最后也不用 `.collect(Collectors.toList())`。

**建议先掌握这三个高频操作符**：`filter`（筛选）、`map`（转换）、`firstOrNull {}`（找第一个符合条件的，找不到返回 `null`）。它们能覆盖大多数日常场景；本节其余操作符较多，标记为"用到再回来查"即可，不必一次记全。

`filter`：筛选符合条件的元素。

```kotlin
val numbers = listOf(1, 2, 3, 4, 5)

val evens = numbers.filter { number ->
    // 返回 true：保留这个元素
    // 返回 false：过滤掉这个元素
    number % 2 == 0
} // 最终结果为 [2, 4]
```

`map`：把每个元素转换成新值。

```kotlin
val names = listOf("Jack", "Lucy")

val lengths = names.map { name ->
    // 把每个名字转换成它的字符数量
    name.length
} // 最终结果为 [4, 4]
```

`mapNotNull`：转换并过滤掉 `null`。

```kotlin
val values = listOf("1", "A", "2")

val numbers = values.mapNotNull { text ->
    // "1"、"2" 转换成功
    // "A" 转换失败得到 null，mapNotNull 会把 null 丢掉
    text.toIntOrNull()
} // 最终结果为 [1, 2]
```

`filterNotNull`：过滤空值并得到非空元素列表。

```kotlin
val values: List<String?> = listOf("A", null, "B")

// 删除全部 null，并把元素类型从 String? 变成 String
val nonNullValues: List<String> = values.filterNotNull()
```

排序：

```kotlin
data class User(val name: String, val age: Int)

val users = listOf(
    User("Jack", 20),
    User("Lucy", 18)
)

val ascending = users.sortedBy { it.age } // 按年龄从小到大
val descending = users.sortedByDescending { it.age } // 从大到小
val reversed = users.reversed() // 只反转当前顺序，不比较字段
```

多条件排序：

```kotlin
val sorted = users.sortedWith(
    compareBy<User> { it.age } // 第一条件：年龄升序
        .thenBy { it.name }    // 年龄相同时，再按名字升序
)
```

注意返回新集合和原地修改的区别：

```kotlin
val source = mutableListOf(3, 1, 2)

// 返回新的 List，source 不变
val sortedCopy = source.sorted()

// 原地修改 source，函数返回 Unit
source.sort()
```

常见对应关系：

| 返回新集合 | 原地修改可变集合 |
| --- | --- |
| `sorted()` | `sort()` |
| `sortedDescending()` | `sortDescending()` |
| `reversed()` | `reverse()` |
| `shuffled()` | `shuffle()` |

#### 2.2.8 查找、判断和聚合

查找：

```kotlin
val numbers = listOf(1, 2, 3, 4)

val firstEven = numbers.firstOrNull { it % 2 == 0 } // 第一个偶数：2
val lastEven = numbers.lastOrNull { it % 2 == 0 }   // 最后一个偶数：4
val found = numbers.find { it > 2 }                 // 第一个大于 2 的数：3

// 只有“恰好一个”元素等于 3 时才返回它，否则返回 null
val single = numbers.singleOrNull { it == 3 }
```

判断：

```kotlin
val hasEven = numbers.any { it % 2 == 0 } // 至少有一个偶数
val allPositive = numbers.all { it > 0 }  // 是否全部大于 0
val noNegative = numbers.none { it < 0 }  // 是否一个负数都没有
val isEmpty = numbers.isEmpty()           // 是否为空集合
val isNotEmpty = numbers.isNotEmpty()     // 是否至少有一个元素
```

统计和聚合：

```kotlin
val count = numbers.count { it % 2 == 0 } // 偶数的数量
val sum = numbers.sum()                   // 所有数字相加
val sumOfSquares = numbers.sumOf { it * it } // 每个数平方后再求和
val max = numbers.maxOrNull()             // 最大值；空集合返回 null
val min = numbers.minOrNull()             // 最小值；空集合返回 null

// result 是上一次累加结果，初始值是 0
// value 是当前遍历到的数字
val total = numbers.fold(0) { result, value ->
    result + value
}
```

`reduce()` 使用第一个元素作为初始值，空集合会抛异常；`fold()` 可以明确提供初始值，通常更安全。

```kotlin
val sum1 = numbers.reduce { result, value -> result + value }
val sum2 = numbers.fold(0) { result, value -> result + value }
```

#### 2.2.9 分组、关联和组合

按条件分成两组：

```kotlin
// 满足条件的进入 evens，不满足条件的进入 odds
val (evens, odds) = listOf(1, 2, 3, 4).partition { it % 2 == 0 }

println(evens) // [2, 4]
println(odds)  // [1, 3]
```

按字段分组：

```kotlin
data class User(val id: Int, val department: String)

val users = listOf(
    User(1, "Android"),
    User(2, "Server"),
    User(3, "Android")
)

// department 作为 Map 的 Key
// 同一部门的用户被放进同一个 List
val usersByDepartment: Map<String, List<User>> =
    users.groupBy { user -> user.department }
```

转换成 Map：

```kotlin
// 用户 id 作为 Key，完整 User 对象作为 Value
val usersById: Map<Int, User> = users.associateBy { it.id }

// 自己决定 Key 和 Value
// Key 是 user.id，Value 是 user.department
val departmentById: Map<Int, String> =
    users.associate { user -> user.id to user.department }
```

如果 `associateBy()` 得到重复 Key，后面的元素会覆盖前面的元素。

> 版本提醒：旧教程里的 `minBy` / `maxBy` 已废弃，Kotlin 1.4 起统一用 `minByOrNull` / `maxByOrNull`（空列表返回 `null`，名称更准确地表达了可空性）。

组合两个列表：

```kotlin
val names = listOf("Jack", "Lucy")
val ages = listOf(18, 20)

// 按相同下标两两配对
// Jack 和 18 配对，Lucy 和 20 配对
val pairs = names.zip(ages) // [(Jack, 18), (Lucy, 20)]

// unzip 是 zip 的逆操作，把 Pair 列表拆成两个列表
val (restoredNames, restoredAges) = pairs.unzip()
```

用循环 `add` 临时收集元素是 Java 旧习惯，Kotlin 可以用 `buildList` / `buildMap` 直接构造：

```kotlin
val squares = buildList {
    for (i in 1..5) add(i * i)   // 块里的 this 是 MutableList
}
val counts = buildMap<String, Int> {
    put("jack", 1)
    put("lucy", 2)
}
```

分页与批处理（Android 里处理接口返回的大列表常用）：

```kotlin
val data = (1..10).toList()

data.chunked(3)       // 按 3 个一组切块：[[1,2,3], [4,5,6], [7,8,9], [10]]
data.windowed(3)      // 长度 3 的滑动窗口：[[1,2,3], [2,3,4], ...]
data.windowed(3, 2)   // 步长 2 的滑动窗口：[[1,2,3], [3,4,5], ...]
```

展开嵌套集合：

```kotlin
val nested = listOf(
    listOf(1, 2),
    listOf(3, 4)
)

// 把二维列表展开成一维列表
val flat1 = nested.flatten() // [1, 2, 3, 4]

// 每个 User 先转换成一个 List，最后把所有 List 展开
val flat2 = users.flatMap { user ->
    listOf(user.id, user.id * 10)
}
```

#### 2.2.10 集合类型转换

```kotlin
val list = listOf("A", "B", "A")

val mutableList = list.toMutableList() // 转成可以增删的列表
val set = list.toSet()                 // 转 Set，同时去重
val mutableSet = list.toMutableSet()   // 转成可变 Set
val array = list.toTypedArray()        // 转成 Array<String>

// 元素本身作为 Key，字符串长度作为 Value
// 结果类似：{A=1, B=1}
val map = list.associateWith { value -> value.length }
```

Java 集合迁移到 Kotlin 时，常见对应关系：

| Java | Kotlin 常用写法 |
| --- | --- |
| `new ArrayList<>()` | `mutableListOf()` / `arrayListOf()` |
| `new HashSet<>()` | `mutableSetOf()` / `hashSetOf()` |
| `new HashMap<>()` | `mutableMapOf()` / `hashMapOf()` |
| `Collections.emptyList()` | `emptyList()` |
| `Arrays.asList(a, b)` | `listOf(a, b)` |

#### 2.2.11 `Sequence`：大量数据的惰性处理

普通集合的每个中间操作通常都会创建一个新集合：

```kotlin
// 普通集合写法
val result = (1..1_000_000)
    .filter { it % 2 == 0 } // 创建包含全部偶数的新集合
    .map { it * 2 }         // 再创建一个乘以 2 的新集合
    .take(10)               // 最后只取前 10 个
```

使用 `Sequence` 后，中间操作按需执行：

```kotlin
val result = (1..1_000_000)
    .asSequence()           // 转成惰性序列
    .filter { it % 2 == 0 } // 先记录筛选规则，不立即遍历
    .map { it * 2 }         // 先记录转换规则
    .take(10)               // 只需要前 10 个结果
    .toList()               // 终止操作：从这里才真正开始计算
```

`filter()`、`map()` 是中间操作；`toList()`、`first()`、`count()` 等是终止操作。调用终止操作后才真正开始计算。

`Sequence` 不一定更快：它减少了中间集合和不必要的遍历，但每个元素会经过迭代器/操作链的调度，数据量很小或操作很少时可能反而有额外开销。还要注意，`Sequence` 的代码会延迟执行，终止操作前不要依赖副作用已经发生。

使用建议：

| 场景 | 建议 |
| --- | --- |
| 数据量小、操作简单 | 直接使用 `List`，代码更直观 |
| 数据量大、连续多次转换 | 考虑 `Sequence` |
| 只需要前几个匹配结果 | `Sequence` 可避免处理全部数据 |
| 需要多次遍历结果 | 先 `toList()` 保存结果 |

#### 2.2.12 Android 中常见集合写法

示例数据类：

```kotlin
data class User(
    val id: Long,
    val department: String,
    val isInvalid: Boolean
)
```

Adapter 数据源：

```kotlin
// val 保证 items 不会重新指向另一个列表
// mutableListOf 允许修改列表内部内容
private val items = mutableListOf<User>()

fun submitList(newItems: List<User>) {
    // 删除旧数据
    items.clear()

    // 添加全部新数据
    items.addAll(newItems)

    // 通知 RecyclerView 重新显示
    notifyDataSetChanged()
}
```

如果使用 `ListAdapter + DiffUtil`，通常保留只读列表并提交新对象：

```kotlin
// 创建一份新的 List 再交给 ListAdapter
// 避免后续修改原集合时影响 DiffUtil 比较
adapter.submitList(newItems.toList())
```

按 ID 快速查找：

```kotlin
// 把用户 id 作为 Key，之后可直接通过 id 查找
val userById: Map<Long, User> = users.associateBy { it.id }

// targetId 不存在时，user 为 null
val user = userById[targetId]
```

按类型分组显示：

```kotlin
// department 相同的用户会被放进同一个列表
val sections: Map<String, List<User>> =
    users.groupBy { it.department }
```

避免在遍历过程中直接删除：

```kotlin
// 错误风险：遍历时修改同一个集合
// for (item in items) {
//     if (item.isInvalid) items.remove(item)
// }

// 推荐
items.removeAll { user ->
    // 返回 true 的用户会被删除
    user.isInvalid
}
```

集合选择原则：

1. 需要保持顺序、允许重复：使用 `List`。
2. 需要去重或频繁判断元素是否存在：使用 `Set`。
3. 需要通过 Key 快速定位数据：使用 `Map`。
4. 内容需要增删改：选择对应的 `MutableList`、`MutableSet`、`MutableMap`。
5. 对外暴露数据时优先返回只读接口，例如 `List<T>`。
6. 默认优先 `val` 保存集合引用，需要修改集合内容时使用 `Mutable*`。

### 2.3 字符串模板

```kotlin
val name = "jack"
val text1 = "name=$name"
val text2 = "length=${name.length}"
```

等价 Java：

```java
String text1 = "name=" + name;
String text2 = "length=" + name.length();
```

### 2.4 数字类型和显式转换

Kotlin 不会像 Java 那样自动把较小数字类型隐式扩大成较大类型：

```kotlin
val intValue: Int = 10

// val longValue: Long = intValue // 编译错误
val longValue: Long = intValue.toLong()

val doubleValue: Double = intValue.toDouble()
```

常用数字类型：

| Kotlin | JVM/Java 对应 | 示例 |
| --- | --- | --- |
| `Byte` | `byte` / `Byte` | `1.toByte()` |
| `Short` | `short` / `Short` | `1.toShort()` |
| `Int` | `int` / `Integer` | `1` |
| `Long` | `long` / `Long` | `1L` |
| `Float` | `float` / `Float` | `1.0f` |
| `Double` | `double` / `Double` | `1.0` |

整数除法仍会舍弃小数部分：

```kotlin
val a = 5 / 2           // 2
val b = 5.toDouble() / 2 // 2.5
```

Java 迁移时要特别检查绘制、动画、尺寸和比例计算中的 `Int/Float/Double` 转换。

**整数溢出**：Kotlin 的整数运算和 Java 一样会静默回绕，不会抛异常。自定义 View 的尺寸、动画插值、进度计算尤其容易踩坑：

```kotlin
val max = Int.MAX_VALUE
println(max + 1)        // -2147483648，回绕成最小值，不报错

// 用运行期变量，避免编译期常量折叠，更贴近真实业务的静默溢出
val factor = 1_000_000_000
println(factor * 3)     // 溢出成负数，编译和运行都不会报错
```

应对策略：

- 用 `coerceAtLeast(min)` / `coerceAtMost(max)` / `coerceIn(min, max)` 把结果限制在有效范围。
- 需要检测溢出时用 `Math.addExact` / `Math.multiplyExact`（溢出抛 `ArithmeticException`）。
- 先乘后除容易溢出时，先转成 `Long` 计算再转回：`(a.toLong() * b / c).toInt()`。
- `Long` 与 `Int` 混算必须显式转换，Kotlin 不做隐式扩宽。

### 2.5 字符串常用能力

字符串是不可变对象，`trim()`、`replace()`、`uppercase()` 等操作会返回新字符串，不会修改原变量：

```kotlin
val source = "  Kotlin  "
val trimmed = source.trim()       // "Kotlin"
println(source)                   // 仍然是 "  Kotlin  "
```

多行字符串使用三引号，默认不会处理缩进；需要去除公共缩进时使用 `trimIndent()`：

```kotlin
val json = """
    {
        "name": "Jack"
    }
""".trimIndent()
```

当多行字符串本身已有对齐、不希望被统一去掉缩进时，用 `trimMargin()` 指定行首标记，它只去掉标记及其左侧内容，保留标记右侧的原始缩进：

```kotlin
val text = """
    |第一行
    |  第二行有缩进
    |第三行
""".trimMargin()   // 每行去掉到 | 为止，第二行保留两个前导空格
```

`String` 与字符数组的互转：`String(charArray)` 由字符数组建串，`str.toCharArray()` 反向得到可变的 `CharArray`。注意 `String` 不可变而 `CharArray` 可改；另外字符串长度按 UTF-16 单元计算，一个 emoji 代理对会让 `length` 大于肉眼字符数。

需要按分隔符解析时，`split()` 会返回列表；需要安全转数字时优先使用 `toIntOrNull()`、`toLongOrNull()` 等函数。不要把 `String` 当作可变字符数组，频繁拼接大量片段时可以考虑 `buildString { append(...) }`。

Java 开发者高频用到的截取与替换函数（Kotlin 都有对应物，不必再调用 `substring(begin, end)` 数下标）：

```kotlin
val path = "https://example.com/user/42"

path.substringAfterLast("/")   // "42"，取最后一个分隔符之后
path.substringBefore("://")    // "https"，取第一个分隔符之前
path.substringAfter("://")     // "example.com/user/42"

val name = "  Kotlin  "
name.ifBlank { "未命名" }       // 空白才替换（ifEmpty 只在长度为 0 时替换）
"5".padStart(3, '0')           // "005"，补零对齐，日志和编号常用
"hello".replaceFirst("l", "L") // "heLlo"，只替换第一处
```

正则用 `Regex`，三引号原始字符串里不需要为 `\` 写双重转义：

```kotlin
val regex = Regex("""(\d{4})-(\d{2})-(\d{2})""")
val match = regex.find("日期 2026-11-24")   // find() 找第一处，matches() 要求整串匹配
val (year, month, day) = match!!.destructured // 按捕获组解构
println("$year-$month-$day")                  // 2026-11-24
```

> 正则解析失败时 `find()` 返回 `null`，务必用 `?.let` 或 `?:` 处理，不要直接 `!!`。简单分隔场景优先用 `substringBefore/After`，可读性好于正则。

### 2.6 `Pair`、`Triple` 与领域模型

`Pair` 和 `Triple` 适合临时组合少量值（创建见 2.2.4，解构见 11.7）：

```kotlin
val pair: Pair<String, Int> = "Jack" to 18
val (name, age) = pair
```

如果值会跨函数、跨模块或长期保存，优先使用有字段名称的 `data class`。`Pair.first`、`Pair.second` 比 `User.name`、`User.age` 更容易产生位置误读。

## 3. 空安全

> **为什么要有这套东西？** Java 里任何对象都可能是 `null`，编译器不会提醒你，所以满屏都是 `if (x != null)`，漏一处就 `NullPointerException`（NPE）。Kotlin 的做法很聪明：把"这里能不能为 null"写进**类型**里，交给编译器检查。结果就是——你忘了判空，代码根本编译不过，NPE 被提前挡在了运行之前。这是 Kotlin 最值得学的特性，务必搞懂。

Kotlin 里类型**默认不能装 null**：

```kotlin
var name: String = "jack"
// name = null // 编译报错：String 类型不接受 null
```

确实需要为 null 时，在类型后面加一个 `?`：

```kotlin
var name: String? = null   // 读作"可空的 String"
```

记住这条规则就够了：**没有 `?` 就不能是 null，想为 null 必须写 `?`**。编译器会据此决定要不要强制你判空。

### 3.1 空安全符号总表

| 符号 | 名称 | 示例 | 含义 | Java 里的近似写法 |
| --- | --- | --- | --- | --- |
| `?` | 可空类型 | `String?` | 允许变量为 `null` | Java 所有引用类型默认都可空，无需标记 |
| `?.` | 安全调用 | `name?.length` | 不为空才调用，为空返回 `null` | `name != null ? name.length() : null` |
| `!!` | 非空断言 | `name!!.length` | 强制非空，为空直接崩 | 裸写 `name.length()`，等于放弃 NPE 保护 |
| `?:` | Elvis 运算符 | `name?.length ?: 0` | 左边为空时使用右边默认值 | `name != null ? name.length() : 0` |
| `?.let {}` | 非空执行 | `name?.let { ... }` | 不为空才执行代码块 | `if (name != null) { ... }` |

> **给 Java 迁移者的重要提醒**：刚学 Kotlin 时最容易到处加 `!!` 让代码"编译通过"——这等于主动放弃空安全，退化成 Java 的裸调用。`!!` 只在"100% 确定非空、且类型系统无法表达这个事实"时才用；日常优先用 `?.`、`?:`、`?.let`。看到自己连写几个 `!!`，通常说明上面的类型设计需要调整。

### 3.2 `?.` 调用无返回值方法

```kotlin
class User {
    fun show() {
        println("show")
    }
}

val user: User? = null
val result = user?.show()
```

如果 `user == null`，`show()` 不执行，`result == null`。

如果 `user != null`，`show()` 执行，`result == Unit`。

所以表达式类型是 `Unit?`（`user == null` 时为 `null`，否则为 `Unit` 单例）。

Java 行为类似：

```java
if (user != null) {
    user.show();
}
```

但 Java 的 `void` 不能作为变量值，所以没有完全等价的 Java 表达式。

### 3.3 字符串比较

```kotlin
var name: String? = "hi"

// equals() 可以在可空接收者上调用，结果本身是 Boolean
val directEquals: Boolean = name.equals("hello")

val a: Boolean? = name?.equals("hello")
val b: Boolean = name?.equals("hello") == true
val c: Boolean = name!!.equals("hello")
val d: Boolean = name == "hello"
```

| 写法 | `name = "hi"` | `name = null` | 说明 |
| --- | --- | --- | --- |
| `name.equals("hello")` | `false` | `false` | `equals()` 可在可空接收者上调用，结果是 `Boolean` |
| `name?.equals("hello")` | `false` | `null` | 显式安全调用的结果是 `Boolean?` |
| `name?.equals("hello") == true` | `false` | `false` | 把可空结果转换成确定的布尔值 |
| `name!!.equals("hello")` | `false` | 抛 NPE | 先强制非空，不推荐日常使用 |
| `name == "hello"` | `false` | `false` | 推荐，空安全结构相等 |

Kotlin 的 `==` 是空安全的结构相等。它会安全地比较两边是否相等；与 `name.equals("hello")` 不同，`==` 两边都可以是可空值，结果始终是 `Boolean`。可以近似理解为：

```kotlin
val result = if (name == null) {
    false // 右边 "hello" 不是 null
} else {
    name.equals("hello")
}
```

还有一个容易忽略的符号 `===`：它比较**引用是否指向同一个对象**，作用类似 Java 的 `==`；而 Kotlin 的 `==` 作用类似 Java 的 `equals()`（结构相等）。

```kotlin
val a = String("hi".toCharArray())
val b = String("hi".toCharArray())
println(a == b)   // true，内容相同
println(a === b)  // false，两个不同对象
```

日常业务优先用 `==`；只有确实需要判断"是不是同一个实例"时才用 `===`。

补充一点：`equals()` 之所以能在可空的 `String?` 上直接调用，是因为 `equals`、`hashCode`、`toString` 这三个方法在 Kotlin 中被特殊对待。这**不适用于其他成员**，例如 `name.length` 仍需写成 `name?.length` 或先判空。

迁移 Java 时，如果原代码是：

```java
name.equals("hello")
```

并且必须严格保留“`name == null` 时抛 NPE”的旧行为，Kotlin 才写：

```kotlin
name!!.equals("hello")
```

通常更推荐安全写法：

```kotlin
name == "hello"
```

如果原 Java 使用常量在前来避免 NPE：

```java
"hello".equals(name)
```

迁移到 Kotlin 同样推荐：

```kotlin
name == "hello"
```

### 3.4 可空性写在什么位置：类型本身 vs 泛型参数

Java 开发者刚接触 Kotlin 时最容易看错的一类声明，是 `?` 到底修饰谁。集合和泛型里，`?` 放在不同位置含义完全不同。先用一张表建立直觉：

| 声明 | 可空的是谁 | 直观含义 |
| --- | --- | --- |
| `String` | 谁都不可空 | 必须是一个字符串，不能是 `null` |
| `String?` | 变量 | 可以是字符串，也可以是 `null` |
| `List<String>` | 谁都不可空 | 集合本身非 null，元素也非 null |
| `List<String>?` | 集合本身 | 集合可能是 `null`，但一旦存在，里面元素都非 null |
| `List<String?>` | 元素 | 集合本身非 null，但元素允许是 `null` |
| `List<String?>?` | 集合和元素 | 集合可能是 `null`，元素也可能是 `null` |

关键记忆点：**`?` 紧贴着谁，谁就可空**。`List<String>?` 的 `?` 在 `>` 后面，修饰整个集合；`List<String?>` 的 `?` 在 `String` 后面，修饰元素。

对照 Java 为什么容易混淆：Java 的 `List<String>` 没有"集合本身是否可空""元素是否可空"的语法表达，这两种信息只能靠注释或约定。Kotlin 把它们写进类型里：

```kotlin
// Java：两者都写成 List<String>，是否可空只能靠猜
// List<String> names = null;              // 集合本身为 null
// Arrays.asList("A", null);               // 元素为 null

// Kotlin：两种情况类型不同，编译器会强制你区别处理
val maybeList: List<String>? = null        // 集合本身可空
val listWithNulls: List<String?> = listOf("A", null) // 元素可空

// 集合本身可空 → 先判集合
val size = maybeList?.size ?: 0

// 元素可空 → 处理每个元素时判空（filterNotNull 见 2.2）
val nonNull: List<String> = listWithNulls.filterNotNull()
```

如果把 `List<String?>` 直接传给期望 `List<String>` 的函数，编译会报错——因为调用方可能遇到 `null` 元素。这正是 Kotlin 想把 Java 里"运行时才发现 null"的问题提前到编译期。

泛型参数同理：

```kotlin
class Box<T>(val value: T)

val boxOfNull: Box<String?> = Box(null)  // T 本身可空，合法
// val box: Box<String> = Box(null)      // 编译报错：Box<String> 不接受 null
```

> 与平台类型的分工：`List<String>?` 这种可空性是**你自己声明**的；Java 传来的集合没有可空性标记，属于**平台类型**，规则见 10.5、10.6。二者不要混为一谈。

## 4. 控制流、区间与异常

Kotlin 中所有异常都是非受检异常：编译器不会强制要求 `try/catch`，也不要求函数声明 `throws`。

对照 Java：Java 里 `IOException` 这类**受检异常**逼着你 `try/catch` 或在方法签名写 `throws IOException`，否则编译不过。Kotlin 取消了这一套——所有异常都像 Java 的 `RuntimeException` 一样，捕不捕获完全自愿。如果确实需要让 Java 调用方看到 `throws` 声明，用 `@Throws(IOException::class)` 标注（见 10.7），否则 Java 代码调用 Kotlin 方法时不会知道可能抛什么。

### 4.1 `when`

```kotlin
val text = when (type) {
    0 -> "气泡漂浮动画"
    1 -> "波浪动画"
    else -> "未知"
}
```

也可以不带参数，替代 `if / else if / else`：

```kotlin
val sticky = when {
    index < 6 -> "吸顶文本1"
    index < 12 -> "吸顶文本2"
    index < 18 -> "吸顶文本3"
    else -> "吸顶文本4"
}
```

当 `when` 作为表达式返回值时，必须覆盖所有可能情况。普通 `Int/String` 通常需要 `else`；枚举和密封类型覆盖全部分支后可以省略 `else`。

> 版本提醒：即使 `when` 只作为**语句**使用（不取值），从 Kotlin 1.7 起，对枚举和密封类型的 `when` 也会强制要求穷尽（1.7 先是警告，后续版本升级为错误）。所以"不取值就可以不写 `else`"只在普通 `Int/String` 上成立，对枚举/密封类型仍建议写全分支或保留 `else`，避免升级后编译报错。

### 4.2 区间

| 写法 | 含义 |
| --- | --- |
| `1..10` | 1 到 10，包含 10 |
| `1 until 10` | 1 到 9，不包含 10 |
| `10 downTo 1` | 10 到 1，倒序 |
| `1..10 step 2` | 1、3、5、7、9 |

```kotlin
for (i in 0 until 40) {
    println(i) // 0..39
}
```

### 4.3 `if` 也是表达式

Kotlin 没有 Java 的三元运算符 `条件 ? A : B`，直接使用 `if` 表达式：

```kotlin
val max = if (a > b) {
    a
} else {
    b
}
```

每个分支最后一条表达式作为结果，因此作为表达式使用时必须有 `else`。

### 4.4 类型判断和智能转换

```kotlin
fun printLength(value: Any) {
    if (value is String) {
        // 判断成功后，value 自动被当作 String
        println(value.length)
    }
}
```

智能转换只在编译器能保证变量不会被其他代码修改时生效。可变属性、存在自定义 getter 的属性或可能并发变化的值，通常需要先保存到局部 `val`。

不确定转换能否成功时，使用 `as?`：

```kotlin
val text: String? = value as? String
val length = text?.length ?: 0
```

`as` 转换失败会抛 `ClassCastException`，`as?` 转换失败返回 `null`。

如果把 `null` 强制转换成非空类型，运行时同样会失败；可空值不要用 `as`“赌”它一定非空，优先使用 `as?`、安全调用或显式校验。

### 4.5 `while`、`break`、`continue` 和标签返回

`while`、`do-while` 与 Java 基本一致：

```kotlin
val items = listOf("A", "B", "C")
var index = 0

while (index < items.size) {
    index++
}

do {
    println("至少执行一次")
} while (false)
```

普通循环可以使用 `break` 和 `continue`：

```kotlin
val items: List<String?> = listOf("A", null, "target")

for (item in items) {
    if (item == null) continue // 跳过本次
    if (item == "target") break // 结束整个循环
}
```

Lambda 中常见的是带标签的局部返回：

```kotlin
val items: List<String?> = listOf("A", null, "B")

items.forEach { item ->
    if (item == null) {
        return@forEach // 只结束本次 Lambda，效果近似 continue
    }

    println(item)
}
```

初学时如果标签、嵌套 Lambda 或非局部返回让流程难以理解，改用普通 `for` 循环通常更清楚。

### 4.6 `try` 也是表达式

```kotlin
val number: Int? = try {
    text.toInt()
} catch (e: NumberFormatException) {
    null
}
```

`try` 或 `catch` 分支的最后一条表达式作为结果；`finally` 用于清理资源，不参与表达式返回值。

如果只是把字符串安全转换成数字，优先使用标准库现成函数：

```kotlin
val number = text.toIntOrNull()
```

### 4.7 主动校验和抛错

```kotlin
fun updateAge(age: Int) {
    require(age >= 0) {
        "age 不能小于 0"
    }
}
```

| 函数 | 失败时异常 | 常用场景 |
| --- | --- | --- |
| `require(condition)` | `IllegalArgumentException` | 校验调用者传入的参数 |
| `check(condition)` | `IllegalStateException` | 校验当前对象/流程状态 |
| `requireNotNull(value)` | `IllegalArgumentException` | 参数必须非空 |
| `checkNotNull(value)` | `IllegalStateException` | 当前状态中的值必须非空 |
| `error(message)` | `IllegalStateException` | 代码走到了不应出现的分支 |

`throw` 在 Kotlin 中是表达式，类型为 `Nothing`：

```kotlin
val user = findUser(id)
    ?: throw IllegalArgumentException("用户不存在：$id")
```

`Nothing` 表示该表达式永远不会正常返回；抛异常、无限循环等场景会使用这个类型。

### 4.8 `use` 自动关闭资源

实现 `Closeable` 的资源优先使用 `use`，类似 Java try-with-resources：

```kotlin
FileInputStream(file).use { input ->
    val bytes = input.readBytes()
    println(bytes.size)
} // 离开代码块后自动 close
```

不要用异常代替正常分支判断，也不要捕获 `Throwable` 后完全忽略。

## 5. 函数、Lambda 与扩展

### 5.1 普通函数

Java：

```java
public int add(int a, int b) {
    return a + b;
}
```

Kotlin：

```kotlin
fun add(a: Int, b: Int): Int {
    return a + b
}
```

### 5.2 单表达式函数

```kotlin
fun add(a: Int, b: Int): Int = a + b
```

这个 `=` 是"直接返回结果"的缩写，等价于 Java 里的 `{ return a + b; }`：

```java
// Java 等价写法
int add(int a, int b) { return a + b; }
```

当函数体只有一行时，用 `=` 更简洁；函数体有多行、需要多个语句时，用花括号 `{ ... }` 并显式 `return`。

返回类型可以由编译器推断：

```kotlin
fun add(a: Int, b: Int) = a + b
```

项目中的例子：

```kotlin
fun getData(): List<HoverItemModel> =
    (0 until MODEL_COUNT).map { index ->
        val sticky = when {
            index < 6 -> "吸顶文本1"
            index < 12 -> "吸顶文本2"
            index < 18 -> "吸顶文本3"
            else -> "吸顶文本4"
        }
        HoverItemModel(sticky, "name:$index")
    }
```

等价 Java：

```java
public List<HoverItemModel> getData() {
    List<HoverItemModel> list = new ArrayList<>();
    for (int index = 0; index < MODEL_COUNT; index++) {
        String sticky;
        if (index < 6) {
            sticky = "吸顶文本1";
        } else if (index < 12) {
            sticky = "吸顶文本2";
        } else if (index < 18) {
            sticky = "吸顶文本3";
        } else {
            sticky = "吸顶文本4";
        }
        list.add(new HoverItemModel(sticky, "name:" + index));
    }
    return list;
}
```

### 5.3 默认参数、命名参数和 `Unit`

默认参数可以减少 Java 中常见的重载：

```kotlin
fun showMessage(
    text: String,
    duration: Int = 2,
    important: Boolean = false
) {
    println("$text, duration=$duration, important=$important")
}

showMessage("hello") // 使用两个默认值
showMessage("hello", 5) // duration=5

// 命名参数可以跳过中间的默认参数
showMessage(text = "hello", important = true)
```

调用 Kotlin 函数时可以使用命名参数。对 Java 方法，Kotlin 1.8 以前一律不能；**Kotlin 1.8+ 仅当 Java 代码以 `-parameters` 编译并保留了参数名时才可用命名参数**（Kotlin 1.8.0 起支持读取 Java 参数名）。由于第三方 Java 库是否带参数名不可控，实战中不要依赖这一能力，仍以位置参数 + `@JvmOverloads` 重载为主。

没有有意义返回值的 Kotlin 函数返回 `Unit`：

```kotlin
fun logMessage(text: String): Unit {
    println(text)
}
```

`Unit` 类似 Java 的 `void`，但 `Unit` 是真实类型，并且只有一个值 `Unit`。返回类型为 `Unit` 时通常省略不写。

### 5.4 Lambda 和点击事件

使用 ViewBinding 的推荐写法：

```kotlin
binding.cancelFingerBtn.setOnClickListener {
    FingerprintUtil.cancel()
}
```

等价 Java：

```java
binding.cancelFingerBtn.setOnClickListener(v -> {
    FingerprintUtil.cancel();
});
```

不要这样写：

```kotlin
binding.cancelFingerBtn.setOnClickListener {
    View.OnClickListener { FingerprintUtil.cancel() }
}
```

这表示“点击时创建一个新的 listener 对象”，但没有执行 `cancel()`。

显式 listener 写法：

```kotlin
binding.cancelFingerBtn.setOnClickListener(
    View.OnClickListener {
        FingerprintUtil.cancel()
    }
)
```

### 5.5 函数类型和高阶函数

函数可以像普通值一样保存到变量、作为参数传递或作为结果返回。

给 Java 读者的锚点：`(Int, Int) -> Int` 这种**函数类型**，可以理解成 Java 里"只有一个方法的接口/回调"，比如 `BiFunction<Integer, Integer, Integer>` 或你自己定义的 `interface Calculator { int calc(int a, int b); }`。区别是 Kotlin 不用真的定义接口，直接用 `(参数类型) -> 返回类型` 描述这个"形状"，然后传一个 Lambda 进去。这就是所谓的高阶函数——"接收函数作为参数的函数"，本质和你熟悉的"传一个 Callback 对象"是一回事，只是写法轻得多。

```kotlin
// (Int, Int) -> Int 表示：
// 接收两个 Int，返回一个 Int
val operation: (Int, Int) -> Int = { a, b ->
    a + b
}

val result = operation(2, 3) // 5
```

接收函数参数的函数称为高阶函数：

```kotlin
fun calculate(
    a: Int,
    b: Int,
    operation: (Int, Int) -> Int
): Int {
    return operation(a, b)
}

val sum = calculate(2, 3) { left, right ->
    left + right
}
```

如果 Lambda 只有一个参数，可以用默认名称 `it`：

```kotlin
val lengths = listOf("A", "Hello").map {
    it.length
}
```

Lambda 最后一条表达式就是返回值，不写 `return`。

### 5.6 扩展函数和扩展属性

扩展函数可以在不继承、不修改原类源码的情况下，为某个类型提供便捷调用形式。

给 Java 读者的锚点：`fun String.lastCharOrNull()` 编译后其实就是个静态方法 `StringUtils.lastCharOrNull(String s)`，第一个参数是接收者（被扩展的那个对象），只是调用时写成了 `s.lastCharOrNull()`，看起来像 `String` 自己的方法。所以扩展函数≈Java 的 `StringUtils`、`CollectionUtils` 那种工具类，只是调用形式更自然。这也解释了后面的限制：它没法访问原类的 `private` 成员，因为它本质只是外部的一个静态方法。

**接收者**（receiver）指的就是被扩展的那个对象，在函数体里用 `this` 指代它。

```kotlin
fun String.lastCharOrNull(): Char? {
    return lastOrNull()
}

val value = "Kotlin".lastCharOrNull() // 'n'
```

`String` 是接收者类型，函数体中的 `this` 表示调用该函数的字符串。

可空接收者扩展可以自己处理 `null`：

```kotlin
fun String?.orEmptyText(): String {
    return this ?: ""
}

val value = null.orEmptyText() // ""
```

扩展属性用同样的语法定义，但**不能有 backing field**。**backing field** 指编译器给属性自动生成的那个存值的私有字段，Java 里你手写的 `private String name;` 就是它；扩展属性不在类内部，没有地方存这个字段，所以值只能现算或转存到别处。注意区分两个概念："没有 backing field"不等于"不能有 setter"——

```kotlin
val String.lastChar: Char?
    get() = lastOrNull()       // val 扩展属性：只能定义 getter

val c = "Kotlin".lastChar // 'n'
```

写成 `var` 的扩展属性**可以同时定义 getter 和 setter**，只是 setter 里不能写 `field = ...`（没有这个字段），必须把值存到别的地方：

```kotlin
// 用一个外部 Map 代为存储，演示 var 扩展属性可以有两个访问器
private val extraLabels = mutableMapOf<String, String>()

var String.label: String
    get() = extraLabels[this] ?: ""
    set(value) { extraLabels[this] = value }
```

日常绝大多数扩展属性都是 `val` + 现算的 `get()`，但要知道"不能有 setter"是个常见误解，真实限制只在 backing field。

> 下面这段涉及 `apply` 等作用域函数，如果还没学过可以先跳过，等看完第 6 章再回来理解。

当扩展函数内部有 lambda 或嵌套接收者（如 `apply`、`run`）时，`this` 会被内层接收者遮蔽，可用 `this@扩展函数名` 明确指向扩展接收者：

```kotlin
fun String.appendWith(label: String) = StringBuilder().apply {
    append(this@appendWith) // 这里的 this 是 StringBuilder，this@appendWith 才是 String
    append(label)
}
```

扩展函数的重要限制：

1. 扩展不会真的修改原类，也不能访问原类的 `private/protected` 成员。
2. 扩展函数是静态分派，调用哪个扩展取决于变量的编译期类型，不是对象的运行时类型。
3. **扩展不能被子类重写**：子类可以定义同名扩展，但那是一个新的静态分派扩展，用父类类型引用调用时仍执行父类版本，不存在 `open`/`override` 语义。
4. 如果成员函数和扩展函数签名相同，成员函数优先。
5. 扩展属性不能保存状态，因此不能有初始化值和独立 backing field。

### 5.7 `vararg`、默认参数和函数引用

`vararg` 允许调用方传入零个或多个参数；函数内部拿到的是数组（定义与 `*` 展开见 2.2.5，默认参数见 5.3）：

```kotlin
fun join(separator: String = ", ", vararg values: String): String =
    values.joinToString(separator)

join(values = *arrayOf("A", "B"))
join(" / ", "A", "B", "C")
```

一个函数最多只能有一个 `vararg` 参数。调用已有函数时，可以用 `::函数名` 取得函数引用（`::` 符号见 1.3 章）：

```kotlin
val printer: (String) -> Unit = ::println
listOf("A", "B").forEach(printer)

val lengths = listOf("A", "Hello").map(String::length)
```

成员函数引用通常需要对象实例：`user::toDisplayName`；类型函数引用如 `String::length` 会把接收者当作第一个参数；`::println` 这类顶层函数引用可直接当函数值传给高阶函数。

## 6. 作用域函数与标准库工具

`apply`、`let`、`also`、`run`、`with` 不是关键字，是 Kotlin 标准库函数，通常叫作用域函数。

**白话解释**：作用域函数就是"给一个对象临时开个小范围，在这个花括号里可以少写重复的对象名"——里面要么用 `this` 指代它、要么用 `it` 拿到它，出了花括号就回到外面的世界。Java 里你写 `builder.setA().setB().setC()` 那种链式配置，在 Kotlin 里用 `apply` 写更省事。

给 Java 读者的锚点：

- `apply` ≈ 链式 Builder，但花括号里直接访问属性，结尾自动 `return this`（不需要你写 `return this`）。
- `also` ≈ 在链式调用中间插一句日志/检查，做完原样把对象传下去。
- `let` ≈ `if (x != null) { 用 x 做点事 }`，最常用于空安全后的处理。

| 函数 | 内部对象名 | 返回值 | 常用场景 |
| --- | --- | --- | --- |
| `apply` | `this` | 原对象 | 初始化 / 配置对象 |
| `also` | `it` | 原对象 | 顺便做日志、调试、副作用 |
| `let` | `it` | Lambda 最后一行 | 判空后处理、转换结果 |
| `run` | `this` | Lambda 最后一行 | 在对象作用域内计算结果 |
| `with` | `this` | Lambda 最后一行 | 对已有对象集中操作 |

### 6.1 `apply`

```kotlin
val textView = TextView(this).apply {
    text = "hello"
    textSize = 16f
}
```

等价：

```kotlin
val textView = TextView(this)
textView.text = "hello"
textView.textSize = 16f
```

项目常见：

```kotlin
mAdapter = MainAdapter(getData()).apply {
    setOnItemClickListener(this@MainActivity)
}
```

这里 `this@MainActivity` 等价 Java 的：

```java
MainActivity.this
```

### 6.2 `also`

```kotlin
val users = getUsers()
    .also { Log.i("TAG", "size=${it.size}") }
    .filter { it.name != null }
```

`also` 返回原对象，适合在链式调用中插入日志。

### 6.3 `let`

```kotlin
val length: Int? = name?.let {
    it.length
}
```

如果 `name == null`，不执行，返回 `null`。

如果有多行，返回最后一行：

```kotlin
val result = name?.let {
    it.length
    Log.i("TAG", "onCreate")
}
```

这里 `result` 是 `Log.i()` 的返回值，不是 `it.length`。

如果想返回长度：

```kotlin
val result = name?.let {
    Log.i("TAG", "onCreate")
    it.length
}
```

### 6.4 `run`

```kotlin
val isValid = editText.run {
    text.isNotEmpty() && visibility == View.VISIBLE
}
```

`run` 内部用 `this`，返回最后一行。

### 6.5 `with`

```kotlin
with(textView) {
    text = "hello"
    textSize = 16f
}
```

适合对已有对象集中设置。

### 6.6 怎么选

| 需求 | 推荐 |
| --- | --- |
| 创建对象后设置属性 | `apply` |
| 链式调用中插入日志 | `also` |
| 可空对象非空时执行 | `?.let {}` |
| 把对象转换成另一个结果 | `let` |
| 在对象内部计算结果 | `run` |
| 对已有对象集中操作 | `with` |

不要为了“看起来 Kotlin”而过度嵌套：

```kotlin
user?.let {
    adapter?.also {
        view?.apply {
            // this、it 混在一起，容易读错
        }
    }
}
```

### 6.7 作用域函数之外的标准库工具

`takeIf`、`takeUnless` 可以把条件判断转换成可空结果：

```kotlin
val port = input.toIntOrNull()
    ?.takeIf { it in 1..65535 }
```

`repeat` 适合明确次数的重复操作：

```kotlin
repeat(3) { index ->
    println("第 $index 次")
}
```

`runCatching` 可以把异常转换成 `Result`，但协程中不要吞掉 `CancellationException`：

```kotlin
val result: Result<Int> = runCatching { text.toInt() }
val value = result.getOrElse { 0 }
```

常用 `Result` API 还有 `isSuccess`、`isFailure`、`getOrNull()`、`exceptionOrNull()`、`fold(onSuccess, onFailure)`、`onFailure {}`、`getOrThrow()`。它适合表达一个操作的成功/失败结果，不应替代所有领域错误类型。

几个限制要记住：

- `Result<T>` 的唯一位置限制是**不能作为 `vararg` 元素类型**（`vararg r: Result<String>` 编译报错 `forbidden vararg parameter type`）。除此之外，函数参数、函数类型内部（`(Result<User>) -> Unit`）、属性类型、`typealias`、返回类型、局部变量在 Kotlin 1.8 都合法。
  - 版本提醒：**Kotlin 1.5 之前** `Result` 限制很多（不能作返回类型、函数参数等），网上很多旧教程和旧问答仍写着"Result 不能当函数参数"。这些规则**从 1.5 起已全部解除**（KEEP #244）；本工程是 1.8.22，按新规则即可。`Result` 本质是 `@JvmInline` 值类，值类不能做 `vararg` 元素，这才是唯一残留的限制。
- 协程里用 `runCatching` / `catch` 时，如果捕获了 `CancellationException` 必须重新抛出，否则会破坏协程取消机制（与 12.4 呼应）。
- `Result` 表达的是"这次调用是否抛异常"，不等于业务上的成功/失败；需要携带领域语义时用密封类（如 7.5 的 `UiState`）更清晰。

## 7. 类、继承、构造

### 7.1 `open class`

Java 类默认可继承：

```java
public class Person {}
public class Student extends Person {}
```

Kotlin 类默认 `final`：

```kotlin
class Person
// class Student : Person() // 报错
```

允许继承必须加 `open`：

```kotlin
open class Person
class Student : Person()
```

### 7.2 `open fun`

即使类是 `open`，方法默认也不能重写。

```kotlin
open class Person {
    open fun say() {}
    fun run() {}
}

class Student : Person() {
    override fun say() {}
    // override fun run() {} // 报错
}
```

`override` 的成员默认仍然可以被下一层子类继续重写；如果要禁止继续重写，使用 `final override`：

```kotlin
open class Parent {
    open fun show() {}
}

open class Child : Parent() {
    final override fun show() {}
}
```

`abstract` 类和抽象成员本身就是可继承/可重写的，不需要再写 `open`。

`public` 控制访问权限，`open` 控制能否继承 / 重写，两者不是一回事。

```kotlin
open class Person {
    public fun test1() {}       // 外部可访问，但不能重写
    open fun test2() {}         // 外部可访问，也能重写
    protected open fun test3() {} // 子类可访问，也能重写
}
```

`private open` 不合法，因为 `private` 表示子类不可见，`open` 表示允许子类重写，语义冲突。

```kotlin
// private open fun test() {} // 不合法
```

### 7.3 构造函数

```kotlin
class User(val name: String, var age: Int)
```

这一行是 Kotlin 最常用的写法，等价于下面这一大段 Java：

```java
// Java：字段 + 全参构造 + getter（+ age 的 setter）
public class User {
    private final String name;
    private int age;
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName() { return name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
```

**两个必须先弄清的概念**：

- **主构造函数**：类名后面那对括号 `(val name: String, var age: Int)` 就是主构造函数，创建对象时最先执行。括号里写 `val`/`var` 才会同时生成属性（字段 + getter/setter），这是 Kotlin 的"主构造参数属性"。
- **普通参数 vs 属性**：如果括号里只写 `name: String`（不带 `val`/`var`），它只是构造参数，不会生成属性，只能在 `init` 块或属性初始化器里使用，对象创建后访问不到 `user.name`。Java 开发者容易误以为括号里全是字段，这里要特别留意。

需要初始化逻辑时：

```kotlin
class User(val name: String) {
    init {
        println("name=$name")
    }
}
```

`init` 块是"构造时自动执行的一段初始化代码"，紧跟主构造参数赋值运行，可以写多个、按源码顺序执行。它大致相当于 Java 构造函数体里那段赋值和校验逻辑。

次构造：

```kotlin
class User {
    var name: String? = null

    constructor(name: String) {
        this.name = name
    }
}
```

### 7.4 可见性修饰符

| 修饰符 | 类成员 | 顶层声明 |
| --- | --- | --- |
| `public` | 任何可见该类的地方都能访问 | 任何地方都能访问 |
| `private` | 仅当前类内部 | 仅当前 `.kt` 文件 |
| `protected` | 当前类及其子类 | 顶层声明不能使用 |
| `internal` | 当前 Gradle/Kotlin 编译模块 | 当前模块 |

Kotlin 没有 Java 的 package-private。还要注意：Java 的 `protected` 允许同包访问，而 Kotlin 的 `protected` 不提供同包访问。

### 7.5 `enum class` 和 `sealed class`

> 术语统一：下文"密封类 / 密封接口"都指 `sealed class` / `sealed interface`，两者只是声明形式不同，作用一致（限制子类只能在编译模块内定义，便于 `when` 穷尽检查）。

> **给 Java 读者的痛点铺垫**：Java 里表示"有限几种状态、每种带不同数据"，通常用抽象基类 + 几个子类，但 `switch`/`when` 时编译器不知道"就这几种"，你被迫写 `default`/`else`。`sealed`（密封）类/接口的价值就是：告诉编译器"子类就这些、别处不能加"，于是 `when` 能检查是否穷尽，漏了一种就编译报错，不用再写 `else`。这是它最实用的地方。
>
> 下面示例里的 `object` 和 `data class` 分别在第 8、9 章才讲，这里先当语法看，知道"Loading 是个单例对象、Success/Error 是携带数据的数据类"即可。

枚举适合表示固定的一组无额外类型层级的常量：

```kotlin
enum class LoadState {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}
```

**枚举的常用成员**（和 Java 枚举对应）：

```kotlin
val state = LoadState.LOADING

println(state.name)          // "LOADING"，常量名
println(state.ordinal)       // 1，从 0 开始的序号（尽量别依赖，插入常量会变）
println(LoadState.values())  // 数组，列出全部常量（本工程 Kotlin 1.8 用这个）
println(LoadState.valueOf("IDLE")) // 按名字取常量，名字不存在会抛异常

for (s in LoadState.entries) println(s) // 1.9+ 提供的更轻量集合版，1.8 暂不可用
```

> 版本提醒：`entries` 从 Kotlin 1.9 起提供，性能优于每次创建新数组的 `values()`。本工程当前是 1.8.22，请用 `values()`；升级到 1.9+ 后可逐步替换为 `entries`。`ordinal` 会随常量顺序变化，适合调试但不适合持久化到数据库或网络。

`enum class` 可以像 Java 枚举一样实现接口，并把不同行为分散到各常量上（策略分发的常见写法）：

```kotlin
interface Shape {
    fun area(): Double
}

enum class Size : Shape {
    SMALL   { override fun area() = 1.0 },
    LARGE   { override fun area() = 4.0 };
}
```

选型口诀：状态固定且**不带额外数据** → `enum`；状态固定但**每种携带不同数据** → `sealed`。两者都能让 `when` 穷尽检查生效，不必写 `else`。

密封类/接口适合表示“有限种、但每种携带的数据不同”的状态：

```kotlin
sealed interface UiState {
    object Loading : UiState
    data class Success(val text: String) : UiState
    data class Error(val throwable: Throwable) : UiState
}

fun render(state: UiState) {
    when (state) {
        UiState.Loading -> println("加载中")
        is UiState.Success -> println(state.text)
        is UiState.Error -> println(state.throwable.message)
    }
}
```

这里 `when` 已覆盖所有直接实现，因此不需要 `else`。密封类/接口的直接子类必须有名称，不能是局部类或匿名对象。所在位置的规则随 Kotlin 版本放宽：Kotlin 1.4 及以前要求与密封类在同一个文件；1.5 起放宽为同一模块同一包（本工程 Kotlin 1.8 适用此规则）；Kotlin 2.0（K2）起进一步放宽为同一模块即可，可不同包。无论哪个版本，其他模块都不能增加直接子类，这正是 `when` 能穷尽判断的前提。

### 7.6 泛型基础、`out`、`in` 和星投影

> **先别慌**：这一节是全书较抽象的部分。Java 读者把 `out`/`in` 理解成 Kotlin 版的 `? extends T` / `? super T` 即可，第一遍可以先掌握结论（"只往外拿数据用 out，只往里塞数据用 in"），细节用到再回来查。

泛型用类型参数表示“这里可以替换成某种具体类型”，和 Java 的 `Box<T>` 一样：

```kotlin
class Box<T>(
    val value: T
)

val stringBox = Box("hello") // 编译器推断为 Box<String>
val intBox = Box(10)         // Box<Int>
```

泛型函数把输入类型和输出类型关联起来：

```kotlin
fun <T> firstOrNull(items: List<T>): T? {
    return items.firstOrNull()
}

val name: String? = firstOrNull(listOf("Jack", "Lucy"))
```

需要限制 `T` 的能力时，添加上界约束：

```kotlin
fun <T : Comparable<T>> maxOfTwo(first: T, second: T): T {
    return if (first >= second) first else second
}
```

多个约束可以使用 `where`：

```kotlin
fun <T> save(value: T)
    where T : CharSequence,
          T : Comparable<T> {
    println(value)
}
```

Java 的泛型常在使用位置写 `? extends`、`? super`；Kotlin 还允许在类型声明处说明这个泛型主要负责生产还是消费：

```kotlin
interface Producer<out T> {
    fun produce(): T
}

interface Consumer<in T> {
    fun consume(value: T)
}
```

记忆：

- `out T`：主要负责生产/返回 `T`，类似 Java `? extends T`。
- `in T`：主要负责消费/接收 `T`，类似 Java `? super T`。
- `MutableList<T>` 默认不协变，不能把 `MutableList<Dog>` 当成 `MutableList<Animal>`，否则可能错误地塞入其他动物。

星投影 `*` 的读写规则可以按"协变读、逆变写"记忆：

- `Producer<*>`（`out T` 的星投影）：可以安全读取，结果类型为 `Any?`；因为只出不进，类型未知也不影响读取。
- `Consumer<*>`（`in T` 的星投影）：通常无法安全写入。Kotlin 会把 `in` 星投影的入参类型当作 `Nothing`，因此任何实参都传不进去（编译期即失败）。
- `Box<*>`（不型变的星投影）：读取为 `Any?`，任何值都写不进去（包括 `null`）——写入位置的类型是 `Nothing`，它没有任何实例，连 `null` 也不是 `Nothing` 的实例（`null` 属于 `Nothing?`）。
- 常见写法 `List<*>` 属于第一种，遍历时元素按 `Any?` 处理。

### 7.7 接口属性、抽象类与实现约束

接口可以声明属性，但不能保存实例状态；实现类必须提供实现：

```kotlin
interface HasId {
    val id: Long
}

abstract class BaseRepository : HasId {
    abstract override val id: Long

    fun logId() = println(id)
}

class UserRepository(override val id: Long) : BaseRepository()
```

抽象类适合共享状态或部分实现，接口适合描述能力/契约。一个类可以实现多个接口，但只能继承一个类。重写属性时，`val` 可以被重写为 `var`，反过来不行，因为可写属性需要额外满足 setter 契约。

初始化顺序通常是：主构造参数求值 → 父类初始化 → 子类属性初始化器和 `init` 代码块（严格按源码出现顺序）→ 次构造函数体。注意主构造参数（含默认参数表达式）在父类构造之前求值，父类构造中的 `open` 成员调用可能读到尚未初始化的子类属性。不要在父类构造阶段调用可被子类重写的成员。

## 8. 属性、`lateinit`、`lazy`

### 8.1 属性和 Java getter / setter

Kotlin：

```kotlin
class User {
    var name: String? = null
}
```

Kotlin 调用：

```kotlin
user.name = "jack"
println(user.name)
```

Java 调用：

```java
user.setName("jack");
System.out.println(user.getName());
```

属性可以自定义 getter/setter。`field` 表示该属性自己的 backing field：

```kotlin
class User {
    var name: String = ""
        set(value) {
            // 赋值前去掉首尾空格
            field = value.trim()
        }

    val displayName: String
        get() = if (name.isEmpty()) "匿名用户" else name
}
```

如果希望“类内部可修改、外部只能读取”，常用 backing property：

```kotlin
class UserRepository {
    private val _users = mutableListOf<User>()

    // 外部只能拿到 List 接口，不能直接 add/remove
    val users: List<User>
        get() = _users

    fun addUser(user: User) {
        _users.add(user)
    }
}
```

对于"外部只读、内部可改"的简单属性，还有更短的写法——给 `var` 的 setter 加 `private`：

```kotlin
class UserViewModel {
    var count: Int = 0
        private set   // 类内部可写 count++，外部只能读 count

    fun increment() {
        count++       // 类内部允许
    }
}
// viewModel.count = 5  // 编译报错：外部不能调用 private setter
```

这比"私有 backing property + 公开 getter"简洁得多，是日常最常用的一招；只有需要在读取时做额外处理（如返回只读集合视图）时才用 `_xxx` 那套。

### 8.2 `lateinit`

`lateinit` 表示：声明时暂不初始化，由开发者承诺在第一次读取前完成赋值。编译器不会替你证明这个承诺。

常用于 Android ViewBinding、依赖注入或测试初始化：

```kotlin
private lateinit var binding: ActivityMainBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.recyclerView.adapter = adapter
}
```

限制：

| 限制 | 示例 |
| --- | --- |
| 只能用于 `var` | `lateinit var name: String` |
| 不能用于 `val` | `lateinit val name: String` 不合法 |
| 属性类型必须非空 | `lateinit var name: String?` 不合法 |
| 不能用于 `Int` 等 JVM 基本类型对应类型 | `lateinit var age: Int` 不合法 |
| 不能有自定义 getter / setter | `lateinit` 依赖 backing field 保存稍后赋的值 |
| 使用前必须赋值 | 否则抛 `UninitializedPropertyAccessException` |
| 不能在构造阶段访问 | 构造函数、`init` 块或属性初始化器中读取尚未赋值的 `lateinit` 属性会失败；构造阶段就要用的值应改用普通初始化或 `by lazy` |

**基本类型怎么办？** `lateinit` 不能用于 `Int`、`Boolean` 等基本类型，这时用 `Delegates.notNull()` 作为替代：

```kotlin
import kotlin.properties.Delegates

var count: Int by Delegates.notNull()
```

它与 `lateinit` 的核心区别：`lateinit` 是局部属性、有 `::count.isInitialized` 可检查；`Delegates.notNull()` 是委托属性、**没有** `isInitialized` 检查，读取未赋值时抛 `IllegalStateException`。它支持基本类型，也支持顶层属性。如果只是想延迟初始化且能用 `lazy`，优先用 `by lazy`（见 8.3）。

检查 `lateinit` 是否初始化：

```kotlin
if (::binding.isInitialized) {
    binding.recyclerView.adapter = adapter
}
```

Activity 的 ViewBinding 通常可以使用 `lateinit`。Fragment 的 View 生命周期短于 Fragment 对象生命周期，不应长期保存一个无法清空的 `lateinit binding`，推荐可空 backing property：

```kotlin
private var _binding: FragmentHomeBinding? = null

// 仅允许在 onCreateView() 到 onDestroyView() 之间访问
private val binding: FragmentHomeBinding
    get() = requireNotNull(_binding)

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
}

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null // 解除对旧 View 层级的引用，避免泄漏
}
```

### 8.3 `by lazy`

`lazy` 表示第一次访问时才初始化，并且只初始化一次。

先解释 `by` 这个新符号：它是**属性委托**的关键字，`val title by lazy { ... }` 的意思是"title 的取值交给 `lazy` 这个委托对象代管——第一次读它时才执行大括号里的代码算出来，之后缓存结果"。`by` 后面跟的是"谁来代管"，不是 `lazy` 的修饰符。这个概念到第 11.4 节会系统展开，这里先按"交给 lazy 代管"理解即可。Java 里没有对应的语法，最接近的心智模型是"手写了一个带缓存的 getter"。

```kotlin
val title by lazy {
    "hello"
}
```

`lazy` 有三种线程安全模式，默认 `SYNCHRONIZED` 用同步锁保证多线程下只初始化一次：

```kotlin
val title by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { "hello" } // 默认
val fast by lazy(LazyThreadSafetyMode.NONE) { "hello" }          // 无锁
val published by lazy(LazyThreadSafetyMode.PUBLICATION) { "hello" } // 并发初始化，取首个发布结果
```

- `SYNCHRONIZED`：默认，加锁，开销最高但最稳妥。
- `NONE`：不加锁，确定只在单线程（如 Android 主线程）访问时才用，不要仅为“更快”随意使用。
- `PUBLICATION`：允许多线程同时计算，但保证只发布一个结果；初始化逻辑无副作用、计算较重时可考虑。

适合能在表达式里完成初始化的 `val`。

| 写法 | 适合场景 |
| --- | --- |
| `lateinit var` | 生命周期中稍后赋值，例如 `onCreate()` 里初始化 ViewBinding |
| `val by lazy` | 第一次使用时再计算，之后不变 |

## 9. object、接口、伴生对象与数据类

### 9.1 `object` 单例

```kotlin
object DisplayUtils {
    fun getDisplayWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }
}
```

Kotlin 调用：

```kotlin
DisplayUtils.getDisplayWidth(context)
```

Java 调用默认是：

```java
DisplayUtils.INSTANCE.getDisplayWidth(context);
```

如果想让 Java 也像静态方法一样调用，加 `@JvmStatic`：

```kotlin
object DisplayUtils {
    @JvmStatic
    fun getDisplayWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }
}
```

Java：

```java
DisplayUtils.getDisplayWidth(context);
```

### 9.2 匿名对象：`object :`

Kotlin 的：

```kotlin
private val mHandle = object : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
    }
}
```

等价 Java：

```java
private final Handler mHandle = new Handler(Looper.getMainLooper()) {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }
};
```

`object : 父类或接口 {}` 表示创建一个匿名子类 / 匿名实现类对象。

### 9.3 接口：`interface` 和 `fun interface`

普通接口用 `interface` 声明，可以包含抽象方法，也可以包含有默认实现的方法。

```kotlin
interface OnItemClickListener {
    fun onItemClick(position: Int)

    fun onLongClick(position: Int) {
        // 默认实现，子类可以不重写
    }
}
```

实现接口时，常见写法有两种。

写成普通类：

```kotlin
class UserAdapter : OnItemClickListener {
    override fun onItemClick(position: Int) {
        println(position)
    }
}
```

写成匿名对象：

```kotlin
val listener = object : OnItemClickListener {
    override fun onItemClick(position: Int) {
        println(position)
    }
}
```

如果接口只有一个抽象方法，可以用 `fun interface`。这种接口也叫函数式接口，Kotlin 允许用 Lambda 直接创建它的实现。

```kotlin
fun interface BindItemTextCallback {
    fun getItemText(position: Int): String?
}
```

完整匿名对象写法：

```kotlin
val callback = object : BindItemTextCallback {
    override fun getItemText(position: Int): String? {
        return userBeans[position].sortLetters
    }
}
```

函数式接口的 Lambda 写法：

```kotlin
val callback = BindItemTextCallback { position ->
    userBeans[position].sortLetters
}
```

当函数或构造函数的最后一个参数是函数类型 / `fun interface` 时，Kotlin 允许把这个 Lambda 放到括号外，叫尾随 Lambda。

```kotlin
HoverItemDecoration(this) { position ->
    userBeans[position].sortLetters
}
```

等价于：

```kotlin
HoverItemDecoration(
    this,
    BindItemTextCallback { position ->
        userBeans[position].sortLetters
    }
)
```

也等价于更完整的匿名对象写法：

```kotlin
HoverItemDecoration(
    this,
    object : BindItemTextCallback {
        override fun getItemText(position: Int): String? {
            return userBeans[position].sortLetters
        }
    }
)
```

理解这类写法时，先看构造函数参数：

```kotlin
open class HoverItemDecoration(
    private val context: Context,
    private val bindItemTextCallback: BindItemTextCallback
)
```

所以：

```kotlin
HoverItemDecoration(this) { position -> userBeans[position].sortLetters }
```

并不是只传了一个参数，而是：

| 参数 | 实际传入 | 说明 |
| --- | --- | --- |
| `context` | `this` | 当前 `Activity` |
| `bindItemTextCallback` | `{ position -> userBeans[position].sortLetters }` | 根据列表位置返回分组文字 |

记忆方式：

```kotlin
函数(普通参数) { 最后一个 Lambda 参数 }
```

常见场景：

```kotlin
button.setOnClickListener { view ->
    println(view.id)
}
```

等价于：

```kotlin
button.setOnClickListener(
    View.OnClickListener { view ->
        println(view.id)
    }
)
```

注意：

| 写法 | 适用场景 |
| --- | --- |
| `interface` | 多个抽象方法，或不想限制为 Lambda |
| `fun interface` | 只有一个抽象方法，希望支持 Lambda |
| `object : 接口 {}` | 需要实现多个方法，或实现逻辑较复杂 |
| `接口名 { 参数 -> ... }` | `fun interface` 的简洁实现 |
| `函数(...) { ... }` | 最后一个参数是 Lambda / 函数式接口 |

### 9.4 `companion object`

`companion object` 是类里的伴生对象，常用来放 Java 中的 `static` 常量或方法。

**一个容易误解的点**：`companion object` 本身其实是一个真实的对象（编译后会生成 `Companion` 单例），并不是把所有成员都变成静态。只有 `const val`（编译期常量）和加了 `@JvmStatic` 的成员，才是 Java 眼里真正的静态字段/静态方法。Java 开发者最关心的对照直接给结论：

| Java 写法 | Kotlin 对应 |
| --- | --- |
| `static final String X = "a"`（编译期常量） | `const val X = "a"` |
| `static final Foo X = new Foo(...)`（运行时常量） | `@JvmField val X = Foo(...)` |
| `static void doSomething()` | `companion object` 里加 `@JvmStatic fun doSomething()` |

```kotlin
class HoverItemActivity3 : AppCompatActivity() {
    companion object {
        const val MODEL_COUNT = 40
    }
}
```

Kotlin 调用：

```kotlin
HoverItemActivity3.MODEL_COUNT
```

Java 调用 `const val` 常量通常也可以：

```java
HoverItemActivity3.MODEL_COUNT
```

普通方法如果希望 Java 直接静态调用：

```kotlin
class FingerprintUtil {
    companion object {
        @JvmStatic
        fun cancel() {}
    }
}
```

Java：

```java
FingerprintUtil.cancel();
```

如果想把伴生对象里的常量暴露为 Java 静态字段（而不是 `getXxx()` 取值），用 `@JvmField`。要求属性有 backing field 且非 `private`；**不能用于 `const val`（它本身已是静态字段、标注属冗余）、委托属性（如 `by lazy`）或有自定义 getter/setter 的属性**：

```kotlin
class Config {
    companion object {
        @JvmField val VERSION = "1.0"   // Java: Config.VERSION
        const val MAX = 40              // const val 本身就是静态字段
    }
}
```

`@JvmStatic` 生成静态方法，`@JvmField` 生成静态字段，两者用途不同，可分别使用。`@JvmField` 的完整限制条件和更多示例见 10.2，更多 Java 签名问题见 10.7。

### 9.5 `data class`

> **Java 读者的锚点**：`data class` 大致相当于 Lombok 的 `@Data`——自动帮你生成 getter/setter、`equals()`、`hashCode()`、`toString()`，此外 Kotlin 还额外生成 `copy()` 和 `componentN()`（解构用）。没用过 Lombok 的话，就理解成"编译器帮你写好了 POJO 的那些样板方法"。限制：主构造函数至少有一个参数，且参数必须标 `val` 或 `var`。

```kotlin
data class UserBean(
    var userName: String? = null,
    var sortLetters: String? = ""
)
```

根据主构造函数中声明为 `val/var` 的属性自动生成：

| 方法 | 作用 |
| --- | --- |
| `equals()` | 按字段比较 |
| `hashCode()` | 按字段生成 hash |
| `toString()` | 输出字段内容 |
| `copy()` | 复制并修改部分字段 |
| `componentN()` | 支持解构 |

类体中额外声明的属性不会参与自动生成的 `equals()`、`hashCode()`、`toString()`、`copy()` 和 `componentN()`。

```kotlin
data class User(val name: String) {
    var selected: Boolean = false
}

val a = User("Jack").apply { selected = false }
val b = User("Jack").apply { selected = true }

println(a == b) // true，selected 不在主构造函数中
```

示例：

```kotlin
val user1 = UserBean("jack", "J")
val user2 = user1.copy(userName = "tom")
```

`copy()` 是浅拷贝：主构造属性会被复制，但其中引用的可变对象不会自动深拷贝。

```kotlin
data class Team(val members: MutableList<String>)

val first = Team(mutableListOf("Jack"))
val second = first.copy()

second.members += "Lucy"
println(first.members) // [Jack, Lucy]，两者仍引用同一个 MutableList
```

还要避免把主构造中含可变 `var` 的数据类直接作为 `HashSet` 元素或 `HashMap` Key。对象放入后若修改了参与 `equals()/hashCode()` 的属性，集合可能无法再正确找到它。优先让这类标识属性保持 `val`，或使用稳定且不可变的 Key。

### 9.6 `data class` 和 `open class` 怎么选

| 写法 | 优点 | 风险 |
| --- | --- | --- |
| `open class` + `var` | 最接近 Java 普通 bean；保留可继承语义 | 不会自动生成基于属性的 `copy/toString/equals` |
| `data class` | 更 Kotlin；适合纯数据模型 | 默认 final；自动根据主构造属性生成相等与哈希语义 |

如果旧代码没有依赖 bean 被继承、引用相等或旧的 `HashMap/HashSet` Key 行为，`data class` 通常更适合纯数据模型。

### 9.7 `@JvmOverloads constructor`

Kotlin 默认参数：

```kotlin
data class UserBean(
    var userName: String? = null,
    var sortLetters: String? = ""
)
```

Kotlin 可以直接：

```kotlin
val user = UserBean()
```

由于所有主构造参数都有默认值，JVM 会自动生成公共无参构造。即使不加 `@JvmOverloads`，Java 也能调用：

```java
new UserBean();
```

但是 Java 不会自动获得只省略部分参数的全部重载。加上：

```kotlin
data class UserBean @JvmOverloads constructor(
    var userName: String? = null,
    var sortLetters: String? = ""
)
```

Java 才能获得这些构造入口：

```java
new UserBean();
new UserBean("jack");
new UserBean("jack", "J");
```

`@JvmOverloads` 从参数列表右侧开始，依次省略带默认值的参数并生成重载。它不会生成可选参数的任意组合，因此应优先把默认参数放在参数列表末尾。

## 10. Java 互操作与安全迁移

### 10.1 Java 互操作注解速查表

> 本节是注解速查表。需要看具体适用场景与生成签名验证时读 10.7；发布公共 API 前的逐项自查见 10.9。

| 注解 | 用途 | 示例 |
| --- | --- | --- |
| `@JvmStatic` | 让 Java 像调用静态方法一样调用 | `DisplayUtils.getWidth()` |
| `@JvmField` | 暴露字段，不生成 getter / setter | `obj.name` |
| `@JvmOverloads` | 从右向左为默认参数生成 Java 重载 | `new UserBean("jack")` |
| `@Throws` | 让 Java 看到 throws 声明，仅影响 Java 调用方 | `@Throws(IOException::class)`（需 `import java.io.IOException`） |

### 10.2 `@JvmField`

Kotlin 属性默认生成 getter / setter：

```kotlin
var name: String? = null
```

Java 调用：

```java
obj.getName();
obj.setName("jack");
```

如果需要保留 Java public 字段访问（原理见 8.1）：

```kotlin
@JvmField
var name: String? = null
```

Java：

```java
obj.name = "jack";
```

`@JvmField` 只适用于能直接暴露为字段的属性；属性不能有自定义 getter/setter，也不能与 `private`、`open` 等不兼容设计混用。它会把 Kotlin 的封装边界暴露给 Java，因此只在确实需要字段级兼容时使用；普通 Kotlin 属性优先保留 getter/setter。

### 10.3 Java 迁移 Kotlin 易错点

| 易错点 | 影响 | 建议 |
| --- | --- | --- |
| Kotlin 类默认 final | Java 子类可能无法继承 | 原 Java 非 final 类按需加 `open` |
| Kotlin 方法默认 final | Java 子类 override 可能失败 | 被子类重写的方法加 `open` |
| Kotlin 默认非空 | Java 传 null 可能触发运行时检查 | Java 未标注非空时谨慎改非空 |
| `public` 不能代替 `open` | 外部能访问不代表子类能重写 | 需要重写必须写 `open` |
| Java `static` 没有直接等价 | Java 调用路径可能变 | 用 `companion object` + `@JvmStatic`（注解用法见 10 章） |
| Java public 字段变 Kotlin 属性 | Java 直接字段访问可能失败 | 需要兼容时用 `@JvmField`（注解用法见 10 章） |
| Kotlin 默认参数不会自动成为 Java 的全部重载 | Java 少参数调用可能失败 | 对 Java API 按需使用 `@JvmOverloads`（注解用法见 10 章、示例见 9.7） |
| bean 改 `data class` | 相等语义变化 | 确认未依赖引用相等 |
| Kotlin `List` 默认只读 | 不能直接 `add/remove` | 需要修改用 `MutableList` |
| `==` 含义变化 | Java `==` 比引用，Kotlin `==` 空安全调用 `equals()` | 需要引用相等用 `===` |
| `!!` 会崩溃 | 空值抛 NPE | 只在需要保留 Java NPE 行为时使用 |
| `internal` 成员被改名 | Java 端无法按原名调用 | `internal` 在字节码中会做名字修饰（name mangling，带模块名后缀），跨 Java/Kotlin 暴露的成员改用 `public` |
| 布尔属性 getter 命名 | Java 端方法名和预期不符 | `val isValid` 生成 `isValid()` 而非 `getValid()`；以 `is` 开头的布尔属性遵循此约定 |
| 作用域函数嵌套过深 | `this/it` 混乱 | 能清晰才用 |

> 关于布尔 getter 的补充：Kotlin 属性 `var enabled: Boolean` 生成 `getEnabled()` / `setEnabled()`，但以 `is` 开头的属性（`val isValid`）生成 `isValid()` / `setValid()`。Java 代码若按 `getIsValid()` 去调用会找不到方法，迁移时要注意 Java 侧的调用点。

### 10.4 推荐迁移原则

1. 先保证逻辑一致，再追求 Kotlin 风格。
2. 在生命周期回调中稍后初始化的字段可以按需使用 `lateinit`，但必须确保第一次读取前已经赋值；Fragment 的 ViewBinding 还要在 `onDestroyView()` 中清空引用。
3. Kotlin 内部使用的无状态工具函数优先考虑顶层函数；需要保持 Java 静态调用入口时，再使用 `object/companion object + @JvmStatic`。
4. Java bean 如果只是数据模型，可以用 `data class`；如果有继承风险，用 `open class`。
5. 原 Java 会 NPE 的地方，如果业务依赖这个行为，Kotlin 用 `!!` 保留；否则优先用 `?.`、`?:`。
6. 集合数据源通常用 `val datas = mutableListOf<T>()`，保持引用稳定，内容可变。
7. 自定义 View / 动画类迁移时，数值类型、重写方法、构造函数签名要特别保守。

### 10.5 平台类型 `T!`

Java 引用类型可能为 `null`，但旧 Java API 往往没有空性注解。Kotlin 遇到这种类型时会在 IDE 或报错信息中显示成平台类型，例如 `String!`。

`T!` 表示编译器不知道它应该是 `T` 还是 `T?`。这个写法只能由编译器展示，不能在 Kotlin 源码中手写。

假设 Java API 是：

```java
public String getName() {
    return null;
}
```

下面的 Kotlin 写法可能把风险延迟到运行时：

```kotlin
val name = javaUser.name
println(name.length) // Java 实际返回 null 时可能抛 NPE
```

在边界处主动选择空性：

```kotlin
val name: String? = javaUser.name
val length = name?.length ?: 0
```

处理平台类型的原则：

1. 不要让平台类型在 Kotlin 业务代码中继续传播。
2. 在 Java/Kotlin 边界添加明确的 `T` 或 `T?` 类型。
3. 能修改 Java 源码时，补充项目认可的 `@Nullable/@NonNull` 注解。
4. 对外部 SDK 的空性声明保持警惕；注解错误仍可能导致运行时异常。

### 10.6 Java 集合同时存在空性和可变性风险

Java 的 `List<T>` 传到 Kotlin 后，编译器还要面对两个未知：

- 这个 List 是否可能为 `null`，元素是否可能为 `null`。
- 调用者是否允许修改它，其他 Java 代码是否还会修改它。

进入 Kotlin 业务层时可以根据契约做一次明确转换：

```kotlin
val rawUsers = javaApi.getUsers()

// 接受 API 可能返回 null，并创建当前时刻的只读浅拷贝
val users: List<User> = rawUsers
    ?.filterNotNull()
    ?.toList()
    .orEmpty()
```

`toList()` 只是列表结构的浅拷贝，不会深拷贝其中的 `User`。如果 Java API 明确要求调用者修改原列表，也不能擅自复制后再假装行为等价。

### 10.7 Kotlin API 给 Java 调用时要检查生成签名

除了前文的 `@JvmStatic`、`@JvmField`、`@JvmOverloads` 和 `@Throws`，还可能遇到：

| 注解 | 什么时候用 |
| --- | --- |
| `@JvmName` | 解决 JVM 签名冲突，或给 Java 一个更合适的方法名 |
| `@file:JvmName` | 修改顶层声明生成的 Java 文件门面类名 |
| `@JvmSuppressWildcards` | Java 泛型签名中的通配符影响框架或调用方时，按需抑制 |

```kotlin
@file:JvmName("TextUtils")

package com.example.text

fun normalize(text: String): String = text.trim()
```

Java 可以调用：

```java
String value = TextUtils.normalize(" hello ");
```

这些注解是兼容工具，不是每个 Kotlin 类都要添加。公共 API 迁移后，应从 Java 写一个最小调用示例，确认构造器、字段、泛型和异常签名符合预期。

### 10.8 Java SAM 与 Kotlin 函数类型

调用只有一个抽象方法的 Java 接口时，Kotlin 通常可以直接传 Lambda：

```kotlin
executor.execute {
    println("running")
}
```

设计仅供 Kotlin 使用的 API 时，通常优先使用函数类型。回调要携带"成功/失败"结果时，可以直接用 `Result`（Kotlin 1.8 完全合法，规则见 6.7）：

```kotlin
fun setOnResult(listener: (Result<User>) -> Unit)
```

> 这是 Kotlin 1.5+ 才允许的写法，旧教程常说"Result 不能当函数参数"，那是 1.5 之前的限制。若回调要跨 Java 调用，注意 `Result` 是 Kotlin 值类，Java 侧使用不便，那种场景改用 sealed 结果类型或 `(User?, Throwable?) -> Unit` 更合适（见 7.5、10.7）。

需要同时给 Java 调用、需要接口名称，或同一个监听器要实现多个语义时，可以使用 `fun interface`。不要把“能写 Lambda”误解成“所有普通 Kotlin 接口都能自动使用 Lambda”。

### 10.9 Java 互操作的常见签名问题

> 本节是收尾自查清单。注解的系统说明见 10.7，这里只汇总**发布前用 Java 实际调用验证**时要看的点，避免重复。

Kotlin 顶层声明会生成 JVM 门面类；需要稳定 Java 名称时使用 `@file:JvmName`（完整规则见 10.7）。重载、泛型和可空性要以生成的 Java 签名为准，不能只看 Kotlin 源码：

```kotlin
@file:JvmName("TextUtils")
package com.example.util

@JvmName("parseUserName")
fun parse(value: String): String = value.trim()
```

公共 API 发布前，实际用 Java 调用一次，重点检查：

1. 是否需要无参或少参数构造器（见 9.7 `@JvmOverloads`）。
2. 属性暴露为字段还是 getter/setter（见 10.2 `@JvmField`）。
3. 泛型是否出现不期望的通配符（见 10.7 `@JvmSuppressWildcards`）。
4. 受检异常是否需要 `@Throws`（见 10.7）。
5. Lambda 参数是否应改成命名的 `fun interface`（见 9.3）。
6. `Unit`、可空类型、函数类型、`@JvmInline value class` 在 Java 侧的表现与 Kotlin 不同，需按生成签名确认。

### 10.10 Java 开发者常见编译错误排错速查

刚从 Java 转过来时，很多报错不是"语法写错"，而是 Kotlin 的类型系统规则不同。按报错原文对号入座，能省下大量搜索时间。

| 报错原文（节选） | 真正的原因 | 最小修复 |
| --- | --- | --- |
| `Type mismatch: inferred type is Int but Long was expected` | Kotlin **不做数字隐式扩宽**，`Int` 不会自动变 `Long` | 显式转换：`val id: Long = 1L` 或 `count.toLong()`（见 2.4） |
| `Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver` | 对可空类型直接用了 `.`，编译器要求先处理 null | 用 `?.` 或 `?.let { }`；确实保证非空才用 `!!`（见 3.1） |
| `Smart cast to 'String' is impossible, because 'x' is a mutable property` | `var` 属性可能在别处被改，编译器不敢智能转换 | 用局部副本：`val s = x; if (s is String) s.length`（见 4.4） |
| `Unresolved reference: it` | `it` 只在**单参数 Lambda** 里存在；这里可能没有 Lambda，或有多个参数 | 显式命名参数：`{ item -> ... }`；集合外自己声明变量（见 2.2） |
| `Val cannot be reassigned` | `val` 声明的引用不能重新赋值 | 需要换对象就改 `var`；只想改内容就用 `MutableList.add()`（见 2.1、2.2） |
| `Unresolved reference: add`（`List` 上） | `listOf()` 返回只读 `List`，没有 `add()` | 用 `mutableListOf()`，或 `list.toMutableList()`（见 2.2） |
| `'when' expression must be exhaustive, add necessary 'else' branch` | 作为**表达式**使用的 `when` 必须覆盖所有情况 | 补 `else ->`，或改用 `sealed`/`enum` 让编译器能穷尽检查（见 4.1、7.5） |
| `Type inference failed: Not enough information to infer parameter T` | 空集合等场景无法推断泛型类型 | 显式写类型：`emptyList<String>()`、`mutableListOf<User>()`（见 1.2） |
| `Suspend function 'xxx' should be called only from a coroutine or another suspend function` | 在普通函数里调用了 `suspend` 函数 | 把调用方也声明为 `suspend`，或在协程作用域里 `launch {}`（见 12.1） |
| `Unresolved reference: layout.xxx`（或 ViewBinding 空指针） | binding 未在 `onCreateView` 后初始化，或用了错误的合成属性 | 用 `lateinit var binding` 并在正确时机 `inflate`（见 8.2、11.10） |

排错顺序建议：**先看报错里的类型名和"nullable / suspend / exhaustive"这些关键词 → 按上表定位到对应章节 → 改完再跑一次编译**。不要用 `!!` 或 `as` 强行"消掉"报错，那只是把编译期错误推迟成运行时崩溃。

## 11. Kotlin 进阶实用能力

### 11.1 包、导入和顶层声明

Kotlin 文件仍然可以声明包和导入：

```kotlin
package com.example.user

import android.view.View
import com.example.model.User
import com.example.model.User as UserModel
```

`as` 可以给导入起别名，适合解决两个包中存在同名类型的问题。

Kotlin 不要求函数和常量必须放进类里：

```kotlin
package com.example.text

const val DEFAULT_SEPARATOR = ","

fun String.isPhoneNumber(): Boolean {
    return length == 11 && all { char ->
        char.isDigit()
    }
}
```

如果这些能力只供 Kotlin 使用，顶层函数通常比创建一个没有状态的 `XxxUtils` 类更直接。顶层声明不等于全局变量，它仍然受包名和可见性修饰符约束。

### 11.2 `Any`、`Any?`、`Unit` 和 `Nothing`

| 类型 | 小白理解 |
| --- | --- |
| `Any` | 所有非空 Kotlin 类型的共同父类型，类似 Java `Object`，但不包含 `null` |
| `Any?` | 可以表示任意值，也可以是 `null` |
| `Unit` | 函数正常执行完但没有有意义的返回数据 |
| `Nothing` | 函数永远不会正常返回，例如一定抛异常 |

```kotlin
fun printValue(value: Any?) {
    println(value)
}

fun fail(message: String): Nothing {
    throw IllegalStateException(message)
}
```

深挖几点容易混淆的：

- **`Nothing` 是所有类型的子类型**：所以 `throw IllegalStateException()`、`TODO()`、`error("...")` 都能作为表达式放在任何需要值的位置，例如 `val name: String = TODO()` 可以编译（运行时抛异常）。
- **`emptyList()` 实际是 `List<Nothing>`**：因为 `Nothing` 是任何类型的子类型，所以 `val names: List<String> = emptyList()` 成立，不必写 `emptyList<String>()`。
- **`Unit` 不是 Java 的 `void`**：它是只有一个实例的对象，可以作为泛型实参，如 `MutableList<Unit>`、`(Int) -> Unit`；函数返回 `Unit` 时可省略声明，但作为类型标注时是真实存在的（Java 侧签名差异见 10.7）。

不要把 `Any` 当成“绕过类型系统”的万能容器。频繁使用 `Any` 加强制转换，通常说明模型还可以继续拆分。

### 11.3 嵌套类和 `inner` 内部类

Java 的非静态内部类默认持有外部类引用；Kotlin 的嵌套类默认不持有外部类引用：

```kotlin
class Screen {
    private val title = "首页"

    class Holder {
        // 这里不能直接访问 title
    }

    inner class ClickHandler {
        fun printTitle() {
            println(title) // inner 类持有 Screen 实例
        }
    }
}
```

只有确实需要访问外部对象时才使用 `inner`。在 Android 中，长生命周期对象持有 Activity、Fragment 或 View 的内部类实例可能造成内存泄漏。

### 11.4 类委托和属性委托：`by`

`by` 不只用于 `lazy`。类委托可以把接口实现转交给另一个对象，减少纯转发代码：

```kotlin
interface Logger {
    fun log(message: String)
}

class ConsoleLogger : Logger {
    override fun log(message: String) {
        println(message)
    }
}

class UserRepository(
    logger: Logger
) : Logger by logger {
    fun loadUser() {
        log("开始加载用户")
    }
}
```

属性委托则把属性的读取、写入逻辑交给委托对象。除了 `lazy`，标准库还提供 `Delegates.observable()`：

```kotlin
import kotlin.properties.Delegates

var name: String by Delegates.observable("未命名") { _, old, new ->
    println("$old -> $new")
}

name = "Jack" // 输出：未命名 -> Jack
```

另外两个实用委托：

```kotlin
import kotlin.properties.Delegates

// vetoable：返回 false 可以拒绝本次赋值
var age: Int by Delegates.vetoable(0) { _, _, new ->
    new >= 0 // 负数直接拒绝，age 保持原值
}

// by map：属性值来自 Map，适合解析配置或 Intent 参数
class Config(map: Map<String, Any?>) {
    val name: String by map       // 取值后需确保类型匹配，否则 ClassCastException
    val timeout: Int by map
}
```

- `vetoable` 的 lambda 返回 `false` 时赋值被丢弃，适合做范围校验。
- `by map` 读取的是 `Map<String, Any?>`，值类型为 `Any?` 需自行保证与实际属性类型一致，常用于把 Map/Bundle 转成强类型属性。
- 委托属性不能再自定义 getter/setter；更复杂的场景可了解 `provideDelegate`。

委托适合消除有明确模式的重复代码，不要为了展示语法把简单属性包装得难以追踪。

**Android 里常见的属性委托**（用到相应库时认识即可，与 11.10 的 KTX 呼应）：

| 写法 | 来自 | 作用 |
| --- | --- | --- |
| `private val vm: MyViewModel by viewModels()` | `activity-ktx` / `fragment-ktx` | 惰性获取 ViewModel，配置变化后自动保留同一实例 |
| `private val args: DetailArgs by navArgs()` | `navigation-fragment-ktx` | 读取 Navigation 传递的参数并解析成类型安全对象 |
| `private val sharedVm: SharedVm by activityViewModels()` | `fragment-ktx` | Fragment 与所在 Activity 共享 ViewModel |
| `var count by remember { mutableStateOf(0) }` | Compose | 记住可组合项内的状态（Compose 场景，语法形态类似但机制不同） |

这些都是标准 `by` 委托思想的具体应用，底层原理和本节一致；初学阶段先记住"看到 `by xxx()` 就是惰性获取/托管"，具体依赖用法用到再查。

### 11.5 `inline` 和 `reified`：先会用，再研究性能

普通泛型参数在 JVM 运行时通常无法直接进行 `is T` 或取得 `T::class`。内联函数的 `reified` 类型参数可以保留调用处的具体类型信息：

```kotlin
inline fun <reified T> Any?.isType(): Boolean {
    return this is T
}

val value: Any = "hello"
println(value.isType<String>()) // true
```

记住三个边界：

1. `reified` 只能用于 `inline` 函数的类型参数（Kotlin 不支持 `inline` 属性）。
2. `inline` 会把函数体复制到调用处，可能减少 Lambda 对象和调用开销，也可能增大生成代码。
3. 不要给所有小函数机械添加 `inline`；先在确实需要 `reified`、非局部返回或性能证据明确时使用。

`noinline`、`crossinline` 属于后续进阶内容，遇到真实需求再学即可。

### 11.6 `typealias` 只创建别名，不创建新类型

复杂函数类型可以用 `typealias` 提升可读性：

```kotlin
typealias UserCallback = (String, Int) -> Unit
typealias ResultCallback = (Result<User>) -> Unit   // Kotlin 1.5+ 合法，规则见 6.7

fun registerCallback(callback: UserCallback) {
    // ...
}
```

`typealias` 只是同一类型的另一个名字，没有运行时包装，也不会获得额外的类型安全。需要真正不同的领域类型时，应考虑普通类、数据类或值类，而不是只起别名。

### 11.7 解构、操作符重载和中缀函数

数据类会生成 `componentN()`，因此可以解构；Map 遍历中的 `(key, value)` 也是同样的机制：

```kotlin
data class Point(val x: Int, val y: Int)

val (x, y) = Point(10, 20)
val (_, second) = listOf("first", "second")
```

下划线表示忽略对应位置。解构只按位置取值，修改数据类主构造参数顺序后，调用方语义可能改变，公共 API 不要滥用。

普通类也能自定义解构，只要手写 `operator fun componentN()`；扩展同样可以提供：

```kotlin
class Point2(val x: Int, val y: Int) {
    operator fun component1() = x
    operator fun component2() = y
}
val (a, b) = Point2(1, 2)  // a=1, b=2，按 component1/2 顺序而非属性名
```

实际业务中，若字段较多或顺序可能调整，用命名属性比解构更稳健。

Kotlin 可以通过 `operator` 为类型定义约定操作：

```kotlin
data class Size(val width: Int, val height: Int) {
    operator fun plus(other: Size) =
        Size(width + other.width, height + other.height)
}

val total = Size(10, 20) + Size(5, 6)
```

操作符函数必须使用 Kotlin 规定的名称和签名。只有当表达式仍然直观时才重载，否则普通命名函数更容易维护。

中缀函数使用 `infix`，调用时可以省略点号和括号：

```kotlin
infix fun Int.timesText(text: String): String = text.repeat(this)

val result = 3 timesText "Hi" // "HiHiHi"
```

中缀函数只能有一个参数，且接收者和参数都必须满足语言限制；它不是让代码“更 Kotlin”的必选项，DSL 之外应优先考虑可读性。

### 11.8 值类：为简单类型增加领域含义

当 `String`、`Int` 等基础类型需要区分业务含义时，可以使用 `@JvmInline value class`：

```kotlin
@JvmInline
value class UserId(val value: String)

fun loadUser(id: UserId) { /* ... */ }
```

`UserId` 与普通 `String` 不是同一类型，能避免把邮箱、用户 ID 等参数传错。JVM 上值类在很多场景会被表示为底层类型，但涉及泛型、可空类型、接口或 Java 调用时可能发生装箱；它也只能有一个主构造属性。对 Java 公共 API 使用前应检查生成签名，不要把其表示方式当作稳定的 Java 字段契约。

### 11.9 Kotlin 2.0 / K2 编译器升级提醒

> 本工程当前使用 Kotlin 1.8.22。升级到 2.0+ 前先了解以下差异，避免全局升级后集中爆编译错误。

Kotlin 2.0 起默认启用新一代 K2 编译器，主要注意点：

| 主题 | 说明 |
| --- | --- |
| 编译器 | K2 成为默认后端；类型推断、重载解析、`when` 穷尽性检查更严格，旧代码可能暴露此前被容忍的问题 |
| 插件版本对齐 | Kotlin 插件（KGP）、Android Gradle Plugin、Compose Compiler 版本必须配套；Compose Compiler 需与 Kotlin 版本一一对应，不能随意混搭 |
| kapt → KSP | kapt 在 K2 下兼容性与性能较差，注解处理库（Room、Hilt、Moshi 等）建议迁移到 KSP |
| 编译速度 | K2 通常显著更快，但首次升级要先在小模块验证，不要一次性全量切换 |
| sealed 子类位置 | 放宽为同一模块即可（详见 7.5），升级后可以减少"必须同包"的限制 |
| 废弃 API | 关注编译警告中的 `deprecated` 项，先在 1.8/1.9 阶段清理完再升级 |

升级步骤建议：先在单模块把 Kotlin 版本提到 1.9.x 清理弃用警告 → 确认所有依赖库支持 K2 → 再升 2.0 并逐模块编译验证。

### 11.10 Android 常用 KTX 与模型类速查

迁移时经常遇到"Kotlin 里怎么写更自然"的小问题，集中记在这里。

**（1）`@Parcelize`：模型类免写 Parcelable 样板代码**

需要 `kotlin-parcelize` 插件（不是旧的 `android-extensions`）：

```kotlin
@Parcelize
data class User(val id: Long, val name: String) : Parcelable
```

- 主构造属性类型必须可 Parcel；`lateinit` 和委托属性不支持。
- 不想参与序列化的属性用 `@IgnoredOnParcel` 标注。
- 可直接放进 `Bundle`：`bundleOf("user" to user)`，或直接作为 `Intent` 的 `putExtra` 值。

**（2）`findViewById` 的泛型与平台类型**

```kotlin
val title: TextView = findViewById(R.id.title)   // 泛型推断，返回平台类型 TextView!
val subtitle = findViewById<TextView>(R.id.subtitle) // 显式泛型
```

- Java 返回的视图是平台类型，可能为 `null`；找不到时通常抛异常而非返回 `null`。
- 新代码优先用 ViewBinding（Kotlin 里通常配 `lateinit var binding`，见 8.2），不必再用 `findViewById`。

**（3）`Bundle` 读写的 KTX 写法**

```kotlin
import android.os.Bundle
import androidx.core.os.bundleOf

val args = bundleOf("id" to 1L, "name" to "jack")
val id = arguments?.getLong("id", 0L) ?: 0L       // 带默认值
val name = arguments?.getString("name")          // 返回 String?，可能为空
```

- `getString()` 返回可空；`getInt(key, default)`、`getLong(key, default)` 带默认值更安全。
- `getStringArray()`、`getParcelableArrayList<T>()` 等泛型 API 在较新 Android 版本上有类型化重载，注意 minSdk 兼容。

### 11.11 反射与 `KClass`（Android 慎用）

Kotlin 有两套 Class 表示，迁移时别混用：

```kotlin
val kClass = User::class        // kotlin.reflect.KClass<User>，Kotlin 反射入口
val javaClass = User::class.java // java.lang.Class<User>，Java 反射 / Android API 常用
val objClass = user.javaClass   // 实例的 java.lang.Class
```

注意点：

- `KClass` 属于 `kotlin-reflect` 库，体积较大，Android 上应谨慎引入；很多场景用 `::class.java` 就够了。
- R8 / ProGuard 混淆后，按名称反射类或成员可能失效，需要保留规则（keep）。
- 需要"根据类型做不同处理"时，优先用 `inline fun <reified T>`（见 11.5）或密封类 + `when`，它们不需要反射且编译期可检查。
- Kotlin 2.0+ 对 `typeOf<T>()` 的支持更完善，作为泛型类型判断的轻量替代值得关注。

把反射当最后手段：它把错误从编译期推迟到运行期，还带来包体积与混淆成本。

## 12. 协程基础与结构化并发

协程来自 `kotlinx.coroutines` 库，不是 Kotlin 语言自动内置的线程功能。

下面的 Android 示例还需要项目引入协程 Android 支持库以及 AndroidX Lifecycle 的 KTX 依赖；只有 Kotlin 标准库时，不能直接使用 `Dispatchers.Main`、`lifecycleScope`、`viewModelScope` 或 `repeatOnLifecycle`。

> **先给 Java/Android 读者一个类比锚点**（不精确但足以入门）：
>
> | 你熟悉的 Java/Android | Kotlin 协程里大致对应 |
> | --- | --- |
> | `new Thread` / `ExecutorService.execute` | `launch`（启动一个不返回结果的任务） |
> | `ExecutorService.submit` 拿 `Future` | `async` 返回 `Deferred`，`await()` 相当于 `Future.get()` |
> | `AsyncTask.doInBackground` + `onPostExecute` | `withContext(Dispatchers.IO) { ... }` 后自动回到原线程继续 |
> | RxJava 的 `subscribeOn` + `observeOn` | `withContext` 切线程，写法更直白 |
> | Executor 的线程池 | `Dispatchers.IO` / `Default` / `Main` 等调度器 |
>
> 协程不是新线程：它更像是"可以暂停、之后接着跑的任务"，暂停时把线程让给别人，不占着线程干等。一个线程上可以跑很多协程，这比开大量线程更省资源。

**三个先记住的白话定义**（后面会逐步展开）：

- **挂起（suspend）**：函数执行到一半暂停，保存现场，把线程让出去；过会儿在有线程时接着往下跑。它不是开线程，也不是 `Thread.sleep` 那种占着线程干等。
- **协程作用域（Scope）**：决定协程活多久、归谁管、跑在哪个线程。用 `lifecycleScope`/`viewModelScope` 时，页面或 ViewModel 销毁会自动取消里面所有协程，不用你手动记着清理。
- **结构化并发**：子协程挂在父作用域上，父取消时子任务连带取消。这正是替代"散养 `GlobalScope`"的核心机制，避免页面关了任务还在后台跑。

### 12.1 `suspend` 不等于后台线程

```kotlin
suspend fun loadUser(): User {
    return repository.loadUser()
}
```

`suspend` 表示函数可以挂起并稍后恢复，不代表它自动运行在 IO 线程。线程由协程上下文和 `Dispatcher` 决定。

一个最直观的对比，帮你理解"挂起不占线程"：

```kotlin
// 错误习惯：Thread.sleep 会占着主线程睡 1 秒，界面卡住
Thread.sleep(1000)

// 正确做法：delay 只是挂起协程，线程被释放去干别的，1 秒后接着跑
delay(1000)
```

在协程里要延时一律用 `delay`，不要用 `Thread.sleep`；后者会把当前线程真的阻塞住。

常用 Dispatcher：

| Dispatcher | 用途 |
| --- | --- |
| `Dispatchers.Main` | 更新 UI、执行轻量主线程任务 |
| `Dispatchers.IO` | 文件、数据库、阻塞式网络 IO |
| `Dispatchers.Default` | CPU 密集计算，例如排序、解析、图像计算 |

```kotlin
suspend fun readText(file: File): String =
    withContext(Dispatchers.IO) {
        file.readText()
    }
```

### 12.2 使用生命周期作用域

Activity/Fragment 中优先使用生命周期感知作用域：

```kotlin
lifecycleScope.launch {
    val user = withContext(Dispatchers.IO) {
        repository.loadUser()
    }

    // withContext 返回后恢复到 Main，可更新 UI
    binding.nameText.text = user.name
}
```

Fragment 收集 `Flow` 时，通常结合 View 生命周期：

```kotlin
viewLifecycleOwner.lifecycleScope.launch {
    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiState.collect { state ->
            render(state)
        }
    }
}
```

### 12.3 协程易错点

1. 不要把 `suspend` 当成“自动开线程”。
2. 不要在主线程执行阻塞 IO，即使代码位于协程中。
3. 优先使用 `lifecycleScope`、`viewModelScope`、`coroutineScope` 等结构化作用域，避免随意使用 `GlobalScope`。
4. 协程取消是协作式的；长时间 CPU 循环要检查取消状态或调用可挂起函数。
5. 需要时间上限时用 `withTimeout` / `withTimeoutOrNull`（用法见 12.4），不要自己造超时逻辑。
6. `launch` 适合不需要返回值的任务；`async` 返回 `Deferred`，通过 `await()` 取得结果并重新抛出任务异常。但在普通结构化作用域中，`async` 子协程失败仍会立即取消父协程，不能把“不调用 await”当作忽略异常。
7. 不要用 `runBlocking` 阻塞 Android 主线程。
8. 默认的结构化作用域中，子协程失败通常会取消父协程和其他兄弟协程；确实需要“一个子任务失败不影响其他子任务”时，明确使用 `supervisorScope` 或 `SupervisorJob`，不要靠吞掉异常来实现。
9. 多个协程同时读写同一个变量（如 `counter++`）并不安全，这不是原子操作；需要共享可变状态时用 `Mutex` 或 `StateFlow`（详见 12.6）。
10. 在 `Dispatchers.IO` 里调用传统阻塞 Java API（如文件读写）时，取消不会中断底层阻塞，线程会被占住；用 `runInterruptible` 包装可以让取消生效（详见 12.6）。

### 12.4 协程结构化并发模板

并行执行多个独立任务时，可以使用 `coroutineScope`：

```kotlin
suspend fun loadPage(): Page = coroutineScope {
    val user = async { repository.loadUser() }
    val messages = async { repository.loadMessages() }
    Page(user.await(), messages.await())
}
```

这里任一子任务失败，整个作用域失败并取消其他子任务。如果任务彼此独立、允许部分失败，使用 `supervisorScope` 并在子任务内部明确处理异常。

```kotlin
// 显式写 : Unit：lambda 末行是 launch（返回 Job），若用表达式体会把 Job 泄漏给调用方
suspend fun refreshWidgets(): Unit = supervisorScope {
    launch {
        try {
            refreshWeather()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            reportFailure(e)
        }
    }
    launch {
        try {
            refreshNews()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            reportFailure(e)
        }
    }
}
```

长时间 CPU 任务应响应取消：

```kotlin
suspend fun calculate(): Long {
    var total = 0L
    repeat(1_000_000) { index ->
        currentCoroutineContext().ensureActive()
        total += index
    }
    return total
}
```

需要时间上限时使用 `withTimeout` 或 `withTimeoutOrNull`，并区分超时、取消和业务失败：

```kotlin
val user = withTimeoutOrNull(3_000) {
    repository.loadUser()
}
```

一次发起多个**返回类型不同**的并发请求时，先各自 `async` 拿到 `Deferred`，再分别 `await()`。这样既并发执行，又保留各自的类型：

```kotlin
suspend fun loadDashboard(): Dashboard = coroutineScope {
    val bannersDeferred = async { api.loadBanners() }   // 立即开始并发执行
    val messagesDeferred = async { api.loadMessages() }
    val profileDeferred = async { api.loadProfile() }

    Dashboard(
        bannersDeferred.await(),   // 三个 await 都在等已经启动的任务
        messagesDeferred.await(),
        profileDeferred.await()
    )
}
```

> 常见坑：不要写成 `listOf(async {...}, async {...}).awaitAll()` 再解构。三个请求返回类型不同（如 `List<Banner>`、`List<Message>`、`Profile`）时，`listOf` 只能推导出公共父类型 `List<Deferred<Any>>`，`awaitAll()` 结果是 `List<Any>`，解构出的变量都是 `Any`，无法传给需要具体类型的 `Dashboard`。`awaitAll()` 只适合**元素类型相同**的并发任务，例如 `listOf(id1, id2, id3).map { async { api.loadUser(it) } }.awaitAll()` 得到 `List<User>`。

需要"部分失败不影响其他任务"时，把外层 `coroutineScope` 换成 `supervisorScope`，并在各子任务内部处理异常（见本节的模板）。

**`suspend` 函数类型与 `Job` 基本操作**（Android 里常把挂起 lambda 当参数注入）：

`suspend` 关键字也能修饰函数类型，表示"这个函数体里可以调用其他挂起函数"：

```kotlin
// 普通函数类型
val onClick: (View) -> Unit = { view -> /* ... */ }

// 挂起函数类型：多一个 suspend，函数体内可直接调 delay、loadUser 等
val onRefresh: suspend () -> Unit = {
    val user = repository.loadUser()
    show(user)
}

// 带参数和返回值的挂起函数类型
typealias SuspendLoader = suspend (Long) -> User
```

`launch` 和 `async` 都会返回句柄（`Job` / `Deferred`），可用来查询状态或取消：

| 操作 | 作用 |
| --- | --- |
| `job.cancel()` | 请求取消协程（协作式，需协程内部配合，见 12.3 第 4 条） |
| `job.join()` | 挂起等待协程执行结束，不取返回值（取返回值用 `Deferred.await()`） |
| `job.isActive` | 是否仍在运行 |
| `job.isCancelled` / `job.isCompleted` | 是否已取消 / 是否已结束 |

日常用 `viewModelScope` / `lifecycleScope` 时通常不用手动管理 `Job`（作用域销毁会统一取消），只有自己创建协程、需要提前取消时才直接操作它。

### 12.5 协程异常处理：`launch` 与 `async` 不一样

Java 里 `ExecutorService.submit()` 抛出的异常会存进 `Future`，`execute()` 的异常则直接冒到线程的未捕获异常处理器。Kotlin 协程的 `launch` 和 `async` 有类似但更容易踩坑的区别，必须分清楚。

**第一条：`async` 的异常存在 `Deferred` 里，等 `await()` 时才抛。**

```kotlin
val deferred = async { throw IllegalStateException("boom") }
// 到这里还没有任何异常抛出
val result = deferred.await()   // 异常在这一行抛出
```

如果只 `async` 不 `await()`，异常不会凭空消失：在普通 `coroutineScope` 里它仍会取消父作用域（见 12.3 第 6 条）。所以别把"不调用 await"当成吞掉异常的办法。

**第二条：`launch` 的异常无法用外层 `try/catch` 捕获。**

```kotlin
// 错误：try/catch 包不住 launch 里异步抛出的异常
try {
    lifecycleScope.launch {
        throw IllegalStateException("boom")
    }
} catch (e: Exception) {
    // 这里捕获不到
}

// 正确：try/catch 放在协程内部，包住真正会抛异常的挂起调用
lifecycleScope.launch {
    try {
        val user = repository.loadUser()
        render(user)
    } catch (e: CancellationException) {
        throw e                    // 取消不是失败，必须重抛（见 12.4、6.7）
    } catch (e: Exception) {
        showError(e)
    }
}
```

原因是 `launch` 会立即返回一个 `Job`，代码块是稍后异步执行的；外层的 `try/catch` 在代码块执行前就已经退出。这和 Java 里"给 `execute()` 套 try/catch 捕获不到子线程异常"是同一回事。

**第三条：兜底用 `CoroutineExceptionHandler`，但它有明确边界。**

当异常没在协程内部处理、又确实需要处理时，可在**作用域或顶层协程**上挂一个处理器：

```kotlin
val handler = CoroutineExceptionHandler { context, throwable ->
    Log.e("Coroutine", "未处理的协程异常", throwable)
}

// 挂到顶层协程
lifecycleScope.launch(handler) {
    repository.doSomethingThatMayThrow()
}
```

| 要点 | 说明 |
| --- | --- |
| 只对 `launch` 生效 | `async` 的异常走 `await()`，`CoroutineExceptionHandler` 不处理它 |
| 只处理"漏出来"的异常 | 协程内部已 `try/catch` 的异常不会再交给它 |
| 子协程的处理器不会生效 | 异常向上委托给父协程，处理器要装在父级/顶层才有机会触发 |
| 不能替代取消处理 | 收到 `CancellationException` 仍应让取消正常传播，不要在这里"消化"掉 |
| Android 上更适合记录日志 | 更新 UI、弹 Toast 优先放在协程内部的 `catch` 里，位置更明确 |

**异常处理优先级建议**（从最推荐到最兜底）：

1. 能在协程内部用 `try/catch` 精确处理的，就地处理——位置最清楚，也最容易测试。
2. 需要把失败变成 UI 状态时，在协程内部 `catch` 后写入 `StateFlow`（见 13.2），并记得重抛 `CancellationException`。
3. 确实无法就地处理、只想知道"哪里漏了异常"时，用 `CoroutineExceptionHandler` 记录日志兜底。
4. 不要把异常处理器当作常规业务流程控制手段。

> 与结构化并发的关系：默认情况下一个子协程失败会取消父作用域和兄弟协程（见 12.3 第 8 条、12.4）。`CoroutineExceptionHandler` 只负责"观察"异常，不会阻止这种取消；要改变传播行为，用的是 `supervisorScope` / `SupervisorJob`，不是异常处理器。

### 12.6 共享状态与阻塞调用：两个进阶坑

这一节对应 Java 里"多线程改同一个变量"和"阻塞 IO 不可中断"两类老问题，只是换到协程语境下。初学阶段可以先知道有这回事，真正写并发逻辑时再回来细看。

**坑一：多个协程同时改一个变量，不是线程安全的。**

Java 里你也知道 `counter++` 不是原子操作（读-改-写三步），多线程同时做会丢数据。协程即使跑在同一个线程上，只要中途有挂起点， interleaving 同样会发生：

```kotlin
var counter = 0

suspend fun addMany() = coroutineScope {
    repeat(1_000) {
        launch(Dispatchers.Default) {
            counter++          // 错误：并发写，结果通常小于 1000
        }
    }
}
```

三种常见解法，按场景选：

| 方案 | 适合场景 | 写法 |
| --- | --- | --- |
| `AtomicInteger` / `AtomicLong` | 只是简单计数 | `atomic.incrementAndGet()`，Java 里那套直接搬过来 |
| `Mutex` | 需要保护一段多步逻辑 | `mutex.withLock { /* 临界区 */ }` |
| `StateFlow` | UI 状态计数 | `flow.update { it + 1 }`，内部线程安全 |

`Mutex` 是协程版的锁，但用法和 Java 的 `synchronized` 有本质区别——它是**挂起**而不是阻塞线程：

```kotlin
val mutex = Mutex()
var counter = 0

suspend fun safeIncrement() {
    mutex.withLock {
        counter++          // 同一时刻只有一个协程能进来
    }
}
```

> 注意：协程里不要用 `synchronized`。它会阻塞线程，而协程的核心优势就是"挂起不占线程"。用 `Mutex` 才能和结构化并发配合。

**坑二：阻塞式 API 不可取消，会用满 IO 线程池。**

协程取消是协作式的。如果你的协程在 `Dispatchers.IO` 里调用一个"死等"的阻塞 API（比如某些老的文件/网络读取），取消协程时底层线程并不会停下来，会一直占着直到调用返回。并发一多，IO 线程池就被占满。

Kotlin 提供 `runInterruptible` 把阻塞调用包起来，取消时会对底层线程发出 `interrupt()`，让它尽快抛异常退出：

```kotlin
suspend fun readLargeFile(file: File): String =
    runInterruptible(Dispatchers.IO) {
        file.readText()    // 阻塞调用：取消时会中断底层线程
    }
```

判断标准：`withContext(Dispatchers.IO)` 适用于**支持中断或本身很快**的阻塞调用；遇到长时间、纯阻塞、且不响应中断的老 API，再考虑 `runInterruptible`。不要见阻塞就无脑包一层。

> 这两个坑都属于"写并发工具代码"时才需要的能力。日常 Android 业务里，把状态放进 `ViewModel` 的 `StateFlow`、协程绑在 `viewModelScope` 上，通常就已经避开了大部分问题（见 12.2、13.2）。

## 13. `Flow`、`StateFlow` 与 Android UI 状态

> **给 RxJava / LiveData 读者的对照锚点**：
>
> | 你熟悉的 | Flow 里大致对应 |
> | --- | --- |
> | RxJava `Observable` / `Flowable` | `Flow<T>`（同样是一串异步产生的数据） |
> | `subscribe()` | `collect { }`（**关键区别**：不 collect，冷流就完全不执行） |
> | `onNext()` | `emit()` |
> | `map` / `filter` / `flatMap` | 同名或 `flatMapLatest` / `flatMapConcat` 等变体 |
> | `BehaviorSubject`（有初值、只重放最新值） | `StateFlow` |
> | `PublishSubject` | `SharedFlow` |
> | `LiveData` | `StateFlow` 在 UI 状态上定位接近，但 StateFlow 不依赖 Android 生命周期 |
>
> 一句话记住 `collect`：它是"真正开始订阅并收数据"的动作。冷流（`flow {}`）里那些代码，只有执行到 `collect` 时才会跑；调用 `flow.map {}` 只是描述处理步骤，不会触发执行。

### 13.1 三种类型怎么选

| 类型 | 特点 | 常见用途 |
| --- | --- | --- |
| `Flow<T>` | 普通 `flow {}` 通常是冷流，每个收集者单独执行上游 | 数据库查询、数据转换、连续异步结果 |
| `StateFlow<T>` | 热流，始终有一个当前值，新收集者先收到当前状态 | 页面 UI 状态 |
| `SharedFlow<T>` | 热流，可向多个收集者广播，可配置重放 | 多订阅者消息；一次性事件要谨慎设计 |

“冷流”可以粗略理解为：没有人 `collect` 时通常不执行，每次 `collect` 都重新开始。“热流”即使暂时没有收集者，也有独立于收集者的生命周期。

> **Java/RxJava 迁移者常踩的坑**：冷流被多个页面或多个地方分别 `collect`，上游会执行多次——比如同一次网络请求被触发两遍。如果一段上游只想执行一次并把结果共享给多个订阅方，用 `stateIn` / `shareIn` 把它转成热流（写法见 13.2）；UI 层收集时再配合 `repeatOnLifecycle`，避免页面重复进入时重复收集。Flow 操作符速查见 13.7。

### 13.2 ViewModel 对外暴露只读状态

```kotlin
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)

    // 外部只能收集，不能直接修改状态
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.value = try {
                val user = repository.loadUser()
                UiState.Success(user.name)
            } catch (e: CancellationException) {
                throw e // 取消不是普通业务失败，必须继续向上传播
            } catch (e: Exception) {
                UiState.Error(e)
            }
        }
    }
}
```

View 层使用前文的 `repeatOnLifecycle` 收集。不要在 Fragment 中直接启动一个永久收集任务后忘记 View 已经销毁。

`runCatching {}` 会捕获 `CancellationException`。在协程中如果直接把所有异常转换成失败状态，可能破坏协作式取消；使用它时也必须把取消异常重新抛出（完整规则见 6.7）。

当上游是冷流、需要把结果共享给多个收集者时，用 `stateIn` 转成 `StateFlow`：

```kotlin
val uiState: StateFlow<UiState> = repository.userFlow
    .map { user -> UiState.Success(user.name) }
    .catch { error -> emit(UiState.Error(error)) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )
```

`SharingStarted.WhileSubscribed(5_000)` 表示最后一个订阅者断开 5 秒后才停止上游：旋转屏幕造成的短暂断连不会重启请求，页面真正离开后又能释放资源。

### 13.3 让数据层函数对主线程安全

调用者不应该猜测 Repository 是否会阻塞主线程。数据层应在内部把阻塞工作切到合适的 Dispatcher：

```kotlin
class UserRepository(
    private val api: UserApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadUser(): User =
        withContext(ioDispatcher) {
            api.loadUserBlocking()
        }
}
```

把 Dispatcher 作为依赖传入，也更方便测试。网络库本身已经提供真正的挂起 API 时，不必机械地再包一层 `Dispatchers.IO`，应先确认所调用 API 是否阻塞线程。

**给 RxJava 读者的 `flowOn` 对照**：它大致相当于 `subscribeOn`——只影响**上游**，下游仍由 `collect` 所在的协程上下文决定线程：

```kotlin
val result = repository.observeUsers()   // 上游：数据库查询
    .map { it.map(User::name) }          // 这个 map 也在 IO 线程
    .flowOn(Dispatchers.IO)              // 把上面所有上游操作切到 IO
    .catch { emit(emptyList()) }
    // 下游 collect 在哪个线程，取决于调用它的协程（UI 通常是 Main）
```

关键区别：`flowOn` 的**位置决定作用域**——写在哪个操作符后面，就从那里往前生效；`withContext` 则是把整段代码切到别的线程并把结果带回来，适合"取一次值"的场景（如上面的 `loadUser()`）。冷流每次 `collect` 都会重新执行上游，多次收集会重复请求（见 13.1）。

### 13.4 一次性事件不要盲目塞进 `StateFlow`

Toast、导航等事件与“页面当前状态”不同。`StateFlow` 会向新收集者重放当前值，旋转屏幕后可能重复消费旧事件。

可选方案取决于业务语义：

- 能建模成状态时，优先建模成状态，并在消费后更新状态。
- 允许收集者暂时离线时丢失的广播，可按需使用 `SharedFlow`。
- 必须保证逐个处理的事件，可考虑 `Channel`，同时明确发送、缓冲和取消策略。

没有一种事件容器适合所有场景。先回答“旋转屏幕后是否应该重放”“没有观察者时是否允许丢失”，再选工具。

### 13.5 LiveData 与 StateFlow 的取舍

迁移存量代码时经常遇到 LiveData，两种方案的关键差异：

| 对比 | LiveData | StateFlow |
| --- | --- | --- |
| 初始值 | 可以没有值 | 必须提供 `initialValue` |
| 生命周期 | 自带生命周期感知 | 需 `repeatOnLifecycle` 或 `collectAsStateWithLifecycle` 配合 |
| 线程写入 | `value` 主线程、`postValue` 后台线程 | `value` / `emit` 均线程安全 |
| 平台依赖 | Android 架构组件 | 纯 Kotlin，跨平台 |
| 互转 | `flow.asLiveData()`、`liveData.asFlow()` | — |

选择建议：新代码且已用协程，优先 `StateFlow`；存量 LiveData 代码不必强行迁移，可通过 `asFlow()`/`asLiveData()` 逐步过渡；混用时注意 LiveData 的 `postValue` 会合并中间值，而 Flow 默认不会。

### 13.6 Compose 场景下收集 Flow

如果 UI 层使用 Jetpack Compose（而非 ViewBinding），收集方式不同：

```kotlin
// 依赖 androidx.lifecycle:lifecycle-runtime-compose
@Composable
fun UserScreen(viewModel: UserViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    // 用 state 渲染 UI
}
```

`collectAsStateWithLifecycle()` 内部同样在 Lifecycle STARTED 时开始收集，效果等价于 View 体系里的 `repeatOnLifecycle`，并且写法更简洁。传统 ViewBinding 页面仍用 12.2 的 `repeatOnLifecycle` 方案。

### 13.7 `Flow` 的常用组合与生命周期

常用 Flow 操作（冷流重复收集的坑与 `repeatOnLifecycle` 见 13.1；`stateIn` 把冷流转 `StateFlow` 的完整写法见 13.2，这里不重复代码）：

| 操作 | 用途 |
| --- | --- |
| `map` | 转换每个值 |
| `filter` | 过滤值 |
| `onEach` | 插入副作用 |
| `catch` | 处理上游异常；不会自动捕获下游异常 |
| `combine` | 任一上游变化时组合多个最新值 |
| `zip` | 按顺序一一配对，任一上游结束时通常结束 |
| `debounce` | 一段时间内没有新值后才发出，适合搜索输入 |
| `collectLatest` | 新值到来时取消上一个处理任务 |
| `flatMapLatest` | 新输入到来时切换到最新的子 Flow |
| `stateIn` | 在作用域中共享为 `StateFlow` |
| `shareIn` | 在作用域中共享为 `SharedFlow` |

`stateIn` 和 `shareIn` 会把冷流变成共享的热流，应明确 Scope、启动策略和重放数量；UI 收集与 `repeatOnLifecycle` 的处理见 13.1。

### 13.8 Android 生命周期与线程边界

常见分工可以记成：

| 层级 | 推荐职责 |
| --- | --- |
| View/Fragment | 收集状态、渲染 UI、转发用户操作 |
| ViewModel | 保存页面状态、启动业务协程、处理配置变化 |
| Repository | 组合数据源、定义数据契约、处理 IO 边界 |
| Data source | 网络、数据库、文件等具体访问 |

ViewModel 不应持有 Activity、Fragment、View 或 Context 的长生命周期引用。需要 Context 时优先使用 `Application` 级别依赖，并通过接口或构造参数注入，便于测试。

事件流和状态流要分开设计：页面当前显示什么通常是 `StateFlow`，导航、Toast、一次性结果需要根据"断开观察时是否允许丢失"和"重新订阅是否应该重放"选择 `SharedFlow` 或 `Channel`（一次性事件的选择标准另见 13.4）。

## 14. 测试、代码风格与练习闭环

### 14.1 先测试纯 Kotlin 逻辑

把计算和判断从 Activity、Fragment、View 中抽成普通函数或类，最容易测试：

```kotlin
class PriceCalculator {
    fun total(price: Int, count: Int): Int {
        require(price >= 0)
        require(count >= 0)
        return price * count
    }
}
```

JUnit 测试：

```kotlin
import org.junit.Assert.assertEquals
import org.junit.Test

class PriceCalculatorTest {
    private val calculator = PriceCalculator()

    @Test
    fun total_twoItems_returnsDoublePrice() {
        val result = calculator.total(price = 10, count = 2)

        assertEquals(20, result)
    }
}
```

至少覆盖：

1. 正常输入。
2. 空值、空集合、边界数字。
3. 异常或非法参数。
4. Java 迁移前后容易改变的相等、排序和默认值行为。

协程代码使用 `kotlinx-coroutines-test`，用 `runTest` 替代 `runBlocking`，它会自动跳过 `delay` 并支持虚拟时间：

```kotlin
@Test
fun `load user returns expected name`() = runTest {
    val result = repository.loadUser(1L)
    assertEquals("jack", result.name)
}
```

测试 Flow 可用第三方库 Turbine，逐条断言发射值：

```kotlin
viewModel.uiState.test {
    assertEquals(UiState.Loading, awaitItem())
    assertEquals(UiState.Success("jack"), awaitItem())
    awaitComplete()
}
```

- 需要 `testImplementation("app.cash.turbine:turbine:版本")`。
- 测试使用 `viewModelScope` 的 ViewModel 时，需要在构造时注入测试调度器（如 `UnconfinedTestDispatcher`），否则 `runTest` 无法控制它的协程。
- 如果代码里用到了 `Dispatchers.Main`（默认主线程调度器），单元测试还需要替换它，否则会报 "Module with the Main dispatcher had failed to initialize"：

```kotlin
@Before
fun setUp() {
    Dispatchers.setMain(testDispatcher)   // testDispatcher 来自 runTest / UnconfinedTestDispatcher
}

@After
fun tearDown() {
    Dispatchers.resetMain()               // 记得恢复，避免影响其他测试
}
```

- 不要在单元测试里依赖真实延时、真实主线程或真实网络。

> 存量代码提醒：`launchWhenStarted` / `launchWhenResumed` 已废弃，它们只是"暂停"协程，页面在后台时上游（如流动的网络请求）仍在运行。UI 收集 Flow 应改用 `repeatOnLifecycle`（见 12.2、13.1）。

### 14.2 初学阶段的代码审查清单

提交 Kotlin 代码前逐项检查：

- [ ] 能用 `val` 的地方是否避免了 `var`？
- [ ] `!!` 是否真的有明确的非空依据？
- [ ] Java 平台类型是否在边界处明确成 `T` 或 `T?`？
- [ ] 集合需要的是只读接口还是可变接口？
- [ ] `data class` 的相等语义和浅拷贝是否符合业务？
- [ ] `HashMap/HashSet` 的 Key 是否稳定？
- [ ] 作用域函数是否让 `this/it` 更清楚，而不是更混乱？
- [ ] 协程是否属于正确的 Scope，阻塞工作是否离开主线程？
- [ ] Fragment ViewBinding 是否在 `onDestroyView()` 清空？
- [ ] 关键逻辑是否有测试或可重复验证步骤？

### 14.3 推荐的四个练习项目

按顺序完成，不建议一开始就堆很多框架：

1. **命令行通讯录**：练习空安全、集合、数据类、排序、查找和测试。
2. **Java 工具类迁移**：选一个真实 Java 类迁移，保留 Java 调用示例，练习平台类型和 JVM 注解。
3. **RecyclerView 列表页**：使用 ViewBinding、ViewModel、密封 UI 状态和 `StateFlow`。
4. **带缓存的数据页面**：Repository 组合网络与本地数据，练习协程取消、错误处理、Fake 依赖和单元测试。

每个练习都保存一份“第一次能运行的版本”和“重构后的版本”，记录为什么改、行为有没有变化。这个对成长的帮助通常大于继续背新的语法。

### 14.4 暂时不用急着学

掌握前面内容之前，可以暂缓：

- DSL、操作符重载、复杂内联控制流。
- 协程底层状态机、Continuation 实现细节。
- 编译器插件、KSP 处理器开发。
- 为了炫技而使用的复杂泛型和多层作用域函数。

遇到项目真实需求再深入，学习效率会更高。

### 14.5 官方学习资源

建议按下面顺序使用，不必同时打开十几套教程：

1. [Kotlin Tour](https://kotlinlang.org/docs/kotlin-tour-welcome.html)：在浏览器中完成基础和中级语法练习。
2. [Kotlin Koans](https://kotlinlang.org/docs/koans.html)：特别适合 Java 开发者，通过修复失败测试熟悉 Kotlin 惯用法。
3. [Calling Java from Kotlin](https://kotlinlang.org/docs/java-interop.html) 和 [Calling Kotlin from Java](https://kotlinlang.org/docs/java-to-kotlin-interop.html)：混编遇到空性、泛型或 JVM 签名问题时查阅。
4. [Android Kotlin 学习入口](https://developer.android.com/kotlin/campaign/learn)：语言基础稳定后再进入 Android 专项课程。
5. [Android 协程最佳实践](https://developer.android.com/kotlin/coroutines/coroutines-best-practices)：开始写 ViewModel、Repository 和异步数据流时重点阅读。

官方文档负责提供准确规则，本速查文档负责帮你建立顺序和避坑意识；两者配合使用，不要只背二手结论。

