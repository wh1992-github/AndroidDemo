# Kotlin 核心知识点速查文档

本文档面向 Java 代码迁移 Kotlin 的学习和查阅。顺序按“先能看懂，再能写对，再能迁移安全”组织。

## 阅读方法：Java 开发者的 Kotlin 成长路线

不要从第一页开始死记所有语法。建议按下面顺序学习，每个阶段都要写一小段能运行的代码。

| 阶段 | 先学什么 | 对应章节 | 学会的判断标准 |
| --- | --- | --- | --- |
| 1. 看懂语法 | `val/var`、类型、字符串、空安全、`if/when/for`、函数 | 1～5 | 能独立读懂一个简单 Kotlin 类，不再逐行翻译成 Java |
| 2. 写出 Kotlin 风格 | 集合操作、Lambda、扩展函数、属性、作用域函数 | 2、4、7、11 | 能用清晰的 `map/filter/let`，但不会为了简短而乱嵌套 |
| 3. 建立对象模型 | 构造、继承、接口、`object`、`data/sealed class`、泛型 | 6、8、9 | 能根据业务语义选择普通类、数据类、接口和密封类型 |
| 4. 安全迁移 Java | 平台类型、Java 集合、异常、JVM 注解 | 10、12、16、19 | Java/Kotlin 混编时能主动检查 null、可变性和调用签名 |
| 5. Android 异步开发 | 协程、生命周期、`Flow/StateFlow` | 15、20 | 不阻塞主线程，能正确启动、取消和收集异步任务 |
| 6. 工程化成长 | 测试、代码风格、重构和练习项目 | 21 | 每次迁移都有测试或可验证结果，而不是“编译通过就算完成” |

建议的学习循环：

1. 先写出与 Java 逻辑等价的 Kotlin。
2. 为关键分支补一个测试或可重复验证的输入。
3. 再使用空安全、集合函数、数据类等 Kotlin 能力重构。
4. 比较重构前后的行为，确认没有偷偷改变 null、相等、异常或线程语义。
5. 最后才追求简洁；看不懂的“一行流”应拆回有名字的局部变量。

> 学习目标不是“代码越短越像 Kotlin”，而是利用类型系统让错误更早暴露，同时保持代码容易阅读和验证。

## 1. Kotlin 和 Java 最核心区别

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

## 2. 变量、类型、字符串

### 2.1 `val` 和 `var`

```kotlin
val name = "jack" // 只读，不能重新赋值
var age = 18      // 可变，可以重新赋值

age = 20
// name = "tom" // 编译报错
```

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

#### 2.2 的核心规则与常用模板

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

Kotlin 集合接口分为两组：

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

因此不要把 `List` 简单理解成线程安全、不可变集合。

同理，`Set`、`Map` 的只读接口也不等于深度不可变，更不自动具备线程安全能力。

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
println(readonly) // A 只保留一份

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

#### 2.2.7 筛选、转换和排序

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

组合两个列表：

