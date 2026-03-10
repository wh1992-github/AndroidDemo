import collections
import os

file_path = os.path.join(os.path.dirname(__file__), 'app', 'src', 'main', 'assets', '长白山.txt')

lines = []
with open(file_path, 'r', encoding='utf-8') as f:
    for line in f:
        line = line.strip()
        if line:
            lines.append(line)

month_map = collections.OrderedDict()
for line in lines:
    ym = line[:7]
    if ym not in month_map:
        month_map[ym] = []
    month_map[ym].append(line)

keys = list(month_map.keys())
total_profit = 0
profit_count = 0
loss_count = 0

print('====== 长白山.txt 月度策略：上月最低买入 -> 下月最高卖出 ======')
for i in range(len(keys) - 1):
    buy_month = keys[i]
    sell_month = keys[i + 1]
    buy_lines = month_map[buy_month]
    sell_lines = month_map[sell_month]

    buy_price = float('inf')
    buy_day = ''
    for l in buy_lines:
        price = float(l[l.index('------') + 6:])
        if price < buy_price:
            buy_price = price
            buy_day = l[:10]

    sell_price = 0
    sell_day = ''
    for l in sell_lines:
        price = float(l[l.index('------') + 6:])
        if price > sell_price:
            sell_price = price
            sell_day = l[:10]

    profit = (sell_price - buy_price) / buy_price * 100
    total_profit += profit
    if profit > 0:
        profit_count += 1
    else:
        loss_count += 1

    mark = ' ******' if profit < 0 else ''
    print('买入 %s 价格 %.2f -> 卖出 %s 价格 %.2f | 收益 %.2f%%%s' % (buy_day, buy_price, sell_day, sell_price, profit, mark))

total = profit_count + loss_count
print('====== 总结 ======')
print('总交易次数 = %d, 盈利次数 = %d, 亏损次数 = %d, 胜率 = %.2f%%, 累计收益 = %.2f%%' % (total, profit_count, loss_count, profit_count * 100.0 / total, total_profit))