```kotlin
val names = listOf("Jack", "Lucy")
val ages = listOf(18, 20)

// 按相同下标两两配对
// Jack 和 18 配对，Lucy 和 20 配对
val pairs = names.zip(ages) // [(Jack, 18), (Lucy, 20)]
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

需要按分隔符解析时，`split()` 会返回列表；需要安全转数字时优先使用 `toIntOrNull()`、`toLongOrNull()` 等函数。不要把 `String` 当作可变字符数组，频繁拼接大量片段时可以考虑 `buildString { append(...) }`。

## 3. 空安全

Kotlin 默认非空：

```kotlin
var name: String = "jack"
// name = null // 编译报错
```

允许为空必须加 `?`：

```kotlin
var name: String? = null
```

### 3.1 空安全符号总表

| 符号 | 名称 | 示例 | 含义 |
| --- | --- | --- | --- |
| `?` | 可空类型 | `String?` | 允许变量为 `null` |
| `?.` | 安全调用 | `name?.length` | 不为空才调用，为空返回 `null` |
| `!!` | 非空断言 | `name!!.length` | 强制非空，为空直接崩 |
| `?:` | Elvis 运算符 | `name?.length ?: 0` | 左边为空时使用右边默认值 |
| `?.let {}` | 非空执行 | `name?.let { ... }` | 不为空才执行代码块 |

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

所以表达式类型是：

```kotlin
Unit?
```

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

## 4. 函数、Lambda、单表达式函数

### 4.1 普通函数

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

### 4.2 单表达式函数

```kotlin
fun add(a: Int, b: Int): Int = a + b
```

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

### 4.3 默认参数、命名参数和 `Unit`

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

调用 Kotlin 函数时可以使用命名参数；Kotlin 调用 Java 方法时不能使用命名参数，因为 Java 字节码不保证始终保留可靠的参数名。

没有有意义返回值的 Kotlin 函数返回 `Unit`：

```kotlin
fun logMessage(text: String): Unit {
    println(text)
}
```

`Unit` 类似 Java 的 `void`，但 `Unit` 是真实类型，并且只有一个值 `Unit`。返回类型为 `Unit` 时通常省略不写。

### 4.4 Lambda 和点击事件

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

### 4.5 函数类型和高阶函数

函数可以像普通值一样保存到变量、作为参数传递或作为结果返回。

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

### 4.6 扩展函数和扩展属性

扩展函数可以在不继承、不修改原类源码的情况下，为某个类型提供便捷调用形式：

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

扩展函数的重要限制：

1. 扩展不会真的修改原类，也不能访问原类的 `private/protected` 成员。
2. 扩展函数是静态分派，调用哪个扩展取决于变量的编译期类型，不是对象的运行时类型。
3. 如果成员函数和扩展函数签名相同，成员函数优先。
4. 扩展属性不能保存状态，因此不能有初始化值和独立 backing field。

## 5. 控制流和区间

### 5.1 `when`

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

### 5.2 区间

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

### 5.3 `if` 也是表达式

Kotlin 没有 Java 的三元运算符 `条件 ? A : B`，直接使用 `if` 表达式：

```kotlin
val max = if (a > b) {
    a
} else {
    b
}
```

每个分支最后一条表达式作为结果，因此作为表达式使用时必须有 `else`。

### 5.4 类型判断和智能转换

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

### 5.5 `while`、`break`、`continue` 和标签返回

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

## 6. 类、继承、构造

### 6.1 `open class`

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

### 6.2 `open fun`

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

### 6.3 构造函数

```kotlin
class User(val name: String, var age: Int)
```

等价于 Java 中构造赋值加 getter / setter 的常见 bean 写法。

需要初始化逻辑时：

```kotlin
class User(val name: String) {
    init {
        println("name=$name")
    }
}
```

次构造：

```kotlin
class User {
    var name: String? = null

    constructor(name: String) {
        this.name = name
    }
}
```

### 6.4 可见性修饰符

| 修饰符 | 类成员 | 顶层声明 |
| --- | --- | --- |
| `public` | 任何可见该类的地方都能访问 | 任何地方都能访问 |
| `private` | 仅当前类内部 | 仅当前 `.kt` 文件 |
| `protected` | 当前类及其子类 | 顶层声明不能使用 |
| `internal` | 当前 Gradle/Kotlin 编译模块 | 当前模块 |

Kotlin 没有 Java 的 package-private。还要注意：Java 的 `protected` 允许同包访问，而 Kotlin 的 `protected` 不提供同包访问。

### 6.5 `enum class` 和 `sealed class`

枚举适合表示固定的一组无额外类型层级的常量：

```kotlin
enum class LoadState {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}
```

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

这里 `when` 已覆盖所有直接实现，因此不需要 `else`。密封类/接口的直接子类必须与密封类型位于同一个 Kotlin 编译模块和包中，并且必须有名称，不能是局部类或匿名对象；其他模块或包不能随意增加直接子类。

### 6.6 泛型基础、`out`、`in` 和星投影

泛型用类型参数表示“这里可以替换成某种具体类型”：

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
- `List<*>`：元素具体类型未知，只能安全地把读取结果当作 `Any?`。
- `MutableList<T>` 默认不协变，不能把 `MutableList<Dog>` 当成 `MutableList<Animal>`，否则可能错误地塞入其他动物。

## 7. 属性、`lateinit`、`lazy`

### 7.1 属性和 Java getter / setter

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

### 7.2 `lateinit`

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

检查是否初始化：

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

### 7.3 `by lazy`

`lazy` 表示第一次访问时才初始化，并且只初始化一次。

```kotlin
val title by lazy {
    "hello"
}
```

默认 `lazy` 使用同步锁，保证多线程下只初始化一次。确定只在 Android 主线程访问时，可以按需选择：

```kotlin
val title by lazy(LazyThreadSafetyMode.NONE) {
    "hello"
}
```

`NONE` 不提供线程安全保证，不要仅为了“更快”而随意使用。

适合能在表达式里完成初始化的 `val`。

| 写法 | 适合场景 |
| --- | --- |
| `lateinit var` | 生命周期中稍后赋值，例如 `onCreate()` 里初始化 ViewBinding |
| `val by lazy` | 第一次使用时再计算，之后不变 |

## 8. `object`、匿名对象、`companion object`

### 8.1 `object` 单例

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

### 8.2 匿名对象：`object :`

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

### 8.3 接口：`interface` 和 `fun interface`

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

### 8.4 `companion object`

`companion object` 是类里的伴生对象，常用来放 Java 中的 `static` 常量或方法。

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

## 9. 数据类和 Java Bean 迁移

### 9.1 `data class`

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

### 9.2 `data class` 和 `open class` 怎么选

| 写法 | 优点 | 风险 |
| --- | --- | --- |
| `open class` + `var` | 最接近 Java 普通 bean；保留可继承语义 | 不会自动生成基于属性的 `copy/toString/equals` |
| `data class` | 更 Kotlin；适合纯数据模型 | 默认 final；自动根据主构造属性生成相等与哈希语义 |

如果旧代码没有依赖 bean 被继承、引用相等或旧的 `HashMap/HashSet` Key 行为，`data class` 通常更适合纯数据模型。

### 9.3 `@JvmOverloads constructor`

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

## 10. Java 互操作常用注解

| 注解 | 用途 | 示例 |
| --- | --- | --- |
| `@JvmStatic` | 让 Java 像调用静态方法一样调用 | `DisplayUtils.getWidth()` |
| `@JvmField` | 暴露字段，不生成 getter / setter | `obj.name` |
| `@JvmOverloads` | 从右向左为默认参数生成 Java 重载 | `new UserBean("jack")` |
| `@Throws` | 让 Java 看到 throws 声明 | `@Throws(IOException::class)` |

### 10.1 `@JvmField`

Kotlin 属性默认生成 getter / setter：

```kotlin
var name: String? = null
```

Java 调用：

```java
obj.getName();
obj.setName("jack");
```

如果需要保留 Java public 字段访问：

```kotlin
@JvmField
var name: String? = null
```

Java：

```java
obj.name = "jack";
```

`@JvmField` 只适用于能直接暴露为字段的属性；属性不能有自定义 getter/setter，也不能与 `private`、`open` 等不兼容设计混用。它会把 Kotlin 的封装边界暴露给 Java，因此只在确实需要字段级兼容时使用；普通 Kotlin 属性优先保留 getter/setter。

## 11. 作用域函数

`apply`、`let`、`also`、`run`、`with` 不是关键字，是 Kotlin 标准库函数，通常叫作用域函数。

| 函数 | 内部对象名 | 返回值 | 常用场景 |
| --- | --- | --- | --- |
| `apply` | `this` | 原对象 | 初始化 / 配置对象 |
| `also` | `it` | 原对象 | 顺便做日志、调试、副作用 |
| `let` | `it` | Lambda 最后一行 | 判空后处理、转换结果 |
| `run` | `this` | Lambda 最后一行 | 在对象作用域内计算结果 |
| `with` | `this` | Lambda 最后一行 | 对已有对象集中操作 |

### 11.1 `apply`

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

### 11.2 `also`

```kotlin
val users = getUsers()
    .also { Log.i("TAG", "size=${it.size}") }
    .filter { it.name != null }
```

`also` 返回原对象，适合在链式调用中插入日志。

### 11.3 `let`

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

### 11.4 `run`

```kotlin
val isValid = editText.run {
    text.isNotEmpty() && visibility == View.VISIBLE
}
```

`run` 内部用 `this`，返回最后一行。

### 11.5 `with`

```kotlin
with(textView) {
    text = "hello"
    textSize = 16f
}
```

适合对已有对象集中设置。

### 11.6 怎么选

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

## 12. 异常处理

Kotlin 中所有异常都是非受检异常：编译器不会强制要求 `try/catch`，也不要求函数声明 `throws`。

### 12.1 `try` 也是表达式

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

### 12.2 主动校验和抛错

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

### 12.3 `use` 自动关闭资源

实现 `Closeable` 的资源优先使用 `use`，类似 Java try-with-resources：

```kotlin
FileInputStream(file).use { input ->
    val bytes = input.readBytes()
    println(bytes.size)
} // 离开代码块后自动 close
```

不要用异常代替正常分支判断，也不要捕获 `Throwable` 后完全忽略。

## 13. 常用特殊符号

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

## 14. `Math.java` 和 `MathJVM.kt`

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

## 15. 协程基础（Android 常用）

协程来自 `kotlinx.coroutines` 库，不是 Kotlin 语言自动内置的线程功能。

下面的 Android 示例还需要项目引入协程 Android 支持库以及 AndroidX Lifecycle 的 KTX 依赖；只有 Kotlin 标准库时，不能直接使用 `Dispatchers.Main`、`lifecycleScope`、`viewModelScope` 或 `repeatOnLifecycle`。

### 15.1 `suspend` 不等于后台线程

```kotlin
suspend fun loadUser(): User {
    return repository.loadUser()
}
```

`suspend` 表示函数可以挂起并稍后恢复，不代表它自动运行在 IO 线程。线程由协程上下文和 `Dispatcher` 决定。

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

### 15.2 使用生命周期作用域

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

### 15.3 协程易错点

1. 不要把 `suspend` 当成“自动开线程”。
2. 不要在主线程执行阻塞 IO，即使代码位于协程中。
3. 优先使用 `lifecycleScope`、`viewModelScope`、`coroutineScope` 等结构化作用域，避免随意使用 `GlobalScope`。
4. 协程取消是协作式的；长时间 CPU 循环要检查取消状态或调用可挂起函数。
5. `launch` 适合不需要返回值的任务；`async` 返回 `Deferred`，通过 `await()` 取得结果并重新抛出任务异常。但在普通结构化作用域中，`async` 子协程失败仍会立即取消父协程，不能把“不调用 await”当作忽略异常。
6. 不要用 `runBlocking` 阻塞 Android 主线程。
7. 默认的结构化作用域中，子协程失败通常会取消父协程和其他兄弟协程；确实需要“一个子任务失败不影响其他子任务”时，明确使用 `supervisorScope` 或 `SupervisorJob`，不要靠吞掉异常来实现。

## 16. Java 迁移 Kotlin 易错点

| 易错点 | 影响 | 建议 |
| --- | --- | --- |
| Kotlin 类默认 final | Java 子类可能无法继承 | 原 Java 非 final 类按需加 `open` |
| Kotlin 方法默认 final | Java 子类 override 可能失败 | 被子类重写的方法加 `open` |
| Kotlin 默认非空 | Java 传 null 可能触发运行时检查 | Java 未标注非空时谨慎改非空 |
| `public` 不能代替 `open` | 外部能访问不代表子类能重写 | 需要重写必须写 `open` |
| Java `static` 没有直接等价 | Java 调用路径可能变 | 用 `companion object` + `@JvmStatic` |
| Java public 字段变 Kotlin 属性 | Java 直接字段访问可能失败 | 需要兼容时用 `@JvmField` |
| Kotlin 默认参数不会自动成为 Java 的全部重载 | Java 少参数调用可能失败 | 对 Java API 按需使用 `@JvmOverloads` |
| bean 改 `data class` | 相等语义变化 | 确认未依赖引用相等 |
| Kotlin `List` 默认只读 | 不能直接 `add/remove` | 需要修改用 `MutableList` |
| `==` 含义变化 | Java `==` 比引用，Kotlin `==` 空安全调用 `equals()` | 需要引用相等用 `===` |
| `!!` 会崩溃 | 空值抛 NPE | 只在需要保留 Java NPE 行为时使用 |
| 作用域函数嵌套过深 | `this/it` 混乱 | 能清晰才用 |

## 17. 推荐迁移原则

1. 先保证逻辑一致，再追求 Kotlin 风格。
2. 在生命周期回调中稍后初始化的字段可以按需使用 `lateinit`，但必须确保第一次读取前已经赋值；Fragment 的 ViewBinding 还要在 `onDestroyView()` 中清空引用。
3. Kotlin 内部使用的无状态工具函数优先考虑顶层函数；需要保持 Java 静态调用入口时，再使用 `object/companion object + @JvmStatic`。
4. Java bean 如果只是数据模型，可以用 `data class`；如果有继承风险，用 `open class`。
5. 原 Java 会 NPE 的地方，如果业务依赖这个行为，Kotlin 用 `!!` 保留；否则优先用 `?.`、`?:`。
6. 集合数据源通常用 `val datas = mutableListOf<T>()`，保持引用稳定，内容可变。
7. 自定义 View / 动画类迁移时，数值类型、重写方法、构造函数签名要特别保守。

## 18. 容易漏学但很实用的 Kotlin 能力

### 18.1 包、导入和顶层声明

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

### 18.2 `Any`、`Any?`、`Unit` 和 `Nothing`

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

不要把 `Any` 当成“绕过类型系统”的万能容器。频繁使用 `Any` 加强制转换，通常说明模型还可以继续拆分。

### 18.3 嵌套类和 `inner` 内部类

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

### 18.4 类委托和属性委托：`by`

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

委托适合消除有明确模式的重复代码，不要为了展示语法把简单属性包装得难以追踪。

### 18.5 `inline` 和 `reified`：先会用，再研究性能

普通泛型参数在 JVM 运行时通常无法直接进行 `is T` 或取得 `T::class`。内联函数的 `reified` 类型参数可以保留调用处的具体类型信息：

```kotlin
inline fun <reified T> Any?.isType(): Boolean {
    return this is T
}

val value: Any = "hello"
println(value.isType<String>()) // true
```

记住三个边界：

1. `reified` 只能用于 `inline` 函数或属性的类型参数。
2. `inline` 会把函数体复制到调用处，可能减少 Lambda 对象和调用开销，也可能增大生成代码。
3. 不要给所有小函数机械添加 `inline`；先在确实需要 `reified`、非局部返回或性能证据明确时使用。

`noinline`、`crossinline` 属于后续进阶内容，遇到真实需求再学即可。

### 18.6 `typealias` 只创建别名，不创建新类型

复杂函数类型可以用 `typealias` 提升可读性：

```kotlin
typealias UserCallback = (Result<User>) -> Unit

fun loadUser(callback: UserCallback) {
    // ...
}
```

`typealias` 只是同一类型的另一个名字，没有运行时包装，也不会获得额外的类型安全。需要真正不同的领域类型时，应考虑普通类、数据类或值类，而不是只起别名。

### 18.7 解构、操作符重载和中缀函数

数据类会生成 `componentN()`，因此可以解构；Map 遍历中的 `(key, value)` 也是同样的机制：

```kotlin
data class Point(val x: Int, val y: Int)

val (x, y) = Point(10, 20)
val (_, second) = listOf("first", "second")
```

下划线表示忽略对应位置。解构只按位置取值，修改数据类主构造参数顺序后，调用方语义可能改变，公共 API 不要滥用。

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

### 18.8 值类：为简单类型增加领域含义

当 `String`、`Int` 等基础类型需要区分业务含义时，可以使用 `@JvmInline value class`：

```kotlin
@JvmInline
value class UserId(val value: String)

fun loadUser(id: UserId) { /* ... */ }
```

`UserId` 与普通 `String` 不是同一类型，能避免把邮箱、用户 ID 等参数传错。JVM 上值类在很多场景会被表示为底层类型，但涉及泛型、可空类型、接口或 Java 调用时可能发生装箱；它也只能有一个主构造属性。对 Java 公共 API 使用前应检查生成签名，不要把其表示方式当作稳定的 Java 字段契约。

## 19. Java 与 Kotlin 混编必须补上的知识

### 19.1 平台类型 `T!`

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

### 19.2 Java 集合同时存在空性和可变性风险

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

### 19.3 Kotlin API 给 Java 调用时要检查生成签名

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

### 19.4 Java SAM 与 Kotlin 函数类型

调用只有一个抽象方法的 Java 接口时，Kotlin 通常可以直接传 Lambda：

```kotlin
executor.execute {
    println("running")
}
```

设计仅供 Kotlin 使用的 API 时，通常优先使用函数类型：

```kotlin
fun setOnResult(listener: (Result<User>) -> Unit)
```

需要同时给 Java 调用、需要接口名称，或同一个监听器要实现多个语义时，可以使用 `fun interface`。不要把“能写 Lambda”误解成“所有普通 Kotlin 接口都能自动使用 Lambda”。

## 20. `Flow`、`StateFlow` 和 Android UI 状态

### 20.1 三种类型怎么选

| 类型 | 特点 | 常见用途 |
| --- | --- | --- |
| `Flow<T>` | 普通 `flow {}` 通常是冷流，每个收集者单独执行上游 | 数据库查询、数据转换、连续异步结果 |
| `StateFlow<T>` | 热流，始终有一个当前值，新收集者先收到当前状态 | 页面 UI 状态 |
| `SharedFlow<T>` | 热流，可向多个收集者广播，可配置重放 | 多订阅者消息；一次性事件要谨慎设计 |

“冷流”可以粗略理解为：没有人 `collect` 时通常不执行，每次 `collect` 都重新开始。“热流”即使暂时没有收集者，也有独立于收集者的生命周期。

### 20.2 ViewModel 对外暴露只读状态

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

`runCatching {}` 会捕获 `CancellationException`。在协程中如果直接把所有异常转换成失败状态，可能破坏协作式取消；使用它时也必须把取消异常重新抛出。

### 20.3 让数据层函数对主线程安全

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

### 20.4 一次性事件不要盲目塞进 `StateFlow`

Toast、导航等事件与“页面当前状态”不同。`StateFlow` 会向新收集者重放当前值，旋转屏幕后可能重复消费旧事件。

可选方案取决于业务语义：

- 能建模成状态时，优先建模成状态，并在消费后更新状态。
- 允许收集者暂时离线时丢失的广播，可按需使用 `SharedFlow`。
- 必须保证逐个处理的事件，可考虑 `Channel`，同时明确发送、缓冲和取消策略。

没有一种事件容器适合所有场景。先回答“旋转屏幕后是否应该重放”“没有观察者时是否允许丢失”，再选工具。

## 21. 测试、代码风格和练习闭环

### 21.1 先测试纯 Kotlin 逻辑

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

协程代码使用 `kotlinx-coroutines-test` 提供的测试调度能力，不要在单元测试里依赖真实延时或真实主线程。

### 21.2 初学阶段的代码审查清单

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

### 21.3 推荐的四个练习项目

按顺序完成，不建议一开始就堆很多框架：

1. **命令行通讯录**：练习空安全、集合、数据类、排序、查找和测试。
2. **Java 工具类迁移**：选一个真实 Java 类迁移，保留 Java 调用示例，练习平台类型和 JVM 注解。
3. **RecyclerView 列表页**：使用 ViewBinding、ViewModel、密封 UI 状态和 `StateFlow`。
4. **带缓存的数据页面**：Repository 组合网络与本地数据，练习协程取消、错误处理、Fake 依赖和单元测试。

每个练习都保存一份“第一次能运行的版本”和“重构后的版本”，记录为什么改、行为有没有变化。这个对成长的帮助通常大于继续背新的语法。

### 21.4 暂时不用急着学

掌握前面内容之前，可以暂缓：

- DSL、操作符重载、复杂内联控制流。
- 协程底层状态机、Continuation 实现细节。
- 编译器插件、KSP 处理器开发。
- 为了炫技而使用的复杂泛型和多层作用域函数。

遇到项目真实需求再深入，学习效率会更高。

### 21.5 官方学习资源

建议按下面顺序使用，不必同时打开十几套教程：

1. [Kotlin Tour](https://kotlinlang.org/docs/kotlin-tour-welcome.html)：在浏览器中完成基础和中级语法练习。
2. [Kotlin Koans](https://kotlinlang.org/docs/koans.html)：特别适合 Java 开发者，通过修复失败测试熟悉 Kotlin 惯用法。
3. [Calling Java from Kotlin](https://kotlinlang.org/docs/java-interop.html) 和 [Calling Kotlin from Java](https://kotlinlang.org/docs/java-to-kotlin-interop.html)：混编遇到空性、泛型或 JVM 签名问题时查阅。
4. [Android Kotlin 学习入口](https://developer.android.com/kotlin/campaign/learn)：语言基础稳定后再进入 Android 专项课程。
5. [Android 协程最佳实践](https://developer.android.com/kotlin/coroutines/coroutines-best-practices)：开始写 ViewModel、Repository 和异步数据流时重点阅读。

官方文档负责提供准确规则，本速查文档负责帮你建立顺序和避坑意识；两者配合使用，不要只背二手结论。

## 22. 全局高频知识点补全

前面的章节覆盖了日常迁移主线，下面集中补充容易漏掉、但在真实 Kotlin/Android 代码中经常遇到的能力。遇到具体问题时，可先按关键词查这一章。

### 22.1 类型推断与显式类型

Kotlin 会根据初始化表达式推断类型，但公共 API、空集合、复杂泛型和容易误读的代码应显式写出类型：

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

### 22.2 `vararg`、默认参数和函数引用

`vararg` 允许调用方传入零个或多个参数；函数内部拿到的是数组：

```kotlin
fun join(separator: String = ", ", vararg values: String): String =
    values.joinToString(separator)

join(values = *arrayOf("A", "B"))
join(" / ", "A", "B", "C")
```

一个函数最多只能有一个 `vararg` 参数。调用已有函数时，可以用 `::函数名` 取得函数引用：

```kotlin
val printer: (String) -> Unit = ::println
listOf("A", "B").forEach(printer)

val lengths = listOf("A", "Hello").map(String::length)
```

成员函数引用可能需要对象实例：`user::toDisplayName`；类型函数引用如 `String::length` 会把接收者作为函数参数。

### 22.3 `Pair`、`Triple` 与领域模型

`Pair` 和 `Triple` 适合临时组合少量值：

```kotlin
val pair: Pair<String, Int> = "Jack" to 18
val (name, age) = pair
```

如果值会跨函数、跨模块或长期保存，优先使用有字段名称的 `data class`。`Pair.first`、`Pair.second` 比 `User.name`、`User.age` 更容易产生位置误读。

### 22.4 接口属性、抽象类与实现约束

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

初始化顺序通常是：父类初始化 → 子类主构造参数求值 → 子类属性初始化器和 `init` 代码块（严格按源码出现顺序）→ 次构造函数体。不要在父类构造阶段调用可被子类重写的成员，子类属性可能尚未初始化。

### 22.5 作用域函数之外的标准库工具

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

常用 `Result` API 还有 `isSuccess`、`isFailure`、`getOrNull()`、`exceptionOrNull()`、`fold(onSuccess, onFailure)`。它适合表达一个操作的成功/失败结果，不应替代所有领域错误类型。

### 22.6 `Flow` 的常用组合与生命周期

常用 Flow 操作：

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

`stateIn` 和 `shareIn` 会把冷流变成共享的热流，应明确 Scope、启动策略和重放数量。UI 收集仍应放在 `repeatOnLifecycle` 中，避免 View 不可见时继续更新已经销毁的视图。

### 22.7 协程结构化并发模板

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
suspend fun refreshWidgets() = supervisorScope {
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

### 22.8 Android 生命周期与线程边界

常见分工可以记成：

| 层级 | 推荐职责 |
| --- | --- |
| View/Fragment | 收集状态、渲染 UI、转发用户操作 |
| ViewModel | 保存页面状态、启动业务协程、处理配置变化 |
| Repository | 组合数据源、定义数据契约、处理 IO 边界 |
| Data source | 网络、数据库、文件等具体访问 |

ViewModel 不应持有 Activity、Fragment、View 或 Context 的长生命周期引用。需要 Context 时优先使用 `Application` 级别依赖，并通过接口或构造参数注入，便于测试。

事件流和状态流要分开设计：页面当前显示什么通常是 `StateFlow`，导航、Toast、一次性结果需要根据“断开观察时是否允许丢失”和“重新订阅是否应该重放”选择 `SharedFlow` 或 `Channel`。

### 22.9 Java 互操作的常见签名问题

Kotlin 顶层声明会生成 JVM 门面类；需要稳定 Java 名称时使用 `@file:JvmName`。重载、泛型和可空性要以生成的 Java 签名为准，不能只看 Kotlin 源码：

```kotlin
@JvmName("parseUserName")
fun parse(value: String): String = value.trim()
```

Kotlin 的 `Unit`、可空类型、默认参数、函数类型和值类在 Java 中都有不同表现。公共 API 发布前，应实际用 Java 调用一次，重点检查：

1. Java 是否需要无参或少参数构造器。
2. 属性是否应暴露为字段还是 getter/setter。
3. 泛型是否出现不期望的通配符。
4. 异常是否需要 `@Throws`。
5. Lambda 参数是否应改成命名的 `fun interface`。
